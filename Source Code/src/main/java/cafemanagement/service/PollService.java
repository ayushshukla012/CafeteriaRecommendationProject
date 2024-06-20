package cafemanagement.service;

import cafemanagement.dao.PollDAO;
import cafemanagement.model.PollItem;

import java.util.Date;
import java.util.List;

public class PollService {
    private final PollDAO pollDAO;

    public PollService() {
        this.pollDAO = new PollDAO();
    }

    public int createPoll(int chefId, Date pollDate) {
        return pollDAO.createPoll(chefId, pollDate);
    }

    public void addItemsToPoll(int pollId, List<Integer> menuItemIds) {
        pollDAO.addItemsToPoll(pollId, menuItemIds);
    }

    public List<PollItem> getPollItemsForToday() {
        return pollDAO.getPollItemsForToday();
    }

    public void castVote(int pollId, int menuItemId, int employeeId) {
        pollDAO.castVote(pollId, menuItemId, employeeId);
    }

    public boolean hasVotedToday(int pollId, int employeeId) {
        return pollDAO.hasVotedToday(pollId, employeeId);
    }

    public void clearVotingResults() {
        pollDAO.clearVotingResults();
    }
}
