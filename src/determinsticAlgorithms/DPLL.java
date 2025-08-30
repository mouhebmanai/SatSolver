package determinsticAlgorithms;

import java.util.*;
import helpers.*;
/**
 * implements DP approach  to solve the
 */
public class DPLL implements CNFSATSolver {

    /**
     method from Interface
     */
    public SatResult solve(CnfFormula formula) {

        Map<Integer, Boolean> result = dpll(new ArrayList<>(formula.clauses()), new HashMap<>());

        return  result == null ?
                new SatResult(false, null):
                new SatResult(true, result) ;

    }

    /**
     * the recursive DPLL algorithm
     *
     * @param clauses The current list of clauses to satisfy.
     * @param assignment   The current assignment of variables.
     * @return A satisfying model if one is found, otherwise null.
     */
    private Map<Integer, Boolean> dpll(List<List<Integer>> clauses, Map<Integer, Boolean> assignment) {
        //  remove already assigned variables
        List<List<Integer>> puredClauses = removeAssigned(clauses, assignment);


        if (puredClauses == null) {
            // one empty clause exists
            return null;
        }
        if (puredClauses.isEmpty()) {
            return assignment;
        }



        //  OLR
        Map<Integer, Boolean> OLR = OneLiteralRuleVars(puredClauses);
        if (!OLR.isEmpty()) {
            assignment.putAll(OLR);
            return dpll(puredClauses, assignment);
        }



        // PLR
        Map<Integer, Boolean> PLR = findPureLiterals(puredClauses);
        if (!PLR.isEmpty()) {
            assignment.putAll(PLR);
            return dpll(puredClauses, assignment);
        }




        // both cases on 1st variable
        int variable = Math.abs(puredClauses.get(0).get(0));

        //  case True
        Map<Integer, Boolean> setTrue = new HashMap<>(assignment);
        setTrue.put(variable, true);
        Map<Integer, Boolean> result = dpll(puredClauses, setTrue);

        if (result != null) {
            return result;
        }

        // case False
        assignment.put(variable, false);
        return dpll(puredClauses, assignment);



    }

    /**
     * Removes the already assigned variables
     * @param clauses The list of clauses.
     * @param assignment   The current variable assignments.
     * @return A formula without assigned variables
     */
    private List<List<Integer>> removeAssigned(List<List<Integer>> clauses, Map<Integer, Boolean> assignment) {

        List<List<Integer>> Result = new ArrayList<>();
        for (List<Integer> clause : clauses) {

            List<Integer> puredClause = new ArrayList<>();
            boolean clauseSatisfied = false;
            for (int literal : clause) {
                int var = Math.abs(literal);

                if (assignment.containsKey(var)) {
                    // literal > 0 is true
                    // <=> satisfied if value of the mapping is also true
                    if (assignment.get(var) == literal > 0) {
                        clauseSatisfied = true;
                        break;
                    }
                } else {
                    puredClause.add(literal); // Keep unassigned literals
                }
            }
            if (!clauseSatisfied) {
                if (puredClause.isEmpty()) {
                    return null; //
                }
                Result.add(puredClause);
            }


        }
        return Result;
    }

    /**
     * Finds all unit clauses (clauses with a single literal) and returns the
     * corresponding assignments.
     *
     * @param clauses The list of clauses.
     * @return A map of variable assignments derived from unit clauses.
     */
    private Map<Integer, Boolean> OneLiteralRuleVars(List<List<Integer>> clauses) {
        Map<Integer, Boolean> assignments = new HashMap<>();
        for (List<Integer> clause : clauses) {
            if (clause.size() == 1) {
                int literal = clause.get(0);
                assignments.put(Math.abs(literal), literal > 0);
            }
        }
        return assignments;
    }








    /**
     * Finds all pure literals (literals that appear only with one polarity) and
     * returns the corresponding assignments.
     *
     * @param clauses The list of clauses.
     * @return A map of variable assignments derived from pure literals.
     */
    private Map<Integer, Boolean> findPureLiterals(List<List<Integer>> clauses) {
        Set<Integer> literals = new HashSet<>();
        Set<Integer> variables = new HashSet<>();
        for (List<Integer> clause : clauses) {
            for (int literal : clause) {
                literals.add(literal);
                variables.add(Math.abs(literal));
            }
        }

        Map<Integer, Boolean> assignments = new HashMap<>();
        for (int var : variables) {
            boolean hasPositive = literals.contains(var);
            boolean hasNegative = literals.contains(-var);
            if (hasPositive != hasNegative) { // Pure literal
                assignments.put(var, hasPositive);
            }
        }
        return assignments;
    }



    public  void Output(CnfFormula formula) {

        long startTime = System.currentTimeMillis();
        SatResult result = this.solve(formula);
        long endTime = System.currentTimeMillis();

        //System.out.println("Formula " + formula.clauses());
        //____Output
        System.out.println("\n|\t DP approach\t|\t deterministic\t|\n");
        if (result.satisfiable()) {
            System.out.println("Satisfiable\n");

            Map<Integer, Boolean> certificateTree = new TreeMap<>(result.certificate());

            int cnt = 0 ;
            System.out.println("Certificate:");
            for (Map.Entry<Integer, Boolean> assign : certificateTree.entrySet()) {
                cnt++;
                System.out.print("|\t" + assign.getKey() + ":\t " + (assign.getValue() ? "T" : "F") + "\t");
                if ( cnt % 10 ==0 ) { System.out.println("|");}
            }
            System.out.println("|");
        } else {
            System.out.println("Unsatisfiable!");
        }
        System.out.println("\n Time taken: " + (endTime - startTime) + " ms \n");

    }



}
