package com.cbt.bean;

import lombok.Data;

/**
 * 1688商品查询条件信息
 * 
 * @author JXW
 *
 */
@Data
public class CustomGoodsQuery {

	private String catid;// 类别id
	private int page = 1;// 页码
	private String sttime;// 上传时间查询开始
	private String edtime;// 上传时间查询结束
	private int state;// 状态 2-产品下架 3-发布失败 4-发布成功
	private int adminId;// 管理员id
	
	private int isEdited = -1;// 是否编辑 0:未编辑1:标题已编辑2:描述已编辑
	private int isAbnormal = -1;// 异常数据 0未选择，1类别错误，2商品太贵，3太便宜
	private int isBenchmark = -1;// 对标参数0全部，1对标，2非对标

	private int bmFlag;// 人为对标货源 0全部，1是，2否
	private int sourceProFlag;// 货源属性1:同店铺商品,2:同款商品 ,3:对标商品,4:1688商品(原始老数据)
	private int soldFlag;// 是否售卖0全部1:卖过,2:没有卖过

	private int priorityFlag;// 商品优先级 1:核心,2：非核心
	private int addCarFlag;// 是否加入购物车 0全部，1已加入购物车，2未加入购物车，3已加入购物车后删除
	private int sourceUsedFlag = -1;// 货源信息可用度0全部，1可用，2不可用
	private int ocrMatchFlag;// OCR判断情况
	// 1：人工OCR判断(详情)，2：未判断，3：未判断(全禁)，4：未判断(全免)，5：无中文，6：有中文，但不删除
	// （因为图片比例少），7：有中文，部分图应被删除，8：没图
	
	private int rebidFlag;// 重新对标 0：默认，1：已对标
	
	private int valid;
	
	/**
	 * 重量检查组合方式( 0 2 3 4 5 2*5 3*5); 0不是异常;2对于重量 比 类别平均重量 高30% 而且 运费占 总价格 占比超 35%的 ; 
	 * 3如果重量 比 类别平均重量低40%，请人为检查; 4重量数据为空的; 5对于所有的 运费占总免邮价格 60%以上的
	 */
	private int weightCheck = -1;

	//侵权标识 0未侵权 1侵权
	private int infringingFlag = -1;


	private double aliWeightBegin;
	private double aliWeightEnd;
	private String onlineTime;
	private String offlineTime;
	private String editBeginTime;
	private String editEndTime;
	private double weight1688Begin;
	private double weight1688End;
	private double price1688Begin;
	private double price1688End;
	private int isSort;
	private String unsellableReason;
	//是否被投诉 0-为投诉  1-投诉
	private int isComplain = -1;
	// 产品上线来源：1店铺上线，2单个商品上线录入，3速卖通对标上线，4跨境上线，5爆款开发上线
	private int fromFlag;

	private double finalWeightBegin;
	private double finalWeightEnd;
	private double minPrice;
	private double maxPrice;
	// 是否免邮 flag:1老客户免邮价,2：新的免邮价
	private int isSoldFlag = -1;
	private int isWeigthZero;
	private int isWeigthCatid;

	private int currPage;
	private String qrCatid;
	private String shopId;
	private String chKeyWord;

}
