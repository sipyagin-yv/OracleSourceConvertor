package conv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class HtmlToJavaConverter {

	private Path sourceFile;
	private Path destFile;

	// общий вид убираемых тэгов:
	// < текст > текст </ текст >
	private final String HTMLTAG = "(<FONT.*>.*</FONT>|<a.*></a>)";
	
	// На что заменять найденные тэги
	private final String HTMLTAG_REPLACETO = ""; 

	// Регулярное выражения для пропускаемых строк
	private final String SKIPLINE = "(^$|<HTML>|</HTML>|<PRE>|</PRE>|<BODY.*>|</BODY>)";
	
	// Что убирать в начале строк
	private final String BEGINLINE_REMOVE = "^ {4}";

	public void setSourceFile(Path sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	public void setDestFile(Path destFile) {
		this.destFile = destFile;
	}
	
	public void convert() throws Exception {

		if( (sourceFile == null) || (destFile == null) ) {
			throw new Exception("Source and/or Destination directory is not set");
		}
		
		// Построчное копирование файла
		BufferedReader br = Files.newBufferedReader(sourceFile);
		BufferedWriter wr = Files.newBufferedWriter(destFile, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
		while(br.ready()) {
			String s = br.readLine().trim();
			
			// Пропустить строчку или нет?
			if( !s.matches(SKIPLINE) ) {
				
				// Убираем тэги HTML
				s = s.replaceAll(HTMLTAG, HTMLTAG_REPLACETO);

				// Убираем мнемоники
				s = s.replace("&lt;", "<").
				      replace("&gt;", ">").
				      replace("&amp;", "&");

				// Окончательная обработка
				s = s.replaceFirst(BEGINLINE_REMOVE, "");
				
				// Записать обработанную строчку
				wr.write(s);
				wr.newLine();
			}
		}
		br.close();
		wr.close();
		
	}
	
}
