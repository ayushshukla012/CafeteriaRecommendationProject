package com.cafeteriamanagement.dao;

import com.cafeteriamanagement.dto.Menu;

import java.util.List;

public interface MenuDao {
    List<Menu> getAllMenus() throws Exception;
    Menu getMenuById(int menuId) throws Exception;
    // Other methods as needed
}
