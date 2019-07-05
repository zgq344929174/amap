package com.zgq.controller;

import com.zgq.utils.RestControllerHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Smileyan
 */
@RestController
public class UserController {

    @RequestMapping("/test")
    private Map<String, Object> test(HttpServletResponse response) {
        RestControllerHelper helper = new RestControllerHelper();
        helper.setCode(RestControllerHelper.SUCCESS);
        helper.setMsg("success");
        // 这里setData可以是某个类的对象，可以是链表等等
        Map name = new HashMap<String,String>();

        Map map = new HashMap<String,String>();
        map.put("name","zhangsan");
        map.put("age",12);
        Map map1 = new HashMap<String,String>();
        map1.put("name","zhangsan");
        map1.put("age",13);

        List list = new ArrayList<Map>();
        list.add(map);
        list.add(map1);
        name.put("list",list);
        helper.setData(name);
        return helper.toJsonMap();
    }
}