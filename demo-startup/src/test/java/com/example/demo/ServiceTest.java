package com.example.demo;

import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testUserCount() {
        int count = userService.count();
        System.out.println(count);
    }

    @Test
    public void add() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("abc" + i);
            user.setAge(20);
            users.add(user);
        }
        boolean ret = userService.saveBatch(users);
        System.out.println(ret);
    }
}
