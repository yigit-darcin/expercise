package com.kodility.service.user;

import com.kodility.dao.user.UserDao;
import com.kodility.domain.user.User;
import com.kodility.utils.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User findById(long id) {
        return userDao.findOne(id);
    }

    public boolean emailNotRegisteredYet(String email) {
        return findByEmail(email) == null;
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

}