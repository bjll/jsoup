package cn.jsoup;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sf.json.JSONArray;

public class CityInfo {
    //http://www.mca.gov.cn/article/sj/tjbz/a/2018/201803/201803191002.html
	//获取县级以上的行政区域的划分 获取集合
	public  static List getAllInfo(){
		List returnList=new ArrayList();//这是准备返回的list
		 try {
		        Document document=Jsoup.connect("http://www.mca.gov.cn/article/sj/tjbz/a/2018/201803/201803191002.html")
		                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
		                .maxBodySize(0)
		                .get();
		       //这是获取到了table
		       Element element=document.getElementById("2017年4季度县以上行政区划代码_12452").getElementsByTag("table").get(0);
		       Elements es= element.select("tr[height=19]");
		       Map map=null;
		       for(Element el:es){
		    	   int i=0;
		    	   map=new HashMap();
		    	   //加一个判断防止出现空值
                 if( el.text()==null||el.text().equals("")){
                	 break;
                 }
		        //里面是每一个tr
		    	for(Element el1:el.select("td.xl7012452") ) {
		    		  if(i==0){ //说明是序列号
		    			if(el1.text()!=null&& !el1.text().equals("")){
		    				map.put("id",el1.text() ); 
		    			}
		    		  }else if(i==1){
		    			  if(el1.text()!=null&& !el1.text().equals("")){
		    				  map.put("name",el1.text() ); 
			    			}
		    			  
		    		  }
		    		  //这里开始取因为每一个都是两个 则 第一个是编号 第二个是对应的名字
		    		  i++;
		    	  }
		    	returnList.add(map);
		       }
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return returnList;
	}
	//这准备插入数据的方法
	public  static void insertDB(){
	 try{
	  List resultList=getAllInfo();//获取到list
	  String URL="jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&amp;characterEncoding=utf-8";
      String USER="root";
      String PASSWORD="root";
      //1.加载驱动程序
      Class.forName("com.mysql.jdbc.Driver");
      //2.获得数据库链接
      Connection conn=DriverManager.getConnection(URL, USER, PASSWORD);
      //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
      Statement st=conn.createStatement();
      PreparedStatement pst=conn.prepareStatement("insert into cityinfo values(?,?,?,?)");;
      //4.执行插入的 效果
      //分析数据：
      //省都是以四个0000结尾的  市都是以00  结尾的   总共是6 位
      
      for(int i=0;i<resultList.size();i++){
    	  Map map=(Map) resultList.get(i);
    	  String id= (String) map.get("id");
    	  String name=(String) map.get("name");
    	  pst.setInt(1, i);
    	  pst.setInt(2, Integer.parseInt(id));
    	  pst.setString(3, name);
    	  if(id.endsWith("0000")){
    		  pst.setInt(4, 0); //说明是省 父id 是0
    	  }else if(id.startsWith("11")||id.startsWith("31")||id.startsWith("50")||id.startsWith("12")){ //这是北京 天津和上海特殊的
    		  pst.setInt(4, Integer.parseInt(id.substring(0, 2)+"0000"));
    	  }else if(!id.endsWith("0000")&&id.endsWith("00")){ //说明是市
    		  pst.setInt(4, Integer.parseInt(id.substring(0, 2)+"0000"));
    	  }else{  //县
    		  pst.setInt(4, Integer.parseInt(id.substring(0, 4)+"00"));  
    	  }
    	  pst.execute();
      }
      //关闭资源
      pst.close();
      st.close();
      conn.close();
	 }catch(Exception e){
		 e.printStackTrace();
	 } 
	}
    /**
     * 生成json 字符转  用前台生成三级联动
     * @return
     */
	public static String cityJson() {
		try { 
			  //1------ .准备连接数据库
			  String URL="jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&amp;characterEncoding=utf-8";
		      String USER="root";
		      String PASSWORD="root";
		      //1.加载驱动程序
		      Class.forName("com.mysql.jdbc.Driver");
		      //2.获得数据库链接
		      Connection conn=DriverManager.getConnection(URL, USER, PASSWORD);
		      //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
		      Statement st=conn.createStatement();
		      Statement cityst=conn.createStatement();
		      Statement countyst=conn.createStatement();
		      ResultSet  resultSet;//省
		      ResultSet  subresultSet=null;//市
		      ResultSet  countyResultSet=null;//县
		      JSONArray  jsonArray=new  JSONArray();//这是最外层的
		      Map provinceMap=null;
		      //1 获取省的信息
				String  sql="select  *  from  cityinfo  c where  c.parentId=0";
				resultSet=st.executeQuery(sql);//执行查询 返回所有省份的集合
				while (resultSet.next()) {
					provinceMap=new HashMap<>();
					int pid=resultSet.getInt("cityid");//得到改城市的id 作父id
					String provinceName=resultSet.getString("cityname");
					//provinceMap.put("provinceName", provinceName);
					provinceMap.put("text", provinceName);
					provinceMap.put("id", pid);
					String tempSql="select  *  from  cityinfo  c where  c.parentId="+pid;
					subresultSet=cityst.executeQuery(tempSql);
					List cityList=new ArrayList<>();//城市的list
					while (subresultSet.next()) {
						Map cityMap=new  HashMap();
						int subId=subresultSet.getInt("cityid");//开始获取县 的父id 
						String cityName=subresultSet.getString("cityname");
						cityMap.put("subId", subId);
						//cityMap.put("cityName", cityName);
						cityMap.put("text", cityName);
						cityMap.put("id", subId);
						String tempSubSql="select  *  from  cityinfo  c where  c.parentId="+subId;
						countyResultSet=countyst.executeQuery(tempSubSql);
						List countyList=new ArrayList();
						while (countyResultSet.next() ){
							String countyName=countyResultSet.getString("cityname");
							int countyId=countyResultSet.getInt("cityid");
							Map countyMap=new  HashMap();
							//countyMap.put("countyId", countyId);
							countyMap.put("id", countyId);
							//countyMap.put("countyName", countyName);
							countyMap.put("text", countyName);
							countyList.add(countyMap);
						}
						//cityMap.put("countyList", countyList);//县放入到市的list中
						cityMap.put("nodes", countyList);
						cityList.add(cityMap);
					}
					//provinceMap.put("cityList", cityList);//市放入到省的list中
					provinceMap.put("nodes", cityList);
					jsonArray.add(provinceMap);
				}
				resultSet.close();
				subresultSet.close();
				countyResultSet.close();
				st.close();
				cityst.close();
				countyst.close();
				conn.close();
			 System.err.println(jsonArray.toString()); 
		} catch (Exception e) {
			System.err.println("-####cityJson####-"+e);
		}
		return null;

	}
	public static void main(String[] args) {
		//getAllInfo();
		//insertDB();
		//cityJson();
		Integer a=130;
		Integer b=130;
		System.err.println(a==b);
	}
	
}
