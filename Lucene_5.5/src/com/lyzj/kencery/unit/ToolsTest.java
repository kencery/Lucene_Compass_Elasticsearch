package com.lyzj.kencery.unit;

import java.util.Date;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.junit.Test;

/**
 * 缓存filter的时候，数字应该怎么做
 * 滤，解决数字和字符串过滤的问题
 * @author kencery
 *
 */
public class ToolsTest {

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFilter() throws Exception {
		 
		/**
		 * 操作数字的工具类
		 * 	lucene4.x以前
		 * 		String s=NumericUtils.intToPrefixCoded(100);
		 * 		int i=NumericUtils.prefixCodedToInt(s)
		 * 
		 */
		
         /**
          * 操作日期的工具类
          */
		 String s1=	DateTools.dateToString(new Date(), Resolution.DAY);
		 String s2=	DateTools.dateToString(new Date(), Resolution.SECOND);
		 
		 Date d1= DateTools.stringToDate(s1);
		 Date d2= DateTools.stringToDate(s2);
		 
		 System.err.println(s1);
		 System.err.println(s2);
		 System.err.println(d1);
		 System.err.println(d2);
	}
}
