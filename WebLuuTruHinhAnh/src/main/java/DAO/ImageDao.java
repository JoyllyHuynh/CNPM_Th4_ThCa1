package DAO;

import com.sun.jdi.connect.spi.Connection;
import model.Image;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImageDao extends BaseDao {

    // [Bước 3.3.3] Thực hiện câu lệnh truy vấn lọc trong cơ sở dữ liệu để tìm ra các bản ghi ảnh thỏa mãn điều kiện
    public List<Image> searchByKW(int userId, String kw) {
        if (kw == null || kw.trim().isEmpty())
            return List.of();
        String keyword = "%" + kw.trim() + "%";
        String sql = """
                SELECT id, user_id, file_name, file_path, description,
                       file_size, upload_date, is_deleted,
                       download_count
                FROM images i
                WHERE i.user_id = :userId
                  AND i.is_deleted = FALSE
                  AND (
                      i.file_name LIKE :keyword 
                      OR i.description LIKE :keyword
                      OR EXISTS (
                          SELECT 1 FROM album_images ai
                          JOIN albums a ON ai.album_id = a.id
                          WHERE ai.image_id = i.id
                            AND a.album_name LIKE :keyword
                      )
                  )
                ORDER BY i.upload_date DESC
                """;
        return getJdbi().withHandle(handle -> handle.createQuery(sql)
                .bind("userId", userId)
                .bind("keyword", keyword)
                .map((rs, ctx) -> mapRow(rs))
                .list());
    }

    public List<Image> getImagesSorted(int userId, String sortBy) {
        String orderByClause = switch (sortBy != null ? sortBy.toLowerCase() : "newest") {
            case "oldest" -> "ORDER BY upload_date ASC";
            case "nameaz" -> "ORDER BY file_name ASC";
            case "nameza" -> "ORDER BY file_name DESC";
            default -> "ORDER BY upload_date DESC";
        };

        String sql = """
                SELECT id, user_id, file_name, file_path, description,
                       file_size, upload_date, is_deleted,
                       download_count
                FROM images
                WHERE user_id = :userId
                  AND is_deleted = FALSE
                """ + " " + orderByClause;

        return getJdbi().withHandle(handle -> handle.createQuery(sql)
                .bind("userId", userId)
                .map((rs, ctx) -> mapRow(rs))
                .list());
    }

    public void insertImage(Image image) {
        String sql = """
                INSERT INTO images (user_id, file_name, file_path, description, file_size, upload_date, is_deleted)
                VALUES (:userId, :fileName, :filePath, :description, :fileSize, :uploadDate, FALSE)
                """;
        getJdbi().useHandle(handle -> handle.createUpdate(sql)
                .bind("userId", image.getUserId())
                .bind("fileName", image.getFileName())
                .bind("filePath", image.getFilePath())
                .bind("description", image.getDescription())
                .bind("fileSize", image.getFileSize())
                .bind("uploadDate", Date.valueOf(image.getUploadDate()))
                .execute());
    }

    public List<Image> getAllImages() {
        String sql = """
                SELECT id, user_id, file_name, file_path, description,
                       file_size, upload_date, is_deleted,
                       download_count
                FROM images
                WHERE is_deleted = FALSE
                ORDER BY upload_date DESC
                """;
        return getJdbi().withHandle(handle -> handle.createQuery(sql)
                .map((rs, ctx) -> mapRow(rs))
                .list());
    }

    public void deleteImage(int id) {
        String sql = "UPDATE images SET is_deleted = TRUE WHERE id = :id";
        getJdbi().useHandle(handle -> handle.createUpdate(sql)
                .bind("id", id)
                .execute());
    }

    public Image findById(int id) {
        String sql = """
                SELECT id, user_id, file_name, file_path, description,
                       file_size, upload_date, is_deleted,
                       download_count
                FROM images
                WHERE id = :id AND is_deleted = FALSE
                """;
        return getJdbi().withHandle(handle -> handle.createQuery(sql)
                .bind("id", id)
                .map((rs, ctx) -> mapRow(rs))
                .findOne()
                .orElse(null));
    }

    public int countImages() {
        return getJdbi().withHandle(handle -> handle.createQuery("SELECT COUNT(*) FROM images WHERE is_deleted = FALSE")
                .mapTo(Integer.class).one());
    }

    public int countDeletedImages() {
        return getJdbi().withHandle(handle -> handle.createQuery("SELECT COUNT(*) FROM images WHERE is_deleted = TRUE")
                .mapTo(Integer.class).one());
    }

    private Image mapRow(java.sql.ResultSet rs) throws java.sql.SQLException {
        Image img = new Image();
        img.setId(rs.getInt("id"));
        img.setUserId(rs.getInt("user_id"));
        img.setFileName(rs.getString("file_name"));
        img.setFilePath(rs.getString("file_path"));
        img.setDescription(rs.getString("description"));
        img.setFileSize(rs.getLong("file_size"));
        img.setDownloadCount(rs.getInt("download_count"));
        java.sql.Timestamp ts = rs.getTimestamp("upload_date");

        if(ts != null){
            img.setUploadDate(
                    ts.toLocalDateTime().toLocalDate()
            );
        }
        img.setDeleted(rs.getBoolean("is_deleted"));
        return img;
    }

    public boolean updateImageName(int id, String newName) {
        String sql = "UPDATE images SET file_name = :newName WHERE id = :id AND is_deleted = FALSE";

        return getJdbi().withHandle(handle -> {
            int rowsAffected = handle.createUpdate(sql)
                    .bind("newName", newName)
                    .bind("id", id)
                    .execute();
            return rowsAffected > 0; // Trả về true nếu update thành công ít nhất 1 dòng
        });
    }

    // [Bước 7.2.8] Truy vấn SELECT DISTINCT file_name (LIMIT 7) trong DB để lấy danh sách gợi ý
    public List<String> getSearchSuggestions(int userId, String kw) {
        if (kw == null || kw.trim().isEmpty())
            return List.of();
        String keyword = "%" + kw.trim() + "%";
        String sql = """
                SELECT DISTINCT file_name
                FROM images
                WHERE user_id = :userId
                  AND is_deleted = FALSE
                  AND file_name LIKE :keyword
                LIMIT 7
                """;
        // Execute query and return suggestions
        return getJdbi().withHandle(handle -> handle.createQuery(sql)
                .bind("userId", userId)
                .bind("keyword", keyword)
                .mapTo(String.class)
                .list());
    }

    public List<Integer> getImageIdsByUserId(int userId) {
        String sql = """
        SELECT id
        FROM images
        WHERE user_id = :userId
          AND is_deleted = FALSE
        ORDER BY id DESC
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .list()
        );
    }

    public List<Image> getImagesByUserId(int userId) {
        String sql = """
        SELECT *
        FROM images
        WHERE user_id = :userId
          AND is_deleted = FALSE
        ORDER BY id DESC
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(Image.class)
                        .list()
        );
    }

    public void increaseDownloadCount(int imageId) {

        String sql = """
        UPDATE images
        SET download_count = download_count + 1
        WHERE id = :id
        """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", imageId)
                        .execute()
        );
    }
}

