package com.mwbtech.utilityapp.objects;

public class CustomerCreation {

    String ledgerType,firmName,companyType,name,emailID,mobileNumber,mobileNumber2,telephoneNumber;
    String billingAddress,area,city,cityCode,state;
    int pincode;
    double lattitude,langitude;
    String registrationType,tinNumber,panNumber,gstImage,panImage;
    String bankName,bankBranch,accountNo,ifscCode,placeCode,bankCity,signatureImage,comment;
    int crLimit,crDays,noofOutstandingBill,openingBalance,salesmanID,createdByID,orgID;


    public CustomerCreation(){


    }

    public CustomerCreation(String ledgerType, String firmName, String companyType, String name, String emailID, String mobileNumber, String mobileNumber2,String telephoneNumber) {
        this.ledgerType = ledgerType;
        this.firmName = firmName;
        this.companyType = companyType;
        this.name = name;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
    }

    public CustomerCreation(String ledgerType, String firmName, String companyType, String name, String emailID, String mobileNumber, String mobileNumber2, String telephoneNumber,String billingAddress, String area, String city, String cityCode, String state, int pincode, double lattitude, double langitude) {
        this.ledgerType = ledgerType;
        this.firmName = firmName;
        this.companyType = companyType;
        this.name = name;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.billingAddress = billingAddress;
        this.area = area;
        this.city = city;
        this.cityCode = cityCode;
        this.state = state;
        this.pincode = pincode;
        this.lattitude = lattitude;
        this.langitude = langitude;
    }


    public CustomerCreation(String ledgerType, String firmName, String companyType, String name, String emailID, String mobileNumber, String mobileNumber2, String telephoneNumber,String billingAddress, String area, String city, String cityCode, String state, int pincode, double lattitude, double langitude, String registrationType, String tinNumber, String panNumber, String gstImage, String panImage) {
        this.ledgerType = ledgerType;
        this.firmName = firmName;
        this.companyType = companyType;
        this.name = name;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.billingAddress = billingAddress;
        this.area = area;
        this.city = city;
        this.cityCode = cityCode;
        this.state = state;
        this.pincode = pincode;
        this.lattitude = lattitude;
        this.langitude = langitude;
        this.registrationType = registrationType;
        this.tinNumber = tinNumber;
        this.panNumber = panNumber;
        this.gstImage = gstImage;
        this.panImage = panImage;
    }

    public CustomerCreation(String ledgerType, String firmName, String companyType, String name, String emailID, String mobileNumber, String mobileNumber2, String telephoneNumber,String billingAddress, String area, String city, String cityCode, String state, int pincode, double lattitude, double langitude, String registrationType, String tinNumber, String panNumber, String gstImage, String panImage, String bankName, String bankBranch, String accountNo, String ifscCode, String bankCity, String signatureImage, String comment) {
        this.ledgerType = ledgerType;
        this.firmName = firmName;
        this.companyType = companyType;
        this.name = name;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.billingAddress = billingAddress;
        this.area = area;
        this.city = city;
        this.cityCode = cityCode;
        this.state = state;
        this.pincode = pincode;
        this.lattitude = lattitude;
        this.langitude = langitude;
        this.registrationType = registrationType;
        this.tinNumber = tinNumber;
        this.panNumber = panNumber;
        this.gstImage = gstImage;
        this.panImage = panImage;
        this.bankName = bankName;
        this.bankBranch = bankBranch;
        this.accountNo = accountNo;
        this.ifscCode = ifscCode;
        this.placeCode = placeCode;
        this.bankCity = bankCity;
        this.signatureImage = signatureImage;
        this.comment = comment;
    }

    public CustomerCreation(String ledgerType, String firmName, String companyType, String name, String emailID, String mobileNumber, String mobileNumber2, String telephoneNumber,String billingAddress, String area, String city, String cityCode, String state, int pincode, double lattitude, double langitude, String registrationType, String tinNumber, String panNumber, String gstImage, String panImage, String bankName, String bankBranch, String accountNo, String ifscCode, String bankCity, String signatureImage, String comment, int crLimit, int crDays, int noofOutstandingBill, int openingBalance, int salesmanID, int createdByID, int orgID) {
        this.ledgerType = ledgerType;
        this.firmName = firmName;
        this.companyType = companyType;
        this.name = name;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.billingAddress = billingAddress;
        this.area = area;
        this.city = city;
        this.cityCode = cityCode;
        this.state = state;
        this.pincode = pincode;
        this.lattitude = lattitude;
        this.langitude = langitude;
        this.registrationType = registrationType;
        this.tinNumber = tinNumber;
        this.panNumber = panNumber;
        this.gstImage = gstImage;
        this.panImage = panImage;
        this.bankName = bankName;
        this.bankBranch = bankBranch;
        this.accountNo = accountNo;
        this.ifscCode = ifscCode;
        this.bankCity = bankCity;
        this.signatureImage = signatureImage;
        this.comment = comment;
        this.crLimit = crLimit;
        this.crDays = crDays;
        this.noofOutstandingBill = noofOutstandingBill;
        this.openingBalance = openingBalance;
        this.salesmanID = salesmanID;
        this.createdByID = createdByID;
        this.orgID = orgID;
    }


