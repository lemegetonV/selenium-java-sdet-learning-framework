package com.learning.examples.module01;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a small test-case name and its ordered steps.
 *
 * This class connects Java collections to test automation. Later modules
 * will use collections for web elements, product names, table rows, and
 * data-driven test records.
 */
public class _03_TestCaseSummary {

    /*
     * The object owns its internal list. Callers can add valid steps through a
     * method, but they cannot directly replace or mutate the field.
     */
    private final String testName;
    private final List<String> steps;

    /**
     * Creates an empty summary. Steps are added one at a time to demonstrate a
     * mutable collection inside an otherwise focused object.
     */
    public _03_TestCaseSummary(String testName) {
        this.testName = testName;
        /*
         * The field type is List<String>, the interface. The concrete object is
         * ArrayList because this class needs to add steps over time. Coding to
         * the interface keeps callers focused on list behavior, not the exact
         * implementation.
         */
        this.steps = new ArrayList<>();
    }

    /**
     * Adds a step only when the input is useful.
     *
     * This introduces defensive validation before the framework reaches
     * exception handling and richer failure messages in later modules.
     */
    public void addStep(String step) {
        if (step == null || step.isBlank()) {
            return;
        }
        steps.add(step);
    }

    /**
     * Gives callers the count without exposing the internal list.
     */
    public int getStepCount() {
        return steps.size();
    }

    /**
     * Produces a simple human-readable summary for demo output.
     */
    public String describe() {
        return testName + " has " + getStepCount() + " steps";
    }

    /**
     * Returns a defensive copy so callers can read steps without mutating this
     * object's private list.
     */
    public List<String> getSteps() {
        /*
         * Returning steps directly would expose the private mutable ArrayList.
         * List.copyOf creates a read-only snapshot so the object keeps ownership
         * of its internal state.
         */
        return List.copyOf(steps);
    }
}
