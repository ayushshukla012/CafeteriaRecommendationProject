package cafemanagement.dao;

import cafemanagement.utils.DatabaseUtil;
import cafemanagement.model.Menu;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class MenuItemDAO {
    
    // Breakfast = 1, Lunch = 2, Dinner = 3, Sweets = 6
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

    public boolean storeMenuItem(String itemName, int categoryId, float itemPrice, boolean availability, String spiceLevel, String cuisineType, boolean isSweet, String dietaryPreference) {
        String sql = "INSERT INTO Menu (name, categoryId, price, availability, spiceLevel, cuisineType, isSweet, dietaryPreference) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, categoryId);
            stmt.setFloat(3, itemPrice);
            stmt.setBoolean(4, availability);
            stmt.setString(5, spiceLevel);
            stmt.setString(6, cuisineType);
            stmt.setBoolean(7, isSweet);
            stmt.setString(8, dietaryPreference);
            stmt.executeUpdate();
            System.out.println("Menu item added: " + itemName + " - " + itemPrice);
            return true;
        } catch (SQLException e) {
            System.err.println("Error storing menu item: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMenuInDatabase(int menuId, String newName, float newPrice, boolean newAvailability, String newSpiceLevel, String newCuisineType, boolean newIsSweet, String newDietaryPreference) {
        String updateQuery = "UPDATE Menu SET name = ?, price = ?, availability = ?, spiceLevel = ?, cuisineType = ?, isSweet = ?, dietaryPreference = ? WHERE menuId = ?";
        
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            
            statement.setString(1, newName);
            statement.setFloat(2, newPrice);
            statement.setBoolean(3, newAvailability);
            statement.setString(4, newSpiceLevel);
            statement.setString(5, newCuisineType);
            statement.setBoolean(6, newIsSweet);
            statement.setString(7, newDietaryPreference);
            statement.setInt(8, menuId);
            
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating menu item: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMenuItem(int menuId) {
        String deleteMenuQuery = "DELETE FROM Menu WHERE menuId = ?";
        String deleteNotificationsQuery = "DELETE FROM Notifications WHERE menuItemId = ?";
        String deleteUserNotificationsQuery = "DELETE FROM UserNotifications WHERE notificationId IN (SELECT notificationId FROM Notifications WHERE menuItemId = ?)";
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement deleteMenuStmt = connection.prepareStatement(deleteMenuQuery);
             PreparedStatement deleteNotificationsStmt = connection.prepareStatement(deleteNotificationsQuery);
             PreparedStatement deleteUserNotificationsStmt = connection.prepareStatement(deleteUserNotificationsQuery)) {
            
            connection.setAutoCommit(false);
            deleteUserNotificationsStmt.setInt(1, menuId);
            int userNotificationsDeleted = deleteUserNotificationsStmt.executeUpdate();
            deleteNotificationsStmt.setInt(1, menuId);
            int notificationsDeleted = deleteNotificationsStmt.executeUpdate();
            deleteMenuStmt.setInt(1, menuId);
            int menuDeleted = deleteMenuStmt.executeUpdate();
            if (menuDeleted > 0 && notificationsDeleted >= 0 && userNotificationsDeleted >= 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting menu item with ID " + menuId + ": " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteMultipleItems(List<Integer> menuIds) {
        if (menuIds.isEmpty()) {
            return true;
        }
    
        StringBuilder deleteMenuQuery = new StringBuilder("DELETE FROM Menu WHERE menuId IN (");
        StringBuilder deleteNotificationsQuery = new StringBuilder("DELETE FROM Notifications WHERE menuItemId IN (");
        StringBuilder deleteUserNotificationsQuery = new StringBuilder("DELETE FROM UserNotifications WHERE notificationId IN (SELECT notificationId FROM Notifications WHERE menuItemId IN (");
    
        for (int i = 0; i < menuIds.size(); i++) {
            deleteMenuQuery.append("?");
            deleteNotificationsQuery.append("?");
            deleteUserNotificationsQuery.append("?");
            if (i < menuIds.size() - 1) {
                deleteMenuQuery.append(",");
                deleteNotificationsQuery.append(",");
                deleteUserNotificationsQuery.append(",");
            }
        }
    
        deleteMenuQuery.append(")");
        deleteNotificationsQuery.append(")");
        deleteUserNotificationsQuery.append("))");
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement deleteMenuStmt = connection.prepareStatement(deleteMenuQuery.toString());
             PreparedStatement deleteNotificationsStmt = connection.prepareStatement(deleteNotificationsQuery.toString());
             PreparedStatement deleteUserNotificationsStmt = connection.prepareStatement(deleteUserNotificationsQuery.toString())) {
    
            connection.setAutoCommit(false);
            for (int i = 0; i < menuIds.size(); i++) {
                deleteUserNotificationsStmt.setInt(i + 1, menuIds.get(i));
            }
            int userNotificationsDeleted = deleteUserNotificationsStmt.executeUpdate();
            for (int i = 0; i < menuIds.size(); i++) {
                deleteNotificationsStmt.setInt(i + 1, menuIds.get(i));
            }
            int notificationsDeleted = deleteNotificationsStmt.executeUpdate();
            for (int i = 0; i < menuIds.size(); i++) {
                deleteMenuStmt.setInt(i + 1, menuIds.get(i));
            }
            int menuDeleted = deleteMenuStmt.executeUpdate();
            if (menuDeleted > 0 && notificationsDeleted >= 0 && userNotificationsDeleted >= 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting menu items: " + e.getMessage());
            return false;
        }
    }

    public List<Menu> getMenuItemsDetailsByCategory(int categoryId) {
        List<Menu> menuItems = new ArrayList<>();
        String query = "SELECT menuId, name, categoryId, price, availability, spiceLevel, cuisineType, isSweet, dietaryPreference FROM Menu WHERE categoryId = ?";
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setInt(1, categoryId);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Menu menuItem = new Menu();
                    menuItem.setMenuId(resultSet.getInt("menuId"));
                    menuItem.setName(resultSet.getString("name"));
                    menuItem.setCategoryId(resultSet.getInt("categoryId"));
                    menuItem.setPrice(resultSet.getFloat("price"));
                    menuItem.setAvailability(resultSet.getBoolean("availability"));
                    menuItem.setSpiceLevel(resultSet.getString("spiceLevel"));
                    menuItem.setCuisineType(resultSet.getString("cuisineType"));
                    menuItem.setSweet(resultSet.getBoolean("isSweet"));
                    menuItem.setDietaryPreference(resultSet.getString("dietaryPreference"));
    
                    menuItems.add(menuItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return menuItems;
    }

    public String getMenuNameById(int menuId) {
        String query = "SELECT name FROM Menu WHERE menuId = ?";
        String menuName = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, menuId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                menuName = rs.getString("name");
            }

        } catch (SQLException e) {
            System.err.println("Error fetching menu name: " + e.getMessage());
        }

        return menuName;
    }

    public List<Menu> getAllMenuItems() {
        List<Menu> menuItems = new ArrayList<>();
        String query = "SELECT menuId, name, categoryId, price, availability, spiceLevel, cuisineType, isSweet, dietaryPreference FROM Menu";
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
    
            while (resultSet.next()) {
                Menu menuItem = new Menu();
                menuItem.setMenuId(resultSet.getInt("menuId"));
                menuItem.setName(resultSet.getString("name"));
                menuItem.setCategoryId(resultSet.getInt("categoryId"));
                menuItem.setPrice(resultSet.getFloat("price"));
                menuItem.setAvailability(resultSet.getBoolean("availability"));
                menuItem.setSpiceLevel(resultSet.getString("spiceLevel"));
                menuItem.setCuisineType(resultSet.getString("cuisineType"));
                menuItem.setSweet(resultSet.getBoolean("isSweet"));
                menuItem.setDietaryPreference(resultSet.getString("dietaryPreference"));
    
                menuItems.add(menuItem);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return menuItems;
    }

    public Menu getMenuItemById(int menuId) {
        Menu menuItem = null;
        String query = "SELECT menuId, name, categoryId, price, availability, spiceLevel, cuisineType, isSweet, dietaryPreference FROM Menu WHERE menuId = ?";
    
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setInt(1, menuId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    menuItem = new Menu();
                    menuItem.setMenuId(resultSet.getInt("menuId"));
                    menuItem.setName(resultSet.getString("name"));
                    menuItem.setCategoryId(resultSet.getInt("categoryId"));
                    menuItem.setPrice(resultSet.getFloat("price"));
                    menuItem.setAvailability(resultSet.getBoolean("availability"));
                    menuItem.setSpiceLevel(resultSet.getString("spiceLevel"));
                    menuItem.setCuisineType(resultSet.getString("cuisineType"));
                    menuItem.setSweet(resultSet.getBoolean("isSweet"));
                    menuItem.setDietaryPreference(resultSet.getString("dietaryPreference"));
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return menuItem;
    }

    public int getMenuIdByName(String name) {
        String query = "SELECT menuId FROM Menu WHERE name = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("menuId");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menuId by name: " + e.getMessage());
        }
        return -1;
    }

    public List<String> getAllMenuNames() {
        List<String> menuNames = new ArrayList<>();
        String query = "SELECT name FROM Menu";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                menuNames.add(resultSet.getString("name").toLowerCase());
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all menu names: " + e.getMessage());
        }
        return menuNames;
    }


}
