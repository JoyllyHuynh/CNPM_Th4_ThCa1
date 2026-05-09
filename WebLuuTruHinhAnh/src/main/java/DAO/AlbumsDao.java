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
                                        COUNT(ai.image_id) AS item_count,
                                        (SELECT i.file_path 
                                         FROM images i
                                         JOIN album_images ai2 ON i.id = ai2.image_id
                                         WHERE ai2.album_id = a.id
                                         LIMIT 1) AS cover_url
                                    FROM albums a
                                    LEFT JOIN album_images ai ON a.id = ai.album_id
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
        int rows = getJdbi().withHandle(handle ->
                handle.createUpdate("""
                                    INSERT INTO albums (user_id, album_name, created_at)
                                    VALUES (:uid, :name, NOW())
                                """)
                        .bind("uid", uid)
                        .bind("name", albumName)
                        .execute()
        );

        return rows > 0;
    }

    public boolean deleteAlbum(int uid, int albumId) {
        return getJdbi().withHandle(handle -> {

            // 1. Xóa quan hệ trước (album_images) để tránh lỗi FK
            handle.createUpdate("""
                DELETE FROM album_images
                WHERE album_id = :albumId
                """)
                    .bind("albumId", albumId)
                    .execute();

            // 2. Xóa album nhưng phải đúng user
            int rows = handle.createUpdate("""
                DELETE FROM albums
                WHERE id = :albumId AND user_id = :uid
                """)
                    .bind("albumId", albumId)
                    .bind("uid", uid)
                    .execute();

            return rows > 0;
        });
    }
}
