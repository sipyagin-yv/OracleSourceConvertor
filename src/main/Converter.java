package main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

import conv.HtmlToJavaConverter;
import helpers.Helpers;

public class Converter {

	private static String sourcePath;
	private static String destPath;
	
	private static final String SRC_EXTENSION = ".html";
	private static final String DST_EXTENSION = ".java";
	
	// (?i) - без учёта регистра
	// .* - любой символ 1 или более раз (т.е. файлы только из одного расширения ".html" не распознаются)
	// \\ + последующая точка из SRC_EXTENSION -> \. -> просто точка
	// $ - конец строки
	private static final String SRC_EXTENSION_REGEXP = "(?i).+\\" + SRC_EXTENSION + "$";

	/**
	 * Проверка параметров.
	 * @param args Массив параметров, с которыми запустилась программа.
	 * @return Возвращает строку (ошибку или сообщение). Если возвращает <code>null</code>, ошибок нет, можно работать дальше.
	 */
	private static String getAndCheckParameters(String[] args) {

		System.out.println("HTML to JAVA converter. Version 22-06-2016.");
		
		// Собираем параметры
		if( args.length != 2 ) {
			return "Usage: Converter Source_Directory Destination_Directory";
		}
		sourcePath = args[0];
		destPath = args[1];

		// Инфа
		System.out.println("Source path = " + sourcePath);
		System.out.println("Destination path = " + destPath);

		// Проверки
		File fileCheckSrc = Paths.get(sourcePath).toFile();
		if (!fileCheckSrc.exists()) {
			return "ERROR: source path is not exist";
		}
		if (!fileCheckSrc.isDirectory()) {
			return "ERROR: source path is not directory";
		}
		
		File fileCheckDst = Paths.get(destPath).toFile();
		if ( fileCheckDst.exists() ) {
			if( !fileCheckDst.isDirectory() ) {
				return "ERROR: destination path is not directory";
			}
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {

		// parameter tmp.: "c:\ouaf\testconvert"
		// parameter source folder: "C:\ouaf\ccb24sp3\splapp\applications\appViewer\data\javadocs\src-html"
		// parameter destination folder: "D:\1"

		String result = getAndCheckParameters(args);
		if( result != null ) {
			System.out.println(result);
			return;
		}

		// "Гуляем" по всем файлам и папкам исходного каталога
		int filesCount = 0;
		Stream<Path> pathStream = Files.walk(Paths.get(sourcePath));
		Iterator<Path> iter = pathStream.iterator();
		while (iter.hasNext()) {
		
			File file = iter.next().toFile();

			if (file.isDirectory()) {
				System.out.println("DIR \"" + file + "\":");

			} else {
				
				if ( file.getName().matches(SRC_EXTENSION_REGEXP) ) {

					filesCount++;
					
					// stat
					System.out.println("* " + file.getName());

					// calc destination filename
					Path destFileName = Helpers.replaceBaseFolder(Paths.get(sourcePath), 
							                                      file.toPath(),
																  Paths.get(destPath));

					destFileName = Helpers.replaceExtension(destFileName, DST_EXTENSION);

					// DEBUG
//					System.out.println("> source filename = " + file);
//					System.out.println("> dest filename = " + destFileName);
//					System.out.println("> dest filename folder = " + destFileName.getParent());

					// create all folders for destination filename
					Path destFolder = destFileName.getParent();
					destFolder.toFile().mkdirs();

					// copy and convert file
					HtmlToJavaConverter conv = new HtmlToJavaConverter();
					conv.setSourceFile(file.toPath());
					conv.setDestFile(destFileName);
					conv.convert();
				}
			}
		}
		pathStream.close();
		System.out.println("DONE, converted " + filesCount + " files.");
		
	}
}
