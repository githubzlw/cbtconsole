package com.cbt.track.ctrl;

import com.cbt.bean.EasyUiJsonResult;
import com.cbt.bean.TabTrackInfo;
import com.cbt.track.service.TabTrackInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/tabtrackinfo")
public class TabTrackInfoController {

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
     * trackState 运行状态      0-查询所有；1-备货中；2-已发货；3-已签收；4-退回；5-异常(海关扣押等)；6-内部异常（抓取方式没有，单号问题等）
     * warning 预警查询         0-查询所有；1-物流问题；2-交期超时；3-未签收；4-退回；5-异常(海关扣押等)；6-内部异常（抓取方式没有，单号问题等）
     * funChange 查询信息       0-运单状态;1-运单预警;2-单个订单号或运单号的查询
     *
     * @return 返回的是easyui数据(json)格式
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public EasyUiJsonResult queryByRepeatList(HttpServletRequest request) {
        //返回数据
        EasyUiJsonResult json = new EasyUiJsonResult();
        //需要查询的信息
        String funChangeStr = request.getParameter("funChange");
        int funChange;
        if (funChangeStr == null || "".equals(funChangeStr) || !funChangeStr.matches("\\d+")) {
            //参数错误
            json.setRows("");
            json.setSuccess(false);
            return json;
        } else {
            funChange = Integer.parseInt(funChangeStr);
        }
        //搜索条件 负责人 userid
        String useridStr = request.getParameter("userid");
        Integer userid = null;
        if (StringUtils.isNotBlank(useridStr) && !"0".equals(useridStr)) {
            userid = Integer.valueOf(useridStr);
        }
        //查询时间范围参数接收
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        if (!"".equals(startDate)) {
            startDate += " 00:00";
        }
        if (!"".equals(endDate)) {
            endDate += " 23:59";
        }

        //查询结果
        Map<String, Object> map = null;
        // 2-单个订单号或运单号的查询
        if (funChange == 2) {
            // 获取参数
            String orderOrTrackNo = request.getParameter("orderOrTrackNo");
            if (orderOrTrackNo == null || "".equals(orderOrTrackNo) || orderOrTrackNo.length() < 3) {
                //参数错误
                json.setRows("");
                json.setSuccess(false);
                return json;
            }
            map = tabTrackInfoService.getRecordListByOrderOrTrackNo(orderOrTrackNo, userid, startDate, endDate);
        } else if (funChange == 3) {
            // 3-根据用户id查询
            // 获取参数
            String orderUserid = request.getParameter("orderUserid");
            if (StringUtils.isBlank(orderUserid)) {
                //参数错误
                json.setRows("");
                json.setSuccess(false);
                return json;
            }
            map = tabTrackInfoService.getRecordListByUserid(orderUserid, userid, startDate, endDate);
        } else {
            //分页参数接收并处理
            String rowsStr = request.getParameter("rows");
            Integer rows = 20;
            if (!(rowsStr == null || "".equals(rowsStr) || "0".equals(rowsStr))) {
                rows = Integer.valueOf(rowsStr);//无该参数时查询默认值20
            }
            String pageStr = request.getParameter("page");
            Integer page = 1;
            if (!(pageStr == null || "".equals(pageStr) || "0".equals(pageStr))) {
                page = Integer.valueOf(pageStr);//无该参数时查询默认值1
            }
            if (funChange == 0) {
                // 查询运单状态
                // 获取参数
                String trackStateStr = request.getParameter("trackState");
                int trackState;
                if (trackStateStr == null || "".equals(trackStateStr) || !trackStateStr.matches("\\d+")) {
                    //参数错误
                    json.setRows("");
                    json.setSuccess(false);
                    return json;
                } else {
                    trackState = Integer.parseInt(trackStateStr);
                }
                map = tabTrackInfoService.getRecordListByTrackState(page, rows, startDate, endDate, trackState, userid);
            } else if (funChange ==1 ) {
                // 查询运单预警
                // 获取参数
                String warningStr = request.getParameter("warning");
                int warning;
                if (warningStr == null || "".equals(warningStr) || !warningStr.matches("\\d+")) {
                    //参数错误
                    json.setRows("");
                    json.setSuccess(false);
                    return json;
                } else {
                    warning = Integer.parseInt(warningStr);
                }
                map = tabTrackInfoService.getWarningRecordList(page, rows, startDate, endDate, warning, userid);
            }
        }
        // 查询结果处理 并返回
        if (map != null && map.size() > 0) {
            json.setSuccess(true);
            json.setRows(map.get("recordList"));
            json.setTotal(Integer.parseInt(map.get("totalCount").toString()));
        } else {
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
    public ResponseEntity<Map<String, Integer>> querywaringnum(HttpServletRequest request) {
        try {
        	//查询时间范围参数接收
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            if (!"".equals(startDate)) {
                startDate += " 00:00";
            }
            if (!"".equals(endDate)) {
                endDate += " 23:59";
            }
        	Map<String, Integer> result = tabTrackInfoService.queryWaringNum(startDate, endDate);
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
