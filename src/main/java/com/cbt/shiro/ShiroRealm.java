package com.cbt.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;

public class ShiroRealm extends AuthenticatingRealm {
	private static AdmUserDao admuserDao = new AdmUserDaoImpl();
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
//		1.把AuthenticationToken强转为usernamePasswordToken
		UsernamePasswordToken uPtoken = (UsernamePasswordToken) token;
		//2.从UsernamePasswordToken中获取username
		String username = uPtoken.getUsername();
		//3.调用数据库的方法，从数据库中获取username对应的password
		Admuser queryForListByName = admuserDao.queryForListByName(username);
		
		//4.若用户不存在，则抛出unKnownAccountException
		if(queryForListByName == null) {
			throw new UnknownAccountException("用户不存在");
		}
		//5.根据用户信息情况，决定是否需要抛出其他的AuthenticationException异常
		if(queryForListByName.getId() == 0) {
			throw new LockedAccountException("用户异常");
		}
		System.out.println("从数据库获取username: "+username+" 所对应的用户信息"+queryForListByName.toString());
		//6.根据用户信息，构建AuthenticationInfo对象并返回，通常使用的实现类是SimpleAuthenticationInfo
		//以下信息是从数据库中获取的
		//1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象. 
		Object principal = queryForListByName.getAdmName();
		//2). credentials: 密码. 
		Object credentials = queryForListByName.getPassword();
		//3). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
		String realmName = getName();
		
		//4). 盐值. 
//		ByteSource credentialsSalt = ByteSource.Util.bytes(username);
//		authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
		
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, realmName);
		return authenticationInfo;
	}

}
