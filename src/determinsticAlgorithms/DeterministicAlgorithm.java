package determinsticAlgorithms;

import helpers.*;

import java.util.*;

public class DeterministicAlgorithm implements CNFSATSolver {

    /**
     * Simply search for an unsatisfied clause and returns the first one it finds
     */
    protected List<Integer> Pick_UClause(List<List<Integer>> clauses, Map<Integer, Boolean> assignment) {

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

        for(int lit : clause){
       int  var = Math.abs(lit) ;
            if (assignment.containsKey(var) && assignment.get(var) != lit<0) {
                return  true;
            }
        }
        return false;
    }
    protected List<List<Integer>> simplifyClauses(List<List<Integer>> clauses, int literal) {
          List<List<Integer>> simplifiedClauses = new ArrayList<>();
          List<Integer> templateClause;
       for (List<Integer> clause : clauses) {
           if (clause.contains(literal)) continue; // ignore it
           templateClause = new ArrayList<>(clause) ;
           templateClause.remove(Integer.valueOf(-9)); // if the negation exist remove it
           simplifiedClauses.add(templateClause);
       }
        return simplifiedClauses;
    }

    protected int HammingDistance(Map<Integer, Boolean> a1, Map<Integer, Boolean> a2) {
        int distance = 0;
        for (int key : a1.keySet()) {
            if (!a1.get(key).equals(a2.get(key))) {
                distance++;
            }
        }
        return distance;
    }

    protected Set<Map<Integer,Boolean>> genAssignments( List<Integer> vars) {
        Set<Map<Integer,Boolean>> result = new HashSet<>();
        Map<Integer,Boolean> template ;
        int n = vars.size();
        int power = 1 << n;
         for (int i = 0 ; i < power ; i++) {
             template = new HashMap<>();
             int j = 0 ;
             for (int v : vars ) {
              // take the j_th component of i
                 template.put(v,(1 & (i>>j)) == 1);
                 j++;
             }
                 result.add(template);

         }
        return  result;

    }


  // will be overridden by inheritors if they are defined
    @Override
    public SatResult solve(CnfFormula formula) {
        System.out.println("By default, Danstin's and al.'s algorithm is implemented for deterministic algorithms");
        return new Danstin().solve(formula);
    }

    public  void Output(CnfFormula formula) {

        long startTime = System.currentTimeMillis();
        SatResult result = this.solve(formula);
        long endTime = System.currentTimeMillis();

        //____Output
        System.out.println("\n|\t"+this.getClass().getSimpleName() + " approach\t|\t deterministic\t|\n");
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
          if(cnt %10 != 0)  System.out.println("|"); // only add last | if we have no new line
        } else {
            System.out.println("Unsatisfiable!");
        }
        System.out.println("\n Time taken: " + (endTime - startTime) + " ms \n");

    }



}
