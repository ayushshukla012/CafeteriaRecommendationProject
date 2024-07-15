package cafemanagement.service;

import java.util.List;

import cafemanagement.dao.FeedbackDAO;
import cafemanagement.model.Feedback;

public class FeedbackService {
    private final FeedbackDAO feedbackDAO;

    public FeedbackService() {
        this.feedbackDAO = new FeedbackDAO();
    }

    public Feedback getFeedbackByEmployeeAndMenu(int employeeId, int menuId) {
        return feedbackDAO.getFeedbackByEmployeeAndMenu(employeeId, menuId);
    }

    public void storeFeedback(Feedback feedback) {
        feedbackDAO.storeFeedback(feedback);
    }

    public boolean updateFeedback(Feedback feedback) {
        return feedbackDAO.updateFeedback(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackDAO.getAllFeedback();
    }

    public Feedback getFeedbackById(int feedbackId) {
        return getFeedbackById(feedbackId);
    }

    public void saveFeedback(int notificationId, int employeeId, String question, String response) {
        feedbackDAO.saveFeedback(notificationId, employeeId, question, response);
    }
}
