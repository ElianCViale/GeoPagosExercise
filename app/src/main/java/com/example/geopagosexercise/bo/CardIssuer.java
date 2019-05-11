package com.example.geopagosexercise.bo;

import com.orm.SugarRecord;

public class CardIssuer extends SugarRecord {
    private int ciId;
    private String ciName;

    public CardIssuer() {
    }

    public CardIssuer(int ciId, String ciName) {
        this.ciId = ciId;
        this.ciName = ciName;
    }

    public int getCiId() {
        return ciId;
    }

    public void setCiId(int ciId) {
        this.ciId = ciId;
    }

    public String getCiName() {
        return ciName;
    }

    public void setCiName(String ciName) {
        this.ciName = ciName;
    }
}
