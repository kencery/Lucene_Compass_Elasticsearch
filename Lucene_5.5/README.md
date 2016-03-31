## 1 Lucene HelloWorld 开发步骤
#### (1)：准备场景(做准备工作)
1. 创建一个Java Project项目Lucene_5.5
2. 创建包(com.lyzj.kencery.domain)，在包下面创建Article实体类(封装文章的实体类)
2. 创建包(com.lyzj.kencery.helloworld)，在包下面创建HelloWorld测试类，创建两个方法(建立索引和测试搜索)
    
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
	备注：完成，分支为one(初步完成创建索引和搜索(功能简单))
	