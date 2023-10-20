# Media
AssProject2
 Social Media Application


Data Analytics Hub App

Overview:

The `DataAnalyticsHubApp` is a JavaFX application that serves as a platform for data analytics. It provides functionalities for user login, registration, profile management, post creation, post removal, post sorting, bulk upload of posts, and exporting post details.

Key Features:

1. User Authentication: The app has a login pane where users can enter their credentials to access the platform. If the credentials are incorrect, an error message is displayed. There's also a registration pane for new users to sign up.

2. Dashboard: Once logged in, users are taken to a dashboard where they can navigate to various functionalities like editing their profile, adding a post, removing a post, sorting posts, bulk uploading posts, and exporting post details.

3. Post Management: Users can add new posts with content, likes, and shares. They can also remove posts by providing a post ID.

4. Bulk Upload: Users can bulk upload posts from a CSV file. The app reads the CSV file and adds each post to the database.

5. Export Post: Users can export the details of a post (by providing its ID) to a CSV file.

6. Sort Posts: Users can view all posts or sort them based on likes or shares. They can also specify the number of posts they want to view.

7. Profile Management: Users can view and update their profile details, including username, first name, last name, and password.

8. Logout: Users can log out of the application, which takes them back to the login pane.

Dependencies:
The app uses JavaFX for the GUI and seems to interact with a database for storing user and post details. There are references to `UserManager`, `UserDAO`, `PostDAO`, and other classes that likely handle database operations and business logic.

Database Utilities

`DatabaseUtils`:

- Provides utility methods to create tables in the database.
- Methods:
  - `createNewTable()`: Creates new database tables for users and posts if they do not already exist.
  - `createUsersTable()`: Creates the 'users' table with appropriate columns if it does not already exist.
  - `createPostsTable()`: Creates the 'posts' table with appropriate columns if it does not already exist.

Post Management

`Post`:

- Represents a post with attributes like ID, content, author, likes, shares, and dateTime.
- Methods include constructors for creating a post object from database records and for creating a new post, getters, and a `toString` method.

`PostDAO`:

- An interface that defines the operations for managing posts in the data access layer.
- Methods include adding posts, retrieving posts (all, by likes, by shares, by ID), and deleting posts.

`PostDAOImpl`:

- Implements the `PostDAO` interface.
- Provides concrete implementations for the methods defined in the `PostDAO` interface.

 User Management

`User`:

- Represents a user with attributes like ID, username, password, first name, and last name.
- Contains methods related to posts, such as retrieving all posts by the user and adding a post.
- Provides getters, setters, and a `toString` method.

`UserDAO`:

- An interface that defines the operations for managing users in the data access layer.
- Methods include adding users, retrieving users (by username and password, by username), updating users, deleting users, and checking if a username exists.

`UserDAOImpl`:

- Implements the `UserDAO` interface.
- Provides concrete implementations for the methods defined in the `UserDAO` interface.

`UserManager`:

- Manages user-related operations.
- Methods include registering a new user, authenticating a user during login, updating a user's profile, and listing all registered users.



How to Run the Application Prerequisites:

 Java Development Kit (JDK): Ensure you have JDK installed (version 8 or above is recommended). 

You can download it from the official Oracle website. JavaFX: Since the application uses JavaFX for its GUI, make sure you have the JavaFX SDK. 

You can download it from OpenJFX. Database: Ensure your database is set up and running. Update the database connection details in the DatabaseConnection class or any configuration files if necessary.



Steps to Run:

1.Clone the Repository:


2. Compile the Application:

3.Run the Application

Replace path_to_your_javafx_lib with the path to your JavaFX lib directory.

Once the application starts, you can use the provided GUI to interact with the application. Start by registering a new user or logging in if you already have an account.


