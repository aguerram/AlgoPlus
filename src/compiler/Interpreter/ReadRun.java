package compiler.Interpreter;

import java.util.Scanner;

import javafx.scene.control.TextArea;

public class ReadRun {
	public ReadRun(TextArea console,String ins)
	{
		run(console,ins);
	}
	public String run(TextArea console,String ins)
	{
		console.setEditable(true);
		int start = console.getText().length();
		RunTheProgram.waitingUserInput = false;
		return console.getText();
	}
}
