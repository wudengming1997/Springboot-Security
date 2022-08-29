package com.security.service.iml;

import com.security.dao.MenuMapper;
import com.security.dao.RoleMapper;
import com.security.service.RoleService;
import com.security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @BelongsProject: Springboot-Security
 * @BelongsPackage: com.security.service.iml
 * @Classname MenuRoleServiceImpl
 * @Description TODO
 * @Date 2022/8/29 4:13 PM
 * @Created by wudengming
 * @Version: 1.0
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean addAllMenuRoleToRedis() {
        boolean flag = true;
        List<String> menuList = menuMapper.getAllMenuUrl();
        if (menuList != null) {
            for (String menu : menuList) {
                String roles = roleMapper.getAllRoleByMenu(menu);
                boolean set = redisUtil.set(menu, roles);
                if (!set){
                    return !flag;
                }
            }
        }
        return flag;
    }
}
