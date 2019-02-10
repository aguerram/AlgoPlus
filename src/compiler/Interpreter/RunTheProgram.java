package compiler.Interpreter;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import compiler.error.ErrorHandler;
import compiler.global.Function;
import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.LexicalAnalysis;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;
import compiler.syntaxe.Syntaxe;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class RunTheProgram extends MainInter {
	public RunTheProgram(int start,int stop) {
		super(start);
		this.stop = stop;
	}
	
	public static int counter = 0;
	public static int lastCounter = 0;
	public static boolean waitingUserInput = false;
	public static int index = 0;
	
	private int stop = 0;
	private boolean Write = false;
	private boolean Start = false;
	private boolean Set = false;
	private boolean Read = false;
	private int lastFunctionCall = 0;
	
	private int loop= 0;
	
	//-------- For inputs in TextArea
	private int countTapped = 0;
	private int lastTextAreaSize = 0;
	//-----
	
	private String lastCode = "";
	private String oldCode = "";
	
	private StringBuffer sb = new StringBuffer();
	private boolean SiConditionTrue = false;
	public static void inis()
	{
		waitingUserInput = false;
		lastCounter = 0;
		counter = 0;
	}
	public void runFunction(TextArea textarea,String funcName)
	{
		Function func = Global.declaredFunctions.get(funcName);
		if(func != null)
		{
			runProg(textarea, func.getInstructions(), Global.FunctiondeclaredVariables,true);
		}
	}
	public synchronized void run(TextArea textarea,boolean function)
	{
		runProg(textarea,Interpreter.Instructions,Global.declaredVariables,function);
	}
	/**
	 * @param
	 * 	isfunction : is to tell if the run method will run in normal run or function run
	 * */
	public synchronized void runProg(TextArea textarea,HashMap<Integer,String> instructions,HashMap<String,Variable> declaredVariables,boolean isfunction) {
		
		if(!isfunction)
			textarea.clear();
		for (index = start; index < stop; index++) {
			int i = index;
			if (waitingUserInput) {
				Thread t = new Thread(()-> {
					if(textarea.isFocused())
					{
						textarea.setOnKeyPressed(e -> {
							if (e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.DELETE) {
								if(countTapped>0)
								{
									countTapped--;
									
								}
								else {
									e.consume();
								}
							}
							else if(lastTextAreaSize != textarea.getText().length())
							{
								countTapped++;
								lastTextAreaSize = textarea.getText().length();
							}
							
							Platform.runLater(() -> {
								if (e.getCode() == KeyCode.ENTER) {
									String co = textarea.getText();
									int startInd = lastCode.length();
									co = co.substring(startInd);
									if (textarea.isEditable() && !co.equals("\n")) {
										setReadedVariable(co, textarea,declaredVariables);
										waitingUserInput = false;
										textarea.setEditable(false);
									}
								}
							});
						});
					}
					
				});
				t.start();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				index--;
				continue;
			}
			
			String ins = instructions.get(i);
			
			//--------
			sb.append(ins+"\n");
			//---------
			char startsWith = ins.charAt(0);
			if (!Start && ins.equals("start"))
			{
				Start = true;
				continue;
			}
			if (!Start && !isfunction)
				continue;	
			switch(startsWith)
			{
				case 'd':
					if(ins.startsWith("delete"))
					{
						//Delete variable
						String varname = ins.substring("delete".length(), ins.length()).trim();
						declaredVariables.remove(varname);
						Global.reservedWords.remove(varname);
						continue;
					}
					break;
				case 'e':
					if (ins.equals("endSet")) {
						Set = false;
						continue;
					} 
					else if (ins.equals("endWrite")) {
						Write = false;
						continue;
					}
					else if (ins.equals("endRead")) {
						Read = false;
						continue;
					}
					else if(ins.equals("else"))
					{
						if(SiConditionTrue)
						{
							String in;
							while(true && i < instructions.size())
							{
								in = instructions.get(++i);
								if(in.equals("endIf"))
								{
									index = i;
									SiConditionTrue = false;
									break;
								}
							}
						}
						continue;
					}
					else if(ins.equals("endLoop"))
					{
						loop = 0;
						continue;
					}
					else if(ins.equals("endFunction"))
					{
						if(!isfunction)
							index = lastFunctionCall;
						continue;
					}
				break;
				case 'f':
					if(ins.startsWith("function") && Start)
					{
						//function funcname index index_in_tokens
						String funcname = ins.substring("function".length(), ins.lastIndexOf(" ")).trim();
						int pos = Integer.parseInt(ins.substring(ins.lastIndexOf(" "), ins.lastIndexOf("!")).trim());
						int token_pos = Integer.parseInt(ins.substring(ins.lastIndexOf("!")+1, ins.length()).trim());
						lastFunctionCall = i;
						index = pos;				
						Interpreter.insideFunction = true;
						Interpreter.funcName = funcname;
						try {
							//Find some arguments and affect them to the function's arguments
							int id = Global.tokens.get(i-1).getId();
							if(id != Tokens.FONCTION && id != Tokens.PROCEDURE)
							{
								String[] list = getFunctionArgumentsAsArray(token_pos+2);								
								int loopIndex = 0;
								Function function = Global.declaredFunctions.get(funcname);
								if(list.length>0 && function.getArgSize()>0)
								for(String l:list)
								{
									
									String varname2 = function.getArgNameList().get(loopIndex);
									String unique = Global.getUniqueId(varname2, funcname);
									String expression = new String(Expression(l)).trim();
									String exp=expression;
									if(!expression.contains("\"") && !expression.contains("'"))
										exp = (String)calcExpressionObject(new StringBuffer(expression));
									else {
										exp = exp.replaceAll("(\"{1}\\s*\\+{1}\\s*\"{1})", "");
									}
									Variable newVar = Global.declaredVariables.get(unique.trim());
									if(newVar != null)
									{
										newVar.setValue(exp);
										Global.declaredVariables.replace(unique.trim(), newVar);
										
									}
									loopIndex++;
								}
							}
						}
						catch(Exception ex) {ex.printStackTrace();}
						continue;
					}
					break;
				case 's':
					if (ins.equals("set")) {
						Set = true;
						continue;
					}
				break;
				case 'i':
					if(ins.equals("if"))
					{
						//If test
						String exp = instructions.get(i+1);
						String res = (String) this.calcExpressionObject(this.Expression(exp));
						if(res.equals("false"))
						{
							String in;
							while(true && i < instructions.size())
							{
								in = instructions.get(++i);
								if(in.equals("endIf") || in.equals("else"))
								{
									index = i;
									break;
								}
							}
						}
						else {
							SiConditionTrue = true;
						}
						continue;
					}
				break;
				case 'g':
					if (ins.equals("goto"))
					{
						loop = index;
						
						if(!instructions.get(i+2).equals("endLoop"))
						{
							String exp = instructions.get(i+2);
							String res = (String) this.calcExpressionObject(this.Expression(exp));
							
							if(!res.equals("false"))
							{
								index = Integer.parseInt(instructions.get(i+1))-1;
							}
							
						}
						else {
							index = Integer.parseInt(instructions.get(i+1))-1;
						}
						
						
						try {
							Thread.sleep(Global.loopSpeed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						continue;
					}
					break;
				case 'r':
					
					if (ins.equals("read")) {
						Read = true;
						continue;
					} 
					if(ins.startsWith("return"))
					{
						//FIXME fix it so it could handle arrays as well
						ins = ins.substring("return".length()+1, ins.length());
						Interpreter.returnedValue = (String)calcExpressionObject(Expression(ins));
						int type=  new LexicalAnalysis().checkType(Interpreter.returnedValue);
						if(type != Tokens.WORD)
						{
							Interpreter.returnedValue = '"'+Interpreter.returnedValue+'"';
						}
						else if(type != Tokens.CHAR)
						{
							Interpreter.returnedValue = "'"+Interpreter.returnedValue+'"';
						}
						continue;
					}
				break;
				case 'l':
					 if(ins.equals("loop"))
						{
							//loop test
							String exp = instructions.get(i+1);
							String res = (String) this.calcExpressionObject(this.Expression(exp));
							
							if(res.equals("false"))
							{
								String in;
								int safe = 0;
								while(true && i < instructions.size())
								{
									
									in = instructions.get(++i);
									if(in.equals("loop"))
									{
										safe++;
									}
									else if(in.equals("endLoop") && safe == 0)
									{
										index = i;
										break;
									}
									else if (in.equals("endLoop") && safe != 0)
									{
										safe--;
									}
								}
							}
							else {
								continue;
							}
							
						}
					 if(ins.startsWith("let"))
					 {
						 String varname= ins.substring("let".length(), ins.lastIndexOf(" ")).trim();
						 String type = ins.substring(ins.lastIndexOf(" "), ins.length()).trim();
						 if(declaredVariables.get(varname) == null)
						 {
							 int t = 0;
							 switch(type)
							 {
							 case "int":t=Tokens.ENTIER;break;
							 case "float": t=Tokens.REEL; break;
							 case "boolean": t=Tokens.BOOLEEN;break;
							 case "String":t=Tokens.CHAINE;break;
							 case "char":t=Tokens.CARACTERE ;break;
							 }
							 Global.declaredVariables.put(varname, new Variable(t));
							 Global.reservedWords.put(varname, t);
						 }
						 continue;
					 }
					break;
				case 'w':
					if (ins.equals("write")) {
						Write = true;
						continue;
					} 
					break;
			}
							
			if (Read) {
				oldCode = textarea.getText();
				ReadRun rr = new ReadRun(textarea, ins);
				
				waitingUserInput = true;
				lastTextAreaSize = textarea.getText().length();
				countTapped = 0;
				Platform.runLater(() -> {
					lastCode = textarea.getText();
					textarea.requestFocus();
				});
			}
			if (Set) {
				Set(ins,declaredVariables,textarea);
			}
			
			if (Write) {
				ins = ins.trim();
				if (ins.startsWith("\"")) {
					Write(ins, textarea);
				}
				else if (ins.startsWith("'")) {
					Write(ins, textarea);

				} else if (ins.startsWith("exp")) {
					
					ins = ins.substring(3, ins.length());
					ins = ins.trim();
					ins = this.getFunctionResult(ins, textarea);
					System.err.println(ins);
					if(ins.contains("["))
					{
						ins = removeVariablesFromTable(ins, declaredVariables);
					}
					String val = "";
					try {
						Variable vvl = declaredVariables.get(ins); 
						/*if(Interpreter.insideFunction)
						{
							vvl = declaredVariables.get(Global.getUniqueId(ins, Interpreter.funcName)); 
						}*/						
						if (vvl == null) {
							val = new String(Expression(ins));	
						}
						else
							val = vvl.getValue();
			
					} catch (Exception ex) {
						val = new String(Expression(ins));
					}
					
					if (val.contains("\"") || val.contains("'")) {
						ins = val;
						
						StringBuffer sb = new StringBuffer();
						boolean opQ = false;
						boolean opDQ = false;
						for (int j = 0; j < val.length(); j++) {
							char c = ins.charAt(j);
							if( c == '\'' && !opDQ)
							{
								opQ = !opQ;
								continue;
							}
							else if( c == '"' && !opQ)
							{
								opDQ = !opDQ;
								continue;
							}
							if(c == ' ' && (!opDQ && !opQ))
								continue;
							if(c == '+' && (!opDQ && !opQ))
								continue;
							sb.append(c);
						}
						
						ins = new String(sb);
					} else {
						try {
							
							StringBuffer getted = Expression(ins);
							System.err.println(getted);
							String Sgetted = new String(getted);
							if(Sgetted.contains("<") || Sgetted.contains("<=")
									||Sgetted.contains(">")||Sgetted.contains(">=")||Sgetted.contains(TokensWords.NOTEQUAL )
									|| Sgetted.contains("&&")|| Sgetted.contains("||") || Sgetted.contains("true") ||Sgetted.contains("false"))
							{
								ins = String.valueOf(this.calcExpressionObject(getted));
								if(ins.equals("false"))
								{
									ins = TokensWords.FAUX;
								}
								else if(ins.equals("true"))
								{
									ins = TokensWords.VRAIS;
								}
							}
							else
							{
								if (new String(getted).contains(".")) {
									ins = this.calcExpressionDouble(getted);
								} else {
									ins = this.calcExpressionInteger(getted);
								}
							}
						} catch (Exception ex) {
							ins = ins.replaceAll("(\"{1}\\s*\\+{1}\\s*\"{1})", "");
							//ins = ins.substring(1, ins.length() - 1);
						}
					}					
					Write(ins, textarea);

				} else {
					Write(ins, textarea);
				}

			}

		}
		Global.endedTime = System.currentTimeMillis();
		try {
			FileWriter out = new FileWriter("output.o");
			for (int i = 0; i < instructions.size(); i++) {
				out.write(instructions.get(i)+"\n\r");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private  void Write(String txt, TextArea text) {
		
		if(txt.startsWith("-"))
		{
			if(txt.substring(1, txt.length()).matches(Global.IdRegExp))
				return;
		}
		
		boolean openQ = false;
		boolean openDQ = false;
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<txt.length();i++)
		{
			char c=  txt.charAt(i);
			sb.append(c);
			if(c == '\'' && !openDQ)
			{
				openQ = !openQ;
				sb.delete(sb.length()-1, sb.length());
			}
			else if(c == '"' && !openQ)
			{
				openDQ = !openDQ;
				sb.delete(sb.length()-1, sb.length());
			}
			if(openQ || openDQ)
				continue;
			if(c == '+' || c == '(' || c ==')')
				sb.delete(sb.length()-1, sb.length());
		}
		Platform.runLater(()->{
			text.appendText(new String(sb));
		});
	}

	private synchronized void Set(String ins,HashMap<String,Variable> declaredVariables,TextArea textarea) {
		try {
			String id = ins.split(" ")[0];
			if(id.contains("["))
			{
				id = removeVariablesFromTable(id, declaredVariables);
			}
			String sndParte = ins.substring(ins.indexOf(" "), ins.length()).trim();
			
			sndParte = new String(Expression(sndParte));
			//This function is to replace function calls with returned results
			sndParte = getFunctionResult(sndParte,textarea);
			if (split(ins)[1].contains("\"") || split(ins)[1].contains("'")) {
				ins = sndParte;
				ins = ins.replace("\"\\s*\\+\\s*\"", "");
			} else {
				StringBuffer getted = new StringBuffer(sndParte);
				
				ins = String.valueOf(this.calcExpressionObject(getted));
				Variable v = declaredVariables.get(id);
				if(v == null)
				{
					ErrorFound(ErrorHandler.ErrorParsingValueFrom+id+"'");
				}
				if (v.getType() == Tokens.ENTIER) {
					int indexOfdot = ins.indexOf(".");
					if (indexOfdot > 0) {
						ins = ins.substring(0, indexOfdot);
					}
				}
			}
			String value = ins;
			Variable old = declaredVariables.get(id);
			if(old.getType() == Tokens.CHAINE && !value.startsWith("\""))
				value = '"'+value+'"';
			else if(old.getType() == Tokens.CARACTERE && !value.startsWith("'"))
				value = "'"+value+"'";
			
			Variable var = new Variable(old.getType(), value);
			declaredVariables.remove(id);
			declaredVariables.put(id, var);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private synchronized void ErrorFound(String error)
	{
		index = Interpreter.Instructions.size()-1;
		waitingUserInput = false;
		addInstr("write");
		addInstr("\"\n#################################\n"+ErrorHandler.ErrorsFound+"\n\t"+error+"\n#################################\"");
		addInstr("endWrite");
		
	}
	private void setReadedVariable(String co, TextArea console,HashMap<String,Variable> declaredVariables) {
		String ins = Interpreter.Instructions.get(index - 1);
		String varname = ins.split(" ")[0];
		int type = Integer.parseInt(ins.split(" ")[1].trim());
		co = co.trim();
		if(co.endsWith("(\n|\r)"))
		{
			co = co.substring(0, co.length()-1);
		}
		if (type == Tokens.ENTIER || type == Tokens.REEL) {
			if (co.matches("^\\-?[0-9]+\\.{0,1}[0-9]*$")) {
				
				try {
					Variable old = declaredVariables.get(varname);
					if(type == Tokens.ENTIER && co.contains("."))
					{
						co = co.substring(0, co.indexOf("."));
					}
					
					else if(type == Tokens.REEL && !co.contains("."))
					{
						co = co+".0";
					}
					Variable New = new Variable(old.getType(), co);
					declaredVariables.replace(varname, New);
				} catch (Exception ex) {
					Variable old = declaredVariables.get(varname);
					Variable New;
					if (type == Tokens.REEL)
						New = new Variable(old.getType(), "0.0");
					else
						New = new Variable(old.getType(), "0");
					declaredVariables.replace(varname, New);
					ex.printStackTrace();
				}
			} else {
				Variable old = declaredVariables.get(varname);
				Variable New;
				if (type == Tokens.REEL)
					New = new Variable(old.getType(), "0.0");
				else
					New = new Variable(old.getType(), "0");
				declaredVariables.replace(varname, New);
			}
		}
		else {
			Variable New = null;
			co = co.trim();
			Variable old = declaredVariables.get(varname);
			if(old.getType() == Tokens.CHAINE)
				New = new Variable(old.getType(), "\""+co+"\"");
			else if(old.getType() == Tokens.CARACTERE) {
				New = new Variable(old.getType(), "'"+co.charAt(0)+"'");
			}
			
			if(New != null)
				declaredVariables.replace(varname, New);
		}
	}

	// Supporting methods
	private String[] split(String ins) {
		String spl[] = new String[2];
		if (ins.contains(" ")) {
			int index = ins.indexOf(" ");
			try {
				spl[0] = ins.substring(0, index);
				spl[1] = ins.substring(index);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return spl;
	}
}
