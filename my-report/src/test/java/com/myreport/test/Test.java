package com.myreport.test;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.test.InitXml;

/**
 * @author ping.huang
 * 创建时间 ：2015年6月19日 上午10:58:31
 * 类描述：
 */
public class Test {

	public static void main(String[] args) {
		BeanFactory factory = InitXml.getBeanFactory();
		JdbcTemplate jdbc = factory.getBean(JdbcTemplate.class);
		String sql = "SELECT * FROM sys_user";
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		System.out.println(list.size());
	}
}
