package com.test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author huangping 2012-11-29
 */
public class InitXml {

	public static BeanFactory getBeanFactory() {
		return new FileSystemXmlApplicationContext("src/main/resources/config/*.xml");
	}
}
