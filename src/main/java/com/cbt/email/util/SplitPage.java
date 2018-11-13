package com.cbt.email.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

/**
 * 构造用于页面显示的分页组件
 *
 */
public class SplitPage {

	/**
	 * @param request  request
	 * @param total  总的记录数
	 * @param pageSize  每页显示数量
	 * @param page  当前页
	 */
	public static void buildPager(HttpServletRequest request, int total,
                                  int pageSize, int page)  {
		if (page < 1) page = 1;		//当前页
		int pages = total / pageSize; //总页数
		if (pages * pageSize < total) pages++; //不能整除时总页数加1
		
		//截取URL参数?前面的部分
		// getRequestURI()： /easybuy/manage/productList?page=3
		String forwardAction = request.getRequestURI();
		forwardAction += "?";// /easybuy/manage/productList?
		//System.out.println("3:" + forwardAction);
		
		//获取已有的全部参数，并且将之拼接到URL上
		//page=3&name=aaa&sex=bbbb
		Map params = request.getParameterMap();
		if (params.size() > 0) {
			//page = 3  
			//name = aaa
			//sex = bbbb
			for (Iterator it = params.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				if ("page".equals(key))//忽略参数中的page项目（后面会添加）
					continue;
				String[] values = (String[]) params.get(key);
				if (values[0] != null && !values[0].trim().equals(""))
					try {
						//将参数拼接到URL上，如果是中文，将之转换为iso8859-1
						forwardAction = forwardAction + key + "=" + new String(values[0].getBytes("iso-8859-1"), "utf-8") + "&";
						//System.out.println("4:" + forwardAction);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
			}
		}
		//上面运行结束后，forwardAction="/easybuy/manage/productList?name=aaa&sex=bbbb&"

		StringBuffer pager = new StringBuffer();
		pager.append("总数:" + total + "　每页：" + pageSize + " ");
		pager.append("\n页次:<span style='color:red'>" + page
				+ "</span>/<span style='color:red'>" + pages + "</span>　");
		pager.append("\n分页: ");
		
		//拼接首页和上页部分
		if (page > 1) {
			pager.append("\n<a class='emanagergetpagea' href='" + forwardAction + "page=" + 1 + "' title='第一页' class='a02'>[首页]</a> ");
			pager.append("\n<a class='emanagergetpagea' href='" + forwardAction + "page=" + (page - 1) + "' title='上一页(第" + (page - 1)
					+ "页)' class='a02'>[上页]</a> ");
		} else {
			pager.append("\n<span title='已是第一页'>[首页]</span> ");
			pager.append("\n<span title='已是第一页'>[上页]</span> ");
		}

		//拼接下页和尾页部分
		if (page < pages) {
			pager.append("\n<a class='emanagergetpagea' href='" + forwardAction + "page=" + (page + 1)
					+ "' title='下一页(第" + (page + 1)
					+ "页)' class='a02'>[下页]</a> ");
			pager.append("\n<a class='emanagergetpagea' href='" + forwardAction + "page=" + pages
					+ "' title='最后一页' class='a02'>[尾页]</a> ");
		} else {
			pager.append("\n<span title='已是最后一页'>[下页]</span> ");
			pager.append("\n<span title='已是最后一页'>[尾页]</span> ");
		}

		pager.append("\n&nbsp;&nbsp;跳转到第");
		pager.append("<input class='emanagergetpagee' name='page' id='page' type='text' value='' size='3'  />页	");
		pager.append("\n<input type='button' value='确定' onclick='jumpPage();' class='new_btn'/>		");
		pager.append("\n<script>														");
		pager.append("\n  function jumpPage(){											");
		pager.append("\n  	var page = document.getElementById('page').value;			");
		pager.append("\n  	if(page=='' || isNaN(page)){								");
		pager.append("\n  		alert('请输入正确的页码'); 								");
		pager.append("\n  		return false; 											");
		pager.append("\n  	} else {													");
		pager.append("\n  		page=parseInt(page);									");
		pager.append("\n  		if(page < 1 || page > " + pages + "){					");
		pager.append("\n  			alert('页码超出范围');return false;					");
		pager.append("\n  		}														");
		pager.append("\n  		window.location='" + forwardAction + "page=' + page;	");
		pager.append("\n  	}															");
		pager.append("\n  }																");
		pager.append("\n</script>														");
		//System.out.println(pager.toString());
		//HttpSession session = request.getSession();
		//session.setAttribute("pager", pager.toString());
		//将分页信息保存到request中
		request.setAttribute("pager", pager.toString());
	}
}
