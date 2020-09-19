package cn.csl.basics.model;

public class ResultResponse {

    //返回结果信息
    private String resultMsg;
    //返回结果码200：返回正常，400：返回异常 401权限不足
    private String resultCode;
    private Object data;

    public String getResultMsg() {
        return resultMsg;
    }
    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
    public String getResultCode() {
        return resultCode;
    }
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setSuccessResult(Object data){
        this.resultCode="200";
        this.data = data;
    }
    public void setFailureResult(String resultMsg){
        this.resultMsg = resultMsg;
        this.resultCode="400";
    }
    public boolean isError(){
        if("400".equals(resultCode)){
            return true;
        }
        return false;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

}
