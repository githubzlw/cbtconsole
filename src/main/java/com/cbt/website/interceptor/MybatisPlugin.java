package com.cbt.website.interceptor;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.util.SqlErrorUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**   
 * @Title : MybatisPlugin.java 
 * @Description : TODO
 * @Company : www.importExpress.com
 * @author : 柒月
 * @date : 2016年11月18日
 * @version : V1.0   
 */

@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class })})
public class MybatisPlugin implements Interceptor {

	
	private Properties properties;
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnValue = null;
        long start = System.currentTimeMillis();
        //执行后的返回值
        returnValue = invocation.proceed();
        
        long end = System.currentTimeMillis();
        long time = (end - start);
        String sql = "";
        if (time > 1) {
        	sql = getSql(configuration, boundSql, sqlId, time);
        	System.err.println(sql);
        }
        
        String resource = DataSourceSelector.get();
        //resource为空则说明用的是默认数据源,不为空则是切换了数据源
        //flag为1:代表本地操作失败,2:远程操作失败
        int flag;
        if (resource == null) {
        	flag = 1;
		}else {
			flag = 2;
		}
        if (Integer.parseInt(String.valueOf(returnValue)) <= 0) {
        	returnValue = SqlErrorUtil.executeSqlError(sql, flag, 2);
		}
        return returnValue;
	}
	
	 public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long time) {
	        String sql = showSql(configuration, boundSql);
	        StringBuilder str = new StringBuilder(100);
//	        str.append(sqlId);
//	        str.append(":");
	        str.append(sql);
//	        str.append(":");
//	        str.append(time);
//	        str.append("ms");
	        return str.toString();
	    }
	 
	    private static String getParameterValue(Object obj) {
	        String value = null;
	        if (obj instanceof String) {
	            value = "'" + obj.toString() + "'";
	        } else if (obj instanceof Date) {
	            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
	            value = "'" + formatter.format(new Date()) + "'";
	        } else {
	            if (obj != null) {
	                value = obj.toString();
	            } else {
	                value = "";
	            }
	 
	        }
	        return value;
	    }
	 
	    public static String showSql(Configuration configuration, BoundSql boundSql) {
	        Object parameterObject = boundSql.getParameterObject();
	        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
	        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
	        if (parameterMappings.size() > 0 && parameterObject != null) {
	            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
	            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
	                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
	 
	            } else {
	                MetaObject metaObject = configuration.newMetaObject(parameterObject);
	                for (ParameterMapping parameterMapping : parameterMappings) {
	                    String propertyName = parameterMapping.getProperty();
	                    if (metaObject.hasGetter(propertyName)) {
	                        Object obj = metaObject.getValue(propertyName);
	                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
	                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
	                        Object obj = boundSql.getAdditionalParameter(propertyName);
	                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
	                    }
	                }
	            }
	        }
	        return sql;
	    }

	@Override
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	@Override
	public void setProperties(Properties arg0) {
		this.properties = arg0;
	}
	
	

}
