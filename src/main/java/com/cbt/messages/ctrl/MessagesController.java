package com.cbt.messages.ctrl;

import com.cbt.admuser.service.AdmuserService;
import com.cbt.bean.Complain;
import com.cbt.bean.ComplainVO;
import com.cbt.bean.UserBean;
import com.cbt.common.CommonConstants;
import com.cbt.messages.service.MessagesService;
import com.cbt.messages.vo.AdminRUser;
import com.cbt.messages.vo.MessagesCountVo;
import com.cbt.pojo.Admuser;
import com.cbt.pojo.Messages;
import com.cbt.pojo.page.Page;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.service.IComplainService;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.util.JsonResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/messages")
public class MessagesController {

	@Autowired
	private MessagesService messagesService;

	@Autowired
	private IComplainService complainService;

    @Autowired
    private AdmuserService admuserService;

	/**
	 * 
	 * @Title: addMessages @Description: 添加消息 @param @param
	 * request @param @param type @param @param userid @param @param
	 * title @param @param content @param @param
	 * hrefName @param @return @param @throws ParseException 设定文件 @return
	 * JsonResult 返回类型 @throws
	 */
	@RequestMapping(value = "/addMessages")
	@ResponseBody
	public JsonResult addMessages(HttpServletRequest request, @Param("type") String type, @Param("userid") int userid,
                                  @Param("title") String title, @Param("content") String content, @Param("hrefName") String hrefName)
			throws ParseException {
		JsonResult json = new JsonResult();
		Messages messages = new Messages();
		messages.setContent(content);
		messages.setHrefName(hrefName);
		messages.setUserid(userid);
		messages.setTitle(title);
		messages.setType(type);
		int rows = messagesService.insertSelective(messages);
		if (rows > 0) {
			json.setOk(true);
		} else {
			json.setOk(false);
		}
		return json;
	}

	/**
	 * 
	 * @Title: showMessages @Description: 查询消息列表 @param @param
	 * request @param @param mav @param @param messages @param @param
	 * currentPage @param @param onePageCount @param @return @param @throws
	 * ParseException 设定文件 @return ModelAndView 返回类型 @throws
	 */
	@RequestMapping(value = "/showMessages")
	@ResponseBody
	public JsonResult showMessages(HttpServletRequest request, @Param("currentPage") int currentPage,
                                   @Param("adminid") int adminid, @Param("userid") int userid) throws ParseException {
		JsonResult json = new JsonResult();
		Messages messages = new Messages();
		messages.setAdminid(adminid);
		if (userid != -1) {
			messages.setUserid(userid);
		}
		int onePageCount = CommonConstants.ONE_PAGE_COUNT;
		Page<Messages> pgPage = messagesService.selectByMessages(messages, currentPage, onePageCount);
		if (pgPage == null) {
			json.setOk(false);
		} else {
			json.setOk(true);
			json.setData(pgPage);
		}
		return json;
	}

