package cafemanagement.dao;

import cafemanagement.utils.DatabaseUtil;
import cafemanagement.model.Menu;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class MenuItemDAO {
    
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
            System.err.println("Error fetching menu items: " + e.getMessage());
        }

        return menuItems;
    }

    public boolean storeMenuItem(String itemName, int categoryId, float itemPrice, boolean availability) {
        String sql = "INSERT INTO Menu (name, categoryId, price, availability) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, categoryId);
            stmt.setFloat(3, itemPrice);
            stmt.setBoolean(4, availability);
            stmt.executeUpdate();
            System.out.println("Menu item added: " + itemName + " - " + itemPrice);
            return true;
        } catch (SQLException e) {
            System.err.println("Error storing menu item: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMenuInDatabase(String itemName, String newItemName, float newItemPrice) {
        String updateQuery = "UPDATE Menu SET name = ?, price = ? WHERE name = ?";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            
            statement.setString(1, newItemName);
            statement.setFloat(2, newItemPrice);
            statement.setString(3, itemName);
            
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating menu item: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMenuItem(int menuId) {
        String deleteQuery = "DELETE FROM Menu WHERE menuId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, menuId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting menu item with ID " + menuId + ": " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMultipleItems(List<Integer> menuIds) {
        StringBuilder deleteQuery = new StringBuilder("DELETE FROM Menu WHERE menuId IN (");
        for (int i = 0; i < menuIds.size(); i++) {
            deleteQuery.append("?");
            if (i < menuIds.size() - 1) {
                deleteQuery.append(",");
            }
        }
        deleteQuery.append(")");
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery.toString())) {
            for (int i = 0; i < menuIds.size(); i++) {
                statement.setInt(i + 1, menuIds.get(i));
            }
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting menu items: " + e.getMessage());
            return false;
        }
    }

    public List<Menu> getMenuItemsDetailsByCategory(int categoryId) {
        List<Menu> menuItems = new ArrayList<>();
        String query = "SELECT menuId, name, categoryId, price, availability FROM Menu WHERE categoryId = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            // Set the categoryId parameter in the prepared statement
            statement.setInt(1, categoryId);
            
            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Menu menuItem = new Menu();
                    menuItem.setMenuId(resultSet.getInt("menuId"));
                    menuItem.setName(resultSet.getString("name"));
                    menuItem.setCategoryId(resultSet.getInt("categoryId"));
                    menuItem.setPrice(resultSet.getFloat("price"));
                    menuItem.setAvailability(resultSet.getBoolean("availability"));
                    
                    // Add menuItem to list
                    menuItems.add(menuItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle properly in your application, e.g., logging
        }
        
        return menuItems;
    }
}
