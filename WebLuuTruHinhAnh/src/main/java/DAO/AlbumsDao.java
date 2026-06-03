package DAO;

import model.Album;

import java.util.Date;
import java.util.List;

public class AlbumsDao extends BaseDao {

    public List<Album> getAllAlbums(int uid) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                    SELECT 
                                        a.id,
                                        a.user_id,
                                        a.album_name,
                                        a.created_at,
                                        COUNT(img.id) AS item_count,
                                        (
                                            SELECT i.file_path 
                                            FROM images i
                                            JOIN album_images ai2 ON i.id = ai2.image_id
                                            WHERE ai2.album_id = a.id
                                              AND i.is_deleted = 0
                                            LIMIT 1
                                        ) AS cover_url
                                    FROM albums a
                                    LEFT JOIN album_images ai ON a.id = ai.album_id
                                    LEFT JOIN images img 
                                           ON img.id = ai.image_id 
                                          AND img.is_deleted = 0
                                    WHERE a.user_id = :id
                                    GROUP BY a.id, a.user_id, a.album_name, a.created_at
                                    ORDER BY a.id DESC
                                """)
                        .bind("id", uid)
                        .map((rs, ctx) -> {
                            Album a = new Album();

                            a.setId(rs.getInt("id"));
                            a.setUserId(rs.getInt("user_id"));
                            a.setAlbumName(rs.getString("album_name"));

                            // FIX LocalDate
                            Date sqlDate = rs.getDate("created_at");
                            if (sqlDate != null) {
                                a.setCreatedAt(((java.sql.Date) sqlDate).toLocalDate());
                            }

                            // UI fields
                            a.setItemCount(rs.getInt("item_count"));
                            a.setCoverUrl(rs.getString("cover_url"));

                            return a;
                        })
                        .list()
        );
    }

    public boolean isAlbumNameExist(int uid, String albumName) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                    SELECT COUNT(*) 
                                    FROM albums 
                                    WHERE user_id = :uid AND album_name = :name
                                """)
                        .bind("uid", uid)
                        .bind("name", albumName)
                        .mapTo(int.class)
                        .one()
        ) > 0;
    }

    public boolean createAlbum(int uid, String albumName) {
        // [Bước 8. Exceptions] System: Bọc trong transaction để nếu có lỗi CSDL -> Hệ thống rollback
        Integer rows = getJdbi().inTransaction(handle ->
                // [Bước 7.1.7 & 7.1.8] System: Tạo album record trong database
                handle.createUpdate("""
                                    INSERT INTO albums (user_id, album_name, created_at)
                                    VALUES (:uid, :name, NOW())
                                """)
                        .bind("uid", uid)
                        .bind("name", albumName)
                        .execute()
        );

        return rows != null && rows > 0;
    }

    public boolean deleteAlbum(int uid, int albumId) {
        // [8. Exceptions] System: Bọc trong transaction để nếu có lỗi CSDL -> Hệ thống rollback transaction
        return getJdbi().inTransaction(handle -> {
            
            // [Bước 2.1.2 & 2.1.6] System: Kiểm tra quyền sở hữu album (SR-19, SR-24) và album còn tồn tại
            int count = handle.createQuery("SELECT COUNT(*) FROM albums WHERE id = :albumId AND user_id = :uid")
                    .bind("albumId", albumId)
                    .bind("uid", uid)
                    .mapTo(int.class)
                    .one();
                    
            if (count == 0) {
                // [Bước 2.3.1 / 2.4.1] System: Không tìm thấy album_id hoặc user không phải owner
                return false;
            }

            // [Bước 2.1.8] System: Xóa toàn bộ quan hệ album–image trong bảng trung gian (SR-16)
            // (Code thực hiện xóa quan hệ trước khi xóa album để tránh lỗi khóa ngoại FK)
            handle.createUpdate("""
                DELETE FROM album_images
                WHERE album_id = :albumId
                """)
                    .bind("albumId", albumId)
                    .execute();

            // [Bước 2.1.7] System: Thực hiện xóa bản ghi album trong database
            int rows = handle.createUpdate("""
                DELETE FROM albums
                WHERE id = :albumId
                """)
                    .bind("albumId", albumId)
                    .execute();

            // [Bước 2.1.9] System: Đảm bảo không xóa ảnh gốc (SR-17, SR-18)
            // (Hoàn toàn không có thao tác DELETE FROM images nào ở đây)

            return rows > 0;
        });
    }

    public Album getAlbum(int aid) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                    SELECT 
                                        a.id,
                                        a.user_id,
                                        a.album_name,
                                        a.created_at,
                                        COUNT(ai.image_id) AS item_count,
                                        (SELECT i.file_path 
                                         FROM images i
                                         JOIN album_images ai2 ON i.id = ai2.image_id
                                         WHERE ai2.album_id = a.id
                                         LIMIT 1) AS cover_url
                                    FROM albums a
                                    LEFT JOIN album_images ai ON a.id = ai.album_id
                                    WHERE a.id = :id
                                    GROUP BY a.id, a.user_id, a.album_name, a.created_at
                                    ORDER BY a.id DESC
                                """)
                        .bind("id", aid)
                        .map((rs, ctx) -> {
                            Album a = new Album();

                            a.setId(rs.getInt("id"));
                            a.setUserId(rs.getInt("user_id"));
                            a.setAlbumName(rs.getString("album_name"));

                            // FIX LocalDate
                            Date sqlDate = rs.getDate("created_at");
                            if (sqlDate != null) {
                                a.setCreatedAt(((java.sql.Date) sqlDate).toLocalDate());
                            }

                            // UI fields
                            a.setItemCount(rs.getInt("item_count"));
                            a.setCoverUrl(rs.getString("cover_url"));

                            return a;
                        })
                        .one()
        );
    }

    public boolean addPhotosToAlbum(int uid, int albumId, List<Integer> photoIds) {
        if (photoIds == null || photoIds.isEmpty()) return false;

        return getJdbi().withHandle(handle -> {
            // [Luồng 10.4]: Bọc an toàn dữ liệu trong một Transaction (All-or-Nothing)
            return handle.inTransaction(txn -> {

                // [BR-01 & Luồng 10.1.6]: Kiểm tra quyền sở hữu album của User hiện tại
                Integer albumOwner = txn.createQuery("SELECT user_id FROM albums WHERE id = :albumId")
                        .bind("albumId", albumId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null);

                if (albumOwner == null || albumOwner != uid) {
                    return false; // Chặn hành vi can thiệp trái phép vào album người khác
                }

                // [BR-02]: Lọc và chỉ giữ lại những ảnh thực sự thuộc quyền sở hữu của User này
                // Tránh trường hợp user truyền bừa một photoId của người khác qua JSON để chèn vào album của mình
                List<Integer> validPhotoIds = txn.createQuery("SELECT id FROM images WHERE id IN (<ids>) AND user_id = :uid AND is_deleted = 0")
                        .bindList("ids", photoIds)
                        .bind("uid", uid)
                        .mapTo(Integer.class)
                        .list();

                if (validPhotoIds.isEmpty()) {
                    return false; // Không có ảnh nào hợp lệ hoặc thuộc quyền sở hữu của user
                }

                // [BR-03 & Luồng 10.1.7]: Lấy danh sách các ảnh ĐÃ TỒN TẠI SẴN trong album để loại trừ trùng lặp
                List<Integer> existingPhotoIds = txn.createQuery("SELECT image_id FROM album_images WHERE album_id = :albumId")
                        .bind("albumId", albumId)
                        .mapTo(Integer.class)
                        .list();

                // Thực hiện vòng lặp chèn dữ liệu
                String insertSql = "INSERT INTO album_images (album_id, image_id) VALUES (:albumId, :imageId)";
                int recordsInserted = 0;

                for (Integer imageId : validPhotoIds) {
                    // [Luồng 10.2.3]: Nếu ảnh đã có trong album rồi thì bỏ qua (Không tạo liên kết trùng)
                    if (existingPhotoIds.contains(imageId)) {
                        continue;
                    }

                    // Thực thi chèn bản ghi mới [Luồng 10.1.8]
                    recordsInserted += txn.createUpdate(insertSql)
                            .bind("albumId", albumId)
                            .bind("imageId", imageId)
                            .execute();
                }

                // Trả về true nếu có ít nhất một bức ảnh mới được liên kết thành công vào album
                return recordsInserted > 0;
            });
        });
    }
}
