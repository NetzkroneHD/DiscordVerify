package de.netzkronehd.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface Configuration extends ConfigurationSection {

	void addDefault(@NotNull String path, @Nullable Object value);

	void addDefaults(@NotNull Map<String, Object> defaults);

	void addDefaults(@NotNull Configuration defaults);

	void setDefaults(@NotNull Configuration defaults);

	@Nullable Configuration getDefaults();

	@NotNull ConfigurationOptions options();
}
