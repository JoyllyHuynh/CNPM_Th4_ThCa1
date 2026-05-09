package DAO;

import model.Image;

import java.util.*;

public class ImageDao extends BaseDao{

    public List<Image> searchByKW(int userId, String kw) {
        if (kw == null || kw.trim().isEmpty()) {
            return List.of();
        }

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

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("keyword", keyword)
                        .mapToBean(Image.class)
                        .list()
        );
    }

    public List<Image> getImagesSorted(int userId, String sortBy) {

        String orderByClause;

        switch (sortBy != null ? sortBy.toLowerCase() : "newest") {
            case "oldest" -> orderByClause = "ORDER BY upload_date ASC";
            case "nameaz" -> orderByClause = "ORDER BY file_name ASC";
            case "nameza" -> orderByClause = "ORDER BY file_name DESC";
            default -> orderByClause = "ORDER BY upload_date DESC"; // newest
        }

        String sql = """
        SELECT id, user_id, file_name, file_path, description, 
               file_size, upload_date, is_deleted 
        FROM images 
        WHERE user_id = :userId 
          AND is_deleted = FALSE
        """ + " " + orderByClause;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(Image.class)
                        .list()
        );
    }
}

