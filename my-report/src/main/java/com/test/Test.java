package com.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import com.mytools.utils.SpringContextUtil;
import com.myreport.bean.SysRoleBean;
import com.myreport.dao.SysUserGroupDao;

public class Test {

	static Logger log = Logger.getLogger(Test.class);
	
	public static void main(String[] args) throws Exception {
		//System.out.println(Integer.MAX_VALUE);
		BeanFactory factory = InitXml.getBeanFactory();
		SysUserGroupDao dao = factory.getBean(SysUserGroupDao.class);
		System.out.println("dao= " + dao);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleName", "name1");
		map.put("roleId", 1);
		SpringContextUtil.loadBean("sysUserDao", SysRoleBean.class, map);
		SysRoleBean role = factory.getBean("sysUserDao1", SysRoleBean.class);
		System.out.println("db= " + role);
		System.out.println(role.getRoleName());
	}
	
	
}
