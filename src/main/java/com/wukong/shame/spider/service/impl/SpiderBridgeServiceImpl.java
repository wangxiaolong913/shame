package com.wukong.shame.spider.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wukong.shame.spider.dao.JobDao;
import com.wukong.shame.spider.service.SpiderBridgeService;
import com.wukong.shame.spider.service.SpiderContentService;

@Service
public class SpiderBridgeServiceImpl implements SpiderBridgeService {

	@Autowired
	SpiderContentService spiderContentService;
	@Autowired
	JobDao jobDao;

	@Override
	public void bridge() {
		List<String> urlList = jobDao.getUrlList();
		for (String url : urlList) {
			spiderContentService.acqUrl(url);
		}
	}
}
