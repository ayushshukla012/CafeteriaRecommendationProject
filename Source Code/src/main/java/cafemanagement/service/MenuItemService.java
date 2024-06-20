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

    public boolean storeMenuItem(String itemName, int categoryId, float itemPrice, boolean availability) {
        return menuItemDAO.storeMenuItem(itemName, categoryId, itemPrice, availability);
    }

    public boolean updateMenuInDatabase(String itemName, String newItemName, float newItemPrice) {
        return menuItemDAO.updateMenuInDatabase(itemName, newItemName, newItemPrice);
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
}
