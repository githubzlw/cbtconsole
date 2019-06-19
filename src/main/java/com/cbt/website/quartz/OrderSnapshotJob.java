package com.cbt.website.quartz;

import com.cbt.parse.service.DownloadMain;
import com.cbt.util.ContentConfig;
import com.cbt.warehouse.dao.OrderSnapshotDao;
import com.cbt.warehouse.dao.OrderSnapshotDaoImpl;
import com.cbt.warehouse.pojo.OrderSnapshot;

import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 
 * @ClassName OrderSnapshotJob
 * @Description 订单快照定时任务
 * @author jxw
 * @date 2018年1月10日
 */
public class OrderSnapshotJob implements Job {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderSnapshotJob.class);
//	private static final String SHOWURL = "http://192.168.1.29:8765/editimg/snapshot/";
	private static final String SHOWURL ="https://img.import-express.com/importcsvimg/snapshot/";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		System.out.println("OrderSnapshot begin");
		LOG.info("OrderSnapshot begin");
		OrderSnapshotDao snapshotDao = new OrderSnapshotDaoImpl();

		System.out.println("--syncOfflineToOnline begin");
		// 1.执行上次生成订单快照后的数据，同步线上，更新同步成功标识
		boolean isSc = snapshotDao.syncOfflineToOnline();
		if (!isSc) {
			System.err.println("this syncOfflineToOnline execute error");
			LOG.error("this syncOfflineToOnline execute error");
		}
		System.out.println("--syncOfflineToOnline end!!");
		LOG.info("--syncOfflineToOnline end!!");

		// 2.取出order_snapshot表的最大id
		int maxId = snapshotDao.queryMaxIdFromOrderSnapshot();
		System.out.println("--query maxId:" + maxId);
		LOG.info("--query maxId:" + maxId);
		if (maxId == 0) {
			 maxId = 256364;
//			maxId = 237500;
		}
		System.out.println("--actual maxId:" + maxId);
		LOG.info("--actual maxId:" + maxId);
		// 3.查询order_details表id大于order_snapshot表的最大id的数据
		List<OrderSnapshot> orderSnapshots = snapshotDao.queryForListByMaxId(maxId);
		System.out.println("--all orderSnapshots size:" + orderSnapshots.size());
		LOG.info("--all orderSnapshots size:" + orderSnapshots.size());

		if (!(orderSnapshots == null || orderSnapshots.size() == 0)) {
			// 4.判断是否存在阿里的商品，存在下载图片到服务器
			List<OrderSnapshot> newOrderSnapshots = new ArrayList<OrderSnapshot>();
			List<OrderSnapshot> goodOrderSnapshots = new ArrayList<OrderSnapshot>();

			for (OrderSnapshot spst : orderSnapshots) {
				// 判断是否是阿里商品的逻辑：根据goods_url来判断
				String goods_url = spst.getGoods_url();

				// 如果goods_url为空,是否使用数据拼凑数据生成url??
				if (goods_url == null || "".equals(goods_url)) {

				} else {
					// 1.goods_url老数据如：source=AFD48D969DE9FD96E939873DDE42D76CB，source=A开头的是阿里商品
					// /product/detail?&source=AFD48D969DE9FD96E939873DDE42D76CB&item=32769967834
					if (goods_url.indexOf("?") > -1 && goods_url.indexOf(".html") == -1
							&& goods_url.indexOf("source=") > -1) {
						String[] urlLst = goods_url.substring(goods_url.indexOf("?")).split("&");
						for (String eqStr : urlLst) {
							String[] pamLst = eqStr.split("=");
							if (pamLst.length == 2 && "source".equals(pamLst[0]) && pamLst[1].indexOf("A") == 0) {
								newOrderSnapshots.add(spst);
								break;
							}
							pamLst = null;
						}
						urlLst = null;
					} else if (goods_url.indexOf("http://www.aliexpress.com") > -1 && goods_url.indexOf(".html") > -1) {
						// 2.goods_url以http://www.aliexpress.com/开头的是阿里商品，如：
						// http://www.aliexpress.com/item/HET9016RD-Ever-Pretty-Red-Double-V-Elegant-Evening-Dress/884358207.html
						newOrderSnapshots.add(spst);
					} else if (goods_url.indexOf("goodsinfo/") > -1 && goods_url.indexOf(".html") > -1) {
						// 3.goods_url伪静态化的链接，如：-2545139283432.html中-2开头的都是阿里商品
						/// goodsinfo/women-s-clothing-accessories-dresses-stripe-dress-irregular-dress-2545139283432.html
						String lastSf = goods_url.substring(goods_url.lastIndexOf("-") + 1);
						if (lastSf.indexOf("2") == 0) {
							newOrderSnapshots.add(spst);
						} else {
							goodOrderSnapshots.add(spst);
						}
					} else if (!(goods_url == null || "".equals(goods_url))) {
						goodOrderSnapshots.add(spst);
					}
				}
			}

			// 5.生成订单快照操作
			if (goodOrderSnapshots.size() > 0) {
				System.out.println("--insert goodOrderSnapshots size:" + orderSnapshots.size());
				LOG.info("--insert goodOrderSnapshots size:" + orderSnapshots.size());
				snapshotDao.batchInsertOrderSnapshot(goodOrderSnapshots);
			}

			orderSnapshots.clear();
			goodOrderSnapshots.clear();

			// 6.下载阿里商品的图片
			if (newOrderSnapshots.size() > 0) {
				System.out.println("--deal insert OrderSnapshots size:" + orderSnapshots.size());
				LOG.info("--deal insert OrderSnapshots size:" + orderSnapshots.size());
				int count = 1;
				downAliImg(count, newOrderSnapshots, snapshotDao);
			}
		}

		System.out.println("OrderSnapshot end!!");
		LOG.info("OrderSnapshot end!!");
	}

	private void downAliImg(int count, List<OrderSnapshot> newOrderSnapshots, OrderSnapshotDao snapshotDao) {

		List<OrderSnapshot> errorList = new ArrayList<OrderSnapshot>();
		Random random = new Random();
		// httpClient调用图片服务器程序处理数据
		for (OrderSnapshot nwSpst : newOrderSnapshots) {
			// 判断本地是否已经下载了相同数据的图片
			String existsImg = snapshotDao.queryExistsImgUrlByPidAndType(nwSpst.getGoods_pid(), nwSpst.getGoods_type());
			if (existsImg == null || "".equals(existsImg)) {
				// 新生成图片名称
				String originalName = nwSpst.getGoods_img();
				String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
				String saveFilename = makeFileName(String.valueOf(random.nextInt(1000)) + fileSuffix);
				// 图片服务器下载ali图片
				String url = ContentConfig.PICTURE_SERVER_IMG_DOWN + "imgUrl=" + originalName + "&newImgName="
						+ nwSpst.getGoods_pid() + "/" + saveFilename;
				String resultJson = DownloadMain.getContentClient(url, null);
				if ("0".equals(resultJson)) {
					System.err.println("PID:" + nwSpst.getGoods_pid() + ",IMG_DOWN下载失败");
					LOG.error("PID:" + nwSpst.getGoods_pid() + ",IMG_DOWN下载失败");
				} else if ("1".equals(resultJson)) {
					nwSpst.setDeal_flag(1);
					nwSpst.setDown_img_url(SHOWURL + nwSpst.getGoods_pid() + "/" + saveFilename);
					boolean success = snapshotDao.insertOrderSnapshot(nwSpst);
					if (!success) {
						errorList.add(nwSpst);
						System.err.println("PID:" + nwSpst.getGoods_pid() + ",更新失败");
						LOG.error("PID:" + nwSpst.getGoods_pid() + ",更新失败");
					}
				} else {
					errorList.add(nwSpst);
					System.err.println("PID:" + nwSpst.getGoods_pid() + ",IMG_DOWN无返回结果");
					LOG.error("PID:" + nwSpst.getGoods_pid() + ",IMG_DOWN无返回结果");
				}
			} else {
				System.out.println("PID:" + nwSpst.getGoods_pid() + ",已下载图片！！ " + existsImg);
				LOG.info("PID:" + nwSpst.getGoods_pid() + ",已下载图片！！！！ " + existsImg);
				nwSpst.setDeal_flag(1);
				nwSpst.setDown_img_url(existsImg);
				boolean success = snapshotDao.insertOrderSnapshot(nwSpst);
				if (!success) {
					errorList.add(nwSpst);
					System.err.println("PID:" + nwSpst.getGoods_pid() + ",更新失败");
					LOG.error("PID:" + nwSpst.getGoods_pid() + ",更新失败");
				}
			}
		}
		// 循环三次进行下载
		if (errorList.size() > 0 && count < 4) {
			System.err.println("继续下载失败的图片,次数:" + count + ",size:" + errorList.size());
			downAliImg(++count, errorList, snapshotDao);
		}
		newOrderSnapshots.clear();
	}

	private String makeFileName(String filename) {
		// 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
		return UUID.randomUUID().toString() + "_" + filename;
	}

}
