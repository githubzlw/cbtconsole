package com.cbt.website.servlet;

import com.cbt.FtpUtil.ContinueFTP2;
import com.cbt.Specification.util.DateFormatUtil;
import com.cbt.bean.BuyAliGoodsBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.OrderProductSource;
import com.cbt.bean.Tb1688OrderHistory;
import com.cbt.common.StringUtils;
import com.cbt.parse.driver.FtpDriver;
import com.cbt.pojo.TaoBaoOrderInfo;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.WebTool;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ExpressTrackInfo;
import com.cbt.website.bean.SearchResultInfo;
import com.cbt.website.dao.ExpressTrackDaoImpl;
import com.cbt.website.dao.IExpressTrackDao;
import com.cbt.website.server.PurchaseServer;
import com.cbt.website.server.PurchaseServerImpl;
import com.cbt.website.thread.AddInventoryThread;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.ContentConfig;
import com.cbt.website.util.DownloadMain;
import com.cbt.website.util.Utility;
import com.google.gson.Gson;
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Servlet implementation class ExpressTrackServlet
 */
public class ExpressTrackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";
    // 上传配置
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
    IExpressTrackDao dao = new ExpressTrackDaoImpl();
    PurchaseServer purchaseServer = new PurchaseServerImpl();

    /**
     * updateMatch
     *
     * @see HttpServlet#HttpServlet()
     */
    public ExpressTrackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        String relationorderid = request.getParameter("relationorderid");
        int type = Integer.parseInt(request.getParameter("type"));
        int res = dao.insert(orderid, relationorderid, type);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void searchByExpressTrackid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String expresstrackid = request.getParameter("expresstrackid");
        List<ExpressTrackInfo> list = dao.search(expresstrackid);
        PrintWriter out = response.getWriter();
        out.print(net.minidev.json.JSONArray.toJSONString(list));
        out.flush();
        out.close();
//		request.setAttribute("res", list);
//		javax.servlet.RequestDispatcher homeDispathcer= request.getRequestDispatcher("website/express_track.jsp");
//		homeDispathcer.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void insertArrivalStatusServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String expresstrackid = request.getParameter("expresstrackid");
        String orderid = request.getParameter("orderid");
        String goodsid = request.getParameter("goodsid");
        int status = Integer.parseInt(request.getParameter("status"));
        int res = dao.updateArrivalStatus(expresstrackid, orderid, goodsid, status);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void updateArriveCountServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String expresstrackid = request.getParameter("expresstrackid");
        String orderid = request.getParameter("orderid");
        String goodsid = request.getParameter("goodsid");
        int goodsarrivecount = Integer.parseInt(request.getParameter("goodsarrivecount"));
        int res = dao.updateArriveCount(expresstrackid, orderid, goodsid, goodsarrivecount);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void updateWarehouseIdServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        int ordercount = Integer.parseInt(request.getParameter("ordercount"));
        int inwarehousecount = Integer.parseInt(request.getParameter("inwarehousecount"));
        String warehouseid = request.getParameter("warehouseid");
        int res = dao.updateWarehouseId(orderid, ordercount, inwarehousecount, warehouseid);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
//    protected void runExeServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Utility util = new Utility();
//        util.openExe();
//    }

    protected void getResultInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String expresstrackid = request.getParameter("expresstrackid");
        String checked = request.getParameter("checked");
        PrintWriter out = response.getWriter();
        List<SearchResultInfo> list=new ArrayList<SearchResultInfo>();
//        if("1".equals(checked)){
//            list=dao.getCheckInfo(expresstrackid);
//        }else{
            list = dao.getOrder(expresstrackid, checked);
