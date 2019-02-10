package compiler.Interpreter;

import java.awt.Label;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;

import compiler.error.ErrorHandler;
import compiler.global.Function;
import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.LexicalAnalysis;
import compiler.lexical.Token;
import compiler.lexical.Tokens;
import compiler.syntaxe.Keyword;
import compiler.syntaxe.Syntaxe;
import javafx.scene.control.TextArea;

public class MainInter extends Keyword {
	public MainInter(int start) {
		super(start);
	}

	protected void addInstr(String code) {
		Interpreter.Instructions.put(this.size(), code);
	}

	private int size() {
		return Interpreter.Instructions.size();
	}

	protected String removeVariablesFromTable(String expr, HashMap<String, Variable> variables) {
		if (expr.contains("[")) {
			String vars[] = getVariablesFromTableau(expr);
			StringBuffer vname = new StringBuffer(expr.substring(0, expr.indexOf("[")));
			for (String v : vars) {
				if (v.matches(Global.IdRegExp)) {
					if (variables == null)
						vname.append("[0]");
					else {
						Variable vv = variables.get(v);
						if (Interpreter.insideFunction) {
							vv = variables.get(Global.getUniqueId(v, Interpreter.funcName));
						}
						if (vv != null) {
							vname.append("[" + vv.getValue() + "]");
						} else {
							vname.append("[" + v + "]");
							ErrorHandler.lunch(ErrorHandler.ErrorParsingValueFrom + v + "'");
						}
					}
				} else {
					vname.append("[" + v + "]");
				}
			}
			return new String(vname);

		}
		return expr;
	}

	protected int getType() {
		for (int i = start + 1; i < Global.tokens.size(); i++) {
			int id = Global.tokens.get(i).getId();
			if (this.isType(id)) {
				return id;
			}
		}
		return -1;
	}

	protected boolean valideFunctionArguments(int index) {
		String funcName = Global.tokens_word.get(index);
		if (Global.declaredFunctions.get(funcName) != null) {
			Function fnc = Global.declaredFunctions.get(funcName);
			String funcArg = (String) getFunctionCall(index)[0];
			funcArg = funcArg.substring(funcName.length() + 1, funcArg.length() - 1);
			checkFunctionArguments(index, funcName, funcArg);
			return true;
		}
		return false;
	}

	private final void checkFunctionArguments(int index, String funcname, String args) {
		// 1st Seperate argument list
		boolean openDQ = false;
		boolean openQ = false;
		StringBuffer sb = new StringBuffer();
		ArrayList<String> stArg = new ArrayList<String>();
		for (int i = 0; i < args.length(); i++) {
			char c = args.charAt(i);
			sb.append(c);
			if (c == '"' && !openQ)
				openDQ = !openDQ;
			else if (c == '\'' && !openDQ)
				openQ = !openQ;
			if (openQ || openDQ)
				continue;
			if (c == ',') {
				sb.delete(sb.length() - 1, sb.length());
				if (sb.length() > 0) {
					String exp = (String) calcExpressionObject(Expression(new String(sb)));
					stArg.add(exp);
					sb.delete(0, sb.length());
				}
			}
		}
		if (sb.length() > 0) {
			String exp = (String) calcExpressionObject(Expression(new String(sb)));
			stArg.add(exp);
		}
		// 2nd get func
		Function func = Global.declaredFunctions.get(funcname);
		ArrayList<Integer> types = new ArrayList<Integer>();
		LexicalAnalysis la = new LexicalAnalysis();

		stArg.forEach(e -> {
			// System.err.println(e + " "+this.toTypeToken(la.checkType(e), e));
			types.add(this.toTypeToken(la.checkType(e), e));
		});
		if (types.size() != func.getArgSize()) {
			ErrorHandler.lunch(Global.tokens.get(index),
					"'" + funcname + "' " + ErrorHandler.FunctionArgumentsNotMatch);
		} else {
			ArrayList<Integer> funcArgList = func.getArgList();
			for (int i = 0; i < types.size(); i++) {
				if (types.get(i) != funcArgList.get(i)) {
					if ((types.get(i) == Tokens.ENTIER && funcArgList.get(i) == Tokens.REEL)
							|| (types.get(i) == Tokens.REEL && funcArgList.get(i) == Tokens.ENTIER)) {

					} else {
						ErrorHandler.lunch(Global.tokens.get(index),
								"'" + funcname + "' " + ErrorHandler.FunctionArgumentsNotMatch);
					}
				}
			}
		}
	}

