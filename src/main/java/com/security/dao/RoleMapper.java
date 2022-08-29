package com.security.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @BelongsProject: Springboot-Security
 * @BelongsPackage: com.security.dao
 * @Classname RoleMapper.xml
 * @Description TODO
 * @Date 2022/8/29 4:33 PM
 * @Created by wudengming
 * @Version: 1.0
 */
@Mapper
public interface RoleMapper {
    String getAllRoleByMenu(String menu);
}
