/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.config.framework.mapped;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.config.framework.ConfigSection;
import me.filoghost.chestcommands.config.framework.ConfigValue;
import me.filoghost.chestcommands.config.framework.ConfigValueType;
import me.filoghost.chestcommands.config.framework.exception.ConfigLoadException;
import me.filoghost.chestcommands.config.framework.exception.ConverterCastException;
import me.filoghost.chestcommands.config.framework.mapped.converter.BooleanConverter;
import me.filoghost.chestcommands.config.framework.mapped.converter.Converter;
import me.filoghost.chestcommands.config.framework.mapped.converter.DoubleConverter;
import me.filoghost.chestcommands.config.framework.mapped.converter.IntegerConverter;
import me.filoghost.chestcommands.config.framework.mapped.converter.ListConverter;
import me.filoghost.chestcommands.config.framework.mapped.converter.StringConverter;
import me.filoghost.chestcommands.config.framework.mapped.modifier.ChatColorsModifier;
import me.filoghost.chestcommands.config.framework.mapped.modifier.ValueModifier;
import me.filoghost.chestcommands.logging.ErrorMessages;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ConfigMapper {

	private static final List<Converter> CONVERTERS = ImmutableList.of(
			new DoubleConverter(),
			new IntegerConverter(),
			new BooleanConverter(),
			new StringConverter(),
			new ListConverter()
	);

	private static final List<ValueModifier<?, ?>> VALUE_MODIFIERS = ImmutableList.of(
			new ChatColorsModifier()
	);

	private final MappedConfig mappedObject;
	private final ConfigSection config;
	private final List<MappedField> mappedFields;

	public ConfigMapper(MappedConfig mappedObject, ConfigSection config) throws ReflectiveOperationException {
		this.mappedObject = mappedObject;
		this.config = config;
		this.mappedFields = getMappableFields(mappedObject.getClass());
	}

	private List<MappedField> getMappableFields(Class<?> type) throws ReflectiveOperationException {
		Field[] declaredFields;
		List<MappedField> mappedFields = new ArrayList<>();

		try {
			declaredFields = type.getDeclaredFields();
			for (Field field : declaredFields) {
				if (isMappable(field)) {
					mappedFields.add(new MappedField(field));
				}
			}

		} catch (Throwable t) {
			throw new ReflectiveOperationException(t);
		}

		return mappedFields;
	}

	private boolean isMappable(Field field) {
		int modifiers = field.getModifiers();
		boolean includeStatic = field.isAnnotationPresent(IncludeStatic.class)
				|| field.getDeclaringClass().isAnnotationPresent(IncludeStatic.class);

		return (!Modifier.isStatic(modifiers) || includeStatic)
				|| !Modifier.isTransient(modifiers)
				|| !Modifier.isFinal(modifiers);
	}

	public Map<MappedField, Object> getFieldValues() throws ReflectiveOperationException {
		Map<MappedField, Object> mappedFieldValues = new HashMap<>();

		for (MappedField mappedField : mappedFields) {
			Object defaultValue = mappedField.getFromObject(mappedObject);

			if (defaultValue == null) {
				throw new ReflectiveOperationException(ErrorMessages.Config.mapperFieldCannotBeNull(mappedField));
			}

			mappedFieldValues.put(mappedField, defaultValue);
		}

		return mappedFieldValues;
	}

	public Map<String, ConfigValue> toConfigValues(Map<MappedField, Object> fieldValues) throws ConfigLoadException {
		Map<String, ConfigValue> configValues = new HashMap<>();

		for (Entry<MappedField, Object> entry : fieldValues.entrySet()) {
			MappedField mappedField = entry.getKey();
			Object defaultValue = entry.getValue();

			Converter converter = findConverter(mappedField.getFieldType());

			try {
				configValues.put(mappedField.getConfigPath(), converter.toConfigValue(mappedField, defaultValue));
			} catch (ConverterCastException e) {
				throw new ConfigLoadException("error while converting field \"" + mappedField.getFieldName() + "\"", e);
			}
		}

		return configValues;
	}

	public boolean addMissingConfigValues(Map<String, ConfigValue> defaultValues) {
		boolean modified = false;

		for (Entry<String, ConfigValue> entry : defaultValues.entrySet()) {
			String path = entry.getKey();
			ConfigValue defaultValue = entry.getValue();

			if (!config.contains(path)) {
				modified = true;
				config.set(path, defaultValue);
			}
		}

		return modified;
	}

	public void injectObjectFields() throws ReflectiveOperationException {
		for (MappedField mappedField : mappedFields) {
			injectObjectField(mappedField);
		}
	}

	private void injectObjectField(MappedField mappedField) throws ReflectiveOperationException {
		Type[] genericTypes = mappedField.getGenericTypes();
		Converter converter = findConverter(mappedField.getFieldType());

		ConfigValueType<?> configValueType = converter.getConfigValueType(genericTypes);
		Object fieldValue = config.get(mappedField.getConfigPath(), configValueType);

		for (Annotation annotation : mappedField.getAnnotations()) {
			fieldValue = applyValueModifiers(fieldValue, annotation);
		}

		mappedField.setToObject(mappedObject, fieldValue);
	}

	private Object applyValueModifiers(Object fieldValue, Annotation annotation) {
		for (ValueModifier<?, ?> modifier : VALUE_MODIFIERS) {
			if (modifier.isApplicable(annotation, fieldValue)) {
				fieldValue = modifier.transform(annotation, fieldValue);
			}
		}
		return fieldValue;
	}

	private Converter findConverter(Class<?> type) {
		return CONVERTERS.stream()
				.filter(converter -> converter.matches(type))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("cannot find converter for type " + type));
	}

}
