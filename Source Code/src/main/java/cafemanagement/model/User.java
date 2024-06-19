package cafemanagement.model;

public class User {
    private int userId;
    private String name;
    private String password;
    private String role;

    public User(int userId, String name, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public User(int userId, String name,  String role) {
        this.userId = userId;
        this.name = name;
        this.password = "";
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
