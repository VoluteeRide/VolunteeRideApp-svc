package com.volunteeride.service.impl.ride;

import com.volunteeride.dao.CenterDAO;
import com.volunteeride.dao.RideDAO;
import com.volunteeride.dao.UserDAO;
import com.volunteeride.dto.UserTypeRideStateKey;
import com.volunteeride.dto.UserTypeRideStateOperationKey;
import com.volunteeride.exception.BaseVolunteerideRuntimeException;
import com.volunteeride.exception.RecordNotFoundException;
import com.volunteeride.exception.ValidationException;
import com.volunteeride.model.Ride;
import com.volunteeride.model.RideOperationEnum;
import com.volunteeride.model.RideStatusEnum;
import com.volunteeride.model.UserTypeEnum;
import com.volunteeride.service.ride.RideService;
import com.volunteeride.util.exception.ValidationExceptionUtil;
import com.volunteeride.util.statetransition.RideStateTransition;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.Arrays;
import java.util.List;

import static com.volunteeride.common.constants.VolunteerideApplicationConstants.ExceptionArgumentConstants.CENTER_EXCP_ARG_KEY;
import static com.volunteeride.common.constants.VolunteerideApplicationConstants.ExceptionArgumentConstants.CENTER_ID_EXCP_ARG_KEY;
import static com.volunteeride.common.constants.VolunteerideApplicationConstants.ExceptionArgumentConstants.RIDE_DROPOFF_LOC_EXCP_ARG_KEY;
import static com.volunteeride.common.constants.VolunteerideApplicationConstants.ExceptionArgumentConstants.RIDE_EXCP_ARG_KEY;
import static com.volunteeride.common.constants.VolunteerideApplicationConstants.ExceptionArgumentConstants.RIDE_PICKUP_LOC_EXCP_ARG_KEY;
import static com.volunteeride.common.constants.VolunteerideApplicationConstants.ExceptionArgumentConstants.RIDE_PICKUP_TIME_EXCP_ARG_KEY;
import static com.volunteeride.common.constants.VolunteerideApplicationConstants.ExceptionArgumentConstants.RIDE_SEEKERS_EXCP_ARG_KEY;
import static com.volunteeride.common.constants.VolunteerideApplicationConstants.ExceptionArgumentConstants.exceptionArgumentBundle;
import static com.volunteeride.common.constants.VolunteerideApplicationConstants.ExceptionResourceConstants.RECORD_NOT_FOUND_EXCEPTION_KEY;
import static com.volunteeride.common.constants.VolunteerideApplicationConstants.ExceptionResourceConstants.RIDE_PICK_UP_TIME_VALIDATION_EXCEPTION_KEY;
import static com.volunteeride.model.RideOperationEnum.CANCEL;
import static com.volunteeride.model.RideStatusEnum.REQUESTED;
import static com.volunteeride.model.UserTypeEnum.RIDE_SEEKER;
import static com.volunteeride.model.UserTypeEnum.VOLUNTEER;
import static com.volunteeride.util.statetransition.RideStateTransition.userRideOperationsMap;

/**
 * Created by ayazlakdawala on 8/31/15.
 */
@Named
public class RideServiceImpl implements RideService {

    @Inject
    private RideDAO rideDAO;

    @Inject
    private CenterDAO centerDAO;

    @Inject
    private UserDAO userDAO;

    @Override
    public Ride requestRide(Ride ride) {

        this.validateRideForSaveOperation(ride);
        ride.setStatus(REQUESTED);
        return rideDAO.save(ride);
    }

    /**
     * Api to execute ride operation.
     *
     * @param rideId
     * @param rideOperation
     * @return
     */
    //TODO Ayaz resolve retrieving user in session
    //TODO Ayaz Write Test
    @Override
    public Ride executeRideOperation(String centerId, String rideId, RideOperationEnum rideOperation) {

        String loggedInUserId = "";

        List<UserTypeEnum> loggedInUserTypes = Arrays.asList(RIDE_SEEKER, VOLUNTEER);

        Ride retrievedRide = rideDAO.findByCenterIdAndId(centerId, rideId);

        if(null == retrievedRide){
            throw new RecordNotFoundException(RECORD_NOT_FOUND_EXCEPTION_KEY,
                    new Object[]{exceptionArgumentBundle.getString(RIDE_EXCP_ARG_KEY), rideId});
        }

        UserTypeEnum userTypeToUse = null;

        if(retrievedRide.getRideSeekerIds().contains(loggedInUserId) && loggedInUserTypes.contains(RIDE_SEEKER)){

            userTypeToUse = RIDE_SEEKER;

        } else if((retrievedRide.getVolunteerId() != null &&
                retrievedRide.getVolunteerId().equals(loggedInUserId) &&
                loggedInUserTypes.contains(VOLUNTEER))){

            userTypeToUse = VOLUNTEER;

        } else if(retrievedRide.getVolunteerId() == null &&
                retrievedRide.getStatus().name().equals(REQUESTED) &&
                loggedInUserTypes.contains(VOLUNTEER)) {

            userTypeToUse = VOLUNTEER;
            retrievedRide.setVolunteerId(loggedInUserId);

        } else {
            //TODO AYAZ Throw Access Denied Forbidden 403 exception
        }

        List<RideOperationEnum> validRideOperationsForLoggedInUser = userRideOperationsMap
                .get(new UserTypeRideStateKey(userTypeToUse, retrievedRide.getStatus()));


        if(!validRideOperationsForLoggedInUser.contains(rideOperation)){
            //TODO AYAZ Throw Access Denied Forbidden 403 exception
        }

        RideStatusEnum rideTransitionedState = RideStateTransition.transitionedRideStateMap
                .get(new UserTypeRideStateOperationKey(userTypeToUse, retrievedRide.getStatus(), rideOperation));

        if(rideTransitionedState == null){
            //TODO AYAZ Throw Access Denied Forbidden 403 exception

        }

        retrievedRide.setStatus(rideTransitionedState);

        List<RideOperationEnum> nextRideOperations = userRideOperationsMap
                .get(new UserTypeRideStateKey(userTypeToUse, retrievedRide.getStatus()));

        retrievedRide.setNextRideUserOperations(nextRideOperations);

        return retrievedRide;
    }