	protected String getExpressionWithoutBrackets(int pos) {
		StringBuffer exp = new StringBuffer();
		boolean Double = false;
		int iddd = Global.tokens.get(pos).getId();
		if (this.isExpresion(iddd) || Global.tokens_word.get(pos).equals("-") || iddd == Tokens.OPENBRACKET) {
			for (int i = pos; i < Global.tokens.size(); i++) {
				int id = Global.tokens.get(i).getId();
				if (id == Tokens.NUMBER)
					Double = true;
				else if (id == Tokens.FUNCTION) {
					/*
					 * Object[] res = getFunctionCallToUnderScore(i); /*exp.append((String) res[0]);
					 * i = (int) res[1];
					 */
					exp.append(Global.tokens_word.get(i));
					continue;
				} else if (id == Tokens.ID) {
					if (Interpreter.insideFunction) {
						exp.append(Global.getUniqueId(Global.tokens.get(i).getWord(), Interpreter.funcName));

						continue;
					}
				}
				if (id != Tokens.ARITHMETIC && !isExpresion(id) && !conditionSymbole(id) && id != Tokens.OR
						&& id != Tokens.AND) {
					Interpreter.index = i;
					if (!openBracketVsClosed(new String(exp))) {
						exp.delete(exp.length() - 1, exp.length());
					}
					return new String(exp);
				}
				if (Global.tokens.get(i).getId() == Tokens.EQULAS)
					exp.append(" == ");
				else if (Global.tokens_word.get(i).equals("Mod"))
					exp.append(" % ");
				else
					exp.append(Global.tokens_word.get(i));
			}
		}
		if (Double) {
			return new String(this.calcExpressionDouble(exp));
		} else {
			return new String(this.calcExpressionInteger(exp));
		}
	}

