package com.example.geopagosexercise.bo;

import com.orm.SugarRecord;

public class PaymentMethod extends SugarRecord {

    private String pmId;
    private String pmName;
    private String pmType;

    public PaymentMethod() {
    }

    public PaymentMethod(String pmId, String pmName, String pmType) {
        this.pmId = pmId;
        this.pmName = pmName;
        this.pmType = pmType;
    }

    public String getPmId() {
        return pmId;
    }

    public void setPmId(String pmId) {
        this.pmId = pmId;
    }

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }

    public String getPmType() {
        return pmType;
    }

    public void setPmType(String pmType) {
        this.pmType = pmType;
    }
}