	/**
	 * @Title: selectBasicMessages @Description: 查询消息数量及相关信息 @param @param
	 * request @param @param timeFrom @param @param
	 * timeTo @param @return @param @throws ParseException 设定文件 @return
	 * JsonResult 返回类型 @throws
	 */
	/*
	 * @RequestMapping(value = "/selectBasicMessages")
	 * 
	 * @ResponseBody public JsonResult selectBasicMessages(HttpServletRequest
	 * request,
	 * 
	 * @Param("timeFrom") String timeFrom,
	 * 
	 * @Param("timeTo") String timeTo,
	 * 
	 * @Param("adminid") int adminid) throws ParseException{ JsonResult json =
	 * new JsonResult(); MessagesCountVo messagesCountVo = new
	 * MessagesCountVo(); SimpleDateFormat sdf = new SimpleDateFormat(
	 * "yyyy-MM-dd hh:mm:ss"); messagesCountVo.setTimeFrom(sdf.parse(timeFrom));
	 * messagesCountVo.setTimeTo(sdf.parse(timeTo));
	 * messagesCountVo.setAdminid(adminid); List<MessagesCountVo>
	 * messagesCountVoListAll = new ArrayList<MessagesCountVo>();
	 * List<MessagesCountVo> messagesCountVoListTime = new
	 * ArrayList<MessagesCountVo>(); //查询退款投诉数量 MessagesCountVo messageCount =
	 * messagesService.selectComplainNum(); //查询订单数量 MessagesCountVo orderNum
	 * =messagesService.selectnoArrgOrderNum(); //查询客户留言数量 //MessagesCountVo
	 * //查询批量优惠申请数量 List<MessagesCountVo> applicationNumList =
	 * messagesService.selectApplicationNum(messagesCountVo); MessagesCountVo
	 * aplicationNum = new MessagesCountVo();
	 * aplicationNum.setType(CommonConstants.BATAPPLY); int
	 * countAll=0,noArrgCount=0,noDeleteCount=0; if(adminid == 1){
	 * for(MessagesCountVo count:applicationNumList){
	 * countAll+=count.getCountAll(); noArrgCount+=count.getNoArrgCount();
	 * if(count.getState()==0){//state为0 表示未处理
	 * aplicationNum.setNoDeleteCount(count.getCountAll()); } }
	 * aplicationNum.setCountAll(countAll);
	 * aplicationNum.setNoArrgCount(noArrgCount); }else{ for(MessagesCountVo
	 * count:applicationNumList){ countAll+=count.getNoDeleteCount();//state
	 * 为1,2 表示已处理 if(count.getState()==0){//state为0 表示未处理
	 * aplicationNum.setNoDeleteCount(count.getNoDeleteCount()); } }
	 * aplicationNum.setCountAll(countAll); } //查询购物车营销数量 //查询商业询盘数量 if(adminid
	 * == 1){ messagesCountVoListAll=messagesService.selectBasicMessagesAll();
	 * messagesCountVoListTime
	 * =messagesService.selectBasicMessagesBytime(messagesCountVo); }else{
	 * messagesCountVoListAll=messagesService.selectBasicMessagesAllbyAndAdmin(
	 * messagesCountVo); messagesCountVoListTime
	 * =messagesService.selectBasicMessagesBytimeAndAdmin(messagesCountVo); }
	 * 
	 * if(messagesCountVoListAll != null && messagesCountVoListAll.size()>0){
	 * for(int i=0;i<messagesCountVoListAll.size();i++){ //客户留言 和购物车营销 只记录一周的数据
	 * if(messagesCountVoListTime.get(i).getType().equals(CommonConstants.
	 * PROPAGEMESSAGE) ||
	 * messagesCountVoListTime.get(i).getType().equals(CommonConstants.
	 * SHOPCARMARKET)){
	 * messagesCountVoListAll.get(i).setCountAll(messagesCountVoListTime.get(i).
	 * getCountAll());
	 * messagesCountVoListAll.get(i).setNoDeleteCount(messagesCountVoListTime.
	 * get(i).getNoDeleteCount()); }
	 * if(messagesCountVoListTime.get(i).getType().equals(CommonConstants.
	 * REFUNDSCOM)){
	 * messagesCountVoListAll.get(i).setNoDeleteCount(messageCount.
	 * getNoDeleteCount());
	 * messagesCountVoListAll.get(i).setCountAll(messageCount.getCountAll()); }
	 * if(messagesCountVoListTime.get(i).getType().equals(CommonConstants.
	 * ORDERMEG)){
	 * messagesCountVoListAll.get(i).setNoArrgCount(orderNum.getNoArrgCount());
	 * } } json.setOk(true); json.setData(messagesCountVoListAll); }else{
	 * json.setMessage("查询失败"); json.setOk(false); } return json; }
	 */

