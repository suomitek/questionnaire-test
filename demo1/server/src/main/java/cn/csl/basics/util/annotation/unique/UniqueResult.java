package cn.csl.basics.util.annotation.unique;

public class UniqueResult {
    String attribute;
    String message;
    String value;

    public UniqueResult(String attribute,String message,String value){
        this.attribute = attribute;
        this.message = message;
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
