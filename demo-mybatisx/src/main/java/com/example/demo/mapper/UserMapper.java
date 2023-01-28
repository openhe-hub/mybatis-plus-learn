package com.example.demo.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.example.demo.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 62552
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-01-28 16:30:46
* @Entity com.example.demo.pojo.User
*/
public interface UserMapper extends BaseMapper<User> {
    int insertSelective(User user);

    int deleteByIdAndName(@Param("id") Long id, @Param("name") String name);

    int updateAgeAndEmailById(@Param("age") Integer age, @Param("email") String email, @Param("id") Long id);

    List<User> selectAgeAndNameByAgeBetween(@Param("beginAge") Integer beginAge, @Param("endAge") Integer endAge);

    List<User> selectAllByIdOrderByAgeDesc(@Param("id") Long id);
}




