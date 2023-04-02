package com.lisihong.auth.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    List<Integer> getRoleIdsByUserId(Integer id);
}
