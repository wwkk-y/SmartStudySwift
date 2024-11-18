package com.sss.mbg.mapper;

import com.sss.mbg.model.TmpTest2;
import com.sss.mbg.model.TmpTest2Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TmpTest2Mapper {
    long countByExample(TmpTest2Example example);

    int deleteByExample(TmpTest2Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(TmpTest2 row);

    int insertSelective(TmpTest2 row);

    List<TmpTest2> selectByExample(TmpTest2Example example);

    TmpTest2 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") TmpTest2 row, @Param("example") TmpTest2Example example);

    int updateByExample(@Param("row") TmpTest2 row, @Param("example") TmpTest2Example example);

    int updateByPrimaryKeySelective(TmpTest2 row);

    int updateByPrimaryKey(TmpTest2 row);
}