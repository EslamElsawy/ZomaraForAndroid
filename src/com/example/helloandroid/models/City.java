package com.example.helloandroid.models;

public class City {

	public String cityId;
	public String cityName;
	public String cityCountry;
	public String cityTimeZone;
	public String cityLatitude;
	public String cityLongitude;

	public City(String cityId, String cityName, String cityCountry, String cityTimeZone, String cityLatitude,
			String cityLongitude) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.cityCountry = cityCountry;
		this.cityTimeZone = cityTimeZone;
		this.cityLatitude = cityLatitude;
		this.cityLongitude = cityLongitude;
	}

	public String toString() {
		return cityName ;
	}

	public String getDetails() {
		return cityId + "\t" + cityName + "\t" + cityCountry + "\t" + cityTimeZone + "\t" + cityLatitude + "\t"
				+ cityLongitude;
	}
}
