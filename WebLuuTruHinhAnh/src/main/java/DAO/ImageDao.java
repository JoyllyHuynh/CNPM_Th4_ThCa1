package DAO;

import model.Image;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ImageDao extends BaseDao {

    public List<Image> searchByKW(int userId, String kw) {
        if (kw == null || kw.trim().isEmpty())
            return List.of();
        String keyword = "%" + kw.trim() + "%";
        String sql = """
                SELECT id, user_id, file_name, file_path, description,
                       file_size, upload_date, is_deleted
                FROM images
                WHERE user_id = :userId
                  AND is_deleted = FALSE
                  AND (file_name LIKE :keyword OR description LIKE :keyword)
                ORDER BY upload_date DESC
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
                       file_size, upload_date, is_deleted
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
                       file_size, upload_date, is_deleted
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
                       file_size, upload_date, is_deleted
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
        Date d = rs.getDate("upload_date");
        img.setUploadDate(d != null ? d.toLocalDate() : LocalDate.now());
        img.setDeleted(rs.getBoolean("is_deleted"));
        return img;
    }
}
