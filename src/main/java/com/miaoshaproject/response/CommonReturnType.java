package com.miaoshaproject.response;

/**
 * @author xuwen
 * @version 1.0
 * @date 2021/1/19 16:27
 */
public class CommonReturnType {
    private  String status;       //sucess or fail
    //status==fail 则返回对应的通用错误码
    private Object data;

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");

    }

    public static CommonReturnType create(Object result,String status){
       CommonReturnType type = new CommonReturnType();
       type.setData(result);
       type.setStatus(status);
       return type;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
