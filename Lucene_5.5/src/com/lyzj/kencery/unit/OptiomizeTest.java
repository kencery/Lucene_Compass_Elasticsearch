package com.lyzj.kencery.unit;

import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.lyzj.kencery.domain.Article;
import com.lyzj.kencery.util.ArticleDocumentUtils;
import com.lyzj.kencery.util.Configuration;
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
     * 
     * 强制合并的政策合并段，直到有<= maxNumSegments。
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
	 * 控制合并频率
	 * 
	 * SetMergeFactor是控制segment合并频率的，其决定了一个索引块中包括多少个文档，
	 * 	当硬盘上的索引块达到多少时，将它们合并成一个较大的索引块。
	 * 当MergeFactor值较大时，生成索引的速度较快。MergeFactor的默认值是10,最小2，建议在建立索引前将其设置的大一些。
	 * @throws Exception 
	 */
	@Test
	public void testAutoOptiomize() throws Exception{
		IndexWriterConfig iwc=new IndexWriterConfig(Configuration.getAnalyzer());
		LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
		mergePolicy.setMergeFactor(2);
		iwc.setMergePolicy(mergePolicy);
		LuceneUtils.getIndexWriter().addDocument(new Document());
	}
	
	/**
	 * 测试内存中存放虚拟目录
	 * @throws Exception 
	 * 
	 */
	@Test
	public void TestDirectory() throws Exception{
		//文件系统中真实的目录，优点：可存储，但速度相对慢
		//Directory fsDirectory = FSDirectory.open(Paths.get("./indexDir/"));
		
        //在内存中虚拟的目录，优点：速度快，但不可存储，还对机器的内存大小有要求
        Directory ramDir = new RAMDirectory();
        
		IndexWriterConfig iwc= new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter indexWriter=  new IndexWriter(ramDir,iwc);
        indexWriter.addDocument(new Document());
        indexWriter.close();
	}
	
	/**
	 * 可以去做测试检验：思路是：首先去保存一个索引，然后再执行下面的test方法，首先执行之后将原来查询到的一条索引写入到内存中，
	 * 	然后又添加了一条缩索引，最后内存中则存在两条索引，最后在程序退出的时候将内存中的两条索引写到文件系统上，
	 * 	在此查询则会查询到三条记录，以此类推。
	 * @throws Exception 
	 * 
	 */
	@Test
	public void TestDirectory1() throws Exception{
		//1 程序启动时把索引库都加载到内存中
		Directory fsDirectory = FSDirectory.open(Paths.get("./indexDir/"));
		RAMDirectory ramDir = new RAMDirectory((FSDirectory) fsDirectory,new IOContext());
        
        //2 程序运行时的正常使用
    	IndexWriterConfig iwc= new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter indexWriter=  new IndexWriter(ramDir,iwc);
		
		Article article=new Article(2, "Lucene是全文检索框架", 
				"全文检索（Full-Text Retrieval）是指以文本作为检索对象，找出含有指定词汇的文本。全面、准确和快速是衡量全文检索系统的关键指标。");
		Document doc=ArticleDocumentUtils.articleToDocument(article);
		
        indexWriter.addDocument(doc);
        indexWriter.close();
        System.err.println("写入完成");
        
        //3 程序退出前，把内存中的索引库写到文件系统上
        //>>如果索引库不存在，就创建，否则，就是用
        IndexWriterConfig iwc1= new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter fsIndexWriter=  new IndexWriter(fsDirectory,iwc1);
		//把指定索引库中的数据合并当前索引库中
        fsIndexWriter.addIndexes(ramDir);
        fsIndexWriter.close();
	}
}