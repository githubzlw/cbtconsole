package ceRong.tools.bean;

import java.io.Serializable;

/**
 * DorpDwonBean
 */
public class DorpDwonBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String indexName;//索引名称
	
	private String indexNameCn;//索引名称中文
	
	private String upIndexName;//图片搜索索引名称
	
	public String getUpIndexName() {
		return upIndexName;
	}

	public void setUpIndexName(String upIndexName) {
		this.upIndexName = upIndexName;
	}

	public String getIndexNameCn() {
		return indexNameCn;
	}

	public void setIndexNameCn(String indexNameCn) {
		this.indexNameCn = indexNameCn;
	}

	private String createTime;//创建时间
	
	private String nameTime;//索引名+创建时间

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getNameTime() {
		return nameTime;
	}

	public void setNameTime(String nameTime) {
		this.nameTime = nameTime;
	}
	
}
