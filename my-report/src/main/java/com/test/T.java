package com.test;

import org.springframework.beans.factory.BeanFactory;

import com.myreport.dao.ReportInfoDao;

public class T {

	static BeanFactory factory = InitXml.getBeanFactory();
	static ReportInfoDao dao = factory.getBean(ReportInfoDao.class);
	
	public static void main(String[] args) {
	}
	
}
