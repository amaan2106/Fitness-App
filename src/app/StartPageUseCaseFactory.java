package app;

import interface_adapter.CalorieCounter.CalorieCounterViewModel;
import interface_adapter.StartPage.StartPageViewModel;
import interface_adapter.*;
import interface_adapter.RecipePageViewModel.RecipePageViewModel;
import interface_adapter.Workout.WorkoutViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.signup.SignupViewModel;
import use_case.RecipePageButton.RecipeSeachInteractor;
import use_case.RecipePageButton.RecipeSearchInputBoundary;
import use_case.RecipePageButton.RecipeSearchOutputBoundary;
import view.StartPageView;
import interface_adapter.StartPage.RecipePageButton.RecipeSearchController;
import interface_adapter.StartPage.RecipePageButton.RecipeSearchPresenter;
import view.LoggedInView;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;

public class StartPageUseCaseFactory {

    /** Prevent instantiation. */
    private StartPageUseCaseFactory() {}


    public static StartPageView create(
            ViewManagerModel viewManagerModel,
            StartPageViewModel startPageViewModel,
            RecipePageViewModel recipePageViewModel,
            WorkoutViewModel workoutViewModel,
            CalorieCounterViewModel calorieCounterViewModel,
            LoggedInView loggedInView,
            LoggedInViewModel loggedInViewModel) {

        RecipeSearchController recipeSearchController = createController(viewManagerModel, recipePageViewModel);

        return new StartPageView(
                startPageViewModel,
                recipeSearchController,
                workoutViewModel,
                viewManagerModel,
                calorieCounterViewModel,
                loggedInView,
                loggedInViewModel); // Pass the existing LoggedInViewModel
    }

    private static RecipeSearchController createController(ViewManagerModel viewManagerModel, RecipePageViewModel recipePageViewModel) {
        RecipeSearchOutputBoundary recipeSearchPresenter = new RecipeSearchPresenter(viewManagerModel, recipePageViewModel);
        RecipeSearchInputBoundary recipeSearchUseCaseInteractor = new RecipeSeachInteractor(recipeSearchPresenter);
        return new RecipeSearchController(recipeSearchUseCaseInteractor);
    }
}