//        }
        out.print(net.minidev.json.JSONArray.toJSONString(list));
        out.flush();
        out.close();
    }

    protected void allTrack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String shipno = request.getParameter("shipno");
        String type = request.getParameter("type");
        String barcode = request.getParameter("barcode");
        String userid = request.getParameter("userid");
        String userName = request.getParameter("userName");
        String tbOrderId = request.getParameter("tbOrderId");
        PrintWriter out = response.getWriter();
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        int row = dao.allTrack(shipno, type, barcode, userid, userName, tbOrderId, adm.getId());
        out.print(row);
        out.flush();
        out.close();
    }

    protected void getTaoBaoInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String admJson = Redis.hget(request.getSession().getId(), "admuser");//获取登录用户
        Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
        int admid = user.getId();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        PrintWriter out = response.getWriter();
        List<TaoBaoOrderInfo> list = dao.getTaoBaoInfo(orderid);
        String code = "";
        int index = 0;
        int noBuy = 0;
        for (int i = 0; i < list.size(); i++) {
            String barcode = list.get(i).getBarcode();
            String orderstatus = list.get(i).getOrderstatus();
            noBuy = list.get(i).getTotalqty();
            if (barcode.equals("无") && (orderstatus.contains("卖家已发货") || orderstatus.contains("等待买家确认收货") || orderstatus.contains("交易成功") || orderstatus.contains("交易已成功") || orderstatus.contains("卖家已发货，等待买家确认收货") || orderstatus.contains("物流派件中") || orderstatus.contains("物流运输中")) && noBuy == 0) {
                index = index + 1;
            } else if (!barcode.equals("无")) {
                code = barcode;
                break;
            }
        }
        if (list.size() == 0) {
            TaoBaoOrderInfo t = new TaoBaoOrderInfo();
            list.add(t);
        }
        if (code.equals("")) {
            if (index != 0 && index == list.size()) {
                //如果对应订单的所有商品都处于 “卖家已发货状态” 或者“已到货状态”就推荐早期库
                code = dao.getBarcode(0, admid, orderid);
                code = code.equals("无") ? dao.getBarcode(1, admid, orderid) : code;
            } else {
                code = dao.getBarcode(1, admid, orderid);//如果该订单还有商品未采购 或者未发货推荐中期库
            }
        }
        if (admid != 32 && !code.contains("CR")) {
            for (int i = 0; i < list.size(); i++) {
                String barcode = list.get(i).getBarcode();
                if (null != barcode && barcode.contains("CR")) {
                    code = barcode;
                    break;
                }
            }
            if (!code.contains("CR")) {
                if (index != 0 && index == list.size()) {
                    //如果对应订单的所有商品都处于 “卖家已发货状态” 或者“已到货状态”就推荐早期库
                    code = dao.getBarcode(0, admid, orderid);
                    code = code.equals("无") ? dao.getBarcode(1, admid, orderid) : code;
                } else {
                    code = dao.getBarcode(1, admid, orderid);//如果该订单还有商品未采购 或者未发货推荐中期库
                }
            }
        }
        if (admid == 32 && !code.contains("GS")) {
            for (int i = 0; i < list.size(); i++) {
                String barcode = list.get(i).getBarcode();
                if (null != barcode && barcode.contains("GS")) {
                    code = barcode;
                    break;
                }
            }
            if (!code.contains("GS")) {
                if (index != 0 && index == list.size()) {
                    //如果对应订单的所有商品都处于 “卖家已发货状态” 或者“已到货状态”就推荐早期库
                    code = dao.getBarcode(0, admid, orderid);
                    code = code.equals("无") ? dao.getBarcode(1, admid, orderid) : code;
                } else {
                    code = dao.getBarcode(1, admid, orderid);//如果该订单还有商品未采购 或者未发货推荐中期库
                }
            }
        }
        //查看该订单是否有过入库记录
        String records = dao.getInventoryRecords(orderid);
        String order_type = dao.getOrderType(orderid);
        if ("3".equals(order_type)) {
            code = "SHCR006003001";
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setImgurl(code);
            list.get(i).setRecords(records);
        }
        out.print(net.minidev.json.JSONArray.toJSONString(list));
        out.flush();
        out.close();
    }

    /**
     * 使用库存时发现库存有误修改当前锁定库存操作
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException      王宏杰 2017-05-09
     */
    protected void updateLockInventory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String all_remaining = request.getParameter("all_remaining");//该商品经仓库确认后的可用库存数量
        String old_remaining = request.getParameter("old_remaining");//该商品原库存数量
        String barcode = request.getParameter("barcode");
        String od_id = request.getParameter("od_id");
        String remark = request.getParameter("remark");
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        Map<String, String> map = new HashMap<String, String>();
        map.put("barcode", barcode);
        map.put("od_id", od_id);
        map.put("userName", adm.getAdmName());
        map.put("all_remaining", all_remaining);
        map.put("old_remaining", old_remaining);
        map.put("remark", remark);
        int row = dao.updateLockInventory(map);
        if (row > 0) {
            //增加库存有误盘点记录
            dao.insertInventoryWrong(map);
        }
        PrintWriter out = response.getWriter();
        out.print(row);
        out.flush();
        out.close();
    }

    /**
     * 确认并移库完成操作
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException      王宏杰 2017-05-03
     */
    protected void confirmMoveLibrary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String barcode = request.getParameter("barcode");//存放库位
        String goodsid = request.getParameter("goodsid");//商品编号
        String orderid = request.getParameter("orderid");//订单号
        String remaining = request.getParameter("remaining");//可使用库存数量
        String purchaseCount = request.getParameter("purchaseCount");//客户下单数量
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        Map<String, String> map = new HashMap<String, String>();
        map.put("barcode", barcode);
        map.put("goodsid", goodsid);
        map.put("orderid", orderid);
        map.put("remaining", remaining);
        map.put("purchaseCount", purchaseCount);
        map.put("userName", adm.getAdmName());
        int row = 0;
        if (Integer.valueOf(remaining) >= Integer.valueOf(purchaseCount)) {
            //库存数量大于客户下单数量，直接将该商品改成到库并验货无误，同时添加入库记录，没有采购信息，同时更改库存和库存金额
            row = dao.confirmMoveLibrary(map);
        } else {
            //库存不够，需要采购去采购买一部分
            row = dao.confirmMoveLittleLibrary(map);
        }
        PrintWriter out = response.getWriter();
        out.print(row);
        out.flush();
        out.close();
    }

    /**
     * 补货
     */
    protected void getReplenishResultInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String expresstrackid = request.getParameter("expresstrackid");
        PrintWriter out = response.getWriter();
        List<SearchResultInfo> list = dao.getReplenishResultInfo(expresstrackid);
        out.print(net.minidev.json.JSONArray.toJSONString(list));
        out.flush();
        out.close();
    }

    /**
     * 亚马逊商品扫描
     */
    protected void getAmazonResultInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String expresstrackid = request.getParameter("expresstrackid");
        PrintWriter out = response.getWriter();
        List<SearchResultInfo> list = dao.getAmazonResultInfo(expresstrackid);
        out.print(net.minidev.json.JSONArray.toJSONString(list));
        out.flush();
        out.close();
    }

    //亚马逊商品入库
    protected void updateAmazonstatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        long goodid = Long.parseLong(request.getParameter("goodid"));
        int status = Integer.parseInt(request.getParameter("status"));
        String goodurl = request.getParameter("goodurl");

        String barcode = request.getParameter("barcode");  //条形码
        String userid = request.getParameter("userid");  //用户id
        String userName = request.getParameter("userName");  //用户名字
        String tbOrderId = request.getParameter("tbOrderId");  //淘宝订单id
        String shipno = request.getParameter("shipno");  //快递单号
        String itemid = request.getParameter("itemid");  //itemid
        String storeName = request.getParameter("storeName");  //区分亚马逊ebay还是淘宝1688
        String warehouseRemark = request.getParameter("warehouseRemark");  //入库备注
        int res = dao.updateAmazonstatus(orderid, goodid, status, goodurl, barcode, userid, userName, tbOrderId, shipno, itemid, warehouseRemark, storeName);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }
    //亚马逊商品取消入库

    protected void cancelAmazonstatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        String itemid = request.getParameter("itemid");
        long goodid = Long.parseLong(request.getParameter("goodid"));
        String warehouseRemark = request.getParameter("warehouseRemark");

        int res = dao.cancelAmazonstatus(orderid, itemid, goodid, warehouseRemark);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    /**
     * 查询20条快递单号匹配不了货源的淘宝信息商品
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author 王宏杰
     */
    public void getNoMatchGoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String page_ = request.getParameter("page");
        int page = StringUtils.isStrNull(page_) ? 0 : (Integer.valueOf(page_) - 1) * 24;
        String orderid = request.getParameter("orderid");
        String goodsid = request.getParameter("goodsid");
        PrintWriter out = response.getWriter();
