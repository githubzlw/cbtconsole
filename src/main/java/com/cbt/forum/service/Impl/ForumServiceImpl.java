package com.cbt.forum.service.Impl;

import com.cbt.forum.dao.ForumDao;
import com.cbt.forum.pojo.ForumClassification;
import com.cbt.forum.pojo.ForumDetails;
import com.cbt.forum.pojo.ForumUser;
import com.cbt.forum.service.ForumService;
import com.cbt.bean.EasyUiJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ForumServiceImpl implements ForumService {

	@Autowired
	private ForumDao forumDao;

	@Override
	public int EditForum_State(int id,int type, int state) {
		return forumDao.EditForum_State(id,type,state);
	}

	@Override
	public int EditForum_details_State(int id) {
		//同时删除帖子评论
		//forumDao.EditForum_post_comment(id);
		return forumDao.EditForum_delete_State(id);
	}

	@Override
	public List<ForumDetails> FidForumList_one(int id) {
		return forumDao.FidForumList_one(id);
	}

	@Override
	public String FindForumUser() {
		int zhi=(int)(1+Math.random()*(50-1+1));
		List<ForumUser> forumUser=forumDao.FindForumUser();
		String [] forumUser_=new String[50];
		for (int i=0;i<forumUser.size();i++){
			forumUser_[i]=forumUser.get(i).getUsersName();
		}
		return forumUser_[zhi];
	}

	@Override
	public EasyUiJsonResult FidForumList(Integer page,Integer rows,String posttitle
			,int type, int reviewFlag,String startDate,String endDate,String audit_user) {
		EasyUiJsonResult json=new EasyUiJsonResult();
		List<ForumDetails> list=forumDao.FidForumList((page-1)*rows,rows,posttitle,type,reviewFlag,startDate,endDate,audit_user);
		Integer count=forumDao.FidForumListConut(posttitle,type,reviewFlag,startDate,endDate,audit_user);
		if(list!=null&&list.size()>0){
			json.setRows(list);
			json.setSuccess(true);
			json.setTotal(count);
		}else{
			json.setRows("");
			json.setSuccess(false);
			json.setTotal(0);
		}

		return json;
	}
	@Override
	public List<ForumClassification> FidForumClass() {
		return forumDao.FidForumClass();
	}
}