package compiler.syntaxe;

import compiler.Interpreter.MainInter;
import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;

public class TantQue extends MainInter {

	public TantQue(int j) {
		super(j);
		if(this.inDebutSection())
		{
			//Expected condition syntaxe x<10 valide x<10+6 not valide 
			if(Global.tokens.get(start+1).getId() == Tokens.OPENBRACKET)
			{
				int idLastOpenB = getFunctionEnd();
				try {
					String exp = new String(this.Expression(this.getExpression(start+1)));
					if(!exp.contains("false") && !exp.contains("true") && !exp.contains(">") && !exp.contains("<") && !exp.contains("=")
							 && !exp.contains("||")  && !exp.contains("&&"))
					{
						ErrorHandler.lunch(currentToken, ErrorHandler.ConditionSyntaxeNotValide);
					}
					else {
						if(Global.tokens.get(idLastOpenB).getId() != Tokens.FAIRE)
						{
							ErrorHandler.Expected(Global.tokens.get(idLastOpenB-1), false, TokensWords.FAIRE);
						}
					}
				}
				catch(Exception ex)
				{
					ErrorHandler.Expected(currentToken, false, ErrorHandler.Expression);
				}
			}
			else {
				ErrorHandler.Expected(currentToken, false, "(");
			}
			
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
			else if(id == Tokens.CLOSBRACKET)
			{
				openB--;
				pos = i;
			}
			if(openB == 0 && foundB)
				return ++pos;
		}
		return this.end;
	}
}
