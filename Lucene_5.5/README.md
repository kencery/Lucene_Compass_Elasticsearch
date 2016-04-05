## 1 Lucene HelloWorld 开发步骤(HelloWorld)
*分支为`one`(初步完成创建索引和搜索(功能简单),第一个程序已完成*
#### (1)：准备场景(做准备工作)
1. 创建一个Java Project项目Lucene_5.5
2. 创建包(com.lyzj.kencery.domain)，在包下面创建Article实体类(封装文章的实体类)
3. 创建包(com.lyzj.kencery.helloworld)，在包下面创建HelloWorld测试类，创建两个方法(建立索引和测试搜索)

#### (2)：添加环境(添加Lucene的jar包等工作)
1. 在项目下创建lib目录，用来存放jar包(添加lucene的jar包)
    *  lucene-core-5.5.0.jar(核心包)  
    *  lucene-analyzers-common-5.5.0.jar(分词器)
    *  lucene-highlighter-5.5.0.jar(高亮显示)  
    *  lucene-memory-5.5.0.jar(高亮显示)
    *  lucene-queryparser-5.5.0.jar(解析字符串)

### (3)：实现功能(对Lucene实现其搜索Demo)
1. 创建索引方法：testCreateIndex
2. 搜索方法：testSearch

## 2 对Lucene进行增删改查(Article)
*分支为`two`(增删改查动作),第二个程序，实现完整的增删改查* 
#### (1):在项目Lucene_5.5下进行操作
1. 在包(com.lyzj.kencery.domain)下创建SearchResult泛型实体类(封装lucene查询返回的实体对象)
2. 创建包(com.lyzj.kencery.util),在包下创建ArticleDocumentUtils类(lucene辅助类)
3. 在包(com.lyzj.kencery.util)下创建Configuration类(配置文件(配置索引路径和分词法))
4. 创建包(com.lyzj.kencery.dao),在包下创建ArticleDao类(对文章进行增删改查封装操作)
5. 创建包(com.lyzj.kencery.unit),在包下创建ArticleDaoTest类(测试ArticleDao的方法是否正确)

## 3 Lucene(IndexWriter(多线程),IndexSearcher(实时更新))
*分支为`three`(IndexWriter(多线程),IndexSearcher(实时更新)),* 
#### (1):在项目Lucene_5.5下进行操作
1. 在包(com.lyzj.kencery.unit)下创建IndexWriterTest类(对于同一个索引库，只能有一个打开的有效的IndexWriter，如果有多个则抛出异常)，抛出问题
2. 在包(com.lyzj.kencery.util)下创建LuceneUtils类(专门用单例模式来维护IndexWriter类)，解决IndexWriter、IndexSearcher
3. 修改包(com.lyzj.kencery.dao)下的ArticleDao的增删改方法，调用LuceneUtils实例化IndexWriter
4. 在包(com.lyzj.kencery.unit)下创建IndexSearchTest类(测试IndexSearcher，实时更新,跟踪状态)

## 4 Lucene优化(合并文件(不建议在项目中使用))
*分支为`four`(合并生成的文件),* 
#### (1):在项目Lucene_5.5下进行操作
1. 在包(com.lyzj.kencery.unit)下创建OptiomizeTest类(测试合并生成的文件，减少文件操作(IO))
2. 所有详细信息参看项目中的备注说明。

### *1为一个案例，2、3、4为同一个案例*