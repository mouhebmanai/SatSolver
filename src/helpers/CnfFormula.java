package helpers;

import java.util.*;

/**
 *  Represent a formula
 */
public record CnfFormula(List<List<Integer>> clauses,Set<Integer> variables, int NumberOfVariables, int NumberOfClauses) {
    /**
     * Constructs a CnfFormula object.
     *
     * @param clauses      The list of clauses.
     * @param variables   The variables themselves
     * @param NumberOfVariables The number of variables declared in the file.
     * @param NumberOfClauses   The number of clauses declared in the file.
     */
    public CnfFormula {
    }

    /**
     * Gets the list of clauses.
     *
     * @return A list of lists, where each inner list represents a clause.
     */
    @Override
    public List<List<Integer>> clauses() {
        return clauses;
    }

    @Override
    public Set<Integer> variables() {
        return variables;
    }


    /**
     * Gets the number of variables.
     *
     * @return The total number of variables.
     */
    @Override
    public int NumberOfVariables() {
        return NumberOfVariables;
    }

    /**
     * Gets the number of clauses.
     *
     * @return The total number of clauses.
     */
    @Override
    public int NumberOfClauses() {
        return NumberOfClauses;
    }


}
