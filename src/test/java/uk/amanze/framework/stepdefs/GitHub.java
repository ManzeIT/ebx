package uk.amanze.framework.stepdefs;

import com.google.gson.*;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import java.util.HashMap;
import java.util.Map;

public class GitHub {
    String valid_auth_token = null;
    String name = null;
    String actualDate = null;
    String errorMessage = null;
    Playwright playwright = Playwright.create();
    private APIRequestContext request;
    Map<String, String> headers = new HashMap<>();


    @Given("^(?:a|an) (valid|invalid) auth token is used$")
    public void valid_auth_token(String tokenType){
        valid_auth_token = tokenType.equalsIgnoreCase("valid")?
                System.getenv("PERSONAL_AUTH_TOKEN"):System.getenv("INVALID_AUTH_TOKEN");

        System.out.println(valid_auth_token);

        headers.put("Accept", "application/vnd.github.v3+json");
        headers.put("Authorization", "token " + valid_auth_token);
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                // All requests we send go to this API endpoint.
                .setBaseURL("https://api.github.com")
                .setExtraHTTPHeaders(headers));
    }

    @When("^requesting the latest commits into master of repo (.*) and username:(.*)$")
    public void request_repo_username(String repo, String username){
        APIResponse commits = request.get("/repos/" + username + "/" + repo + "/commits");
        update_user_date(commits);

    }

    @Then("^the most recent commit is authored by (.*) and the date is (.*)$")
    public void validate_user(String user, String expectedDate){
        Assert.assertTrue(name.equalsIgnoreCase(user));
        System.out.println(expectedDate);
        System.out.println(actualDate.split("T")[0]);
        Assert.assertTrue(actualDate.split("T")[0].equalsIgnoreCase(expectedDate));
    }

    @Then("^the (.*) error message is in the response$")
    public void validate_user(String expectedErrorMsg){
        Assert.assertTrue(errorMessage.contains(expectedErrorMsg));
    }


    public void update_user_date(APIResponse response){

        System.out.println(response.text());
        try {
            JsonArray json = new Gson().fromJson(response.text(), JsonArray.class);
            JsonObject latestCommit = json.getAsJsonArray().get(0).getAsJsonObject().get("commit").getAsJsonObject().get("author").getAsJsonObject();
            name = latestCommit.get("name").getAsString();
            actualDate = latestCommit.get("date").getAsString();
        }
        catch (JsonSyntaxException ee){

            errorMessage = response.text();
        }

        catch (Exception ee){
            System.out.println(response.text());
            System.out.println("unable to retrieve the name and actual date:" + ee.getMessage());
        }

    }
}
