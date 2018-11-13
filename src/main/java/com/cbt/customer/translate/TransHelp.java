package com.cbt.customer.translate;

import com.cbt.parse.service.GoodsBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Date;

public class TransHelp {
	private static final Log LOG = LogFactory.getLog(TransHelp.class);
	  /**
     * 方法描述:翻译前与翻译后的实体转换
     * author: 李湛君
     * date:2015年4月13日
     * @param data
     * @return
     */
    public static GoodsBean TransBean(GoodsBean data) {
    	if(data == null) return null;
    	GoodsHelp gh = new GoodsHelp();
		gh.setTitle(data.getTitle());
		gh.setpName(data.getpName());
		gh.setsName(data.getsName());
		gh.setpPriceUnit(data.getpPriceUnit());
		gh.setpGoodsUnit(data.getpGoodsUnit());
		gh.setMinOrder(data.getMinOrder());
//		gh.setMaxOrder(data.getMaxOrder());
		gh.setCategory(data.getCategory());;
//		gh.setPcolor(data.getpColor());
		gh.setpFreight(data.getpFreight());
		gh.setpInfo(data.getpInfo());
//		gh.setpDetail(data.getpDetail());
		JSONArray jsonArray = JSONArray.fromObject(gh);
		String json = jsonArray.toString();
		gh = translate(json.toString());
		if(gh != null) {
			data.setTitle(gh.getTitle());
			data.setpName(gh.getpName());
			data.setsName(gh.getsName());
			data.setpPriceUnit(gh.getpPriceUnit());
			data.setpGoodsUnit(gh.getpGoodsUnit());
			data.setMinOrder(gh.getMinOrder());
//			data.setMaxOrder(gh.getMaxOrder());
			data.setCategory(gh.getCategory());;
//			data.setpColor(gh.getPcolor());
			data.setpFreight(gh.getpFreight());
			data.setpInfo(gh.getpInfo());
//			data.setpDetail(gh.getpDetail());
		}    	
    	return data;
    }
    
    /**
     * 方法描述:翻译淘宝返回的信息内容
     * author: 李湛君
     * date:2015年4月13日
     * @return
     */
    public static GoodsHelp translate(String data) {
    	GoodsHelp gh = null;
    	long d1 = new Date().getTime();
    	String translate = Google.translate(data, "中文简体", "英语", true);
    	long d2 = new Date().getTime();
    	LOG.warn("google翻译所花的时间:"+(d2-d1));
    	LOG.warn("subOne翻译后的返回结果:"+translate);
    	try {
			gh = jsonToBean(translate);
			LOG.warn("gh:"+gh.getTitle());
			/*String jt2 = jsonToList(translate2);
			System.out.println("jt2:"+jt2);*/
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return gh;
    }
    
    /**
     * 方法描述:将google翻译后的json字符串转换成javabean对象
     * author: 李湛君
     * date:2015年4月13日
     * @param json
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static GoodsHelp jsonToBean(String json) throws JsonParseException, JsonMappingException, IOException {
    	if(json == null) return null;
    	json = json.substring(0,json.indexOf("\"zh-CN\""))+"\"zh-CN\"}";
    	 GoodsHelp goods = null;
    	ObjectMapper om = new ObjectMapper();
        TransWrapper transWrapper = om.readValue(json, TransWrapper.class);
        //当字符串比较长时，google会进行分段翻译，所以这里得到的是一个数组（注意：不是List）
        Trans[] trans = transWrapper.getSentences();
        String translation = "" ;
         
        for (Trans tran : trans)
        {  
            translation += tran.getTrans();
        }
        char c = translation.charAt(3);
        c = Character.toLowerCase(c);
        translation = translation.replace(translation.charAt(3), c);
        translation = translation.replaceAll(",\"\"","\",\"");
        translation = translation.replaceAll(",\" \"","\",\"");
        translation = translation.replaceAll(", \"\"","\",\"");
        translation = translation.replaceAll("\"\"","\",\"");
        translation = translation.replaceAll("\" \"","\",\"");
        translation = translation.replaceAll(" \"","\"");
        translation = translation.replaceAll("\" ","\"");
        translation = translation.replaceAll("\"and\"","and");
        translation = translation.substring(1,translation.length()-1);
        LOG.warn("translation:"+translation);
        //List<GoodsHelp> transList = new ArrayList<GoodsHelp>();
        JSONObject jo = JSONObject.fromObject(translation);
        //把json数组字符串转换成json数组
        //JSONArray ja = JSONArray.fromObject(translation);
        //for (int i = 0; i < ja.size(); i++) {
       	 String s = jo.toString();
       	 s = s.replaceAll("\" ", "\"");
       	 s = s.replaceAll(" \"", "\"");
       	goods = (GoodsHelp) JSONObject.toBean(jo, GoodsHelp.class);
       	 //goods = om.readValue(s, GoodsHelp.class);
		//}
        return goods;
	}
}
