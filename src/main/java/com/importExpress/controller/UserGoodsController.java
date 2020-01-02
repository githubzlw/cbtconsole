package com.importExpress.controller;

import com.cbt.website.util.EasyUiJsonResult;
import com.importExpress.pojo.UserGoods;
import com.importExpress.service.UserGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.controller
 * @date:2019/12/24
 */
@Slf4j
@Controller
@RequestMapping("/userGoods")
public class UserGoodsController {


    private final UserGoodsService userGoodsService;

    public UserGoodsController(UserGoodsService userGoodsService) {
        this.userGoodsService = userGoodsService;
    }

    @RequestMapping("/list")
    @ResponseBody
    public EasyUiJsonResult queryForList(String pid, Integer userId,
                                         @RequestParam(name = "rows", required = true, defaultValue = "30") Integer rows,
                                         @RequestParam(name = "page", required = true, defaultValue = "1") Integer page) {
        EasyUiJsonResult json = new EasyUiJsonResult();

        try {
            int startNum = (page - 1) * rows;
            UserGoods param = new UserGoods();
            if (StringUtils.isNotBlank(pid)) {
                param.setPid(pid);
            }
            if (userId != null && userId > 0) {
                param.setUserId(userId);
            }
            param.setRows(rows);
            param.setStartNum(startNum);
            List<UserGoods> userGoodsList = userGoodsService.queryForList(param);
            int count = userGoodsService.queryForListCount(param);
            json.setSuccess(userGoodsList, count);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("queryForList error:", e);
            json.setError(e.getMessage());
        }
        return json;
    }
}
