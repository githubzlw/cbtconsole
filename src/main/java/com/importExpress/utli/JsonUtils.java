package com.importExpress.utli;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

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
	
}