package com.cafeteriamanagement.service;

import com.cafeteriamanagement.dto.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getAllMenus() throws Exception;
    Menu getMenuById(int menuId) throws Exception;
    // Other methods as needed
}
