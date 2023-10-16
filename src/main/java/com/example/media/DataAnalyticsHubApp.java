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
    private final Label loginMessageLabel = new Label();
    private final Label dashboardMessageLabel = new Label();
    private TextArea postContentArea;
    private TextField likesField;
    private TextField sharesField;

    private User loggedInUser = null;

    private TabPane tabPane;
    private Tab loginTab;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DatabaseOperations.createNewTable();  // This line creates the table when the application starts

        primaryStage.setTitle("Data Analytics Hub");

        VBox mainLayout = new VBox();
        tabPane = new TabPane();

        loginTab = new Tab("Login");
        loginTab.setContent(createLoginPane());

        tabPane.getTabs().add(loginTab);
        mainLayout.getChildren().add(tabPane);

        Scene scene = new Scene(mainLayout, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLoginPane() {
        VBox loginPane = createPane(loginMessageLabel);
        GridPane grid = (GridPane) loginPane.getChildren().get(0);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(((TextField) grid.getChildren().get(1)).getText(), ((PasswordField) grid.getChildren().get(3)).getText()));

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> showRegisterPane());

        grid.add(loginButton, 1, 2);
        grid.add(registerButton, 1, 3);

        return loginPane;
    }

    private void showRegisterPane() {
        VBox registerPane = createPane(new Label());
        GridPane grid = (GridPane) registerPane.getChildren().get(0);

        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();

        grid.add(firstNameLabel, 0, 2);
        grid.add(firstNameField, 1, 2);
        grid.add(lastNameLabel, 0, 3);
        grid.add(lastNameField, 1, 3);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> loginTab.setContent(createLoginPane()));

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> handleRegistration(((TextField) grid.getChildren().get(1)).getText(), ((PasswordField) grid.getChildren().get(3)).getText(), firstNameField.getText(), lastNameField.getText()));

        grid.add(backButton, 0, 4);
        grid.add(registerButton, 1, 4);

        loginTab.setContent(registerPane);
    }

    private VBox createPane(Label messageLabel) {
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
            loggedInUser = user;
            loginMessageLabel.setText("Login successful!");
            showDashboardPane();
        } else {
            loginMessageLabel.setText("Invalid credentials. Please try again.");
        }
    }

    private void handleRegistration(String username, String password, String firstName, String lastName) {
        boolean registrationSuccessful = userManager.registerUser(username, password, firstName, lastName);
        if (registrationSuccessful) {
            loginMessageLabel.setText("Registration successful! You can now log in.");
            loginTab.setContent(createLoginPane());
        } else {
            loginMessageLabel.setText("Username already exists. Please choose a different username.");
        }
    }

    private void showDashboardPane() {
        VBox dashboardPane = new VBox(10);
        dashboardPane.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome, " + loggedInUser.getFirstName() + "!");

        ListView<String> userPosts = new ListView<>();
        for (Post post : loggedInUser.getAllPosts()) {
            userPosts.getItems().add(post.toString());
        }

        postContentArea = new TextArea();
        postContentArea.setPromptText("Write your post here...");

        likesField = new TextField();
        likesField.setPromptText("Likes");
        sharesField = new TextField();
        sharesField.setPromptText("Shares");

        Button postButton = new Button("Add Post");
        postButton.setOnAction(e -> handleAddPost());

        VBox postBox = new VBox(10);
        postBox.getChildren().addAll(postContentArea, likesField, sharesField, postButton);

        Button userProfileButton = new Button("UserProfile");
        userProfileButton.setOnAction(e -> showUserProfilePane());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());

        dashboardPane.getChildren().addAll(welcomeLabel, userPosts, postBox, userProfileButton, logoutButton, dashboardMessageLabel);

        loginTab.setText("Dashboard");
        loginTab.setContent(dashboardPane);
    }

    private void showUserProfilePane() {
        VBox userProfilePane = new VBox(10);
        userProfilePane.setPadding(new Insets(20));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField(loggedInUser.getUsername());
        usernameField.setDisable(true); // Disable editing the username
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField(loggedInUser.getFirstName());
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField(loggedInUser.getLastName());

        Button updateButton = new Button("Update Profile");
        updateButton.setOnAction(e -> {
            handleProfileUpdate(usernameField.getText(), firstNameField.getText(), lastNameField.getText());
            showDashboardPane();  // This will take the user back to the dashboard after updating
        });

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> showDashboardPane());

        userProfilePane.getChildren().addAll(usernameLabel, usernameField, firstNameLabel, firstNameField, lastNameLabel, lastNameField, updateButton, backButton);
        loginTab.setContent(userProfilePane);
    }

    private void handleProfileUpdate(String username, String firstName, String lastName) {
        // Update the loggedInUser object
        loggedInUser.setFirstName(firstName);
        loggedInUser.setLastName(lastName);

        // Update the database with the new information
        DatabaseOperations.updateUser(loggedInUser.getId(), loggedInUser.getPassword(), firstName, lastName);

        // Provide feedback to the user
        dashboardMessageLabel.setText("Profile updated successfully!");
    }

    private void handleAddPost() {
        if (loggedInUser != null && !postContentArea.getText().trim().isEmpty()) {
            String content = postContentArea.getText().trim();
            int likes = Integer.parseInt(likesField.getText().trim());
            int shares = Integer.parseInt(sharesField.getText().trim());

            // Add the post to the database
            DatabaseOperations.addPostToDatabase(content, loggedInUser.getId(), likes, shares);

            // Provide feedback to the user
            dashboardMessageLabel.setText("Post added successfully!");

            // Clear the input fields
            postContentArea.clear();
            likesField.clear();
            sharesField.clear();
        } else {
            dashboardMessageLabel.setText("Please fill in all the post details before adding.");
        }
    }

    private void handleLogout() {
        loggedInUser = null;
        dashboardMessageLabel.setText("Logged out successfully.");
        loginTab.setText("Login");
        loginTab.setContent(createLoginPane());
    }
}
