package com.cbt.shiro;

import com.cbt.bean.UserBean;
import com.cbt.processes.service.IUserServer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.io.Serializable;

public class ShiroDbRealm  extends AuthorizingRealm{   
  
	private IUserServer iUserServer;
	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String password=new String(token.getPassword());
		UserBean user = iUserServer.login(token.getUsername(),password);
		if (user != null) {
			return new SimpleAuthenticationInfo(new ShiroUser((long)user.getId(),
						user.getName(), user.getEmail()),
						user.getPass(),getName());
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		//UserBean user = iUserServer.findUserByLoginName(shiroUser.loginName);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//info.addRoles(user.getRoleList());
		return info;
	}

	
	
	public IUserServer getiUserServer() {
		return iUserServer;
	}

	public void setiUserServer(IUserServer iUserServer) {
		this.iUserServer = iUserServer;
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = 1L;
		public Long id;
		public String loginName;
		public String email;

		public ShiroUser(Long id, String loginName, String email) {
			this.id = id;
			this.loginName = loginName;
			this.email = email;
		}

		public String getEmail() {
			return email;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}

		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this, "loginName");
		}

		/**
		 * 重载equals,只比较loginName
		 */
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj, "loginName");
		}
	}

}  