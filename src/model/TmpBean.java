package model;

public class TmpBean {
    float num;
    String type;

    public TmpBean(float num, String type) {
        this.num = num;
        this.type = type;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public String getType() {
        if (type.equals("sp"))
            return type;
        else {
            return "dp";
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}
