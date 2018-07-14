package cn.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 这是用知乎为例进行 测试的
 * @author Chris
 *
 */
public class JsoupTest {
	 public static void main(String[] args) throws IOException {
		     String s = null;
		     //assert s!=null?true:false;
		      assert false;
		     System.out.println("end");
		 /*	 try {
	      //获取编辑推荐页
	        Document document=Jsoup.connect("https://www.zhihu.com/explore/recommendations")
	                //模拟火狐浏览器
	                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
	                .get();
	        Element main=document.getElementById("zh-recommend-list-full");
	        Elements es= main.select("div.zh-general-list");
	        for(Element e:es){
	        	for(Element el:e.select("h2 > a[href]")){
	        		System.err.println(el.attr("abs:href")); //在这里加入abs 选项是为了获取绝对路径
	        		//这里就可以把得到的数据及进行处理 
	        		System.err.println(el.text());
	        	}
	        	
	        }
	       
		 //  提取易佰教程的 图片
		    Document doc = Jsoup.connect("http://www.yiibai.com").get();  
           Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");  
           for (Element image : images) {  
               System.out.println("src : " + image.attr("src"));  
               System.out.println("height : " + image.attr("height"));  
               System.out.println("width : " + image.attr("width"));  
               System.out.println("alt : " + image.attr("alt"));  
           }  
	} catch (Exception e) {
		e.printStackTrace();
	} */
		 
		 
	    }
}
