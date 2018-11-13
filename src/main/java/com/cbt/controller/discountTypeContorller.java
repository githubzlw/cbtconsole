
package com.cbt.controller;

import com.cbt.service.IDiscountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/discountType")
public class discountTypeContorller {
	@Autowired
	private IDiscountTypeService discountTypeService;
	

	@RequestMapping("queryForAliCategory")
	public String queryForAliCategory(Model model, String cid, String category){
		
		List<HashMap<String, String>>  AliCategorylist = discountTypeService.queryForAliCategory(cid, category);
		 model.addAttribute("cid", cid);
		 model.addAttribute("category", category);
		 model.addAttribute("aliCategoryList", AliCategorylist);
	     return "discountType";
	}

	/**
	 * 类别折扣上限修改
	 */
	@RequestMapping("updateDiscountTypeByCid")
	@ResponseBody
	public int updateDiscountTypeByCid(String cid,String category,double maxDiscount) {
		int rows = discountTypeService.updateDiscountTypeByCid(cid,category, maxDiscount);
		return rows;
	}

}
