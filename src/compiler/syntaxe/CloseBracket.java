package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.Token;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;

public class CloseBracket extends Keyword{
	public CloseBracket(int start) {
		super(start);
		if(Syntaxe.inDbutFunction)
		{
			try {
				int id = Global.tokens.get(start+1).getId();
				if(id != Tokens.SEMICOLON && id != Tokens.ARITHMETIC && id != Tokens.COMMA && id != Tokens.CLOSBRACKET && id != Tokens.ALORS
						&& id != Tokens.OR && id != Tokens.AND && id != Tokens.FAIRE && id != Tokens.JUSQUA)
				{
					ErrorHandler.Expected(currentToken, false, ";");
				}
				else {
					if( id == Tokens.SEMICOLON && !Syntaxe.MainFunctionFounded)
					{
						//FIXME I commented this line, I don't know what it does
						/*Syntaxe.currentFunction = null;
						Syntaxe.currentFunctionName = null;*/
					}
				}
			}
			catch (Exception e) {
				ErrorHandler.Expected(currentToken, false, ";");
			}
		}
		else if(Syntaxe.inFunctionArg)
		{
			//Fonction 
			if(!Syntaxe.procedure)
			{
				if(Global.tokens.get(start+1).getId() != Tokens.TOWPOINTS)
				{
					ErrorHandler.Expected(currentToken, false, ErrorHandler.Type);
				}
			}
			//Procedure déclaration
			else {
				Token tok = Global.tokens.get(start+1);
				System.out.println(tok);
				if(tok.getId() != Tokens.CONSTANTES && tok.getId() != Tokens.DEBUT && tok.getId() != Tokens.VARIABLES)
				{
					ErrorHandler.lunch(tok, ErrorHandler.UnExpectedToken+" '"+tok.getWord()+"'");
				}
				else {
					Syntaxe.currentFunction.setReturnType(-1);
					//Save this function in global array of functions
					Global.declaredFunctions.put(Syntaxe.currentFunctionName, Syntaxe.currentFunction);	
					Global.reservedWords.put(Syntaxe.currentFunctionName, Tokens.FUNCTION);
					addDeclaration();
				}
			}
		}
		else {
			ErrorHandler.lunch(currentToken,ErrorHandler.TokenInWrongSection);
		}
	}
	private void addDeclaration()
	{
		//Variables declaration in arguments
		String varname = Global.tokens.get(start-2).getWord();
		if(varname.matches(Global.IdRegExp))
		{
			String unique = Global.getUniqueId(varname, Syntaxe.currentFunctionName);
			if(Global.declaredVariables.get(unique) != null)
			{
				//This causes problems in Procedure
				//ErrorHandler.lunch(Global.tokens.get(start-2),"'"+varname+ ErrorHandler.IsAlreadyDeclared);
			}
			else {
				Global.declaredVariables.put(unique,
						new Variable(currentToken.getId()));
				Global.reservedWords.put(unique, currentToken.getId());
			}
		}
	}

}
