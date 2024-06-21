package cafemanagement.model;

public class Recommendation {
    private int recommendationId;
    private int menuId;
    private double averageRating;
    private String sentimentAnalysis;

    public Recommendation(int menuId, double averageRating, String sentimentAnalysis) {
        this.menuId = menuId;
        this.averageRating = averageRating;
        this.sentimentAnalysis = sentimentAnalysis;
    }

    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getSentimentAnalysis() {
        return sentimentAnalysis;
    }

    public void setSentimentAnalysis(String sentimentAnalysis) {
        this.sentimentAnalysis = sentimentAnalysis;
    }
}
