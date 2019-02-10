package compiler.Interpreter;

import java.net.URLConnection;

import org.springframework.http.server.reactive.HttpHeadResponseDecorator;

import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;

public class EcrireInter extends MainInter{

	public EcrireInter(int start) {
		
		super(start);
		this.addInstr("write");
		int sf = start+1;
		for(int i = start+1;i<end;i++)
		{
			
			sf = i;
			Token to = Global.tokens.get(i);
			if(to.getId() == Tokens.COMMA || to.getId() == Tokens.OPENBRACKET || to.getId() == Tokens.CLOSBRACKET)
			{
				continue;
			}
			if(to.getId() == Tokens.FUNCTION ||  to.getId() == Tokens.NUMBER || to.getId() == Tokens.INTEGER || to.getId() == Tokens.ID || Global.tokens_word.get(i).equals("-"))
			{
				String exp = getExpression(i);
				if(!exp.equals(""))
				{
					addInstr("exp "+exp);
					i = Interpreter.index;
				}
			}
			else
			{
				String word = Global.tokens_word.get(i);
				word = word.replace("\\n", "\n");
				word = word.replace("\\t", "\t");
				addInstr(word);
			}
			
		}
		addInstr("endWrite");
		Interpreter.index = sf; 
	}

}
