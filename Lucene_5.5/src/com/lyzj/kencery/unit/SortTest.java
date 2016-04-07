package com.lyzj.kencery.unit;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.lyzj.kencery.domain.Article;
import com.lyzj.kencery.util.ArticleDocumentUtils;
import com.lyzj.kencery.util.Configuration;
import com.lyzj.kencery.util.LuceneUtils;

/**
 * 1.相关度排序
		根据搜索条件与文本实时计算出来的。
   2.按制定的字段排序
		Sort对象
		
	备注：lucene自定义评分没搞定
 * @author kencery
 *
 */
public class SortTest {
	
	/**
	 * 吐槽lucene网址：http://ju.outofmemory.cn/entry/217369
	 * 测试权重信息(给需要添加的值信息设置高权重)
	 * 使用备注等都在ArticleDocumentUtils类下的articleToDocument中含有说明
	 * @throws Exception
	 */
	@Test
	public void testSave1() throws Exception {
		Article article=new Article(27, "Lucene是全文检索框架", 
				"全文检索（Full-Text Retrieval）是指以文本作为检索对象，找出含有指定词汇的文本。全面、准确和快速是衡量全文检索系统的关键。");
		//创建索引
		Document document=ArticleDocumentUtils.articleToDocument(article);
		
		LuceneUtils.getIndexWriter().addDocument(document);
		
	}
	
	/**
	 * 测试排序，按照指定的字段进行排序
	 * 案例，按照Id排序
	 * TopDocs topDocs= indexSearcher.search(query,filter,n,sort,doDocScores,doMaxScore);
	 * 1.query参数将查询字符串转换为query对象，传入     2.filter用来再次过滤结果，   3.n表示只返回Top N
	 * 4.sort表示排序对象   5.doDocScores表示是否对文档进行相关性打分，如果你设为false,那你索引文档的score值就是NAN,,默认为true
	 * 5.doMaxScore举例说明：假如你有两个Query(QueryA和QueryB)，两个条件是通过BooleanQuery连接起来的，
	 * 	假如QueryA条件匹配到某个索引文档，而QueryB条件也同样匹配到该文档，如果doMaxScore设为true,
	 * 	表示该文档的评分计算规则为取两个Query(当然你可能会有N个Query链接，那就是N个Query中取最大值)之中的最大值，
	 * 	否则就是取两个Query查询评分的相加求和。默认为false.
	 * 备注说明：在Lucene4.x时代，doDocScores和doMaxScore这两个参数可以通过indexSearcher类来设置，如：
	 * 		searcher.setDefaultFieldSortScoring(true, false); 
	 * 	而在Lucene5.x时代，你只能在调用search方法时传入这两个参数，如：
	 * 		TopDocs topDocs= indexSearcher.search(query,filter,n,sort,doDocScores,doMaxScore);
	 * @throws Exception
	 * 
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
		
		
		//作用排序功能，按照Id进行倒序顺序排序
		//------------------------------------------------- 
		//表示按照评分排序，降序排列
		//Sort sort=Sort.RELEVANCE;
		
		/*表示按文档索引排序，什么叫按文档索引排序？意思是按照索引文档的docId排序，我们在创建索引文档的时候，
		Lucene默认会帮我们自动加一个Field(docId),如果你没有修改默认的排序行为，
		默认是先按照索引文档相关性评分降序排序(如果你开启了对索引文档打分功能的话)，然后如果两个文档评分相同，再按照索引文档id升序排列。*/
		//Sort sort=Sort.INDEXORDER;  
		
		/* 如果在将实体转换为Document的时候没有调用下面的这段话，则会报错，报错内容为：
		 * java.lang.IllegalStateException: unexpected docvalues type NONE for field 'id' (expected=SORTED). Use UninvertingReader or index with docvalues.
		 * 	document.add(new NumericDocValuesField("id", article.getId()));
		 */
		//Sort sort=new Sort(new SortField("id", SortField.Type.INT,true));//按照Id降序排序
		
		//报错，怎么对content进行排序呢？因为其是字符串，所以如何排序
		//Sort sort=new Sort(new SortField("content", SortField.Type.STRING,true));
		
		/*
		 * 表示先按照content字符串升序排列， 再按照评分降序排列，最后按照Id降序排列，完美实现
		 * 一句话，域的排序顺序跟你StoreField定义的先后顺序保持一致。注意Sort的默认排序行为。
		 */
		Sort sort=new Sort(new SortField("content",Type.STRING),SortField.FIELD_SCORE,new SortField("id",Type.INT,true));
		
		/*
		 * 首先按照id进行降序排列，然后在按照评分降序排列
		 * 已经测试，没问题
		 */
		/*Sort sort=new Sort(new SortField[] {
                new SortField("id", Type.INT,true), SortField.FIELD_SCORE });*/
		//-------------------------------------------------
		
		
		TopDocs topDocs= indexSearcher.search(query, 100,sort,true,false);  //返回查询出来的前n条结果
		
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
