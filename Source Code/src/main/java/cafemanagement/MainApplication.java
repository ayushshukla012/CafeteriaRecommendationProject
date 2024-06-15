package cafemanagement;

import cafemanagement.authentication.AuthController;

public class MainApplication {

    public static void main(String[] args) {
        AuthController authController = new AuthController();
        authController.start();
    }
}