//		List order = dao.getNoMatchGoods(expresstrackid,username);
        int all_count = 0;
        List<OrderDetailsBean> order = dao.getNoPictureGoods(orderid, goodsid, page);
        if (order.size() > 0) {
            if (order.get(0).getBuycount() % 24 == 0) {
                all_count = order.get(0).getBuycount() / 24;
            } else {
                all_count = order.get(0).getBuycount() / 24 + 1;
            }
            order.get(0).setBuy_for_me(Integer.valueOf(page_));
            order.get(0).setChecked(all_count);
//			request.setAttribute("allnum", order.get(0).getBuycount());
//			request.setAttribute("all_count", all_count);
//			request.setAttribute("page", page);
        }
        Gson gson = new Gson();
        String json = gson.toJson(order);
        out.print(json);
        out.flush();
        out.close();
    }

    /**
     * 根据订单号和商品编号关联入库
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author 王宏杰
     * 2016-10-10
     */
    public void updateMatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        String goodsid = request.getParameter("goodsid");
        String sku = request.getParameter("sku");
        int state = Integer.valueOf(request.getParameter("state"));
        String tborderid = request.getParameter("tborderid");
        String itemid = request.getParameter("itemids");
        PrintWriter out = response.getWriter();
        int order = dao.updateMatch(orderid, goodsid, sku, itemid, tborderid, state);
        Gson gson = new Gson();
        String json = gson.toJson(order);
        out.print(json);
        out.flush();
        out.close();
    }


    protected void getGoodsData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String expresstrackid = request.getParameter("expresstrackid");
        PrintWriter out = response.getWriter();
        try {
            //记录扫描快递包裹日志记录scan_parcel_log_info
            String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
            Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
            dao.insertScanLog(expresstrackid, adm.getAdmName());
        } catch (Exception e) {

        }
        List<Tb1688OrderHistory> order = dao.getGoodsData(expresstrackid);
        Gson gson = new Gson();
        String json = gson.toJson(order);
        out.print(json);
        out.flush();
        out.close();
    }

    protected void getAliGoodsData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String admuserid = request.getParameter("admuserid");
        PrintWriter out = response.getWriter();
        List<BuyAliGoodsBean> order = dao.getAliGoodsData(admuserid);
        Gson gson = new Gson();
        String json = gson.toJson(order);
        out.print(json);
        out.flush();
        out.close();
    }

    //查询一周内有问题订单号
    protected void getIdRelationtable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String expresstrackid = request.getParameter("expresstrackid");
        String flage = request.getParameter("flage");
        PrintWriter out = response.getWriter();
        List<Map<String, String>> order = null;
        if (flage.equals("false")) {
            order = dao.getIdRelationtable(1, 10);
        } else if (flage.equals("true")) {
            order = dao.getIdRelationtable(1, 10000);
        } else {
            order = dao.getIdRelationtable(1, 10);
        }
        Gson gson = new Gson();
        String json = gson.toJson(order);
        out.print(json);
        out.flush();
        out.close();
    }

    //获取仓库位置
    protected void getPositionAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String barcode = request.getParameter("barcode");
        PrintWriter out = response.getWriter();
        String position = dao.getPosition(barcode);
        out.print(position);
        out.flush();
        out.close();
    }


    /**
     * 强制入库
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author 王宏杰
     * 2016-10-09
     */
    protected void mandatoryPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String barcode = request.getParameter("barcode");  //条形码
        String tbOrderId = request.getParameter("orderid");  //淘宝订单id
        String shipno = request.getParameter("shipno");  //快递单号
        String itemid = request.getParameter("itemid");  //itemid
        String itemprice = request.getParameter("itemprice");//淘宝价格
        String sku = request.getParameter("sku");//淘宝规格
        String userid = request.getParameter("userid");//淘宝价格
        String username = request.getParameter("username");//淘宝规格
        String itemqty = request.getParameter("itemqty");
        String remark = request.getParameter("remark");
        int res = dao.mandatoryPut(tbOrderId, shipno, itemid, barcode, itemprice, sku, userid, username, itemqty, remark);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    protected void addPhoto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		File file=new File("D:\\picture\\"+orderid+"_"+goodid+".jpg");
        String savePath = "D:\\picture\\";
        File tmp_path = new File(savePath);
        tmp_path.mkdirs();
        System.out.println("照片数据保存路径:" + savePath);
        String pic_base_64_data = request.getParameter("picData");
        String orderid = request.getParameter("orderid");
        String goodsid = request.getParameter("goodsid");
        BASE64Decoder decode = new BASE64Decoder();
        byte[] datas = decode.decodeBuffer(pic_base_64_data);
        String filename = orderid + "_" + goodsid + ".jpg";
        File file = new File(savePath + filename);
        OutputStream fos = new FileOutputStream(file);
        System.out.println("照片文件名称:" + savePath + filename);
        fos.write(datas);
        fos.close();
    }

    /**
     * 当验货数量大于商品销售数量时，将多余的商品存入库存表中invenntory
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addInventory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        String barcode = request.getParameter("barcode");//库存位置
        String inventory_count = request.getParameter("inventory_count");//库存数量
        String orderid = request.getParameter("orderid");//库存商品的订单号
        String odid = request.getParameter("odid");//库存商品编号
        String storage_count = request.getParameter("storage_count");//验货数量
        String when_count = request.getParameter("when_count");//当次验货数量
        String unit = request.getParameter("unit");
        int res = dao.addInventory(barcode, inventory_count, orderid, odid, storage_count, when_count, adm.getAdmName(), unit);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    /**
     * 验货无误
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void updateCheckStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        long goodid = Long.parseLong(request.getParameter("goodid"));
        int status = Integer.parseInt(request.getParameter("status"));
        String goodurl = request.getParameter("goodurl");

        String barcode = request.getParameter("barcode");  //条形码
        String userid = request.getParameter("userid");  //用户id
        String userName = request.getParameter("userName");  //用户名字
        String tbOrderId = request.getParameter("tbOrderId");  //淘宝订单id
        String shipno = request.getParameter("shipno");  //快递单号
        String itemid = request.getParameter("itemid");  //itemid
        String repState = request.getParameter("repState");  //区分是否是补货
        String warehouseRemark = request.getParameter("warehouseRemark");  //入库备注
        int count = Integer.valueOf(request.getParameter("count"));  //验货总数=当次验货数量+已验货数量
        String weight = "0.00";
        weight = StringUtil.isBlank(weight) ? "0.00" : weight;//商品入库包裹重量
        int res = dao.updateTbstatus(orderid, goodid, status, goodurl, barcode, userid, userName, tbOrderId, shipno, itemid, warehouseRemark, repState, count, weight);
        int num = 0;
        if (res > 0) {
            //验货无误成功，判断该订单是否全部到库并且验货无误
            num = dao.checkOrderState(orderid);
        }
        PrintWriter out = response.getWriter();
        out.print(res + "," + num);
        out.flush();
        out.close();
    }

    /**
     * 如果采购通过线下或原链接购买商品忘记录入订单信息时，仓库通过反查找到对应的商品时，关联入库时增加采购订单信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @ 2017-04-13  whj
     */
    protected void insertTbInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> map = new HashMap<String, String>();
        String goodsid = request.getParameter("goodid");
        String orderid = request.getParameter("orderid");
        OrderProductSource ops = dao.queryOps(orderid, goodsid);
        String admName = "";
        admName = dao.queryBuyCount(ops.getConfirmUserid());
        map.put("username", admName);
        map.put("shipno", "");
        map.put("taobaoPrice", ops.getGoodsPrice().toString());
        map.put("taobaoFeight", "0");
        map.put("goodsQty", ops.getBuycount().toString());
        map.put("taobao_url", ops.getGoodsPUrl());
        map.put("goods_sku", ops.getCar_type());
        map.put("taobao_name", ops.getGoodsName());
        map.put("preferential", "0");
        map.put("goods_imgs", ops.getCar_img());
        map.put("TbOrderid", orderid);
        map.put("TbGoodsid", goodsid);
        double prices = ops.getBuycount() * ops.getGoodsPrice();
        map.put("totalprice", String.valueOf(prices));
        int row = purchaseServer.insertSources(map);
        if (row > 0) {
            dao.updateOfflinePurchase(orderid, goodsid);
        }
        String shipno = dao.qyeryShipno(orderid, goodsid);
        PrintWriter out = response.getWriter();
        out.print(shipno);
        out.flush();
        out.close();
    }

    /**
     * 商品到库操作
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void updateGoodStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        long goodid = Long.parseLong(request.getParameter("goodid"));
        int status = Integer.parseInt(request.getParameter("status"));
        String goodurl = request.getParameter("goodurl");

        String barcode = request.getParameter("barcode");
        String userid = request.getParameter("userid");
        String userName = request.getParameter("userName");
        String tbOrderId = request.getParameter("tbOrderId");
        String shipno = request.getParameter("shipno");
        String itemid = request.getParameter("itemid");
        String repState = request.getParameter("repState");
        String warehouseRemark = request.getParameter("warehouseRemark");
        if (warehouseRemark == null || "".equals(warehouseRemark)) {
            warehouseRemark = "";
        } else {
            warehouseRemark = userName + ":" + warehouseRemark + "(" + DateFormatUtil.getWithMinutes(new Date()) + ");";
        }
        // 验货有误时的验货数量
        int count = Integer.valueOf(request.getParameter("count"));
        int res = dao.updateGoodStatus(orderid, goodid, status, goodurl, barcode, userid, userName, tbOrderId, shipno, itemid, warehouseRemark, repState, count);

        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    //淘宝1688商品入库取消
    protected void updatecanceltatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        long goodid = Long.parseLong(request.getParameter("goodid"));
        String repState = request.getParameter("repState");
        String warehouseRemark = request.getParameter("warehouseRemark");
        String count = request.getParameter("count");

        // jxw 2017-4-13  记录操作人
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        int res = dao.updatecanceltatus(orderid, goodid, repState, warehouseRemark, count, adm.getId());
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    //淘宝1688商品验货取消
    protected void updatecancelChecktatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        long goodid = Long.parseLong(request.getParameter("goodid"));
        String repState = request.getParameter("repState");
        String warehouseRemark = request.getParameter("warehouseRemark");
        String count = request.getParameter("count");//取消验货数量
        String barcode = request.getParameter("barcode");
        String seiUnit = request.getParameter("seiUnit");
        String cance_inventory_count = request.getParameter("cance_inventory_count");
        if (cance_inventory_count == null || "".equals(cance_inventory_count)) {
            cance_inventory_count = "0";
        }
        String weight = request.getParameter("weight");
        weight = StringUtil.isBlank(weight) ? "0.00" : weight;
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
        int res = dao.updatecancelChecktatus(orderid, goodid, repState, warehouseRemark, count, adm.getId(), barcode, cance_inventory_count, seiUnit, weight);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

    protected void picture(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        String goodid = request.getParameter("goodid");
        String path = "D:\\picture";
        File filedir = new File(path);
        if (!filedir.exists()) {
            filedir.mkdir();
        }
        File file = new File(path + "\\" + orderid + "_" + goodid + ".jpg");
        try {
            OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
            if (file.exists()) {
                FileInputStream is = new FileInputStream(path + "\\" + orderid + "_" + goodid + ".jpg");
                int length = is.available();
                byte[] data = new byte[length];
                is.read(data);
                is.close();
                response.setContentType("image/*");
                toClient.write(data); // 输出数据
            }
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //上传图片
    protected void uploadfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        String goodid = request.getParameter("goodid");


        File file = new File("D:\\picture\\" + orderid + "_" + goodid + ".jpg");
        //文件上传
        try {
            if (file.exists()) {
                FTPClient ftp = FtpDriver.connect("", "198.38.82.146", 21, "product@china-clothing-wholesale.com", "product!");
                FtpDriver.upload(file, ftp);
            } else {
                System.out.println("图片文件不存在！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取系统根路径
     * (路径结尾不包括‘/’)
     *
     * @param request
     * @return demo:
     */
    private String getRootPath(HttpServletRequest request) {
        String prjPath = request.getSession().getServletContext().getRealPath("");
        prjPath = prjPath.replace("\\", "/");// 兼容window环境
        return prjPath.substring(0, prjPath.lastIndexOf("/"));
    }


    //上传图片
    public void uploadImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        //设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");

        String orderid = request.getParameter("orderid");
        String odid = request.getParameter("odid");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int day = c.get(Calendar.DATE);
        int minute = c.get(Calendar.HOUR);
        int second = c.get(Calendar.SECOND);
        String storePath = "";
        if (odid == null || "".equals(odid)) {
            storePath = time + "/" + orderid + "_" + minute + second + ".jpg";
        } else {
            storePath = time + "/" + orderid + "_" + odid + "_" + c.getTimeInMillis() + ".jpg";
        }
        String fileData = request.getParameter("fileData");//Base64编码过的图片数据信息，对字节数组字符串进行Base64解码
        HashMap<String, Object> resp = new HashMap<String, Object>();
        int status = 0;
        String imgPath = "D:/product/" + storePath;
        //如果此文件夹不存在则创建
        File f = new File(imgPath);
        if (!f.exists()) {
            if (f.isDirectory()) {
                f.mkdirs();
            } else {
                File parent = f.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                    f.createNewFile();
                }
            }
        }
        //status = uploadImage(fileData, f, imgPath);//进行文件上传操作，上传到服务器tomcat中
        if (status > 0) {
            //远程上传到图片服务器
            AddInventoryThread a = new AddInventoryThread(storePath, imgPath, orderid, odid, 0);
            a.start();
            //进行文件上传操作，上传到服务器tomcat中，并等比压缩
            //status = uploadImage(fileData, f, imgPath);
            //支持断点续存上传图片,ss
            ContinueFTP2 f1 = new ContinueFTP2("104.247.194.50", "importweb", "importftp@123", "21", "/stock_picture/" + storePath + "", imgPath);
            //远程上传到图片服务器
            f1.start();
            if (status == 1 && StringUtil.isNotBlank(odid)) {
                dao.saveImgPath(storePath, orderid, odid, "https://img.import-express.com/importcsvimg/inspectionImg/" + storePath + "", false);
            } else if (status == 1 && (odid == null || "".equals(odid))) {
                dao.saveImgPathForInfo("https://img.import-express.com/importcsvimg/packimg" + storePath, orderid, storePath);
            }
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("imgs", storePath));
            DownloadMain.postContentClient(ContentConfig.DOWNLOAD_LIST_URL, nvps);
        }
        resp.put("status", status);
        resp.put("localPath", "http://192.168.1.34:8085/" + storePath + "");
        resp.put("picPath", "https://img.import-express.com/importcsvimg/inspectionImg/" + storePath + "");
        WebTool.writeJson(SerializeUtil.ObjToJson(resp), response);
    }


    //删除图片
    public void delUploadImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String delPath = request.getParameter("delPath");
        String delPathOrderid = request.getParameter("delPathOrderid");
        int row = dao.delUploadImage(delPath, delPathOrderid);
        String urls = "";
        String ip = request.getLocalAddr();
        if (row > 0) {
            String imgFiles = dao.getImgFiles(delPathOrderid);
            if (imgFiles != null && imgFiles.indexOf("&") > -1) {
                for (int j = 0; j < imgFiles.split("&").length; j++) {
                    urls += "<img width='50px' onclick='BigImg(this,\"" + imgFiles.split("&")[j] + "\",\"" + delPathOrderid + "\");' id='" + imgFiles.split("&")[j] + "' height='50px' alt='' src='" + ip + ":8084/" + imgFiles.split("&")[j] + "'/>";
                }
            } else if (imgFiles != null && imgFiles.length() > 0) {
                urls = "<img width='50px' height='50px' onclick='BigImg(this,\"" + imgFiles + "\",\"" + delPathOrderid + "\");' id='" + imgFiles + "' src='" + ip + ":8084/" + imgFiles + "'/>";
            }
        }
        PrintWriter out = response.getWriter();
        out.print(urls);
        out.flush();
        out.close();
    }

    /**
     * 删除验货时拍摄的不好的照片 王宏杰 2018-06-28
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void delPics(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int row = 0;
        Map<String, String> map = new HashMap<String, String>(3);
        PrintWriter out = response.getWriter();
        String orderid = request.getParameter("orderid");
        String goodsid = request.getParameter("goodsid");
        String picPath = request.getParameter("picPath");
        try {
            map.put("orderid", orderid);
            map.put("goodsid", goodsid);
            map.put("picPath", picPath);
            row = dao.delPics(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.print(row);
        out.flush();
        out.close();
    }

    /**
     * 获取订单下本地图片路径
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getImgsPath(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderid = request.getParameter("orderId");
        String imgFiles = dao.getImgFiles(orderid);
        List<String> list = new ArrayList<String>();
        if (imgFiles != null && imgFiles.indexOf("&") > -1) {
            for (int j = 0; j < imgFiles.split("&").length; j++) {
                list.add(imgFiles.split("&")[j]);
            }
        } else if (imgFiles != null && imgFiles.length() > 0) {
            list.add(imgFiles);
        }
        PrintWriter out = response.getWriter();
        out.print(list);
        out.flush();
        out.close();
    }

    /**
     * 文件上传
     *
     * @param fileData
     * @param file
     * @return
     */
