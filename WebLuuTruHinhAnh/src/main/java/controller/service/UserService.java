package controller.service;

import DAO.UserDao;
import model.User;

public class UserService {

    private UserDao userDao = new UserDao();

    public User getUserById(int id){
        return userDao.getUserById(id);
    }
}