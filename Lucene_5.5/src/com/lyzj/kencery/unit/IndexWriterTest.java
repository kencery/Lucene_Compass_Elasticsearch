package com.lyzj.kencery.unit;

import java.nio.file.Paths;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import com.lyzj.kencery.util.Configuration;

/**
 * 测试多个请求出现的问题
 * @author kencery
 *
 */
public class IndexWriterTest {

	/**
	 * 测试IndexWriter
	 * @throws Exception 
	 */
	@Test
	public void testIndexWriter() throws Exception{
		//>>对于同一个索引库，只能有一个打开的有效的IndexWriter，如果有多个则抛出异常
		//java.lang.IllegalStateException: do not share IndexWriterConfig instances across IndexWriters

		Directory directory=FSDirectory.open(Paths.get("./indexDir/"));
		
		IndexWriterConfig iwc=new IndexWriterConfig(Configuration.getAnalyzer());
		
		new IndexWriter(directory,iwc);
		new IndexWriter(directory,iwc);
	}
	
	
	
	
	
	
}