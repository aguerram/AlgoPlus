package compiler.syntaxe;

import compiler.Interpreter.Interpreter;
import compiler.error.ErrorHandler;
import compiler.global.Function;
import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;

public class Procedure extends Keyword {

	public Procedure(int j) {
		super(j);
		if(Syntaxe.inFunctionSection || Syntaxe.inFunctionArg)
		{
			ErrorHandler.lunch(currentToken, ErrorHandler.TokenInWrongSection);
		}
		else {
			Syntaxe.inFunctionArg = true;
			Syntaxe.inFunctionSection = true;
			Syntaxe.procedure = true;
			if (Syntaxe.MainFunctionFounded) {
				ErrorHandler.lunch(currentToken, ErrorHandler.TokenInWrongSection);
			} else {
				//Every thing is okay! then save the function's name
				String funcName = Global.tokens.get(start+1).getWord();
				Function fnc = new Function(null, 0,null);
				Syntaxe.currentFunction = fnc;
				Syntaxe.currentFunctionName = funcName;
				
				Interpreter.insideFunction = true;
				Interpreter.funcName = funcName;
				if(Global.reservedWords.get(funcName) != null)
				{
					ErrorHandler.lunch(Global.tokens.get(start+1),"'"+funcName+ ErrorHandler.IsAlreadyDeclared);
				}
				else {
					Global.declaredVariables.put(Global.getUniqueId(TokensWords.VRAIS, Syntaxe.currentFunctionName), new Variable(Tokens.BOOLEEN,"true"));
					Global.declaredVariables.put(Global.getUniqueId(TokensWords.FAUX, Syntaxe.currentFunctionName), new Variable(Tokens.BOOLEEN,"false"));
				}
			}
		}
	}

}
