package com.sss.qms.mapper;

import com.sss.qms.vo.SubjectQueryVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SubjectMapper {

    @Select("select id, name from qms_subject")
    List<SubjectQueryVo> listAll();

}
