package com.example.media;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DataAnalyticsHubApp extends Application {

    private final UserManager userManager = new UserManager();
    private final Label loginMessageLabel = new Label(); // To display login messages
    private final Label registerMessageLabel = new Label(); // To display registration messages

    private User loggedInUser = null; // To keep track of the logged-in user
    private final Label dashboardMessageLabel = new Label(); // To display dashboard messages

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Analytics Hub");

        VBox mainLayout = new VBox();
        TabPane tabPane = new TabPane();

        Tab loginTab = new Tab("Login");
        Tab registerTab = new Tab("Register");
        Tab dashboardTab = new Tab("Dashboard");

        loginTab.setContent(createLoginPane());
        registerTab.setContent(createRegisterPane());
        dashboardTab.setContent(createDashboardPane());

        tabPane.getTabs().addAll(loginTab, registerTab, dashboardTab);
        mainLayout.getChildren().add(tabPane);

        Scene scene = new Scene(mainLayout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLoginPane() {
        VBox loginPane = createPane("Login", loginMessageLabel);
        GridPane grid = (GridPane) loginPane.getChildren().get(0);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(((TextField) grid.getChildren().get(1)).getText(), ((PasswordField) grid.getChildren().get(3)).getText()));

        grid.add(loginButton, 1, 2);
        return loginPane;
    }

    private VBox createRegisterPane() {
        VBox registerPane = createPane("Register", registerMessageLabel);
        GridPane grid = (GridPane) registerPane.getChildren().get(0);

        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();

        grid.add(firstNameLabel, 0, 2);
        grid.add(firstNameField, 1, 2);
        grid.add(lastNameLabel, 0, 3);
        grid.add(lastNameField, 1, 3);

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> handleRegistration(((TextField) grid.getChildren().get(1)).getText(), ((PasswordField) grid.getChildren().get(3)).getText(), firstNameField.getText(), lastNameField.getText()));

        grid.add(registerButton, 1, 4);
        return registerPane;
    }

    private VBox createPane(String type, Label messageLabel) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(20));

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        pane.getChildren().addAll(grid, messageLabel);
        return pane;
    }

    private void handleLogin(String username, String password) {
        User user = userManager.loginUser(username, password);
        if (user != null) {
            loggedInUser = user; // Set the logged-in user
            loginMessageLabel.setText("Login successful!");
        } else {
            loginMessageLabel.setText("Invalid credentials. Please try again.");
        }
    }

    private void handleRegistration(String username, String password, String firstName, String lastName) {
        boolean registrationSuccessful = userManager.registerUser(username, password, firstName, lastName);
        if (registrationSuccessful) {
            registerMessageLabel.setText("Registration successful! You can now log in.");
        } else {
            registerMessageLabel.setText("Username already exists. Please choose a different username.");
        }
    }

    private VBox createDashboardPane() {
        VBox dashboardPane = new VBox(10);
        dashboardPane.setPadding(new Insets(20));

        Label welcomeLabel = new Label();
        if (loggedInUser != null) {
            welcomeLabel.setText("Welcome, " + loggedInUser.getFirstName() + "!");
        }

        ListView<String> userPosts = new ListView<>();
        if (loggedInUser != null) {
            for (Post post : loggedInUser.getAllPosts()) {
                userPosts.getItems().add(post.toString());
            }
        }

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());

        dashboardPane.getChildren().addAll(welcomeLabel, userPosts, logoutButton, dashboardMessageLabel);
        return dashboardPane;
    }



    private void handleLogout() {
        loggedInUser = null;
        dashboardMessageLabel.setText("Logged out successfully.");
        // TODO: Switch to the login tab or perform other actions upon successful logout
    }
}
