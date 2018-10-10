/**
 * @FileName : shame
 * @author : lenovo
 * @Date : 2018年10月10日
 * @Description :
 * @check
 */
package com.wukong.shame.spider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author lenovo
 *
 */
public class MergeTxt {
	public static void main(String[] args) {
		// 遍历文件夹
		String path = "D:\\temp\\filecontent";
		String newFile = "D:\\temp\\all.txt";
		StringBuilder strb = new StringBuilder();
		File file = new File(path);
		File[] fs = file.listFiles();
		for (File f : fs) {
			// 获取文件夹下所有文件内容
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				byte[] b = new byte[fis.available()];
				fis.read(b);
				String content = new String(b);
				strb.append(content + " ");
				fis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 将所有文件内容存入新的文件
		try {
			FileOutputStream fos = new FileOutputStream(newFile);
			fos.write(strb.toString().getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
