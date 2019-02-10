package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Tokens;

public class TowPoints extends Keyword{

	public TowPoints(int start) {
		super(start);
		if(Syntaxe.inVariableSection)
		{
			if(Syntaxe.inTableauBlock)
			{
				try {
					if(!this.isType(Global.tokens.get(start+1).getId()))
					{
						ErrorHandler.Expected(Global.tokens.get(start),false,ErrorHandler.Type);
					}
					if(Global.tokens.get(start-1).getId() != Tokens.CLOSESQUAREBRACKET)
					{
						ErrorHandler.Expected(Global.tokens.get(start),true,"]");
					}
				}
				catch(Exception ex) {}
			}
			else {
				try {
					if(!this.isType(Global.tokens.get(start+1).getId()))
					{
						ErrorHandler.Expected(Global.tokens.get(start),false,ErrorHandler.Type);
					}
					if(Global.tokens.get(start-1).getId() != Tokens.ID)
					{
						ErrorHandler.Expected(Global.tokens.get(start),true,ErrorHandler.Identifier);
					}
				}
				catch(Exception ex) {}
			}
		}
		else if(Syntaxe.inFunctionArg)
		{
			int id = Global.tokens.get(start+1).getId();
			if(id != Tokens.ENTIER && id != Tokens.REEL && id != Tokens.CARACTERE && id != Tokens.BOOLEEN && id != Tokens.CHAINE)
			{
				ErrorHandler.Expected(currentToken, false, ErrorHandler.Type);
			}
		}
	}
	
}
