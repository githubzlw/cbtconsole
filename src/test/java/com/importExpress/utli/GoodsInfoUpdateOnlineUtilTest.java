package com.importExpress.utli;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.utli
 * @date:2020/1/7
 */
public class GoodsInfoUpdateOnlineUtilTest {


    @Test
    public void setOffOnlineByPidTest() {
        String pid = "1246359203";
        boolean isSu = GoodsInfoUpdateOnlineUtil.setOffOnlineByPid(pid, "更新MongoDB异常");
        Assert.assertTrue("更新MongoDB异常", isSu);

    }
}
