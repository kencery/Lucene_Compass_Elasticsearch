## 1 Compass_2.2简单说明
*分支为`compass_one`(对compass的简单说明)*

1. Compass的jar包已停止更新(compass2.2)，只支持lucene2.4以下，而lucene现在最新的版本为5.5，版本更新优化了性能等各方各面，故而Compass的包只支持lucene2.4以下,故而这个项目使用的lucene为2.4，compass为2.2
2. 网站介绍：http://yufenfei.iteye.com/blog/1683546
3. Compass——>lucene——>索引库   相当于Hibernate——>JDBC——>数据库
4. CompassAPI框架代码以及XML配置
```java
	java代码思路
		CompassConfiguration ccfg=new CompassConfiguration().Configure(); //compass.cfg.xml
		CompassSessionFactory csf=ccfg.buildCompass();
		CompassSession session=csf.openSession();
		try{
			CompassTransaction ctx=session.beginTransaction();
			//操作,增(create，建立索引)删(delete)改(save)查(get/load)
			ctx.commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			throw new Exception(e);
		}finally{
			session.close();
		}
  xml配置：
	1.主配置文件 compass.cfg.xml
  		索引库信息(路径)
  		导入映射配置
  		其它配置
  	2.映射关系  
 		类——Document
 		属性——Field 
```
## 2 Compass HelloWorld 开发
*分支为`compass_one`(compass HelloWorld实现)*
#### (1)：准备工作
1. 创建一个Java Project项目Compass_2.2
2. 创建包(com.lyzj.compass.domain)，在包下面创建Article实体类(文章信息),SearchResult实体类(文章查询分页信息)
3. 创建包(com.lyzj.compass.helloworld)，在包下面创建HelloWorld测试类，创建两个方法(建立索引和测试搜索)
4. 在项目根目录下创建lib目录，用来存放jar包(compass和lucene包)
	* lucene-core.jar
    * lucene-analyzers.jar
    * lucene-highlighter.jar
    * lucene-queries.jar
    * lucene-snowball.jar
    * lucene-spellchecker.jar
    * compass-2.2.0.jar
    * commons-logging.jar
5. 在项目src目录下创建配置文件compass.cfg.xml,配置compass
6. 创建包(com.lyzj.kencery.helloworld)，在包下面创建HelloWorld测试类，创建两个方法(建立索引和测试搜索)

## 3 对Compass进行增删改查(Article)
*分支为`compass_two`(增删改查动作),封装完整的增删改查* 
#### (1):在项目Compass_2.2下进行操作
1. 创建包(com.lyzj.compass.dao),在包下面创建ArticleDao类(使用Compass对文章进行增删改查封装操作)
2. 创建包(com.lyzj.compass.util),再包下面创建CompassUtil类(维护Compass的全局资源)
3. 创建包(com.lyzj.compass.unit)，在包下面创建ArticleDaoTest(测试ArticleDao类是否没有问题)

## 4 分词器和高亮
*分支为`compass_three`(分词器和高亮的使用)* 
#### (1):在项目Compass_2.2下进行操作
1. 在ArticleDao下面的search方法中添加高亮显示实现的代码
2. 在compass.cfg.xml配置细节(摘要大小，高亮器，分词器)
3. 添加极易分词器je-analysis-1.5.3.jar，支持lucene2.4

















