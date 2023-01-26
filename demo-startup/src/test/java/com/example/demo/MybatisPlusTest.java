package com.example.demo;

import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MybatisPlusTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectList() {
        userMapper.selectList(null)
                .forEach(System.out::println);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setName("openhe4");
        user.setAge(80);
        user.setEmail("444@qq.com");
        int result = userMapper.insert(user);
        System.out.println("result:" + result);
        System.out.println("id=" + user.getId());
    }

    @Test
    public void testDeleteById() {
        int result = userMapper.deleteById(1618488368245923842L);
        System.out.println(result);
    }

    @Test
    public void testDeleteByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "openhe");
        map.put("age", 23);
        int result = userMapper.deleteByMap(map);
        System.out.println(result);
    }

    @Test
    public void testDeleteInBatch() {
        List<Long> ids = Arrays.asList(1L, 2L);
        int result = userMapper.deleteBatchIds(ids);
        System.out.println(result);
    }

    @Test
    public void testUpdateById(){
        User user = userMapper.selectById(4L);
        user.setAge(100);
        int result = userMapper.updateById(user);
        System.out.println(result);
    }

    @Test
    public void testSelectBatchIds(){
        List<Long> ids = Arrays.asList(3L, 4L, 5L);
        List<User> users = userMapper.selectBatchIds(ids);
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "openhe3");
        map.put("age", 23);
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectMapById(){
        Map<String,Object> map=userMapper.selectMapById(3L);
        System.out.println(map);
    }
}
