package com.lyzj.kencery.util;

import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;

/**
 * 单例模式来维护IndexWrite类
 * @author kencery
 *
 */
public class LuceneUtils {
	
	/**
	 * 全局变量
	 */
	private static IndexWriter indexWriter;
	private static IndexSearcher indexSearcher;
	private static IndexReader indexReader=null;
	
	static{
		try {
			//初始化IndexWriter
			IndexWriterConfig iwc=new IndexWriterConfig(Configuration.getAnalyzer());
			
			indexWriter=new IndexWriter(Configuration.getDirectory(), iwc);

			System.out.println("---   已经初始化IndexWriter  ---");
			
			//指定在JVM推出前要执行的代码
			Runtime.getRuntime().addShutdownHook(new Thread(){
				@Override
				public void run() {
					closeIndexWriter();
					closeIndexReader();
				}
			});
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取全局唯一的IndexWriter实例
	 * 
	 * @return
	 */
	public static IndexWriter getIndexWriter() {
		return indexWriter;
	}

	
	 /**
	  * 获取全局唯一的IndexWriter实例
	  * @return
	  */
	public static IndexSearcher getIndexSearcher() {
		if(indexSearcher==null){
			if(indexReader==null){
				System.err.println("---初始化IndexReader---");
				try {
					indexReader= DirectoryReader.open(Configuration.getDirectory());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			indexSearcher=new IndexSearcher(indexReader);
			System.out.println("---》创建了IndexSearchers实例《---");
		}
		return indexSearcher;
	}



	/**
	 * 通知索引库更改了
	 */
	public static void indexChanged(){
		if(indexSearcher!=null){
			try {
				indexSearcher=null;
				System.out.println("---1.关闭了IndexSearchers实例---");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}		
		}
	}
	
	/**
	 * 关闭IndexWriter
	 */
	private static void closeIndexWriter(){
		if(indexWriter!=null){
			try {
				indexWriter.close();
				System.out.println("---   关闭IndexWriter   ---");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 关闭indexReader
	 */
	private static void closeIndexReader(){
			if(indexReader!=null){
			try {
				indexReader.close();
				System.out.println("---2.关闭了IndexSearchers实例---");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
