package cn.jsoup.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 京东页面测试
 * @author lilin
 *
 */
public class JDTest {
   public static void main(String[] args) {
	   //模拟不同的agent
	   String[] ua = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
               "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
               "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
               "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
               "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
               "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
               "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
               "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
               "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
               "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
               "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
               "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
               "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
               "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
               };
	   try {
		   HttpClientBuilder builder = HttpClientBuilder.create();
	       CloseableHttpClient client = builder.build();
	       //执行
	       HttpUriRequest httpGet = new HttpGet("https://search.jd.com/Search?keyword=卫裤男&enc=utf-8&wq=针织衫男&pvid=lqq7v5wi.7ri5o7");
	       CloseableHttpResponse response = client.execute(httpGet);
	       HttpEntity entity = response.getEntity();
	       if(entity!=null){
	           String  entityStr= EntityUtils.toString(entity,"utf-8");
	           Integer totalPage=100;//默认100 页
	           for (int i = 1; i < 2; i++) {
	        	   Document document=Jsoup.connect("https://search.jd.com/Search?keyword=牛奶&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=牛奶&stock=1&page=3&s=59&click=0")
			                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
			                .maxBodySize(0)
			                .get();
	        	   //System.err.println(document.toString());
	        	   System.err.println(document.getElementsByClass("gl-item").select("div[class=p-commit]").select("a").text());
	        	  // System.err.println(document.getElementsByClass("gl-item").select("div[class=p-shop]").select("a").attr("href"));
			 }
	       }
//	       System.out.println(response.toString());
	} catch (Exception e) {
		e.printStackTrace();
	}
	  
}
}
