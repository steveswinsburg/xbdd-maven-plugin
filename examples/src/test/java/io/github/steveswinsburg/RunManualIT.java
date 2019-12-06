package io.github.steveswinsburg;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Runner for @Manual tests
 */
@RunWith(Cucumber.class)
@CucumberOptions(tags = "@Manual", features = "src/test/resources", plugin = { "pretty",
		"json:target/cucumber-report-manual.json" }, monochrome = true, strict = false, dryRun = true)
public class RunManualIT {
}
