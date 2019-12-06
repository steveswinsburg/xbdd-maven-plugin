package io.github.steveswinsburg.stepdefs;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Steps {

	@Given("^I have an automated test$")
	public void i_have_an_automated_test() {
		Assert.assertTrue(true);
	}

	@When("^I run this$")
	public void i_run_this() {
		Assert.assertTrue(true);
	}

	@Then("^It will fail$")
	public void it_will_fail() {
		Assert.fail("This is meant to fail");
	}

	@Given("^I have an automated test called addition(\\d+)$")
	public void i_have_an_automated_test_called_addition(final int arg1) {
		Assert.assertTrue(arg1 > 0);
	}

	@Then("^It will pass$")
	public void it_will_pass() {
		Assert.assertTrue(true);
	}

	@When("^I add (\\d+) to (\\d+) I get (\\d+)$")
	public void i_add_to_I_get(final int arg1, final int arg2, final int total) {
		Assert.assertTrue(arg1 + arg2 == total);
	}

}
