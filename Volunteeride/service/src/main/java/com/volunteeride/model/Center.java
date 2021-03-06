package com.volunteeride.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Model class representing religious center
 *
 * Created by ayazlakdawala on 9/1/15.
 */
@Document
public class Center extends BaseModelObject {

    private String name;
    private Location location;
    private List<Location> pickUpLocations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Location> getPickUpLocations() {
        return pickUpLocations;
    }

    public void setPickUpLocations(List<Location> pickUpLocations) {
        this.pickUpLocations = pickUpLocations;
    }
}
