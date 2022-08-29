package com.security.dao;

import com.security.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
	List<Menu> getAllMenus();

    List<String> getAllMenuUrl();

}
