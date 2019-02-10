package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;

public class Variables extends Keyword{

	public Variables(int start) {
		super(start);
		this.inFunctionSection();
		if(!Syntaxe.inVariableSection)
		{
			if(!Syntaxe.inDbutFunction)
			{
				Syntaxe.inVariableSection = true;
				Syntaxe.inConstantesFunction = false;
			}
			else {
				ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.MustBeBeforeDebutTokent);
				Syntaxe.index = this.nextLine();
			}
		}
		else {
			ErrorHandler.lunch(Global.tokens.get(start), ErrorHandler.MultipleDecalreDefinition);
		}
		
	}
	
}
