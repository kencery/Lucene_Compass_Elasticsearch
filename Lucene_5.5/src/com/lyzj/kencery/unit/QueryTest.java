package com.lyzj.kencery.unit;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.spans.SpanQuery;
import org.junit.Test;

import com.lyzj.kencery.domain.Article;
import com.lyzj.kencery.util.ArticleDocumentUtils;
import com.lyzj.kencery.util.Configuration;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 1.查询字符串 QueryParserr/MultiFieldQueryParser，可以使用查询语法
 * 2.直接构建Query对象
 * @author kencery
 *
 */
@SuppressWarnings({ "unused", "deprecation" })
public class QueryTest {
	
	/**
	 * 搜索测试，测试查询方式
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {		
		//第一种查询方式
		/*
		 * 1  查看关键字只在title中出现的lucene,，查询条件为："title:lucene"
		 * 2  查看关键字只在content中出现的lucene,，查询条件为："content:lucene"
		 * 3 查看关键字在必须在titile和content中都满足，查询条件为：title:lucene AND content:lucene
		 * 4 查看关键字在只要在titile和content中，查询条件为：title:lucene OR content:lucene
		 */
		//String queryCondition="title:lucene AND content:lucene";
		//--------------------------搜索代码-----------------------------
		//2.1 把查询字符串转为Query对象(只在title中查询)
		//QueryParser queryParser=new MultiFieldQueryParser(new String[]{"title","content"},Configuration.getAnalyzer());
		//Query query=queryParser.parse(queryCondition);
		
		//第二种查询方式    content:lucene
		Query query=new TermQuery(new Term("content","lucene"));
		System.err.println("对应的查询字符串为："+query.toString());
		
		//2.2 执行搜索得到结果
		IndexReader indexReader=DirectoryReader.open(Configuration.getDirectory());
		IndexSearcher indexSearcher=new IndexSearcher(indexReader);
		
		TopDocs topDocs= indexSearcher.search(query,100 );  //返回查询出来的前n条结果
		
		Integer count= topDocs.totalHits; //总结果数量
		ScoreDoc[] scoreDocs=topDocs.scoreDocs;  //返回前N条结果信息
		
		//2.3 处理结果
		List<Article> articles=new ArrayList<Article>();
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

	/**
	 * 范围查询   完
	 */
	@Test
	public void testNumericRangeQuery() {
		Query query=NumericRangeQuery.newIntRange("id", 5, 15, true, false);
		searchAndShowResult(query);
	}
	
	/**
	 * 前缀查询 完
	 */
	@Test
	public void testPrefixQuery(){
		Term term=	new Term("content","这是");
		PrefixQuery query=new PrefixQuery(term);
		searchAndShowResult(query);
	}
	
	/**
	 * 关键词查询
	 */
	@Test
	public void testTermQuery() {
		Query query=new TermQuery(new Term("title","lucene"));
		searchAndShowResult(query);
	}
	
	/**
	 * 通配符查询 完
	 * ？ 标识一个任意字符
	 * * 代表0或多个任意字符
	 * 对应的查询字符串为：title:lu*n?
	 */
	@Test
	public void testWildcardQuery() { 
		Query query=new WildcardQuery(new Term("title","lu*n?")); 
		searchAndShowResult(query);
	}
	
	/**
	 * 查询所有
	 * 对应的查询字符串为：*:*
	 */
	@Test
	public void testMatchAllDocsQuery () {
		Query query=new MatchAllDocsQuery(); 
		searchAndShowResult(query);
	}
	
	/**
	 *  模糊查询 完
	 *  对应的查询字符串为：title:lucenX~1
	 */
	@Test
	public void testFuzzyQuery(){ 
		//最后一个参数为最小相似度,标识有多少的写对了就查询出来
		Query query=new FuzzyQuery(new Term("title","lucenX")); 
		searchAndShowResult(query);
	}
	
	/*
	 * 短语查询 完
	 * 对应的查询字符串为：title:"lucene 框架"~5
	 */
	@Test
	public void testPhraseQuery() { 
		PhraseQuery phraseQuery=new PhraseQuery();
		phraseQuery.add(new Term("title","lucene"));  //第一个位置从0开始
		phraseQuery.add(new Term("title","框架"));  //第一个位置从0开始
		phraseQuery.setSlop(5);  //之间的间隔最大不能超过5个
		searchAndShowResult(phraseQuery);
	}
	
	/**
	 * 布尔查询 完
	 */
	@Test
	public void testBooleanQuery() {
		BooleanQuery booleanQuery=new BooleanQuery();
		//booleanQuery.add(query,Occur.MUST);  //必须满足
		//booleanQuery.add(query,Occur.MUST_NOT);  //非
		//booleanQuery.add(query,Occur.SHOULD);  //多个SHOULD一起用是OR的关系
		
		Query query1=new MatchAllDocsQuery(); 
		Query query2=NumericRangeQuery.newIntRange("id", 5, 15, true, false);
		//第一种
		//对应的查询字符串为：+*:* +id:[5 TO 15}，+*:* AND id:[5 TO 15}
		//booleanQuery.add(query1,Occur.MUST);
		//booleanQuery.add(query2,Occur.MUST);
		
		//第二种
		//对应的查询字符串为：+*:* -id:[5 TO 15}，+*:* NOT id:[5 TO 15}
		booleanQuery.add(query1,Occur.MUST);
		booleanQuery.add(query2,Occur.MUST_NOT);
		
		//第二种
		//对应的查询字符串为：*:* id:[5 TO 15},
		//booleanQuery.add(query1,Occur.SHOULD);
		//booleanQuery.add(query2,Occur.SHOULD);
		searchAndShowResult(booleanQuery);
		
		
	}
	
	/**
	 * 工具方法，用于执行搜索与显示结果的工具类
	 * @param query
	 */
	private void searchAndShowResult(Query query){
		System.err.println("对应的查询字符串为："+query.toString());
		try{
			//2.2 执行搜索得到结果
			IndexReader indexReader=DirectoryReader.open(Configuration.getDirectory());
			IndexSearcher indexSearcher=new IndexSearcher(indexReader);
			
			TopDocs topDocs= indexSearcher.search(query, 100);  //返回查询出来的前n条结果
			
			Integer count= topDocs.totalHits; //总结果数量
			ScoreDoc[] scoreDocs=topDocs.scoreDocs;  //返回前N条结果信息
			
			//2.3 处理结果
			List<Article> articles=new ArrayList<Article>();
			for (int i = 0; i < scoreDocs.length; i++) {
				//System.err.println("得分："+scoreDocs[i].score);
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
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}
