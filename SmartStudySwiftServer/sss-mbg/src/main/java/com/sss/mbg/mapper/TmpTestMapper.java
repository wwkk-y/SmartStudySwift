package com.sss.mbg.mapper;

import com.sss.mbg.model.TmpTest;
import com.sss.mbg.model.TmpTestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TmpTestMapper {
    long countByExample(TmpTestExample example);

    int deleteByExample(TmpTestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TmpTest row);

    int insertSelective(TmpTest row);

    List<TmpTest> selectByExample(TmpTestExample example);

    TmpTest selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") TmpTest row, @Param("example") TmpTestExample example);

    int updateByExample(@Param("row") TmpTest row, @Param("example") TmpTestExample example);

    int updateByPrimaryKeySelective(TmpTest row);

    int updateByPrimaryKey(TmpTest row);
}