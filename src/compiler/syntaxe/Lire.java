package compiler.syntaxe;

import java.util.ArrayList;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;
import javafx.scene.control.TextArea;

public class Lire extends Keyword{

	public Lire(int start) {
		super(start);
		//check after
		if(inDebutSection())
		{
			boolean err = false;
			Token token = Global.tokens.get(start+1);
			
			if(token.getId() != Tokens.OPENBRACKET)
			{
				ErrorHandler.Expected(Global.tokens.get(start), false, "(");
				err = true;
			}
			if(!err)
			{
				
				ArrayList<Integer> tokens = this.getFunctionArguments(start+2);
				tokens.forEach(e->{
					if(e != Tokens.ID && e != Tokens.COMMA)
					{
						ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.LireCantReadIdentifiersOnly);
					}
				});
			}
		}
	}
	
}
