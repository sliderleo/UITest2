package com.example.uitest;

public class CompanyInfo {
    private String companyname,towdriverid,insurance,pdfurl,name,status;

    public CompanyInfo(String companyname,String name, String towdriverid, String insurance, String pdfurl,String status) {
        this.companyname = companyname;
        this.towdriverid = towdriverid;
        this.insurance = insurance;
        this.pdfurl = pdfurl;
        this.name=name;
        this.status=status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
