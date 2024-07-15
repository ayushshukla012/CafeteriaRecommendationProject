package cafemanagement.dao;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import cafemanagement.model.User;
import cafemanagement.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoTest {

    private UserDAO userDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @Before
    public void setup() throws SQLException {
        userDAO = new UserDAO();

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        DatabaseUtil.setConnectionProvider(() -> mockConnection);
    }

    @Test
    public void testAuthenticate_Success() throws SQLException {
        int userId = 1;
        String password = "password";
        String roleName = "employee";
        User expectedUser = new User(userId, "John Doe", password, roleName);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("employeeId")).thenReturn(userId);
        when(mockResultSet.getString("name")).thenReturn("John Doe");
        when(mockResultSet.getString("password")).thenReturn(password);
        when(mockResultSet.getString("roleName")).thenReturn(roleName);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        User authenticatedUser = userDAO.authenticate(userId, password, roleName);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setInt(1, userId);
        verify(mockPreparedStatement).setString(2, password);
        verify(mockPreparedStatement).setString(3, roleName);
        verify(mockPreparedStatement).executeQuery();

        assertNotNull(authenticatedUser);
        assertEquals(expectedUser, authenticatedUser);
    }

    @Test
    public void testAuthenticate_UserNotFound() throws SQLException {
        int userId = 2;
        String password = "password";
        String roleName = "employee";

        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        User authenticatedUser = userDAO.authenticate(userId, password, roleName);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setInt(1, userId);
        verify(mockPreparedStatement).setString(2, password);
        verify(mockPreparedStatement).setString(3, roleName);
        verify(mockPreparedStatement).executeQuery();

        assertNull(authenticatedUser);
    }

    @Test
    public void testGetUserByUsername_Success() throws SQLException {
        String username = "John Doe";
        User expectedUser = new User(1, username, "password", "employee");

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("employeeId")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn(username);
        when(mockResultSet.getString("password")).thenReturn("password");
        when(mockResultSet.getString("roleName")).thenReturn("employee");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        User user = userDAO.getUserByUsername(username);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setString(1, username);
        verify(mockPreparedStatement).executeQuery();

        assertNotNull(user);
        assertEquals(expectedUser, user);
    }

    @Test
    public void testGetUserByUsername_UserNotFound() throws SQLException {
        String username = "NonExistingUser";

        when(mockResultSet.next()).thenReturn(false); // Simulate user not found
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        User user = userDAO.getUserByUsername(username);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setString(1, username);
        verify(mockPreparedStatement).executeQuery();

        assertNull(user);
    }

    @Test
    public void testGetAllEmployeeIds() throws SQLException {
        List<Integer> expectedEmployeeIds = List.of(1, 2, 3);

        when(mockResultSet.next()).thenReturn(true, true, true, false);
        when(mockResultSet.getInt("employeeId")).thenReturn(1, 2, 3);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        List<Integer> employeeIds = userDAO.getAllEmployeeIds();

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();

        assertEquals(expectedEmployeeIds, employeeIds);
    }

    @Test
    public void testLogUserAttempt() throws SQLException {
        int userId = 1;
        String status = "Success";

        userDAO.logUserAttempt(userId, status);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setInt(1, userId);
        verify(mockPreparedStatement).setString(2, status);
        verify(mockPreparedStatement).executeUpdate();
    }
}
