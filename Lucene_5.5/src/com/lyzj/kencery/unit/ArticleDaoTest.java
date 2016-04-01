package com.lyzj.kencery.unit;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lyzj.kencery.dao.ArticleDao;
import com.lyzj.kencery.domain.Article;
import com.lyzj.kencery.domain.SearchResult;

/**
 * 测试ArticleDao实现
 * @author kencery
 *
 */
public class ArticleDaoTest {

	private ArticleDao articleDao=new ArticleDao();
	
	/**
	 * 测试保存索引
	 */
	@Test
	public void testSave1() {
		Article article=new Article(1, "Lucene是全文检索框架", 
				"全文检索（Full-Text Retrieval）是指以文本作为检索对象，找出含有指定词汇的文本。全面、准确和快速是衡量全文检索系统的关键指标。");
		articleDao.save(article);
	}

	/**
	 * 测试保存索引,循环录入25条记录
	 */
	@Test
	public void testSave2() {
		for (int i = 1; i <= 25; i++) {
			Article article=new Article(i, "Lucene是全文检索框架", 
					"全文检索（Full-Text Retrieval）是指以文本作为检索对象，找出含有指定词汇的文本。全面、准确和快速是衡量全文检索系统的关键指标。");
			articleDao.save(article);
		}
	}
	
	/**
	 * 测试删除索引
	 */
	@Test
	public void testDelete() {
		articleDao.delete(1);
	}

	/**
	 * 测试更新索引
	 */
	@Test
	public void testUpdate() {
		Article article=new Article(1, "Lucene是全文检索框架", 
				"这是更新后的索引信息，全文检索（Full-Text Retrieval）是指以文本作为检索对象，找出含有指定词汇的文本。全面、准确和快速是衡量全文检索系统的关键指标。");
		articleDao.update(article);
	}

	/**
	 * 测试查询
	 */
	@Test
	public void testSearchStringInt() {
		String query="lucene";
		
		SearchResult<Article> searchResult=articleDao.search(query,1000);
		
		System.err.println("总结果数："+searchResult.getCount());
		for (Article article : searchResult.getList()) {
			System.out.println("查询结果为："+article);
		}
	}

	/**
	 * 测试查询
	 */
	@Test
	public void testSearchStringIntInt() {	
		String query="lucene";
		//分页测试查询
		SearchResult<Article> searchResult=articleDao.search(query, 20, 10);
		
		System.err.println("总结果数："+searchResult.getCount());
		for (Article article : searchResult.getList()) {
			System.out.println("查询结果为："+article);
		}
	}
}
