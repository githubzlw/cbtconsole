package com.cbt.Specification.ctrl;

import com.cbt.Specification.bean.SpecificationComparison;
import com.cbt.Specification.service.ComparisonService;
import com.cbt.Specification.util.parseAliAndTaoBaoHtml;
import com.cbt.website.util.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/specificationComparison")
public class SpecificationComparisonController {

	@Resource
	ComparisonService comparisonService;

	@RequestMapping(value = "/queryChoiceComparisonByCid")
	@ResponseBody
	public JsonResult queryChoiceComparisonByCid(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST,GET");
		JsonResult json = new JsonResult();
		String aliCid = request.getParameter("aliCid");// AliExpress的类别id
		String aliSpecificationStr = request.getParameter("aliSpecificationStr");// AliExpress规格信息
		String choiceAreaStr = request.getParameter("choiceAreaStr");// 1688选择区的数据
		if (aliCid == null || "".equals(aliCid)) {
			json.setOk(false);
			json.setMessage("获取aliCid失败");
			return json;
		}
		if (aliSpecificationStr == null || "".equals(aliSpecificationStr)) {
			json.setOk(false);
			json.setMessage("获取AliExpress规格信息失败");
			return json;
		}
		if (choiceAreaStr == null || "".equals(choiceAreaStr)) {
			json.setOk(false);
			json.setMessage("获取AliExpress选择区数据失败");
			return json;
		}
		try {
			// 解析传递过来的AliExpress规格,如:Color:Black@10;Size:XL@100014065
			String[] aliSpecificationLst = aliSpecificationStr.split(";");
			String specificationNames = "";
			String attributeNames = "";
			for (String aliSpec : aliSpecificationLst) {
				String[] infos = aliSpec.split(":");
				if (infos.length == 2) {
					specificationNames += "," + infos[0].split("@")[0];
					attributeNames += "," + infos[1].split("@")[0];
				}
			}
			String[] spcNamelst = specificationNames.substring(1).split(",");
			String[] attrNamelst = attributeNames.substring(1).split(",");
			if (spcNamelst.length > 0 && attrNamelst.length > 0) {
				List<SpecificationComparison> aliChoiceComparisons = comparisonService.queryChoiceByAliCid(aliCid,
						spcNamelst, attrNamelst);// 获取选择区翻译数据
				// 解析传递过来的1688规格和规格属性的数据,数据的格式是：规格名:规格属性,规格名:规格属性
				List<String> reDetails = parseChoiceAreaStr(URLDecoder.decode(choiceAreaStr, "UTF-8"));
				// 获取解析结果
				List<SpecificationComparison> comparisonsResult = new ArrayList<SpecificationComparison>();
				comparisonsResult = getComparisonResult(reDetails, aliChoiceComparisons);
				json.setOk(true);
				json.setMessage("查询成功");
				json.setData(comparisonsResult);
			} else {
				json.setOk(false);
				json.setMessage("参数解析失败，请确认参数格式是否正确");
			}

			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	/**
	 * 查询规格详情区的比对数据，需要AliExpress的类别id和1688的url
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryDetailComparisonBy1688")
	@ResponseBody
	public JsonResult queryDetailComparisonBy1688(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();

		String aliCid = request.getParameter("aliCid");// AliExpress的类别id
		String url1688 = request.getParameter("url1688");// 1688的url
		if (aliCid == null || "".equals(aliCid)) {
			json.setOk(false);
			json.setMessage("获取aliCid失败");
			return json;
		}
		if (url1688 == null || "".equals(url1688)) {
			json.setOk(false);
			json.setMessage("获取1688的url失败");
			return json;
		}
		try {
			List<SpecificationComparison> aliDetailComparisons = comparisonService.queryDetailByAliCid(aliCid);// 获取详情区翻译数据
			List<String> reDetails = parseAliAndTaoBaoHtml.parseDetailBy1688Url(url1688);// 解析1688规格详情
			// 获取解析结果
			List<SpecificationComparison> comparisonsResult = new ArrayList<SpecificationComparison>();
			comparisonsResult = getComparisonResult(reDetails, aliDetailComparisons);
			json.setOk(true);
			json.setMessage("查询成功");
			json.setData(comparisonsResult);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	/**
	 * 查询规格详情区的比对数据，需要AliExpress的类别id和1688的url
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryDetailComparisonByTaobao")
	@ResponseBody
	public JsonResult queryDetailComparisonByTaobao(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();

		String aliCid = request.getParameter("aliCid");// AliExpress的类别id
		String urlTaobao = request.getParameter("urlTaobao");// urlTaobao的url
		if (aliCid == null || "".equals(aliCid)) {
			json.setOk(false);
			json.setMessage("获取aliCid失败");
			return json;
		}
		if (urlTaobao == null || "".equals(urlTaobao)) {
			json.setOk(false);
			json.setMessage("获取taobao的url失败");
			return json;
		}
		try {
			List<SpecificationComparison> aliDetailComparisons = comparisonService.queryDetailByAliCid(aliCid);// 获取详情区翻译数据
			List<String> reDetails = parseAliAndTaoBaoHtml.parseDetailByTaobaoUrl(urlTaobao);// 解析taobao规格详情
			// 获取解析结果
			List<SpecificationComparison> comparisonsResult = new ArrayList<SpecificationComparison>();
			comparisonsResult = getComparisonResult(reDetails, aliDetailComparisons);
			json.setOk(true);
			json.setMessage("查询成功");
			json.setData(comparisonsResult);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	private List<SpecificationComparison> getComparisonResult(List<String> reDetails,
                                                              List<SpecificationComparison> aliComparisons) {

		// 过滤读取html页面节点信息时，最后一个节点有规格名但没有属性值存在的情况
		int conunt = reDetails.size() % 2 == 0 ? reDetails.size() / 2 : (reDetails.size() - 1) / 2;
		// 获取所有1688信息，保存到比对数据中
		List<SpecificationComparison> comparisons1688 = new ArrayList<SpecificationComparison>();
		List<SpecificationComparison> removeCps1688 = new ArrayList<SpecificationComparison>();
		for (int k = 0; k < conunt; k++) {
			SpecificationComparison cps1688 = new SpecificationComparison();
			cps1688.setSpecificationChName1688(reDetails.get(k * 2));
			cps1688.setAttributeChName1688(reDetails.get(k * 2 + 1));
			comparisons1688.add(cps1688);
		}
		List<SpecificationComparison> nwComparisons = new ArrayList<SpecificationComparison>();
		// 开始比对
		for (SpecificationComparison aliCom : aliComparisons) {// 循环ali
			String aliSpcNameStr = aliCom.getAliSpecificationChName();
			if (!(aliSpcNameStr == null || "".equals(aliSpcNameStr))) {
				for (SpecificationComparison choice1688 : comparisons1688) {// 循环1688
					// 将详情区的AliExpress规格名和1688规格名进行比对,对比相等的数据
					String[] aliSpcNameLst = aliSpcNameStr.split(",");// 翻译规格中文名进行分割
					for (String aliSpcName : aliSpcNameLst) {// 中文名分割循环处理
						if (aliSpcName.equals(choice1688.getSpecificationChName1688())) {
							// 规格匹配成功，移除1688集合的当前数据
							removeCps1688.add(choice1688);
							// 判断中文是否存在，不存在的就不进行比对
							if (!(aliCom.getAliAttributeChName() == null
									|| "".equals(aliCom.getAliAttributeChName()))) {
								// 分割规格属性数据
								String[] attLst = aliCom.getAliAttributeChName().split(",");
								boolean flag = true;
								for (String attStr : attLst) {
									// 匹配成功的标记
									if (attStr.equals(choice1688.getAttributeChName1688())) {
										aliCom.setSpecificationChName1688(choice1688.getSpecificationChName1688());
										aliCom.setAttributeChName1688(choice1688.getAttributeChName1688());
										aliCom.setAttributeMatch(true);
										flag = false;
										break;
									}
								}
								if (flag) {
									// 如果规格属性比对不一致，放入一行，标记匹配不成功
									aliCom.setSpecificationChName1688(choice1688.getSpecificationChName1688());
									aliCom.setAttributeChName1688(choice1688.getAttributeChName1688());
									aliCom.setAttributeMatch(false);
								}
								break;
							}
						}
					}
				}
			}
			nwComparisons.add(aliCom);
		}
		// 循环结束后，从1688集合中移除匹配成功的数据
		comparisons1688.removeAll(removeCps1688);
		// 将剩余的1688集合数据添加到结果集中
		nwComparisons.addAll(comparisons1688);
		return nwComparisons;
	}

	private List<String> parseChoiceAreaStr(String choiceAreaStr) {
		List<String> choiceLst = new ArrayList<String>();
		if (choiceAreaStr == null || "".equals(choiceAreaStr)) {
			return choiceLst;
		}
		String[] singleDts = choiceAreaStr.split(",");
		for (String single : singleDts) {
			String[] specificationS = single.split(":");
			choiceLst.add(specificationS[0]);
			choiceLst.add(specificationS[1]);
		}
		return choiceLst;
	}

}
