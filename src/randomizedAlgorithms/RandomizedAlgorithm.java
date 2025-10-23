package randomizedAlgorithms;

import java.util.*;
import helpers.*;

/**
 * This class will be inherited by random algorithms
 * to this point they are pureSCH , SCH_withH and PAP
 * to be extended when others are added
 * It contains the following functions (to this point) :
 *  Random_init : randomly assign truth values to the variables of a formula
 *  ClauseSatisfied : tells if a given assignment satisfies a given clause
 *  clean_Formula : if the assignment of some variables is obvious this function assign it to them and remove them
 *  from the formula
 *  Random_Pick_UClause : it returns a clause that is not satisfied by an assignment at random
 *  Smallest_Pick_UClause : same as the function above but ensures that the size of the clause is minimum
 *  High_Priority_Pick_UClause : same as Random_Pick_UClause, but it takes in weighted clauses and returns a random clause
 *  with maximum possible weight
 * Highly_frequent_literal: takes some clauses and literals of a given clause and return the literal that satisfies the most of them
 * stochastic_literal_selection: takes some clauses and literals of a clause and returns one of them, influenced by their frequency (more frequent= more probable to be picked)
 * flip: takes in an assignment and flip all its values
 *
 */

public class RandomizedAlgorithm implements CNFSATSolver {

    protected final Random rand ;
    public RandomizedAlgorithm(){
        this.rand = new Random(123);
    }
    public RandomizedAlgorithm(long Seed) {
        this.rand = new Random(Seed);

    }

    public Map<Integer, Boolean> Random_init (CnfFormula formula) {

        Map<Integer,Boolean> beta = new HashMap<>();

        for (int var : formula.variables()) {
            beta.put(var,rand.nextBoolean());
        }

        return beta;

    }
    public boolean ClauseSatisfied(List<Integer> clause, Map<Integer,Boolean> assignment ) {
        int  var;

        for(int lit : clause){
            var = Math.abs(lit) ;

            // present and true iff literal is positive (aka not negated)
            if (assignment.containsKey(var) && assignment.get(var) != lit<0) {
                return  true;
            }

        }


        return false;
    }


    /**
     * Recursively take out variables as long as they obey OLR PLR
     * @return simplified formula
     */
    public CnfFormula clean_Formula (CnfFormula formula) {
        List<List<Integer>> clauses = new ArrayList<>(formula.clauses());
        Set<Integer> variables = new HashSet<>(formula.variables());
        boolean simplified;
        do {
            simplified = false;

            // OLR
            boolean unitClauseFound;
            do {
                unitClauseFound = false;
                Integer unitLiteral = null;
                for (List<Integer> clause : clauses) {
                    if (clause.size() == 1) {
                        unitLiteral = clause.get(0);
                        unitClauseFound = true;
                        break;
                    }
                }

                if (unitClauseFound) {
                    simplified = true;

                    //remove all its clauses
                    Integer finalUnitLiteral = unitLiteral; // variable should be final
                    clauses.removeIf(clause -> clause.contains(finalUnitLiteral));
                    // remove it if it is negated
                    for (List<Integer> clause : clauses) {
                        clause.removeIf(literal -> literal == -finalUnitLiteral);
                    }
                    variables.remove(Math.abs(unitLiteral));
                }
            } while (unitClauseFound);


            // PLR
            // Find all pure literals
            Set<Integer> pureLiterals = new HashSet<>();
            for(int variable : variables) {
                boolean purePositive = true;
                boolean pureNegative = true;

                // check if positive
                for (List<Integer> clause : clauses) {
                    if (clause.contains(-variable)){
                        purePositive = false ;
                        break;
                    }
                }
                // check if negative
                for (List<Integer> clause : clauses) {
                    if (clause.contains(variable)){
                        pureNegative = false ;
                        break;
                    }
                }
                if (purePositive|| pureNegative) pureLiterals.add(variable);

            }

            // remove pure literals
            if (!pureLiterals.isEmpty()) {
                simplified = true;
                for (int pureLiteral : pureLiterals) {
                    clauses.removeIf(clause -> clause.contains(pureLiteral) || clause.contains(-pureLiteral) );
                    variables.remove(Math.abs(pureLiteral));
                }

            }

        } while (simplified);

        // Create a new formula object with the cleaned clauses

        return new CnfFormula(formula.type(), clauses, variables, variables.size(), clauses.size());
    }


    /**
     *  Randomly chooses a clause that assignment do not satisfy
     */
    protected  List<Integer> Random_Pick_UClause(List<List<Integer>> clauses, Map<Integer, Boolean> assignment) {
        List<List<Integer>> UnsatisfiedClauses = new ArrayList<>();

        for (List<Integer>  clause : clauses) {

            // clauses  C unsatisfied <=> (forall var in C : var negated <=> assignment is true)
            boolean satisfied = false ;

            for (int literal : clause) {

                int var = Math.abs(literal);
                if(assignment.get(var) ==  literal > 0) {
                    satisfied = true;
                    break;
                }


            }

            if(!satisfied)   UnsatisfiedClauses.add(clause);

        }

        if (UnsatisfiedClauses.isEmpty()) {
            return null;
        }

        return UnsatisfiedClauses.get(rand.nextInt(UnsatisfiedClauses.size()));

    }


