package com.zgq.service;

import java.io.IOException;

public interface ConnectMap {

    String getJson(String url) throws IOException;

    int getCount(String url) throws IOException;

}
