package api.tests;

import api.endpoint.UserEndPoints;
import api.payload.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class DDTests {

    public Logger logger;

    @BeforeClass
    public void setupLogger() {
        logger = LogManager.getLogger(this.getClass());
    }

    @Test(priority = 1, dataProvider = "Data",dataProviderClass = DataProviders.class)
    public void testPostUser(Map<String, String> userData) {
        User userPayload = new User();

        logger.info("********** Creating user: {} **********", userData.get("UserName"));

        userPayload.setId(Integer.parseInt(userData.get("UserID")));
        userPayload.setUsername(userData.get("UserName"));
        userPayload.setFirstName(userData.get("FirstName"));
        userPayload.setLastName(userData.get("LastName"));
        userPayload.setEmail(userData.get("Email"));
        userPayload.setPassword(userData.get("Password"));
        userPayload.setPhone(userData.get("Phone"));

        Response response = UserEndPoints.createUser(userPayload);
        Assert.assertEquals(response.getStatusCode(), 200);

        logger.info("********** User {} created successfully **********", userData.get("UserName"));
    }

    @Test(priority = 2, dataProvider = "UserNames", dataProviderClass = DataProviders.class)
    public void deleteUserByName(String UserNames)
    {
        logger.info("********** Deleting user: {} **********", UserNames);
        Response response = UserEndPoints.deleteUser(UserNames);
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("********** User {} deleted successfully **********", UserNames);
    }
}
