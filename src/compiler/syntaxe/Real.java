package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Tokens;

public class Real extends Keyword{

	public Real(int start) {
		super(start);
		//befor tokens
		if(Syntaxe.inConstantesFunction)
		{
			try {
				int id = Global.tokens.get(start-1).getId();
				if(id != Tokens.EQULAS)
				{
					ErrorHandler.Expected(Global.tokens.get(start), true, "=");
				}
			}
			catch(Exception ex)
			{
				ErrorHandler.Expected(Global.tokens.get(start), true, "=");
			}
		}
		else {
			try {
				int id = Global.tokens.get(start-1).getId();
				if(id!= Tokens.SEMICOLON && id!=Tokens.ARITHMETIC && id!=Tokens.REFECTOR  &&
						id!=Tokens.OPENBRACKET && id!=Tokens.COMMA && !conditionSymbole(id)
						&& id != Tokens.OR && id != Tokens.AND && id != Tokens.FINSI && id != Tokens.SINON 
						&& id != Tokens.ALORS && id != Tokens.FINTANTQUE && id != Tokens.DE && id != Tokens.A && id != Tokens.PAS)
				{
					ErrorHandler.Expected(Global.tokens.get(start), true,";");
				}
			}
			catch(Exception ex)
			{
				ErrorHandler.Expected(Global.tokens.get(start), true, ";");
			}
		}
		
		//after tokens
		if(Syntaxe.inConstantesFunction)
		{
			try {
				int id = Global.tokens.get(start+1).getId();
				if(id!=Tokens.SEMICOLON)
				{
					ErrorHandler.Expected(Global.tokens.get(start), false,";");
				}
			}
			catch(Exception ex)
			{
				ErrorHandler.Expected(Global.tokens.get(start), false, ";");
			}
		}
		else {
			try {
				int id = Global.tokens.get(start+1).getId();
				if(!conditionSymbole(id) && id!=Tokens.ARITHMETIC  && id!=Tokens.CLOSBRACKET && id!=Tokens.COMMA && id!=Tokens.SEMICOLON
						&& id != Tokens.OR && id != Tokens.AND && id != Tokens.FINSI && id != Tokens.SINON 
						&& id != Tokens.ALORS && id != Tokens.FINTANTQUE && id != Tokens.A && id != Tokens.FAIRE && id != Tokens.FUNCTION && id != Tokens.PAS)
				{
					ErrorHandler.Expected(Global.tokens.get(start), false,";");
				}
			}
			catch(Exception ex)
			{
				ErrorHandler.Expected(Global.tokens.get(start), false,";");
			}
		}
	}

}
