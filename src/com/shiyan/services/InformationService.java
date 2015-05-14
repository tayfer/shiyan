package com.shiyan.services;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.shiyan.APPConstant;
import com.shiyan.vo.NewItem;

public class InformationService {

	private static final String TAG = InformationService.class.getSimpleName();
	private LinkedList<NewItem> newsItems;
	private Document doc = null;

	public InformationService() {
		File input = new File(APPConstant.HTMLSOURCEPAHT);
		try {
			doc = Jsoup.parse(input, "gbk", APPConstant.TARGETURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<NewItem> getTopNews() {
		newsItems = new LinkedList<NewItem>();
		NewItem newItem;
		try {
			// 1:首先去本地读取html,如果没有读取到,或是已经过期了的数据,则重新在进行请求

			Element element = doc.getElementById("xsy1lb");
			Elements eles = element.getElementsByTag("a");
			for (Element link : eles) {
				newItem = new NewItem();
				String linkHref = link.attr("href");
				String linkText = link.text();
				newItem.setText(linkText);
				newItem.setDetailUrl(linkHref);
				newsItems.add(newItem);
			}
		} catch (Exception e) {

		}
		return newsItems;
	}

	public List<NewItem> getTopNewPics() {
		newsItems = new LinkedList<NewItem>();
		NewItem newItem;
		try {
			// 1:首先去本地读取html,如果没有读取到,或是已经过期了的数据,则重新在进行请求
			Element element = doc.getElementById("xsy1t");
			Elements eles = element.getElementsByTag("a");
			for (Element link : eles) {
				newItem = new NewItem();
				String linkHref = link.attr("href");
				String linkText = link.getElementsByTag("img").first()
						.attr("alt");
				String imageurl = link.getElementsByTag("img").first()
						.attr("src");
				newItem.setText(linkText);
				newItem.setDetailUrl(linkHref);
				newItem.setImageUrl("http://www.shiyan.com/" + imageurl);
				newsItems.add(newItem);
			}
		} catch (Exception e) {

		}
		return newsItems;
	}
}
