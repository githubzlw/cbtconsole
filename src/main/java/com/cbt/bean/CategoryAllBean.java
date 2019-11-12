package com.cbt.bean;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.cbt.bean
 * @date:2019/11/12
 */
@Data
public class CategoryAllBean {
    private Integer id;

    private Integer lv;

    private String categoryId;

    private String name;

    private String childids;

    private String path;

    private String parentId;

    private String aliCategoryId;

    private String newcid;

    private String newpath;

    private String cat;

    private String allnewcid;

    private String updatechilds;

    private String createtime;

    private String enName;

    private String description;

    private Integer flag;

    private Integer newArrivalsFlag;

    private Integer enable;

    private String newArrivalDate;

    private String minVolumeWeight;

    private Integer freightFlag;

    private Integer weightSortFlag;

    private String changeParentId;

    private String changePath;

    private String changeChildids;

    private String changeName;

    private String changeEnName;

    private Integer changeLv;

    private String changeTime;

    private Integer updateAdminId;

    private String updateAdminName;
}
