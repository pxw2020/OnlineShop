package com.miaoshaproject.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {

    //校验结果是否有错
    private boolean hasErrors = false;
    //将错误信息存进map
    private Map<String,String> errorMsgMap = new HashMap<>();

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMSgMap() {
        return errorMsgMap;
    }

    public void setErrorMSgMap(Map<String, String> errorMSgMap) {
        this.errorMsgMap = errorMSgMap;
    }

    //格式化字符串信息获取错误结果
    public String getErrMsg(){
      return StringUtils.join(errorMsgMap.values().toArray(),",");
    }
}
