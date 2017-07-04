package backend;

import backend.steps.IssueApi;
import backend.utils.JiraJsonFixture;
import com.jayway.restassured.RestAssured;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.TestCase;

import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

public class SimpleTest {

    String sessionId = "";

    @BeforeTest(groups = "backend")
    public void login() {
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
        JiraJsonFixture jiraJsonFixture = new JiraJsonFixture();
        String loginJson = jiraJsonFixture.generateJSONForLogin();


        sessionId = given().
                header("Content-Type", "application/json").
                body(loginJson).
                when().
                post("/rest/auth/1/session").
                then().
                extract().
                path("session.value");

    }

    @TestCase(id = "1")
    @Test(groups = {"functional", "backend"})
    public void subTaskCRUD() throws InterruptedException {

        String issueKey;
        RestAssured.baseURI = "http://soft.it-hillel.com.ua:8080";
        JiraJsonFixture jiraJsonFixture = new JiraJsonFixture();
        String issueJson = jiraJsonFixture.generateJSONForIssue();

        IssueApi issueApi = new IssueApi();
        issueApi.createIssue(issueJson);

        issueKey = issueApi.requestSender.response.then().extract().path("key");
        String response = issueApi.getIssue(issueKey).asString();
        assertTrue(response.contains(issueKey));


        issueApi.deleteIssue(issueKey );
        String responsed = issueApi.getIssue(issueKey).asString();
        assertTrue(responsed.contains(issueKey));


    }

//        issueKey = given().
//                header("Content-Type", "application/json").
//                header("Cookie", "JSESSIONID=" + sessionId).
//                body(issueJson).
//                when().
//                post("/rest/api/2/issue").
//                then().
//                extract().
//                path("key");
//

//        given().
//                header("Content-Type", "application/json").
//                header("Cookie", "JSESSIONID=" + sessionId).
//                when().
//                delete("/rest/api/2/issue/" + issueKey).
//                then();

}