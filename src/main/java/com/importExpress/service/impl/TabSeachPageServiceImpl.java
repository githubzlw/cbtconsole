package com.importExpress.service.impl;

import com.importExpress.mapper.TabSeachPageMapper;
import com.importExpress.pojo.ShopUrlAuthorizedInfoPO;
import com.importExpress.pojo.TabSeachPageBean;
import com.importExpress.pojo.TabSeachPagesDetailBean;
import com.importExpress.service.TabSeachPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TabSeachPageServiceImpl implements TabSeachPageService {
	
	@Autowired
	private TabSeachPageMapper tabSeachPageMapper;

	@Transactional
	@Override
	public int insert(TabSeachPageBean bean) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.insert(bean);
	}

	@Override
	public List<TabSeachPageBean> list(int id, int webSite) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.list(id, webSite);
	}

	@Override
	public int delete(Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.delete(id);
	}

	@Override
	public TabSeachPageBean get(Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.get(id);
	}

	@Override
	public int update(TabSeachPageBean bean) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.update(bean);
	}

	@Override
	public List<Map<String, Object>> aliCategory() {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.aliCategory();
	}

	@Override
	public int insertDetail(TabSeachPagesDetailBean bean) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.insertDetail(bean);
	}

	@Override
	public List<TabSeachPagesDetailBean> detailList(Integer sid) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.detailList(sid);
	}

    @Override
    public boolean updateTitleAndKey(int sid) {
        List<TabSeachPagesDetailBean> detailList = detailList(sid);
        if (detailList == null || detailList.size() == 0){
            return false;
        }
        TabSeachPageBean tabSeachPageBean = get(sid);
        String title = "Promotion " + tabSeachPageBean.getKeyword() + " Shopping Festival-China Wholesale Online, Buying Chinese Products | our website";
        StringBuffer keywords = new StringBuffer();
        StringBuffer description = new StringBuffer();
        for (TabSeachPagesDetailBean bean : detailList) {
            keywords.append(bean.getName().trim()).append(",");
            description.append(bean.getKeyword().replace(";", ",").replace("；", ",")).append(",");
            if (description.toString().indexOf(bean.getName().trim() + ",") == -1){
                description.append(bean.getName().trim()).append(",");
            }
        }
        tabSeachPageBean.setPageTitle(title);
        tabSeachPageBean.setPageKeywords(keywords.toString().substring(0,keywords.toString().length() - 1));
        tabSeachPageBean.setPageDescription("China Wholesale Online, Buying Chinese Products "
                + description.toString().substring(0, description.toString().length() - 1)
                + " Choose the best products from China, Pick coupons, Find your favorite products, Get the best prices!");
        update(tabSeachPageBean);
        return true;
    }

    @Override
	public int updateDetail(TabSeachPagesDetailBean bean) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.updateDetail(bean);
	}

	@Override
	public int deleteDetail(Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.deleteDetail(id);
	}

	@Override
	public TabSeachPagesDetailBean getDetail(Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getDetail(id);
	}

	@Override
	public int getWordsCount(String keyword) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getWordsCount(keyword);
	}
	
	@Override
	public int getWordsCount1(String keyword, Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getWordsCount1(keyword, id);
	}

	@Override
	public int getNameCount(String name,Integer sid) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getNameCount(name,sid);
	}

	@Override
	public int getNameCount1(String name, Integer sid, Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getNameCount1(name, sid, id);
	}

	@Override
	public int updateIsshow(Integer isshow, Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.updateIsshow(isshow, id);
	}

	@Override
	public Integer getCategoryId(String keyword) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getCategoryId(keyword);
	}

	@Override
	public void deleCate(int id) {
		//所有的移动到未分类下
		tabSeachPageMapper.move(id);
		//删除该分类节点
		tabSeachPageMapper.delete(id);
		
	}

	@Override
	public long updateAuthorizedInfo(ShopUrlAuthorizedInfoPO bean) {
	    tabSeachPageMapper.updateShopBrand(bean);
	    if (bean.getId() != null){
            return tabSeachPageMapper.updateAuthorizedInfo(bean);
        }
        return tabSeachPageMapper.insertAuthorizedInfo(bean);
	}

	@Override
	public ShopUrlAuthorizedInfoPO queryAuthorizedInfo(String shopId) {
	    String shopBrand = tabSeachPageMapper.queryShopBrand(shopId);
        ShopUrlAuthorizedInfoPO bean = tabSeachPageMapper.queryAuthorizedInfo(shopId);
        if (bean == null){
            bean = new ShopUrlAuthorizedInfoPO();
            bean.setShopId(shopId);
        }
        bean.setShopBrand(shopBrand);
        return bean;
	}

	@Override
	public List<TabSeachPageBean> queryStaticizeAll() {
		return tabSeachPageMapper.queryStaticizeAll();
	}

    @Override
    public long updateAuthorizedInfoValid(String shopId, int valid) {
        return tabSeachPageMapper.updateAuthorizedInfoValid(shopId, valid);
    }
}
