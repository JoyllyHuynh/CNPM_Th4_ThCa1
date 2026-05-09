package DAO;

import com.mysql.cj.jdbc.MysqlDataSource;
import model.User;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class BaseDao {

    private Jdbi jdbi;

    protected Jdbi getJdbi() {
        if (jdbi == null) connect();
        return jdbi;
    }

    protected void connect() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://" + DBProperties.host + ":" + DBProperties.port + "/" + DBProperties.name
                + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        dataSource.setUser(DBProperties.user);
        dataSource.setPassword(DBProperties.password);
        try {
            dataSource.setAutoReconnect(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        jdbi = Jdbi.create(dataSource);

        // Map LocalDate <-> SQL DATE
        jdbi.registerColumnMapper(java.time.LocalDate.class, (rs, col, ctx) -> {
            java.sql.Date d = rs.getDate(col);
            return d != null ? d.toLocalDate() : null;
        });

        // Bỏ qua cột không match (tránh lỗi khi DB có cột thừa)
        jdbi.getConfig(org.jdbi.v3.core.mapper.reflect.ReflectionMappers.class)
                .setStrictMatching(false);
    }

    public static class DBProperties {
        private static final Properties prop = new Properties();

        static {
            try {
                File file = new File("..\\src\\main\\resources\\db.properties");
                if (file.exists()) {
                    prop.load(new FileInputStream(file));
                } else {
                    prop.load(DBProperties.class.getClassLoader().getResourceAsStream("db.properties"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static final String host = prop.getProperty("db.host");
        public static final String port = prop.getProperty("db.port");
        public static final String user = prop.getProperty("db.user");
        public static final String password = prop.getProperty("db.pass");
        public static final String name = prop.getProperty("db.name");
    }
}