//    private int uploadImage(String fileData, File file, String imgPath) {
//        int status = 0;
//        //使用BASE64对图片文件数据进行解码操作
//        BASE64Decoder decoder = new BASE64Decoder();
//        ByteArrayInputStream bais = null;
//        byte[] bytes = null;
//        BufferedImage bi = null;
//        try {
//            //通过Base64解密，将图片数据解密成字节数组
//            bytes = decoder.decodeBuffer(fileData);
//            //构造字节数组输入流
//            bais = new ByteArrayInputStream(bytes);
//            //读取输入流的数据
//            bi = ImageIO.read(bais);
//            //将数据信息写进图片文件中
//            ImageIO.write(bi, "png", file);// 不管输出什么格式图片，此处不需改动
//            //上传完成后判断图片大小，如果小于10KB则上传失败
//            File new_file = new File(imgPath);
//            if (new_file.exists() && new_file.isFile()) {
//                long fileS = new_file.length();
//                double size = (double) fileS / 1024;
//                if (size < 9) {
//                    //图片上传失败删除图片
//                    new_file.delete();
//                    System.out.println("验货图片数据丢失上传失败，已删除");
//                } else {
//                    //压缩图片
//                    resize(new File(imgPath), new File(imgPath), 1.00, 0.9f);
//                    status = 1;
//                }
//            }
//        } catch (IOException e) {
//            status = 0;
//        } finally {
//            if (bais != null) {
//                try {
//                    bais.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            bytes = null;
//            bi = null;
//        }
//        return status;
//    }


    /**
     * @param originalFile 原文件
     * @param resizedFile  压缩目标文件
     * @param quality      压缩质量（越高质量越好）
     * @param scale        缩放比例;  1等大.
     * @throws IOException
     */
