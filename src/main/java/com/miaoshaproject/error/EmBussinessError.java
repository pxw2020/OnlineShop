package com.miaoshaproject.error;

public enum EmBussinessError implements CommonError {


    //通用错误类型00001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    //
    //通用错误类型00001
    UNKNOWN_ERROR(10002,"未知错误"),
    //
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户登录失败"),
    USER_NOT_LOGIN(20003,"用户还未登录"),

    //库存不足
    STOCK_NOT_ENOUGH(30001,"库存不足");


  private EmBussinessError(int errCode,String errMsg) {
      this.errCode = errCode;
      this.errMsg = errMsg;
  }
    private int errCode;
    private String errMsg;
    @Override
    public int getErrCode() {
        return errCode;
    }
    @Override
    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
