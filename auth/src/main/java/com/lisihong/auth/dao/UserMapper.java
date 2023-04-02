package com.lisihong.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    Integer getIdByAuthInfo(@Param("username") String username
            , @Param("password") String password);
}
