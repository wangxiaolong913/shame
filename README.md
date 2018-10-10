技术：
  框架：springboot maven  mybaties  
  工具：
      ContentExtractor-提取网页正文
      word分词
      jsoup爬虫
功能：
  1、爬虫某招聘网站获取薪资等信息
  2、获取各个公司招聘详情链接
  3、获取链接内网站正文、分词
  4、MergeTxt：将所有分词文本内容合并，用于词云（wordcloud项目）
命令：
  maven打包：mvn package -Dmaven.test.skip=true  
  运行程序：java -jar shame-0.0.1-SNAPSHOT.jar
接口地址：
  提取网页正文、分词：http://localhost:8080/shame/extract
  网站爬虫：http://localhost:8080/shame/spider
  爬虫后进行分词处理中间接口：http://localhost:8080/shame/bridge
  
