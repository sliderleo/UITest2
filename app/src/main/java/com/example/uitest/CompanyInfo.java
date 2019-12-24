package com.example.uitest;

public class CompanyInfo {
    private String companyname,towdriverid,insurance,pdfurl;

    public CompanyInfo(String companyname, String towdriverid, String insurance, String pdfurl) {
        this.companyname = companyname;
        this.towdriverid = towdriverid;
        this.insurance = insurance;
        this.pdfurl = pdfurl;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getTowdriverid() {
        return towdriverid;
    }

    public void setTowdriverid(String towdriverid) {
        this.towdriverid = towdriverid;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }
}
