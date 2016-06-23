package helpers;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Helpers {

	private final static String FILEEXT_REGEXP = "\\..+$";
	
	/**
	 * Заменить расширение указанного файла на новое
	 * @param fileName Имя файла, в котором будет меняться расширение
	 * @param newExtension Новое расширение
	 * @return Имя файла с измененным расширением
	 */
	public static Path replaceExtension(Path fileName, String newExtension) {

		return Paths.get(fileName.toString().replaceFirst(FILEEXT_REGEXP, newExtension));
	}
	
	/**
	 * Заменить в имени файла базовый путь на новый путь
	 * @param baseFolder Начальная папка
	 * @param sourceFile Путь к исходному файлу (включает начальную папку)
	 * @param newFolder Конечная папка
	 * @return Путь к файлу, перенесенный из начальной папки в конечную папку
	 */
	public static Path replaceBaseFolder(Path baseFolder, Path sourceFile, Path newFolder) {
		
		// Выделить локальный путь к файлу (исключая начальную папку)
		Path local = baseFolder.relativize(sourceFile);

		// Добавить локальный путь к файлу к конечной папке
		return Paths.get(newFolder.toString(), local.toString());
	}
}
