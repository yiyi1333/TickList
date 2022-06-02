package edu.zjut.zzy.ticklist.android;

public class CalendarAccount {
    private long calID;
    private String displayName;
    private String accountName;
    private String ownerName;


    public CalendarAccount(long calID, String displayName, String accountName, String ownerName) {
        this.calID = calID;
        this.displayName = displayName;
        this.accountName = accountName;
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "CalendarAccount{" +
                "calID=" + calID +
                ", displayName='" + displayName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", ownerName='" + ownerName + '\'' +
                '}';
    }

    public long getCalID() {
        return calID;
    }

    public void setCalID(long calID) {
        this.calID = calID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
