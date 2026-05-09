package controller.service;

import DAO.AuthDao;
import model.User;
import java.util.Optional;

public class AuthService {

    private final AuthDao userDao = new AuthDao();

    public boolean register(String email, String password, String fullName) {
        if (userDao.existsByEmail(email)) {
            return false;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullname(fullName);
        user.setRole("USER");

        userDao.save(user);
        return true;
    }

    public Optional<User> login(String email, String password) {
        Optional<User> userOpt = userDao.findByEmail(email);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        if (user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}