package com.zgq.controller;

import com.zgq.service.AnalyticalData;
import com.zgq.websocket.Websocket;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MapController {

    @Autowired
    AnalyticalData analyticalData;

    @Autowired
    Websocket websocket;

    @RequestMapping("/map")
    public String analyticalData(@RequestParam("type") String type, @RequestParam("location") String centerPoint, @RequestParam("area") String area ){

        if (StringUtils.isEmpty(type)){
            websocket.sendMessage("请填写分类id");
            return "redirect:index";
        }
        analyticalData.analyticalJson(type ,centerPoint,0, area);
        return "redirect:index";
    }

    @RequestMapping("/map1")
    public String analyticalData1(@RequestParam("type") String type, @RequestParam("location") String centerPoint,  @RequestParam("area") String area ){

        if (StringUtils.isEmpty(type)){
            websocket.sendMessage("请填写分类id");
            return "redirect:index1";
        }
        if (StringUtils.isEmpty(area)){
            websocket.sendMessage("请填写分区");
            return "redirect:index1";
        }

        analyticalData.analyticalJson(type ,centerPoint,1, area);
        return "redirect:index1";
    }
}
