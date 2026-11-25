package BankingSystemOOAD.view;

import BankingSystemOOAD.controller.AccountController;
import BankingSystemOOAD.model.Account;
import BankingSystemOOAD.model.Customer;

import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Pop-up for opening a new Savings, Investment or Cheque account. */
public class OpenAccountView {

    private final Customer customer;
    private final Consumer<String> onSuccess;

    public OpenAccountView(Customer customer, Consumer<String> onSuccess) {
        this.customer = customer;
        this.onSuccess = onSuccess;
    }

    public void start(Stage stage) {

        AccountController controller = new AccountController();

        ComboBox<String> type = new ComboBox<>(
                FXCollections.observableArrayList("Savings", "Investment", "Cheque"));
        type.getSelectionModel().selectFirst();

        TextField branch = new TextField();
        branch.setPromptText("e.g. Main");

        TextField deposit = new TextField();
        deposit.setPromptText("0.00");
        Label depositHint = Theme.label("", "hint");

        TextField employer = new TextField();
        TextField employerAddress = new TextField();

        VBox employerBox = Theme.field("Employer Name (Cheque only)", employer);
        VBox employerAddressBox = Theme.field("Employer Address (Cheque only)", employerAddress);

        Label message = Theme.messageLabel();

        // Investment needs a minimum deposit, employer details are only for Cheque
        Runnable updateForType = () -> {
            String chosen = type.getValue();
            boolean cheque = "Cheque".equals(chosen);
            employerBox.setDisable(!cheque);
            employerAddressBox.setDisable(!cheque);
            depositHint.setText("Investment".equals(chosen)
                    ? "Investment account: minimum BWP500.00"
                    : "Optional - leave empty to open with no deposit.");
            Theme.clearMessage(message);
        };
        type.setOnAction(e -> updateForType.run());
        updateForType.run();

        Button confirm = new Button("Confirm");
        confirm.getStyleClass().add("primary-button");
        confirm.setDefaultButton(true);

        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);

        confirm.setOnAction(e -> {
            try {
                Account account = controller.openAccount(
                        customer,
                        type.getValue(),
                        branch.getText(),
                        deposit.getText(),
                        employer.getText(),
                        employerAddress.getText()
                );
                onSuccess.accept(account.getAccountType() + " account "
                        + account.getAccountNumber() + " opened successfully.");
                stage.close();
            } catch (IllegalArgumentException ex) {
                Theme.showMessage(message, ex.getMessage(), "error");
            }
        });

        cancel.setOnAction(e -> stage.close());

        HBox buttons = new HBox(16, confirm, cancel);
        buttons.setAlignment(Pos.CENTER);

        VBox depositBox = new VBox(4,
                Theme.field("Initial Deposit (BWP)", deposit), depositHint);

        VBox body = new VBox(14,
                Theme.field("Account Type", type),
                Theme.field("Branch", branch),
                depositBox,
                employerBox,
                employerAddressBox,
                buttons,
                message
        );
        body.getStyleClass().add("popup-body");
        body.setPadding(new Insets(24, 32, 26, 32));
        VBox.setVgrow(body, Priority.ALWAYS);

        VBox root = new VBox(
                Theme.popupHeader("Open New Account",
                        "Customer: " + customer.getName() + " (" + customer.getCustomerId() + ")"),
                body);

        Scene scene = Theme.scene(root, 460, 700);
        Theme.show(stage, scene, "Open New Account");
    }
}
