package api.tests;

import api.endpoint.UserEndPoints;
import api.payload.User;
import api.utilities.DataProviders;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class DDTests {

    @Test(priority = 1, dataProvider = "Data", dataProviderClass = DataProviders.class)
    public void testPostUser(Map<String, String> userData) {
        User userPayload = new User();
        userPayload.setId(Integer.parseInt(userData.get("UserID")));
        userPayload.setUsername(userData.get("UserName"));
        userPayload.setFirstName(userData.get("FirstName"));
        userPayload.setLastName(userData.get("LastName"));
        userPayload.setEmail(userData.get("Email"));
        userPayload.setPassword(userData.get("Password"));
        userPayload.setPhone(userData.get("Phone"));

        Response response = UserEndPoints.createUser(userPayload);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2, dataProvider = "Data", dataProviderClass = DataProviders.class)
    public void testGetUserWithFieldAndSchemaValidation(Map<String, String> userData) {
        String expectedUsername = userData.get("UserName");
        Response response = UserEndPoints.readUser(expectedUsername);

        // 1. Status Code & Header Validation
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getHeader("Content-Type"), "application/json");

        // 2. JSON Schema Validation (Contract Testing)
        response.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("user-schema.json"));

        // 3. Field-Level Validation using JsonPath
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getInt("id"), Integer.parseInt(userData.get("UserID")));
        Assert.assertEquals(jsonPath.getString("username"), userData.get("UserName"));
        Assert.assertEquals(jsonPath.getString("firstName"), userData.get("FirstName"));
        Assert.assertEquals(jsonPath.getString("lastName"), userData.get("LastName"));
        Assert.assertEquals(jsonPath.getString("email"), userData.get("Email"));
        Assert.assertEquals(jsonPath.getString("phone"), userData.get("Phone"));

        // 4. POJO Deserialization Validation
        User actualUser = response.as(User.class);
        Assert.assertEquals(actualUser.getUsername(), userData.get("UserName"));
        Assert.assertEquals(actualUser.getEmail(), userData.get("Email"));
    }

    @Test(priority = 3, dataProvider = "UserNames", dataProviderClass = DataProviders.class)
    public void deleteUserByName(String userName) {
        Response response = UserEndPoints.deleteUser(userName);
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
