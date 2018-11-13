package com.cbt.util;

import java.util.HashMap;
import java.util.Map.Entry;

public class ParamToSqlUtil {
	/**
	 * 生成SQL体,注意时间字段必须传递正确string格式字符串的值
	 * 
	 * @param tableName
	 *            : 表名
	 * @param type
	 *            : 操作类型,0:插入操作,1:更新操作
	 * @param parMap
	 *            : 更新参数键值对,不允许为NULL值
	 * @param keyMap
	 *            : 关键字段键值对,操作类型为更新操作时,不允许为NULL值
	 * @return
	 */
	public static String genSql(String tableName, int type, HashMap<String, Object> parMap,
			HashMap<String, Object> keyMap) throws Exception{
		String sqlStr = "";// 拼湊SQL体字符串
		// 判断传递的类型 0:插入操作,1:更新操作
		if (type == 0) {
			String fieldsStr = "";// 字段拼湊字符串
			String valuesStr = "";// 字段值拼湊字符串
			sqlStr = "insert into " + tableName + "(";
			// 循环获取键值对的数据
			for (Entry<String, Object> entity : parMap.entrySet()) {
				fieldsStr += "," + entity.getKey();
				Object obj = entity.getValue();
				if(obj == null){
					throw new Exception("更新参数键值对parMap含有参数值为NULL");
				}
				
				// 传递的参数是String类型的，都在拼凑SQL时加单引号
				if (obj instanceof String) {
					valuesStr += ",'" + entity.getValue() + "'";
				} else {
					valuesStr += "," + entity.getValue();
				}
			}
			sqlStr += fieldsStr.substring(1) + ") values(" + valuesStr.substring(1) + ")";
		} else {
			sqlStr = "update " + tableName + " set ";
			String fieldsAndvaluesStr = "";// 字段和字段值拼湊字符串
			// 循环获取键值对的数据
			for (Entry<String, Object> entity : parMap.entrySet()) {
				Object obj = entity.getValue();
				// 传递的参数是String类型的，都在拼凑SQL时加单引号
				if (obj instanceof String) {
					fieldsAndvaluesStr += "," + entity.getKey() + "='" + obj + "'";
				} else {
					fieldsAndvaluesStr += "," + entity.getKey() + "=" + obj;
				}
			}
			// 判断关键字段的map是否为空
			if (!(keyMap == null || keyMap.isEmpty())) {
				String whereSql = "";// where语句字段和字段值拼湊字符串
				for (Entry<String, Object> keyEntity : keyMap.entrySet()) {
					Object keyObj = keyEntity.getValue();
					if(keyObj == null){
						throw new Exception("更新参数键值对keyMap含有参数值为NULL");
					}
					// 传递的参数是String类型的，都在拼凑SQL时加单引号
					if (keyObj instanceof String) {
						whereSql += " and " + keyEntity.getKey() + "='" + keyObj + "'";
					} else {
						whereSql += " and " + keyEntity.getKey() + "=" + keyObj;
					}
				}
				sqlStr += fieldsAndvaluesStr.substring(1) + " where 1=1 " + whereSql.substring(1);
			} else {
				throw new Exception("关键字段键值对keyMap为空");
			}
		}
		return sqlStr;
	}

}
