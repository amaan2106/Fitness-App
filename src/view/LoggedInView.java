package view;

import interface_adapter.StartPage.RecipePageButton.RecipeSearchController;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginState;

import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import java.awt.Cursor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;





public class LoggedInView extends JPanel implements ActionListener, PropertyChangeListener {

    private final JButton signUpButton;
    private final JButton loginButton;
    private final ViewManagerModel viewManagerModel; // Add this field

    public final String viewName = "User Screen";
    private final LoggedInViewModel loggedInViewModel;
    private final LoginViewModel loginViewModel;
    private final SignupViewModel signupViewModel;

    JLabel username;

    final JButton logOut, homePage;

    private final JPanel buttons;
    private final JPanel smallButtonsPanel;

    /**
     * A window with a title and a JButton.
     */
    public LoggedInView(ViewManagerModel viewManagerModel, LoggedInViewModel loggedInViewModel, LoginViewModel loginViewModel, SignupViewModel signupViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);
        viewManagerModel.setLoggedInUsername(loggedInViewModel.getLoggedInUser());

        JLabel title = new JLabel("User Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        signUpButton = createSmallButton("Sign Up", "src/signu.png");
        loginButton = createSmallButton("Log In", "src/login.png");

        JLabel usernameInfo = new JLabel("Currently logged in: ");
        username = new JLabel();

        buttons = new JPanel();
        logOut = new JButton(loggedInViewModel.LOGOUT_BUTTON_LABEL);
        homePage = new JButton(loggedInViewModel.HOMEPAGE_BUTTON_LABEL);

        signUpButton.addActionListener(e -> {
            viewManagerModel.setActiveView(signupViewModel.getViewName());
            viewManagerModel.firePropertyChanged();
        });

        loginButton.addActionListener(e -> {
            viewManagerModel.setActiveView(loginViewModel.getViewName());
            viewManagerModel.firePropertyChanged();
        });

        homePage.addActionListener(this);
        logOut.addActionListener(this);

        buttons.add(homePage);
        buttons.add(logOut);
        buttons.add(signUpButton);
        buttons.add(loginButton);

        // Initialize smallButtonsPanel
        smallButtonsPanel = new JPanel();
        smallButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        smallButtonsPanel.add(signUpButton);
        smallButtonsPanel.add(loginButton);

        // Add components to the main panel
        this.add(title, BorderLayout.NORTH);
        this.add(usernameInfo, BorderLayout.CENTER);
        this.add(username, BorderLayout.CENTER);

        // Update buttons initially
        SwingUtilities.invokeLater(this::updateButtons);
    }

    // Method to create small buttons
    private JButton createSmallButton(String text, String iconPath) {
        ImageIcon icon = new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton button = new JButton(text, icon);
        button.setPreferredSize(new Dimension(100, 40)); // Adjust size as needed
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);
        return button;
    }

    /**
     * React to a button click that results in evt.
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(logOut)) {
            LoggedInState loggedInState = new LoggedInState();
            loggedInState.setUsername("");
            loggedInViewModel.setState(loggedInState);
            loggedInViewModel.firePropertyChanged();
            LoggedInState currentState = loggedInViewModel.getState();
            currentState.setUsername("");
            loggedInViewModel.setState(currentState);
            viewManagerModel.setLoggedInUsername("");
            viewManagerModel.setActiveView("start page"); // Use the view name of StartPageView
            viewManagerModel.firePropertyChanged();
            JOptionPane.showMessageDialog(this, "You have been logged out. Thank you for using our service.");
        }
        if (evt.getSource().equals(homePage)) {
            viewManagerModel.setActiveView("start page"); // Use the view name of StartPageView
            viewManagerModel.firePropertyChanged();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            updateButtons();
        }
    }

    private void updateButtons() {
        LoggedInState currentState = loggedInViewModel.getState();

        if (currentState.isLoggedIn()) {
            System.out.println("Logged in as: " + currentState.getUsername());
            this.removeAll();  // Clear existing components
            this.add(buttons, BorderLayout.SOUTH);
        } else {
            System.out.println("Not Logged in Bro");
            this.removeAll();  // Clear existing components
            this.add(smallButtonsPanel, BorderLayout.SOUTH);
        }

        revalidate(); // Ensure the layout is updated
        repaint();    // Ensure the changes are immediately visible
    }
}