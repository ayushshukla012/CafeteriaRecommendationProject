// package cafemanagement.dao;

// import cafemanagement.model.UserPreferences;
// import cafemanagement.utils.ConnectionProvider;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// public class UserPreferencesDaoTest {


//     @Mock
//     private ConnectionProvider mockConnectionProvider;

//     @Mock
//     private Connection mockConnection;

//     @Mock
//     private PreparedStatement mockStatement;

//     @Mock
//     private ResultSet mockResultSet;

//     private UserPreferencesDAO userPreferencesDAO;

//     @BeforeEach
//     public void setUp() throws SQLException {
//         MockitoAnnotations.openMocks(this);
//         userPreferencesDAO = new UserPreferencesDAO(mockConnectionProvider);

//         // Provide mock connection when getConnection() is called
//         when(mockConnectionProvider.getConnection()).thenReturn(mockConnection);
//         when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
//         when(mockStatement.executeQuery()).thenReturn(mockResultSet);
//         when(mockResultSet.next()).thenReturn(true).thenReturn(false);
//         when(mockResultSet.getInt("preferenceId")).thenReturn(1);
//         when(mockResultSet.getInt("employeeId")).thenReturn(1);
//         when(mockResultSet.getString("dietaryPreference")).thenReturn("Vegetarian");
//         when(mockResultSet.getString("spiceLevel")).thenReturn("Medium");
//         when(mockResultSet.getString("preferredCuisine")).thenReturn("Indian");
//         when(mockResultSet.getBoolean("sweetTooth")).thenReturn(true);
//     }

//     @AfterEach
//     public void tearDown() throws SQLException {
//         verify(mockConnection, atLeastOnce()).close();
//     }

//     @Test
//     public void testGetPreferencesByEmployeeId_Success() throws SQLException {
//         // Setup
//         int employeeId = 1;

//         // Execution
//         UserPreferences preferences = userPreferencesDAO.getPreferencesByEmployeeId(employeeId);

//         // Verification
//         assertNotNull(preferences);
//         assertEquals(employeeId, preferences.getEmployeeId());
//         assertEquals("Vegetarian", preferences.getDietaryPreference());
//         assertEquals("Medium", preferences.getSpiceLevel());
//         assertEquals("Indian", preferences.getPreferredCuisine());
//         assertTrue(preferences.isSweetTooth());

//         verify(mockConnection, times(1)).prepareStatement(anyString());
//         verify(mockStatement, times(1)).setInt(1, employeeId);
//         verify(mockStatement, times(1)).executeQuery();
//     }

//     @Test
//     public void testGetPreferencesByEmployeeId_NoPreferencesFound() throws SQLException {
//         // Setup
//         int employeeId = 2;
//         when(mockResultSet.next()).thenReturn(false);

//         // Execution
//         UserPreferences preferences = userPreferencesDAO.getPreferencesByEmployeeId(employeeId);

//         // Verification
//         assertNull(preferences);

//         verify(mockConnection, times(1)).prepareStatement(anyString());
//         verify(mockStatement, times(1)).setInt(1, employeeId);
//         verify(mockStatement, times(1)).executeQuery();
//     }

//     @Test
//     public void testUpdateUserPreferences_Success() throws SQLException {
//         // Setup
//         UserPreferences preferences = new UserPreferences(1, "Vegan", "High", "Italian", false);

//         // Execution
//         userPreferencesDAO.updateUserPreferences(preferences);

//         // Verification
//         verify(mockConnection, times(1)).prepareStatement(anyString());
//         verify(mockStatement, times(1)).setString(1, preferences.getDietaryPreference());
//         verify(mockStatement, times(1)).setString(2, preferences.getSpiceLevel());
//         verify(mockStatement, times(1)).setString(3, preferences.getPreferredCuisine());
//         verify(mockStatement, times(1)).setBoolean(4, preferences.isSweetTooth());
//         verify(mockStatement, times(1)).setInt(5, preferences.getEmployeeId());
//         verify(mockStatement, times(1)).executeUpdate();
//     }

//     @Test
//     public void testInsertUserPreferences_Success() throws SQLException {
//         // Setup
//         UserPreferences preferences = new UserPreferences(2, "Vegetarian", "Medium", "Indian", true);

//         // Execution
//         userPreferencesDAO.insertUserPreferences(preferences);

//         // Verification
//         verify(mockConnection, times(1)).prepareStatement(anyString());
//         verify(mockStatement, times(1)).setInt(1, preferences.getEmployeeId());
//         verify(mockStatement, times(1)).setString(2, preferences.getDietaryPreference());
//         verify(mockStatement, times(1)).setString(3, preferences.getSpiceLevel());
//         verify(mockStatement, times(1)).setString(4, preferences.getPreferredCuisine());
//         verify(mockStatement, times(1)).setBoolean(5, preferences.isSweetTooth());
//         verify(mockStatement, times(1)).executeUpdate();
//     }

//     @Test
//     public void testInsertUserPreferences_SQLException() throws SQLException {
//         // Setup
//         UserPreferences preferences = new UserPreferences(3, "Vegetarian", "Medium", "Indian", true);
//         doThrow(SQLException.class).when(mockStatement).executeUpdate();

//         // Verification
//         assertThrows(SQLException.class, () -> userPreferencesDAO.insertUserPreferences(preferences));

//         verify(mockConnection, times(1)).prepareStatement(anyString());
//         verify(mockStatement, times(1)).setInt(1, preferences.getEmployeeId());
//         verify(mockStatement, times(1)).setString(2, preferences.getDietaryPreference());
//         verify(mockStatement, times(1)).setString(3, preferences.getSpiceLevel());
//         verify(mockStatement, times(1)).setString(4, preferences.getPreferredCuisine());
//         verify(mockStatement, times(1)).setBoolean(5, preferences.isSweetTooth());
//     }
// }
