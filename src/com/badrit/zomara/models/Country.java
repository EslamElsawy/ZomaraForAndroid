package com.badrit.zomara.models;

public class Country {

	public String countryCode;
	public String countryName;

	public Country(String code, String name) {

		countryCode = code;
		countryName = name;
	}

	public String toString() {
		return countryName;
	}
}
