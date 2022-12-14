package com.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户实体类，需实现 security 的 UserDetails 接口
 */
public class User implements Serializable, UserDetails {

	private Integer id;
	private String username;
	private String password;
	private Boolean enabled;
	private Boolean locked;
	private List<Role> roles;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取当前用户对象的用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 当前账户是否未过期
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 当前账户密码是否未过期
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 当前账户是否未锁定
	 */
	@Override
	public boolean isAccountNonLocked() {
		return !this.getLocked();
	}

	/**
	 * 当前账户是否可用
	 */
	@Override
	public boolean isEnabled() {
		return this.getEnabled();
	}


	/**
	 * 获取当前用户对象所具有的角色信息
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		System.out.println("----------------> User.getAuthorities()");
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	/**
	 * 获取当前用户对象的密码
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
