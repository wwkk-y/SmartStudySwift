package com.sss.security.mapper;

import com.sss.common.dao.UmsLoginLog;
import com.sss.security.dao.UmsUserDao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UmsUserDetailMapper {
    UmsUserDao selectUserByUsername(@Param("username") String username);

    List<String> selectRoleNameByUserId(@Param("userId") Long userId);

    List<String> selectPermissionNameByUserId(@Param("userId") Long userId);

    @Update("update ums_user set login_time = CURRENT_TIMESTAMP " +
            "where status = 1 " +
            "and username = #{username} " +
            "limit 1")
    void flushAccountLoginTime(@Param("username") String username);

    @Select("select * from ums_login_log " +
            "where username = #{username} and token = #{token}")
    UmsLoginLog getLoginLog(@Param("username") String username, @Param("token") String token);

    @Insert("insert into ums_login_log(username, token) values (#{username}, #{token})")
    void addLoginLog(String username, String token);

    @Update("update ums_login_log set logout=true " +
            "where username=#{username} limit 1")
    void loginLogLogout(String username);
}
