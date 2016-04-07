package com.lyzj.kencery.unit;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.junit.Test;

import com.lyzj.kencery.domain.Article;
import com.lyzj.kencery.util.ArticleDocumentUtils;
import com.lyzj.kencery.util.Configuration;

/**
 * 高亮的测试
 * @author kencery
 *
 */
public class HighlightTest {

	/**
	 * 高亮显示
	 * @throws Exception
	 */
	@Test
	public void testHighlight() throws Exception {
		
	}
	
	/**
	 * 高亮显示
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
		TopDocs topDocs= indexSearcher.search(query, 100); //返回查询出来的前n条结果
		
		Integer count= topDocs.totalHits; //总结果数量
		ScoreDoc[] scoreDocs=topDocs.scoreDocs;  //返回前N条结果信息
		
		//---------------------------------------------
		//1.创建并配置高亮器
		//---------------------------------------------
		//指定配置前缀和后缀,默认为<b></b>
		int fragmentSize=100;
		Formatter formatter=new SimpleHTMLFormatter("<font color='red'>", "</font>");
		//
		Scorer fragmentScorer=new QueryScorer(query);
		Highlighter highlighter=new Highlighter(formatter, fragmentScorer);
		//制定摘要大小，默认100个字符
		highlighter.setTextFragmenter(new SimpleFragmenter(fragmentSize));
		//把全部分本进行高亮
		//highlighter.setTextFragmenter(new NullFragmenter());
		//2.3 处理结果
		for (int i = 0; i < scoreDocs.length; i++) {
			//根据内部编号取出真正的Document数据
			Document doc=indexSearcher.doc(scoreDocs[i].doc);
			
			//---------------------------------------------
			//2.做高亮操作，一次高亮一段文本(字段值),如果当前高亮的字段中没有出现关键字，则返回null,
			TokenStream tokenStream = Configuration.getAnalyzer().tokenStream("",new StringReader(doc.get("content")));  
			String str = highlighter.getBestFragment(tokenStream, doc.get("content"));  
			System.out.println(str);
			
			//---------------------------------------------
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
	}
	
	
}