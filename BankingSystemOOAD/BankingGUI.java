package BankingSystemOOAD;

import BankingSystemOOAD.view.LoginView;

import javafx.application.Application;
import javafx.stage.Stage;

//This class runs the login screen.//
public class BankingGUI extends Application {

    @Override
    public void start(Stage stage) {
        new LoginView().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
