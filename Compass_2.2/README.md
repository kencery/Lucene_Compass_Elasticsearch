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


3. 创建包(com.lyzj.kencery.helloworld)，在包下面创建HelloWorld测试类，创建两个方法(建立索引和测试搜索)
4. 在项目下创建lib目录，用来存放jar包(compass和lucene包) 



















