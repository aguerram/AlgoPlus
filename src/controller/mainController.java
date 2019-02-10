package controller;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.controlsfx.dialog.ExceptionDialog;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import de.jensd.fx.glyphs.fontawesome.*;

import application.StaticVars;
import compiler.StartCodeChecker;
import compiler.Interpreter.Interpreter;
import compiler.Interpreter.RunTheProgram;
import compiler.error.ErrorClass;
import compiler.error.ErrorHandler;
import compiler.global.Global;
import controller.support.RunTextContextMenu;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import jdbc.FileHandler;
import system.GUI;

public class mainController extends ControllerActions {
	@FXML
	private TreeView treeProjectExplorer;

	@FXML
	private TabPane tabPaneMain; // Main tabPane

	@FXML
	private BorderPane panelFooter;

	@FXML
	private Label labelLines;

	@FXML
	private TextArea textRunTime;

	@FXML
	private Pane panelHelpersButton;

	@FXML
	private Button buttonStopExecution;
	@FXML
	private Button buttonClearConsole;
	@FXML
	private MenuBar menuMain;
	@FXML
	private MenuItem newFileFileMenu;
	@FXML
	private MenuItem newProjectFileMenu;

	@FXML
	private MenuItem openFileFileMenu;

	@FXML
	private MenuItem openFolderFileMenu;
	@FXML
	private MenuItem saveFileFileMenu;
	@FXML
	private MenuItem saveAsFileFileMenu;

	@FXML
	private MenuItem quitFileMenu;

	@FXML
	private MenuItem menuMainRunRun;
	@FXML
	private MenuItem aboutMeHelpMenu;
	@FXML
	private TabPane bottomTabPane;
	// Log Table View
	@FXML
	private TableView<String> logTableView;
	@FXML
	private TableColumn<ErrorClass, String> logTableViewLineCol;
	@FXML
	private TableColumn<ErrorClass, String> logTableViewColonneCol;
	@FXML
	private TableColumn<ErrorClass, String> logTableViewFileCol;
	@FXML
	private TableColumn<ErrorClass, String> logTableViewMessageCol;
	// -------------------
	// run text ContextMenu
	private RunTextContextMenu runTextContextMenu;
	@FXML
	private ContextMenu textRunContextMenu;
	@FXML
	private MenuItem textRunContextMenuClear;
	@FXML
	private MenuItem textRunContextMenuCopy;
	@FXML
	private MenuItem textRunContextMenuPaste;
	@FXML
	private Label TimeToken;
	// ----------------
	// Progress bar
	@FXML
	private JFXProgressBar runningProgressBar;
	// -------------
	@FXML
	private Menu fileMainMenu;

	private TextArea OldtextRunTime;
	// ToolBar
	@FXML
	private Button NewFileNav;
	@FXML
	private Button OpenFileNav;
	@FXML
	private Button SaveFileNav;
	@FXML
	private Button CloseAllNav;
	@FXML
	private Button FindNav;
	@FXML
	private Button FindAndReplaceNav;
	@FXML
	private Button CompileAndRunNav;
	@FXML
	private Button RunNav;
	@FXML
	private Button StopExecutionNav;
	// --------------

