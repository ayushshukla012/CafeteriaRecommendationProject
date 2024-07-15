package cafemanagement.service;

import cafemanagement.dao.MenuItemDAO;
import cafemanagement.model.Menu;

import java.util.List;

public class MenuItemService {
    private final MenuItemDAO menuItemDAO;

    public MenuItemService() {
        this.menuItemDAO = new MenuItemDAO();
    }

    public List<String> getMenuItemsByCategory(int categoryId) {
        return menuItemDAO.getMenuItemsByCategory(categoryId);
    }    

    public boolean storeMenuItem(String itemName, int categoryId, float itemPrice, boolean availability, String spiceLevel, String cuisineType, boolean isSweet, String dietaryPreference) {
        return menuItemDAO.storeMenuItem(itemName, categoryId, itemPrice, availability, spiceLevel, cuisineType, isSweet, dietaryPreference);
    }

    public boolean updateMenuInDatabase(int menuId, String newName, float newPrice, boolean newAvailability, String newSpiceLevel, String newCuisineType, boolean newIsSweet, String newDietaryPreference) {
        return menuItemDAO.updateMenuInDatabase(menuId, newName, newPrice, newAvailability, newSpiceLevel, newCuisineType, newIsSweet, newDietaryPreference);
    }

    public List<Menu> getMenuItemsDetailsByCategory(int categoryId) {
        return menuItemDAO.getMenuItemsDetailsByCategory(categoryId);
    }

    public boolean deleteMultipleItems(List<Integer> menuIds) {
        return menuItemDAO.deleteMultipleItems(menuIds);
    }

    public boolean deleteMenuItem(int menuId) {
        return menuItemDAO.deleteMenuItem(menuId);
    }

    public String getMenuNameById(int menuId) {
        return menuItemDAO.getMenuNameById(menuId);
    }

    public List<Menu> getAllMenuItems() {
        return menuItemDAO.getAllMenuItems();
    }

    public Menu getMenuItemById(int menuId) {
        return menuItemDAO.getMenuItemById(menuId);
    }

    public int getMenuIdByName(String name) {
        return menuItemDAO.getMenuIdByName(name);
    }

    public List<String> getAllMenuNames() {
        return menuItemDAO.getAllMenuNames();
    }
}
