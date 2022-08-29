package com.security.service.iml;

import com.alibaba.fastjson.JSON;
import com.security.dao.UserMapper;
import com.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户接口，需实现 security 的 UserDetailsService 接口
 */
@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * UserDetailsService接口中的实现方法（用户登陆时自动调用）
	 *
	 * @param username 用户登录时输入的用户名
	 *                 通过用户名去数据库中查找，如果没有查到，就抛出一个用户不存在的异常；
	 *                 否则，继续查找该用户所具备的角色信息，并将获取到的user对象返回，再由系统提供的DaoAuthenticationProvider类对比密码是否正确
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.loadUserByUsername(username);
		if (null == user) {
			throw new UsernameNotFoundException("账户不存在！");
		}
		user.setRoles(userMapper.getUserRolesByUid(user.getId()));
		return user;
	}
}
