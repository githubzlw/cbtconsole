
package com.cbt.report.ctrl;

import com.cbt.bean.*;
import com.cbt.customer.service.IShopUrlService;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.ImgDownload;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.*;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.thread.CoreGoodsDealThread;
import com.cbt.website.thread.CoreGoodsSyncThread;
import com.cbt.website.thread.ShopGoodsDealThread;
import com.cbt.website.thread.ShopGoodsSyncThread;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.EasyUiJsonResult;
import com.cbt.website.util.JsonResult;
import com.cbt.website.util.MD5Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

@Controller
@RequestMapping(value = "/CoreUrlC")
public class CoreUrlController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CoreUrlController.class);
	private String rootPath = "F:/console/tomcatImportCsv/webapps/";
	private String localIP = "http://27.115.38.42:8083/";
	private String wanlIP = "http://192.168.1.27:8083/";
	private String chineseChar = "([\\???-\\???]+)";
	private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();
	private List<Category1688Bean> category1688List = new ArrayList<Category1688Bean>();
	// ???????????????????????????
	// private static final String SHOPGOODSWEIGHTCLEARURL =
	// "http://127.0.0.1:8080/checkimage/clear/shopGoodsWeight?";
	private static final String SHOPGOODSWEIGHTCLEARURL = "http://192.168.1.31:8080/checkimage/clear/shopGoodsWeight?";

	@Autowired
	private IShopUrlService shopUrlService;

	@Autowired
	private CustomGoodsService customGoodsService;

	/**
	 * ????????????:?????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/findAll")
	@ResponseBody
	protected EasyUiJsonResult findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();
		// ??????????????????
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		// String parse = null;
		String timeFrom = null;
		String timeTo = null;
		String shopUserName = request.getParameter("shopUserName");
		if ((shopUserName == null || "".equals(shopUserName)) && adm != null) {
			if (adm.getAdmName().equalsIgnoreCase("Ling") || adm.getAdmName().equalsIgnoreCase("camry")
					|| adm.getAdmName().equalsIgnoreCase("testAdm")) {
				shopUserName = "";
			} else {
				shopUserName = adm.getAdmName();
			}

		}
		String str = request.getParameter("page");
		String date = request.getParameter("createTime");
		// String type = request.getParameter("questionType");
		String shopId = request.getParameter("shopId");
		String time1 = request.getParameter("timeFrom");
		String time2 = request.getParameter("timeTo");
		String isOnStr = request.getParameter("isOn");
		String stateStr = request.getParameter("state");
		int isOn = -1;
		if (!(isOnStr == null || "".equals(isOnStr))) {
			isOn = Integer.valueOf(isOnStr);
		}

		if (time1 != null && time1 != "") {
			timeFrom = time1;
		}
		if (time2 != null && time2 != "") {
			timeTo = time2;
		}
		int state = -1;
		if (!(stateStr == null || "".equals(stateStr))) {
			state = Integer.valueOf(stateStr);
		}
		try {
			if (date != null && !"".equals(date)) {
				// parse = date;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		int page = 1;
		if (str == null) {
			str = "1";
		} else {
			page = Integer.parseInt(str);
		}
		int start = (page - 1) * 30;
		List<ShopUrl> findAll = shopUrlService.findAll(shopId, null, shopUserName, date, start, 30, timeFrom, timeTo, isOn,
				state,-1,-1,-1,-1,-1,-1,
				"", -1, -1, null);
		int total = shopUrlService.total(shopId, null, shopUserName, date, timeFrom, timeTo, isOn, state,-1,-1,
				-1,-1,-1,-1,"", -1, -1, null);
		json.setRows(findAll);
		json.setTotal(total);
		return json;
	}

	@ResponseBody
	@RequestMapping("/selectOneShop.do")
	public JsonResult getOneProduct(HttpServletRequest request, HttpServletResponse response) {

		ShopUrl shopUrl = null;
		JsonResult jr = new JsonResult();
		try {

			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			shopUrl = shopUrlService.findById(id);

			if (shopUrl != null) {
				jr.setData(shopUrl);
				jr.setOk(true);
				return jr;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		jr.setOk(false);
		jr.setData(null);
		return jr;
	}

	@ResponseBody
	@RequestMapping("/insertOrUpdate.do")
	public JsonResult insertOrUpdate(HttpServletRequest request, HttpServletResponse response) {
		JsonResult jr = new JsonResult();

		String sid = request.getParameter("id");
		String shopUrl = request.getParameter("shopUrl");
		String shopId = request.getParameter("shopId");
		String salesVolume = request.getParameter("salesVolume");
		String downloadNum = request.getParameter("downloadNum");
		String isValid = request.getParameter("isValid");
		String inputShopName = request.getParameter("inputShopName");
		String urlType = request.getParameter("urlType");
		String[] typeShopUrls = request.getParameter("typeShopUrls").split(",");
		// ??????????????????
		String admuserJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		ShopUrl su = new ShopUrl();
		if (StringUtils.isNotBlank(sid)) {
			su.setId(Integer.parseInt(sid));
			ShopUrl temp = shopUrlService.findById(Integer.parseInt(sid));
			if (temp.getOnlineStatus() == 0) {
				su.setFlag(0);
			} else {
				su.setFlag(1);
			}
		}
		if (StringUtils.isNotBlank(salesVolume)) {
			su.setSalesVolume(Integer.parseInt(salesVolume));
		} else {
			su.setSalesVolume(1);
		}

		if (StringUtils.isNotBlank(downloadNum)) {
			su.setDownloadNum(Integer.parseInt(downloadNum));
		} else {
			su.setDownloadNum(1000);
		}
		if (urlType.equals("0")) {
			shopId = shopUrl.substring(8, shopUrl.indexOf(".1688.com/"));
			shopUrl = shopUrl.substring(0, shopUrl.indexOf(".1688.com/") + 10);
		} else {
			shopId = typeShopUrls[0].substring(8, typeShopUrls[0].indexOf(".1688.com/"));
		}

		su.setAdminId(adm.getId());
		su.setAdmUser(adm.getAdmName());
		su.setIsValid(Integer.parseInt(isValid));
		su.setUrlType(Integer.parseInt(urlType));
		su.setShopUrl(shopUrl);
		su.setShopId(shopId);
		su.setInputShopName(inputShopName);
		su.setCreateTime(new Date());
		su.setUpdatetime(new Date());

		int result = shopUrlService.insertOrUpdate(su, typeShopUrls,0);
		if (result == 1) {
			jr.setOk(true);
		} else {
			jr.setOk(false);
		}
		return jr;
	}

	@ResponseBody
	@RequestMapping("/deleShopUrl.do")
	public JsonResult deleShopUrl(HttpServletRequest request, HttpServletResponse response) {
		JsonResult jr = new JsonResult();
		String sid = request.getParameter("id");
		if (sid == null && "".equals(sid)) {
			jr.setOk(false);
			return jr;
		}

		int result = shopUrlService.delById(Integer.parseInt(sid));
		if (result == 1) {
			jr.setOk(true);
		} else {
			jr.setOk(false);
		}
		return jr;
	}

	@RequestMapping("/updateExcel.do")
	@ResponseBody
	public JsonResult updateExcel(HttpServletRequest request, HttpServletResponse rs) {

		JsonResult jr = new JsonResult();
		try {

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("files");
			if (file == null || file.isEmpty()) {
				jr.setOk(false);
				return jr;
			}
			InputStream is = new ByteArrayInputStream(file.getBytes());
			Map<String, String> map = readExcel2Map(is);
			if (map == null) {
				jr.setOk(false);
				return jr;
			}
			int line = 0;
			for (Entry<String, String> entry : map.entrySet()) {
				ShopUrl su = new ShopUrl();
				su.setShopId(entry.getKey());
				su.setShopUrl(entry.getValue());
				int result = shopUrlService.insertOrUpdate(su, null,0);
				if (result == 1) {
					line++;
				}
			}
			jr.setData(line);
			jr.setOk(true);
		} catch (Exception e) {
			e.printStackTrace();
			jr.setOk(false);
		}

		return jr;
	}

	@SuppressWarnings("resource")
	private Map<String, String> readExcel2Map(InputStream is) {
		Map<String, String> map = null;
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			int rowstart = xssfSheet.getFirstRowNum();
			int rowEnd = xssfSheet.getLastRowNum();
			map = new HashMap<String, String>();
			for (int i = rowstart; i <= rowEnd; i++) {
				XSSFRow row = xssfSheet.getRow(i);
				if (null == row)
					continue;
				XSSFCell cell = row.getCell(0);
				String oldUrl = cell.getStringCellValue();
				if (oldUrl != null && oldUrl != "") {
					String temp = oldUrl.substring(8, oldUrl.indexOf(".1688.com/"));
					String temp1 = oldUrl.substring(0, oldUrl.indexOf(".1688.com/") + 10);
					map.put(temp, temp1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * ????????????:???????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/findAllGoods")
	@ResponseBody
	protected EasyUiJsonResult findAllGoods(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ParseException {
		EasyUiJsonResult json = new EasyUiJsonResult();

		String str = request.getParameter("page");
		String shopId = request.getParameter("shopId");

		int page = 1;
		if (str == null) {
			str = "1";
		} else {
			page = Integer.parseInt(str);
		}
		int start = (page - 1) * 30;
		List<ShopGoods> findAll = shopUrlService.findAllGoods(shopId, start, 30);
		int total = shopUrlService.goodsTotal(shopId);
		json.setRows(findAll);
		json.setTotal(total);
		return json;
	}

	/**
	 * 
	 * @Title jumpGoodsReady
	 * @Description ?????????????????????????????????????????????
	 * @param request
	 * @param response
	 * @return
	 * @return ModelAndView
	 */
	@RequestMapping("/jumpGoodsReady.do")
	public ModelAndView jumpGoodsReady(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("goodsReady");
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			mv.addObject("msgStr", "???????????????");
			mv.addObject("show", 0);
			return mv;
		}

		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			mv.addObject("msgStr", "??????shopId??????");
			mv.addObject("show", 0);
			return mv;
		} else {
			mv.addObject("shopId", shopId);
		}

		try {
			List<ShopInfoBean> infos = shopUrlService.queryInfoByShopId(shopId, "");
			List<ShopGoodsInfo> goodsList = shopUrlService.query1688GoodsByShopId(shopId);
			boolean is = false;

			// ?????????????????????????????????????????????????????????????????????
			List<ShopInfoBean> newInfos = new ArrayList<ShopInfoBean>();
			Map<String, ShopInfoBean> resultMap = new HashMap<String, ShopInfoBean>();
			for (ShopGoodsInfo goods : goodsList) {
				if (goods.getWeight() == null || "".equals(goods.getWeight())) {
					// ?????????????????????
					if (resultMap.containsKey(goods.getCategoryId())) {
						ShopInfoBean spInfo = resultMap.get(goods.getCategoryId());
						spInfo.setGoodsNum(spInfo.getGoodsNum() + 1);
						spInfo.setMinWeight(0);
						spInfo.setMaxWeight(0);
						spInfo.setWeightInterval(spInfo.getMinWeight() + "-" + spInfo.getMaxWeight());
						spInfo.setShopId(shopId);
					} else {
						ShopInfoBean otSpInfo = new ShopInfoBean();
						otSpInfo.setCategoryId(goods.getCategoryId());
						otSpInfo.setMinWeight(0);
						otSpInfo.setMaxWeight(0);
						otSpInfo.setGoodsNum(1);
						otSpInfo.setShopId(shopId);
						otSpInfo.setWeightInterval(otSpInfo.getMinWeight() + "-" + otSpInfo.getMaxWeight());
						resultMap.put(goods.getCategoryId(), otSpInfo);
					}
				} else {
					// ????????????,??????????????????????????????
					String weight_1688 = StrUtils.object2Str(goods.getWeight());
					weight_1688 = StrUtils.matchStr(weight_1688, "(\\d+(\\.\\d+){0,1})");
					float weight = Float.valueOf(weight_1688);
					// ?????????????????????
					if (resultMap.containsKey(goods.getCategoryId())) {
						ShopInfoBean spInfo = resultMap.get(goods.getCategoryId());
						spInfo.setGoodsNum(spInfo.getGoodsNum() + 1);
						// ?????????
						if (spInfo.getMinWeight() > weight) {
							spInfo.setMinWeight(weight);
						}
						// ?????????
						if (spInfo.getMaxWeight() < weight) {
							spInfo.setMaxWeight(weight);
						}
						spInfo.setWeightInterval(spInfo.getMinWeight() + "-" + spInfo.getMaxWeight());
						spInfo.setShopId(shopId);
					} else {
						ShopInfoBean otSpInfo = new ShopInfoBean();
						otSpInfo.setCategoryId(goods.getCategoryId());
						otSpInfo.setMinWeight(weight);
						otSpInfo.setMaxWeight(weight);
						otSpInfo.setGoodsNum(1);
						otSpInfo.setWeightInterval(weight + "-" + weight);
						otSpInfo.setShopId(shopId);
						resultMap.put(goods.getCategoryId(), otSpInfo);
					}
				}
			}

			List<ShopInfoBean> updateInfos = new ArrayList<ShopInfoBean>();
			if (!(infos.size() == resultMap.size())) {
				for (ShopInfoBean newSpInfo : resultMap.values()) {
					boolean check = false;
					for (ShopInfoBean spIf : infos) {
						if (spIf.getCategoryId().equals(newSpInfo.getCategoryId())) {
							if (!spIf.getWeightInterval().equals(newSpInfo.getWeightInterval())) {
								updateInfos.add(newSpInfo);
							}
							check = true;
							break;
						}
					}
					if (!check) {
						newInfos.add(newSpInfo);
					}
				}
				if (updateInfos.size() > 0) {
					shopUrlService.updateShopInfos(updateInfos);
					updateInfos.clear();
				}
				if (newInfos.size() > 0) {
					is = shopUrlService.insertShopInfos(newInfos);
					if (!is) {
						shopUrlService.insertShopInfos(newInfos);
					}
					if (is) {
						infos.clear();
						infos = shopUrlService.queryInfoByShopId(shopId, "");
					} else {
						mv.addObject("msgStr", "?????????????????????????????????");
						mv.addObject("show", 0);
						return mv;
					}
				}
			} else {
				for (ShopInfoBean newSpInfo : resultMap.values()) {
					for (ShopInfoBean spIf : infos) {
						if (spIf.getCategoryId().equals(newSpInfo.getCategoryId())) {
							if (!spIf.getWeightInterval().equals(newSpInfo.getWeightInterval())) {
								updateInfos.add(newSpInfo);
							}
							break;
						}
					}
				}
				if (updateInfos.size() > 0) {
					shopUrlService.updateShopInfos(updateInfos);
					updateInfos.clear();
					infos.clear();
					infos = shopUrlService.queryInfoByShopId(shopId, "");
				}
			}

			Map<String, ShopInfoBean> cidMap = new HashMap<String, ShopInfoBean>();// ???????????????
			// ?????????????????????
			float wholeRate = 0;
			int count = 0;
			if (category1688List == null || category1688List.size() == 0) {
				category1688List = shopUrlService.queryAll1688Category();
			}
			// ?????????????????????????????????
			List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);
			List<ShopInfoKeyWeight> infoNews = new ArrayList<ShopInfoKeyWeight>();
			for (ShopInfoBean tpSp : infos) {
				if (tpSp.getFirstIntervalRate() > 0) {
					wholeRate += tpSp.getFirstIntervalRate();
					count++;
				}
				cidMap.put(tpSp.getCategoryId(), tpSp);
				for (Category1688Bean category : category1688List) {
					if (tpSp.getCategoryId().equals(category.getCategoryId())) {
						tpSp.setCategoryName(category.getCategoryName());
						break;
					}
				}
				// ????????????
				ShopInfoKeyWeight infoKw = new ShopInfoKeyWeight();
				infoKw.setCategoryId(tpSp.getCategoryId());
				infoKw.setCategoryName(tpSp.getCategoryName());
				infoKw.setFirstIntervalRate(tpSp.getFirstIntervalRate());
				infoKw.setGoodsNum(tpSp.getGoodsNum());
				infoKw.setIsChoose(tpSp.getIsChoose());
				infoKw.setOtherIntervalRate(tpSp.getOtherIntervalRate());
				infoKw.setShopId(tpSp.getShopId());
				infoKw.setSuggestRate(tpSp.getSuggestRate());
				infoKw.setWeightInterval(tpSp.getWeightInterval());
				if (tpSp.getWeightVal() == 0) {
					float tempWeight = shopUrlService.calculateAvgWeightByCatid(shopId, tpSp.getCategoryId());
					BigDecimal b = new BigDecimal(tempWeight);
					infoKw.setWeightVal(b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
				} else {
					infoKw.setWeightVal(tpSp.getWeightVal());
				}

				List<ShopCatidWeight> ctwts = new ArrayList<ShopCatidWeight>();
				infoKw.setCtwts(ctwts);

				// ?????????????????????????????????
				for (ShopCatidWeight scWt : ctwtList) {
					if (scWt.getCatid().equals(infoKw.getCategoryId())
							&& !("".equals(scWt.getKeyword()) || scWt.getKeyword() == null)) {
						infoKw.getCtwts().add(scWt);
					}
				}

				// ?????????????????????????????????????????????????????????

				infoNews.add(infoKw);

			}
			if (count == 0) {
				mv.addObject("wholeRate", 0);
			} else {
				mv.addObject("wholeRate",
						new BigDecimal(wholeRate / count).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());
			}

			// mv.addObject("infos", infos);

			infos.clear();

			List<GoodsProfitReference> profits = shopUrlService.queryGoodsProfitReferences(shopId);
			if (!(profits == null || profits.size() == 0)) {

				// ??????????????????????????????????????????
				for (GoodsProfitReference pft : profits) {
					if (pft.getFinalWeight() == null || "".equals(pft.getFinalWeight())) {
						continue;
					}
					String wprice = pft.getWprice();
					if (wprice == null || "".equals(wprice) || wprice.length() < 3) {
						pft.setFirstPrice(pft.getPrice());
					} else {
						wprice = wprice.replace("[", "").replace("]", "");
						String[] priceLis = wprice.split(",");
						String[] priceVs = priceLis[0].split("\\$");
						pft.setFirstPrice(priceVs[1].trim());
						priceLis = null;
						priceVs = null;
					}
					// ?????????????????????
					double price = Double.valueOf(pft.getFirstPrice());
					// ??????
					double finalWeight = Double.valueOf(pft.getFinalWeight());
					// ??????
					double feeprice = FeightUtils.getCarFeightNew(finalWeight,Integer.valueOf(pft.getCategoryId()));
					pft.setFreight(new BigDecimal(feeprice / GoodsInfoUtils.EXCHANGE_RATE).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					if (pft.getWholesalePrice() == null || "".equals(pft.getWholesalePrice())) {
						continue;
					}
					String[] wholesalePrice = pft.getWholesalePrice().replace("[", "").replace("]", "").split(",");
					// 1688??????SKU????????????
					String strWholesalePrice = wholesalePrice[0].split("\\$")[1];
					if (strWholesalePrice.indexOf("-") > -1) {
						strWholesalePrice = strWholesalePrice.split("-")[1];
					}
					// ???????????????=
					// (??????????????????????????????/???1688??????SKU????????????+??????(????????????????????????????????????))-1
					double profitMargin = (price
							/ ((Float.valueOf(strWholesalePrice) + feeprice) / GoodsInfoUtils.EXCHANGE_RATE)) - 1;
					pft.setRate(new BigDecimal(profitMargin * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					if (cidMap.containsKey(pft.getCategoryId())) {
						ShopInfoBean spInf = cidMap.get(pft.getCategoryId());
						spInf.setTotalNum(spInf.getTotalNum() + 1);
						spInf.setTotalRate(spInf.getTotalRate() + profitMargin);
					}
					
					// ??????5????????????????????????
					double feeprice5 = FeightUtils.getCarFeightNew(finalWeight * 5,Integer.valueOf(pft.getCategoryId()));
					pft.setFreight5Gd(
							new BigDecimal(feeprice5 / (5*GoodsInfoUtils.EXCHANGE_RATE) * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
			}
			if (cidMap.size() > 0) {
				for (ShopInfoBean tpIf : cidMap.values()) {
					if (tpIf.getTotalNum() > 0) {
						tpIf.setSuggestRate(new BigDecimal(tpIf.getTotalRate() / tpIf.getTotalNum())
								.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					}
				}
			}
			mv.addObject("profits", profits);
			// ???????????????????????????????????????
			Map<String, Integer> rsMap = shopUrlService.queryDealState(shopId);
			if (rsMap == null || rsMap.size() == 0) {
				mv.addObject("dealState", 0);
			} else {
				if (rsMap.get("shop_state") > 0) {
					if (rsMap.get("shop_state") == 1) {
						mv.addObject("dealState", 1);
					} else if (rsMap.get("shop_state") == 2) {
						mv.addObject("dealState", 2);
					} else {
						mv.addObject("dealState", 0);
					}
				} else {
					mv.addObject("dealState", 0);
				}
			}

			// ?????????????????????????????????
			Map<String, Double> catidWeightMap = new HashMap<String, Double>();
			for (ShopInfoKeyWeight infoKey : infoNews) {
				catidWeightMap.put(infoKey.getCategoryId(), (double) infoKey.getWeightVal());
			}

			List<GoodsOfferBean> goodsInfos = shopUrlService.queryOriginalGoodsInfo(shopId);
			// resultMap????????????
			Map<String, CatidStatisticalResult> catidResultMap = new HashMap<String, CatidStatisticalResult>();
			List<GoodsOfferBean> goodsErrInfos = new ArrayList<GoodsOfferBean>();
			// ?????????????????????
			for (GoodsOfferBean gdOf : goodsInfos) {
				// ??????????????????????????? * ??????
				gdOf.setAvgWeightfreight(FeightUtils.getCarFeightNew(catidWeightMap.get(gdOf.getCatid()),Integer.valueOf(gdOf.getCatid())));
				// ????????????????????? * ??????
				if (!(catidWeightMap.get(gdOf.getCatid()) == null || catidWeightMap.get(gdOf.getCatid()) == 0)) {
					gdOf.setGoodsWeightfreight(FeightUtils.getCarFeightNew(gdOf.getWeight(),Integer.valueOf(gdOf.getCatid())));
				}
				// ?????????????????????0???????????????????????????????????????????????????????????????
				if (gdOf.getAvgWeightfreight() == 0) {
					continue;
				}
				// ??????????????????????????????0???????????????????????????????????????????????????
				if (gdOf.getGoodsWeightfreight() == 0) {
					if (gdOf.getWeightFlag() == 0 && gdOf.getWeightFlag() != 5) {
						gdOf.setWeightFlag(5);
						goodsErrInfos.add(gdOf);
					}
					if (catidResultMap.containsKey(gdOf.getCatid())) {
						CatidStatisticalResult csRe1 = catidResultMap.get(gdOf.getCatid());
						csRe1.setWeightZoneNum(csRe1.getWeightZoneNum() + 1);
					} else {
						CatidStatisticalResult csRe = new CatidStatisticalResult();
						csRe.setCatid(gdOf.getCatid());
						csRe.setShopId(shopId);
						csRe.setWeightZoneNum(1);
						catidResultMap.put(gdOf.getCatid(), csRe);
					}
					continue;
				}
				// 1??????:?????? ????????? ????????? ??????????????????????????? * ?????? > ??????????????????*0.4 ??????????????????????????? ?????????
				// ?????????????????? ??? 30%??????????????????????????????????????????
				if (gdOf.getAvgWeightfreight() > gdOf.getPrice() * 0.4) {
					if (gdOf.getWeight() < catidWeightMap.get(gdOf.getCatid()) * 0.7) {
						if (gdOf.getWeightFlag() == 0 && gdOf.getWeightFlag() != 3) {
							gdOf.setWeightFlag(3);
							goodsErrInfos.add(gdOf);
						}
						if (catidResultMap.containsKey(gdOf.getCatid())) {
							CatidStatisticalResult csRe1 = catidResultMap.get(gdOf.getCatid());
							csRe1.setTooLightNum(csRe1.getTooLightNum() + 1);
						} else {
							CatidStatisticalResult csRe = new CatidStatisticalResult();
							csRe.setCatid(gdOf.getCatid());
							csRe.setShopId(shopId);
							csRe.setTooLightNum(1);
							catidResultMap.put(gdOf.getCatid(), csRe);
						}
					}
				}

				// 2??????: ????????????????????? * ?????? > ??????????????????*0.8 ??? ??????????????????????????????????????? ??? ?????????????????? ??? 30%
				// ????????????????????????????????????????????????
				if (gdOf.getGoodsWeightfreight() > gdOf.getPrice() * 0.8) {
					if (gdOf.getWeight() > catidWeightMap.get(gdOf.getCatid()) * 1.3) {
						if (gdOf.getWeightFlag() == 0 && gdOf.getWeightFlag() != 4) {
							gdOf.setWeightFlag(4);
							goodsErrInfos.add(gdOf);
						}
						if (catidResultMap.containsKey(gdOf.getCatid())) {
							CatidStatisticalResult csRe1 = catidResultMap.get(gdOf.getCatid());
							csRe1.setTooHeavyNum(csRe1.getTooHeavyNum() + 1);
						} else {
							CatidStatisticalResult csRe = new CatidStatisticalResult();
							csRe.setCatid(gdOf.getCatid());
							csRe.setShopId(shopId);
							csRe.setTooHeavyNum(1);
							catidResultMap.put(gdOf.getCatid(), csRe);
						}
					}
				}
			}
			// ?????????????????????????????????
			if (goodsErrInfos.size() > 0) {
				shopUrlService.batchUpdateErrorWeightGoods(goodsErrInfos);
			}

			// ??????????????????
			for (ShopInfoKeyWeight infoKey : infoNews) {
				if (catidResultMap.containsKey(infoKey.getCategoryId())) {
					CatidStatisticalResult csRe3 = catidResultMap.get(infoKey.getCategoryId());
					infoKey.setTooLightNum(csRe3.getTooLightNum());
					infoKey.setTooHeavyNum(csRe3.getTooHeavyNum());
					infoKey.setWeightZoneNum(csRe3.getWeightZoneNum());
				}
			}
			goodsInfos.clear();
			catidResultMap.clear();
			ctwtList.clear();
			mv.addObject("infos", infoNews);

			mv.addObject("show", 1);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("jumpGoodsReady error:" + e.getMessage());
			LOG.error("jumpGoodsReady error:" + e.getMessage());
			mv.addObject("msgStr", "??????????????????????????????" + e.getMessage());
			mv.addObject("show", 0);
		}
		return mv;
	}

	/**
	 * 
	 * @Title saveAndUpdateInfos
	 * @Description ???????????????????????????????????????????????????
	 * @param request
	 * @param response
	 * @return
	 * @return JsonResult
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/saveAndUpdateInfos.do")
	public JsonResult saveAndUpdateInfos(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}
		String shopId = request.getParameter("shopId");
		if (shopId == null && "".equals(shopId)) {
			json.setOk(false);
			json.setMessage("??????shopId??????");
			return json;
		}
		String infos = request.getParameter("infos");
		if (infos == null && "".equals(infos)) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}
		try {

			JSONArray jsonArray = JSONArray.fromObject(infos);// ???String?????????json
			List<ShopInfoBean> shopInfos = (List<ShopInfoBean>) JSONArray.toCollection(jsonArray, ShopInfoBean.class);

			List<ShopCatidWeight> cidWtList = new ArrayList<ShopCatidWeight>();

			for (ShopInfoBean spInfo : shopInfos) {
				spInfo.setShopId(shopId);
				spInfo.setAdminId(user.getId());
				// ?????????????????????????????????
				if (!(spInfo.getKeyWeight() == null || "".equals(spInfo.getKeyWeight()))) {
					String[] tempWeightList = spInfo.getKeyWeight().split(",");
					for (String keyWt : tempWeightList) {
						String[] keyWtLs = keyWt.split("@");
						if (keyWtLs != null && keyWtLs.length == 3) {
							ShopCatidWeight ctwt = new ShopCatidWeight();
							ctwt.setId(Integer.valueOf(keyWtLs[0]));
							ctwt.setAdminId(user.getId());
							ctwt.setAvgWeight(Double.valueOf(keyWtLs[1]));
							ctwt.setCatid(spInfo.getCategoryId());
							ctwt.setKeyword(keyWtLs[2]);
							ctwt.setShopId(shopId);
							cidWtList.add(ctwt);
						}
						keyWtLs = null;
					}
					tempWeightList = null;
				}
			}
			shopUrlService.saveAndUpdateInfos(shopInfos);
			// ?????????????????????????????????????????????????????????????????????????????????
			if (cidWtList.size() > 0) {
				List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);
				if (ctwtList == null || ctwtList.size() == 0) {
					shopUrlService.batchInsertCatidWeight(cidWtList);
				} else {
					List<ShopCatidWeight> insertCidWtList = new ArrayList<ShopCatidWeight>();
					List<ShopCatidWeight> updateCidWtList = new ArrayList<ShopCatidWeight>();
					for (ShopCatidWeight newCtwt : cidWtList) {
						if (newCtwt.getId() > 0) {
							for (ShopCatidWeight oldCtwt : ctwtList) {
								if (newCtwt.getId() == oldCtwt.getId()) {
									updateCidWtList.add(newCtwt);
									break;
								}
							}
						} else {
							insertCidWtList.add(newCtwt);
						}
					}
					if (insertCidWtList.size() > 0) {
						shopUrlService.batchInsertCatidWeight(insertCidWtList);
					}
					if (updateCidWtList.size() > 0) {
						shopUrlService.batchUpdateCatidWeight(updateCidWtList);
					}
				}
			}
			//??????????????????
			boolean isSuccess = shopUrlService.updateWeightFlag(shopId);
			if(!isSuccess){
				shopUrlService.updateWeightFlag(shopId);
			}
			// ?????????????????????????????????
			// Thread thread = new Thread(new
			// ShopGoodsDealThread(shopId));thread.start();
			json.setOk(true);
			json.setMessage("????????????");
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("????????????????????????" + e.getMessage());
			System.err.println("saveAndUpdateInfos error:" + e.getMessage());
			LOG.error("saveAndUpdateInfos error:" + e.getMessage());
		}
		return json;
	}

	@RequestMapping("/doGoodsClear.do")
	public JsonResult doGoodsClear(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setMessage("???????????????");
			json.setOk(false);
			return json;
		}

		String sourceTbl = request.getParameter("sourceTbl");
		String saveTbl = request.getParameter("saveTbl");
//		if (shopId == null || "".equals(shopId)) {
//			json.setMessage("??????shopId??????");
//			json.setOk(false);
//			return json;
//		}

		try {// ?????????????????????????????????
			Thread thread = new Thread(new CoreGoodsDealThread(sourceTbl,saveTbl));
			thread.start();
			json.setOk(true);
			json.setMessage("????????????");
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("????????????????????????" + e.getMessage());
			System.err.println("doGoodsClear error:" + e.getMessage());
			LOG.error("doGoodsClear error:" + e.getMessage());
		}
		return json;
	}

	/**
	 * 
	 * @Title beforeOnlineGoodsShow
	 * @Description ????????????????????????
	 * @param request
	 * @param response
	 * @return
	 * @return ModelAndView
	 */
	@RequestMapping("/beforeOnlineGoodsShow.do")
	public ModelAndView beforeOnlineGoodsShow(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("goodsReadyShow");
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			mv.addObject("msgStr", "???????????????");
			mv.addObject("show", 0);
			return mv;
		}

		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			mv.addObject("msgStr", "??????shopId??????");
			mv.addObject("show", 0);
			return mv;
		} else {
			mv.addObject("shopId", shopId);
		}

		try {
			List<ShopGoodsInfo> goodsList = shopUrlService.queryDealGoodsByShopId(shopId);
			if (goodsList == null || goodsList.size() == 0) {
				mv.addObject("msgStr", "????????????????????????????????????!");
				mv.addObject("show", 0);
				mv.addObject("goodsNum", 0);
			} else {
				//?????????????????????????????????
				List<CustomGoodsPublish> gdList =  shopUrlService.queryOnlineGoodsByShopId(shopId);
				Map<String,Integer> validMap = new HashMap<String, Integer>();
				Map<String,String> isEditMap = new HashMap<String, String>();
				for(CustomGoodsPublish onlGd: gdList){
					validMap.put(onlGd.getPid(), onlGd.getValid());
					isEditMap.put(onlGd.getPid(), String.valueOf(onlGd.getIsEdited()));
				}
				
				for (ShopGoodsInfo spGoods : goodsList) {
					if(validMap.containsKey(spGoods.getPid())){
						spGoods.setOnlineFlag(1);
						spGoods.setOnlineValid(validMap.get(spGoods.getPid()));
					}
					if(isEditMap.containsKey(spGoods.getPid())){
						spGoods.setOnlineEdit(Integer.valueOf(isEditMap.get(spGoods.getPid())));
					}
					String range_price = StrUtils.object2Str(spGoods.getRangePrice());
					if (StringUtils.isBlank(range_price)) {
						List<String> matchStrList = StrUtils.matchStrList("(\\$\\s*\\d+\\.\\d+)",
								StrUtils.object2Str(spGoods.getWprice()));
						if (matchStrList != null && !matchStrList.isEmpty()) {
							range_price = StrUtils.matchStr(matchStrList.get(matchStrList.size() - 1), "(\\d+\\.\\d+)");
							if (matchStrList.size() > 1) {
								range_price = range_price + "-"
										+ StrUtils.matchStr(matchStrList.get(0), "(\\d+\\.\\d+)");
							}
						} else {
							range_price = StrUtils.object2Str(spGoods.getPrice());
						}
					}
					spGoods.setShowPrice(range_price);
					// ??????????????????
					if (!(spGoods.getRemotePath() == null || "".equals(spGoods.getRemotePath()))) {
						spGoods.setImgUrl(spGoods.getRemotePath() + spGoods.getImgUrl());
					}
					spGoods.setGoodsUrl("&source=D" + Md5Util.encoder(spGoods.getPid()) + "&item=" + spGoods.getPid());

					// ??????????????????????????????
					if (spGoods.getEnInfo() == null || "".equals(spGoods.getEnInfo())) {
						continue;
					} else {
						// jsoup??????eninfo??????
						Document tempDoc = Jsoup.parseBodyFragment(spGoods.getEnInfo());
						Elements imgLst = tempDoc.getElementsByTag("img");
						if (!(imgLst == null || imgLst.size() == 0)) {
							spGoods.setEnInfoNum(imgLst.size());
						}
						tempDoc = null;
					}
				}
				mv.addObject("goodsList", goodsList);
				mv.addObject("goodsNum", goodsList.size());
				// ???????????????????????????????????????
				Map<String, String> stateMap = shopUrlService.queryShopDealState(shopId);
				if (stateMap.size() > 0) {
					// 0????????? 1???????????? 2????????????3????????????
					if ("0".equals(stateMap.get("onlineState"))) {
						mv.addObject("onlineState", 0);
						mv.addObject("stateDescribe", "?????????");
					} else if ("1".equals(stateMap.get("onlineState"))) {
						mv.addObject("onlineState", 1);
						mv.addObject("stateDescribe", "????????????");
					} else if ("2".equals(stateMap.get("onlineState"))) {
						mv.addObject("onlineState", 2);
						mv.addObject("stateDescribe", "????????????");
					} else if ("3".equals(stateMap.get("onlineState"))) {
						mv.addObject("onlineState", 3);
						mv.addObject("stateDescribe", "????????????");
					} else if ("4".equals(stateMap.get("onlineState"))) {
						mv.addObject("onlineState", 4);
						mv.addObject("stateDescribe", "?????????");
					}
				} else {
					mv.addObject("onlineState", 0);
					mv.addObject("stateDescribe", "?????????");
				}
				mv.addObject("show", 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("beforeOnlineGoodsShow error:" + e.getMessage());
			LOG.error("beforeOnlineGoodsShow error:" + e.getMessage());
			mv.addObject("msgStr", "??????????????????????????????" + e.getMessage());
			mv.addObject("show", 0);
		}
		return mv;
	}

	/**
	 * 
	 * @Title deleteShopReadyGoods
	 * @Description ?????????????????????????????????????????????????????????
	 * @param request
	 * @param response
	 * @return
	 * @return JsonResult
	 */
	@ResponseBody
	@RequestMapping("/deleteShopReadyGoods.do")
	public JsonResult deleteShopReadyGoods(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}
		String shopId = request.getParameter("shopId");
		if (shopId == null && "".equals(shopId)) {
			json.setOk(false);
			json.setMessage("??????shopId??????");
			return json;
		}
		String pids = request.getParameter("pids");
		if (pids == null && "".equals(pids)) {
			json.setOk(false);
			json.setMessage("??????pid??????");
			return json;
		}
		try {

			shopUrlService.deleteShopReadyGoods(shopId, pids);
			json.setOk(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("????????????????????????" + e.getMessage());
			System.err.println("shopId:" + shopId + ",pids:" + pids + ",deleteShopGoods error:" + e.getMessage());
			LOG.error("shopId:" + shopId + ",pids:" + pids + ",deleteShopGoods error:" + e.getMessage());
		}
		return json;
	}
	
	
	/**
	 * 
	 * @Title deleteShopOfferGoods 
	 * @Description ?????????????????????????????????
	 * @param request
	 * @param response
	 * @return
	 * @return JsonResult
	 */
	@ResponseBody
	@RequestMapping("/deleteShopOfferGoods.do")
	public JsonResult deleteShopOfferGoods(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}
		String shopId = request.getParameter("shopId");
		if (shopId == null && "".equals(shopId)) {
			json.setOk(false);
			json.setMessage("??????shopId??????");
			return json;
		}
		String pids = request.getParameter("pids");
		if (pids == null && "".equals(pids)) {
			json.setOk(false);
			json.setMessage("??????pid??????");
			return json;
		}
		try {

			shopUrlService.deleteShopOfferGoods(shopId, pids);
			json.setOk(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("????????????????????????" + e.getMessage());
			System.err.println("shopId:" + shopId + ",pids:" + pids + ",deleteShopGoods error:" + e.getMessage());
			LOG.error("shopId:" + shopId + ",pids:" + pids + ",deleteShopGoods error:" + e.getMessage());
		}
		return json;
	}

	/**
	 * 
	 * @Title showShopPublicImg
	 * @Description ?????????????????????????????????
	 * @param request
	 * @param response
	 * @return
	 * @return ModelAndView
	 */
	@RequestMapping("/showShopPublicImg.do")
	public ModelAndView showShopPublicImg(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("goodsAllImgShow");
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			mv.addObject("msgStr", "???????????????");
			mv.addObject("show", 0);
			return mv;
		}

		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			mv.addObject("msgStr", "??????shopId??????");
			mv.addObject("show", 0);
			return mv;
		} else {
			mv.addObject("shopId", shopId);
		}

		String useHmStr = request.getParameter("useHm");
		int useHm = 0;
		if (useHmStr == null || "".equals(useHmStr)) {
			useHm = 0;
		} else {
			useHm = Integer.valueOf(useHmStr);
		}
		mv.addObject("useHm", useHm);

		try {
			List<ShopGoodsEnInfo> imgsList = shopUrlService.queryDealGoodsWithInfoByShopId(shopId);
			if (imgsList == null || imgsList.size() == 0) {
				mv.addObject("msgStr", "??????????????????");
				mv.addObject("show", 0);
			} else {

				List<ShopGoodsLocalImg> localPidImgs = new ArrayList<ShopGoodsLocalImg>();
				for (ShopGoodsEnInfo spGoods : imgsList) {
					// jsoup??????eninfo??????
					if (spGoods.getEnInfo() == null || "".equals(spGoods.getEnInfo())) {
						continue;
					}
					Document tempDoc = Jsoup.parseBodyFragment(spGoods.getEnInfo());
					Elements imgLst = tempDoc.getElementsByTag("img");
					if (!(imgLst == null || imgLst.size() == 0)) {
						List<String> imgs = new ArrayList<String>();
						for (Element imgEl : imgLst) {
							String imgUrl = imgEl.attr("src");
							if (imgUrl == null || "".equals(imgUrl)) {
								continue;
							} else {
								imgs.add(imgUrl);
								if (useHm > 0) {
									ShopGoodsLocalImg lcImg = new ShopGoodsLocalImg();
									lcImg.setPid(spGoods.getPid());
									lcImg.setLpImg(spGoods.getLocalPath().replace("\\", "/") + imgUrl);
									File tempFile = new File(lcImg.getLpImg());
									long tempLength = tempFile.length();
									lcImg.setImgSize(tempLength);
									lcImg.setImgMd5(MD5Util.getFileMD5String(tempFile));
									localPidImgs.add(lcImg);
								}
							}
						}
						spGoods.setImgs(imgs);
					}
					tempDoc = null;
					imgLst.clear();
				}
				// ????????????????????????????????????????????????????????????
				Map<String, ShopGoodsPublicImg> resultMap = new HashMap<String, ShopGoodsPublicImg>();
				// ?????? ????????????
				if (useHm > 0) {
					// ????????????????????????????????????????????????????????????
					for (ShopGoodsLocalImg lcImg : localPidImgs) {
						if (resultMap.containsKey(lcImg.getImgMd5())) {
							ShopGoodsPublicImg tpLcImg = resultMap.get(lcImg.getImgMd5());
							tpLcImg.setTotalNum(tpLcImg.getTotalNum() + 1);
						} else {
							ShopGoodsPublicImg tpLcImg = new ShopGoodsPublicImg();
							tpLcImg.setImgUrl(lcImg.getLpImg().replace("K:/shopimages/", "http://117.144.21.74:8000/"));
							tpLcImg.setPids(lcImg.getImgMd5());
							tpLcImg.setTotalNum(tpLcImg.getTotalNum() + 1);
							resultMap.put(lcImg.getImgMd5(), tpLcImg);
						}
					}
				} else {
					// ????????????????????????
					Map<String, List<String>> pidEninfoImgs = new HashMap<String, List<String>>();
					// 1.?????????????????????????????????
					for (ShopGoodsEnInfo enInfo : imgsList) {
						if (!(enInfo.getImgs() == null || enInfo.getImgs().size() == 0)) {
							// ????????????????????????
							List<String> localImgs = new ArrayList<String>();
							if (pidEninfoImgs.containsKey(enInfo.getPid())) {
								localImgs = pidEninfoImgs.get(enInfo.getPid());
							} else {
								pidEninfoImgs.put(enInfo.getPid(), localImgs);
							}

							for (String tempImg : enInfo.getImgs()) {
								// ????????????????????????????????????
								localImgs.add(enInfo.getLocalPath().replace("\\", "/") + tempImg);
								String checkImg = tempImg.substring(tempImg.lastIndexOf("/") + 1);
								if (resultMap.containsKey(checkImg)) {
									ShopGoodsPublicImg gdImg = resultMap.get(checkImg);
									String pids = (gdImg.getPids() == null ? "" : gdImg.getPids()) + ",";
									if (!(pids.contains(enInfo.getPid() + ","))) {
										gdImg.setPids((gdImg.getPids() == null ? "" : gdImg.getPids()) + ","
												+ enInfo.getPid());
										gdImg.setTotalNum(gdImg.getTotalNum() + 1);
									}
								} else {
									ShopGoodsPublicImg gdImg = new ShopGoodsPublicImg();
									gdImg.setPids(
											(gdImg.getPids() == null ? "" : gdImg.getPids()) + "," + enInfo.getPid());
									gdImg.setTotalNum(gdImg.getTotalNum() + 1);
									gdImg.setImgUrl(enInfo.getRemotePath() + tempImg);
									resultMap.put(checkImg, gdImg);
								}
							}
						}
					}
				}

				List<ShopGoodsPublicImg> newImgsList = new ArrayList<ShopGoodsPublicImg>();
				if (resultMap.size() > 0) {
					for (ShopGoodsPublicImg tempGd : resultMap.values()) {
						newImgsList.add(tempGd);
					}
				}
				if (newImgsList.size() > 0) {
					// ??????????????????
					Collections.sort(newImgsList, new Comparator<ShopGoodsPublicImg>() {
						public int compare(ShopGoodsPublicImg o1, ShopGoodsPublicImg o2) {
							return o2.getTotalNum() - o1.getTotalNum();
						}
					});
				}
				imgsList.clear();
				localPidImgs.clear();
				mv.addObject("newImgsList", newImgsList);
				mv.addObject("showTotal", newImgsList.size());
				mv.addObject("show", 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("shopId:" + shopId + ",showShopPublicImg error:" + e.getMessage());
			LOG.error("shopId:" + shopId + ",showShopPublicImg error:" + e.getMessage());
			mv.addObject("msgStr", "??????????????????????????????" + e.getMessage());
			mv.addObject("show", 0);
		}
		return mv;
	}

	public void showShopPublicImgTest(String shopId) {
	}

	/**
	 * 
	 * @Title deleteGoodsImgs
	 * @Description ?????????????????????????????????
	 * @param request
	 * @param response
	 * @return
	 * @return JsonResult
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/deleteGoodsImgs.do")
	public JsonResult deleteGoodsImgs(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}
		String shopId = request.getParameter("shopId");
		if (shopId == null && "".equals(shopId)) {
			json.setOk(false);
			json.setMessage("??????shopId??????");
			return json;
		}
		String useHmStr = request.getParameter("useHm");
		int useHm = 0;
		if (useHmStr == null && "".equals(useHmStr)) {
			json.setOk(false);
			json.setMessage("??????????????????????????????");
			return json;
		} else {
			useHm = Integer.valueOf(useHmStr);
		}

		String imgs = request.getParameter("imgs");
		if (imgs == null && "".equals(imgs)) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}
		try {
			JSONArray jsonArray = JSONArray.fromObject(imgs);// ???String?????????json
			List<ShopGoodsPublicImg> gdImgList = (List<ShopGoodsPublicImg>) JSONArray.toCollection(jsonArray,
					ShopGoodsPublicImg.class);

			if (gdImgList == null || gdImgList.size() == 0) {
				json.setOk(false);
				json.setMessage("??????????????????????????????");
				return json;
			} else {
				// ??????????????????????????????????????????????????????
				if (useHm > 0) {
					// ??????MD5?????????list?????????????????????
					List<String> tempMd5List = new ArrayList<String>();
					for (ShopGoodsPublicImg pbImg : gdImgList) {
						tempMd5List.add(pbImg.getPids());
					}
					// ?????????????????????pid,???????????????????????????
					List<ShopGoodsEnInfo> imgsList = shopUrlService.queryDealGoodsWithInfoByShopId(shopId);
					List<ShopGoodsEnInfo> deletLst = new ArrayList<ShopGoodsEnInfo>();
					// ??????????????????
					List<ShopGoodsInfo> deleteGoodsInfos = new ArrayList<ShopGoodsInfo>();
					for (ShopGoodsEnInfo imgGd : imgsList) {

						if (imgGd.getEnInfo() == null || "".equals(imgGd.getEnInfo())) {
							continue;
						}
						Document tempDoc = Jsoup.parseBodyFragment(imgGd.getEnInfo());
						Elements imgLst = tempDoc.getElementsByTag("img");
						boolean isExists = false;
						if (!(imgLst == null || imgLst.size() == 0)) {
							for (Element imgEl : imgLst) {
								String imgUrl = imgEl.attr("src");
								if (imgUrl == null || "".equals(imgUrl)) {
									continue;
								} else {
									String tempMd5 = MD5Util.getFileMD5String(
											new File(imgGd.getLocalPath().replace("\\", "/") + imgUrl));
									if (tempMd5List.contains(tempMd5)) {
										isExists = true;
										ShopGoodsInfo delGd = new ShopGoodsInfo();
										delGd.setShopId(shopId);
										delGd.setPid(imgGd.getPid());
										delGd.setImgUrl(imgUrl);
										delGd.setLocalPath(imgGd.getLocalPath());
										delGd.setRemotePath(imgGd.getRemotePath());
										deleteGoodsInfos.add(delGd);
										imgEl.remove();
									}
								}
							}
						}
						if(isExists){
							imgGd.setEnInfo(tempDoc.toString());
							deletLst.add(imgGd);
						}						
						tempDoc = null;
						imgLst.clear();				
					}
					tempMd5List.clear();
					shopUrlService.updateGoodsEninfo(deletLst);
					shopUrlService.insertShopGoodsDeleteImgs(deleteGoodsInfos, user.getId());
					deleteGoodsInfos.clear();
					json.setOk(true);
					json.setMessage("????????????");
				} else {

					// ?????????list??????,??????map????????????????????????
					Map<String, List<String>> pidImgs = new HashMap<String, List<String>>();
					for (ShopGoodsPublicImg gdImg : gdImgList) {
						if (!(gdImg.getPids() == null || "".equals(gdImg.getPids()))) {
							String tempImgName = gdImg.getImgUrl().substring(gdImg.getImgUrl().lastIndexOf("/"));
							String[] tempPids = gdImg.getPids().split(",");
							for (String tpPid : tempPids) {
								if (!(tpPid == null || "".equals(tpPid))) {
									if (pidImgs.containsKey(tpPid)) {
										List<String> imgList = pidImgs.get(tpPid);
										imgList.add(tempImgName);
									} else {
										List<String> tempImgs = new ArrayList<String>();
										tempImgs.add(tempImgName);
										pidImgs.put(tpPid, tempImgs);
									}
								}
							}
						}
					}
					// ?????????????????????pid,???????????????????????????
					List<ShopGoodsEnInfo> imgsList = shopUrlService.queryDealGoodsWithInfoByShopId(shopId);
					List<ShopGoodsEnInfo> deletLst = new ArrayList<ShopGoodsEnInfo>();
					// ??????????????????
					List<ShopGoodsInfo> deleteGoodsInfos = new ArrayList<ShopGoodsInfo>();
					for (ShopGoodsEnInfo imgGd : imgsList) {
						if (pidImgs.containsKey(imgGd.getPid())) {
							if (imgGd.getEnInfo() == null || "".equals(imgGd.getEnInfo())) {
								continue;
							}
							Document tempDoc = Jsoup.parseBodyFragment(imgGd.getEnInfo());
							Elements imgLst = tempDoc.getElementsByTag("img");
							if (!(imgLst == null || imgLst.size() == 0)) {
								List<String> tempImgs = pidImgs.get(imgGd.getPid());
								for (Element imgEl : imgLst) {
									String imgUrl = imgEl.attr("src");
									if (imgUrl == null || "".equals(imgUrl)) {
										continue;
									} else {
										String tempImgName = imgUrl.substring(imgUrl.lastIndexOf("/"));
										if (tempImgs.contains(tempImgName)) {
											ShopGoodsInfo delGd = new ShopGoodsInfo();
											delGd.setShopId(shopId);
											delGd.setPid(imgGd.getPid());
											delGd.setImgUrl(imgUrl);
											delGd.setLocalPath(imgGd.getLocalPath());
											delGd.setRemotePath(imgGd.getRemotePath());
											deleteGoodsInfos.add(delGd);
											imgEl.remove();
										}
									}
								}
							}
							imgGd.setEnInfo(tempDoc.toString());
							deletLst.add(imgGd);
							tempDoc = null;
							imgLst.clear();
						}
					}
					shopUrlService.updateGoodsEninfo(deletLst);
					shopUrlService.insertShopGoodsDeleteImgs(deleteGoodsInfos, user.getId());
					deleteGoodsInfos.clear();
					json.setOk(true);
					json.setMessage("????????????");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("????????????????????????" + e.getMessage());
			System.err.println("shopId:" + shopId + ",deleteGoodsImgs error:" + e.getMessage());
			LOG.error("shopId:" + shopId + ",deleteGoodsImgs error:" + e.getMessage());
		}
		return json;
	}

	/**
	 * 
	 * @Title editGoods
	 * @Description ??????????????????
	 * @param request
	 * @param response
	 * @return
	 * @return ModelAndView
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping("/editGoods.do")
	public ModelAndView editGoods(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("goodsEdit");
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			mv.addObject("msgStr", "???????????????");
			mv.addObject("show", 0);
			return mv;
		}

		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			mv.addObject("msgStr", "??????shopId??????");
			mv.addObject("show", 0);
			return mv;
		} else {
			mv.addObject("shopId", shopId);
		}
		String pid = request.getParameter("pid");
		if (pid == null || "".equals(pid)) {
			mv.addObject("msgStr", "??????pid??????");
			mv.addObject("show", 0);
			return mv;
		} else {
			mv.addObject("pid", pid);
		}

		try {
			CustomGoodsPublish goods = shopUrlService.queryGoodsInfo(shopId, pid);
			if (goods == null) {
				mv.addObject("msgStr", "??????????????????");
				mv.addObject("show", 0);
				return mv;
			}

			// ??????shopid??????????????????
			int queryId = 0;
			if (!(goods.getShopId() == null || "".equals(goods.getShopId()))) {
				ShopManagerPojo spmg = customGoodsService.queryByShopId(goods.getShopId());
				if (spmg != null) {
					queryId = spmg.getId();
				}
			}

			mv.addObject("jumpShopId", queryId);

			// ??????1688???????????????

			// ???goods???entype??????????????????,????????????
			List<TypeBean> typeList = deal1688GoodsType(goods);

			// ???goods???img??????????????????,????????????
			request.setAttribute("showimgs", JSONArray.fromObject("[]"));
			List<String> imgs = GoodsInfoUtils.deal1688GoodsImg(goods.getImg(),goods.getLocalpath());
			if (imgs.size() > 0) {
				String firstImg = imgs.get(0);
				goods.setShowMainImage(firstImg.replace(".60x60.", ".400x400."));
				request.setAttribute("showimgs", JSONArray.fromObject(imgs));
			}

			HashMap<String, String> pInfo = GoodsInfoUtils.deal1688Sku(goods);
			request.setAttribute("showattribute", pInfo);

			// ??????Sku??????
			// ?????????????????????????????????????????????????????????sku??????????????????

			if (goods.getRangePrice() == null || "".equals(goods.getRangePrice()) || goods.getSku() == null
					|| "".equals(goods.getSku())) {
				// request.setAttribute("showSku", JSONArray.fromObject("[]"));
			} else {
				List<ImportExSku> skuList = new ArrayList<ImportExSku>();
				JSONArray sku_json = JSONArray.fromObject(goods.getSku());
				skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
				// ????????????????????????
				List<ImportExSkuShow> cbSkus = GoodsInfoUtils.combineSkuList(typeList, skuList);
				// ????????????
				Collections.sort(cbSkus, new Comparator<ImportExSkuShow>() {
					public int compare(ImportExSkuShow o1, ImportExSkuShow o2) {
						return o1.getPpIds().compareTo(o2.getPpIds());
					}
				});
				request.setAttribute("showSku", JSONArray.fromObject(cbSkus));

				Map<String, Object> typeNames = new HashMap<String, Object>();
				for (TypeBean tyb : typeList) {
					if (!typeNames.containsKey(tyb.getTypeId())) {
						typeNames.put(tyb.getTypeId(), tyb.getType());
					}
				}
				request.setAttribute("typeNames", typeNames);
			}

			if (typeList.size() > 0) {
				request.setAttribute("showtypes", JSONArray.fromObject(typeList));
			} else {
				request.setAttribute("showtypes", JSONArray.fromObject("[]"));
			}

			// ????????????????????????
			String localpath = goods.getLocalpath();
			if (localpath == null || "".equals(localpath)) {
				localpath = "";
			}
			// ????????????????????????
			if (!(goods.getShowMainImage().indexOf("http://") > -1
					|| goods.getShowMainImage().indexOf("https://") > -1)) {
				goods.setShowMainImage(localpath + goods.getShowMainImage());
			}
			// ??????eninfo??????????????????remotepath???????????????
			String enInfo = goods.getEninfo().replaceAll("<br><img", "<img").replaceAll("<br /><img", "<img");
			// ??????img??????????????????
			String[] enInfoLst = enInfo.split("<img");
			StringBuffer textBf = new StringBuffer();
			for (String srcStr : enInfoLst) {
				// ????????????????????????????????????
				if (srcStr.indexOf("http:") > -1 || srcStr.indexOf("https:") > -1) {
					// ????????????img????????????????????????img??????src?????????
					if (srcStr.indexOf("src=") > -1) {
						textBf.append("<br><img " + srcStr);
					} else {
						textBf.append(srcStr);
					}
				} else {
					// ????????????img????????????????????????img??????src?????????
					if (srcStr.indexOf("src=") > -1) {
						textBf.append("<br><img " + srcStr.replaceAll("src=\"", "src=\"" + localpath));
					} else {
						textBf.append(srcStr);
					}
				}
			}
			// ???????????????????????????
			enInfoLst = null;

			// ??????????????????????????????????????????????????????????????????????????????????????????????????????
			if (goods.getReviseWeight() == null || "".equals(goods.getReviseWeight())) {
				goods.setReviseWeight(goods.getFinalWeight());
			}

			String text = textBf.toString();

			// ????????????????????????????????????
			mv.addObject("text", text);
			mv.addObject("pid", pid);
			goods.setWeight(StrUtils.matchStr(goods.getWeight(), "(\\d+\\.*\\d*)"));
			mv.addObject("goods", goods);
			// ????????????????????????----????????????
			String savePath = localpath.replace(localIP, rootPath);
			if (IpCheckUtil.checkIsIntranet(request)) {
				savePath = localpath.replace(wanlIP, rootPath);
			}
			mv.addObject("savePath", savePath);
			mv.addObject("localpath", localpath);
			mv.addObject("show", 1);
			return mv;

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("shopId:" + shopId + ",editGoods error:" + e.getMessage());
			LOG.error("shopId:" + shopId + ",editGoods error:" + e.getMessage());
			mv.addObject("msgStr", "??????????????????????????????" + e.getMessage());
			mv.addObject("show", 0);
		}
		return mv;
	}

	// ??????1688???????????????????????????
	private List<TypeBean> deal1688GoodsType(CustomGoodsPublish cgbean) {// ??????
		List<TypeBean> typeList = new ArrayList<TypeBean>();
		if (!(cgbean.getEntype() == null || "".equals(cgbean.getEntype()))) {
			Map<String, List<TypeBean>> typeMap = new HashMap<String, List<TypeBean>>();
			String types = cgbean.getEntype();
			// String remotPath = cgbean.getRemotpath();
			String localPath = cgbean.getLocalpath();
			if (StringUtils.isNotBlank(types) && !StringUtils.equals(types, "[]")) {
				types = types.replace("[[", "[").replace("]]", "]").trim();
				String[] matchStrList = types.split(",\\s*\\[");
				TypeBean typeBean = null;
				String[] tems = null;
				String tem = null;
				for (String str : matchStrList) {
					str = str.replace("[", "").replace("]", "");
					if (str.isEmpty()) {
						continue;
					}
					typeBean = new TypeBean();
					String[] type = str.split(",\\s*");
					for (int j = 0; j < type.length; j++) {
						if (type[j].indexOf("id=") > -1) {
							tems = type[j].split("id=");
							tem = tems.length > 1 ? tems[1] : "";
							typeBean.setId(tem);
						} else if (type[j].indexOf("type=") > -1) {
							tems = type[j].split("type=");
							tem = tems.length > 1 ? tems[1] : "";
							typeBean.setType(tem.replaceAll(chineseChar, ""));
							typeBean.setLableType(tem.replaceAll(chineseChar, ""));
						} else if (type[j].indexOf("value=") > -1) {
							tems = type[j].split("value=");
							tem = tems.length > 1 ? tems[1] : "";
							tem = StringUtils.equals(tem, "null") ? String.valueOf(j) : tem;
							typeBean.setValue(tem.replaceAll(chineseChar, ""));
						} else if (type[j].indexOf("img=") > -1) {
							tems = type[j].split("img=");
							tem = tems.length > 1 ? tems[1] : "";
							tem = tem.endsWith(".jpg") ? tem : "";
							tem = StringUtils.isBlank(tem) || StringUtils.equals(tem, "null") ? "" : localPath + tem;
							typeBean.setImg(tem);
						}
					}
					List<TypeBean> list = typeMap.get(typeBean.getType());
					if (list == null) {
						list = new ArrayList<TypeBean>();
					}
					if (StringUtils.isBlank(typeBean.getType())) {
						continue;
					}
					if (StringUtils.isBlank(typeBean.getValue())) {
						typeBean.setType(typeBean.getId());
					}
					list.add(typeBean);
					typeMap.put(typeBean.getType(), list);
				}
				Iterator<Entry<String, List<TypeBean>>> iterator = typeMap.entrySet().iterator();
				while (iterator.hasNext()) {
					typeList.addAll(iterator.next().getValue());
				}

			}
		}

		return typeList;

	}


	@RequestMapping(value = "/uploads", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> getLoads(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = "";
		String err = "";
		String pid = request.getParameter("pid");

		if (pid == null || "".equals(pid)) {
			msg = "";
			err = "??????PID??????";
		} else {
			System.out.println("pid:" + pid);
			try {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				// ???????????????
				List<MultipartFile> fileList = multipartRequest.getFiles("filedata");
				Random random = new Random();
				// ????????????????????????
				if (ftpConfig == null) {
					ftpConfig = GetConfigureInfo.getFtpConfig();
				}
				// ??????????????????????????????????????????
				JsonResult json = new JsonResult();
				checkFtpConfig(ftpConfig, json);
				String localDiskPath = ftpConfig.getLocalDiskPath();
				if (json.isOk()) {
					for (MultipartFile mf : fileList) {
						if (!mf.isEmpty()) {
							// ???????????????????????????mf.getOriginalFilename()
							String originalName = mf.getOriginalFilename();
							// ????????????????????????
							String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
							String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)) + fileSuffix);
							// ??????????????????????????????
							String localFilePath = "shopimg/" + pid + "/" + saveFilename;
							// ?????????????????????????????????????????????
							ImgDownload.writeImageToDisk(mf.getBytes(), localDiskPath + localFilePath);
							// ?????????????????????
							boolean is = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 100, 100);
							if (is) {
								msg = ftpConfig.getLocalShowPath() + localFilePath;
							} else {
								// ????????????????????????????????????
								File file = new File(localFilePath);
								if (file.exists()) {
									file.delete();
								}
								msg = "";
								err = "?????????????????????100";
							}
						}
					}
				} else {
					msg = "";
					err = json.getMessage();
				}
			} catch (Exception e) {
				msg = "";
				err = "????????????";
				e.printStackTrace();
				LOG.error("???????????????" + e.getMessage());
			}
		}
		map.put("err", err);
		map.put("msg", msg);
		return map;
	}

	/**
	 * ??????js????????????
	 * 
	 * @return
	 */
	@RequestMapping(value = "/uploadByJs", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult uploadByJs(@RequestParam(value = "pid", required = true) String pid,
                                 @RequestParam(value = "uploadfile", required = true) MultipartFile file, HttpServletRequest request) {
		JsonResult json = new JsonResult();

		if (pid == null || "".equals(pid)) {
			json.setOk(false);
			json.setMessage("??????PID??????");
		} else {
			System.out.println("pid:" + pid);
			try {
				if (!file.isEmpty()) {
					// ????????????????????????
					if (ftpConfig == null) {
						ftpConfig = GetConfigureInfo.getFtpConfig();
					}
					// ??????????????????????????????????????????
					checkFtpConfig(ftpConfig, json);
					if (!json.isOk()) {
						return json;
					}
					String localDiskPath = ftpConfig.getLocalDiskPath();
					Random random = new Random();
					String originalName = file.getOriginalFilename();
					// ????????????????????????
					String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
					String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
					// ??????????????????????????????
					String localFilePath = "shopimg/" + pid + "/" + saveFilename + fileSuffix;
					// ?????????????????????????????????????????????
					ImgDownload.writeImageToDisk(file.getBytes(), localDiskPath + localFilePath);

					// ????????????????????????????????????400*200
					boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 400, 200);
					if (checked) {
						// ????????????400x400
						String localFilePath400x400 = "shopimg/" + pid + "/" + saveFilename + ".400x400" + fileSuffix;
						boolean is400 = ImageCompression.reduceImgByWidth(400, localDiskPath + localFilePath,
								localDiskPath + localFilePath400x400);
						// ????????????60x60
						String localFilePath60x60 = "shopimg/" + pid + "/" + saveFilename + ".60x60" + fileSuffix;
						boolean is60 = ImageCompression.reduceImgByWidth(60, localDiskPath + localFilePath,
								localDiskPath + localFilePath60x60);
						if (is60 && is400) {
							json.setData(ftpConfig.getLocalShowPath() + localFilePath60x60);
							json.setOk(true);
							json.setMessage("????????????????????????");
						} else {
							// ????????????????????????????????????
							File file400 = new File(localFilePath400x400);
							if (file400.exists()) {
								file400.delete();
							}
							File file60 = new File(localFilePath60x60);
							if (file60.exists()) {
								file60.delete();
							}
							// ??????????????????????????????
							json.setOk(false);
							json.setMessage("????????????60x60???400x400?????????????????????");
						}
					} else {
						// ?????????????????????400*200??????????????????
						json.setOk(false);
						json.setMessage("?????????????????????400*200???????????????");
					}
				} else {
					json.setOk(false);
					json.setMessage("??????????????????????????????");
				}
			} catch (Exception e) {
				e.printStackTrace();
				json.setOk(false);
				json.setMessage("????????????:" + e.getMessage());
				LOG.error("???????????????" + e.getMessage());
			}
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveEditGoods")
	@ResponseBody
	public JsonResult saveEditGoods(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		String pidStr = request.getParameter("pid");
		CustomGoodsPublish cgp = new CustomGoodsPublish();
		if (!(pidStr == null || "".equals(pidStr))) {
			cgp.setPid(pidStr);
		} else {
			json.setOk(false);
			json.setMessage("??????pid??????");
			return json;
		}
		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			json.setOk(false);
			json.setMessage("??????shopId??????");
			return json;
		}
		try {
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				json.setOk(false);
				json.setMessage("????????????????????????????????????");
				return json;
			}

			String contentStr = request.getParameter("content");

			// ??????????????????
			CustomGoodsPublish orGoods = shopUrlService.queryGoodsInfo(shopId, pidStr);
			String orFinalWeight = orGoods.getFinalWeight();

			String remotepath = request.getParameter("remotepath");
			if (remotepath == null || "".equals(remotepath)) {
				json.setOk(false);
				json.setMessage("??????????????????????????????");
				return json;
			} else {
				cgp.setRemotpath(remotepath);
			}
			String enname = request.getParameter("enname");
			if (!(enname == null || "".equals(enname))) {
				cgp.setEnname(enname);
			} else {
				json.setOk(false);
				json.setMessage("????????????????????????");
				return json;
			}
			String weightStr = request.getParameter("weight");
			if (!(weightStr == null || "".equals(weightStr))) {
				// ???????????????????????????,????????????????????????????????????????????????
				// ?????????????????????????????????weight????????????????????????????????????
				if (weightStr.equals(orGoods.getFinalWeight())) {
					cgp.setReviseWeight("0");
					cgp.setFeeprice("0");
				} else {
					// ?????????????????????????????????reviseWeight????????????????????????????????????
					if (weightStr.equals(orGoods.getReviseWeight())) {
						cgp.setReviseWeight("0");
						cgp.setFeeprice("0");
					} else {
						cgp.setReviseWeight(weightStr);
						// ????????????????????????????????????E??????????????????????????????0.08*??????+9???/6.75
						DecimalFormat df = new DecimalFormat("######0.00");
						// ??????????????????????????????????????????????????????????????????1kg
						double weight = Double.valueOf(weightStr) < 0.000001 ? 1.00 : Double.valueOf(weightStr);
						double cFreight = (0.08 * weight * 1000 + 9) / Util.EXCHANGE_RATE;
						cgp.setFeeprice(df.format(cFreight));
					}
				}
			} else {
				json.setOk(false);
				json.setMessage("????????????????????????");
				return json;
			}
			String imgInfo = request.getParameter("imgInfo");
			if (!(imgInfo == null || "".equals(imgInfo))) {
				// ????????????????????????????????????
				cgp.setImg("[" + imgInfo.replace(";", ",").replace(remotepath, "") + "]");
			} else {
				json.setOk(false);
				json.setMessage("?????????????????????");
				return json;
			}
			String endetailStr = request.getParameter("endetail");
			if (!(endetailStr == null || "".equals(endetailStr))) {
				// ???????????????????????????????????????
				cgp.setEndetail("[" + endetailStr.replaceAll(";", ", ") + "]");
			} else {
				// ?????????????????????
				cgp.setEndetail("[]");
			}
			if (!(contentStr == null || "".equals(contentStr))) {
				// ????????????
				String eninfo = contentStr.replaceAll(remotepath, "");
				cgp.setEninfo(eninfo);
			} else {
				json.setOk(false);
				json.setMessage("????????????????????????");
				return json;
			}

			String rangePrice = request.getParameter("rangePrice");

			if (rangePrice == null || "".equals(rangePrice)) {
				String wprice = request.getParameter("wprice");
				if (wprice == null || "".equals(wprice)) {
					// ??????wprice???????????????????????????????????????wprice???price???
					if (orGoods.getWprice() == null || "".equals(orGoods.getWprice())
							|| orGoods.getWprice().trim().length() < 3) {
						cgp.setPrice(orGoods.getPrice());
						cgp.setWprice("[]");
					} else {
						json.setOk(false);
						json.setMessage("??????????????????????????????");
						return json;
					}
				} else {

					String[] priceLst = wprice.split(",");
					double price = Double.valueOf(priceLst[0].split("@")[1]);
					for (String priceStr : priceLst) {
						double tempPrice = Double.valueOf(priceStr.split("@")[1]);
						if (tempPrice < price) {
							price = tempPrice;
						}
					}
					DecimalFormat df = new DecimalFormat("######0.00");
					cgp.setPrice(df.format(price));
					cgp.setWprice("[" + wprice.replace("@", " $ ") + "]");
				}
			} else {
				String sku = request.getParameter("sku");
				if (sku == null || "".equals(sku)) {
					json.setOk(false);
					json.setMessage("??????????????????????????????");
					return json;
				} else {
					JSONArray sku_json = JSONArray.fromObject(orGoods.getSku());
					List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
					boolean isSuccess = dealSkuByParam(skuList, sku, cgp);
					if (!isSuccess) {
						json.setOk(false);
						json.setMessage("??????????????????????????????????????????????????????");
						return json;
					}
				}
			}
			boolean success = shopUrlService.saveEditGoods(cgp, shopId, user.getId());
			if (success) {
				if (weightStr.equals(orFinalWeight)) {
					json.setOk(true);
					json.setMessage("????????????");
				} else {
					String url = SHOPGOODSWEIGHTCLEARURL + "pid=" + cgp.getPid() + "&finalWeight=" + weightStr
							+ "&sourceTable=shop_goods_ready&database=28";
					String resultJson = DownloadMain.getContentClient(url, null);
					System.err.println("pid=" + cgp.getPid() + ",result:[" + resultJson + "]");
					JSONObject jsonJt = JSONObject.fromObject(resultJson);
					System.out.println(json.toString());
					if (!jsonJt.getBoolean("ok")) {
						json.setOk(false);
						json.setMessage("?????????????????????" + jsonJt.getString("message"));
					} else {
						json.setOk(true);
						json.setMessage("????????????");
					}
				}
			} else {
				json.setOk(false);
				json.setMessage("????????????????????????");
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("shopId:" + shopId + ",pid:" + pidStr + ",????????????????????????" + e.getMessage());
			LOG.error("shopId:" + shopId + ",pid:" + pidStr + ",????????????????????????" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/publishEditGoods")
	@ResponseBody
	public JsonResult publishEditGoods(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String sourceTbl = request.getParameter("sourceTbl");
		String saveTbl = request.getParameter("saveTbl");
		try {

			
			// ??????????????????????????????????????????????????????
			Thread thread = new Thread(new CoreGoodsSyncThread(sourceTbl,saveTbl));
			thread.start();
			json.setOk(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("tbl:" + sourceTbl + ",????????????????????????" + e.getMessage());
			LOG.error("tbl:" + sourceTbl + ",????????????????????????" + e.getMessage());
		}
		return json;
	}
	
	
	@RequestMapping(value = "/publishCheckEditGoods")
	@ResponseBody
	public JsonResult publishCheckEditGoods(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			json.setOk(false);
			json.setMessage("??????shopId??????");
			return json;
		}
		try {

			Map<String, Integer> rsMap = shopUrlService.queryDealState(shopId);
			if (rsMap == null || rsMap.size() == 0) {
				json.setOk(false);
				json.setMessage("??????????????????????????????????????????");
			} else {
				if (rsMap.get("shop_state") > 0) {
					if (rsMap.get("shop_state") == 1) {
						json.setOk(false);
						json.setMessage("????????????????????????????????????");
						return json;
					} else if (rsMap.get("shop_state") == 2) {
						if (rsMap.get("online_state") == 1) {
							json.setOk(false);
							json.setMessage("????????????????????????????????????????????????");
							return json;
						} else if (rsMap.get("online_state") == 2) {
							json.setOk(false);
							json.setMessage("????????????????????????????????????????????????");
							return json;
						}
					}
				}
			} 
			// ??????????????????????????????????????????????????????
			Thread thread = new Thread(new ShopGoodsSyncThread(shopId));
			thread.start();

			json.setOk(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("shopId:" + shopId + ",????????????????????????" + e.getMessage());
			LOG.error("shopId:" + shopId + ",????????????????????????" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/readyToOnline")
	@ResponseBody
	public JsonResult readyToOnline(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			json.setOk(false);
			json.setMessage("??????shopId??????");
			return json;
		}
		try {

			Map<String, Integer> rsMap = shopUrlService.queryDealState(shopId);
			if (rsMap == null || rsMap.size() == 0) {
				json.setOk(false);
				json.setMessage("??????????????????????????????????????????");
			} else {
				if (rsMap.get("shop_state") > 0) {
					if (rsMap.get("shop_state") == 1) {
						json.setOk(false);
						json.setMessage("????????????????????????????????????");
						return json;
					} else if (rsMap.get("shop_state") == 2) {
						if (rsMap.get("online_state") == 1) {
							json.setOk(false);
							json.setMessage("????????????????????????????????????????????????");
							return json;
						} else if (rsMap.get("online_state") == 2) {
							json.setOk(false);
							json.setMessage("????????????????????????????????????????????????");
							return json;
						}
					}
				}
			}
			shopUrlService.updateShopState(shopId, 4);
			json.setOk(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("shopId:" + shopId + ",????????????????????????" + e.getMessage());
			LOG.error("shopId:" + shopId + ",????????????????????????" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/showCatidErrorWeightGoods")
	@ResponseBody
	public ModelAndView showCatidErrorWeightGoods(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mv = new ModelAndView("goodsWeightErrpor");
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			mv.addObject("msgStr", "???????????????");
			mv.addObject("show", 0);
			return mv;
		}

		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			mv.addObject("msgStr", "??????shopId??????");
			mv.addObject("show", 0);
			return mv;
		} else {
			mv.addObject("shopId", shopId);
		}
		try {
			List<GoodsOfferBean> goodsInfos = shopUrlService.queryOriginalGoodsInfo(shopId);

			// ?????????????????????
			Collections.sort(goodsInfos, new Comparator<GoodsOfferBean>() {
				public int compare(GoodsOfferBean o1, GoodsOfferBean o2) {
					return o1.getWeightFlag() - o2.getWeightFlag();
				}
			});
			
			List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);
			List<ShopInfoBean> infos = shopUrlService.queryInfoByShopId(shopId, "");

			// ????????????
			Map<String, List<GoodsOfferBean>> resultMapError = new HashMap<String, List<GoodsOfferBean>>();
			// ????????????
			Map<String, List<GoodsOfferBean>> resultMapNomal = new HashMap<String, List<GoodsOfferBean>>();
			int errorTotal = 0;
			int nomalTotal = 0;
			for (GoodsOfferBean gdOf : goodsInfos) {
				BigDecimal b = new BigDecimal(gdOf.getPrice());
				gdOf.setPrice(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				if (gdOf.getWeightFlag() > 0) {
					errorTotal++;
					if(gdOf.getSetWeight() == 0){
						//????????? ???????????????????????? ??? ??? ???????????????????????????
						//????????????????????????????????????????????? ?????? ????????????????????????
						checkCatidAvgWeright(ctwtList,infos,gdOf);
					}else{
						BigDecimal bWe = new BigDecimal(gdOf.getSetWeight());
						gdOf.setSetWeight(bWe.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					}					

					if (resultMapError.containsKey(gdOf.getCatid())) {
						resultMapError.get(gdOf.getCatid()).add(gdOf);
					} else {
						List<GoodsOfferBean> gdOfLs = new ArrayList<GoodsOfferBean>();
						gdOfLs.add(gdOf);
						resultMapError.put(gdOf.getCatid(), gdOfLs);
					}
				} else {
					nomalTotal++;
					if (resultMapNomal.containsKey(gdOf.getCatid())) {
						resultMapNomal.get(gdOf.getCatid()).add(gdOf);
					} else {
						List<GoodsOfferBean> gdOfLs = new ArrayList<GoodsOfferBean>();
						gdOfLs.add(gdOf);
						resultMapNomal.put(gdOf.getCatid(), gdOfLs);
					}
				}
			}

			List<ShopErrorGoodsInfo> errorList = new ArrayList<ShopErrorGoodsInfo>();
			List<ShopErrorGoodsInfo> nomalList = new ArrayList<ShopErrorGoodsInfo>();

			// ??????????????????
			List<ShopInfoBean> shopInfos = shopUrlService.queryInfoByShopId(shopId, "");
			for (Entry<String, List<GoodsOfferBean>> errMap : resultMapError.entrySet()) {
				ShopErrorGoodsInfo shErGd = new ShopErrorGoodsInfo();
				shErGd.setCategoryId(errMap.getKey());
				shErGd.setCategoryName(genCategoryName(errMap.getKey()));
				shErGd.setWeightVal(queryWeightVal(shopInfos, errMap.getKey()));
				shErGd.setGdOfLs(errMap.getValue());
				shErGd.setTotalNum(errMap.getValue().size());
				errorList.add(shErGd);
			}

			for (Entry<String, List<GoodsOfferBean>> nomMap : resultMapNomal.entrySet()) {
				ShopErrorGoodsInfo shErGd = new ShopErrorGoodsInfo();
				shErGd.setCategoryId(nomMap.getKey());
				shErGd.setCategoryName(genCategoryName(nomMap.getKey()));
				shErGd.setWeightVal(queryWeightVal(shopInfos, nomMap.getKey()));
				shErGd.setGdOfLs(nomMap.getValue());
				shErGd.setTotalNum(nomMap.getValue().size());
				nomalList.add(shErGd);
			}

			resultMapError.clear();
			resultMapNomal.clear();
			shopInfos.clear();
			goodsInfos.clear();

			mv.addObject("errorList", errorList);
			mv.addObject("nomalList", nomalList);
			mv.addObject("errorTotal", errorTotal);
			mv.addObject("nomalTotal", nomalTotal);
			mv.addObject("show", 1);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("msgStr", "??????????????????????????????????????????" + e.getMessage());
			mv.addObject("show", 0);
			LOG.error("shopId:" + shopId + ",??????????????????????????????????????????" + e.getMessage());
		}
		return mv;
	}
	
	private void checkCatidAvgWeright(List<ShopCatidWeight> ctwtList, List<ShopInfoBean> infos, GoodsOfferBean gdOf){
		
		boolean noKeyWeigth = true;
		for(ShopCatidWeight cdWt : ctwtList){
			if(cdWt.getCatid().equals(gdOf.getCatid())){
				if(gdOf.getGoodsName().contains(cdWt.getKeyword())){
					BigDecimal bWe = new BigDecimal(cdWt.getAvgWeight());
					gdOf.setSetWeight(bWe.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					noKeyWeigth = false;
					break;
				}
			}
		}
		if(noKeyWeigth){
			for(ShopInfoBean spIf : infos){
				if(spIf.getCategoryId().equals(gdOf.getCatid())){
					BigDecimal bWe = new BigDecimal(spIf.getWeightVal());
					gdOf.setSetWeight(bWe.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					break;
				}
			}
		}
	}

	@RequestMapping(value = "/saveKeyWordWeight")
	@ResponseBody
	public JsonResult saveKeyWordWeight(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}
		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			json.setOk(false);
			json.setMessage("????????????ID??????");
			return json;
		}
		String catid = request.getParameter("catid");
		if (catid == null || "".equals(catid)) {
			json.setOk(false);
			json.setMessage("????????????ID??????");
			return json;
		}
		String avgWeight = request.getParameter("avgWeight");
		if (avgWeight == null || "".equals(avgWeight)) {
			json.setOk(false);
			json.setMessage("????????????????????????");
			return json;
		}
		String keyword = request.getParameter("keyword");
		if (keyword == null || "".equals(keyword)) {
			json.setOk(false);
			json.setMessage("???????????????????????????");
			return json;
		}
		try {

			List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);

			List<ShopCatidWeight> insertCidWtList = new ArrayList<ShopCatidWeight>();
			List<ShopCatidWeight> updateCidWtList = new ArrayList<ShopCatidWeight>();

			ShopCatidWeight newCtwt = new ShopCatidWeight();
			newCtwt.setAvgWeight(Double.valueOf(avgWeight));
			newCtwt.setCatid(catid);
			newCtwt.setKeyword(keyword);
			newCtwt.setShopId(shopId);
			newCtwt.setAdminId(user.getId());
			boolean isInsert = true;
			for (ShopCatidWeight oldCtwt : ctwtList) {
				if (oldCtwt.getCatid().equals(catid) && oldCtwt.getKeyword().equals(keyword)) {
					newCtwt.setId(oldCtwt.getId());
					updateCidWtList.add(newCtwt);
					isInsert = false;
					break;
				}
			}
			if (isInsert) {
				insertCidWtList.add(newCtwt);
			}
			if (insertCidWtList.size() > 0) {
				shopUrlService.batchInsertCatidWeight(insertCidWtList);
			}
			if (updateCidWtList.size() > 0) {
				shopUrlService.batchUpdateCatidWeight(updateCidWtList);
			}

			json.setOk(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("shopId:" + shopId + ",????????????????????????" + e.getMessage());
			LOG.error("shopId:" + shopId + ",????????????????????????" + e.getMessage());
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveDealErrorShopGoods")
	@ResponseBody
	public JsonResult saveDealErrorShopGoods(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}
		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			json.setOk(false);
			json.setMessage("????????????ID??????");
			return json;
		}
		String infos = request.getParameter("infos");
		if (infos == null && "".equals(infos)) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}

		try {
			JSONArray jsonArray = JSONArray.fromObject(infos);// ???String?????????json
			List<GoodsOfferBean> goodsErrInfos = (List<GoodsOfferBean>) JSONArray.toCollection(jsonArray,
					GoodsOfferBean.class);
			if (goodsErrInfos == null || goodsErrInfos.size() == 0) {
				json.setOk(false);
				json.setMessage("?????????????????????????????????????????????");
				return json;
			} else {
				for (GoodsOfferBean gdOf : goodsErrInfos) {
					gdOf.setShopId(shopId);
				}
			}
			boolean isSuccess = shopUrlService.batchUpdateErrorWeightGoods(goodsErrInfos);
			if (isSuccess) {
				/**
				 * ???????????????????????????????????? A.?????? ????????? ?????????+???????????????????????? ?????? 30%????????????????????????????????? ????????????
				 * ??????????????????????????????????????? B.????????? ????????????????????????????????????????????????????????????
				 */

				// ?????????????????????????????????
				List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);
				// ?????????????????????????????????map??????
				Map<String, ShopCatidWeight> ctwtMap = new HashMap<String, ShopCatidWeight>();
				for (ShopCatidWeight ctWg : ctwtList) {
					ctwtMap.put(ctWg.getCatid(), ctWg);
				}
				List<GoodsOfferBean> orGoodsInfos = shopUrlService.queryOriginalGoodsInfo(shopId);
				List<GoodsOfferBean> resultInfos = new ArrayList<GoodsOfferBean>();
				List<GoodsOfferBean> updateGoodsInfos = new ArrayList<GoodsOfferBean>();
				for (GoodsOfferBean orGd : orGoodsInfos) {
					// ???????????? 0????????? 1?????????
					if (orGd.getWeightFlag() <= 2) {
						// orGd.setWeightDeal(1);
						// updateGoodsInfos.add(orGd);
					} else {
						// ?????? ????????? ?????????+???????????????????????? ?????? 30%????????????????????????
						if (ctwtMap.size() == 0) {
							resultInfos.add(orGd);
						} else {
							boolean isSuit = ctwtMap.containsKey(orGd.getCatid());
							isSuit = isSuit && orGd.getGoodsName().contains(ctwtMap.get(orGd.getCatid()).getKeyword());
							isSuit = isSuit && Math.abs(ctwtMap.get(orGd.getCatid()).getAvgWeight() - orGd.getWeight())
									/ ctwtMap.get(orGd.getCatid()).getAvgWeight() <= 0.3;
							if (isSuit) {
								orGd.setWeightFlag(2);
								// orGd.setWeightDeal(1);
								updateGoodsInfos.add(orGd);
							} else {
								resultInfos.add(orGd);
							}
						}
					}
				}
				// ??????????????? ?????????+???????????????????????? ?????? 30%????????????????????????????????????
				if (updateGoodsInfos.size() > 0) {
					boolean isUpdate = shopUrlService.batchUpdateErrorWeightGoods(updateGoodsInfos);
					if (isUpdate) {
						json.setOk(true);
					} else {
						json.setOk(false);
						json.setMessage("??????????????????????????????????????????");
						return json;
					}
					updateGoodsInfos.clear();
				}

				goodsErrInfos.clear();
				orGoodsInfos.clear();
				resultInfos.clear();
			} else {
				json.setOk(false);
				json.setMessage("??????????????????????????????????????????");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("????????????????????????" + e.getMessage());
			LOG.error("shopId:" + shopId + ",??????????????????????????????????????????" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/showHasDealShopGoods")
	public ModelAndView showHasDealShopGoods(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mv = new ModelAndView("goodsWeightDealShow");
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			mv.addObject("msgStr", "???????????????");
			mv.addObject("show", 0);
			return mv;
		}

		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			mv.addObject("msgStr", "??????shopId??????");
			mv.addObject("show", 0);
			return mv;
		} else {
			mv.addObject("shopId", shopId);
		}

		try {

			/**
			 * ???????????????????????????????????? A.?????? ????????? ?????????+???????????????????????? ?????? 30%????????????????????????????????? ????????????
			 * ??????????????????????????????????????? B.????????? ????????????????????????????????????????????????????????????
			 */

			// ?????????????????????????????????
			List<ShopCatidWeight> ctwtList = shopUrlService.queryShopCatidWeightListByShopId(shopId);
			// ?????????????????????????????????map??????
			Map<String, ShopCatidWeight> ctwtMap = new HashMap<String, ShopCatidWeight>();
			for (ShopCatidWeight ctWg : ctwtList) {
				ctwtMap.put(ctWg.getCatid(), ctWg);
			}
			List<GoodsOfferBean> orGoodsInfos = shopUrlService.queryOriginalGoodsInfo(shopId);
			List<GoodsOfferBean> resultInfos = new ArrayList<GoodsOfferBean>();
			List<GoodsOfferBean> updateGoodsInfos = new ArrayList<GoodsOfferBean>();
			for (GoodsOfferBean orGd : orGoodsInfos) {
				// ???????????? 0????????? 1?????????
				if (orGd.getWeightFlag() <= 2) {
					orGd.setWeightDeal(1);
					// updateGoodsInfos.add(orGd);
				} else {
					// ?????? ????????? ?????????+???????????????????????? ?????? 30%????????????????????????
					if (ctwtMap.size() == 0) {
						resultInfos.add(orGd);
					} else {
						// A.?????? ????????? ?????????+???????????????????????? ?????? 30%????????????????????????????????? ????????????
						boolean isSuit = ctwtMap.containsKey(orGd.getCatid());
						isSuit = isSuit && orGd.getGoodsName().contains(ctwtMap.get(orGd.getCatid()).getKeyword());
						isSuit = isSuit && Math.abs(ctwtMap.get(orGd.getCatid()).getAvgWeight() - orGd.getWeight())
								/ ctwtMap.get(orGd.getCatid()).getAvgWeight() <= 0.3;
						if (isSuit) {
							orGd.setWeightFlag(2);
							// orGd.setWeightDeal(1);
							updateGoodsInfos.add(orGd);
						} else {
							resultInfos.add(orGd);
						}
					}
				}
			}
			// ??????????????? ?????????+???????????????????????? ?????? 30%????????????????????????????????????
			if (updateGoodsInfos.size() > 0) {
				shopUrlService.batchUpdateErrorWeightGoods(updateGoodsInfos);
				updateGoodsInfos.clear();
			}
			orGoodsInfos.clear();

			// ????????????
			Map<String, List<GoodsOfferBean>> resultMapError = new HashMap<String, List<GoodsOfferBean>>();
			int errorTotal = 0;
			for (GoodsOfferBean gdOf : resultInfos) {
				BigDecimal b = new BigDecimal(gdOf.getPrice());
				gdOf.setPrice(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				errorTotal++;
				if (resultMapError.containsKey(gdOf.getCatid())) {
					resultMapError.get(gdOf.getCatid()).add(gdOf);
				} else {
					List<GoodsOfferBean> gdOfLs = new ArrayList<GoodsOfferBean>();
					gdOfLs.add(gdOf);
					resultMapError.put(gdOf.getCatid(), gdOfLs);
				}
			}

			List<ShopErrorGoodsInfo> errorList = new ArrayList<ShopErrorGoodsInfo>();

			// ??????????????????
			List<ShopInfoBean> shopInfos = shopUrlService.queryInfoByShopId(shopId, "");
			for (Entry<String, List<GoodsOfferBean>> errMap : resultMapError.entrySet()) {
				ShopErrorGoodsInfo shErGd = new ShopErrorGoodsInfo();
				shErGd.setCategoryId(errMap.getKey());
				shErGd.setCategoryName(genCategoryName(errMap.getKey()));
				shErGd.setWeightVal(queryWeightVal(shopInfos, errMap.getKey()));
				shErGd.setGdOfLs(errMap.getValue());
				shErGd.setTotalNum(errMap.getValue().size());
				errorList.add(shErGd);
			}

			resultInfos.clear();
			resultMapError.clear();
			shopInfos.clear();

			mv.addObject("errorList", errorList);
			mv.addObject("errorTotal", errorTotal);
			mv.addObject("show", 1);

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("msgStr", "????????????????????????" + e.getMessage());
			mv.addObject("show", 0);
			LOG.error("shopId:" + shopId + ",??????????????????????????????????????????" + e.getMessage());
		}
		return mv;
	}

	@RequestMapping(value = "/checkShowDealShopGoodsAndClear")
	@ResponseBody
	public JsonResult checkShowDealShopGoodsAndClear(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String userJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setOk(false);
			json.setMessage("??????????????????");
			return json;
		}
		String shopId = request.getParameter("shopId");
		if (shopId == null || "".equals(shopId)) {
			json.setOk(false);
			json.setMessage("????????????ID??????");
			return json;
		}

		try {

			//????????????????????????????????????0????????????????????????????????????????????????
			List<ShopInfoBean> catidInfos = shopUrlService.queryInfoByShopId(shopId, "");
			boolean isZone = false;
			String catid = "" ;
			for(ShopInfoBean  catInfo : catidInfos){
				if(catInfo.getWeightVal() == 0){
					isZone = true;
					catid = catInfo.getCategoryId();
					break;
				}
			}
			if(isZone){
				json.setOk(false);
				json.setMessage("?????????" + catid +  ",???????????????????????????????????????????????????");
				return json;
			}
			
			
			List<GoodsOfferBean> orGoodsInfos = shopUrlService.queryOriginalGoodsInfo(shopId);
			List<GoodsOfferBean> updateGoodsInfos = new ArrayList<GoodsOfferBean>();
			for (GoodsOfferBean orGd : orGoodsInfos) {
				orGd.setWeightDeal(1);
				updateGoodsInfos.add(orGd);
			}
			// ??????????????? ?????????+???????????????????????? ?????? 30%????????????????????????????????????
			if (updateGoodsInfos.size() > 0) {
				boolean isUpdate = shopUrlService.batchUpdateErrorWeightGoods(updateGoodsInfos);
				if (isUpdate) {
					json.setOk(true);
				} else {
					json.setOk(false);
					json.setMessage("??????????????????????????????????????????");
					return json;
				}
				updateGoodsInfos.clear();
			}
			orGoodsInfos.clear();

			Thread thread = new Thread(new ShopGoodsDealThread(shopId));
			thread.start();

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("????????????????????????" + e.getMessage());
			LOG.error("shopId:" + shopId + ",??????????????????????????????????????????" + e.getMessage());
		}
		return json;
	}

	private void checkFtpConfig(FtpConfig ftpConfig, JsonResult json) {
		json.setOk(true);
		// ???????????????????????????????????????
		if (ftpConfig == null || !ftpConfig.isOk()) {
			json.setOk(false);
			json.setMessage("????????????????????????");
		} else {
			if (StringUtil.isBlank(ftpConfig.getFtpURL())) {
				json.setOk(false);
				json.setMessage("??????ftpURL??????");
			} else if (StringUtil.isBlank(ftpConfig.getFtpPort())) {
				json.setOk(false);
				json.setMessage("??????ftpPort??????");
			} else if (StringUtil.isBlank(ftpConfig.getFtpUserName())) {
				json.setOk(false);
				json.setMessage("??????ftpUserName??????");
			} else if (StringUtil.isBlank(ftpConfig.getFtpPassword())) {
				json.setOk(false);
				json.setMessage("??????ftpPassword??????");
			} else if (StringUtil.isBlank(ftpConfig.getRemoteShowPath())) {
				json.setOk(false);
				json.setMessage("??????remoteShowPath??????");
			} else if (StringUtil.isBlank(ftpConfig.getLocalDiskPath())) {
				json.setOk(false);
				json.setMessage("??????localDiskPath??????");
			} else if (StringUtil.isBlank(ftpConfig.getLocalShowPath())) {
				json.setOk(false);
				json.setMessage("??????localShowPath??????");
			}
		}
	}

	// ??????sku?????????????????????????????????????????????????????????
	private boolean dealSkuByParam(List<ImportExSku> skuList, String sku, CustomGoodsPublish cgp) {
		List<ImportExSku> newSkuList = new ArrayList<ImportExSku>();

		float minPrice = 0;
		float maxPrice = 0;
		int count = 1;
		String[] skuSplits = sku.split(";");
		for (String skuIds : skuSplits) {
			String[] idAndPrice = skuIds.split("@");
			String ppids = idAndPrice[0].replace("_", ",");
			for (ImportExSku ies : skuList) {
				if (ppids.equals(ies.getSkuPropIds())) {
					float tempPrice = Float.valueOf(idAndPrice[1]);
					if (count == 1) {
						minPrice = tempPrice;
						maxPrice = tempPrice;
						count++;
					}
					if (minPrice > tempPrice) {
						minPrice = tempPrice;
					}
					if (maxPrice < tempPrice) {
						maxPrice = tempPrice;
					}
					ies.getSkuVal().setActSkuCalPrice(tempPrice);
					ies.getSkuVal().setActSkuMultiCurrencyCalPrice(tempPrice);
					ies.getSkuVal().setActSkuMultiCurrencyDisplayPrice(tempPrice);
					ies.getSkuVal().setSkuCalPrice(tempPrice);
					ies.getSkuVal().setSkuMultiCurrencyCalPrice(tempPrice);
					ies.getSkuVal().setSkuMultiCurrencyDisplayPrice(tempPrice);
					newSkuList.add(ies);
					break;
				}
			}
			ppids = null;
		}
		cgp.setRangePrice(genFloatWidthTwoDecimalPlaces(minPrice) + "-" + genFloatWidthTwoDecimalPlaces(maxPrice));
		cgp.setSku(newSkuList.toString());
		if (minPrice == 0 || maxPrice == 0 || newSkuList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	private String makeFileName(String filename) { // 2.jpg
		// ???????????????????????????????????????????????????????????????????????????????????????
		return UUID.randomUUID().toString() + "_" + filename;
	}

	private float genFloatWidthTwoDecimalPlaces(float numVal) {
		BigDecimal bd = new BigDecimal(numVal);
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	private float queryWeightVal(List<ShopInfoBean> shopInfos, String catid) {
		float weight = 0;
		for (ShopInfoBean spIf : shopInfos) {
			if (spIf.getCategoryId().equals(catid)) {
				weight = spIf.getWeightVal();
				break;
			}
		}
		return weight;
	}

	private String genCategoryName(String categoryId) {
		String categoryName = "";
		if (category1688List == null || category1688List.size() == 0) {
			category1688List = shopUrlService.queryAll1688Category();
		}
		for (Category1688Bean ct1688 : category1688List) {
			if (ct1688.getCategoryId().equals(categoryId)) {
				categoryName = ct1688.getCategoryName();
				break;
			}
		}
		return categoryName;
	}

}