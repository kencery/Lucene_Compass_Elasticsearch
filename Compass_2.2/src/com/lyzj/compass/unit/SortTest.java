package com.lyzj.compass.unit;

import java.util.ArrayList;
import java.util.List;

import org.compass.core.CompassHits;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryFilter;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;
import org.junit.Test;

import com.lyzj.compass.domain.Article;
import com.lyzj.compass.util.CompassUtil;

/**
 * 测试排序
 * 
 * @author kencery
 * 
 */
public class SortTest {
	/**
	 * 为文章建立索引
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateIndex() throws Exception {
		// 1 将需要添加的实体构造成实体对象
		Article article = new Article(27, "Lucene是全文检索框架",
				"全文检索（Full-Text Retrieval）是指以文本作为检索对象，找出含有指定词汇的文本。"
						+ "全面、准确和快速是衡量全文检索系统的关键指标。");
		article.setBoostValue(2f); // 改变boost属性值
		// 3.建立索引(Compass)
		CompassSession session = CompassUtil.openSession();
		CompassTransaction ctx = session.beginTransaction();

		// 建立索引
		session.create(article);

		ctx.commit();
		session.close();
	}

	/**
	 * 得分排序搜索
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {
		// 1 搜索条件
		String queryCondition = "lucene";

		// 2 执行搜索（Compass）
		CompassSession session = CompassUtil.openSession();
		CompassTransaction ctx = session.beginTransaction();

		// 建立索引
		CompassHits hits = session.find(queryCondition);
		int count = hits.length(); // 总结果数量
		// 3 控制台显示结果
		List<Article> articles = new ArrayList<Article>();
		for (int i = 0; i < hits.length(); i++) {
			System.out.println("得分是：" + hits.score(i));
			Article article = (Article) hits.data(i);
			articles.add(article);
		}

		ctx.commit();
		session.close();

		System.out.println("总结果数：" + count);
		for (Article article : articles) {
			System.out.println("查询结果为：" + article);
		}
	}

	/**
	 * 自定义排序搜索
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearch1() throws Exception {
		// 1 搜索条件
		String queryCondition = "lucene";

		// 2 执行搜索（Compass）
		CompassSession session = CompassUtil.openSession();
		CompassTransaction ctx = session.beginTransaction();

		// 建立索引
		// CompassHits hits = session.find(queryCondition);

		// --------------------自定义排序----------------------
		CompassQuery query = session.queryBuilder().queryString(queryCondition)
				.toQuery();

		//排序
		query.addSort("id"); //按照Id升序排序
		//query.addSort("id", SortDirection.REVERSE); // 按照Id降序排序

		//过滤
		CompassQueryFilter filter=session.queryFilterBuilder().between("id", 5, 15, false, true);
		query.setFilter(filter);
		
		CompassHits hits = query.hits();

		// ------------------------------------------------

		int count = hits.length(); // 总结果数量
		// 3 控制台显示结果
		List<Article> articles = new ArrayList<Article>();
		for (int i = 0; i < hits.length(); i++) {
			System.out.println("得分是：" + hits.score(i));
			Article article = (Article) hits.data(i);
			articles.add(article);
		}

		ctx.commit();
		session.close();

		System.out.println("总结果数：" + count);
		for (Article article : articles) {
			System.out.println("查询结果为：" + article);
		}
	}
}
