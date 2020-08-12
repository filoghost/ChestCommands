/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.commons.config;

import me.filoghost.commons.Preconditions;
import org.bukkit.configuration.file.YamlConfiguration;

import java.nio.file.Path;

public class Config extends ConfigSection {

	private final YamlConfiguration yaml;
	private final Path sourceFile;

	public Config(Path sourceFile) {
		this(new YamlConfiguration(), sourceFile);
	}

	public Config(YamlConfiguration yaml, Path sourceFile) {
		super(yaml);
		Preconditions.notNull(sourceFile, "sourceFile");
		this.yaml = yaml;
		this.sourceFile = sourceFile;
	}

	public Path getSourceFile() {
		return sourceFile;
	}

	public String saveToString() {
		return yaml.saveToString();
	}

	public void setHeader(String value) {
		yaml.options().header(value);
	}

}
