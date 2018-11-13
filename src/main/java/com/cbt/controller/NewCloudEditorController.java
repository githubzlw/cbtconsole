package com.cbt.controller;

import com.alibaba.fastjson.JSON;
import com.cbt.bean.*;
import com.cbt.parse.bean.Set;
import com.cbt.parse.service.DownloadMain;
import com.cbt.parse.service.GoodsBean;
import com.cbt.parse.service.ImgDownload;
import com.cbt.parse.service.StrUtils;
import com.cbt.service.NewCloudGoodsService;
import com.cbt.util.*;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.bean.ShopManagerPojo;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

@Controller
@RequestMapping(value = "/newcloudEdit")
public class NewCloudEditorController {
	private static final Log LOG = LogFactory.getLog(NewCloudEditorController.class);
	private String rootPath = "F:/console/tomcatImportCsv/webapps/";
	private String localIP = "http://27.115.38.42:8083/";
	private String wanlIP = "http://192.168.1.27:8083/";
	private DecimalFormat format = new DecimalFormat("#0.00");
	private String chineseChar = "([\\一-\\龥]+)";
	private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();

	@Autowired
	private NewCloudGoodsService newCloudGoodsService;

	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/detalisEdit", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView detalisEdit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mv = new ModelAndView("newcloudGoodsDetalis");

		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			mv.addObject("uid", 0);
			return mv;
		} else {
			mv.addObject("uid", user.getId());
			mv.addObject("roletype", user.getRoletype());
		}
		// 获取需要编辑的内容
		String pid = request.getParameter("pid");
		if (pid == null || pid.isEmpty()) {
			return mv;
		}

		// 取出1688商品的全部信息
		CustomGoodsPublish goods = newCloudGoodsService.queryGoodsDetails(pid, 0);

		if (goods == null) {
			mv.addObject("uid", -1);
			return mv;
		}

		// 根据shopid查询店铺数据
		int queryId = 0;
		if (!(goods.getShopId() == null || "".equals(goods.getShopId()))) {
			ShopManagerPojo spmg = newCloudGoodsService.queryByShopId(goods.getShopId());
			if (spmg != null) {
				queryId = spmg.getId();
			}
		}

		mv.addObject("shopId", queryId);

		// 取出主图筛选数量
		GoodsPictureQuantity pictureQt = newCloudGoodsService.queryPictureQuantityByPid(pid);
		pictureQt.setImgDeletedSize(pictureQt.getTypeOriginalSize() + pictureQt.getImgOriginalSize()
				- pictureQt.getImgSize() - pictureQt.getTypeSize());
		// pictureQt.setTypeDeletedSize(pictureQt.getTypeOriginalSize()-pictureQt.getTypeSize());
		pictureQt.setInfoDeletedSize(pictureQt.getInfoOriginalSize() - pictureQt.getInfoSize());
		request.setAttribute("pictureQt", pictureQt);

		// 取出1688原货源链接

		// 将goods的entype属性值取出来,即规格图
		List<TypeBean> typeList = deal1688GoodsType(goods);

		// 将goods的img属性值取出来,即橱窗图
		request.setAttribute("showimgs", JSONArray.fromObject("[]"));
		List<String> imgs = deal1688GoodsImg(goods, goods.getRemotpath());
		if (imgs.size() > 0) {
			request.setAttribute("showimgs", JSONArray.fromObject(imgs));
		}

		HashMap<String, String> pInfo = deal1688Sku(goods);
		request.setAttribute("showattribute", pInfo);

		// 处理Sku数据
		// 获取sku数据进行处理
		if ("".equals(goods.getSku()) || goods.getSku() == null
				|| "".equals(goods.getSku())) {
			// request.setAttribute("showSku", JSONArray.fromObject("[]"));
		} else {
			List<ImportExSku> skuList = new ArrayList<ImportExSku>();
			JSONArray sku_json = JSONArray.fromObject(goods.getSku());
			skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
			// 规格标题名称集合
			List<ImportExSkuShow> cbSkus = combineSkuList(typeList, skuList);
			// 集合排序
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

		// 直接使用远程路径
		String localpath = goods.getRemotpath();
		// 设置默认图的路径
		if (!(goods.getShowMainImage().indexOf("http://") > -1 || goods.getShowMainImage().indexOf("https://") > -1)) {
			goods.setShowMainImage(localpath + goods.getShowMainImage());
		}
		// 分割eninfo数据，不替换remotepath相同的路径
		String enInfo = goods.getEninfo().replaceAll("<br><img", "<img").replaceAll("<br /><img", "<img");
		// 使用img标签进行分割
		String[] enInfoLst = enInfo.split("<img");
		StringBuffer textBf = new StringBuffer();
		for (String srcStr : enInfoLst) {
			// 判断是否含有全路径的图片
			if (srcStr.indexOf("http:") > -1 || srcStr.indexOf("https:") > -1) {
				// 是否存在img标签的判断，使用img含有src的判断
				if (srcStr.indexOf("src=") > -1) {
					textBf.append("<br><img " + srcStr);
				} else {
					textBf.append(srcStr);
				}
			} else {
				// 是否存在img标签的判断，使用img含有src的判断
				if (srcStr.indexOf("src=") > -1) {
					textBf.append("<br><img " + srcStr.replaceAll("src=\"", "src=\"" + localpath));
				} else {
					textBf.append(srcStr);
				}
			}
		}
		// 使用完成后清理数据
		enInfoLst = null;

		// 判断是否是人为修改的重量，如果是则显示修改的重量，否则显示默认的重量
		if (goods.getReviseWeight() == null || "".equals(goods.getReviseWeight())) {
			goods.setReviseWeight(goods.getFinalWeight());
		}

		String text = textBf.toString();

		// 当前抓取aliexpress的商品数据
		/*boolean isLocal = true;
		if (!(goods.getAliGoodsPid() == null || "".equals(goods.getAliGoodsPid()))) {
			GoodsBean algood = null;
			System.err.println("url:" + goods.getAliGoodsUrl());
			if (isLocal) {
				// 本地直接获取ali商品信息
				algood = ParseGoodsUrl.parseGoodsw(goods.getAliGoodsUrl(), 3);
				String resultJson = DownloadMain.getContentClient(ContentConfig.CRAWL_ALI_URL + goods.getAliGoodsUrl(),
						null);
				JSONObject goodsObj = new JSONObject().fromObject(resultJson);
				algood = (GoodsBean) JSONObject.toBean(goodsObj, GoodsBean.class);
			}

			if (algood == null || algood.getValid() == 0) {
				goods.setAliGoodsName("get aliexpress goodsinfo failure this is a test goods");
				goods.setAliGoodsImgUrl(
						"https://ae01.alicdn.com/kf/HTB1AYzjSpXXXXbHXpXXq6xXFXXXx/960P-1-3MP-HD-Wireless-IP-Camera-wi-fi-Robot-camera-Wifi-Night-Vision-Camera-IP.jpg");
			} else {
				goods.setAliGoodsName(algood.getpName());

				if (algood.getImgSize().length >= 2) {
					goods.setAliGoodsImgUrl(algood.getpImage().get(0) + algood.getImgSize()[1]);
				} else {
					goods.setAliGoodsImgUrl(algood.getpImage().get(0) + algood.getImgSize()[0]);
				}
				// 获取ali商品的详情文字，并去掉文字中敏感词的数据

				getTextByHtml(goods, algood, isLocal);
			}
		}*/
		// 返回待编辑数据到编辑页面
		mv.addObject("text", text);
		mv.addObject("pid", pid);
		goods.setWeight(StrUtils.matchStr(goods.getWeight(), "(\\d+\\.*\\d*)"));
		mv.addObject("goods", goods);
		// 上传图片保存路径----酌情配置
		String savePath = localpath.replace(localIP, rootPath);
		if (IpCheckUtil.checkIsIntranet(request)) {
			savePath = localpath.replace(wanlIP, rootPath);
		}
		mv.addObject("savePath", savePath);
		mv.addObject("localpath", localpath);

		return mv;
	}

	private void getTextByHtml(CustomGoodsPublish goods, GoodsBean algood, boolean isLocal) {
		// 敏感词库
		List<String> sensitiveWords = new ArrayList<String>();
		sensitiveWords.add("Aliexpress:");
		sensitiveWords.add("aliexpress:");
		sensitiveWords.add("alibaba:");
		sensitiveWords.add("Alibaba:");
		sensitiveWords.add("QQ:");
		sensitiveWords.add("qq:");
		sensitiveWords.add("微信:");
		sensitiveWords.add("weixin:");
		sensitiveWords.add("WeiXin:");
		sensitiveWords.add("Logistics:");
		sensitiveWords.add("logistics:");
		sensitiveWords.add("Ships From:");
		sensitiveWords.add("ships from:");

		// 判断商品是否编辑过，是否有infourl链接，没有编辑和有则infourl链接的则进行信息获取
		if (!(algood.getInfourl() == null || "".equals(algood.getInfourl().trim()))) {
			String page = "";
			if (isLocal) {
				// 本地解析
				Set set = new Set();
				page = DownloadMain.getJsoup("https:" + algood.getInfourl(), 1, set);
			} else {
				// 远程解析
				page = DownloadMain.getContentClient(ContentConfig.CRAWL_ALI_INFO_URL + algood.getInfourl(), null);
			}
			if (page == null || page.isEmpty() || "".equals(page.trim()) || "httperror".equals(page.trim())) {
				System.err.println(algood.getInfourl() + ":获取失败");
			} else {
				goods.setAliGoodsInfo(dealAliInfoData(page.replaceAll("window.productDescription=\'", "")));
			}
			page = null;
		}
	}

	// 处理1688商品的规格图片数据
	private List<TypeBean> deal1688GoodsType(CustomGoodsPublish cgbean) {// 规格
		List<TypeBean> typeList = new ArrayList<TypeBean>();
		if (!(cgbean.getEntype() == null || "".equals(cgbean.getEntype()))) {
			Map<String, List<TypeBean>> typeMap = new HashMap<String, List<TypeBean>>();
			String types = cgbean.getEntype();
			String remotPath = cgbean.getRemotpath();
			// String localPath = cgbean.getLocalpath();
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
							tem = StringUtils.isBlank(tem) || StringUtils.equals(tem, "null") ? "" : remotPath + tem;
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

	// 处理1688商品的规格图片数据
	private List<String> deal1688GoodsImg(CustomGoodsPublish cgbean, String remotPath) {

		List<String> imgList = new ArrayList<String>();
		// 图片
		String img = cgbean.getImg();
		if (StringUtils.isNotBlank(img)) {
			img = img.replace("[", "").replace("]", "").trim();
			String[] imgs = img.split(",\\s*");

			for (int i = 0; i < imgs.length; i++) {
				if (!imgs[i].isEmpty()) {
					// imgList.add(remotPath + imgs[i].replace(".60x60.jpg",
					// ""));
					// 统一路径，下面代码屏蔽
					if (imgs[i].indexOf("http://") > -1 || imgs[i].indexOf("https://") > -1) {
						imgList.add(imgs[i]);
					} else {
						imgList.add(remotPath + imgs[i]);
					}
				}
			}
			cgbean.setShowImages(imgList);
		}
		return imgList;
	}

	private HashMap<String, String> deal1688Sku(CustomGoodsPublish cgbean) {
		// detail明细
		HashMap<String, String> pInfo = new HashMap<String, String>();
		String detail = cgbean.getEndetail() == null ? "" : cgbean.getEndetail();
		if (StringUtils.isNotBlank(detail)) {
			String[] details = detail.substring(1, detail.length() - 1).split(",");
			int details_length = details.length;
			for (int i = 0; i < details_length; i++) {
				String str_detail = details[i].trim().replaceAll(chineseChar, "");
				if (str_detail.isEmpty() || StrUtils.isMatch(str_detail.substring(0, 1), "\\d+")) {
					continue;
				}
				if (StrUtils.isFind(str_detail, "(brand\\:)")) {
					continue;
				}
				if (str_detail.length() < 6) {
					continue;
				}
				pInfo.put(i + "",
						str_detail.substring(0, 1).toUpperCase() + str_detail.substring(1, str_detail.length()));
			}
		}
		return pInfo;
	}

	/**
	 * 组合生成展示的Sku信息
	 * 
	 * @param typeList
	 * @param skuList
	 * @return
	 */
	private List<ImportExSkuShow> combineSkuList(List<TypeBean> typeList, List<ImportExSku> skuList) {

		List<ImportExSkuShow> cbSkuLst = new ArrayList<ImportExSkuShow>();

		for (ImportExSku ites : skuList) {
			String skuAttrs = "";
			ImportExSkuShow ipes = new ImportExSkuShow();
			// PropIds分组循环
			String[] ppidLst = ites.getSkuPropIds().split(",");
			int totalCount = 0;
			int arrLength = ppidLst.length;
			for (String ppid : ppidLst) {
				// 比较type类别的数据，获取类别信息
				for (TypeBean tyb : typeList) {
					if (ppid.equals(tyb.getId())) {
						skuAttrs += ";" + tyb.getId() + "@" + tyb.getType() + "@" + tyb.getValue();
						totalCount++;
						break;
					}
				}
			}
			ppidLst = null;
			// 解析attr数据，获取类别名称对应的ID
			String[] skuAtLst = ites.getSkuAttr().split(";");
			for (String ska : skuAtLst) {
				String[] cbLst = ska.split(":");
				if (cbLst.length == 2) {
					for (TypeBean tyb : typeList) {
						if (cbLst[1].equals(tyb.getId())) {
							tyb.setTypeId(cbLst[0]);
							break;
						}
					}
				}
			}
			skuAtLst = null;
			ipes.setPpIds(ites.getSkuPropIds().replace(",", "_"));
			ipes.setPrice(ites.getSkuVal().getActSkuCalPrice());
			if (skuAttrs == null || "".equals(skuAttrs)) {
				ipes = null;
			} else {
				ipes.setSkuAttrs(skuAttrs.substring(1));
				// skuAttrs获取失败，则不显示sku数据，并且在更新后覆盖原数据
				// type多规格生成的数据中sku只有单规格的数据也剔除掉
				if (arrLength > 0 && arrLength == totalCount) {
					cbSkuLst.add(ipes);
				}

			}
			skuAttrs = null;
		}
		skuList = null;
		return cbSkuLst;
	}

	/**
	 * 处理阿里详情数据
	 * 
	 * @author jxw
	 * @date 2017-11-10
	 * @param content
	 * @return
	 */
	private String dealAliInfoData(String content) {

		Document nwDoc = Jsoup.parseBodyFragment(content);
		// 移除所有的页面效果 kse标签,实际div
		Elements divLst = nwDoc.getElementsByTag("div");
		for (Element dEl : divLst) {
			if (!(dEl.attr("name") == null || "".equals(dEl.attr("name").trim()))) {
				if ("productItem".equalsIgnoreCase(dEl.attr("name").trim())) {
					dEl.remove();
					continue;
				}
			}
			// 移除所有div下面包含a标签的数据
			Elements aLst = dEl.getElementsByTag("a");
			if (aLst.size() > 0) {
				dEl.remove();
			}
		}
		// 移除所有的 a标签
		Elements aLst = nwDoc.getElementsByTag("a");
		for (Element ael : aLst) {
			ael.remove();
		}

		// 移除所有的 包裹列表div主体
		nwDoc.select(".pnl-packaging-main").remove();
		// 移除所有的 买家交易信息div主体
		nwDoc.select(".transaction-feedback-main").remove();
		// 移除所有的 更多产品，相关产品
		nwDoc.select(".related-products-main").remove();
		// 移除所有的 相关产品搜索
		nwDoc.select("#j-related-searches").remove();

		// 移除所有的 img属性含有以下字符的图片
		Elements imgLst = nwDoc.getElementsByTag("img");
		for (Element imel : imgLst) {
			if (imel.hasAttr("alt")) {
				String attrVal = imel.attr("alt");
				if ("Shipping".equalsIgnoreCase(attrVal)) {
					imel.remove();
				} else if ("Payment".equalsIgnoreCase(attrVal)) {
					imel.remove();
				} else if ("Feedback".equalsIgnoreCase(attrVal)) {
					imel.remove();
				} else if ("Contact us".equalsIgnoreCase(attrVal)) {
					imel.remove();
				} else if ("Return".equalsIgnoreCase(attrVal)) {
					imel.remove();
				}
			}
		}
		Elements ckImgLst = nwDoc.getElementsByTag("img");
		if (ckImgLst.size() == 0) {
			nwDoc = Jsoup.parseBodyFragment(content);
			// 移除所有的 a标签
			Elements nwALst = nwDoc.getElementsByTag("a");
			for (Element nael : nwALst) {
				nael.remove();
			}
		}
		return nwDoc.html();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveEditDetalis")
	@ResponseBody
	public JsonResult saveEditDetalis(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");

		try {
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				json.setOk(false);
				json.setMessage("获取登录信息失败，请登录");
				return json;
			}

			CustomGoodsPublish cgp = new CustomGoodsPublish();

			String contentStr = request.getParameter("content");
			// String localpath = request.getParameter("localpath");

			String pidStr = request.getParameter("pid");
			if (!(pidStr == null || "".equals(pidStr))) {
				cgp.setPid(pidStr);
			} else {
				json.setOk(false);
				json.setMessage("获取pid失败");
				return json;
			}
			// 获取商品信息
			CustomGoodsPublish orGoods = newCloudGoodsService.queryGoodsDetails(pidStr, 0);

			String remotepath = request.getParameter("remotepath");
			if (remotepath == null || "".equals(remotepath)) {
				json.setOk(false);
				json.setMessage("获取图片远程路径失败");
				return json;
			} else {
				cgp.setRemotpath(remotepath);
			}
			String enname = request.getParameter("enname");
			if (!(enname == null || "".equals(enname))) {
				cgp.setEnname(enname);
			} else {
				json.setOk(false);
				json.setMessage("获取产品名称失败");
				return json;
			}
			String weightStr = request.getParameter("weight");
			if (!(weightStr == null || "".equals(weightStr))) {
				// 判断重量是否被修改,只有被修改后才进行更新重量和运费
				// 判断显示的重量是否来自weight字段的值，是则不更新数据
				if (weightStr.equals(orGoods.getFinalWeight())) {
					cgp.setReviseWeight("0");
					cgp.setFeeprice("0");
				} else {
					// 判断显示的重量是否来自reviseWeight字段的值，是则不更新数据
					if (weightStr.equals(orGoods.getReviseWeight())) {
						cgp.setReviseWeight("0");
						cgp.setFeeprice("0");
					} else {
						cgp.setReviseWeight(weightStr);
						// 更新运费，逻辑：运输方式E邮宝，运费计算公式（0.08*克重+9）/6.75
						DecimalFormat df = new DecimalFormat("######0.00");
						// 判断重量是否是很小的值，如果是很小值则设置为1kg
						double weight = Double.valueOf(weightStr) < 0.000001 ? 1.00 : Double.valueOf(weightStr);
						double cFreight = (0.08 * weight * 1000 + 9) / 6.75;
						cgp.setFeeprice(df.format(cFreight));
					}
				}
			} else {
				json.setOk(false);
				json.setMessage("获取产品重量失败");
				return json;
			}
			String imgInfo = request.getParameter("imgInfo");
			if (!(imgInfo == null || "".equals(imgInfo))) {
				// 获取的橱窗图进行集合封装
				cgp.setImg("[" + imgInfo.replace(";", ",").replace(remotepath, "") + "]");
			} else {
				json.setOk(false);
				json.setMessage("获取橱窗图失败");
				return json;
			}
			String endetailStr = request.getParameter("endetail");
			if (!(endetailStr == null || "".equals(endetailStr))) {
				// 获取的商品属性进行集合封装
				cgp.setEndetail("[" + endetailStr.replaceAll(";", ", ") + "]");
			} else {
				// 不校检商品属性
				cgp.setEndetail("[]");
			}
			if (!(contentStr == null || "".equals(contentStr))) {
				// 产品详情
				String eninfo = contentStr.replaceAll(remotepath, "");
				cgp.setEninfo(eninfo);
			} else {
				json.setOk(false);
				json.setMessage("获取商品详情失败");
				return json;
			}

			//String rangePrice = request.getParameter("rangePrice");
			
			String sku = request.getParameter("sku");

			if (sku == null || "".equals(sku)) {
				String price = request.getParameter("price");
				if (price == null || "".equals(price)) {
					json.setOk(false);
					json.setMessage("获取价格数据失败");
					return json;
				} else {
					DecimalFormat df = new DecimalFormat("######0.00");
					cgp.setPrice(df.format(Double.parseDouble(price)));
				}
			} else  {
					JSONArray sku_json = JSONArray.fromObject(orGoods.getSku());
					List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
					boolean isSuccess = dealSkuByParam(skuList, sku, cgp);
					if (!isSuccess) {
						json.setOk(false);
						json.setMessage("商品单规格价格生成异常，请确认价格！");
						return json;
					}
				}
			

			String type = request.getParameter("type");
			// type 0 保存 1 保存并发布
			if (!(type == null || "".equals(type) || "0".equals(type))) {
				cgp.setValid(1);
				int success = newCloudGoodsService.saveEditDetalis(cgp, user.getAdmName(), user.getId(),
						Integer.valueOf(type));
				if (success > 0) {
					NewCloudPublishGoodsToOnlie pbThread = new NewCloudPublishGoodsToOnlie(pidStr, newCloudGoodsService, ftpConfig);
					pbThread.start();
				}
			} else {
				newCloudGoodsService.saveEditDetalis(cgp, user.getAdmName(), user.getId(), 0);
			}

			json.setOk(true);
			if (!(type == null || "".equals(type) || "0".equals(type))) {
				json.setMessage("更新成功,异步上传图片中，请等待");
			} else {
				json.setMessage("更新成功");
			}

		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("保存错误，原因：" + e.getMessage());
			e.printStackTrace();
			LOG.error("保存错误，原因：" + e.getMessage());
		}
		return json;
	}

	// 处理sku数据，跟参数传递过来的价格数据进行赋值
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
		cgp.setPrice(String.valueOf(genFloatWidthTwoDecimalPlaces(minPrice)));;
		if(maxPrice - minPrice > 0.0099){
			cgp.setRangePrice(genFloatWidthTwoDecimalPlaces(minPrice) + "-" + genFloatWidthTwoDecimalPlaces(maxPrice));
			DecimalFormat df = new DecimalFormat("######0.00");
			cgp.setPrice(df.format(minPrice));
		}else{
			cgp.setRangePrice(null);
		}
		
		cgp.setSku(newSkuList.toString());
		if (minPrice == 0 || maxPrice == 0 || newSkuList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 生成两位小数的float类型数据
	 * 
	 * @param numVal
	 * @return
	 */
	private float genFloatWidthTwoDecimalPlaces(float numVal) {
		BigDecimal bd = new BigDecimal(numVal);
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	@RequestMapping(value = "/setGoodsValid")
	@ResponseBody
	public JsonResult setGoodsValid(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");

		String pidStr = request.getParameter("pid");
		if (pidStr == null || "".equals(pidStr)) {
			json.setOk(false);
			json.setMessage("获取pid失败");
			return json;
		}
		try {
			Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
			if (user == null || user.getId() == 0) {
				json.setOk(false);
				json.setMessage("获取登录信息失败，请登录");
				return json;
			}

			String type = request.getParameter("type");
			if (type == null || "".equals(type)) {
				json.setOk(false);
				json.setMessage("获取设置类型type失败");
				return json;
			}
			// type -1 下架该商品 1 检查通过
			newCloudGoodsService.setGoodsValid(pidStr, user.getAdmName(), user.getId(), Integer.valueOf(type));
			json.setOk(true);
			json.setMessage("执行成功");

		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
			LOG.error("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
		}
		return json;
	}

	@RequestMapping(value = "/setGoodsInvalid")
	@ResponseBody
	public JsonResult setGoodsInvalid(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();

		String pidStr = request.getParameter("pid");
		if (pidStr == null || "".equals(pidStr)) {
			json.setOk(false);
			json.setMessage("获取pid失败");
			return json;
		}
		try {

			String adminId = request.getParameter("adminId");
			if (adminId == null || "".equals(adminId)) {
				json.setOk(false);
				json.setMessage("获取操作人id失败");
				return json;
			}
			// type -1 下架该商品 1 检查通过
			newCloudGoodsService.setGoodsValid(pidStr, "", Integer.valueOf(adminId), -1);
			json.setOk(true);
			json.setMessage("执行成功");

		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
			LOG.error("pid : " + pidStr + " 执行错误，原因：" + e.getMessage());
		}
		return json;
	}

	/**
	 * 编辑器内容保存
	 * 
	 * @date 2016年12月15日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/save", method = { RequestMethod.POST, RequestMethod.GET })
	public String getSave(HttpServletRequest request, HttpServletResponse response) {
		// 获取需要保存的内容
		String pid = "";
		try {
			String content = request.getParameter("content");
			System.err.println("text----------" + content);

			CustomGoodsBean bean = new CustomGoodsBean();
			pid = request.getParameter("pid");

			String localpath = request.getParameter("localpath");
			// 产品详情
			String eninfo = content.replace(localpath, "");
			if (IpCheckUtil.checkIsIntranet(request)) {
				eninfo = content.replace(localpath.replace(localIP, wanlIP), "");
			}

			bean.setPid(pid);
			bean.setEninfo(eninfo);
			String goodskeyword = request.getParameter("goodskeyword");
			bean.setKeyword(goodskeyword);

			String goodsname = request.getParameter("goodsname");
			bean.setEnname(goodsname);

			String goodsprice = request.getParameter("goodsprice");
			System.err.println(goodsprice);
			if (goodsprice != null && !goodsprice.isEmpty()) {
				goodsprice = StrUtils.matchStr(goodsprice, "(\\d+\\.*\\d*)");
				System.err.println("goodsprice:" + goodsprice);
			}
			bean.setPrice(goodsprice);
			// 价格区间
			String lastPrice = request.getParameter("lastPrice");
			bean.setLastPrice(lastPrice);
			lastPrice = StrUtils.isRangePrice(lastPrice) ? lastPrice : "0";
			double minPrice = Double.valueOf(lastPrice.split("-")[0]);
			double maxPrice = minPrice;

			String sku = request.getParameter("sku");
			// System.err.println("sku:"+sku);
			if (sku != null && !sku.isEmpty() && sku.startsWith("[")) {
				JSONArray sku_json = JSONArray.fromObject(sku);
				List<SkuAttrBean> skuList = (List<SkuAttrBean>) JSONArray.toCollection(sku_json, SkuAttrBean.class);
				for (SkuAttrBean skuBean : skuList) {
					// System.err.println(skuBean.toString());
					SkuValBean skuVal = skuBean.getSkuVal();
					String actSkuCalPrice = request.getParameter("actSkuCalPrice_" + skuBean.getSkuPropIds());
					double price = Double.valueOf(actSkuCalPrice);
					if (price - 0.001 < minPrice) {
						minPrice = price;
					}
					if (price - 0.001 > maxPrice) {
						maxPrice = price;
					}

					skuVal.setActSkuCalPrice(actSkuCalPrice);
					skuVal.setActSkuMultiCurrencyCalPrice(actSkuCalPrice);
					skuVal.setActSkuMultiCurrencyDisplayPrice(actSkuCalPrice);
					skuVal.setSkuCalPrice(actSkuCalPrice);
					skuVal.setSkuMultiCurrencyCalPrice(actSkuCalPrice);
					skuVal.setSkuMultiCurrencyDisplayPrice(actSkuCalPrice);
					skuBean.setSkuVal(skuVal);
				}
				String skuStr = JSON.toJSONString(skuList);
				bean.setSku(skuStr);

			}

			if (minPrice > 0) {
				lastPrice = format.format(minPrice);
				if (maxPrice > minPrice) {
					lastPrice = lastPrice + "" + format.format(maxPrice);
				}
				bean.setLastPrice(lastPrice);
			}

			if ((lastPrice == null || lastPrice.isEmpty() || "0".equals(lastPrice))
					&& (goodsprice == null || goodsprice.isEmpty())) {

				return "redirect:/newcloudEdit/edit?pid=" + pid;
			}

			String goodsdetail = request.getParameter("goodsdetail");
			bean.setEndetail(goodsdetail);

			String goodsweight = request.getParameter("goodsweight");
			goodsweight = StrUtils.matchStr(goodsweight, "(\\d+\\.*\\d*)");
			goodsweight = goodsweight.isEmpty() ? "0.5kg" : goodsweight + "kg";
			bean.setWeight(goodsweight);

			// String goodsmethod = request.getParameter("goodsmethod");
			// bean.setMethod(goodsmethod);

			// String goodsfeeprice = request.getParameter("goodsfeeprice");
			// bean.setFeeprice(goodsfeeprice);

			// String goodsposttime = request.getParameter("goodsposttime");
			// bean.setPosttime(goodsposttime);

			System.err.println("eninfo----------" + eninfo);
			newCloudGoodsService.updateInfo(bean);

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("save error :" + e.getMessage());
		}
		return "redirect:/newcloudEdit/edit?pid=" + pid;
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public String deleteImages(HttpServletRequest request, HttpServletResponse response) {
		// 获取需要保存的内容
		String pid = "";
		try {
			pid = request.getParameter("deletepid");
			String catid = request.getParameter("deletecatid");
			String image = request.getParameter("deleteimage");
			// <img
			// src="http://192.168.1.27:8083/importsvimg/img/1494837116435/electroniccigaretts/desc/541135342407/4164109109_1488196098.jpg"
			// alt="" />
			System.err.println("11image:" + image);
			if (image.indexOf("<img") > -1) {
				image = StrUtils.matchStr(image, "(?:src=\")(.*?)(?:\\.jpg)");
				image = image.isEmpty() ? "" : image + ".jpg";
			}
			System.err.println("22image:" + image);
			if (!image.endsWith(".jpg")) {
				return "redirect:/newcloudEdit/edit?pid=" + pid;
			}
			int lastIndexOf = image.lastIndexOf("/");
			image = image.substring(lastIndexOf + 1);
			System.err.println("33image:" + image);
			List<CustomGoodsBean> goodsListByCatid = newCloudGoodsService.getGoodsListByCatid(catid);
			List<CustomGoodsBean> list = new ArrayList<CustomGoodsBean>();
			for (CustomGoodsBean bean : goodsListByCatid) {
				String eninfo = bean.getEninfo();
				if (eninfo.indexOf(image) == -1) {
					continue;
				}
				Element document = Jsoup.parse(eninfo).body();
				Elements customInfo = document.select("div[class=custom_info]");
				Elements seImgs_result = new Elements();
				seImgs_result.addAll(customInfo);
				Elements seImgs = document.select("img");
				for (Element seimg : seImgs) {
					// 详情描述图片路径
					String src = seimg.attr("src");
					if (src == null || src.isEmpty()) {
						continue;
					}
					if (src.indexOf(image) > -1) {
						continue;
					}
					seImgs_result.add(seimg);
				}
				bean.setEninfo(seImgs_result.toString());
				list.add(bean);
			}
			newCloudGoodsService.updateInfoList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/newcloudEdit/edit?pid=" + pid;
	}

	/**
	 * 接受上传文件
	 * 
	 * @date 2016年12月16日
	 * @author abc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/uploads", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> getLoads(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = "";
		String err = "";
		String pid = request.getParameter("pid");

		if (pid == null || "".equals(pid)) {
			msg = "";
			err = "获取PID失败";
		} else {
			System.out.println("pid:" + pid);
			try {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				// 获取文件域
				List<MultipartFile> fileList = multipartRequest.getFiles("filedata");
				Random random = new Random();
				// 获取配置文件信息
				if (ftpConfig == null) {
					ftpConfig = GetConfigureInfo.getFtpConfig();
				}
				// 检查配置文件信息是否正常读取
				JsonResult json = new JsonResult();
				checkFtpConfig(ftpConfig, json);
				String localDiskPath = ftpConfig.getLocalDiskPath();
				if (json.isOk()) {
					for (MultipartFile mf : fileList) {
						if (!mf.isEmpty()) {
							// 得到文件保存的名称mf.getOriginalFilename()
							String originalName = mf.getOriginalFilename();
							// 文件的后缀取出来
							String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
							String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)) + fileSuffix);
							// 本地服务器磁盘全路径
							String localFilePath = "cloudimg/imgs/" + pid + "/" + saveFilename;
							// 文件流输出到本地服务器指定路径
							ImgDownload.writeImageToDisk(mf.getBytes(), localDiskPath + localFilePath);
							// 检查图片分辨率
							boolean is = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 100, 100);
							if (is) {
								is = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 700, 700);
								if(is){
									ImageCompression.reduceImgByWidth(700, 0.9f, localDiskPath + localFilePath, localDiskPath + localFilePath);
								}
								msg = ftpConfig.getLocalShowPath() + localFilePath;
							} else {
								// 判断分辨率不通过删除图片
								File file = new File(localFilePath);
								if (file.exists()) {
									file.delete();
								}
								msg = "";
								err = "图片分辨率小于100";
							}
						}
					}
				} else {
					msg = "";
					err = json.getMessage();
				}
			} catch (Exception e) {
				msg = "";
				err = "上传错误";
				e.printStackTrace();
				LOG.error("上传错误：" + e.getMessage());
			}
		}
		map.put("err", err);
		map.put("msg", msg);
		return map;
	}

	/**
	 * 编辑详情网路图片下载本地并上传服务器
	 * 
	 * @return
	 */
	@RequestMapping(value = "/uploadEinfoNetImg", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public JsonResult uploadEinfoNetImg(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();

		String imgs = request.getParameter("imgs");
		if (imgs == null || "".equals(imgs)) {
			json.setOk(false);
			json.setMessage("获取网路图片路径失败");
			return json;
		}
		String pid = request.getParameter("pid");
		if (pid == null || "".equals(pid)) {
			json.setOk(false);
			json.setMessage("获取pid失败");
			return json;
		}
		System.err.println("pid:" + pid + ";imgs" + imgs);

		String newImgUrl = "";
		Random random = new Random();
		String[] imgLst = imgs.split(";");
		boolean isSuccess = true;
		try {
			// 获取配置文件信息
			if (ftpConfig == null) {
				ftpConfig = GetConfigureInfo.getFtpConfig();
			}
			// 检查配置文件信息是否正常读取
			checkFtpConfig(ftpConfig, json);
			if (!json.isOk()) {
				return json;
			}
			String localDiskPath = ftpConfig.getLocalDiskPath();
			for (String imgUrl : imgLst) {
				if (!(imgUrl == null || "".equals(imgUrl.trim()) || imgUrl.length() < 10)) {
					// 得到文件保存的名称
					if (imgUrl.indexOf("?") > -1) {
						imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
					}
					// 兼容没有http头部的src
					if (imgUrl.indexOf("//") == 0) {
						imgUrl = "http:" + imgUrl;
					}
					// 文件的后缀取出来
					String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
					// 生成唯一文件名称
					String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
					// 本地服务器磁盘全路径
					String localFilePath = "cloudimg/imgs" + pid + "/" + saveFilename + fileSuffix;
					// 下载网络图片到本地
					boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
					if (is) {
						// 判断图片的分辨率是否小于100*100
						boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 100, 100);
						if (checked) {
							newImgUrl += ";" + ftpConfig.getLocalShowPath() + localFilePath;
							json.setOk(true);
							json.setMessage("图片上传本地成功");
						} else {
							// 判断分辨率不通过删除图片
							File file = new File(localFilePath);
							if (file.exists()) {
								file.delete();
							}
							isSuccess = false;
							json.setOk(false);
							json.setMessage("图片分辨率小于100*100，终止执行");
							break;
						}
					} else {
						isSuccess = false;
						json.setOk(false);
						json.setMessage("下载网路图片到本地失败，请重试");
						break;
					}
				}
			}
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("执行出错：" + e.getMessage());
			LOG.error("执行出错：" + e.getMessage());
		}
		if (isSuccess) {
			json.setOk(true);
			json.setData(newImgUrl.substring(1));
			json.setMessage("执行成功");
		}

		return json;
	}

	/**
	 * 橱窗图网路图片下载本地并上传服务器
	 */
	@RequestMapping(value = "/uploadTypeNetImg", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public JsonResult uploadTypeNetImg(HttpServletRequest request, HttpServletResponse response) {

		JsonResult json = new JsonResult();

		String imgs = request.getParameter("imgs");
		if (imgs == null || "".equals(imgs)) {
			json.setOk(false);
			json.setMessage("获取网路图片路径失败");
			return json;
		}
		String pid = request.getParameter("pid");
		if (pid == null || "".equals(pid)) {
			json.setOk(false);
			json.setMessage("获取pid失败");
			return json;
		}
		System.err.println("pid:" + pid + ";imgs" + imgs);

		String newImgUrl = "";
		Random random = new Random();
		String[] imgLst = imgs.split(";");
		boolean isSuccess = true;
		try {
			// 获取配置文件信息
			if (ftpConfig == null) {
				ftpConfig = GetConfigureInfo.getFtpConfig();
			}
			// 检查配置文件信息是否正常读取
			checkFtpConfig(ftpConfig, json);
			if (!json.isOk()) {
				return json;
			}
			String localDiskPath = ftpConfig.getLocalDiskPath();
			for (String imgUrl : imgLst) {
				if (!(imgUrl == null || "".equals(imgUrl.trim()))) {
					// 得到文件保存的名称
					if (imgUrl.indexOf("?") > -1) {
						imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
					}
					// 兼容没有http头部的src
					if (imgUrl.indexOf("//") == 0) {
						imgUrl = "http:" + imgUrl;
					}
					// 文件的后缀取出来
					String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
					// 生成唯一文件名称
					String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
					// 本地服务器磁盘全路径
					String localFilePath = "cloudimg/imgs/" + pid + "/" + saveFilename + fileSuffix;
					// 下载网络图片到本地
					boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
					if (is) {
						// 判断图片的分辨率是否小于400*200
						boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 400, 200);
						if (checked) {
							// 压缩图片400x400
							String localFilePath400x400 = "cloudimg/imgs/" + pid + "/" + saveFilename + ".400x400"
									+ fileSuffix;

							boolean is400 = ImageCompression.reduceImgByWidth(400, localDiskPath + localFilePath,
									localDiskPath + localFilePath400x400);
							// 压缩图片60x60
							String localFilePath60x60 = "cloudimg/imgs/" + pid + "/" + saveFilename + ".60x60" + fileSuffix;
							boolean is60 = ImageCompression.reduceImgByWidth(60, localDiskPath + localFilePath,
									localDiskPath + localFilePath60x60);
							if (is60 && is400) {
								newImgUrl += ";" + ftpConfig.getLocalShowPath() + localFilePath60x60;
								json.setOk(true);
								json.setMessage("本地图片上传成功");
							} else {
								// 判断分辨率不通过删除图片
								File file400 = new File(localDiskPath + localFilePath400x400);
								if (file400.exists()) {
									file400.delete();
								}
								File file60 = new File(localDiskPath + localFilePath60x60);
								if (file60.exists()) {
									file60.delete();
								}
								// 压缩失败整体终止执行
								isSuccess = false;
								json.setOk(false);
								json.setMessage("压缩图片60x60和400x400失败，终止执行");
								break;
							}
						} else {
							// 图片分辨率小于400*200整体终止执行
							isSuccess = false;
							json.setOk(false);
							json.setMessage("图片分辨率小于400*200，终止执行");
							break;
						}
					} else {
						isSuccess = false;
						json.setOk(false);
						json.setMessage("下载网路图片到本地失败，请重试");
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
			json.setOk(false);
			json.setMessage("执行出错：" + e.getMessage());
		}
		if (isSuccess) {
			json.setOk(true);
			json.setData(newImgUrl.substring(1));
			json.setMessage("执行成功");
		}

		return json;
	}

	/**
	 * 普通js上传文件
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
			json.setMessage("获取PID失败");
		} else {
			System.out.println("pid:" + pid);
			try {
				if (!file.isEmpty()) {
					// 获取配置文件信息
					if (ftpConfig == null) {
						ftpConfig = GetConfigureInfo.getFtpConfig();
					}
					// 检查配置文件信息是否正常读取
					checkFtpConfig(ftpConfig, json);
					if (!json.isOk()) {
						return json;
					}
					String localDiskPath = ftpConfig.getLocalDiskPath();
					Random random = new Random();
					String originalName = file.getOriginalFilename();
					// 文件的后缀取出来
					String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
					String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
					// 本地服务器磁盘全路径
					String localFilePath = "cloudimg/imgs/" + pid + "/" + saveFilename + fileSuffix;
					// 文件流输出到本地服务器指定路径
					ImgDownload.writeImageToDisk(file.getBytes(), localDiskPath + localFilePath);

					// 判断图片的分辨率是否小于400*200
					boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 400, 200);
					if (checked) {
						// 压缩图片400x400
						String localFilePath400x400 = "cloudimg/imgs/" + pid + "/" + saveFilename + ".400x400" + fileSuffix;
						boolean is400 = ImageCompression.reduceImgByWidth(400, localDiskPath + localFilePath,
								localDiskPath + localFilePath400x400);
						// 压缩图片60x60
						String localFilePath60x60 = "cloudimg/imgs/" + pid + "/" + saveFilename + ".60x60" + fileSuffix;
						boolean is60 = ImageCompression.reduceImgByWidth(60, localDiskPath + localFilePath,
								localDiskPath + localFilePath60x60);
						if (is60 && is400) {
							json.setData(ftpConfig.getLocalShowPath() + localFilePath60x60);
							json.setOk(true);
							json.setMessage("上传本地图片成功");
						} else {
							// 判断分辨率不通过删除图片
							File file400 = new File(localFilePath400x400);
							if (file400.exists()) {
								file400.delete();
							}
							File file60 = new File(localFilePath60x60);
							if (file60.exists()) {
								file60.delete();
							}
							// 压缩失败整体终止执行
							json.setOk(false);
							json.setMessage("压缩图片60x60和400x400失败，终止执行");
						}
					} else {
						// 图片分辨率小于400*200整体终止执行
						json.setOk(false);
						json.setMessage("图片分辨率小于400*200，终止执行");
					}
				} else {
					json.setOk(false);
					json.setMessage("获取文件失败，请重试");
				}
			} catch (Exception e) {
				e.printStackTrace();
				json.setOk(false);
				json.setMessage("上传错误:" + e.getMessage());
				LOG.error("上传错误：" + e.getMessage());
			}
		}
		return json;
	}

	/**
	 * 使用速卖通详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/useAliGoodsDetails", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult useAliGoodsDetails(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		json.setOk(true);
		String pid = request.getParameter("pid");
		if (pid == null || "".equals(pid)) {
			json.setOk(false);
			json.setMessage("获取PID失败");
			return json;
		}
		String aliGoodsInfo = request.getParameter("aliGoodsInfo");
		if (aliGoodsInfo == null || "".equals(aliGoodsInfo)) {
			json.setOk(false);
			json.setMessage("获取速卖通详情失败");
			return json;
		}

		Document nwDoc = null;
		try {
			// string转html文档类型
			nwDoc = Jsoup.parseBodyFragment(aliGoodsInfo);
			// 去掉html里面a标签数据
			nwDoc.getElementsByTag("a").remove();
			// 获取img标签
			Elements imgEls = nwDoc.getElementsByTag("img");
			Random random = new Random();
			// 使用线程池
			// 获取配置文件信息
			if (ftpConfig == null) {
				ftpConfig = GetConfigureInfo.getFtpConfig();
			}
			// 检查配置文件信息是否正常读取
			checkFtpConfig(ftpConfig, json);
			if (json.isOk()) {
				String localDiskPath = ftpConfig.getLocalDiskPath();
				for (Element imgEl : imgEls) {
					System.out.println("src:" + imgEl.attr("src"));

					String imgUrl = imgEl.attr("src");
					// 得到文件保存的名称
					if (imgUrl.indexOf("?") > -1) {
						imgUrl = imgUrl.substring(0, imgUrl.indexOf("?"));
					}
					// 兼容没有http头部的src
					if (imgUrl.indexOf("//") == 0) {
						imgUrl = "http:" + imgUrl;
					}
					// 文件的后缀取出来
					String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
					// 生成唯一文件名称
					String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
					// 本地服务器磁盘全路径
					String localFilePath = "cloudimg/imgs" + pid + "/" + saveFilename + fileSuffix;

					// 下载网络图片到本地
					boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
					if (is) {
						imgEl.attr("src", ftpConfig.getLocalShowPath() + localFilePath);
					} else {
						json.setOk(false);
						json.setMessage("下载图片失败，请重试！");
						break;
					}
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("执行错误：" + e.getMessage());
			LOG.error("执行错误：" + e.getMessage());
		}

		if (nwDoc == null) {
			json.setOk(false);
			json.setMessage("解析html数据失败，请重试");
		} else if (json.isOk()) {
			json.setData(nwDoc.toString());
			nwDoc = null;
		}
		return json;
	}

	/**
	 * 
	 * @Title addSimilarGoods
	 * @Description 插入相似商品
	 * @return JsonResult
	 */
	@RequestMapping(value = "/addSimilarGoods", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult addSimilarGoods(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();

		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setOk(false);
			json.setMessage("请登录后操作");
			return json;
		}

		String mainPid = request.getParameter("mainPid");
		if (mainPid == null || "".equals(mainPid)) {
			json.setOk(false);
			json.setMessage("获取商品PID失败");
			return json;
		}
		String similarPids = request.getParameter("similarPids");
		if (similarPids == null || "".equals(similarPids)) {
			json.setOk(false);
			json.setMessage("获取相似商品pid失败");
			return json;
		}
		List<SimilarGoods> existSimilarGoods = null;
		List<String> pidList = new ArrayList<String>();
		try {
			existSimilarGoods = newCloudGoodsService.querySimilarGoodsByMainPid(mainPid);
			pidList = new ArrayList<String>();
			List<String> showExistPids = new ArrayList<String>();
			String[] similarPidList = similarPids.split(";");
			if (!(existSimilarGoods == null || existSimilarGoods.size() == 0)) {
				for (SimilarGoods sml : existSimilarGoods) {
					pidList.add(sml.getSimilarPid());
				}
			}
			if (pidList.size() > 0) {
				for (String similarPid : similarPidList) {
					if (similarPid.equals(mainPid)) {
						showExistPids.add(similarPid);
					} else if (pidList.contains(similarPid)) {
						showExistPids.add(similarPid);
					}
				}
			}

			boolean is = newCloudGoodsService.batchInsertSimilarGoods(mainPid, similarPids, user.getId(), pidList);
			if (is) {
				json.setOk(true);
				if (showExistPids.size() > 0) {
					json.setMessage("插入成功,部分商品已经存在:" + showExistPids);
				} else {
					json.setMessage("插入成功");
				}
			} else {
				json.setOk(false);
				if (showExistPids.size() > 0) {
					json.setMessage("批量插入错误,部分商品已经存在:" + showExistPids);
				} else {
					json.setMessage("批量插入错误，请重试");
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("mainPid:" + mainPid + " addSimilarGoods 执行错误：" + e.getMessage());
			LOG.error("mainPid:" + mainPid + " addSimilarGoods 执行错误：" + e.getMessage());
		} finally {
			if (existSimilarGoods != null) {
				existSimilarGoods.clear();
			}
			if (pidList != null) {
				pidList.clear();
			}
		}
		return json;
	}

	@RequestMapping(value = "/querySimilarGoodsByMainPid", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult querySimilarGoodsByMainPid(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();

		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setOk(false);
			json.setMessage("请登录后操作");
			return json;
		}

		String mainPid = request.getParameter("mainPid");
		if (mainPid == null || "".equals(mainPid)) {
			json.setOk(false);
			json.setMessage("获取商品PID失败");
			return json;
		}

		try {
			List<SimilarGoods> goodsList = newCloudGoodsService.querySimilarGoodsByMainPid(mainPid);
			json.setOk(true);
			json.setData(goodsList);
		} catch (Exception e) {
			e.getStackTrace();
			json.setOk(false);
			json.setMessage("mainPid:" + mainPid + " querySimilarGoodsByMainPid 执行错误：" + e.getMessage());
			LOG.error("mainPid:" + mainPid + " querySimilarGoodsByMainPid 执行错误：" + e.getMessage());
		}
		return json;
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

	private void checkFtpConfig(FtpConfig ftpConfig, JsonResult json) {
		json.setOk(true);
		// 判断获取的配置信息是否有效
		if (ftpConfig == null || !ftpConfig.isOk()) {
			json.setOk(false);
			json.setMessage("获取配置文件失败");
		} else {
			if (StringUtil.isBlank(ftpConfig.getFtpURL())) {
				json.setOk(false);
				json.setMessage("获取ftpURL失败");
			} else if (StringUtil.isBlank(ftpConfig.getFtpPort())) {
				json.setOk(false);
				json.setMessage("获取ftpPort失败");
			} else if (StringUtil.isBlank(ftpConfig.getFtpUserName())) {
				json.setOk(false);
				json.setMessage("获取ftpUserName失败");
			} else if (StringUtil.isBlank(ftpConfig.getFtpPassword())) {
				json.setOk(false);
				json.setMessage("获取ftpPassword失败");
			} else if (StringUtil.isBlank(ftpConfig.getRemoteShowPath())) {
				json.setOk(false);
				json.setMessage("获取remoteShowPath失败");
			} else if (StringUtil.isBlank(ftpConfig.getLocalDiskPath())) {
				json.setOk(false);
				json.setMessage("获取localDiskPath失败");
			} else if (StringUtil.isBlank(ftpConfig.getLocalShowPath())) {
				json.setOk(false);
				json.setMessage("获取localShowPath失败");
			}
		}
	}

}
