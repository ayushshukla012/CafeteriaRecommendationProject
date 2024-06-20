package cafemanagement.dao;

import cafemanagement.model.PollItem;
import cafemanagement.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PollDAO {

    // Breakfast = 1, Lunch = 2, Dinner = 3
    public List<String> getMenuItemsByCategory(int categoryId) {
        List<String> menuItems = new ArrayList<>();
        String query = "SELECT name FROM Menu WHERE categoryId = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                menuItems.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuItems;
    }

    // Method to create a new poll
    public int createPoll(int chefId, Date pollDate) {
        String insertPollQuery = "INSERT INTO Polls (chefId, pollDate) VALUES (?, ?)";
        int pollId = -1;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertPollQuery, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, chefId);
            pstmt.setDate(2, new java.sql.Date(pollDate.getTime()));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                pollId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pollId;
    }

    // Method to add items to a poll
    public void addItemsToPoll(int pollId, List<Integer> menuItemIds) {
        String insertPollItemQuery = "INSERT INTO PollItems (pollId, menuItemId) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertPollItemQuery)) {

            for (int menuItemId : menuItemIds) {
                pstmt.setInt(1, pollId);
                pstmt.setInt(2, menuItemId);
                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PollItem> getPollItemsForToday() {
        List<PollItem> pollItems = new ArrayList<>();
        String selectQuery = "SELECT pi.pollItemId, pi.pollId, pi.menuItemId, m.name " +
                             "FROM PollItems pi " +
                             "JOIN Polls p ON pi.pollId = p.pollId " +
                             "JOIN Menu m ON pi.menuItemId = m.menuId " +
                             "WHERE DATE(p.pollDate) = CURDATE() " +
                             "AND p.pollId = (SELECT MAX(pollId) FROM Polls WHERE DATE(pollDate) = CURDATE())";
    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectQuery);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                int pollItemId = rs.getInt("pollItemId");
                int pollId = rs.getInt("pollId");
                int menuItemId = rs.getInt("menuItemId");
                String itemName = rs.getString("name");
    
                PollItem pollItem = new PollItem(pollItemId, pollId, menuItemId, itemName);
                pollItems.add(pollItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pollItems;
    }

    // Method to cast a vote
    public void castVote(int pollId, int menuItemId, int employeeId) {
        String insertVoteQuery = "INSERT INTO Votes (pollId, menuItemId, employeeId) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertVoteQuery)) {

            pstmt.setInt(1, pollId);
            pstmt.setInt(2, menuItemId);
            pstmt.setInt(3, employeeId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasVotedToday(int pollId, int employeeId) {
        String checkVoteQuery = "SELECT COUNT(*) AS count FROM Votes WHERE pollId = ? AND employeeId = ? AND DATE(voteDate) = CURDATE()";
        boolean hasVoted = false;
    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkVoteQuery)) {
    
            pstmt.setInt(1, pollId);
            pstmt.setInt(2, employeeId);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                int count = rs.getInt("count");
                if (count > 0) {
                    hasVoted = true;
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return hasVoted;
    }

    // Method to clear voting results before new poll
    public void clearVotingResults() {
        String deleteVotesQuery = "DELETE FROM Votes WHERE voteDate < CURDATE()";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteVotesQuery)) {

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}