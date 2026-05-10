# Module 12 Exercises

## Exercise 1 - Add A Missing Password Row

Add a missing-password row to the JSON file and make sure the JSON DataProvider
test handles it.

Hint:

- use username `standard_user`.
- use an empty password.
- expected message can be a stable fragment such as `Password is required`.

Expected outcome:

- the JSON DataProvider runs one additional row.
- the test method does not need to be duplicated.

## Exercise 2 - Add A CSV Column

Add a `category` column to the CSV file.

Hint:

- update the header.
- update each row.
- decide whether `LoginScenario` should include it or whether it is only
  metadata for a future module.

Expected outcome:

- the learner can explain that changing data shape may require model changes.

## Exercise 3 - Explain Excel Tradeoffs

Write a short answer explaining when Excel is useful and when it is not.

Expected outcome:

- a good answer mentions business readability.
- a good answer also mentions merge conflicts, binary diffs, and versioning
  challenges.

## Exercise 4 - Keep DataProviders Browser-Free

Explain why `LoginDataProviders` should not call `new LoginPage(...)`.

Expected outcome:

- the answer separates data preparation from browser behavior.
- the answer mentions that WebDriver lifecycle belongs to `BaseTest`.