	protected String getExpression(int pos) {
		return getExpressionCalled(pos, true);
	}
	/**Method
	 * @param Expression to check
	 * @return new expression containts bracket at it begining and ending
	 * */
	private String addBracketIfItSNeeded(String exp) {
		boolean openQ = false;
		boolean openDQ = false;
		int countBracket = 0;
		for(int i = 0;i<exp.length();i++)
		{
			char c = exp.charAt(i);
			if(c == '"' && !openQ)
			{
				openDQ = !openDQ;
				continue;
			}
			else if(c == '\'' && !openDQ)
			{
				openQ = !openQ;
				continue;
			}
			
			if(c == '(')
			{
				countBracket++;
				continue;
			}
			else if(c == ')')
			{
				countBracket--;
				continue;
			}
		}
		if(countBracket == 1)
			return exp.substring(1);
		if(countBracket < 0)
		{
			return exp.substring(0, exp.length() - Math.abs(countBracket));
		}
		return exp;
	}
	// Get expression, with considerBrackets as arguments, because in some cases
	// when need to ignore brackets
	// ins other cases we can't
	protected String getExpressionCalled(int pos, boolean considerBrackets) {
		StringBuffer exp = new StringBuffer();
		if (Global.tokens.get(pos - 1).getId() == Tokens.OPENBRACKET)
			exp.append("(");
		boolean Double = false;
		boolean functionFounded = false;
		int calcBrackets = 0;
		int iddd = Global.tokens.get(pos).getId();
		if (this.isExpresion(iddd) || Global.tokens_word.get(pos).equals("-") || iddd == Tokens.OPENBRACKET) {
			for (int i = pos; i < Global.tokens.size(); i++) {
				int id = Global.tokens.get(i).getId();
				if (id == Tokens.NUMBER)
					Double = true;
				// Consider Brackets
				if (considerBrackets) {
					if (id == Tokens.FUNCTION) {
						/*
						 * Object[] res = getFunctionCallToUnderScore(i); /*exp.append((String) res[0]);
						 * i = (int) res[1];
						 */
						functionFounded = true;
						exp.append(Global.tokens.get(i).getWord());
						continue;
					} else if (id == Tokens.ID) {
						if (Interpreter.insideFunction) {
							exp.append(Global.getUniqueId(Global.tokens.get(i).getWord(), Interpreter.funcName));

							continue;
						}
					} else if (id == Tokens.OPENBRACKET) {
						calcBrackets++;
					} else if (id == Tokens.CLOSBRACKET) {
						calcBrackets--;
						if (calcBrackets == 0)
							functionFounded = false;
					}
					if (id != Tokens.ARITHMETIC && !isExpresion(id) && id != Tokens.OPENBRACKET
							&& id != Tokens.CLOSBRACKET && !conditionSymbole(id) && id != Tokens.OR
							&& id != Tokens.AND) {
						if (id == Tokens.COMMA && (functionFounded && calcBrackets == 1)) {
							exp.append(",");
							continue;
						}

						Interpreter.index = i;
						return this.addBracketIfItSNeeded(new String(exp));
					}
				}
				// Don't consider brackets
				else {
					if (id == Tokens.FUNCTION) {
						Object[] res = getFunctionCallToUnderScore(i);
						exp.append((String) res[0]);
						i = (int) res[1];
						continue;
					} else if (id == Tokens.ID) {
						if (Interpreter.insideFunction) {
							exp.append(Global.getUniqueId(Global.tokens.get(i).getWord(), Interpreter.funcName));

							continue;
						}
					}
					if (id != Tokens.ARITHMETIC && !isExpresion(id) && id != Tokens.OPENBRACKET
							&& id != Tokens.CLOSBRACKET && !conditionSymbole(id) && id != Tokens.OR
							&& id != Tokens.AND) {
						Interpreter.index = i;
						if (!openBracketVsClosed(new String(exp))) {
							exp.delete(exp.length() - 1, exp.length());
						}
						return this.addBracketIfItSNeeded(new String(exp)) ;
					}
				}

				if (Global.tokens_word.get(i).equals("Mod"))
					exp.append(" % ");
				else
					exp.append(Global.tokens_word.get(i));
			}
		}
		return this.addBracketIfItSNeeded(new String(exp));
	}

	protected String calcExpressionDouble(StringBuffer expr) {
		String expre = new String(expr).replace("--", "+");
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(expre);
		double message = (double) exp.getValue();
		return String.valueOf(message);
	}

	protected String calcExpressionInteger(StringBuffer expr) {
		String expre = new String(expr).replace("--", "+");
		;
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(expre);
		int message = (int) exp.getValue();
		return String.valueOf(message);
	}

	protected Object calcExpressionObject(StringBuffer expr) {
		String expre = new String(expr).replace("--", "+");
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(expre);
		
		Object message = exp.getValue();
		return String.valueOf(message);
	}