	/**
	 * @Title: selectBasicMessages @Description: 查询消息数量及相关信息 @param @param
	 * request @param @param timeFrom @param @param
	 * timeTo @param @return @param @throws ParseException 设定文件 @return
	 * JsonResult 返回类型 @throws
	 */
	@RequestMapping(value = "/findBasicMessages")
	@ResponseBody
	public JsonResult findBasicMessages(HttpServletRequest request, @Param("timeFrom") String timeFrom,
                                        @Param("timeTo") String timeTo, @Param("adminid") int adminid) throws ParseException {
		JsonResult json = new JsonResult();
		MessagesCountVo messagesCountVo = new MessagesCountVo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		messagesCountVo.setTimeFrom(sdf.parse(timeFrom));
		messagesCountVo.setTimeTo(sdf.parse(timeTo));
		messagesCountVo.setAdminid(adminid);

		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		if (admJson == null) {
			return json;
		}
		Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
		int strm = user.getRoletype();
		int admuserid = adminid;
		if (strm == 0) {
			admuserid = 0;
		}

		List<MessagesCountVo> messagesCountVoListAll = new ArrayList<MessagesCountVo>();
		// 查询客户投诉数量
		MessagesCountVo messageCount = messagesService.selectComplainNum(admuserid);
		messagesCountVoListAll.add(messageCount);
		//显示客户回复的未读个数
		MessagesCountVo selectComplainNum1 = messagesService.selectComplainNum1(admuserid);
		messagesCountVoListAll.add(selectComplainNum1);
		// 系统进账/订单生成
		MessagesCountVo systemFailure = messagesService.selectSystemFailure();
		messagesCountVoListAll.add(systemFailure);
		// 订单需沟通条数	
		MessagesCountVo orderMessCount = messagesService.selectOrderessNum(admuserid);
//		int om = messagesService.selectDelOrderessNum(admuserid);
//		orderMessCount.setNoArrgCount(om);
		messagesCountVoListAll.add(orderMessCount);
		// 查询订单数量
		MessagesCountVo orderNum = messagesService.selectnoArrgOrderNum(adminid);
		messagesCountVoListAll.add(orderNum);
		// 产品单页客户 折扣和定制需求
		MessagesCountVo GuestbookNum = messagesService.selectGuestbookNum(admuserid);
		messagesCountVoListAll.add(GuestbookNum);
		// 购物车&支付 页面问题统计	
		MessagesCountVo customerInfoCollection = messagesService.selectCustomerInfoCollectionNum(admuserid);
		messagesCountVoListAll.add(customerInfoCollection);
		// 产品单页用户留言(Customer Questions & Answers)	
		MessagesCountVo questionnum = messagesService.selectQuestionNum(admuserid);
		messagesCountVoListAll.add(questionnum);
		// 查询批量优惠申请数量
		/*List<MessagesCountVo> applicationNumList = messagesService.selectApplicationNum(messagesCountVo);
		MessagesCountVo aplicationNum = new MessagesCountVo();
		aplicationNum.setType(CommonConstants.BATAPPLY);
		int countAll = 0, noArrgCount = 0;
		int noDeleteCount = 0;*/
		/*
		 * if(adminid == 1){ for(MessagesCountVo count:applicationNumList){
		 * countAll+=count.getCountAll(); noArrgCount+=count.getNoArrgCount();
		 * if(count.getState()==0){//state为0 表示未处理
		 * aplicationNum.setNoDeleteCount(count.getCountAll()); } }
		 * aplicationNum.setCountAll(countAll);
		 * aplicationNum.setNoArrgCount(noArrgCount); }else{ for(MessagesCountVo
		 * count:applicationNumList){ countAll+=count.getCountAll();//state 为1,2
		 * 表示已处理 if(count.getState()==0){//state为0 表示未处理
		 * aplicationNum.setNoDeleteCount(count.getCountAll()); } }
		 * if(aplicationNum.getNoDeleteCount()==null){
		 * aplicationNum.setNoDeleteCount(0); }
		 * aplicationNum.setCountAll(countAll); }
		 */
		// 批量优惠统计
		/*aplicationNum.setCountAll(applicationNumList.get(0).getCountAll());
		aplicationNum.setNoDeleteCount(applicationNumList.get(0).getNoDeleteCount());
		aplicationNum.setNoArrgCount(applicationNumList.get(0).getNoArrgCount());

		messagesCountVoListAll.add(aplicationNum);*/
		// 查询购物车营销数量TODO
		MessagesCountVo cartMarketing = new MessagesCountVo();
		cartMarketing = messagesService.selectCountAllCartMarketingNum();
		int noArrgCartNum = messagesService.selectNoArrgCountNum();
		cartMarketing.setNoArrgCount(noArrgCartNum);
		messagesCountVoListAll.add(cartMarketing);
		// 查询商业询盘数量businquiries
        MessagesCountVo busiessNumList = messagesService.selectBusiessNumNew(admuserid);
        messagesCountVoListAll.add(busiessNumList);
//		List<MessagesCountVo> busiessNumList = messagesService.selectBusiessNum(messagesCountVo);
//		MessagesCountVo busiessNum = new MessagesCountVo();
//		busiessNum.setType(CommonConstants.BUSINQUIRIES);
//		int countAll = 0;
//		int noArrgCount = 0;
//		if (adminid == 1 || adminid == 83) {
//			for (MessagesCountVo count : busiessNumList) {
//				countAll += count.getCountAll();
//				noArrgCount += count.getNoArrgCount();
//				if (count.getState() == 0) {// state为0 表示未处理
//					busiessNum.setNoDeleteCount(count.getCountAll());
//				}
//			}
//			busiessNum.setCountAll(countAll);
//			busiessNum.setNoArrgCount(noArrgCount);
//		} else {
//			for (MessagesCountVo count : busiessNumList) {
//				countAll += count.getNoDeleteCount();// state 为1,2 表示已处理
//				if (count.getState() == 0) {// state为0 表示未处理
//					busiessNum.setNoDeleteCount(count.getNoDeleteCount());
//				}
//			}
//			busiessNum.setCountAll(countAll);
//		}
//		messagesCountVoListAll.add(busiessNum);

		if (messagesCountVoListAll.size() > 0) {
			json.setOk(true);
			json.setData(messagesCountVoListAll);
		} else {
			json.setMessage("查询失败");
			json.setOk(false);
		}
		return json;
	}
	/**
	 * 后台首页 各种客户留言 区块 待处理消息数量
	 * 		/messages/findCustomerMessages
	 */
	@RequestMapping(value = "/findCustomerMessages")
    @ResponseBody
	public Map<String, Object> findCustomerMessages(HttpServletRequest request) {
        Map<String, Object> res = new HashMap<String, Object>();

        String admJson = Redis.hget(request.getSession().getId(), "admuser");
		if (admJson == null) {
			return null;
		}
		Admuser user = (Admuser) SerializeUtil.JsonToObj(admJson, Admuser.class);
		int admuserid = user.getId();
		int strm = user.getRoletype();
		//临时添加Sales1账号查看投诉管理统计数据
		if (strm == 0) {
			admuserid = 0;
		}
		
		try {
            List<HashMap<String, String>> list = messagesService.findCustomerMessages(admuserid);
            res.put("state" , true);
            res.put("data" , list);
        } catch (Exception e) {
            System.out.println(e);
            res.put("state" , false);
        }
        return res;
	}

