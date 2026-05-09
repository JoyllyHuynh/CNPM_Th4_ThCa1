package DAO;

import model.User;
import java.util.Optional;

public class AuthDao extends BaseDao {

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = :email";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public void save(User user) {
        String sql = """
            INSERT INTO users (email, password, full_name, role, created_at)
            VALUES (:email, :password, :fullName, :role, NOW())
            """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("email", user.getEmail())
                        .bind("password", user.getPassword())
                        .bind("fullName", user.getFullname())
                        .bind("role", user.getRole() != null ? user.getRole() : "USER")
                        .execute()
        );
    }

    public Optional<User> findByEmail(String email) {
        String sql = """
            SELECT id, email, password, full_name as fullName, 
                   role, created_at as createdAt
            FROM users 
            WHERE email = :email
            """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .mapToBean(User.class)
                        .findOne()
        );
    }
}