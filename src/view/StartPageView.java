package view;

import interface_adapter.StartPage.RecipePageButton.RecipeSearchController;
import interface_adapter.StartPage.StartPageViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.Workout.WorkoutViewModel;

import interface_adapter.CalorieCounter.CalorieCounterViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.ViewManagerModel;
import view.LoggedInView;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.RandomAccessFile;

public class StartPageView extends JPanel implements ActionListener, PropertyChangeListener {
    public final String viewName = "start page";

    private final StartPageViewModel startPageViewModel;
    private final RecipeSearchController recipeSearchController;
    private final JButton recipeSearchButton;
    private final JLabel usernameLabel;
    private final JButton planMealButton;
    private final JButton viewSavedRecipesButton;
    private final JButton workoutSearchButton;
    private final JButton calorieCounterButton;
    private final JButton userScreenButton;

    private boolean addedToWindow = false;

    private ViewManagerModel viewManagerModel;
    private LoggedInView loggedInView;

    public void setViewManagerAndLoggedIn(ViewManagerModel viewManagerModel, LoggedInView loggedInView) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInView = loggedInView;
    }

    public StartPageView(StartPageViewModel startPageViewModel, RecipeSearchController recipeSearchController,
                         WorkoutViewModel workoutViewModel,
                         ViewManagerModel viewManagerModel,
                         CalorieCounterViewModel calorieCounterViewModel, LoggedInView loggedInView, LoggedInViewModel loggedInViewModel) {

        this.startPageViewModel = startPageViewModel;
        this.recipeSearchController = recipeSearchController;
        this.startPageViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel(startPageViewModel.TITLE_LABEL);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);


        recipeSearchButton = createStyledButton(startPageViewModel.recipe_search_BUTTON_LABEL, "src/rsearch.png");
        planMealButton = createStyledButton(startPageViewModel.plan_meal_BUTTON_LABEL, "src/planm.png");
        viewSavedRecipesButton = createStyledButton(startPageViewModel.view_saved_BUTTON_LABEL, "src/savedr.png");
        workoutSearchButton = createStyledButton(startPageViewModel.Workout_BUTTON_LABEL, "src/worko.png");
        calorieCounterButton = createStyledButton(startPageViewModel.Calorie_counter_BUTTON_LABEL, "src/calk.png");
        usernameLabel = new JLabel(startPageViewModel.username_Button_Label);
        userScreenButton = createStyledButton(startPageViewModel.USER_SCREEN_BUTTON_LABEL, "src/user.png");

        // Add action listeners

        userScreenButton.addActionListener(this::actionPerformed);

        viewSavedRecipesButton.addActionListener(e -> readLatestRecipeData());

        recipeSearchButton.addActionListener(e -> recipeSearchController.execute());

        calorieCounterButton.addActionListener(e -> {
            viewManagerModel.setActiveView(calorieCounterViewModel.getViewName());
            viewManagerModel.firePropertyChanged();
        });


        workoutSearchButton.addActionListener(e -> {
            viewManagerModel.setActiveView(workoutViewModel.getViewName());
            viewManagerModel.firePropertyChanged();
        });

        JPanel buttonPanel1 = createButtonPanel(userScreenButton);
        JPanel buttonPanel2 = createButtonPanel(recipeSearchButton, viewSavedRecipesButton);
        JPanel buttonPanel3 = createButtonPanel(workoutSearchButton, calorieCounterButton);

        this.setLayout(new BorderLayout());
        this.add(title, BorderLayout.NORTH);
        this.add(buttonPanel1, BorderLayout.NORTH);
        this.add(buttonPanel2, BorderLayout.CENTER);
        this.add(buttonPanel3, BorderLayout.SOUTH);

        // Apply shadow or gradient if needed

        // Set the background color of the panel
        if (SwingUtilities.getWindowAncestor(this) instanceof JFrame) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            Container contentPane = frame.getContentPane();
            contentPane.setBackground(Color.BLACK);
            if (contentPane instanceof JPanel) {
                JPanel mainPanel = (JPanel) contentPane;
                mainPanel.setBackground(Color.BLACK);
            }
        }

    }

    private JButton createStyledButton(String text, String iconPath) {
        ImageIcon icon = new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        // Create a panel to hold the icon and text using GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Add the icon to the panel
        JLabel iconLabel = new JLabel(icon);
        panel.add(iconLabel, gbc);

        // Increment the row for the text
        gbc.gridy++;

        // Add the text to the panel with CENTER alignment
        JLabel textLabel = new JLabel(text);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(textLabel, gbc);

        // Create a button with the panel as its component
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.add(panel, BorderLayout.CENTER);

        // Customize button appearance
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false); // Remove the focus border
        button.setBorderPainted(false); // Remove the border
        button.setPreferredSize(new Dimension(150, 150)); // Adjust size as needed
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor on hover

        // Adjusted foreground color for better visibility
        button.setForeground(new Color(0, 0, 0)); // White text

        // Add padding
        button.setMargin(new Insets(10, 10, 10, 10));

        // Apply shadow or gradient if needed

        return button;
    }


    private JPanel createButtonPanel(JButton... buttons) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50)); // Increased gaps

        for (JButton button : buttons) {
            buttonPanel.add(button);
        }

        return buttonPanel;
    }


    /**
     * React to a button click that results in evt.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            System.out.println("Button clicked!");
            if (evt.getSource().equals(userScreenButton)) {
                System.out.println("Switching to logged in view");
                viewManagerModel.setActiveView(loggedInView.viewName);
                viewManagerModel.firePropertyChanged();
                System.out.println("Active view set to: " + viewManagerModel.getActiveView());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }


    public void readLatestRecipeData() {
        String csvFile = "./saved_recipes.csv";
        String lastLine = "";

        try (RandomAccessFile fileHandler = new RandomAccessFile(csvFile, "r")) {
            long fileLength = fileHandler.length() - 1;
            StringBuilder sb = new StringBuilder();

            for (long filePointer = fileLength; filePointer != -1; filePointer--) {
                fileHandler.seek(filePointer);
                int readByte = fileHandler.readByte();

                if (readByte == 0xA) {
                    if (filePointer == fileLength) {
                        continue;
                    }
                    break;

                } else if (readByte == 0xD) {
                    if (filePointer == fileLength - 1) {
                        continue;
                    }
                    break;
                }

                sb.append((char) readByte);
            }

            lastLine = sb.reverse().toString();
            JOptionPane.showMessageDialog(null, lastLine);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading recipe data.");
        }

    }

}


