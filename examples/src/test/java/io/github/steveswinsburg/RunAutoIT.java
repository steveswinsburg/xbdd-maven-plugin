package io.github.steveswinsburg;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Runner for @Automated tests
 */
@RunWith(Cucumber.class)
@CucumberOptions(tags = "@Automated", features = "src/test/resources", plugin = {
		"json:target/cucumber-report-auto.json"
}, glue = {
		"io.github.steveswinsburg.stepdefs"
}, strict = true)

public class RunAutoIT {
}
