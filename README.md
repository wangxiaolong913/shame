技术：<br>
----
  框架：springboot maven  mybaties  <br>
  ----
  工具：<br>
  ----
      ContentExtractor-提取网页正文<br>
      word分词<br>
      jsoup爬虫<br>
      python相关：anaconda、wordcloud、matplotlib
功能：<br>
----
  1、爬虫某招聘网站获取薪资等信息<br>
  2、获取各个公司招聘详情链接<br>
  3、获取链接内网站正文、分词<br>
  4、MergeTxt：将所有分词文本内容合并，用于词云（wordcloud项目）<br>
命令：<br>
----
  maven打包：mvn package -Dmaven.test.skip=true  <br>
  运行程序：java -jar shame-0.0.1-SNAPSHOT.jar<br>
接口地址：<br>
------
  提取网页正文、分词：http://localhost:8080/shame/extract<br>
  网站爬虫：http://localhost:8080/shame/spider<br>
  爬虫后进行分词处理中间接口：http://localhost:8080/shame/bridge<br>
  
  
