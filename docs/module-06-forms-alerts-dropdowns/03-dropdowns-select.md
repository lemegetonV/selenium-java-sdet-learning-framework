# Dropdowns and Selenium Select

## Files In This Topic

```text
src/test/java/com/learning/tests/learning/_13_CheckboxDropdownTest.java
```

The test uses:

```text
https://the-internet.herokuapp.com/dropdown
```

## HTML Behind A Dropdown

Selenium's `Select` helper works with real HTML `<select>` elements:

```html
<select id="dropdown">
  <option value="" disabled selected>Please select an option</option>
  <option value="1">Option 1</option>
  <option value="2">Option 2</option>
</select>
```

The `<select>` is the dropdown control. Each `<option>` is a selectable value.

## Creating A Select Object

Module 06 introduces:

```java
WebElement dropdownElement = driver.findElement(By.id("dropdown"));
Select dropdown = new Select(dropdownElement);
```

`Select` is a Selenium support class. It wraps a `WebElement` only when that
element is a real `<select>`.

If the application uses a custom dropdown built from `div`, `button`, `ul`,
or `li` elements, do not use `Select`. Test it as normal clickable elements.

## Common Dropdown Actions

Module 06 demonstrates:

```java
dropdown.selectByVisibleText("Option 1");
dropdown.selectByValue("2");
dropdown.getFirstSelectedOption();
dropdown.isMultiple();
```

Common actions:

| Action | Use |
| --- | --- |
| `selectByVisibleText(...)` | choose the option by user-visible label |
| `selectByValue(...)` | choose the option by its `value` attribute |
| `selectByIndex(...)` | choose by zero-based index, but avoid unless order is stable |
| `getFirstSelectedOption()` | assert the selected option |
| `getOptions()` | inspect all options |
| `isMultiple()` | check whether multiple selections are allowed |

## Beginner Mistakes

- using `Select` on a custom dropdown that is not a `<select>`.
- selecting by index when option order changes often.
- clicking the `<select>` and assuming the option changed.
- forgetting to assert the selected option after selection.
