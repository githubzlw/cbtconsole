package com.cbt.test.catch_Page;

import com.cbt.jdbc.DBHelper;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//生成sitemap
public class SiteMap {

    public static void main(String[] args) {
        try {
            createXML();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 生成方法
    public static String createXML() throws Exception {
        String xmlurl = "";
        Element urlset = new Element("urlset");
        Document document = new Document(urlset);
        //文章链接的集合
        List<String> listStr=new ArrayList<String>();
        List<String> productInfoList = getProductInfo();
        for(int j=0;j<productInfoList.size();j++){
//            listStr.add("https://www.kidscharming.com/goodsinfo/"+productInfoList.get(j)+".com");
            listStr.add("https://www.petstoreinc.com/goodsinfo/"+productInfoList.get(j)+".html");
        }
        int i=1;
        for (String str : listStr) {
            System.out.println(str+"生成中..."+i);
            i++;
            //<!--必填标签,这是具体某一个链接的定义入口，每一条数据都要用<url>和</url>包含在里面，这是必须的 -->
            Element url = new Element("url");
            //<!--必填,URL链接地址,长度不得超过256字节-->
            Element loc = new Element("loc");
            loc.setText(str);
            url.addContent(loc);
            //<!--可以不提交该标签,用来指定该链接的最后更新时间-->
            Element lastmod = new Element("lastmod");
            lastmod.setText("2019-12-02");
            url.addContent(lastmod);
            //<!--可以不提交该标签,用这个标签告诉此链接可能会出现的更新频率 -->
            Element changefreq = new Element("changefreq");
            changefreq.setText("weekly");
            url.addContent(changefreq);
           //<!--可以不提交该标签,用来指定此链接相对于其他链接的优先权比值，此值定于0.0-1.0之间-->
            Element priority = new Element("priority");
            priority.setText("0.8");
            url.addContent(priority);
            urlset.addContent(url);
        }
        XMLOutputter XMLOut = new XMLOutputter();
        try {
            Format f = Format.getPrettyFormat();
            f.setEncoding("UTF-8");//default=UTF-8
            XMLOut.setFormat(f);
            String path = "D://sitemap//sitemap3.xml";
            XMLOut.output(document, new FileOutputStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlurl;
    }

    // 产品数据
    public static List<String> getProductInfo() {
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs=null;
        List<String> list=new ArrayList<String>();
//        String sql="select substring_index(REPLACE(REPLACE(enname,'\\'','-'),' ','-'),'-',10) as ennameNew,substring_index(REPLACE(path_catid,',','-'),'-',2) as pathcatidNew,CONCAT('1',pid) as pidNew from custom_benchmark_ready where valid=1 and  path_catid like '1038378,125386001,%'";
        //kids
//        String sql="select substring_index(REPLACE(REPLACE(enname,'\\'','-'),' ','-'),'-',10) as ennameNew,substring_index(REPLACE(path_catid,',','-'),'-',2) as pathcatidNew,CONCAT('1',pid) as pidNew from custom_benchmark_ready where valid=1 and   (path_catid like '%,1037002' or path_catid like '%,122110001' or path_catid like '%,123184002' or path_catid like '%,125296002' or path_catid like '%,125386001,%' or path_catid like '%,126128002,%' or path_catid like '%,1752,%' or path_catid like '%1501,%' or path_catid like '%1813,%' or path_catid like '%311,%') ";
        //pets
        String sql="select substring_index(REPLACE(REPLACE(enname,'\\'','-'),' ','-'),'-',10) as ennameNew,substring_index(REPLACE(path_catid,',','-'),'-',2) as pathcatidNew,CONCAT('1',pid) as pidNew from custom_benchmark_ready where valid=1 and   (path_catid like '%122916001,%') limit 10000,5000 ";
        try {
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while(rs.next()){

                String ennameNew = rs.getString("ennameNew");
                String pathcatidNew = rs.getString("pathcatidNew");
                String pidNew = rs.getString("pidNew");
                list.add(ennameNew+"-"+pathcatidNew+"-"+pidNew);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        DBHelper.getInstance().closeConnection(conn);
        return list;
    }
}
