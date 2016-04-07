package com.lyzj.kencery.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.lucene.util.NumericUtils;

import com.lyzj.kencery.domain.Article;
import com.lyzj.kencery.domain.SearchResult;
import com.lyzj.kencery.util.ArticleDocumentUtils;
import com.lyzj.kencery.util.Configuration;
import com.lyzj.kencery.util.LuceneUtils;

/**
 * lucene对索引完整的增删改查
 * @author kencery
 *
 */
public class ArticleDao {
	
	/**
	 * 添加索引
	 * @param article	添加的实体
	 */
	public void save(Article article){
		//1 把Article转为Document
		Document document=ArticleDocumentUtils.articleToDocument(article);
		//2 保存到索引库中
		try {
			LuceneUtils.getIndexWriter().addDocument(document);
			LuceneUtils.getIndexWriter().commit();
			LuceneUtils.indexChanged();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 删除索引
	 * @param id	删除的Id
	 */
	public void delete(Integer id){
		try {
			//Term就是指定字段中的一个关键词
			BytesRefBuilder bytes = new BytesRefBuilder();
            NumericUtils.intToPrefixCoded(id, 0, bytes);
			Term term=new Term("id",bytes);
			//删除含有指定Term的所有Document
			LuceneUtils.getIndexWriter().deleteDocuments(term);
			LuceneUtils.getIndexWriter().commit();
			LuceneUtils.indexChanged();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 更新索引
	 * @param article	修改的实体
	 */
	public void update(Article article){
		//1 把Article转为Document
		Document document=ArticleDocumentUtils.articleToDocument(article);
		
		//2 修改到索引库中
		try {
			BytesRefBuilder bytes = new BytesRefBuilder();
            NumericUtils.intToPrefixCoded(article.getId(), 0, bytes);
			//Term就是指定字段中的一个关键词
			Term term=new Term("id",bytes);
			//更新索引就是先删除，在创建
			LuceneUtils.getIndexWriter().updateDocument(term, document);
			LuceneUtils.getIndexWriter().commit();
			LuceneUtils.indexChanged();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 *  查询
	 * @param querySearch 查询条件
	 * @param maxResult  查询多少条数据
	 * @return 返回一段数据+总结果数量
	 */
	public SearchResult<Article> search(String	querySearch,int maxResult){
		try {
			//1 把查询字符串转换为Query对象
			//  >>默认只在"title"中搜索
			//QueryParser queryParser=new QueryParser("title",Configuration.getAnalyzer());
			//  >>希望能在"title"和"content"中搜索
			QueryParser queryParser = new MultiFieldQueryParser(new String[] {
					"title", "content" }, Configuration.getAnalyzer());
			Query query = queryParser.parse(querySearch);
			IndexSearcher indexSearcher=LuceneUtils.getIndexSearcher();
			TopDocs topDocs = indexSearcher.search(query, maxResult);
			int count = topDocs.totalHits;
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//3 处理结果
			List<Article> list = new ArrayList<Article>();
			for (int i = 0; i < scoreDocs.length; i++) { //一段数据
				//根据编号取出正真的Document数据，
				Document document = indexSearcher.doc(scoreDocs[i].doc);
				//把Document数据
				Article article = ArticleDocumentUtils
						.documentToArticle(document);
				list.add(article);
			}
			return new SearchResult<Article>(count, list);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询(分页查询)   demo 假设公有25条数据，每页显示10条，则共3页
	 * @param querySearch	查询条件
	 * @param firstResult	从结果列表中的那个索引开始读取数据
	 * @param maxResult	最多读取多少条数据
	 * @return	返回一段数据+总结果数量
	 */
	public SearchResult<Article> search(String	querySearch,int firstResult,int maxResult){
		try {
			//1 把查询字符串转换为Query对象
			//  >>希望能在"title"和"content"中搜索
			QueryParser queryParser = new MultiFieldQueryParser(new String[] {
					"title", "content" }, Configuration.getAnalyzer());
			Query query = queryParser.parse(querySearch);
			//2 执行查询得到中间结果
			IndexSearcher indexSearcher=LuceneUtils.getIndexSearcher();
			
			TopDocs topDocs = indexSearcher.search(query, firstResult+maxResult);
			int count = topDocs.totalHits;
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//3 处理结果
			List<Article> list = new ArrayList<Article>();
			int endIndex=Math.min(firstResult+maxResult, scoreDocs.length);
			for (int i = firstResult; i < endIndex; i++) { //一段数据
				//根据编号取出正真的Document数据，
				Document document = indexSearcher.doc(scoreDocs[i].doc);
				//把Document数据
				Article article = ArticleDocumentUtils
						.documentToArticle(document);
				list.add(article);
			}
			return new SearchResult<Article>(count, list);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}