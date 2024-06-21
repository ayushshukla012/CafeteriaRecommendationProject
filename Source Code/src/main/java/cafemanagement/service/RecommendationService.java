package cafemanagement.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import cafemanagement.dao.FeedbackDAO;
import cafemanagement.dao.RecommendationDAO;
import cafemanagement.model.Feedback;
import cafemanagement.model.Recommendation;

public class RecommendationService {

    private final FeedbackDAO feedbackDAO = new FeedbackDAO();
    private final RecommendationDAO recommendationDAO = new RecommendationDAO();
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

    public void recommendFood() {
        List<Feedback> feedbacks = feedbackDAO.getAllFeedback();
        Map<Integer, List<Feedback>> feedbackByMenu = groupFeedbackByMenuId(feedbacks);

        for (Map.Entry<Integer, List<Feedback>> entry : feedbackByMenu.entrySet()) {
            int menuId = entry.getKey();
            List<Feedback> menuFeedbacks = entry.getValue();
            double averageRating = calculateFeedbackAverage(menuFeedbacks);

            executorService.submit(() -> {
                String sentiment = calculateSentiment(menuFeedbacks);
                Recommendation recommendation = new Recommendation(menuId, averageRating, sentiment);
                System.out.println("MenuID: " + recommendation.getMenuId() +
                                "Average Rating: " + recommendation.getAverageRating() +
                                "Sentiment Analysis:" + recommendation.getSentimentAnalysis());
                recommendationDAO.saveRecommendation(recommendation);

            });
        }
        executorService.shutdown();
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

    private String calculateSentiment(List<Feedback> feedbacks) {
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
