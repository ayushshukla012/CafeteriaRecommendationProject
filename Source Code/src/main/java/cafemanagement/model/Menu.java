package cafemanagement.model;

public class Menu {
    private int menuId;
    private String name;
    private int categoryId;
    private float price;
    private boolean availability;

    public Menu() {
    }

    public Menu(String name, int categoryId, float price, boolean availability) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.availability = availability;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuId=" + menuId +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", availability=" + availability +
                '}';
    }
}
