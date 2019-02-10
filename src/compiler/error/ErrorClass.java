package compiler.error;

import javafx.beans.property.SimpleStringProperty;

public class ErrorClass {
	private final SimpleStringProperty line;
	private final SimpleStringProperty column;
	private final SimpleStringProperty file;
	private final SimpleStringProperty message;
	public ErrorClass()
	{
		this("","","","");
	}
	public ErrorClass(String fline,String fcolumn,String ffile,String fmessage)
	{
		this.line = new SimpleStringProperty(fline);
		this.column = new SimpleStringProperty(fcolumn);
		this.file = new SimpleStringProperty(ffile);
		this.message = new SimpleStringProperty(fmessage);
	}
	public String getLine() {
		return line.get();
	}
	public String getColumn() {
		return column.get();
	}
	public String getFile() {
		return file.get();
	}
	public String getMessage() {
		return message.get();
	}
}
