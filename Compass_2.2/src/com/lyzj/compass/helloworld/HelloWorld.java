package com.lyzj.compass.helloworld;

import java.util.ArrayList;
import java.util.List;

import org.compass.core.Compass;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;
import org.compass.core.config.CompassConfiguration;
import org.junit.Test;

import com.lyzj.compass.domain.Article;

/**
 * Compass_HelloWorld测试数据
 * @author kencery
 *
 */
public class HelloWorld {
	
	private CompassConfiguration ccfg=new CompassConfiguration().configure(); //读取默认配置文件
	private Compass compass=ccfg.buildCompass();
		
	/**
	 * 为文章建立索引
	 * @throws IOException 
	 */
	@Test
	public void testCreateIndex() throws Exception {
		//1  将需要添加的实体构造成实体对象
		Article article=new Article(1, "Lucene是全文检索框架", 
				"全文检索（Full-Text Retrieval）是指以文本作为检索对象，找出含有指定词汇的文本。" +
				"全面、准确和快速是衡量全文检索系统的关键指标。");
		
		//3.建立索引(Compass)
		CompassSession session=compass.openSession();
		CompassTransaction ctx=session.beginTransaction();
			
		//建立索引
		session.create(article);
		
		ctx.commit();
		session.close();
	}
	
	
	/**
	 * 测试搜索
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {
		//1 搜索条件
		String queryCondition="lucene";
		
		//2 执行搜索（Compass）
		CompassSession session=compass.openSession();
		CompassTransaction ctx=session.beginTransaction();
			
		//建立索引
		CompassHits hits= session.find(queryCondition);
		int count=hits.length();  //总结果数量
		//3  控制台显示结果
		List<Article> articles=new ArrayList<Article>();
		for (int i = 0; i < hits.length(); i++) {
			System.out.println("得分是："+hits.score(i));
			Article article=(Article) hits.data(i);
			articles.add(article);
		}
	
		ctx.commit();
		session.close();
		
		System.out.println("总结果数："+count);
		for (Article article : articles) {
			System.out.println("查询结果为："+article);
		}
	}
}