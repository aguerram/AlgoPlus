package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Tokens;

public class Constantes extends Keyword {

	public Constantes(int start) {
		super(start);
		Syntaxe.inTableauBlock = false;
		this.inFunctionSection();
		if(!Syntaxe.inConstantesFunction)
		{
			if(Syntaxe.inVariableSection)
			{
				ErrorHandler.lunch(Global.tokens.get(start), ErrorHandler.ConstatntesMustBeBeforVariableToken);
			}
			else {
				Syntaxe.inConstantesFunction = true;
				if(Global.tokens.get(start+1).getId() != Tokens.ID)
				{
					ErrorHandler.Expected(Global.tokens.get(start+1), false,ErrorHandler.Identifier);
				}
			}
		}
		else {
			ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.MultiConstatesDefinition);
		}
		
		
	}

}
