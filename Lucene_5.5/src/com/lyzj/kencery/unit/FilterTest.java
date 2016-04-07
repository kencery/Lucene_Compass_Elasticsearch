package com.lyzj.kencery.unit;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.lyzj.kencery.domain.Article;
import com.lyzj.kencery.util.ArticleDocumentUtils;
import com.lyzj.kencery.util.Configuration;

/**
 * 过滤
 * @author kencery
 *
 */
public class FilterTest {
	
	/**
	 * 过滤，查询Id为0-15的所有数据
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {
		//1 搜索条件
		String queryCondition="全文检索";
		
		//2 执行搜索(lucene)
		List<Article> articles=new ArrayList<Article>();
		//--------------------------搜索代码-----------------------------
		//2.1 把查询字符串转为Query对象(只在title中查询)
		QueryParser queryParser=new MultiFieldQueryParser(new String[]{"title","content"},Configuration.getAnalyzer());
		Query query=queryParser.parse(queryCondition);
		
		//2.2 执行搜索得到结果
		IndexReader indexReader=DirectoryReader.open(Configuration.getDirectory());
		IndexSearcher indexSearcher=new IndexSearcher(indexReader);
	
		//---------------------过滤lucene4.*以前------------------------
		//1.域   2.起始位置  3.结束位置   4.是否包含起始位置    5.是否包含结束位置 
		/*
		 * 在这里因为索引存放的时候存放的是字符串，而我们这里传递的是数字，所以查询结果会为0，那么如何实现这个功能呢？
		 * lucene4.x以前提供了工具类进行转换(在创建索引的时候使用)，
		 * 	lucene4.x以前
		 * 		String s=NumericUtils.intToPrefixCoded(100);
		 * 		int i=NumericUtils.prefixCodedToInt(s)。
		 * lucene4.x以后直接使用下面语句创建索引即可
		 *	document.add(new IntField("id", article.getId(),Store.YES));
		 *
		 */
		Filter filter=NumericRangeFilter.newIntRange("id", 0, 15, false, true);
		
		//------------------------------------------------
		
		TopDocs topDocs= indexSearcher.search(query,filter,100);  //返回查询出来的前n条结果
		
		Integer count= topDocs.totalHits; //总结果数量
		ScoreDoc[] scoreDocs=topDocs.scoreDocs;  //返回前N条结果信息
		
		//2.3 处理结果
		for (int i = 0; i < scoreDocs.length; i++) {
			System.err.println("得分："+scoreDocs[i].score);
			//根据内部编号取出真正的Document数据
			Document doc=indexSearcher.doc(scoreDocs[i].doc);
			//将document转化为Article
			Article article=ArticleDocumentUtils.documentToArticle(doc);
			articles.add(article);			
		}
		//------------------------------------------------------------
		
		//3  控制台显示结果
		System.err.println("总结果数："+count);
		for (Article article : articles) {
			System.out.println("查询结果为："+article);
		}
		indexSearcher.getIndexReader().close();
	}
}
