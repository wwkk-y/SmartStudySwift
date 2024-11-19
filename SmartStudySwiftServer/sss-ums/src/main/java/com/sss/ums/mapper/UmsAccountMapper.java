package com.sss.ums.mapper;

import com.sss.common.dao.UmsUser;
import com.sss.ums.vo.AccountRegisterVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UmsAccountMapper {

    @Select("select username, password from ums_user " +
            "where username = #{username} " +
            "and status = 1 " +
            "limit 1")
    UmsUser getUserAccountByUsername(@Param("username") String username);

    @Select("select username, password from ums_user " +
            "where email = #{email} " +
            "and status = 1 " +
            "limit 1")
    UmsUser getUserAccountByEmail(@Param("email") String email);

    @Select("select count(*) from ums_user " +
            "where status = 1 " +
            "and (email = #{email} or username = #{username}) " +
            "limit 1")
    boolean existsAccount(@Param("username") String username, @Param("email") String email);

    void registerAccount(@Param("account") AccountRegisterVo account);
}
