package cn.jsoup;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 抓取智联
 * @author lilin
 *
 */
public class GetJonInfo {
    private String url="http://sou.zhaopin.com/jobs/searchresult.ashx?jl=";  //智联招聘网站
    private  String city="西安"; //搜索工作的城市
    private  String keywords="java";  //搜索工作的关键字
    public GetJonInfo(String city,String keywords){
        this.city=city;
        this.keywords =keywords;
    }
 
    public void getZhiLianWork(){
    	String resultStr="";//要写出的字符串
        try {
            for (int i=0;i<100;i++) {
                System.out.println("*********开始遍历第"+(i+1)+"页的求职信息*********");
                Document doc = Jsoup.connect(url+city+"&kw="+keywords+"&p="+(i+1)+"&isadv=0").get();
                Element content = doc.getElementById("newlist_list_content_table");
                Elements zwmcEls = content.getElementsByClass("zwmc");
                Elements gsmcEls = content.getElementsByClass("gsmc");
                Elements zwyxEls = content.getElementsByClass("zwyx");
                Elements gzddEls = content.getElementsByClass("gzdd");
                Elements gxsjEls = content.getElementsByClass("gxsj");
                for(int j = 0;j<zwmcEls .size();j++){
                    String jobName=zwmcEls.get(j).tagName("a").text();
                    String company=gsmcEls.get(j).tagName("a").text();
                    String salary=zwyxEls.get(j).tagName("a").text();
                    String place=gzddEls.get(j).tagName("a").text();
                    String date=gxsjEls.get(j).tagName("a").text();
                    System.out.println(
                    jobName+"\t"+company+"\t"+salary+"\t"+place+"\t"+date
                            );
                   resultStr+=jobName+"\t"+company+"\t"+salary+"\t"+place+"\n";
                }
                System.out.println("*********结束遍历第"+(i+1)+"页的求职信息*********");
            }
        //将结果写出到文件
            BufferedWriter bWriter = new BufferedWriter( new OutputStreamWriter(
                            new FileOutputStream("E:\\test.txt"),"utf-8"));
            bWriter.write(resultStr);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
    	GetJonInfo jHtml = new GetJonInfo("上海", "java");
        jHtml.getZhiLianWork();
    }
}
