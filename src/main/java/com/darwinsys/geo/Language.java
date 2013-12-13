package com.darwinsys.geo;

public enum Language {
	en("English"),
	cn_tw("Chinese(Cantonese)"),
	cn_zh("Chinese(Mandarin)"),
	cz("Czech"),
	de("German"),
	es("Spanish"),
	fr("French"),
	jp("Japanese"),
	kr("Korean"),
	pl("Polish"),
	ru("Russian");

	private final String name;

	private Language(String name) {
		this.name = name;
	}

	@Override public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		throw new IllegalArgumentException("enums are immutable");
	}

	public static Language valueOfIgnoreCase(String string) {
		for (Language l : values()) {
			if (l.getName().equalsIgnoreCase(string)) {
				return l;
			}
		}
		throw new IllegalArgumentException("Unknown language " + string);
	}
}