    public String getLedgerType() {
        return ledgerType;
    }

    public String getFirmName() {
        return firmName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public String getName() {
        return name;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getMobileNumber2() {
        return mobileNumber2;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getState() {
        return state;
    }

    public int getPincode() {
        return pincode;
    }

    public double getLattitude() {
        return lattitude;
    }

    public double getLangitude() {
        return langitude;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public String getGstImage() {
        return gstImage;
    }

    public String getPanImage() {
        return panImage;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public String getPlaceCode() {
        return placeCode;
    }

    public String getBankCity() {
        return bankCity;
    }

    public String getSignatureImage() {
        return signatureImage;
    }

    public String getComment() {
        return comment;
    }

    public int getCrLimit() {
        return crLimit;
    }

    public int getCrDays() {
        return crDays;
    }

    public int getNoofOutstandingBill() {
        return noofOutstandingBill;
    }

    public int getOpeningBalance() {
        return openingBalance;
    }

    public int getSalesmanID() {
        return salesmanID;
    }

    public int getCreatedByID() {
        return createdByID;
    }

    public int getOrgID() {
        return orgID;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setLedgerType(String ledgerType) {
        this.ledgerType = ledgerType;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setMobileNumber2(String mobileNumber2) {
        this.mobileNumber2 = mobileNumber2;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public void setLangitude(double langitude) {
        this.langitude = langitude;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public void setGstImage(String gstImage) {
        this.gstImage = gstImage;
    }

    public void setPanImage(String panImage) {
        this.panImage = panImage;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public void setPlaceCode(String placeCode) {
        this.placeCode = placeCode;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public void setSignatureImage(String signatureImage) {
        this.signatureImage = signatureImage;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCrLimit(int crLimit) {
        this.crLimit = crLimit;
    }

    public void setCrDays(int crDays) {
        this.crDays = crDays;
    }

    public void setNoofOutstandingBill(int noofOutstandingBill) {
        this.noofOutstandingBill = noofOutstandingBill;
    }

    public void setOpeningBalance(int openingBalance) {
        this.openingBalance = openingBalance;
    }

    public void setSalesmanID(int salesmanID) {
        this.salesmanID = salesmanID;
    }

    public void setCreatedByID(int createdByID) {
        this.createdByID = createdByID;
    }

    public void setOrgID(int orgID) {
        this.orgID = orgID;
    }

    @Override
    public String toString() {
        return "CustomerCreation{" +
                "ledgerType='" + ledgerType + '\'' +
                ", firmName='" + firmName + '\'' +
                ", companyType='" + companyType + '\'' +
                ", name='" + name + '\'' +
                ", emailID='" + emailID + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", mobileNumber2='" + mobileNumber2 + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", area='" + area + '\'' +
                ", city='" + city + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", state='" + state + '\'' +
                ", pincode=" + pincode +
                ", lattitude=" + lattitude +
                ", langitude=" + langitude +
                ", registrationType='" + registrationType + '\'' +
                ", tinNumber='" + tinNumber + '\'' +
                ", panNumber='" + panNumber + '\'' +
                ", gstImage='" + gstImage + '\'' +
                ", panImage='" + panImage + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankBranch='" + bankBranch + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                ", placeCode='" + placeCode + '\'' +
                ", bankCity='" + bankCity + '\'' +
                ", signatureImage='" + signatureImage + '\'' +
                ", comment='" + comment + '\'' +
                ", crLimit=" + crLimit +
                ", crDays=" + crDays +
                ", noofOutstandingBill=" + noofOutstandingBill +
                ", openingBalance=" + openingBalance +
                ", salesmanID=" + salesmanID +
                ", createdByID=" + createdByID +
                ", orgID=" + orgID +
                '}';
    }
}
