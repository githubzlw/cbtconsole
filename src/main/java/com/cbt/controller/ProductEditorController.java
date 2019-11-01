package com.cbt.controller;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.ImportExSku;
import com.cbt.bean.ImportExSkuShow;
import com.cbt.bean.TypeBean;
import com.cbt.parse.bean.Set;
import com.cbt.parse.service.*;
import com.cbt.parse.service.ImgDownload;
import com.cbt.parse.service.StrUtils;
import com.cbt.service.ProductOfflineService;
import com.cbt.util.*;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.util.JsonResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
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
@RequestMapping(value = "/productEdit")
public class ProductEditorController {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ProductEditorController.class);
	private String chineseChar = "([\\一-\\龥]+)";
	private FtpConfig ftpConfig = GetConfigureInfo.getFtpConfig();

	@Autowired
	private ProductOfflineService pdOlService;

	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/detalisEdit", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView detalisEdit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mv = new ModelAndView("product_manage_detalis");

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
		CustomGoodsPublish goods = pdOlService.queryGoodsDetails(pid, 0);
		if(goods == null){
			mv.addObject("uid", -1);
			return mv;
		}

		// 取出1688原货源链接

		// 将goods的entype属性值取出来,即规格图
		List<TypeBean> typeList = deal1688GoodsType(goods);

		// 将goods的img属性值取出来,即橱窗图
		request.setAttribute("showimgs", JSONArray.fromObject("[]"));
		List<String> imgs = deal1688GoodsImg(goods, goods.getLocalpath());
		if (imgs.size() > 0) {
			request.setAttribute("showimgs", JSONArray.fromObject(imgs));
		}

		HashMap<String, String> pInfo = deal1688Sku(goods);
		request.setAttribute("showattribute", pInfo);

		// 处理Sku数据
		// 判断是否是区间价格，含有区间价格的获取sku数据进行处理

		if (goods.getRangePrice() == null || "".equals(goods.getSku()) || goods.getSku() == null
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
		String localpath = goods.getLocalpath();
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
		boolean isLocal = false;
		if (!(goods.getAliGoodsPid() == null || "".equals(goods.getAliGoodsPid()))) {
			GoodsBean algood = null;
			System.err.println("url:" + goods.getAliGoodsUrl());
			if (isLocal) {
				// 本地直接获取ali商品信息
				algood = ParseGoodsUrl.parseGoodsw(goods.getAliGoodsUrl(), 3);
			} else {
				// 远程访问获取ali商品信息
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
		}
		// 返回待编辑数据到编辑页面
		mv.addObject("text", text);
		mv.addObject("pid", pid);
		goods.setWeight(StrUtils.matchStr(goods.getWeight(), "(\\d+\\.*\\d*)"));
		mv.addObject("goods", goods);
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
			String localPath = cgbean.getLocalpath();
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
							if (StringUtils.isBlank(tem) || StringUtils.equals(tem, "null")) {
								typeBean.setImg("");
							} else {
								if (tem.indexOf("http://") > -1 || tem.indexOf("https://") > -1) {
									typeBean.setImg(tem);
								} else {
									typeBean.setImg(localPath + tem);
								}
							}
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
			for (String ppid : ppidLst) {
				// 比较type类别的数据，获取类别信息
				for (TypeBean tyb : typeList) {
					if (ppid.equals(tyb.getId())) {
						skuAttrs += ";" + tyb.getId() + "@" + tyb.getType() + "@" + tyb.getValue();
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
				cbSkuLst.add(ipes);
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

			String pidStr = request.getParameter("pid");
			if (!(pidStr == null || "".equals(pidStr))) {
				cgp.setPid(pidStr);
			} else {
				json.setOk(false);
				json.setMessage("获取pid失败");
				return json;
			}
			// 获取商品信息
			CustomGoodsPublish orGoods = pdOlService.queryGoodsDetails(pidStr, 0);
			cgp.setEntype(orGoods.getEntype());

			String enname = request.getParameter("enname");
			if (!(enname == null || "".equals(enname))) {
				cgp.setEnname(enname);
			} else {
				json.setOk(false);
				json.setMessage("获取产品名称失败");
				return json;
			}

			String mainImg = request.getParameter("mainImg");
			if (!(mainImg == null || "".equals(mainImg))) {
				cgp.setShowMainImage(mainImg);
			} else {
				json.setOk(false);
				json.setMessage("获取搜索图失败");
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
						double cFreight = (0.08 * weight * 1000 + 9) / Util.EXCHANGE_RATE;
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
				cgp.setImg("[" + imgInfo.replace(";", ",") + "]");
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
				json.setOk(false);
				json.setMessage("获取商品属性失败");
				return json;
			}
			if (!(contentStr == null || "".equals(contentStr))) {
				// 产品详情
				cgp.setEninfo(contentStr);
			} else {
				json.setOk(false);
				json.setMessage("获取商品详情失败");
				return json;
			}

			String rangePrice = request.getParameter("rangePrice");

			if (rangePrice == null || "".equals(rangePrice)) {
				String wprice = request.getParameter("wprice");
				if (wprice == null || "".equals(wprice)) {
					json.setOk(false);
					json.setMessage("获取区间价格数据失败");
					return json;
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
					json.setMessage("获取单规格价数据失败");
					return json;
				} else {
					JSONArray sku_json = JSONArray.fromObject(orGoods.getSku());
					List<ImportExSku> skuList = (List<ImportExSku>) JSONArray.toCollection(sku_json, ImportExSku.class);
					dealSkuByParam(skuList, sku, cgp);
				}
			}
			
			int success = pdOlService.saveEditDetalis(cgp, user.getId(), 0);
			if (success > 0) {
				json.setOk(true);
				json.setMessage("更新成功");
			}else{
				json.setOk(false);
				json.setMessage("保存失败，请重试！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("保存错误，原因：" + e.getMessage());
			LOG.error("保存错误，原因：" + e.getMessage());
		}
		return json;
	}

	// 处理sku数据，跟参数传递过来的价格数据进行赋值
	private void dealSkuByParam(List<ImportExSku> skuList, String sku, CustomGoodsPublish cgp) {
		String[] skuLst = sku.split(";");
		float minPrice = 0;
		float maxPrice = 0;
		for (String ppid : skuLst) {
			String[] idpc = ppid.split("@");
			for (ImportExSku ies : skuList) {
				if (ies.getSkuPropIds().equals(idpc[0])) {
					float tempPrice = Float.valueOf(idpc[1]);
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
					break;
				}
			}
		}
		cgp.setRangePrice(genFloatWidthTwoDecimalPlaces(minPrice) + "-" + genFloatWidthTwoDecimalPlaces(maxPrice));
		cgp.setSku(skuList.toString());
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
							String localFilePath = "offlineimg/" + pid + "/desc/" + saveFilename;
							// 文件流输出到本地服务器指定路径
							ImgDownload.writeImageToDisk(mf.getBytes(), localDiskPath + localFilePath);
							// 检查图片分辨率
							boolean is = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 100, 100);
							if (is) {
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
				LOG.error("上传错误，原因：" + e.getMessage());
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
					//兼容没有http头部的src
					if(imgUrl.indexOf("//") == 0){
						imgUrl = "http:" + imgUrl;
					}
					// 文件的后缀取出来
					String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
					// 生成唯一文件名称
					String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
					// 本地服务器磁盘全路径
					String localFilePath = "offlineimg/" + pid + "/desc/" + saveFilename + fileSuffix;
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
					//兼容没有http头部的src
					if(imgUrl.indexOf("//") == 0){
						imgUrl = "http:" + imgUrl;
					}
					// 文件的后缀取出来
					String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
					// 生成唯一文件名称
					String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
					// 本地服务器磁盘全路径
					String localFilePath = "offlineimg/" + pid + "/" + saveFilename + fileSuffix;
					// 下载网络图片到本地
					boolean is = ImgDownload.execute(imgUrl, localDiskPath + localFilePath);
					if (is) {
						// 判断图片的分辨率是否小于400*200
						boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 400, 200);
						if (checked) {
							// 压缩图片400x400
							String localFilePath400x400 = "offlineimg/" + pid + "/" + saveFilename + ".400x400"
									+ fileSuffix;

							boolean is400 = ImageCompression.reduceImgByWidth(400, localDiskPath + localFilePath,
									localDiskPath + localFilePath400x400);
							// 压缩图片60x60
							String localFilePath60x60 = "offlineimg/" + pid + "/" + saveFilename + ".60x60"
									+ fileSuffix;
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
					String localFilePath = "offlineimg/" + pid + "/" + saveFilename + fileSuffix;
					// 文件流输出到本地服务器指定路径
					ImgDownload.writeImageToDisk(file.getBytes(), localDiskPath + localFilePath);

					// 判断图片的分辨率是否小于400*200
					boolean checked = ImageCompression.checkImgResolution(localDiskPath + localFilePath, 400, 200);
					if (checked) {
						// 压缩图片400x400
						String localFilePath400x400 = "offlineimg/" + pid + "/" + saveFilename + ".400x400"
								+ fileSuffix;
						boolean is400 = ImageCompression.reduceImgByWidth(400, localDiskPath + localFilePath,
								localDiskPath + localFilePath400x400);
						// 压缩图片60x60
						String localFilePath60x60 = "offlineimg/" + pid + "/" + saveFilename + ".60x60" + fileSuffix;
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
					//兼容没有http头部的src
					if(imgUrl.indexOf("//") == 0){
						imgUrl = "http:" + imgUrl;
					}
					// 文件的后缀取出来
					String fileSuffix = imgUrl.substring(imgUrl.lastIndexOf("."));
					// 生成唯一文件名称
					String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)));
					// 本地服务器磁盘全路径
					String localFilePath = "offlineimg/" + pid + "/desc/" + saveFilename + fileSuffix;

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
			e.printStackTrace();
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
	 * @Title publishGoods 
	 * @Description 走流程发布到线上，非即时上架，上架中的商品将不再显示
	 * @param request
	 * @param response
	 * @return JsonResult
	 */
	@RequestMapping(value = "/publishGoods", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult publishGoods(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
		if (user == null || user.getId() == 0) {
			json.setOk(false);
			json.setMessage("获取登录用户失败");
			return json;
		}
		String pid = request.getParameter("pid");
		if (pid == null || "".equals(pid)) {
			json.setOk(false);
			json.setMessage("获取PID失败");
			return json;
		}		
		try {
			CustomGoodsPublish goods = pdOlService.queryGoodsDetails(pid, 0);
			if(goods == null){
				json.setOk(false);
				json.setMessage("商品信息失败");
			}else{
				json = downloadNetImg(goods);
				if(json.isOk()){
					int count = pdOlService.saveEditDetalis(goods, user.getId(), 0);
					if(count > 0){
						boolean isSc = pdOlService.publishGoods(pid,user.getId());
						if(isSc){
							json.setOk(true);
						}else{
							json.setOk(false);
							json.setMessage("执行失败，请重试");
						}
					}else{
						json.setOk(false);
						json.setMessage("下载网络图片到本地失败");
					}	
				}	
			}			
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("执行错误：" + e.getMessage());
			LOG.error("执行错误：" + e.getMessage());
		}
		return json;
	}
	
	
	/**
	 * 
	 * @Title downloadNetImg
	 * @Description 下载网络图片
	 * @param goods
	 * @return JsonResult
	 */
	private JsonResult downloadNetImg(CustomGoodsPublish goods) {
		JsonResult json = new JsonResult();
		// 分类别下载goods中的图片
		// 获取配置文件信息
		if (ftpConfig == null) {
			ftpConfig = GetConfigureInfo.getFtpConfig();
		}
		// 1.搜索图，格式是200x200.jpg替换.jpg
		String mainImg = goods.getShowMainImage();
		String mainFileSuffix = goods.getShowMainImage();
		// 取出后缀名称
		int mainLastIndexOf = mainImg.lastIndexOf("/");
		if (mainLastIndexOf > -1) {
			mainFileSuffix = mainImg.substring(mainLastIndexOf + 1).replace(".400x400", "").replace(".jpg",
					".220x220.jpg");
		}
		// 本地服务器磁盘全路径
		String mainLocalFilePath = "offlineimg/" + goods.getPid() + "/" + mainFileSuffix;
		// 下载网络图片到本地
		boolean is = ImgDownload.execute(mainImg, ftpConfig.getLocalDiskPath() + mainLocalFilePath);
		// 下载成功,赋值新的网络路径
		if (is) {
			goods.setShowMainImage(mainLocalFilePath);
			goods.setLocalpath(ftpConfig.getLocalShowPath());
			json.setOk(true);
		} else {
			// 下载不成功
			json.setOk(false);
			json.setMessage("下载搜索图失败！");
			return json;
		}

		// 2.规格图 400x400.jpg替换.jpg和60x60.jpg替换.jpg
		// 规格图bean
		List<TypeBean> typeList = deal1688GoodsType(goods);
		if (typeList.size() > 0) {
			String typeImg = "";
			String typeSuffix60 = "";
			String typeSuffix400 = "";
			String typeLocalFilePath = "";
			int typeIndexOf = -1;
			// 循环下载

			for (TypeBean tyImg : typeList) {
				typeImg = tyImg.getImg();
				// 判断规格是否有图片
				if (!(typeImg == null || "".equals(typeImg.trim()) || typeImg.length() < 5)) {
					// 判断是否是本地上传的图片，是的话单独替换即可
					if (typeImg.contains(ftpConfig.getLocalShowPath())) {
						tyImg.setImg(typeImg.replace(ftpConfig.getLocalShowPath(), ""));
					} else {
						// 网络图片进行下面操作
						typeSuffix60 = tyImg.getImg();
						typeSuffix400 = tyImg.getImg();
						// 取出后缀名称
						typeIndexOf = typeImg.lastIndexOf("/");
						if (typeIndexOf > -1) {
							typeSuffix60 = typeImg.substring(typeIndexOf + 1).replace(".jpg", ".60x60.jpg");
							typeSuffix400 = typeImg.substring(typeIndexOf + 1).replace(".jpg", ".400x400.jpg");
						}
						// 下载网络图片400x400到本地
						// 本地服务器磁盘全路径
						typeLocalFilePath = "offlineimg/" + goods.getPid() + "/" + typeSuffix400;
						is = ImgDownload.execute(typeImg, ftpConfig.getLocalDiskPath() + typeLocalFilePath);
						if (is) {
							// 本地服务器磁盘全路径
							typeLocalFilePath = "offlineimg/" + goods.getPid() + "/" + typeSuffix60;
							// 下载网络图片60x60到本地
							is = ImgDownload.execute(typeImg, ftpConfig.getLocalDiskPath() + typeLocalFilePath);
							// 下载60x60图片成功,赋值新的网络路径
							if (is) {
								tyImg.setImg(typeLocalFilePath);
								goods.setLocalpath(ftpConfig.getLocalShowPath());
							} else {
								System.out.println("download typeLocalFilePath[" + typeLocalFilePath + "] error!");
								// 下载不成功
								json.setOk(false);
								json.setMessage("下载规格图失败！");
								break;
							}
						} else {
							System.out.println("download typeLocalFilePath[" + typeLocalFilePath + "] error!");
							// 下载400x400图片不成功
							json.setOk(false);
							json.setMessage("下载规格图失败！");
							break;
						}
					}
				}
			}
			if (json.isOk()) {
				// 更新成功重新赋值规格数据
				goods.setType(typeList.toString());
			} else {
				return json;
			}
		}

		// 3.橱窗图 400x400.jpg替换.jpg和60x60.jpg替换.jpg
		// 橱窗图list集合
		List<String> imgs = deal1688GoodsImg(goods, goods.getLocalpath());
		if (imgs.size() > 0) {

			String windowSuffix60 = "";
			String windowSuffix400 = "";
			int windowIndexOf = -1;
			String windowLocalFilePath = "";
			List<String> newImgs = new ArrayList<String>();
			// 循环下载
			for (String windowImg : imgs) {
				// 判断是否是本地上传的图片，是的话单独替换即可
				if (windowImg.contains(ftpConfig.getLocalShowPath())) {
					newImgs.add(windowImg.replace(ftpConfig.getLocalShowPath(), ""));
				} else {
					// 网络图片进行下面操作
					windowSuffix60 = windowImg;
					windowSuffix400 = windowImg;
					// 取出后缀名称
					windowIndexOf = windowImg.lastIndexOf("/");
					if (windowIndexOf > -1) {
						windowSuffix60 = windowImg.substring(windowIndexOf + 1).replace(".jpg", ".60x60.jpg");
						windowSuffix400 = windowImg.substring(windowIndexOf + 1).replace(".jpg", ".400x400.jpg");
					}
					// 下载网络图片400x400到本地
					// 本地服务器磁盘全路径
					windowLocalFilePath = "offlineimg/" + goods.getPid() + "/" + windowSuffix400;
					is = ImgDownload.execute(windowImg, ftpConfig.getLocalDiskPath() + windowLocalFilePath);
					if (is) {
						// 本地服务器磁盘全路径
						windowLocalFilePath = "offlineimg/" + goods.getPid() + "/" + windowSuffix60;
						// 下载网络图片60x60到本地
						is = ImgDownload.execute(windowImg, ftpConfig.getLocalDiskPath() + windowLocalFilePath);
						// 下载60x60图片成功,赋值新的网络路径
						if (is) {
							newImgs.add(windowLocalFilePath);
							goods.setShowMainImage(windowLocalFilePath);
							goods.setLocalpath(ftpConfig.getLocalShowPath());
						} else {
							System.out.println("download windowLocalFilePath[" + windowLocalFilePath + "] error!");
							// 下载不成功
							json.setOk(false);
							json.setMessage("下载橱窗图失败！");
							break;
						}
					} else {
						System.out.println("download windowLocalFilePath[" + windowLocalFilePath + "] error!");
						// 下载400x400图片不成功
						json.setOk(false);
						json.setMessage("下载橱窗图失败！");
						break;
					}
				}
			}
			if (json.isOk()) {
				// 更新成功重新赋值橱窗图数据
				goods.setImg(newImgs.toString());
			} else {
				return json;
			}
		}

		// 4.详情图
		Document htmlDoc = Jsoup.parseBodyFragment(goods.getEninfo());
		// 获取所有的 img属性的图片
		Elements infoImgLst = htmlDoc.getElementsByTag("img");
		String infoLocalFilePath = "";
		// 判断含有图片时进行图片下载处理
		if (infoImgLst.size() > 0) {
			for (Element imEl : infoImgLst) {
				// 判断本地图片，直接替换路径
				String infoImg = imEl.attr("src");
				if (infoImg.contains(ftpConfig.getLocalShowPath())) {
					imEl.attr("src", infoImg.replace(ftpConfig.getLocalShowPath(), ""));
				} else {
					// 判断网络图片，下载本地
					String infoFileSuffix = infoImg;
					// 取出后缀名称
					int infoLastIndexOf = infoImg.lastIndexOf("/");
					if (infoLastIndexOf > -1) {
						infoFileSuffix = infoImg.substring(infoLastIndexOf);
					}
					// 本地服务器磁盘全路径
					infoLocalFilePath = "offlineimg/" + goods.getPid() + "/" + infoFileSuffix;
					// 下载网络图片到本地
					is = ImgDownload.execute(infoImg, ftpConfig.getLocalDiskPath() + infoLocalFilePath);
					// 下载成功,赋值新的网络路径
					if (is) {
						imEl.attr("src", infoLocalFilePath);
					} else {
						// 下载不成功
						json.setOk(false);
						json.setMessage("下载详情图失败！");
						break;
					}
				}
			}
			if (json.isOk()) {
				// 更新成功重新赋值详情图数据
				goods.setEninfo(htmlDoc.html());
				goods.setLocalpath(ftpConfig.getLocalShowPath());
			} else {
				return json;
			}
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
