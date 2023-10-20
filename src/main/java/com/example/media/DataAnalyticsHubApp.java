package com.example.media;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
import javafx.scene.layout.BorderPane;
import java.util.stream.Collectors;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;








public class DataAnalyticsHubApp extends Application {

    private final UserManager userManager = new UserManager();
    private final Label loginMessageLabel = new Label();
    private final Label dashboardMessageLabel = new Label();
    private final Label postMessageLabel = new Label();
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
        sortPostButton.setOnAction(e -> showSortPostPane());

        Button bulkUploadButton = new Button("Bulk Upload");
        bulkUploadButton.setStyle("-fx-padding: 10px 20px; -fx-background-radius: 5px;");
        bulkUploadButton.setOnAction(e -> handleBulkUpload());

        Button exportPostByIdButton = new Button("Export Post by ID");
        exportPostByIdButton.setStyle("-fx-padding: 10px 20px; -fx-background-radius: 5px;");
        exportPostByIdButton.setOnAction(e -> handleExportPostById());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());

        dashboardPane.getChildren().addAll(titleLabel, welcomeLabel, editProfileButton, postPageButton, removePostButton, sortPostButton, bulkUploadButton, exportPostByIdButton, logoutButton, dashboardMessageLabel);

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

        // Buttons
        Button addPostButton = new Button("ADD POST");
        addPostButton.setOnAction(e -> handleAddPost());

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> showDashboardPane());

        // Message Label for Post Page
        postMessageLabel.setTextFill(Color.GREEN); // Set the text color to red for visibility. You can adjust as needed.
        postMessageLabel.setAlignment(Pos.CENTER);

        postPageContent.getChildren().addAll(titleLabel, postContentLabel, postContentArea, postLikesLabel, likesField, postSharesLabel, sharesField, addPostButton, backButton, postMessageLabel);

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

                // Get the current date and time
                String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                boolean success = postDAO.addPost(postId, content, loggedInUser.getId(), likes, shares, dateTime);

                if (success) {
                    postMessageLabel.setText("Post added successfully!");
                    postContentArea.clear();
                    likesField.clear();
                    sharesField.clear();
                } else {
                    postMessageLabel.setText("Failed to add post. Please try again.");
                }
            } catch (NumberFormatException e) {
                postMessageLabel.setText("Invalid input for likes or shares. Please enter valid numbers.");
            }
        } else {
            postMessageLabel.setText("Please enter valid post content.");
        }
    }

    private final Label removePostMessageLabel = new Label();

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

                // Check if the post exists before trying to delete
                Post post = postDAO.getPostById(postId); // Assuming you have a method getPostById in your PostDAO
                if (post == null) {
                    removePostMessageLabel.setText("No post found with the provided ID.");
                    return;
                }

                if (postDAO.deletePost(postId, loggedInUser.getId())) {
                    removePostMessageLabel.setText("Post deleted successfully!");
                } else {
                    removePostMessageLabel.setText("Unable to delete post. Ensure it's your post.");
                }
            } catch (NumberFormatException ex) {
                removePostMessageLabel.setText("Invalid post ID entered.");
            }
        });


        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> showDashboardPane());

        removePostPane.getChildren().addAll(titleLabel, postIdLabel, postIdField, deleteByIdButton, backButton, removePostMessageLabel);

        mainLayout.getChildren().setAll(removePostPane);
    }



    private List<String> fetchPosts() {
        List<Post> posts = postDAO.getAllPosts();
        return posts.stream().map(Post::toString).collect(Collectors.toList());
    }


    private void showSortPostPane() {
        VBox sortPostPane = new VBox(20);
        sortPostPane.setPadding(new Insets(20));
        sortPostPane.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Sort Posts");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Amount Box
        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter number of posts");
        amountField.setMinWidth(150);
        amountField.setMaxWidth(150);

        // Initialize the ListView
        postsListView = new ListView<>();

        // Style and padding for buttons
        String buttonStyle = "-fx-padding: 10px 20px; -fx-background-radius: 5px;";

        // Buttons for sorting
        Button showAllPostsButton = new Button("List all Posts");
        showAllPostsButton.setStyle(buttonStyle);
        showAllPostsButton.setOnAction(e -> {
            List<String> posts = fetchPosts();
            postsListView.getItems().setAll(posts);
        });

        Button sortByLikesButton = new Button("Likes");
        sortByLikesButton.setStyle(buttonStyle);
        sortByLikesButton.setOnAction(e -> {
            List<Post> posts = postDAO.getAllPostsSortedByLikes();
            int amount = getAmountFromField(amountField);
            if (amount > 0 && amount < posts.size()) {
                posts = posts.subList(0, amount);
            }
            List<String> postStrings = posts.stream().map(Post::toString).collect(Collectors.toList());
            postsListView.getItems().setAll(postStrings);
        });

        Button sortBySharesButton = new Button("Shares");
        sortBySharesButton.setStyle(buttonStyle);
        sortBySharesButton.setOnAction(e -> {
            List<Post> posts = postDAO.getAllPostsSortedByShares();
            int amount = getAmountFromField(amountField);
            if (amount > 0 && amount < posts.size()) {
                posts = posts.subList(0, amount);
            }
            List<String> postStrings = posts.stream().map(Post::toString).collect(Collectors.toList());
            postsListView.getItems().setAll(postStrings);
        });


        Button clearButton = new Button("Clear");
        clearButton.setStyle(buttonStyle);
        clearButton.setOnAction(e -> {
            amountField.clear();
            postsListView.getItems().clear(); // Clear the ListView
            // TODO: Implement the function to clear the sorting and show default posts
        });

        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(e -> showDashboardPane());

        // Create a VBox for buttons with increased spacing
        VBox buttonsBox = new VBox(15, amountLabel, amountField, showAllPostsButton, sortByLikesButton, sortBySharesButton, clearButton, backButton);
        buttonsBox.setAlignment(Pos.CENTER); // Center the buttonsBox
        buttonsBox.setPadding(new Insets(0, 20, 0, 0)); // Added right padding to separate from the ListView

        // Use a BorderPane to position the buttons and ListView
        BorderPane layoutPane = new BorderPane();
        layoutPane.setLeft(buttonsBox);
        layoutPane.setCenter(postsListView);  // Set the ListView to the center of the BorderPane
        layoutPane.setPadding(new Insets(20, 50, 20, 50));  // Adjusted padding to center the layoutPane content

        sortPostPane.getChildren().addAll(titleLabel, layoutPane);

        mainLayout.getChildren().setAll(sortPostPane);
    }


    private int getAmountFromField(TextField amountField) {
        try {
            return Integer.parseInt(amountField.getText().trim());
        } catch (NumberFormatException e) {
            return -1; // Return -1 if the input is not a valid number
        }
    }


    private void handleBulkUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File for Bulk Upload");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                // Skip the header line
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    System.out.println("Reading line: " + line); // Debugging line
                    String[] parts = line.split(","); // Changed from split("\t")
                    if (parts.length == 6) {
                        int postId = Integer.parseInt(parts[0]);
                        String content = parts[1];
                        String author = parts[2];
                        int likes = Integer.parseInt(parts[3]);
                        int shares = Integer.parseInt(parts[4]);
                        String dateTime = parts[5];

                        boolean success = postDAO.addPost(postId, content, loggedInUser.getId(), likes, shares, dateTime);
                        if (success) {
                            System.out.println("Added post with ID: " + postId); // Debugging line
                        } else {
                            System.out.println("Failed to add post with ID: " + postId); // Debugging line
                        }
                    } else {
                        System.out.println("Incorrect number of fields in line: " + line); // Debugging line
                    }
                }
                dashboardMessageLabel.setText("Bulk upload successful!");
            } catch (Exception e) { // Catching general exception for debugging
                e.printStackTrace(); // Print the stack trace for detailed error info
                dashboardMessageLabel.setText("Error during bulk upload: " + e.getMessage());
            }
        } else {
            dashboardMessageLabel.setText("No file selected.");
        }
    }

    private void handleExportPostById() {
        // Prompt the user for a post ID
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Export Post by ID");
        dialog.setHeaderText("Enter the Post ID to export:");
        dialog.setContentText("Post ID:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                int postId = Integer.parseInt(result.get());
                Post post = postDAO.getPostById(postId); // Assuming you have a method getPostById in your PostDAO

                if (post != null) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Post Details");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

                    File file = fileChooser.showSaveDialog(null);

                    if (file != null) {
                        try (PrintWriter writer = new PrintWriter(file)) {
                            writer.println("Post ID: " + post.getID());
                            writer.println("Content: " + post.getContent());
                            writer.println("Author: " + post.getAuthor());
                            writer.println("Likes: " + post.getLikes());
                            writer.println("Shares: " + post.getShares());
                            writer.println("Date Time: " + post.getDateTime());

                            dashboardMessageLabel.setText("Post exported successfully!");
                        } catch (IOException e) {
                            dashboardMessageLabel.setText("Error during export: " + e.getMessage());
                        }
                    } else {
                        dashboardMessageLabel.setText("Export cancelled.");
                    }
                } else {
                    dashboardMessageLabel.setText("No post found with the provided ID.");
                }
            } catch (NumberFormatException e) {
                dashboardMessageLabel.setText("Invalid post ID entered.");
            }
        } else {
            dashboardMessageLabel.setText("Export cancelled.");
        }
    }




    private void handleLogout() {
        loggedInUser = null;
        mainLayout.getChildren().setAll(createLoginPane());
    }
}
