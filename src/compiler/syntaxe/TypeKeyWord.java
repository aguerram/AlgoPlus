package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.Token;
import compiler.lexical.Tokens;

public class TypeKeyWord extends Keyword{

	public TypeKeyWord(int start) {
		super(start);
		//In variables section
		if(Syntaxe.inVariableSection)
		{
			try {
				if(Global.tokens.get(start-1).getId() != Tokens.TOWPOINTS)
				{
					ErrorHandler.Expected(Global.tokens.get(start),true,":");
				}
				if(Global.tokens.get(start+1).getId() != Tokens.SEMICOLON)
				{
					ErrorHandler.Expected(currentToken,false,";");
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		else if(Syntaxe.inFunctionArg)
		{
			//get id before
			Token before = Global.tokens.get(start-1);
			if(before.getId() != Tokens.TOWPOINTS)
			{
				ErrorHandler.Expected(currentToken, true, ":");
			}
			//After
			Token after = Global.tokens.get(start+1);
			if(after.getId() != Tokens.COMMA && after.getId() != Tokens.CLOSBRACKET && after.getId() != Tokens.DEBUT && after.getId() != Tokens.VARIABLES 
					&& (after.getId() != Tokens.CONSTANTES && !Syntaxe.procedure))
			{
				ErrorHandler.Expected(currentToken, false, ")");
			}
			int id = after.getId();
			if(id == Tokens.DEBUT || id == Tokens.VARIABLES || id == Tokens.CONSTANTES)
			{
				Syntaxe.currentFunction.setReturnType(currentToken.getId());
				//Save this function in global array of functions
				Global.declaredFunctions.put(Syntaxe.currentFunctionName, Syntaxe.currentFunction);	
				Global.reservedWords.put(Syntaxe.currentFunctionName, Tokens.FUNCTION);
			}
			else if(id == Tokens.CLOSBRACKET)
			{
				//Procedure save
				addDeclaration();
				
			}
			else {
				addDeclaration();
			}
		}
		else {
			ErrorHandler.lunch(currentToken, ErrorHandler.TokenInWrongSection);
		}
	}
	//Add the arguement to variables list
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
