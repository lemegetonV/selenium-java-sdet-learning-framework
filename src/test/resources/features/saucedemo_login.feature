@bdd @saucedemo
Feature: SauceDemo login and checkout
  BDD scenarios describe user-visible behavior in business language.
  Step definitions translate this language into existing Page Object calls.

  Background:
    Given the SauceDemo login page is open

  @smoke @login
  Scenario: Standard user reaches the products page
    When I login as "standard_user" with password "secret_sauce"
    Then the products page should show title "Products"
    And the product catalog should contain 6 items

  @regression @login
  Scenario: Locked-out user sees a clear login error
    When I submit login for "locked_out_user" with password "secret_sauce"
    Then the login error should contain "Sorry, this user has been locked out."

  @regression @login
  Scenario Outline: Invalid login attempts stay on the login page
    When I submit login for "<username>" with password "<password>"
    Then the login error should contain "<message>"

    Examples:
      | username      | password     | message                                                                   |
      | standard_user | wrong_secret | Username and password do not match                                        |
      | problem_user  |              | Password is required                                                      |

  @regression @checkout
  Scenario: Standard user can start checkout for a selected product
    When I login as "standard_user" with password "secret_sauce"
    And I add the following products to the cart:
      | product             |
      | Sauce Labs Backpack |
    Then the cart badge should show "1"
    When I open the cart
    Then the cart should contain "Sauce Labs Backpack"
    When I start checkout
    Then the checkout title should be "Checkout: Your Information"
    And the customer information form should be displayed
