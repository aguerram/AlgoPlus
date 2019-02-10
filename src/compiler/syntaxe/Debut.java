package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;

public class Debut extends Keyword {

	public Debut(int start) {
		super(start);
		this.inFunctionSection();
		Syntaxe.inDbutFunction = true;
		Syntaxe.inVariableSection = false;
		Syntaxe.inConstantesFunction = false;
		Syntaxe.inTableauBlock = false;
		Syntaxe.DebutCount++;
		if(Syntaxe.DebutCount > 1)
		{
			ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.MultipleDecalreDefinition);
		}
	}

}
