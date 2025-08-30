package determinsticAlgorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeterministicAlgorithm {

    /**
     * Simply search for an unsatisfied clause and returns the first one it finds
     */
    protected  List<Integer> Pick_UClause(List<List<Integer>> clauses, Map<Integer, Boolean> assignment) {

        for (List<Integer>  clause : clauses) {

            boolean satisfied = false ;

            for (int literal : clause) {

                int var = Math.abs(literal);
                if(assignment.get(var) !=  literal < 0) {
                    satisfied = true;
                    break;
                }


            }

            if(!satisfied)  return  clause;

        }

        return null;

    }

    /**
     * Removes the already assigned variables
     * @param clauses The list of clauses.
     * @param assignment   The current variable assignments.
     * @return A formula without assigned variables
     */
    protected List<List<Integer>> removeAssigned(List<List<Integer>> clauses, Map<Integer, Boolean> assignment) {

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



}
