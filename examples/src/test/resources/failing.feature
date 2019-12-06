Feature: Some automated tests that always fail
  These tests always fail

  @Automated
  Scenario: Automated failing test 1
    Given I have an automated test
    When I run this
    Then It will fail
    
  @Automated
  Scenario Outline: Automated failing test 2
    Given I have an automated test called <name>
    When I add <first_num> to <second_num> I get <total>

    Examples: 
      | name      | first_num | second_num | total |
      | addition1 |     1     |   1        |   9   |
      | addition2 |     2     |   2        |   27  |
   

