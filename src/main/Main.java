package main;

import helperDB.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.io.*;

/**
 * The main class contains the start and main functions. These begin the first page of the program (the login page),
 * run the program, and then end the program.
 */
public class Main extends Application {

    /**
     * The start function loads the beginning login page.
     *
     * @param primaryStage the primary stage of the program
     * @throws Exception exception thrown in case of error
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/login-form.fxml"));
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    /**
     * The main function runs the program.
     *
     * @param args main program arguments
     * @throws SQLException exception in case sql commands return an error
     * @throws IOException exception in case of input/output error
     */
    public static void main(String[] args) throws SQLException, IOException {
        JDBC.makeConnection();
        launch(args);
    }
}
