package com.example.geopagosexercise.bo;

import com.orm.SugarRecord;

public class Currency  extends SugarRecord {
    private String cSymbol;
    private String cCountry;

    public Currency() {
    }

    public Currency(String cSymbol, String cCountry) {
        this.cSymbol = cSymbol;
        this.cCountry = cCountry;
    }

    public String getcSymbol() {
        return cSymbol;
    }

    public void setcSymbol(String cSymbol) {
        this.cSymbol = cSymbol;
    }

    public String getcCountry() {
        return cCountry;
    }

    public void setcCountry(String cCountry) {
        this.cCountry = cCountry;
    }
}
