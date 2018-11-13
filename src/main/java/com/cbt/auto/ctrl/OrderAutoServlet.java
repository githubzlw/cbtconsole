package com.cbt.auto.ctrl;

import com.cbt.auto.service.IOrderAutoService;
import com.cbt.auto.service.PreOrderAutoService;
import com.cbt.bean.AdmDsitribution;
import com.cbt.category.HexUtil;
import com.cbt.category.SecurityUtil;
import com.cbt.common.StringUtils;
import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.GoodsDistribution;
import com.cbt.util.AppConfig;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.service.PaymentConfirmServer;
import com.importExpress.service.AllotAdminService;
import com.importExpress.service.AllotAdminServiceImpl;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServlet;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 鑷姩鍒嗛厤閿�鍞�
 *
 * @author 鐜嬪畯鏉� 2016-10-10
 */
@SuppressWarnings("unchecked")
public class OrderAutoServlet extends HttpServlet {
    @Autowired
    private PaymentConfirmServer paymentConfirmServer;
    private static final long serialVersionUID = 1L;
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderAutoServlet.class);
    private static final int base = 5;
    IOrderAutoService preOrderAutoService = new PreOrderAutoService();
    public static final String[] FEMALE_FIRST_NAMES = {
            "Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan",
            "Margaret", "Dorothy", "Lisa", "Nancy", "Karen", "Betty", "Helen", "Sandra", "Donna",
            "Carol", "Ruth", "Sharon", "Michelle", "Laura", "Sarah", "Kimberly", "Deborah", "Jessica",
            "Shirley", "Cynthia", "Angela", "Melissa", "Brenda", "Amy", "Anna", "Rebecca", "Virginia",
            "Kathleen", "Pamela", "Martha", "Debra", "Amanda", "Stephanie", "Carolyn", "Christine",
            "Marie", "Janet", "Catherine", "Frances", "Ann", "Joyce", "Diane", "Alice", "Julie",
            "Heather", "Teresa", "Doris", "Gloria", "Evelyn", "Jean", "Cheryl", "Mildred", "Katherine",
            "Joan", "Ashley", "Judith", "Rose", "Janice", "Kelly", "Nicole", "Judy", "Christina",
            "Kathy", "Theresa", "Beverly", "Denise", "Tammy", "Irene", "Jane", "Lori", "Rachel",
            "Marilyn", "Andrea", "Kathryn", "Louise", "Sara", "Anne", "Jacqueline", "Wanda", "Bonnie",
            "Julia", "Ruby", "Lois", "Tina", "Phyllis", "Norma", "Paula", "Diana", "Annie", "Lillian",
            "Emily", "Robin", "Peggy", "Crystal", "Gladys", "Rita", "Dawn", "Connie", "Florence",
            "Tracy", "Edna", "Tiffany", "Carmen", "Rosa", "Cindy", "Grace", "Wendy", "Victoria", "Edith",
            "Kim", "Sherry", "Sylvia", "Josephine", "Thelma", "Shannon", "Sheila", "Ethel", "Ellen",
            "Elaine", "Marjorie", "Carrie", "Charlotte", "Monica", "Esther", "Pauline", "Emma",
            "Juanita", "Anita", "Rhonda", "Hazel", "Amber", "Eva", "Debbie", "April", "Leslie", "Clara",
            "Lucille", "Jamie", "Joanne", "Eleanor", "Valerie", "Danielle", "Megan", "Alicia", "Suzanne",
            "Michele", "Gail", "Bertha", "Darlene", "Veronica", "Jill", "Erin", "Geraldine", "Lauren",
            "Cathy", "Joann", "Lorraine", "Lynn", "Sally", "Regina", "Erica", "Beatrice", "Dolores",
            "Bernice", "Audrey", "Yvonne", "Annette", "June", "Samantha", "Marion", "Dana", "Stacy",
            "Ana", "Renee", "Ida", "Vivian", "Roberta", "Holly", "Brittany", "Melanie", "Loretta",
            "Yolanda", "Jeanette", "Laurie", "Katie", "Kristen", "Vanessa", "Alma", "Sue", "Elsie",
            "Beth", "Jeanne"};

    public void PreOrderAutoToSale() {
        LOG.info("=======================寮�濮嬭嚜鍔ㄥ垎閰嶉攢鍞�=========================");
        IOrderAutoService preOrderAutoService = new PreOrderAutoService();
        AllotAdminService service = new AllotAdminServiceImpl();
        List<AutoToSalePojo> userIdList = preOrderAutoService.getOrderAutoToSale();
        LOG.info("=======================鑷姩鍒嗛厤閿�鍞暟閲忋��" + userIdList.size()
                + "銆�=========================");
        for (int i = 0; i < userIdList.size(); i++) {
            AutoToSalePojo b = userIdList.get(i);
            int userId = b.getUserid();
            String email = b.getEmail();
            String username = b.getUsername();
            String orderAdmin = b.getOrderAdmin();
            service.allotAdminSaler(-1, userId, "", email, orderAdmin, username);
        }
        LOG.info("=======================缁撴潫鑷姩鍒嗛厤閿�鍞�=========================");
    }

    /**
     *
     * @param goodoab
     *            鍙備笌鍒嗛厤閲囪喘鐨勫晢鍝佸疄浣撶被
     * @param userList
     *            瀛樻斁鎵�鏈夐噰璐殑闆嗗悎
     * @param map
     *            閲囪喘瀵瑰簲鐨勪笓闀�
     * @param autoCount
     *            閲囪喘鍒嗛厤鎯呭喌闆嗗悎
     * @return 鍒嗛厤鐨勯噰璐汉鍛榠d
     */
    public String getAdminid(OrderAutoBean goodoab, List userList,
                             Map<Integer, List<String>> map, Map<Integer, Integer> autoCount) {
        String adminId = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (goodoab.getUserid() == 248 || goodoab.getUserid() == 4909
                || goodoab.getUserid() == 8953) {
            adminId = String.valueOf("9");
        } else {
            List<AdmDsitribution> adList = new ArrayList<AdmDsitribution>();
            for (int i = 0; i < userList.size(); i++) {
                AdmDsitribution a = preOrderAutoService
                        .getAdmuserNoComplete(userList.get(i).toString());
                adList.add(a);
            }
            int admuserId = CalculationMin(adList);
            adminId = String.valueOf(admuserId);
        }
        return adminId;
    }

    public String getAdmId(String cid){
        cid=preOrderAutoService.getFirdstCid(cid);
        String admuserid="9";
        if("57".equals(cid) || "7".equals(cid) || "509".equals(cid) || "69".equals(cid)){
            admuserid="9";//camry
        }else if("1038378".equals(cid) || "1042954".equals(cid) || "10165".equals(cid) || "67".equals(cid)){
            admuserid="58";//Jessie
        }else if("1501".equals(cid) || "58".equals(cid) || "18".equals(cid) || "20005194".equals(cid)){
            admuserid="53";//Mindy
        }else if("97".equals(cid) || "71".equals(cid) || "122916001".equals(cid) || "7".equals(cid)){
            admuserid="9";//灏忕敯
        }else if("54".equals(cid) || "124268003".equals(cid) || "122194001".equals(cid) || "17".equals(cid)){
            admuserid="50";//Alisa
        }else if("10166".equals(cid) || "13".equals(cid) || "1813".equals(cid) || "6".equals(cid) || "13".equals(cid)){
            admuserid="58";//Kyra
        }
        return admuserid;
    }

    public void insertDG(Map<Integer, Integer> autoCount,
                         OrderAutoBean goodoab, String adminId, String order) {
        Date date = new Date();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df1.format(date);
        Timestamp CreateDate = Timestamp.valueOf(time);
        GoodsDistribution gd = new GoodsDistribution();
        gd.setOdid(Integer.valueOf(goodoab.getOdid()));// order_detail id
        gd.setAdmuserid(Integer.valueOf(adminId));// admuser id
        gd.setGoodsdataid(String.valueOf(goodoab.getGoodsdataid()));//
        gd.setGoodsid(goodoab.getGoodsid());// 鍟嗗搧id
        gd.setCreateTime(CreateDate);// 鍒涘缓璁板綍鏃堕棿
        gd.setOrderid(order);// 宸ュ崟鍙�
        gd.setGoodscatid(goodoab.getGoodscatid());
        gd.setDistributionid("0");// 0浠ｈ〃鑷姩鍒嗛厤1浠ｈ〃鎵嬪姩鍒嗛厤
        gd.setGoods_pid(goodoab.getGoods_pid());
        int row = preOrderAutoService.insertDG(gd);
//        if (row > 0) {
//            if(autoCount.get(Integer.valueOf(adminId))==null){
//                adminId="68";
//            }
//            int index1 = autoCount.get(Integer.valueOf(adminId));
//            autoCount.remove(Integer.valueOf(adminId));
//            autoCount.put(Integer.valueOf(adminId), index1 + 1);
//        }
    }

    /**
     * 导入商品评论
     */
    public static void importCommText(){
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        try{
            File readfile = new File("D:/销量前300产品评论camry.xls");
            Workbook book = Workbook.getWorkbook(readfile);
            Sheet sheet1 = book.getSheet(0);
            for (int j = 1; j < sheet1.getRows(); j++) {
                String pid=sheet1.getCell(1, j).getContents();
                String c1=sheet1.getCell(2, j).getContents();
                String c2=sheet1.getCell(3, j).getContents();
                String c3=sheet1.getCell(4, j).getContents();
                System.out.println("pid:"+pid);
                System.out.println("c1:"+c1);
                System.out.println("c2:"+c2);
                System.out.println("c3:"+c3);
                StringBuffer sql=new StringBuffer();
                sql.append("insert into goods_comments_real(goods_pid,comments_content) values");
                if(StringUtil.isNotBlank(c1)){
                    c1=c1.replace("'","#");
                    sql.append("('"+pid+"','"+c1+"')");
                }
                if(StringUtil.isNotBlank(c2)){
                    c2=c2.replace("'","#");
                    sql.append(",('"+pid+"','"+c2+"')");
                }
                if(StringUtil.isNotBlank(c3)){
                    c3=c3.replace("'","#");
                    sql.append(",('"+pid+"','"+c3+"')");
                }
                LOG.info("sql========="+sql);
                stmt=conn.prepareStatement(sql.toString());
                stmt.executeUpdate();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String resetAdminid(int amount, String adminId, int i,
                               List<String> isAuto, List userList,
                               List<OrderAutoBean> orderDetailInfo,
                               Map<Integer, List<String>> map, Map<Integer, Integer> autoCount) {
        if (!isAuto.contains(adminId)) {
            isAuto.add(adminId);
        } else {
            int count_ = i * base;
            while (true) {
                count_ = count_ + 1;
                if (count_ < (i == 0 ? ((i + 1) * base) : orderDetailInfo
                        .size())) {
                    adminId = this.getAdminid(orderDetailInfo.get(count_),
                            userList, map, autoCount);
                    if (!isAuto.contains(adminId)) {
                        isAuto.add(adminId);
                        return adminId;
                    }
                } else {
                    if (isAuto.size() < userList.size()) {
                        for (int m = 0; m < userList.size(); m++) {
                            if (!isAuto.contains(userList.get(m))) {
                                adminId = String.valueOf(userList.get(m));
                                isAuto.add(adminId);
                                return adminId;
                            }
                        }
                    } else {
                        adminId = String.valueOf(userList.get(0));
                        isAuto.add(adminId);
                        return adminId;
                    }
                }
            }
        }
        return adminId;
    }

    public void importType(){
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        try{
            File readfile = new File("D:/缂撳瓨鐑悳璇�.xls");
            Workbook book = Workbook.getWorkbook(readfile);
            Sheet sheet1 = book.getSheet(0);
            for (int j = 0; j < sheet1.getRows(); j++) {
                String keys=sheet1.getCell(0, j).getContents();
                String sql="insert ignore into cache_keysord(keyword) values('"+keys+"')";
                LOG.info("sql========="+sql);
                stmt=conn.prepareStatement(sql);
                stmt.executeUpdate();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void importEnName(){
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs=null;
        try{
            String sql="select * from 1688_cate_0212";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            while(rs.next()){
                String en_name=rs.getString("en_name");
                if(!StringUtils.isStrNull(en_name)){
                    en_name = en_name.substring(0, 1).toUpperCase() + en_name.substring(1);
                    sql="update 1688_cate_0212 set en_name='"+en_name+"' where id='"+rs.getInt("id")+"'";
                    stmt=conn.prepareStatement(sql);
                    stmt.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        DBHelper.getInstance().closeConnection(conn);
    }

    public static void updateCategoryEnName(){
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        try{
            File readfile = new File("D:/liebie/1688类别名翻译优化-ROUND13-MAKE UP.xls");
            Workbook book = Workbook.getWorkbook(readfile);
            Sheet sheet1 = book.getSheet(0);
            for (int j = 1; j < sheet1.getRows(); j++) {
                String cate_id=sheet1.getCell(1, j).getContents();
                String en_name=sheet1.getCell(4, j).getContents().replace("'", "#");
                if(en_name==null || "".equals(en_name)){
                    continue;
                }
                String sql="update 1688_category set en_name='"+en_name+"' where category_id='"+cate_id+"'";
                LOG.info("sql========="+sql+"j=========="+j);
                stmt=conn.prepareStatement(sql);
                stmt.executeUpdate();
            }
//			UPDATE 1688_category SET en_name=REPLACE(en_name,'#',"'") WHERE en_name LIKE '%#%'
        }catch(Exception e){
            e.printStackTrace();
        }finally {

        }
    }


    public static void getPath(){
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs=null;
        try{
            String sql="select * from 1688_category where lv=2";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            while(rs.next()){
                String p_path=rs.getString("path");
                String c_id=rs.getString("childids");
                String c_ids[]=c_id.split(",");
                for (String s : c_ids) {
                    System.out.println("瀛愯妭鐐癸細"+s);
                    String pa_id=p_path+","+s;
                    System.out.println("path="+pa_id);
                    sql="update 1688_category set path='"+pa_id+"' where category_id='"+s+"'";
                    stmt=conn.prepareStatement(sql);
                    stmt.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 优先类别数据导入
     */
    public static void importCategory(){
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        try{
            File readfile = new File("D:/camry优先类别数据.xls");
            Workbook book = Workbook.getWorkbook(readfile);
            Sheet sheet1 = book.getSheet(0);
            StringBuffer sql=new StringBuffer();
            sql.append("insert into priority_category(keyword,category) values");
            for (int j = 0; j < sheet1.getRows(); j++) {
                String key=sheet1.getCell(0, j).getContents();
                String cid=sheet1.getCell(1, j).getContents();
                System.out.println("pid:"+key);
                System.out.println("c1:"+cid);
//				if(StringUtil.isNotBlank(c1)){
//					c1=c1.replace("'","#");
                sql.append("('"+key+"','"+cid+"'),");
//				}
//				if(StringUtil.isNotBlank(c2)){
//					c2=c2.replace("'","#");
//					sql.append(",('"+pid+"','"+c2+"')");
//				}
            }
            LOG.info("sql========="+sql);
            if(sql.length()>50){
                stmt=conn.prepareStatement(sql.toString().substring(0,sql.length()-1));
                stmt.executeUpdate();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        DBHelper.getInstance().closeConnection(conn);
    }

    public static void main(String[] args) {
//		String en_name=DownloadMain.getContentClient("http://192.168.1.27:9089/translation/gettrans?catid=0&cntext=濂崇敤鍣ㄥ叿",null);
//		System.out.println("en_name="+en_name);
//		get1688cate();
//		getPath();
//		try{
//			Connection conn = DBHelper.getInstance().getConnection();
//			 PreparedStatement stmt = null;
//				ResultSet rs=null;
//			String sql="select * from 1688_cate_0212 where lv>0 and en_name is null";
//			stmt=conn.prepareStatement(sql);
//			rs=stmt.executeQuery();
//			while(rs.next()){
//				String cnName=rs.getString("name");
//				String en_name=DownloadMain.getContentClient("http://192.168.1.27:9089/translation/gettrans?catid=0&cntext="+cnName,null);
//				en_name=en_name.replace("'", "");
//				System.out.println("en_name="+en_name);
//				sql="update 1688_cate_0212 set en_name='"+en_name+"' where id="+rs.getString("id")+"";
//				stmt=conn.prepareStatement(sql);
//				stmt.executeUpdate();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		try{
//			Connection conn = DBHelper.getInstance().getConnection();
//			 PreparedStatement stmt = null;
//			File readfile = new File("D:/342535555.xls");
//			Workbook book = Workbook.getWorkbook(readfile);
//			Sheet sheet1 = book.getSheet(0);
//			for (int j = 1; j < sheet1.getRows(); j++) {
//				String cate_id=sheet1.getCell(2, j).getContents();
//				String en_name=sheet1.getCell(4, j).getContents().replace("'", "#");
//				String sql="update 1688_cate_0212 set en_name='"+en_name+"' where category_id='"+cate_id+"'";
//				LOG.info("sql========="+sql+"j=========="+j);
//				stmt=conn.prepareStatement(sql);
//				stmt.executeUpdate();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		get1688cate();
//		updateCategoryEnName();
//		importCommText();
//		importCategory();
//        get1688cate();
//		try{
//			Map<String, String> params=new HashMap<String, String>();
//			params.put("webSite","1688");
//			params.put("categoryID","1");
//			String _aop_signature=	HexUtil.encode(HmacSHA1Encrypt("param2/1/com.alibaba.product/alibaba.category.get/9461223categoryID4webSite1688","5os1gmNQpA5")).toUpperCase();
//			System.out.println("_aop_signature="+_aop_signature);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
	    get1688cate();
    }


    public static void updateYFHShipment(){
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        try{
			File readfile = new File("D:/updateyfh.xls");
			Workbook book = Workbook.getWorkbook(readfile);
			Sheet sheet1 = book.getSheet(0);
			for (int j =0; j < sheet1.getRows(); j++) {
				String expressno=sheet1.getCell(0, j).getContents();
                String country=sheet1.getCell(1, j).getContents();
                String weight=sheet1.getCell(2, j).getContents();
                if(StringUtil.isNotBlank(expressno)){
                    String sql="update shipment set country='"+country+"',realWeight="+weight+",bulkWeight="+weight+",settleWeight="+weight+" where orderNo='"+expressno+"'";
                    LOG.info("sql========="+sql+"j=========="+j);
                    stmt=conn.prepareStatement(sql);
                    stmt.executeUpdate();
                }
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
            DBHelper.getInstance().closeConnection(conn);
            DBHelper.getInstance().closePreparedStatement(stmt);
        }
    }

    public static String signatureWithParamsOnly(Map<String, String> params, String appSecretKey){
        List<String> paramValueList = new ArrayList<String>();
        if(params != null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramValueList.add(entry.getKey() + entry.getValue());
            }
        }
        Collections.sort(paramValueList);
        String[] datas = new String[paramValueList.size()];
        paramValueList.toArray(datas);
        byte[] signature = SecurityUtil.hmacSha1(datas, com.cbt.category.StringUtil.toBytes(appSecretKey));
        return com.cbt.category.StringUtil.encodeHexStr(signature);
    }
    /**
     * 1688绫诲埆鑾峰彇
     * @Title get1688cate
     * @Description TODO
     * @return void
     * @throws IOException
     */
    public static void get1688cate(){
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs=null;
        try {
            for(int i=0;i<=6;i++){
                System.out.println("开始抓取第【"+i+"】级下的类别数据");
                String sql="select * from 1688_category_0828 where lv="+i+"";
                stmt=conn.prepareStatement(sql);
                rs=stmt.executeQuery();
                int k=1;
                while(rs.next()){
                    String childIds=rs.getString("childids");
                    String parent_id=rs.getString("category_id");
                    if(childIds!=null && !"".equals(childIds)){
                        System.out.println("childIds==="+childIds);
                        if(childIds.indexOf(",")>-1){
                            String [] str=childIds.split(",");
                            for (String s : str) {
                                getData(s,i,parent_id);
                            }
                        }else{
                            System.out.println("childIds==="+childIds);
                            getData(childIds,i,parent_id);
                        }
                    }
                    k++;
                }
                System.out.println("结束抓取第【"+i+"】级下的类别数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
	        DBHelper.getInstance().closeConnection(conn);
	        DBHelper.getInstance().closeResultSet(rs);
	        DBHelper.getInstance().closePreparedStatement(stmt);
        }
    }


    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception
    {
        byte[] data=encryptKey.getBytes("UTF-8");
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance("HmacSHA1");
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes("UTF-8");
        //完成 Mac 操作
        return mac.doFinal(text);
    }

    public static void getData(String catid,int i,String parent_id){
        System.out.println("准备抓取："+catid);
        String jsons="";
        BufferedReader read=null;
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs=null;
        String lvs="";
        String category_id="";
        String childID="";
        try{
            String sql="select * from 1688_category_0828 where category_id="+catid+"";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            if(!rs.next()){
                System.out.println("准备抓取："+catid+"未抓取");
                String _aop_signature=	HexUtil.encode(HmacSHA1Encrypt("param2/1/com.alibaba.product/alibaba.category.get/9461223categoryID"+catid+"webSite1688","5os1gmNQpA5")).toUpperCase();
                System.out.println("catid==="+catid+"          _aop_signature==="+_aop_signature);
                URL realurl=new URL("https://gw.open.1688.com/openapi/param2/1/com.alibaba.product/alibaba.category.get/9461223?webSite=1688&categoryID="+catid+"&_aop_signature="+_aop_signature+"");
//				URL realurl=new URL("https://gw.open.1688.com/openapi/param2/1/com.alibaba.product/alibaba.category.get/9461223?webSite=1688&categoryID=2&_aop_signature=E9D3D234E909F68FE027BD707F40DD89A527D15E");
                URLConnection connection=realurl.openConnection();
//   			 connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//				connection.setRequestProperty("Content-Length", "81");
//				connection.setRequestProperty("Host", "gw.open.1688.com");
//				connection.setRequestProperty("Connection", "Keep-Alive");
//                connection.setRequestProperty("user-agent", "Apache-HttpClient/4.5 (Java/1.7.0_75)");
//				connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                connection.connect();
                read = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
                String line;
                System.out.println("开始获取数据");
                while ((line = read.readLine()) != null) {
                    jsons += line;
                }
                System.out.println("result="+jsons);
                if(!jsons.contains("category does not exist") && jsons.indexOf("[")>-1){
                    System.out.println("开始解析数据");
                    StringBuffer bf=new StringBuffer();
                    String [] s=jsons.split(",");
                    category_id=s[0].split(":")[s[0].split(":").length-1];
                    String chName=jsons.split("enName")[0].split(":")[jsons.split("enName")[0].split(":").length-1].replaceAll("\"","");
                    chName=chName.substring(0,chName.length()-1);
                    String lv=jsons.split("isLeaf")[0].split(":")[jsons.split("isLeaf")[0].split(":").length-1].replaceAll("\"","");
                    lv=lv.substring(0,lv.length()-1);
                    lvs=lv;
                    String msg=jsons.split("errorMsg")[0].split("childIDs")[1].split(":")[1].split("]")[0];
                    msg=msg.substring(1,msg.length());
                    Thread.sleep(500);
                    if(msg.indexOf(",")>-1){
                        String [] childid_s=msg.split(",");
                        int a[]=new int[childid_s.length];
                        for(int m=0;m<childid_s.length;m++){
                            a[m]=Integer.valueOf(childid_s[m]);
                        }
                        for(int k=0;k<a.length-1;k++){
                            for(int o=k+1;o<a.length;o++){
                                if (a[k]>a[o]){
                                    int temp=a[k];
                                    a[k]=a[o];
                                    a[o]=temp;
                                }
                            }
                        }
                        for(int p=0;p<a.length;p++){
                            bf.append(a[p]).append(",");
                        }
                        childID=bf.toString().substring(0,bf.toString().length()-1);
                    }
                    sql="insert into 1688_category_0828 (lv,category_id,name,childids,parent_id,createtime) values(?,?,?,?,?,now())";
                    stmt=conn.prepareStatement(sql);
                    stmt.setInt(1, (i+1));
                    stmt.setString(2, category_id);
                    stmt.setString(3, lvs);
                    stmt.setString(4, childID);
                    stmt.setString(5, parent_id);
                    stmt.executeUpdate();
                    System.out.println("结束解析数据");
                }
            }else{
                System.out.println("准备抓取："+catid+"已抓取");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
	        DBHelper.getInstance().closeConnection(conn);
	        DBHelper.getInstance().closeResultSet(rs);
	        DBHelper.getInstance().closePreparedStatement(stmt);
        }
    }

    public void compareSF(){
        List<String> list=new ArrayList<String>();
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs=null;
        try {
            File readfile = new File("D:/9-12鏈圫F杩愯垂.xls");
            Workbook book = Workbook.getWorkbook(readfile);
            Sheet sheet1 = book.getSheet(0);
            for (int j = 0; j < sheet1.getRows(); j++) {
                String orderNo=sheet1.getCell(0, j).getContents();
                double totalprice=Double.valueOf(sheet1.getCell(1, j).getContents());
                String sql="select * from shipment where orderNo='"+orderNo.trim()+"' and transportCompany='SF' and sentTime>='2016-09-01 00:00:00' and sentTime<='2016-12-31 23:59:59'";
                LOG.info("sql========="+sql);
                stmt=conn.prepareStatement(sql);
                rs=stmt.executeQuery();
                if(rs.next()){
                    double price=rs.getDouble("totalprice");
                    if(totalprice-price!=0){
                        list.add(orderNo);
                    }
                }else{
                    list.add(orderNo);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(list.toString());
    }

    public void importComments(){
        int a[]={2,6,12,13,24,35,36,43};
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        try {
            File readfile = new File("D:/4564564564.xls");
            Workbook book = Workbook.getWorkbook(readfile);
            Sheet sheet1 = book.getSheet(0);
            int index=0;
            for (int j = 0; j < sheet1.getRows(); j++) {
                String goods_type="13";
                String comments=sheet1.getCell(1, j).getContents();
                String custom_name=FEMALE_FIRST_NAMES[j];
                int custom_country=a[index];
                index=index+1;
                if(index==7){
                    index=0;
                }
                if(comments!=null && !"".equals(comments)){
                    String sql="insert into goods_comments (goods_type,custom_name,custom_country,comments,comment_time) values(?,?,?,?,now())";
                    stmt=conn.prepareStatement(sql);
                    stmt.setString(1, goods_type);
                    stmt.setString(2, custom_name);
                    stmt.setInt(3, custom_country);
                    stmt.setString(4, comments);
                    stmt.executeUpdate();
                }
            }
            Sheet sheet2 = book.getSheet(1);
            int index2=0;
            for (int j = 0; j < sheet2.getRows(); j++) {
                String goods_type="322";
                String comments=sheet2.getCell(1, j).getContents();
                String custom_name=FEMALE_FIRST_NAMES[j];
                int custom_country=a[index2];
                index2=index2+1;
                if(index2==7){
                    index2=0;
                }
                if(comments!=null && !"".equals(comments)){
                    String sql="insert into goods_comments (goods_type,custom_name,custom_country,comments,comment_time) values(?,?,?,?,now())";
                    stmt=conn.prepareStatement(sql);
                    stmt.setString(1, goods_type);
                    stmt.setString(2, custom_name);
                    stmt.setInt(3, custom_country);
                    stmt.setString(4, comments);
                    stmt.executeUpdate();
                }
            }
            Sheet sheet3 = book.getSheet(2);
            int index3=0;
            for (int j = 0; j < sheet3.getRows(); j++) {
                String goods_type="26";
                String comments=sheet3.getCell(1, j).getContents();
                String custom_name=FEMALE_FIRST_NAMES[j];
                int custom_country=a[index3];
                index3=index3+1;
                if(index3==7){
                    index3=0;
                }
                if(comments!=null && !"".equals(comments)){
                    String sql="insert into goods_comments (goods_type,custom_name,custom_country,comments,comment_time) values(?,?,?,?,now())";
                    stmt=conn.prepareStatement(sql);
                    stmt.setString(1, goods_type);
                    stmt.setString(2, custom_name);
                    stmt.setInt(3, custom_country);
                    stmt.setString(4, comments);
                    stmt.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("===============瀹屾垚==================");
    }




    public void cancalTbOrder1(){
        int index=0;
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> set=new ArrayList<String>();
        StringBuffer bf=new StringBuffer();
        try {
            File readfile = new File("D:/2017骞撮噰璐鍗�/1688/1鏈堜唤/1鏈堜唤鎬绘敮鍑�1.xls");
            Workbook book = Workbook.getWorkbook(readfile);
            // 鑾峰緱绗竴涓伐浣滆〃瀵硅薄
            Sheet sheet1 = book.getSheet(0);
            Map<String,Double> map=new HashMap<String, Double>();
            List<String> list1=new ArrayList<String>();
            for (int j = 0; j < sheet1.getRows(); j++) {
                String orderid=sheet1.getCell(0, j).getContents().trim();
                double totalprice=Double.valueOf(sheet1.getCell(3, j).getContents().trim().replace(",", ""));
                list1.add(orderid);
                map.put(orderid, totalprice);//1鏈堜唤鑻忎互鍚�1688璁㈠崟鐢靛晢
            }
//				Sheet sheet2 = book.getSheet(1);
//				Map<String,Double> map1=new HashMap<String, Double>();
//				List<String> list2=new ArrayList<String>();
//				for (int j = 0; j < sheet2.getRows(); j++) {
//					String orderid=sheet2.getCell(0, j).getContents().trim();
//					double totalprice=Double.valueOf(sheet2.getCell(1, j).getContents().trim().replace(",", ""));
//					list2.add(orderid);
//					map1.put(orderid, totalprice);//1鏈堜唤鑻忎互鍚�1688璁㈠崟鐢靛晢
//				}
//				Sheet sheet2 = book.getSheet(1);
//				List<String> list=new ArrayList<String>();
//				for (int j = 0; j < sheet2.getRows(); j++) {
//					String orderid=sheet2.getCell(0, j).getContents().trim();
//					list.add(orderid);//1鏈堜唤鎵�鏈夐��娆捐鍗�
//				}
            int a=1;
            for(int i=0;i<list1.size();i++){
                String sql="SELECT DISTINCT tbOr1688,orderid,totalprice,paydata,username FROM taobao_1688_order_history WHERE orderstatus<>'浜ゆ槗鍏抽棴' "
                        + "AND LOCATE('閫�娆�',orderstatus)<1 and  paydata>='2017-01-01' and paydata<='2017-01-31 23:59:59' AND tbOr1688  "
                        + "in('1') and orderid='"+list1.get(i)+"'";
                stmt=conn.prepareStatement(sql);
                rs=stmt.executeQuery();
                if(rs.next()){
                    String orderid=rs.getString("orderid");
                    double totalprice=rs.getDouble("totalprice");
                    if(map.get(orderid)!=null){
                        double price=map.get(orderid);
                        if(price-totalprice!=0){
                            set.add(orderid);
                        }
                    }else{
                        set.add(orderid);
                    }
                }else{
                    set.add(list1.get(i));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        try {
            if(conn!=null){
                conn.close();
            }
            if(rs!=null){
                rs.close();
            }
            if(stmt!=null){
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOG.info("============"+set.toString());
        LOG.info("index========="+index);
    }


    public void cancalTbOrder(){
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql="select id,itemurl from taobao_1688_order_history where paydata>='2017-04-01 00:00:00' and itemurl<>'' and tbOr1688 in ('0','1')";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            while(rs.next()){
                String itemid="0";
                if(rs.getString("itemurl").indexOf("1688")>-1 || rs.getString("itemurl").indexOf("taobao")>-1){
                    itemid = getItemid(rs.getString("itemurl"));
                }
                if(!"0".equals(itemid)){
                    sql="update taobao_1688_order_history set itemid='"+itemid+"' where id="+rs.getInt("id")+"";
                    stmt=conn.prepareStatement(sql);
                    System.out.println("sql=="+sql);
                    stmt.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("============");
    }

    public String getItemid(String u) {
        if (u.length() < 12) {// http://aaa&123
            return "0";
        }
        String ret = "";
        Pattern p = Pattern.compile("\\d{2,}");// 杩欎釜2鏄寚杩炵画鏁板瓧鐨勬渶灏戜釜鏁�
        String maxStr = "";
        Matcher m = p.matcher(u);
        int i = 0;
        while (m.find()) {
            String temp = m.group();
            int c = u.indexOf(temp);
            int len = c + m.group().length() + 5;
            if (len > u.length()) {
                len = c + m.group().length();
            }
            temp = u.substring(c - 4, len);
            if (temp.indexOf("?id=") != -1 || temp.indexOf("&id=") != -1
                    || temp.indexOf(".html") != -1) {
                if (m.group().length() > maxStr.length()) {
                    maxStr = m.group();
                }
            }
            i++;
        }
        ret = maxStr;
        return ret;
    }

   /* public void importInventory() {
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Workbook book = Workbook.getWorkbook(new File("D:/鏂板缓 Microsoft Excel 97-2003 宸ヤ綔琛�.xls"));
            // 鑾峰緱绗竴涓伐浣滆〃瀵硅薄
            Sheet sheet = book.getSheet(0);
            for (int i = 1; i < sheet.getRows(); i++) {
//				String good_name=sheet.getCell(0, i).getContents().replace("'", " ");
//				String goods_url=sheet.getCell(1, i).getContents();
//				String barcode=sheet.getCell(2, i).getContents();
//				String sku=sheet.getCell(3, i).getContents();
//				String goods_p_url=sheet.getCell(4, i).getContents();
//				String goods_p_price=sheet.getCell(5, i).getContents().replace("锟�", "");
//				String goodscatid=sheet.getCell(6, i).getContents().replace("'", " ");
//				String new_remaining=sheet.getCell(7, i).getContents();
//				String new_inventory_amount=sheet.getCell(8, i).getContents();
//				String car_img="";
                System.out.println(sheet.getCell(0, i).getContents() + "===="
                        + sheet.getCell(1, i).getContents());
                String sql = "SELECT ops.goods_url,ops.tb_1688_itemid,ops.goods_name,od.car_type,ops.goods_p_url,od.goodscatid,od.car_img,ops.goods_p_price FROM order_details od INNER JOIN order_product_source ops ON od.id=ops.od_id WHERE od.goodsid='"
                        + sheet.getCell(0, i).getContents() + "'";
                stmt = conn.prepareStatement(sql.toString());
                rs = stmt.executeQuery();
                if(rs.next()){
                    sql = "INSERT INTO inventory (goods_url,remaining,new_remaining,can_remaining,good_name,sku,barcode,"
                            + "goods_p_url,goodscatid,inventory_amount,new_inventory_amount,car_img,goods_p_price,flag,createtime) select '"
                            + rs.getString("goods_url")
                            + "','"
                            + 0
                            + "','"
                            + sheet.getCell(1, i).getContents()
                            + "','"
                            + sheet.getCell(1, i).getContents()
                            + "','"
                            + rs.getString("goods_name").replace("'", "")
                            + "','"
                            + rs.getString("car_type")
                            + "','STK9','"
                            + rs.getString("goods_p_url")
                            + "','','"
                            + 0
                            + "','"
                            + Integer.valueOf(sheet.getCell(1, i).getContents())*Double.valueOf(rs.getString("goods_p_price"))
                            + "','"
                            + rs.getString("car_img")
                            + "','"
                            + rs.getString("goods_p_price")
                            + "',"
                            + 1
                            + ",now() "
                            + "FROM DUAL WHERE NOT EXISTS (SELECT * FROM inventory WHERE goods_url='"
                            + rs.getString("goods_url")
                            + "' and sku='"
                            + rs.getString("car_type")
                            + "' and barcode='STK9')";
                    System.out.println("==" + sql.toString());
                    stmt = conn.prepareStatement(sql.toString());
                    stmt.executeUpdate();
                }
            }
            book.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/

    /**
     * 比较采购最多和最少数量的差值
     * @param mapList
     * @return
     */
    public String getbuyLess(Map<Integer, Integer> mapList) {
	    String adminId = "0";
	    if(mapList.size()>0){
		    Collection<Integer> c = mapList.values();
		    Object[] obj = c.toArray();
		    Arrays.sort(obj);
		    int max=(int)obj[obj.length-1];
		    int min=(int)obj[0];
		    if(max-min>=10){
			    Iterator it = mapList.keySet().iterator();
			    while (it.hasNext()) {
				    int key;
				    key = Integer.valueOf(it.next().toString());
				    if(mapList.get(key)==min){
					    adminId = String.valueOf(key);
					    break;
				    }
			    }
		    }
	    }
	    return adminId;
    }

    /**
     * 获取map中采购数量最少的采购人id
     * @param mapList
     * @return
     */
    public String getId(Map<Integer, Integer> mapList) {
        String adminId = "";
        Collection<Integer> c = mapList.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        Iterator it = mapList.keySet().iterator();
        while (it.hasNext()) {
            int key;
            key = Integer.valueOf(it.next().toString());
            if (mapList.get(key) == obj[0]) {
                adminId = String.valueOf(key);
                break;
            }
        }
        return adminId;
    }

    /**
     * 获取map中采购数量最多的采购人id
     * @param mapList
     * @return
     */
    public String getMaxId(Map<Integer, Integer> mapList) {
        String adminId = "";
        Collection<Integer> c = mapList.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        Iterator it = mapList.keySet().iterator();
        while (it.hasNext()) {
            int key;
            key = Integer.valueOf(it.next().toString());
            if (mapList.get(key) == obj[obj.length-1]) {
                adminId = String.valueOf(key);
                break;
            }
        }
        return adminId;
    }

    public void insertCheck(){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = DBHelper.getInstance().getConnection();
        try{
            String sql="SELECT goodsid,goodsname,car_url,car_img FROM order_details WHERE orderid='Q219641350545468'";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            while(rs.next()){
                sql="INSERT INTO goodsdatacheck(id,goodsname,url,imgurl,imgpath) value ('"+rs.getString("goodsid")+"','"+rs.getString("goodsname")+"','"+rs.getString("car_url")+"','"+rs.getString("car_img")+"','"+rs.getString("car_img")+"')";
                stmt=conn.prepareStatement(sql);
                stmt.executeUpdate();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("end====================================");
    }

    /**
     * 淇敼褰曞叆璐ф簮鏃堕棿涓虹┖鐨勮揣婧愯褰�
     */
    public void PreAutoSourceAddTime(){
        preOrderAutoService.getPreAutoSourceAddTime();
    }

    /**
     * 新采购、销售员分配
     * whj
     */
    public void newPreOrderAutoDistribution() {
        try{
            List<String> orderPayList=preOrderAutoService.getUnassignedOrders();
            LOG.info("未分配采购订单数量:" + orderPayList.size());
            //纯销售账号
	        List<String> salesList = preOrderAutoService.getPureSalesAdmUser();
	        //纯采购账号
	        List<String> purchaseList = preOrderAutoService.getPurePurchaseAdmUser();
	        //采销一体账号
	        List<String> userList = preOrderAutoService.getAllAdmUser();
	        List<String> saleList=new ArrayList<String>();
            List<String> buyList=new ArrayList<String>();
            //销售人员
	        if(salesList.size()>0){
                saleList.addAll(salesList);
	        }
	        if(userList.size()>0){
                saleList.addAll(userList);
	        }
	        //采购人员
            if(purchaseList.size()>0){
                buyList.addAll(purchaseList);
            }
            if(userList.size()>0){
                buyList.addAll(userList);
            }
            Map<Integer, Integer> autoCount = new HashMap<Integer, Integer>();
//	        for(int k =0;k<1;k++){
            for (int k = 0; k < orderPayList.size(); k++) {
                String orderNo = orderPayList.get(k).toString();
//                String orderNo="QB01569340348633";
                //查询该订单的order_details信息
                List<OrderAutoBean> orderDetailInfo = preOrderAutoService.getAllOrderAutoInfo(orderNo);
                //1、判断该订单用户是否被分配过销售
                int adminid=preOrderAutoService.getAdminId(orderNo);
	            if(adminid == 0){
		            //如果销售没有分配过销售
		            adminid=getAdmuserId(orderNo,saleList);
		            //判断该采购是否是纯采购
		            if(purchaseList.contains(adminid)){
		            	//如果是纯采购，则获取最近7天分配用户最少的纯销售
			            Map<Integer, Integer> saleMap=preOrderAutoService.getLessSaler();
			            for(String s:salesList){
				            if(saleMap.get(Integer.valueOf(s)) == null){
					            saleMap.put(Integer.parseInt(s),0);
				            }
			            }
                        //添加销售记录表，分配用户销售
                        preOrderAutoService.insertAdminUser(orderNo,Integer.valueOf(getId(saleMap)));
		            }else{
                        //添加销售记录表，分配用户销售
                        preOrderAutoService.insertAdminUser(orderNo,adminid);
                    }
	            }else if(adminid > 0 && salesList.contains(String.valueOf(adminid))){
		            //如果销售分配的是纯销售，则重新分配采购
		            adminid=getAdmuserId(orderNo,buyList);
	            }
	            if(!buyList.contains(String.valueOf(adminid)) && buyList.size()>0){
                    adminid=Integer.valueOf(buyList.get(0));
                }
	            if(adminid > 0){
                    //开始循环分配订单的每个商品，一个订单只允许一个采销员
                    for(OrderAutoBean goodoab:orderDetailInfo){
                        this.insertDG(autoCount, goodoab, String.valueOf(adminid), orderNo);
                    }
                }
                preOrderAutoService.inventoryLock(orderNo);
                //判断该订单paypal地址和订单地址是否一致且为欧洲国家，则自动确认到账
                //获取该订单的paypal地址
//                String ipnInfo=preOrderAutoService.getPayPalAddress(orderNo);
            }
            //查看是否有客户同意替换的商品需要分配采购
            preOrderAutoService.queryChangeLogToAotu();
            //更新库存标识
            preOrderAutoService.updateIsStockFlag();
        }catch (Exception e){
            System.out.println("新分配采购销售出错");
            e.printStackTrace();
        }
    }

    public int getAdmuserId(String orderNo,List<String> userList){
    	int adminid=56;
    	//判断该订单中的商品（pid）以前谁采购的比较多
	    Map<Integer,Integer> p_map=preOrderAutoService.getMoreGoodsPid(orderNo);
	    if(p_map == null || p_map.size()==0){
	    	//如果都没有采购过，则取最近7天采购数量最少的那个采购人（纯采购、采销一体的账号）
		    //过去最近7天采购的分配情况
		    p_map=preOrderAutoService.getSevenLeastAdmuser();
		    //获取分配数量最少的采销员
		    for(String s:userList){
			    if(p_map.get(Integer.valueOf(s)) == null){
				    p_map.put(Integer.parseInt(s),0);
			    }
		    }
		    adminid =  Integer.valueOf(this.getId(p_map));
	    }else if(p_map.size() != userList.size()){
		    //该订单中商品只要部分采购参与过购买，获取最近7天采购数量最多和最少的的采购人，
		    // 如果两者相差超过10中产品，则分给最少的哪个采购，如果在10种以内，则分配订单中采购过最多的哪个采购
		    Map<Integer,Integer> sMap=preOrderAutoService.getSevenLeastAdmuser();
		    for(String s:userList){
			    if(sMap.get(Integer.valueOf(s)) == null){
				    sMap.put(Integer.parseInt(s),0);
			    }
		    }
		    adminid=Integer.valueOf(getbuyLess(sMap));
		    if(adminid == 0){
			    //最近7天最多采购和最少采购分配产品相差少于10种，分给订单中采购过最多的哪个采购
			    adminid =  Integer.valueOf(this.getMaxId(p_map));
		    }
	    }else if(p_map.size() == userList.size()){
		    //该订单中商品所有采购都有参与，则分配采购数量最多的那个人
		    adminid =  Integer.valueOf(this.getMaxId(p_map));
	    }
    	return adminid;
    }




    /**
     * 鎵归噺鍒嗛厤璁㈠崟鐨勯噰璐汉鍛�
     */
    public void PreOrderAutoDistribution() {
        try {
            List<String> orderPayList=preOrderAutoService.getUnassignedOrders();
            LOG.info("未分配采购订单数量:" + orderPayList.size());
            List<String> userList = preOrderAutoService.getAllAdmUser();
            Map<Integer, Integer> autoCount = new HashMap<Integer, Integer>();
            for (int i = 0; i < userList.size(); i++) {
                autoCount.put(Integer.valueOf(userList.get(i).toString()), 0);
            }
            for (int k = 0; k < orderPayList.size(); k++) {
//				 for(int k=0;k<1;k++){
                String order = orderPayList.get(k).toString();
//				 String order="Q818016341036116";
                String adminId = "";
                if (AppConfig.isAuto) {
                    Map<String, List<OrderAutoBean>> noBuyGoods = new HashMap<String, List<OrderAutoBean>>();
                    List<OrderAutoBean> orderDetailInfo = preOrderAutoService.getAllOrderAutoInfo(order.trim());
                    for (int i = 0; i < orderDetailInfo.size(); i++) {
                        OrderAutoBean goodoab = orderDetailInfo.get(i);
                        if(!StringUtils.isStrNull(goodoab.getCar_urlMD5()) && goodoab.getCar_urlMD5().indexOf("N")==0){
                            int id = preOrderAutoService.getNewColudBuyer(goodoab.getGoods_pid());
                            this.insertDG(autoCount, goodoab, String.valueOf(id), order);
                            continue;
                        }
                        if(goodoab.getUserid()==2840){
                            this.insertDG(autoCount, goodoab, "68", order);
                            continue;
                        }
                        int id = preOrderAutoService.getExitGoodsPid(goodoab.getGoods_pid());
                        if (id > 0 && userList.contains(String.valueOf(id))) {
                            Map<Integer, Integer> mapList = preOrderAutoService.getAllBuyerCount();
                            for (int m = 0; m < userList.size(); m++) {
                                if (mapList.get(Integer.parseInt(userList.get(m).toString())) == null) {
                                    mapList.put(Integer.parseInt(userList.get(m).toString()), 0);
                                }
                            }
                            if (mapList.get(Integer.valueOf(id)) <= 40) {
                                adminId = String.valueOf(id);
                            }
                            // 暂时去掉产品线分配
//							else if(!StringUtils.isStrNull(goodoab.getGoodscatid())){
//								String category=preOrderAutoService.getAliCategoryType(goodoab.getGoodscatid());
//								adminId = this.getAdmId(category);
//							}
                            else{
                                adminId = this.getId(mapList);
                            }
                            this.insertDG(autoCount, goodoab, adminId, order);
                        } else {
                            if (noBuyGoods.get(goodoab.getCarUrl()) != null) {
                                noBuyGoods.get(goodoab.getCarUrl()).add(goodoab);
                            } else {
                                List<OrderAutoBean> list1 = new ArrayList<OrderAutoBean>();
                                list1.add(goodoab);
                                noBuyGoods.put(goodoab.getCarUrl(), list1);
                            }
                        }
                    }
                    // =========================================
                    if (noBuyGoods.size() <= base) {
                        Map<Integer, Integer> mapList = preOrderAutoService.getAllBuyerCount();
                        for (int m = 0; m < userList.size(); m++) {
                            if (mapList.get(Integer.parseInt(userList.get(m).toString())) == null) {
                                mapList.put(Integer.parseInt(userList.get(m).toString()), 0);
                            }
                        }
                        adminId = this.getId(mapList);
                        for (String key : noBuyGoods.keySet()) {
                            List<OrderAutoBean> os = noBuyGoods.get(key);
                            for (OrderAutoBean orderAutoBean : os) {
                                this.insertDG(autoCount, orderAutoBean,adminId, order);
                            }
                        }
                    } else {
                        int counts = 1;
                        List<String> list_key = new ArrayList<String>();
                        Map<Integer, Integer> mapList = preOrderAutoService
                                .getAllBuyerCount();
                        for (int m = 0; m < userList.size(); m++) {
                            if (mapList.get(Integer.parseInt(userList.get(m)
                                    .toString())) == null) {
                                mapList.put(Integer.parseInt(userList.get(m)
                                        .toString()), 0);
                            }
                        }
                        adminId = this.getId(mapList);
                        for (String key : noBuyGoods.keySet()) {
                            List<OrderAutoBean> os = noBuyGoods.get(key);
                            for (OrderAutoBean orderAutoBean : os) {
                                this.insertDG(autoCount, orderAutoBean,
                                        adminId, order);
                            }
                            list_key.add(key);
                            if (counts % 40 == 0) {
                                Map<Integer, Integer> mapList1 = preOrderAutoService
                                        .getAllBuyerCount();
                                for (int m = 0; m < userList.size(); m++) {
                                    if (mapList1.get(Integer.parseInt(userList
                                            .get(m).toString())) == null) {
                                        mapList1.put(Integer.parseInt(userList
                                                .get(m).toString()), 0);
                                    }
                                }
                                adminId = this.getId(mapList1);
                            }
                            counts = counts + 1;
                        }
                    }
                }
                preOrderAutoService.inventoryLock(order);
            }
            LOG.info("分配情况:" + autoCount.toString());
            //查看是否有客户同意替换的商品需要分配采购
            preOrderAutoService.queryChangeLogToAotu();
        } catch (Exception e) {
            LOG.info("分配出错");
            e.printStackTrace();
        }
    }

    public void reFreshData(){
        try{
            preOrderAutoService.getAllOrderRefresh();
        }catch(Exception e){
            System.out.println("鍒锋柊璁㈠崟鏈�鏅氶��璐ф椂闂村嚭閿�");
        }
    }

    /**
     * 刷新搜索词的最低价格最高价
     */
    public static void flushMinPriceAndMaxPrice(){
        DecimalFormat df = new DecimalFormat("#0.##");
        ResultSet rs=null;
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        try{
            String sql="select id,category from priority_category where category is not null and category !=''";
            stmt=conn.prepareStatement(sql);
            rs=stmt.executeQuery();
            while(rs.next()){
                double p=0.00;
                String category=rs.getString("category");
                if(category.indexOf(",")>-1){
                    String [] c=category.split(",");
                    for(String s:c){
                        double p1=price(s);
                        if(p1<p){
                            p=p1;
                        }
                    }
                }else{
                    p=price(category);
                }
                sql="update priority_category set minPrice="+df.format(p/6.65)+" where id="+rs.getInt("id")+"";
                stmt=conn.prepareStatement(sql);
                stmt.executeUpdate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBHelper.getInstance().closePreparedStatement(stmt);
            DBHelper.getInstance().closeResultSet(rs);
            DBHelper.getInstance().closeConnection(conn);
        }
    }

    public static double price(String catid){
        double price=0.00;
        ResultSet rs=null;
        Connection conn = DBHelper.getInstance().getConnection();
        PreparedStatement stmt = null;
        try{
            String sql="select p1 from 1688_price_median_for_weight where 1688_cate_id=?";
            stmt=conn.prepareStatement(sql);
            stmt.setString(1,catid);
            rs=stmt.executeQuery();
            if(rs.next()){
                price=rs.getDouble("p1");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return price;
    }

    /**
     * 鑾峰彇鏈�灏戦噰璐暟閲忕殑閲囪喘浜哄憳
     *
     * @param sampleList
     * @return
     * @author whj
     */
    public int CalculationMin(List<AdmDsitribution> sampleList) {
        int index = -99;
        try {
            int totalCount = sampleList.size();
            if (totalCount >= 1) {
                int min = sampleList.get(0).getCount();
                for (int i = 0; i < totalCount; i++) {
                    int temp = sampleList.get(i).getCount();
                    if (min >= temp) {
                        min = temp;
                        index = i;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sampleList.get(index).getAdid();
    }

}
