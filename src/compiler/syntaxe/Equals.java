package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Tokens;

public class Equals extends Keyword {

	public Equals(int start) {
		super(start);
		if(this.inFunctionSection())
		{
			if(Syntaxe.inConstantesFunction)
			{
				int id = Global.tokens.get(start+1).getId();
				if(id != Tokens.WORD && id!=Tokens.CHAR && id != Tokens.NUMBER && id != Tokens.INTEGER)
				{
					ErrorHandler.Expected(Global.tokens.get(start), false, ErrorHandler.Identifier);
				}
			}
			else {
				ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.TokenInWrongSection);
			}
		}
	}

}
