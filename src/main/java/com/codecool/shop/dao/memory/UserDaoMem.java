package com.codecool.shop.dao.memory;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.User;

public class UserDaoMem implements UserDao {
    private User user;
    private static UserDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private UserDaoMem() {}

    public static UserDaoMem getInstance() {
        if (instance == null) {
            instance = new UserDaoMem();
        }
        return instance;
    }

    @Override
    public void add(User user) {
        this.user = user;
    }
}
