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
    public Album getAlbumByOwner(int aid, int uid) {
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
                                WHERE a.id = :id AND a.user_id = :uid
                                GROUP BY a.id, a.user_id, a.album_name, a.created_at
                            """)
                        .bind("id", aid)
                        .bind("uid", uid)
                        .map((rs, ctx) -> {
                            Album a = new Album();

                            a.setId(rs.getInt("id"));
                            a.setUserId(rs.getInt("user_id"));
                            a.setAlbumName(rs.getString("album_name"));

                            // FIX LocalDate
                            java.util.Date sqlDate = rs.getDate("created_at");
                            if (sqlDate != null) {
                                a.setCreatedAt(((java.sql.Date) sqlDate).toLocalDate());
                            }

                            // UI fields
                            a.setItemCount(rs.getInt("item_count"));
                            a.setCoverUrl(rs.getString("cover_url"));

                            return a;
                        })
                        .findOne() // Sử dụng findOne() thay vì one() để tránh ném Exception khi không tìm thấy bản ghi (trả về Optional)
                        .orElse(null) // Nếu không tìm thấy hoặc user_id không khớp, lập tức trả về null
        );
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

    public String addPhotosToAlbum(int uid, int albumId, List<Integer> ids) {
        // [Bước 10.3.1] System: Phát hiện user không chọn ảnh
        if (ids == null || ids.isEmpty()) {
            // [Bước 10.3.2] System: Trả về lỗi
            return "Vui lòng chọn ít nhất một ảnh.";
        }

        return getJdbi().inTransaction(handle -> {
            // [Bước 10.1.6] System: Kiểm tra quyền chỉnh sửa album (BR-01)
            int albumOwnerCount = handle.createQuery("SELECT COUNT(*) FROM albums WHERE id = :albumId AND user_id = :uid")
                    .bind("albumId", albumId)
                    .bind("uid", uid)
                    .mapTo(int.class)
                    .one();
            if (albumOwnerCount == 0) {
                return "Bạn không có quyền chỉnh sửa album này.";
            }

            // System: Kiểm tra quyền sở hữu các ảnh (BR-02)
            List<Integer> validImageIds = handle.createQuery("SELECT id FROM images WHERE id IN (<ids>) AND user_id = :uid AND is_deleted = 0")
                    .bindList("ids", ids)
                    .bind("uid", uid)
                    .mapTo(int.class)
                    .list();

            if (validImageIds.isEmpty()) {
                return "Các ảnh đã chọn không hợp lệ hoặc không thuộc quyền sở hữu của bạn.";
            }

            // [Bước 10.1.7 & 10.2.1] System: Kiểm tra ảnh đã tồn tại trong album chưa (BR-03)
            List<Integer> existingIds = handle.createQuery("SELECT image_id FROM album_images WHERE album_id = :albumId AND image_id IN (<validIds>)")
                    .bind("albumId", albumId)
                    .bindList("validIds", validImageIds)
                    .mapTo(int.class)
                    .list();

            boolean hasDuplicate = !existingIds.isEmpty();
            
            // Lọc bỏ các ảnh trùng (10.2.3 System không tạo liên kết trùng lặp)
            validImageIds.removeAll(existingIds);

            if (validImageIds.isEmpty()) {
                // [Bước 10.2.2] System: Tất cả đều đã tồn tại -> Thông báo
                return "Một hoặc nhiều ảnh đã tồn tại trong album.";
            }

            // [Bước 10.1.8] System: Tạo liên kết giữa ảnh và album (10.2.4 Tiếp tục xử lý ảnh hợp lệ)
            String sql = """
                INSERT INTO album_images (album_id, image_id)
                VALUES (:albumId, :imageId)
            """;

            for (Integer imageId : validImageIds) {
                handle.createUpdate(sql)
                        .bind("albumId", albumId)
                        .bind("imageId", imageId)
                        .execute();
            }

            if (hasDuplicate) {
                // Vẫn có ảnh trùng nhưng đã thêm được các ảnh mới
                return "Một hoặc nhiều ảnh đã tồn tại trong album. Các ảnh mới đã được thêm thành công.";
            }

            // [Bước 10.1.9] System: Trả kết quả
            return "Thêm ảnh thành công";
        });
    }
}
