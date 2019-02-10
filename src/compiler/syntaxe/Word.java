package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Tokens;

public class Word extends Keyword{

	public Word(int start) {
		super(start);
		//after
		if(Syntaxe.inDbutFunction)
		{
			int id = Global.tokens.get(start+1).getId();
			if(id != Tokens.WORD && !Global.tokens_word.get(start+1).equals("+") && id!=Tokens.CLOSBRACKET && id!=Tokens.COMMA && id!=Tokens.SEMICOLON
					&& !this.conditionSymbole(id) && id != Tokens.AND && id != Tokens.OR && id != Tokens.FUNCTION )
			{
				ErrorHandler.Expected(Global.tokens.get(start), false, ";");
			}
			
			//before
			id = Global.tokens.get(start-1).getId();
			if(id != Tokens.WORD && !Global.tokens_word.get(start-1).equals("+") && id!=Tokens.REFECTOR  && id!=Tokens.OPENBRACKET && id!=Tokens.COMMA 
					&& !this.conditionSymbole(id) && id != Tokens.OR && id != Tokens.AND && id != Tokens.FINSI && id != Tokens.SINON 
					&& id != Tokens.ALORS && id != Tokens.FINTANTQUE )
			{
				ErrorHandler.Expected(Global.tokens.get(start), true, ";");
			}
		}
		else if(Syntaxe.inConstantesFunction)
		{
			int id = Global.tokens.get(start+1).getId();
			if(id != Tokens.SEMICOLON)
			{
				ErrorHandler.Expected(Global.tokens.get(start), false, ";");
			}
			
			//before
			id = Global.tokens.get(start-1).getId();
			if(id != Tokens.EQULAS)
			{
				ErrorHandler.Expected(Global.tokens.get(start), true, "=");
			}
		}
		else {
			ErrorHandler.lunch(Global.tokens.get(start), ErrorHandler.TokenInWrongSection);
		}
	}

}