	@FXML
	void initialize() {
		assert treeProjectExplorer != null : "fx:id=\"treeProjectExplorer\" was not injected: check your FXML file 'mainView.fxml'.";
		assert tabPaneMain != null : "fx:id=\"tabPaneMain\" was not injected: check your FXML file 'mainView.fxml'.";
		assert panelFooter != null : "fx:id=\"panelFooter\" was not injected: check your FXML file 'mainView.fxml'.";
		assert labelLines != null : "fx:id=\"labelLines\" was not injected: check your FXML file 'mainView.fxml'.";
		assert textRunTime != null : "fx:id=\"textRunTime\" was not injected: check your FXML file 'mainView.fxml'.";
		assert panelHelpersButton != null : "fx:id=\"panelHelpersButton\" was not injected: check your FXML file 'mainView.fxml'.";
		assert buttonStopExecution != null : "fx:id=\"buttonStopExecution\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuMain != null : "fx:id=\"menuMain\" was not injected: check your FXML file 'mainView.fxml'.";
		assert newFileFileMenu != null : "fx:id=\"newFileFileMenu\" was not injected: check your FXML file 'mainView.fxml'.";
		assert fileMainMenu != null : "fx:id=\"fileMainMenu\" was not injected: check your FXML file 'mainView.fxml'.";
		assert aboutMeHelpMenu != null : "fx:id=\"aboutMeHelpMenu\" was not injected: check your FXML file 'mainView.fxml'.";
		assert TimeToken != null : "fx:id=\"aboutMeHelpMenu\" was not injected: check your FXML file 'mainView.fxml'.";

		// inisialaize button
		this.buttonStopExecutionInit();
		this.buttonClearConsoleInit();
		// --------------
		tabPaneMain.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
		this.textRunTime.setEditable(false);
		runningProgressBar.setVisible(false);
		// initialize listeners
		this.iconsInit();
		this.Listen();

		// Translate
		this.translateComponent();

		// inistiale Log Table View
		this.inis_loagTableVieew();
		// initialize line number label
		this.labelLinesListener();

		// Initialize other classes
		runTextContextMenu = new RunTextContextMenu(this.textRunTime, this.textRunContextMenuCopy,
				this.textRunContextMenuClear, this.textRunContextMenuPaste);
		textRunTime.setWrapText(true);
		// Initialize Tree Project Explorer
		listFilesInDirectory(treeProjectExplorer, tabPaneMain);
		addListenerToTreeView();

		addActionListen();
	}

	// initialize listeners
	private void Listen() {
		// Execute the program
		this.menuMainRunRun.setOnAction(e -> {
			runningProgressBar.setVisible(true);
			this.executeProgram();
		});
	}

	private void translateComponent() {
		this.newFileFileMenu.setText(StaticVars.newfile);
		this.newProjectFileMenu.setText(StaticVars.newprojecet);
		this.quitFileMenu.setText(StaticVars.quit);
		this.openFileFileMenu.setText(StaticVars.openfile);
		this.openFolderFileMenu.setText(StaticVars.opendfolder);
		this.fileMainMenu.setText(StaticVars.file);

	}

	private void inis_loagTableVieew() {
		logTableViewLineCol.setCellValueFactory(new PropertyValueFactory<ErrorClass, String>("line"));
		logTableViewColonneCol.setCellValueFactory(new PropertyValueFactory<ErrorClass, String>("column"));
		logTableViewFileCol.setCellValueFactory(new PropertyValueFactory<ErrorClass, String>("file"));
		logTableViewMessageCol.setCellValueFactory(new PropertyValueFactory<ErrorClass, String>("message"));
		ErrorClass ec = new ErrorClass("1", "54", "testAlgo.algo", "Main Function ot found");
		this.logTableView.setPlaceholder(new Label(""));
	}

	private void buttonStopExecutionInit() {

		this.buttonStopExecution.setPrefHeight(24);
		this.buttonStopExecution.setPrefWidth(24);
		Tooltip tp = new Tooltip(StaticVars.stopExecution);
		this.buttonStopExecution.setTooltip(tp);
		this.buttonStopExecution.setOnMouseEntered(e -> {
			this.buttonStopExecution.setCursor(Cursor.HAND);
			GUI.stage.show();
		});
	}

	private void buttonClearConsoleInit() {

		this.buttonClearConsole.setPrefHeight(24);
		this.buttonClearConsole.setPrefWidth(24);
		Tooltip tp = new Tooltip(StaticVars.clearConsole);
		this.buttonClearConsole.setTooltip(tp);
		this.buttonClearConsole.setOnMouseEntered(e -> {
			this.buttonClearConsole.setCursor(Cursor.HAND);
			GUI.stage.show();
		});
	}

