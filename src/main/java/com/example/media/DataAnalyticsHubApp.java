package com.example.media;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.layout.BorderPane;


public class DataAnalyticsHubApp extends Application {

    private final UserManager userManager = new UserManager();
    private final Label loginMessageLabel = new Label();
    private final Label dashboardMessageLabel = new Label();
    private TextArea postContentArea;
    private TextField likesField;
    private TextField sharesField;
    private TextField dateTimeField;

    private ListView<String> postsListView;

    private User loggedInUser = null;

    private VBox mainLayout;

    private UserDAO userDAO;  // Declare userDAO
    private PostDAO postDAO;  // Declare postDAO


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        userDAO = new UserDAOImpl();  // Initialize userDAO
        postDAO = new PostDAOImpl();  // Initialize postDAO


        DatabaseUtils.createNewTable();

        primaryStage.setTitle("Data Analytics Hub");

        // Prevent the window from being resizable

        mainLayout = new VBox();
        mainLayout.getChildren().add(createLoginPane());

        Scene scene = new Scene(mainLayout, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLoginPane() {
        VBox loginPane = new VBox(10);
        loginPane.setPadding(new Insets(10));
        loginPane.setAlignment(Pos.CENTER);

        Label banner = new Label("LOGIN");
        banner.setFont(Font.font("Arial", FontWeight.BOLD, 40));

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> showRegisterPane());

        grid.add(loginButton, 1, 2);
        grid.add(registerButton, 1, 3);

        loginPane.getChildren().addAll(banner, grid, loginMessageLabel);

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
        backButton.setOnAction(e -> mainLayout.getChildren().setAll(createLoginPane()));

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> handleRegistration(((TextField) grid.getChildren().get(1)).getText(), ((PasswordField) grid.getChildren().get(3)).getText(), firstNameField.getText(), lastNameField.getText()));

        grid.add(backButton, 0, 4);
        grid.add(registerButton, 1, 4);

