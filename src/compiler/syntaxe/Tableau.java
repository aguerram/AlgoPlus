package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;

public class Tableau extends Keyword {

	public Tableau(int start) {
		super(start);
		
		if(inVariableSection())
		{
			if(!Syntaxe.inTableauBlock)
			{
				Syntaxe.inTableauBlock = true;
			}
			else {
				ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.MultipleTableauDefinition);
			}
		}
	}

}
