package com.zgq.service.impl;

import com.zgq.mapper.MapMapper;
import com.zgq.model.Map;
import com.zgq.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.spi.WebServiceFeatureAnnotation;
import java.util.List;

@Service
public class MapServiceImpl implements MapService {

    @Autowired
    MapMapper mapMapper;

    @Override
    public int insertBanch(List<Map> listMap) {
        return mapMapper.insertBanch(listMap);
    }
}
