package com.zgq.service.impl;

import com.zgq.mapper.UserMapper;
import com.zgq.model.User;
import com.zgq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by z on 2018/6/24.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public List<User> getAllUser() {

        //redis序列化
        RedisSerializer redisSerializer = new StringRedisSerializer();
        //设置redis的键的 序列化类型 如果不设置的话，redis键是乱码，看不懂
        redisTemplate.setKeySerializer(redisSerializer);

        //高并发条件下，此处有点问题：缓存穿透
        //查询缓存
        List<User> userList = (List<User>)redisTemplate.opsForValue().get("allUsers");

        //双层检测锁
        if(null == userList){
            System.out.println("查询的数据库。。。。。。。。");
            //缓存为空，查询一遍数据库
            userList = userMapper.selectAllUsers();
            //把数据库查询出来的数据，fangruredis中
            redisTemplate.opsForValue().set("allUsers",userList);
        }else{
            System.out.println("查询的缓存。。。。。。。。");
        }

        return userList;
    }

    /*@Override
    public List<User> getAllUser() {

        //redis序列化
        RedisSerializer redisSerializer = new StringRedisSerializer();
        //设置redis的键的 序列化类型 如果不设置的话，redis键是乱码，看不懂
        redisTemplate.setKeySerializer(redisSerializer);

        //高并发条件下，此处有点问题：缓存穿透
        //查询缓存
        List<User> userList = (List<User>)redisTemplate.opsForValue().get("allUsers");

        //双层检测锁
        if(null == userList){
            synchronized (this){
                userList = (List<User>)redisTemplate.opsForValue().get("allUsers");
                if(null == userList){
                    System.out.println("查询的数据库。。。。。。。。");
                    //缓存为空，查询一遍数据库
                    userList = userMapper.selectAllUsers();
                    //把数据库查询出来的数据，fangruredis中
                    redisTemplate.opsForValue().set("allUsers",userList);
                }else{
                    System.out.println("查询的缓存。。。。。。。。");
                }
            }
        }else {
            System.out.println("查询的缓存。。。。。。。。");
        }

        return userList;
    }*/

    @Transactional
    @Override
    public int update() {
        User user = new User();
        user.setId(1L);
        user.setName("张刚强");
        user.setRemakes("张刚强-update");
        user.setAge(30);
        int update = userMapper.updateByPrimaryKey(user);
        System.out.println("更新结果======"+ update);

        //抛出异常
//        int a = 10 / 0;


        return update;
    }
}
