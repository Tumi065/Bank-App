package BankingSystemOOAD.view;

import BankingSystemOOAD.controller.LoginController;
import BankingSystemOOAD.model.Customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    public void start(Stage stage) {

        Label title = Theme.label("Welcome back", "dialog-title");
        Label subtitle = Theme.label("Log in to your account to continue", "hint");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Button login = new Button("Login");
        login.getStyleClass().add("primary-button");
        login.setMaxWidth(Double.MAX_VALUE);
        login.setDefaultButton(true);

        Button register = new Button("New customer? Register");
        register.getStyleClass().add("link-button");

        Label message = Theme.messageLabel();

        login.setOnAction(e -> {
            try {
                Customer customer = new LoginController().login(
                        username.getText(),
                        password.getText());
                new DashboardView(customer).start(stage);
            } catch (IllegalArgumentException ex) {
                Theme.showMessage(message, ex.getMessage(), "error");
            }
        });

        register.setOnAction(e -> new RegisterView().start(stage));

        VBox form = new VBox(14,
                title,
                subtitle,
                Theme.field("Username", username),
                Theme.field("Password", password),
                login,
                message,
                register
        );
        form.setAlignment(Pos.CENTER_LEFT);
        form.setMaxWidth(340);
        form.setMaxHeight(VBox.USE_PREF_SIZE);

        StackPane formPanel = new StackPane(form);
        formPanel.getStyleClass().add("form-panel");
        formPanel.setPadding(new Insets(40));
        HBox.setHgrow(formPanel, Priority.ALWAYS);

        HBox root = new HBox(Theme.brandPanel(), formPanel);

        Scene scene = Theme.scene(root, 860, 540);
        Theme.show(stage, scene, "Banking System - Login");
    }
}