//    public static void resize(File originalFile, File resizedFile, double scale, float quality) throws IOException {
//        ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
//        Image i = ii.getImage();
//        int iWidth = (int) (i.getWidth(null) * scale);
//        int iHeight = (int) (i.getHeight(null) * scale);
//        //在这你可以自定义 返回图片的大小 iWidth iHeight
//        Image resizedImage = i.getScaledInstance(iWidth, iHeight, Image.SCALE_SMOOTH);
//        // 获取图片中的所有像素
//        Image temp = new ImageIcon(resizedImage).getImage();
//        // 创建缓冲
//        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
//        // 复制图片到缓冲流中
//        Graphics g = bufferedImage.createGraphics();
//        // 清除背景并开始画图
//        g.setColor(Color.white);
//        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
//        g.drawImage(temp, 0, 0, null);
//        g.dispose();
//        // 柔和图片.
//        float softenFactor = 0.05f;
//        float[] softenArray = {0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
//        Kernel kernel = new Kernel(3, 3, softenArray);
//        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
//        bufferedImage = cOp.filter(bufferedImage, null);
//        FileOutputStream out = new FileOutputStream(resizedFile);
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
//        param.setQuality(quality, true);
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(bufferedImage);
//        bufferedImage.flush();
//        out.close();
//    }

