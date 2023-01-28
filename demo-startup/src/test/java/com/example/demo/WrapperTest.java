package com.example.demo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WrapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testQueryWrapper() {
        // query username contains '3', age between 20 and 30, email!=null
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "3")
                .between("age", 20, 30)
                .isNotNull("email");
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user : users) {
            System.out.println(user.toString());
        }
    }

    @Test
    public void testQueryWrapper2() {
        // sort user by age
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("age")
                .orderByAsc("id");
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user : users) {
            System.out.println(user.toString());
        }
    }

    @Test
    public void testDeleteWrapper() {
        // remove email==null
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("email");
        int ret = userMapper.delete(queryWrapper);
        System.out.println(ret);
    }

    @Test
    public void testUpdateWrapper() {
        // remove email==null
        User user = userMapper.selectById(3);
        user.setName("openhe1");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", user.getId())
                .or()
                .gt("age", 99);
        int res = userMapper.update(user, queryWrapper);
        System.out.println(res);
    }

    @Test
    public void testQueryWrapper3() {
        // query username contains '3', age between 20 and 30, email!=null
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "i")
                .and(
                        wrapper -> wrapper.gt("age", 10)
                                .or()
                                .isNotNull("email")
                );
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user : users) {
            System.out.println(user.toString());
        }
    }

    @Test
    public void testQueryWrapper4() {
        // query username contains '3', age between 20 and 30, email!=null
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id", "select id from user where id<=100");
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user : users) {
            System.out.println(user.toString());
        }
    }

    @Test
    public void testUpdateWrapper2() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", "openhe_x")
                .set("name", "openhe111");
        int res = userMapper.update(null, updateWrapper);
        System.out.println(res);
    }

    @Test
    public void testCondition() {
        // if condition is false, the condition is omitted.
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Integer age = null;
        queryWrapper.ge(age != null, "age", 10);
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user : users) {
            System.out.println(user.toString());
        }
    }

    @Test
    public void testLambdaQueryWrapper() {
        // query username contains '3', age between 20 and 30, email!=null
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getName, "openhe")
                .between(User::getAge, 20, 30)
                .isNotNull(User::getEmail);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void testLambdaUpdateWrapper() {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getName, "openhe111")
                .set(User::getName, "openhe_x");
        int res = userMapper.update(null, updateWrapper);
        System.out.println(res);
    }
}
