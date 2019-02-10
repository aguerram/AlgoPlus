package compiler.Interpreter;

import compiler.global.Global;
import compiler.lexical.Tokens;

public class IdVariables extends MainInter {

	public IdVariables(int start) {
		super(start);
		String name = Global.tokens_word.get(start);
		String type = "String";
		switch(this.getType())
		{
			case Tokens.ENTIER:type = "int";break;
			case Tokens.REEL:type = "float";break;
			case Tokens.CARACTERE:type = "char";break;
			case Tokens.BOOLEEN:type = "boolean";break;
		}
		this.addInstr("let "+name+" "+type);
	}
	
}
