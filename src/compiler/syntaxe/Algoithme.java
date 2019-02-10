package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;

public class Algoithme extends Keyword {

	public Algoithme(int start,String OpenedtabTitle) {
		super(start);
		Syntaxe.DeclareCount = 0;
		Syntaxe.DebutCount = 0;
		
		Syntaxe.AlgorithmeCount ++;
		Syntaxe.inFunctionSection = true;
		Syntaxe.MainFunctionFounded = true;
		if(Syntaxe.AlgorithmeCount > 1)
		{
			ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.MultipleAlgorithmeDefinition);
		}
		Token to = Global.tokens.get(start+1);
		if(to.getId() != Tokens.ID)
		{
			ErrorHandler.Expected(Global.tokens.get(start), false, ErrorHandler.Title);
		}
		//Title founded
		else {
			//Remove ' *' from title if it's exist
			if(OpenedtabTitle.endsWith(" *"))
			{
				OpenedtabTitle = OpenedtabTitle.substring(0,OpenedtabTitle.length()-" *".length());
			}
			if(!OpenedtabTitle.equals(Global.tokens_word.get(start+1)))
			{
				ErrorHandler.lunch(Global.tokens.get(start+1), ErrorHandler.TheNameOfTheProgramMustBeAsFile);
			}
		}
	}

}
