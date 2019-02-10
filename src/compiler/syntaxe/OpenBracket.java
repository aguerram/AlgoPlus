package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.Token;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;

public class OpenBracket extends Keyword{

	public OpenBracket(int start) {
		super(start);
		if(Syntaxe.inDbutFunction)
		{
			try {
				int id = Global.tokens.get(start+1).getId();
				if(!this.isExpresion(id) && !Global.tokens_word.get(start+1).equals("-") && id!=Tokens.OPENBRACKET && id != Tokens.CLOSBRACKET)
				{
					ErrorHandler.Expected(Global.tokens.get(start), false, ErrorHandler.Identifier);
				}
			}
			catch (Exception e) {
				ErrorHandler.Expected(Global.tokens.get(start), false, ";");
			}
		}
		else if(Syntaxe.inFunctionArg)
		{
			//get id before
			Token before = Global.tokens.get(start-1);
			if(before.getId() != Tokens.FUNCTION)
			{
				ErrorHandler.Expected(currentToken, true, ErrorHandler.Identifier);
			}
			//After
			Token after = Global.tokens.get(start+1);
			if(after.getId() != Tokens.ID && after.getId() != Tokens.CLOSBRACKET)
			{
				ErrorHandler.Expected(currentToken, false, ErrorHandler.Identifier);
			}
			//in this case means that the function has no arguments
			else if (after.getId() == Tokens.CLOSBRACKET)
			{
				String varname = Global.tokens.get(start-1).getWord();
				if(varname.matches(Global.IdRegExp))
				{
					String unique = Global.getUniqueId(varname, Syntaxe.currentFunctionName);
					if(Global.declaredVariables.get(unique) != null)
					{
						ErrorHandler.lunch(Global.tokens.get(start-2),"'"+varname+ ErrorHandler.IsAlreadyDeclared);
					}
					else {
						Global.declaredVariables.put(unique,
								new Variable(currentToken.getId()));
						Global.reservedWords.put(unique, currentToken.getId());
					}
				}
			}
		}
		else {
			ErrorHandler.lunch(currentToken,ErrorHandler.TokenInWrongSection);
		}
		
	}
	//Add the arguement to variables list
	//N.B : This method exist also in TypeKeyWord
	private void addDeclaration()
	{
		//Variables declaration in arguments
		String varname = Global.tokens.get(start-2).getWord();
		if(varname.matches(Global.IdRegExp))
		{
			String unique = Global.getUniqueId(varname, Syntaxe.currentFunctionName);
			if(Global.declaredVariables.get(unique) != null)
			{
				ErrorHandler.lunch(Global.tokens.get(start-2),"'"+varname+ ErrorHandler.IsAlreadyDeclared);
			}
			else {
				Global.declaredVariables.put(unique,
						new Variable(currentToken.getId()));
				Global.reservedWords.put(unique, currentToken.getId());
			}
		}
	}
}
