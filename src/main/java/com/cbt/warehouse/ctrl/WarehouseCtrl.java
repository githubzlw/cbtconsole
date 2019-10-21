package com.cbt.warehouse.ctrl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.encode.InvalidAtributeException;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cbt.FreightFee.service.FreightFeeSerive;
import com.cbt.FtpUtil.ContinueFTP2;
import com.cbt.Specification.util.DateFormatUtil;
import com.cbt.bean.AliInfoDataBean;
import com.cbt.bean.BlackList;
import com.cbt.bean.CustomGoodsBean;
import com.cbt.bean.Forwarder;
import com.cbt.bean.GuestBookBean;
import com.cbt.bean.LocationManagementInfo;
import com.cbt.bean.LocationTracking;
import com.cbt.bean.Logisticsinfo;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.OrderInfoPrint;
import com.cbt.bean.Orderinfo;
import com.cbt.bean.RechargeRecord;
import com.cbt.bean.ShippingBean;
import com.cbt.bean.StorageInspectionLogPojo;
import com.cbt.bean.StorageLocationBean;
import com.cbt.bean.TrackBean;
import com.cbt.bean.TypeBean;
import com.cbt.bean.ZoneBean;
import com.cbt.change.util.ChangeRecordsDao;
import com.cbt.change.util.CheckCanUpdateUtil;
import com.cbt.change.util.ErrorLogDao;
import com.cbt.change.util.OnlineOrderInfoDao;
import com.cbt.common.StringUtils;
import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.customer.service.GuestBookServiceImpl;
import com.cbt.customer.service.IGuestBookService;
import com.cbt.fee.service.IZoneServer;
import com.cbt.fee.service.ZoneServer;
import com.cbt.jcys.bean.DataInfo;
import com.cbt.jcys.bean.GoodsPojo;
import com.cbt.jcys.bean.PriceData;
import com.cbt.jcys.bean.PriceReturnJsonNew;
import com.cbt.jcys.bean.RecList;
import com.cbt.jcys.util.HttpUtil;
import com.cbt.jcys.util.JcgjSoapHttpPost;
import com.cbt.jcys.util.Md5Helper;
import com.cbt.jdbc.DBHelper;
import com.cbt.jdbc.MiniConnectionPoolManager.TimeoutException;
import com.cbt.messages.service.MessagesService;
import com.cbt.method.service.OrderDetailsServiceImpl;
import com.cbt.onlinesql.ctr.SaveSyncTable;
import com.cbt.orderinfo.service.IOrderinfoService;
import com.cbt.parse.service.ImgDownload;
import com.cbt.parse.service.StrUtils;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.BuyerCommentPojo;
import com.cbt.pojo.CustomsRegulationsPojo;
import com.cbt.pojo.RedManProductBean;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.processes.service.SendEmail;
import com.cbt.processes.servlet.Currency;
import com.cbt.report.service.GeneralReportService;
import com.cbt.util.FtpConfig;
import com.cbt.util.GetConfigureInfo;
import com.cbt.util.Md5Util;
import com.cbt.util.NewFtpUtil;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.SpringContextUtil;
import com.cbt.util.SysParamUtil;
import com.cbt.util.Util;
import com.cbt.warehouse.dao.WarehouseMapper;
import com.cbt.warehouse.pojo.AdmuserPojo;
import com.cbt.warehouse.pojo.AllProblemPojo;
import com.cbt.warehouse.pojo.DisplayBuyInfo;
import com.cbt.warehouse.pojo.Dropshiporder;
import com.cbt.warehouse.pojo.GoodsInventory;
import com.cbt.warehouse.pojo.JcexPrintInfo;
import com.cbt.warehouse.pojo.Mabangshipment;
import com.cbt.warehouse.pojo.OrderFeePojo;
import com.cbt.warehouse.pojo.OrderInfoCountPojo;
import com.cbt.warehouse.pojo.OrderInfoPojo;
import com.cbt.warehouse.pojo.OrderProductSurcePojo;
import com.cbt.warehouse.pojo.OrderReplenishmentPojo;
import com.cbt.warehouse.pojo.RefundSamplePojo;
import com.cbt.warehouse.pojo.SbxxPojo;
import com.cbt.warehouse.pojo.ShippingPackage;
import com.cbt.warehouse.pojo.Skuinfo;
import com.cbt.warehouse.pojo.Tb1688Account;
import com.cbt.warehouse.pojo.Tb1688Pojo;
import com.cbt.warehouse.pojo.returndisplay;
import com.cbt.warehouse.service.IWarehouseService;
import com.cbt.warehouse.service.MabangshipmentService;
import com.cbt.warehouse.service.SkuinfoService;
import com.cbt.warehouse.service.ZoneShippingService;
import com.cbt.warehouse.thread.warehouseThread;
import com.cbt.warehouse.util.ExcelUtil;
import com.cbt.warehouse.util.OrderInfoPage;
import com.cbt.warehouse.util.OrderPrintInfoUtil;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.warehouse.util.UtilAll;
import com.cbt.warehouse.util.Utility;
import com.cbt.website.bean.ProductBean;
import com.cbt.website.bean.PurchaseSamplingStatisticsPojo;
import com.cbt.website.bean.SampleGoodsBean;
import com.cbt.website.bean.SearchResultInfo;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.bean.UserInfo;
import com.cbt.website.bean.UserOrderDetails;
import com.cbt.website.dao.ExpressTrackDaoImpl;
import com.cbt.website.dao.IExpressTrackDao;
import com.cbt.website.dao2.ChargeCalculateSample;
import com.cbt.website.dao2.CreateAndPreAlertOrderSample;
import com.cbt.website.dao2.FindOrderSample;
import com.cbt.website.dao2.RemoveOrderSample;
import com.cbt.website.dao2.feeCount;
import com.cbt.website.server.PurchaseServer;
import com.cbt.website.server.PurchaseServerImpl;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import com.cbt.website.servlet.Purchase;
import com.cbt.website.thread.AddInventoryThread;
import com.cbt.website.util.ContentConfig;
import com.cbt.website.util.DownloadMain;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.FileTool;
import com.cbt.website.util.GetCompanyName;
import com.cbt.website.util.JsonResult;
import com.cbt.website.util.UploadByOkHttp;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Maps;
import com.importExpress.mail.SendMailFactory;
import com.importExpress.mail.TemplateType;
import com.importExpress.mapper.IPurchaseMapper;
import com.importExpress.service.IPurchaseService;
import com.importExpress.service.TabCouponService;
import com.importExpress.utli.DESUtils;
import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.MultiSiteUtil;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SearchFileUtils;
import com.importExpress.utli.SendMQ;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sun.misc.BASE64Encoder;

@SuppressWarnings("deprecation")
@Controller
@RequestMapping("/warehouse")
public class WarehouseCtrl {
	private static final String UPLOAD_IMG_PATH = "/usr/local/goodsimg/importcsvimg/inspectionImg/";
	// 上传文件存储目录
	private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();
//	private static final String UPLOAD_DIRECTORY = "upload";
	// 上传配置
//	private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;
//	private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40;
//	private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(WarehouseCtrl.class);
	IExpressTrackDao expressTrackDao= new ExpressTrackDaoImpl();

	Lock lock = new ReentrantLock();
	PurchaseServer purchaseServer = new PurchaseServerImpl();
	@Autowired
	private IWarehouseService iWarehouseService;
	@Autowired
	private WarehouseMapper dao;
	@Autowired
	private GeneralReportService generalReportService;
	@Autowired
	private IOrderinfoService iOrderinfoService;
	@Autowired
	private SendMailFactory sendMailFactory;
	@Autowired
	private MabangshipmentService mabangshipmentService;
	@Autowired
	private IPurchaseMapper pruchaseMapper;
	@Autowired
	private SkuinfoService skuinfoService;
	@Autowired
	private com.cbt.warehouse.service.DropshiporderService dropshiporderService;
	@Autowired
	private com.cbt.warehouse.service.OrderService orderService;
	@Autowired
	private ZoneShippingService zoneShippingService;
	private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	IExpressTrackDao dao1 = new ExpressTrackDaoImpl();
	IOrderwsServer server1 = new OrderwsServer();
	@Autowired
	private IOrderinfoService orderinfoService;
	@Autowired
	private IPurchaseService iPurchaseService;
	@Autowired
	private FreightFeeSerive freightFeeSerive;
	@Autowired
	private TabCouponService tabCouponService;
	/**
	 *
	 * @Title getAllBuyer
	 * @Description 获取所有采购人
	 * @param request 客户端请求
	 * @param response 返回客户端参数
	 * @return
	 * @throws ServletException servlet异常
	 * @throws IOException 输入输出流异常
	 * @throws ParseException
	 * @return com.alibaba.fastjson.JSONArray 返回结果类型
	 */
	@RequestMapping(value = "/getAllBuyer", method = RequestMethod.GET)
	@ResponseBody
	protected com.alibaba.fastjson.JSONArray getAllBuyer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		com.alibaba.fastjson.JSONArray jsonArr=new com.alibaba.fastjson.JSONArray();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm =(Admuser)SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		if(adm == null){
			return jsonArr;
		}
		List<com.cbt.pojo.AdmuserPojo> list=iWarehouseService.getAllBuyer(adm.getId());
		List<com.cbt.pojo.AdmuserPojo> result = new ArrayList<com.cbt.pojo.AdmuserPojo>();
		com.cbt.pojo.AdmuserPojo admuser=new com.cbt.pojo.AdmuserPojo();
		admuser.setId(1);
		admuser.setAdmName("全部");
		result.add(admuser);
		com.cbt.pojo.AdmuserPojo a=new com.cbt.pojo.AdmuserPojo();
		if(adm.getId()==1 || adm.getId()==83 || adm.getId()==84){
			a.setId(1);
			a.setAdmName("Ling");
			result.add(a);
		}else if(adm.getId()==18){
			a.setId(18);
			a.setAdmName("testadm");
			result.add(a);
		}
		result.addAll(list);
		jsonArr = JSON.parseArray(JSON.toJSONString(result));
		return jsonArr;
	}

	@RequestMapping(value = "/getAllZone", method = RequestMethod.GET)
	@ResponseBody
	protected com.alibaba.fastjson.JSONArray getAllZone(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		List<ZoneBean> list=iWarehouseService.getAllZone();
		List<ZoneBean> result = new ArrayList<ZoneBean>();
		ZoneBean z=new ZoneBean();
		z.setCountry("全部");
		result.add(z);
		result.addAll(list);
		com.alibaba.fastjson.JSONArray jsonArr = JSON.parseArray(JSON.toJSONString(result));
		return jsonArr;
	}

	/**
	 * 提交采样商品反馈信息
	 * @Title addSampleRemark
	 * @Description TODO
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @return void
	 */
	@RequestMapping(value = "/addSampleRemark")
	public void addSampleRemark(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int row=0;
		try{
			String od_id=request.getParameter("od_id");
			String remark=request.getParameter("remark");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("od_id", od_id);
			map.put("remark", remark);
			row=iWarehouseService.addSampleRemark(map);
		}catch(Exception e){
			e.printStackTrace();
			row=0;
		}
		out.print(row);
		out.close();
	}
	/**
	 * 删除手工录入的替换商品goods_source
	 * @Title deleteSource
	 * @Description TODO
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @return void
	 */
	@RequestMapping(value = "/deleteSource")
	public void deleteSource(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int row=0;
		try{
			String goods_p_url=request.getParameter("goods_p_url");
			String goods_url=request.getParameter("goods_url");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("goods_p_url", goods_p_url);
			map.put("goods_url", goods_url);
			row=iWarehouseService.deleteSource(map);
		}catch(Exception e){
			e.printStackTrace();
			row=0;
		}
		out.print(row);
		out.close();
	}

	/**
	 * 采购详情手动更新采购价格
	 * @Title addSampleRemark
	 * @Description TODO
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @return void
	 */
	@RequestMapping(value = "/updatePrice")
	public void updatePrice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int row=0;
		try{
			String orderid=request.getParameter("orderid");
			String odid=request.getParameter("odid");
			String price=request.getParameter("price");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderid", orderid);
			map.put("odid", odid);
			map.put("price", price);
			row=iWarehouseService.updatePrice(map);
		}catch(Exception e){
			e.printStackTrace();
			row=0;
		}
		out.print(row);
		out.close();
	}

	/**
	 * 优先类别模块更新类别的最低价格最高价
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/updateCatePrice")
	public void updateCatePrice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int row=0;
		try{
			String id=request.getParameter("id");
			String minPrice=request.getParameter("minPrice");
			String type=request.getParameter("type");
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id);
			map.put("minPrice", (StringUtil.isBlank(minPrice)?"-":minPrice));
			map.put("type",type);
			row=iWarehouseService.updateCatePrice(map);
			if(row>0){
				SendMQ sendMQ=new SendMQ();
				String sql="";
				sql="update priority_category set minPrice="+minPrice+" where id="+id+"";
				sendMQ.sendMsg(new RunSqlModel(sql));
				sendMQ.closeConn();
			}
		}catch(Exception e){
			e.printStackTrace();
			row=0;
		}
		out.print(row);
		out.close();
	}

	/**
	 * 采购页面添加商品评论
	 * @param request
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/saveCommentContent", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult saveCommentContent(@RequestParam(value = "cm_odid", required = true) String cm_odid, @RequestParam(value = "comment_content_", required = true) String comment_content_, @RequestParam(value = "uploadfile", required = true) MultipartFile file, HttpServletRequest request) {
		JsonResult json = new JsonResult();
		boolean flag=false;
		int row=0;
		try{
			Map<String, String> map = new HashMap<String, String>();
			map.put("cm_odid", cm_odid);
			map.put("commentsContent", comment_content_);
			String picPath="";
			if (!file.isEmpty()) {
				SimpleDateFormat order=new SimpleDateFormat("ddHHmm");
				Date data=new Date();
				String dir="";
				String old_name="";
				// 本地服务器磁盘全路径
				String localFilePath = DateFormatUtil.getCurrentYearAndMonth() + "/" + order.format(data)+old_name+".jpg";
				// 文件流输出到本地服务器指定路径
				if (ftpConfig == null) {
					ftpConfig = GetConfigureInfo.getFtpConfig();
				}
				String imgUploadPath = ftpConfig.getLocalDiskPath();
				String imgPath = imgUploadPath + localFilePath;
				System.out.println("新上传的评论图片名："+(order.format(data)+old_name+".jpg"));
				System.out.println("新上传的评论图片路径："+imgPath);
				flag=ImgDownload.writeImageToDisk1(file.getBytes(), imgPath);
				picPath=Util.PIC_URL+localFilePath+"";
				if(flag){
					// flag=NewFtpUtil.uploadFileToRemote(Util.PIC_IP, 21, Util.PIC_USER, Util.PIC_PASS, "/inspectionImg/", localFilePath, imgPath);
					flag = UploadByOkHttp.uploadFile(new File(imgPath),UPLOAD_IMG_PATH + localFilePath, 0);
					picPath=Util.PIC_URL+localFilePath+"";
				}
			}
			map.put("picPath",picPath);
			BuyerCommentPojo bp=iWarehouseService.getBuyerCommentPojo(map);
			if(bp != null && StringUtil.isNotBlank(bp.getOrderid())){
				map.put("orderid",bp.getOrderid());
				map.put("od_id",bp.getId());
				map.put("admuserid",bp.getAdmuserid());
				map.put("countryid",bp.getCountryid());
				map.put("email",bp.getEmail());
				map.put("uid",bp.getUid());
				map.put("car_type",bp.getCar_type());
				map.put("goods_pid",bp.getGoods_pid());
				map.put("goodsid",bp.getGoodsid());
				row=iWarehouseService.saveCommentContent(map);
				if(row>0){
					SendMQ sendMQ=new SendMQ();
					sendMQ.sendMsg(new RunSqlModel("insert into goods_comments_real(oid,car_type,order_no,user_id,user_name,country_id,goods_pid,comments_content," +
							"comments_time,admin_id,goodsid,picPath) VALUES" +
							"('"+map.get("od_id")+"','"+map.get("car_type")+"','"+map.get("orderid")+"','"+map.get("uid")+"','"+map.get("email")+"','"+map.get("countryid")+"'" +
							",'"+map.get("goods_pid")+"','"+map.get("commentsContent")+"',now(),'"+map.get("admuserid")+"','"+map.get("goodsid")+"','"+map.get("picPath")+"')"));
					sendMQ.closeConn();
					flag=true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			row=0;
		}
		json.setOk(flag);
		return json;
	}

	/**
	 * 获取验货商品重量
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/getWeight")
	public void getWeight(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try{
			SearchResultInfo s=iWarehouseService.getWeight();
			if(s != null){
				String createtime=s.getCreatetime();
				String weight=s.getWeight();
				Date date=new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time=sdf.format(date);
				long sec=StringUtil.getSecTwoTimeComparing(time,createtime);
				if(sec>180){
					out.print("");
				}else{
					out.print(weight);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		out.close();
	}

	/**
	 *
	 * 保存验货重量信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/saveWeight")
	public void saveWeight(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String,String> map=new HashMap<String,String>(3);
		PrintWriter out = response.getWriter();
		try{
			String orderid=request.getParameter("orderid");
			String odid=request.getParameter("odid");
			String weight=request.getParameter("weight");
			String volumeWeight=request.getParameter("volumeWeight");
			String pid=request.getParameter("pid");
			//数据校验
			if (StringUtil.isBlank(pid) || pid.length() < 3 || StringUtil.isBlank(weight) || !Pattern.compile("(\\d+([.]{1}\\d+)?)").matcher(weight).matches()) {
				out.print(2);
				out.close();
				return;
			}
			List<OrderDetailsBean> odb=iOrderinfoService.getOrdersDetails(orderid);
			String goods_type = "";
			for(OrderDetailsBean orderDetails : odb){
				if(orderDetails.getId() == Integer.valueOf(odid)){
					goods_type = orderDetails.getCar_type();
					break;
				}
			}
			map.put("orderid",orderid);
			map.put("odid",odid);
			map.put("weight",weight);
			map.put("pid",pid);
			map.put("goodsType",goods_type);
			if(org.apache.commons.lang3.StringUtils.isBlank(volumeWeight) || "0".equals(volumeWeight)){
				map.put("volumeWeight","");
			}else{
				map.put("volumeWeight",volumeWeight);
			}
			iWarehouseService.saveWeight(map);
			out.print(1);
		}catch(Exception e){
			out.print(0);
			e.printStackTrace();
		}
		out.close();
	}

    /**
     *
     * 将重量同步到产品库（使用蒋先伟接口）
     *  2018/11/16 10:41 ly
     *
     *  //result 0-处理异常;2-pid数据问题;1-同步到产品库成功;3-未找到重量数据;4-已经同步到产品库过;
     */
    @RequestMapping(value = "/saveWeightFlag")
    public void saveWeightFlag(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try{
        	String sessionId = request.getSession().getId();
			String userJson = Redis.hget(sessionId, "admuser");
			com.cbt.website.userAuth.bean.Admuser user = (com.cbt.website.userAuth.bean.Admuser) SerializeUtil.JsonToObj(userJson, com.cbt.website.userAuth.bean.Admuser.class);
            String pid=request.getParameter("pid");
            String odId=request.getParameter("odId");
            //数据校验
            if (StringUtil.isBlank(pid) || pid.length() < 3) {
                out.print(2);
                out.close();
                return;
            }
            int result = iWarehouseService.saveWeightFlag(pid, user.getId(), Integer.valueOf(odId));
            out.print(result);
        }catch(Exception e){
            out.print(0);
            e.printStackTrace();
        }
        out.close();
    }


	/**
	 * 验货图片编辑页面增加评论
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertEvaluation")
	public void insertEvaluation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int index=0;
		PrintWriter out = response.getWriter();
		String goods_pid=request.getParameter("goods_pid");
		String evaluation=request.getParameter("evaluation");
		Map<String,String> map=new HashMap<String,String>(2);
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
		try{
			map.put("goods_pid",goods_pid);
			map.put("evaluation",evaluation);
			map.put("admName",adm != null?adm.getAdmName():"admin");
			index=iWarehouseService.insertEvaluation(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		out.print(index);
        out.close();
	}

	/**
	 * 验货图片关联验货商品
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/insInsp")
	public void insInsp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int index=0;
		PrintWriter out = response.getWriter();
		String goodsPid=request.getParameter("goodsPid");
		String odid=request.getParameter("odid");
		String picPath=request.getParameter("picPath");
		String orderid=request.getParameter("orderid");
		Map<String,String> map=new HashMap<String,String>(2);
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
		try{
			map.put("goodsPid",goodsPid);
			map.put("odid",odid);
			map.put("picPath",picPath);
			map.put("orderid",orderid);
			index=iWarehouseService.insInsp(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		out.print(index);
		out.close();
	}

	/**
	 * 停用启用验货图片
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/disabled")
	public void disabled(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int row=0;
		PrintWriter out = response.getWriter();
		try{
			StringBuilder sb=new StringBuilder();
			Map<String,String> map=new HashMap<String,String>(1);
			String i_id=request.getParameter("i_id");
			String state=request.getParameter("state");
			String picture=request.getParameter("picture");
			if(StringUtil.isNotBlank(i_id) && StringUtil.isNotBlank(state)){
				map.put("i_id",i_id);
				map.put("state",state);
				map.put("picture",picture);
				row=iWarehouseService.disabled(map);
				SendMQ sendMQ = new SendMQ();
				sendMQ.sendMsg(new RunSqlModel("update inspection_picture set state='"+map.get("state")+"' where pic_path='"+map.get("picture")+"' and isdelete=0"));
				sendMQ.closeConn();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		out.print(row);
		out.close();
	}

	/**
	 * 删除验货图片路径
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	  @RequestMapping(value = "/delInPic")
	  public void delInPic(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int row=0;
		PrintWriter out = response.getWriter();
		try{
			StringBuilder sb=new StringBuilder();
			Map<String,String> map=new HashMap<String,String>(5);
			String orderid=request.getParameter("orderid");
			String goodsid=request.getParameter("goodsid");
			String goods_pid=request.getParameter("goods_pid");
			String path=request.getParameter("path");
			String i_id=request.getParameter("i_id");
			map.put("orderid",StringUtil.isBlank(orderid)?"9999999":orderid);
			map.put("goodsid",goodsid);
			map.put("goods_pid",goods_pid);
			map.put("path",path);
			map.put("i_id",i_id);
			iWarehouseService.delInPic(map);
			SendMQ sendMQ = new SendMQ();
			sendMQ.sendMsg(new RunSqlModel("update order_details set picturepath='' where orderid='"+map.get("orderid")+"' and goodsid='"+map.get("goods_pid")+"'"));
			sendMQ.sendMsg(new RunSqlModel("update inspection_picture set isdelete=1 where pic_path='"+map.get("path")+"'"));
			sendMQ.closeConn();
			row=1;
		}catch (Exception e){
			e.printStackTrace();
			row=0;
		}
		out.print(row);
		out.close();
	}

	@RequestMapping(value = "/insertStorageProblemOrder")
	public void insertStorageProblemOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try{
			DataSourceSelector.restore();
			String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
			Admuser adm =(Admuser)SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			Map<String, Object> map = new HashMap<String, Object>();
			String tbOrderId=request.getParameter("tbOrderId");
			String remarkUserId=request.getParameter("remarkUserId");
			String expresstrackid=request.getParameter("expresstrackid");
			map.put("tbOrderId", tbOrderId);
			map.put("remarkUserId",remarkUserId);
			map.put("expresstrackid", expresstrackid);
			if(adm!=null){
				map.put("name", adm.getAdmName());
			}else{
				map.put("name", "eric");
			}
			iWarehouseService.insertStorageProblemOrder(map);
		}catch(Exception e){
			e.printStackTrace();
		}
		out.print(1);
		out.close();
	}

	@RequestMapping(value = "/getRedManProduct", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult getRedManProduct(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, String> map = new HashMap<String, String>();
		int page = Integer.parseInt(request.getParameter("page"));
		if (page > 0) {
			page = (page - 1) * 50;
		}
		String email=request.getParameter("email");
		String shipno=request.getParameter("shipno");
		String shipnoState=request.getParameter("shipnoState");
		email=StringUtil.isBlank(email)?null:email;
		map.put("email",email);
		map.put("page",String.valueOf(page));
		map.put("shipno",StringUtil.isNotBlank(shipno)?shipno:null);
		map.put("shipnoState",StringUtil.isNotBlank(shipnoState)?shipnoState:"0");
		List<RedManProductBean> list=iWarehouseService.getRedProduct(map);
		List<RedManProductBean> listCount=iWarehouseService.getRedProductCount(map);
		json.setTotal(listCount.size());
		json.setRows(list);
		return json;
	  }

	/**
	 * 添加修改红人产品发货单号
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/insertShipno", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String insertShipno(HttpServletRequest request, Model model) {
		Map<String, String> map = new HashMap<String, String>();
		String a_id=request.getParameter("a_id");
		String type=request.getParameter("type");
		String newShipno=request.getParameter("newShipno");
		map.put("a_id",a_id);
		map.put("type",type);
		map.put("newShipno",newShipno);
		return iWarehouseService.insertShipno(map) + "";
	}

	/**
	 * 新品上线努力度
	 * @Title purchaseSamplingStatistics
	 * @Description
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 * @return EasyUiJsonResult
	 */
	@RequestMapping(value = "/purchaseSamplingStatistics", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult purchaseSamplingStatistics(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, Object> map = new HashMap<String, Object>();
		List<PurchaseSamplingStatisticsPojo> list = new ArrayList<PurchaseSamplingStatisticsPojo>();
		int page = Integer.parseInt(request.getParameter("page"));
		String days=request.getParameter("days");
		if (page > 0) {
			page = (page - 1) * 50;
		}
		map.put("page", page);
		map.put("days",days);
		list = iWarehouseService.getPurchaseSamplingStatistics(map);
		json.setRows(list);
		json.setTotal(list.size());
		return json;
	}

	/**
	 * 产品被购买详情
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/salesPerformanDetails", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult salesPerformanDetails(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, String> map = new HashMap<String, String>();
		List<PurchaseSamplingStatisticsPojo> list = new ArrayList<PurchaseSamplingStatisticsPojo>();
		List<PurchaseSamplingStatisticsPojo> listCount = new ArrayList<PurchaseSamplingStatisticsPojo>();
		String admName=request.getParameter("admName");
		String editTime=request.getParameter("editTime");
		int page = Integer.parseInt(request.getParameter("page"));
		if (page > 0) {
			page = (page - 1) * 20;
		}
		map.put("admName",admName);
		map.put("editTime",StringUtil.isBlank(editTime)?null:editTime);
		map.put("page",String.valueOf(page));
		list=iWarehouseService.salesPerformanDetails(map);
		listCount=iWarehouseService.salesPerformanDetailsCount(map);
		json.setRows(list);
		json.setTotal(listCount.size());
		return json;
	}

	/**
	 * 人为编辑过的产品销售业绩
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/salesPerformance", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult salesPerformance(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, Object> map = new HashMap<String, Object>();
		List<PurchaseSamplingStatisticsPojo> list = new ArrayList<PurchaseSamplingStatisticsPojo>();
		int page = Integer.parseInt(request.getParameter("page"));
		String goodsPid=request.getParameter("goodsPid");
		String adminId=request.getParameter("adminId");
		String updatetime=request.getParameter("updatetime");
		if (page > 0) {
			page = (page - 1) * 20;
		}
		map.put("page", page);
		map.put("goodsPid",StringUtil.isNotBlank(goodsPid)?goodsPid:null);
		map.put("adminId","0".equals(adminId)?null:adminId);
		map.put("updatetime","0".equals(updatetime)?null:updatetime);
		list = iWarehouseService.salesPerformance(map);
		json.setRows(list);
		json.setTotal(list.size());
		return json;
	}

	/**
	 * 疑似重量有问题产品详情
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/weightProblemDetails", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult weightProblemDetails(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, String> map = new HashMap<String, String>();
		List<PurchaseSamplingStatisticsPojo> list = new ArrayList<PurchaseSamplingStatisticsPojo>();
		int page = Integer.parseInt(request.getParameter("page"));
		String adminId=request.getParameter("adminId");
		String editTime=request.getParameter("editTime");
		String createtime=request.getParameter("createtime");
		editTime="0".equals(editTime)?null:editTime;
		adminId="0".equals(adminId)?null:adminId;
		createtime="0".equals(createtime)?null:createtime;
		try{
			if (page > 0) {
				page = (page - 1) * 20;
			}
			map.put("adminId",adminId);
			map.put("page",String.valueOf(page));
			map.put("editTime",editTime);
			map.put("createtime",createtime);
			list = iWarehouseService.weightProblemDetails(map);
			List<PurchaseSamplingStatisticsPojo> listCount = iWarehouseService.weightProblemDetailsCount(map);
			json.setRows(list);
			json.setTotal(listCount.size());
		}catch (Exception e){
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 清洗质量 (包含人为编辑和 店铺批量上线未编辑的)
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/cleaningQuality", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult cleaningQuality(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, String> map = new HashMap<String, String>();
		List<PurchaseSamplingStatisticsPojo> list = new ArrayList<PurchaseSamplingStatisticsPojo>();
		int page = Integer.parseInt(request.getParameter("page"));
		String adminId=request.getParameter("adminId");
		String updatetime=request.getParameter("updatetime");
		String createtime=request.getParameter("createtime");
		adminId="0".equals(adminId)?null:adminId;
		updatetime="0".equals(updatetime)?null:updatetime;
		createtime="0".equals(createtime)?null:createtime;
		map.put("adminId",adminId);
		map.put("editTime",updatetime);
		map.put("createtime",createtime);
		try{
			list = iWarehouseService.getCleaningQuality(map);
			json.setRows(list);
			json.setTotal(list.size());
		}catch (Exception e){
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 月销售商品努力报表
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/monthSalesEffortsList", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult monthSalesEffortsList(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, String> map = new HashMap<String, String>();
		String pages=request.getParameter("page");
		if(StringUtil.isBlank(pages)){
			pages="1";
		}
		int page=(Integer.valueOf(pages)-1)*20;
		map.put("page",String.valueOf(page));
		List<PurchaseSamplingStatisticsPojo> list = iWarehouseService.monthSalesEffortsList(map);
		List<PurchaseSamplingStatisticsPojo> listCount=iWarehouseService.monthSalesEffortsListCount(map);
		json.setRows(list);
		json.setTotal(listCount.size());
		return json;
	}

	/**
	 * 黑名单用户管理
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getUserBackList", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult getUserBackList(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, String> map = new HashMap<String, String>();
		List<BlackList> list = new ArrayList<BlackList>();
		String blackVlue = request.getParameter("blackVlue");
		int page = Integer.parseInt(request.getParameter("page"));
		String flag=request.getParameter("flag");
		String paramType=request.getParameter("paramType");
		flag=StringUtil.isBlank(flag)?null:flag;
		if (page > 0) {
			page = (page - 1) * 20;
		}
		map.put("page", String.valueOf(page));
		map.put("blackVlue", StringUtil.isNotBlank(blackVlue)?blackVlue:null);
		map.put("flag",flag);
		map.put("paramType",StringUtil.isNotBlank(paramType)?paramType:"-1");
		try{
			list = iWarehouseService.getUserBackList(map);
			List<BlackList> counts = iWarehouseService.getUserBackListCount(map);
			json.setRows(list);
			json.setTotal(counts.size());
		}catch (Exception e){
			e.printStackTrace();
		}
		return json;
	}


	/**
	 *
	 * @Title getShopManagerDetails
	 * @Description 获取店铺下产品信息
	 * @param request 客户daunt请求
	 * @param model 参数值
	 * @return easy ui结果集
	 * @throws ParseException
	 * @return EasyUiJsonResult
	 */
	@RequestMapping(value = "/getShopManagerDetails", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult getShopManagerDetails(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, Object> map = new HashMap<String, Object>();
		List<ShopManagerPojo> list = new ArrayList<ShopManagerPojo>();
		String id = request.getParameter("id");
		int page = Integer.parseInt(request.getParameter("page"));
		String goods_pid = request.getParameter("goods_pid");
		if (page > 0) {
			page = (page - 1) * 40;
		}
		if (goods_pid == null || "".equals(goods_pid)) {
			goods_pid = null;
		}
		map.put("id", id);
		map.put("page", page);
		map.put("goods_pid", goods_pid);
		DataSourceSelector.set("dataSource28hop");
		int counts=0;
		try{
			list = iWarehouseService.getShopManagerDetailsList(map);
			counts = iWarehouseService.getShopManagerListDetailsCount(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		json.setRows(list);
		json.setTotal(counts);
		return json;
	}

	/**
	 * 供应商的采购历史
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getShopBuyLogInfo", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult getShopBuyLogInfo(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, String> map = new HashMap<String, String>();
		List<ShopManagerPojo> list = new ArrayList<ShopManagerPojo>();
		int page = Integer.parseInt(request.getParameter("page"));
		String orderid = request.getParameter("orderid");
		String shopId = request.getParameter("shopId");
		if (page > 0) {
			page = (page - 1) * 40;
		}
		orderid=StringUtil.isBlank(orderid)?null:orderid;
		map.put("page", String.valueOf(page));
		map.put("orderid", orderid);
		map.put("shopId",shopId);
		int counts=0;
		list = iWarehouseService.getShopBuyLogInfo(map);
		counts = iWarehouseService.getShopBuyLogInfoCount(map);
		json.setRows(list);
		json.setTotal(counts);
		return json;
	}

	/**
	 *
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getPriorityCategory", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult getPriorityCategory(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, Object> map = new HashMap<String, Object>();
		List<ShopManagerPojo> list = new ArrayList<ShopManagerPojo>();
		String keyword=request.getParameter("keyword");
		int page = Integer.parseInt(request.getParameter("page"));
		if (page > 0) {
			page = (page - 1) * 40;
		}
		keyword=StringUtil.isBlank(keyword)?null:keyword;
		map.put("page", page);
		map.put("keyword",keyword);
		int counts=0;
		list = iWarehouseService.getPriorityCategory(map);
		counts = iWarehouseService.getPriorityCategoryCount(map);
		json.setRows(list);
		json.setTotal(counts);
		return json;
	}

	/**
	 * 查询采样订单退样信息
	 * @Title searchRefundSample
	 * @Description TODO
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 * @return EasyUiJsonResult
	 */
	@RequestMapping(value = "/searchRefundSample", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult searchRefundSample(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, Object> map = new HashMap<String, Object>();
		List<RefundSamplePojo> list = new ArrayList<RefundSamplePojo>();
		int page = Integer.parseInt(request.getParameter("page"));
		String t_orderid=request.getParameter("t_orderid");
		page=page>0?(page = (page - 1) * 50):page;
		map.put("page", page);
		map.put("t_orderid", t_orderid);
		list=iWarehouseService.searchRefundSample(map);
		List<RefundSamplePojo> list_cont=iWarehouseService.searchRefundSampleCount(map);
		json.setRows(list);
		json.setTotal(list_cont.size());
		return json;
	}

	/**
	 * 获取采样订单的采购数据
	 * @Title searchRefundOrder
	 * @Description TODO
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 * @return EasyUiJsonResult
	 */
	@RequestMapping(value = "/searchRefundOrder", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult searchRefundOrder(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, Object> map = new HashMap<String, Object>();
		List<TaoBaoOrderInfo> list = new ArrayList<TaoBaoOrderInfo>();
		int page = Integer.parseInt(request.getParameter("page"));
		String admuserid=request.getParameter("admuserid");
		String state=request.getParameter("state");
		page=page>0?(page = (page - 1) * 50):page;
		map.put("page", page);
		map.put("state", state);
		map.put("admuserid","1".equals(admuserid)?null:admuserid);
		list=iWarehouseService.searchRefundOrder(map);
		List<TaoBaoOrderInfo> list_cont=iWarehouseService.searchRefundOrderCount(map);
		json.setRows(list);
		json.setTotal(list_cont.size());
		return json;
	}
	/**
	 * 月退款统计查询
	 * @Title searchMonthlyRefund
	 * @Description TODO
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 * @return EasyUiJsonResult
	 */
	@RequestMapping(value = "/searchMonthlyRefund", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult searchMonthlyRefund(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, Object> map = new HashMap<String, Object>();
		List<TaoBaoOrderInfo> list = new ArrayList<TaoBaoOrderInfo>();
		int page = Integer.parseInt(request.getParameter("page"));
		page=page>0?(page = (page - 1) * 20):page;
		map.put("page", page);
		list=iWarehouseService.searchMonthlyRefund(map);
		List<TaoBaoOrderInfo> list_cont=iWarehouseService.searchMonthlyRefundCount(map);
		json.setRows(list);
		json.setTotal(list_cont.size());
		return json;
	}


	/**
	 *
	 * @Title getShopManager
	 * @Description 获取店铺信息
	 * @param request
	 * @param model
	 * @return easy ui结果集
	 * @throws ParseException
	 * @return EasyUiJsonResult
	 */
	@RequestMapping(value = "/getShopManager")
	@ResponseBody
	public EasyUiJsonResult getShopManager(HttpServletRequest request, Model model) throws ParseException {
		// DataSourceSelector.set("dataSource28hop");
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, Object> map = new HashMap<String, Object>();
		String shop_name = request.getParameter("shop_name");
		String remark = request.getParameter("remark");
		if (shop_name == null || "".equals(shop_name)) {
			shop_name = null;
		}
		if(StringUtils.isStrNull(remark)){
			remark = "-1";
		}
		int page = Integer.parseInt(request.getParameter("page"));
		if (page > 0) {
			page = (page - 1) * 40;
		}
		map.put("shop_name", shop_name);
		map.put("remark", remark);
		map.put("page", page);
		List<ShopManagerPojo> list = new ArrayList<ShopManagerPojo>();
		int counts =0;
		try{
			list = iWarehouseService.getShopManagerList(map);
			counts = iWarehouseService.getShopManagerListCount(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		json.setRows(list);
		json.setTotal(counts);
		return json;
	}

	/**
	 *
	 * @Title getSubUtil
	 * @Description  正则表达式匹配两个指定字符串中间的内容
	 * @param soap 参数值
	 * @param rgex 参数值
	 * @return List<String> 返回结果类型
	 */
	public static List<String> getSubUtil(String soap, String rgex) {
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(rgex);
		Matcher m = pattern.matcher(soap);
		while (m.find()) {
			int i = 1;
			list.add(m.group(i));
			i++;
		}
		return list;
	}

	/**
	 *
	 * @Title updateShopState
	 * @Description 更新店铺状态，免检必检
	 * @param request 客户端请求
	 * @param response 返回客户端信心
	 * @throws ServletException
	 * @throws IOException
	 * @return void
	 */
	@RequestMapping(value = "/updateShopState")
	public void updateShopState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		com.cbt.website.userAuth.bean.Admuser adm = (com.cbt.website.userAuth.bean.Admuser) SerializeUtil.JsonToObj(admuserJson, com.cbt.website.userAuth.bean.Admuser.class);
		Map<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		String state = request.getParameter("state");
		String type=request.getParameter("type");
		map.put("id", id);
		map.put("state", state);
		map.put("type", type);
		map.put("admuser", adm.getAdmName());
		int ret=0;
		DataSourceSelector.set("dataSource28hop");
		try{
			ret = iWarehouseService.updateShopState(map);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			DataSourceSelector.restore();
		}
		PrintWriter out = response.getWriter();
		out.print(ret);
		out.flush();
		out.close();

	}


	/**
	 * 1688采购订单建议退货管理
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getBuyReturnManage", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult getBuyReturnManage(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		String goodsid=request.getParameter("goodsid");
		String state=request.getParameter("state");
		String startTime="";
		String endTime="";
		int page=Integer.valueOf(request.getParameter("page"));
		page=(page-1)*40;
		goodsid=StringUtil.isBlank(goodsid)?null:goodsid;
		state=StringUtil.isBlank(state)?"0":state;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 15);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		startTime=df.format(c.getTime());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 90);
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		endTime=df.format(c.getTime());
		List<TaoBaoOrderInfo> tList=iWarehouseService.getBuyReturnManage(goodsid,page,state,startTime,endTime);
		int count=iWarehouseService.getBuyReturnManageCount(goodsid,state,startTime,endTime);
		json.setRows(tList);
		json.setTotal(count);
		return json;
	}

	/**
	 *
	 * @Title getUserInfo
	 * @Description 获取用户信息
	 * @param request 客户端请求
	 * @param model
	 * @return easy ui结果集
	 * @throws ParseException
	 * @return EasyUiJsonResult
	 */
	@RequestMapping(value = "/getUserInfo.do", method = RequestMethod.POST)
	@ResponseBody
	public EasyUiJsonResult getUserInfo(HttpServletRequest request, Model model) throws ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
		if (adm == null) {
			return json;
		}
		Map<String, Object> map = getStringObjectMap(request);
		if(!(org.apache.commons.lang3.StringUtils.isNotBlank((String)map.get("userid"))&& org.apache.commons.lang3.StringUtils.isNumeric((String)map.get("userid")))){
			json.setMessage("用户id 格式不正确");
			return json;
		}
		map.put("roleType",String.valueOf(adm.getRoletype()));
		userInfos = iWarehouseService.getUserInfoForPrice(map);
		List<UserInfo> userInfoCount = iWarehouseService.getUserInfoForPriceCount(map);
		json.setRows(userInfos);
		json.setTotal(userInfoCount.size());
		return json;
	}

	private Map<String, Object> getStringObjectMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String userid = request.getParameter("userid");
		String stateDate = request.getParameter("stateDate");
		String endDate = request.getParameter("endDate");
		String date = request.getParameter("date");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String recipients = request.getParameter("recipients");
		String recipientsaddress = request.getParameter("recipientsaddress");
		String paymentusername = request.getParameter("paymentusername");
		String paymentid = request.getParameter("paymentid");
		String paymentemail = request.getParameter("paymentemail");
		String conutry = request.getParameter("conutry");
		String admUserId = request.getParameter("admUser");
		String vip = request.getParameter("vip");
		String cyState=request.getParameter("cyState");
		String wjState=request.getParameter("wjState");
		String shState=request.getParameter("shState");
		String fkState=request.getParameter("fkState");
		String cgState=request.getParameter("cgState");
		String dkState=request.getParameter("dkState");
		String stateNumStr = request.getParameter("page");
		String address=request.getParameter("address");
		String state=request.getParameter("state");
		int stateNum=!(stateNumStr == null || "".equals(stateNumStr) || "0".equals(stateNumStr))?Integer.valueOf(stateNumStr):1;
		paymentemail=!StringUtils.isStrNull(paymentemail)?paymentemail.toLowerCase():null;
		email=email != null && !email.equals("")?email.toLowerCase():null;
		vip=StringUtils.isStrNull(vip)?"0":vip;
		admUserId=StringUtils.isStrNull(admUserId)?"0":admUserId;
		map.put("userid", StringUtils.isStrNull(userid)?"0":userid);
		map.put("startdate", StringUtils.isStrNull(stateDate)?null:stateDate);
		map.put("endDate", StringUtils.isStrNull(endDate)?null:endDate);
		map.put("date", date);
		map.put("name", StringUtils.isStrNull(name)?null:name);
		map.put("email", email);
		map.put("recipients", recipients);
		map.put("recipientsaddress", recipientsaddress);
		map.put("paymentusername", paymentusername);
		map.put("paymentid", StringUtils.isStrNull(paymentid)?null:paymentid);
		map.put("paymentemail", paymentemail);
		map.put("admUserId", admUserId);
		map.put("vip", vip);
		map.put("address", StringUtil.isNotBlank(address)?address:null);
		wjState=StringUtil.isNotBlank(wjState)?"4":"9";
		cyState=StringUtil.isNotBlank(cyState)?"3":"9";
		shState=StringUtil.isNotBlank(shState)?"5":"9";
		fkState=StringUtil.isNotBlank(fkState)?"0":"9";
		cgState=StringUtil.isNotBlank(cgState)?"1":"9";
		dkState=StringUtil.isNotBlank(dkState)?"2":"9";
		StringBuilder states=new StringBuilder();
		if(!"9".equals(wjState)){
			states.append(wjState).append(",");
		}
		if(!"9".equals(cyState)){
			states.append(cyState).append(",");
		}
		if(!"9".equals(shState)){
			states.append(shState).append(",");
		}
		if(!"9".equals(fkState)){
			states.append(fkState).append(",");
		}
		if(!"9".equals(cgState)){
			states.append(cgState).append(",");
		}
		if(!"9".equals(dkState)){
			states.append(dkState).append(",");
		}
		map.put("states",states.toString().length()>0?states.toString().substring(0,states.toString().length()-1):null);
		map.put("conutry", StringUtils.isStrNull(conutry)?null:conutry);
		map.put("start", (stateNum - 1) * 20);
		map.put("end", 20);
		map.put("state",StringUtil.isBlank(state)?null:state);
		return map;
	}

	/**
	 * 获取当月平均汇率在用户月利润统计报表
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/getExchange", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> getExchange(HttpServletRequest request, Model model) throws ParseException {
		Map<String,String> p_map=new HashMap<String,String>();
		Map<String,String> r_map=new HashMap<String,String>();
		try{
			String year=request.getParameter("year");
			String month=request.getParameter("month");
			if(StringUtil.isBlank(month) || StringUtil.isBlank(year)){
				return r_map;
			}
			p_map.put("time",year+"-"+month);
			Map<String,String> resultMap=iWarehouseService.getExchange(p_map);
			r_map.put("eur_rate",resultMap.get("eur_rate"));
			r_map.put("cad_rate",resultMap.get("cad_rate"));
			r_map.put("gbp_rate",resultMap.get("gbp_rate"));
			r_map.put("aud_rate",resultMap.get("aud_rate"));
			r_map.put("rmb_rate",resultMap.get("rmb_rate"));
		}catch (Exception e){
			e.printStackTrace();
		}
		return r_map;
	}

	@RequestMapping(value = "/getSalesPerformanceData", method = RequestMethod.POST)
	@ResponseBody
	public String getSalesPerformanceData(HttpServletRequest request, Model model) throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		String state=request.getParameter("state");
		String admuserid=request.getParameter("admuserid");
		String goods_pid=request.getParameter("goodsPid");
		String updatetime=request.getParameter("updatetime");
		map.put("adminId","0".equals(admuserid)?null:admuserid);
		map.put("goodsPid",StringUtil.isBlank(goods_pid)?null:goods_pid);
		map.put("updatetime","0".equals(updatetime)?null:updatetime);
		OrderInfoCountPojo oicp=new OrderInfoCountPojo();
		if("1".equals(state)){
			//产品在搜索结果中被呈现次数
//			oicp = iWarehouseService.getPresentationsData(map);
		}else if("2".equals(state)){
			//产品页面被打开次数
			oicp = iWarehouseService.getOpenCountData(map);
		}else if("3".equals(state)){
			//产品被加购物车次数
			oicp = iWarehouseService.getAddCarData(map);
		}else if("4".equals(state)){
			//产品被购买次数
			oicp = iWarehouseService.getGoodsBuyData(map);
		}else if("5".equals(state)){
			//产生销售额
			oicp = iWarehouseService.getGoodsSalesAmountData(map);
		}else if("6".equals(state)){
			//产品被取消次数
			oicp = iWarehouseService.getCancelData(map);
		}
		return JSONObject.fromObject(oicp).toString();
	}
	/**
	 *
	 * @Title questionnaireStatistics
	 * @Description 问卷调查统计
	 * @param request 客户端请求
	 * @param model
	 * @return jsp页面名称
	 * @throws ParseException
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/questionnaireStatistics.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String questionnaireStatistics(HttpServletRequest request, Model model) throws ParseException {
		// 问卷调查统计
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "(select 'A' title,first_question wt,count(first_question) cnt  from payment_completion_survey group by first_question HAVING first_question != '') union "
				+ "(select 'B',second_question,count(second_question) from payment_completion_survey group by second_question HAVING second_question != '') union "
				+ "(select 'C',third_question,count(third_question) from payment_completion_survey group by third_question HAVING third_question != '')";
		map.put("sql", sql);
		List<AllProblemPojo> allProblem = iWarehouseService.getAllProblem(map);
		sql = "select count(*) from payment_completion_survey";
		map.put("sql", sql);
		int totalNumber = iWarehouseService.getTotalNumber(map);
		sql = "select fourth_question from payment_completion_survey where fourth_question != ''";
		map.put("sql", sql);
		List<String> listStr = iWarehouseService.getAllProposal(map);
		request.setAttribute("allProblem", allProblem);
		request.setAttribute("totalNumber", totalNumber);
		request.setAttribute("listStr", listStr);
		return "investigation1";
	}

	/**
	 *
	 * @Title receivingInvestigation
	 * @Description 问卷调查统计
	 * @param request
	 * @param model
	 * @return jsp页面名称
	 * @throws ParseException
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/receivingInvestigation.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String receivingInvestigation(HttpServletRequest request, Model model) throws ParseException {
		// 问卷调查统计
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select 'A' title,first_question wt,count(first_question) cnt  from confirm_receipt_investigation group by first_question HAVING first_question != '' ";
		map.put("sql", sql);
		List<AllProblemPojo> allProblem = iWarehouseService.getAllProblem(map);
		sql = " select title,wt,sum(cnt) cnt from( "
				+ " (select 'Bparvop' title,second_question wt,count(second_question)cnt from confirm_receipt_investigation group by second_question HAVING second_question like '%Provide a richer variety of products%') union "
				+ " (select 'Bpmcp',second_question,count(second_question) from confirm_receipt_investigation group by second_question HAVING second_question like '%Provide more cheaper products%') union "
				+ " (select 'Bpmcpf',second_question,count(second_question) from confirm_receipt_investigation group by second_question HAVING second_question like '%Provide more convenient payment flow%') union "
				+ " (select 'Boccas' title,second_question wt,count(second_question)cnt  from confirm_receipt_investigation group by second_question HAVING second_question like '%Optimize commodity classification and searching%') union "
				+ " (select 'Bo',second_question,count(second_question) from confirm_receipt_investigation group by second_question HAVING second_question like '%Other%')) a group by title ";
		map.put("sql", sql);
		List<AllProblemPojo> allProblem2 = iWarehouseService.getAllProblem(map);
		sql = "select count(*) from confirm_receipt_investigation";
		map.put("sql", sql);
		int totalNumber = iWarehouseService.getTotalNumber(map);
		sql = "select third_question from confirm_receipt_investigation where third_question != ''";
		map.put("sql", sql);
		List<String> listStr = iWarehouseService.getAllProposal(map);
		request.setAttribute("allProblem", allProblem);
		request.setAttribute("allProblem2", allProblem2);
		request.setAttribute("totalNumber", totalNumber);
		request.setAttribute("listStr", listStr);
		return "investigation2";
	}

	/**
	 *
	 * @Title getTb1688State
	 * @Description 获得淘宝状态
	 * @param request
	 * @param model
	 * @return 返回超过一天未发货订单数量
	 * @throws ParseException
	 * @return String 返回结果类型
	 */
	@RequestMapping(value = "/getTb1688State.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTb1688State(HttpServletRequest request, Model model) throws ParseException {
		String orderstatus = request.getParameter("orderstatus");
		Calendar cal = Calendar.getInstance();
		String admsuerid=request.getParameter("admuserid");
		// 获取采购对应的采购账号名称
		String username = iWarehouseService.getBuyerNames(admsuerid);
		String sql = "SELECT count(DISTINCT orderid) AS num from taobao_1688_order_history where tbOr1688 in ('0','1') and creattime>'2017-01-01' and (orderstatus like '%买家已付款%' or orderstatus like '%等待卖家发货%')";
		if (!"1".equals(admsuerid)) {
			sql += " and username='" + username + "'";
		}
		if ("1".equals(orderstatus)) {
			// 超过1天未发货
			cal.add(Calendar.DATE, -1);
			String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
			sql += "  and orderdate<'" + (yesterday + "16:00:00") + "'";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		Tb1688Pojo oicp = iWarehouseService.getTbState(map);
		return JSONObject.fromObject(oicp).toString();
	}

	/**
	 *
	 * @Title getOrderfeeFreight
	 * @Description 运费统计报表
	 * @param request
	 * @param model
	 * @return jsp页面名称
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getOrderfeeFreight.do", method = RequestMethod.GET)
	public String getOrderfeeFreight(HttpServletRequest request, Model model) {
		String orderid = (String) request.getParameter("orderid");
		String userid = (String) request.getParameter("userid");
		String ckStartTime = (String) request.getParameter("ckStartTime");
		String ckEndTime = (String) request.getParameter("ckEndTime");
		String trans_method = (String) request.getParameter("trans_method");
		String t = request.getParameter("pageNum");
		int pageNum=!StringUtils.isStrNull(t)?Integer.parseInt(t):1;
		t = request.getParameter("pageSize");
		int pageSize=!StringUtils.isStrNull(t)?Integer.parseInt(t):50;
		int startNum = pageNum * pageSize - pageSize;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderid", orderid);
		map.put("userid", userid);
		map.put("ckStartTime", ckStartTime);
		map.put("ckEndTime", ckEndTime);
		if ("0".equals(trans_method)) {
			map.put("trans_method", null);
		} else {
			map.put("trans_method", trans_method);
		}
		int count = iWarehouseService.getOrderfeeFreightCount(map);
		map.put("startNum", startNum);
		map.put("endNum", pageSize);
		List<OrderFeePojo> list = iWarehouseService.getOrderfeeFreight(map,request);
		OrderInfoPage oip = new OrderInfoPage();
		oip.setOrderfeelist(list);
		oip.setPageNum(pageNum);
		oip.setPageSize(pageSize);
		oip.setPageSum(count);
		oip.setOrderid(orderid);
		oip.setUserid(userid);
		oip.setCkEndTime(ckEndTime);
		oip.setCkStartTime(ckStartTime);
		oip.setTrans_method(trans_method);
		request.setAttribute("oip", oip);
		return "orderfeestatistics";
	}

	/**
	 *
	 * @Title getsourceValidation
	 * @Description 获取采购货源验证数据
	 * @param request
	 * @param model
	 * @return 返回Jsp页面名称
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getsourceValidation.do", method = RequestMethod.GET)
	public String getsourceValidation(HttpServletRequest request, Model model) {
		String admuserid = request.getParameter("buyer");
		Map<String, Object> map = new HashMap<String, Object>();
		String acount = "";
		if ("0".equals(admuserid)) {
			admuserid = null;
			acount = null;
		}
		map.put("admuserid", admuserid);
		map.put("acount", acount);
		// 昨天确认采购的订单
		List<String> list_orderid = iWarehouseService.getsourceValidation(map);
		// 采购 所有实际采购的订单
		List<String> list_tbOrderid = iWarehouseService.getsourceValidationForBuy(map);
		OrderInfoPage oip = new OrderInfoPage();
		oip.setList_orderid(list_orderid);
		oip.setList_tbOrderid(list_tbOrderid);
		request.setAttribute("oip", oip);
		return "source_validation";
	}

    /**
     * 新出库审核页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/getDetailsForOrderid", method = RequestMethod.GET)
    public String getDetailsForOrderid(HttpServletRequest request, Model model) {
	    String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
	    Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
	    if (adm == null) {
		    return "1003";
	    }
	    Map<String, Object> map = getStringMapForDetails(request);
        map.put("admName", adm.getId());
        List<StorageLocationBean> list = iWarehouseService.getOrderinfoPage(map);
        OrderInfoPage oip = new OrderInfoPage();
        oip.setPagelist(list);
        request.setAttribute("oip", oip);
        request.setAttribute("oip_size",list.size());
        return "new_outboundAudit";
    }

	private Map<String, Object> getStringMapForDetails(HttpServletRequest request) {
		String orderid = (String) request.getParameter("orderid");
		String userid = (String) request.getParameter("userid");
		String goodid = (String) request.getParameter("goodid");
		String ckStartTime = (String) request.getParameter("ckStartTime");
		String ckEndTime = (String) request.getParameter("ckEndTime");
		String state = (String) request.getParameter("state");
		state="0".equals(state)?null:state;
		String t = request.getParameter("pageNum");
		int pageNum=!StringUtils.isStrNull(t)?Integer.parseInt(t):1;
		t = request.getParameter("pageSize");
		int pageSize=!StringUtils.isStrNull(t)?Integer.parseInt(t):50;
		String sql = "";
		int startNum = pageNum * pageSize - pageSize;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderid", StringUtils.isStrNull(orderid)?"":orderid.trim());
		map.put("userid", userid);
		map.put("goodid", goodid);
		map.put("ckStartTime", ckStartTime);
		map.put("ckEndTime", ckEndTime);
		map.put("state", state);
		map.put("sql", sql);
		map.put("startNum", startNum);
		map.put("endNum", pageSize);
		return map;
	}

	/**
	 * 采购管理页面进入采样页面根据订单查询数据
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getSampleGoods", method = RequestMethod.GET)
	public String getSampleGoods(HttpServletRequest request, Model model) {
    	Map<String,String> paramMap=new HashMap<String,String>();
    	Map<String,Integer> shopNumMap=new HashMap<String,Integer>();
		List<SampleGoodsBean> allList=new ArrayList<SampleGoodsBean>();
    	String orderid=request.getParameter("orderid");
    	try{
    		if(StringUtil.isBlank(orderid)){
    			return "samplingInfo";
		    }
    		paramMap.put("orderid",orderid);
    		//获取订单中非取消的商品pid
		   String pids=iWarehouseService.getAllOrderPid(paramMap);
		   if(StringUtil.isBlank(pids)){
		   	   return "samplingInfo";
		   }
		    //查询客户下单产品必须采样产品信息
		    List<SampleGoodsBean> sList=iWarehouseService.getSampleGoods(pids.split(","));
		   if(sList.size()>0){
			   allList.addAll(sList);
		   }
		   StringBuilder shopIds=new StringBuilder();
		   int index=1;
		   for(SampleGoodsBean s:sList){
		   	 if(index == sList.size()){
			     shopIds.append(s.getR_shop_id());
		     }else{
			     shopIds.append(s.getR_shop_id()).append(",");
		     }
		   }
		    index=1;
		    StringBuilder pIds=new StringBuilder();
		    for(SampleGoodsBean s:sList){
			    if(index == sList.size()){
				    pIds.append(s.getR_pid());
			    }else{
				    pIds.append(s.getR_pid()).append(",");
			    }
		    }
		    //查询备用采样商品
    		    List<SampleGoodsBean> sameList=iWarehouseService.getLiSameGoods(pids.split(","),shopIds.toString().split(","),pIds.toString().split(","));
		    if(sameList.size()>0){
			    allList.addAll(sameList);
		    }
		    for(SampleGoodsBean s:allList){
			    if(shopNumMap.get(s.getR_shop_id())!=null){
				    shopNumMap.put(s.getR_shop_id(),shopNumMap.get(s.getR_shop_id())+1);
			    }else{
				    shopNumMap.put(s.getR_shop_id(),1);
			    }
		    }
		    String maxShop=getMaxId(shopNumMap);
		    //查询建议采样商品信息
		    StringBuilder isExitPids=new StringBuilder();
		    for(SampleGoodsBean s:allList){
				String shopId=s.getR_shop_id();
				if(maxShop.equals(shopId)){
					isExitPids.append("'").append(s.getR_pid()).append("',");
				}
		    }
		    String pid=isExitPids.toString();
		    pid=pid.length()>0?pid.substring(0,pid.length()-1):null;
		    paramMap.put("pids",pid);
		    paramMap.put("maxShop",maxShop);
		    //获取建议采样结果集
		    List<SampleGoodsBean> recommdedSameList=iWarehouseService.getRecommdedSameGoods(paramMap);
		    allList.addAll(recommdedSameList);
		    //解析那样商品数据封装成页面显示的集合形式
		    List<AliInfoDataBean> aliList=analysisSampleData(allList);
		    analysisSampleSkuData(aliList);
		    //解析采样商品类型
		    analysisSampleTypeData(aliList);
		    request.setAttribute("allList",aliList);
	    }catch (Exception e){
		    LOG.error("采购页面那样请求失败"+e);
	    }
		return "samplingInfo";
	}

	/**
	 * 解析那样商品数据封装成页面显示的集合形式
	 * @param recommdedSameList
	 * @return
	 */
	public List<AliInfoDataBean> analysisSampleData(List<SampleGoodsBean> recommdedSameList){
		List<AliInfoDataBean> list=new ArrayList<AliInfoDataBean>();
		for(SampleGoodsBean s:recommdedSameList){
			AliInfoDataBean a = new AliInfoDataBean();
			a.setGoods_pid(s.getPid());
			a.setShop_id(s.getShop_id());
			a.setGoods_name(s.getEnname());
			a.setImg_1688(s.getRemotpath()+s.getCustom_main_image());
			a.setUrl_1688("https://detail.1688.com/offer/"+s.getPid()+".html");
			a.setGoods_url("https://www.aliexpress.com/item/a/"+s.getAli_pid()+".html");
			a.setPrice_1688(s.getWholesale_price());
			a.setR_pid(s.getR_pid());
			a.setR_shop_id(s.getR_shop_id());
			a.setRemarks(s.getRemark());
			String types = com.cbt.util.StrUtils.object2Str(s.getEntype());
			if(!com.cbt.common.StringUtils.isStrNull(types)){
				a.setEntype(Util.getTypeList(types,s.getRemotpath()));
			}
			list.add(a);
		}
		return list;
	}

	public void analysisSampleSkuData(List<AliInfoDataBean> list){
		for (AliInfoDataBean a : list) {
			List<TypeBean> typeList = new ArrayList<TypeBean>();
			Map<String,List<TypeBean>> typeMap = new HashMap<String, List<TypeBean>>();
			String types = com.cbt.util.StrUtils.object2Str(a.getEntype());
			if( !StringUtils.isStrNull(types) && !StringUtils.equals(types, "[]")){
				types = types.replace("[[", "[").replace("]]", "]").trim();
				String[] matchStrList = types.split(Util.CHAR_ONE);
				TypeBean typeBean = null;
				String[] tems = null;
				String tem = null;
				for(String str:matchStrList){
					str = str.replace("[", "").replace("]", "");
					if(str.isEmpty()){
						continue;
					}
					typeBean = new TypeBean();
					String[] type = str.split(Util.NUM_CHAR);
					for(int j=0;j<type.length;j++){
						if(type[j].indexOf("id=") > -1){
							tems = type[j].split("id=");
							tem = tems.length > 1 ? tems[1] : "";
							typeBean.setId(tem);
						}else if(type[j].indexOf("type=") > -1){
							tems = type[j].split("type=");
							tem = tems.length > 1 ? tems[1] : "";
							typeBean.setType(tem.replaceAll(Util.CHINESE_CHAR, ""));
						}else if(type[j].indexOf("value=") > -1){
							tems = type[j].split("value=");
							tem = tems.length>1 ? tems[1] : "";
							tem = StringUtils.equals(tem, "null") ? "" : tem;
							typeBean.setValue(tem.replaceAll(Util.CHINESE_CHAR, ""));
						}else if(type[j].indexOf("img=") > -1){
							tems = type[j].split("img=");
							tem = tems.length > 1 ? tems[1] : "";
							tem = tem.endsWith(".jpg") ? tem : "";
							tem = StringUtils.isStrNull(tem) || StringUtils.equals(tem, "null") ? "" : "" + tem;
							typeBean.setImg(tem);
						}
					}
					List<TypeBean> list1 = typeMap.get(typeBean.getType());
					if(list1 == null ){
						list1 = new ArrayList<TypeBean>();
					}
					if(StringUtils.isStrNull(typeBean.getType())){
						continue;
					}
					if(StringUtils.isStrNull(typeBean.getValue())){
						continue;
					}
					list1.add(typeBean);
					typeMap.put(typeBean.getType(), list1);
				}
				Iterator<Map.Entry<String, List<TypeBean>>> iterator = typeMap.entrySet().iterator();
				List<TypeBean> valueImg = new ArrayList<TypeBean>();
				List<TypeBean> valueTitle = new ArrayList<TypeBean>();
				while(iterator.hasNext()){
					List<TypeBean> values = iterator.next().getValue();
					valueImg.clear();
					valueTitle.clear();
					for(TypeBean value : values){
						if(!StringUtils.isStrNull(value.getImg())){
							valueImg.add(value);
						}else{
							valueTitle.add(value);
						}
					}
					if(!valueImg.isEmpty()){
						typeList.addAll(valueImg);
					}
					if(!valueTitle.isEmpty()){
						typeList.addAll(valueTitle);
					}
				}
				a.setType(typeList);
			}
			StringBuilder list_type=new StringBuilder();
			List<TypeBean> type = a.getType();
			for(TypeBean typeBean : type){
				if(StringUtils.isStrNull(typeBean.getType())){
					continue;
				}
				list_type.append(typeBean.getType());
				String typeParam  = typeBean.getType().trim();
				typeParam = !StringUtils.isStrNull(typeParam) ? typeParam.replaceAll(Util.CHAR_B, " ").replaceAll(Util.CHAR_TWO, "").replace("<", Util.CHAT_FOUR).replace(">", Util.CHAR_FIVE) : "";
				typeBean.setType(typeParam.substring(0,1).toUpperCase()+typeParam.substring(1));
				String labType = !StringUtils.isStrNull(typeParam) ? typeParam.replaceAll(Util.CHAR_W, "").toLowerCase() : "";
				typeBean.setLableType(labType);
				String value = typeBean.getValue();
				value = !StringUtils.isStrNull(value) ? value.replaceAll(Util.CHAR_A, "").replaceAll(Util.CHAR_SEV, "") : "";
				if(!StringUtils.isStrNull(value)){
					value = value.replace("<", Util.CHAT_FOUR).replace(">", Util.CHAR_FIVE);
					typeBean.setValue(value.replaceAll(Util.CHAT_SIX, " "));
				}
			}
			a.setList_type(list_type.toString());
		}
	}

	/**
	 * 解析采样商品类型
	 * @param list
	 */
	public void analysisSampleTypeData(List<AliInfoDataBean> list){
		for(int i=0;i<list.size();i++){
			StringBuilder sb=new StringBuilder();
			String type_name="";
			AliInfoDataBean a=list.get(i);
			a.setR_pid(StringUtil.isBlank(a.getR_pid())?a.getGoods_pid():a.getR_pid());
			List<TypeBean> b_list=a.getType();
			for(int j=0;j<b_list.size();j++){
				TypeBean t=b_list.get(j);
				if(type_name.equals(t.getLableType())){
					if(StringUtils.isStrNull(t.getImg())){
						sb.append("<em id='"+i+"_"+t.getId()+"' onclick='changeColor("+i+",\""+ t.getId() + "\",\""+ a.getR_pid() + "\",\""+ t.getLableType() + "\",\""+ t.getId()+"_"+t.getValue()+"_"+t.getLableType()+"_"+t.getType()+"_"+t.getImg().replace("http:", "https:") + "\");' "
								+ "style='margin: 5px; padding: 3px; display: inline-block; border: 1px solid green; "
								+ "font-style: normal' title='"+t.getValue()+"'>"+t.getValue()+"</em>");
					}else{
						sb.append("<img id='"+i+"_"+t.getId()+"' onclick='changeColor("+i+",\""+ t.getId() + "\",\""+ a.getR_pid() + "\",\""+ t.getLableType() + "\",\""+ t.getId()+"_"+t.getValue()+"_"+t.getLableType()+"_"+t.getType()+"_"+t.getImg().replace("http:", "https:") + "\");' "
								+ "style='margin: 5px; float: left; width: 44px; height: 44px; display: inline-block; "
								+ "border: 1px solid green' title='"+t.getValue()+"' src='"+t.getImg().replace("http:", "https:")+"'>");
					}
					if((b_list.size()>j+1 && !b_list.get(j+1).getLableType().equals(type_name)) || b_list.size()==j+1){
						sb.append("<input type='hidden' class='css_"+a.getR_pid()+"' id='"+i+""+t.getLableType()+"_"+a.getR_pid()+"'>");
						sb.append("</div>");
					}
				}else{
					if(!StringUtils.isStrNull(type_name)){
						sb.append("</div>");
					}
					sb.append("<br>"+t.getLableType()).append(":<div style='overflow:hidden;width:300px;'>");
					if(StringUtils.isStrNull(t.getImg())){
						sb.append("<em id='"+i+"_"+t.getId()+"' onclick='changeColor("+i+",\""+ t.getId() + "\",\""+ a.getR_pid() + "\",\""+ t.getLableType() + "\",\""+ t.getId()+"_"+t.getValue()+"_"+t.getLableType()+"_"+t.getType()+"_"+t.getImg().replace("http:", "https:") + "\");' "
								+ "style='margin: 5px; padding: 3px; display: inline-block; border: 1px solid green; "
								+ "font-style: normal' title='"+t.getValue()+"'>"+t.getValue()+"</em>");
					}else{
						sb.append("<img id='"+i+"_"+t.getId()+"' onclick='changeColor("+i+",\""+ t.getId() + "\",\""+ a.getR_pid() + "\",\""+ t.getLableType() + "\",\""+ t.getId()+"_"+t.getValue()+"_"+t.getLableType()+"_"+t.getType()+"_"+t.getImg().replace("http:", "https:") + "\");' "
								+ "style='margin: 5px; float: left; width: 44px; height: 44px; display: inline-block; "
								+ "border: 1px solid green' title='"+t.getValue()+"' src='"+t.getImg().replace("http:", "https:")+"'>");
					}
				}
				type_name=t.getLableType();
			}
			a.setType_msg(sb.toString());
			a.setSku_inventory(getSkuCount(a.getSku_inventory()));
		}
	}

	/**
	 * 获取规格库存数量
	 * @param skuMap
	 * @return
	 */
	public  String getSkuCount(String skuMap){
		StringBuilder sb=new StringBuilder();
		if(!StringUtils.isStrNull(skuMap) && skuMap.contains("skuMap:") && skuMap.contains(",end")){
			skuMap=skuMap.replaceAll("\"","");
			skuMap=skuMap.split("skuMap:")[1];
			skuMap=skuMap.split(",end")[0];
			skuMap=skuMap.substring(1,skuMap.length());
			skuMap=skuMap.substring(0,skuMap.length()-2);
			String [] skus =skuMap.split("},");
			int count=0;
			for(int i=0;i<skus.length;i++){
				String sku_=skus[i];
				sku_=sku_.replace(Util.CHAR_FIVE, "&");
				String []maps=sku_.split(Util.CHAR_C);
				String []sku1=maps[1].split(",");
				String name=maps[0];
				for (int j=0;j<sku1.length;j++) {
					if(sku1[j].contains("canBookCount") && Integer.valueOf(sku1[j].split(":")[1])<=0){
						sb.append(name).append(":").append("<span style='color:red'>"+sku1[j].split(":")[1]+"</span>").append("<br>");
						count++;
						break;
					}
				}
			}
			if(count>=10){
				sb=new StringBuilder("该商品库存数量超过10个为0，不建议采样");
			}
		}
		return sb.toString();
	}

	/**
	 * 查询匹配商品最多的店铺id
	 * @param shopMap
	 * @return
	 */
	public String getMaxId(Map<String, Integer> shopMap) {
		String shopId = "99999";
		Collection<Integer> c = shopMap.values();
		Object[] obj = c.toArray();
		Arrays.sort(obj);
		Iterator it = shopMap.keySet().iterator();
		while (it.hasNext()) {
			String key;
			key = it.next().toString();
			if (shopMap.get(key) == obj[obj.length-1]) {
				shopId = key;
				break;
			}
		}
		return shopId;
	}

	/**
	 *
	 * @Title getOrderinfoPage
	 * @Description 商品库位对照
	 * @param request
	 * @param model
	 * @return 返回jsp页面名称
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getOrderinfoPage.do", method = RequestMethod.GET)
	public String getOrderinfoPage(HttpServletRequest request, Model model) {
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
		if (adm == null) {
			return "1003";
		}
		String orderid = (String) request.getParameter("orderid");
		String userid = (String) request.getParameter("userid");
		String goodid = (String) request.getParameter("goodid");
		String ckStartTime = (String) request.getParameter("ckStartTime");
		String ckEndTime = (String) request.getParameter("ckEndTime");
		String state = (String) request.getParameter("state");
		state="0".equals(state)?null:state;
		String t = request.getParameter("pageNum");
		int pageNum=!StringUtils.isStrNull(t)?Integer.parseInt(t):1;
		t = request.getParameter("pageSize");
		int pageSize=!StringUtils.isStrNull(t)?Integer.parseInt(t):50;
		String sql = "";
		int startNum = pageNum * pageSize - pageSize;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderid", StringUtils.isStrNull(orderid)?"":orderid.trim());
		map.put("userid", userid);
		map.put("goodid", goodid);
		map.put("ckStartTime", ckStartTime);
		map.put("ckEndTime", ckEndTime);
		map.put("state", state);
		map.put("sql", sql);
		map.put("admName", adm.getId());
		int count = iWarehouseService.getCountOrderinfo(map);
		map.put("startNum", startNum);
		map.put("endNum", pageSize);
		List<StorageLocationBean> list = iWarehouseService.getOrderinfoPage(map);
		OrderInfoPage oip = new OrderInfoPage();
		// 如果不是第一次 查询 就已经保存了 OrderInfoPage
		oip.setPagelist(list);
		oip.setPageNum(pageNum);
		oip.setPageSize(pageSize);
		oip.setPageSum(count);
		oip.setOrderid(orderid);
		oip.setUserid(userid);
		oip.setCkEndTime(ckEndTime);
		oip.setCkStartTime(ckStartTime);
		oip.setGoodid(goodid);
		oip.setRoleType(adm.getRoletype());
		request.setAttribute("oip", oip);
		return "orderinfopage";
	}

	/**
	 *
	 * @Title getOrderMainFee
	 * @Description 正式出库列表
	 * @param request
	 * @param model
	 * @return 返回Jsp页面名称
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getOrderMainFee.do", method = RequestMethod.GET)
	public String getOrderMainFee(HttpServletRequest request, Model model) {
		String orderidQuery = request.getParameter("orderidQuery");
		String useridQuery = request.getParameter("useridQuery");
		request.setAttribute("orderidQuery", orderidQuery);
		request.setAttribute("useridQuery", useridQuery);
		return "orderfeepage";
	}

    /**
     * 出库审核验证商品号是否为该订单商品
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/checkedGoods")
    public void checkedGoods(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
//		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
//		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
        String orderid=request.getParameter("orderid");
        String goodsid=request.getParameter("goodsid");
        Map<String, String> map = new HashMap<String, String>(); // sql 参数
        map.put("orderid", orderid);
        map.put("goodsid", goodsid);
        PrintWriter out = response.getWriter();
        out.print(iWarehouseService.checkedGoods(map));
        out.close();
    }

	/**
	 * 采购对采购的商品进行授权操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/productAuthorization")
	public void productAuthorization(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String odid=request.getParameter("odid");
		String type=request.getParameter("type");
		String goodsPid=request.getParameter("goodsPid");
		Map<String, String> map = new HashMap<String, String>(); // sql 参数
		map.put("odid", odid);
		map.put("flag", type);
		map.put("goodsPid",goodsPid);
		PrintWriter out = response.getWriter();
		out.print(iWarehouseService.productAuthorization(map));
		out.close();
	}

	/**
	 * 停用、启用优先类别数据
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateStateCategory", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateStateCategory(HttpServletRequest request, Model model) {
		String id=request.getParameter("id");
		String state=request.getParameter("state");
		Map<String, String> map = new HashMap<String, String>(); // sql 参数
		map.put("state", state);
		map.put("id", id);
		return iWarehouseService.updateStateCategory(map) + "";
	}

	/**
	 * 修改搜索词对应的优先类别
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editKeyword", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String editKeyword(HttpServletRequest request, Model model) {
		String id=request.getParameter("id");
		String cid=request.getParameter("cid");
		Map<String, String> map = new HashMap<String, String>(); // sql 参数
		map.put("cid", cid);
		map.put("id", id);
		return iWarehouseService.editKeyword(map) + "";
	}

	/**
	 * 新增优先类别数据
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addKeyword", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addKeyword(HttpServletRequest request, Model model) {
		String cateId=request.getParameter("cateId");
		String keyword=request.getParameter("keyword");
		Map<String, String> map = new HashMap<String, String>(); // sql 参数
		map.put("keyword", keyword);
		map.put("cateId", cateId);
		return iWarehouseService.addKeyword(map) + "";
	}
	/**
	 *
	 * @Title getSjCgCount
	 * @Description 获得实际采购数量
	 * @param request
	 * @param model
	 * @return 返回采购实际采购数量
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/insertOrderRemark", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String insertOrderRemark(HttpServletRequest request, Model model) {
		String admuserid=request.getParameter("admuserid");
		String orderid=request.getParameter("orderid");
		String remark=request.getParameter("remark");
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("admuserid", admuserid);
		map.put("orderid", orderid);
		map.put("remark", remark);
		return iWarehouseService.insertOrderRemark(map) + "";
	}

	/**
	 * 出库时检验申报金额是否超出预定金额
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/checkAmount", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String checkAmount(HttpServletRequest request, Model model) {
		int res=0;
		String count=request.getParameter("sbsl");
		String price=request.getParameter("sbjg");
		String orderid=request.getParameter("orderid");
		double amount=0.00;
		if(StringUtil.isNotBlank(count) && StringUtil.isNotBlank(price)){
			amount=Double.parseDouble(count)*Double.parseDouble(price);
		}
		//获取订单国家或所属地区
		CustomsRegulationsPojo c=iWarehouseService.getCustomsRegulationsPojo(orderid);
		if(c !=null && amount>c.getAmount()){
			res=1;
		}
		return res+"";
	}

	/**
	 * 出运时运费预警信息录入
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/insertWarningInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String insertWarningInfo(HttpServletRequest request, Model model) {
		int res=0;
		Map<String,String> map=new HashMap<String,String>(2);
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String time = sdf.format(date);
		String remark=request.getParameter("remark");
		String orderid=request.getParameter("orderid");
		String old_remark=request.getParameter("old_remark");
		StringBuilder remarks=new StringBuilder();
		try{
			old_remark=StringUtil.isBlank(old_remark)?"":old_remark;
			remarks.append(old_remark).append("\r\n").append(adm.getAdmName()).append("-").append(time).append(":").append(remark);
			map.put("orderid",orderid);
			map.put("remark",remarks.toString());
			res=iWarehouseService.insertWarningInfo(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		return res + "";
	}

	/**
	 *
	 * @Title sendMail
	 * @Description 发送邮件
	 * @param request
	 * @param model
	 * @return 是否成功标识
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/sendMail.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String sendMail(HttpServletRequest request, Model model) {
		String userid = request.getParameter("userid"); // 用户id
		String toEmail = request.getParameter("toEmail"); // 用户邮箱
		String sbBuffer = request.getParameter("sbBuffer"); // 发送内容
		String orderNo = request.getParameter("orderNo"); // 订单号
		userid=StringUtils.isStrNull(userid)?"0":userid;
		toEmail=StringUtils.isStrNull(toEmail)?"0":toEmail;
		sbBuffer=StringUtils.isStrNull(sbBuffer)?"0":sbBuffer;
		toEmail=StringUtils.isStrNull(toEmail)?"0":toEmail;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		AdmuserPojo admuser = iWarehouseService.getAdmuserSendMailInfo(map);
		String sendemail = admuser.getEmail();
		String pwd = admuser.getEmailpass();
		int res=0;
		try{
			SendMQ sendMQ = new SendMQ();
			res = SendEmail.send(sendemail,pwd,toEmail,sbBuffer.toString(),"Attention! Shipping fee needed before we deliver out your ImportExpress order!","", orderNo, 1);
			// 设置订单还需付款
			String remaining_price = request.getParameter("remaining_price");
			if (remaining_price != null && !"".equals(remaining_price)) {
				double iTemp = Double.parseDouble(remaining_price);
				if (iTemp > 0) {
					Map<String, String> map2 = new HashMap<String, String>();
					map2.put("orderid", orderNo);
					map2.put("remaining_price", remaining_price);
					iWarehouseService.updateRemainingPrice(map2);
					sendMQ.sendMsg(new RunSqlModel("update orderinfo set remaining_price='"+remaining_price+"' where order_no='"+orderNo+"'"));
					sendMQ.sendMsg(new RunSqlModel("update shipping_package set issendmail=1 where orderid='"+orderNo+"'"));
					// 标志位已发送邮件
					iWarehouseService.updateSendMail(map2);
				}
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return res + "";
	}

	/**
	 *
	 * @Title getOrderAddress
	 * @Description 根据订单id获得地址
	 * @param request
	 * @param model
	 * @return 返回地址信息
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getOrderAddress.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOrderAddress(HttpServletRequest request, Model model) {
		String orderid = request.getParameter("orderid");
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("orderid", StringUtils.isStrNull(orderid)?"":orderid);
		String ret = iWarehouseService.getOrderAddress(map);
		return ret;
	}

	/**
	 *
	 * @Title getOrderFee
	 * @Description 正式出库列表
	 * @param request
	 * @param model
	 * @return 返回List<OrderFeePojo>字符串
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getOrderFee.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOrderFee(HttpServletRequest request, Model model) {
		String orderType = request.getParameter("typeOrder");
		String useridAndorderno = request.getParameter("useridAndorderno");
		String sql = "";
		if (orderType == null || "".equals(orderType)) {
			sql = "and state in(1,4) ";
		} else if ("1".equals(orderType)) {
			sql = "and state in(1,4) ";
		} else if ("2".equals(orderType)) {
			sql = "and state=0";
		}
		if (useridAndorderno != null && !"".equals(useridAndorderno)) {
			sql += " and (userid like '%" + useridAndorderno+ "%' or orderno like '%" + useridAndorderno + "%'  )";
		}
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("sql", sql);
		List<OrderFeePojo> oflist = iWarehouseService.getOrderFee(map);
		// 处理差钱标志
		if ("2".equals(orderType)) {
			Map<String, Object> map2 = new HashMap<String, Object>(); // sql 参数
			List<OrderInfoPojo> orderInfoPojoList = iWarehouseService.getNotMoneyOrderinfo(map2);
			for (int i = 0; i < oflist.size(); i++) {
				for (int j = 0; j < orderInfoPojoList.size(); j++) {
					// 存在已还清订单 IsBadMoney设置 为 1
					if (oflist.get(i).getOrderno().equals(orderInfoPojoList.get(j).getOrder_no())) {
						oflist.get(i).setIsBadMoney("1");
						break;
					}
				}
			}
		}
		return net.sf.json.JSONArray.fromObject(oflist).toString();
	}

	/**
	 *
	 * @Title getOrderProblem
	 * @Description 综合采购获得订单问题
	 * @param request
	 * @param model
	 * @return 返回map字符串
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getOrderProblem.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOrderProblem(HttpServletRequest request, Model model) {
		String orderno = request.getParameter("orderno");
		orderno=StringUtils.isStrNull(orderno)?"1001":orderno;
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("orderno", orderno);
		return iWarehouseService.getOrderProblem(map);
	}

	/**
	 *
	 * @Title getFpxCountryCode
	 * @Description 查询国家
	 * @param request
	 * @param model
	 * @return 返回List<OrderFeePojo>字符串
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getFpxCountryCode.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getFpxCountryCode(HttpServletRequest request, Model model) {
		List<OrderFeePojo> countryCodeList = iWarehouseService.getFpxCountryCode();
		return net.sf.json.JSONArray.fromObject(countryCodeList).toString();
	}

	/**
	 *
	 * @Title getNotMoneyOrderinfo
	 * @Description 差钱已还清订单
	 * @param request
	 * @param model
	 * @return 返回List<OrderInfoPojo>字符串
	 * @return String 返回值类型
	 */
	@RequestMapping(value = "/getNotMoneyOrderinfo.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getNotMoneyOrderinfo(HttpServletRequest request, Model model) {
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		List<OrderInfoPojo> orderInfoPojoList = iWarehouseService.getNotMoneyOrderinfo(map);
		return net.sf.json.JSONArray.fromObject(orderInfoPojoList).toString();
	}

	// 起他出货方式
	@RequestMapping(value = "/getCodemaster.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getCodemaster(HttpServletRequest request, Model model) {
		List<OrderFeePojo> countryCodeList = iWarehouseService.getCodemaster();
		return net.sf.json.JSONArray.fromObject(countryCodeList).toString();
	}

	// 4px运输方式
	@RequestMapping(value = "/getFpxProductCode.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getFpxProductCode(HttpServletRequest request, Model model) {
		List<OrderInfoPojo> fpxProductCode = iWarehouseService.getFpxProductCode();
		return net.sf.json.JSONArray.fromObject(fpxProductCode).toString();
	}

	// 出库和欠费数量
	@RequestMapping(value = "/getOutCount.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOutCount(HttpServletRequest request, Model model) {
		List<OrderInfoPojo> fpxProductCode = iWarehouseService.getOutCount();
		return net.sf.json.JSONArray.fromObject(fpxProductCode).toString();
	}

	// 所有金额
	@RequestMapping(value = "/getPaymentFy.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPaymentFy(HttpServletRequest request, Model model) {
		// 取查询条件参数
		String order_no = (String) request.getParameter("order_no");
		String user_id = (String) request.getParameter("user_id");
		if (StringUtils.isStrNull(order_no) || StringUtils.isStrNull(user_id)) {
			return "0";
		}
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("user_id", user_id);
		map.put("order_no", order_no);
		OrderInfoPojo fpxProductCode = iWarehouseService.getPaymentFy(map);
//		double exchange_rate = 1;
		Map<String, Double> maphl = Currency.getMaphl(request);
//		exchange_rate = maphl.get("RMB");
//		exchange_rate = exchange_rate / maphl.get(fpxProductCode.getCurrency());
		double t = Double.parseDouble(fpxProductCode.getExchange_rate())* (Double.parseDouble(fpxProductCode.getSumPayment_amount())-fpxProductCode.getMemberFee());
		double actual_lwh = Double.parseDouble(fpxProductCode.getExchange_rate())*fpxProductCode.getActual_lwh();
		BigDecimal b = new BigDecimal(t);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		fpxProductCode.setSumPayment_amountRMB(f1 + "");
		b = new BigDecimal(t);
		f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		fpxProductCode.setActual_lwh(f1);
		return net.sf.json.JSONObject.fromObject(fpxProductCode).toString();
	}

	// 获得嘉城运输方式
	@RequestMapping(value = "/getJcYsfs.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getJcYsfs(HttpServletRequest request, Model model) {
		String fpxCountryCode = request.getParameter("fpxCountryCode"); // 国家
		String weight = request.getParameter("weight"); // 重量
		String volumelwh = request.getParameter("volumelwh"); // 体积
		// 佳成
		PriceData pd = new PriceData();
		pd.setHYBM("SH0809"); // 账号 ZYW0037
		pd.setPWD("SH61504007"); // 密码 1
		pd.setWorldCountry(fpxCountryCode); // 城市
		pd.setFasterWay(""); // 快件网络
		pd.setFasterPort("0"); // 出运口岸 KNSHA
		pd.setPackageType("PAK"); // 货件类型
		pd.setCweight(weight); // 货物实重
		if (volumelwh != null && !"".equals(volumelwh)) {
			String[] s = volumelwh.split("\\*");
			if (s.length == 3) {
				pd.setClong(s[0]); // 长
				pd.setCwidth(s[1]); // 宽
				pd.setCheight(s[2]); // 高
			}
		}
		return getJcexTime(pd);
	}

	// 设置参数
	public Map<String, Object> setMapData(Map<String, Object> mainMap) {
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("orderno", mainMap.get("orderid"));
		map.put("userid", mainMap.get("userid"));
		map.put("yfhNum", mainMap.get("courierNumber"));
		map.put("country_code", mainMap.get("fpxCountryCode"));
		String tempStr = (String) mainMap.get("unpaidFreight");
		tempStr=StringUtils.isStrNull(tempStr)?"0":tempStr;
		map.put("unpay", tempStr);
		map.put("cargo_type", "PAK");
		map.put("state", '1');
		map.put("trans_method", mainMap.get("trans_method"));
		map.put("trans_details", mainMap.get("fpxProductCode"));
		map.put("package_fee", mainMap.get("packingCharge"));
		tempStr = (String) mainMap.get("actualFreight");
		tempStr=StringUtils.isStrNull(tempStr)?"0":tempStr;
		map.put("acture_fee", tempStr);
		map.put("volume_lwh", mainMap.get("volumelwh"));
		map.put("weight", mainMap.get("weight"));
		map.put("order_area", mainMap.get("trans"));
		map.put("logistics_name", mainMap.get("logistics_name"));
		return map;
	}

	// 原飞航出货
	public int outYfhShipment(UserOrderDetails uod, Map<String, Object> mainMap) {
		String orderlist = (String) mainMap.get("orderid") + "<br/>";// 合并订单
		String[] ck = orderlist.split("<br/>");
		uod.setMark(ck[0]);
		String yuanfeihangno = (String) mainMap.get("courierNumber");
		return yfhApiApplication(uod, orderlist, yuanfeihangno);
	}

	// 原飞航出货
	public int outYfhShipment127(UserOrderDetails uod,Map<String, Object> mainMap) {
		String orderlist = (String) mainMap.get("orderid") + "<br/>";// 合并订单
		String[] ck = orderlist.split("<br/>");
		uod.setMark(ck[0]);
		String yuanfeihangno = (String) mainMap.get("courierNumber");
		Purchase p = new Purchase();
		return p.yfhApiApplication127(uod, orderlist, yuanfeihangno);
	}

	public String saveOnlyshipping(Map<String, Object> map,Map<String, Object> mainMap,float fy){
		String msg="";
		map.put("state", "0"); // 还差钱
		try{
			int t = iWarehouseService.updateOrderFeeByOrderid(map);
			// 保存
			if (t == 0) {
				msg="1001"; // 保存失败，导致出货失败
			} else {
				// 保存成功，继续修改orderinfo 将差的钱 存放进去
				Map<String, Object> mapOrderinf = new HashMap<String, Object>(); //
				mapOrderinf.put("order_no", mainMap.get("orderid"));
				// 本地
				iWarehouseService.updateOrderinfoAll(mapOrderinf); // 如果合并订单
				// 线上
//			DataSourceSelector.set("dataSource127hop");
//			iWarehouseService.updateOrderinfoAll(mapOrderinf); // 如果合并订单
//			DataSourceSelector.restore();
				SendMQ sendMQ = new SendMQ();
				sendMQ.sendMsg(new RunSqlModel("update orderinfo set remaining_price=0 where order_no in(select orderno from order_fee where mergeOrders='"+mapOrderinf.get("order_no")+"')"));
				mapOrderinf.put("remaining_price", fy); // 差的钱
				mapOrderinf.put("actual_ffreight", mainMap.get("actualFreight")); // 运费
				mapOrderinf.put("actual_freight_c",mainMap.get("actualFreight"));// 运费
				// 本地
				iWarehouseService.updateOrderinfo(mapOrderinf); // 在将钱，存放合并的主订单
				// 线上
				sendMQ.sendMsg(new RunSqlModel("update orderinfo set  remaining_price='"+mapOrderinf.get("remaining_price")+"' , actual_ffreight='"+mapOrderinf.get("actual_ffreight")+"' , " +
						"actual_freight_c='"+mapOrderinf.get("actual_freight_c")+"' where order_no = '"+mapOrderinf.get("order_no")+"'"));
				msg="1003";
				sendMQ.closeConn();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return msg;
	}

	// 出库
	@RequestMapping(value = "/shipment.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTest(HttpServletRequest request, Model model, @RequestBody Map<String, Object> mainMap) {
		// 未支付费用
		String tStr = (String) mainMap.get("unpaidFreight");
		float fy=!StringUtils.isStrNull(tStr)?Float.parseFloat(tStr):-9999;
		// 保存的数据
		int t = 0;
		final Map<String, Object> map = setMapData(mainMap);
		if (fy > 0) { // 差钱 只保存不出货
			return saveOnlyshipping(map,mainMap,fy);
		} else { // 不差钱开始出货
			map.put("state", "1"); // 可出库
			// 本地
			t = iWarehouseService.updateOrderFeeByOrderid(map);
			if (t == 0) {
				return "1001"; // 保存失败，导致出货失败
			}
			// 获取出货信息
			String orderid = (String) mainMap.get("orderid");
			PurchaseServer purchaseServer = new PurchaseServerImpl();
			UserOrderDetails uod = purchaseServer.getUserDetails(orderid + ",");
			tStr = mainMap.get("trans_method").toString(); // 出货方式
			// 设置国家
			uod.setUserzone((String) mainMap.get("fpxCountryCode"));
			uod.setOutmethod("0");// 出货方式,0:4PX;1:原飞航;2:其他方式
			uod.setUsercity(uod.getAddress2()); // 城市
			uod.setUserstreet(uod.getUserstreet() + uod.getAddress()); // 街道
			uod.setUserstate(uod.getStatename());// 省
			uod.setUsercode(uod.getZipcode()); // 收件人邮编
			uod.setUserphone(uod.getPhone()); // 收件人电话
			uod.setTransport((String) mainMap.get("fpxProductCode"));// 产品代码，指DHL、新加坡小包挂号、联邮通挂号等，
			// 申报信息添加
			if (tStr.equals("1") || tStr.equals("3") || tStr.equals("4")) { // 4px
				List<Map<String, Object>> listmap = (List<Map<String, Object>>) mainMap.get("listmap");
				List<ProductBean> productList = new ArrayList<ProductBean>();
				for (int i = 0; i < listmap.size(); i++) {
					ProductBean pbean = new ProductBean();
					pbean.setProductname(listmap.get(i).get("sbzwpm").toString());
					pbean.setProducenglishtname(listmap.get(i).get("sbywpm").toString());
					pbean.setProductremark(listmap.get(i).get("sbphbz").toString());
					pbean.setProductnum(listmap.get(i).get("sbsl").toString());
					pbean.setProductprice(listmap.get(i).get("sbjg").toString());
					pbean.setProductcurreny(listmap.get(i).get("sbdw").toString());
					productList.add(pbean);
					// 保存申报信息
					Map<String, Object> declares = new HashMap<String, Object>(); // sql
					declares.put("productname", pbean.getProductname());
					declares.put("producenglishtname",pbean.getProducenglishtname());
					declares.put("productnum", pbean.getProductnum());
					declares.put("productprice", pbean.getProductprice());
					declares.put("productcurreny", pbean.getProductcurreny());
					declares.put("productremark", pbean.getProductremark());
					declares.put("orderid", mainMap.get("orderid"));
					t = iWarehouseService.updateOrderFeeByOrderid(map);
					iWarehouseService.insertDeclareinfo(declares);
				}
				uod.setProductBean(productList);
			}
			String volumelwh = (String) mainMap.get("volumelwh");
			String volumelwhArray[] = volumelwh.split("\\*");
			uod.setWeight((String) mainMap.get("weight"));
			// 保存信息用来打印面单
			request.getSession().setAttribute("pc", volumelwhArray[0]);
			request.getSession().setAttribute("pk", volumelwhArray[1]);
			request.getSession().setAttribute("pg", volumelwhArray[2]);
			request.getSession().setAttribute("uod", uod);
			JcgjSoapHttpPost jc = new JcgjSoapHttpPost();
			String number = "";
			try {
				number = jc.callGetNumber(uod.getUserzone(), "887799");
			} catch (Exception e) {
				LOG.info("获取加成运单号失败可能是访问API失败");
			}
			request.getSession().setAttribute("number", number);
			// 判断出货方式
			if ("1".equals(tStr)) { // 4px
				uod.setGoodstype("P"); // 包裹
				Purchase p = new Purchase();
				String orderlist = (String) mainMap.get("orderid") + "<br/>";// 合并订单
				int ri = p.fpxApiApplication(uod, orderlist, "4PX");
				if (ri == 4) {
					return "1000";
				} else {
					return "1004";
				}
			} else if ("2".equals(tStr)) { // 原飞航
				outYfhShipment(uod, mainMap);
				String orderlist = (String) mainMap.get("orderid") + "<br/>";// 合并订单
				String[] ck = orderlist.split("<br/>");
				uod.setMark(ck[0]);
				String yuanfeihangno = (String) mainMap.get("courierNumber");
				Purchase p = new Purchase();
				p.yfhApiApplication(uod, orderlist, yuanfeihangno);
			} else if ("3".equals(tStr)) { // 嘉城
				try {
					uod.setGoodstype("PAK"); // 包裹
					Purchase p = new Purchase();
					int ri = Integer.parseInt(p.outJc(uod, number));
					if (ri == 10000) {
						return "1000";
					}else {
						return "1003";
					}
				} catch (Exception e) {
					LOG.info("佳成出货失败");
				}
			} else if ("4".equals(tStr)) {
				List<RecList> lr = new ArrayList<RecList>();
				List<GoodsPojo> list = new ArrayList<GoodsPojo>();
				// 申报信息
				List<ProductBean> proBeanList = new ArrayList<ProductBean>();
				proBeanList = uod.getProductBean();
				int ppp = proBeanList.size();
				for (int p = 0; p < ppp; p++) {
					// 申报信息
					GoodsPojo gl = new GoodsPojo();
					gl.setCxGoods(proBeanList.get(p).getProductname()); // 物品描述,0-63字符。必须。
					gl.setIxQuantity(proBeanList.get(p).getProductnum()); // 物品数量。必须。
					gl.setFxPrice(proBeanList.get(p).getProductprice()); // 物品单价，2位小数。
					gl.setCxGoodsA(proBeanList.get(p).getProductname()); // 物品英文描述,0-63字符。
					list.add(gl);
				}
				// 运单信息
				RecList r = new RecList();
				r.setiID("0");
				r.setnItemType("1"); // 1, 快件类型，默认为1。取值为：0(文件),1(包裹),2(防水袋)
				String orderidTemp = uod.getOrderno();
				if (orderid.indexOf("-1") != -1) {
					orderidTemp = orderidTemp.substring(0,orderidTemp.indexOf("-1"));
				}
				r.setcRNo(orderidTemp); // (传用户系统订单号，只允许数字和字母，中划线，其他符号不接受）
				r.setcDes(uod.getUserzone()); // 收件国家名
				r.setfWeight(uod.getWeight()); // 重量，公斤，3位小数
				r.setcReceiver(uod.getUserName()); // 收件人,0-63字符
				r.setcRPhone(uod.getUserphone()); // 收件电话,0-63字符。
				r.setcREMail(uod.getEmail()); // 收件电邮,0-63字符
				r.setcRPostcode(uod.getUsercode()); // 收件邮编,0-15字符。
				r.setcRCountry(uod.getUserzone()); // 收件国家【必须为英文】,0-126字符。
				r.setcRProvince(uod.getUserstate()); // 收件省州,0-63字符。
				r.setcRCity(uod.getUsercity()); // 收件城市,0-126字符。
				r.setcRAddr(uod.getAddress() + uod.getUserstreet()); // 收件地址,0-254字符。.
				r.setGoodsList(list);
				r.setcEmsKind((String) mainMap.get("fpxProductCode"));
				lr.add(r);
				Map<String, Object> mapR = new HashMap<String, Object>();
				mapR.put("RequestName", "PreInputSet"); // PreInputSet
				mapR.put("icID", 14334);// 71 14334
				String nowTime = null;
				try {
					nowTime = jc.preInputSet("{\"RequestName\":\"TimeStamp\"}");
					nowTime = nowTime.substring(nowTime.indexOf(":") + 1,nowTime.length() - 1);
				} catch (Exception e1) {
					LOG.info("获取CNE物流单号失败");
				}
				String md5 = jc.string2MD5("14334" + nowTime+ "Huloqx3H3hko1MU");
				mapR.put("TimeStamp", nowTime);
				mapR.put("MD5", md5);
				mapR.put("RecList", lr);
				String t2 = jc.objToGson(mapR);
				String ret = "";
				try {
					ret = jc.preInputSet(t2);
				} catch (Exception e) {
					LOG.info("获取CNE物流单号失败");
				}
				JSONObject json = JSONObject.fromObject(ret);
				List<Map<String, String>> edit = (List<Map<String, String>>) json.getJSONArray("ErrList");
				String cNum = edit.get(0).get("cNum");
				String logistics_name = (String) mainMap.get("logistics_name");
				String orderlist = (String) mainMap.get("orderid") + "<br/>";// 合并订单
				Purchase p = new Purchase();
				p.outOtherApplication(uod, orderlist, cNum, logistics_name);
			}
		}
		return "1000";
	}

	// 超时退出方法
	public String checkDBStatus(final String country, final String weight,
								final String lwh, final String pack, final String procode,
								final String currency, final Map<String, Double> maphl) {
		String bdStatus = "500";
		final ExecutorService exec = Executors.newFixedThreadPool(1);
		Callable<String> call = new Callable<String>() {
			public String call() throws Exception {
				ChargeCalculateSample sample = new ChargeCalculateSample();
				return sample.chargeCalculateService(country, weight, lwh,pack, procode, currency, maphl);
			}
		};
		try {
			Future<String> future = exec.submit(call);
			String obj = future.get(1000 * 4, TimeUnit.MILLISECONDS);
			bdStatus = obj;
			System.out.println("the return value from call is :" + obj);
		} catch (TimeoutException ex) {
			System.out.println("====================4px执行超时===============");
			ex.printStackTrace();
			bdStatus = "500";
		} catch (Exception e) {
			System.out.println("failed to handle.");
			e.printStackTrace();
			bdStatus = "600";
		}
		exec.shutdown();
		return bdStatus;
	}

	// 超时退出方法
	public String getJcexTime(final PriceData pd) {
		String bdStatus = "500";
		final ExecutorService exec = Executors.newFixedThreadPool(1);
		Callable<String> call = new Callable<String>() {
			public String call() throws Exception {
				JcgjSoapHttpPost jc = new JcgjSoapHttpPost(); // gson
				List<PriceReturnJsonNew> list = jc.getJcFreight(pd);
				if (list != null) {
					getJcexTime(pd);
					return net.sf.json.JSONArray.fromObject(list).toString();
				} else {
					return "500";
				}

			}
		};
		try {
			Future<String> future = exec.submit(call);
			String obj = future.get(1000 * 5, TimeUnit.MILLISECONDS);
			bdStatus = obj;
			System.out.println("the return value from call is :" + obj);
		} catch (TimeoutException ex) {
			System.out.println("====================佳成执行超时===============");
			ex.printStackTrace();
			bdStatus = "500";
		} catch (Exception e) {
			System.out.println("failed to handle.");
			e.printStackTrace();
			bdStatus = "600";
		}
		exec.shutdown();
		return bdStatus;
	}

	// 4px运输方式对应运输时间
	@RequestMapping(value = "/getQtFs.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getQtFs(HttpServletRequest request, Model model) {
		JcgjSoapHttpPost jc = new JcgjSoapHttpPost();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("RequestName", "EmsKindList"); // PreInputSet
		String t = jc.objToGson(map);
		System.out.println(t);
		String r = "";
		try {
			r = jc.preInputSet(t);
			System.out.println(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = JSONObject.fromObject(r);
		List<Map<String, String>> edit = (List<Map<String, String>>) json.getJSONArray("List");
		List<String> ls = new ArrayList<String>();
		for (int i = 0; i < edit.size(); i++) {
			String oName = edit.get(i).get("oName");
			ls.add(oName);
		}
		return net.sf.json.JSONArray.fromObject(ls).toString();
	}

	// 4px运输方式对应运输时间
	@RequestMapping(value = "/get4pxTransportationTime.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String get4pxTransportationTime(HttpServletRequest request, Model model) {
		String freight = "0";
		Map<String, Double> maphl = Currency.getMaphl(request); // 汇率
		String currency = request.getParameter("currency"); // 货币单位
		String fpxCountryCode = request.getParameter("fpxCountryCode"); // 国家
		String weight = request.getParameter("weight"); // 重量
		String volumelwh = request.getParameter("volumelwh"); // 体积
		// //运输方式
		List<OrderInfoPojo> ofplist = iWarehouseService.getFpxProductCode();
		// 查询运输方式对应的运输时间ofplist.size()
		ChargeCalculateSample sample = new ChargeCalculateSample();
		for (int i = 0; i < ofplist.size(); i++) {
			freight = checkDBStatus(fpxCountryCode, weight, volumelwh, "P",ofplist.get(i).getProductcode(), currency, maphl);
			if ("500".equals(freight) || "600".equals(freight)) {
				return freight;
			}
			if (!"count_failure".equals(freight)) {
				ofplist.get(i).setChinesename(ofplist.get(i).getChinesename()+ "-------"+ freight.substring(freight.indexOf("#") + 1,freight.length()) + "(天)");
			} else {
				ofplist.remove(i); // 查询不出来运费的 我给他删掉
			}
		}
		return net.sf.json.JSONArray.fromObject(ofplist).toString();
	}

	// 计算运费
	@RequestMapping(value = "/getFreight.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getFreight(HttpServletRequest request, Model model) {
		String freight = "0";
		Map<String, Double> maphl = Currency.getMaphl(request); // 汇率

		String currency = request.getParameter("currency"); // 货币单位
		String fpxCountryCode = request.getParameter("fpxCountryCode"); // 国家
		String weight = request.getParameter("weight"); // 重量
		String volumelwh = request.getParameter("volumelwh"); // 体积
		String type = request.getParameter("type"); // 运输类型 4px 原飞航 佳成
		String fpxProductCode = request.getParameter("fpxProductCode"); // 运输方式
		String trans = request.getParameter("trans"); // 区域
		if ("1".equals(type)) { // 4PX运输
			ChargeCalculateSample sample = new ChargeCalculateSample();
			freight = sample.chargeCalculateService(fpxCountryCode, weight,volumelwh, "P", fpxProductCode, currency, maphl);
			freight=freight.indexOf("#") != -1?freight.substring(0, freight.indexOf("#")):"500";
		} else if ("2".equals(type)) { // 原飞航运输
			feeCount fc = new feeCount();
			freight = fc.getYFHFee(trans, weight, currency, maphl);
			freight=freight.indexOf("#") != -1?freight.substring(0, freight.indexOf("#")):"500";
		}
		return freight;
	}

	// 查询单个出库订单的详细信息
	@RequestMapping(value = "/getOutOrderInfo.do", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOutOrderInfo(HttpServletRequest request, Model model) {
		String order_no = request.getParameter("order_no");
		if (order_no == null || "".equals(order_no)) {
			return "0";
		}
		Map<String, Object> map = new HashMap<String, Object>(); //
		map.put("order_no", order_no);
		OrderInfoPojo oiflist = iWarehouseService.getOutOrderInfo(map);
		return net.sf.json.JSONArray.fromObject(oiflist).toString();
	}

	@RequestMapping(value ="/deleteVideoPath",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> deleteVideoPath(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		try{
			String goods_pid=request.getParameter("goods_pid");
			if(StringUtil.isBlank(goods_pid)){
				map.put("msg","0");
				return map;
			}else{
				map.put("goods_pid",goods_pid);
				map.put("path","");
				int row= iWarehouseService.updateCustomVideoUrl(map);
				GoodsInfoUpdateOnlineUtil.videoUrlToOnlineByMongoDB(goods_pid,"");
				map.put("msg","1");
			}
		}catch (Exception e){
			map.put("msg","0");
		}
		return map;
	}


	/**
	 * 产品单页视频图片编辑上传
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/vimeoUpload",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> vimeoUpload(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			SendMQ sendMQ = new SendMQ();
			String goods_pid=request.getParameter("pid");
			if(StringUtil.isBlank(goods_pid)){
				map.put("msg","0");
			}
			String filePath="";
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> fileList = multipartRequest.getFiles("file");
			if(fileList == null || fileList.size() == 0){
				//			return addResultMapMsg(false,"请上传文件,注意文件的name属性为file");
				System.out.println("请上传文件,注意文件的name属性为file");
			}
			MultipartFile file = fileList.get(0);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
			String time = df.format(new Date());// new Date()为获取当前系统时间
			String filename=goods_pid+"_"+time;
			if (ftpConfig == null) {
				ftpConfig = GetConfigureInfo.getFtpConfig();
			}
			// 检查配置文件信息是否正常读取
			String imgUploadPath = ftpConfig.getLocalDiskPath();
			filePath=imgUploadPath+"_"+filename+"_"+file.getOriginalFilename();
			FileOutputStream fs=new FileOutputStream(filePath);
			int i=1/0;
			byte[] buffer =new byte[1024*1024];
			int bytesum = 0;
			int byteread = 0;
			InputStream stream=file.getInputStream();
			while ((byteread=stream.read(buffer))!=-1){
				bytesum+=byteread;
				fs.write(buffer,0,byteread);
				fs.flush();
			}
			fs.close();
			stream.close();
			File video=new File(filePath);
			if (video.exists()) {
				boolean flag=NewFtpUtil.uploadFileToRemote(Util.PIC_IP, 21, Util.PIC_USER, Util.PIC_PASS, "/insp_video/", filename+"_"+file.getOriginalFilename(), filePath);
				if(flag){
					map.put("msg","1");
					map.put("goods_pid",goods_pid);
					String path="https://img.import-express.com/importcsvimg/insp_video/"+(filename+"_"+file.getOriginalFilename())+"";
					map.put("path",path);
					GoodsInfoUpdateOnlineUtil.videoUrlToOnlineByMongoDB(goods_pid,path);
					iWarehouseService.updateCustomVideoUrl(map);
				}
			}else{
				map.put("msg","0");
			}
			sendMQ.closeConn();
		} catch (Exception e) {
			map.put("msg","0");
		}
		return map;
	}

	/**
	 * 查询商品产品单页是否有视频数据
	 * @param request
	 * @return 产品视频信息
	 */
	@RequestMapping("/queryVideo")
	@ResponseBody
	public Map<String, String> queryVideo(HttpServletRequest request) {
		String goods_pid = request.getParameter("goods_pid");
		try{
			Map<String, String> map = new HashMap<String, String>(); //
			map.put("goods_pid", goods_pid);
			OrderDetailsBean od = iWarehouseService.queryVideo(map);
			if(od!=null && StringUtil.isNotBlank(od.getDzOrderno())){
				map.put("car_url",od.getCar_url());
				map.put("goods_url",od.getGoods_pid());
				map.put("dzOrderno",od.getDzOrderno());
			}
//			resMap = iWarehouseService.getDetailsByRemarks(remarks);
			return map;
		}catch (Exception e){
			e.printStackTrace();
		}
		return new HashMap<String,String>();
	}

	@RequestMapping(value = "/queryPictureInfos")
	@ResponseBody
	public EasyUiJsonResult queryPictureInfos(HttpServletRequest request, Model model) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String,String> map=new HashMap<String, String>();
		try{
			saveValueToMap(request, map);
			List<SearchResultInfo> list = iWarehouseService.queryPictureInfos(map);
			int count=iWarehouseService.queryPictureInfosCount(map);
			json.setRows(list);
			json.setTotal(count);
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}
	/**
	 *
	 * @Title getAllBuyer
	 * @Description 获取所有采购人
	 * @param request 客户端请求
	 * @param response 返回客户端参数
	 * @return
	 * @throws ServletException servlet异常
	 * @throws IOException 输入输出流异常
	 * @throws ParseException
	 * @return com.alibaba.fastjson.JSONArray 返回结果类型
	 */
	@RequestMapping(value = "/getAllBuyerInsp", method = RequestMethod.GET)
	@ResponseBody
	protected com.alibaba.fastjson.JSONArray getAllBuyerInsp(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		List<com.cbt.pojo.AdmuserPojo> list=iWarehouseService.getAllBuyer(1);
		System.out.println("采购人长度："+list.size());

		List<com.cbt.pojo.AdmuserPojo> result = new ArrayList<com.cbt.pojo.AdmuserPojo>();
		com.cbt.pojo.AdmuserPojo admuser=new com.cbt.pojo.AdmuserPojo();
		admuser.setId(1);
		admuser.setAdmName("全部");
		result.add(admuser);

		if(1==1){
			com.cbt.pojo.AdmuserPojo a=new com.cbt.pojo.AdmuserPojo();
			a.setId(1);
			a.setAdmName("Ling");
			result.add(a);
		}
		result.addAll(list);
		com.alibaba.fastjson.JSONArray jsonArr = JSON.parseArray(JSON.toJSONString(result));
		return jsonArr;
	}





	private void saveValueToMap(HttpServletRequest request, Map<String, String> map) {
		String page=request.getParameter("page");
		String goods_pid=request.getParameter("pid");
		String orderno=request.getParameter("orderno");
		String odid=request.getParameter("odid");
		String goods_id=request.getParameter("goods_id");
		String times=request.getParameter("times");
		String admuserid=request.getParameter("admuserid");
		String oldOrderid=request.getParameter("oldOrderid");
		if(StringUtil.isNotBlank(times) && "999".equals(times)){
			times=null;
		}else if(StringUtil.isBlank(times)){
			times="1";
		}
		goods_pid= StringUtils.isStrNull(goods_pid)?null:goods_pid;
		orderno=StringUtils.isStrNull(orderno)?null:orderno;
		goods_id=StringUtils.isStrNull(goods_id)?null:goods_id;
		times=StringUtils.isStrNull(times)?null:times;
		page=StringUtils.isStrNull(page)?"1":page;
		int pages=(Integer.valueOf(page)-1)*10;
		map.put("page",String.valueOf(pages));
		map.put("goods_pid", goods_pid);
		map.put("times",StringUtil.isNotBlank(odid)?null:times);
		map.put("orderno",orderno);
		map.put("goods_id",goods_id);
		map.put("admuserid",admuserid);
		map.put("odid",odid);
		map.put("oldOrderid",oldOrderid);
	}

	// 出库验货
	@RequestMapping(value = "/getOrderInfoInspection.do", method = RequestMethod.GET)
	public String getOrderInfoInspection(HttpServletRequest request, Model model) {
		// 取查询条件参数
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
		String orderid = (String) request.getParameter("orderid");
		String userid = (String) request.getParameter("userid");
		String dateday = (String) request.getParameter("day");
		String orderstruts = (String) request.getParameter("orderstruts");
		String orderPosition = (String) request.getParameter("orderPosition");
		String t = request.getParameter("pageNum");
		int pageNum=!StringUtils.isStrNull(t)?Integer.parseInt(t):1;
		// 页大小
		t = request.getParameter("pageSize");
		int pageSize=!StringUtils.isStrNull(t)?Integer.parseInt(t):50;
		// 未审核 所有 已审核 有问题
		orderstruts="".equals(orderstruts) || orderstruts == null?"1":orderstruts;
		String sqldate = "";
		// 时间
		sqldate=StringUtils.isStrNull(dateday)?"":"  and to_days(now()) - to_days(of.create_time) < "+ dateday + "  ";
		int startNum = pageNum * pageSize - pageSize; // 开始位置
		String sql = "";
		// 0 已审核通过；1 可出库了；2 已出库 3 审核有问题
		if ("0".equals(orderstruts)) { // 所有
			sql = "";
		} else if ("1".equals(orderstruts)) { // 未审核
			sql = " and (of.orderno is null)";
		} else if ("2".equals(orderstruts)) { // 审核通过
			sql = " and of.state='4' ";
		} else if ("3".equals(orderstruts)) { // 有问题
			sql = " and of.state='3' ";
		}
		// 1 公司订单 ; 2 仓库订单 0 所有订单(不区分位置)
		int orderPos=StringUtils.isStrNull(orderPosition)?0:Integer.parseInt(orderPosition);
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("order_no", orderid);
		map.put("user_id", userid);
		map.put("sqldate", sqldate);
		map.put("sql", sql); // 0 所有 1 未审核 2已通过 3 有问题
		map.put("orderPos", orderPos);
		// //总记录数
		map.put("startNum", startNum);
		map.put("endNum", pageSize);
		map.put("admName", adm!=null?"15":adm.getId());
		String ip = "http://" +request.getLocalAddr();
		List<StorageLocationBean> list = iWarehouseService.getOrderInfoInspection(map,ip);
		int count = list.size();
		OrderInfoPage oip = new OrderInfoPage(); // 页
		// 如果不是第一次 查询 就已经保存了 OrderInfoPage
		oip.setPagelist(list); // 一页数据
		oip.setPageNum(pageNum); // 当前页
		oip.setPageSize(pageSize); // 也大小
		oip.setPageSum(count); // 总记录数
		oip.setOrderid(orderid); // 查询条件
		oip.setUserid(userid);
		oip.setOrderstruts(orderstruts);//
		oip.setDay(dateday); // 几天之内
		request.setAttribute("oip", oip);
		request.setAttribute("orderPos", orderPos);
		return "orderinfoInspection";
	}

	// 出库验货 查询全部订单  2018/07/20 10:45  ly
	@RequestMapping(value = "/getOrderInfoInspectionall.do", method = RequestMethod.GET)
	public String getOrderInfoInspectionall(HttpServletRequest request, Model model) {
		// 取查询条件参数
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
		String orderid = (String) request.getParameter("orderid");
		String userid = (String) request.getParameter("userid");
		String dateday = (String) request.getParameter("day");
		String orderstruts = (String) request.getParameter("orderstruts");
		String orderPosition = (String) request.getParameter("orderPosition");
		String t = request.getParameter("pageNum");
		int pageNum=!StringUtils.isStrNull(t)?Integer.parseInt(t):1;
		// 页大小
		t = request.getParameter("pageSize");
		int pageSize=!StringUtils.isStrNull(t)?Integer.parseInt(t):50;
		// 未审核 所有 已审核 有问题
		orderstruts="".equals(orderstruts) || orderstruts == null?"1":orderstruts;
		String sqldate = "";
		// 时间
		sqldate=StringUtils.isStrNull(dateday)?"":"  and to_days(now()) - to_days(of.create_time) < "+ dateday + "  ";
		int startNum = pageNum * pageSize - pageSize; // 开始位置
		String sql = "";
		// 0 已审核通过；1 可出库了；2 已出库 3 审核有问题
		if ("0".equals(orderstruts)) { // 所有
			sql = "";
		} else if ("1".equals(orderstruts)) { // 未审核
			sql = " and (of.orderno is null)";
		} else if ("2".equals(orderstruts)) { // 审核通过
			sql = " and of.state='4' ";
		} else if ("3".equals(orderstruts)) { // 有问题
			sql = " and of.state='3' ";
		}
		// 1 公司订单 ; 2 仓库订单 0 所有订单(不区分位置)
		int orderPos=StringUtils.isStrNull(orderPosition)?0:Integer.parseInt(orderPosition);
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("order_no", orderid);
		map.put("user_id", userid);
		map.put("sqldate", sqldate);
		map.put("sql", sql); // 0 所有 1 未审核 2已通过 3 有问题
		map.put("orderPos", orderPos);
		// //总记录数
		map.put("startNum", startNum);
		map.put("endNum", pageSize);
		map.put("admName", adm!=null?"15":adm.getId());
		String ip = "http://" +request.getLocalAddr();
		List<StorageLocationBean> list = iWarehouseService.getOrderInfoInspectionall(map,ip); // 结果集
		int count = list.size();
		OrderInfoPage oip = new OrderInfoPage(); // 页
		// 如果不是第一次 查询 就已经保存了 OrderInfoPage
		oip.setPagelist(list); // 一页数据
		oip.setPageNum(pageNum); // 当前页
		oip.setPageSize(pageSize); // 也大小
		oip.setPageSum(count); // 总记录数
		oip.setOrderid(orderid); // 查询条件
		oip.setUserid(userid);
		oip.setOrderstruts(orderstruts);//
		oip.setDay(dateday); // 几天之内
		request.setAttribute("oip", oip);
		request.setAttribute("orderPos", orderPos);
		return "orderinfoInspectionall";
	}

	// 删除审核和有问题
	// 审核
	@RequestMapping(value = "/delteFromOrderFeeByOrderid.do", method = RequestMethod.GET)
	@ResponseBody
	// jqery ajax 访问时加上 这样就不会返回页面名字，而是实际数据
	public String delteFromOrderFeeByOrderid(HttpServletRequest request,
											 Model model) {
		String orderid = (String) request.getParameter("orderid");
		if (orderid == null && "".equals(orderid)) {
			return "0";
		}
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("orderno", orderid);
		int ret = 0;
		if (iWarehouseService.selectOrderFeeByOrderid(map) > 0) {
			ret = iWarehouseService.delteFromOrderFeeByOrderid(map); // 先删除
		}
		// 重新审核 删除已打印
		if (ret > 0) {
			// 先删除
			Map<String, String> m2 = new HashMap<String, String>();
			m2.put("orderid", orderid);
			if (iWarehouseService.selectShippingPackage(m2) > 0) {
				iWarehouseService.deleteShippingPackage(m2);
			}
			try {
				DataSourceSelector.set("dataSource127hop");
				if (iWarehouseService.selectShippingPackage(m2) > 0) {
					// 判断是否开启线下同步线上配置
					if (GetConfigureInfo.openSync()) {
						String orderId, businessType, tableName, sqlStr = null;
						orderId = m2.get("orderid"); // 订单号
						businessType = "出库--删掉打印的标签纸"; // 业务类型
						tableName = "shipping_package"; // 执行表名称
						sqlStr = " delete  from  shipping_package where orderid ="+ orderId + " "; // 执行SQL体
						SaveSyncTable.InsertOnlineDataInfo(0, orderId,businessType, tableName, sqlStr);
					} else {
						iWarehouseService.deleteShippingPackage(m2);
					}
				}
				DataSourceSelector.restore();
			} catch (Exception e) {
				LOG.error("链接线上数据库", e);
			}
		}
		return ret + "";
	}

	// 取消合并
	@RequestMapping(value = "/updateFromOrderFeeByOrderid.do", method = RequestMethod.GET)
	@ResponseBody
	// jqery ajax 访问时加上 这样就不会返回页面名字，而是实际数据
	public String updateFromOrderFeeByOrderid(HttpServletRequest request,
											  Model model) {
		String mergeOrders = (String) request.getParameter("mergeOrders");
		if (mergeOrders == null && "".equals(mergeOrders)) {
			return "0";
		}
		Map<String, Object> map = new HashMap<String, Object>(); //
		map.put("mergeOrders", mergeOrders);
		int ret = iWarehouseService.updateFromOrderFeeByOrderid(map);
		// 删除拆分
		final Map<String, String> m2 = new HashMap<String, String>();
		m2.put("orderid", mergeOrders);
		if (iWarehouseService.selectShippingPackage(m2) > 0) {
			iWarehouseService.deleteShippingPackage(m2);
		}
		// 开启线程
		new Thread() {
			public void run() {
				DataSourceSelector.set("dataSource127hop");
				if (iWarehouseService.selectShippingPackage(m2) > 0) {
					iWarehouseService.deleteShippingPackage(m2);
				}
				DataSourceSelector.restore();
			}
		}.start();
		return ret + "";
	}

	@RequestMapping(value = "/getWLHtml.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	//
	public String getWLHtml(HttpServletRequest request, Model model) {
		String nums = request.getParameter("nums");
		String url = "http://www.17track.net/en/externalcall?nums=" + nums;
		String html = "";
		try {
			//WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);
            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			HtmlPage rootPage = webClient.getPage(url);
			webClient.waitForBackgroundJavaScript(1000 * 3);
			webClient.setJavaScriptTimeout(0);
			html = rootPage.asText();
			String src = html;
			if (html.indexOf(nums) != -1) {
				int htmlIndexStart = src.indexOf("Destination Country");
				int htmlIndexEnd = src.indexOf("Origin Country");
				if (htmlIndexStart != -1 && htmlIndexEnd != -1) {
					src = src.substring(htmlIndexStart, htmlIndexEnd);
					String infoArray[] = src.split("\r\n");
					System.out
							.println("第一个时间开始：——————————————————————————————————————————————————————");
					for (int i = 1; i < infoArray.length; i = i + 2) {
						String time = infoArray[i];
						String context = infoArray[i + 1];
						System.out.println("时间" + time + "--地点--" + context);
						TrackBean trackBean = new TrackBean();
						trackBean.setTime(time);
						trackBean.setContext(context);
					}
				} else {
					src = "";
				}
				src = html;
				htmlIndexStart = src.indexOf("Origin Country");
				htmlIndexEnd = src.indexOf("Copy ResultCopy Link");
				if (htmlIndexStart != -1 && htmlIndexEnd != -1) {
					src = src.substring(htmlIndexStart, htmlIndexEnd);
					String infoArray[] = src.split("\r\n");
					System.out
							.println("第二个时间开始：——————————————————————————————————————————————————————");
					for (int i = 1; i < infoArray.length; i = i + 2) {
						String time = infoArray[i];
						String context = infoArray[i + 1];
						System.out.println("时间" + time + "--地点--" + context);
						TrackBean trackBean = new TrackBean();
						trackBean.setTime(time);
						trackBean.setContext(context);
					}
				} else {
					src = "";
				}
			} else {
				src = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(src);
		return html;
	}

	@RequestMapping(value = "/updateExperssNo.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateExperssNo(HttpServletRequest request, Model model) {

		String order_no = (String) request.getParameter("order_no");
		String express_no = (String) request.getParameter("express_no");
		String logistics_name = (String) request.getParameter("logistics_name");

		if (order_no == null && "".equals(order_no)) {
			return "0";
		}

		Map<String, Object> map = new HashMap<String, Object>(); //
		map.put("order_no", order_no);
		map.put("express_no", express_no);
		map.put("logistics_name", logistics_name);

		int ret = iWarehouseService.updateExperssNo(map);

		return ret + "";
	}

	// 修改快递单号
	@RequestMapping(value = "/updateExperssNopPlck.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	// jqery ajax 访问时加上 这样就不会返回页面名字，而是实际数据
	public String updateExperssNopPlck(HttpServletRequest request, Model model) {

		String shipmentno = (String) request.getParameter("shipmentno");
		String express_no = (String) request.getParameter("express_no");
		String logistics_name = (String) request.getParameter("logistics_name");
		String sweight = request.getParameter("sweight");
		String svolume = request.getParameter("svolume");
		String freight = request.getParameter("freight");
		String estimatefreight = request.getParameter("estimatefreight");
		if (org.apache.commons.lang3.StringUtils.isBlank(shipmentno)
                || org.apache.commons.lang3.StringUtils.isBlank(express_no)) {
			return "0";
		}
		if (estimatefreight.length() == 0) {
			estimatefreight = "0";
		}
		String[] volumes = svolume.split("\\*");
		double volumeweight = 0;
		if (volumes.length > 2) {
			double length = Double.parseDouble(volumes[0]);
			double weight = Double.parseDouble(volumes[1]);
			double height = Double.parseDouble(volumes[2]);
			volumeweight = length * weight * height / 5000;
		}

		Map<String, Object> map = new HashMap<String, Object>(); //
		map.put("shipmentno", shipmentno);
		map.put("express_no", express_no);
		map.put("logistics_name", logistics_name);
		map.put("sweight", sweight);
		map.put("svolume", svolume);
		map.put("volumeweight", volumeweight);
		map.put("freight", freight);
		map.put("estimatefreight", estimatefreight);

		iWarehouseService.insertChangeLog(map); // 2018/08/22 20:34 保存运单变更记录
		int ret = iWarehouseService.updateExperssNoPlck(map);
		// 异步更新线上运单信息
		updateExperssNoPlckThread thread = new updateExperssNoPlckThread(map);
		thread.start();
		return ret + "";
	}

	// 审核
	@RequestMapping(value = "/insertOrderfee.do", method = RequestMethod.GET)
	@ResponseBody
	// jqery ajax 访问时加上 这样就不会返回页面名字，而是实际数据
	public String insertOrderfeeFromOrderInfo(HttpServletRequest request,
											  Model model) {

		String orderid = (String) request.getParameter("orderid");
		String userid = (String) request.getParameter("userid");
		userid = userid.replace("D", "");
		String state = (String) request.getParameter("state");
		String length = (String) request.getParameter("length");
		String dropship = request.getParameter("isDropshipOrderFlag");
		int dropshipFlag = dropship.equals("0") ? 0 : Integer
				.parseInt(dropship);
		String problem = "";

		String t = (String) request.getParameter("problem");
		if (t != null && !"".equals(t)) {
			problem = t;
		}
		// lock.lock();// 取得锁
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("orderno", orderid);
		// 先查，若有，则删除
		int res = iWarehouseService.selectOrderFeeByOrderid(map);
		if (res > 0) {
			iWarehouseService.delteFromOrderFeeByOrderid(map); // 先删除
		}
		String megerOrder=iWarehouseService.getMegerOrder(orderid);
		orderid=StringUtil.isNotBlank(megerOrder)?megerOrder:orderid;
		map.put("userid", userid);
		map.put("state", state);
		map.put("problem", problem); // 审核不通过的 问题描述
		map.put("mergeOrders", orderid); // 这里是合并订单用的
		map.put("mergestate", StringUtil.isNotBlank(megerOrder)?"1":"0");
//		map.put("mergestate", "0");
		int ret = iWarehouseService.insertOrderfeeFromOrderInfo(map,
				dropshipFlag);
		// lock.unlock();// 释放锁

		// 拆分包裹
		// if(ret>0 && "4".equals(state)){
		//
		// //先删除
		// Map<String,String> m2 = new HashMap<String,String>();
		// m2.put("orderid", orderid);
		// int cnt = iWarehouseService.selectShippingPackage(m2);
		// if( cnt <1){ //是否已经有了数据
		// iWarehouseService.deleteShippingPackage(m2);
		// //获取出货号
		// String shipmentno = "";
		// shipmentno = iWarehouseService.getShipmentno();
		// //批量插入
		// List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		// String suborderid = orderid;
		// Map<String,String> m = new HashMap<String,String>();
		// m.put("shipmentno", shipmentno);
		// m.put("orderid", orderid);
		// m.put("suborderid", suborderid);
		// list.add(m);
		// iWarehouseService.batchInsertSP(list);
		// }
		//
		// }
		// 将问题订单备注添加到订单详情
		/*
		 * if( problem !=null && !"".equals(problem) ){ if(ret>0){ problem
		 * ="订单审核-问题订单: "+problem; String sessionId =
		 * request.getSession().getId(); String admuserJson =
		 * Redis.hget(sessionId, "admuser"); Admuser admuser = (Admuser)
		 * SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		 * iOrderinfoService
		 * .addOrderRemark(orderid,problem,admuser.getAdmname(),0);
		 *
		 * InsertMessageNotification insertMsG = new
		 * InsertMessageNotification(); //同时通知对应销售 int type = 9 ; // 消息类型(订单备注)
		 * try {
		 * insertMsG.insertProblemOrder(orderid,problem,admuser.getId(),type); }
		 * catch (Exception e) {
		 * e.printStackTrace(); } } }
		 */
		return String.valueOf(ret);
	}

	/**
	 * 批量审核
	 *
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/insertOrderfees.do", method = { RequestMethod.POST })
	@ResponseBody
	// jqery ajax 访问时加上 这样就不会返回页面名字，而是实际数据
	public String insertOrderfeesFromOrderInfo(HttpServletRequest request,
                                               Model model, @RequestBody List<String> infos) {
		int count = 0;
		try {
			// 获取js 提交的信息 orderid,isDropshipOrderFlag,userid
			for (String str : infos) {
				String[] orders = str.split(",");
				String orderid = orders[0];
				String userid = orders[2];
				String dropship = orders[1];
				userid = userid.replace("D", "");
				String state = "4"; // 批量审核通过 默认为 4
				int dropshipFlag = dropship.equals("0") ? 0 : Integer
						.parseInt(dropship);
				String problem = "";

				Map<String, Object> map = new HashMap<String, Object>(); // sql
				// 参数
				//出库审核通过时判断该订单是否有选择合并出运
				map.put("orderno", orderid);
				// 先查，若有，则删除
				int res = iWarehouseService.selectOrderFeeByOrderid(map);
				if (res > 0) {
					iWarehouseService.delteFromOrderFeeByOrderid(map); // 先删除
				}
				String megerOrder=iWarehouseService.getMegerOrder(orderid);
				orderid=StringUtil.isNotBlank(megerOrder)?megerOrder:orderid;
				map.put("userid", userid);
				map.put("state", state);
				map.put("problem", problem); // 审核不通过的 问题描述
				map.put("mergeOrders", orderid); // 这里是合并订单用的
//				map.put("mergestate","0");
				map.put("mergestate",StringUtil.isNotBlank(megerOrder)?"1":"0");
//				map.put("mergestate", "0");
				int ret = iWarehouseService.insertOrderfeeFromOrderInfo(map,
						dropshipFlag);
				count += ret;
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("批量审核 异常【订单相关信息:" + infos + "】", e);
		}
		return String.valueOf(count);
	}

	// 拆分出货(打印标签)
	@RequestMapping(value = "/batchInsertSP", method = RequestMethod.POST)
	@ResponseBody
	public String batchInsertSP(HttpServletRequest request, Model model,
                                @RequestBody Map<String, Object> mainMap) throws IOException {
		Map<String,Object> reqMap = new HashMap<String, Object>();
		// lock.lock();// 取得锁 这里有时候会导致永远锁住
		String orderid = (String) mainMap.get("orderid");
		String length = (String) mainMap.get("length");
		String userid = (String) mainMap.get("userid");
		userid = userid.replace("D", "");
		List<String> listOrderid = (List<String>) mainMap.get("list");

		// 如果主订单不是dropship订单,则这些合并订单也不是
		String isDropshipFlag = "1";
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("orders", "'" + orderid + "'");
		List<String> orderNoList = iWarehouseService.getOrderNoList(map);
		if (orderNoList.size() > 0) {
			isDropshipFlag = null;
		}
		// 获取出货号
		String shipmentno = "";
		shipmentno = iWarehouseService.getShipmentno();
		// 先删除
		Map<String, String> m2 = new HashMap<String, String>();
		m2.put("orderid", orderid);
		if (iWarehouseService.selectShippingPackage(m2) > 0) {
			iWarehouseService.deleteShippingPackage(m2);
		}
		// DataSourceSelector.set("dataSource127hop");
		// iWarehouseService.deleteShippingPackage(m2);
		// DataSourceSelector.restore();
		// 批量插入
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String remarks = orderid;

		// 拼接合并订单
		String hBOrders = "";
		String query = "";
		for (int i = 0; i < listOrderid.size(); i++) {
			hBOrders += listOrderid.get(i) + ",";
			query = query + "'" + listOrderid.get(i) + "',";
		}
		if (listOrderid.size() >= 1) {
			remarks = hBOrders;
			query = query.substring(0, query.length() - 1);
		}

		int shipmentnoLen = 1;
		for (int i = 0; i < 1; i++) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("shipmentno", (Integer.parseInt(shipmentno) + i) + "");
			m.put("orderid", orderid);
			m.put("remarks", remarks);
			if (listOrderid.size() < 2) { // 如果不是是合并订单拆分
				remarks = orderid + ((char) ('A' + 1 + i));
			}
			list.add(m);
			shipmentnoLen++;// 记录出货号的长度
		}
		int ret = iWarehouseService.batchInsertSP(list);
		// 同步更新线上数据
		// DataSourceSelector.set("dataSource127hop");
		// //删除原来orderid对应的数据
		// iWarehouseService.deleteShippingPackage(m2);
		// //插入新数据
		// iWarehouseService.batchInsertSP(list);
		// DataSourceSelector.restore();
		// 异步线程
		warehouseThread thread = new warehouseThread(m2, list);
		thread.start();

		List<BufferedImage> retListImg = new ArrayList<BufferedImage>();
		// 开始打印不干纸
		if (ret > 0) {
			Map<String, String> mt = new HashMap<String, String>();
			mt.put("orderids", query);
			mt.put("isDropshipFlag", isDropshipFlag);

			// 获得数量最多的图片
			List<Map<String, String>> listMapImg = iWarehouseService
					.getMaxImg(mt);
			String url1 = "http://g01.a.alicdn.com/kf/HTB13BKxIFXXXXaKXXXXq6xXFXXXe/mens-t-shirts-fashion-2015-new-summer-style-men-clothes-Reggae-bob-male-color-block-decoration.jpg";
			String url2 = "https://img.alicdn.com/imgextra/i4/693060164/TB2_hNtlVXXXXaZXXXXXXXXXXXX_!!693060164.jpg_430x430q90.jpg";
			String url3 = "https://img.alicdn.com/imgextra/i3/693060164/TB2JwgyiVXXXXarXpXXXXXXXXXX_!!693060164.jpg_430x430q90.jpg";

			if (listMapImg.size() == 1) {
				url1 = ((Map<String, String>) listMapImg.get(0)).get("car_img");
				url2 = ((Map<String, String>) listMapImg.get(0)).get("car_img");
				url3 = ((Map<String, String>) listMapImg.get(0)).get("car_img");
			} else if (listMapImg.size() == 2) {
				url1 = ((Map<String, String>) listMapImg.get(0)).get("car_img");
				url2 = ((Map<String, String>) listMapImg.get(1)).get("car_img");
				url3 = ((Map<String, String>) listMapImg.get(0)).get("car_img");
			} else if (listMapImg.size() == 3) {
				url1 = ((Map<String, String>) listMapImg.get(0)).get("car_img");
				url2 = ((Map<String, String>) listMapImg.get(1)).get("car_img");
				url3 = ((Map<String, String>) listMapImg.get(2)).get("car_img");
			}
			url1="";
			url2="";
			url3="";
			String mode_transport[] = ((Map<String, String>) listMapImg.get(0))
					.get("mode_transport").split("@");
			// 出货号
			int shipmentno2 = Integer.parseInt(shipmentno);

			// 获得种类和全部数量
			List<Map<String, String>> listCntSum = iWarehouseService
					.getCntSum(mt);
			String number = ((Map) listCntSum.get(0)).get("num").toString();
			String typeNum = ((Map) listCntSum.get(0)).get("cnt").toString();
			String gjCode = "USA";
			// 表示有国家
			if (mode_transport.length > 2) {
				gjCode = mode_transport[2];
			}
			List<BufferedImage> listimg = new ArrayList<BufferedImage>();
			int i = 1;
			int flag = 0;

			// 获得图片
			BufferedImage img1=null;
			BufferedImage img2 = null;
			BufferedImage img3 = null;
			try {
				if(!StringUtils.isStrNull(url1)){
					img1 = UtilAll.zoomOutImage(UtilAll.getImageBytes2NetByUrl(url1));
					if (url1 != null && !"".equals(url1) && url2 != null && !"".equals(url2)) {
						if (url1.equals(url2)) {
							img2 = img1;
						} else {
							img2 = UtilAll.zoomOutImage(UtilAll
									.getImageBytes2NetByUrl(url2));
						}
					} else {
						img2 = UtilAll.zoomOutImage(UtilAll.getImageBytes2NetByUrl(url2));
					}

					if (url2 != null && !"".equals(url2) && url3 != null && !"".equals(url3)) {
						if (url2.equals(url3)) {
							img3 = img2;
						} else {
							img3 = UtilAll.zoomOutImage(UtilAll
									.getImageBytes2NetByUrl(url3));
						}
					} else {
						img3 = UtilAll.zoomOutImage(UtilAll
								.getImageBytes2NetByUrl(url3));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
//				img1 = UtilAll.zoomOutImage(UtilAll.getImageBytes2NetByUrl("http://b.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=60be384cde54564ee530ec3d86eeb0b4/d439b6003af33a87c0ba0445c55c10385243b59d.jpg"));
//				img2 = img1;// ImageIO.read(new
//				img3 = img1;// ImageIO.read(new
			}
			reqMap.put("express_id", (shipmentno2++) + "");
			reqMap.put("gjCode", gjCode);
			for (i=1; i < shipmentnoLen; i++) {

				BufferedImage bi = UtilAll.printDryPaper(listOrderid,
						(shipmentno2++) + "", img1, img2, img3, userid, number,
						typeNum, gjCode, "D:\\images\\" + i + "a.png");
				listimg.add(bi);
				if (i % 4 == 0) {
					BufferedImage retImg = UtilAll.print(listimg,
							"D:\\images\\AAA" + shipmentno + i + "---" + i
									+ ".png");
					if (retImg != null) {
						retListImg.add(retImg);
					}
					listimg = new ArrayList<BufferedImage>();
					flag = 1;
				}
			}
			if ((flag == 0) || (i - 1) % 4 != 0) {
				BufferedImage retImg = UtilAll.print(listimg, "D:\\images\\BBB"
						+ shipmentno + ".png");
				if (retImg != null) {
					retListImg.add(retImg);
				}
			}

		}
		// lock.unlock();// 释放锁

		List<String> retLsit = new ArrayList<String>();
		for (int i = 0; i < retListImg.size(); i++) {

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(retListImg.get(i), "png", outputStream);
			BASE64Encoder encoder = new BASE64Encoder();
			retLsit.add(encoder.encode(outputStream.toByteArray()));
		}
//		return JSONArray.fromObject(retLsit).toString();
		reqMap.put("orderid", hBOrders.replace(",", ";"));
		reqMap.put("sp_id", shipmentno);
		reqMap.put("userid", userid);
		JSONArray.fromObject(reqMap).toString();
		System.out.println(JSONArray.fromObject(reqMap).toString());
		return JSONArray.fromObject(reqMap).toString();
	}

	// 取消拆分
	@RequestMapping(value = "/deleteShippingPackage", method = RequestMethod.GET)
	@ResponseBody
	// jqery ajax 访问时加上 这样就不会返回页面名字，而是实际数据
	public String deleteShippingPackage(HttpServletRequest request, Model model) {

		String orderid = (String) request.getParameter("orderid");

		final Map<String, String> m = new HashMap<String, String>();
		m.put("orderid", orderid);
		int ret = 0;
		if (iWarehouseService.selectShippingPackage(m) > 0) {
			ret = iWarehouseService.deleteShippingPackage(m);
		}
		// 开启线程

		new Thread() {
			@Override
			public void run() {
				try {
					/*DataSourceSelector.set("dataSource127hop");
					// 删除原来orderid对应的数据
					if (iWarehouseService.selectShippingPackage(m) > 0) {
						// 判断是否开启线下同步线上配置
						if (GetConfigureInfo.openSync()) {
							String orderId, businessType, tableName, sqlStr = null;
							orderId = m.get("orderid"); // 订单号
							businessType = "出库--删除打印的标签纸"; // 业务类型
							tableName = "shipping_package"; // 执行表名称
							sqlStr = " delete  from  shipping_package where orderid = '"
									+ orderId + "' "; // 执行SQL体
							SaveSyncTable.InsertOnlineDataInfo(0, orderId,
									businessType, tableName, sqlStr);
						} else {
							int ret = iWarehouseService
									.selectShippingPackage(m);
						}
					}
					DataSourceSelector.restore();*/
					NotifyToCustomerUtil.sendSqlByMq("delete from shipping_package where orderid ='"+orderid+"'");
				} catch (Exception e) {
					LOG.error("删除线上打印标签纸记录异常【订单号:" + m.get("orderid") + "】", e);
				}
			}
		}.start();

		return ret + "";
	}

	// 订单合并
	@RequestMapping(value = "/selectOrderidAddress.do", method = RequestMethod.POST)
	@ResponseBody
	// jqery ajax 访问时加上 这样就不会返回页面名字，而是实际数据
	public String selectOrderidAddress(HttpServletRequest request, Model model) {

		Map<String, Object> map = new HashMap<String, Object>();

		String[] orderids = (String[]) request.getParameterValues("ordersArr");
		String userid = (String) request.getParameter("userid");
		List<String> orderAddresslList = new ArrayList<String>();
		String orderNo = "";
		try {
			if (orderids == null) {
				return "0";
			}
			for (int i = 0; i < orderids.length; i++) {
				orderNo += orderids[i] + ";";
				map.put("orderid", orderids[i]);
				orderAddresslList.add(iWarehouseService.getOrderidAddress(map));
			}
			if (orderAddresslList.size() < 1) {
				return "0";
			}
			String orderAddressStr = orderAddresslList.get(0);

			// 合并的订单是否一致
			for (int i = 0; i < orderAddresslList.size(); i++) {
				LOG.info(orderids[i] + "收货地址" + orderAddresslList.get(i));
				if (!orderAddressStr.equals(orderAddresslList.get(i))) {
					return "0";
				}
			}
		} catch (Exception e) {
			LOG.error("订单合并异常【合并订单号:" + orderNo + "】", e);
			return "0";
		}

		// 插入数据到待付款
		Map<String, Object> mapinsert = new HashMap<String, Object>();
		for (int i = 0; i < orderAddresslList.size(); i++) {
			// userid orderno
			mapinsert.put("orderno", orderids[i]);
			if (iWarehouseService.selectOrderFeeByOrderid(mapinsert) > 0) {
				iWarehouseService.delteFromOrderFeeByOrderid(mapinsert);
			}
			mapinsert.put("userid", userid);
			// 4 代表审核通过
			mapinsert.put("state", "4");
			// 这里只有审核不通过的时候才需要
			mapinsert.put("problem", "");
			mapinsert.put("mergeOrders", orderids[0]);
			mapinsert.put("mergestate", "1");
			iWarehouseService.insertOrderfeeFromOrderInfo(mapinsert, 0);
		}

		return "1";
	}

	// 打印出库单
	@RequestMapping(value = "/OrderPrint.do", method = RequestMethod.GET)
	public String OrderPrint(HttpServletRequest request, Model model) {
		// 取出打印结果集
		// List<StorageLocationBean> list = (List<StorageLocationBean>)
		// request.getSession().getAttribute("printResultSet");

		// 取出需要打印的订单
		String orderids = (String) request.getParameter("ordersArr");
		String[] orderidsArray = orderids.split(" ,");
		String orderParameter = "";
		for (int i = 0; i < orderidsArray.length; i++) {
			orderParameter += "'" + orderidsArray[i] + "',";
		}
		if ("".equals(orderParameter)) {
			return "orderPrint";
		}
		orderParameter = orderParameter.substring(0,
				orderParameter.length() - 1);
		LOG.info("打印出货单传过来的参数" + orderParameter);

		// 获得订单需要打印的信息
		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("orders", orderParameter);
		List<OrderInfoPrint> orderList = iWarehouseService
				.getOrderidPrinInfo(map);

		/**
		 * wkk dropship 打印 获取dropship 订单信息
		 */
		/*
		 * List<String> orderNoList = iWarehouseService.getOrderNoList(map);
		 * List<String> dropshipOrderList = new ArrayList<String>(); int flag =
		 * 0 ; for(String row:orderidsArray){ for(String od:orderNoList){
		 * if(row.equals(od)){ flag = 1; break; } } if(flag==1){ flag = 0;
		 * continue; }else{ dropshipOrderList.add(row); } } for(OrderInfoPrint
		 * bean:orderList){ bean.setIsDropshipFlag(0); for(String
		 * str:dropshipOrderList){ if(bean.getOrderid().equals(str)){
		 * bean.setIsDropshipFlag(1); } } }
		 */

		// List<OrderInfoPrint> newList = new ArrayList<OrderInfoPrint>();

		// 取出id
		String userids = "";
		String[] arrayUserids = null;
		List<OrderPrintInfoUtil> oipflist = new ArrayList<OrderPrintInfoUtil>();
		// 取出用户id 用来作为外层循环
		for (int i = 0; i < orderList.size(); i++) {
			if (userids.indexOf(orderList.get(i).getUserid()) == -1) {
				userids += orderList.get(i).getUserid() + ",";
			}
		}
		if (userids != "") { // 有订单 去结尾逗号
			userids = userids.substring(0, userids.length() - 1);

			if (userids.indexOf(",") == -1) // 如果只有一个订单
			{
				arrayUserids = new String[1];
				arrayUserids[0] = userids;

			} else {

				arrayUserids = userids.split(",");
			}
		}

		// 同一用户 不同订单 有可能存在不同的收件人
		String strod = "";
		for (int i = 0; i < arrayUserids.length; i++) {
			for (int j = 0; j < orderList.size(); j++) {
				OrderPrintInfoUtil opif = new OrderPrintInfoUtil();
				opif.setUserid(arrayUserids[i]);
				opif.setUserName(iWarehouseService.getUserName(arrayUserids[i]));
				int totalCount=iWarehouseService.getUndeliveredOrder(orderList.get(j).getOrderid());
				opif.setRemark(totalCount);
				if (arrayUserids[i].equals(orderList.get(j).getUserid())) {

					if (strod.indexOf(orderList.get(j).getOrderid()) == -1) {
						strod += orderList.get(j).getOrderid() + ",";
						// 查出订单地址
						Map<String, Object> map2 = new HashMap<String, Object>(); // sql
						// 参数
						map2.put("orderid", orderList.get(j).getOrderid());
						opif.setOrderAddress(iWarehouseService
								.getOrderidAddress(map2));
						opif.setIsDropshipFlag(orderList.get(j)
								.getIsDropshipFlag());
						// 查询时间
						Map<String, Object> map3 = new HashMap<String, Object>(); // sql
						// 参数
						map3.put("orderid", orderList.get(j).getOrderid());
						int dropshipFlag = orderList.get(j).getIsDropshipFlag() == 0 ? 0
								: 1;
						opif.setCreateTime(iWarehouseService
								.getOrderCreateTime(map3, dropshipFlag));
						// 处理地址显示格式
						String tempAddress = opif.getOrderAddress();

						System.out.println("地址++++++++++" + tempAddress);
						if (tempAddress != null && !"".equals(tempAddress)) {
							opif.setOrderAddress("</br>"
									+ tempAddress.replace("_", "</br>"));
						} else {
							opif.setOrderAddress("无");
						}

						opif.getOrders().add(orderList.get(j).getOrderid());

						// 订单对应商品的个数
						int count = 0;
						for (int k = 0; k < orderList.size(); k++) {
							if (orderList.get(j).getOrderid()
									.equals(orderList.get(k).getOrderid())) {
								count++;
							}
						}
						opif.getMap().put(orderList.get(j).getOrderid(), count);
//						opif.setRemark(totalCount);
						oipflist.add(opif);
					}
				}
			}

			// 创建时间
			// Date nowTime = new Date(System.currentTimeMillis());
			// SimpleDateFormat sdFormatter = new
			// SimpleDateFormat("yyyy-MM-dd");
			// String retStrFormatNowDate = sdFormatter.format(nowTime);
			// opif.setCreateTime(retStrFormatNowDate);

		}
		// 处理规格 od.car_type
		for (int i = 0; i < orderList.size(); i++) {
			String str = orderList.get(i).getCar_type();
			String goodtype = "";
			if (str != null) {
				if ((str).contains("@")) {
					String[] gdtp = (str).split(",");
					for (String ty : gdtp) {
						goodtype = goodtype + (!ty.contains("@")?ty:ty.substring(0, (ty.indexOf("@")))) + ";　";
					}
				} else if ((str).contains(",")) {
					goodtype = str.replace(",", ";<br/>");
				} else {
					goodtype = str;
				}
			}
			orderList.get(i).setCar_type(goodtype);
		}

		// 取出当前登录用户admname
		String admuserJson = Redis
				.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,
				Admuser.class);
		if (adm == null) {
			return "main_login";
		}

		// maplist

		for (int i = 0; i < oipflist.size(); i++) {
			Iterator iter = oipflist.get(i).getMap().entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry e = (Map.Entry) iter.next();
				// System.out.println(e.getKey()+"="+e.getValue());
				// maplist.

				List<OrderInfoPrint> listo = new ArrayList<OrderInfoPrint>();
				for (int j = 0; j < orderList.size(); j++) {

					if (e.getKey().equals(orderList.get(j).getOrderid())) {
						listo.add(orderList.get(j));
					}
				}
				oipflist.get(i).getMaplist().put((String) e.getKey(), listo);
			}

		}

		request.setAttribute("newListPrint", orderList);
		request.setAttribute("oipflist", oipflist);
		request.setAttribute("admname", adm.getAdmname());
		return "orderPrint1";
	}

	public String[] getOrdersProc(List<OrderInfoPrint> pagelist) {
		// 没有查询出来订单
		if (pagelist == null) {
			return null;
		}

		// 保存所有的订单 不包含重复 用来作为页面第一个循次数
		String strOrders = "";
		String[] arrayOrders = null;

		for (int i = 0; i < pagelist.size(); i++) {
			if (strOrders.indexOf(pagelist.get(i).getOrderid()) == -1) {
				strOrders += pagelist.get(i).getOrderid() + ",";
			}
		}
		if (strOrders != "") { // 有订单 去结尾逗号
			strOrders = strOrders.substring(0, strOrders.length() - 1);

			if (strOrders.indexOf(",") == -1) // 如果只有一个订单
			{
				arrayOrders = new String[1];
				arrayOrders[0] = strOrders;

			} else { // 多个订单
				// 分割出来每一个订单 保存数组
				arrayOrders = strOrders.split(",");
			}
		}
		return arrayOrders;
	}

	// 出库部分 根据订单查询所有商品
	@RequestMapping(value = "/getOrderdatels.do", method = RequestMethod.GET)
	@ResponseBody
	// jqery ajax 访问时加上 这样就不会返回页面名字，而是实际数据
	public String getOrderdatels(HttpServletRequest request, Model model) {

		String orderid = (String) request.getParameter("orderid");
		String userid = (String) request.getParameter("userid");
		String state = (String) request.getParameter("state");
		String problem = "";

		String t = (String) request.getParameter("problem");
		if (t != null && !"".equals(t)) {
			problem = t;
		}

		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("orderno", orderid);
		iWarehouseService.delteFromOrderFeeByOrderid(map); // 先删除
		map.put("userid", userid);
		map.put("state", state);
		map.put("problem", problem); // 审核不通过的 问题描述
		map.put("mergeOrders", orderid); // 这里是合并订单用的
		// mapinsert.put("mergestate", "1");
		int ret = iWarehouseService.insertOrderfeeFromOrderInfo(map, 0);

		return "123123123132123";
	}

	// 已出货列表
	@RequestMapping(value = "/getForwarder.do", method = RequestMethod.GET)
	public String getForwarder(HttpServletRequest request, Model model) {
		int pageNum = 1; // 当前页 默认第一页
		int pageSize = 50; // 当前页大小 默认

		// 取查询条件参数
		String orderid = (String) request.getParameter("orderid");
		String userid = (String) request.getParameter("userid");
		String express_no = (String) request.getParameter("express_no");
		String t = request.getParameter("pageNum");

		if (t != null && !"".equals(t)) {
			pageNum = Integer.parseInt(t);
		}
		// 页大小
		t = request.getParameter("pageSize");
		if (t != null && !"".equals(t)) {
			pageSize = Integer.parseInt(t);
		}

		int startNum = pageNum * pageSize - pageSize; // 开始位置

		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("orderid", orderid);
		map.put("userid", userid);
		map.put("express_no", express_no);
		int count = iWarehouseService.getCountForwarder(map); // 总记录数
		map.put("startNum", startNum);
		map.put("endNum", pageSize);
		map.put("settleWeight", "");// 重量
		map.put("shipprice", "");// 价格

		List<Forwarder> forwarderlist = iWarehouseService.getForwarder(map); // 结果集

		OrderInfoPage oip = new OrderInfoPage(); // 页

		// 如果不是第一次 查询 就已经保存了 OrderInfoPage

		oip.setForwarderlist(forwarderlist); // 一页数据
		oip.setPageNum(pageNum); // 当前页
		oip.setPageSize(pageSize); // 也大小
		oip.setPageSum(count); // 总记录数
		oip.setOrderid(orderid); // 查询条件
		oip.setUserid(userid); // 用户id
		oip.setExpress_no(express_no);
		request.setAttribute("oip", oip);

		return "forwarderpage";
	}

	/**
	 * *****************************************************************************************
	 * 类描述：XXXXXXXXXXXXXXXX
	 *
	 * @author: ly
	 * @date： 2018/05/17 15:22
	 * @version 1.0
	 *
	 *
	 * Version    Date                ModifiedBy                 Content
	 * --------   ---------           ----------                -----------------------
	 * 1.0.0        2018/05/17 15:22           ly                    通过订单查询订单详情 getDetailsByRemarks
	 *******************************************************************************************
	 */
	/*
	 * 示例 http://127.0.0.1:8086/cbtconsole/warehouse/getDetailsByRemarks.do?remarks=P814857461052042,P814877465443391,P815547538454717,P816066244626360,P817067128194334,P818182012480180,P818242773437573,P818292061660646,P818593287371124,P822698694783993,
	 * 返回值 picturepaths 入库验货照片 集合
	 *      ftpPicPaths 	出货照片 集合
	 */
	@RequestMapping("/getDetailsByRemarks")
	@ResponseBody
	public Map<String, Object> getDetailsByRemarks(HttpServletRequest request) {
		String remarks = request.getParameter("remarks");
		Map<String, Object> resMap = new HashMap<String, Object>();
		//参数判断
		if (remarks == null || "".equals(remarks) || remarks.length() < 3) {
			resMap.put("message", "error");
			return resMap;
		}
		//查询该订单相关信息
		resMap = iWarehouseService.getDetailsByRemarks(remarks);
		return resMap;
	}

	// 已出货列表 批量出库版本
//	@RequestMapping(value = "/getForwarderPlck.do", method = RequestMethod.GET)
	@RequestMapping(value = "/getForwarderPlck")
	@ResponseBody
	public EasyUiJsonResult getForwarderPlck(HttpServletRequest request, Model model) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		DecimalFormat df = new DecimalFormat("#0.##");
		// 取查询条件参数
		String orderid = (String) request.getParameter("orderid");
		String userid = (String) request.getParameter("userid");
		String express_no = (String) request.getParameter("express_no");
		int page = Integer.valueOf(request.getParameter("page"));
		if (page > 0) {
			page = (page - 1) * 20;
		}

		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("orderid", orderid);
		map.put("userid", userid);
		map.put("express_no", express_no);
		int count = iWarehouseService.getCountForwarderplck(map); // 总记录数
		map.put("startNum", page);
		map.put("settleWeight", "");// 重量
		map.put("shipprice", "");// 价格

		List<Forwarder> forwarderlist = iWarehouseService.getForwarderplck(map); // 结果集
		for (Forwarder forwarder : forwarderlist) {
			if(forwarder.getIsDropshipFlag()==1){
				forwarder.setUser_id(forwarder.getUser_id()+"<img src='/cbtconsole/img/ds1.png' style='width:25px;margin-top: 2px;' title='drop shipping'>");
			}
			double a=0.00;
			if(StringUtil.isNotBlank(forwarder.getSvolume()) && forwarder.getSvolume().contains("*")){
				String [] datas=forwarder.getSvolume().split("\\*");
				if("emsinten".equals(forwarder.getTransportcompany())){
					a=Double.valueOf(datas[0])*Double.valueOf(datas[1])*Double.valueOf(datas[2])/5000;
				}else{
					a=Double.valueOf(datas[0])*Double.valueOf(datas[1])*Double.valueOf(datas[2])/6000;
				}
				double b=Double.valueOf(forwarder.getSweight())>Double.valueOf(forwarder.getVolumeweight())?Double.valueOf(forwarder.getSweight()):Double.valueOf(forwarder.getVolumeweight());
				if(a>b){
					forwarder.setTypes("抛货");
				}else{
					forwarder.setTypes("重货");
				}
			}
			StringBuffer bf=new StringBuffer();
			bf.append("<select id='logistics_name_"+forwarder.getId()+"'>");
			bf.append("<option value='JCEX'>佳成JCEX</option>");
			bf.append("<option value='emsinten'>邮政</option>");
			bf.append("<option value='SF'>顺丰-睦鹏</option>");
			bf.append("<option value='ups'>ups</option>");
			bf.append("<option value='DHL'>DHL</option>");
			bf.append("<option value='TNT'>TNT</option>");
			bf.append("<option value='TL'>泰蓝-舜衡安</option>");
			bf.append("<option value='深圳诚泰'>航邮-深圳诚泰</option>");
			bf.append("<option value='灿鑫'>灿鑫</option>");
			bf.append("<option value='Dpex'>Dpex</option>");
			bf.append("<option value='Fedex'>Fedex</option>");
			bf.append("<option value='原飞航'>原飞航</option>");
			bf.append("<option value='yodel'>yodel</option>");
			bf.append("<option value='zto'>中通</option>");
			bf.append("<option value='迅邮'>迅邮</option>");
			bf.append("</select><script>$('#logistics_name_"+forwarder.getId()+"').val('"+forwarder.getTransportcompany()+"');</script>");
			forwarder.setTransportcompany(bf.toString());
			bf.setLength(0);
			if(StringUtil.isBlank(forwarder.getExchange_rate()) || "null".equals(forwarder.getExchange_rate())){
				forwarder.setExchange_rate("6.3");
			}
			forwarder.setEstimatefreight(Double.valueOf(df.format(forwarder.getEstimatefreight()*Double.parseDouble(forwarder.getExchange_rate()))));
			forwarder.setExpressno("<input type='text' id='express_no_"+forwarder.getId()+"' value='"+forwarder.getExpressno()+"'/><button onclick=\"updateExperssNo("+forwarder.getId()+",'" + forwarder.getShipmentno()+ "','"+forwarder.getSvolume()+"','"+forwarder.getSweight()+"','"+forwarder.getFreight()+"','"+forwarder.getZoneId()+"')\">修改</button>");
		}
		json.setRows(forwarderlist);
		json.setTotal(count);
		return json;
	}

	@RequestMapping("/mabangShipmentUpload")
	private @ResponseBody
    String mabangShipmentUpload(
            MultipartHttpServletRequest request, HttpServletResponse response) {
		String result = "";
		String uploadPath = SysParamUtil.getParam("uploadPath");
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		// 每次上传的数据标识
		// String uuid = UUID.randomUUID().toString();
		while (itr.hasNext()) {
			// 取出文件
			mpf = request.getFile(itr.next());
			Long time = System.currentTimeMillis();
			String path = uploadPath + "/" + time + "/";
			isExist(path);
			// 获取后缀
			// String suffix =
			// mpf.getOriginalFilename().substring((mpf.getOriginalFilename()).lastIndexOf("."));
			// 获取前缀
			String prefix = mpf.getOriginalFilename().substring(0,
					(mpf.getOriginalFilename()).lastIndexOf("."));
			try {
				// 输出(保存)文件
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(path
						+ mpf.getOriginalFilename()));
				if ("sku".equalsIgnoreCase(prefix)) {
					List<Skuinfo> list = ExcelUtil.readSkuExcel(path
							+ mpf.getOriginalFilename());
					int res = skuinfoService.insertSkuinfo(list);
					if (res != 0) {
						result = "{\"success\":true,\"message\":\"导入成功!"
								+ mpf.getOriginalFilename() + "共导入" + res
								+ "条[sku]数据!\"}";
					} else {
						result = "{\"success\":false,\"message\":\"没有数据导入!\"}";
					}
				} else {
					Map<String, List<?>> resmap = ExcelUtil
							.readMabangExcel(path + mpf.getOriginalFilename());
					List<Mabangshipment> list = (List<Mabangshipment>) resmap
							.get("success");
					List<String> failList = (List<String>) resmap.get("fail");
					// 从SKU表中获取shipImgLink，为mabangShipment添加 url、item_id字段
					int res = 0;
					for (int i = 0; i < list.size(); i++) {
						Skuinfo sku = new Skuinfo();
						Mabangshipment mabangshipment = list.get(i);
						sku.setSku(mabangshipment.getSku());
						List<Skuinfo> skus = skuinfoService
								.selectShipImgLinkBySKU(sku);
						if (skus == null || skus.size() == 0) { // 部分订单
							// 在sku中找不到url，暂时跳过
							continue;
						}
						String url = skus.get(0).getShipimglink();
						String itemid = getItem_id(url);
						mabangshipment.setUrl(url);
						mabangshipment.setItem_id(itemid);

						int num = mabangshipmentService
								.insertMabangShipment(mabangshipment);
						if (num == 2) {
							res += 1;
						}
					}

					// int res =
					// mabangshipmentService.insertMabangShipment(list);
					if (res != 0) {
						if (failList != null && failList.size() > 0) {
							result = "{\"success\":true,\"message\":\"导入成功!"
									+ mpf.getOriginalFilename() + "共导入" + res
									+ "条[运单]数据!\\r\\n运单号:"
									+ failList.toString()
									+ "导入失败!请检查运单号是否存在或其他原因!\"}";
						} else {
							result = "{\"success\":true,\"message\":\"导入成功!"
									+ mpf.getOriginalFilename() + "共导入" + res
									+ "条[运单]数据!\"}";
						}
					} else {
						result = "{\"success\":false,\"message\":\"没有数据导入!\"}";
					}
				}
				// 导入完成,删除文件夹及其文件夹下的文件
				FileTool.deleteDirectory(path);
			} catch (Exception e) {
				e.printStackTrace();
				result = "{\"success\":false,\"message\":\"Error:请联系开发人员!\"}";
			}
		}
		return result;
	}

	public String getItem_id(String u) {
		if (u == null || "".equals(u) || u.length() < 12) {
			return "0";
		}
		String ret = "";
		Pattern p = Pattern.compile("\\d{2,}");
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

	// 判断文件是否存在,不存在则创建
	public Boolean isExist(String path) {
		File file = new File(path);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdirs();
		}
		return true;
	}

	// *****************************************综合采购**********************************************
	// 查询商品快递单号，和淘宝状态
	@RequestMapping(value = "/getlogisticsidAndState.do", method = RequestMethod.GET)
	@ResponseBody
	// jqery ajax 访问时加上 这样就不会返回页面名字，而是实际数据
	public String getlogisticsidAndState(HttpServletRequest request, Model model) {

		String goodsid = (String) request.getParameter("goodsid");

		if (goodsid == null && "".equals(goodsid)) {
			return "0";
		}

		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("goodsid", goodsid);
		List<Logisticsinfo> lgis = iWarehouseService
				.getlogisticsidAndState(map);
		System.out.println(JSONArray.fromObject(lgis).toString());
		return JSONArray.fromObject(lgis).toString();
	}
	/**
	 * 采样订单整单退货
	 * @Title refundOrderShipnoEntry
	 * @Description TODO
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping("/refundOrderShipnoEntry")
	protected void refundOrderShipnoEntry(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		response.setCharacterEncoding("utf-8");
		String shipno = request.getParameter("shipno");
		String t_ordrid = request.getParameter("t_orderid");
		String remark = request.getParameter("remark");
		map.put("t_orderid", t_ordrid);
		map.put("page", 0);
		List<RefundSamplePojo> list=iWarehouseService.searchRefundSample(map);
		int row=0;
		StringBuilder goodsids=new StringBuilder();
		StringBuilder orderids=new StringBuilder();
		for(int i=0;i<list.size();i++){
			RefundSamplePojo r=list.get(i);
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("tb_id", r.getT_id());
			map1.put("re_shipno", shipno);
			map1.put("orderid", t_ordrid);
			map1.put("remark", remark);
			map1.put("in_id", r.getIn_id());
			map1.put("od_orderid", r.getOd_orderid());
			map1.put("goodsid", r.getGoodsid());
			map1.put("counts", r.getItemqty());
			row+=iWarehouseService.refundShipnoEntry(map1);
			//退样后库存相应减少
//			String state=iWarehouseService.getState(map1);//是否已经退过货0 未退货  1退货中
			//退样后库存相应减少
			int storag_count=iWarehouseService.getStoragCount(map1);
			if(storag_count>0){
				if(storag_count>0){
					map1.put("storag_count", storag_count);
					map1.put("flag",  r.getFlag());
					iWarehouseService.updateInventoryCount(map1);
				}
				row=iWarehouseService.refundShipnoEntry(map1);
				goodsids.append("'").append(map1.get("goodsid")).append("',");
				orderids.append("'").append(map1.get("od_orderid")).append("',");
			}
			map1.clear();
		}
		if(row>0 && orderids.toString().length()>0 && goodsids.toString().length()>0){
			List<String> goods_pids=iWarehouseService.getRefundGoodsPid(goodsids.toString().substring(0,goodsids.toString().length()-1),orderids.toString().substring(0,orderids.toString().length()-1));
			StringBuilder s=new StringBuilder();
			if(goods_pids.size()>0){
				for(int i=0;i<goods_pids.size();i++){
					s.append("'").append(goods_pids.get(i)).append("',");
				}
				iWarehouseService.updateSamplFlag(s.toString().substring(0,s.toString().length()-1),0);
			}
		}
		PrintWriter out = response.getWriter();
		out.print(row+"");
		out.flush();
		out.close();
	}
	/**
	 * 单个商品采样退货
	 * @Title refundShipnoEntry
	 * @Description TODO
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return void
	 */
	@RequestMapping("/refundShipnoEntry")
	protected void refundShipnoEntry(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		response.setCharacterEncoding("utf-8");
		String tb_id = request.getParameter("t_id");
		String shipno = request.getParameter("shipno");
		String t_ordrid = request.getParameter("t_orderid");
		String remark = request.getParameter("remark");
		String in_id = request.getParameter("in_id");
		String od_orderid = request.getParameter("od_orderid");
		String goodsid = request.getParameter("goodsid");
		String flag = request.getParameter("flag");
		String counts=request.getParameter("counts");
		map.put("tb_id", tb_id);
		map.put("re_shipno", shipno);
		map.put("orderid", t_ordrid);
		map.put("remark", remark);
		map.put("in_id", in_id);
		map.put("od_orderid", od_orderid);
		map.put("goodsid", goodsid);
		map.put("counts", counts);
		String state=iWarehouseService.getState(map);//是否已经退过货0 未退货  1退货中
		//退样后库存相应减少
		int row=0;
		int storag_count=iWarehouseService.getStoragCount(map);
		if(storag_count>0){
			if(storag_count>0 && (StringUtils.isStrNull(state) || Integer.valueOf(state)==0)){
				map.put("storag_count", storag_count);
				map.put("flag", flag);
				iWarehouseService.updateInventoryCount(map);
			}
			row=iWarehouseService.refundShipnoEntry(map);
			List<String> goods_pids=iWarehouseService.getRefundGoodsPid("'"+map.get("goodsid").toString()+"'","'"+map.get("od_orderid").toString()+"'");
			//更改28库中ali_info_data 的sampl_flag为0  未采用
			if(goods_pids.size()>0){
				iWarehouseService.updateSamplFlag("'"+goods_pids.get(0)+"'",0);
			}
		}
		PrintWriter out = response.getWriter();
		out.print(row+"");
		out.flush();
		out.close();
	}

	// 查询淘宝订单信息
	@RequestMapping(value = "/getlogisticsinfo.do", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	// jqery ajax 访问时加上 这样就不会返回页面名字，而是实际数据
	public String getlogisticsinfo(HttpServletRequest request, Model model) {

		String logistics_id = (String) request.getParameter("logistics_id");

		if (logistics_id == null && "".equals(logistics_id)) {
			return "0";
		}
		String taobao_itemid = (String) request.getParameter("taobao_itemid");

		if (taobao_itemid == null && "".equals(taobao_itemid)) {
			return "0";
		}

		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("logistics_id", logistics_id);
		map.put("taobao_itemid", taobao_itemid);
		Logisticsinfo lgis = iWarehouseService.getlogisticsinfo(map);

		map.put("taobao_itemid", lgis.getTaobao_itemid());
		List<OrderProductSurcePojo> ppsplist = iWarehouseService
				.getlogisticsAndOrderProductSurc(map);

		// Map<String,Object> reqMap = new HashMap<String, Object>();
		// reqMap.put("lgis", lgis);
		// reqMap.put("ppsplist", ppsplist);
		//
		// JSONArray.fromObject(reqMap).toString();
		// System.out.println(JSONArray.fromObject(reqMap).toString());
		// //return JSONArray.fromObject(reqMap).toString();
		//
		return getTableHtml(lgis, ppsplist); // 拼装table
	}

	// 库存
	@RequestMapping(value = "/getIdrelationtable.do", method = RequestMethod.GET)
	public String getIdrelationtable(HttpServletRequest request, Model model) {
		int pageNum = 1; // 当前页 默认第一页
		int pageSize = 50; // 当前页大小 默认

		// 取查询条件参数
		String goodsname = (String) request.getParameter("goodsname");

		String t = request.getParameter("pageNum");

		if (t != null && !"".equals(t)) {
			pageNum = Integer.parseInt(t);
		}
		// 页大小
		t = request.getParameter("pageSize");
		if (t != null && !"".equals(t)) {
			pageSize = Integer.parseInt(t);
		}

		int startNum = pageNum * pageSize - pageSize; // 开始位置

		Map<String, Object> map = new HashMap<String, Object>(); // sql 参数
		map.put("goodsname", goodsname);
		int count = iWarehouseService.getCountIdrelationtable(map); // 总记录数
		map.put("startNum", startNum);
		map.put("endNum", pageSize);
		List<OrderInfoPrint> oipList = iWarehouseService
				.getIdrelationtable(map); // 结果集

		OrderInfoPage oip = new OrderInfoPage(); // 页

		// 如果不是第一次 查询 就已经保存了 OrderInfoPage
		oip.setOipList(oipList);
		oip.setPageNum(pageNum); // 当前页
		oip.setPageSize(pageSize); // 也大小
		oip.setPageSum(count); // 总记录数
		oip.setGoodsname(goodsname); // 查询条件

		request.setAttribute("oip", oip);

		return "oiplistpage";
	}

	// 淘宝状态转换
	public String getState(String state) {
		if (state == "") {
			return "没有状态";
		}
		int newState = Integer.parseInt(state);
		String strRet = "";
		switch (newState) {
			case 0:
				strRet = "等待卖家发货";
				break;
			case 1:
				strRet = "卖家已发货";
				break;
			case 2:
				strRet = "等待买家付款";
				break;
			case 3:
				strRet = "买家已付款";
				break;
			case 4:
				strRet = "物流运输中";
				break;
			case 5:
				strRet = "物流派件中";
				break;
			case 6:
				strRet = "交易关闭";
				break;
			case 7:
				strRet = "快件已签收";
				break;
			case 8:
				strRet = "交易成功";
				break;
			case 9:
				strRet = "等待评价";
				break;
			case 10:
				strRet = "等待买家确认收货";
				break;
		}
		return strRet;
	}

	// 拼接table
	public String getTableHtml(Logisticsinfo lgis,
							   List<OrderProductSurcePojo> ppsplist) {
		String ppspTable = "";
		ppspTable += "<table border='1'>" + "<tr>" + "<td>淘宝运单号<td>"
				+ "<td>状态<td>" + "<td>下单时间<td>" + "<td>是否跟进<td>" + "	</tr>"
				+ "	<tr>" + "	<td >" + lgis.getLogistics_id() + "<td>"
				+ "	<td >" + getState(lgis.getTb_state()) + "<td>" + "	<td >"
				+ lgis.getCreatetime() + "<td>" + "	<td >是<td>" + "</tr>"
				+ "</table>";
		String ppsplistTable = "";
		ppsplistTable += "<table border='1' ><tr>";
		ppsplistTable += "<td>";
		ppsplistTable += "<table border='1'>";
		ppsplistTable += "<tr><td>";
		ppsplistTable += "淘宝商品信息";
		ppsplistTable += "</td></tr>";

		ppsplistTable += "<tr><td>";
		ppsplistTable += "<img width='150px' height='150px' src='"
				+ lgis.getGoodimg() + "' data-original='" + lgis.getGoodimg()
				+ "' width='150px' height='150px' />";
		ppsplistTable += "</td></tr>";

		ppsplistTable += "<tr><td>";
		ppsplistTable += lgis.getTaobao_orderid();
		ppsplistTable += "</td></tr>";

		ppsplistTable += "<tr><td>价格:";
		ppsplistTable += lgis.getItemprice();
		ppsplistTable += "</td></tr>";

		ppsplistTable += "<tr><td rowspan='3'>数量:";
		ppsplistTable += lgis.getGoodscount();
		ppsplistTable += "</td></tr>";

		ppsplistTable += "</table>";
		ppsplistTable += "</td>";

		for (int i = 0; i < ppsplist.size(); i++) {
			ppsplistTable += "<td>";
			ppsplistTable += "<table border='1'>";

			ppsplistTable += "<tr><td>";
			ppsplistTable += "自己商品信息";
			ppsplistTable += "</td></tr>";

			ppsplistTable += "<tr><td>";
			ppsplistTable += "<img width='150px' height='150px' src='"
					+ ppsplist.get(i).getOdcar_img() + "' data-original='"
					+ ppsplist.get(i).getOdcar_img()
					+ "' width='150px' height='150px' />";
			ppsplistTable += "</td></tr>";

			ppsplistTable += "<tr><td>";
			ppsplistTable += ppsplist.get(i).getOrderid();
			ppsplistTable += "</td></tr>";

			ppsplistTable += "<tr><td>价格:";
			ppsplistTable += ppsplist.get(i).getGoods_price();
			ppsplistTable += "</td></tr>";

			ppsplistTable += "<tr><td>数量:";
			ppsplistTable += (ppsplist.get(i).getBuycount() == null ? "0"
					: ppsplist.get(i).getBuycount());
			ppsplistTable += "</td></tr>";

			ppsplistTable += "<tr><td>商品id:";
			ppsplistTable += (ppsplist.get(i).getGoodsid() == null ? ""
					: ppsplist.get(i).getGoodsid());
			ppsplistTable += "</td></tr>";

			ppsplistTable += "<tr><td>采购人员:";
			ppsplistTable += (ppsplist.get(i).getAdmName() == null ? "无"
					: ppsplist.get(i).getAdmName());
			ppsplistTable += "</td></tr>";

			ppsplistTable += "</table>";
			ppsplistTable += "</td>";
		}
		ppsplistTable += "</tr></table>";

		return ppspTable + ppsplistTable;
	}

	// 采购补货
	@RequestMapping(value = "/dhybh.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String dhybh(HttpServletRequest request, Model model) {
		// var userid = $("#bh_userid").val();
		// var orderid = $("#bh_orderid").val();
		// var goodsid = $("#bh_goodsid").val();
		// var goods_url = $("#bh_goods_url").val();
		// var goods_title = $("#bh_goods_title").val();
		// var dhy_jg1 = $("#dhy_jg1").val();
		// var dhy_hy1 = $("#dhy_hy1").val();
		// var dhy_jg2 = $("#dhy_jg2").val();
		// var dhy_hy2 = $("#dhy_hy2").val();

		String userid = request.getParameter("userid");
		String orderid = request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");
		String goods_url = request.getParameter("goods_url");
		String goods_title = request.getParameter("goods_title");
		String goods_type = "1";// 多货源
		String goods_p_url1 = request.getParameter("dhy_hy1");
		String buycount1 = request.getParameter("dhy_sl1");
		String goods_price1 = request.getParameter("dhy_jg1");

		String goods_p_url2 = request.getParameter("dhy_hy2");
		String buycount2 = request.getParameter("dhy_sl2");
		String goods_price2 = request.getParameter("dhy_jg2");

		String id1 = request.getParameter("dhy_id1");
		String id2 = request.getParameter("dhy_id2");
		String remark = "";
		String rep_type = "1";

		String inId = "";
		String tb_1688_itemid1 = Util.getItemid(goods_p_url1);
		String tb_1688_itemid2 = Util.getItemid(goods_p_url2);
		if (!"0".equals(id1) && id1 != null) {
			inId += "'" + id1 + "'";
		}
		if (!"0".equals(id2) && id2 != null) {
			if (!"0".equals(id1) && id1 != null) {
				inId += ",";
			}
			inId += "'" + id2 + "'";
		}
		if (inId.length() > 1) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", inId);
			iWarehouseService.delteOrderReplenishment(map);
		}
		//

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("userid", userid);
		map.put("orderid", orderid);
		map.put("goodsid", goodsid);
		map.put("goods_url", goods_url);
		map.put("remark", remark);
		map.put("rep_type", rep_type);
		map.put("goods_title", goods_title);
		map.put("goods_type", goods_type);

		map.put("goods_p_url", goods_p_url1);
		map.put("goods_price", goods_price1);
		map.put("tb_1688_itemid", tb_1688_itemid1);
		map.put("buycount", buycount1);

		int ret = iPurchaseService.insertOrderReplenishment(map);

		map.put("goods_p_url", goods_p_url2);
		map.put("goods_price", goods_price2);
		map.put("tb_1688_itemid", tb_1688_itemid2);
		map.put("buycount", buycount2);

		// http:3333.html
		int t = 1;
		if ("".equals(goods_p_url2) || goods_p_url2 == null) {
			t = 0;
		}
		if ("".equals(goods_price2) || goods_price2 == null) {
			t = 0;
		}
		if ("".equals(buycount2) || buycount2 == null) {
			t = 0;
		}
		if (t == 1) {
			ret += iPurchaseService.insertOrderReplenishment(map);
		}

		return "" + ret;
	}

	// 显示采样订单Log
	@RequestMapping(value = "/displayBuyLog", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String displayBuyLog(HttpServletRequest request, Model model) {
		String goods_pid = request.getParameter("goods_pid");
		String car_urlMD5 = request.getParameter("car_urlMD5");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goods_pid", goods_pid);
		map.put("car_urlMD5", car_urlMD5);

		List<DisplayBuyInfo> list = iWarehouseService.displayBuyLog(map);
		if(list.size()>0){
			Map<String, String> map1=iWarehouseService.getCompanyInfo(goods_pid);
			for (DisplayBuyInfo d : list) {
				d.setCompany(map1.get("shop_id"));
				d.setLevel(map1.get("levels"));
			}
		}

		return JSONArray.fromObject(list).toString();
	}

	// 多货源查询
	@RequestMapping(value = "/getDhy.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getDhy(HttpServletRequest request, Model model) {
		String orderid = request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderid", orderid);
		map.put("goodsid", goodsid);
		map.put("goods_type", "1");

		List<OrderReplenishmentPojo> list = iWarehouseService
				.getIsReplenishment(map);

		return JSONArray.fromObject(list).toString();
	}

	// 发送邮件
	@RequestMapping(value = "/sendFahuoEmail.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String sendFahuoEmail(HttpServletRequest request, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		OrderwsServer os = new OrderwsServer();
		String userid = request.getParameter("userid"); // 用户id
		String expetTime = request.getParameter("expetTime"); // 发送内容
		String toEmail = request.getParameter("toEmail"); // 用户邮箱
		String orderNo = request.getParameter("orderNo"); // 订单号
		String etime = expetTime.replace("天", " days");
		userid=StringUtils.isStrNull(userid)?"0":userid;
		toEmail=StringUtils.isStrNull(toEmail)?"0":toEmail;
		expetTime=StringUtils.isStrNull(expetTime)?"0":expetTime;
		toEmail=StringUtils.isStrNull(toEmail)?"0":toEmail;
		map.put("userid", userid);
		int res = os.sendShipment(orderNo, Integer.parseInt(userid), toEmail,etime);
		return res + "";
	}

	// 佳成面单二维码
	@RequestMapping(value = "/getNumImg.do", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void getNumImg(HttpServletRequest request,
						  HttpServletResponse response) {
		String number = request.getParameter("number");
		BufferedImage localBufferedImage = null;
		String str = number;
		JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(),WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
		try {
			localBufferedImage = localJBarcode.createBarcode(str);
		} catch (InvalidAtributeException e) {
			e.printStackTrace();
		}
		try {
			ImageIO.write(localBufferedImage, "jpg", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// 佳成面单信息导出
	@RequestMapping(value = "/exportJcexPrintInfo")
	@ResponseBody
	protected void exportJcexPrintInfo(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException, ParseException {
		String ckStartTime = (String) request.getParameter("ckStartTime");
		String ckEndTime = (String) request.getParameter("ckEndTime");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ckStartTime", ckStartTime);
		map.put("ckEndTime", ckEndTime);
		List<JcexPrintInfo> jcexList = iWarehouseService.getJcexPrintInfoPlckCount(map);
		Date date = new Date(System.currentTimeMillis());
		int year = date.getYear() + 1900;
		String filename = "佳成面单信息" + year;
		HSSFWorkbook wb  = generalReportService.exportJcexPrintInfo(jcexList);
		filename=StringUtils.getFileName(filename);
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}



	// 佳成面单信息
	@RequestMapping(value = "/getJcexPrintInfoPlck")
	@ResponseBody
	public EasyUiJsonResult getJcexPrintInfoPlck(HttpServletRequest request, HttpServletResponse response) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		String ckStartTime = (String) request.getParameter("ckStartTime");
		String ckEndTime = (String) request.getParameter("ckEndTime");
		int page = Integer.valueOf(request.getParameter("page"));
		page=page>0?(page - 1) * 20:1;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ckStartTime", ckStartTime);
		map.put("ckEndTime", ckEndTime);
		map.put("page", page);
		List<JcexPrintInfo> jcexList = iWarehouseService.getJcexPrintInfoPlck(map);
		List<JcexPrintInfo> jcexListCount = iWarehouseService.getJcexPrintInfoPlckCount(map);
		json.setRows(jcexList);
		json.setTotal(jcexListCount.size());
		return json;
	}

	// 写入已导出
	@RequestMapping(value = "/updateDeclareinfoByOrderid.do", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateDeclareinfoByOrderid(HttpServletRequest request,
											 HttpServletResponse response) {
		String orderidArray = request.getParameter("orderidArray");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderidArray", orderidArray);
		int ret = iWarehouseService.updateDeclareinfoByOrderid(map);
		return ret + "";
	}

	// 写入已导出
	@RequestMapping(value = "/queryUser", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryUser(HttpServletRequest request,
										 HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> localList = iWarehouseService.queryUser();
		DataSourceSelector.set("dataSource1");
		List<Integer> remoteList = iWarehouseService.queryUser();
		map.put("local", localList);
		map.put("remote", remoteList);
		return map;
	}

	/**
	 * 根据产品表数据生成采样订单
	 * @Title getGoodsCar
	 * @Description TODO
	 * @param request
	 * @param response
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/createBuyOrderForGoods", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createBuyOrderForGoods(HttpServletRequest request,
	                             HttpServletResponse response) {
		DataSourceSelector.restore();
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
		int row=0;
		Map<Integer, Integer> mapList=new HashMap<Integer, Integer>();
		Map<String, Object> map = new HashMap<String, Object>();
		String goods_pids = request.getParameter("goods_pids");
		String sku_list=request.getParameter("sku_val");
		sku_list=sku_list.substring(0,sku_list.length()-1);
		List<OrderDetailsBean> od_list=new ArrayList<OrderDetailsBean>();
		Map<String,String> map_sku=new HashMap<String, String>();
		Map<String,String> map_count=new HashMap<String, String>();
		SendMQ sendMQ=null;
		try{
			sendMQ=new SendMQ();
			String sku_str[]=sku_list.split("&");
			for (String s : sku_str) {
				StringBuilder sb=new StringBuilder();
				String []skus=s.split("\\$");
				String usecount=skus[1];
				String sku_lists="";
				if(skus.length>=3){
					sku_lists=skus[2];
				}else{
					map_count.put(skus[0], usecount);
					map_sku.put(skus[0], sb.toString());
					continue;
				}
				if(sku_lists.indexOf("|")>=-1){
					String s_list []=sku_lists.split("\\|");
					for (String s_ : s_list) {
						if(s_.indexOf("_")>=-1){
							sb.append(s_.split("_")[3]).append(":").append(s_.split("_")[1]).append("@").append(s_.split("_")[0]).append(",");
						}
					}
				}
				map_sku.put(skus[0], sb.toString());
				map_count.put(skus[0], usecount);
				sb.setLength(0);
			}
			map.put("goods_pids", goods_pids);
			List<CustomGoodsBean> listGoods=iWarehouseService.getAllGoodsInfos(goods_pids);
//			DataSourceSelector.set("dataSource127hop");
			if(listGoods.size()>0){
				//生成采样订单号
				SimpleDateFormat order=new SimpleDateFormat("yyyyMMddHHmmss");
				Date data=new Date();
				String order_no=order.format(data);
				Orderinfo oi=new Orderinfo();
				oi.setOrderNo(order_no);
				oi.setDetailsNumber(goods_pids.split(",").length);
//				row+=iWarehouseService.insertOrderInfo(oi);
				sendMQ.sendMsg(new RunSqlModel("insert into orderinfo (order_no,orderRemark,isDropshipOrder,details_number,create_time,user_id,state,delivery_time,mode_transport,product_cost,pay_price) " +
						"values('"+oi.getOrderNo()+"','采样订单',3,"+oi.getDetailsNumber()+",now(),13653,1,'3-6','Epacket@3-6@USA@all',0,0)"));
				//生成order_details
				StringBuilder sqls=new StringBuilder();
				sqls.append("INSERT INTO order_details (orderid,goodsname,goodsid,car_url,car_img,goodscatid,car_urlMD5,goods_pid,car_type,userid,goodsprice,yourorder) VALUES ");
				for (CustomGoodsBean a : listGoods) {
					OrderDetailsBean od=new OrderDetailsBean();
					od.setOrderid(order_no);
					SimpleDateFormat goodsid=new SimpleDateFormat("ddHHmmss");
					Date data1=new Date();
					Thread.sleep(1000);
					int goodid=Integer.valueOf(goodsid.format(data1));
					od.setGoodsid(goodid);
					od.setGoodsname(a.getEnname());
					od.setGoods_pid(a.getPid());
					od.setGoods_url("https://www.import-express.com/goodsinfo/cbtconsole-1"+a.getPid()+".html");
					od.setGoods_img(a.getRemotpath()+a.getCustomMainImage());
					od.setCar_img(a.getRemotpath()+a.getCustomMainImage());
					od.setUserid(13653);
					od.setYourorder(1);
					od.setGoodsprice(String.valueOf(Double.valueOf(a.getPrice())/Util.EXCHANGE_RATE));
					if(!StringUtils.isStrNull(map_sku.get(a.getPid()))){
						od.setCar_type(map_sku.get(a.getPid()));
					}
					String goodscatid="";
					od.setGoodscatid(goodscatid);
					od.setCar_urlMD5(itemIDToUUID(a.getPid()));
					//判断需要生成的order_details是否在库存已有采样商品（非取消）
					int od_exit=iWarehouseService.getOrderDetailsExit(od.getGoods_pid(),od.getCar_type());
					if(od_exit>0){
						continue;
					}
					od_list.add(od);
					sqls.append(" ('"+od.getOrderid()+"','"+od.getGoodsname()+"','"+od.getGoodsid()+"','"+od.getGoods_url()+"','"+od.getCar_img()+"'," +
							"'"+od.getGoodscatid()+"','"+od.getCar_urlMD5()+"','"+od.getGoods_pid()+"','"+od.getCar_type()+"','"+od.getUserid()+"','"+od.getGoodsprice()+"',1),");
				}
				if(od_list.size()>0){
//					row+=iWarehouseService.insertOrderDetails(od_list);
					sendMQ.sendMsg(new RunSqlModel(sqls.toString().substring(0,sqls.toString().length()-1)));
					//分配采购
					String adminid=String.valueOf(user.getId());
//					List<OrderDetailsBean> list_od=iWarehouseService.getOrderDetailsByOrderid(order_no);
					for (OrderDetailsBean a : od_list) {
						a.setFreight(adminid);
					}
					iWarehouseService.insertGd(od_list);
					iWarehouseService.updateGdOdid();
					for(int i=0;i<od_list.size();i++){
						iWarehouseService.updateCrossShopr(od_list.get(i).getGoods_pid());
						sendMQ.sendMsg(new RunSqlModel("update custom_benchmark_ready set samplingStatus=2 where pid='"+od_list.get(i).getGoods_pid()+"'"));
					}
				}
			}
		}catch(Exception e){
			LOG.error("createBuyOrder:",e);
			row=0;
		}finally {
			sendMQ.closeConn();
		}
		return "1";
	}


	/**
	 * 根据客户下单产品生成采样订单
	 * @Title getGoodsCar
	 * @Description TODO
	 * @param request
	 * @param response
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/createBuyOrder", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String createBuyOrder(HttpServletRequest request,
								 HttpServletResponse response) {
		DataSourceSelector.restore();
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
		int row=0;
		Map<Integer, Integer> mapList=new HashMap<Integer, Integer>();
		Map<String, Object> map = new HashMap<String, Object>();
		String goods_pids = request.getParameter("goods_pids");
		String pid_list=request.getParameter("pid_list");
		if(StringUtil.isBlank(pid_list)){
			return 0+"";
		}
		pid_list=pid_list.substring(0,pid_list.length()-1);
		String sku_list=request.getParameter("sku_val");
		sku_list=sku_list.substring(0,sku_list.length()-1);
		System.out.println("sku_val========"+sku_list);
		List<OrderDetailsBean> od_list=new ArrayList<OrderDetailsBean>();
		Map<String,String> map_sku=new HashMap<String, String>();
		Map<String,String> pidMap=new HashMap<String, String>();
		Map<String,String> map_count=new HashMap<String, String>();
		try{
			SendMQ sendMQ=new SendMQ();
			String remarks_val=request.getParameter("remarks_val");
			Map<String,String> remarkMap=new HashMap<String,String>();
			String remark_str []=remarks_val.split("&");
			for(String r:remark_str){
				String [] rs=r.split("\\$");
				remarkMap.put(rs[0],rs[1]);
			}
			//拼接规格属性534306826519|8 beautiful no. 57.2mm|white
			String sku_str[]=sku_list.split("&");
			for (String s : sku_str) {
				StringBuilder sb=new StringBuilder();
				String []skus=s.split("\\$");
				//客户下单pid
				String c_pid=skus[0];
				//采样pid
				String p_pid=skus[1];
				pidMap.put(p_pid,c_pid);
				//规格信息
				String sku_lists="";
				if(skus.length>=3){
					sku_lists=skus[2];
				}else{
					map_sku.put(skus[0], sb.toString());
					continue;
				}
				System.out.println("skus:"+sku_lists);
				if(StringUtil.isNotBlank(sku_lists) && sku_lists.indexOf("|")>=-1){
					String s_list []=sku_lists.split("\\|");
					for (String s_ : s_list) {
						if(s_.indexOf("_")>=-1){
							sb.append(s_.split("_")[3]).append(":").append(s_.split("_")[1]).append("@").append(s_.split("_")[0]).append(",");
						}
					}
				}
				map_sku.put(p_pid, sb.toString());
				sb.setLength(0);
			}
//			map.put("goods_pids", goods_pids);
//			List<AliInfoDataBean> list_goods=iWarehouseService.getAllGoodsInfos(goods_pids);
			String [] pids=pid_list.split(",");
//			DataSourceSelector.set("dataSource127hop");
			if(pids.length>0){
				//生成采样订单号
				SimpleDateFormat order=new SimpleDateFormat("yyyyMMddHHmmss");
				Date data=new Date();
				//使用时间来生成订单号
				String order_no=order.format(data);
				Orderinfo oi=new Orderinfo();
				oi.setOrderNo(order_no);
				oi.setDetailsNumber(pids.length);
//				row+=iWarehouseService.insertOrderInfo(oi);
				sendMQ.sendMsg(new RunSqlModel("insert into orderinfo (order_no,orderRemark,isDropshipOrder,details_number,create_time,user_id,state,delivery_time,mode_transport,product_cost,pay_price) " +
							"values('"+oi.getOrderNo()+"','采样订单',3,"+oi.getDetailsNumber()+",now(),13653,1,'3-6','Epacket@3-6@USA@all',0,0)"));
				row=1;
				if(row>0){
					StringBuilder sqls=new StringBuilder();
					sqls.append("INSERT INTO order_details (orderid,goodsname,goodsid,car_url,car_img,car_urlMD5,goods_pid,car_type,userid,goodsprice,yourorder,remark) VALUES ");
					//生成order_details
					for (String s:pids) {
						OrderDetailsBean od=new OrderDetailsBean();
						OrderDetailsBean o=iWarehouseService.getOldDetails(pidMap.get(s));
						if(o == null){
							o=iWarehouseService.getCustomBeack(pidMap.get(s));
						}
						od.setOrderid(order_no);
						SimpleDateFormat goodsid=new SimpleDateFormat("ddHHmmss");
						Date data1=new Date();
						Thread.sleep(1000);
						int goodid=Integer.valueOf(goodsid.format(data1));
						od.setGoodsid(goodid);
						od.setGoodsname(o.getGoodsname().replace("'"," "));
						od.setGoods_pid(pidMap.get(s));
						od.setGoods_url("https://detail.1688.com/offer/"+s+".html");
						od.setCar_img(o.getCar_img());
						od.setCar_img(StringUtil.isNotBlank(o.getCar_img())?o.getCar_img():"");
						od.setUserid(13653);
						od.setCar_url("https://www.import-express.com/goodsinfo/cbtconsole-1"+pidMap.get(s)+".html");
						od.setYourorder(1);
						od.setRemark(remarkMap.get(s));
						od.setGoodsprice(o.getGoodsprice());
						if(!StringUtils.isStrNull(map_sku.get(s))){
							od.setCar_type(map_sku.get(s));
						}
						od.setCar_urlMD5(StringUtil.isNotBlank(o.getCar_urlMD5())?o.getCar_urlMD5():"");
						//判断需要生成的order_details是否在库存已有采样商品（非取消）
//						int od_exit=iWarehouseService.getOrderDetailsExit(od.getGoods_pid(),od.getCar_type());
//						if(od_exit>0){
//							continue;
//						}
						od_list.add(od);
						sqls.append(" ('"+od.getOrderid()+"','"+od.getGoodsname()+"','"+od.getGoodsid()+"','"+od.getCar_url()+"','"+od.getCar_img()+"'," +
								"'"+od.getCar_urlMD5()+"','"+od.getGoods_pid()+"','"+od.getCar_type()+"','"+od.getUserid()+"','"+od.getGoodsprice()+"',1,'"+od.getRemark()+"'),");
//						if(mapList.get(a.getAli_catid1())!=null){
//							mapList.put(Integer.valueOf(a.getAli_catid1()), mapList.get(a.getAli_catid1())+1);
//						}else{
//							if(!StringUtils.isStrNull(a.getAli_catid1())){
//								mapList.put(Integer.valueOf(a.getAli_catid1()),1);
//							}
//						}
					}
					if(od_list.size()>0){
//						row+=iWarehouseService.insertOrderDetails(od_list);
						sendMQ.sendMsg(new RunSqlModel(sqls.toString().substring(0,sqls.toString().length()-1)));
						//分配采购
						String adminid=String.valueOf(user.getId());
//						List<OrderDetailsBean> list_od=iWarehouseService.getOrderDetailsByOrderid(order_no);
						for (OrderDetailsBean a : od_list) {
							a.setFreight(adminid);
						}
						iWarehouseService.insertGd(od_list);
						for(int i=0;i<od_list.size();i++){
							//更新本地
							iWarehouseService.updateCrossShopr(od_list.get(i).getGoods_pid());
							sendMQ.sendMsg(new RunSqlModel("update custom_benchmark_ready set samplingStatus=2 where pid='"+od_list.get(i).getGoods_pid()+"'"));
						}
//						if(row==od_list.size()+1){
//							//分配采购
//							String adminid=String.valueOf(user.getId());
//							List<OrderDetailsBean> list_od=iWarehouseService.getOrderDetailsByOrderid(order_no);
//							for (OrderDetailsBean a : list_od) {
//								a.setFreight(adminid);
//							}
//							iWarehouseService.insertGd(list_od);
//							for(int i=0;i<od_list.size();i++){
//								//更新本地
//								iWarehouseService.updateCrossShopr(od_list.get(i).getGoods_pid());
////								sendMQ.sendMsg(new RunSqlModel("update custom_benchmark_ready set samplingStatus=2 where pid='"+od_list.get(i).getGoods_pid()+"'"));
//							}
//						}
					}
					iWarehouseService.updateGdOdid();
				}
			}
		}catch(Exception e){
			LOG.error("createBuyOrder:",e);
			row=0;
		}finally {
//			DataSourceSelector.restore();
		}
		return row+"";
	}
	public static String getAdmId(String cid){
		String admuserid="9";
		if("502".equals(cid) || "7".equals(cid) || "509".equals(cid) || "200003590".equals(cid)){
			admuserid="9";//camry
		}else if("322".equals(cid) || "1524".equals(cid) || "100003070".equals(cid) || "21".equals(cid)){
			admuserid="57";//Jessie
		}else if("1501".equals(cid) || "39".equals(cid) || "18".equals(cid) || "20005194".equals(cid)){
			admuserid="53";//Mindy
		}else if("66".equals(cid) || "34".equals(cid) || "15".equals(cid) || "44".equals(cid)){
			admuserid="51";//小田
		}else if("1509".equals(cid) || "1511".equals(cid) || "100003235".equals(cid) || "17".equals(cid)){
			admuserid="50";//Alisa
		}else if("100003109".equals(cid) || "13".equals(cid) || "26".equals(cid) || "5".equals(cid) || "1503".equals(cid)){
			admuserid="58";//Kyra
		}
		return admuserid;
	}
	/**
	 * 获取mapList最大值
	 * @Title getId
	 * @Description TODO
	 * @param mapList
	 * @return
	 * @return String
	 */
	public static String getId(Map<Integer, Integer> mapList) {
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
		return getAdmId(StringUtils.isStrNull(adminId)?"9":adminId);
	}

	/**产品ID转换UUID
	 * @param itemId 产品ID
	 * @return
	 */
	public static String itemIDToUUID(String itemId){
		if(StringUtils.isStrNull(itemId)){
			return "";
		}
		return "D"+Md5Util.encoder(itemId);
	}

	// 获得原商品链接
	@RequestMapping(value = "/getGoodsCar", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getGoodsCar(HttpServletRequest request,
							  HttpServletResponse response) {
		String Goodsdata_id = request.getParameter("Goodsdata_id");
		String userid = request.getParameter("userid");
		String price = request.getParameter("price");
		String url = request.getParameter("url");
		String orderid = request.getParameter("orderid");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Goodsdata_id", Goodsdata_id);
		map.put("userid", userid);
		map.put("price", price);
		map.put("url", url);
		map.put("orderid", orderid);

		// 线上
		DataSourceSelector.set("dataSource127hop");
		List<Map<String, String>> retMap = iWarehouseService.getGoodsCar(map);
		DataSourceSelector.restore();

		return JSONArray.fromObject(retMap).toString();
	}

	// 获得原商品链接
	@RequestMapping(value = "/getHsCode", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getHsCode(HttpServletRequest request,
							HttpServletResponse response) {
		String productName = request.getParameter("productName");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productName", productName);
		String ret = iWarehouseService.getHsCode(map);
		return ret;
	}

	// 获得包裹信息
	@RequestMapping(value = "/getPackageInfo_bak", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPackageInfo_bak(HttpServletRequest request,
								 HttpServletResponse response) {
		String shipmentno = request.getParameter("shipmentno");
		Map<String, String> map = new HashMap<String, String>();
		map.put("shipmentno", shipmentno);
		ShippingPackage shippingPackage =null;// iWarehouseService.getPackageInfo(map);
		return JSONObject.fromObject(shippingPackage).toString();
	}

	@RequestMapping(value = "/getPackageInfo")
	@ResponseBody
	protected EasyUiJsonResult getPackageInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String, String> map = new HashMap<String, String>();
		String shipmentno = request.getParameter("shipmentno");
		map.put("shipmentno",shipmentno);
		List<ShippingPackage>  shippingPackage = iWarehouseService.getPackageInfo(map);
		json.setTotal(1);
		json.setRows(shippingPackage);
		return json;
	}

	// 获得库位信息
	@RequestMapping(value = "/getOrderLibrary")
	@ResponseBody
	public EasyUiJsonResult getOrderLibrary(HttpServletRequest request,
											HttpServletResponse response) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		EasyUiJsonResult json = new EasyUiJsonResult();
		int page = Integer.parseInt(request.getParameter("page"));
		if (page > 0) {
			page = (page - 1) * 20;
		}
		map.put("page", page);
		List<StorageLocationBean> orders = iWarehouseService.getAllOutboundorder(map);
		for (StorageLocationBean storageLocationBean : orders) {
			storageLocationBean.setOperation("<a target='_blank' href='/cbtconsole/warehouse/getOrderInfoInspection.do?userid=&orderid="+storageLocationBean.getOrderid()+"&day=&pageSize=50&orderstruts=1'>出库审核</a>");
		}
		json.setRows(orders);
		json.setTotal(iWarehouseService.noInspection());
		return json;
	}

	// 获得全到库未全验货订单
	@RequestMapping(value = "/getAllLibrary")
	@ResponseBody
	public EasyUiJsonResult getAllLibrary(HttpServletRequest request, HttpServletResponse response) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<Object, Object> map = new HashMap<Object, Object>();
		int page = Integer.parseInt(request.getParameter("page"));
		if (page > 0) {
			page = (page - 1) * 20;
		}
		map.put("page", page);
		List<StorageLocationBean> orders = iWarehouseService.allLibrary(map);
		for (StorageLocationBean storageLocationBean : orders) {
			storageLocationBean.setOperation("<a target='_blank' href='/cbtconsole/website/location_management.jsp?orderid="+storageLocationBean.getOrderid()+"'>关联验货</a>");
		}
		int allLibrary = iWarehouseService.allLibraryCount().size();// 全部到库未全验货订单
		json.setRows(orders);
		json.setTotal(allLibrary);
		return json;
	}

	// 获得采购订单信息
	@RequestMapping(value = "/getAllBuy", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAllBuy(HttpServletRequest request,
							HttpServletResponse response) {
		List<Tb1688Account> list = new ArrayList<Tb1688Account>();
		list = iWarehouseService.getAllBuy();
		return JSONArray.fromObject(list).toString();
	}

	// 获得采购订单信息
	@RequestMapping(value = "/getPurchaseOrderDetails", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPurchaseOrderDetails(HttpServletRequest request,
										  HttpServletResponse response) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		String data = request.getParameter("data");
		String shipno = request.getParameter("shipno");
		String orderid = request.getParameter("orderid");
		String type = request.getParameter("type");
		String username = request.getParameter("username");
		shipno=StringUtils.isStrNull(shipno)?null:shipno;
		orderid=StringUtils.isStrNull(orderid)?null:orderid;
		if (data.equals("0")) {
			data = null;
		}
		if (type.equals("-1")) {
			type = null;
		}
		if (username.equals("0") || StringUtils.isStrNull(username)) {
			username = null;
		}
		int page = Integer.parseInt(request.getParameter("page"));
		if (page > 0) {
			page = (page - 1) * 20;
		}
		map.put("page", page);
		map.put("data", data);
		map.put("shipno", shipno);
		map.put("orderid", orderid);
		map.put("type", type);
		map.put("username", username);
		List<TaoBaoOrderInfo> orders = iWarehouseService.getPurchaseOrderDetails(map);
		int acount = iWarehouseService.getAllCount(map).size();
		for (int i = 0; i < orders.size(); i++) {
			orders.get(i).setBuycount(acount);
		}
		return JSONArray.fromObject(orders).toString();
	}

	// 获得库位信息
	@RequestMapping(value = "/geOrderState", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String geOrderState(HttpServletRequest request,
							   HttpServletResponse response) {
		try {
			String orderid = request.getParameter("orderid");
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("orderid", orderid);
			List<OrderDetailsBean> list = iWarehouseService
					.getOrderDetailsInfo(map);
			return JSONArray.fromObject(list).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据简写的库位拼装新的库位
	 *
	 * @param barcode
	 *            简写库位
	 * @return
	 */
	public String getBarcode(String barcode) {
		StringBuffer bar = new StringBuffer();
		if (barcode.length() == 5) {
			for (int i = 0; i < barcode.length(); i++) {
				char item = barcode.charAt(i);
				if (i < 2) {
					bar.append(item);
				} else {
					bar.append("-").append(item);
				}
			}
		} else if (barcode.length() == 6) {
			for (int i = 0; i < barcode.length(); i++) {
				char item = barcode.charAt(i);
				if (i < 2) {
					bar.append(item);
				} else if (i == 2 || i == 3) {
					bar.append(item);
				} else {
					bar.append("-").append(item);
				}
				if (i == 1) {
					bar.append("-");
				}
			}
		}
		return bar.toString();
	}

	@RequestMapping(value = "/getStorageInspectionLogInfo")
	@ResponseBody
	public EasyUiJsonResult getStorageInspectionLogInfo(HttpServletRequest request,
														HttpServletResponse response) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		Map<String,String> map=new HashMap<String,String>();
		int page = Integer.parseInt(request.getParameter("page"));
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		startTime=StringUtil.isBlank(startTime)?"2018-01-01 00:00:00":(startTime+" 00:00:00");
		endTime=StringUtil.isBlank(endTime)?"2018-12-31 23:59:59":(endTime+" 23:59:59");
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		map.put("page",String.valueOf(page));
		List<StorageInspectionLogPojo> list=iWarehouseService.getStorageInspectionLogInfo(map);
		json.setRows(list);
		json.setTotal(1);
		return json;
	}

	// 获得库位信息
	@RequestMapping(value = "/getLocationManagementInfo")
	@ResponseBody
	public EasyUiJsonResult getLocationManagementInfo(HttpServletRequest request,
													  HttpServletResponse response) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		try {
			String location_type = request.getParameter("location_type");
			String location = request.getParameter("location");
			String is_empty = request.getParameter("is_empty");
			String orderid = request.getParameter("orderid");
			String barcode = request.getParameter("barcode");
			String shipno=request.getParameter("shipno");
			String pid=request.getParameter("pid");
			if(StringUtil.isBlank(pid)){
				pid = null;
			}
			int page = Integer.parseInt(request.getParameter("page"));
			if (page > 0) {
				page = (page - 1) * 20;
			}
			if (orderid == null || orderid.equals("")) {
				orderid = null;
			}
			if (barcode == null || barcode.equals("")
					|| barcode.equals("如:GS271")) {
				barcode = null;
			} else {
				barcode = getBarcode(barcode);
			}
			if(location_type==null || "".equals(location_type)){
				location_type="-1";
			}
			if(location==null || "".equals(location)){
				location="-1";
			}
			if(is_empty==null || "".equals(is_empty)){
				is_empty="-1";
			}
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("location_type", location_type);
			map.put("location", location);
			map.put("is_empty", is_empty);
			map.put("orderid", orderid);
			map.put("barcode", barcode);
			map.put("page", page);
			map.put("pid", pid);
			int acount = 0;
			int mid = iWarehouseService.getMid();// 中期库位剩余
			int shortTerm = iWarehouseService.getShortTerm();// 短期库位剩余
			List<LocationManagementInfo> list = new ArrayList<LocationManagementInfo>();
			if(StringUtil.isNotBlank(shipno)){
				//根据运单号获取销售订单号
				List<String> order_list=iWarehouseService.getSaleOrderid(shipno);
				if(order_list.size()==1){
					orderid=order_list.get(0);
				}
			}
			if (orderid != null && !"".equals(orderid)) {
				list = iWarehouseService
						.getLocationManagementInfoByOrderid(orderid);
				acount = list.size();
			} else {
				list = iWarehouseService.getLocationManagementInfo(map);
				acount = iWarehouseService.searchCount(map);
			}
			StringBuffer order_nos=new StringBuffer();
			String orders="";
			for (int i = 0; i < list.size(); i++) {
				LocationManagementInfo info = list.get(i);
				// 该库位对应最近的公司订单
				String orderids = "";
				if (orderid != null && !orderid.equals("")) {
					orderids = orderid;
				} else {
					orderids = iWarehouseService.getOrderIdByBarcode(info
							.getBarcode());
				}
				if (orderids != null && !orderids.equals("") && info.getAcount().equals("否")) {
					// 该公司订单对于的淘宝订单和快递号
					LocationManagementInfo info1 = iWarehouseService.getTaoBaoInfos(orderids);
					int amounts = iWarehouseService.getAmounts(orderids);
					int acountss = iWarehouseService.getAcounts(orderids);
					info.setOrderids(orderids);
					info.setShipnos(info1 != null ? info1.getShipnos() : "");
					if(info1 != null && info1.getTborderids().indexOf(",")>-1){
						String tb_orderids[]=info1.getTborderids().split(",");
						StringBuffer bf_tbId=new StringBuffer();
						for(int m=0;m<tb_orderids.length;m++){
							bf_tbId.append(tb_orderids[m]).append(",");
							if(m>0 && (m+1)%3==0){
								bf_tbId.append("<br>");
							}
						}
						info.setTborderids(bf_tbId.toString().length()>0?bf_tbId.toString().substring(0,bf_tbId.toString().length()-1):"");
					}else if(info1 != null){
						info.setTborderids(info1.getTborderids());
					}else{
						info.setTborderids("无");
					}
					info.setAmount(String.valueOf(amounts));
					info.setAcounts(String.valueOf(acountss));
				}
				String StDate="";
				returndisplay re=this.iWarehouseService.FindReturnTime(orderids);
				if (re !=null) {
					 StDate = re.getApplyUser() + "于" + re.getApplyTime() + "退货";
				}

				info.setOperation("<button  onclick=\"resetLocation('"+info.getBarcode()+"','"+info.getShort_term()+"')\">清空库位</button>/<button onclick=\"returnOr('"+info.getOrderids()+"')\">发起退货</button><div id='th"+info.getOrderids()+"'>"+StDate+"</div>");
				order_nos.append("'").append(orderids).append("'").append(",");
				//获取该库位最后一次入库时间、最后一次出库时间、最后一次强制清空时间
				String createtime=iWarehouseService.getCreateTime(info.getBarcode());
				info.setCreatetime(createtime);
			}
			if(order_nos.toString().length()>0){
				orders=order_nos.toString().substring(0, order_nos.toString().length()-1);
			}else{
				orders=order_nos.append("''").toString();
			}
			int noInspection = iWarehouseService.noInspection();
			int allLibrary = iWarehouseService.allLibraryCount().size();// 全部到库未全验货订单
			int stockOrderInfo = iWarehouseService.getStockOrderInfo();// 有待确认使用库存的商品
			request.getSession().setAttribute("mid_barcode", String.valueOf(mid));
			request.getSession().setAttribute("shortTerm", String.valueOf(shortTerm));
			request.getSession().setAttribute("noInspection", String.valueOf(noInspection));
			request.getSession().setAttribute("allLibrary", String.valueOf(allLibrary));
			request.getSession().setAttribute("stockOrderInfo", String.valueOf(stockOrderInfo));
			List<LocationManagementInfo> shipno_List = iWarehouseService.getCheckOrders(orders.toString(),1);
			order_nos.setLength(0);
			List<String> checkedList = new ArrayList<String>();// 包裹已验货
			List<String> noCheckedList = new ArrayList<String>();// 包裹未验货
			List<String> partCheckedList = new ArrayList<String>();// 部分验货
			for (int m = 0; m < shipno_List.size(); m++) {
				LocationManagementInfo info = shipno_List.get(m);
				if (info.getAmount() != null
						&& info.getAmount().equals(info.getAcounts())) {
					checkedList.add(info.getShipnos());
				} else if (info.getAcounts() == null
						|| info.getAcounts().equals("0")) {
					noCheckedList.add(info.getShipnos());
				} else {
					partCheckedList.add(info.getShipnos());
				}
			}
			for (int i = 0; i < list.size(); i++) {
				LocationManagementInfo info = list.get(i);
				StringBuffer bf = new StringBuffer();
				if (info.getOrderids() != null) {
					if (request.getLocalAddr().equals("")) {
					}
					bf.append("<a target='_blank' style='text-decoration:underline' href='/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="
							+ info.getOrderids()
							+ "'>"
							+ info.getOrderids()
							+ "</a>");
					info.setOrderids(bf.toString());
				}
				bf.setLength(0);
				if (info.getShipnos() != null && info.getShipnos().contains(",")) {
					String nos[] = info.getShipnos().split(",");
					for (int j = 0; j < nos.length; j++) {
						if (checkedList.size() > 0
								&& checkedList.contains(nos[j])) {
							bf.append(","
									+ "<a target='_blank' style='color:green;text-decoration:underline' title='"+nos[j]+"' href='/cbtconsole/website/inspection.jsp?shipno="
									+ nos[j] + "&barcode=" + info.getBarcode()
									+ "'>" + nos[j]
									+ "</a>(包裹已验货)");
						} else if (noCheckedList.size() > 0
								&& noCheckedList.contains(nos[j])) {
							bf.append(","
									+ "<a target='_blank' style='color:red;text-decoration:underline' title='"+nos[j]+"' href='/cbtconsole/website/inspection.jsp?shipno="
									+ nos[j] + "&barcode=" + info.getBarcode()
									+ "'>" + nos[j]
									+ "</a>(包裹未验货)");
						} else {
							bf.append(","
									+ "<a target='_blank' style='color:black;text-decoration:underline' title='"+nos[j]+"' href='/cbtconsole/website/inspection.jsp?shipno="
									+ nos[j] + "&barcode=" + info.getBarcode()
									+ "'>" + nos[j]
									+ "</a>");
						}
						if(j>0 && (j+1)%3==0){
							bf.append("<br>");
						}
					}
					info.setShipnos(bf.substring(1, bf.length()).toString());
				} else if (info.getShipnos() != null) {
					if (checkedList.size() > 0 && checkedList.contains(info.getShipnos())) {
						bf.append("<a target='_blank' style='color:green;text-decoration:underline' title='"+info.getShipnos()+"' href='/cbtconsole/website/inspection.jsp?shipno="
								+ info.getShipnos()
								+ "&barcode="
								+ info.getBarcode()
								+ "'>"
								+ info.getShipnos() + "</a>(包裹已验货)");
					} else if (noCheckedList.size() > 0 && noCheckedList.contains(info.getShipnos())) {
						bf.append("<a target='_blank' style='color:red;text-decoration:underline' title='"+info.getShipnos()+"' href='/cbtconsole/website/inspection.jsp?shipno="
								+ info.getShipnos()
								+ "&barcode="
								+ info.getBarcode()
								+ "'>"
								+ info.getShipnos() + "</a>(包裹未验货)");
					} else {
						bf.append("<a target='_blank' style='color:black;text-decoration:underline' title='"+info.getShipnos()+"' href='/cbtconsole/website/inspection.jsp?shipno="
								+ info.getShipnos()
								+ "&barcode="
								+ info.getBarcode()
								+ "'>"
								+ info.getShipnos() + "</a>");
					}
					info.setShipnos(bf.toString());
				}
//				info.setNoInspection(noInspection);
//				info.setAllLibrary(allLibrary);
//				info.setStockOrderInfo(stockOrderInfo);
				bf.setLength(0);
				String amount = info.getAmount();
				String acounts = info.getAcounts();
				if (acounts != null && !amount.equals("0")
						&& amount.equals(acounts)) {
					info.setState("全部已验货-待出库");
				} else if (acounts == null) {
					info.setState("");
				} else if (acounts != null && acounts.equals("0")) {
					info.setState("全部未验货-待验货");
				} else if (acounts != null && !acounts.equals("0")
						&& !amount.equals(acounts)) {
					info.setState("部分验货");
				}
				info.setId(acount);
			}
			json.setRows(list);
			json.setTotal(list.size()>0?list.get(0).getId():0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	// 获得库位清空记录
	@RequestMapping(value = "/getLocationTracking")
	@ResponseBody
	public EasyUiJsonResult getLocationTracking(HttpServletRequest request,
												HttpServletResponse response) {
		EasyUiJsonResult json = new EasyUiJsonResult();
		int page = Integer.parseInt(request.getParameter("page"));
		if (page > 0) {
			page = (page - 1) * 20;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("page", page);
		List<LocationTracking> list = iWarehouseService.getLocationTracking(map);
		int acount = iWarehouseService.searchCount1();
		json.setRows(list);
		json.setTotal(acount);
		return json;
	}

	/**
	 * 1688订单退货状态更改
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateTbState", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateTbState(HttpServletRequest request,
	                           HttpServletResponse response) {
		String admJson = Redis.hget(request.getSession().getId(), "admuser");// 获取登录用户
		Admuser user = (Admuser) SerializeUtil
				.JsonToObj(admJson, Admuser.class);
		String orderid = request.getParameter("orderid");
		String sku = request.getParameter("sku");
		String remark = request.getParameter("remark");
		String old_remark = request.getParameter("old_remark");
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtil.isBlank(old_remark)) {
			old_remark = "";
		}else{
			remark=remark+"<br>"+old_remark;
		}
		map.put("orderid", orderid);
		map.put("sku", sku);
		map.put("remark", remark);
		int row = iWarehouseService.updateTbState(map);
		return "" + row;
	}

	@RequestMapping(value = "/insertRemark", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String insertRemark(HttpServletRequest request,
							   HttpServletResponse response) {
		String admJson = Redis.hget(request.getSession().getId(), "admuser");// 获取登录用户
		Admuser user = (Admuser) SerializeUtil
				.JsonToObj(admJson, Admuser.class);
		String orderid = request.getParameter("orderid");
		String sku = request.getParameter("sku");
		String remark = request.getParameter("remark");
		String old_remark = request.getParameter("old_remark");
		Map<String, String> map = new HashMap<String, String>();
		if (old_remark == null || "".equals(old_remark)) {
			old_remark = "";
		}
		remark = old_remark + "</br>" + user.getAdmName() + ":" + remark;
		map.put("orderid", orderid);
		map.put("sku", sku);
		map.put("remark", remark);
		int row = iWarehouseService.insertRemark(map);
		return "" + row;
	}

	@RequestMapping(value = "/updateState", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateState(HttpServletRequest request,
							  HttpServletResponse response) {
		String orderid = request.getParameter("orderid");
		String goodsid = request.getParameter("goodsid");
		int row1=0;
		int row2 = 0;
		int row=0;
		try{
			SendMQ sendMQ = new SendMQ();
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderid", orderid);
			map.put("goodsid", goodsid);
			row = iWarehouseService.updateState(map);
			row1 = iWarehouseService.queryOrderState(map);
			if (row1 == 0) {
				row2 = iWarehouseService.updateOrderState(map);
			}
			sendMQ.sendMsg(new RunSqlModel("update order_details set state=1,checked=1 where orderid='"+orderid+"' and goodsid='"+goodsid+"'"));
			DataSourceSelector.set("dataSource127hop");
			iWarehouseService.queryOrderState(map);
			if (row1 == 0) {
				row2 =1;
				// iWarehouseService.updateOrderState(map);
				sendMQ.sendMsg(new RunSqlModel("update orderinfo set state=2 where order_no='"+orderid+"'"));
			}
			DataSourceSelector.restore();
		}catch (Exception e){
			e.printStackTrace();
		}
		System.out.println(row + "-" + row1 + "-" + row2);
		return "" + row;
	}

	@RequestMapping(value = "/updateAllDetailsState", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateAllDetailsState(HttpServletRequest request, HttpServletResponse response) {
		String orderid = request.getParameter("orderid");
		String state = request.getParameter("state");
		int row = 0;
		try{
			SendMQ sendMQ = new SendMQ();
			if (state.equals("0")) {
				return "0";
			} else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderid", orderid);
				if (state.equals("2")) {
					map.put("order_details_state", "1");
					map.put("order_state", "2");
					map.put("source_state", "4");
				} else if (state.equals("1")) {
					map.put("order_details_state", "0");
					map.put("order_state", "1");
					map.put("source_state", "3");
				} else if (state.equals("3")) {
					map.put("order_details_state", "1");
					map.put("order_state", "3");
					map.put("source_state", "4");
				} else if (state.equals("5")) {
					map.put("order_details_state", "0");
					map.put("order_state", "5");
					map.put("source_state", "0");
				}
				row = iWarehouseService.updateAllDetailsState(map);
				row =1;
				sendMQ.sendMsg(new RunSqlModel("update order_details set state='"+map.get("order_details_state")+"',checked='"+map.get("order_details_state")+"' where orderid='"+map.get("orderid")+"'"));
				sendMQ.sendMsg(new RunSqlModel("update orderinfo set state='"+map.get("order_state")+"' where order_no='"+map.get("orderid")+"'"));
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return "" + row;
	}

	/**
	 * 用户黑名单单利更新状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateFlag", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateFlag(HttpServletRequest request, HttpServletResponse response) {
		int row=0;
		String id=request.getParameter("id");
		String type=request.getParameter("type");
		try{
			row=iWarehouseService.updateFlag(id,type);
		}catch (Exception e){
			e.printStackTrace();
		}
		return row+"";
	}

	@RequestMapping(value = "/addBackUser", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addBackUser(HttpServletRequest request, HttpServletResponse response) {
		int row=0;
		String blackVlue=request.getParameter("blackVlue");
		String type=request.getParameter("type");
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
		try{
			if(adm == null){
				return "0";
			}
			row=iWarehouseService.addBackUser(blackVlue,type,adm.getAdmName());
		}catch (Exception e){
			e.printStackTrace();
		}
		return row+"";
	}

	@RequestMapping(value = "/updatebackEmail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updatebackEmail(HttpServletRequest request, HttpServletResponse response) {
		int row=0;
		String id=request.getParameter("id");
		String newBlackVlue=request.getParameter("newBlackVlue");
		String type=request.getParameter("type");
		try{
			row=iWarehouseService.updatebackEmail(id,newBlackVlue,type);
		}catch (Exception e){
			e.printStackTrace();
		}
		return row+"";
	}


	// 重置库位
	@RequestMapping(value = "/resetLocation", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String resetLocation(HttpServletRequest request,
								HttpServletResponse response) {
		String barcode = request.getParameter("barcode");
		String short_term = request.getParameter("short_term");
		Map<String, String> map = new HashMap<String, String>();
		map.put("barcode", barcode);
		int row = iWarehouseService.resetLocation(map);
		map.put("short_term", short_term);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String time = sdf.format(date);
		map.put("createtime", time);
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
		map.put("admuser",new OrderDetailsServiceImpl().getUserName(user.getId()));
		System.out.println(user.getId());
		if (row > 0) {
			iWarehouseService.inertLocationTracking(map);
		}
		return row + "";
	}

	/**
	 * 查看申报注意事项
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/openTipInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String openTipInfo(HttpServletRequest request, Model model) {
		String orderid = request.getParameter("orderid");
		List<CustomsRegulationsPojo> list=new ArrayList<CustomsRegulationsPojo>();
		CustomsRegulationsPojo c=iWarehouseService.getCustomsRegulationsPojo(orderid);
		list.add(c);
		return JSONArray.fromObject(list).toString();
	}

	public static String LOCALHOST; //本地服务器地址

	// 获得包裹列表
	@RequestMapping(value = "/getPackageInfoList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public String getPackageInfoList(HttpServletRequest request,
									 HttpServletResponse response) {
	    if (LOCALHOST == null) {
            LOCALHOST = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        }
		DecimalFormat df = new DecimalFormat("#.00");
		Map<String, String> map = new HashMap<String, String>();
		List<ShippingPackage> list = iWarehouseService.getPackageInfoList(map);
		for (int i = 0; i < list.size(); i++) {
			ShippingPackage s=list.get(i);
			//获取预估运费 原飞航
			String svolume=s.getSvolume();
			double vol=0.00;
			if(svolume.contains("*") && svolume.split("\\*").length>2){
				String [] t=svolume.split("\\*");
				vol = (Double.parseDouble(t[0])*0.01)*(Double.parseDouble(t[1])*0.01)*(Double.parseDouble(t[2])*0.01);
			}
			Map freightFeeMap = freightFeeSerive.getFreightFee(Double.parseDouble(s.getSweight()), vol, s.getZid(), "原飞航", s.getFedexie(), svolume);
			double freightFee=Double.valueOf(freightFeeMap.get("freightFee").toString());
			freightFee=freightFee/s.getExchange_rate();
			freightFee=Double.parseDouble(df.format(freightFee));
			s.setYfhFreight(freightFee);
			//获取预估运费 佳成
			freightFeeMap = freightFeeSerive.getFreightFee(Double.parseDouble(s.getSweight()), vol, s.getZid(), "JCEX", s.getFedexie(), svolume);
			freightFee=Double.valueOf(freightFeeMap.get("freightFee").toString());
			freightFee=freightFee/s.getExchange_rate();
			freightFee=Double.parseDouble(df.format(freightFee));
			s.setJcexFreight(freightFee);
			list.get(i).setEstimatefreight(df.format(Double.valueOf(s.getEstimatefreight())*s.getExchange_rate()));
			String orderid = list.get(i).getOrderid();
			s.setSumcgprice(String.valueOf(Double.parseDouble(StringUtil.isBlank(s.getSumcgprice())?"0.00":s.getSumcgprice())+s.getPid_amount()));
			PurchaseServer purchaseServer = new PurchaseServerImpl();
			// UserOrderDetails uod =
			// purchaseServer.getUserDetails(orderid+",");
			UserOrderDetails uod;
			// 客户付的钱-采购金额-预估运费 ≥-20元 ， 提示 警告
			String str_sumprice=list.get(i).getSumprice();
			if( str_sumprice==null || "".equals(str_sumprice) )
			{
				str_sumprice="0";
			}
			list.get(i).setSubAmount(Double.valueOf(str_sumprice) * list.get(i).getExchange_rate()
					- Double.valueOf(list.get(i).getSumcgprice()) - Double.valueOf(list.get(i).getEstimatefreight()));
			try {
				uod = purchaseServer.getUserAddr(orderid);
				list.get(i).setUod(uod);
				// 所有图片
				String goodsimg = list.get(i).getGoogs_img();

				List<String> tempList = new ArrayList<String>();
				if (goodsimg != null && !"".equals(goodsimg)) {
					if (goodsimg.indexOf(",") != -1) {
						String tempArray[] = goodsimg.split(",");
						for (int j = 0; j < tempArray.length; j++) {
							tempList.add(tempArray[j]);
						}
					} else {
						tempList.add(goodsimg);
					}
				}
				list.get(i).setListImg(tempList);

				// 订单支付时间距离现在超过十天的
				String orderPayTime = list.get(i).getOrderpaytime();
				if(orderPayTime==null || "".equals(orderPayTime)){
					continue;
				}
				int moreTime = Utility.daysBetween(sdf.parse(orderPayTime),new Date());
				if (moreTime >= 10) {
					list.get(i).setOverTimeFlag(1);
				}

				// 国家提醒 orderid yigo /  Guam/ hawaii/Honolulu/Puerto Rico/Virgin Islands/Samoa/Mariana Islands
                list.get(i).setCountryMsg(orderinfoService.checkCountryMsg(orderid));

			} catch (Exception e) {

				LOG.error("包裹列变获取用户地址信息出错：订单号:【" + orderid + "】", e);
			}

		}

		Map<String, String> map2 = new HashMap<String, String>();
		List<SbxxPojo> sbxxlist = iWarehouseService.getSbxxList(map2);

		request.setAttribute("list", list);
		request.setAttribute("sbxxlist", sbxxlist);
		return "packagelist";
	}

	// 查询到货情况
	@RequestMapping("/selectInstorage")
	public void selectInstorage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int state = Integer.parseInt(request.getParameter("state"));
		int page = Integer.parseInt(request.getParameter("page"));
		int count = 0;
		Mabangshipment mb = new Mabangshipment();
		mb.setState(state);
		mb.setPage(page * 20 - 20);
		List<Mabangshipment> list = mabangshipmentService.selectInstorage(mb);
		count = mabangshipmentService.selectInstorageCount(mb);
		if (list != null && list.size() != 0) {
			if (count % 20 == 0) {
				list.get(0).setPage(count / 20);
			} else {
				list.get(0).setPage(count / 20 + 1);
			}
			list.get(0).setCount(count);
		}
		JSONArray json = JSONArray.fromObject(list);
		PrintWriter out = response.getWriter();
		out.write(json.toString());
		out.close();
	}

	// 查询采购成本
	@RequestMapping("/selectPurchaseCost")
	public void selectPurchaseCost(HttpServletRequest request,
								   HttpServletResponse response) throws IOException {
		String start_time = request.getParameter("start_date");
		String end_time = request.getParameter("end_date");
		Mabangshipment mb = new Mabangshipment();
		mb.setStart_time(start_time);
		mb.setEnd_time(end_time);
		List<Mabangshipment> list = mabangshipmentService
				.selectPurchaseCost(mb);
		JSONArray json = JSONArray.fromObject(list);
		PrintWriter out = response.getWriter();
		out.write(json.toString());
		out.close();
	}

	@RequestMapping(value = "/saveALl", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveALl(HttpServletRequest request, Model model,
                          @RequestBody Map<String, Object> mainMap) {
		List<Map<String, String>> listmap = (List<Map<String, String>>) mainMap
				.get("listmap");

		if(listmap == null || listmap.size() == 0){
			return "0";
		} else {
			int count = 0;
			for(Map<String, String> map : listmap){
				if(org.apache.commons.lang3.StringUtils.isBlank(map.get("shipmentno"))){
					count ++;
					break;
				}
			}
			if(count > 0){
				return "0";
			}
		}
		int ret = iWarehouseService.batchUpdateShippingPackage(listmap);

		// 异步更新线上自动测量
		batchUpdateShippingPackageThread thread = new batchUpdateShippingPackageThread(
				listmap);
		thread.start();
		return ret + "";
	}

	@RequestMapping(value = "/getPackagelist", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPackagelist(HttpServletRequest request, Model model,
                                 @RequestBody Map<String, Object> mainMap) {
		List<Map<String, String>> listmap = (List<Map<String, String>>) mainMap
				.get("listmap");
		int ret = iWarehouseService.batchUpdateShippingPackage(listmap);
		return ret + "";
	}

	@RequestMapping(value = "/getSPById", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getShippingPackageById(HttpServletRequest request) {
		String orderid = request.getParameter("orderid");

		Map<String, String> map = new HashMap<String, String>();
		map.put("orderid", orderid);
		List<ShippingPackage> list = iWarehouseService
				.getShippingPackageById(map);
		return JSONArray.fromObject(list).toString();
	}

	// 插入申报信息

	@RequestMapping(value = "/insertSbxx", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String insertSbxx(HttpServletRequest request) {
		String zwpm = request.getParameter("zwpm");
		String ywpm = request.getParameter("ywpm");
		String px = request.getParameter("px");

		Map<String, String> map = new HashMap<String, String>();
		map.put("zwpm", zwpm);
		map.put("ywpm", ywpm);
		map.put("px", px);

		int ret = iWarehouseService.insertSbxx(map);
		return ret + "";
	}

	@RequestMapping(value = "/updateALlpak", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateALlpak(HttpServletRequest request, Model model,
                               @RequestBody Map<String, Object> mainMap) {

		// 未支付费用
		String tStr = (String) mainMap.get("unpaidFreight");
		// 获取出货信息
		String orderid = (String) mainMap.get("orderid");
		PurchaseServer purchaseServer = new PurchaseServerImpl();
		UserOrderDetails uod = purchaseServer.getUserDetails(orderid + ",");
		tStr = mainMap.get("trans_method").toString(); // 出货方式
		// 设置国家
		Map<String, Object> map = setMapData(mainMap);
		// 申报信息添加
		if (tStr.equals("1") || tStr.equals("3") || tStr.equals("4")) { // 4px
			// 佳成
			List<Map<String, Object>> listmap = (List<Map<String, Object>>) mainMap
					.get("listmap");
			List<ProductBean> productList = new ArrayList<ProductBean>();
			for (int i = 0; i < listmap.size(); i++) {
				ProductBean pbean = new ProductBean();
				pbean.setProductname(listmap.get(i).get("sbzwpm").toString());
				pbean.setProducenglishtname(listmap.get(i).get("sbywpm")
						.toString());
				pbean.setProductremark(listmap.get(i).get("sbphbz").toString());
				pbean.setProductnum(listmap.get(i).get("sbsl").toString());
				pbean.setProductprice(listmap.get(i).get("sbjg").toString());
				pbean.setProductcurreny(listmap.get(i).get("sbdw").toString());
				productList.add(pbean);

				// 保存申报信息
				Map<String, Object> declares = new HashMap<String, Object>(); // sql
				// 参数
				declares.put("productname", pbean.getProductname());
				declares.put("producenglishtname",
						pbean.getProducenglishtname());
				declares.put("productnum", pbean.getProductnum());
				declares.put("productprice", pbean.getProductprice());
				declares.put("productcurreny", pbean.getProductcurreny());
				declares.put("productremark", pbean.getProductremark());
				declares.put("orderid", mainMap.get("orderid"));
				;
				// insertDeclareinfo
				// int t = iWarehouseService.updateOrderFeeByOrderid(map);

				// iWarehouseService.insertDeclareinfo(declares);

			}
			uod.setProductBean(productList);
		}
		List<Map<String, Object>> listmain = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> listmap2 = (List<Map<String, Object>>) mainMap
				.get("listmap2");
		List<ProductBean> productList = new ArrayList<ProductBean>();
		for (int i = 0; i < listmap2.size(); i++) {
			Map<String, Object> m = new HashMap<String, Object>();
			m = map;
			String shipmentno = listmap2.get(i).get("shipmentno").toString();
			String sweight = listmap2.get(i).get("sweight").toString();
			String svolume = listmap2.get(i).get("svolume").toString();

			m.put("shipmentno", shipmentno);
			m.put("sweight", sweight);
			m.put("svolume", svolume);

			listmain.add(m);
		}

		return "";
	}

	public String getGsType(String t) {

		if ("4px".equals(t)) {
			return "1";
		} else if ("原飞航".equals(t)) {
			return "2";
		} else if ("JCEX".equals(t)) {
			return "3";
		} else if("zto".equals(t)){ // 中通
			return "5";
		} else if("飞特".equals(t)){ // 飞特
			return "7";
		} else { // 邮政
			return "4";
		}
	}

//	@RequestMapping(value = "/batchCk", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public static String batchCk(HttpServletRequest request, Model model,
//						  @RequestBody Map<String, Object> mainMap) {
//		String  strs=outFte(null);
//		return strs;
//
//	}
@RequestMapping(value = "/testSendEmail")
	public String testSendEmail(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> model = new HashedMap();
		StringBuilder msg=new StringBuilder();
		String chatText= msg.toString();
		model.put("chatText",chatText);
		model.put("name","919923437@qq.com");
		model.put("email","919923437@qq.com");
	    model.put("orderid","123456788");
	    model.put("recipients","Jones Drugs");
		model.put("street","959 East Main St");
		model.put("street1","");
		model.put("city","Prattville");
		model.put("state","Alabama");
		model.put("country","UAS");
		model.put("zipCode","36066");
		model.put("phone","3343581630");
		sendMailFactory.sendMail(String.valueOf(model.get("email")), null, "Shipping Notification - Import Express", model, TemplateType.BATCK);
		return "1";
	}

	@RequestMapping(value = "/batchCk", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String batchCk(HttpServletRequest request, Model model, @RequestBody Map<String, Object> mainMap) {
		JcgjSoapHttpPost jc = new JcgjSoapHttpPost();
		List<Map<String, String>> bgList = (List<Map<String, String>>) mainMap.get("bgList");
		List<Map<String, Object>> sbxxList = (List<Map<String, Object>>) mainMap.get("sbxxList");
		String WebSite= request.getParameter("WebSite");
		int cont = sbxxList.size();
		List<String> removeId = new ArrayList<String>();
		try{
			for (int i = 0; i < cont; i++) {
				Map<String, Object> declares = sbxxList.get(i);
				String orderid = (String) declares.get("orderid");
				// 封装包裹信息
				List<ProductBean> lpb=getProductBean(declares);
				String expressNo = "";
				String pdfUrl="";
				int j = i;
				Map<String, String> bgMap = bgList.get(j);
				if (StringUtil.isNotBlank(orderid) && orderid.equals((String) bgMap.get("orderid"))) {
					// 运输公司
					String tStr = getGsType((String) bgMap.get("transportcompany"));
					// 获取数据
					PurchaseServer purchaseServer = new PurchaseServerImpl();
					UserOrderDetails uod = purchaseServer.getUserDetails(orderid + ",");
					//将数据保存到UserOrderDetails中
					saveUod(lpb, bgMap, uod);
					String number = "";
					if ("3".equals(tStr)) {
						number = jc.callGetNumber(uod.getUserzone(), "887799");
						LOG.info("佳成运单号================" + number);
						//校验运单数据是否合法
						int ri = Integer.parseInt(outJc(uod, number));
						expressNo = number;
						if (ri != 10000){
							removeId.add(bgList.get(j).get("shipmentno"));
							continue;
						}
					}else if("7".equals(tStr)){
						Map<String,String> map=getFTData(orderid,declares,uod);
						String  strs=outFte(map);
						expressNo=strs;
					}else if("5".equals(tStr)){
						Map<String,Object> map=getZtoData(orderid,declares,uod);
						String [] strs=outZto(map);
						expressNo=strs[0];
						pdfUrl=strs[1];
					}else if ("4".equals(tStr)) {
						List<RecList> lr = new ArrayList<RecList>();
						List<GoodsPojo> list = new ArrayList<GoodsPojo>();
						// 申报信息
						List<ProductBean> proBeanList = new ArrayList<ProductBean>();
						proBeanList = uod.getProductBean();
						int ppp = proBeanList.size();
						StringBuilder en_name=new StringBuilder();
						StringBuilder cn_name=new StringBuilder();
						getGoodsPojoInfo(list, proBeanList, ppp, en_name, cn_name);
						RecList r = new RecList();
						r.setiID("0");
						r.setnItemType("1");
						String orderidTemp = uod.getOrderno();
						if (orderid.indexOf("-1") != -1) {
							orderidTemp = orderidTemp.substring(0,orderidTemp.indexOf("-1"));
						}
						lr=getRecList(bgMap, uod, lr, list, en_name, cn_name, r, orderidTemp);
						Map<String, Object> mapR = new HashMap<String, Object>();
						mapR.put("RequestName", "PreInputSet");
						mapR.put("icID", 14972);
						String nowTime = null;
						nowTime = getNowTime(jc, nowTime);
						String md5 =JcgjSoapHttpPost.string2MD5("14972" + nowTime+ "Yb70MrMh9Q4Odl5");
						mapR.put("TimeStamp", nowTime);
						mapR.put("MD5", md5);
						mapR.put("RecList", lr);
						String t2 = jc.objToGson(mapR);
						LOG.info("==============t2==================="+t2);
						//调用CNE的API
						String ret = jc.preInputSet(t2);
						JSONObject json = JSONObject.fromObject(ret);
						if(!json.toString().contains("请及时付款")){
							List<Map<String, Object>> edit = (List<Map<String, Object>>) json.getJSONArray("ErrList");
							int iID = (Integer) edit.get(0).get("iID");
							if (iID == 0) {
								LOG.info("【订单号:" + orderid+ "】 对接CNE获取运单号有误  -----"+ edit.get(0).get("cMess").toString());
								removeId.add(bgList.get(j).get("shipmentno"));
								continue;
							}
							//CNE包裹号
							String cNum = (String) edit.get(0).get("cNum");
							expressNo = cNum;
						}
					}
					bgMap.put("expressno", expressNo); // 添加包裹号
					bgMap.put("pdfUrl", pdfUrl); // 添加包裹号
				}
			}
			//发送邮件给客户提示发货
			for (int i = 0; i < cont; i++) {
				Map<String, Object> declares = sbxxList.get(i);
				String orderid = (String) declares.get("orderid");
				//发送邮件给客户告知已经发货
				IGuestBookService ibs = new GuestBookServiceImpl();
				OrderBean ob=iWarehouseService.getUserOrderInfoByOrderNo(orderid);
				Map<String,Object> modelM = new HashedMap();
				modelM.put("name",ob.getEmail());
				modelM.put("orderid",orderid);
				modelM.put("recipients",ob.getRecipients());
				if(org.apache.commons.lang3.StringUtils.isNotBlank(ob.getAddresss())){
					modelM.put("street",ob.getAddresss() + " " + ob.getStreet());
				}else{
					modelM.put("street",ob.getStreet());
				}
				modelM.put("street1","");
				modelM.put("city",ob.getAddress2());
				modelM.put("state",ob.getStatename());
				modelM.put("country",ob.getCountry());
				modelM.put("zipCode",ob.getZipcode());
				modelM.put("phone",ob.getPhonenumber());
				modelM.put("toHref","https://www.import-express.com/apa/tracking.html?loginflag=false&orderNo="+orderid+"");
				String temp="";

				if (MultiSiteUtil.getSiteTypeNum(orderid)==1){
					modelM.put("websiteType",1);
					sendMailFactory.sendMail(String.valueOf(modelM.get("name")), null, "Order delivery notice", modelM, TemplateType.BATCK);
				}
				if (MultiSiteUtil.getSiteTypeNum(orderid)==2){
					modelM.put("websiteType",2);
					modelM.put("toHref","https://www.kidsproductwholesale.com/apa/tracking.html?loginflag=false&orderNo="+orderid+"");
					sendMailFactory.sendMail(String.valueOf(modelM.get("name")), null, "Order delivery notice", modelM, TemplateType.BATCK_KIDS);
				}
				if (MultiSiteUtil.getSiteTypeNum(orderid)==3){
					modelM.put("websiteType",3);
					modelM.put("toHref","https://www.lovelypetsupply.com/apa/tracking.html?loginflag=false&orderNo="+orderid+"");
					sendMailFactory.sendMail(String.valueOf(modelM.get("name")), null, "Order delivery notice", modelM, TemplateType.BATCK_PET);
				}

			}
		}catch (Exception e){
			e.printStackTrace();
		}
		// 更新订单状态
		updateOrderState(bgList, sbxxList, cont);
		// 存申报信息
		saveDeclareInfo(sbxxList);
		// 修改包裹信息
		int ret = 0;
		ret = updateBgState(bgList, ret);
		return String.valueOf(ret);
	}



	private void saveUod(List<ProductBean> lpb, Map<String, String> bgMap, UserOrderDetails uod) {
		uod.setShipmentno((String) bgMap.get("shipmentno"));
		// 设置国家
		uod.setUserzone((String) bgMap.get("fpxCountryCode"));
		uod.setOutmethod("0");
		uod.setUsercity(uod.getAddress2());
		if (StrUtils.isNotNullEmpty(uod.getUserstreet())) {
			// 判断address 和 street 是否一致,若一致 取其一; 若不一致 取其和
			if (uod.getUserstreet().equals(uod.getAddress())) {
				uod.setUserstreet(uod.getAddress());
			} else {
				uod.setUserstreet(uod.getUserstreet()+ uod.getAddress());
			}
		} else {
			uod.setUserstreet(uod.getAddress());
		}
		uod.setUserstate(uod.getStatename());
		uod.setUsercode(uod.getZipcode());
		uod.setUserphone(uod.getPhone());
		uod.setTransport((String) bgMap.get("fpxProductCode"));
		uod.setProductBean(lpb);
		uod.setWeight((String) bgMap.get("sweight"));
		uod.setGoodstype("PAK");
	}

	private List<ProductBean> getProductBean(Map<String, Object> declares) {
		List<ProductBean> lpb = new ArrayList<ProductBean>();
		ProductBean pb = new ProductBean();
		pb.setProducenglishtname((String) declares.get("producenglishtname"));
		pb.setProductcurreny((String) declares.get("productcurreny"));
		pb.setProductname((String) declares.get("productname"));
		pb.setProductnum((String) declares.get("productnum"));
		pb.setProductprice((String) declares.get("productprice"));
		pb.setProductremark((String) declares.get("productremark"));
		lpb.add(pb);
		return lpb;
	}

	private void getGoodsPojoInfo(List<GoodsPojo> list, List<ProductBean> proBeanList, int ppp, StringBuilder en_name, StringBuilder cn_name) {
		for (int p = 0; p < ppp; p++) {
			// 申报信息
			GoodsPojo gl = new GoodsPojo();
			gl.setCxGoods(proBeanList.get(p).getProductname()); // 物品描述,0-63字符。必须。
			gl.setIxQuantity(proBeanList.get(p).getProductnum()); // 物品数量。必须。
			gl.setFxPrice(proBeanList.get(p).getProductprice()); // 物品单价，2位小数。
			gl.setCxGoodsA(proBeanList.get(p).getProducenglishtname()); // 物品英文描述,0-63字符。
			gl.setcGoods(proBeanList.get(p).getProductname());
			gl.setcGoodsA(proBeanList.get(p).getProducenglishtname());
			list.add(gl);
			en_name.append(proBeanList.get(p).getProductname()).append(",");
			cn_name.append(proBeanList.get(p).getProducenglishtname()).append(",");
		}
	}

	private int updateBgState(List<Map<String, String>> bgList, int ret) {
		if (bgList.size() > 0) {
			ret = iWarehouseService.bgUpdate(bgList);
			// 异步线程 更新线上数据
			batchCKThread thread = new batchCKThread(bgList);
			thread.start();
		}
		return ret;
	}

	private void saveDeclareInfo(List<Map<String, Object>> sbxxList) {
		try {
			for (int i = 0; i < sbxxList.size(); i++) {
				Map<String, Object> declares = sbxxList.get(i);
				iWarehouseService.insertDeclareinfo(declares);
			}
		} catch (Exception e) {
			LOG.info("插入申报信息,更新库存表失败");
		}
	}

	/**
	 * 更新订单状态
	 * @param bgList
	 * @param sbxxList
	 * @param cont
	 */
	private void updateOrderState(List<Map<String, String>> bgList, List<Map<String, Object>> sbxxList, int cont) {
		for (int i = 0; i < cont; i++) {
			Map<String, Object> declares = sbxxList.get(i);
			String orderid = (String) declares.get("orderid");
			Map<String, String> bgMap = bgList.get(i);
			String isDropshipFlag = bgMap.get("isDropshipFlag");
			if (orderid != null && !"".equals(orderid)) {
				if (orderid.equals((String) bgMap.get("orderid"))) {
					// 出库更新表状态
					OutPortNow1(orderid, isDropshipFlag);
					// 更新库位状态为空闲
					try {
						iWarehouseService.updateBarcodeByOrderNo(orderid);
						// 更新货源变商品状态->改为出运中
						iWarehouseService.updateOrderSourceState(orderid);
						//发货商品authorized_flag标记为2，商品发货
						iWarehouseService.checkAuthorizedFlag(orderid);
					} catch (Exception e) {
						LOG.error("更新orderid 的库位状态/货源表商品状态【订单号:" + orderid+ "】", e);
					}
				}
			}
			try{
				//插入出库记录 王宏杰 2018-06-20
				iWarehouseService.insertGoodsInventory(orderid);
			}catch (Exception e){
				LOG.error("插入出库明细失败【"+orderid+"】", e);
			}
		}
	}

	private String getNowTime(JcgjSoapHttpPost jc, String nowTime) {
		try {
			nowTime = jc.preInputSet("{\"RequestName\":\"TimeStamp\"}");
			nowTime = nowTime.substring(nowTime.indexOf(":") + 1,nowTime.length() - 1);
			System.out.println(nowTime);
		} catch (Exception e1) {
			LOG.info("==============获取邮政出货单号失败===================");
		}
		return nowTime;
	}

	private List<RecList> getRecList(Map<String, String> bgMap, UserOrderDetails uod, List<RecList> lr, List<GoodsPojo> list, StringBuilder en_name, StringBuilder cn_name, RecList r, String orderidTemp) {
		r.setcRNo(orderidTemp); // (传用户系统订单号，只允许数字和字母，中划线，其他符号不接受）
		r.setcDes(uod.getUserzone()); // 收件国家名
		r.setcGoods(en_name.toString());
		r.setcGoodsA(cn_name.toString());
		r.setfWeight(uod.getWeight()); // 重量，公斤，3位小数
		r.setcReceiver(uod.getRecipients() == null ? uod.getUserName() : uod.getRecipients()); // 收件人,0-63字符
		r.setcRPhone(uod.getUserphone()); // 收件电话,0-63字符。
		r.setcREMail(uod.getEmail()); // 收件电邮,0-63字符
		r.setcRPostcode(uod.getUsercode()); // 收件邮编,0-15字符。
		r.setcRCountry(uod.getUserzone()); // 收件国家【必须为英文】,0-126字符。
		r.setcRProvince(uod.getUserstate()); // 收件省州,0-63字符。
		r.setcRCity(uod.getUsercity()); // 收件城市,0-126字符。
		r.setcRAddr(uod.getUserstreet()); // 收件地址,0-254字符。.
		r.setGoodsList(list);
		r.setcEmsKind((String) bgMap.get("fpxProductCode"));
		lr.add(r);
		return lr;
	}


	// 4PX出货处理
	public String fpxApiApplication(UserOrderDetails uod, String orderlist,
									String transportname) {
		int state = 0;
		FindOrderSample fos = new FindOrderSample();
		// CreateOrderSample cos = new CreateOrderSample();// 生成递4方单号API
		CreateAndPreAlertOrderSample cos = new CreateAndPreAlertOrderSample(); // 生成并预报订单接口
		RemoveOrderSample ros = new RemoveOrderSample();
		int tt = fos.findOrderService(orderlist);// 查询客户单号是否已经存在
		int ttt = purchaseServer.findOrderService(orderlist);// 在数据库中查找订单是否出库
		if (tt == 0) {
			// System.out.println("查询操作失败!");
			state = 1;
		} else if (tt > 1 || ttt != 0) { // 判断客户单号是否已经存在，若存在，则删除
			// ros.removeOrderService(orderlist);//删除API
			// System.out.println("客户单号已经存在!");
			state = 2;
		} else {
			// 获取服务商单号TrackingNumber
			// String TrackingNumber = cos.createOrderService(uod);

		}
		String TrackingNumber = cos.createAndPreAlertOrderService(uod);// 生成并预报订单
		if (TrackingNumber.equals("Failure")) {
			// System.out.println("操作失败！");
			state = 3;
		} else {

			System.out.println("服务商单号：" + TrackingNumber);
			return TrackingNumber;
			// 多个包裹 快递号
		}
		return "";
	}

	// 原飞航-出货处理
	public int yfhApiApplication(UserOrderDetails uod, String orderlist,String yuanfeihangno) {
		int state = 0;
		if (yuanfeihangno == null || yuanfeihangno.equals("")) { // 原飞航运单号//
			state = 5;
			return state;
		} else {
			int ttt = purchaseServer.findOrderService(orderlist);// 在数据库中查找订单是否出库
			if (ttt != 0) {
				// System.out.println("包含单号已经出库！");
				state = 6;
			} else {
				// yuanfeihangno 多个包裹运单号
			}
		}
		return state;
	}

	/**
	 * 拼接飞特物流信息参数
	 * @param orderid
	 * @param declares
	 * @param uod
	 * @return
	 */
	public static Map<String,String> getFTData(String orderid,Map<String, Object> declares,UserOrderDetails uod){
		Map<String,String> map=new HashMap<String,String>();
		try{
			map.put("ApiOrderId",orderid);
			map.put("Address1",StringUtil.isBlank(uod.getUserstreet())?"":uod.getUserstreet());
			map.put("County", StringUtil.isBlank(uod.getUserzone())?"":uod.getUserzone());
			map.put("City", StringUtil.isBlank(uod.getUsercity())?"":uod.getUsercity());
			map.put("CiId", StringUtil.isBlank(uod.getUserzone())?"":uod.getUserzone());
			map.put("Zip", StringUtil.isBlank(uod.getUsercode())?"":uod.getUsercode());
			map.put("Phone",  StringUtil.isBlank(uod.getUserphone())?"":uod.getUserphone());
			map.put("ReceiverName",StringUtil.isBlank(uod.getEmail())?"":uod.getEmail());
			map.put("Quantities", StringUtil.isBlank(declares.get("productnum").toString())?"":declares.get("productnum").toString());
			map.put("Price", StringUtil.isBlank((String) declares.get("productprice"))?"":(String) declares.get("productprice"));
			map.put("Weight",StringUtil.isBlank(uod.getWeight())?"":uod.getWeight());
			map.put("HwCode","");//海关编码
			map.put("BtId","");//不带电可为空
			map.put("Sku","");//规格
			map.put("Quantities_i",StringUtil.isBlank(declares.get("productnum").toString())?"":declares.get("productnum").toString());
			map.put("ItemEnName", declares.get("productname")==null || "".equals(declares.get("productname"))?"made in china":declares.get("productname").toString());
			map.put("ItemCnName", declares.get("productname")==null || "".equals(declares.get("productname"))?"made in china":declares.get("productname").toString());
			map.put("UnitPrice", StringUtil.isBlank((String) declares.get("productprice"))?"":(String) declares.get("productprice"));
		}catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 拼接中通物流的参数数据
	 */
	public Map<String,Object> getZtoData(String orderid,Map<String, Object> declares,UserOrderDetails uod){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("logisticsId", "");//国际运单号(接口返回)
		map.put("tradeOrderId", orderid);//客户订单号
		map.put("platformSource", "1112 ");//平台来源
		map.put("orderSource", "0");//订单来源
		map.put("buyerName", uod.getRecipients() == null ? uod.getUserName() : uod.getRecipients());//收件人姓名(英文)
		map.put("buyerMobile", uod.getUserphone());//收件人手机
		map.put("buyerCountry", "United Kingdom");//收件人国家(英文)
		map.put("buyerProvince", uod.getUserstate());//收件人州/省(英文)
		map.put("buyerCity", uod.getUsercity());//收件人市(英文)
		map.put("buyerDistrict",uod.getStatename());//收件人区(英文)
		map.put("buyerStreetaddress", uod.getUserstreet());//收件人地址(英文)
		map.put("buyerZipcode", uod.getUsercode());//收件人邮编
		map.put("senderName", "Eric");//发件人名称(英文)
		map.put("senderMobile",02163757326);//发件人手机
		map.put("senderCountry", "China");//发件人国家(英文)
		map.put("senderProvince", "上海市");//发件人省
		map.put("senderCity", "shanghaishi");//发件人市
		map.put("senderDistrict", "普陀区");//发件人区
		map.put("senderStreetAddress", "上海市普陀区云岭东路609号1号楼605室");//发件人地址
		map.put("productId", 129);//产品ID
		map.put("userId", "0");//用户id/客户ID(官网注册的用户对应的ID)
		map.put("senderCompany", "上海凯融信息科技有限公司");//发件人公司
		map.put("warehouseCode", "1420018");
		//==========商品明细
		map.put("itemId", "31");//商品ID(对应海关hscode)
		map.put("itemName", declares.get("productname")==null || "".equals(declares.get("productname"))?"made in china":declares.get("productname"));//商品名称
		map.put("itemUnitPrice", (String) declares.get("productprice"));//商品价格
		map.put("itemQuantity", declares.get("productnum"));//商品购买数量
		map.put("currencyType", declares.get("productcurreny"));//货币类型
		map.put("hasBattery", "0");//商品是否带电
		return map;
	}



	/**
	 * 调用飞特API获取物流单号
	 * @param map
	 * @return
	 */
	public static String outFte(Map<String,String> map){
		String expressno="";
		try{
			String data="{\"Token\":\"39E74D0D-3ACF-6C38-A9DD-AE5A65DFE544\",\n" + "\"UAccount\":\"33150\",\n"
					+ "\"Password\":\"31B68D81E2A8824C65D552EAAF116D2A\",\n" + "\"OrderList\":[{\"Address1\":\""+map.get("Address1")+"\",\n"
					+ "\"Address2\":\"\",\n" + "\"ApiOrderId\":\""+map.get("ApiOrderId")+"\",\n" + "\"City\":\""+map.get("City")+"\",\n" + "\"CiId\":\""+map.get("CiId")+"\",\n"
					+ "\"County\":\""+map.get("County")+"\",\n" + "\"CCode\":\"USD\",\n" + "\"Email\":\"\",\n" + "\"PackType\":\"3\",\n" + "\"Phone\":\""+map.get("Phone")+"\",\n"
					+ "\"PtId\":\"THAMR\",\n" + "\"ReceiverName\":\""+map.get("ReceiverName")+"\",\n" + "\"SalesPlatformFlag\":\"0\",\n"
					+ "\"SyncPlatformFlag\":\"flyt.logistics.import-express\",\n" + "\"Zip\":\""+map.get("Zip")+"\",\n"
					+ "\"OrderDetailList\":[\n" + "{\"ItemName\":\"\",\n" + "\"Price\":\""+map.get("Price")+"\",\n"
					+ "\"Quantities\":\""+map.get("Quantities_i")+"\",\n" + "\"Sku\":\""+map.get("Sku")+"\"}],\n" + "\"HaikwanDetialList\":[\n"
					+ "{\"HwCode\":\"\",\n" + "\"ItemCnName\":\"商品\",\n" + "\"ItemEnName\":\""+map.get("ItemEnName")+"\",\n"
					+ "\"ItemId\":\"\",\n" + "\"ProducingArea\":\"CN\",\n" + "\"Quantities\":\""+map.get("Quantities")+"\",\n" + "\"UnitPrice\":\""+map.get("UnitPrice")+"\",\n"
					+ "\"Weight\":\""+map.get("")+"\",\n" + "\"BtId\":\"\",\n" + "\"CCode\":\"USD\",\n"
					+ "\"Purpose\":\"\",\n" + "\"Material\":\"\"}\n" + "]}]}";
			HttpPost method = null;
			String apiURL = "http://exorderwebapi.flytcloud.com/api/OrderSyn/ErpUploadOrder";
			org.apache.http.client.HttpClient httpClient = null;
			httpClient = new DefaultHttpClient();
			method = new HttpPost(apiURL);
			method.addHeader("Content-type","application/json; charset=utf-8");
			method.setHeader("Accept", "application/json");
			method.setEntity(new StringEntity(data, Charset.forName("UTF-8")));
			HttpResponse response = httpClient.execute(method);
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println("statusCode==="+statusCode);
			if (statusCode == HttpStatus.SC_OK){
				String body = EntityUtils.toString(response.getEntity());
				System.out.println("body=="+body);
				JSONObject json = JSONObject.fromObject(body);
				String Success=json.getString("Success");
				if(StringUtil.isNotBlank(Success) && "true".equals(Success)){
					System.out.println("上传飞特订单接口成功");
					String ErpSuccessOrders=json.getString("ErpSuccessOrders").replace("[","").replace("]","");
					json = JSONObject.fromObject(ErpSuccessOrders);
					expressno=json.get("TraceId").toString();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("expressno==="+expressno);
		return expressno;
	}




//		public static void main(String[] args) {
////		Map<String,String> map=getFTData(null,null,null);
////		outFte(null);
//			batchCk(null,null,null);
//	}


	public String [] outZto(Map<String,Object> map1){
		String [] strs=new String[2];
		String orderNo="";
		String pdfUrl="";
		Map<String, Object> map = new HashMap<String, Object>();
		String key="DDC3C8C3E593C708A0";
		String data = "[{\"logisticsId\":\"\",\"tradeOrderId\":\""+map1.get("tradeOrderId")+"\",\"platformSource\":1112,\"orderSource\":"+map1.get("orderSource")+","
				+ "\"buyerName\":\""+map1.get("buyerName")+"\",\"buyerMobile\":\""+map1.get("buyerMobile")+"\","
				+ "\"buyerCountry\":\""+map1.get("buyerCountry")+"\",\"buyerProvince\":\""+map1.get("buyerProvince")+"\",\"hasBattery\":\""+map1.get("hasBattery")+"\",\"buyerCity\":\""+map1.get("buyerCity")+"\","
				+ "\"buyerDistrict\":\""+map1.get("buyerDistrict")+"\",\"buyerStreetaddress\":\""+map1.get("buyerStreetaddress")+"\",\"buyerZipcode\":\""+map1.get("buyerZipcode")+"\","
				+ "\"senderName\":\""+map1.get("senderName")+"\",\"senderMobile\":\""+map1.get("senderMobile")+"\","
				+ "\"senderCountry\":\""+map1.get("senderCountry")+"\",\"senderProvince\":\""+map1.get("senderProvince")+"\",\"senderCity\":\""+map1.get("senderCity")+"\","
				+ "\"senderDistrict\":\""+map1.get("senderDistrict")+"\",\"senderStreetAddress\":\""+map1.get("senderStreetAddress")+"\","
				+ "\"productId\":"+map1.get("productId")+",\"warehouseCode\":"+map1.get("warehouseCode")+",\"userId\":\""+map1.get("userId")+"\","
				+ "\"senderCompany\":\""+map1.get("senderCompany")+"\",\"intlOrderItemList\":[{\"itemId\":\""+map1.get("itemId")+"\",	"
				+ "\"itemName\":\""+map1.get("itemName")+"\",	\"itemUnitPrice\":"+map1.get("itemUnitPrice")+",	\"itemQuantity\":"+map1.get("itemQuantity")+",	"
				+ "\"currencyType\":\""+map1.get("currencyType")+"\"}]}]";
		LOG.info("中通API下单参数:"+data);
		String str=String.format("%s%s", data,key);
		map.put("data", data);
		map.put("msg_type", "eur.order.insert");
		map.put("data_digest",Md5Helper.md5(str, "utf-8", true));
		map.put("company_id", "SHKRZYX1410577E1");

		try {
			String json=HttpUtil.post("https://gjapi.zt-express.com/api/Order/init", "UTF-8", map);
			json=new String(json.getBytes("GBK"),"UTF-8");
			System.out.println("中通下单返回结果=="+json);
			if(json.indexOf("true")>-1 && json.indexOf("logisticsId")>-1){
				orderNo=json.indexOf("logisticsId")>-1?json.split("logisticsId")[1]:"";
				orderNo=orderNo.split(",")[0].split(":")[1].toString().replaceAll("\"","");
				//获取标签
				if(orderNo!=null && !"".equals(orderNo)){
					Map<String, Object> map2 = new HashMap<String, Object>();
					data = "{\"logisticsId\":\""+orderNo+"\"}";
					str=String.format("%s%s", data,key);
					map2.put("data", data);
					map2.put("msg_type", "export.pdf.select");
					map2.put("data_digest",Md5Helper.md5(str, "utf-8", true));
					map2.put("company_id", "SHKRZYX1410577E1");
					json=HttpUtil.post("https://gjapi.zt-express.com/api/export/init", "UTF-8", map2);
					json=new String(json.getBytes("GBK"),"UTF-8");
					System.out.println("中通获取打印标签地址返回结果=="+json);
					if(json.indexOf("true")>-1 && json.indexOf("pdfUrl")>-1){
						pdfUrl=json.split("pdfUrl")[1].split(",")[0].toString().replaceAll("\"","");
						pdfUrl=pdfUrl.substring(1,pdfUrl.length()).replace("u0026", "&");
						System.out.println("pdfUrl========"+pdfUrl);
					}
					System.out.println("中通获取打印标签地址=="+pdfUrl);
				}
			}
//			 JSONObject jb = JSONObject.fromObject(json);
//			 orderNo=JSONObject.fromObject(jb.getString("data")).getString("logisticsId");
			System.out.println(orderNo);
		} catch (Exception  e) {
			LOG.error("获取中通国际运单号错误",e);
		}
		strs[0]=orderNo;
		strs[1]=pdfUrl;
		return strs;
	}


	// 佳成出货
	public String outJc(UserOrderDetails uod, String billNo){
		JcgjSoapHttpPost jc = new JcgjSoapHttpPost();
		DataInfo di = new DataInfo();
		saveDataInfo(uod, billNo, di);
		List<ProductBean> proBeanList = new ArrayList<ProductBean>();
		proBeanList = uod.getProductBean();
		// 申报信息
		int ppp = proBeanList.size();
		for (int p = 0; p < ppp; p++) {
			// 海关申报英文品名(Length <= 200)
			di.setPM(proBeanList.get(p).getProducenglishtname());
			// 海关申报中文品名(Length <= 200)
			di.setYWPM(proBeanList.get(p).getProductname());// /////#######
			// 件数(默认: 1)(0 < Amount <= 999)
			di.setJS(proBeanList.get(p).getProductnum());

			// 申报单位类型代码(默认: PCE)，参照申报单位类型代码表(Length = 3)
			di.setWPLX("PCE");// 件
			// 单价(0 < Amount <= [10,2])
			di.setSBJE(proBeanList.get(p).getProductprice());
		}
		di.setHSBM(""); // HS编码
		di.setSBBZ(""); // 申报币种
		di.setZSBGBZ(""); // 是否报关标志 1-是，0-否
		di.setTYBM(""); // 统一编码
		di.setSJZL(uod.getWeight()); // 实重
		di.setTJZL(uod.getWeight()); // 体重
		di.setCKMC(""); // 仓库名称
		di.setSUBFLAG(""); // 仓库标识符 必填
		// pageck
		Map<String, Object> mapdata = new HashMap<String, Object>();
		Map pacs[] = new HashMap[1];
		Map<String, Object> pacdata = new HashMap<String, Object>();
		pacdata.put("DataInfo", di); // di 运单
		pacs[0] = pacdata;
		mapdata.put("Package", pacs);
		String strpac = jc.objToGson(mapdata);
		try{
			String retmsg = jc.callRequest(strpac);
			System.out.println("REQUEST__结果：" + retmsg);
			if (retmsg.indexOf("操作成功") != -1) {
				// di.getBillNo() 多个包裹 多个运单号
				return "10000";// 成功
			} else {
				return "1";// 失败
			}
		}catch (Exception e){
//			return "1";
		}
		return "1";
	}

	private void saveDataInfo(UserOrderDetails uod, String billNo, DataInfo di) {
		di.setHYBM("SH0809"); // 客户编码
		di.setPWD("SH61504007"); // 客户密码
		di.setBillNo(billNo);
		di.setKJLX(uod.getGoodstype()); // 快件类型
		di.setFJRGS(uod.getAdmincompany()); // 发件人公司
		di.setFJRDH(uod.getAdminphone()); // 发件人电话
		di.setFJRXM(uod.getAdminname()); // 发件人姓名
		di.setHGZCBM(""); // 海关注册编码
		di.setSJRXM(uod.getRecipients() == null ? uod.getUserName() : uod.getRecipients()); // 收件人姓名
		di.setSJRDH(uod.getUserphone()); // 收件人电话
		di.setSJRGJ(uod.getUserzone()); // 收件人国家
		di.setSJRYB(uod.getUsercode()); // 收件人邮编
		di.setSJRCS_YWMC(""); // 收件人城市 ???
		di.setSTATENAME(uod.getUserstate()); // 收件人州名
		di.setSJRCS(uod.getUsercity()); // 收件人城市
		di.setSJRGS(uod.getUsercompany()); // 收件人公司
		di.setPPCC(""); // 预付到付 1.PP 2.CC 3.TT
		di.setWPLX(""); // 物品类型
	}

	public void OutPortNow1(String orderno, String isDropshipFlag) {
		List<String> list = iWarehouseService.getOrderFeeOrderNO(orderno);
		String query = "";
		int order_fee_ret = 0;
		int Id_relationtable_ret = 0;
		int OrderInfo_ret = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		if (list.size() > 0) {
			try {
				for (String str : list) {
					query = query + "'" + str + "',";
				}
				query = query.substring(0, query.length() - 1);
				map.put("orders", query);
				// 更新本地正式库order_fee为已出库
				order_fee_ret = iWarehouseService.upOrderFee(map);
				iWarehouseService.upWaraingState(map);
				// 更新本地正式库 id_relationtable为已出库
				Id_relationtable_ret = iWarehouseService.upId_relationtable(map);
				// dropship
				if (isDropshipFlag.equals("1")) {
					// 更新dropshiporder
					iWarehouseService.updateDropshipState(map);
					// 更新线上orderinfo
					// 然后再判断主单状态,并更新主单状态
					String mainOrder = iWarehouseService.checkState(orderno);
					if (StrUtils.isNotNullEmpty(mainOrder)) {
						iWarehouseService.updateMainDropshipState(mainOrder);
					}
					// 同步更新线上dropshiporder 和 orderinfo 表
					DropshipOrderThread thread = new DropshipOrderThread(map,mainOrder);
					thread.start();
				} else {
					// 更新本地正式库orderinfo
					OrderInfo_ret = iWarehouseService.upOrderInfo(map);
					// 更新线上orderinfo
					OrderinfoThread thread = new OrderinfoThread(map, orderno);
					thread.start();
				}
				LOG.info("【订单号:" + map.get("orders")
						+ "】 出库更新状态:order_fee_ret:" + order_fee_ret
						+ " Id_relationtable_ret:" + Id_relationtable_ret
						+ " OrderInfo_ret:" + OrderInfo_ret);
			} catch (Exception e) {
				LOG.error("【订单号:"+ map.get("orders")+ "】 更新orderinfo,order_fee,id_relationtable,dropshiporder订单状态",e);
			}
		}
	}

	// 出货//更改forwarder表
	public void OutPortNow(String orderno) {
		String sqlorderinfo = "";// "update orderinfo set state='3' where
		// order_no in (?)";//更改orderinfo表;
		String sqlorder_fee = "";// "update order_fee set state=2 where orderno
		// in (?)";//更改order_fee表;
		String sqlid_relationtable = "";
		// 查询合并的订单
		// String sql = "select group_concat(\"'\",orderno,\"'\") orderarr from
		// order_fee where mergeOrders= '"
		// + orderno + "' "; //and state='1'
		String sql = "select orderno from order_fee  where mergeOrders= '"
				+ orderno + "'";
		List<String> list = new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		Connection conn2 = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null, stmtfee = null, mergeStmt = null, stmtid = null;
		ResultSet rs = null;
		PreparedStatement stmt2 = null;
		try {
			// 先查询合并订单
			mergeStmt = conn.prepareStatement(sql);
			rs = mergeStmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("orderno"));
			}
			// if (rs.next()) {
			// orderNew = rs.getString("orderarr");
			// }
			/**
			 *
			 * 更新orderinfo state -->3
			 *
			 */
			sqlorderinfo = "update orderinfo set state='3' where 1=1  ";
			if (list.size() == 1) {
				sqlorderinfo += "and  order_no=?";
			} else {
				for (int i = 0; i < list.size(); i++) {
					if (i == 0) {
						sqlorderinfo += " and (  order_no=?";
					} else if (i == (list.size() - 1)) {
						sqlorderinfo += " or order_no=? )";
					} else {
						sqlorderinfo += " or order_no=?";
					}
				}
			}
			stmt = conn.prepareStatement(sqlorderinfo);
			for (int i = 0; i < list.size(); i++) {
				stmt.setString(i + 1, list.get(i));
			}
			if (list.size() > 0) {
				stmt.executeUpdate();
			}

			// sqlorderinfo =
			// "update orderinfo set state='3' where order_no in ("
			// + orderNew + ")";

			/**
			 *
			 * 更新线上数据库 orderinfo 表 状态
			 *
			 */
			stmt2 = conn2.prepareStatement(sqlorderinfo);
			for (int i = 0; i < list.size(); i++) {
				stmt2.setString(i + 1, list.get(i));
			}
			if (list.size() > 0) {
				stmt2.executeUpdate();
			}

			/**
			 *
			 * 修改库存已出货
			 *
			 */
			// sqlorderinfo =
			// "update id_relationtable set state='1' where  is_delete != 1 and orderid in ("
			// + orderNew + ")";
			sqlid_relationtable = "update id_relationtable set state='1' where  is_delete != 1  ";
			if (list.size() == 1) {
				sqlid_relationtable += " and  orderid=?";
			} else {
				for (int i = 0; i < list.size(); i++) {
					if (i == 0) {
						sqlid_relationtable += " and (  orderid=?";
					} else if (i == (list.size() - 1)) {
						sqlid_relationtable += " or orderid=? )";
					} else {
						sqlid_relationtable += " or orderid=?";
					}
				}
			}
			stmtid = conn.prepareStatement(sqlid_relationtable);
			for (int i = 0; i < list.size(); i++) {
				stmtid.setString(i + 1, list.get(i));
			}
			if (list.size() > 0) {
				stmtid.executeUpdate();
			}

			/**
			 *
			 * 更新 order_fee 表
			 */
			// sqlorder_fee = "update order_fee set state=2 where orderno in ("
			// + orderNew + ")";// 更改order_fee表;
			sqlorder_fee = "update order_fee set state=2 where 1=1  ";
			if (list.size() == 1) {
				sqlorder_fee += " and  orderno=?";
			} else {
				for (int i = 0; i < list.size(); i++) {
					if (i == 0) {
						sqlorder_fee += " and (  orderno=?";
					} else if (i == (list.size() - 1)) {
						sqlorder_fee += " or orderno=? )";
					} else {
						sqlorder_fee += " or orderno=?";
					}
				}
			}
			stmtfee = conn.prepareStatement(sqlorder_fee);
			for (int i = 0; i < list.size(); i++) {
				stmtfee.setString(i + 1, list.get(i));
			}
			if (list.size() > 0) {
				stmtfee.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (mergeStmt != null) {
				try {
					mergeStmt.close();
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
			if (stmtid != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtfee != null) {
				try {
					stmtfee.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
			DBHelper.getInstance().closeConnection(conn2);
		}
	}

	// 采购确认
	@RequestMapping(value = "/purchaseConfirm", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String purchaseConfirm(HttpServletRequest request,
                                  @RequestParam(value = "websiteType", defaultValue = "1", required = false) Integer websiteType //网站名
                                  ) {
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,Admuser.class);
		// 判断是否登录失效，失效则不能执行
		if (adm != null) {
			String orderid = request.getParameter("orderno");
			String odid = request.getParameter("odid");
			String purchase_state = request.getParameter("purchase_state"); // child_order_no,isDropshipOrder
			String child_order_no = request.getParameter("child_order_no");// dropship的订单号
			String isDropshipOrder = request.getParameter("isDropshipOrder");// 是否是dropship的订单，0不是，1是2/15
			// 确认采购流程：两种判断，一种是判断订单状态有没有取消，另外一种是其中的商品被取消了
			// 符合条件的告知不能操作，请刷新界面
			boolean isPass = checkOrderInfoStateAndOrderDetailsState(orderid,Integer.valueOf(odid));
			if (isPass) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderid", orderid);
				map.put("odid", odid);
				map.put("purchase_state", purchase_state);
				Map<String, String> mapc = new HashMap<String, String>();
				mapc.put("child_order_no", child_order_no);
				mapc.put("odid", odid);
				mapc.put("purchase_state", purchase_state);
				map.put("admid", String.valueOf(adm.getId()));
				// iWarehouseService.updateGoodsDistribution(map); //给王宏杰自动分配用的
				// 修改order_product_source 状态 为采购，修改采购时间，本地状态
				// 修改 order_details 的字段 purchase_state，本地状态
				iWarehouseService.updateODPState(map);
				int ret = iWarehouseService.updateOpsState(map);
				// 同上，线上数据库也修改一份
				// DataSourceSelector.set("dataSource127hop");
				// iWarehouseService.updateODPState(map);
				// DataSourceSelector.restore();
				// 同上，线上数据库也修改一份 异步更新12.15
				PurchaseConfirmThread thread = new PurchaseConfirmThread(map);
				thread.start();
				if (ret > 0) {
					// 修改订单数量，本地
					ret = iWarehouseService.updateOrderinfoNumber(map);
				}
				if (ret > 0) {
					int isSendEmail=pruchaseMapper.getIsSendEmail(orderid);
					if(isSendEmail<=0){
						//发送邮件给客户告知已经发货
						IGuestBookService ibs = new GuestBookServiceImpl();
						OrderBean ob=dao.getUserOrderInfoByOrderNo(orderid);
						Map<String,Object> modelM = new HashedMap();
						modelM.put("name",ob.getEmail());
						modelM.put("orderid",orderid);
						modelM.put("recipients",ob.getRecipients());
						modelM.put("street",ob.getStreet());
						modelM.put("street1",ob.getAddresss());
						modelM.put("city",ob.getAddress2());
						modelM.put("state",ob.getStatename());
						modelM.put("country",ob.getCountry());
						modelM.put("zipCode",ob.getZipcode());
						modelM.put("phone",ob.getPhonenumber());
						modelM.put("websiteType", websiteType);
						if (websiteType == 1) {
                            modelM.put("toHref", "https://www.import-express.com/apa/tracking.html?loginflag=false&orderNo=" + orderid + "");
                        } else if (websiteType == 2) {
                            modelM.put("toHref", "https://www.kidsproductwholesale.com/apa/tracking.html?loginflag=false&orderNo=" + orderid + "");
                        } else if (websiteType == 3) {
                            modelM.put("toHref", "https://www.lovelypetsupply.com/apa/tracking.html?loginflag=false&orderNo=" + orderid + "");
                        }
                        sendMailFactory.sendMail(String.valueOf(modelM.get("name")), null, "Order purchase notice", modelM, TemplateType.PURCHASE);
						//插入发送邮件记录
						pruchaseMapper.insertPurchaseEmail(orderid);
					}
                    //查询客户ID和订单状态
                    Map<String,Object> orderinfoMap = iPurchaseService.queryUserIdAndStateByOrderNo(map.get("orderid"));
                    map.put("old_state",orderinfoMap.get("old_state").toString());
                    map.put("user_id",orderinfoMap.get("user_id").toString());
                    orderinfoMap.clear();
					// 修改订单状态，
					// 逻辑:当前订单的下的所有商品如果都是已经采购，那么修改订单状态为1 采购中，本地
					// 新逻辑：当前订单下的商品只要有1个商品在采购中，当前的订单状态更改为1，采购中。
					if (isDropshipOrder.equals("1")) {
						ret = iWarehouseService.updateChildOrderinfoState(mapc);
						ret = iWarehouseService.updateOrderinfoState(map);
					} else {
						ret = iWarehouseService.updateOrderinfoState(map);
					}
					// 线上也更新一份订单状态
					int state = iWarehouseService.GetSetOrdrerState(map);
					map.put("state", state + "");
					mapc.put("state", state + "");
					map.put("adminId", adm.getId() + "");
					mapc.put("adminId", adm.getId() + "");
					// jxw 2017-4-13
					// 线上
					// DataSourceSelector.set("dataSource127hop");
					// iWarehouseService.updateOrder(map);
					// DataSourceSelector.restore();
					// 同上，线上数据库也修改一份 异步更新不卡12.15
					UpdateOrderThread threadc = new UpdateOrderThread(map);
					threadc.start();
					if (isDropshipOrder.equals("1")) {
						UpdateChildOrderThread threadcc = new UpdateChildOrderThread(
								mapc);
						threadcc.start();
					} else {
					}
				}
				return ret + "";
			} else {
				// 弹框
				return "-1";
			}
		} else {
			// 弹框
			return "-1";
		}

	}

	// 判断订单状态有没有取消，或者其中的商品被取消了
	private boolean checkOrderInfoStateAndOrderDetailsState(String orderNo,
															int odid) {
		String orderInfoState = iWarehouseService
				.getOrderInfoStateByOrderNo(orderNo);
		// 判断订单状态
		if (orderInfoState == "-1" || orderInfoState == "6") {
			return false;
		} else {
			// 判断订单详情的状态
			int orderDetailsState = iWarehouseService
					.getOrderDetailsStateByOrderNoAndGoodsid(orderNo, odid);
			if (orderDetailsState == 2) {
				return false;
			} else {
				return true;
			}
		}
	}

	// 采购商品入库
	@RequestMapping(value = "/purchaseStorage", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String purchaseStorage(HttpServletRequest request) {
		String orderid = request.getParameter("orderno");
		String goodsid = request.getParameter("goodid");
		String odid = request.getParameter("odid");
		String admid = request.getParameter("admid");
		String goodsurl = request.getParameter("goodsurl");
		String purchase_state = "4";
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderid", orderid);
		map.put("goodsid", goodsid);
		map.put("odid", odid);
		map.put("admid", admid);
		map.put("purchase_state", purchase_state);
		map.put("goodsurl", goodsurl);
		int r = 0;
		try{
			SendMQ sendMQ = new SendMQ();
			int ret = iWarehouseService.updateOpsState(map);
			// 入库增加入库详情
			int ret1 = iWarehouseService.insertId_relationtable(map);
			r += ret;
			if (ret > 0) {
				ret = iWarehouseService.updateorderDetailsState(map);
				r += ret;
				sendMQ.sendMsg(new RunSqlModel("update order_details set state=1,purchase_confirmation='"+map.get("admid")+"',purchase_state=3 where orderid='"+map.get("orderid")+"'" +
						" and id='"+map.get("odid")+"'"));
			}
			if (ret > 0) {
				ret = iWarehouseService.getOdIsState(map);
				if (1 == ret) {
					map.put("state", "2");
					iWarehouseService.updateOrder(map);
					sendMQ.sendMsg(new RunSqlModel("update orderinfo set state = '"+map.get("state")+"' where order_no='"+map.get("orderid")+"'"));
				}
				r += ret;
			}
			sendMQ.closeConn();
		}catch (Exception e){
			e.printStackTrace();
		}
		return r + "";
	}
//
//	// 一键确认采购 线上
//	@RequestMapping(value = "/getTreeByUserId", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public void getTreeByUserId(HttpServletRequest request,HttpServletResponse response) throws IOException{
//		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
//		response.setCharacterEncoding("UTF-8");
//		com.cbt.website.userAuth.bean.Admuser adm = (com.cbt.website.userAuth.bean.Admuser) SerializeUtil.JsonToObj(admuserJson,com.cbt.website.userAuth.bean.Admuser.class);
//		String admName=adm.getAdmName();
//		List<TblAuthInfoPojo> list=iWarehouseService.getTreeByUserId(admName);
//		PrintWriter out = response.getWriter();
//		out.print(JSONArray.fromObject(list).toString());
//		out.flush();
//		out.close();
//	}

	// 一键确认采购 线上
	@RequestMapping(value = "/allcgqrQr", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void allcgqrQr(HttpServletRequest request,
						  HttpServletResponse response) throws ServletException, IOException,
			NumberFormatException, InterruptedException, ExecutionException {
		String listmap = request.getParameter("listmap");
		JSONObject json = JSONObject.fromObject(listmap);
		List<Map<String, String>> edit = (List<Map<String, String>>) json
				.getJSONArray("listmap");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>(); // Future
		// 相当于是用来存放Executor执行的结果的一种容器

		for (int i = 0; i < edit.size(); i++) {
			try {
				results.add(exec.submit(new AllcgqrQr(edit.get(i))));
			} catch (Exception e) {
				System.out.println("死锁：" + edit.get(i).get("goodsid"));
				results.add(exec.submit(new AllcgqrQr(edit.get(i))));
			}

		}

		for (int i = 0; i < results.size(); i++) {
			Future<String> fs = results.get(i);
			Map<String, String> map = edit.get(i);
			// (Future<String> fs : results) {
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			Map<String, String> m = new HashMap<String, String>();
			m.put("orderno", orderNo);
			m.put("goodsid", goodid + "");
			list.add(m);
			if (!"".equals(fs.get())) {

				System.out.println("正确：" + fs.get() + "----------"
						+ (String) map.get("goodsid"));
			} else {
				System.out.println("错误：" + fs.get() + "----------"
						+ (String) map.get("goodsid"));
				exec.submit(new AllcgqrQr(edit.get(i)));
			}
		}
		exec.shutdown();
		PrintWriter out = response.getWriter();
		//
		out.print(JSONArray.fromObject(list).toString());
		out.flush();
		out.close();
	}

	// 一键确认采购线程
	class AllcgqrQr implements Callable<String> {
		private Map<String, String> map;
		public AllcgqrQr(Map<String, String> map) {
			this.map = map;
		}
		@Override
		public String call() throws Exception {
			String orderid = (String) map.get("orderno");
			String goodsid = (String) map.get("goodsid");
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderid", orderid);
			map.put("goodsid", goodsid);
			map.put("purchase_state", "3");
			try{
				SendMQ sendMQ = new SendMQ();
				// 修改order_product_source 状态
				int ret = iWarehouseService.updateOpsState(map);
				iWarehouseService.updateODPState(map);
				sendMQ.sendMsg(new RunSqlModel("update order_details set purchase_state='"+map.get("purchase_state")+"',purchase_time ="+("3".equals(map.get("purchase_state"))?"now()":"null")+" where orderid='"+orderid+"' and goodsid='"+goodsid+"'"));
				if (ret > 0) {
					ret = iWarehouseService.updateOrderinfoNumber(map);
				}
				if (ret > 0) {
					ret = iWarehouseService.updateOrderinfoState(map);
					int state = iWarehouseService.GetSetOrdrerState(map);
					map.put("state", state + "");
					sendMQ.sendMsg(new RunSqlModel("update orderinfo set state = '"+map.get("state")+"' where order_no='"+map.get("orderid")+"'"));
				}
				sendMQ.closeConn();
			}catch (Exception e){
				e.printStackTrace();
			}
			return "";
		}
	}

	// 取消确认采购
	@RequestMapping(value = "/allQxcgQr", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void allQxcgQr(HttpServletRequest request,
						  HttpServletResponse response) throws ServletException, IOException,
			NumberFormatException, InterruptedException, ExecutionException {
		String listmap = request.getParameter("listmap");
		JSONObject json = JSONObject.fromObject(listmap);
		List<Map<String, String>> edit = (List<Map<String, String>>) json
				.getJSONArray("listmap");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> results = new ArrayList<Future<String>>(); // Future
		// 相当于是用来存放Executor执行的结果的一种容器

		for (int i = 0; i < edit.size(); i++) {
			try {
				results.add(exec.submit(new AllQxcgQr(edit.get(i))));
			} catch (Exception e) {
				results.add(exec.submit(new AllQxcgQr(edit.get(i))));
			}
		}

		for (int i = 0; i < results.size(); i++) {
			Future<String> fs = results.get(i);
			Map<String, String> map = edit.get(i);
			String orderNo = (String) map.get("orderno");
			String goodidd = (String) map.get("goodsid");
			int goodid = Integer.parseInt(goodidd);
			Map<String, String> m = new HashMap<String, String>();
			m.put("orderno", orderNo);
			m.put("goodsid", goodid + "");
			list.add(m);
			if (!"".equals(fs.get())) {

				System.out.println("正确：" + fs.get() + "----------"
						+ (String) map.get("goodsid"));
			} else {
				System.out.println("错误：" + fs.get() + "----------"
						+ (String) map.get("goodsid"));
				exec.submit(new AllQxcgQr(edit.get(i)));
			}
		}
		exec.shutdown();
		PrintWriter out = response.getWriter();
		//
		out.print(JSONArray.fromObject(list).toString());
		out.flush();
		out.close();
	}

    /**
     * 商品采购后取消退货时产品的库存的也一并取消
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/refundGoods")
    public void refundGoods(HttpServletRequest request,
                             HttpServletResponse response) throws ServletException, IOException {
        int row=0;
        Map<String,String> map=new HashMap<String,String>(2);
        try{
            String orderid=request.getParameter("orderid");
            String goodsid=request.getParameter("goodsid");
            map.put("orderid",orderid);
            map.put("goodsid",goodsid);
           row = iWarehouseService.refundGoods(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(row);
        out.flush();
        out.close();
    }

	// 入库备注
	@RequestMapping(value = "/treasuryNote.do")
	public void treasuryNote(HttpServletRequest request,
							 HttpServletResponse response) throws ServletException, IOException {

		String admuserJson = Redis
				.hget(request.getSession().getId(), "admuser");
		com.cbt.website.userAuth.bean.Admuser adm = (com.cbt.website.userAuth.bean.Admuser) SerializeUtil
				.JsonToObj(admuserJson,
						com.cbt.website.userAuth.bean.Admuser.class);

		String messageStr = "";
		String orderNo = request.getParameter("orderNo");
		String type = request.getParameter("type");
		if (orderNo == null || "".equals(orderNo)) {
			messageStr = "备注失败，获取订单号失败";
		}
		int goodsid = 0;
		String goodsidStr = request.getParameter("goodsid");
		if (goodsidStr == null || "".equals(goodsidStr)) {
			messageStr = "备注失败，获取商品id失败";
		} else {
			goodsid = Integer.valueOf(goodsidStr);
		}
		String remarkContent = request.getParameter("remarkContent");
		if (remarkContent == null || "".equals(remarkContent)) {
			messageStr = "备注失败，获取备注内容失败";
		} else {
			remarkContent = adm.getAdmName() + ":" + remarkContent + "("
					+ DateFormatUtil.getWithMinutes(new Date()) + ");";
			if (type.equals("1")) {
				remarkContent += "已将疑问验货改为验货无误;";
			}
		}
		if ("".equals(messageStr)) {
			iWarehouseService.treasuryNote(orderNo, goodsid, remarkContent);
			if (type.equals("1")) {
				// 更新疑问验货为验货无误
				iWarehouseService.updateCheckForNote(orderNo, goodsid);
				// int row=iWarehouseService.
			}
			messageStr = "ok";
		}
		PrintWriter out = response.getWriter();
		out.print(messageStr);
		out.flush();
		out.close();
	}

	class AllQxcgQr implements Callable<String> {
		private Map<String, String> map;

		public AllQxcgQr(Map<String, String> map) {
			this.map = map;
		}

		@Override
		public String call() throws Exception {
			String orderid = (String) map.get("orderno");
			String goodsid = (String) map.get("goodsid");
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderid", orderid);
			map.put("goodsid", goodsid);
			map.put("purchase_state", "1");
			try{
				SendMQ sendMQ = new SendMQ();
				// 修改order_product_source 状态
				int ret = iWarehouseService.updateOpsState(map);
				iWarehouseService.updateODPState(map);
				DataSourceSelector.set("dataSource127hop");
				iWarehouseService.updateODPState(map);
				sendMQ.sendMsg(new RunSqlModel("update order_details set purchase_state='"+map.get("purchase_state")+"',purchase_time ="+("3".equals(map.get("purchase_state"))?"now()":"null")+" where orderid='"+orderid+"' and goodsid='"+goodsid+"'"));
				DataSourceSelector.restore();
				if (ret > 0) {
					ret = iWarehouseService.updateOrderinfoNumber(map);
				}
				if (ret > 0) {
					ret = iWarehouseService.updateOrderinfoState(map);
					int state = iWarehouseService.GetSetOrdrerState(map);
					map.put("state", state + "");
					// 线上
					DataSourceSelector.set("dataSource127hop");
					iWarehouseService.updateOrder(map);
					DataSourceSelector.restore();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			return "";
		}
	}

	// 更新 库存表
	public void updateInvetory(GoodsInventory bean) throws Exception {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Date date = new Date();
		int count = 0;
		String inventorySql = "";
		int flag = 0; // 默认 0 .barcode | 1. new_barcode
		String sql = "select count(*) as count  from inventory where goods_url= ? and sku= ?  and  new_barcode = ?   ";
		try {
			stmt2 = conn.prepareStatement(sql);
			stmt2.setString(1, bean.getGoods_url());
			stmt2.setString(2, bean.getSku());
			stmt2.setString(3, bean.getBarcode());
			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				count = rs2.getInt("count");
			}
			if (count > 0) {
				flag = 1;
				inventorySql = "select *  from  inventory where goods_url= ? and sku= ?  and  new_barcode = ? ";
			} else {
				flag = 0;
				inventorySql = "select *  from  inventory where goods_url= ? and sku= ?  and  barcode = ? ";
			}

			stmt = conn.prepareStatement(inventorySql);
			stmt.setString(1, bean.getGoods_url());
			stmt.setString(2, bean.getSku());
			stmt.setString(3, bean.getBarcode());
			rs = stmt.executeQuery();
			int row = 0;
			while (rs.next()) {
				// new_remaining >0
				if (rs.getInt("new_remaining") > 0) {
					if (flag == 1) {
						inventorySql = "update inventory set out_goods=?,new_remaining=? ,can_remaining =?,last_out_time =? ,inventory_amount = ? where goods_url=? and sku=? and new_barcode = ?";
					} else {
						inventorySql = "update inventory set out_goods=?,new_remaining=? ,can_remaining =?,last_out_time =? ,inventory_amount = ? where goods_url=? and sku=? and  barcode = ?";
					}
				} else {
					if (flag == 1) {
						inventorySql = "update inventory set out_goods=?,remaining=? ,can_remaining =?,last_out_time =? ,inventory_amount = ? where goods_url=? and sku=? and new_barcode = ?";
					} else {
						inventorySql = "update inventory set out_goods=?,remaining=? ,can_remaining =?,last_out_time =? ,inventory_amount = ? where goods_url=? and sku=? and  barcode = ?";
					}
				}
				stmt = conn.prepareStatement(inventorySql);
				stmt.setInt(1, rs.getInt("out_goods") - bean.getInGoodNum());
				if (rs.getInt("new_remaining") > 0) {
					stmt.setInt(2,
							rs.getInt("new_remaining") - bean.getInGoodNum());
					stmt.setInt(3,
							rs.getInt("can_remaining") - bean.getInGoodNum());
				} else {
					stmt.setInt(2, rs.getInt("remaining") - bean.getInGoodNum());
					stmt.setInt(3,
							rs.getInt("can_remaining") - bean.getInGoodNum());
				}
				stmt.setString(4, sdf.format(date));
				stmt.setDouble(5,
						rs.getDouble("inventory_amount") - bean.getInGoodNum()
								* Double.parseDouble(bean.getItemprice()));
				stmt.setString(6, rs.getString("goods_url"));
				stmt.setString(7, rs.getString("sku"));
				if (flag == 1) {
					stmt.setString(8, rs.getString("new_barcode"));
				} else {
					stmt.setString(8, rs.getString("barcode"));
				}
				row = stmt.executeUpdate();
				if (row > 0) {
					System.out.println("更新库存表成功");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("往inventory库存表 新添信息", e);
		}
		DBHelper.getInstance().closeConnection(conn);

	}

	/**
	 * 出库列表 新添 按钮 新增一条记录
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/insertSP", method = RequestMethod.POST)
	@ResponseBody
	public String insertSP(HttpServletRequest request,
						   HttpServletResponse response) {
		String shipmentno = request.getParameter("shipmentno");
		String result = "true";
		// 如果线上数据连接超时，不影响后面操作
		try {
			// 本地
			iWarehouseService.insertSp(shipmentno);

			// 判断是否开启线下同步线上配置
			if (GetConfigureInfo.openSync()) {
				String orderId, businessType, tableName, sqlStr = null;
				orderId = ""; // 订单号
				businessType = "出库--新增信息"; // 业务类型
				tableName = "shipping_package"; // 执行表名称
				sqlStr = " insert  into shipping_package(shipmentno,orderid,remarks,createtime,sweight,svolume,volumeweight,sflag,transportcompany,shippingtype"
						+ ",transportcountry,expressno,freight,estimatefreight,issendmail,settleWeight,totalPrice)"
						+ "select concat(shipmentno,'_1'),orderid,remarks,createtime,sweight,svolume,volumeweight,sflag,transportcompany,shippingtype"
						+ ",transportcountry,expressno,freight,estimatefreight,issendmail,settleWeight,totalPrice from  shipping_package where shipmentno =  '"
						+ shipmentno + "'  and sflag = 3 "; // 执行SQL体

				SaveSyncTable.InsertOnlineDataInfo(0, orderId, businessType,
						tableName, sqlStr);
			} else {
				// 线上
				DataSourceSelector.set("dataSource127hop");
				int ret = iWarehouseService.insertSp(shipmentno);
				DataSourceSelector.restore();
			}
		} catch (Exception e) {
			LOG.error("更新线上出库列表  新添信息", e);
		}
		String json = JSON.toJSONString(result);
		return json;
	}

	/**
	 * 补录出货订单信息
	 *
	 */
	@RequestMapping(value = "/makeup", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String Makeup(HttpServletRequest request,
						 HttpServletResponse response,
						 @RequestBody Map<String, Object> mainMap) {
		List<Map<String, Object>> bgList = (List<Map<String, Object>>) mainMap
				.get("bgList");

		for (int i = 0; i < bgList.size(); i++) {
			try {
				Map<String, Object> bean = bgList.get(i);

				String orderid = (String) bean.get("orderid");
				bean.put("orderid", "'" + orderid + "'");
				String remarks = (String) bean.get("remarks");
				bean.put("remarks", "'" + remarks + "'");
				String expressno = (String) bean.get("expressno");
				bean.put("expressno", "'" + expressno + "'");
				String createtime = (String) bean.get("createtime");
				bean.put("createtime", "'" + createtime + "'");
				String transportcompany = (String) bean.get("transport");
				bean.put("transport", "'" + transportcompany + "'");
				String svolume = bean.get("svolume").toString();
				bean.put("svolume", "'" + svolume + "'");
				String[] volumes = svolume.split("\\*");
				double volumeweight = 0;
				if (volumes.length > 2) {
					double length = Double.parseDouble(volumes[0]);
					double weight = Double.parseDouble(volumes[1]);
					double height = Double.parseDouble(volumes[2]);
					volumeweight = length * weight * height / 5000;
				}
				bean.put("volumeweight", volumeweight);
				bean.put("flag", 1);
			} catch (Exception e) {
				// TODO: handle exception
				LOG.error("补录出货订单信息", e);
			}
		}
		int ret = iWarehouseService.insertBuluInfo(bgList);

		// 同步更新订单状态
		// int result = iWarehouseService.updateBuluOrderState(bgList);

		if (ret > 0) {
			// 同步更新线上
			BuluOrderinfoThread thread = new BuluOrderinfoThread(bgList);
			thread.start();
		}
		return "" + ret;
	}

	/**
	 * 出库 更新线上 线程
	 *
	 * @author admin
	 *
	 */
	class batchCKThread extends Thread {
		private List<Map<String, String>> bgList;

		public batchCKThread(List<Map<String, String>> bgList) {
			super();
			this.bgList = bgList;
		}

		@Override
		public synchronized void run() {
			try {

				/*DataSourceSelector.set("dataSource127hop");
				int ret = iWarehouseService.bgUpdate(bgList);
				LOG.info("更新线上shipping_package包裹状态:" + ret);
				DataSourceSelector.restore();*/

				// 加入日志
				iWarehouseService.bgUpdateLog(bgList);

				StringBuffer upAllSql = new StringBuffer();
				if (bgList != null && bgList.size() > 0) {
					for (Map<String, String> param : bgList) {
						StringBuffer upSql = new StringBuffer();
						upSql.append("update shipping_package set ")
								.append("transportcompany = '" + param.get("transportcompany") + "',")
								.append("shippingtype = '" + param.get("fpxProductCode") + "',")
								.append("transportcountry = '" + param.get("fpxCountryCode") + "',")
								.append("expressno = '" + param.get("expressno") + "',")
								.append("freight = '" + param.get("freight") + "',")
								.append("estimatefreight = '" + param.get("estimatefreight") + "',")
								.append("pdfUrl = '" + param.get("pdfUrl") + "',")
								.append("sflag='3',createtime=now() ")
								.append(" where shipmentno ='" + param.get("shipmentno") + "';");
						iWarehouseService.insertMqLog(upSql.toString(), param.get("shipmentno"), "出库更新线上",param.toString());
						upAllSql.append(upSql.toString());
					}

					NotifyToCustomerUtil.sendSqlByMq(upAllSql.toString());
				}
			} catch (Exception e) {
				LOG.error("更新线上出库信息", e);
			}

		}
	}

	/**
	 * 出库 更新线上 orderinfo 线程
	 *
	 * @author admin
	 *
	 */
	class OrderinfoThread extends Thread {
		private Map<String, Object> map;
		private String orderNo;

		public OrderinfoThread(Map<String, Object> map, String orderNo) {
			super();
			this.map = map;
			this.orderNo = orderNo;
		}

		@Override
		public synchronized void run() {
			try {
//				DataSourceSelector.set("dataSource127hop");// 操作本地数据源切换到线上数据源
//				int orderid_flag = iWarehouseService.upOrderInfo(map);
//				LOG.info("更新线上orderinfo订单状态:" + orderid_flag);
				SendMQ sendMQ = new SendMQ();
				sendMQ.sendMsg(new RunSqlModel("update orderinfo set state = '3'  where order_no  in ("+map.get("orders")+")"));
				sendMQ.closeConn();
				//通知客户
				//查询客户ID
				int userId = orderinfoService.queryUserIdByOrderNo(orderNo);
				//发送消息给客户
				NotifyToCustomerUtil.updateOrderState(userId,orderNo,2,3);
//				DataSourceSelector.restore();
			} catch (Exception e) {
				e.getStackTrace();
				LOG.error("更新线上orderinfo订单信息失败," + "订单号:" + orderNo, e);
				// jxw 2017-4-17 插入失败，插入信息放入失败记录表中
				String sqlStr = "update orderinfo set state ='3' where order_no = '"
						+ orderNo + "';";
				ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, 15, 3,
						"更新失败,原因:" + e.getMessage());
			}

		}
	}

	/**
	 * 修改 更新线上运单信息 线程
	 *
	 * @author admin
	 *
	 */
	class updateExperssNoPlckThread extends Thread {
		private Map<String, Object> map;

		public updateExperssNoPlckThread(Map<String, Object> map) {
			super();
			this.map = map;
		}

		@Override
		public synchronized void run() {
			try {
				String sql = "update shipping_package set expressno='" + map.get("express_no") + "',transportcompany='"
						+ map.get("logistics_name") + "',sweight='" + map.get("sweight") + "'," + "svolume='" + map.get("svolume")
						+ "',volumeweight='" + map.get("volumeweight") + "',freight = '" + map.get("freight") + "' where shipmentno='"
						+ map.get("shipmentno") + "'";
				iWarehouseService.insertMqLog(sql, String.valueOf(map.get("shipmentno")), String.valueOf(map.get("express_no")),map.toString());
				NotifyToCustomerUtil.sendSqlByMq(sql);
			} catch (Exception e) {
				LOG.error("更新线上运单信息", e);
			}
		}
	}

	// 更新线上 dropship 状态
	class DropshipOrderThread extends Thread {
		private Map<String, Object> map;
		private String mainOrder;

		public DropshipOrderThread(Map<String, Object> map, String mainOrder) {
			super();
			this.map = map;
			this.mainOrder = mainOrder;
		}

		@Override
		public synchronized void run() {
			try {
				SendMQ sendMQ = new SendMQ();
				sendMQ.sendMsg(new RunSqlModel("update dropshiporder set state = '3'  where child_order_no = '"+map.get("orders")+"'"));
				if (StrUtils.isNotNullEmpty(mainOrder)) {
					sendMQ.sendMsg(new RunSqlModel("update orderinfo set state = '3' where  order_no  ='"+mainOrder+"'"));
					//查询客户ID
					int userId = orderinfoService.queryUserIdByOrderNo(mainOrder);
					//发送消息给客户
					NotifyToCustomerUtil.updateOrderState(userId,mainOrder,2,3);
				}
				sendMQ.sendMsg(new RunSqlModel("update dropshiporder set state = '3'  where child_order_no = '"+map.get("orders")+"'"));
				sendMQ.closeConn();
			} catch (Exception e) {
				e.getStackTrace();
				LOG.error("更新线上dropshipOrder订单失败，订单号：" + mainOrder, e);
				// jxw 2017-4-17 插入失败，插入信息放入失败记录表中
				String sqlStr = "update orderinfo set state ='3' where order_no = '"
						+ mainOrder + "';";
				ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, 15, 3,
						"更新失败,原因:" + e.getMessage());
			}

		}
	}

	private void insertChangeRecords(String orderNo, int operationType,
									 int adminId) {

		Orderinfo orderinfo = new Orderinfo();
		try {

			// 插入成功，插入信息放入更改记录表中
			OnlineOrderInfoDao infoDao = new OnlineOrderInfoDao();
			orderinfo = infoDao.queryOrderInfoByOrderNo(orderNo);
			if (orderinfo != null) {
				ChangeRecordsDao cRecordsDao = new ChangeRecordsDao();
				cRecordsDao
						.insertOrderChange(orderinfo, adminId, operationType);
			} else {
				LOG.error("订单[" + orderNo + "]获取数据失败，插入更改记录取消");
			}

		} catch (Exception e) {
			e.getStackTrace();
			if (orderinfo != null) {
				LOG.error("订单[" + orderinfo.getOrderNo() + "]更改失败，修改状态为："
						+ operationType);
			} else {
				LOG.error("订单[" + orderNo + "]获取数据失败，插入更改记录取消");
			}
		}
	}

	// 更新线上 自动测量 状态
	class batchUpdateShippingPackageThread extends Thread {
		private List<Map<String, String>> listmap;

		public batchUpdateShippingPackageThread(
				List<Map<String, String>> listmap) {
			super();
			this.listmap = listmap;
		}

		@Override
		public synchronized void run() {
			try {
				StringBuilder sqls;
				for(Map<String, String> map:listmap){
					sqls=new StringBuilder();
					sqls.append("update shipping_package set sflag='2'");
					if(map.get("sweight")!=null){
						sqls.append(",sweight='"+map.get("sweight")+"'");
					}
					if(map.get("svolume")!=null){
						sqls.append(",svolume='"+map.get("svolume")+"'");
					}
					if(map.get("volumeweight")!=null){
						sqls.append(",volumeweight='"+map.get("volumeweight")+"'");
					}
					if(map.get("shipmentno")!=null){
						sqls.append(" where shipmentno='"+map.get("shipmentno")+"'");
						iWarehouseService.insertMqLog(sqls.toString(), map.get("shipmentno"), "自动测量",map.toString());
						NotifyToCustomerUtil.sendSqlByMq(sqls.toString());
					}
				}
			} catch (Exception e) {
				LOG.error("更新线上自动测量 重量", e);
			}

		}
	}

	/**
	 * 更新采购线上采购状态
	 *
	 * @author cerong
	 *
	 */
	class PurchaseConfirmThread extends Thread {
		private Map<String, String> map;

		public PurchaseConfirmThread(Map<String, String> map) {
			super();
			this.map = map;
		}

		@Override
		public synchronized void run() {
			try {
				LOG.info("更新线上order_details采购确认状态:" + map.get("purchase_state"));
				SendMQ sendMQ = new SendMQ();
				StringBuilder sql=new StringBuilder();
				sql.append("update order_details set purchase_state='"+map.get("purchase_state")+"',purchase_time =");
				if(!"3".equals(map.get("purchase_state"))){
					sql.append("null");
				}
				if("3".equals(map.get("purchase_state"))){
					sql.append("now()");
				}
				sql.append(" where orderid='"+map.get("orderid")+"' and id='"+map.get("odid")+"'");
//				map.clear();
				sendMQ.sendMsg(new RunSqlModel(sql.toString()));
				sendMQ.closeConn();
			} catch (Exception e) {
				LOG.error("更新线上order_details采购确认状态", e);
			}

		}
	}

	/**
	 * 更新采购线上采购状态 //逻辑:当前订单的下的所有商品如果都是已经采购，那么修改订单状态为1 采购中。
	 *
	 * @author cerong
	 *
	 */
	class UpdateOrderThread extends Thread {
		private Map<String, String> map;

		public UpdateOrderThread(Map<String, String> map) {
			super();
			this.map = map;
		}

		@Override
		public synchronized void run() {
			try {
				LOG.info("更新线上orderinfo state为" + map.get("state") + ",订单号:"
						+ map.get("orderid"));
				//DataSourceSelector.set("dataSource127hop");
				// jxw 2017-4-13添加订单状态判断,
				boolean isCheck = CheckCanUpdateUtil
						.updateOnlineOrderInfoByLocal(map.get("orderid"),
								Integer.valueOf(map.get("state")),
								Integer.valueOf(map.get("adminId")));
				if (isCheck) {
					//iWarehouseService.updateOrder(map);
					String sql = "update orderinfo set state = '"+map.get("state")+"' where order_no='"+map.get("orderid")+"'";
					NotifyToCustomerUtil.sendSqlByMq(sql);

                    //判断订单状态不一致，进行消息提醒
                    if(!map.get("state").equals(map.get("old_state"))){
                        NotifyToCustomerUtil.updateOrderState(Integer.valueOf(map.get("user_id")),map.get("orderid"),
                                Integer.valueOf(map.get("old_state")),Integer.valueOf(map.get("state")));
                    }

				}
				//DataSourceSelector.restore();
			} catch (Exception e) {
				e.getStackTrace();
				LOG.error("更新线上orderinfo state状态失败 ,订单号:" + map.get("orderid"),
						e);
				// jxw 2017-4-16 插入失败，插入信息放入失败记录表中
				String sqlStr = "update orderinfo set state ='"
						+ map.get("state") + "' where order_no = '"
						+ map.get("orderid") + "';";
				ErrorLogDao.insertErrorInfo("orderinfo", sqlStr,
						Integer.valueOf(map.get("adminId")),
						Integer.valueOf(map.get("state")),
						"更新失败，" + e.getMessage());
			}

		}
	}

	class UpdateChildOrderThread extends Thread {
		private Map<String, String> map;

		public UpdateChildOrderThread(Map<String, String> map) {
			super();
			this.map = map;
		}

		@Override
		public synchronized void run() {
			try {
				LOG.info("更新线上dropshiporder state为" + map.get("state")
						+ ",订单号:" + map.get("child_order_no"));
				SendMQ sendMQ = new SendMQ();
				sendMQ.sendMsg(new RunSqlModel("update dropshiporder set state= (SELECT IF ((select count(*) from ( select * from order_details where dropshipid='"+map.get("child_order_no")+"' " +
						"and state!=2 AND purchase_state=3) a)>0,'1','5')) where child_order_no='"+map.get("child_order_no")+"'"));
				sendMQ.closeConn();
			} catch (Exception e) {
				e.getStackTrace();
				LOG.error(
						"更新线上dropshiporder state状态失败,订单号:"
								+ map.get("child_order_no"), e);
			}

		}
	}

	/**
	 * 同步更新线上出货补录订单信息
	 *
	 * @author admin
	 *
	 */
	class BuluOrderinfoThread extends Thread {

		private List<Map<String, Object>> bgList;

		public BuluOrderinfoThread(List<Map<String, Object>> bgList) {
			super();
			this.bgList = bgList;
		}

		@Override
		public synchronized void run() {
			try {
				// 判断是否开启线下同步线上配置
				if (GetConfigureInfo.openSync()) {
					String orderId, businessType, tableName, sqlStr = null;
					for (Map<String, Object> map : bgList) {
						orderId = map.get("orderid").toString(); // 订单号
						businessType = "出库  补录信息"; // 业务类型
						tableName = "shipping_package"; // 执行表名称
						sqlStr = " insert into shipping_package(orderid,remarks,createtime,sweight,svolume,volumeweight ,sflag ,transportcompany ,expressno, estimatefreight ,flag) values "
								+ "('"
								+ map.get("orderid").toString()
								+ "','"
								+ map.get("remarks").toString()
								+ "','"
								+ map.get("createtime").toString()
								+ "','"
								+ map.get("weight").toString()
								+ "','"
								+ map.get("svolume").toString()
								+ "','"
								+ map.get("volumeweight").toString()
								+ "',3,'"
								+ map.get("transport").toString()
								+ "' "
								+ ",'"
								+ map.get("expressno").toString()
								+ "','"
								+ map.get("estimatefreight").toString()
								+ "',1) "; // 执行SQL体
						SaveSyncTable.InsertOnlineDataInfo(0, orderId,
								businessType, tableName, sqlStr);
					}
				} else {
					SendMQ sendMQ = new SendMQ();
					StringBuilder sql=new StringBuilder();
					sql.append("insert into shipping_package(orderid,remarks,createtime,sweight,svolume,volumeweight ,sflag ,transportcompany ,expressno, estimatefreight  ,flag) values ");
					for(int i=0;i<bgList.size();i++){
						Map<String, Object> map=bgList.get(i);
						sql.append("('"+map.get("orderid")+"','"+map.get("remarks")+"','"+map.get("createtime")+"','"+map.get("weight")+"','"+map.get("svolume")+"','"+map.get("volumeweight")+"',3,'"+map.get("transport")+"','"+map.get("expressno")+"','"+map.get("estimatefreight")+"',1),");
					}
					sendMQ.sendMsg(new RunSqlModel(sql.toString().substring(0,sql.toString().length()-1)));
					sendMQ.closeConn();
				}
			} catch (Exception e) {
				LOG.error("线上出运插入补录订单 shipping_package ", e);
			}

		}
	}

	/**
	 * 销售直接取消商品(分为正常订单，dropship订单)
	 *
	 * @author cjc 4.12
	 *
	 */
	@SuppressWarnings({ "null", "unused" })
	@RequestMapping("/deleteOrderGoods")
	public @ResponseBody
    Object deleteOrderGoods(HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException {
		IZoneServer zone = new ZoneServer();
		String orderNo = request.getParameter("orderNo");// 订单id
		int goodId = Integer.parseInt(request.getParameter("goodId"));// 商品id
		int purchase_state = Integer.parseInt(request
				.getParameter("purchase_state"));// 是否确认货源
		int userId = Integer.parseInt(request.getParameter("userId"));// 用户id
		int res = 0;
		Integer[] deleteCtpoOrderGoods = { 1 };
		// 判断是否是drop ship订单,取消商品重新计算运费和产品金额,退回用户余额
		Dropshiporder dropShipOrder = dropshiporderService.getDropShipOrder(
				orderNo, userId);
		// 获取当前订单的货币单位
		String currency = null;
		// 获取用户当前的余额
		double userAccount = Double.parseDouble(String.valueOf(dao
				.getBalance_currency(userId).get("available_m")));

		if (dropShipOrder != null) {
			currency = dropShipOrder.getCurrency().toString();
			if ("5".equals(dropShipOrder.getState())
					|| "1".equals(dropShipOrder.getState())) {
				// 获取droship订单的信息，根据dropshipid 获取order_details 中子订单的
				List<OrderDetailsBean> orderdetailsList = orderService
						.getDelOrderByDropshipIdAndStatus(orderNo, goodId);
				// 订单内没取消的商品数大于1的话则计算应退回的金额,否则直接退回该订单总金额
				if (orderdetailsList.size() > 1) {
					// 取消商品应退回的总金额
					BigDecimal cancelTotalPrice = new BigDecimal(0.0);
					// 取消商品应退回的产品金额
					BigDecimal productprice = new BigDecimal(0.0);
					// 取消商品对应的重量
					BigDecimal productweight = new BigDecimal(0.0);
					// 取消产品的体积 od_bulk_volume
					BigDecimal productvolume = new BigDecimal(0.0);
					// 没有取消产品的体积，信息表中没有只能自己算
					BigDecimal productvolume1 = new BigDecimal(0.0);
					// 取消商品对应数量
					int productNum = 0;
					// 剩余产品金额
					BigDecimal remainPrice = new BigDecimal(0.0);
					// 该订单余下的重量
					BigDecimal remainWeight = new BigDecimal(0.0);
					// 该订单余下的体积
					BigDecimal remainvolume = new BigDecimal(0.0);
					// 剩下的商品重量,类别数组
					List<Double> weightList = new ArrayList<Double>();
					List<String> typeList = new ArrayList<String>();
					// 获取订单的运输方式
					// String fname =
					// dropShipOrder.getModeTransport().split(1,"@");
					String fname1 = dropShipOrder.getModeTransport().substring(
							0, dropShipOrder.getModeTransport().indexOf("@"));
					for (OrderDetailsBean orderDetailsBean : orderdetailsList) {
						if (orderDetailsBean.getGoodsid() == goodId) {
							// 产品金额累加
							productprice = productprice.add(
									new BigDecimal(orderDetailsBean
											.getGoodsprice())
											.multiply(new BigDecimal(
													orderDetailsBean
															.getYourorder())))
									.setScale(2, BigDecimal.ROUND_HALF_UP);
							// 重量累加
							productweight = productweight.add(
									new BigDecimal(orderDetailsBean
											.getOd_total_weight())).setScale(2,
									BigDecimal.ROUND_HALF_UP);
							// 体积累加
							try {

								productvolume = productvolume.add(
										new BigDecimal(orderDetailsBean
												.getOd_bulk_volume()))
										.setScale(2, BigDecimal.ROUND_UP);
							} catch (Exception e) {
							}
							// 数量累加
							productNum += orderDetailsBean.getYourorder();
						} else {
							productvolume1 = productvolume.add(
									new BigDecimal(orderDetailsBean
											.getOd_bulk_volume())).setScale(2,
									BigDecimal.ROUND_UP);
							weightList.add(orderDetailsBean
									.getOd_total_weight());
							typeList.add(orderDetailsBean.getGoodscatid());
						}
					}
					// 剩余的产品价格
					remainPrice = new BigDecimal(dropShipOrder.getProductCost()).subtract(productprice).setScale(2, BigDecimal.ROUND_HALF_UP);
					// 剩余的产品重量
					remainWeight = new BigDecimal(dropShipOrder.getActualWeightEstimate()).subtract(productweight).setScale(2, BigDecimal.ROUND_HALF_UP);
					// 剩余的产品的体积
					remainvolume = productvolume1.subtract(productvolume).setScale(2, BigDecimal.ROUND_UP);
					// 获取当前订单的国家id
					String countryId = orderService.getCountryIdFromOrderNo(orderNo);
					// 根据调用后台运费接口计算出剩下的产品重量对应的运费
					List<ShippingBean> orderFreight = zone.getShippingBeans(Integer.parseInt(countryId), new double[] { Double.valueOf(remainWeight.toString()) }, new double[] { Double.valueOf(remainPrice.toString()) }, new double[] { Double.valueOf(remainWeight.toString()) }, new String[] { "0" }, new int[] { 1 });
					String freight1 = null;
					for (ShippingBean orderFreight_ : orderFreight) {
						if (orderFreight_.getName().equals(fname1)) {
							freight1 = String.valueOf(orderFreight_.getResult());
						}
					}
					// 取消的商品对应的运费
					BigDecimal backFright = new BigDecimal(dropShipOrder.getForeignFreight()).subtract(new BigDecimal(freight1).setScale(2, BigDecimal.ROUND_HALF_UP));
					// 统计需要退回的总金额
					cancelTotalPrice = cancelTotalPrice.add(productprice).add(backFright).setScale(2, BigDecimal.ROUND_HALF_UP);
					dropShipOrder.setModeTransport(dropShipOrder.getModeTransport().replace(dropShipOrder.getForeignFreight(), freight1 + ""));
					dropShipOrder.setProductCost(remainPrice.toString());
					dropShipOrder.setForeignFreight(freight1 + "");// 国外运费
					dropShipOrder.setPayPriceTow(freight1 + "");// 运费
					dropShipOrder.setPayPrice(remainPrice.add(new BigDecimal(freight1)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					dropShipOrder.setDetailsNumber(dropShipOrder.getDetailsNumber() - productNum);
					dropShipOrder.setActualWeightEstimate(remainWeight.doubleValue());
					if (dropShipOrder.getDetailsNumber() == productNum && dropShipOrder.getProductCost().equals(productprice)) {
						dropShipOrder.setState("-1");// 取消订单
					}
					// 更新子订单表(dropshiporder)
					try {
						DataSourceSelector.set("dataSource127hop");
						dropshiporderService.updateDropShipOrder(dropShipOrder);
						LOG.info("更新子订单表信息 :" + dropShipOrder.getChildOrderNo());
					} catch (Exception e) {
						LOG.error("更新子订单表信息", e);
					}
                    // 更新主订单表(orderinfo)
                    Map<String, Object> updateMap = new HashMap<String, Object>();
                    updateMap.put("productcost", productprice);
                    updateMap.put("foreignfreight", backFright);
                    updateMap.put("detailsnumber", productNum);
                    updateMap.put("actualweightestimate", productweight);
                    updateMap.put("orderno", dropShipOrder.getParentOrderNo());
                    updateMap.put("userid", userId);
                    // 更新余额变更表
                    // 添加充值记录
                    RechargeRecord rr = new RechargeRecord();
                    rr.setUserid(userId);
                    rr.setPrice(cancelTotalPrice.doubleValue());
                    rr.setRemark_id(orderNo);
                    rr.setType(1);
                    rr.setBalanceAfter(userAccount + cancelTotalPrice.doubleValue());
                    rr.setRemark("cancel:" + orderNo + ",goodsid:" + goodId);
                    rr.setCurrency(currency);
					// 更新order_details信息状态为2
					try {
                        SendMQ sendMQ = new SendMQ();
                        sendMQ.sendMsg(new RunSqlModel("update order_details set state = 2 where goodsid='"+goodId+"' and dropshipid='"+orderNo+"'"));
                        sendMQ.sendMsg(new RunSqlModel("update order_change set del_state=1 where goodId='"+goodId+"' and orderNo='"+dropShipOrder.getParentOrderNo()+"'"));
                        StringBuilder sql=new StringBuilder();
                        sql.append("update orderinfo set product_cost = round(product_cost-"+updateMap.get("productcost")+",2),foreign_freight = round(foreign_freight-"+updateMap.get("foreignfreight")+",2)," +
                                "pay_price = round(pay_price-"+updateMap.get("productcost")+"-"+updateMap.get("foreignfreight")+",2),pay_price_tow = round(pay_price_tow-"+updateMap.get("foreignfreight")+",2)," +
                                "details_number = round(details_number-"+updateMap.get("detailsnumber")+",0),actual_weight_estimate=round(actual_weight_estimate-"+updateMap.get("actualweightestimate")+",2) ");
                        if(StringUtil.isNotBlank(updateMap.get("state").toString())){
                            sql.append(",state = '"+updateMap.get("state")+"' ");
                        }
                        if(StringUtil.isNotBlank(updateMap.get("server_update").toString())){
                            sql.append(",server_update = '"+updateMap.get("server_update")+"'  ");
                        }
                        sql.append("where order_no = '"+updateMap.get("orderno")+"' and user_id ='"+updateMap.get("userid")+"'");
                        sendMQ.sendMsg(new RunSqlModel(sql.toString()));
                        sendMQ.sendMsg(new RunSqlModel("insert recharge_record(userid,price,type,remark,remark_id,datatime,usesign,currency,balanceAfter) values('"+rr.getUserid()+"', '"+rr.getPrice()+"','"+rr.getType()+"', '"+rr.getRemark()+"','"+rr.getRemark_id()+"', now(), '"+rr.getUsesign()+"','"+rr.getCurrency()+"','"+rr.getBalanceAfter()+"')"));
                        sendMQ.closeConn();
						LOG.info("更新子订单详情表信息 :"+ dropShipOrder.getChildOrderNo());
					} catch (Exception e) {
						LOG.error("更新子订单详情表信息", e);
					}
					// 更新用户表余额
					int rc = 0;
					try {
						DataSourceSelector.set("dataSource127hop");
						rc = orderService.upUserPrice(userId, cancelTotalPrice.doubleValue());
						LOG.info("更新用户表余额(user) :" + userId);
						DataSourceSelector.restore();
					} catch (Exception e) {
						LOG.error("更新用户表余额(user)", e);
					}
					deleteCtpoOrderGoods[0] = rc;
				} else {
					// 支付的价格
					String productCost = dropShipOrder.getProductCost();
					String freight = dropShipOrder.getForeignFreight();
					String price = dropShipOrder.getPayPrice();
					int productNum = dropShipOrder.getDetailsNumber();
					BigDecimal productweight = new BigDecimal(dropShipOrder.getActualWeightEstimate()).setScale(2, BigDecimal.ROUND_HALF_UP);
					dropShipOrder.setProductCost("0.00");
					dropShipOrder.setForeignFreight("0.00");
					dropShipOrder.setPayPriceTow("0.00");
					dropShipOrder.setPayPrice("0.00");
					dropShipOrder.setState("-1");// 取消订单
					// 更新子订单表(dropshiporder)
					try {
						DataSourceSelector.set("dataSource127hop");
						dropshiporderService.updateDropShipOrder(dropShipOrder);
						LOG.info("更新子订单表(dropshiporder) :"+ dropShipOrder.getChildOrderNo());
						DataSourceSelector.restore();
					} catch (Exception e) {
						LOG.error("更新子订单表(dropshiporder)", e);
					}
					// 更新order_details信息状态为2
					try {
                        SendMQ sendMQ = new SendMQ();
                        sendMQ.sendMsg(new RunSqlModel("update order_details set state = 2 where goodsid='"+goodId+"' and dropshipid='"+orderNo+"'"));
                        sendMQ.sendMsg(new RunSqlModel("update order_change set del_state=1 where goodId='"+goodId+"' and orderNo='"+dropShipOrder.getParentOrderNo()+"'"));
                        sendMQ.closeConn();
					} catch (Exception e) {
						LOG.error("更新order_details信息状态为2", e);
					}

					// 更新主订单表(orderinfo)
					Map<String, Object> updateMap = new HashMap<String, Object>();
					// 查看当前子订单所属的主订单下是否还有没取消的订单,如果当前子订单是主订单下的最后一个订单,且是最后一件商品,则更改整个主订单状态
					List<Dropshiporder> dropshiporderList = new ArrayList<Dropshiporder>();
					try {
						DataSourceSelector.set("dataSource127hop");
						dropshiporderList = dropshiporderService.getDropshiporderList(dropShipOrder.getParentOrderNo(), userId, "5");
						LOG.info("查看当前子订单所属的主订单下是否还有没取消的订单:" + dropShipOrder.getParentOrderNo());
						DataSourceSelector.restore();
					} catch (Exception e) {
						LOG.error("查看当前子订单所属的主订单下是否还有没取消的订单", e);
					}
					if (dropshiporderList.size() == 0) {
						updateMap.put("state", -1);
					}

					updateMap.put("productcost", productCost);
					updateMap.put("foreignfreight", freight);
					updateMap.put("detailsnumber", productNum);
					updateMap.put("actualweightestimate", productweight);
					updateMap.put("orderno", dropShipOrder.getParentOrderNo());
					updateMap.put("userid", userId);
					try {
					    SendMQ sendMQ=new SendMQ();
					    StringBuilder sql=new StringBuilder();
                        sql.append("update orderinfo set product_cost = round(product_cost-"+updateMap.get("productcost")+",2),foreign_freight = round(foreign_freight-"+updateMap.get("foreignfreight")+",2)," +
                                "pay_price = round(pay_price-"+updateMap.get("productcost")+"-"+updateMap.get("foreignfreight")+",2),pay_price_tow = round(pay_price_tow-"+updateMap.get("foreignfreight")+",2)," +
                                "details_number = round(details_number-"+updateMap.get("detailsnumber")+",0),actual_weight_estimate=round(actual_weight_estimate-"+updateMap.get("actualweightestimate")+",2) ");
                        if(StringUtil.isNotBlank(updateMap.get("state").toString())){
                            sql.append(",state = '"+updateMap.get("state")+"' ");
                        }
                        if(StringUtil.isNotBlank(updateMap.get("server_update").toString())){
                            sql.append(",server_update = '"+updateMap.get("server_update")+"'  ");
                        }
                        sql.append("where order_no = '"+updateMap.get("orderno")+"' and user_id ='"+updateMap.get("userid")+"'");
                        sendMQ.sendMsg(new RunSqlModel(sql.toString()));
                        sendMQ.closeConn();
					} catch (Exception e) {
						LOG.error("更新orderinfo信息", e);
					}
					// 更新余额变更表
					// 添加充值记录
					RechargeRecord rr = new RechargeRecord();
					rr.setUserid(userId);
					rr.setPrice(Double.parseDouble(price));
					rr.setRemark_id(orderNo);
					rr.setType(1);
					rr.setBalanceAfter(userAccount + Double.parseDouble(price)); // available_m=available_m+(#{price})
					rr.setRemark("cancel:" + orderNo + ",goodsid:" + goodId);
					rr.setCurrency(currency);
					try {
                        SendMQ sendMQ = new SendMQ();
                        sendMQ.sendMsg(new RunSqlModel("insert recharge_record(userid,price,type,remark,remark_id,datatime,usesign,currency,balanceAfter) values('"+rr.getUserid()+"', '"+rr.getPrice()+"','"+rr.getType()+"', '"+rr.getRemark()+"','"+rr.getRemark_id()+"', now(), '"+rr.getUsesign()+"','"+rr.getCurrency()+"','"+rr.getBalanceAfter()+"')"));
                        sendMQ.closeConn();
					} catch (Exception e) {
						LOG.error("更新余额变更表(rechagerecord)", e);
					}
					// 更新用户表
					int rc = 0;
					try {
						DataSourceSelector.set("dataSource127hop");
						rc = orderService.upUserPrice(userId,Double.parseDouble(price));
						LOG.info("更新用户表余额:" + dropShipOrder.getParentOrderNo());
						DataSourceSelector.restore();
					} catch (Exception e) {
						LOG.error("更新用户表余额", e);
					}
					deleteCtpoOrderGoods[0] = rc;
				}
			} else {
				LOG.error("取消dropship订单的商品出错！");
			}
		} else {
			try {
				DataSourceSelector.set("dataSource127hop");
				deleteCtpoOrderGoods = iWarehouseService.deleteOrderGoods(orderNo, goodId, userId,purchase_state, null, response);
				LOG.info("直接取消商品:" + deleteCtpoOrderGoods[0]);
				DataSourceSelector.restore();
			} catch (Exception e) {
				LOG.error("直接取消商品失败：", e);
			}
		}
		return deleteCtpoOrderGoods[0];
	}

	public  String getSignature(String data,String key) throws Exception{
		byte[] keyBytes=key.getBytes();
		SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(data.getBytes());
		StringBuilder sb=new StringBuilder();
		for(byte b:rawHmac){
			sb.append(byteToHexString(b));
		}
		return org.apache.commons.lang.StringUtils.upperCase(sb.toString());
	}

	private  String byteToHexString(byte ib){
		char[] Digit={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		char[] ob=new char[2];
		ob[0]=Digit[(ib>>>4)& 0X0f];
		ob[1]=Digit[ib & 0X0F];
		String s=new String(ob);
		return s;
	}

	// 更具ali的链接获取我司链接
	@RequestMapping(value = "/ourLink", method = RequestMethod.POST)
	@ResponseBody
	public String getOurLink(HttpServletRequest request,
							 HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String url = request.getParameter("url");
		String ourLink = GetCompanyName.OurLink(url);
		request.setAttribute("ourLink", ourLink);
		return ourLink;
	}

	/**
	 * @Method: makeFileName
	 * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
	 * @param filename
	 *            文件的原始名称
	 * @return uuid+"_"+文件的原始名称
	 */
	private String makeFileName(String filename) { // 2.jpg
		// 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
		return UUID.randomUUID().toString() + "_" + filename;
	}



	@RequestMapping(value = "/uploadVideo", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult uploadVideo(@RequestParam(value = "pid", required = true) String pid, @RequestParam(value = "uploadfile", required = true) MultipartFile file, HttpServletRequest request) {
		JsonResult json = new JsonResult();
		boolean flag=false;
		boolean success = false;
		System.out.println("pid:" + pid);
		try{
			if (!file.isEmpty() && !StringUtils.isStrNull(pid)) {
				SimpleDateFormat order=new SimpleDateFormat("ddHHmm");
				Date data=new Date();
				String dir="";
				String old_name=pid+".mp4";
				// 本地服务器磁盘全路径
				String localFilePath ="2020-08/video/" +old_name;
				// 文件流输出到本地服务器指定路径
				if (ftpConfig == null) {
					ftpConfig = GetConfigureInfo.getFtpConfig();
				}
				String imgUploadPath = ftpConfig.getLocalDiskPath();
				String imgPath = imgUploadPath + localFilePath;
				System.out.println("新上传的文件名："+old_name);
				System.out.println("新上传的文件路径："+imgPath);
				flag=ImgDownload.writeImageToDisk1(file.getBytes(), imgPath);
				if(flag){
					String ip =Util.PIC_IP;
					String user = Util.PIC_USER;
					String passw = Util.PIC_PASS;
					String remotePath =Util.PIC_PATH;
					String fileName = old_name;
					String oriFilePath = imgPath;
					success = NewFtpUtil.uploadFileToRemote(ip, 21, user, passw, remotePath, fileName, oriFilePath);
					System.out.println("success :" + success);
				}
				json.setOk(success);
			}else{
				json.setOk(success);
			}
		}catch(Exception e){
			json.setOk(false);
			e.printStackTrace();
		}
		return json;
	}


	@RequestMapping(value = "/uploadTypeNewPictures", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult uploadTypeNewPictures(@RequestParam(value = "new_pid", required = true) String new_pid, @RequestParam(value = "uploadfile1", required = true) MultipartFile file, HttpServletRequest request) {
		JsonResult json = new JsonResult();
		boolean flag=false;
		System.out.println("new_pid:" + new_pid);
		try{
			if (!file.isEmpty() && !StringUtils.isStrNull(new_pid)) {
				long tims=System.currentTimeMillis();
				String dir="";
				String old_name=new_pid+"_new.jpg";
				// 本地服务器磁盘全路径
				String localFilePath = DateFormatUtil.getCurrentYearAndMonth() + "/" + tims+old_name;
				// 文件流输出到本地服务器指定路径
				if (ftpConfig == null) {
					ftpConfig = GetConfigureInfo.getFtpConfig();
				}
				String imgUploadPath = ftpConfig.getLocalDiskPath();
				String imgPath = imgUploadPath.replace("\\","/") + localFilePath;
				System.out.println("新上传的文件名："+(tims+old_name));
				System.out.println("新上传的文件路径："+imgPath);
				flag=ImgDownload.writeImageToDisk1(file.getBytes(), imgPath);
				if(flag){
					// flag=NewFtpUtil.uploadFileToRemote(Util.PIC_IP, 21, Util.PIC_USER, Util.PIC_PASS, "/inspectionImg/", localFilePath, imgPath);
					flag = UploadByOkHttp.uploadFile(new File(imgPath),UPLOAD_IMG_PATH + localFilePath.substring(0,localFilePath.lastIndexOf("/")), 0);
				}
				int row=0;
				if(flag){
					//插入数据inspection_picture
					row=iWarehouseService.insertInspectionPicture(new_pid,Util.PIC_URL+localFilePath+"");
					if(row>0){
						DataSourceSelector.set("dataSource127hop");
						row=iWarehouseService.insertInspectionPicture(new_pid,Util.PIC_URL+localFilePath+"");
						DataSourceSelector.restore();
					}
				}
				if(row>0){
					flag=true;
				}
				json.setOk(flag);
			}else{
				json.setOk(flag);
			}
		}catch(Exception e){
			json.setOk(false);
			e.printStackTrace();
		}
		return json;
	}
	@RequestMapping(value = "/reply", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult reply(@RequestParam(value = "gbookid", required = true) String gbookid,
                            @RequestParam(value = "replyContent1", required = true) String replyContent1,
                            @RequestParam(value = "websiteType", defaultValue = "1", required = false) Integer websiteType, //网站名
                            @RequestParam(value = "uploadfile1", required = true) MultipartFile file, HttpServletRequest request) {
		JsonResult json = new JsonResult();
		boolean flag=false;
		IGuestBookService ibs = new GuestBookServiceImpl();
		MessagesService messagesService = SpringContextUtil.getBean("MessagesService",MessagesService.class);
		try{
			json.setOk(false);
			int id  = 0;
			int count=0;
			String qustion="";
			String name="";
			String pname="";
			String email="";
			String purl="";
			String userId="";
			String sale_email="";
			String picPath="";
			if (!file.isEmpty()){
				long tims=System.currentTimeMillis();
				String dir="";
				String old_name=gbookid+"guestbook.jpg";
				// 本地服务器磁盘全路径
				if (ftpConfig == null) {
					ftpConfig = GetConfigureInfo.getFtpConfig();
				}
				// 检查配置文件信息是否正常读取
				String imgUploadPath = ftpConfig.getLocalDiskPath();
				String localFilePath = DateFormatUtil.getCurrentYearAndMonth() + "/" + tims+old_name;
				// 文件流输出到本地服务器指定路径
				System.out.println("新上传的文件名："+(tims+old_name));
				System.out.println("新上传的文件路径："+(imgUploadPath + localFilePath));
				flag=ImgDownload.writeImageToDisk1(file.getBytes(), imgUploadPath + localFilePath);
				if(flag){
					// flag=NewFtpUtil.uploadFileToRemote(Util.PIC_IP, 21, Util.PIC_USER, Util.PIC_PASS, "/inspectionImg/", localFilePath, imgUploadPath + localFilePath);
					flag = UploadByOkHttp.uploadFile(new File(imgUploadPath + localFilePath),UPLOAD_IMG_PATH + localFilePath.substring(0,localFilePath.lastIndexOf("/")), 0);
				}
				int row=0;
				if(flag){
					picPath=Util.PIC_URL+localFilePath;
					row=iWarehouseService.updateQuestPicPath(gbookid,Util.PIC_URL+localFilePath+"");
					if(row>0){
						json.setOk(true);
						SendMQ sendMQ=new SendMQ();
						sendMQ.sendMsg(new RunSqlModel("update guestbook set picPath='"+Util.PIC_URL+localFilePath+"' where id="+gbookid+""));
						sendMQ.closeConn();
					}
				}
			}
			if(StringUtil.isNotBlank(gbookid) && StringUtil.isNotBlank(replyContent1)) {
				id = Integer.parseInt(gbookid);
				GuestBookBean g=ibs.getGuestBookBean(gbookid);
				int  questionType = g.getQuestionType();
				if(questionType==2){
					qustion = "orderQuantity: "+g.getOrderQuantity()+"; targetPrice: "+g.getTargetPrice() ;
				}else if(questionType==3){
					qustion ="orderQuantity: "+g.getOrderQuantity()+"; customizationNeed: "+g.getCustomizationNeed();
				}else{
					qustion=g.getContent();
				}

				name=g.getUserName();
				pname=g.getPname();
				email=g.getEmail()==null?"":g.getEmail();
				userId=String.valueOf(g.getUserId());
				purl=g.getPurl();
				sale_email=g.getSale_email();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				Date now = new Date();
				dateFormat.setLenient(false);
				String date = dateFormat.format(now);
				// 发送邮件等 原方法不能注入 邮件发送转移位置
                tabCouponService.SendGuestbook(id, replyContent1,date,name,qustion,pname,email,Integer.parseInt(userId),purl,sale_email,picPath, websiteType);
                count = ibs.reply(id, replyContent1,date,name,qustion,pname,email,Integer.parseInt(userId),purl,sale_email,picPath);
				if(count>0){
					json.setOk(true);
				}
			}
		}catch(Exception e){
			json.setOk(false);
			e.printStackTrace();
		}
		return json;
	}


	/**
	 * 使用Ajax异步上传图片
	 *
	 * @param pic 封装图片对象
	 * @param request
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping(value = "/uploadPic", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult uploadPic(@RequestParam(value = "pid", required = true) String pid, @RequestParam(value = "orderid", required = true) String orderid,
                                @RequestParam(value = "goodsid", required = true) String goodsid, @RequestParam(value = "pic", required = true) String pic, @RequestParam(value = "i_id", required = true) String i_id,
                                @RequestParam(value = "uploadfile", required = true) MultipartFile file, HttpServletRequest request) {
		JsonResult json = new JsonResult();
		boolean flag=false;
		System.out.println("pid:" + pid);
		System.out.println("orderid:" + orderid);
		System.out.println("goodsid:" + goodsid);
		System.out.println("pic:" + pic);
		System.out.println("i_id:"+i_id);
		try{
			if (!file.isEmpty() && !StringUtils.isStrNull(pic)) {
				long tims=System.currentTimeMillis();
				String dir="";
				String old_name="";
				if(pic.contains("http")){
					String pics []=pic.split("inspectionImg/")[1].split("/");
					dir=pics[0];//目录
					old_name=pics[1];//原始图片名称
				}else{
					String pics []=pic.split("/");
					dir=pics[0];
					old_name=pics[1];
				}
				// 本地服务器磁盘全路径
				String localFilePath = DateFormatUtil.getCurrentYearAndMonth() + "/" + tims+old_name;
				// 文件流输出到本地服务器指定路径
				if (ftpConfig == null) {
					ftpConfig = GetConfigureInfo.getFtpConfig();
				}
				String imgUploadPath = ftpConfig.getLocalDiskPath();
				String imgPath = imgUploadPath + localFilePath;
				System.out.println("新上传的文件名："+(tims+old_name));
				System.out.println("新上传的文件路径："+imgPath);
				flag=ImgDownload.writeImageToDisk1(file.getBytes(), imgPath);
				if(flag){
					flag=uploadPic(localFilePath, imgPath,orderid,goodsid,1,i_id);
				}
				json.setOk(flag);
			}else{
				json.setOk(flag);
			}
		}catch(Exception e){
			json.setOk(false);
			e.printStackTrace();
		}
		return json;
	}


	public boolean uploadPic(String storePath,String imgPath,String orderid,String goodsid,int index,String i_id){
		boolean flag=false;
		try {
			// flag=NewFtpUtil.uploadFileToRemote(Util.PIC_IP, 21, Util.PIC_USER, Util.PIC_PASS, "/inspectionImg/", storePath, imgPath);
			flag = UploadByOkHttp.uploadFile(new File(imgPath),UPLOAD_IMG_PATH + storePath.substring(0,storePath.lastIndexOf("/")), 0);
			if(flag){
				Connection conn = DBHelper.getInstance().getConnection2();// 仓库不用
				Connection conn1 = DBHelper.getInstance().getConnection();
				PreparedStatement stmt = null;
				String sql = "UPDATE order_details t SET t.picturepath = ? WHERE t.orderid = ? AND t.goodsid = ?;";
				try {
					stmt = conn.prepareStatement(sql);// 仓库不用
					stmt.setString(1, Util.PIC_URL+storePath+"");
					stmt.setString(2, StringUtil.isBlank(orderid)?"999999":orderid);
					stmt.setString(3, goodsid);
					stmt.executeUpdate();
					stmt = conn1.prepareStatement(sql);
					stmt.setString(1, Util.PIC_URL+storePath+"");
					stmt.setString(2, StringUtil.isBlank(orderid)?"999999":orderid);
					stmt.setString(3, goodsid);
					stmt.executeUpdate();
					sql="UPDATE id_relationtable set picturepath='"+storePath+"' where orderid=? and goodid=?";
					stmt = conn1.prepareStatement(sql);
					stmt.setString(1, StringUtil.isBlank(orderid)?"999999":orderid);
					stmt.setString(2, goodsid);
					stmt.executeUpdate();
					sql="update inspection_picture set pic_path=? where id=? and isdelete=0";
					stmt = conn1.prepareStatement(sql);
					stmt.setString(1, Util.PIC_URL+storePath+"");
					stmt.setString(2, i_id);
					stmt.executeUpdate();
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, Util.PIC_URL+storePath+"");
					stmt.setString(2, i_id);
					stmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					DBHelper.getInstance().closePreparedStatement(stmt);
					DBHelper.getInstance().closeConnection(conn);
					DBHelper.getInstance().closeConnection(conn1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/** 出货验货中的出库照片上传  */
	@RequestMapping(value="/uploadImageFile", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> uploadImageFile(HttpServletRequest request) throws Exception{
		//保存上传的图片名
		List<String> uploadImgList = new ArrayList<String>();

		String orderid = request.getParameter("orderid");
		String goodid = request.getParameter("goodid");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
		String time = df.format(new Date());// new Date()为获取当前系统时间
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		//上传文件目录
		String relatDir = SearchFileUtils.LOCALPATHZIPIMG;
		//文件夹不存在则创建
		File fdir = new File(relatDir);
		File fdirExi = new File(relatDir + time + "/");
		if (!fdirExi.exists()) {
			fdirExi.mkdirs();
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles("file");	//多图片的
		for (MultipartFile multipartFile : files) {
			CommonsMultipartFile temFile = (CommonsMultipartFile) multipartFile;
			//上传文件名
			String imgName ="";
			if(goodid==null || "".equals(goodid)){
				imgName = time + "/" + orderid + "_" + (int)(Math.random() * 100000);
			}else{
				imgName = time + "/" + orderid + "_" + goodid;
			}
			String oriName = temFile.getOriginalFilename();
			imgName += oriName.substring(oriName.lastIndexOf("."));
			File tempFile = new File(fdir.getPath() + File.separator + imgName);
			temFile.transferTo(tempFile);
			AddInventoryThread a= new AddInventoryThread(imgName, tempFile.getPath(), orderid, goodid, 0);
			a.start();
			//支持断点续存上传图片,
			ContinueFTP2 f1=new ContinueFTP2(Util.PIC_IP, Util.PIC_USER, Util.PIC_PASS,
					"21", "/stock_picture/" + imgName,tempFile.getPath());
			//远程上传到图片服务器
			f1.start();
			//保存数据
			expressTrackDao.saveImgPathForInfo(Util.PIC_URL + imgName, orderid, imgName);

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("imgs", tempFile.getPath()));
			DownloadMain.postContentClient(ContentConfig.DOWNLOAD_LIST_URL, nvps);
//            String imgFiles=dao.getImgFiles(orderid);
//    		resp.put("status", status);

		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("uploadImgList", uploadImgList);
		result.put("orderid", orderid);
        result.put("imagehost", SearchFileUtils.IMAGEHOSTURL);
		return result;
	}
	@RequestMapping("/query")
	public String getFreightByCountryIdWeight(){
		OkHttpClient okHttpClient = new OkHttpClient();

		okhttp3.RequestBody formBody = new FormBody.Builder()
				.add("cdes", "美国")
				.add("cdes", "美国")
				.add("fweight","7.51")
				.add("itype","1")
				.add("w","cnexx")
				.add("cmodel","cmodel:emsprice")
				.add("type","20")
				.add("itype","1")
				.build();
		/*Request request = new Request.Builder().url(getFreightCostUrl).post(formBody).build();*/
		String url = "http://www.cne.com/cgi-bin/Ginfo.dll?EmsPriceQueryi";
		Request request = new Request.Builder().addHeader("Accept","*/*")
				.addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
				.url(url).post(formBody).build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			String resultStr = response.body().string();
			System.out.print(resultStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(value = "/encodeStr", method = RequestMethod.POST)
	@ResponseBody
	public String getUserInfo(String str){
		String encodeStr = "";
		if(null!=str && !"".equals(str)){
			try{
				encodeStr = URLEncoder.encode(DESUtils.encode(str));
			}catch(Exception e){
				LOG.error(" 加密用户名错误 USERID:{}",str);
			}
		}
		return encodeStr;
	}
	/**
	 * 产品单页视频图片编辑上传
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/UploadAll4",method = RequestMethod.POST)
	@ResponseBody
	public List<String>  UploadAll4(HttpServletRequest request) {
		Map<String,String> map = new HashMap<String,String>();
		List<String> list=new ArrayList<>();
		try {
			SendMQ sendMQ = new SendMQ();
			String filePath="";
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> fileList = multipartRequest.getFiles("file");
			if(fileList == null || fileList.size() == 0){
				System.out.println("请上传文件,注意文件的name属性为file");
			}
			for (int i=0;i<fileList.size();i++) {

				MultipartFile file = fileList.get(i);
				String filename1 = file.getOriginalFilename();
				String pid = filename1.replaceAll(".mp4", "").replaceAll("[^0-9]","");
				try {
					String VidoPath=this.iWarehouseService.getRepathByPid(pid);
					String Vpath=VidoPath.split("/")[4];
					System.out.println(filename1);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
				String time = df.format(new Date());// new Date()为获取当前系统时间
				String filename = pid + "_" + time;
				if (ftpConfig == null) {
					ftpConfig = GetConfigureInfo.getFtpConfig();
				}
				// 检查配置文件信息是否正常读取
				String imgUploadPath = ftpConfig.getLocalDiskPath();
				filePath = imgUploadPath  + file.getOriginalFilename();
				//filePath=imgUploadPath+"_"+filename+"_"+file.getOriginalFilename();
				FileOutputStream fs = new FileOutputStream(filePath);
				byte[] buffer = new byte[1024 * 1024];
				int bytesum = 0;
				int byteread = 0;
				InputStream stream = file.getInputStream();
				while ((byteread = stream.read(buffer)) != -1) {
					bytesum += byteread;
					fs.write(buffer, 0, byteread);
					fs.flush();
				}
				fs.close();
				stream.close();
				File video = new File(filePath);
				if (video.exists()) {
//					UploadByOkHttp.uploadFile(video,filePath);
					boolean flag = NewFtpUtil.uploadFileToRemote(Util.PIC_IP, 21, Util.PIC_USER, Util.PIC_PASS, "/"+Vpath+pid+"/", filename + "_" + file.getOriginalFilename(), filePath);
					if (flag) {
						map.put("msg", "1");
						map.put("goods_pid", pid);
						String path = "https://img.import-express.com/importcsvimg/"+Vpath+pid+"/" + (filename + "_" + file.getOriginalFilename()) + "";
						map.put("path", path);
						GoodsInfoUpdateOnlineUtil.videoUrlToOnlineByMongoDB(pid, path);
						iWarehouseService.updateCustomVideoUrl(map);
					}
				} else {
					list.add(pid);
				}
			} catch (Exception e) {
					list.add(pid);
			}

			}
			sendMQ.closeConn();
			if (list.size()==fileList.size()){
				list.clear();
				list.add("0");
			}
			if (list.size()==0){
				list.add("1");
			}
		} catch (Exception e) {
			list.add("0");
			return list;
		}
		return list;
	}
	public String  UploadAll2(MultipartFile file) {
		String list="1";
		String filePath = "";
		String filename1 = file.getOriginalFilename();
		String pid = filename1.replaceAll(".mp4", "").replaceAll("[^0-9]", "");
		try {
		SendMQ sendMQ = new SendMQ();
		Map<String, String> map = new HashMap<String, String>();

				String VidoPath = this.iWarehouseService.getRepathByPid(pid);
				String Vpath = VidoPath.split("/")[4];
				System.out.println(filename1);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
				String time = df.format(new Date());// new Date()为获取当前系统时间
				String filename = pid + "_" + time;
				if (ftpConfig == null) {
					ftpConfig = GetConfigureInfo.getFtpConfig();
				}
				// 检查配置文件信息是否正常读取
				String imgUploadPath = ftpConfig.getLocalDiskPath();
				filePath = imgUploadPath + file.getOriginalFilename();
				//filePath=imgUploadPath+"_"+filename+"_"+file.getOriginalFilename();
				FileOutputStream fs = new FileOutputStream(filePath);
				byte[] buffer = new byte[1024 * 1024];
				int bytesum = 0;
				int byteread = 0;
				InputStream stream = file.getInputStream();
				while ((byteread = stream.read(buffer)) != -1) {
					bytesum += byteread;
					fs.write(buffer, 0, byteread);
					fs.flush();
				}
				fs.close();
				stream.close();
				File video = new File(filePath);
				if (video.exists()) {
//					UploadByOkHttp.uploadFile(video,filePath);
					boolean flag = NewFtpUtil.uploadFileToRemote(Util.PIC_IP, 21, Util.PIC_USER, Util.PIC_PASS, "/" + Vpath + pid + "/", filename + "_" + file.getOriginalFilename(), filePath);
					if (flag) {
						map.put("msg", "1");
						map.put("goods_pid", pid);
						String path = "https://img.import-express.com/importcsvimg/" + Vpath + pid + "/" + (filename + "_" + file.getOriginalFilename()) + "";
						map.put("path", path);
						GoodsInfoUpdateOnlineUtil.videoUrlToOnlineByMongoDB(pid, path);
						iWarehouseService.updateCustomVideoUrl(map);
					}
				} else {
					list="0";
				}
			sendMQ.closeConn();
			} catch (Exception e) {
				list=pid;
			}

		return list;
	}
	@RequestMapping(value ="/UploadAll",method = RequestMethod.POST)
	@ResponseBody
	public  List<String> UploadAll(HttpServletRequest request) {
		List<String> list=new ArrayList<>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> fileList = multipartRequest.getFiles("file");
		if(fileList == null || fileList.size() == 0){
			System.out.println("请上传文件,注意文件的name属性为file");
		}
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < fileList.size(); i++) {
			final int index = i;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cachedThreadPool.execute(new Runnable() {
				public void run() {
					String pid=UploadAll2(fileList.get(index));
					list.add(pid);
				}
			});
		}
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (list.size()>0) {
			for (int i = list.size() - 1; i >= 0; i--) {
				if ("1".equals(list.get(i))) {
					list.remove(i);
				}
			}
			list.remove("1");
			if (list.size() == fileList.size()) {
				list.clear();
				list.add("0");
			}
			if (list.size() == 0) {
				list.add("1");
			}
		}
		System.out.println(list);
		return list;
	}

	/**
	 *
	 * @Description 海外仓正式出库列表
	 * @param request
	 * @param model
	 * @return 返回Jsp页面名称
	 * @return ModelAndView 返回值类型
	 */
	@RequestMapping(value = "/ows.do", method = RequestMethod.GET)
	public ModelAndView overseaWarehouseStock(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("ows_shipment");
		List<Map<String, Object>> ows = iWarehouseService.getOverseasWarehouseStockOrder(null);
		mv.addObject("ows", ows);
		if(ows != null && !ows.isEmpty()) {
			Map<String, Object> firstOrder = ows.get(0);
			int userid = (Integer)firstOrder.get("user_id");
			String orderno = (String)firstOrder.get("order_no");
			Map<String, Object> owsDetail = orderinfoService.getOverseasWarehouseStockOrderDetail(orderno, userid);
			mv.addObject("owsDetail", owsDetail);
		}
		return mv;
	}
	
	/**
	 *
	 * @Description 海外仓正式出库列表
	 * @param request
	 * @return Map<String,Object> 返回值类型
	 */
	@RequestMapping(value = "/owsorder.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> OverseaWarehouseStockOrder(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> result = Maps.newHashMap();
		String useridOrOrderno = request.getParameter("useridOrOrderno");
		List<Map<String, Object>> ows = iWarehouseService.getOverseasWarehouseStockOrder(useridOrOrderno);
		if(ows != null && !ows.isEmpty()) {
			Map<String, Object> firstOrder = ows.get(0);
			int userid = (Integer)firstOrder.get("user_id");
			String orderno = (String)firstOrder.get("order_no");
			Map<String, Object> owsDetail = orderinfoService.getOverseasWarehouseStockOrderDetail(orderno, userid);
			result.put("status", 200);
			if(owsDetail == null || owsDetail.isEmpty()) {
				result.put("status", 100);
				result.put("message", "获取订单错误");
			}else {
				result.put("owsorder",owsDetail);
			}
			result.put("ows", ows);
		}else {
			result.put("status", 101);
			result.put("message", "获取订单错误");
		}
		return result;
	}
	/**
	 *
	 * @Description 海外仓尾程运单追踪号
	 * @param request
	 * @return Map<String,Object> 返回值类型
	 */
	@RequestMapping(value = "shipno.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> OverseaWarehouseStockOrderShipno(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> result = Maps.newHashMap();
		String orderno = request.getParameter("orderno");
		String shipno = request.getParameter("shipno");
		Map<String, Object> map = Maps.newHashMap();
		map.put("orderno", orderno);
		map.put("shipno", shipno);
		int owsAdd = iWarehouseService.addOverseasWarehouseStockOrder(map );
		if(owsAdd > 0) {
			result.put("status", 200);
			
		}else {
			result.put("status", 101);
			result.put("message", "添加运单号出现错误");
			
		}
		return result;
	}
	/**
	 *
	 * @Description 海外仓出货
	 * @param request
	 * @return Map<String,Object> 返回值类型
	 */
	@RequestMapping(value = "shipout.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> OverseaWarehouseStockOrderShipOut(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> result = Maps.newHashMap();
		String orderno = request.getParameter("orderno");
		String shipno = request.getParameter("shipno");
		Map<String, Object> map = Maps.newHashMap();
		map.put("orderno", orderno);
		map.put("shipno", shipno);
		int owsAdd = iWarehouseService.shipoutOverseasWarehouseStockOrder(map );
		if(owsAdd > 0) {
			result.put("status", 200);
			
		}else {
			result.put("status", 101);
			result.put("message", "添加运单号出现错误");
			
		}
		return result;
	}
	
	
	
}
