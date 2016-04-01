package com.lyzj.kencery.unit;

import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.lyzj.kencery.domain.Article;
import com.lyzj.kencery.util.ArticleDocumentUtils;
import com.lyzj.kencery.util.LuceneUtils;

/**
 * 测试合并生成的文件，减少文件操作(IO)
 * @author kencery
 *
 */
public class OptiomizeTest {
	
	/**
	 * 
	 * 测试合并生成的文件，减少文件操作(IO)
	 * 备注：forceMerge是lucene3.5之前替代optimize方法的，其实只是改了个名称，因为优化的使效率变低 
     * 因为一到优化它就会全部更新索引，这个所涉及到的负载是很大的 ，所以改了个名称，不推荐使用，在做优化的时候会把索引回收站中的数据文件全部删除 
     * lucene会在你写索引的时候根据你的索引的段越来越多会自动帮忙优化的，force是强制优化 
	 */
	@Test
	public void TestOptiomize(){
		try {
			//// 当小文件达到多少个时，就自动合并多个小文件为一个大文件
			LuceneUtils.getIndexWriter().forceMerge(1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 测试内存中存放虚拟目录
	 * @throws Exception 
	 * 
	 */
	@Test
	public void TestDirectory() throws Exception{
		//文件系统中真实的目录，可存储，但速度相对慢
		//Directory fsDirectory = FSDirectory.open(Paths.get("./indexDir/"));
		
        //在内存中虚拟的目录，速度快，但不存储，还对机器的内存大小有要求
        Directory ramDir = new RAMDirectory();
        
		IndexWriterConfig iwc= new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter indexWriter=  new IndexWriter(ramDir,iwc);
        indexWriter.addDocument(new Document());
        indexWriter.close();
	}
	
	/**
	 * 有错误，暂未处理
	 * @throws Exception 
	 * 
	 */
	@Test
	public void TestDirectory1() throws Exception{
		//1 程序启动时把索引库都加载到内存中
		Directory fsDirectory = FSDirectory.open(Paths.get("./indexDir/"));
		IOContext ioContext=new IOContext();
        Directory ramDir = new RAMDirectory((FSDirectory) fsDirectory,ioContext);
        
        //2 程序运行时的正常使用
    	IndexWriterConfig iwc= new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter indexWriter=  new IndexWriter(fsDirectory,iwc);
		
		Article article=new Article(1, "Lucene是全文检索框架", 
				"全文检索（Full-Text Retrieval）是指以文本作为检索对象，找出含有指定词汇的文本。全面、准确和快速是衡量全文检索系统的关键指标。");
		Document doc=ArticleDocumentUtils.articleToDocument(article);
		
        indexWriter.addDocument(doc);
        System.err.println(1111);
        //3 程序退出前，把内存中的索引库写到文件系统上
        IndexWriterConfig iwc1= new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter fsIndexWriter=  new IndexWriter(ramDir,iwc1);
		
		//把指定索引库中的数据合并当前索引库中
		//>>
        fsIndexWriter.addIndexes(ramDir);
        fsIndexWriter.close();
	}
}