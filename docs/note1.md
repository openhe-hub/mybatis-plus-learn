# Mybatis Plus Note1: 入门配置使用，单表增删改查，常用注解
1. 数据库建表(环境：mysql 8+) 
    ```sql  
    CREATE DATABASE `mybatis_plus` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
    use `mybatis_plus`;
    CREATE TABLE `user` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `name` varchar(30) DEFAULT NULL COMMENT '姓名',
    `age` int(11) DEFAULT NULL COMMENT '年龄',
    `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    ```
2. 整合Springboot配置及demo
   1. `maven`依赖：mysql驱动+mybatis plus启动器
      ```xml
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.2.0</version>
        </dependency>
      ``` 
   2. `application.yml`配置
      ```yaml
      spring:
        datasource:
            type: com.zaxxer.hikari.HikariDataSource
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT%2B8&characterEncoding=utf-8&useSSL=false
            username: root
            password: ******
      ``` 
   3. pojo类：注意类名和属性名与数据库表名字段名相对应，否则要显式配置   
      ```java
        @Data
        public class User {
            private Long id;
            private String name;
            private Integer age;
            private String email;
        }
      ``` 
   4. `mapper`接口
      ```java
        @Repository
        public interface UserMapper extends BaseMapper<User> {

        }
      ``` 
      * 注意还要在启动类上添加扫描`/mapper`的注解配置
        ```java
        @MapperScan("com.example.demo.mapper")
        ```
   5. 使用：demo使用Springboot的单元测试 
      ```java
        @SpringBootTest
        public class MybatisPlusTest {
            @Autowired
            private UserMapper userMapper;

            @Test
            public void testSelectList() {
                userMapper.selectList(null)
                        .forEach(System.out::println);
            }
        }    
      ``` 
3. 单表增删改查
   * Mybatis Plus内置了大量的增删改查方法，可以不写sql直接使用
   ```java
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
   ``` 
   * 启用sql生成语句的日志输出
     ```yaml
     mybatis-plus:
        configuration:
            log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
     ``` 
4. 自定义mybatis mapper
   1. 默认mybatis mapper xml文件在`/resource/mapper/**/*.xml`，使用与mybatis相同
   2. demo code：（自定义：将查询结果封装成map）
        * UserMapper.xml 
            ```xml
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
                        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
                <mapper namespace="com.example.demo.mapper.UserMapper">
                    <select id="selectMapById" resultType="map">
                        select id, name, age, email
                        from user
                        where id = #{id}
                    </select>
                </mapper>
            ``` 
      * 在接口中添加对应方法
        ```java
        @Repository
        public interface UserMapper extends BaseMapper<User> {
            Map<String, Object> selectMapById(Long id);
        }
        ``` 
      * 使用
        ```java
        @Test
        public void testSelectMapById(){
            Map<String,Object> map=userMapper.selectMapById(3L);
            System.out.println(map);
        }
        ``` 
5. 通用Service层接口
   * Mybatis Plus封装了常用的Service层的代码，接口：`IService<Bean>`，实现类：`ServiceImpl<Mapper, Bean>`，只需继承后开箱即用
   * demo code
     1. Service接口
        ```java
        public interface UserService extends IService<User> {

        }
        ``` 
     2. Service实现类，注意`@Service`注解应该放在实现类上，因为Spring ioc容器无法实例化接口
        ```java
        @Service
        public class UserServiceImpl extends ServiceImpl<UserMapper, User>
                implements UserService {

        }
        ``` 
     3. 使用：demo测试了批量添加和count功能
        ```java
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
        ``` 
6. 常用注解
   1. `@TableName("table_name")`设置对应数据表名称
   2. `@TableId`标明该属性对应主键
      1. `value`设置对应表中主键的名字
      2. `type`设置主键生成策略
         1. `IdType.ASSIGN_ID`：雪花算法
         2. `IdType.AUTO`：自增
   3. `@TableField`设置属性对应字段名
      * Mybatis plus在属性名不对应的情况下，自动将java代码中的驼峰命名=>下划线命名，如`userName=>user_name`
   4. `@TableLogic`创建逻辑删除字段：删除场景下，数据不会被物理删除，而是将对应数据中代表是否被删除字段的状态修改为被删除状态，后续可以进行数据恢复
7. Mybatis Plus主键生成策略:雪花算法
   ![](https://raw.githubusercontent.com/openhe-hub/my-img-repo/main/img/uTools_1674737372731.png) 
   ![](https://raw.githubusercontent.com/openhe-hub/my-img-repo/main/img/uTools_1674737386028.png)
   ![](https://raw.githubusercontent.com/openhe-hub/my-img-repo/main/img/uTools_1674737431399.png)