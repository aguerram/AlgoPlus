package compiler.syntaxe;

import compiler.Interpreter.Interpreter;
import compiler.error.ErrorHandler;
import compiler.lexical.TokensWords;

public class FinProcedure extends Keyword {

	public FinProcedure(int j) {
		super(j);
		if(Syntaxe.inDbutFunction && !Syntaxe.MainFunctionFounded)
		{
			Syntaxe.inFunctionSection = false; 
			Syntaxe.inVariableSection = false;
			Syntaxe.inDbutFunction = false;
			Syntaxe.inConstantesFunction = false;
			Syntaxe.inTableauBlock = false;
			Syntaxe.inFunctionArg = false;
			
			Syntaxe.DebutCount = 0;
			Syntaxe.DeclareCount = 0;
			Syntaxe.currentFunction = null;
			Syntaxe.currentFunctionName = null;
			Interpreter.insideFunction = false;
			Interpreter.funcName = null;
			
			if(Syntaxe.retourneFounded)
			{
				ErrorHandler.lunch(currentToken,ErrorHandler.UnExpectedToken+" '"+ TokensWords.RETOURNE+"'");
				Syntaxe.retourneFounded = false;
			}
			if(!Syntaxe.procedure)
			{
				ErrorHandler.lunch(currentToken, ErrorHandler.UnExpectedToken+" '"+TokensWords.FINPROCEDURE+"'");
			}
			Syntaxe.procedure = false;
			Syntaxe.retourneFounded = false;
		}
		else {
			ErrorHandler.lunch(currentToken,ErrorHandler.TokenInWrongSection);
		}
	}

}
