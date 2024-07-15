package cafemanagement.dao;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import cafemanagement.model.PollItem;
import cafemanagement.utils.ConnectionProvider;
import cafemanagement.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PollDaoTest {

    private PollDAO pollDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private ConnectionProvider mockConnectionProvider;

    @Before
    public void setup() throws SQLException {
        pollDAO = new PollDAO();

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        mockConnectionProvider = mock(ConnectionProvider.class);

        DatabaseUtil.setConnectionProvider(mockConnectionProvider);
        when(mockConnectionProvider.getConnection()).thenReturn(mockConnection);

        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(1);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testCreatePoll() throws SQLException {
        int chefId = 1;
        Date pollDate = new Date();

        int pollId = pollDAO.createPoll(chefId, pollDate);

        verify(mockConnection).prepareStatement(anyString(), anyInt());
        verify(mockPreparedStatement).setInt(1, chefId);
        verify(mockPreparedStatement).setDate(2, new java.sql.Date(pollDate.getTime()));
        verify(mockPreparedStatement).executeUpdate();
        verify(mockPreparedStatement).getGeneratedKeys();

        assertEquals(1, pollId);
    }

    @Test
    public void testAddItemsToPoll() throws SQLException {
        int pollId = 1;
        List<Integer> menuItemIds = List.of(1, 2, 3);

        pollDAO.addItemsToPoll(pollId, menuItemIds);

        verify(mockConnection, times(menuItemIds.size())).prepareStatement(anyString());
        verify(mockPreparedStatement, times(menuItemIds.size())).setInt(1, pollId);
        verify(mockPreparedStatement, times(menuItemIds.size())).setInt(2, anyInt());
        verify(mockPreparedStatement, times(menuItemIds.size())).addBatch();
        verify(mockPreparedStatement).executeBatch();
    }

    @Test
    public void testGetPollItemsForToday() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("pollItemId")).thenReturn(1);
        when(mockResultSet.getInt("pollId")).thenReturn(1);
        when(mockResultSet.getInt("menuItemId")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Menu Item");

        List<PollItem> pollItems = pollDAO.getPollItemsForToday();

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();

        assertEquals(1, pollItems.size());
        assertEquals("Menu Item", pollItems.get(0).getItemName());
    }

    @Test
    public void testCastVote() throws SQLException {
        int pollId = 1;
        int menuItemId = 1;
        int employeeId = 1;

        pollDAO.castVote(pollId, menuItemId, employeeId);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setInt(1, pollId);
        verify(mockPreparedStatement).setInt(2, menuItemId);
        verify(mockPreparedStatement).setInt(3, employeeId);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testHasVotedToday() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("count")).thenReturn(1);

        boolean hasVoted = pollDAO.hasVotedToday(1, 1);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setInt(2, 1);
        verify(mockPreparedStatement).executeQuery();

        assertTrue(hasVoted);
    }

    @Test
    public void testClearVotingResults() throws SQLException {
        pollDAO.clearVotingResults();

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeUpdate();
    }
}
