package edu.zjut.zzy.ticklist.bean;

public class RankInfo {
    private int rankNumber;
    private String userName;
    private int totalFocusTime;
    private String imageUrl;
    private String email;

    @Override
    public String toString() {
        return "RankInfo{" +
                "rankNumber=" + rankNumber +
                ", userName='" + userName + '\'' +
                ", totalFocusTime=" + totalFocusTime +
                ", imageUrl='" + imageUrl + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public int getRankNumber() {
        return rankNumber;
    }

    public void setRankNumber(int rankNumber) {
        this.rankNumber = rankNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalFocusTime() {
        return totalFocusTime;
    }

    public void setTotalFocusTime(int totalFocusTime) {
        this.totalFocusTime = totalFocusTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
