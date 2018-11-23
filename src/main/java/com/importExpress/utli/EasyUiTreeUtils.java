package com.importExpress.utli;

import com.cbt.bean.CategoryBean;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EasyUiTree的处理
 */
public class EasyUiTreeUtils {


    public static List<Map<String, Object>> genEasyUiTree(List<CategoryBean> categorys, int count) {
        Map<String, Object> treeRoot = new HashMap<String, Object>();// 根节点
        List<Map<String, Object>> treeMap = new ArrayList<Map<String, Object>>();// 根节点的所有子节点

        List<Map<String, Object>> parentMaps = new ArrayList<Map<String, Object>>();
        // 循环获取一级目录数据并赋值子菜单
        for (CategoryBean ct : categorys) {
            if (ct.getLv() == 1 && !"2".equals(ct.getCid())) {
                Map<String, Object> childMap = new HashMap<String, Object>();
                childMap.put("id", ct.getCid());
                childMap.put("text", ct.getCategoryName());
                childMap.put("state", "closed");
                childMap.put("total", ct.getTotal());
                childMap.put("children", getChildMap(ct.getCid(), categorys, ct.getLv()));
                parentMaps.add(childMap);
            }
        }
        List<Map<String, Object>> nwParentMaps = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> ptMap : parentMaps) {
            int ptCount = doTreeCount(ptMap);
            if (ptCount == 0) {
                ptMap.put("children", null);
            } else {
                ptCount += Integer.valueOf(ptMap.get("total").toString());
                ptMap.put("text", ptMap.get("text") + "(<b>" + ptCount + "</b>)");
                nwParentMaps.add(ptMap);
            }
        }
        parentMaps.clear();

        treeRoot.put("children", nwParentMaps);
        treeRoot.put("id", "0");
        treeRoot.put("text", "全部类别" + "(<b>" + count + "</b>)");
        treeMap.add(treeRoot);
        return treeMap;
    }

    // 根据cid和lv寻找次级目录
    public static List<Map<String, Object>> getChildMap(String cid, List<CategoryBean> categorys, int lv) {
        List<Map<String, Object>> childMaps = new ArrayList<Map<String, Object>>();
        for (CategoryBean ct : categorys) {
            // 判断path数据不为空lv是传递参数的+1值，并且在path中含有父类的cid
            if ((ct.getLv() == lv + 1) && StringUtils.isNotBlank(ct.getPath())) {
                // 寻找当前数据的
                if((","+ct.getPath()+ ",").contains(","+cid+ ",")){
                    Map<String, Object> child = new HashMap<String, Object>();
                    child.put("id", ct.getCid());
                    child.put("text", ct.getCategoryName());
                    child.put("total", ct.getTotal());

                    List<Map<String, Object>> childList = getChildMap(ct.getCid(), categorys, ct.getLv());
                    // 递归创建
                    child.put("children", childList);
                    if (ct.getLv() == 2 && childList.size() > 0) {
                        child.put("state", "closed");
                    }
                    childMaps.add(child);
                }
            }
        }
        return childMaps;
    }


    // 递归统计总数
    @SuppressWarnings("unchecked")
    public static int doTreeCount(Map<String, Object> parentMap) {
        int count = 0;
        List<Map<String, Object>> list = (List<Map<String, Object>>) parentMap.get("children");
        if (list == null || list.size() == 0) {
            return count;
        }
        List<Map<String, Object>> nwList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> childMap : list) {
            // 统计子节点的孩子总数+本身的总数
            int cur_cnt = doTreeCount(childMap);
            if (cur_cnt == 0) {
                childMap.put("children", null);
            }
            cur_cnt += Integer.valueOf(childMap.get("total").toString());
            childMap.put("text", childMap.get("text") + "(<b>" + cur_cnt + "</b>)");
            // 覆盖赋值
            childMap.put("total", cur_cnt);
            if (cur_cnt > 0) {
                nwList.add(childMap);
            }
            count += cur_cnt;
        }
        parentMap.put("children", nwList);
        list.clear();
        // 返回前记录当前节点的统计个数
        return count;
    }

}
