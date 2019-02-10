package controller;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdbc.FileHandler;
import system.GUI;

import org.controlsfx.dialog.*;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import application.StaticVars;
import compiler.Interpreter.Interpreter;
import compiler.Interpreter.RunTheProgram;
import compiler.global.Global;
import compiler.lexical.TokensWords;

public class ControllerActions {
	

	public synchronized  Tab addNewTab(String title)
	{
		RichTextArea rich = new RichTextArea();
		String notes = "//We still developing AlgoPlus this is just the version beta 2.0.0,"+
		"\n//So please report us if you find any bugs, we still working on 'functions', and we will work later  on"+
				"\n//completing the IDE because it's still so poor, and we will make sure to add awesome tools, Enjoy ! -- WebSite : www.AlgoPlus.ga!\n\n";
		String textToAppend = notes+TokensWords.ALGORITHME+" "+title+
				"\n"+TokensWords.VARIABLES+
				"\n"+TokensWords.DEBUT+
				"\n\t"+TokensWords.ECRIRE+"(\"Hello world !\");"+
				"\n"+TokensWords.FIN;
		
		CodeArea ca = rich.getCodeArea();
		ca.appendText(textToAppend);
		AnchorPane ap = new AnchorPane();
		Tab tab = new Tab(title);
		
		StackPane sp = new StackPane(new VirtualizedScrollPane(ca));
		tab.setContent(sp);
		return tab;
	}
	public void openAboutMenu()
	{
		Stage stage = new Stage();	
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("view/aboutMe.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("..\\res\\css\\main.css").toExternalForm());
		try {
			stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("img/logo.png")));
		}
		catch(Exception ex) {
			new FileHandler().log(ex.getMessage());
		}
		stage.setResizable(false);
		
		//To clock parent modal
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(GUI.stage.getScene().getWindow());
		stage.setScene(scene);
		stage.setTitle(StaticVars.aboutme);
		stage.show();
	}
	public void openFindWindow()
	{
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("view/findView.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Scene scene = new Scene(root);
		try {
			stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("img/logo.png")));
		}
		catch(Exception ex) {
			new FileHandler().log(ex.getMessage());
		}
		stage.setResizable(false);
		
		//To clock parent modal
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(GUI.stage.getScene().getWindow());
		stage.setScene(scene);
		stage.setTitle(StaticVars.find);
		stage.show();
	}
	public void runTextContextMenu()
	{
		
	}
	
	public CodeArea getOpenedCodeArea(TabPane tabPaneMain)
	{
		SelectionModel<Tab> sm = tabPaneMain.getSelectionModel();
		Tab tab = tabPaneMain.getTabs().get(sm.getSelectedIndex());
		StackPane sp = (StackPane) tab.getContent();
		VirtualizedScrollPane vsp = (VirtualizedScrollPane) sp.getChildren().get(0);
		return (CodeArea)vsp.getContent();
	}
	public void saveFileDialog(TabPane tabPaneMain,TreeView tree,boolean exist)
	{
		FileChooser chooser = new FileChooser();
		String lastPath = StaticVars.lastWorkingDirectory;
		if (lastPath != "")
			chooser.setInitialDirectory(new File(lastPath));
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(StaticVars.algoPlus, "*.algo"));
		
		SelectionModel<Tab> sm = tabPaneMain.getSelectionModel();
		Tab tab = tabPaneMain.getTabs().get(sm.getSelectedIndex());
		
		CodeArea textarea = getOpenedCodeArea(tabPaneMain);
		
		
		String title = tab.getText();
		if(title.endsWith(" *"))
		{
			title = title.substring(0,title.length() - " *".length());
		}
		
		chooser.setInitialFileName(title);
		chooser.setTitle(StaticVars.saveFile);
		
		File file = chooser.showSaveDialog(null);
		try {
			if(file != null)
			{
				String newTitle = file.getName();
				if(newTitle.endsWith(".algo"))
				{
					newTitle = newTitle.substring(0,newTitle.length() - ".algo".length());
					tab.setText(newTitle);
				}
				else if(newTitle.endsWith(".txt"))
				{
					newTitle = newTitle.substring(0,newTitle.length() - ".txt".length());
					
				}
				else
				{
					String ap = file.getAbsolutePath();
					file = new File(ap+".algo");
					newTitle = file.getName();
				}
				tab.setText(newTitle);
				FileWriter fw = new FileWriter(file);
				fw.write(textarea.getText());
				fw.flush();
				fw.close();
				this.listFilesInDirectory(tree, tabPaneMain);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			new FileHandler().log(ex.getMessage());
		}	
	}
	public void listFilesInDirectory(TreeView<String> tree,TabPane tabpane)
	{
		File file = new File(StaticVars.lastWorkingDirectory);
		if(StaticVars.lastWorkingDirectory.length()<1)
			StaticVars.lastWorkingDirectory = "/";
		
		if(file != null)
		{
			TreeItem root = new TreeItem(file.getPath());
			try {
				root.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/openFolder16.png"))));
			}
			catch(Exception ex) {}
			ArrayList<TreeItem> al = new ArrayList<TreeItem>();
			File files[] = file.listFiles();
			for(int i = 0;i<files.length;i++)
			{
				try {
					if(!files[i].isDirectory())
					{
						String fileName = files[i].getName();
						if(fileName.endsWith(".algo"))
						{
						
							TreeItem tv = new TreeItem(files[i].getName());
							
							try {
								tv.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/fileImage.png"))));
							}
							catch(Exception ex) {}
							al.add(tv);
						}
						
					}
				}
				catch(Exception ex)
				{
					new FileHandler().log(ex.getMessage());
				}
			}
			root.setExpanded(true);
			root.getChildren().addAll(al);
			tree.setRoot(root);
		}
	}
	
}
