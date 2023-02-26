import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
            features = "src/test/resources/features",
            plugin = {"html:target/cucumber-html-report.html", "json:target/cucumber-json-report.json"},
            dryRun = true
            ,glue = {"uk.amanze.framework"}
            )
public class CucumberTest {}
