package com.cbt.track.ctrl;

import com.cbt.bean.EasyUiJsonResult;
import com.cbt.bean.TabTrackInfo;
import com.cbt.track.service.TabTrackInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/tabtrackinfo")
public class TabTrackInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TabTrackInfoController.class);

    @Autowired
    private TabTrackInfoService tabTrackInfoService;

    /**
     * 通过运单号查询物流信息
     * /tabtrackinfo/querybytrackno?trackNo=
     * /rest/tabtrackinfo/querybytrackno?trackNo=00340434198101427496
     *
     * @param trackNo 运单号
     * @return
     */
    @RequestMapping(value = "/querybytrackno.do", method = RequestMethod.GET)
    public ResponseEntity<TabTrackInfo> queryByTrackNo(@RequestParam("trackNo") String trackNo) {
        try {
            if (trackNo == null || trackNo.length() < 3 || trackNo.length() > 30) {
                // 参数错误，响应400
                return new ResponseEntity<TabTrackInfo>(HttpStatus.BAD_REQUEST);
            }
            TabTrackInfo tabTrackInfo = tabTrackInfoService.queryByTrackNo(trackNo);
            if (tabTrackInfo == null) {
                // 未查询到 响应404(调整为返回空对象)
                return new ResponseEntity<TabTrackInfo>(HttpStatus.NOT_FOUND);
            }
            // 响应200
            return new ResponseEntity<TabTrackInfo>(tabTrackInfo, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return new ResponseEntity<TabTrackInfo>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 查询订单运单列表，有分页，有条件
     * http://127.0.0.1:8081/rest/tabtrackinfo/list
     *
     * rows 每页行数			   为空则默认20
     * page 当前页			   为空则默认1
     *
     * orderUserid 用户id或邮箱 默认空
     * orderNo 订单编号 默认空
     * orderTrackNo 运单号 转单号 默认空
     *
     * payStartDate 默认空
     * payEndDate 支付起始时间 默认空
     *
     * startDate 默认空
     * endDate 出货起始时间 默认空
     * userid 负责人 默认0是全部 不进行筛选
     *
     * trackState 运行状态      默认0-查询所有；1-备货中；2-已发货；3-已签收；4-退回；5-异常(海关扣押等)；6-内部异常（抓取方式没有，单号问题等）
     * warning 预警查询         默认0-查询所有；-1-不进行预警筛选；1-物流问题；2-交期超时；3-未签收；4-退回；5-异常(海关扣押等)；6-内部异常（抓取方式没有，单号问题等）
     *
     * export 是否导出 1是导出      默认0 不导出
     *
     * @return 返回的是easyui数据(json)格式
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public EasyUiJsonResult getTrackInfoList(@RequestParam(value = "rows", defaultValue = "20", required = false) Integer rows,
                                              @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                              @RequestParam(value = "orderUserid", defaultValue = "", required = false) String orderUserid,
                                              @RequestParam(value = "orderNo", defaultValue = "", required = false) String orderNo,
                                              @RequestParam(value = "orderTrackNo", defaultValue = "", required = false) String orderTrackNo,
                                              @RequestParam(value = "payStartDate", defaultValue = "", required = false) String payStartDate,
                                              @RequestParam(value = "payEndDate", defaultValue = "", required = false) String payEndDate,
                                              @RequestParam(value = "startDate", defaultValue = "", required = false) String startDate,
                                              @RequestParam(value = "endDate", defaultValue = "", required = false) String endDate,
                                              @RequestParam(value = "userid", defaultValue = "0", required = false) Integer userid,
                                              @RequestParam(value = "trackState", defaultValue = "0", required = false) Integer trackState,
                                              @RequestParam(value = "warning", defaultValue = "0", required = false) Integer warning,
                                              @RequestParam(value = "export", defaultValue = "0", required = false) Integer export) {
        EasyUiJsonResult json = new EasyUiJsonResult();
        Map<String, Object> param = new HashMap<String, Object>(){{
            put("rows", rows);
            put("page", page);
            put("orderUserid", orderUserid);
            put("orderNo", orderNo);
            put("orderTrackNo", orderTrackNo);
            put("userid", userid);
            put("trackState", trackState);
            put("warning", warning);
            put("export", export);
        }};
        if (!"".equals(payStartDate)) {
            payStartDate += " 00:00";
            param.put("payStartDate", payStartDate);
        }
        if (!"".equals(payEndDate)) {
            payEndDate += " 23:59";
            param.put("payEndDate", payEndDate);
        }
        if (!"".equals(startDate)) {
            startDate += " 00:00";
            param.put("startDate", startDate);
        }
        if (!"".equals(endDate)) {
            endDate += " 23:59";
            param.put("endDate", endDate);
        }

        if (!"".equals(orderUserid))
            param.put("orderUseridLike", "%" + orderUserid + "%");
        if (!"".equals(orderNo))
            param.put("orderNoLike", "%" + orderNo + "%");
        if (!"".equals(orderTrackNo))
            param.put("orderTrackNoLike", "%" + orderTrackNo + "%");

        if (export == 0) {
            // page==null?null:(page-1)*rows, rows
            param.put("startBars", (page - 1) * rows);
        }

        try {
            tabTrackInfoService.getTrackInfoList(json, param);
        } catch (Exception e) {
            LOGGER.error("getTrackInfoList error ", e);
            json.setRows(new ArrayList<TabTrackInfo>());
            json.setTotal(0);
            json.setSuccess(false);
        }
        return json;
    }


    /**
     * 操作运单状态
     * /tabtrackinfo/updatestate?trackNo=3A5V435358583&trackNote=备注11&trackState=2
     *
     * @param trackNo 运单号
     * @param trackState 修改成的状态
     * @param trackNote 修改备注
     * @return
     */
    @RequestMapping(value = "/updatestate.do", method = RequestMethod.POST)
    public ResponseEntity<String> updatestate(@RequestParam("trackNo") String trackNo, @RequestParam("trackNote") String trackNote, @RequestParam("trackState") Integer trackState) {
        try {
            if (null == trackNo || trackNo.length() < 3 || trackNo.length() > 30 || null == trackState) {
                // 参数错误，响应400
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
            TabTrackInfo tabTrackInfo = new TabTrackInfo(trackNo, trackState, trackNote);
            boolean res = tabTrackInfoService.updatestate(tabTrackInfo);
            if (res == false) {
                // 修改失败
                return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
            }
            // 响应200
            return new ResponseEntity<String>("success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * 通过运单号查询运单状态信息
     * /tabtrackinfo/querystatebytrackno?trackNo=
     *
     * @param trackNo 运单号
     * @return
     */
    @RequestMapping(value = "/querystatebytrackno.do", method = RequestMethod.GET)
    public ResponseEntity<TabTrackInfo> querystatebytrackno(@RequestParam("trackNo") String trackNo) {
        try {
            if (trackNo == null || trackNo.length() < 3 || trackNo.length() > 30) {
                // 参数错误，响应400
                return new ResponseEntity<TabTrackInfo>(HttpStatus.BAD_REQUEST);
            }
            TabTrackInfo tabTrackInfo = tabTrackInfoService.queryStateByTrackNo(trackNo);
            if (tabTrackInfo == null) {
                // 未查询到 响应404(调整为返回空对象)
                return new ResponseEntity<TabTrackInfo>(HttpStatus.NOT_FOUND);
            }
            // 响应200
            return new ResponseEntity<TabTrackInfo>(tabTrackInfo, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return new ResponseEntity<TabTrackInfo>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * 查询预警数量
     * /tabtrackinfo/querywaringnum.do?startDate=&endDate=
     *
     *  trackNo 运单号
     * @return
     */
    @RequestMapping(value = "/querywaringnum.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Integer>> querywaringnum(@RequestParam(value = "payStartDate", defaultValue = "", required = false) String payStartDate,
                                                                @RequestParam(value = "payEndDate", defaultValue = "", required = false) String payEndDate,
                                                                @RequestParam(value = "export", defaultValue = "0", required = false) Integer export) {
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            if (!"".equals(payStartDate)) {
                payStartDate += " 00:00";
                param.put("payStartDate", payStartDate);
            }
            if (!"".equals(payEndDate)) {
                payEndDate += " 23:59";
                param.put("payEndDate", payEndDate);
            }
        	Map<String, Integer> result = tabTrackInfoService.queryWaringNum(param);
            if (result == null) {
                // 未查询到 响应404(调整为返回空对象)
                return new ResponseEntity<Map<String, Integer>>(HttpStatus.NOT_FOUND);
            }
            // 响应200
            return new ResponseEntity<Map<String, Integer>>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 响应500
        return new ResponseEntity<Map<String, Integer>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
