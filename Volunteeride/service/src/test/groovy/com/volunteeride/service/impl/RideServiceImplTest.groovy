package com.volunteeride.service.impl

import com.volunteeride.common.BaseUnitTest
import com.volunteeride.dao.CenterDAO
import com.volunteeride.dao.RideDAO
import com.volunteeride.dao.UserDAO
import com.volunteeride.exception.RecordNotFoundException
import com.volunteeride.exception.ValidationException
import com.volunteeride.model.Location
import com.volunteeride.model.Ride
import com.volunteeride.model.RideStatusEnum
import com.volunteeride.service.impl.RideServiceImpl
import org.joda.time.DateTime

/**
 * Created by ayazlakdawala on 9/1/15.
 */
class RideServiceImplTest extends BaseUnitTest  {

    def ride
    def rideService = new RideServiceImpl()

    // create a mocked ride dao object
    def mockedRideDAO = Mock(RideDAO)

    // create a mocked Center dao object
    def mockedCenterDAO = Mock(CenterDAO)

    // create a mocked user dao object
    def mockedUserDAO = Mock(UserDAO)

    void setup() {

        //inject mock object in the service
        rideService.rideDAO = mockedRideDAO
        rideService.centerDAO = mockedCenterDAO
        rideService.userDAO = mockedUserDAO

        ride = new Ride();
        ride.centerId = "456"
        ride.pickupLoc = new Location()
        ride.dropoffLoc = new Location()
        ride.pickupTime = new DateTime().plusHours(2)
        def rideseekers = new ArrayList<String>()
        rideseekers << "123"
        ride.rideSeekerIds = rideseekers
    }

    void cleanup() {

    }

    def "validate ride object for Request Ride api "() {

        setup : "prepare ride object for test"
        def ride = null;

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        def excp = thrown(ValidationException)
        this.testForEmptyOrNull(excp, "Ride")
    }

    def "validate Center object for Request Ride api "(){

        setup: "set up ride object for test"
        ride.centerId = null;

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        def excp = thrown(ValidationException)
        this.testForEmptyOrNull(excp, "Center Id")
    }

    def "validate pick up location object for Request Ride api "(){

        setup: "set up ride object for test"
        ride.pickupLoc = null;

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        1* mockedCenterDAO.exists(_ as String) >> true
        def excp = thrown(ValidationException)
        this.testForEmptyOrNull(excp, "Ride Pick up Location")
    }

    def "validate drop off location object for Request Ride api "(){

        setup: "set up ride object for test"
        ride.dropoffLoc = null;

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        1* mockedCenterDAO.exists(_ as String) >> true
        def excp = thrown(ValidationException)
        this.testForEmptyOrNull(excp, "Ride Drop off Location")
    }

    def "validate pick up time object for Request Ride api "(){

        setup: "set up ride object for test"
        ride.pickupTime = null;

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        1* mockedCenterDAO.exists(_ as String) >> true
        def excp = thrown(ValidationException)
        this.testForEmptyOrNull(excp, "Ride Pick Up Time")
    }

    def "validate ride seekers for null for Request Ride api "(){

        setup: "set up ride object for test"
        ride.rideSeekerIds = null;

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        1* mockedCenterDAO.exists(_ as String) >> true
        def excp = thrown(ValidationException)
        this.testForEmptyOrNull(excp, "Ride Seekers")
    }

    def "validate ride seekers for empty list for Request Ride api "(){

        setup: "set up ride object for test"
        ride.rideSeekerIds = new ArrayList<String>();

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        1* mockedCenterDAO.exists(_ as String) >> true
        def excp = thrown(ValidationException)
        this.testForEmptyOrNull(excp, "Ride Seekers")
    }

    def "test request ride api"(){

        setup : "set up expected ride object"
        ride.id = "123"

        when: "function under test is executed"
        def  actualRide = rideService.requestRide(ride);

        then:
        1* mockedCenterDAO.exists(_ as String) >> true
        1* mockedUserDAO.exists(_ as String) >> true
        1 * mockedRideDAO.save(_ as Ride) >> ride
        actualRide.id == ride.id
        actualRide.status == RideStatusEnum.REQUESTED
    }

    def "validate center id exists for Request Ride api"(){

        setup : "set up expected ride object"
        ride.id = "123"

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        1* mockedCenterDAO.exists(_ as String) >> false
        def rnfe = thrown(RecordNotFoundException)
        this.testForRecordNotFoundException(rnfe, "Center", ride.getCenterId())
    }

    def "validate ride seeker ids exists for Request Ride api"(){

        setup : "set up expected ride object"
        ride.id = "123"

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        1* mockedCenterDAO.exists(_ as String) >> true
        1* mockedUserDAO.exists(_ as String) >> false
        def rnfe = thrown(RecordNotFoundException)
        this.testForRecordNotFoundException(rnfe, "Ride Seekers", ride.getRideSeekerIds().get(0))
    }

    def "validate ride pick up time before the current date time"(){
        setup : "set up expected ride object"
        ride.id = "123"
        ride.pickupTime = new DateTime().minusDays(1)


        and: "set up expected exception"
        def expectedMsg = "Invalid Ride Pick Up Time provided."
        def expectedErrCode = "VR1-03"
        def expectedCustomCause = "Ride Pick Up Time is either before current time or is 2 or more days in advance."
        def expectedResolution = "Please select a valid Ride Pick Up Time not more than one day in advance."

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        1* mockedCenterDAO.exists(_ as String) >> true

        def vExcp = thrown(ValidationException)
        this.assertExpectedException(vExcp, expectedMsg, expectedCustomCause, expectedResolution, expectedErrCode)
    }

    def "validate ride pick up time more than one day in advance"(){
        setup : "set up expected ride object"
        ride.id = "123"
        ride.pickupTime = new DateTime().plusDays(2)

        and: "set up expected exception"
        def expectedMsg = "Invalid Ride Pick Up Time provided."
        def expectedErrCode = "VR1-03"
        def expectedCustomCause = "Ride Pick Up Time is either before current time or is more than one day in advance."
        def expectedResolution = "Please select a valid Ride Pick Up Time not more than one day in advance."

        when: "function under test is executed"
        rideService.requestRide(ride);

        then:
        1* mockedCenterDAO.exists(_ as String) >> true

        def vExcp = thrown(ValidationException)
        this.assertExpectedException(vExcp, expectedMsg, expectedCustomCause, expectedResolution, expectedErrCode)
    }

}