	protected StringBuffer Expression(String ins) {

		ins = ins.trim();
		StringBuffer sb = new StringBuffer();
		StringBuffer exp = new StringBuffer();
		boolean openQ = false;
		boolean openDQ = false;
		char c = ' ';
		boolean funcFound = false;
		int openBrackets = 0;
		char lastDeter = ' ';
		for (int i = 0; i < ins.length(); i++) {
			c = ins.charAt(i);
			sb.append(c);
			if (c == '"' && !openQ)
				openDQ = !openDQ;
			else if (c == '\'' && !openDQ)
				openQ = !openQ;
			if (openQ || openDQ)
				continue;
			if (Global.inArray(c, Global.separatorsSpecial)) {
				lastDeter = c;
				if (c == '(')
					openBrackets++;
				else if (c == ')')
					openBrackets--;
				sb.delete(sb.length() - 1, sb.length());
				if (new String(sb).matches(Global.IdRegExp)) {
					if (Global.declaredFunctions.get(new String(sb).trim()) != null) {
						funcFound = true;
					} else {
						try {
							if (sb.indexOf("[") > 0) {
								sb = new StringBuffer(
										removeVariablesFromTable(new String(sb), Global.declaredVariables));
							}
							Variable variable = Global.declaredVariables.get(new String(sb).trim());

							if (Interpreter.insideFunction && variable == null) {
								variable = Global.declaredVariables
										.get(Global.getUniqueId(new String(sb).trim(), Interpreter.funcName));
							}
							String val = variable.getValue();
							/*
							 * if(variable.getType() == Tokens.CHAINE && !val.startsWith("\"")) { val =
							 * '"'+val+'"'; } else if(variable.getType() == Tokens.CARACTERE &&
							 * !val.startsWith("'")) { val = "'"+val+"'"; }
							 */
							if (val != null) {
								sb.delete(0, sb.length());
								sb.append(val + " " + c);
							} else {
								if (Global.declaredFunctions.get(new String(sb).trim()) != null) {

								} else {
									sb.append("0");
									System.err.println("MainInter.Expression line 175 val not found");
								}

							}

						} catch (Exception x) {
							// sb.append("0" + c);
						}
					}

				} else {
					sb.append(c);
				}

				exp.append(sb);
				if (funcFound && openBrackets == 1) {
					exp.append("(");
					funcFound = false;
				}
				sb.delete(0, sb.length());
			}

		}

		if (new String(sb).matches(Global.IdRegExp)) {
			try {
				String val = Global.declaredVariables.get(new String(sb)).getValue();
				sb.delete(0, sb.length());
				sb.append(val);
			} catch (Exception x) {

			}
		}
		if (!new String(exp).endsWith(lastDeter + "")) {
			exp.append(lastDeter + " " + sb);
		} else
			exp.append(sb);
		return exp;
	}

	protected Object[] getFunctionCallToUnderScore(int index) {
		int foundBracket = 0;
		boolean exit = false;
		Object[] res = (Object[]) new Object[2];
		StringBuffer sb = new StringBuffer();
		sb.append("__func_");
		sb.append(Global.tokens_word.get(index++) + "_");
		while (!exit) {
			try {
				int id = Global.tokens.get(index).getId();
				switch (id) {
				case Tokens.OPENBRACKET:
					foundBracket++;
					break;
				case Tokens.CLOSBRACKET:
					foundBracket--;
					if (foundBracket == 0) {
						sb.append("_");
						res[0] = new String(sb);
						res[1] = index;
						return res;
					}
					break;
				}
				String word = Global.tokens_word.get(index);
				if (word.equals(",") || word.equals("(") || word.equals(")"))
					sb.append("_");
				else if (word.matches("^[0-9]+\\.[0-9]+$")) {
					word = word.replace(".", "___");
					sb.append(word);
				} else
					sb.append(word);
				index++;
			} catch (Exception ex) {
				ex.printStackTrace();
				res[0] = new String(sb);
				res[1] = index;
				return res;
			}
		}
		res[0] = new String(sb);

		res[1] = index;
		return res;
	}

