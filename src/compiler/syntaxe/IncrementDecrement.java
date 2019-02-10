package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Tokens;

public class IncrementDecrement extends Keyword{

	public IncrementDecrement(int start) {
		super(start);
		
		if(this.inDebutSection())
		{
			boolean idTokenBefore = false;
			boolean idTokenAfter = false;
			//before
			int idBefore = Global.tokens.get(start-1).getId();
			int idAfter = Global.tokens.get(start+1).getId();
			if(Global.tokens.get(start-1).getId() == Tokens.ID)
			{
				idTokenBefore = true;
			}
			if(Global.tokens.get(start+1).getId() == Tokens.ID)
			{
				idTokenAfter = true;
			}
			if(idTokenAfter && idTokenBefore)
			{
				ErrorHandler.Expected(Global.tokens.get(start), true, ";");
			}
			else
			{
				if(idTokenAfter)
				{
					if(idAfter != Tokens.SEMICOLON)
					{
						ErrorHandler.Expected(Global.tokens.get(start), false, ";");
					}
				}
				else if(idTokenBefore)
				{
					if(idAfter != Tokens.SEMICOLON && idAfter != Tokens.ARITHMETIC && idAfter!=Tokens.CLOSBRACKET && idAfter!=Tokens.COMMA && idAfter!=Tokens.SEMICOLON)
					{
						ErrorHandler.Expected(Global.tokens.get(start), false, ";");
					}
				}
			}
			
		}
	}
	private void Increment()
	{
		
	}
	private void Decrease()
	{
		
	}

}
