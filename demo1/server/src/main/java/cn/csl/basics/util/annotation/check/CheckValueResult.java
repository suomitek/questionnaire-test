package cn.csl.basics.util.annotation.check;

public class CheckValueResult {
    public String attributeName;
    public String message;

    public CheckValueResult(String attributeName,String message){
        this.attributeName = attributeName;
        this.message = message;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
