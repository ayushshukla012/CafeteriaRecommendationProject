package cafemanagement.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import cafemanagement.model.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MenuItemDaoTest {

    private MenuItemDAO menuItemDAO;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @Before
    public void setup() throws SQLException {
        menuItemDAO = new MenuItemDAO();

        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testGetMenuItemsByCategory() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("name")).thenReturn("Item1", "Item2");

        List<String> menuItems = menuItemDAO.getMenuItemsByCategory(1);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).setInt(1, 1);
        verify(mockStatement).executeQuery();

        assertEquals(2, menuItems.size());
        assertEquals("Item1", menuItems.get(0));
        assertEquals("Item2", menuItems.get(1));
    }

    @Test
    public void testStoreMenuItem() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        boolean stored = menuItemDAO.storeMenuItem("Item1", 1, 10.5f, true);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).setString(1, "Item1");
        verify(mockStatement).setInt(2, 1);
        verify(mockStatement).setFloat(3, 10.5f);
        verify(mockStatement).setBoolean(4, true);
        verify(mockStatement).executeUpdate();

        assertTrue(stored);
    }

    @Test
    public void testUpdateMenuInDatabase() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        boolean updated = menuItemDAO.updateMenuInDatabase("Item1", "NewName", 12.5f);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).setString(1, "NewName");
        verify(mockStatement).setFloat(2, 12.5f);
        verify(mockStatement).setString(3, "Item1");
        verify(mockStatement).executeUpdate();

        assertTrue(updated);
    }

    @Test
    public void testDeleteMenuItem() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        boolean deleted = menuItemDAO.deleteMenuItem(1);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).setInt(1, 1);
        verify(mockStatement).executeUpdate();

        assertTrue(deleted);
    }

    @Test
    public void testDeleteMultipleItems() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(2);

        List<Integer> menuIds = List.of(1, 2);
        boolean deleted = menuItemDAO.deleteMultipleItems(menuIds);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement, times(2)).setInt(anyInt(), anyInt());
        verify(mockStatement).executeUpdate();

        assertTrue(deleted);
    }

    @Test
    public void testGetMenuItemsDetailsByCategory() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("menuId")).thenReturn(1, 2);
        when(mockResultSet.getString("name")).thenReturn("Item1", "Item2");

        List<Menu> menuItems = menuItemDAO.getMenuItemsDetailsByCategory(1);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).setInt(1, 1);
        verify(mockStatement).executeQuery();

        assertEquals(2, menuItems.size());
        assertEquals("Item1", menuItems.get(0).getName());
        assertEquals("Item2", menuItems.get(1).getName());
    }

    @Test
    public void testGetMenuNameById() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("name")).thenReturn("Item1");

        String menuName = menuItemDAO.getMenuNameById(1);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).setInt(1, 1);
        verify(mockStatement).executeQuery();

        assertEquals("Item1", menuName);
    }

    @Test
    public void testGetAllMenuItems() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("menuId")).thenReturn(1, 2);
        when(mockResultSet.getString("name")).thenReturn("Item1", "Item2");

        List<Menu> menuItems = menuItemDAO.getAllMenuItems();

        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).executeQuery();

        assertEquals(2, menuItems.size());
        assertEquals("Item1", menuItems.get(0).getName());
        assertEquals("Item2", menuItems.get(1).getName());
    }

}