    /**
     * Api to retrieve Ride details
     *
     * @param centerId
     * @param rideId
     * @return
     */
    //TODO Ayaz resolve retrieving user in session
    //TODO Ayaz Write Test
    @Override
    public Ride retrieveRideDetails(String centerId, String rideId) {

        String loggedInUserId = "";

        List<UserTypeEnum> loggedInUserTypes = Arrays.asList(RIDE_SEEKER, VOLUNTEER);

        Ride retrievedRide = rideDAO.findByCenterIdAndId(centerId, rideId);

        if(null == retrievedRide){
            throw new RecordNotFoundException(RECORD_NOT_FOUND_EXCEPTION_KEY,
                    new Object[]{exceptionArgumentBundle.getString(RIDE_EXCP_ARG_KEY), rideId});
        }

        UserTypeEnum userTypeToUse = null;

        if(retrievedRide.getRideSeekerIds().contains(loggedInUserId) && loggedInUserTypes.contains(RIDE_SEEKER)){

            userTypeToUse = RIDE_SEEKER;

        } else if((retrievedRide.getVolunteerId() != null &&
                retrievedRide.getVolunteerId().equals(loggedInUserId) &&
                loggedInUserTypes.contains(VOLUNTEER)) ||
                loggedInUserTypes.contains(VOLUNTEER)){

            userTypeToUse = VOLUNTEER;

        } else {
            //TODO AYAZ Throw Access Denied Forbidden 403 exception
        }

        List<RideOperationEnum> nextRideOperations = userRideOperationsMap
                .get(new UserTypeRideStateKey(userTypeToUse, retrievedRide.getStatus()));

        retrievedRide.setNextRideUserOperations(nextRideOperations);

        return retrievedRide;
    }

    private void validateRideForSaveOperation(Ride ride) {
        //validate required data
        ValidationExceptionUtil.validateForEmptyOrNull(ride, new Object[]{RIDE_EXCP_ARG_KEY});

        //validate center
        ValidationExceptionUtil.validateForEmptyOrNull(ride.getCenterId(), new Object[]{CENTER_ID_EXCP_ARG_KEY});

        if(!centerDAO.exists(ride.getCenterId())){
            throw new RecordNotFoundException(RECORD_NOT_FOUND_EXCEPTION_KEY,
                    new Object[]{exceptionArgumentBundle.getString(CENTER_EXCP_ARG_KEY), ride.getCenterId()});
        }

        ValidationExceptionUtil.validateForEmptyOrNull(ride.getPickupLoc(), new Object[]{RIDE_PICKUP_LOC_EXCP_ARG_KEY});
        ValidationExceptionUtil.validateForEmptyOrNull(ride.getDropoffLoc(), new Object[]{RIDE_DROPOFF_LOC_EXCP_ARG_KEY});

        //validate pick up time
        ValidationExceptionUtil.validateForEmptyOrNull(ride.getPickupTime(), new Object[]{RIDE_PICKUP_TIME_EXCP_ARG_KEY});

        if(ride.getPickupTime().isBeforeNow() || (ride.getPickupTime().getDayOfYear() > DateTime.now().plusDays(1).getDayOfYear())){
            throw new ValidationException(RIDE_PICK_UP_TIME_VALIDATION_EXCEPTION_KEY);
        }

        //validate Ride Seekers
        ValidationExceptionUtil.validateForEmptyOrNull(ride.getRideSeekerIds(), new Object[]{RIDE_SEEKERS_EXCP_ARG_KEY});

        StringBuilder invalidRideSeekers = null;

        for(String rideSeekerId : ride.getRideSeekerIds()){

            if(!userDAO.exists(rideSeekerId)){

                if(null == invalidRideSeekers){
                    invalidRideSeekers = new StringBuilder();
                }
                invalidRideSeekers.append(rideSeekerId);
                invalidRideSeekers.append(" ");
            }
        }

        if(null != invalidRideSeekers){
            throw new RecordNotFoundException(RECORD_NOT_FOUND_EXCEPTION_KEY,
                    new Object[]{exceptionArgumentBundle.getString(RIDE_SEEKERS_EXCP_ARG_KEY),
                            invalidRideSeekers.toString().trim()});
        }
    }




}
