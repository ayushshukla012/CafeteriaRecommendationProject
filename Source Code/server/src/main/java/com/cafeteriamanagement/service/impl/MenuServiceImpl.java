package com.cafeteriamanagement.service.impl;

import com.cafeteriamanagement.dto.Menu;
import com.cafeteriamanagement.service.MenuService;
import com.cafeteriamanagement.dao.MenuDao;

import java.util.List;

public class MenuServiceImpl implements MenuService {

    private final MenuDao menuDao;

    public MenuServiceImpl(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    public List<Menu> getAllMenus() throws Exception {
        return menuDao.getAllMenus();
    }

    @Override
    public Menu getMenuById(int menuId) throws Exception {
        return menuDao.getMenuById(menuId);
    }
    // Implement other methods as needed
}
