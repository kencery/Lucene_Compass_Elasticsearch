## 1 Lucene HelloWorld 开发步骤(HelloWorld)
*分支为`lucene_one`(初步完成创建索引和搜索(功能简单),第一个程序已完成*
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
*分支为`luceue_two`(增删改查动作),第二个程序，实现完整的增删改查* 
#### (1):在项目Lucene_5.5下进行操作
1. 在包(com.lyzj.kencery.domain)下创建SearchResult泛型实体类(封装lucene查询返回的实体对象)
2. 创建包(com.lyzj.kencery.util),在包下创建ArticleDocumentUtils类(lucene辅助类)
3. 在包(com.lyzj.kencery.util)下创建Configuration类(配置文件(配置索引路径和分词法))
4. 创建包(com.lyzj.kencery.dao),在包下创建ArticleDao类(对文章进行增删改查封装操作)
5. 创建包(com.lyzj.kencery.unit),在包下创建ArticleDaoTest类(测试ArticleDao的方法是否正确)

## 3 Lucene(IndexWriter(多线程),IndexSearcher(实时更新))
*分支为`lucene_three`(IndexWriter(多线程),IndexSearcher(实时更新))* 
#### (1):在项目Lucene_5.5下进行操作
1. 在包(com.lyzj.kencery.unit)下创建IndexWriterTest类(对于同一个索引库，只能有一个打开的有效的IndexWriter，如果有多个则抛出异常)，抛出问题
2. 在包(com.lyzj.kencery.util)下创建LuceneUtils类(专门用单例模式来维护IndexWriter类)，解决IndexWriter、IndexSearcher
3. 修改包(com.lyzj.kencery.dao)下的ArticleDao的增删改方法，调用LuceneUtils实例化IndexWriter
4. 在包(com.lyzj.kencery.unit)下创建IndexSearchTest类(测试IndexSearcher，实时更新,跟踪状态)

## 4 Lucene优化(合并文件(不建议在项目中使用))
*分支为`lucene_four`(合并生成的文件)* 
#### (1):在项目Lucene_5.5下进行操作
1. 在包(com.lyzj.kencery.unit)下创建OptiomizeTest类(测试合并生成的文件，减少文件操作(IO))
2. 所有详细信息参看项目中的备注说明。

## 5 分词器
*分支为`lucene_five`(分词法)* 
#### (1):在项目Lucene_5.5下进行操作
1. 建立索引与搜索都可能会用到分词器，因使用同一个，否则可能会搜索不到结果
2. 在包(com.lyzj.kencery.unit)下创建AnalyzerTest类(测试分词器，在类下面有详细的说明，请参考)
3. 在lib文件下面复制IKAnalyzer5.2.1.jar包，将其包添加到项目下，然后给其配置分词规则
4. 将IKAnalyzer包下面的ext.dic和stopword.dic以及IKAnalyzer.cfg.xml文件拷贝到项目根目录下，进行修改
5. 测试的分词法含有：StandardAnalyzer、CJKAnalyzer、IKAnalyzer。

## 6 高亮
*分支为`lucene_six`(高亮，作用是生成一段摘要，把摘要中的关键词高亮显示)* 
#### (1):在项目Lucene_5.5下进行操作
1. 在包(com.lyzj.kencery.unit)下创建HighlightTest类(高亮，在类下面有详细的说明，请参考)
2. 在HighlightTest类中有详细的高亮现实的说明，欢迎大家补充

## 7 排序
*分支为`lucene_seven`(排序，对查询出来的结果按照相关度排序)* 
#### (1):在项目Lucene_5.5下进行操作
1. 在包(com.lyzj.kencery.unit)下创建SortTest类(排序，在类下面有详细的说明，请参考)
2. 本人初步研究，很多东西也是似懂非懂，如果大家有什么建议或者其它问题，欢迎交流。
3. 在SortTest类中有详细的高亮现实的说明，欢迎大家补充

## 8 过滤
*分支为`lucene_eight`(过滤，对查询出来的结果进行过滤)* 
#### (1):在项目Lucene_5.5下进行操作
1. 在包(com.lyzj.kencery.unit)下创建FilterTest类(过滤，在类下面有详细的说明，请参考)
2. 在包(com.lyzj.kencery.unit)下创建ToolsTest类(过滤，解决数字和字符串过滤的问题，再类里面有详细的说明)
3. 本人初步研究，很多东西也是似懂非懂，如果大家有什么建议或者其它问题，欢迎交流。

## 9 各种查询
*分支为`lucene_nine`(查询，各种各样的查询测试)* 
#### (1):在项目Lucene_5.5下进行操作
1. 在包(com.lyzj.kencery.unit)下创建QueryTest类(各种查询，在类下面有详细的说明，请参考)

### *1为一个案例，2、3、4、5、6、7、8、9为同一个案例*

###### lucene建议使用高版本，本实例为学习使用，详细备注请看代码
本案例完结，大家如果遇到不懂得问题或者其它需要探讨的问题，可以在QQ中联系我进行探讨，谢谢
