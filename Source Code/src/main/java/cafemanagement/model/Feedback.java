package cafemanagement.model;

import java.util.Date;

public class Feedback {
    private int feedbackId;
    private int employeeId;
    private int menuId;
    private int quality;
    private int valueForMoney;
    private int quantity;
    private int taste;
    private int rating;
    private String comment;
    private Date feedbackDate;

    public Feedback() {
    }

    public Feedback(int feedbackId, int employeeId, int menuId, int quality, int valueForMoney, 
        int quantity, int taste, int rating, String comment, Date feedbackDate) {
        this.feedbackId = feedbackId;
        this.employeeId = employeeId;
        this.menuId = menuId;
        this.quality = quality;
        this.valueForMoney = valueForMoney;
        this.quantity = quantity;
        this.taste = taste;
        this.rating = rating;
        this.comment = comment;
        this.feedbackDate = feedbackDate;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getValueForMoney() {
        return valueForMoney;
    }

    public void setValueForMoney(int valueForMoney) {
        this.valueForMoney = valueForMoney;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTaste() {
        return taste;
    }

    public void setTaste(int taste) {
        this.taste = taste;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", employeeId=" + employeeId +
                ", menuId=" + menuId +
                ", quality=" + quality +
                ", valueForMoney=" + valueForMoney +
                ", quantity=" + quantity +
                ", taste=" + taste +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", feedbackDate='" + feedbackDate + '\'' +
                '}';
    }
}
