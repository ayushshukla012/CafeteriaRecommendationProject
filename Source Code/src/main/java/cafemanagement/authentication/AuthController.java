package cafemanagement.authentication;

import java.util.Scanner;

public class AuthController {
    private AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim(); // Trim whitespace

            switch (input) {
                case "1":
                    login(scanner);
                    break;
                case "2":
                    logout();
                    break;
                case "3":
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("1. Login");
        System.out.println("2. Logout");
        System.out.println("3. Exit");
    }

    private void login(Scanner scanner) {
        if (authService.getLoggedInUser() != null) {
            System.out.println("Already logged in as " + authService.getLoggedInUser().getName());
            return;
        }

        System.out.print("Enter User ID: ");
        int userId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Enter Role: ");
        String roleName = scanner.nextLine().trim();

        String result = authService.login(userId, password, roleName);
        System.out.println(result);
    }

    private void logout() {
        String result = authService.logout();
        System.out.println(result);
    }
}