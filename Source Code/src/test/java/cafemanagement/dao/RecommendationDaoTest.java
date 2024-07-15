package cafemanagement.dao;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import cafemanagement.model.Recommendation;
import cafemanagement.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecommendationDaoTest {

    private RecommendationDAO recommendationDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @Before
    public void setup() throws SQLException {
        recommendationDAO = new RecommendationDAO();

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        DatabaseUtil.setConnectionProvider(() -> mockConnection);
    }

    @Test
    public void testSaveRecommendation_Success() throws SQLException {
        Recommendation recommendation = new Recommendation(1, 4.5, "Positive");

        recommendationDAO.saveRecommendation(recommendation);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setInt(1, recommendation.getMenuId());
        verify(mockPreparedStatement).setDouble(2, recommendation.getAverageRating());
        verify(mockPreparedStatement).setString(3, recommendation.getSentimentAnalysis());
        verify(mockPreparedStatement).executeUpdate();

        verifyNoMoreInteractions(mockConnection, mockPreparedStatement);
    }

    @Test
    public void testSaveRecommendation_SQLException() throws SQLException {
        Recommendation recommendation = new Recommendation(2, 3.8, "Neutral");

        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);

        recommendationDAO.saveRecommendation(recommendation);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setInt(1, recommendation.getMenuId());
        verify(mockPreparedStatement).setDouble(2, recommendation.getAverageRating());
        verify(mockPreparedStatement).setString(3, recommendation.getSentimentAnalysis());
        verify(mockPreparedStatement).executeUpdate();

        verifyNoMoreInteractions(mockConnection, mockPreparedStatement);
    }

    @Test
    public void testSaveRecommendation_NullRecommendation() {
        recommendationDAO.saveRecommendation(null);

        verifyNoMoreInteractions(mockConnection, mockPreparedStatement);
    }
}