	/**
	* @Title: selectAdminUser 
	* @Description: 查询客服列表 （adminid=1 除外 （超级管理员））
	* @param @param request
	* @param @return
	* @param @throws ParseException    设定文件 
	* @return JsonResult    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/selectAdminUser")
	@ResponseBody
	public JsonResult selectAdminUser(HttpServletRequest request) throws ParseException{
		JsonResult json = new JsonResult();
		List<Admuser> admUserList = messagesService.selectAdmuser();
		String type = request.getParameter("type");
		
		List<Admuser> nwAdmUserList = new ArrayList<Admuser>();
		if(admUserList!=null &&admUserList.size()>0){
			
			//判断是获取销售列表时，过滤其他类别的用户数据
//			if(type != null && !"".equals(type) && "sell".equals(type)){
//				for(int i=0;i<admUserList.size();i++){
//					if(admUserList.get(i).getRoletype() == 1 || admUserList.get(i).getAdmname().equalsIgnoreCase("Ling")
//							|| admUserList.get(i).getAdmname().equalsIgnoreCase("testAdm")){
//						nwAdmUserList.add(admUserList.get(i));
//					}
//				}
//				json.setData(nwAdmUserList);
//			} else{
//				json.setData(admUserList);
//			}
			json.setData(admUserList);
			json.setOk(true);
			
		}else{
			json.setMessage("客服列表查询失败！");
			json.setOk(false);
		}
		return json;
	}

	/**
	 * @Title: distributionMessages @Description: 将消息分配到客服 @param @param
	 * request @param @param messages @param @return @param @throws
	 * ParseException 设定文件 @return JsonResult 返回类型 @throws
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@RequestMapping(value = "/distributionMessages")
	@ResponseBody
	public JsonResult distributionMessages(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		JsonResult json = new JsonResult();
		int userid  = Integer.valueOf(request.getParameter("userid")==null?"0":request.getParameter("userid"));
		int adminid  = Integer.valueOf(request.getParameter("adminid")==null?"0":request.getParameter("adminid"));
		String hrefName=request.getParameter("hrefName");
		String adminName=request.getParameter("adminName");
		com.cbt.website.dao.UserDao dao = new UserDaoImpl();
		IUserDao userDao = new UserDao();
		String username = "";
		List<Admuser> admUserList = messagesService.selectAdmuser();
		for (Admuser admuser : admUserList) {
			if (admuser.getId() == adminid) {
				adminName = admuser.getAdmname();
			}
		}
		// 分配消息时同时将任务分配到事件本身
		// dao.updateAdminuser(userid, adminid,users, email, username, admName);
		if (userid != 0) {
			UserBean user = userDao.getUserEmailId(userid);
			username = user.getName();
		}
		int row = dao.updateAdminuser(userid, adminid, "Ling", hrefName,
				username, adminName);

		if (row > 0) {
			json.setOk(true);
		} else {
			json.setMessage("消息分配客服失败！");
			json.setOk(false);
		}
		return json;
	}

	@RequestMapping(value = "/showMessagesByOne")
	@ResponseBody
	public JsonResult showMessagesByOne(HttpServletRequest request) throws ParseException {
		JsonResult json = new JsonResult();
		Messages messages = new Messages();
		// 获取登录用户
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		String admName = adm.getAdmname();
		Complain t = new Complain();
		t.setComplainState(0);
		Page<ComplainVO> page = new Page<ComplainVO>();
		page.setCurrentPage(1);
		page.setStartIndex(1);

		page = complainService.searchComplainByParam(t, null, page, admName,adm.getRoletype(),0);
		if (page.getList() == null) {
			json.setOk(false);
		} else {
			json.setOk(true);
			Random random = new Random();
			// 随机产生一条消息作为置顶消息。
			json.setData(page.getList().get(random.nextInt(page.getList().size())));
		}
		return json;
	}

	@RequestMapping(value = "/getBusiess")
	public ModelAndView getBusiess(HttpServletRequest request, ModelAndView mav,
                                   @RequestParam(value = "status", required = false, defaultValue = "0") int status,
                                   @RequestParam(value = "state", required = false, defaultValue = "-1") int state,
                                   @RequestParam("adminid") int adminid) throws ParseException {
		/*String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		adminid = adm.getId();*/
		mav.addObject("status", status);
		mav.addObject("state", state);
		mav.addObject("adminid", adminid);
		//负责人列表
        List<Admuser> admuserLis = admuserService.selectAdmuser();
		request.setAttribute("admList", admuserLis);
        mav.setViewName("/busiesslist");
		return mav;
	}

	@RequestMapping(value = "/preferential")
	public ModelAndView preferential(HttpServletRequest request, ModelAndView mav, @Param("type") int type,
                                     @Param("adminid") int adminid) throws ParseException {
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		adminid = adm.getId();
		mav.addObject("type", type);
		mav.addObject("adminid", adminid);
		mav.setViewName("/preferential");
		return mav;
	}

	@RequestMapping(value = "/assignment")
	@ResponseBody
	public JsonResult assignment(HttpServletRequest request) throws ParseException {
		JsonResult json = new JsonResult();
		// 获取登录用户
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		List<AdminRUser> admRUserList = messagesService.selectNoArrageGuestbook();
		List<Admuser> admUserList = messagesService.selectAdmuser();
		Random random = new Random();
		int ranNum = 0;
		int row;
		int num = 0;
		com.cbt.website.dao.UserDao dao = new UserDaoImpl();
		if (admRUserList != null) {
			for (AdminRUser admRUser : admRUserList) {
				ranNum = random.nextInt(admUserList.size());
				admRUser.setAdminid(admUserList.get(ranNum).getId());
				admRUser.setAdmname(admUserList.get(ranNum).getAdmname());
				row = dao.updateAdminuser(admRUser.getUserid(), admRUser.getAdminid(), "Ling", admRUser.getUseremail(),
						admRUser.getUsername(), admRUser.getAdmname());
				num += row;
			}
		}
		if (num > 0) {
			json.setOk(true);
			json.setData(num);
		} else {
			json.setOk(false);
			json.setMessage("客服分配失败！");
		}
		return json;
	}

}