package com.importExpress.mapper;

import com.importExpress.pojo.ShopUrlAuthorizedInfoPO;
import com.importExpress.pojo.TabSeachPageBean;
import com.importExpress.pojo.TabSeachPagesDetailBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TabSeachPageMapper {
	public int insert(TabSeachPageBean bean);
	
	public List<TabSeachPageBean> list(@Param("id") int id, @Param("webSite") int webSite);

	public int delete(@Param("id") Integer id);

	public TabSeachPageBean get(@Param("id") Integer id);

	public int update(TabSeachPageBean bean);

	public List<Map<String,Object>> aliCategory();

	public int insertDetail(TabSeachPagesDetailBean bean);

	public List<TabSeachPagesDetailBean> detailList(@Param("sid") Integer sid);

	public int updateDetail(TabSeachPagesDetailBean bean);

	public int deleteDetail(@Param("id") Integer id);

	public TabSeachPagesDetailBean getDetail(@Param("id") Integer id);

	public int getWordsCount(@Param("keyword") String keyword);

	public int getWordsCount1(@Param("keyword") String keyword, @Param("id") Integer id);

	public int getNameCount(@Param("name") String name, @Param("sid") Integer sid);

	public int getNameCount1(@Param("name") String name, @Param("sid") Integer sid, @Param("id") Integer id);

	public int updateIsshow(@Param("isshow") Integer isshow, @Param("id") Integer id);

	public Integer getCategoryId(@Param("keyword") String keyword);

	public void move(@Param("id") int id);

	public long insertAuthorizedInfo(ShopUrlAuthorizedInfoPO bean);

    long updateAuthorizedInfo(ShopUrlAuthorizedInfoPO bean);

	public ShopUrlAuthorizedInfoPO queryAuthorizedInfo(@Param("shopId") String shopId);

	public List<TabSeachPageBean> queryStaticizeAll();

    long updateAuthorizedInfoValid(@Param("shopId") String shopId, @Param("valid") int valid);

    void updateShopBrand(ShopUrlAuthorizedInfoPO bean);

    String queryShopBrand(@Param("shopId") String shopId);

    int updateSeachPagesSite(@Param("website") int website, @Param("id") int id);

    int updateSeachPagesSate(@Param("id") int id, @Param("sate") int sate);

    int deleteCate(@Param("id") int id);

    int deleteBanner(@Param("id") int id);
}
