package com.lyzj.kencery.unit;

import org.junit.Test;

import com.lyzj.kencery.util.LuceneUtils;

/**
 * 测试IndexSearch
 * @author kencery
 *
 */
public class IndexSearchTest {
	
	/**
	 * 测试IndexSearch
	 */
	@Test
	public void testIndexSearch() {

		LuceneUtils.getIndexSearcher();
		//LuceneUtils.getIndexWriter().add
		
		LuceneUtils.indexChanged();
		
		LuceneUtils.getIndexSearcher();
		
		LuceneUtils.indexChanged();
		LuceneUtils.indexChanged();
		LuceneUtils.indexChanged();
		
		LuceneUtils.getIndexSearcher();
	}
}