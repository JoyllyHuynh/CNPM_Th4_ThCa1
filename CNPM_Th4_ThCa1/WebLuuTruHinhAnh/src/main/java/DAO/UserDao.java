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
}

