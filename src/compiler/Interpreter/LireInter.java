package compiler.Interpreter;

import org.controlsfx.dialog.ExceptionDialog;

import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;
import javafx.scene.control.TextArea;

public class LireInter extends MainInter{

	public LireInter(int start) {
		super(start);
		try {
			
			int openB = 0;
			int sf = start;
			for(int i=start+2;i<Global.tokens_word.size();i++)
			{
				sf = i;
				Token token = Global.tokens.get(i);
				int id = token.getId();
				
				if(id == Tokens.OPENBRACKET)
				{
					openB++;
					continue;
				}
				else if(id == Tokens.COMMA)
				{
					continue;
				}
				else if(id == Tokens.CLOSBRACKET && openB == 0)
				{
					break;
				}
				else if(id == Tokens.CLOSBRACKET)
				{
					openB--;
					continue;
				}
				else if(id == Tokens.ID)
				{
					String varname = Global.tokens_word.get(i);
					if(Interpreter.insideFunction)
					{
						varname = Global.getUniqueId(varname, Interpreter.funcName);
					}
					int type = Global.declaredVariables.get(varname).getType();
					addInstr("read");
					addInstr(varname+" "+type);
					addInstr("endRead");
				}
				
			}
			
			
		}
		catch(Exception e)
		{
			//new ExceptionDialog(e).show();
		}
	}

}
