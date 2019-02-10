package controller;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import compiler.global.Global;
import javafx.concurrent.Task;

public class RichTextArea {
	private final String[] KEYWORDS = Global.KEYWORDS;
	private final String[] SecondaryKeywords = Global.SECONDARYKEYWORDS;

	private final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	private final String SECONDARYKEYWORD_PATTERN = "\\b(" + String.join("|", SecondaryKeywords) + ")\\b";
	private final String SYMBOLES_PATTERN = "(\\<\\-)";
	private final String PAREN_PATTERN = "\\(|\\)";
	private final String BRACE_PATTERN = "\\{|\\}";
	private final String BRACKET_PATTERN = "\\[|\\]";
	private final String SEMICOLON_PATTERN = "\\;";
	private final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"?";
	private final String CHAR_PATTERN = "\'([^\'\\\\]|\\\\.)*\'?";
	private final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
	private final Pattern PATTERN = Pattern.compile("(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "|(?<PAREN>" + PAREN_PATTERN
			+ ")" + "|(?<BRACE>" + BRACE_PATTERN + ")" + "|(?<SYMBOLES>" + SYMBOLES_PATTERN + ")" + "|(?<SNDKEYWORD>"
			+ SECONDARYKEYWORD_PATTERN + ")" + "|(?<BRACKET>" + BRACKET_PATTERN + ")" + "|(?<SEMICOLON>"
			+ SEMICOLON_PATTERN + ")" + "|(?<STRING>" + STRING_PATTERN + ")" + "|(?<CHAR>" + CHAR_PATTERN + ")"
			+ "|(?<COMMENT>" + COMMENT_PATTERN + ")");
	private CodeArea codeArea;
	private ExecutorService executor;

	public CodeArea getCodeArea() {
		executor = Executors.newSingleThreadExecutor();
		codeArea = new CodeArea();
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		Subscription cleanupWhenDone = codeArea.multiPlainChanges().successionEnds(Duration.ofMillis(10))
				.supplyTask(this::computeHighlightingAsync).awaitLatest(codeArea.multiPlainChanges()).filterMap(t -> {
					if (t.isSuccess()) {
						return Optional.of(t.get());
					} else {
						t.getFailure().printStackTrace();
						return Optional.empty();
					}
				}).subscribe(this::applyHighlighting);
		try {
			codeArea.getStylesheets().add(getClass().getClassLoader().getResource("css/style.css").toExternalForm());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.codeArea;
	}

	public void destroy() {
		executor.shutdown();
	}

	private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
		String text = codeArea.getText();
		Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
			@Override
			protected StyleSpans<Collection<String>> call() throws Exception {
				return computeHighlighting(text);
			}
		};
		executor.execute(task);
		return task;
	}

	private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
		codeArea.setStyleSpans(0, highlighting);
	
	}

	private StyleSpans<Collection<String>> computeHighlighting(String text) {
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {
			String styleClass = matcher.group("KEYWORD") != null ? "keyword"
					: matcher.group("PAREN") != null ? "paren"
							: matcher.group("SNDKEYWORD") != null ? "sndkeyword"
									: matcher.group("BRACE") != null ? "brace"
											: matcher.group("SYMBOLES") != null ? "symboles"
													: matcher.group("BRACKET") != null ? "bracket"
															: matcher.group("SEMICOLON") != null ? "semicolon"
																	: matcher.group("CHAR") != null ? "charr"
																			: matcher.group("STRING") != null ? "string"
																					: matcher.group("COMMENT") != null
																							? "comment"
																							: null;
			/* never happens */ assert styleClass != null;
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}
}
