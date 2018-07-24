package com.zgq;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zgq.model.Map;
import com.zgq.model.User;
import com.zgq.service.ConnectMap;
import com.zgq.service.MapService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private ConnectMap connectMap;

    @Autowired
    private MapService mapService;

	@Test
	public void contextLoads() throws IOException {

		String url = "http://restapi.amap.com/v3/place/around?key=ca944d4a49f9aaede2f3e12068dddb81&location=116.314603,39.980688&output=json&radius=3000&types=120000&offset=50&page=18";

        Connection.Response accept = Jsoup.connect(url)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                .ignoreContentType(true).execute();
        String body = accept.body();
        System.out.println(body);
    }

    @Test
    public void getJson() throws IOException, InterruptedException {
	    String urlStart = "http://restapi.amap.com/v3/place/around?key=ca944d4a49f9aaede2f3e12068dddb81" +
                "&location=116.314603,39.980688&output=json&radius=3000";
	    String type = "&types=";
	    String offset = "&offset=";
	    String page = "&page=";
        /**http://restapi.amap.com/v3/place/around?key=ca944d4a49f9aaede2f3e12068dddb81
        &location=116.314603,39.980688&output=json&radius=3000&types=120000&offset=50&page=18
         **/
        int type1 = 120000;
        String url = "http://restapi.amap.com/v3/place/around?key=ca944d4a49f9aaede2f3e12068dddb81&location=116.314603,39.980688&output=json&radius=3000&types=120000&offset=50&page=18";
        Integer count = connectMap.getCount(url);
        int pageSize =20;
        int totalPage = 0 ;
        if(count % pageSize >0){
            totalPage = count/pageSize + 1;
        }else{
            totalPage = count/pageSize;
        }

        System.out.println();
        if(count>0){
            for(int i=0;i<totalPage; i++){
                int currentpage = i+1;
                String json = connectMap.getJson(urlStart + type + type1 + offset + pageSize + page + currentpage);
                JSONObject jsonObject = JSONObject.parseObject(json);
                String pois = String.valueOf(jsonObject.get("pois"));
                JSONArray jsonArray = JSON.parseArray(pois);
                ArrayList<Map> listMap = new ArrayList<>();
                for(int j=0;j<jsonArray.size();j++){
                    Map map = new Map();
                    String address = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("address"));
                    String adname = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("adname"));
                    String name = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("name"));
                    String location = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("location"));
                    String cityname = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("cityname"));
                    String typechild = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("type"));
                    String typecode = String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("typecode"));
                    long distance = Long.parseLong(String.valueOf(JSONObject.parseObject(String.valueOf(jsonArray.get(j))).get("distance")));
                    map.setAddress(address);
                    map.setAdname(adname);
                    map.setCityname(cityname);
                    map.setDistance(distance);
                    map.setLocation(location);
                    map.setName(name);
                    map.setType(typechild);
                    map.setTypecode(typecode);
                    listMap.add(map);
                    System.out.println("总数量" + count + "===============>第" + currentpage + "页" + "====》内容===" + jsonArray.get(j));
                }

                int insertTotalCount = mapService.insertBanch(listMap);
                System.out.println(insertTotalCount);
                System.out.println("总数量" + count + "===============>第" + currentpage + "页");

                Thread.sleep(1500);
            }
        }
    }

}
