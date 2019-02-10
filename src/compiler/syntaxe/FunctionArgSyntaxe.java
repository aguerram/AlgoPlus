package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Tokens;

public class FunctionArgSyntaxe extends Keyword{
	public FunctionArgSyntaxe(int start,int argsLength)
	{
		super(start);
		if(end > 0)
		{
			int id = Global.tokens.get(start).getId();
			if(id != Tokens.OPENBRACKET)
			{
				//ErrorHandler.lunch(Global.tokens.get(start), "Expected ( ");
				ErrorHandler.Expected(Global.tokens.get(start-1),false,"(");
			}
			if(Global.tokens.get(end-1).getId() != Tokens.CLOSBRACKET)
			{
				ErrorHandler.Expected(Global.tokens.get(end-1), false, ")");
				
			}
			if(Global.tokens.get(end).getId() != Tokens.SEMICOLON)
			{
				ErrorHandler.Expected(Global.tokens.get(end-1), false, ";");
			}
		}
		else {
			ErrorHandler.Expected(Global.tokens.get(Global.tokens.size()-1), false, ";");
		}
	}

	private int getFunctionEnd()
	{
		int openB = 0;
		boolean foundB = false;
		int i;
		int pos = 0;
		for(i = this.start;i<Global.tokens.size();i++)
		{
			int id = Global.tokens.get(i).getId();
			if(id == Tokens.OPENBRACKET)
			{
				openB++;
				
				foundB = true;
			}
			if(id == Tokens.CLOSBRACKET)
			{
				openB--;
				pos = i;
			}
		}
		
		if(openB == 0 && foundB)
			return ++pos;
		return this.end;
		
	}
}
