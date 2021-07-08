package model;

public class Dimen {

    private String bucket;
    private String directory;
    private float factorSp;
    private float factorDp;
    private float factorPx;
    private boolean isSelected;
    private boolean isMandatory;

    public String getBucket() {
        return bucket;
    }

    public Dimen setBucket(String bucket) {
        this.bucket = bucket;
        if (this.bucket.equals("mdpi")) {
            this.directory = "values";
        } else {
            this.directory = "values-" + bucket;
        }
        return this;
    }

    public float getFactorSp() {
        return factorSp;
    }

    public float getFactorPx() {
        return factorPx;
    }

    public Dimen setFactorSp(float factorSp) {
        this.factorSp = factorSp;
        return this;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Dimen setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public Dimen setMandatory(boolean mandatory) {
        isMandatory = mandatory;
        return this;
    }

    public float getFactorDp() {
        return factorDp;
    }

    public Dimen setFactorDp(float factorDp) {
        this.factorDp = factorDp;
        return this;
    }

    public Dimen setFactorPx(float factorPx) {
        this.factorPx = factorDp;
        return this;
    }

    public float getFactor() {
        if (factorDp != 0) {
            return factorDp;
        } else if (factorPx != 0) {
            return factorPx;
        }
        return factorSp;
    }


    public String getDirectory() {
        return directory;
    }

}
