package com.lyzj.compass.util;

import org.compass.core.Compass;
import org.compass.core.CompassSession;
import org.compass.core.config.CompassConfiguration;

/**
 * 维护Compass的全局资源
 * @author kencery
 *
 */
public class CompassUtil {
		
	private static Compass compassSessionFactory;
	
	/**
	 * 静态方法，初始化
	 */
	static{
		compassSessionFactory=new CompassConfiguration().configure()
				.buildCompass();
	}

	/**
	 * 获取全局唯一的Compass对象
	 * @return
	 */
	public static Compass getCompassSessionFactory() {
		return compassSessionFactory;
	}
	
	/**
	 * 开启索引
	 * @return
	 */
	public static CompassSession openSession(){
		return compassSessionFactory.openSession();
	}
	
}

