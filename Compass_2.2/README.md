# Compass_2.2简单说明
*分支为`compass_one`(对compass的简单说明)*

1. Compass的jar包已停止更新(compass2.2)，只支持lucene2.4，而lucene现在最新的版本为5.5，版本更新优化了性能等各方各面，故而Compass的包只支持lucene2.4,所以这里只是用来学习一下。
2. 网站介绍：http://yufenfei.iteye.com/blog/1683546
3. Compass——>lucene——>索引库   相当于Hibernate——>JDBC——>数据库
4. CompassAPI框架代码
```java
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
```
5. Compass_xml配置
```xml
  1.主配置文件 compass.cfg.xml
  	索引库信息(路径)
  	导入映射配置
  	其它配置
  2.映射关系  
 	类——Document
 	属性——Field
```