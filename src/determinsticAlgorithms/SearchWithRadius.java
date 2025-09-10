package determinsticAlgorithms;
import helpers.*;
import randomizedAlgorithms.RandomizedAlgorithm;

import java.util.*;
public class SearchWithRadius extends  DeterministicAlgorithm  {

    public SearchWithRadius() {

    }

    @Override
    public SatResult solve(CnfFormula formula) {

        return DoubleSidedSearch(formula);
    }


    public SatResult simpleSearch(CnfFormula formula) {
        return search(formula.clauses(), formula.NumberOfVariables(), new RandomizedAlgorithm().Random_init(formula));
    }

    public SatResult DoubleSidedSearch(CnfFormula formula) {
        CnfFormula  cnfFormula;
        if (formula.variables() == null) {
            return  null;
        }
        if(formula.NumberOfVariables() %2 !=0) {
            Set<Integer> cnfVariables =  formula.variables();
            cnfVariables.add(cnfVariables.size()+1) ; // add a new dummy variable  under the assumption the variables are in the form 1,2,...,n
            cnfFormula = new CnfFormula(formula.type(), formula.clauses(),cnfVariables, formula.NumberOfVariables()+1,formula.NumberOfClauses() );
        } else {
            cnfFormula = formula;
        }

        Map<Integer, Boolean> ALL_ONE = new HashMap<>();
        Map<Integer, Boolean> ALL_ZERO = new HashMap<>();
        for (Integer var : cnfFormula.variables()) {
            ALL_ZERO.put(var, false);
            ALL_ONE.put(var,true);
        }
        SatResult resultZero = search(cnfFormula.clauses(), formula.NumberOfVariables()/2, ALL_ZERO);
        if (resultZero.satisfiable()) return  resultZero;


        return search(cnfFormula.clauses(), formula.NumberOfVariables()/2, ALL_ONE);
    }




    public SatResult search(List<List<Integer>> clauses, int r, Map<Integer, Boolean> beta) {

        if (r <= 0) {
            return new SatResult(false, null);
        }

        if (clauses.isEmpty()) {
            return new SatResult(true, new HashMap<>());
        }
        if (clauses.contains(Collections.emptyList())) {
            return  new SatResult(false,null);
        }

        List<Integer> PickedClause =Pick_UClause(clauses,beta);
        // if beta satisfies F then return (yes, beta)
        if (PickedClause == null) {
            return new SatResult(true, beta);
        }

        for (int literal : PickedClause) {

            // beta_1 <- beta
            Map<Integer, Boolean> beta1 = new HashMap<>(beta);

            // set the assignment of l to 1 in beta_1
            int var = Math.abs(literal);
            beta1.put(var, literal > 0); // Set var to true if literal is positive, false otherwise

            // Simplify the formula F where l=1
            List<List<Integer>> simplifiedClauses = simplifyClauses(clauses, literal);

            // if Search(F|l=1, r-1, beta_1) returns (yes, beta_2) then
            SatResult result = search(simplifiedClauses, r - 1, beta1);

            if (result.satisfiable()) {
                // in this implementation we are sure
                // the value of literal will stay the same in result
                return result;
            }
        }

        // return (no, NULL)
        return new SatResult(false, null);
    }




}