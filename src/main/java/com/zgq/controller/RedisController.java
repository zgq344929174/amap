package com.zgq.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by z on 2018/6/24.
 */
@Controller
public class RedisController {
    private RedisTemplate<String,String> redisTemplate;
    @RequestMapping("/boot/redis/{key}")
    public String getRedisValue(@PathVariable("key") String key){

        String bb = redisTemplate.opsForValue().get(key);
        if(null == bb){
            redisTemplate.opsForValue().set("bb","testredis");
        }
        return null;

    }
}