	private void addActionListen() {
		// Navigation bar Listener
		this.CompileAndRunNav.setOnAction(e -> {
			this.executeProgram();
		});

		this.StopExecutionNav.setOnAction(e -> {
			this.stopExecution();
		});

		this.SaveFileNav.setOnAction(e -> {
			this.SaveOpenedFiled();
		});
		this.CloseAllNav.setOnAction(e -> {
			this.tabPaneMain.getTabs().clear();
		});
		this.NewFileNav.setOnAction(e -> {
			this.newFileFileMenu();
		});
		// Dropdown menu listener
		this.saveFileFileMenu.setOnAction(e -> {
			this.SaveOpenedFiled();
		});
		this.saveAsFileFileMenu.setOnAction(e -> {
			this.saveAs();
		});

		this.RunNav.setTooltip(new Tooltip(StaticVars.run));
		this.StopExecutionNav.setTooltip(new Tooltip(StaticVars.stopExecution));
		this.CompileAndRunNav.setTooltip(new Tooltip(StaticVars.compileRun));
		this.FindNav.setTooltip(new Tooltip(StaticVars.find));
		this.FindAndReplaceNav.setTooltip(new Tooltip(StaticVars.findAndReplace));
		this.SaveFileNav.setTooltip(new Tooltip(StaticVars.save));
		this.OpenFileNav.setTooltip(new Tooltip(StaticVars.openfile));
		this.NewFileNav.setTooltip(new Tooltip(StaticVars.newfile));
		this.CloseAllNav.setTooltip(new Tooltip(StaticVars.closeAll));
	}

	private void iconsInit() {
		try {
			// stage icon
			GUI.stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("img/logo.png")));

			this.buttonStopExecution.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/stop.png"))));
			this.buttonClearConsole.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/clear24.png"))));

			// ToolTip

			this.RunNav.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/run24.png"))));
			this.StopExecutionNav.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/stop.png"))));
			this.CompileAndRunNav.setGraphic(new ImageView(
					new Image(getClass().getClassLoader().getResourceAsStream("img/compileAndRun24.png"))));
			this.FindNav.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/find24.png"))));
			this.FindAndReplaceNav.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/replace24.png"))));
			this.SaveFileNav.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/save24.png"))));
			this.OpenFileNav.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/openFile24.png"))));
			this.SaveFileNav.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/save24.png"))));
			this.CloseAllNav.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/closeAll24.png"))));
			this.NewFileNav.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/newFile24.png"))));
			// ----------------------------------------------
			// Main Menu icons
			// File menu Icons
			this.newFileFileMenu.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/newFile16.png"))));

