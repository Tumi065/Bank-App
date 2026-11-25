package BankingSystemOOAD.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * One place for the look of every screen: colours, buttons, tables, messages.
 * The stylesheet is kept as text below and written to a temp file when the
 * app starts, so it works no matter how the project is run.
 */
public final class Theme {

    private static final String CSS =
            ".root { -fx-font-family: 'Segoe UI', Arial, sans-serif; -fx-font-size: 13px; -fx-accent: #0e8a7b; -fx-focus-color: #0e8a7b; -fx-faint-focus-color: rgba(14,138,123,0.25); }\n" +
            ".page { -fx-background-color: linear-gradient(to bottom, #ffffff 0%, #b6f2e8 100%); }\n" +
            ".header-bar { -fx-background-color: linear-gradient(to right, #0a5f57, #0e8a7b); -fx-padding: 18 28 18 28; }\n" +
            ".header-title { -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; }\n" +
            ".header-sub { -fx-text-fill: #e6fffb; -fx-font-size: 14px; }\n" +
            ".card { -fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #cfe9e5; -fx-border-radius: 14; -fx-effect: dropshadow(gaussian, rgba(10,60,55,0.16), 16, 0.05, 0, 4); }\n" +
            ".stat-label { -fx-text-fill: #2f5a55; -fx-font-size: 11px; -fx-font-weight: bold; }\n" +
            ".stat-value { -fx-text-fill: #0a4f49; -fx-font-size: 22px; -fx-font-weight: bold; }\n" +
            ".section-title { -fx-text-fill: #0a5f57; -fx-font-size: 13px; -fx-font-weight: bold; }\n" +
            ".form-label { -fx-text-fill: #0f2a2a; -fx-font-size: 13px; -fx-font-weight: bold; }\n" +
            ".hint { -fx-text-fill: #2f5a55; -fx-font-size: 12px; }\n" +
            ".dialog-title { -fx-text-fill: #0a4f49; -fx-font-size: 26px; -fx-font-weight: bold; }\n" +
            ".logo-circle { -fx-fill: linear-gradient(to bottom right, #14a392, #0a5f57); }\n" +
            ".logo-text { -fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: bold; }\n" +
            ".button { -fx-background-color: #e3e7ea; -fx-background-insets: 0; -fx-background-radius: 10; -fx-border-color: #b4bec4; -fx-border-radius: 10; -fx-border-width: 1; -fx-padding: 9 24 9 24; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0f2a2a; -fx-cursor: hand; }\n" +
            ".button:hover { -fx-background-color: #d3d9dd; }\n" +
            ".button:pressed { -fx-background-color: #c4cccf; }\n" +
            ".primary-button { -fx-background-color: linear-gradient(to bottom, #0e8a7b, #0a6f63); -fx-border-color: transparent; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(10,95,87,0.35), 8, 0.1, 0, 2); }\n" +
            ".primary-button:hover { -fx-background-color: linear-gradient(to bottom, #11a290, #0b7d70); }\n" +
            ".primary-button:pressed { -fx-background-color: #0a5f57; }\n" +
            ".ghost-button { -fx-background-color: transparent; -fx-border-color: rgba(255,255,255,0.8); -fx-text-fill: white; }\n" +
            ".ghost-button:hover { -fx-background-color: rgba(255,255,255,0.2); }\n" +
            ".ghost-button:pressed { -fx-background-color: rgba(255,255,255,0.3); }\n" +
            ".link-button { -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #0a5f57; -fx-underline: true; -fx-padding: 4 8 4 8; }\n" +
            ".link-button:hover { -fx-background-color: rgba(14,138,123,0.12); }\n" +
            ".text-field, .password-field { -fx-background-color: #f1f5f6; -fx-background-insets: 0; -fx-background-radius: 10; -fx-border-color: #9fb6b3; -fx-border-radius: 10; -fx-border-width: 1.5; -fx-padding: 9 12 9 12; -fx-font-size: 14px; -fx-text-fill: #0f2a2a; -fx-prompt-text-fill: #587471; }\n" +
            ".text-field:focused, .password-field:focused { -fx-background-color: white; -fx-border-color: #0e8a7b; }\n" +
            ".combo-box-base, .combo-box { -fx-background-color: #f1f5f6; -fx-background-insets: 0; -fx-background-radius: 10; -fx-border-color: #9fb6b3; -fx-border-radius: 10; -fx-border-width: 1.5; }\n" +
            ".combo-box-base:focused { -fx-border-color: #0e8a7b; }\n" +
            ".combo-box-base .arrow-button { -fx-background-color: transparent; }\n" +
            ".combo-box-base .arrow { -fx-background-color: #0a5f57; }\n" +
            ".combo-box .list-cell { -fx-text-fill: #0f2a2a; -fx-font-size: 14px; -fx-padding: 8 4 8 10; }\n" +
            ".combo-box-popup .list-view { -fx-background-color: white; -fx-border-color: #9fb6b3; -fx-background-radius: 8; -fx-border-radius: 8; }\n" +
            ".combo-box-popup .list-view .list-cell { -fx-background-color: white; -fx-text-fill: #0f2a2a; }\n" +
            ".combo-box-popup .list-view .list-cell:filled:hover, .combo-box-popup .list-view .list-cell:filled:selected { -fx-background-color: #b6f2e8; -fx-text-fill: #0f2a2a; }\n" +
            ".table-view { -fx-background-color: white; -fx-background-insets: 0; -fx-background-radius: 10; -fx-border-color: #cfe9e5; -fx-border-radius: 10; -fx-padding: 0; }\n" +
            ".table-view .column-header-background { -fx-background-color: #dff5f1; -fx-background-radius: 10 10 0 0; }\n" +
            ".table-view .column-header, .table-view .filler { -fx-size: 38px; -fx-background-color: transparent; -fx-border-color: transparent transparent #9fd8cf transparent; }\n" +
            ".table-view .column-header .label { -fx-alignment: CENTER; -fx-text-fill: #0a4f49; -fx-font-weight: bold; }\n" +
            ".table-view .table-row-cell { -fx-background-color: white; -fx-cell-size: 36px; -fx-table-cell-border-color: transparent; }\n" +
            ".table-view .table-row-cell:odd { -fx-background-color: #f3fbf9; }\n" +
            ".table-view .table-row-cell:filled:hover { -fx-background-color: #e3f6f2; }\n" +
            ".table-view .table-row-cell:filled:selected, .table-view:focused .table-row-cell:filled:selected { -fx-background-color: #b6f2e8; }\n" +
            ".table-view .table-cell { -fx-alignment: CENTER; -fx-text-fill: #0f2a2a; }\n" +
            ".table-view .table-row-cell:filled:selected .table-cell, .table-view:focused .table-row-cell:filled:selected .table-cell { -fx-text-fill: #0f2a2a; }\n" +
            ".table-view .placeholder .label { -fx-text-fill: #46635f; }\n" +
            ".msg { -fx-background-radius: 8; -fx-padding: 8 14 8 14; -fx-font-size: 13px; -fx-font-weight: bold; -fx-alignment: CENTER; -fx-text-alignment: CENTER; }\n" +
            ".msg-ok { -fx-background-color: #d5f5e3; -fx-text-fill: #0b5a33; }\n" +
            ".msg-error { -fx-background-color: #fde2e2; -fx-text-fill: #8f1d1d; }\n" +
            ".msg-info { -fx-background-color: #d3f1ec; -fx-text-fill: #0a4f49; }\n" +
            ".dashboard-root { -fx-background-color: #eef6f5; }\n" +
            ".sidebar { -fx-background-color: linear-gradient(to bottom, #0a4f49, #0b6f65); }\n" +
            ".avatar-circle { -fx-fill: #e6fffb; }\n" +
            ".avatar-text { -fx-text-fill: #0a4f49; -fx-font-size: 28px; -fx-font-weight: bold; }\n" +
            ".sidebar-name { -fx-text-fill: white; -fx-font-size: 19px; -fx-font-weight: bold; -fx-text-alignment: CENTER; }\n" +
            ".sidebar-chip { -fx-background-color: rgba(255,255,255,0.2); -fx-background-radius: 20; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3 12 3 12; }\n" +
            ".sidebar-caption { -fx-text-fill: #c4f0e9; -fx-font-size: 11px; -fx-font-weight: bold; }\n" +
            ".sidebar-value { -fx-text-fill: white; -fx-font-size: 14px; }\n" +
            ".sidebar-divider { -fx-background-color: rgba(255,255,255,0.28); -fx-pref-height: 1; -fx-min-height: 1; -fx-max-height: 1; }\n" +
            ".page-title { -fx-text-fill: #0a3f3a; -fx-font-size: 27px; -fx-font-weight: bold; }\n" +
            ".page-subtitle { -fx-text-fill: #46635f; -fx-font-size: 14px; }\n" +
            ".status-chip { -fx-background-color: #d5f5e3; -fx-background-radius: 20; -fx-text-fill: #0b5a33; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 14 6 14; }\n" +
            ".stat-card-primary { -fx-background-color: linear-gradient(to bottom right, #0e8a7b, #0a5f57); -fx-background-radius: 14; -fx-effect: dropshadow(gaussian, rgba(10,95,87,0.35), 16, 0.05, 0, 4); }\n" +
            ".stat-label-light { -fx-text-fill: #dffaf5; -fx-font-size: 11px; -fx-font-weight: bold; }\n" +
            ".stat-value-light { -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; }\n" +
            ".brand-panel { -fx-background-color: linear-gradient(to bottom, #0a4f49, #0b6f65); }\n" +
            ".brand-title { -fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold; }\n" +
            ".brand-text { -fx-text-fill: #d9fbf5; -fx-font-size: 14px; -fx-text-alignment: CENTER; }\n" +
            ".form-panel { -fx-background-color: white; }\n" +
            ".popup-header { -fx-background-color: linear-gradient(to right, #0a5f57, #0e8a7b); -fx-padding: 22 32 20 32; }\n" +
            ".popup-title { -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; }\n" +
            ".popup-sub { -fx-text-fill: #e6fffb; -fx-font-size: 13px; }\n" +
            ".popup-body { -fx-background-color: #f4faf9; }\n" +
            "";

