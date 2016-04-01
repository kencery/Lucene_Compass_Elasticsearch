package com.lyzj.kencery.util;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;

import com.lyzj.kencery.domain.Article;

/**
 * 辅助类，封装Lucene
 * @author kencery
 *
 */
public class ArticleDocumentUtils {
	
	/**
	 * 将实体类对象转换为Document
	 * @param article 实体类对象
	 * @return	返回Document对象
	 */
	public static Document articleToDocument(Article article){
		Document document=new Document();
		
		document.add(new TextField("id", article.getId().toString(),Store.YES));
		document.add(new TextField("title",article.getTitle(),Store.YES));
		document.add(new TextField("content",article.getContent(),Store.YES));
		return document;
	}
	
	/**
	 * 将Document对象转换为实体类
	 * @param document Document对象
	 * @return 返回Article对象
	 */
	public static Article documentToArticle(Document document){
		Article article=new Article(Integer.parseInt(document.get("id")), 
				document.get("title"),
				document.get("content"));
		return article;
	}	
}
