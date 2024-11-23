package com.sss.common.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface GlobalSetMapper {

    @Insert("insert into global_set(value) values (#{value})")
    void insert(@Param("value") String value);
}