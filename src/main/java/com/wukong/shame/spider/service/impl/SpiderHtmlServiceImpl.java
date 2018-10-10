package com.wukong.shame.spider.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wukong.shame.spider.dao.JobDao;
import com.wukong.shame.spider.entity.Job;
import com.wukong.shame.spider.service.SpiderHtmlService;

@Service
public class SpiderHtmlServiceImpl implements SpiderHtmlService {
	@Autowired
	JobDao jobDao;

	// 获取职位描述
	public String spider() {
		Map<String, String> map = new HashMap<>();
		try {
			// 遍历分页
			for (int i = 1; i <= 10; i++) {
				String url = "https://www.zhipin.com/c101010100/h_101010100/?query=java&page=" + i + "&ka=page-" + i;
				Document doc = null;
				try {
					doc = Jsoup.connect(url)
							.header("Accept",
									"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
							.header("Accept-Encoding", "gzip, deflate, sdch")
							.header("Accept-Language", "zh-CN,zh;q=0.8")
							.header("User-Agent",
									"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
							.get();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String html = doc.html();
				getJobInfo(html);
			}
			map.put("result", "success");
			map.put("msg", "");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
			map.put("result", "fail");
			map.put("msg", sw.toString());
		}
		ObjectMapper om = new ObjectMapper();
		String result = null;
		try {
			result = om.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取职位详情
	public void getJobInfo(String html) {
		String headUrl = "https://www.zhipin.com";
		Document doc = Jsoup.parse(html);
		Elements jobPrimarys = doc.getElementsByClass("job-primary");// 主体
		for (Element jobPrimary : jobPrimarys) {
			Job job = new Job();

			Element jobInfo = jobPrimary.getElementsByClass("info-primary").get(0);// 职位信息
			String minSalary = jobInfo.getElementsByClass("red").get(0).text().split("-")[0].replace("k", "");// 最低薪资
			String maxSalary = jobInfo.getElementsByClass("red").get(0).text().split("-")[1].replace("k", "");// 最高薪资
			String requirement = jobInfo.getElementsByTag("p").get(0).text();// 任职要求

			// 工作地点
			String placeEndStr = "";
			Pattern placePattern = Pattern.compile("[0-9]|经验|应届生");
			Matcher placeMatcher = placePattern.matcher(requirement);
			while (placeMatcher.find()) {
				placeEndStr = placeMatcher.group(0);
				break;
			}
			int endIndex = requirement.indexOf(placeEndStr);
			String place = requirement.substring(0, endIndex);

			// 学历
			String education = "";
			if (requirement.indexOf("学历不限") > -1) {
				education = "学历不限";
			} else if (requirement.indexOf("本科") > -1) {
				education = "本科";
			} else if (requirement.indexOf("大专") > -1) {
				education = "大专";
			} else if (requirement.indexOf("硕士") > -1) {
				education = "硕士";
			}

			// 工作经验
			String experience = "";
			if (requirement.indexOf("经验不限") > -1) {
				experience = "经验不限";
			} else if (requirement.indexOf("应届生") > -1) {
				experience = "应届生";
			} else {
				Pattern expPattern = Pattern.compile("[0-9]年|[0-9]-[0-9]年|[0-9]-[0-9]+年");
				Matcher expMatcher = expPattern.matcher(requirement);
				while (expMatcher.find()) {
					experience = expMatcher.group(0);
					break;
				}
			}

			String url = headUrl + jobInfo.getElementsByTag("a").get(0).attr("href");// 职位链接

			Element companyInfo = jobPrimary.getElementsByClass("info-company").get(0);// 公司信息
			String name = companyInfo.getElementsByClass("name").get(0).text();// 公司名称

			job.setUrl(url);
			job.setName(name);
			job.setEducation(education);
			job.setExperience(experience);
			job.setMaxSalary(Integer.valueOf(maxSalary));
			job.setMinSalary(Integer.valueOf(minSalary));
			job.setPlace(place);
			jobDao.addJob(job);
		}
	}
}
