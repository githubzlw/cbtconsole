package com.cbt.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

public class CustomAuthorizationFilter extends PermissionsAuthorizationFilter {
	//保留shiro原有的“，”分隔and的校验规则，新增“|”分隔的规则
		@Override
	    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
	        Subject subject = this.getSubject(request, response);
	        String[] perms = (String[]) mappedValue;
	        boolean isPermitted = true;
	        if (perms != null && perms.length > 0) {
	            if (perms.length == 1) {
	                if (!isOneOfPermitted(perms[0], subject)) {
	                    isPermitted = false;
	                }
	            } else if (!isAllPermitted(perms,subject)) {
	                isPermitted = false;
	            }
	        }
	        return isPermitted;
	    }
		
		//权限表达式","分隔，and的校验规则
	    private boolean isAllPermitted(String[] permStrArray, Subject subject) {
	        boolean isPermitted = true;
	        for (int index = 0, len = permStrArray.length; index < len; index++) {
	            if (!isOneOfPermitted(permStrArray[index], subject)) {
	                isPermitted = false;
	            }
	        }
	        return isPermitted;
	    }
	  //权限表达式"|"分隔，or的校验规则
	    private boolean isOneOfPermitted(String permStr, Subject subject) {
	        boolean isPermitted = false;
	        String[] permArr = permStr.split("\\|");
	        if (permArr.length > 0) {
	            for (int index = 0, len = permArr.length; index < len; index++) {
	                if (subject.isPermitted(permArr[index])) {
	                    isPermitted = true;
	                }
	            }
	        }
	        return isPermitted;
	    }

}
