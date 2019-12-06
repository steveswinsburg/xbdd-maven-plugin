@ShouldAlwaysPass
Feature: Some automated tests that always pass
  These tests always pass

  @Automated
  Scenario: Automated passing test 1
    Given I have an automated test
    When I run this
    Then It will pass

  @Automated
  Scenario Outline: Automated passing test 2
    Given I have an automated test called <name>
    When I add <first_num> to <second_num> I get <total>

    Examples: 
      | name      | first_num | second_num | total |
      | addition1 |     1     |   1        |   2   |
      | addition2 |     2     |   2        |   4   |
