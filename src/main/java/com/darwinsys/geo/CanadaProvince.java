package com.darwinsys.geo;

/**
 * Canadian provinces
 */
public enum CanadaProvince {
	AB("Alberta"),
	BC("British Columbia"),
	MB("Manitoba"),
	NB("New Brunswick"),
	NL("Newfoundland & Labrador"),
	NS("Nova Scotia"),
	NV("Nunavut"),
	NW("North West Territories"),
	ON("Ontario"),
	PE("Prince Edward Island"),
	QC("Quebec"),
	SK("Saskatchewan"),
	YK("Yukon Territories"),
	;
	
	private final String longName;

	private CanadaProvince(String longName) {
		this.longName = longName;
	}

	public String getLongName() {
		return longName;
	}
	
	@Override
	public String toString () {
		return this.getLongName();
	}
}
