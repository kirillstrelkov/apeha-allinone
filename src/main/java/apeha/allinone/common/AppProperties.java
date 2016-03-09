package apeha.allinone.common;

import java.io.*;
import java.util.Properties;

public class AppProperties {
	static String KEY_DEFAULT_FILE = "default.file";
	static String KEY_DEFAULT_SPELL = "default.spell";
	static String KEY_LOAD_AT_START = "load.at.start";
	private static String DEFAULT_FILE_NAME = ".apeha.app.properties";
	static String DEFAULT_FILE = System.getProperty("user.home")
			+ File.separator + DEFAULT_FILE_NAME;
	Properties properties = null;

	private AppProperties() {
		Properties properties = new Properties();
		File file = new File(DEFAULT_FILE);
		if (file.exists()) {
			try {
				properties.load(new InputStreamReader(
						new FileInputStream(file), "UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.properties = properties;
	}

	public static AppProperties newAppProperties() {
		return new AppProperties();
	}

	public String getDefaultFile() {
		String property = properties.getProperty(KEY_DEFAULT_FILE);
		return property;
	}

	public void setDefaultFile(String value) {
		this.properties.setProperty(KEY_DEFAULT_FILE, value);
	}

	public String getDefaultSpell() {
		return this.properties.getProperty(KEY_DEFAULT_SPELL);
	}

	public void setDefaultSpell(String value) {
		this.properties.setProperty(KEY_DEFAULT_SPELL, value);
	}

	public boolean getLoadAtStart() {
		boolean load = Boolean.parseBoolean(properties
				.getProperty(KEY_LOAD_AT_START));
		return load;
	}

	public void setLoadAtStart(boolean value) {
		this.properties.setProperty(KEY_LOAD_AT_START, String.valueOf(value));
	}

	public void save() {
		try {
			this.properties.store(new OutputStreamWriter(new FileOutputStream(
					DEFAULT_FILE), "UTF-8"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
