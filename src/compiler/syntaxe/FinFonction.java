package compiler.syntaxe;

import compiler.Interpreter.Interpreter;
import compiler.error.ErrorHandler;
import compiler.lexical.TokensWords;

public class FinFonction extends Keyword {

	public FinFonction(int j) {
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
			
			if(!Syntaxe.retourneFounded)
			{
				ErrorHandler.Expected(currentToken, true, TokensWords.RETOURNE);
			}
			if(Syntaxe.procedure)
			{
				ErrorHandler.lunch(currentToken, ErrorHandler.UnExpectedToken+" '"+TokensWords.FINFONCTION+"'");
			}
			Syntaxe.procedure = false;
			Syntaxe.retourneFounded = false;
		}
		else {
			ErrorHandler.lunch(currentToken,ErrorHandler.TokenInWrongSection);
		}
	}

}
