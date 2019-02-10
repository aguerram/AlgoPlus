package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Tokens;

public class CloseSquareBracket extends Keyword {

	public CloseSquareBracket(int start) {
		super(start);
		if(Syntaxe.inTableauBlock)
		{
			int id = Global.tokens.get(start-1).getId();
			//Before
			// ID
			if(id != Tokens.ID && id != Tokens.INTEGER)
			{
				ErrorHandler.Expected(currentToken, true, ErrorHandler.ArraySize);
			}
			
			//After
			// ID
			id = Global.tokens.get(start+1).getId();
			if(id != Tokens.OPENSQUAREBRACKET && id != Tokens.TOWPOINTS && id != Tokens.COMMA)
			{
				ErrorHandler.Expected(currentToken, false, ":");
			}
		}
		
	}

} 
