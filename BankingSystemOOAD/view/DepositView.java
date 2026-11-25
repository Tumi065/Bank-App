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
import javafx.util.StringConverter;


public class DepositView {

    private final Customer customer;
    private final Account preselected;
    private final boolean withdrawMode;
    private final Consumer<String> onSuccess;

    public DepositView(Customer customer, Account preselected,
                       boolean withdrawMode, Consumer<String> onSuccess) {
        this.customer = customer;
        this.preselected = preselected;
        this.withdrawMode = withdrawMode;
        this.onSuccess = onSuccess;
    }

    public void start(Stage stage) {

        AccountController controller = new AccountController();
        String heading = withdrawMode ? "Withdraw Funds" : "Deposit Funds";

        ComboBox<Account> account = new ComboBox<>(
                FXCollections.observableArrayList(customer.getAccounts()));
        account.setConverter(new StringConverter<Account>() {
            @Override
            public String toString(Account a) {
                return a == null ? "" : a.getAccountNumber() + " - " + a.getAccountType();
            }

            @Override
            public Account fromString(String text) {
                return null;
            }
        });
        if (preselected != null) {
            account.getSelectionModel().select(preselected);
        } else {
            account.getSelectionModel().selectFirst();
        }

        TextField amount = new TextField();
        amount.setPromptText("0.00");

        TextField note = new TextField();
        note.setPromptText("Optional reference");

        Label message = Theme.messageLabel();

        Button confirm = new Button("Confirm");
        confirm.getStyleClass().add("primary-button");
        confirm.setDefaultButton(true);

        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);

        confirm.setOnAction(e -> {
            try {
                Account chosen = account.getValue();
                String text;
                if (withdrawMode) {
                    double done = controller.withdraw(chosen, amount.getText());
                    text = "Withdrawal of " + Theme.money(done) + " from "
                            + chosen.getAccountNumber() + " successful.";
                } else {
                    double done = controller.deposit(chosen, amount.getText(), note.getText());
                    text = "Deposit of " + Theme.money(done) + " into "
                            + chosen.getAccountNumber() + " successful.";
                }
                onSuccess.accept(text);
                stage.close();
            } catch (IllegalArgumentException ex) {
                Theme.showMessage(message, ex.getMessage(), "error");
            }
        });

        cancel.setOnAction(e -> stage.close());

        HBox buttons = new HBox(16, confirm, cancel);
        buttons.setAlignment(Pos.CENTER);

        VBox body = new VBox(14,
                Theme.field("Account", account),
                Theme.field("Amount (BWP)", amount)
        );
        if (!withdrawMode) {
            body.getChildren().add(Theme.field("Note (Reference)", note));
        }
        body.getChildren().addAll(buttons, message);
        body.getStyleClass().add("popup-body");
        body.setPadding(new Insets(24, 32, 26, 32));
        VBox.setVgrow(body, Priority.ALWAYS);

        VBox root = new VBox(
                Theme.popupHeader(heading,
                        "Customer: " + customer.getName() + " (" + customer.getCustomerId() + ")"),
                body);

        Scene scene = Theme.scene(root, 460, withdrawMode ? 420 : 500);
        Theme.show(stage, scene, heading);
    }
}
