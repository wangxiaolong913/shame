/**
 * @FileName : shame
 * @author : lenovo
 * @Date : 2018年10月10日
 * @Description : 程序结果是为mahout lda提供数据基础.也可以为爬虫使用,因为实现了提取网页正文和进行了分词处理
 * @check
 */
package com.wukong.shame.spider.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.segmentation.WordRefiner;
import org.apdplat.word.tagging.PartOfSpeechTagging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wukong.shame.spider.service.SpiderContentService;

import cn.edu.hfut.dmic.htmlbot.contentextractor.ContentExtractor;

@Service
public class SpiderContentServiceImpl implements SpiderContentService {
	@Value("${fileNamePath}")
	private String fileNamePath;

	@Value("${fileContentPath}")
	private String fileContentPath;

	public String acqUrl(String urlKey) {
		Map<String, String> map = new HashMap<String, String>();
		String urlContent;
		try {
			/**
			 * 提取网页正文
			 */
			urlContent = ContentExtractor.getContentByURL(urlKey);

			/**
			 * word分词处理
			 */
			// 去除停用词
			List<Word> wordSegm = WordSegmenter.seg(urlContent);
			// 加载自定义组合词词库
			PartOfSpeechTagging.process(wordSegm);
			wordSegm = WordRefiner.refine(wordSegm);
			// 过滤掉逗号、数字、数字+单位、词性标注、[、]、日期
			String wordReg = ",|[0-9]+/m| [0-9]+[\u4e00-\u9fa5]+|/[a-z]+|/[a-z]|\\[|\\]";
			String words = wordSegm.toString().replaceAll(wordReg, " ");

			/**
			 * 分词内容写入文件，文件名是规范后的url url和文件名映射写入文件
			 */
			// 文件夹存在验证
			File file = new File(fileContentPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			// 规范分词内容文件名,所有符号转为"-"
			String urlReg = "[^0-9A-Za-z]";
			String urlFilename = urlKey.replaceAll(urlReg, "-");

			FileWriter urlFilewriter = new FileWriter(fileContentPath + urlFilename, true);
			urlFilewriter.write(words);
			urlFilewriter.close();

			// 文件名和url映射写入文件
			FileWriter urlmapFilewriter = new FileWriter(fileNamePath, true);
			urlmapFilewriter.write("\r\n" + urlKey + ", /" + urlFilename);
			urlmapFilewriter.close();

			map.put("result", "success");
			map.put("msg", "");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			map.put("result", "fail");
			map.put("msg", sw.toString());
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String result = null;
		try {
			result = objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
