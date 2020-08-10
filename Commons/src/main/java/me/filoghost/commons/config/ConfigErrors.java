package me.filoghost.commons.config;

import me.filoghost.commons.config.mapped.MappedConfig;
import me.filoghost.commons.config.mapped.MappedField;
import me.filoghost.commons.config.mapped.converter.Converter;

import java.nio.file.Path;

public class ConfigErrors {

	public static final String readIOException = "I/O exception while reading file";
	public static final String createDefaultIOException = "I/O exception while creating default file";
	public static final String writeDataIOException = "I/O exception while writing data to file";
	public static final String invalidYamlSyntax = "invalid YAML syntax";

	public static final String valueNotSet = "value is not set";
	public static final String valueNotList = "value is not a list";
	public static final String valueNotBoolean = "value is not a boolean";
	public static final String valueNotNumber = "value is not a number";
	public static final String valueNotString = "value is not a string";
	public static final String valueNotSection = "value is not a configuration section";

	public static String createParentFolderIOException(Path rootDataFolder, Path folder) {
		return "I/O exception while creating parent directory \"" + formatPath(rootDataFolder, folder) + "\"";
	}

	public static String mapperInitError(MappedConfig mappedConfig) {
		return "couldn't initialize config mapper for class \"" + mappedConfig.getClass() + "\"";
	}

	public static String fieldReadError(MappedConfig mappedConfig) {
		return "couldn't read field values from class \"" + mappedConfig.getClass() + "\"";
	}

	public static String fieldInjectError(MappedConfig mappedConfig) {
		return "couldn't inject fields values in class \"" + mappedConfig.getClass() + "\"";
	}

	public static String mapperFieldCannotBeNull(MappedField mappedField) {
		return "mapped field \"" + mappedField.getFieldName() + "\" cannot be null by default";
	}

	public static String converterFailed(Object value, Converter converter) {
		return "value of type \"" + value.getClass() + "\" couldn't be converted by \"" + converter.getClass() + "\"";
	}

	private static String formatPath(Path rootDataFolder, Path path) {
		if (path.startsWith(rootDataFolder)) {
			// Remove root data folder prefix
			return path.subpath(rootDataFolder.getNameCount(), path.getNameCount()).toString();
		} else {
			return path.toString();
		}
	}

}
