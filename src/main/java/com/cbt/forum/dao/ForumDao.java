package com.cbt.forum.dao;

import com.cbt.forum.pojo.ForumClassification;
import com.cbt.forum.pojo.ForumDetails;
import com.cbt.forum.pojo.ForumUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumDao {
	/***
	 * 查询数据库中forumDetails中的论坛帖子数据
	 * @param page
	 * @param rows
	 * @param posttitle
	 * @param type
	 * @param reviewFlag
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<ForumDetails> FidForumList(@Param("page")Integer page,@Param("rows")Integer rows,@Param("posttitle")String posttitle
	,@Param("type")int type,@Param("reviewFlag")int reviewFlag,@Param("startDate")String startDate,@Param("endDate")String endDate);
	/***
	 * 查询数据库帖子数据总数
	 * @param posttitle
	 * @param type
	 * @param reviewFlag
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Integer FidForumListConut(@Param("posttitle")String posttitle
			,@Param("type")int type,@Param("reviewFlag")int reviewFlag,@Param("startDate")String startDate,@Param("endDate")String endDate);

	/***
	 * 搜索帖子类型
	 * @return
	 */
	public List<ForumClassification> FidForumClass();

	/***
	 * 随机生成发帖名
	 * @return
	 */
	public List<ForumUser> FindForumUser();

	/***
	 * 添加帖子
	 * @return
	 */
	public int AddForumPost(ForumDetails forumDetails);

	/***
	 * 修改审核状态以及是否启用 置顶帖子
	 * @return
	 */
	public int EditForum_State(@Param("id")int id, @Param("type")int type,@Param("state")int state);

	/***
	 * 删除帖子
	 * @return
	 */
	public int EditForum_delete_State(@Param("id")int id);

	/***
	 * 同时删除帖子评论信息
	 * @return
	 */
	public int EditForum_post_comment(@Param("id")int id);

	/***
	 * 查询帖子单条数据的值
	 * @param id
	 * @return
	 */
	public List<ForumDetails> FidForumList_one(@Param("id") int id);
}