			this.menuMainRunRun.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/run16.png"))));
			this.saveFileFileMenu.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/save16.png"))));
			this.openFolderFileMenu.setGraphic(
					new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/openFolder16.png"))));
		} catch (Exception ex) {
			ex.printStackTrace();
			new FileHandler().log(ex.getMessage());
		}
	}

	/* Main Menu */
	// File Menu
	public void newFileFileMenu() {
		int id = tabPaneMain.getTabs().size();
		String title = StaticVars.tabText + "" + String.valueOf(id);
		this.newFileFileMenu(title);

	}

	public void newFileFileMenu(String title) {
		int id = tabPaneMain.getTabs().size();
		tabPaneMain.getTabs().add(this.addNewTab(title));
		SelectionModel<Tab> sm = tabPaneMain.getSelectionModel();
		sm.select(id);
		this.labelLinesListener();
		this.textCodeAreaListener();
	}

	public void quitApplication() {
		GUI.saveChangesToDB();
	}

	public void clearConsoleText() {
		this.textRunTime.clear();
		this.logTableView.setItems(null);
	}

	public void executeProgram() {
		if (tabPaneMain.getTabs().size() > 0) {
			Global.startedTime = System.currentTimeMillis();
			StartCodeChecker start = new StartCodeChecker();
			try {

				int id = this.tabPaneMain.getTabs().size();
				SelectionModel<Tab> sm = this.tabPaneMain.getSelectionModel();
				Tab tab = this.tabPaneMain.getTabs().get(sm.getSelectedIndex());

				CodeArea textarea = getOpenedCodeArea();

				this.textRunTime.setEditable(false);
				ErrorHandler.fileName = tab.getText() + StaticVars.Extentions;
				start.main(textarea.getText(), tab.getText(), this.logTableView, bottomTabPane);

				if (ErrorHandler.ErrorData.size() == 0) {

					Thread t = new Thread(() -> {
						RunTheProgram run = new RunTheProgram(0, Interpreter.Instructions.size());
						run.run(textRunTime, false);

					});

					t.setName("RunProgram");
					try {
						t.start();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					synchronized (t) {
						// FIXME add this t a separated text area under textRunTime
						// textRunTime.appendText("\n------------------\nI took :
						// "+(Global.endedTime-Global.startedTime)+" ms");
						Global.endedTime = System.currentTimeMillis();
						TimeToken.setText(StaticVars.compiledIn + " "
								+ String.valueOf(Global.endedTime - Global.startedTime) + "ms");
					}
				}

				/*
				 * Timer time = new Timer(); TimerTask ts = new TimerTask() {
				 * 
				 * @Override public void run() { runningProgressBar.setVisible(false);
				 * 
				 * }
				 * 
				 * }; time.schedule(ts, 3000L);
				 */
			} catch (Exception ex) {
				ExceptionDialog exds = new ExceptionDialog(ex);
				exds.show();
				new FileHandler().log(ex);
			}
		}
	}

	public void labelLinesListener() {
		this.labelLines.setVisible(false);

		if (tabPaneMain.getTabs().size() > 0) {
			try {
				CodeArea textarea = getOpenedCodeArea();
				textarea.setOnKeyPressed(e -> {
					int lines = textarea.getText().split("\n|\r").length;
					labelLines.setText(String.valueOf(textarea.getCaretColumn()));
				});
			} catch (Exception ex) {

			}
		}

	}

	public CodeArea getOpenedCodeArea() {
		SelectionModel<Tab> sm = this.tabPaneMain.getSelectionModel();
		Tab tab = this.tabPaneMain.getTabs().get(sm.getSelectedIndex());
		StackPane sp = (StackPane) tab.getContent();
		VirtualizedScrollPane vsp = (VirtualizedScrollPane) sp.getChildren().get(0);
		return (CodeArea) vsp.getContent();
	}

	public void textCodeAreaListener() {
		if (tabPaneMain.getTabs().size() > 0) {
			try {
				int id = this.tabPaneMain.getTabs().size();
				SelectionModel<Tab> sm = this.tabPaneMain.getSelectionModel();
				Tab tab = this.tabPaneMain.getTabs().get(sm.getSelectedIndex());
				CodeArea textarea = getOpenedCodeArea();
				textarea.setContextMenu(this.addContextMenutoTextCodeArea());

				textarea.setOnKeyTyped(e -> {
					int tabSize = getLastLine(textarea);
					if (e.getCharacter().equals("\n") || e.getCharacter().equals("\r")) {
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < tabSize; i++) {
							sb.append("\t");
						}
						Platform.runLater(() -> {
							String ss = new String(sb);
							textarea.replaceText(textarea.getCaretPosition(), textarea.getCaretPosition(), ss);
						});
					}
					String title = tab.getText();
					if (!title.endsWith(" *")) {
						tab.setText(title + " *");
					}
				});
				tab.setOnCloseRequest(e -> {

					if (tab.getText().endsWith(" *")) {
						int select = DialogAlgoPlus.ConfirmationClosing("This file isn't saved",
								"Your file isn't saved",
								"Your file isn't save, Are your sure you want to close it without saving ?");
						if (select == 0) {
							// Cancel choice
							e.consume();
						} else if (select == 2) {
							// Save choice
							this.SaveOpenedFiled();
						} else {
							// Ok choice
						}
					}
				});
			} catch (Exception ex) {

			}
		}
	}

	private int getLastLine(CodeArea ca) {
		int cartePos = ca.getCaretPosition() - 1;
		String txt = ca.getText();
		boolean openQ = false, openDQ = false;
		int tabCount = 0;
		for (int i = 0; i < cartePos; i++) {
			char c = txt.charAt(i);
			if (c == '"' && !openQ) {
				openDQ = !openDQ;
			} else if (c == '\'' && !openDQ) {
				openQ = !openQ;
			}
			if (openQ || openDQ)
				continue;
			if (c == '\t') {
				tabCount++;
			} else if (c == '\n' || c == '\r') {
				tabCount = 0;
			}
		}
		return tabCount;
	}

	public ContextMenu addContextMenutoTextCodeArea() {
		ContextMenu context = new ContextMenu();
		MenuItem copy = new MenuItem(StaticVars.copy);
		MenuItem cut = new MenuItem(StaticVars.cut);
		MenuItem paste = new MenuItem(StaticVars.paste);
		MenuItem undo = new MenuItem(StaticVars.undo);
		MenuItem redo = new MenuItem(StaticVars.redo);
		MenuItem save = new MenuItem(StaticVars.save);
		MenuItem run = new MenuItem(StaticVars.compileRun);

		// Set icons
		copy.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.COPY));
		cut.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.CUT));
		paste.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PASTE));

		undo.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.UNDO));
		redo.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.REPEAT));

		save.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SAVE));
		run.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PLAY_CIRCLE));
		// set Accelerator
		copy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
		cut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
		paste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));

		undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
		redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));

		save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		run.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));

		// set listener

		save.setOnAction(e -> {
			this.SaveOpenedFiled();
		});
		run.setOnAction(e -> {
			this.executeProgram();
		});
		context.getItems().addAll(copy, paste, cut, new SeparatorMenuItem(), undo, redo, new SeparatorMenuItem(), save,
				new SeparatorMenuItem(), run);

		return context;
	}

	public void addListenerToTreeView() {
		try {
			treeProjectExplorer.setOnMouseClicked(e -> {
				if (e.getClickCount() == 2) {
					SelectionModel sm = treeProjectExplorer.getSelectionModel();
					TreeItem tv = (TreeItem) sm.getSelectedItem();
					String rootPath = (String) treeProjectExplorer.getRoot().getValue();
					String title = (String) tv.getValue();
					String fullPath = rootPath + "\\" + title;
					if (!title.contains("\\")) {
						if (!this.isTabOpened(title)) {
							File file = new File(fullPath);
							if (file.exists()) {
								if (title.endsWith(".algo"))
									title = title.substring(0, title.length() - ".algo".length());
								this.newFileFileMenu(title);
								try {
									BufferedReader br = new BufferedReader(new FileReader(file));
									String line;
									StringBuffer sb = new StringBuffer();
									while ((line = br.readLine()) != null) {
										sb.append(line);
										sb.append(System.lineSeparator());
									}
									sb.delete(sb.length() - 1, sb.length());
									br.close();
									sm = tabPaneMain.getSelectionModel();
									Tab tab = tabPaneMain.getTabs().get(sm.getSelectedIndex());

									CodeArea textarea = getOpenedCodeArea();
									textarea.replaceText(new String(sb));

								} catch (Exception ex) {
									new FileHandler().log(ex.getMessage());
									new ExceptionDialog(ex).show();
									;
								}

							}
						} else {
							int index = 0;
							for (Tab tab : tabPaneMain.getTabs()) {
								if (title.endsWith(".algo"))
									title = title.substring(0, title.length() - ".algo".length());
								if (tab.getText().trim().equals(title.trim())) {
									break;
								}
								index++;
							}
							try {
								tabPaneMain.getSelectionModel().select(index);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			});
		} catch (Exception ex) {
			new FileHandler().log(ex.getMessage());
		}
	}

	public void SaveOpenedFiled() {
		if (this.tabPaneMain.getTabs().size() > 0 && this.treeProjectExplorer.getRoot() != null) {
			String FileTitle = (String) treeProjectExplorer.getRoot().getValue();
			SelectionModel sm2 = this.tabPaneMain.getSelectionModel();
			String title = this.tabPaneMain.getTabs().get(sm2.getSelectedIndex()).getText();
			if (title.endsWith(" *"))
				title = title.substring(0, title.length() - " *".length());
			String path = FileTitle + "\\" + title + ".algo";
			if (new File(path).exists()) {
				SelectionModel<Tab> sm = tabPaneMain.getSelectionModel();
				Tab tab = tabPaneMain.getTabs().get(sm.getSelectedIndex());

				CodeArea textarea = getOpenedCodeArea();

				try {
					FileWriter fw = new FileWriter(path);
					fw.write(textarea.getText());
					fw.flush();
					fw.close();

					Platform.runLater(() -> {
						Tab tab2 = tabPaneMain.getTabs().get(sm.getSelectedIndex());
						String title2 = tab2.getText();
						if (title2.endsWith(" *"))
							title2 = title2.substring(0, title2.length() - " *".length());
						tab2.setText(title2);
					});

				} catch (IOException e) {
					e.printStackTrace();
					new FileHandler().log(e.getMessage());
				}
			} else {
				this.saveFileDialog(tabPaneMain, treeProjectExplorer, false);
			}
		} else if (this.tabPaneMain.getTabs().size() > 0) {
			this.saveFileDialog(tabPaneMain, treeProjectExplorer, false);
		}

	}

	public void saveAs() {
		this.saveFileDialog(tabPaneMain, treeProjectExplorer, false);
	}

	public boolean isTabOpened(String title) {
		if (title.endsWith(".algo"))
			title = title.substring(0, title.length() - ".algo".length());
		for (Tab tab : tabPaneMain.getTabs()) {
			if (tab.getText().trim().equals(title.trim())) {
				return true;
			}
		}
		return false;
	}

	public void stopExecution() {
		if (Interpreter.Instructions.size() > 0) {
			if (RunTheProgram.index < Interpreter.Instructions.size()) {
				RunTheProgram.index = Interpreter.Instructions.size() - 1;
				RunTheProgram.waitingUserInput = false;
				textRunTime.setEditable(false);
			}
		}
		//TODO select option
		//getOpenedCodeArea().setStyleClass(0, 15, "select");
	}

	// Open file menu action
	public void openFile() {
		FileChooser chooser = new FileChooser();

		String lastPath = StaticVars.lastWorkingDirectory;
		if (lastPath != "")
			chooser.setInitialDirectory(new File(lastPath));
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(StaticVars.algoPlus, "*.algo"));
		File file = chooser.showOpenDialog(GUI.stage);
		if (file != null) {
			StaticVars.lastWorkingDirectory = file.getParent();
			String title = file.getName().substring(0, file.getName().length() - ".algo".length());
			if (this.isTabOpened(title))
				return;
			tabPaneMain.getTabs().add(this.addNewTab(title));

			FileReader fis;
			try {
				fis = new FileReader(file);
				BufferedReader reader = new BufferedReader(fis);
				String line = "";
				StringBuffer sb = new StringBuffer();
				while ((line = reader.readLine()) != null) {
					sb.append(line + System.lineSeparator());
				}
				tabPaneMain.getSelectionModel().select(tabPaneMain.getTabs().size() - 1);
				sb.delete(sb.length() - 1, sb.length());
				this.getOpenedCodeArea().appendText(new String(sb));
				this.listFilesInDirectory(treeProjectExplorer, tabPaneMain);
			} catch (Exception e) {
				e.printStackTrace();
				new FileHandler().log(e);
			}
		}

	}
}
