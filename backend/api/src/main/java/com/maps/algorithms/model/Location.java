package com.maps.algorithms.model ;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Repository;

@Document(collection="Locations")
public class Location<T extends GeoLocation> {

        @Id
        private String id ;
	private String name ;
	private String details ;
        @Field("geolocation")
	private T geoLocation ;
        private String address ;
	
	public GeoLocation getGeoLocation() {
		return geoLocation;
	}
	public void setGeoLocation(T geoLocation) {
		this.geoLocation = geoLocation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
//        @Override
//        public String toString()
//        {
//            return ("\nName="+this.name+"\n Details="+this.details+
//                    "\n Geolocation: latitude="+this.geoLocation.getLatitude()+
//                    "\n Geolocation: longitude="+this.geoLocation.getLongitude()+
//                    "\n address"+this.address) ;
//                    
//        }
	
}
