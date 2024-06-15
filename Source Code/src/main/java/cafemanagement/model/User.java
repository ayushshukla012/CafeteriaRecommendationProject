package cafemanagement.model;

public class User {
    private int employeeId;
    private String name;
    private String password;
    private String role;

    public User(int employeeId, String name, String password, String role) {
        this.employeeId = employeeId;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public int getEmployeeId() {
        return employeeId;
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
