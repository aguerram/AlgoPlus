package compiler.lexical;

import compiler.lexical.Tokens;
import compiler.global.Global;

public class LexicalAnalysis {
	private String code;
	private int colon = 0;
	private int colon_back = 0;
	private int line = 0;
	private int line_back = 0;
	private int position = 0;
	private boolean inDebutSection = false;

	public LexicalAnalysis() {
		//DO Nothing
	}

	public boolean check(String code) {
		inDebutSection = false;
		this.code = code;
		try {
			while (true) {
				String str = this.next();
				if (str == "")
					break;
				int type = checkType(str);
				if (type == Tokens.NULL || type == Tokens.SPACE) {
					this.colon = this.colon_back;
					this.line = this.line_back;
					continue;
				}
				
				if (type == Tokens.DEBUT)
					inDebutSection = true;
				else if (type == Tokens.FIN || type == Tokens.FINFONCTION || type == Tokens.FINPROCEDURE)
					inDebutSection = false;
				
				else if (type == Tokens.OPENBRACKET) {
					try {
						int lastid = Global.tokens.size() - 1;
						if (Global.tokens.get(lastid).getId() == Tokens.ID) {
							Token t = Global.tokens.get(lastid);
							Global.tokens.remove(lastid);
							t.setId(Tokens.FUNCTION);
							Global.tokens.add(t);
						}
					} catch (Exception ex) {

					}
				}
				
				if (str.equals(TokensWords.OU))
					str = " || ";
				else if (str.equals(TokensWords.ET))
					str = " && ";
				else if (str.equals(TokensWords.NOTEQUAL))
					str = " != ";
				else if(str.equals("="))
					str = " == ";
				else if(str.equals("-") && Global.tokens.get(Global.tokens.size()-1).getId() == Tokens.LESSTEHN)
				{
					Global.tokens.remove(Global.tokens.size()-1);
					Global.tokens_word.remove(Global.tokens_word.size()-1);
					type = Tokens.REFECTOR;
					str = "<-";
				}
				Global.tokens.add(new Token(type, this.line + " : " + this.colon,str));
				Global.tokens_word.add(str);
				this.colon = this.colon_back;
				this.line = this.line_back;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Global.tokens.add(new Token(Tokens.NULL, this.line + " : " + this.colon + 1,""));
		Global.tokens_word.add("");
		return false;
	}

	public int checkType(String str) {

		// Types keywords
		if(str.length()<1)
			return Tokens.NULL;
		switch(str.toUpperCase().charAt(0))
		{
		case 'A':
			if (str.equals(TokensWords.ALORS)) {
				return Tokens.ALORS;
			}
			if(str.equals(TokensWords.A))
			{
				return Tokens.A;
			}
			if(str.equals(TokensWords.ALLANT))
			{
				return Tokens.ALLANT;
			}
			if (str.equals(TokensWords.ALGORITHME)) {
				return Tokens.ALGORITHME;
			}
			break;
		case 'B':
			if (str.equals(TokensWords.BOOLEEN)) {
				return Tokens.BOOLEEN;
			}
			break;
		case 'C':
			if (str.equals(TokensWords.CONSTANTES)) {
				return Tokens.CONSTANTES;
			}
			if (str.equals(TokensWords.CHAINE)) {
				return Tokens.CHAINE;
			}
			if (str.equals(TokensWords.CARACTERE)) {
				return Tokens.CARACTERE;
			}
			break;
		case 'D':
			if (str.equals(TokensWords.DEBUT)) {
				return Tokens.DEBUT;
			}
			if(str.equals(TokensWords.DE))
			{
				return Tokens.DE;
			}
			
			break;
		case 'E':
			if (str.equals(TokensWords.ENTIER)) {
				return Tokens.ENTIER;
			}
			if (str.equals(TokensWords.ECRIRE)) {
				return Tokens.ECRIRE;
			}
			if (str.equals(TokensWords.ENTIER)) {
				return Tokens.ENTIER;
			}
			break;
		case 'F':
			if (str.equals(TokensWords.FIN)) {
				return Tokens.FIN;
			}
			if (str.equals(TokensWords.FINSI)) {
				return Tokens.FINSI;
			}
			if(str.equals(TokensWords.FAIRE))
			{
				return Tokens.FAIRE;
			}
			if(str.equals(TokensWords.FINTANTQUE))
			{
				return Tokens.FINTANTQUE;
			}
			if(str.equals(TokensWords.FINPOUR))
			{
				return Tokens.FINPOUR;
			}
			if(str.equals(TokensWords.FONCTION))
			{
				return Tokens.FONCTION;
			}
			if(str.equals(TokensWords.FINFONCTION))
			{
				return Tokens.FINFONCTION;
			}
			if(str.equals(TokensWords.FINPROCEDURE))
			{
				return Tokens.FINPROCEDURE;
			}
			break;
		case 'G':break;
		case 'H':break;
		case 'I':break;
		case 'J':
			if (str.equals(TokensWords.JUSQUA)) {
				return Tokens.JUSQUA;
			}
			break;
		case 'K':break;
		case 'L':
			if(str.equals(TokensWords.LIRE))
			{
				return Tokens.LIRE;
			}
			break;
		case 'M':break;
		case 'N':break;
		case 'O':break;
		case 'P':
			if(str.equals(TokensWords.POUR))
			{
				return Tokens.POUR;
			}
			if(str.equals(TokensWords.PAS))
			{
				return Tokens.PAS;
			}
			if(str.equals(TokensWords.PROCEDURE))
			{
				return Tokens.PROCEDURE;
			}
			break;
		case 'Q':break;
		case 'R':
			if (str.equals(TokensWords.REPETER)) {
				return Tokens.REPETER;
			}
			if (str.equals(TokensWords.REEL)) {
				return Tokens.REEL;
			}
			if (str.equals(TokensWords.RETOURNE)) {
				return Tokens.RETOURNE;
			}
			break;
		case 'S':
			if (str.equals(TokensWords.SI)) {
				return Tokens.SI;
			}
			
			if (str.equals(TokensWords.SINON)) {
				return Tokens.SINON;
			}
			break;
		case 'T':
			if (str.equals(TokensWords.TABLEAU)) {
				return Tokens.TABLEAU;
			}
			if(str.equals(TokensWords.TANTQUE))
			{
				return Tokens.TANTQUE;
			}
			break;
		case 'U':break;
		case 'V':
			if (str.equals(TokensWords.VARIABLES)) {
				return Tokens.VARIABLES;
			}
			break;
		case 'W':break;
		case 'X':break;
		case 'Y':break;
		case 'Z':break;
		}
		
		//Default
		
		if (str.equals(TokensWords.ET)) {
			return Tokens.AND;
		}
		if (str.equals(TokensWords.OU)) {
			return Tokens.OR;
		}
		if (str.equals(TokensWords.XOR)) {
			return Tokens.XOR;
		}
		if (str.equals(",")) {
			return Tokens.COMMA;
		}

		if (str.equals("(")) {
			return Tokens.OPENBRACKET;
		}
		if (str.equals(")")) {
			return Tokens.CLOSBRACKET;
		}
		if (str.equals("[")) {
			return Tokens.OPENSQUAREBRACKET;
		}
		if (str.equals("]")) {
			return Tokens.CLOSESQUAREBRACKET;
		}
		if (str.equals(";")) {
			return Tokens.SEMICOLON;
		}
		if (str.equals(":")) {
			return Tokens.TOWPOINTS;
		}
		if (str.equals("\n")) {
			return Tokens.SEMICOLON;
		}
		if (str.equals("")) {
			return Tokens.NULL;
		}
		if (str.equals(" ")) {
			return Tokens.SPACE;
		}
		if (str.equals("<-")) {
			return Tokens.REFECTOR;
		}
		/*if (str.equals("++")) {
			return Tokens.INCREMENT;
		}
		if (str.equals("--")) {
			return Tokens.DECREASE;
		}*/
		if (str.equals("<")) {
			return Tokens.LESSTEHN;
		}
		if (str.equals(">")) {
			return Tokens.MORETHEN;
		}
		if (str.equals("=")) {
			return Tokens.EQULAS;
		}
		if (str.equals("<=")) {
			return Tokens.LESSTEHNOREQUALS;
		}
		if (str.equals(">=")) {
			return Tokens.MORETHENOREQUALS;
		}
		if (str.equals(TokensWords.NOTEQUAL)) {
			return Tokens.NOTEQULAS;
		}
		
		if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("%")
				|| str.equals("Mod") || str.equals("^")) {
			return Tokens.ARITHMETIC;
		}
		if (str.matches("^[a-zA-Z_][a-zA-Z0-9_]*[\\[\\d\\w*\\]]*$")) {
			return Tokens.ID;
		}
		if (str.matches("^\".*\"$")) {
			return Tokens.WORD;
		}
		if (str.matches("^\'.*\'$")) {
			return Tokens.CHAR;
		}
		if (str.matches("(?s)^\'.*[^\']$")) {
			return Tokens.ExpectedQuote;
		}
		if (str.matches("^[0-9]+$")) {
			return Tokens.INTEGER;
		}
		if (str.matches("^[0-9]+\\.[0-9]+$")) {
			return Tokens.NUMBER;
		}
		if (str.matches("^(\\(|\\)|,)$")) {
			return 0;
		}
		return -1;

	}

	private String next() {
		boolean openQ = false;
		boolean openDQ = false;
		boolean singleLineComment = false;
		this.line_back = this.line;
		this.colon_back = this.colon;
		StringBuffer sb = new StringBuffer();
		if (this.position == code.length())
			return "";
		singleLineComment = false;
		for (int i = this.position; i < this.code.length(); i++) {
			char c = code.charAt(i);
			char after = ' ';
			try {
				after = code.charAt(i + 1);
			} catch (Exception e) {
			}

			this.colon_back++;
			this.position++;
			// Check single line comment
			if ((c == '/' && after == '/') && (!openDQ && !openDQ)) {
				singleLineComment = true;
				continue;
			}
			if (c == '\n' && singleLineComment) {
				singleLineComment = false;
			}
			if (singleLineComment)
				continue;
			// End check
			if (c == '\'' && !openDQ)
				openQ = !openQ;
			else if (c == '"' && !openQ)
				openDQ = !openDQ;
			else if (c == '\n' && (!openDQ || !openQ)) {
				this.line_back++;
				this.colon_back = 0;
				String ss = new String(sb);
				// this.colon+=ss.length();
				return ss.trim();
			}
			if (openQ || openDQ) {
				sb.append(c);
				continue;
			}
			if (c == '\t')
				this.colon_back += 4;
			if (c != '\n' && c != '\t')
				sb.append(c);
			if (Global.inArray(c, Global.separators)) {
				if (inDebutSection && (c == '[' || c == ']'))
					continue;
				boolean test = false;
				try {
					test = Global.inArray(sb.charAt(sb.length() - 1), Global.separators);
				} catch (Exception ex) {

				}
				if (test && sb.length() > 1) {
					if (c != '"' && c != '\'') {
						sb.delete(sb.length() - 1, sb.length());
						this.position--;
					}
					try {
						if (c == '<') {
							if ((this.position != this.code.length()) && code.charAt(this.position + 1) == '-') {
								String str = new String(sb);
								Global.tokens.add(new Token(checkType(str), this.line + " : " + this.colon,"<-"));
								Global.tokens_word.add(str);
								// Global.tokens.remove(Global.tokens.size()-1);
								this.position += 2;
								// this.colon+=2;
								return new String("<-").trim();
							}
							
						} else if (c == '+') {
							if ((this.position != this.code.length()) && code.charAt(this.position + 1) == '+') {
								String str = new String(sb);
								Global.tokens.add(new Token(checkType(str), this.line + " : " + this.colon,"++"));
								Global.tokens_word.add(str);
								this.position += 2;
								return new String("++").trim();
							}
						} else if (c == '-') {
							if ((this.position != this.code.length()) && code.charAt(this.position + 1) == '-') {
								String str = new String(sb);
								Global.tokens.add(new Token(checkType(str), this.line + " : " + this.colon,"--"));
								Global.tokens_word.add(str);
								this.position += 2;
								return new String("++").trim();
							}
						}

						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				else {
					if (c == '>') // Different
					{
						if (code.charAt(this.position) == '=') {
							
							this.position += 1;
							return new String(">=").trim();
						}
					}
					else if (c == '<') // Different
					{
						if (code.charAt(this.position) == '=') {
							
							this.position += 1;
							return new String("<=").trim();
						}
					}
					else if (c == '=') // Different
					{
						if (code.charAt(this.position) == '=') {
							
							this.position += 1;
							return new String("==").trim();
						}
					}
					 if (c == TokensWords.NOTEQUAL.charAt(0)) // Different <> : Default
					{
						if ((this.position != this.code.length())
								&& code.charAt(this.position) == TokensWords.NOTEQUAL.charAt(1)) {
							
							this.position += 1;
							return new String(TokensWords.NOTEQUAL).trim();
						}
					}
				}
				String ss = new String(sb);
				// this.colon+=ss.length();
				return ss.trim();
			}

		}
		String ss = new String(sb);
		// this.colon+=ss.length();
		return ss.trim();
	}
}
