package BankingSystemOOAD.view;

import BankingSystemOOAD.controller.CustomerController;
import BankingSystemOOAD.model.Customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterView {

    public void start(Stage stage) {

        Label title = Theme.label("Register Customer", "dialog-title");
        Label subtitle = Theme.label("Create your profile, then open your first account", "hint");

        TextField name = new TextField();
        name.setPromptText("Full name");

        TextField phone = new TextField();
        phone.setPromptText("Phone");

        TextField address = new TextField();
        address.setPromptText("Address");

        TextField username = new TextField();
        username.setPromptText("Choose a username");

        PasswordField password = new PasswordField();
        password.setPromptText("Min 4 characters");

        Button register = new Button("Register");
        register.getStyleClass().add("primary-button");
        register.setMaxWidth(Double.MAX_VALUE);
        register.setDefaultButton(true);

        Button back = new Button("Already registered? Back to login");
        back.getStyleClass().add("link-button");

        Label message = Theme.messageLabel();

        register.setOnAction(e -> {
            try {
                Customer customer = new CustomerController().registerCustomer(
                        name.getText(),
                        address.getText(),
                        phone.getText(),
                        username.getText(),
                        password.getText()
                );
                new DashboardView(customer,
                        "Registration successful. Click Open Account to create your first account.")
                        .start(stage);
            } catch (IllegalArgumentException ex) {
                Theme.showMessage(message, ex.getMessage(), "error");
            }
        });

        back.setOnAction(e -> new LoginView().start(stage));

        // two columns so the form stays short
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);
        ColumnConstraints half = new ColumnConstraints();
        half.setPercentWidth(50);
        ColumnConstraints half2 = new ColumnConstraints();
        half2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(half, half2);

        grid.add(Theme.field("Name", name), 0, 0);
        grid.add(Theme.field("Phone", phone), 1, 0);
        grid.add(Theme.field("Address", address), 0, 1, 2, 1);
        grid.add(Theme.field("Username", username), 0, 2);
        grid.add(Theme.field("Password", password), 1, 2);

        VBox form = new VBox(16, title, subtitle, grid, register, message, back);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setMaxWidth(440);
        form.setMaxHeight(VBox.USE_PREF_SIZE);

        StackPane formPanel = new StackPane(form);
        formPanel.getStyleClass().add("form-panel");
        formPanel.setPadding(new Insets(40));
        HBox.setHgrow(formPanel, Priority.ALWAYS);

        HBox root = new HBox(Theme.brandPanel(), formPanel);

        Scene scene = Theme.scene(root, 900, 600);
        Theme.show(stage, scene, "Banking System - Register");
    }
}
