package com.darwinsys.geo;

public enum Language {
	en("English"),
	cn_tw("Chinese(Cantonese)"),
	cn_zh("Chinese(Mandarin)"),
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
}
