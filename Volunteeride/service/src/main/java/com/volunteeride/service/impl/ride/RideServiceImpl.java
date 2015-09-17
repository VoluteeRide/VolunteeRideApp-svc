package com.volunteeride.service.impl.ride;

import com.volunteeride.dao.RideDAO;
import com.volunteeride.model.Ride;
import com.volunteeride.model.RideStatusEnum;
import com.volunteeride.service.ride.RideService;
import com.volunteeride.util.exception.ValidationExceptionUtil;

/**
 * Created by ayazlakdawala on 8/31/15.
 */
public class RideServiceImpl implements RideService {

    private RideDAO rideDAO;

    @Override
    public Ride requestRide(Ride ride) {

        //validate required data
        ValidationExceptionUtil.validateForEmptyOrNull(ride, "Ride");
        ValidationExceptionUtil.validateForEmptyOrNull(ride.getCenter(), "Center");
        ValidationExceptionUtil.validateForEmptyOrNull(ride.getPickupLoc(), "Ride Pick up Location");
        ValidationExceptionUtil.validateForEmptyOrNull(ride.getDropoffLoc(), "Ride Drop off Location");
        ValidationExceptionUtil.validateForEmptyOrNull(ride.getPickupTime(), "Ride Pick Up Time");
        ValidationExceptionUtil.validateForEmptyOrNull(ride.getRideSeekers(), "Ride Seekers");

        ride.setStatus(RideStatusEnum.REQUESTED);

        return (Ride) rideDAO.save(ride);
    }
}
