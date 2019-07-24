package com.importExpress.thread;

import com.cbt.bean.CustomGoodsPublish;
import com.cbt.bean.ShopGoodsInfo;
import com.cbt.customer.service.IShopUrlService;
import com.cbt.service.CustomGoodsService;
import com.cbt.util.GoodsInfoUtils;
import com.importExpress.pojo.GoodsMd5Bean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异步删除图片MD5信息
 */
public class DeleteImgByMd5Thread extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(DeleteImgByMd5Thread.class);

    private String pid;
    private int adminId;
    private String shopId;
    private String url;
    private CustomGoodsService customGoodsService;
    private IShopUrlService shopUrlService;


    public DeleteImgByMd5Thread(String pid, int adminId, String shopId, String url,
                                CustomGoodsService customGoodsService, IShopUrlService shopUrlService) {
        this.pid = pid;
        this.adminId = adminId;
        this.shopId = shopId;
        this.url = url;
        this.customGoodsService = customGoodsService;
        this.shopUrlService = shopUrlService;
    }

    @Override
    public void run() {
        try {
            String remotePath = GoodsInfoUtils.changeRemotePathToLocal(url, 0);
            List<GoodsMd5Bean> md5BeanList = customGoodsService.queryMd5ImgByUrlList(pid, remotePath, shopId);
            List<String> pidList = new ArrayList<>(md5BeanList.size());
            Map<String, List<GoodsMd5Bean>> pidMap = new HashMap<>();
            List<ShopGoodsInfo> deleteGoodsInfos = new ArrayList<>();
            for (GoodsMd5Bean md5Bean : md5BeanList) {
                pidList.add(md5Bean.getPid());
                if (pidMap.containsKey(md5Bean.getPid())) {
                    pidMap.get(md5Bean.getPid()).add(md5Bean);
                } else {
                    List<GoodsMd5Bean> childList = new ArrayList<>();
                    childList.add(md5Bean);
                    pidMap.put(md5Bean.getPid(), childList);
                }
                ShopGoodsInfo delGd = new ShopGoodsInfo();
                delGd.setShopId(shopId);
                delGd.setPid(md5Bean.getPid());
                delGd.setImgUrl(md5Bean.getRemotePath().substring(md5Bean.getRemotePath().indexOf(md5Bean.getPid()) + 1));
                delGd.setLocalPath(md5Bean.getLocalPath());
                delGd.setRemotePath(GoodsInfoUtils.changeLocalPathToRemotePath(md5Bean.getRemotePath()));
                deleteGoodsInfos.add(delGd);
            }
            List<CustomGoodsPublish> goodsList = customGoodsService.queryGoodsByPidList(pidList);
            for (CustomGoodsPublish gd : goodsList) {
                if (pidMap.containsKey(gd.getPid())) {
                    deleteAndUpdateGoodsImg(gd, pidMap.get(gd.getPid()));
                }
            }
            // 更新回收标识
            customGoodsService.updateMd5ImgDeleteFlag(pid, remotePath, shopId);
            // 插入公共图片表中
            shopUrlService.insertShopGoodsDeleteImgs(deleteGoodsInfos, adminId);
            md5BeanList.clear();
            pidList.clear();
            pidMap.clear();
            goodsList.clear();
            deleteGoodsInfos.clear();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("pid:" + pid + ",url:" + url + ",deleteImgByMd5 error:", e);
        }

    }

    private void deleteAndUpdateGoodsImg(CustomGoodsPublish gd, List<GoodsMd5Bean> md5BeanList) {
        try {

            Document nwDoc = Jsoup.parseBodyFragment(gd.getEninfo());
            // 移除所有的页面效果 kse标签,实际div
            Elements imgEls = nwDoc.getElementsByTag("img");
            for (Element imgEl : imgEls) {
                for (GoodsMd5Bean md5Bean : md5BeanList) {
                    if (md5Bean.getRemotePath().contains(imgEl.attr("src"))) {
                        imgEl.remove();
                    }
                }
            }
            gd.setEninfo(nwDoc.html());
            customGoodsService.updatePidEnInfo(gd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            logger.error("pid:" + gd.getPid() + ",deleteAndUpdateGoodsImg error:", e);
        }
    }

}
