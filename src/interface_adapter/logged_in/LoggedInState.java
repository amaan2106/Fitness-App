package interface_adapter.logged_in;

public class LoggedInState {
    private String username = "";

    public LoggedInState(LoggedInState copy) {
        username = copy.username;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public LoggedInState() {}

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        // Assuming a user is considered logged in if the username is not empty
        return !username.isEmpty();
    }
}
