package activity.login.loginactivityusinghttpurlconnectio;

import java.net.FileNameMap;

public class AccountModel {
    private int FIId;
    private String FIName;
    private int AcctId;

    public String getExtendedAccountType() {
        return ExtendedAccountType;
    }

    public void setExtendedAccountType(String extendedAccountType) {
        ExtendedAccountType = extendedAccountType;
    }

    private String ExtendedAccountType;
    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    private String AccountType;


    public int getFIId() {
        return FIId;
    }

    public void setFIId(int FIId) {
        this.FIId = FIId;
    }

    public String getFIName() {
        return FIName;
    }

    public void setFIName(String FIName) {
        this.FIName = FIName;
    }

    public int getAcctId() {
        return AcctId;
    }

    public void setAcctId(int acctId) {
        AcctId = acctId;
    }

}
