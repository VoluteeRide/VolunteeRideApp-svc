package com.volunteeride.common.constants;

import java.util.ResourceBundle;

/**
 * Created by ayazlakdawala on 9/26/15.
 */
public class VolunteerideApplicationConstants {

    public static class ExceptionKeyConstants {

        public static final String CAUSE_KEY_SUFFIX = ".cause";

        public static final String RESOLUTION_KEY_SUFFIX = ".resolution";

        public static final String ERROR_CODE_KEY_SUFFIX = ".errorcode";
    }

    public static class ExceptionResourceConstants {

        public static ResourceBundle exceptionResourceBundle = ResourceBundle.getBundle("exception-resource");

        public static final String REQUIRED_DATA_MISSING_EXCEPTION_KEY = "required.data.missing.exception";

        public static final String RECORD_NOT_FOUND_EXCEPTION_KEY = "record.not.found.exception";

        public static final String RIDE_PICK_UP_TIME_VALIDATION_EXCEPTION_KEY = "ride.pick.up.time.validation.exception";

    }

    public static class ExceptionArgumentConstants {

        public static ResourceBundle exceptionArgumentBundle = ResourceBundle.getBundle("exception-arguments");

        //Ride Specific Exception Arguments
        public static final String RIDE_EXCP_ARG_KEY = "ride";
        public static final String RIDE_PICKUP_LOC_EXCP_ARG_KEY = "ride.pickup.loc";
        public static final String RIDE_DROPOFF_LOC_EXCP_ARG_KEY = "ride.dropoff.loc";
        public static final String RIDE_PICKUP_TIME_EXCP_ARG_KEY = "ride.pickup.time";
        public static final String RIDE_SEEKERS_EXCP_ARG_KEY = "ride.seekers";

        //Center Specific
        public static final String CENTER_ID_EXCP_ARG_KEY = "center.id";
        public static final String CENTER_EXCP_ARG_KEY = "center";

    }






}