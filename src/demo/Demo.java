package demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author vinny
 *
 * No bonus overhead besides launching the main application form. Does no logic on its own.
 */
public class Demo extends Application{

	@Override
    public void start(Stage primaryStage) throws Exception{

		// load main scene
        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
	}	

    public static void main(String[] args) {
        launch(args);
    }

}
