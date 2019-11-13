package com.anxin.jisuan.common;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * 网络爬虫：WebMagic
 * F12->Elements->Copy->Copy XPath
 *
 * @author: ly
 * @date: 2019/11/13 14:09
 * https://blog.csdn.net/rensihui/article/details/78393398
 */
public class MyCnblogsSpider implements PageProcessor {

    // 抓取网站的相关配置，可以包括编码、抓取间隔1s、重试次数等
    private Site site = Site.me().setCharset("utf8").setRetryTimes(1000).setSleepTime(1000);

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        String title = html.xpath("/html/head/title").get();//系统title。F12->Elements->Copy->Copy XPath
        System.out.println("系统名称: " + title);
    }

    public static void main(String[] args) {
        MyCnblogsSpider my = new MyCnblogsSpider();
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(my).addUrl("https://www.sina.com.cn/").thread(1).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒");

    }
}
