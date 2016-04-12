package com.lyzj.compass.unit;

import java.util.ArrayList;
import java.util.List;

import org.compass.core.CompassHits;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryBuilder.CompassMultiPhraseQueryBuilder;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;
import org.junit.Test;

import com.lyzj.compass.domain.Article;
import com.lyzj.compass.util.CompassUtil;

/**
 * Compass各种各样的查询
 * 
 * @author kencery
 * 
 */
public class QueryTest {

	/**
	 * 各种各样查询
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearch1() throws Exception {
		CompassSession session = CompassUtil.openSession();
		CompassTransaction ctx = session.beginTransaction();
		
		// --------------------查询----------------------
		CompassQuery query =null;   //session.queryBuilder().queryString("lucene").toQuery();

		//1.关键字查询
		CompassQuery query1=session.queryBuilder().term("title", "lucene");
		
		//2.范围查询
		CompassQuery query2=query=session.queryBuilder().between("id", 5, 15, true);
		
		//3.通配符查询
		query=session.queryBuilder().wildcard("title", "lu*n?");
		
		//4.查询所有
		query=session.queryBuilder().matchAll();
		
		//5.模糊查询
		query=session.queryBuilder().fuzzy("title", "luceneX", 0.8F);
		
		//6.短语查询
		CompassMultiPhraseQueryBuilder phraseQueryBuilder=session.queryBuilder().multiPhrase("title");
		phraseQueryBuilder.add("lucene",0);
		phraseQueryBuilder.add("框架",3);
		query=phraseQueryBuilder.toQuery();
		
		query=session.queryBuilder().multiPhrase("title")
				.add("lucene",0).add("框架",3)
				.toQuery();
		query=session.queryBuilder().multiPhrase("title")
				.add("lucene",0).add("框架",3)
				.setSlop(5)  //之间的间隔最多不超过5个
				.toQuery();
		
		//7.布尔查询
		query=session.queryBuilder().bool()
				.addMust(query1).addMustNot(query2).addShould(query).
				toQuery();
		
		CompassHits hits = query.hits();
		// ------------------------------------------------
		System.out.println("对应的查询字符串："+query.toString());
		int count = hits.length(); // 总结果数量
		List<Article> articles = new ArrayList<Article>();
		for (int i = 0; i < hits.length(); i++) {
			//System.out.println("得分是：" + hits.score(i));
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