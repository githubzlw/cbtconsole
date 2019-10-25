package com.importExpress.controller;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext-base.xml", "classpath:applicationContext-shiro.xml", "classpath:spring-mvc.xml", "classpath:SqlMapConfig.xml", "classpath:applicationContext-mail.xml"})
public class RecommendCatalogControllerTest {
	@Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private MockMvc mockMvc2;
    private MockHttpSession session;
    /**
     * 设置登录用户
     * @throws Exception 
     */
    private void loginFirst() throws Exception {
        //设置登录信息
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/userLogin/checkUserInfoss.do")
                    .param("userName", "ling")
                    .param("passWord", "123"))
                    //.andExpect(view().name("redirect:/website/main_menu.jsp"))
                    .andExpect(status().isOk()).andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).build();
        loginFirst();
    }
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	/**
	 * 目录页面直接选中
	 * @throws Exception
	 */
	@Test
    public void catalogSave1() throws Exception {
        ResultActions resultActions = this.mockMvc
	            .perform(MockMvcRequestBuilders.post("/catalog/save")
	            		 .param("pid", "1002237335")
	                     .param("del", "0")
	                     .param("editid", "")
	                     .param("tem", "1")/*.session(session)*/);
	    resultActions.andExpect(MockMvcResultMatchers.status().isOk());
	    resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
	    /*{"product":"[{\"category\":\"Artificial plants\",\"catid\":\"1038172\",
	     * \"productCount\":1,\"products\":[{\"catid\":\"1038172\",
	     * \"img\":\"https://img.import-express.com/importcsvimg/shopimg/1002237335/4286707674_201525255.220x220.jpg\",
	     * \"name\":\"Imitation Rose Rose Curl Rose Rose Foam Rose Wedding Flower Wall Flower Arranging Flower Ball\",
	     * \"pid\":\"1002237335\",\"price\":\"0.07-0.08\",\"sold\":1612,\"unit\":\"piece\",
	     * \"url\":\"/goodsinfo/imitation-rose-rose-curl-rose-rose-foam-rose-wedding-flower-0-0-11002237335.html\"}]}]",
	     * "productSize":1,"status":200}
        */
	
	}
	/**
	 * 目录页面直接删除
	 * @throws Exception
	 */
	@Test
	public void catalogSave2() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/save")
						.param("pid", "1002237335")
						.param("del", "1")
						.param("editid", "")
						.param("tem", "1")/*.session(session)*/);
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		//{"catid":"1038172","product":"","productSize":0,"pid":"1002237335","status":200}
	}
	/**
	 * 管理页面过来的编辑取消（已生成的目录里商品）
	 * @throws Exception
	 */
	@Test
	public void catalogSave3() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/save")
						.param("pid", "42492808766")
						.param("del", "1")
						.param("editid", "3")
						.param("tem", "1")/*.session(session)*/);
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		
		/*{"product":"[{\"category\":\"Stud earrings\",\"catid\":\"1037271\",\"productCount\":4,
		 * \"products\":[{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg\",
		 * \"name\":\"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package\",\"pid\":\"522071836030\",
		 * \"price\":\"1.30\",\"sold\":2507,\"unit\":\"pair\",\"url\":\"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html\"},
		 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg\",
		 * \"name\":\"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments\",\"pid\":\"545507571800\",
		 * \"price\":\"1.30\",\"sold\":1238,\"unit\":\"piece\",\"url\":\"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html\"},
		 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg3/543655711785/3748795668_1451029551.220x220.jpg\",
		 * \"name\":\"Popular Bee Ear Nail Alloy Electroplated Flat Hollow Bee-shaped Ear Nail Ear Needle Insect Earrings\",\"pid\":\"543655711785\",
		 * \"price\":\"0.37-0.47\",\"sold\":3065,\"unit\":\"pair\",\"url\":\"/goodsinfo/popular-bee-ear-nail-alloy-electroplated-flat-hollow-bee-shaped-0-0-1543655711785.html\"},
		 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/847618866/4195450582_1383456478.220x220.jpg\",
		 * \"name\":\"Korean Earrings Loving Pearl Earrings Refined And Simple Earrings Jewelry\",\"pid\":\"847618866\",\"price\":\"0.13-0.18\",\"sold\":990,
		 * \"unit\":\"pair\",\"url\":\"/goodsinfo/korean-earrings-loving-pearl-earrings-refined-and-simple-earrings-jewelr-0-0-1847618866.html\"}]},
		 * {\"category\":\"Ring\",\"catid\":\"122706005\",\"productCount\":1,\"products\":[{\"catid\":\"122706005\",
		 * \"img\":\"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg\",
		 * \"name\":\"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop\",\"pid\":\"521163142969\",\"price\":\"1.30\",
		 * \"sold\":413,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html\"}]}]",
		 * "productSize":2,"pid":"42492808766","status":200}
		 */
	}
	/**
	 * 管理页面过来的编辑选中（已生成的目录里商品）
	 * @throws Exception
	 */
	@Test
	public void catalogSave4() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/save")
						.param("pid", "42492808766")
						.param("del", "0")
						.param("editid", "3")
						.param("tem", "1")/*.session(session)*/);
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*{"product":"[{\"category\":\"Stud earrings\",\"catid\":\"1037271\",\"productCount\":5,\"products\":[{\"catid\":\"1037271\",
		 * \"img\":\"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg\",
		 * \"name\":\"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package\",
		 * \"pid\":\"522071836030\",\"price\":\"1.30\",\"sold\":2507,\"unit\":\"pair\",
		 * \"url\":\"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html\"},
		 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg\",
		 * \"name\":\"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments\",\"pid\":\"545507571800\",
		 * \"price\":\"1.30\",\"sold\":1238,\"unit\":\"piece\",
		 * \"url\":\"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html\"},
		 * {\"catid\":\"1037271\",
		 * \"img\":\"https://img.import-express.com/importcsvimg/coreimg3/543655711785/3748795668_1451029551.220x220.jpg\",
		 * \"name\":\"Popular Bee Ear Nail Alloy Electroplated Flat Hollow Bee-shaped Ear Nail Ear Needle Insect Earrings\",
		 * \"pid\":\"543655711785\",\"price\":\"0.37-0.47\",\"sold\":3065,\"unit\":\"pair\",
		 * \"url\":\"/goodsinfo/popular-bee-ear-nail-alloy-electroplated-flat-hollow-bee-shaped-0-0-1543655711785.html\"},
		 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/847618866/4195450582_1383456478.220x220.jpg\",
		 * \"name\":\"Korean Earrings Loving Pearl Earrings Refined And Simple Earrings Jewelry\",\"pid\":\"847618866\",\"price\":\"0.13-0.18\",
		 * \"sold\":990,\"unit\":\"pair\",\"url\":\"/goodsinfo/korean-earrings-loving-pearl-earrings-refined-and-simple-earrings-jewelr-0-0-1847618866.html\"},
		 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg\",
		 * \"name\":\"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching\",\"pid\":\"42492808766\",
		 * \"price\":\"0.13-0.17\",\"sold\":100353,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html\"}]},
		 * {\"category\":\"Ring\",\"catid\":\"122706005\",\"productCount\":1,\"products\":[{\"catid\":\"122706005\",
		 * \"img\":\"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg\",
		 * \"name\":\"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop\",\"pid\":\"521163142969\",
		 * \"price\":\"1.30\",\"sold\":413,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html\"}]}]",
		 * "productSize":2,"status":200}
		 */
	}
	/**
	 * 管理页面过来的编辑选中（不是已生成的目录里的商品）
	 * @throws Exception
	 */
	@Test
	public void catalogSave5() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/save")
						.param("pid", "1005613870")
						.param("del", "0")
						.param("editid", "3")
						.param("tem", "1")/*.session(session)*/);
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*{"product":"[{\"category\":\"Stud earrings\",\"catid\":\"1037271\",\"productCount\":5,
		 * \"products\":[{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg\",
		 * \"name\":\"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package\",\"pid\":\"522071836030\",
		 * \"price\":\"1.30\",\"sold\":2507,\"unit\":\"pair\",\"url\":\"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html\"},
		 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg\",
		 * \"name\":\"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments\",\"pid\":\"545507571800\",\"price\":\"1.30\",
		 * \"sold\":1238,\"unit\":\"piece\",\"url\":\"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html\"},
		 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg3/543655711785/3748795668_1451029551.220x220.jpg\",
		 * \"name\":\"Popular Bee Ear Nail Alloy Electroplated Flat Hollow Bee-shaped Ear Nail Ear Needle Insect Earrings\",\"pid\":\"543655711785\",
		 * \"price\":\"0.37-0.47\",\"sold\":3065,\"unit\":\"pair\",\"url\":\"/goodsinfo/popular-bee-ear-nail-alloy-electroplated-flat-hollow-bee-shaped-0-0-1543655711785.html\"},
		 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/847618866/4195450582_1383456478.220x220.jpg\",
		 * \"name\":\"Korean Earrings Loving Pearl Earrings Refined And Simple Earrings Jewelry\",\"pid\":\"847618866\",\"price\":\"0.13-0.18\",\"sold\":990,
		 * \"unit\":\"pair\",\"url\":\"/goodsinfo/korean-earrings-loving-pearl-earrings-refined-and-simple-earrings-jewelr-0-0-1847618866.html\"},
		 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg\",
		 * \"name\":\"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching\",\"pid\":\"42492808766\",
		 * \"price\":\"0.13-0.17\",\"sold\":100353,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html\"}]},
		 * {\"category\":\"Ring\",\"catid\":\"122706005\",\"productCount\":1,\"products\":[{\"catid\":\"122706005\",
		 * \"img\":\"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg\",
		 * \"name\":\"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop\",\"pid\":\"521163142969\",\"price\":\"1.30\",
		 * \"sold\":413,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html\"}]},
		 * {\"category\":\"Women's snow boots\",\"catid\":\"123666005\",\"productCount\":1,\"products\":[{\"catid\":\"123666005\",\"img\":\"https://img.import-express.com/importcsvimg/img/1003177319/1516604905_1852983350.220x220.jpg\",\"name\":\"Summer Breathable Mesh Sport Casual Female Shoes Thick Sole Loose Cake Sandal Shake Shoe Sandal Female Soft-soled Shoes\",\"pid\":\"1003177319\",\"price\":\"6.52-8.27\",\"sold\":7,\"unit\":\"pair\",\"url\":\"/goodsinfo/summer-breathable-mesh-sport-casual-female-shoes-thick-sole-loose-0-0-11003177319.html\"}]},{\"category\":\"Ice bag & ice bag & picnic bag\",\"catid\":\"1031741\",
		 * \"productCount\":1,\"products\":[{\"catid\":\"1031741\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg2/1005613870/603979210_207972962.220x220.jpg\",
		 * \"name\":\"Extra Large Allow Ice Bag Blade Junket Keep Fresh Bag Vehicle Ice Bag Ice Bag Summer Cool Bag\",\"pid\":\"1005613870\",\"price\":\"2.07-3.29\",
		 * \"sold\":15,\"unit\":\"piece\",\"url\":\"/goodsinfo/extra-large-allow-ice-bag-blade-junket-keep-fresh-bag-0-0-11005613870.html\"}]}]","productSize":4,"status":200}
		 */
	}
	/**
	 * 管理页面过来的编辑取消（不是已生成的目录里的商品）
	 * @throws Exception
	 */
	@Test
	public void catalogSave6() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/save")
						.param("pid", "1005613870")
						.param("del", "1")
						.param("editid", "3")
						.param("tem", "1")/*.session(session)*/);
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
	/*	{"catid":"1031741","product":"[{\"category\":\"Stud earrings\",\"catid\":\"1037271\",\"productCount\":5,
	 * \"products\":[{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg\",
	 * \"name\":\"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package\",\"pid\":\"522071836030\",
	 * \"price\":\"1.30\",\"sold\":2507,\"unit\":\"pair\",\"url\":\"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html\"},
	 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg\",
	 * \"name\":\"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments\",\"pid\":\"545507571800\",\"price\":\"1.30\",\"sold\":1238,
	 * \"unit\":\"piece\",\"url\":\"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html\"},{\"catid\":\"1037271\",
	 * \"img\":\"https://img.import-express.com/importcsvimg/coreimg3/543655711785/3748795668_1451029551.220x220.jpg\",
	 * \"name\":\"Popular Bee Ear Nail Alloy Electroplated Flat Hollow Bee-shaped Ear Nail Ear Needle Insect Earrings\",\"pid\":\"543655711785\",\"price\":\"0.37-0.47\",
	 * \"sold\":3065,\"unit\":\"pair\",\"url\":\"/goodsinfo/popular-bee-ear-nail-alloy-electroplated-flat-hollow-bee-shaped-0-0-1543655711785.html\"},
	 * {\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/847618866/4195450582_1383456478.220x220.jpg\",
	 * \"name\":\"Korean Earrings Loving Pearl Earrings Refined And Simple Earrings Jewelry\",\"pid\":\"847618866\",\"price\":\"0.13-0.18\",\"sold\":990,\"unit\":\"pair\",
	 * \"url\":\"/goodsinfo/korean-earrings-loving-pearl-earrings-refined-and-simple-earrings-jewelr-0-0-1847618866.html\"},{\"catid\":\"1037271\",
	 * \"img\":\"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg\",
	 * \"name\":\"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching\",\"pid\":\"42492808766\",\"price\":\"0.13-0.17\",
	 * \"sold\":100353,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html\"}]},
	 * {\"category\":\"Ring\",\"catid\":\"122706005\",\"productCount\":1,\"products\":[{\"catid\":\"122706005\",
	 * \"img\":\"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg\",
	 * \"name\":\"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop\",\"pid\":\"521163142969\",\"price\":\"1.30\",\"sold\":413,\"unit\":\"piece\",
	 * \"url\":\"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html\"}]},
	 * {\"category\":\"Women's snow boots\",
	 * \"catid\":\"123666005\",\"productCount\":1,\"products\":[{\"catid\":\"123666005\",
	 * \"img\":\"https://img.import-express.com/importcsvimg/img/1003177319/1516604905_1852983350.220x220.jpg\",
	 * \"name\":\"Summer Breathable Mesh Sport Casual Female Shoes Thick Sole Loose Cake Sandal Shake Shoe Sandal Female Soft-soled Shoes\",
	 * \"pid\":\"1003177319\",\"price\":\"6.52-8.27\",\"sold\":7,\"unit\":\"pair\",
	 * \"url\":\"/goodsinfo/summer-breathable-mesh-sport-casual-female-shoes-thick-sole-loose-0-0-11003177319.html\"}]}]",
	 * "productSize":3,"pid":"1005613870","status":200}
	 */
		
	}
	
	/**
	 * 目录生成页面预览按钮（之前从管理页面编辑过来的 然后点击预览）
	 * @throws Exception
	 */
	@Test
	public void product1() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/product")
						.param("isManag", "false")
						.param("id", "3"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*{"product":"[{\"category\":\"Stud earrings\",\"catid\":\"1037271\",\"productCount\":5,\"products\":[{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg\",\"name\":\"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package\",\"pid\":\"522071836030\",\"price\":\"1.30\",\"sold\":2507,\"unit\":\"pair\",\"url\":\"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html\"},{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg\",\"name\":\"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments\",\"pid\":\"545507571800\",\"price\":\"1.30\",\"sold\":1238,\"unit\":\"piece\",\"url\":\"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html\"},{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg3/543655711785/3748795668_1451029551.220x220.jpg\",\"name\":\"Popular Bee Ear Nail Alloy Electroplated Flat Hollow Bee-shaped Ear Nail Ear Needle Insect Earrings\",\"pid\":\"543655711785\",\"price\":\"0.37-0.47\",\"sold\":3065,\"unit\":\"pair\",\"url\":\"/goodsinfo/popular-bee-ear-nail-alloy-electroplated-flat-hollow-bee-shaped-0-0-1543655711785.html\"},{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/847618866/4195450582_1383456478.220x220.jpg\",\"name\":\"Korean Earrings Loving Pearl Earrings Refined And Simple Earrings Jewelry\",\"pid\":\"847618866\",\"price\":\"0.13-0.18\",\"sold\":990,\"unit\":\"pair\",\"url\":\"/goodsinfo/korean-earrings-loving-pearl-earrings-refined-and-simple-earrings-jewelr-0-0-1847618866.html\"},{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg\",\"name\":\"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching\",\"pid\":\"42492808766\",\"price\":\"0.13-0.17\",\"sold\":100353,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html\"}]},{\"category\":\"Ring\",\"catid\":\"122706005\",\"productCount\":1,\"products\":[{\"catid\":\"122706005\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg\",\"name\":\"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop\",\"pid\":\"521163142969\",\"price\":\"1.30\",\"sold\":413,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html\"}]},{\"category\":\"Women's snow boots\",\"catid\":\"123666005\",\"productCount\":1,\"products\":[{\"catid\":\"123666005\",\"img\":\"https://img.import-express.com/importcsvimg/img/1003177319/1516604905_1852983350.220x220.jpg\",\"name\":\"Summer Breathable Mesh Sport Casual Female Shoes Thick Sole Loose Cake Sandal Shake Shoe Sandal Female Soft-soled Shoes\",\"pid\":\"1003177319\",\"price\":\"6.52-8.27\",\"sold\":7,\"unit\":\"pair\",\"url\":\"/goodsinfo/summer-breathable-mesh-sport-casual-female-shoes-thick-sole-loose-0-0-11003177319.html\"}]}]","productSize":3,"status":200}
		 */
		
	}
	/**
	 * 管理页面点击预览
	 * @throws Exception
	 */
	@Test
	public void product2() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/product")
						.param("isManag", "true")
						.param("id", "3"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*{"product":"[{\"category\":\"Stud earrings\",\"catid\":\"1037271\",\"productCount\":2,\"products\":[{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg\",\"name\":\"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching\",\"pid\":\"42492808766\",\"price\":\"2.13\",\"sold\":100353,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html\"},{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg\",\"name\":\"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package\",\"pid\":\"522071836030\",\"price\":\"1.30\",\"sold\":2507,\"unit\":\"pair\",\"url\":\"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html\"}]}]","productSize":1,"status":200}
		 */
		
	}
	/**
	 * 目录页面点击预览
	 * @throws Exception
	 */
	@Test
	public void product3() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/product"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*{"product":"[{\"category\":\"Stud earrings\",\"catid\":\"1037271\",\"productCount\":2,\"products\":[{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg\",\"name\":\"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching\",\"pid\":\"42492808766\",\"price\":\"2.13\",\"sold\":100353,\"unit\":\"piece\",\"url\":\"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html\"},{\"catid\":\"1037271\",\"img\":\"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg\",\"name\":\"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package\",\"pid\":\"522071836030\",\"price\":\"1.30\",\"sold\":2507,\"unit\":\"pair\",\"url\":\"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html\"}]}]","productSize":1,"status":200}
		 */
		
	}
	/**
	 * 管理页面点击预览
	 * @throws Exception
	 */
	@Test
	public void product4() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/product")
						.param("isManag", "true")
						.param("id", "d"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*{"message":"数据不存在！！！","status":100}*/
		
	}
	/**
	 * 管理页面点击预览
	 * @throws Exception
	 */
	@Test
	public void product5() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/product")
						.param("isManag", "true")
						.param("id", "0"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*{"message":"数据不存在！！！","status":101}*/
		
	}
	/**
	 * 生成目录
	 * @throws Exception
	 */
	@Test
	public void catalogCreate1() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/create")
						.param("tem", "1")
						.param("catalogname", "junittest")
						.param("id", "0"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*mq {"message":"生成目录失败！！！","status":102}*/
		/*{"addCatelog":1,"status":200}*/
	}
	/**
	 * 更新目录
	 * @throws Exception
	 */
	@Test
	public void catalogCreate2() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/create")
						.param("tem", "1")
						.param("catalogname", "junittest2")
						.param("id", "3"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*mq {"message":"生成目录失败！！！","status":102}*/
		/*{"addCatelog":1,"status":200}*/
	}
	
	/**
	 * 一键清空目录
	 * @throws Exception
	 */
	@Test
	public void catalogClear() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/clear"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*{"status":200}*/
	}
	
	/**
	 * 删除目录
	 * @throws Exception
	 */
	@Test
	public void catalogDelete() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/delete").param("id", "12"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/*{"status":200}*/
	}
	/**
	 * 目录列表
	 * @throws Exception
	 */
	@Test
	public void catalogList() throws Exception {
		ResultActions resultActions = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/catalog/list")
						.param("template", "0").param("page", "1"));
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
		/* [RecommendCatalog(id=2, catalogName=tesssst, template=1, createAdmin=ling, createTime=2019-09-26 17:16:19.0, productCount=6, 
		 * productList=[{"category":"Stud earrings","catid":"1037271","productCount":5,"products":[{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg","name":"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching","pid":"42492808766","price":"2.13","sold":100353,"unit":"piece","url":"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg","name":"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package","pid":"522071836030","price":"1.30","sold":2507,"unit":"pair","url":"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg","name":"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments","pid":"545507571800","price":"1.30","sold":1238,"unit":"piece","url":"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/coreimg3/543655711785/3748795668_1451029551.220x220.jpg","name":"Popular Bee Ear Nail Alloy Electroplated Flat Hollow Bee-shaped Ear Nail Ear Needle Insect Earrings","pid":"543655711785","price":"0.37-0.47","sold":3065,"unit":"pair","url":"/goodsinfo/popular-bee-ear-nail-alloy-electroplated-flat-hollow-bee-shaped-0-0-1543655711785.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/847618866/4195450582_1383456478.220x220.jpg","name":"Korean Earrings Loving Pearl Earrings Refined And Simple Earrings Jewelry","pid":"847618866","price":"0.13-0.18","sold":990,"unit":"pair","url":"/goodsinfo/korean-earrings-loving-pearl-earrings-refined-and-simple-earrings-jewelr-0-0-1847618866.html"}]},{"category":"Ring","catid":"122706005","productCount":1,"products":[{"catid":"122706005","img":"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg","name":"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop","pid":"521163142969","price":"1.30","sold":413,"unit":"piece","url":"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html"}]}], catalogFile=null, status=1), RecommendCatalog(id=3, catalogName=tttt, template=1, createAdmin=ling, createTime=2019-09-26 17:16:19.0, productCount=2, productList=[{"category":"Stud earrings","catid":"1037271","productCount":2,"products":[{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg","name":"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching","pid":"42492808766","price":"2.13","sold":100353,"unit":"piece","url":"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg","name":"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package","pid":"522071836030","price":"1.30","sold":2507,"unit":"pair","url":"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html"}]}], catalogFile=null, status=1), RecommendCatalog(id=4, catalogName=test3, template=1, createAdmin=ling, createTime=2019-09-26 17:16:19.0, productCount=4, productList=[{"category":"Stud earrings","catid":"1037271","productCount":3,"products":[{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg","name":"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching","pid":"42492808766","sold":100353,"price":"2.13","unit":"piece","url":"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg","name":"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package","pid":"522071836030","sold":2507,"price":"1.30","unit":"pair","url":"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg","name":"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments","pid":"545507571800","sold":1238,"price":"1.30","unit":"piece","url":"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html"}]},{"category":"Ring","catid":"122706005","productCount":1,"products":[{"catid":"122706005","img":"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg","name":"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop","pid":"521163142969","sold":413,"price":"1.30","unit":"piece","url":"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html"}]}], catalogFile=null, status=1), RecommendCatalog(id=5, catalogName=test4, template=1, createAdmin=ling, createTime=2019-09-26 17:16:19.0, productCount=4, productList=[{"category":"Stud earrings","catid":"1037271","productCount":3,"products":[{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg","name":"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching","pid":"42492808766","sold":100353,"price":"2.13","unit":"piece","url":"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg","name":"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package","pid":"522071836030","sold":2507,"price":"1.30","unit":"pair","url":"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg","name":"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments","pid":"545507571800","sold":1238,"price":"1.30","unit":"piece","url":"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html"}]},{"category":"Ring","catid":"122706005","productCount":1,"products":[{"catid":"122706005","img":"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg","name":"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop","pid":"521163142969","sold":413,"price":"1.30","unit":"piece","url":"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html"}]}], catalogFile=null, status=1), RecommendCatalog(id=6, catalogName=test5, template=1, createAdmin=ling, createTime=2019-09-26 17:16:19.0, productCount=4, productList=[{"category":"Stud earrings","catid":"1037271","productCount":3,"products":[{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg","name":"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching","pid":"42492808766","sold":100353,"price":"2.13","unit":"piece","url":"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg","name":"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package","pid":"522071836030","sold":2507,"price":"1.30","unit":"pair","url":"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg","name":"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments","pid":"545507571800","sold":1238,"price":"1.30","unit":"piece","url":"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html"}]},{"category":"Ring","catid":"122706005","productCount":1,"products":[{"catid":"122706005","img":"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg","name":"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop","pid":"521163142969","sold":413,"price":"1.30","unit":"piece","url":"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html"}]}], catalogFile=null, status=1), RecommendCatalog(id=7, catalogName=test6, template=1, createAdmin=ling, createTime=2019-09-26 17:16:19.0, productCount=4, productList=[{"category":"Stud earrings","catid":"1037271","productCount":3,"products":[{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg","name":"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching","pid":"42492808766","sold":100353,"price":"2.13","unit":"piece","url":"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg","name":"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package","pid":"522071836030","sold":2507,"price":"1.30","unit":"pair","url":"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg","name":"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments","pid":"545507571800","sold":1238,"price":"1.30","unit":"piece","url":"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html"}]},{"category":"Ring","catid":"122706005","productCount":1,"products":[{"catid":"122706005","img":"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg","name":"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop","pid":"521163142969","sold":413,"price":"1.30","unit":"piece","url":"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html"}]}], catalogFile=null, status=1), RecommendCatalog(id=8, catalogName=test7, template=1, createAdmin=ling, createTime=2019-09-26 17:16:19.0, productCount=4, productList=[{"category":"Stud earrings","catid":"1037271","productCount":3,"products":[{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg","name":"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching","pid":"42492808766","sold":100353,"price":"2.13","unit":"piece","url":"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/522071836030/2467864836_1208020529.220x220.jpg","name":"Diy Korea Ear Jewelry Accessories Full Diamond Hearts Love Circular Earring Pendant Earrings Material Package","pid":"522071836030","sold":2507,"price":"1.30","unit":"pair","url":"/goodsinfo/diy-korea-ear-jewelry-accessories-full-diamond-hearts-love-circular-0-0-1522071836030.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg","name":"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments","pid":"545507571800","sold":1238,"price":"1.30","unit":"piece","url":"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html"}]},{"category":"Ring","catid":"122706005","productCount":1,"products":[{"catid":"122706005","img":"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg","name":"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop","pid":"521163142969","sold":413,"price":"1.30","unit":"piece","url":"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html"}]}], catalogFile=null, status=1), RecommendCatalog(id=9, catalogName=yulanshegcheng, template=1, createAdmin=ling, createTime=2019-09-27 11:43:12.0, productCount=3, productList=[{"category":"Stud earrings","catid":"1037271","productCount":2,"products":[{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg","name":"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching","pid":"42492808766","price":"0.13-0.17","sold":100353,"unit":"piece","url":"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg","name":"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments","pid":"545507571800","price":"0.17-0.22","sold":1238,"unit":"piece","url":"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html"}]},{"category":"Ring","catid":"122706005","productCount":1,"products":[{"catid":"122706005","img":"https://img.import-express.com/importcsvimg/coreimg3/521163142969/2373993687_2130544268.220x220.jpg","name":"Korean Revolution Chao Fan Pearl Three Rings Female Ring Ring 10 Yuan Shop","pid":"521163142969","price":"0.15-0.21","sold":413,"unit":"piece","url":"/goodsinfo/korean-revolution-chao-fan-pearl-three-rings-female-ring-ring-0-0-1521163142969.html"}]}], catalogFile=null, status=1), RecommendCatalog(id=10, catalogName=rt, template=1, createAdmin=ling, createTime=2019-09-27 11:45:43.0, productCount=2, productList=[{"category":"Hair Ring","catid":"122710002","productCount":1,"products":[{"catid":"122710002","img":"https://img.import-express.com/importcsvimg/coreimg3/551251976674/6138011987_1683847999.220x220.jpg","name":"Golden Bead Hair Loop Hair Ornament Hair Tie Hair Band 2 Yuan Store Headwear","pid":"551251976674","price":"0.01","sold":676974,"unit":"piece","url":"/goodsinfo/golden-bead-hair-loop-hair-ornament-hair-tie-hair-band-0-0-1551251976674.html"}]},{"category":"Bracelets","catid":"1037268","productCount":1,"products":[{"catid":"1037268","img":"https://img.import-express.com/importcsvimg/img/40972241499/1659491060_284580921.220x220.jpg","name":"925 Silver-plated Bracelet Round Box Bracelet Xianmei Jewelry","pid":"40972241499","price":"0.79","sold":683,"unit":"piece","url":"/goodsinfo/925-silver-plated-bracelet-round-box-bracelet-xianmei-jewelr-0-0-140972241499.html"}]}], catalogFile=null, status=1), RecommendCatalog(id=11, catalogName=ty, template=1, createAdmin=ling, createTime=2019-09-27 14:36:37.0, productCount=3, productList=[{"category":"Stud earrings","catid":"1037271","productCount":2,"products":[{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/coreimg1/42492808766/3087172784_1327870113.220x220.jpg","name":"Korean Version Of The Double-sided Cute Imitation Pearl Size Ball Earrings Fashion Candy Color Stitching","pid":"42492808766","price":"0.13-0.17","sold":100353,"unit":"piece","url":"/goodsinfo/korean-version-of-the-double-sided-cute-imitation-pearl-size-0-0-142492808766.html"},{"catid":"1037271","img":"https://img.import-express.com/importcsvimg/img/545507571800/3872137893_876436393.220x220.jpg","name":"Golden Best-selling Crystal Pseudo-nose Ring And Pseudo-nose Nail Piercing Nose Ornaments","pid":"545507571800","price":"0.17-0.22","sold":1238,"unit":"piece","url":"/goodsinfo/golden-best-selling-crystal-pseudo-nose-ring-and-pseudo-nose-0-0-1545507571800.html"}]},{"category":"Flannel bag","catid":"1047297","productCount":1,"products":[{"catid":"1047297","img":"https://img.import-express.com/importcsvimg/shopimg/45700861009/3749604095_1450011339.220x220.jpg","name":"Flocking Bags 5*7 Trumpet Fingertip Gyroscope Packaging Bags Jewelry Collection Bags Customized","pid":"45700861009","price":"0.06-0.07","sold":0,"unit":"piece","url":"/goodsinfo/flocking-bags-5-7-trumpet-fingertip-gyroscope-packaging-bags-jewelry-0-0-145700861009.html"}]}], catalogFile=null, status=1)]
 */
	}
	
	
	
}
