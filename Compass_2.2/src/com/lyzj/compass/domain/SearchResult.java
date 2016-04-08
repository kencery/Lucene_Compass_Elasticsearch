package com.lyzj.compass.domain;

import java.util.List;

/**
 * 封装搜索返回的实体对象(包含实体对象和总数)
 * @author kencery
 *
 */
public class SearchResult<T> {
	
	/**
	 * 总数
	 */
	private int count;
	
	/**
	 * 返回的实体对象
	 */
	private List<T> list;

	/**
	 * 构造方法
	 * @param count	总数
	 * @param list	返回集合
	 */
	public SearchResult(int count, List<T> list) {
		this.count = count;
		this.list = list;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	
	@Override
	public String toString() {
		return "SearchResult [count=" + count + ", list=" + list + "]";
	}	
	
}

