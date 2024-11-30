package com.sss.ums.mapper;

import com.sss.ums.vo.UmsUserVo;

import java.util.List;

public interface UmsUserMapper {

    /**
     * 查询用户信息
     * @param name 匹配 username | nickName
     */
    List<UmsUserVo> queryUmsUserList(String name);

}
