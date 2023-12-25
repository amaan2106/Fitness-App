package use_case.login;

import entity.User;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

public class LoginInteractor implements LoginInputBoundary {
    final LoginUserDataAccessInterface userDataAccessObject;
    final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        String username = loginInputData.getUsername();
        String enteredPassword = loginInputData.getPassword();


        if (!userDataAccessObject.existsByName(username)) {
            System.out.println("user does not exist");
            loginPresenter.prepareFailView(username + ": Account does not exist.");

        } else {
            User user = userDataAccessObject.get(username);
            String actualPassword = user.getPassword();

            if (!enteredPassword.equals(actualPassword)) {
                loginPresenter.prepareFailView("Incorrect password for " + username + ".");
            } else {
                LoginOutputData loginOutputData = new LoginOutputData(user.getName(), false);
                LoginState loginState = new LoginState();
                loginState.setLoggedIn(true);
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }


}