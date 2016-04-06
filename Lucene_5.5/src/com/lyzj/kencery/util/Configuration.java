package com.lyzj.kencery.util;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 配置文件(配置索引路径和分词法)
 * @author kencery
 *
 */
public class Configuration {
	
	/**
	 * 配置索引路径
	 */
	private static Directory directory;
	
	/**
	 * 分词法
	 */
	private static Analyzer analyzer;
		
	static{
		//初始化配置，应通过读取配置文件(config.properties),暂时写死
		try {
			directory=FSDirectory.open(Paths.get("./indexDir/"));
			analyzer=new IKAnalyzer();	
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Directory getDirectory() {
		return directory;
	}
	public static void setDirectory(Directory directory) {
		Configuration.directory = directory;
	}
	public static Analyzer getAnalyzer() {
		return analyzer;
	}
	public static void setAnalyzer(Analyzer analyzer) {
		Configuration.analyzer = analyzer;
	}
}