	protected Object[] getFunctionCall(int index) {
		int foundBracket = 0;
		boolean exit = false;
		Object[] res = (Object[]) new Object[2];
		StringBuffer sb = new StringBuffer();
		while (!exit) {
			try {
				int id = Global.tokens.get(index).getId();
				switch (id) {
				case Tokens.OPENBRACKET:
					foundBracket++;
					break;
				case Tokens.CLOSBRACKET:
					foundBracket--;
					if (foundBracket == 0) {
						sb.append(")");
						res[0] = new String(sb);
						res[1] = index;
						return res;
					}
					break;
				}
				sb.append(Global.tokens_word.get(index));
				index++;
			} catch (Exception ex) {
				ex.printStackTrace();
				res[0] = new String(sb);
				res[1] = index;
				return res;
			}
		}
		res[0] = new String(sb);

		res[1] = index;
		return res;
	}

	private boolean openBracketVsClosed(String str) {
		int count = 0;
		char s[] = str.toCharArray();
		for (char c : s) {
			if (c == '(')
				count++;
			else if (c == ')')
				count--;
		}
		return count == 0;
	}

	// Get Expression last position
	protected int getNextPos(int pos, HashMap<String, Variable> declaredVariables) {
		// ignore ( ) + - * / % Mod [ ] function id number reel word char boolean
		int id = Global.tokens.get(pos).getId();
		int[] array = new int[] { Tokens.OPENBRACKET, Tokens.CLOSBRACKET, Tokens.ARITHMETIC, Tokens.OPENSQUAREBRACKET,
				Tokens.CLOSESQUAREBRACKET, Tokens.FUNCTION, Tokens.ID, Tokens.NUMBER, Tokens.REEL, Tokens.WORD,
				Tokens.CHAR, Tokens.TRUE, Tokens.FALSE, Tokens.INTEGER };
		while (Global.inArray(id, array)) {
			if (id == Tokens.WORD || id == Tokens.CHAR || id == Tokens.TRUE || id == Tokens.FALSE) {
				ErrorHandler.lunch(Global.tokens.get(pos), ErrorHandler.OnlyIntegersAreAllowed);
			} else if (id == Tokens.ID) {
				Variable var = declaredVariables.get(Global.tokens.get(pos).getWord().trim());
				if (var != null && var.getType() != Tokens.ENTIER) {
					ErrorHandler.lunch(Global.tokens.get(pos), ErrorHandler.VariableTypeMustBeInteger);
				}

			}
			pos++;
			id = Global.tokens.get(pos).getId();
		}
		return pos;
	}

