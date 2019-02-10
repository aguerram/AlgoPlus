package compiler.syntaxe;

import compiler.Interpreter.MainInter;
import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;

public class Function extends MainInter{

	public Function(int start) {
		super(start);
		if(Syntaxe.inDbutFunction)
		{
			String funName = Global.tokens_word.get(start);
			if(Global.declaredFunctions.get(funName) == null)
			{
				
				ErrorHandler.lunch(Global.tokens.get(start), ErrorHandler.FuncNotDeclared+"'"+funName+"'");
			}
			else {
				valideFunctionArguments(start);
			}
		}
		else if(Syntaxe.inFunctionArg)
		{
			//get id before
			Token before = Global.tokens.get(start-1);
			if(before.getId() != Tokens.FONCTION && before.getId() != Tokens.PROCEDURE)
			{
				ErrorHandler.Expected(currentToken, true, TokensWords.FONCTION);
			}
			//After
			Token after = Global.tokens.get(start+1);
			if(after.getId() != Tokens.OPENBRACKET)
			{
				ErrorHandler.Expected(currentToken, false, "(");
			}
		}
		else {
			ErrorHandler.lunch(currentToken,ErrorHandler.TokenInWrongSection);
		}
	}

}
