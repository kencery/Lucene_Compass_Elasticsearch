package com.lyzj.kencery.domain;

/**
 * 文章实体类(对文章进行全文检索)
 * @author kencery
 *
 */
public class Article {
	
	/**
	 * 文章Id
	 */
	private Integer id;
	
	/**
	 * 文章标题
	 */
	private String title;
	
	/**
	 * 文章内容
	 */
	private String content;

	/**
	 * 构造方法
	 * @param id
	 * @param title
	 * @param content
	 */
	public Article(Integer id, String title, String content) {
		super();
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