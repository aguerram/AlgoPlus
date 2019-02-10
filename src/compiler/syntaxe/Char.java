package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;

public class Char extends Keyword{

	public Char(int start) {
		super(start);
		this.inDebutSection();
		String word = Global.tokens_word.get(start);
		if(word.length() > 3 && (!word.equals("'\\n'") && !word.equals("'\\t'")))
		{
			ErrorHandler.lunch(Global.tokens.get(start), ErrorHandler.CharExpectedBuWordFounded);
		}
	}
	
}
