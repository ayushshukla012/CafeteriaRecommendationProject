package com.cafeteriamanagement.dao.impl;

import com.cafeteriamanagement.dto.Menu;
import com.cafeteriamanagement.dao.MenuDao;
import com.cafeteriamanagement.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuDaoImpl implements MenuDao {

    private final ConnectionPool connectionPool;

    public MenuDaoImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Menu> getAllMenus() throws Exception {
        List<Menu> menus = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = connectionPool.getConnection(); // Use instance reference
            String query = "SELECT * FROM Menu";
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Menu menu = extractMenuFromResultSet(rs);
                menus.add(menu);
            }
        } finally {
            connectionPool.releaseConnection(connection); // Release connection to pool
        }

        return menus;
    }

    @Override
    public Menu getMenuById(int menuId) throws Exception {
        Menu menu = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = connectionPool.getConnection(); // Use instance reference
            String query = "SELECT * FROM Menu WHERE menuId = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, menuId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                menu = extractMenuFromResultSet(rs);
            }
        } finally {
            connectionPool.releaseConnection(connection); // Release connection to pool
        }

        return menu;
    }

    private Menu extractMenuFromResultSet(ResultSet rs) throws Exception {
        Menu menu = new Menu();
        menu.setMenuId(rs.getInt("menuId"));
        menu.setName(rs.getString("name"));
        menu.setCategoryId(rs.getInt("categoryId"));
        menu.setPrice(rs.getFloat("price"));
        menu.setAvailability(rs.getBoolean("availability"));
        menu.setSpiceLevel(rs.getString("spiceLevel"));
        menu.setCuisineType(rs.getString("cuisineType"));
        menu.setSweet(rs.getBoolean("isSweet"));
        menu.setDietaryPreference(rs.getString("dietaryPreference"));
        return menu;
    }

    // Other methods as needed
}
