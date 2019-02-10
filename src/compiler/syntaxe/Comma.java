package compiler.syntaxe;

import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;

public class Comma extends Keyword{

	public Comma(int start)
	{
		super(start);
		if(!Syntaxe.inDbutFunction && !Syntaxe.inVariableSection && !Syntaxe.inFunctionArg)
		{
			ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.TokenInWrongSection);
			Syntaxe.index = this.nextLine();
		}
		/***********
		 * in Debut section
		 * *********
		 * ********
		 * */
		if(Syntaxe.inDbutFunction)
		{
			//Before comma token
			int id = Global.tokens.get(start-1).getId();
			boolean err = false;
			if(!this.isExpresion(id) && id!= Tokens.CLOSBRACKET && id!= Tokens.INCREMENT && id!=Tokens.DECREASE)
			{
				ErrorHandler.Expected(Global.tokens.get(start-1),true,ErrorHandler.Expression);
				err = true;
			}
			
			//after
			Token tok = Global.tokens.get(start+1);
			id = tok.getId();
			if(!err && id != Tokens.OPENBRACKET && !this.isExpresion(id) && id!= Tokens.INCREMENT && id!=Tokens.DECREASE && !tok.getWord().equals("-"))
			{
				ErrorHandler.Expected(Global.tokens.get(start), false, ErrorHandler.Identifier);
			}
		}
		else if(Syntaxe.inVariableSection)
		{
			if(Syntaxe.inTableauBlock)
			{
				//Before comma token
				int id = Global.tokens.get(start-1).getId();
				boolean err = false;
				if(id!= Tokens.CLOSESQUAREBRACKET)
				{
					ErrorHandler.Expected(Global.tokens.get(start-1),true,"]");
					err = true;
				}
				
				//after
				id = Global.tokens.get(start+1).getId();
				if(!err && !this.isExpresion(id))
				{
					ErrorHandler.Expected(currentToken, false, ErrorHandler.ExpectedIdentifier);
				}
			}
			else {
				//Before comma token
				int id = Global.tokens.get(start-1).getId();
				boolean err = false;
				if(!this.isExpresion(id) /*&& id!= Tokens.CLOSBRACKET*/)
				{
					ErrorHandler.Expected(currentToken,true,ErrorHandler.Expression);
					err = true;
				}
				
				//after
				id = Global.tokens.get(start+1).getId();
				if(!err /*&& id != Tokens.OPENBRACKET*/ && !this.isExpresion(id))
				{
					ErrorHandler.Expected(currentToken, false, ErrorHandler.ExpectedIdentifier);
				}
			}
		}
		else if(Syntaxe.inFunctionArg)
		{
			
		}
		else {
			ErrorHandler.lunch(currentToken,ErrorHandler.TokenInWrongSection);
		}
		
	}
}
