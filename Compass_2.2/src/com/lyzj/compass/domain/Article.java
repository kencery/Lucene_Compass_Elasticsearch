package com.lyzj.compass.domain;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;

/**
 * 文章实体类(对文章进行全文检索)
 * @author kencery
 *
 */
@Searchable  /*表示当前对象是否为可搜索对象*/
public class Article {
	
	/**
	 * 文章Id
	 */
	@SearchableId(name="id")   /*唯一标示符*/
	private Integer id;
	
	/**
	 * 文章标题
	 */
	@SearchableProperty(name="title",store=Store.YES,index=Index.ANALYZED)
	private String title;
	
	/**
	 * 文章内容
	 */
	@SearchableProperty(name="content",store=Store.YES,index=Index.ANALYZED)
	private String content;

	/**
	 * 需要设置无参构造函数，否则会报错
	 */
	public Article() {
	}
	
	/**
	 * 构造方法
	 * @param id
	 * @param title
	 * @param content
	 */
	public Article(Integer id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", content="
				+ content + "]";
	}

}