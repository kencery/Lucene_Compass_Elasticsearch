package com.lyzj.kencery.util;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.BytesRef;

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
		/*
		 * 测试方法为：SortTest下的testSearch
		 * 使用一个NumericDocValuesField索引专用字段用来保存需要索引的字段,然后在搜索的时候使用，
		 * 使其可以按照我们想要的字段进行排序	
		*/
		document.add(new NumericDocValuesField("id", article.getId()));
		
		/*document.add(new TextField("id", article.getId().toString(),Store.YES));
		document.add(new IntField("id",article.getId()),FieldType.NumericType.INT));*/
		 /**
		  * lucene3.x以前，使用
		  * document.setBoost(2.0f)添加权重，
         * 在Lucene4.x以后，只能给域加权，不能给文档加权，如果要提高文档的加权，需要给
         * 文档的每个域进行加权
         * TextField contentField = new TextField("content", article.getContent(), Store.YES);
		 * contentField.setBoost(2.0f);
		 * 
		 * StringField设置权重会出现错误，错误原因为：
		 * 	StringField contentField = new StringField("content", article.getContent(), Store.YES);
		 * 不能在 omitNorms的字段上 setBoost，而 Lucene 4.x 的 StringField, StoredField, IntField 等都是 omitNorms 的，
		 * 我测试发现只有 TextField 是非 omitNorms 的，故可以在 TextFields 上 setBoost。
		 * 备注：现在有个新的问题，给其中一个字段设置了 boost，但如果条件里没有这个字段，顺序就没任何变化。而 4.x 的 Document 上并没有 setBoost 方法。
		 * 刚实践了一个好办法，添加了一个 TextField 且所有索引记录的这个字段的值都是一个相同的字，然后在这个字段上 setBoost ，在查询条件上默认的加上这个字段的查询，关系是 SHOULD，就能实现按打分+相关度共同排序了。
		 * 
         * **/
		TextField titleField = new TextField("title", article.getTitle(), Store.YES);
		titleField.setBoost(3.0f);
		document.add(titleField);
		
		TextField contentField = new TextField("content", article.getContent(), Store.YES);
		contentField.setBoost(3.0f);
		document.add(contentField);
		
		/**
		 * 将content字段设置为可以排序的字段
		 */
		document.add(new SortedDocValuesField("content", new BytesRef(article.getContent()))); 
		
	/*	document.add(new TextField("title",article.getTitle(),Store.YES));
		document.add(new TextField("content",article.getContent(),Store.YES));*/

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
