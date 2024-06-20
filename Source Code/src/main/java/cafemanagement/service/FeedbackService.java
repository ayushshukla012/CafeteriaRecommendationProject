package cafemanagement.service;

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

    public void updateFeedback(Feedback feedback) {
        feedbackDAO.updateFeedback(feedback);
    }
}