//    /**
//     * ly 重写图片压缩
//     *
//     * @param originalFile 原文件
//     * @param resizedFile  压缩目标文件
//     * @param quality      压缩质量（越高质量越好）
//     * @param iWidth       压缩后的宽
//     * @throws IOException
//     */
//    public static void resize(File originalFile, File resizedFile, int iWidth, float quality, boolean flag) throws IOException {
//        ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
//        Image i = ii.getImage();
////	       int iWidth = (int) (i.getWidth(null)*scale);
////	       int iHeight = (int) (i.getHeight(null)*scale);
//        int iHeight = (int) (iWidth * i.getHeight(null) / i.getWidth(null));
//        //在这你可以自定义 返回图片的大小 iWidth iHeight
//        Image resizedImage = i.getScaledInstance(iWidth, iHeight, Image.SCALE_SMOOTH);
//        // 获取图片中的所有像素
//        Image temp = new ImageIcon(resizedImage).getImage();
//        // 创建缓冲
//        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
//        // 复制图片到缓冲流中
//        Graphics g = bufferedImage.createGraphics();
//        // 清除背景并开始画图
//        g.setColor(Color.white);
//        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
//        g.drawImage(temp, 0, 0, null);
//        g.dispose();
//        // 柔和图片.
//        float softenFactor = 0.05f;
//        float[] softenArray = {0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
//        Kernel kernel = new Kernel(3, 3, softenArray);
//        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
//        bufferedImage = cOp.filter(bufferedImage, null);
//        FileOutputStream out = new FileOutputStream(resizedFile);
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
//        param.setQuality(quality, true);
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(bufferedImage);
//        bufferedImage.flush();
//        out.close();
//    }

    public void outputPicture(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String picPath = request.getParameter("pic");
        String picBasePath = request.getSession().getServletContext().getRealPath("/");

        File file = new File(picBasePath + picPath);
        if (file.length() > 0 && file.exists()) {
            try {
                OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
                if (file.exists()) {
                    FileInputStream is = new FileInputStream(picBasePath + picPath);
                    int length = is.available();
                    byte[] data = new byte[length];
                    is.read(data);
                    is.close();
                    response.setContentType("image/*");
                    toClient.write(data); // 输出数据
                }
                toClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void getPicturePathList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //取出当前登录用户
        String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
        Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

        List<Map<String, String>> list = dao.getPicturePathList(admuser.getId().toString());

        request.setAttribute("picturePathList", list);
        request.getRequestDispatcher("website/examine_goods_picture.jsp").forward(request, response);
    }

    //这个不拍的情况删除图片
    protected void deletefile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String orderid = request.getParameter("orderid");
        String goodid = request.getParameter("goodid");
        PrintWriter out = response.getWriter();
        String path = "D:\\picture";
        File file = new File(path + "\\" + orderid + "_" + goodid + ".jpg");
        if (file.exists()) {
            file.delete();
            out.print(1);
        } else {
            out.print(0);
        }
    }


    //重拍图片
    protected void pictureagain(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String orderid = request.getParameter("orderid");
        long goodid = Long.parseLong(request.getParameter("goodid"));
        int res = dao.pictureagain(orderid, goodid);
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }


    //重拍图片
    protected void uploadInPicture(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderid = request.getParameter("orderid");
        String goodsid = request.getParameter("goodsid");
        //http://img.import-express.com/importcsvimg/inspectionImg/2017-08/P821325642819798_772540.jpg
        String old_url = request.getParameter("old_url");
        String ml = old_url.split("inspectionImg/")[1].split("/")[0];
        SimpleDateFormat order = new SimpleDateFormat("ddHHmmss");
        Date data = new Date();
        String img_name = orderid + goodsid + order.format(data);
        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);
        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        // 中文处理
        upload.setHeaderEncoding("UTF-8");
        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = "D:/product/" + File.separator + ml;
        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked") List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
//                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + img_name;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件的上传路径
                        System.out.println(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                        request.setAttribute("message", "文件上传成功!");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("message", "文件上传失败!");
        }
    }
}
