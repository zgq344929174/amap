package com.zgq.controller;

import com.zgq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by z on 2018/6/24.
 */
@RestController
public class MyBatisController {

    @Autowired
    private UserService userService;

    @GetMapping("/boot/getusers")
    public Object getUsers(){

        //线程，该线程调用底层查询所有user的方法
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                userService.getAllUser();
            }
        };
        //多线程测试一下缓存穿透问题
        ExecutorService executorService = Executors.newFixedThreadPool(39);
        for(int i=0;i<10000;i++){
            executorService.submit(runnable);
        }


        return userService.getAllUser();
    }

    @GetMapping("/boot/update")
    public int update(){
        return userService.update();
    }

}
