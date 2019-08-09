package com.importExpress.utli;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	 // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final ObjectMapper MAPPERNOTNULL = new ObjectMapper();

    static{
        MAPPERNOTNULL.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

	/**
     * 将对象转换成json字符串。 (去除null值)
     * @param data
     * @return
     */
    public static String objectToJsonNotNull(Object data) {
    	try {
			String string = MAPPERNOTNULL.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     *
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
	
}