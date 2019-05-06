package com.cbt.forum.service;


import com.cbt.bean.EasyUiJsonResult;
import com.cbt.forum.pojo.ForumClassification;
import com.cbt.forum.pojo.ForumDetails;
import com.cbt.forum.pojo.ForumUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumService {

	public EasyUiJsonResult FidForumList(Integer page,Integer rows,String posttitle
			,int type, int reviewFlag,String startDate,String endDate,String audit_user);

	public List<ForumClassification> FidForumClass();

	public String FindForumUser();

	public int EditForum_State(int id,int type,int state);

	public int EditForum_details_State(int id);

	public List<ForumDetails> FidForumList_one(int id);
}