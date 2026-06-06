package DAO;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends BaseDao {

    public List<User> getAllUsers() {

        String sql = """
                SELECT *
                FROM users
                ORDER BY created_at DESC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(User.class)
                        .list()
        );
    }

    public void deleteUser(int id) {

        String sql = """
            DELETE FROM users
            WHERE id = :id
            """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", id)
                        .execute()
        );
    }
    public User getUserById(int id) {

        String sql = """
        SELECT *
        FROM users
        WHERE id = :id
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", id)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public User login(String email, String password) {

        String sql = """
            SELECT *
            FROM users
            WHERE email = :email
            AND password = :password
            LIMIT 1
            """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .bind("password", password)
                        .mapToBean(User.class)
                        .findFirst()
                        .orElse(null)
        );
    }

    public int countUsers() {

        String sql = "SELECT COUNT(*) FROM users";

        return getJdbi().withHandle(handle ->

                handle.createQuery(sql)
                        .mapTo(int.class)
                        .one()
        );
    }

    public void updateStatus(int id, String status) {
        String sql = """
            UPDATE users
            SET status = :status
            WHERE id = :id
            """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("id", id)
                        .execute()
        );
    }

//    public User getUserById(int id) {
//
//        String sql = """
//        SELECT *
//        FROM users
//        WHERE id = :id
//        """;
//
//        return getJdbi().withHandle(handle ->
//                handle.createQuery(sql)
//                        .bind("id", id)
//                        .mapToBean(User.class)
//                        .findOne()
//                        .orElse(null)
//        );
//    }

    public boolean updateProfile(
            int userId,
            String fullName,
            String email) {

        String sql = """
        UPDATE users
        SET full_name = :fullName,
            email = :email
        WHERE id = :id
        """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", userId)
                        .bind("fullName", fullName)
                        .bind("email", email)
                        .execute() > 0
        );
    }

    public User findByEmail(String email) {

        String sql = """
        SELECT *
        FROM users
        WHERE email = :email
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public void updateProfileAndPassword(
            int userId,
            String fullName,
            String email,
            String password){

        String sql = """
        UPDATE users
        SET full_name = :fullName,
            email = :email,
            password = :password
        WHERE id = :id
        """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", userId)
                        .bind("fullName", fullName)
                        .bind("email", email)
                        .bind("password", password)
                        .execute());
    }

    public void updateAvatar(
            int userId,
            String avatar){

        String sql = """
        UPDATE users
        SET avatar = :avatar
        WHERE id = :id
        """;

        getJdbi().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", userId)
                        .bind("avatar", avatar)
                        .execute());
    }
}

