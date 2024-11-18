package com.sss.security.mapper;

import com.sss.common.dao.UmsUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsUserDetailMapper {
    UmsUser selectUserByUsername(@Param("username") String username);

    List<String> selectRoleNameByUserId(@Param("userId") Long userId);

    List<String> selectPermissionNameByUserId(@Param("userId") Long userId);
}
