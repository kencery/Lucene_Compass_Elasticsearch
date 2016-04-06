package com.lyzj.kencery.helloworld;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.lyzj.kencery.domain.Article;

/**
 * HelloWorld测试数据
 * @author kencery
 *
 */
public class HelloWorld {
	
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
		
		//2 保存到数据库(此步骤暂时省略)
		
		//3 建立索引(lucene)
		Directory directory=FSDirectory.open(Paths.get("./indexDir/"));  //索引库目录
		Analyzer analyzer=new IKAnalyzer();		//分词器
		IndexWriterConfig iwc= new IndexWriterConfig(analyzer);
		
		// >>3.1   将Article转为Document
		/** Store参数说明
			No 本字段的原始值不存储
			YES 本字段的原始值会存在出在数据库区中
		如果不存在出，则搜索出来的结果中这个字段的值为null */
		
		/** 
	     * 自Lucene4开始 创建field对象使用不同的类型 只需要指定是否需要保存源数据 不需指定分词类别  
	     * 之前版本的写法如下  
	     * doc.Add(new Field("id", article.id.ToString(), Field.Store.YES, Field.Index.ANALYZED)); 
	     */
		Document doc=new Document();
		doc.add(new TextField("id", article.getId().toString(), Store.YES));
		doc.add(new TextField("title", article.getTitle(), Store.YES));
		doc.add(new TextField("content", article.getContent(), Store.YES));
		
		// >>3.2 保存到索引库中
		IndexWriter indexWriter=new IndexWriter(directory,iwc);
		indexWriter.addDocument(doc);
		indexWriter.close();  //释放资源
	}
	
	/**
	 * 测试搜索
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {
		//1 搜索条件
		String queryCondition="lucene";
		
		//2 执行搜索(lucene)
		List<Article> articles=new ArrayList<Article>();
		
		//--------------------------搜索代码-----------------------------
		Directory directory=FSDirectory.open(Paths.get("./indexDir/"));  //索引库目录
		Analyzer analyzer=new StandardAnalyzer();		//分词器
		
		//2.1 把查询字符串转为Query对象(只在title中查询)
		QueryParser queryParser=new QueryParser("title",analyzer);
		Query query=queryParser.parse(queryCondition);
		
		//2.2 执行搜索得到结果
		IndexReader indexReader=DirectoryReader.open(directory);
		IndexSearcher indexSearcher=new IndexSearcher(indexReader);
		TopDocs topDocs= indexSearcher.search(query, 100); //返回查询出来的前n条结果
		
		Integer count= topDocs.totalHits; //总结果数量
		ScoreDoc[] scoreDocs=topDocs.scoreDocs;  //返回前N条结果信息
		
		//2.3 处理结果
		for (int i = 0; i < scoreDocs.length; i++) {
			ScoreDoc scoreDoc=scoreDocs[i];
			int docId=scoreDoc.doc;
			System.out.println("得分是："+scoreDoc.score+"，内部编号是："+docId);
			
			//根据内部编号取出真正的Document数据
			Document doc=indexSearcher.doc(docId);
			
			//将document转化为Article
			Article article=new Article(Integer.parseInt(doc.get("id")), doc.get("title"), doc.get("content"));
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