package com.cbt.bean;

/**
 * 1688商品查询条件信息
 * 
 * @author JXW
 *
 */
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

	private int infringingFlag = -1;//侵权标识 0未侵权 1侵权


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



	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSttime() {
		return sttime;
	}

	public void setSttime(String sttime) {
		this.sttime = sttime;
	}

	public String getEdtime() {
		return edtime;
	}

	public void setEdtime(String edtime) {
		this.edtime = edtime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public int getIsEdited() {
		return isEdited;
	}

	public void setIsEdited(int isEdited) {
		this.isEdited = isEdited;
	}

	public int getIsAbnormal() {
		return isAbnormal;
	}

	public void setIsAbnormal(int isAbnormal) {
		this.isAbnormal = isAbnormal;
	}

	public int getIsBenchmark() {
		return isBenchmark;
	}

	public void setIsBenchmark(int isBenchmark) {
		this.isBenchmark = isBenchmark;
	}

	public int getBmFlag() {
		return bmFlag;
	}

	public void setBmFlag(int bmFlag) {
		this.bmFlag = bmFlag;
	}

	public int getSourceProFlag() {
		return sourceProFlag;
	}

	public void setSourceProFlag(int sourceProFlag) {
		this.sourceProFlag = sourceProFlag;
	}

	public int getSoldFlag() {
		return soldFlag;
	}

	public void setSoldFlag(int soldFlag) {
		this.soldFlag = soldFlag;
	}

	public int getPriorityFlag() {
		return priorityFlag;
	}

	public void setPriorityFlag(int priorityFlag) {
		this.priorityFlag = priorityFlag;
	}

	public int getAddCarFlag() {
		return addCarFlag;
	}

	public void setAddCarFlag(int addCarFlag) {
		this.addCarFlag = addCarFlag;
	}

	public int getSourceUsedFlag() {
		return sourceUsedFlag;
	}

	public void setSourceUsedFlag(int sourceUsedFlag) {
		this.sourceUsedFlag = sourceUsedFlag;
	}

	public int getOcrMatchFlag() {
		return ocrMatchFlag;
	}

	public void setOcrMatchFlag(int ocrMatchFlag) {
		this.ocrMatchFlag = ocrMatchFlag;
	}

	public int getRebidFlag() {
		return rebidFlag;
	}

	public void setRebidFlag(int rebidFlag) {
		this.rebidFlag = rebidFlag;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getWeightCheck() {
		return weightCheck;
	}

	public void setWeightCheck(int weightCheck) {
		this.weightCheck = weightCheck;
	}

	public int getInfringingFlag() {
		return infringingFlag;
	}

	public void setInfringingFlag(int infringingFlag) {
		this.infringingFlag = infringingFlag;
	}

	public double getAliWeightBegin() {
		return aliWeightBegin;
	}

	public void setAliWeightBegin(double aliWeightBegin) {
		this.aliWeightBegin = aliWeightBegin;
	}

	public double getAliWeightEnd() {
		return aliWeightEnd;
	}

	public void setAliWeightEnd(double aliWeightEnd) {
		this.aliWeightEnd = aliWeightEnd;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(String offlineTime) {
		this.offlineTime = offlineTime;
	}

	public String getEditBeginTime() {
		return editBeginTime;
	}

	public void setEditBeginTime(String editBeginTime) {
		this.editBeginTime = editBeginTime;
	}

	public String getEditEndTime() {
		return editEndTime;
	}

	public void setEditEndTime(String editEndTime) {
		this.editEndTime = editEndTime;
	}

	public double getWeight1688Begin() {
		return weight1688Begin;
	}

	public void setWeight1688Begin(double weight1688Begin) {
		this.weight1688Begin = weight1688Begin;
	}

	public double getWeight1688End() {
		return weight1688End;
	}

	public void setWeight1688End(double weight1688End) {
		this.weight1688End = weight1688End;
	}

	public double getPrice1688Begin() {
		return price1688Begin;
	}

	public void setPrice1688Begin(double price1688Begin) {
		this.price1688Begin = price1688Begin;
	}

	public double getPrice1688End() {
		return price1688End;
	}

	public void setPrice1688End(double price1688End) {
		this.price1688End = price1688End;
	}

	public int getIsSort() {
		return isSort;
	}

	public void setIsSort(int isSort) {
		this.isSort = isSort;
	}
}
