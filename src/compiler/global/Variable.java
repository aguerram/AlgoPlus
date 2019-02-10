package compiler.global;

import compiler.lexical.Tokens;

public class Variable {
	protected int type;
	protected String value;

	public Variable(int type, String value) {
		this.type = type;
		this.value = value;
	}

	public Variable(int type) {
		this.type = type;
		if(type == Tokens.WORD)
			type = Tokens.CHAINE;
		else if(type == Tokens.CHAR)
			type = Tokens.CARACTERE;
		if (type == Tokens.CARACTERE || type == Tokens.CHAR)
			this.value = "''";
		else if (type == Tokens.CHAINE || type == Tokens.WORD)
			this.value = "\"\"";
		else if (type == Tokens.BOOLEEN)
			this.value = "false";
		else
			this.value = "0";
	}

	public String getValue() {
		return this.value;
	}

	public int getType() {
		return this.type;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	@Override
	public String toString() {
		return "Type = " + type + ", Value = " + value;
	}

}
