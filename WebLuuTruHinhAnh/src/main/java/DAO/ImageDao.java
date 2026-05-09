package DAO;

import model.Image;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ImageDao extends BaseDao{
    Connection conn;

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

    public List<Image> getAllImages() {

        String sql = """
                SELECT *
                FROM images
                WHERE is_deleted = FALSE
                ORDER BY upload_date DESC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Image.class)
                        .list()
        );
    }

    public void deleteImage(int id) {

        String sql = """
            UPDATE images
            SET is_deleted = TRUE
            WHERE id = :id
            """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", id)
                        .execute()
        );
    }

    public int countImages() {

        String sql = """
        SELECT COUNT(*)
        FROM images
        WHERE is_deleted = FALSE
        """;

        return getJdbi().withHandle(handle ->

                handle.createQuery(sql)
                        .mapTo(int.class)
                        .one()
        );
    }

    public int countDeletedImages() {

        String sql = """
        SELECT COUNT(*)
        FROM images
        WHERE is_deleted = TRUE
        """;

        return getJdbi().withHandle(handle ->

                handle.createQuery(sql)
                        .mapTo(int.class)
                        .one()
        );
    }

}

