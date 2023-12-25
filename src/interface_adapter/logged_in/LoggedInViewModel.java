package interface_adapter.logged_in;

import interface_adapter.StartPage.StartPageViewModel;
import interface_adapter.ViewModel;
import interface_adapter.login.LoginState;
// In StartPageView.java
import interface_adapter.ViewManagerModel;
import view.LoggedInView;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LoggedInViewModel extends ViewModel {
    public final String TITLE_LABEL = "Logged In View";

    private LoggedInState state = new LoggedInState();

    public static final String LOGOUT_BUTTON_LABEL = "Log out";
    public static final String HOMEPAGE_BUTTON_LABEL = "Home Page";
    private String loggedInUser;

    private final LoginState loginState;

    public LoggedInViewModel(LoginState loginState) {
        super("logged in");
        this.loginState = loginState;
    }

    public void setState(LoggedInState state) {
        this.state = state;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // This is what the Login Presenter will call to let the ViewModel know
    // to alert the View
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }


    public LoggedInState getState() {
        return state;
    }


    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public LoginState getLoginState() {
        return loginState;
    }

}
