package com.myreport.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回码的枚举类型
 * @author huangping<br />
 * @date 2014-03-11
 */
public enum ReturnCodeEnum {

	/**
	 * 操作成功
	 */
	CODE_OK("操作成功"),
	
	/**
	 * 操作失败
	 */
	CODE_ERROR("操作失败"),
	
	/**
	 * 未知错误
	 */
	UNKNOWN_ERROR("未知错误"),
	
	/**
	 * 验证码错误
	 */
	CHECK_CODE_ERROR("验证码错误"),
	
	/**
	 * 登录失败，用户名或密码错误
	 */
	LOGIN_ERROR("登录失败，用户名或密码错误"),
	
	/**
	 * 用户不存在
	 */
	USER_IS_NOT_EXIST("用户不存在"),
	
	/**
	 * 登录名重复
	 */
	LOGIN_NAME_EXIST("登录名重复"),
	
	/**
	 * 用户名称重复
	 */
	USER_NAME_EXIST("用户名称重复"),
	
	/**
	 * 该用户创建过其他用户，请先删除子用户，再删除该用户
	 */
	USER_HAVE_CHILD_USER("该用户创建过其他用户，请先删除子用户，再删除该用户"),
	
	/**
	 * 该用户创建过角色，请先删除该用户创建过的角色，再删除该用户
	 */
	USER_HAVE_CHILD_ROLE("该用户创建过角色，请先删除该用户创建过的角色，再删除该用户"),
	
	/**
	 * 原始密码错误
	 */
	OLD_PASSWORD_ERROR("原始密码错误"),
	
	/**
	 * 菜单名称重复
	 */
	MENU_NAME_EXISTS("菜单名称重复"),
	
	/**
	 * 菜单有子菜单，请先删除子菜单，再删除该菜单
	 */
	MENU_HAVE_CHILD_MENU("菜单有子菜单，请先删除子菜单，再删除该菜单"),
	
	/**
	 * 该菜单已经有角色绑定，请先解除该菜单与角色绑定，再删除该菜单
	 */
	MENU_HAVE_ROLE_BIND("该菜单已经有角色绑定，请先解除该菜单与角色绑定，再删除该菜单"),
	
	/**
	 * 角色名称重复
	 */
	ROLE_NAME_EXISTS("角色名称重复"),
	
	/**
	 * 角色【{0}】已经被其他用户使用，请先解除用户与角色的绑定，再删除角色
	 */
	ROLE_HAVE_USER_BIND("角色【{0}】已经被其他用户使用，请先解除用户与角色的绑定，再删除角色"),
	
	/**
	 * 你的账号密码已经过期,请更改密码！
	 */
	PASSWORD_TIME_OUT("你的账号密码已经过期,请更改密码！"),
	
	/**
	 * 你所设置的密码使用过于频繁，请重新设置！
	 */
	PASSWORD_USED_FREQUENTLY("你所设置的密码使用过于频繁，请重新设置！"),
	
	/**
	 * 您的密码过于简单，请重新设置！
	 */
	PASSWORD_IS_SIMPLE("您的密码过于简单，请重新设置！"),
	
	/**
	 * 该地区包含子地区！
	 */
	REGION_HAVE_CHILD("该地区包含子地区！"),
	
	/**
	 * 用户组名称重复
	 */
	USER_GROUP_NAME_EXIST("用户组名称重复"),
	
	/**
	 * 用户组【{0}】中有用户绑定。请先删除绑定关系，再删除此用户组
	 */
	USER_GROUP_HAVE_USER_BIND("用户组【{0}】中有用户绑定。请先删除绑定关系，再删除此用户组"),
	
	/**
	 * 用户组【{0}】中有报表组关联，请先删除报表组的关联关系，再删除此用户组
	 */
	USER_GROUP_HAVE_REPORT_GROUP_BIND("用户组【{0}】中有报表组关联，请先删除报表组的关联关系，再删除此用户组"),
	
	/**
	 * 有用户关联此报表组
	 */
	REPORT_GROUP_HAVE_USER_GROUP_BIND("有用户组关联此报表组"),
	
	/**
	 * 有报表关联此报表组
	 */
	REPORT_GROUP_HAVE_REPORT_BIND("有报表关联此报表组"),
	
	/**
	 * 报表组中有子组，不能删除
	 */
	REPORT_GROUP_HAVE_CHILD("报表组中有子组，不能删除"),
	
	/**
	 * 数据库【{0}】中有报表绑定，请先删除报表的绑定，再删除此数据库
	 */
	DATABASE_HAVE_REPORT_BIND("数据库【{0}】中有报表绑定，请先删除报表的绑定，再删除此数据库")
	;
	
	private ReturnCodeEnum(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return this.text;
	}

	/**
	 * 返回该枚举的map格式
	 * @return 返回该枚举的map格式
	 */
	public static Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < values().length; i++) {
			map.put(values()[i].toString(), values()[i].getText());
		}
		return map;
	}
}

