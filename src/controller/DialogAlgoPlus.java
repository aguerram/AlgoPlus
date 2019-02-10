package controller;

import java.util.Optional;

import application.StaticVars;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import system.GUI;

public class DialogAlgoPlus {
	public static int ConfirmationClosing(String title,String header,String content)
	{
		/***
		 * returns 0 if cancel, 1 if ok 2 if accept saving
		 */
		ButtonType ok = new ButtonType(StaticVars.yes, ButtonData.OK_DONE);
		ButtonType no = new ButtonType(StaticVars.close, ButtonData.CANCEL_CLOSE);
		ButtonType save = new ButtonType(StaticVars.save, ButtonData.APPLY);
		
		Alert alert = new Alert(AlertType.INFORMATION,content,ok,no,save);
		Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
		try {
			stage.getIcons().add(new Image(GUI.class.getResourceAsStream("../res/img/logo.png")));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		alert.setTitle(title);
		alert.setHeaderText(header);
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.isPresent() && (result.get() == ok))
			return 1;
		else if(result.isPresent() && (result.get() == save))
			return 2;
		return 0;
	}
}
