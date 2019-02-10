package compiler.syntaxe;

import compiler.Interpreter.MainInter;
import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;

public class Retourne extends Keyword {

	public Retourne(int j) {
		super(j);
		if(!Syntaxe.inDbutFunction && !Syntaxe.MainFunctionFounded)
		{
			ErrorHandler.lunch(currentToken, ErrorHandler.TokenInWrongSection);
		}
		else {
			Syntaxe.retourneFounded = true;
		}
		new FunctionArgSyntaxe(start+1, 1);
		valideArgumentsWithFunction(start+1);
	}
	/**
	 * Method to check if variables types if valid compare to a function's return type
	 * */
	private boolean valideArgumentsWithFunction(int s)
	{
		int type;
		try {
			 type = Global.declaredFunctions.get(Syntaxe.currentFunctionName.trim()).getReturnType();
		}
		catch(Exception ex) {return false;}
		
		type = getTypeEquivalent(type);
		int equiv=0;
		for(int i =s;i<this.end;i++)
		{
			Token tok = Global.tokens.get(i);
			if(tok.getId() == Tokens.OPENBRACKET || tok.getId() == Tokens.CLOSBRACKET ||
					tok.getId() == Tokens.ARITHMETIC)
				continue;
			equiv = getTypeEquivalent(tok.getId());
			if(tok.getId() == Tokens.ID)
			{
				String varname = tok.getWord();
				if(Syntaxe.currentFunctionName != null)
				{
				 varname = Global.getUniqueId(varname, Syntaxe.currentFunctionName);
				}
				equiv = getTypeEquivalent(
							Global.declaredVariables.get(varname).getType()
						);
						
			}
			if(type != equiv)
			{
				ErrorHandler.lunch(tok, ErrorHandler.ReturnTypeMustBeLikeFunctionsReturnType);
			}
		}
		return true;
	}
}
