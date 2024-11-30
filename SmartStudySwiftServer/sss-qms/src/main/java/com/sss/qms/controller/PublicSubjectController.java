package com.sss.qms.controller;

import com.sss.common.api.RPage;
import com.sss.common.api.RResult;
import com.sss.qms.service.SubjectService;
import com.sss.qms.vo.SubjectQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("学科相关")
@RequestMapping("/public/subject")
public class PublicSubjectController {

    @Resource
    private SubjectService subjectService;

    @GetMapping("/list/all")
    @ApiOperation("查询所有学科")
    public RResult<List<SubjectQueryVo>> listAll(){
        return RResult.success(subjectService.listAll());
    }
}
