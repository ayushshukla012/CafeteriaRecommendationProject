package cafemanagement.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import cafemanagement.dao.FeedbackDAO;
import cafemanagement.model.Feedback;
import cafemanagement.model.Recommendation;
import cafemanagement.model.Menu;

public class RecommendationService {

    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final MenuItemService menuItemService = new MenuItemService();
    private final List<String> positiveWords;
    private final List<String> negativeWords;
    private final ExecutorService executorService;

    public RecommendationService() throws IOException {
        positiveWords = loadWordsFromFile("positive-words.txt");
        negativeWords = loadWordsFromFile("negative-words.txt");
        executorService = Executors.newFixedThreadPool(10);
    }

    private List<String> loadWordsFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(filename), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    public List<Map<String, Object>> recommendFood(int menuCategoryId) throws InterruptedException {
        List<Feedback> feedbacks = feedbackDAO.getAllFeedback();
        Map<Integer, List<Feedback>> feedbackByMenu = groupFeedbackByMenuId(feedbacks);
        List<Menu> menuItems = menuItemService.getMenuItemsDetailsByCategory(menuCategoryId);
        List<Map<String, Object>> recommendedItems = new ArrayList<>();

        List<Recommendation> recommendations = new ArrayList<>();

        for (Menu menuItem : menuItems) {
            int menuId = menuItem.getMenuId();
            List<Feedback> menuFeedbacks = feedbackByMenu.getOrDefault(menuId, new ArrayList<>());
            double averageRating = calculateFeedbackAverage(menuFeedbacks);
            String sentiment = calculateSentiment(menuFeedbacks);

            recommendations.add(new Recommendation(menuId, averageRating, sentiment));
        }

        recommendations.sort(Comparator.comparingDouble(Recommendation::getAverageRating).reversed());

        List<Recommendation> veryPositiveRecommendations = recommendations.stream()
                .filter(r -> "very positive".equals(r.getSentimentAnalysis()))
                .limit(5)
                .collect(Collectors.toList());

        if (veryPositiveRecommendations.size() < 5) {
            List<Recommendation> positiveRecommendations = recommendations.stream()
                    .filter(r -> "positive".equals(r.getSentimentAnalysis()))
                    .limit(5 - veryPositiveRecommendations.size())
                    .collect(Collectors.toList());

            veryPositiveRecommendations.addAll(positiveRecommendations);
        }

        for (Recommendation recommendation : veryPositiveRecommendations) {
            Menu menuItem = menuItemService.getMenuItemById(recommendation.getMenuId());

            Map<String, Object> menuItemWithDetails = new HashMap<>();
            menuItemWithDetails.put("menuId", menuItem.getMenuId());
            menuItemWithDetails.put("name", menuItem.getName());
            menuItemWithDetails.put("categoryId", menuItem.getCategoryId());
            menuItemWithDetails.put("price", menuItem.getPrice());
            menuItemWithDetails.put("availability", menuItem.isAvailability());
            menuItemWithDetails.put("averageRating", recommendation.getAverageRating());
            menuItemWithDetails.put("sentiment", recommendation.getSentimentAnalysis());

            recommendedItems.add(menuItemWithDetails);
        }

        return recommendedItems;
    }

    private Map<Integer, List<Feedback>> groupFeedbackByMenuId(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .collect(Collectors.groupingBy(Feedback::getMenuId));
    }

    private double calculateFeedbackAverage(List<Feedback> feedbacks) {
        double averageQuality = calculateAverageQuality(feedbacks);
        double averageValueForMoney = calculateAverageValueForMoney(feedbacks);
        double averageQuantity = calculateAverageQuantity(feedbacks);
        double averageTaste = calculateAverageTaste(feedbacks);
        double averageRating = calculateAverageRating(feedbacks);

        return (averageQuality + averageValueForMoney + averageQuantity + averageTaste + averageRating) / 5;
    }

    public String calculateSentiment(List<Feedback> feedbacks) {
        int positiveCount = 0;
        int negativeCount = 0;

        for (Feedback feedback : feedbacks) {
            String[] words = feedback.getComment().split("\\s+");
            for (String word : words) {
                if (positiveWords.contains(word)) {
                    positiveCount++;
                } else if (negativeWords.contains(word)) {
                    negativeCount++;
                }
            }
        }
        int totalWords = positiveCount + negativeCount;
        double sentimentScore = (double) (positiveCount - negativeCount) / totalWords;

        if (sentimentScore >= 0.5) {
            return "very positive";
        } else if (sentimentScore > 0) {
            return "positive";
        } else if (sentimentScore == 0) {
            return "neutral";
        } else if (sentimentScore > -0.5) {
            return "negative";
        } else {
            return "very negative";
        }
    }

    public String calculateUserFeedbackSentiment(String feedback) {
        int positiveCount = 0;
        int negativeCount = 0;

        String[] words = feedback.split("\\s+");
        for (String word : words) {
            if (positiveWords.contains(word)) {
                positiveCount++;
            } else if (negativeWords.contains(word)) {
                negativeCount++;
            }
        }
        int totalWords = positiveCount + negativeCount;
        double sentimentScore = (double) (positiveCount - negativeCount) / totalWords;

        if (sentimentScore >= 0.5) {
            return "very positive";
        } else if (sentimentScore > 0) {
            return "positive";
        } else if (sentimentScore == 0) {
            return "neutral";
        } else if (sentimentScore > -0.5) {
            return "negative";
        } else {
            return "very negative";
        }
    }

    // Define helper methods
    private double calculateAverageQuality(List<Feedback> feedbacks) {
        return calculateAverage(feedbacks, Feedback::getQuality);
    }

    private double calculateAverageValueForMoney(List<Feedback> feedbacks) {
        return calculateAverage(feedbacks, Feedback::getValueForMoney);
    }

    private double calculateAverageQuantity(List<Feedback> feedbacks) {
        return calculateAverage(feedbacks, Feedback::getQuantity);
    }

    private double calculateAverageTaste(List<Feedback> feedbacks) {
        return calculateAverage(feedbacks, Feedback::getTaste);
    }

    private double calculateAverageRating(List<Feedback> feedbacks) {
        return calculateAverage(feedbacks, Feedback::getRating);
    }

    private double calculateAverage(List<Feedback> feedbacks, ToIntFunction<Feedback> mapper) {
        return feedbacks.stream()
                .mapToInt(mapper)
                .average()
                .orElse(0);
    }

    public void awaitTermination() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

}
