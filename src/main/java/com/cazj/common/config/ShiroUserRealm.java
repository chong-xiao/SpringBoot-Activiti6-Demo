package com.cazj.common.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cazj.common.util.ShiroUtils;
import com.cazj.dao.EmpDao;
import com.cazj.dao.EmpRoleDao;
import com.cazj.dao.MenuDao;
import com.cazj.dao.RoleMenuDao;
import com.cazj.pojo.Emp;

@Service
public class ShiroUserRealm extends AuthorizingRealm{

	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		//创建凭证匹配对象
		HashedCredentialsMatcher cMatcher = new HashedCredentialsMatcher();
		//设置加密算法
		cMatcher.setHashAlgorithmName("MD5");
		//设置加密次数
		cMatcher.setHashIterations(1);
		super.setCredentialsMatcher(cMatcher);
	}
	@Autowired
	private EmpRoleDao empRoleDao;
	@Autowired
	private RoleMenuDao roleMenuDao;
	@Autowired
	private MenuDao menuDao;
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//1.获取登陆用户信息
		Emp emp = (Emp)principals.getPrimaryPrincipal();
		Integer empId = emp.getId();
//		Emp emp2 = ShiroUtils.getEmp();
//		Integer id2 = emp2.getId();
		//2.基于员工id获取员工拥有的角色
		List<Integer> roleIds = empRoleDao.findRoleIdsByEmpId(empId);
		if(roleIds==null||roleIds.size()==0)
			throw new AuthorizationException("没有权限");
		//3.基于角色id获取菜单id
		Integer[] array= {};
		List<Integer> menuIds = roleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(array));
		if(menuIds==null||menuIds.size()==0)
			throw new AuthenticationException("没有权限");
		//4.基于菜单id获取权限标识
		List<String> permissions = menuDao.findPermissions(menuIds.toArray(array));
		//5.对权限标识进行封装并返回
		Set<String> set=new HashSet<>();
		for (String per : permissions) {
			if(!StringUtils.isEmpty(per)){
	    		set.add(per);
	    	}
		}
		SimpleAuthorizationInfo info= new SimpleAuthorizationInfo();
		info.setStringPermissions(set);
		return info;
	}
	@Autowired
	EmpDao empDao;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.获取用户名
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		//2.基于用户名查询用户信息
		Emp emp = empDao.findEmpByUserName(username);
		//3.判断用户是否存在
		if(username == null) throw new UnknownAccountException();
		//4.判断用户是否禁用
		if(emp.getValid() == null || emp.getValid() == 0) throw new LockedAccountException();
		//5.封装用户信息
		ByteSource credentialsSalt = ByteSource.Util.bytes(emp.getSalt());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(emp, emp.getPassword(), credentialsSalt, this.getName());
		//6.返回封装结果
		return info;
	}

}
