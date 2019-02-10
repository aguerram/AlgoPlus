package compiler.error;

import java.util.ArrayList;

import application.StaticVars;
import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.TokensWords;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class ErrorHandler {
	public static ArrayList<String> error;
	public static String code;
	public static ObservableList<ErrorClass> ErrorData = FXCollections.observableArrayList();
	// Single words
	public static final String Identifier = "s'identifier";
	public static final String Expression = "Expression";
	public static final String Number = "nombre";
	public static final String Type = "Type";
	public static final String Entier = "Entier";
	public static final String Title = " titre du programme ";
	public static final String Refector = "<-";
	public static final String Arithmetic = "arithm�tique";
	public static final String ExpectedTheSameTypeAsBefor = "The variable type as next id";
	public static final String ArraySize = "La taille du tableau";
	public static final String ErrorsFound = "Erreurs d'ex�cution trouv�es: ";
	public static final String OnlyIntegersAreAllowed = "Seuls les nombres entiers sont autoris�s";
	public static final String ReturnTypeMustBeLikeFunctionsReturnType = "Le type de retour doit obligatoirement �tre le type de retour de la fonction";

	public static final String UnExpectedToken = "Jeton inattendu";
	public static final String ErrorParsingValueFrom = "Erreur d'analyse de la valeur '";
	public static final String FunctionArgumentsNotMatch = "Les arguments de votre fonction ne correspondent pas";
	public static final String VariableTypeMustBeInteger = "La variable doit etre de type entier";
	public static final String OpenedSiBlockButNotClosed = "Ouvert " + TokensWords.SI + " mais n'est pas ferm�";
	public static final String OpenedPourBlockButNotClosed = "Ouvert " + TokensWords.POUR + " mais n'est pas ferm�";
	public static final String OpenedRepeteBlockButNotClosed = "Ouvert " + TokensWords.REPETER
			+ " mais n'est pas ferm�";
	public static final String OpenedTantQueBlockButNotClosed = "Ouvert " + TokensWords.TANTQUE
			+ " mais n'est pas ferm�";
	public static final String UnexpectedFinSi = "Inattendu " + TokensWords.FINSI + "";
	public static final String ConditionSyntaxeNotValide = "Syntaxe de la condition n'est pas valide";
	public static final String SaveCodeForSupport = "Enregistrer ce code pour les utilisations de support";
	public static final String arrayIndexOutOfBounds = "' Indice de tableau hors limites";
	public static final String ArraySizeNotAccepted = "Les tableau ne sont pas support�";
	public static final String LireCantReadIdentifiersOnly = TokensWords.LIRE
			+ " la fonction ne peut lire que les identifiants";
	public static final String FuncNotDeclared = "Fonction pas d�clarer ";
	public static final String CantDoArithmeticOperationsBooleen = " Impossible d effectuer des op�rations arithm�tiques sur les expressions bool�ennes ";
	public static final String SemiColonExpected = "Attendu ';'";
	public static final String TowPointExpected = "Attendu ':'";
	public static final String ExpectedType = "Attendu type";
	public static final String CharExpectedBuWordFounded = "Caract�re attendu mais mot fond�";
	public static final String OpenBracketButNotClosed = "Support ouvert mais pas ferm�";
	public static final String CloseBracketButNotClosed = "support ferm� mais pas ouvert";
	public static final String InvalideExpression = "expressions invalides";
	public static final String ExpectedIdentifier = "Identifiant attendu";
	public static final String IsAlreadyDeclared = "' Est d�j� d�clar�";
	public static final String QuoteOpened = "Citation ' ouverte mais pas fermer";
	public static final String DoubleQuoteOpened = "Double citation \" ouverte mais pas fermer";
	public static final String TokenInWrongSection = "Le jeton est dans une zone inaccessible";
	public static final String TableSizeMustBeInteger = "La taille du tableau doit �tre Integer";
	public static final String MultipleAlgorithmeDefinition = "D�finition multiple de mot cl� algorithme";
	public static final String MultipleDecalreDefinition = "Plusieurs d�finitions de Declare mot-cl�";
	public static final String MultipleDebutDefinition = "Plusieurs definition de Debut mot-cl�";
	public static final String MultipleTableauDefinition = "Plusieurs definition de Tableau mot-cl�";
	public static final String ExpectedFinIntTheEndOfProgram = "Attendu fin pour le programme ";
	public static final String IsNotDeclaredInThisScope = "' n est pas d�clarer dans cette port�e";
	public static final String MustBeBeforeDebutTokent = "Ce mot cl� doit �tre avant le jeton de d�but";
	public static final String ExpectedBeforeToken = "Attend %s avant '%s' jeton";
	public static final String ExpectedAfterToken = "Attendu %s apres '%s' jeton";
	public static final String ConstatntesMustBeBeforVariableToken = "Constantes doit �tre avant le jeton Variables";

	public static final String MainFunctionNotFound = "Fonction principale introuvable";
	public static final String MultiConstatesDefinition = "D�finition multiple de Constates";
	public static final String ConstantesCantBeChanged = "la variable constante ne pas etre changer";
	public static final String SinonTokenWithoutPreviousSiToken = "'" + TokensWords.SINON + "' Jeton sans pr�c�dent '"
			+ TokensWords.SI + "'";
	public static final String TheNameOfTheProgramMustBeAsFile = "Le nom du programme doit �tre comme le nom du fichier";

	public static String fileName = "";
	static {
		error = new ArrayList<String>();
	}

	private static String getFileName() {
		String name = StaticVars.lastWorkingDirectory + "\\" + fileName;
		return name.replace(" *", "");
	}

	public static void lunch(Token line, String err) {
		String er = "";
		er += String.format(err);
		ErrorData.add(new ErrorClass(line.getLine(), line.getColonNumber(), getFileName(), er));
	}

	public static void lunch(Token line, String err, boolean after) {
		lunch(line, err);
	}

	public static void lunch(String err) {
		ErrorData.add(new ErrorClass("", "", getFileName(), err));
	}

	public static void Expected(Token currentToken, boolean beforeToken, String... expect) {
		Token token = currentToken;
		StringBuffer sb = new StringBuffer();
		String expected = "";
		int count = 0;
		for (String ss : expect) {
			count++;
			if (count == expect.length)
				sb.append("'" + ss + "'");
			else
				sb.append("'" + ss + "',");

		}
		expected = new String(sb);
		String line = getLine(code, token);
		String word = Global.tokens_word.get(token.getOrderNumber());
		int nn = line.indexOf(word);
		String er = "";
		if (beforeToken) {
			er += String.format(ExpectedBeforeToken, expected, word);
		} else {
			er += String.format(ExpectedAfterToken, expected, Global.tokens_word.get(token.getOrderNumber()));
		}
		ErrorData.add(new ErrorClass(token.getLine(), token.getColonNumber(), getFileName(), er));
	}

	private static String getLine(String code, Token line) {
		try {
			String lines = code.split("\n")[line.getLineNumber()];
			return lines;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
}
