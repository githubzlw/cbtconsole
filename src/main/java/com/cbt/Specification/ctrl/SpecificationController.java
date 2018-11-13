package com.cbt.Specification.ctrl;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.Specification.bean.SpecificationMapping;
import com.cbt.Specification.bean.SpecificationTranslation;
import com.cbt.Specification.service.SpecificationService;
import com.cbt.Specification.util.InitSpecification;
import com.cbt.website.util.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/specification")
public class SpecificationController {

	@Resource
	SpecificationService specificationService;

	@RequestMapping(value = "/initSpecification")
	@ResponseBody
	public JsonResult initSpecification(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			InitSpecification initSpc = new InitSpecification();
			initSpc.insertSpecification();
			json.setOk(true);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("执行初始化规格和规格属性失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/queryAliCategoryByLvOne")
	@ResponseBody
	public JsonResult queryAliCategoryByLvOne(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			List<AliCategory> aliCategories = specificationService.queryAliCategoryByLvOne();
			json.setOk(true);
			json.setData(aliCategories);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/queryAliCategoryByCidAndLv")
	@ResponseBody
	public JsonResult queryAliCategoryByCidAndLv(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String cid = request.getParameter("cid");
			String lvStr = request.getParameter("lv");
			if (cid == null || "".equals(cid)) {
				json.setOk(false);
				json.setMessage("获取cid失败！");
				return json;
			}
			if (lvStr == null || "".equals(lvStr)) {
				json.setOk(false);
				json.setMessage("获取lv失败！");
				return json;
			}
			List<AliCategory> aliCategories = specificationService.queryAliCategoryByCidAndLv(cid,
					Integer.valueOf(lvStr));
			json.setOk(true);
			json.setData(aliCategories);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/queryPathByCid")
	@ResponseBody
	public JsonResult queryPathByCid(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String cid = request.getParameter("cid");
			if (cid == null || "".equals(cid)) {
				json.setOk(false);
				json.setMessage("获取cid失败！");
				return json;
			}
			String path = specificationService.queryPathByCid(cid);
			json.setOk(true);
			json.setData(path);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/queryTranslationByCid")
	@ResponseBody
	public JsonResult queryTranslationByCid(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String cid = request.getParameter("cid");
			if (cid == null || "".equals(cid)) {
				json.setOk(false);
				json.setMessage("获取cid失败！");
				return json;
			}
			List<SpecificationTranslation> sTranslations = specificationService.queryTranslationByCid(cid);
			json.setOk(true);
			json.setData(sTranslations);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/queryTranslationById")
	@ResponseBody
	public JsonResult queryTranslationById(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String translationId = request.getParameter("translationId");
			if (translationId == null || "".equals(translationId)) {
				json.setOk(false);
				json.setMessage("获取translationId失败！");
				return json;
			}
			SpecificationTranslation sTranslation = specificationService
					.queryTranslationById(Integer.valueOf(translationId));
			json.setOk(true);
			json.setData(sTranslation);
			return json;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/insertTranslation")
	@ResponseBody
	public JsonResult insertTranslation(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			SpecificationTranslation sTranslation = getSpecificationTranslation(request);
			specificationService.insertTranslation(sTranslation);
			String isSyn = request.getParameter("isSyn");
			if (isSyn == null || "".equals(isSyn)) {
				json.setOk(true);
				json.setMessage("插入Translation数据成功！同步失败，原因：获取同步标识失败！");
				return json;
			} else {
				if (isSyn.equals("Y")) {
					specificationService.updateSpecificationByEnName(sTranslation.getChName(),
							sTranslation.getEnName());
					json.setOk(true);
					json.setMessage("插入Translation数据成功！同步成功！");
					return json;
				} else {
					json.setOk(true);
					json.setMessage("插入Translation数据成功！");
					return json;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("插入失败,原因：" + e.getMessage());
			return json;
		}

	}

	@RequestMapping(value = "/updateTranslation")
	@ResponseBody
	public JsonResult updateTranslation(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			SpecificationTranslation sTranslation = getSpecificationTranslation(request);
			if (sTranslation.getId() == 0) {
				json.setOk(false);
				json.setMessage("获取translationId失败！");
				return json;
			}
			specificationService.updateTranslation(sTranslation);

			String isSyn = request.getParameter("isSyn");
			if (isSyn == null || "".equals(isSyn)) {
				json.setOk(true);
				json.setMessage("修改Translation数据成功！同步失败，原因：获取同步标识失败！");
				return json;
			} else {
				if (isSyn.equals("Y")) {
					specificationService.updateSpecificationByEnName(sTranslation.getChName(),
							sTranslation.getEnName());
					json.setOk(true);
					json.setMessage("修改Translation数据成功！同步成功！");
					return json;
				} else {
					json.setOk(true);
					json.setMessage("修改Translation数据成功！");
					return json;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("修改失败,原因：" + e.getMessage());
			return json;
		}

	}

	@RequestMapping(value = "/deleteTranslationById")
	@ResponseBody
	public JsonResult deleteTranslationById(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String idsStr = request.getParameter("translationIds");
			if (idsStr == null || "".equals(idsStr)) {
				json.setOk(false);
				json.setMessage("获取translationIds失败！");
				return json;
			}
			String[] idLst = idsStr.split(",");
			if (idLst.length > 0) {
				for (String id : idLst) {
					specificationService.deleteTranslation(Integer.valueOf(id));
					specificationService.deleteMappingByTranslationId(Integer.valueOf(id));
				}
				json.setOk(true);
				if (idLst.length == 1) {
					json.setMessage("删除Translation数据成功！");
				} else {
					json.setMessage("批量删除Translation数据成功！");
				}
				return json;
			} else {
				json.setOk(true);
				json.setMessage("获取translationId失败！");
				return json;
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("删除失败,原因：" + e.getMessage());
			return json;
		}

	}

	private SpecificationTranslation getSpecificationTranslation(HttpServletRequest request) {
		SpecificationTranslation sTranslation = new SpecificationTranslation();
		String id = request.getParameter("id");
		if (!(id == null || "".equals(id))) {
			sTranslation.setId(Integer.valueOf(id));
		}
		String chName = request.getParameter("chName");
		if (!(chName == null || "".equals(chName))) {
			// 全部替换中文逗号到英文逗号
			sTranslation.setChName(chName.replaceAll("，", ","));
		}
		String enName = request.getParameter("enName");
		if (!(enName == null || "".equals(enName))) {
			sTranslation.setEnName(enName);
		}
		String type = request.getParameter("type");
		if (!(type == null || "".equals(type))) {
			sTranslation.setType(Integer.valueOf(type));
		}
		String productCategoryId = request.getParameter("productCategoryId");
		if (!(productCategoryId == null || "".equals(productCategoryId))) {
			sTranslation.setProductCategoryId(productCategoryId);
		}
		return sTranslation;
	}

	@RequestMapping(value = "/queryMappingByTranslationId")
	@ResponseBody
	public JsonResult queryMappingByTranslationId(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();

		try {
			String translationId = request.getParameter("translationId");
			if (translationId == null || "".equals(translationId)) {
				json.setOk(false);
				json.setMessage("获取translationId失败！");
				return json;
			}

			List<SpecificationMapping> sMappings = specificationService
					.queryMappingByTranslationId(Integer.valueOf(translationId));
			json.setOk(true);
			json.setData(sMappings);
			return json;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/queryMappingById")
	@ResponseBody
	public JsonResult queryMappingById(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String mappingId = request.getParameter("mappingId");
			if (mappingId == null || "".equals(mappingId)) {
				json.setOk(false);
				json.setMessage("获取mappingId失败！");
				return json;
			}
			SpecificationMapping sMapping = specificationService.queryMappingById(Integer.valueOf(mappingId));
			json.setOk(true);
			json.setData(sMapping);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/insertMapping")
	@ResponseBody
	public JsonResult insertMapping(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			SpecificationMapping sMapping = getSpecificationMapping(request);
			specificationService.insertMapping(sMapping);

			String isSyn = request.getParameter("isSyn");
			if (isSyn == null || "".equals(isSyn)) {
				json.setOk(true);
				json.setMessage("插入Mapping数据成功！同步失败，原因：获取同步标识失败！");
				return json;
			} else {
				if (isSyn.equals("Y")) {
					specificationService.updateMappingByEnName(sMapping.getChName(), sMapping.getEnName());
					json.setOk(true);
					json.setMessage("插入Mapping数据成功！同步成功！");
					return json;
				} else {
					json.setOk(true);
					json.setMessage("插入Mapping数据成功！");
					return json;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("插入失败,原因：" + e.getMessage());
			return json;
		}

	}

	@RequestMapping(value = "/updateMapping")
	@ResponseBody
	public JsonResult updateMapping(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			SpecificationMapping sMapping = getSpecificationMapping(request);
			if (sMapping.getId() == 0) {
				json.setOk(false);
				json.setMessage("获取mappingId失败！");
				return json;
			}
			specificationService.updateMapping(sMapping);

			String isSyn = request.getParameter("isSyn");
			if (isSyn == null || "".equals(isSyn)) {
				json.setOk(true);
				json.setMessage("修改Mapping数据成功！同步失败，原因：获取同步标识失败！");
				return json;
			} else {
				if (isSyn.equals("Y")) {
					specificationService.updateMappingByEnName(sMapping.getChName(), sMapping.getEnName());
					json.setOk(true);
					json.setMessage("修改Mapping数据成功！同步成功！");
					return json;
				} else {
					json.setOk(true);
					json.setMessage("修改Mapping数据成功！");
					return json;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("修改失败,原因：" + e.getMessage());
			return json;
		}

	}

	@RequestMapping(value = "/deleteMapping")
	@ResponseBody
	public JsonResult deleteMapping(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String ids = request.getParameter("ids");
			if (ids == null || "".equals(ids)) {
				json.setOk(false);
				json.setMessage("获取mappingIds失败！");
				return json;
			}
			String[] idLst = ids.split(",");
			if (idLst.length > 0) {
				for (String id : idLst) {
					specificationService.deleteMappingById(Integer.valueOf(id));
				}
				json.setOk(true);
				if (idLst.length == 1) {
					json.setMessage("删除Mapping数据成功！");
				} else {
					json.setMessage("批量删除Mapping数据成功！");
				}
				return json;
			} else {
				json.setOk(true);
				json.setMessage("获取mappingId失败！");
				return json;
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("删除失败,原因：" + e.getMessage());
			return json;
		}

	}

	private SpecificationMapping getSpecificationMapping(HttpServletRequest request) {
		SpecificationMapping sMapping = new SpecificationMapping();
		String id = request.getParameter("id");
		if (!(id == null || "".equals(id))) {
			sMapping.setId(Integer.valueOf(id));
		}
		String chName = request.getParameter("chName");
		if (!(chName == null || "".equals(chName))) {
			// 全部替换中文逗号到英文逗号
			sMapping.setChName(chName.replaceAll("，", ","));
		}
		String enName = request.getParameter("enName");
		if (!(enName == null || "".equals(enName))) {
			sMapping.setEnName(enName);
		}
		String productCategoryId = request.getParameter("productCategoryId");
		if (!(productCategoryId == null || "".equals(productCategoryId))) {
			sMapping.setProductCategoryId(productCategoryId);
		}
		String specificationId = request.getParameter("specificationId");
		if (!(specificationId == null || "".equals(specificationId))) {
			sMapping.setSpecificationId(specificationId);
		}
		return sMapping;
	}

	@RequestMapping(value = "/queryTranslationEnName")
	@ResponseBody
	public JsonResult queryTranslationEnName(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			List<String> enNameLst = new ArrayList<String>();
			enNameLst = specificationService.queryTranslationEnName();
			json.setOk(true);
			json.setData(enNameLst);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/updateSpecificationByEnName")
	@ResponseBody
	public JsonResult updateSpecificationByEnName(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String enName = request.getParameter("enName");
			String chName = request.getParameter("chName");
			if ((enName == null || "".equals(enName)) || (chName == null || "".equals(chName))) {
				json.setOk(false);
				json.setMessage("获取失败,中文或英文字符失败");
			}
			specificationService.updateSpecificationByEnName(chName, enName);
			json.setOk(true);
			json.setMessage("更新成功");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/queryMappingEnName")
	@ResponseBody
	public JsonResult queryMappingEnName(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			List<String> enNameLst = new ArrayList<String>();
			enNameLst = specificationService.queryMappingEnName();
			json.setOk(true);
			json.setData(enNameLst);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

	@RequestMapping(value = "/updateMappingByEnName")
	@ResponseBody
	public JsonResult updateMappingByEnName(HttpServletRequest request, HttpServletResponse response) {
		JsonResult json = new JsonResult();
		try {
			String enName = request.getParameter("enName");
			String chName = request.getParameter("chName");
			if ((enName == null || "".equals(enName)) || (chName == null || "".equals(chName))) {
				json.setOk(false);
				json.setMessage("获取失败,中文或英文字符失败");
			}
			specificationService.updateMappingByEnName(chName, enName);
			json.setOk(true);
			json.setMessage("更新成功");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("获取失败,原因：" + e.getMessage());
			return json;
		}
	}

}
