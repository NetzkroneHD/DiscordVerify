package de.netzkronehd.configuration.file;

import de.netzkronehd.configuration.MemoryConfiguration;
import de.netzkronehd.configuration.MemoryConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileConfigurationOptions extends MemoryConfigurationOptions {
    private String header = null;
    private boolean copyHeader = true;

    protected FileConfigurationOptions(@NotNull MemoryConfiguration configuration) {
        super(configuration);
    }

    @NotNull
    @Override
    public FileConfiguration configuration() {
        return (FileConfiguration) super.configuration();
    }

    @NotNull
    @Override
    public FileConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @NotNull
    @Override
    public FileConfigurationOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }


    @Nullable
    public String header() {
        return header;
    }


    @NotNull
    public FileConfigurationOptions header(@Nullable String value) {
        this.header = value;
        return this;
    }

    public boolean copyHeader() {
        return copyHeader;
    }

    @NotNull
    public FileConfigurationOptions copyHeader(boolean value) {
        copyHeader = value;

        return this;
    }
}
