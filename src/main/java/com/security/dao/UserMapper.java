package com.security.dao;

import com.security.entity.Role;
import com.security.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
	User loadUserByUsername(String username);

	List<Role> getUserRolesByUid(Integer id);
}
