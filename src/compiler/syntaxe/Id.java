package compiler.syntaxe;

import java.util.ArrayList;

import compiler.error.ErrorHandler;
import compiler.global.Array;
import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.Token;
import compiler.lexical.Tokens;

public class Id extends Keyword {

	public Id(int start) {
		super(start);
		if (Syntaxe.inVariableSection && !Syntaxe.inTableauBlock) {
			int type = this.getType();
			if (type != -1) {
				int idd = Global.tokens.get(start - 1).getId();
				if (idd != Tokens.SEMICOLON && idd != Tokens.VARIABLES && idd != Tokens.COMMA) {
					ErrorHandler.Expected(currentToken, true, ";");
				} else {
					String wrd = Global.tokens_word.get(start);
					if(Syntaxe.currentFunction != null)
					{
						wrd = Global.getUniqueId(wrd, Syntaxe.currentFunctionName);
					}
					
					if (Global.reservedWords.get(wrd) == null) {
						//FIXME check if this is important after i removed __func_add__4___3 style
						if (true /*!wrd.startsWith("__func_")*/) {
							Global.reservedWords.put(wrd, 0);
							Global.declaredVariables.put(wrd, new Variable(type));
						}/* else {
							ErrorHandler.lunch(Global.tokens.get(start),
									"'" + Global.tokens_word.get(start) + ErrorHandler.IsAlreadyDeclared);
						}*/
					} else {
						ErrorHandler.lunch(Global.tokens.get(start),
								"'" + Global.tokens_word.get(start) + ErrorHandler.IsAlreadyDeclared);
					}
				}
			}

		} else if (Syntaxe.inDbutFunction) {
			String varname = Global.tokens_word.get(start).trim();
			//There is tow cases, the first one is on Algorithme section
			//the second one is on Function or Procédure section
			//Algorithme section
			if(Syntaxe.currentFunction != null)
			{
				varname = Global.getUniqueId(varname, Syntaxe.currentFunctionName);
			}
			if (Global.declaredVariables.get(varname) == null ) {
				if (!varname.contains("[")) {
					ErrorHandler.lunch(Global.tokens.get(start),
							"'" + Global.tokens_word.get(start) + ErrorHandler.IsNotDeclaredInThisScope);
				} else {
					String vv = varname.substring(0, varname.indexOf("["));
					if(!checkOpenSquareBracketVsClosed(varname))
					{
						ErrorHandler.lunch(currentToken, ErrorHandler.UnExpectedToken+"'"+varname+"'");
					}
					else {
						//There is tow cases, the first one is on Algorithme section
						//the second one is on Function or Procédure section
						//Algorithme section
						boolean errorFound = false;
						if(Syntaxe.currentFunction == null)
						{
							if (Global.declaredTables.get(vv) == null) {
								ErrorHandler.lunch(Global.tokens.get(start),
										"'" + Global.tokens_word.get(start) + ErrorHandler.IsNotDeclaredInThisScope);
								errorFound = true;
							}
						}
						else {
							String newVarName = Global.getUniqueId(vv, Syntaxe.currentFunctionName);
							if (Global.declaredTables.get(newVarName) == null) {
								ErrorHandler.lunch(Global.tokens.get(start),
										"'" + Global.tokens_word.get(start) + ErrorHandler.IsNotDeclaredInThisScope);
								errorFound = true;
							}
								
						}
						if(!errorFound) {
							String vars[] = getVariablesFromTableau(varname);
							boolean error = false;
							for(String v:vars)
							{

								if(v.matches(Global.IdRegExp))
								{
									Variable var = Global.declaredVariables.get(v.trim());
									if(var == null)
									{
										
										ErrorHandler.lunch(currentToken,"'"+v+ ErrorHandler.IsNotDeclaredInThisScope);
									}
									else {
										if(var.getType() != Tokens.ENTIER)
										{
											ErrorHandler.lunch(currentToken,"'"+v+"' "+ErrorHandler.VariableTypeMustBeInteger);
										}
									}
								}
								else {
									error= true;
								}
							}
							if(error)
							{
								ErrorHandler.lunch(Global.tokens.get(start),
										"'" + Global.tokens_word.get(start) + ErrorHandler.arrayIndexOutOfBounds);
							}
						}
					}
					
				}
			}
			//Check id after this token
			int id = Global.tokens.get(start + 1).getId();
			if (!conditionSymbole(id) && id != Tokens.SEMICOLON && id != Tokens.ARITHMETIC && id != Tokens.CLOSBRACKET
					&& id != Tokens.COMMA && id != Tokens.REFECTOR && id != Tokens.INCREMENT && id != Tokens.DECREASE
					&& id != Tokens.OR && id != Tokens.AND && id != Tokens.ALLANT && id != Tokens.FUNCTION
					&& id != Tokens.A && id != Tokens.FAIRE && id != Tokens.PAS) {
				ErrorHandler.Expected(Global.tokens.get(start), false, ";");
			}
			//Check id before this token
			id = Global.tokens.get(start - 1).getId();
			if (!conditionSymbole(id) && id != Tokens.DEBUT && id != Tokens.SEMICOLON && id != Tokens.ARITHMETIC
					&& id != Tokens.OPENBRACKET && id != Tokens.COMMA && id != Tokens.REFECTOR && id != Tokens.INCREMENT
					&& id != Tokens.DECREASE && id != Tokens.OR && id != Tokens.AND && id != Tokens.FINSI
					&& id != Tokens.SINON && id != Tokens.ALORS && id != Tokens.FINTANTQUE && id != Tokens.POUR
					&& id != Tokens.A && id != Tokens.DE && id != Tokens.FAIRE && id != Tokens.PAS && id!=Tokens.REPETER
					&& id != Tokens.FINPOUR) {
				ErrorHandler.Expected(Global.tokens.get(start), true, ";");
			}

		} else if (Syntaxe.inConstantesFunction) {
			if (Global.tokens.get(start + 1).getId() == Tokens.EQULAS) {
				try {
					Token next = Global.tokens.get(start + 2);
					int id = next.getId();
					if (id != Tokens.CHAR || id != Tokens.WORD || id != Tokens.NUMBER || id != Tokens.INTEGER) {
						String varname = Global.tokens_word.get(start);
						if (Global.declaredVariables.get(varname) != null) {
							ErrorHandler.lunch(Global.tokens.get(start),
									"'" + Global.tokens_word.get(start) + ErrorHandler.IsAlreadyDeclared);
						} else {
							int type = -1;
							try {
								switch (Global.tokens.get(start + 2).getId()) {
								case Tokens.WORD:
									type = Tokens.CHAINE;
									break;
								case Tokens.CHAR:
									type = Tokens.CARACTERE;
									break;
								case Tokens.INTEGER:
									type = Tokens.ENTIER;
									break;
								case Tokens.NUMBER:
									type = Tokens.REEL;
									break;
								}
							} catch (Exception ecx) {
							}
							Variable v = new Variable(type, Global.tokens_word.get(start + 2));
							Global.declaredVariables.put(varname, v);
							Global.declaredConstantes.put(varname, v);
							Global.reservedWords.put(varname, 0);
						}
					} else {
						ErrorHandler.Expected(Global.tokens.get(start + 1), false, ErrorHandler.Expression);
					}
				} catch (Exception ex) {
					ErrorHandler.Expected(Global.tokens.get(start + 1), false, ErrorHandler.Expression);
				}
			} else {
				ErrorHandler.Expected(Global.tokens.get(start), false, "=");
			}
		} else if (Syntaxe.inTableauBlock && Syntaxe.inVariableSection) {
			String varname = Global.tokens_word.get(start);
			int idd = Global.tokens.get(start - 1).getId();
			if (idd != Tokens.SEMICOLON && idd != Tokens.TABLEAU && idd != Tokens.COMMA) {
				ErrorHandler.Expected(currentToken, true, ";");
			} else {
				if (Global.reservedWords.get(varname) == null) {
					if (Global.tokens.get(start + 1).getId() != Tokens.OPENSQUAREBRACKET) {
						ErrorHandler.Expected(currentToken, false, "[");
					} else {
						ArrayList<Integer> size = TableGetDim(start + 1);
						int type = this.getType();
						if (size.size() <= 3) {
							Array array = new Array(type, size.size(), size);
							Global.reservedWords.put(varname, 0);
							Global.declaredTables.put(varname, array);

							Variable varr = new Variable(type);
							// 1D Array
							int arraySize = size.size();
							if (arraySize == 1) {
								for (int i = 0; i < size.get(0); i++) {
									Global.declaredVariables.put(varname + "[" + i + "]", varr);
									Global.reservedWords.put(varname + "[" + i + "]", 0);
								}
							} else if (arraySize == 2) {
								// 2D array
								for (int i = 0; i < size.get(0); i++) {
									for (int j = 0; j < size.get(1); j++) {
										Global.declaredVariables.put(varname + "[" + i + "][" + j + "]", varr);
										Global.reservedWords.put(varname + "[" + i + "][" + j + "]", 0);
									}
								}
							} else if (arraySize == 3) {
								// 3D array
								for (int i = 0; i < size.get(0); i++) {
									for (int j = 0; j < size.get(1); j++) {
										for (int k = 0; k < size.get(2); k++) {
											Global.declaredVariables.put(varname + "[" + i + "][" + j + "][" + k + "]",
													varr);
											Global.reservedWords.put(varname + "[" + i + "][" + j + "][" + k + "]", 0);
										}
									}
								}
							}
						} else {
							ErrorHandler.lunch(currentToken, size.size() + ErrorHandler.ArraySizeNotAccepted);
						}

					}
				} else {
					ErrorHandler.lunch(currentToken, ErrorHandler.IsAlreadyDeclared);
				}
			}

		}
		//In Fonction arguments area
		else if(Syntaxe.inFunctionArg)
		{
			//get id before
			Token before = Global.tokens.get(start-1);
			if(before.getId() != Tokens.OPENBRACKET && before.getId() != Tokens.COMMA)
			{
				ErrorHandler.Expected(currentToken, true, "(");
			}
			//After
			Token after = Global.tokens.get(start+1);
			if(after.getId() != Tokens.TOWPOINTS)
			{
				ErrorHandler.Expected(currentToken, false, ":");
			}
			//Added this argument to function's arguments
			//Check first if the function is saved
			if(Syntaxe.currentFunction != null && Syntaxe.currentFunctionName != null)
			{
				try {
					Syntaxe.currentFunction.setArgList(addArgument(Syntaxe.currentFunction.getArgList(), Global.tokens.get(start+2).getId()));
					Syntaxe.currentFunction.setArgNameList(addArgumentName(Syntaxe.currentFunction.getArgNameList(), currentToken.getWord()));
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		else if(Syntaxe.inFunctionSection) {
			//Do nothing just to avoid error
		}
		else {
			ErrorHandler.lunch(currentToken,ErrorHandler.TokenInWrongSection);
		}
	}

	private int getType() {
		for (int i = start + 1; i < Global.tokens.size(); i++) {
			int id = Global.tokens.get(i).getId();
			if (this.isType(id)) {
				return id;
			}
		}
		return -1;
	}
	//method to add args to function's arguments
	private ArrayList<Integer> addArgument(ArrayList<Integer> list,int arg)
	{
		if(list == null)
		{
			list = new ArrayList<Integer>();
			list.add(arg);
		}
		else {
			list.add(arg);
		}
		return list;
	}
	private ArrayList<String> addArgumentName(ArrayList<String> list,String name)
	{
		if(list == null)
		{
			list = new ArrayList<String>();
			list.add(name);
		}
		else {
			list.add(name);
		}
		return list;
	}
}
