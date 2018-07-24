package com.zgq.mapper;

import com.zgq.model.Map;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapMapper {
    int insert(Map record);

    int insertSelective(Map record);

    int insertBanch(List<Map> listMap);
}