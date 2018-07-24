package com.zgq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zgq.model.Map;
import com.zgq.service.AnalyticalData;
import com.zgq.service.ConnectMap;
import com.zgq.service.MapService;
import com.zgq.websocket.Websocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class AnalyticalDataImpl implements AnalyticalData {

    private final String POINTURL = "http://restapi.amap.com/v3/place/around?key=ca944d4a49f9aaede2f3e12068dddb81&output=json&radius=3000";
    private final String RECURL = "https://restapi.amap.com/v3/place/polygon?key=ca944d4a49f9aaede2f3e12068dddb81&output=json";


    @Autowired
    ConnectMap connectMap;

    @Autowired
    MapService mapService;

    @Autowired
    Websocket websocket;

    @Value("${sleepTime}")
    private Integer sleepTime;

    @Async
    @Override
    public String analyticalJson(String type, String centerPoint, Integer flag, String area) {

        String url = "";
        String urlStart;


        if(flag==0){
            if(StringUtils.isEmpty(centerPoint)){
                centerPoint = "116.314603,39.980688";
            }
            url = POINTURL +"&location=" + centerPoint + "&types=" + type + "&offset=1&page=1";
            urlStart =POINTURL + "&location=" + centerPoint;
        }else {
            if(StringUtils.isEmpty(centerPoint)){
                centerPoint = "116.305206,39.985795|116.353357,39.987176|116.354731,39.967707|116.308039,39.962905";
            }
            url = RECURL + "&polygon=" + centerPoint + "&types=" + type + "&offset=1&page=1";
            urlStart = RECURL + "&polygon=" + centerPoint;
        }



//        String url = "http://restapi.amap.com/v3/place/around?key=ca944d4a49f9aaede2f3e12068dddb81&location=" + centerPoint + "&output=json&radius=3000&types=120000&offset=20&page=";


        /**http://restapi.amap.com/v3/place/around?key=ca944d4a49f9aaede2f3e12068dddb81
         &location=116.314603,39.980688&output=json&radius=3000&types=120000&offset=50&page=18
         **/
//        String urlStart = "http://restapi.amap.com/v3/place/around?key=ca944d4a49f9aaede2f3e12068dddb81" +
//                "&location=" + centerPoint + "&output=json&radius=3000";
        String types = "&types=";
        String offset = "&offset=";
        String page = "&page=";
        String result = "";


//            int type1 = 120000;

        Integer count = null;
        try {
            count = connectMap.getCount(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int pageSize = 20;
        int totalPage = 0;
        if (count % pageSize > 0) {
            totalPage = count / pageSize + 1;
        } else {
            totalPage = count / pageSize;
        }

        if (count > 0) {
            for (int i = 0; i < totalPage; i++) {
                int currentpage = i + 1;
                String json = null;
                try {
                    String currentUrl = urlStart  + types + type + offset + pageSize + page + currentpage;
                    json = connectMap.getJson(currentUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = JSONObject.parseObject(json);
                String pois = String.valueOf(jsonObject.get("pois"));
                JSONArray jsonArray = JSON.parseArray(pois);
                ArrayList<Map> listMap = new ArrayList<>();
                for (int j = 0; j < jsonArray.size(); j++) {
                    Map map = new Map();
                    String address = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("address"));
                    String adname = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("adname"));
                    String name = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("name"));
                    String location = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("location"));
                    String cityname = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("cityname"));
                    String typechild = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("type"));
                    String typecode = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("typecode"));
//                    long distance = Long.parseLong(String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("distance")));
                    map.setAddress(address);
                    map.setAdname(adname);
                    map.setCityname(cityname);
//                    map.setDistance(distance);
                    map.setLocation(location);
                    map.setName(name);
                    map.setType(typechild);
                    map.setTypecode(typecode);
                    map.setArea(area);

                    listMap.add(map);
                    System.out.println("总数量" + count + "===============>第" + currentpage + "页" + "====》内容===" + jsonArray.get(j));

                }

                if (listMap.isEmpty()) {
                    result = "插入完成";
                    websocket.sendMessage(result);
                    break;
                }
                int insertTotalCount = mapService.insertBanch(listMap);
                System.out.println("总数量" + count + "===============>第" + currentpage + "页" + "===插入成功" + insertTotalCount + "条记录");
                result = "总数量" + count + "===============>第" + currentpage + "页" + "===插入成功" + insertTotalCount + "条记录";
                websocket.sendMessage(result);

                try {

                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
