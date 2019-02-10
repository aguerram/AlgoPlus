package compiler;

import java.util.ArrayList;
import java.util.HashMap;

import compiler.Interpreter.Interpreter;
import compiler.Interpreter.RunTheProgram;
import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.LexicalAnalysis;
import compiler.syntaxe.Syntaxe;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class StartCodeChecker {
	static int i = 0;
	public void main(String code,String OpenedtabTitle,TableView errorView,TabPane tab) {
		/*ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("5++");
		int message = (int) exp.getValue();
		System.out.println(message);
		*/
		
		//Lexical Analyze
		reset();
		Interpreter.Instructions = new HashMap<Integer,String>();
		Interpreter.functionsPositions = new HashMap<String,Integer>();
		Interpreter.endfunctionsPositions = new HashMap<String,Integer>();
		LexicalAnalysis lexical = new LexicalAnalysis();
		
		Global.startStatic();
		System.out.println("Scanning ...");
		lexical.check(code);
		ErrorHandler.code = code;
		Syntaxe syntaxe = new Syntaxe(OpenedtabTitle);
		//check if there is no errors then call interpreter
		if(ErrorHandler.ErrorData.size() == 0)
		{
			SelectionModel<Tab> sel = tab.getSelectionModel();
			sel.select(0);
			Interpreter program = new Interpreter();
			program.start();
		}
		else {
			SelectionModel<Tab> sel = tab.getSelectionModel();
			sel.select(1);
			errorView.setItems(ErrorHandler.ErrorData);
		}
		
		/*Global.declaredVariables.forEach((e,k)->{
			System.out.println(e+" "+k.getValue());
		});*/
		/*Global.tokens.forEach((e)->{
			System.out.println(e.getOrderNumber() +" > "+e.getLine()+" :: "+e.getId()+" line = "+ e.getLineNumber() +" --> "+Global.tokens_word.get(i));
			i++;
		});/*
		Interpreter.Instructions.forEach((e,k)->{
			System.out.println(k);
		});*/
	}
	private static void reset()
	{
		Syntaxe.inFunctionSection = false; // check if it's inside a function
		Syntaxe.inVariableSection = false;
		Syntaxe.inDbutFunction = false;
		Syntaxe.inConstantesFunction = false;
		Syntaxe.MainFunctionFounded = false;
		Syntaxe.inFunctionArg = false;
		Syntaxe.inTableauBlock = false;
		Syntaxe.retourneFounded = false;
		Syntaxe.procedure = false;
		Syntaxe.AlgorithmeCount = 0;
		Syntaxe.DeclareCount = 0;
		Syntaxe.DebutCount = 0;
		Syntaxe.index = 0;
		Syntaxe.SiBlock = new ArrayList<Integer>();
		Syntaxe.SiNonWord = new ArrayList<Integer>();
		Syntaxe.TantQueBlock = new ArrayList<Integer>();
		Syntaxe.PourBlock = new ArrayList<Integer>();
		Syntaxe.RepeterBlock = new ArrayList<Integer>();
		//
		Syntaxe.currentFunction = null;
		Syntaxe.currentFunctionName = null;
		
		Global.endedTime = System.currentTimeMillis();
	}

}
