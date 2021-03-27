package Login;


//This objected has been created to pass the user info into different windows.
public class User {

    private final int userID;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String email;

    public User(int userID, String username, String firstname, String lastname, String email) {
        this.userID = userID;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }
}
