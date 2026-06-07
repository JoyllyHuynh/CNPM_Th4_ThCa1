package controller.service;

import DAO.UserDao;
import model.User;

public class UserService {

    private UserDao userDao = new UserDao();
    // [UC09 - Bước 9.1.6.1]
    // Lấy thông tin uploader của ảnh
    public User getUserById(int id){
        return userDao.getUserById(id);
    }
}