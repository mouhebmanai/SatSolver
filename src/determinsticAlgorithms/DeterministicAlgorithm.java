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

    protected boolean ClauseSatisfied(List<Integer> clause, Map<Integer,Boolean> assignment ) {
        int  var;
        for(int lit : clause){
            var = Math.abs(lit) ;
            if (assignment.containsKey(var) && assignment.get(var) != lit<0) {
                return  true;
            }
        }
        return false;
    }
    protected List<List<Integer>> simplifyClauses(List<List<Integer>> clauses, int literal) {
          List<List<Integer>> simplifiedClauses = new ArrayList<>(clauses);
        return simplifiedClauses;
    }




}
