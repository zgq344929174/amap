package com.zgq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zgq.service.ConnectMap;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class ConnectMapImpl implements ConnectMap {
    @Override
    public String getJson(String url) throws IOException {

        if (StringUtils.isEmpty(url)){
            return null;
        }
        Connection.Response accept = Jsoup.connect(url)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                .ignoreContentType(true).execute();
        return accept.body();
    }

    @Override
    public int getCount(String url) throws IOException {
        if(StringUtils.isEmpty(getJson(url))){
            return 0;
        }
        return Integer.parseInt(String.valueOf(JSONObject.parseObject(getJson(url)).get("count")));
    }
}
