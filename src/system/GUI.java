package system;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.controlsfx.dialog.ProgressDialog;

import application.Main;
import application.StaticVars;
import compiler.global.Global;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jdbc.Jdbc;
public class GUI extends Application{
	public static Stage stage;
	public void start(Stage primaryStage) throws IOException
	{
		stage = primaryStage;	
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/mainView.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("css/main.css").toExternalForm());
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.setTitle(StaticVars.programTitle);
		stage.show();
		stage.setOnCloseRequest(e->{
			e.consume();
			saveChangesToDB();
		});
	}
	public void initial(final String... args)
	{
		launch(args);
	}
	public static void saveChangesToDB()
	{
		Jdbc db = new Jdbc();
		Task<Object> work = new Task<Object>(){
			@Override
			protected Object call() throws Exception {
				int progress = 0;
				//Changes to be saved and there progress
				db.setConfig("lastWorkingDirectory", StaticVars.lastWorkingDirectory);
				updateProgress(++progress, 99);
				db.setConfig("lang", StaticVars.systemLanguage);
				updateProgress(++progress, 99);
				
				updateProgress(99, 99);
				Thread.sleep(300);
				return null;
			}
			
		};
		ProgressDialog pd = new ProgressDialog(work);
		Stage stage = (Stage)pd.getDialogPane().getScene().getWindow();
		
		try {
			stage.getIcons().add(new Image(GUI.class.getClassLoader().getResourceAsStream("img/logo.png")));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		pd.setTitle(StaticVars.saving);
		pd.setContentText(StaticVars.saving);
		pd.setHeaderText(StaticVars.saving);
		pd.setOnHidden(e->{
			Platform.exit();
			System.exit(0);
		});
		Thread th = new Thread(work);
		th.setDaemon(true);
		th.start();		
	}
}
