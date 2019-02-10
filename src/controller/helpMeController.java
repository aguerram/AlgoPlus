package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextArea;

import application.StaticVars;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class helpMeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text algoPlusTitle;

    @FXML
    private JFXTextArea peopleTextArea;
    @FXML
    private ImageView logo;
    @FXML
    void initialize() {
        assert algoPlusTitle != null : "fx:id=\"algoPlusTitle\" was not injected: check your FXML file 'aboutMe.fxml'.";
        assert peopleTextArea != null : "fx:id=\"peopleTextArea\" was not injected: check your FXML file 'aboutMe.fxml'.";
        logo.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/logo.png")));
        algoPlusTitle.setText(StaticVars.AlgoPlusVersion);
        peopleTextArea.setText(text);
    }
    
    private String text = "Développé par:\r\n" + 
    		"	Mostafa Aguerram\r\n" + 
    		"\r\n" + 
    		"Spécial remerciement à:\r\n" + 
    		"	Professeur Fahd Karami\r\n" + 
    		"	Mr Mehdi Erraji\r\n" + 
    		"---------------------------\r\n"+
    		"Lahcen LAMKIRICH\r\n" + 
    		"\r\n" + 
    		"\r\n" + 
    		"\r\n" + 
    		"\r\n" + 
    		"\r\n" + 
    		"\r\n" + 
    		"Site officiel :\r\n" + 
    		"				www.algoplus.ga";
}
