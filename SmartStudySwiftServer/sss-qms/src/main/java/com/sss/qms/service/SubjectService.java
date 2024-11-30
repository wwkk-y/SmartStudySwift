package com.sss.qms.service;

import com.sss.qms.mapper.SubjectMapper;
import com.sss.qms.vo.SubjectQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

@Service
@Validated
public class SubjectService {

    @Resource
    private SubjectMapper subjectMapper;

    public List<SubjectQueryVo> listAll(){
        return subjectMapper.listAll();
    }

}
