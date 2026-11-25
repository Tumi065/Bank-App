package BankingSystemOOAD.view;

import BankingSystemOOAD.controller.AccountController;
import BankingSystemOOAD.model.Account;
import BankingSystemOOAD.model.Customer;
import BankingSystemOOAD.model.Transaction;
import BankingSystemOOAD.model.TransactionType;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class DashboardView {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Customer customer;
    private final String welcomeMessage;
    private final AccountController controller = new AccountController();

    private Stage stage;
    private TableView<Account> accountsTable;
    private TableView<Transaction> transactionsTable;
    private Label totalValue;
    private Label countValue;
    private Label transactionsTitle;
    private Label message;

    public DashboardView(Customer customer) {
        this(customer, null);
    }

    public DashboardView(Customer customer, String welcomeMessage) {
        this.customer = customer;
        this.welcomeMessage = welcomeMessage;
    }

    public void start(Stage stage) {
        this.stage = stage;

        VBox sidebar = buildSidebar(stage);

        VBox titles = new VBox(2,
                Theme.label("Dashboard", "page-title"),
                Theme.label("Welcome back, " + customer.getName(), "page-subtitle"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label status = Theme.label("\u25CF  Logged in as " + customer.getUsername(), "status-chip");

        HBox header = new HBox(titles, spacer, status);
        header.setAlignment(Pos.CENTER_LEFT);

    
        totalValue = Theme.label("", "stat-value-light");
        countValue = Theme.label("", "stat-value");
        Label idValue = Theme.label(customer.getCustomerId(), "stat-value");

        VBox totalCard = new VBox(2, Theme.label("TOTAL BALANCE", "stat-label-light"), totalValue);
        totalCard.getStyleClass().add("stat-card-primary");
        totalCard.setPadding(new Insets(14, 18, 14, 18));
        HBox.setHgrow(totalCard, Priority.ALWAYS);
        totalCard.setMaxWidth(Double.MAX_VALUE);

        HBox stats = new HBox(14,
                totalCard,
                statCard("ACCOUNTS", countValue),
                statCard("CUSTOMER ID", idValue)
        );


        accountsTable = new TableView<>();
        accountsTable.setPlaceholder(new Label("No accounts yet - click Open Account"));
        accountsTable.setFixedCellSize(36);
        accountsTable.setPrefHeight(150);
        accountsTable.setMinHeight(116);

        addColumn(accountsTable, "Account Number", 0.34, a -> a.getAccountNumber());
        addColumn(accountsTable, "Type", 0.30, a -> a.getAccountType());
        addColumn(accountsTable, "Balance", 0.36, a -> Theme.money(a.getBalance()));

        accountsTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldAccount, newAccount) -> showTransactions(newAccount));

        VBox accountsCard = card("MY ACCOUNTS", accountsTable);

    
        transactionsTable = new TableView<>();
        transactionsTable.setPlaceholder(new Label("No transactions to show"));
        transactionsTable.setFixedCellSize(36);
        transactionsTable.setPrefHeight(190);

        addColumn(transactionsTable, "Date", 0.30, t -> t.getDateTime().format(DATE_FORMAT));
        TableColumn<Transaction, String> typeColumn =
                addColumn(transactionsTable, "Type", 0.24, t -> pretty(t.getType()));
        addColumn(transactionsTable, "Amount", 0.22, t -> signedAmount(t));
        addColumn(transactionsTable, "Balance After", 0.24, t -> Theme.number(t.getBalanceAfter()));

        typeColumn.setCellFactory(col -> new TableCell<Transaction, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-font-weight: bold; -fx-text-fill: " + typeColor(item) + ";");
                }
            }
        });

        transactionsTitle = Theme.label("TRANSACTIONS", "section-title");
        VBox transactionsCard = new VBox(8, transactionsTitle, transactionsTable);
        transactionsCard.getStyleClass().add("card");
        transactionsCard.setPadding(new Insets(14, 16, 16, 16));
        VBox.setVgrow(transactionsTable, Priority.ALWAYS);
        VBox.setVgrow(transactionsCard, Priority.ALWAYS);

        Button openAccount = new Button("Open Account");
        openAccount.getStyleClass().add("primary-button");
        Button deposit = new Button("Deposit");
        deposit.getStyleClass().add("primary-button");
        Button withdraw = new Button("Withdraw");
        Button payInterest = new Button("Pay Interest");

        openAccount.setOnAction(e -> {
            Stage popup = Theme.popup(stage);
            new OpenAccountView(customer, text -> {
                refresh();
                Theme.showMessage(message, text, "ok");
            }).start(popup);
        });

        deposit.setOnAction(e -> openMoneyDialog(false));
        withdraw.setOnAction(e -> openMoneyDialog(true));

        payInterest.setOnAction(e -> {
            Account selected = accountsTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Theme.showMessage(message, "Select an account first.", "error");
                return;
            }
            try {
                double interest = controller.payInterest(selected);
                refresh();
                Theme.showMessage(message, "Interest of " + Theme.money(interest)
                        + " paid into " + selected.getAccountNumber() + ".", "ok");
            } catch (IllegalArgumentException ex) {
                Theme.showMessage(message, ex.getMessage(), "error");
            }
        });

        HBox buttons = new HBox(14, openAccount, deposit, withdraw, payInterest);
        buttons.setAlignment(Pos.CENTER_LEFT);

        message = Theme.messageLabel();

        VBox content = new VBox(16, header, stats, accountsCard, transactionsCard, buttons, message);
        content.setPadding(new Insets(24, 30, 24, 30));
        content.getStyleClass().add("dashboard-root");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(content);

        refresh();

        if (welcomeMessage != null) {
            Theme.showMessage(message, welcomeMessage, "info");
        } else if (customer.getAccounts().isEmpty()) {
            Theme.showMessage(message, "You have no accounts yet. Click Open Account to create one.", "info");
        }

        Scene scene = Theme.scene(root, 1060, 740);
        Theme.show(stage, scene, "Customer Dashboard");
    }

    private void openMoneyDialog(boolean withdrawMode) {
        if (customer.getAccounts().isEmpty()) {
            Theme.showMessage(message, "Open an account first.", "error");
            return;
        }
        Stage popup = Theme.popup(stage);
        new DepositView(customer,
                accountsTable.getSelectionModel().getSelectedItem(),
                withdrawMode,
                text -> {
                    refresh();
                    Theme.showMessage(message, text, "ok");
                }).start(popup);
    }

    private void refresh() {
        Account selected = accountsTable.getSelectionModel().getSelectedItem();
        String keep = selected == null ? null : selected.getAccountNumber();

        List<Account> accounts = customer.getAccounts();
        accountsTable.getItems().setAll(accounts);

        double total = 0;
        for (Account a : accounts) {
            total += a.getBalance();
        }
        totalValue.setText(Theme.money(total));
        countValue.setText(String.valueOf(accounts.size()));

        Account toSelect = null;
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(keep)) {
                toSelect = a;
            }
        }
        if (toSelect == null && !accounts.isEmpty()) {
            toSelect = accounts.get(0);
        }

        if (toSelect != null) {
            accountsTable.getSelectionModel().select(toSelect);
        } else {
            accountsTable.getSelectionModel().clearSelection();
        }
        showTransactions(toSelect);
    }

    private void showTransactions(Account account) {
        if (account == null) {
            transactionsTitle.setText("TRANSACTIONS");
            transactionsTable.getItems().clear();
        } else {
            transactionsTitle.setText("TRANSACTIONS - " + account.getAccountNumber());
            transactionsTable.getItems().setAll(account.getTransactions());
        }
    }

    private VBox buildSidebar(Stage stage) {
        Circle circle = new Circle(38);
        circle.getStyleClass().add("avatar-circle");
        StackPane avatar = new StackPane(circle, Theme.label(initials(customer.getName()), "avatar-text"));

        Label name = Theme.label(customer.getName(), "sidebar-name");
        name.setWrapText(true);
        name.setAlignment(Pos.CENTER);

        Label chip = Theme.label("CUSTOMER", "sidebar-chip");

        VBox top = new VBox(10, avatar, name, chip);
        top.setAlignment(Pos.CENTER);

        VBox details = new VBox(14,
                detail("CUSTOMER ID", customer.getCustomerId()),
                detail("USERNAME", customer.getUsername()),
                detail("PHONE", customer.getPhone()),
                detail("ADDRESS", customer.getAddress())
        );
        if (customer.getCompanyName() != null && !customer.getCompanyName().isEmpty()) {
            details.getChildren().add(detail("COMPANY", customer.getCompanyName()));
        }

        Region divider = new Region();
        divider.getStyleClass().add("sidebar-divider");

        Region grow = new Region();
        VBox.setVgrow(grow, Priority.ALWAYS);

        Button logout = new Button("Logout");
        logout.getStyleClass().add("ghost-button");
        logout.setMaxWidth(Double.MAX_VALUE);
        logout.setOnAction(e -> new LoginView().start(stage));

        VBox sidebar = new VBox(22, top, divider, details, grow, logout);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPadding(new Insets(32, 24, 28, 24));
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250);
        sidebar.setMaxWidth(250);
        return sidebar;
    }

    private VBox detail(String caption, String value) {
        Label text = Theme.label(value == null || value.isEmpty() ? "-" : value, "sidebar-value");
        text.setWrapText(true);
        return new VBox(2, Theme.label(caption, "sidebar-caption"), text);
    }

    private String initials(String name) {
        String[] parts = name.trim().split("\\s+");
        String result = parts[0].isEmpty() ? "?" : parts[0].substring(0, 1);
        if (parts.length > 1) {
            result += parts[parts.length - 1].substring(0, 1);
        }
        return result.toUpperCase();
    }

    // ----- small helpers -----

    private VBox statCard(String caption, Label value) {
        VBox box = new VBox(2, Theme.label(caption, "stat-label"), value);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(12, 16, 12, 16));
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private VBox card(String heading, TableView<?> table) {
        VBox box = new VBox(8, Theme.label(heading, "section-title"), table);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(14, 16, 16, 16));
        return box;
    }

    private <T> TableColumn<T, String> addColumn(TableView<T> table, String title,
                                                 double share, Function<T, String> text) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new SimpleStringProperty(text.apply(cell.getValue())));
        column.setSortable(false);
        column.prefWidthProperty().bind(table.widthProperty().subtract(18).multiply(share));
        table.getColumns().add(column);
        return column;
    }

    private String pretty(TransactionType type) {
        String name = type.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    private String signedAmount(Transaction t) {
        String sign = t.getType() == TransactionType.WITHDRAWAL ? "-" : "+";
        return sign + Theme.number(t.getAmount());
    }

    private String typeColor(String type) {
        switch (type) {
            case "Deposit":
                return "#0b5a33";
            case "Withdrawal":
                return "#8f1d1d";
            default:
                return "#7a4f00";
        }
    }
}
