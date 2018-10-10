package com.wukong.shame.spider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wukong.shame.spider.service.SpiderBridgeService;
import com.wukong.shame.spider.service.SpiderContentService;
import com.wukong.shame.spider.service.SpiderHtmlService;

@Controller
@RequestMapping("/shame")
public class SpiderRest {
	@Autowired
	private SpiderContentService spiderContentService;
	@Autowired
	private SpiderHtmlService spiderHtmlService;
	@Autowired
	private SpiderBridgeService spiderBridgeService;

	/**
	 * 正文、分词
	 * 
	 * @param urlKey
	 * @return
	 */
	@RequestMapping(value = "/extract", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String acqUrl(String urlKey) {
		String result = spiderContentService.acqUrl(urlKey);
		return result;
	}

	/**
	 * BOSS爬虫
	 * 
	 * @return
	 */
	@RequestMapping(value = "/spider", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String spider() {
		String result = spiderHtmlService.spider();
		return result;
	}

	/**
	 * 入口
	 */
	@RequestMapping(value = "/bridge", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void bridge() {
		spiderBridgeService.bridge();
	}
}