    private static String cssUrl;

    private Theme() {
    }

    private static String stylesheet() {
        if (cssUrl == null) {
            try {
                Path file = Files.createTempFile("banking-theme", ".css");
                Files.write(file, CSS.getBytes(StandardCharsets.UTF_8));
                file.toFile().deleteOnExit();
                cssUrl = file.toUri().toString();
            } catch (IOException e) {
                cssUrl = "";
            }
        }
        return cssUrl;
    }

    /** Creates a scene with the banking stylesheet already applied. */
    public static Scene scene(Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        String url = stylesheet();
        if (!url.isEmpty()) {
            scene.getStylesheets().add(url);
        }
        return scene;
    }

    /** Puts a scene on a stage (used both for first show and for switching screens). */
    public static void show(Stage stage, Scene scene, String title) {
        boolean wasShowing = stage.isShowing();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.sizeToScene();
        if (wasShowing) {
            stage.centerOnScreen();
        } else {
            stage.show();
        }
    }

    /** A pop-up window that blocks the screen behind it until it is closed. */
    public static Stage popup(Stage owner) {
        Stage popup = new Stage();
        popup.initOwner(owner);
        popup.initModality(Modality.WINDOW_MODAL);
        popup.setResizable(false);
        return popup;
    }

    /** Teal panel with the bank name, used on the left of the login and register screens. */
    public static VBox brandPanel() {
        Circle circle = new Circle(34);
        circle.getStyleClass().add("avatar-circle");
        StackPane logo = new StackPane(circle, label("B", "avatar-text"));

        Label text = label("Manage your accounts, deposits and interest in one place.", "brand-text");
        text.setWrapText(true);

        VBox panel = new VBox(16, logo, label("Banking System", "brand-title"), text);
        panel.getStyleClass().add("brand-panel");
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(40));
        panel.setPrefWidth(320);
        panel.setMinWidth(320);
        panel.setMaxWidth(320);
        return panel;
    }

    /** Teal title band at the top of a pop-up. */
    public static VBox popupHeader(String title, String subtitle) {
        VBox header = new VBox(3, label(title, "popup-title"), label(subtitle, "popup-sub"));
        header.getStyleClass().add("popup-header");
        return header;
    }

    public static Label label(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    /** A caption above an input, e.g. "Username" over a text field. */
    public static VBox field(String caption, Control input) {
        input.setMaxWidth(Double.MAX_VALUE);
        return new VBox(5, label(caption, "form-label"), input);
    }

    public static StackPane logo() {
        Circle circle = new Circle(28);
        circle.getStyleClass().add("logo-circle");
        StackPane logo = new StackPane(circle, label("B", "logo-text"));
        logo.setAlignment(Pos.CENTER);
        return logo;
    }

    /** The message line at the bottom of a screen. Hidden until it has text. */
    public static Label messageLabel() {
        Label label = new Label();
        label.getStyleClass().add("msg");
        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setVisible(false);
        label.setManaged(false);
        return label;
    }

    /** kind is "ok" (green), "error" (red) or "info" (teal). */
    public static void showMessage(Label label, String text, String kind) {
        label.getStyleClass().removeAll("msg-ok", "msg-error", "msg-info");
        label.getStyleClass().add("msg-" + kind);
        label.setText(text);
        label.setVisible(true);
        label.setManaged(true);
    }

    public static void clearMessage(Label label) {
        label.setText("");
        label.setVisible(false);
        label.setManaged(false);
    }

    public static String number(double value) {
        return String.format(Locale.US, "%,.2f", value);
    }

    public static String money(double value) {
        return "BWP " + number(value);
    }
}
