package com.maps.algorithms.model ;

import org.springframework.data.mongodb.core.mapping.Field;

public class GeoLocation {
	
        @Field("lng")
	private double longitude ;
	@Field("lat")
        private double latitude ;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
	
        
	
}
