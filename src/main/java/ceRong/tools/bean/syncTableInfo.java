package ceRong.tools.bean;

import java.io.Serializable;


public class syncTableInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imgPath;
	private String uploadImgPath;
	
	private int score;
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getFileIdentifier() {
		return fileIdentifier;
	}
	public void setFileIdentifier(String fileIdentifier) {
		this.fileIdentifier = fileIdentifier;
	}

	private String fileIdentifier;
	/**
	 * catId
	 */
	private int catId;
	
	/**
	 * catName
	 */
	private String catName;
	/**
	 * 原url
	 */
	private String sourceUrl;
	
	/**
	 * 原img
	 */
	private String sourceImg;
	
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getSourceImg() {
		return sourceImg;
	}
	public void setSourceImg(String sourceImg) {
		this.sourceImg = sourceImg;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getUploadImgPath() {
		return uploadImgPath;
	}
	public void setUploadImgPath(String uploadImgPath) {
		this.uploadImgPath = uploadImgPath;
	}
	
	public syncTableInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