        mainLayout.getChildren().setAll(registerPane);
    }

    private VBox createPane(Label messageLabel) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

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
            mainLayout.getChildren().setAll(createLoginPane());
        } else {
            loginMessageLabel.setText("Username already exists. Please choose a different username.");
        }
    }

    private void showDashboardPane() {
        VBox dashboardPane = new VBox(20);
        dashboardPane.setPadding(new Insets(20));
        dashboardPane.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("DATA ANALYTICS HUB");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label welcomeLabel = new Label("Welcome " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName() + " to Data Analytics HUB");

        Button editProfileButton = new Button("Edit Profile");
        editProfileButton.setOnAction(e -> showUserProfilePane());

        Button postPageButton = new Button("Post Page");
        postPageButton.setOnAction(e -> showPostPagePane());

        Button removePostButton = new Button("Remove Post");
        removePostButton.setOnAction(e -> showRemovePostPane());  // Added action for removePostButton

        Button sortPostButton = new Button("Sort Post");
        // TODO: Add action for sortPostButton

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());

        dashboardPane.getChildren().addAll(titleLabel, welcomeLabel, editProfileButton, postPageButton, removePostButton, sortPostButton, logoutButton, dashboardMessageLabel);

        mainLayout.getChildren().setAll(dashboardPane);
    }

    private final Label userProfileMessageLabel = new Label();




    private void showPostPagePane() {
        VBox postPageContent = new VBox(15); // Adjusted spacing between elements
        postPageContent.setPadding(new Insets(20, 50, 20, 20)); // Adjusted padding for better alignment
        postPageContent.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Post Page");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        // Post Content
        Label postContentLabel = new Label("Post Content:");
        postContentArea = new TextArea();
        postContentArea.setWrapText(true);
        postContentArea.setPrefHeight(100);
        postContentArea.setPrefWidth(300); // Adjusted width

        // Set the min and max width and height for postContentArea
        postContentArea.setMinWidth(500);
        postContentArea.setMaxWidth(300);
        postContentArea.setMinHeight(100);
        postContentArea.setMaxHeight(100);



        // Post Likes
        Label postLikesLabel = new Label("Post Likes:");
        likesField = new TextField();
        likesField.setPrefWidth(150); // Adjusted width


        likesField.setMinWidth(150);
        likesField.setMaxWidth(150);

        // Post Shares
        Label postSharesLabel = new Label("Post Shares:");
        sharesField = new TextField();
        sharesField.setPrefWidth(150); // Adjusted width

        sharesField.setMinWidth(150);
        sharesField.setMaxWidth(150);

        // Date and Time
        Label dateTimeLabel = new Label("Date and Time (format: YYYY-MM-DD  HH:MM):");
        dateTimeField = new TextField();
        dateTimeField.setPrefWidth(200); // Adjusted width


        dateTimeField.setMinWidth(200);
        dateTimeField.setMaxWidth(200);

        // Buttons
        Button addPostButton = new Button("ADD POST");
        addPostButton.setOnAction(e -> handleAddPost());

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> showDashboardPane());

        postPageContent.getChildren().addAll(titleLabel, postContentLabel, postContentArea, postLikesLabel, likesField, postSharesLabel, sharesField, dateTimeLabel, dateTimeField, addPostButton, backButton);

        mainLayout.getChildren().setAll(postPageContent); // Set only the postPageContent to the mainLayout
    }


    // Placeholder method to fetch posts from the database


    private void showUserProfilePane() {
        VBox userProfilePane = new VBox(20);
        userProfilePane.setPadding(new Insets(20));
        userProfilePane.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("User Profile");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Username
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField(loggedInUser.getUsername());
        usernameField.setMinWidth(200);
        usernameField.setMaxWidth(200);

        // First Name
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField(loggedInUser.getFirstName());
        firstNameField.setMinWidth(200);
        firstNameField.setMaxWidth(200);

        // Last Name
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField(loggedInUser.getLastName());
        lastNameField.setMinWidth(200);
        lastNameField.setMaxWidth(200);

        // Password
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password or leave blank to keep unchanged");
        passwordField.setMinWidth(200);
        passwordField.setMaxWidth(200);

        // Update Button
        Button updateButton = new Button("Update Profile");
        updateButton.setOnAction(e -> {
            handleProfileUpdate(usernameField.getText(), firstNameField.getText(), lastNameField.getText(), passwordField.getText());
            passwordField.clear(); // Clear the password field after updating
        });

        // Back Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> showDashboardPane());

        userProfilePane.getChildren().addAll(titleLabel, usernameLabel, usernameField, firstNameLabel, firstNameField, lastNameLabel, lastNameField, passwordLabel, passwordField, updateButton, backButton, userProfileMessageLabel); // Added userProfileMessageLabel

        mainLayout.getChildren().setAll(userProfilePane);
    }


    private void handleProfileUpdate(String newUsername, String firstName, String lastName, String newPassword) {
        if (!newUsername.equals(loggedInUser.getUsername()) && userDAO.usernameExists(newUsername)) {
            userProfileMessageLabel.setText("Username already exists. Please choose a different username.");
            return;
        }

        loggedInUser.setUsername(newUsername);
        loggedInUser.setFirstName(firstName);
        loggedInUser.setLastName(lastName);
        if (!newPassword.isEmpty()) {
            loggedInUser.setPassword(newPassword);
        }
        userManager.updateUserProfile(loggedInUser.getId(), newUsername, loggedInUser.getPassword(), firstName, lastName);
        userProfileMessageLabel.setText("Profile updated successfully!");
    }

    private void handleAddPost() {
        if (loggedInUser != null && !postContentArea.getText().trim().isEmpty()) {
            String content = postContentArea.getText().trim();
            try {
                int postId = (int) (Math.random() * 1000000); // Generating a random post ID for simplicity
                int likes = Integer.parseInt(likesField.getText().trim());
                int shares = Integer.parseInt(sharesField.getText().trim());
                String dateTime = dateTimeField.getText().trim(); // Accepting dateTime as a string

                boolean success = postDAO.addPost(postId, content, loggedInUser.getId(), likes, shares, dateTime);

                if (success) {
                    dashboardMessageLabel.setText("Post added successfully!");
                    postContentArea.clear();
                    likesField.clear();
                    sharesField.clear();
                } else {
                    dashboardMessageLabel.setText("Failed to add post. Please try again.");
                }
            } catch (NumberFormatException e) {
                dashboardMessageLabel.setText("Invalid input for likes or shares. Please enter valid numbers.");
            }
        } else {
            dashboardMessageLabel.setText("Please enter valid post content.");
        }
    }



    private void showRemovePostPane() {
        VBox removePostPane = new VBox(20);
        removePostPane.setPadding(new Insets(20));
        removePostPane.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Remove Post");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label postIdLabel = new Label("Enter Post ID:");
        TextField postIdField = new TextField();
        postIdField.setMinWidth(200);
        postIdField.setMaxWidth(200);

        Button deleteByIdButton = new Button("Delete Post");
        deleteByIdButton.setOnAction(e -> {
            try {
                int postId = Integer.parseInt(postIdField.getText().trim());
                if (postDAO.deletePost(postId, loggedInUser.getId())) {
                    // Display a success message, e.g., "Post deleted successfully!"
                } else {
                    // Display an error message, e.g., "Unable to delete post. Ensure it's your post."
                }
            } catch (NumberFormatException ex) {
                // Handle invalid post ID input, e.g., "Invalid post ID entered."
            }
        });

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> showDashboardPane());

        removePostPane.getChildren().addAll(titleLabel, postIdLabel, postIdField, deleteByIdButton, backButton);

        mainLayout.getChildren().setAll(removePostPane);
    }


    private List<String> fetchPosts() {
        // TODO: Implement this method to fetch posts from the database and return them as a list of strings.
        return new ArrayList<>(); // Placeholder return
    }


    private void handleLogout() {
        loggedInUser = null;
        mainLayout.getChildren().setAll(createLoginPane());
    }
}
