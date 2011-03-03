package com.darwinsys.geo;

/**
 * US States provinces
 */
public enum USState {
	AL("ALABAMA"),
	AK("ALASKA"),
	AS("AMERICAN SAMOA"),
	AZ("ARIZONA"),
	AR("ARKANSAS"),
	CA("CALIFORNIA"),
	CO("COLORADO"),
	CT("CONNECTICUT"),
	DE("DELAWARE"),
	DC("DISTRICT OF COLUMBIA"),
	FM("FEDERATED STATES OF MICRONESIA"),
	FL("FLORIDA"),
	GA("GEORGIA"),
	GU("GUAM"),
	HI("HAWAII"),
	ID("IDAHO"),
	IL("ILLINOIS"),
	IN("INDIANA"),
	IA("IOWA"),
	KS("KANSAS"),
	KY("KENTUCKY"),
	LA("LOUISIANA"),
	ME("MAINE"),
	MH("MARSHALL ISLANDS"),
	MD("MARYLAND"),
	MA("MASSACHUSETTS"),
	MI("MICHIGAN"),
	MN("MINNESOTA"),
	MS("MISSISSIPPI"),
	MO("MISSOURI"),
	MT("MONTANA"),
	NE("NEBRASKA"),
	NV("NEVADA"),
	NH("NEW HAMPSHIRE"),
	NJ("NEW JERSEY"),
	NM("NEW MEXICO"),
	NY("NEW YORK"),
	NC("NORTH CAROLINA"),
	ND("NORTH DAKOTA"),
	MP("NORTHERN MARIANA ISLANDS"),
	OH("OHIO"),
	OK("OKLAHOMA"),
	OR("OREGON"),
	PW("PALAU"),
	PA("PENNSYLVANIA"),
	PR("PUERTO RICO"),
	RI("RHODE ISLAND"),
	SC("SOUTH CAROLINA"),
	SD("SOUTH DAKOTA"),
	TN("TENNESSEE"),
	TX("TEXAS"),
	UT("UTAH"),
	VT("VERMONT"),
	VI("VIRGIN ISLANDS"),
	VA("VIRGINIA"),
	WA("WASHINGTON"),
	WV("WEST VIRGINIA"),
	WI("WISCONSIN"),
	WY("WYOMING"),
	AA("Armed Forces Americas"),
	AP("Armed Forces Pacific"),
	AE("Armed Forces - Other"),
	;
	
	private final String longName;

	private USState(String longName) {
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