    /**
     *  Same as the function Random_Pick_UClause but ensures the size of the clause is minimal
     */
    protected List<Integer> Smallest_Pick_UClause(List<List<Integer>> clauses, Map<Integer, Boolean> assignment) {
        List<List<Integer>> SmallestUnsatisfiedClauses = new ArrayList<>();
        int minSize = Integer.MAX_VALUE;
        for (List<Integer>  clause : clauses) {

            // clauses  C unsatisfied <=> (forall var in C : var negated <=> assignment is true)
            boolean satisfied = false ;

            for (int literal : clause) {

                int var = Math.abs(literal);
                if(assignment.get(var) !=  literal < 0) {
                    satisfied = true;
                    break;
                }


            }

            if(!satisfied)  {
                int size = clause.size();
                if (size< minSize) {
                    minSize = size;
                    SmallestUnsatisfiedClauses.clear();
                    SmallestUnsatisfiedClauses.add(clause);
                }

                if (minSize == size) SmallestUnsatisfiedClauses.add(clause) ;


            }

        }
        if (SmallestUnsatisfiedClauses.isEmpty()) {
            return null;
        }

        return SmallestUnsatisfiedClauses.get(rand.nextInt(SmallestUnsatisfiedClauses.size()));
    }

    protected List<Integer> High_Priority_Pick_UClause(List<WeightedClause> Clauses, Map<Integer, Boolean> assignment) {

        long maxWeight = -1;

        // Find the maximum weight
        for (WeightedClause weightedClause : Clauses) {
            if (!ClauseSatisfied(weightedClause.clause(), assignment)) {
                if (weightedClause.weight() > maxWeight) {
                    maxWeight = weightedClause.weight();
                }
            }
        }

        // All are satisfied
        if (maxWeight == -1) {
            return null;
        }

        // List of highest weights
        List<WeightedClause> highest_Priority = new ArrayList<>();
        for (WeightedClause wClause : Clauses ) {
            if (wClause.weight() == maxWeight) highest_Priority.add(wClause);
        }
          // choose one from them at random
        return  highest_Priority.get(rand.nextInt(highest_Priority.size())).clause();

    }

    protected  int Highly_frequent_literal(List<List<Integer>> Clauses,List<Integer> clause) {
        if (Clauses == null || clause == null || clause.isEmpty()) {
            return 0 ; } // edge cases (although the< will never happen in our implementation)
        int highest = -1;
        int max  = -1;


        for (int literal : clause) {
            int frequency = 0 ;
            for(List<Integer> c : Clauses) {
                if(c.contains(literal)) frequency++;
            }
            if(frequency>highest) {
                highest = frequency;
                max = literal;
            }

        }

        return max;
    }
    protected  int stochastic_literal_selection(List<List<Integer>> Clauses,List<Integer> clause) {
        if (Clauses == null || clause == null || clause.isEmpty()) {
            return 0 ; } // edge cases (although the< will never happen in our implementation)

         int total = 0;
       Map<Integer,Integer> frequencies = new LinkedHashMap<>();

        for (int literal : clause) {
            int frequency = 0 ;
            for(List<Integer> c : Clauses) {
                if(c.contains(literal)) frequency++;
            }


           frequencies.put(literal,frequency);
            total += frequency;


        }
        int pick = rand.nextInt(total);
        int cdf = 0;
        for (Map.Entry<Integer,Integer> literalFrequency : frequencies.entrySet() ) {
            cdf +=  literalFrequency.getValue();
            if (pick <cdf) return literalFrequency.getKey();
        }

       // will practically never happen but needed for the correctness of the code
        return clause.get(0);
    }

    /**
     *
     * @return the same assignment but flips the truth values of each variable
     */
   public  Map<Integer, Boolean> flip( Map<Integer, Boolean>  assignment) {
        Map<Integer, Boolean> newAssignment = new HashMap<>() ;
        if (assignment == null) return  newAssignment;

        for(Map.Entry<Integer, Boolean> entry : assignment.entrySet()) {
            newAssignment.put(entry.getKey(), !entry.getValue());
        }

        return newAssignment;
    }


    public Random getRand() {
        return rand;
    }

    @Override
    public SatResult solve(CnfFormula formula) {
        System.out.println(" By default Schoening's algorithm is used");
        return new pureSCH().solve(formula);
    }

    @Override
    public void Output(CnfFormula formula) {

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
            System.out.println("likely Unsatisfiable!");
        }
        System.out.println("\n Time taken: " + (endTime - startTime) + " ms \n");

    }


}
