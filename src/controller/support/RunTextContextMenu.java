package controller.support;


import compiler.error.ErrorHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import jdbc.FileHandler;

public class RunTextContextMenu {
	private MenuItem copy;
	private MenuItem paste;
	private MenuItem clear;
	
	public RunTextContextMenu(TextArea ta,MenuItem copy,MenuItem clear,MenuItem paste) {
		this.copy = copy;
		this.clear = clear;
		if(!ta.isEditable())
		{
			paste.setDisable(true);
		}
		this.paste = paste;
		this.listeners(ta);
	}
	
	
	public void listeners(TextArea ta)
	{
		//COPY
		try {
			copy.setOnAction(e->{	
				final Clipboard clipboard = Clipboard.getSystemClipboard();
				final ClipboardContent content = new ClipboardContent();
				content.putString(ta.getSelectedText());
				clipboard.setContent(content);
			});
		}
		catch(Exception ex)
		{
			new FileHandler().log(ex.getMessage());
		}
		//PASTE
		try {
			paste.setOnAction(e->{	
				final Clipboard clipboard = Clipboard.getSystemClipboard();
				int caretPos = ta.getCaretPosition();
				String start = ta.getText(0, caretPos);
				String end = ta.getText(caretPos, ta.getText().length());
				ta.setText(start+clipboard.getString()+end);
				
			});
		}
		catch(Exception ex)
		{
			new FileHandler().log(ex.getMessage());
		}
		
	}
	
}
