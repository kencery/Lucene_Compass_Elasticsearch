package com.lyzj.compass.dao;

import java.util.ArrayList;
import java.util.List;

import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;

import com.lyzj.compass.domain.Article;
import com.lyzj.compass.domain.SearchResult;
import com.lyzj.compass.util.CompassUtil;


/**
 * Compass对索引完整的增删改查
 * @author kencery
 *
 */
public class ArticleDao {
	
	/**
	 * 添加索引
	 * @param article	添加的实体
	 */
	public void save(Article article){
		CompassSession session=CompassUtil.openSession();		
		CompassTransaction tx=null;
		try{
			tx=session.beginTransaction();
			session.create(article);
			tx.commit();
		}finally{
			session.close();
		}
	}
	
	/**
	 * 删除索引
	 * @param id	删除的Id
	 */
	public void delete(Integer id){
		CompassSession session=CompassUtil.openSession();		
		CompassTransaction tx=null;
		try{
			tx=session.beginTransaction();
			session.delete(Article.class,id);
			tx.commit();
		}finally{
			session.close();
		}
	}
	
	/**
	 * 更新索引
	 * @param article	修改的实体
	 */
	public void update(Article article){
		CompassSession session=CompassUtil.openSession();		
		CompassTransaction tx=null;
		try{
			tx=session.beginTransaction();
			session.save(article);
			tx.commit();
		}finally{
			session.close();
		}
	}
	
	/**
	 * 查询(分页查询)   demo 假设公有25条数据，每页显示10条，则共3页
	 * @param querySearch	查询条件
	 * @param firstResult	从结果列表中的那个索引开始读取数据
	 * @param maxResult	最多读取多少条数据
	 * @return	返回一段数据+总结果数量
	 */
	public SearchResult<Article> search(String querySearch,int firstResult,int maxResult){
		CompassSession session=CompassUtil.openSession();
		CompassTransaction tx=null;
		
		try{
			tx=session.beginTransaction();
			
			// start查询
			CompassHits hits= session.find(querySearch);
			int count=hits.length();    //总结果数量
			
			List<Article> articles=new ArrayList<Article>();
			
			int endIndex=Math.min(firstResult+maxResult,count);
			
			for (int i = firstResult; i < endIndex; i++) {   //只取一段数据
				Article article=(Article) hits.data(i);
				articles.add(article);
			}
	
			// end  结束
			tx.commit();
			
			return new SearchResult<Article>(count, articles);
		}finally{
			session.close();
		}
	}
}