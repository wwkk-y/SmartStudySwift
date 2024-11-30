package com.sss.ums.service;

import com.sss.ums.mapper.UmsUserMapper;
import com.sss.ums.vo.UmsUserVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UmsUserService {

    @Resource
    private UmsUserMapper umsUserMapper;

    /**
     * 查询用户信息
     * @param name 匹配 username | nickName
     */
    public List<UmsUserVo> list(String name){
        return umsUserMapper.queryUmsUserList(name);
    }
}
