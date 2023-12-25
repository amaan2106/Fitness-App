package interface_adapter;
// In StartPageView.java
import interface_adapter.ViewManagerModel;
import view.LoggedInView;

import view.StartPageView;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ViewManagerModel {


    private String activeViewName;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public String getActiveView() {
        return activeViewName;
    }

    public void setActiveView(String activeViewName) {
        if (!activeViewName.equals(this.activeViewName)) {
            this.activeViewName = activeViewName;
            support.firePropertyChange("view", null, this.activeViewName);
            System.out.println("ViewManagerModel: " + activeViewName);
        } else {
            System.out.println("ViewManagerModel: " + activeViewName + " (no change)");
        }
    }

    // This is what the Signup Presenter will call to let the ViewModel know
    // to alert the View


    public void firePropertyChanged() {
        support.firePropertyChange("view", null, this.activeViewName);
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    private String loggedInUsername;

    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }
}