	/**
	 * This method returns arguments of a call of a function or a void function
	 */
	protected String[] getFunctionArgumentsAsArray(int pos) {
		List<String> list = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = pos; i < Global.tokens.size(); i++) {
				Token token = Global.tokens.get(i);
				if (token.getId() == Tokens.COMMA) {
					list.add(new String(sb));
					sb.delete(0, sb.length());
				} else if (token.getId() == Tokens.CLOSBRACKET
						&& Global.tokens.get(i + 1).getId() == Tokens.SEMICOLON) {
					list.add(new String(sb));
					break;
				} else {
					sb.append(token.getWord());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String[] newList = new String[list.size()];
		for (int i = 0; i < newList.length; i++) {
			newList[i] = list.get(i);
		}
		return newList;
	}

	/*
	 *******
	 ******* This section is just for methods that deals with functions
	 *******
	 */
	// This method is to get function arguments and call the function and return the
	// result
	private int _pos_ = 0;

	public String getFunctionResult(String expression, TextArea textarea) {
		StringBuffer sb = new StringBuffer();
		boolean openQ = false;
		boolean openDQ = false;
		int lastIDPos = 0;
		boolean funcFound = false;
		int openBrackets = 0;
		char c = '\0';
		for (int i = 0; i < expression.length(); i++) {
			c = expression.charAt(i);
			//FIXME I commented this line because expressions with brackets are not working as expected
			/*if(c == '(' && i==0)
				continue;
			else if(c == ')' && i == expression.length()-1)
				continue;*/
			sb.append(c);
			if (c == '\'' && !openDQ) {
				openQ = !openQ;
			} else if (c == '"' && !openQ) {
				openDQ = !openDQ;
			}
			if (openQ || openDQ)
				continue;
			if (c == '(')
				openBrackets++;
			else if (c == ')')
				openBrackets--;
			try {
				if (c == '(' && i!=0 && (!Global.inArray(expression.charAt(i - 1), Global.separators))) {
					_pos_ = i;
					String fncName = new String(sb).substring(0, sb.length() - 1);
					String val = getFunctionReturnedValue(expression, fncName,
							textarea);
					funcFound = true;
					int fncReturnType = Global.declaredFunctions.get(fncName).getReturnType();
					if(fncReturnType == Tokens.ENTIER || fncReturnType == Tokens.REEL)
					{
						val = val.replace("\"", "");
						val = val.replace("\'", "");
					}
					sb.delete(lastIDPos, i + 1);
					i = _pos_;
					sb.append(val);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (funcFound && openBrackets == 0)
				funcFound = false;
			if (Global.inArray(c, Global.separators)) {
				lastIDPos = i;
			}
		}
		_pos_ = 0;
		return new String(sb);
	}

	/**
	 * This method is responsible of returning the functions's result
	 */
	private String getFunctionReturnedValue(String ins, String funcName, TextArea textarea) {
		StringBuffer sb = new StringBuffer();
		boolean openQ = false;
		boolean openDQ = false;
		int openBrackets = 0;
		ArrayList<String> args = new ArrayList<String>();
		char c = '\0';
		for (int i = _pos_; i < ins.length(); i++) {
			_pos_ = i;
			c = ins.charAt(i);
			sb.append(c);
			if (c == '\'' && !openDQ) {
				openQ = !openQ;
			} else if (c == '"' && !openQ) {
				openDQ = !openDQ;
			}
			if (openQ || openDQ)
				continue;
			if (c == '(')
				openBrackets++;
			else if (c == ')')
				openBrackets--;
			if (c == ',' || (openBrackets == 0 && c == ')')) {
				sb.delete(sb.length() - 1, sb.length());
				String expr = new String(sb);
				if (expr.startsWith("(") && !expr.endsWith(")"))
					expr = expr + ")";
				//Check if the function's arguments list is empty
				if(!expr.trim().equals("()"))
				{
					args.add((String) calcExpressionObject(Expression(expr)));
				}
				sb.delete(0, sb.length());
			}
			if (openBrackets == 0) {
				Interpreter.argsList = new String[args.size()];
				for (int j = 0; j < args.size(); j++) {
					Interpreter.argsList[j] = args.get(j);
				}				
				// **** This section is respo of appending function arguments to declared
				// argumetns ******//
				Interpreter.insideFunction = true;
				Interpreter.funcName = funcName;
				try {
					// Find some arguments and affect them to the function's arguments

					int loopIndex = 0;
					Function function = Global.declaredFunctions.get(funcName);
					for (String l : Interpreter.argsList) {
						String varname2 = function.getArgNameList().get(loopIndex);
						String unique = Global.getUniqueId(varname2, funcName);

						Variable newVar = Global.declaredVariables.get(unique.trim());
						if (newVar != null) {
							newVar.setValue(l);
							Global.declaredVariables.replace(unique.trim(), newVar);

						}
						loopIndex++;
					}
					//Run the function
					Interpreter.lastIndex = RunTheProgram.index;
					RunTheProgram run = new RunTheProgram(Interpreter.functionsPositions.get(funcName),
							Interpreter.endfunctionsPositions.get(funcName));
					run.run(textarea, true);
					RunTheProgram.index = Interpreter.lastIndex;
					return Interpreter.returnedValue;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				// ******
			}

		}
		new Exception(
				"MainInter.getFunctionReturnedValue something went wrong it didn't returned the value required !!");
		return "";
		/// ----- End of functions section -----------///
	}
}
