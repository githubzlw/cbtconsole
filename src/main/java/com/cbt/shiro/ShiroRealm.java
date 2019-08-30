package com.cbt.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.cbt.util.SerializeUtil;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.Dao.UserAuthDao;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;
import com.cbt.website.userAuth.impl.UserAuthDaoImpl;

public class ShiroRealm extends AuthorizingRealm {
	private  AdmUserDao admuserDao = new AdmUserDaoImpl();
	private  UserAuthDao userauthDao = new UserAuthDaoImpl();
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
//		1.把AuthenticationToken强转为usernamePasswordToken
		UsernamePasswordToken uPtoken = (UsernamePasswordToken) token;
		//2.从UsernamePasswordToken中获取username
		String username = uPtoken.getUsername();
		//3.调用数据库的方法，从数据库中获取username对应的password
		Admuser admuser = admuserDao.queryForListByName(username);
		
		//4.若用户不存在，则抛出unKnownAccountException
		if(admuser == null) {
			throw new UnknownAccountException("用户不存在");
		}
		//5.根据用户信息情况，决定是否需要抛出其他的AuthenticationException异常
		if(admuser.getId() == 0) {
			throw new LockedAccountException("用户异常");
		}
		System.out.println("从数据库获取username: "+username+" 所对应的用户信息"+admuser.toString());
		//6.根据用户信息，构建AuthenticationInfo对象并返回，通常使用的实现类是SimpleAuthenticationInfo
		//以下信息是从数据库中获取的
		//1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象. 
		Object principal = admuser;
		//2). credentials: 密码. 
		Object credentials = admuser.getPassword();
		//3). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
		String realmName = getName();
		
		//4). 盐值. 
//		ByteSource credentialsSalt = ByteSource.Util.bytes(username);
//		authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
		
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, realmName);
		SecurityUtils.getSubject().getSession().setAttribute("admuser", SerializeUtil.ObjToJson(admuser));
		SecurityUtils.getSubject().getSession().setAttribute("admuser_username", SerializeUtil.ObjToJson(admuser.getAdmName()));
		return authenticationInfo;
	}
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("doGetAuthorizationInfo ");
		//1.从PrincipalCollection中获取登录信息
		Object principal = principals.getPrimaryPrincipal();
		
		//2.利用登录的用户获取当前用户的角色与权限（可能需要查询数据库）
		Set<String> roles = new HashSet<>();
		roles.add("0");
		/*try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//3.创建SimpleAuthorizationInfo，并设置roles
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
		
		//4.返回SimpleAuthorizationInfo对象
		
		return info;
	}

}
