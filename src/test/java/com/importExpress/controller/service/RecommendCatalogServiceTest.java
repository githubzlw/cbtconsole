package com.importExpress.controller.service;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cbt.util.SerializeUtil;
import com.importExpress.pojo.CatalogProduct;
import com.importExpress.pojo.RecommendCatalog;
import com.importExpress.service.RecommendCatalogService;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})
public class RecommendCatalogServiceTest {
	@Autowired
	private RecommendCatalogService recommendCatalogService;
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	/**
	 * 获取产品数据
	 */
	@Test
	public void product() {
		String pids = "1000571063";
		int site = 1;
		CatalogProduct product = recommendCatalogService.product(pids, site);
		System.out.println(SerializeUtil.ObjToJson(product));
	}
	/**
	 * 生成目录
	 */
	@Test
	public void addCatelog() {
		RecommendCatalog catalog = new RecommendCatalog();
		catalog.setCatalogName("");
		catalog.setCreateAdmin("");
		catalog.setProductCount(3);
		catalog.setProductList("[{\"category\":\"Stud earrings\",\"catid\":\"1037271\",\"productCount\":2,\"products\":[{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg\",\"name\":\"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching\",\"pid\":\"42492808766\",\"price\":\"0.13-0.17\",\"sold\":100353,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html\"},{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg\",\"name\":\"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments\",\"pid\":\"545507571800\",\"price\":\"0.17-0.22\",\"sold\":1238,\"unit\":\"piece\",\"url\":\"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html\"}]},{\"category\":\"Ring\",\"catid\":\"122706005\",\"productCount\":1,\"products\":[{\"catid\":\"122706005\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg\",\"name\":\"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop\",\"pid\":\"521163142969\",\"price\":\"0.15-0.21\",\"sold\":413,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html\"}]}]");
		catalog.setStatus(1);
		catalog.setTemplate(1);
		int product = recommendCatalogService.addCatelog(catalog);
		System.out.println(product);
	}
	/**
	 * 更新目录
	 */
	@Test
	public void updateCatalog() {
		RecommendCatalog catalog = new RecommendCatalog();
		catalog.setId(4);
		catalog.setCatalogName("");
		catalog.setCreateAdmin("");
		catalog.setProductCount(3);
		catalog.setProductList("[{\"category\":\"Stud earrings\",\"catid\":\"1037271\",\"productCount\":2,\"products\":[{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg\",\"name\":\"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching\",\"pid\":\"42492808766\",\"price\":\"0.13-0.17\",\"sold\":100353,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html\"},{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg\",\"name\":\"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments\",\"pid\":\"545507571800\",\"price\":\"0.17-0.22\",\"sold\":1238,\"unit\":\"piece\",\"url\":\"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html\"}]},{\"category\":\"Ring\",\"catid\":\"122706005\",\"productCount\":1,\"products\":[{\"catid\":\"122706005\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg\",\"name\":\"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop\",\"pid\":\"521163142969\",\"price\":\"0.15-0.21\",\"sold\":413,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html\"}]}]");
		catalog.setStatus(1);
		catalog.setTemplate(1);
		int product = recommendCatalogService.updateCatalog(catalog);
		System.out.println(product);
	}
	/**
	 * 删除目录
	 */
	@Test
	public void deleteCatalog() {
		int product = recommendCatalogService.deleteCatalog(3);
		System.out.println(product);
	}
	/**
	 * 获取目录
	 */
	@Test
	public void catalogById() {
		RecommendCatalog product = recommendCatalogService.catalogById(3);
		System.out.println(SerializeUtil.ObjToJson(product));
	}
	/**
	 * 获取目录列表
	 * 目录名称无值
	 */
	@Test
	public void catalogList1() {
		List<RecommendCatalog> product = recommendCatalogService.catalogList(0, 1, null);
		System.out.println(SerializeUtil.ObjToJson(product));
	}
	/**
	 * 获取目录列表
	 * 目录名称有值
	 */
	@Test
	public void catalogList2() {
		List<RecommendCatalog> product = recommendCatalogService.catalogList(0, 1, "test3");
		System.out.println(SerializeUtil.ObjToJson(product));
	}

	
}
