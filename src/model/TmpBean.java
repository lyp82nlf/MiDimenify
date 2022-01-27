package model;

public class TmpBean {
    float num;
    String type;
    Dimen dimen;

    public TmpBean(float num, String type, Dimen dimen) {
        this.num = num;
        this.type = type;
        this.dimen = dimen;
    }

    public Dimen getDimen() {
        return dimen;
    }

    public float getNum() {
        return num;
    }

    public float getFactorDp(){
        return dimen.getFactorDp();
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

    public String getCalculateType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
