package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.Tokens;

public class OpenSquareBracket extends Keyword {

	public OpenSquareBracket(int start) {
		super(start);
		if(Syntaxe.inTableauBlock)
		{
			int id = Global.tokens.get(start-1).getId();
			//Before
			// ID or [
			if(id != Tokens.ID && id != Tokens.CLOSESQUAREBRACKET)
			{
				ErrorHandler.Expected(currentToken, true, ErrorHandler.Identifier);
			}
			
			//After
			id = Global.tokens.get(start+1).getId();
			if(id != Tokens.INTEGER && id != Tokens.ID)
			{
				ErrorHandler.Expected(currentToken, false, ErrorHandler.ArraySize);
			}
			else {
				if(id == Tokens.ID)
				{
					String name = Global.tokens_word.get(start+1);
					try {
						Variable var = Global.declaredConstantes.get(name);
						if(var != null)
						{
							if(var.getType() != Tokens.ENTIER)
							{
								ErrorHandler.Expected(currentToken, false, ErrorHandler.TableSizeMustBeInteger);
							}
						}
						else {
							ErrorHandler.Expected(currentToken, false, ErrorHandler.ArraySize);
						}
					}
					catch(Exception ex)
					{
						
					}
				}
			}
		}
	}

}
