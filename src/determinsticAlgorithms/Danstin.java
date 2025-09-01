package determinsticAlgorithms;

import java.util.*;
import helpers.*;
public class Danstin extends DeterministicAlgorithm implements  CNFSATSolver  {


    // split the variables in 6 parts and create a covering code
    // implemented for 3_CNF , for more than 3 simply change the radius in the last line
    @Override
    public SatResult solve(CnfFormula formula) {

        // As long as needed, create dummy variables
        List<Integer> variables  = new ArrayList<>(formula.variables());

        int num = formula.NumberOfVariables();
        while (num % 6 != 0) {
            variables.add(num+1);
            num++;
        }
        int r = (int) Math.ceil(num / (double) (formula.type() +1 ) ) ;
        // now create codes for each partition

        List<Integer> vars;
        List<List<Map<Integer, Boolean>>> Codes = new ArrayList<>(6) ;
        for  (int i = 0  ; i < 6 ; i++) {

            vars = variables.subList(i * (num/6) , (i+1)*(num/6)) ;
           List<Map<Integer, Boolean>> code_i = greedyCreateCoveringCode(vars, r/6) ;
               Codes.add(code_i);

        }

        // now concatenate the codes

        List<Map<Integer,Boolean>> code = concatCodes(Codes);



         // now search for each center
          SearchWithRadius Search = new SearchWithRadius();
        for(Map<Integer,Boolean> c : code) {
            SatResult temp  = Search.search(formula.clauses(), r/6,c) ;
            if (temp.satisfiable()  ) return temp;
        }


        // if no center found a result, there is none

        return new SatResult(false,null);
    }



    @Override
    public void Output(CnfFormula formula) {

    }
//_____________ needed helper functions for Danstin and al algorithm (to be moved to DeterministicAlgorithm.java) if
//_____________ other algorithms need them



    /**
     * !!! Attention vars is turned into a list (instead of set in other parts) because
     *    order is needed for the splitting !!!
     * @param vars variables
     * @param r radius
     * @return the covering code as per Danstin and al.
     */
    public List<Map<Integer, Boolean>> greedyCreateCoveringCode(List<Integer> vars, int r) {
        List<Map<Integer, Boolean>> Result = new ArrayList<>();
        Set<Map<Integer,Boolean>> NotCovered = genAssignments(vars);
        Set<Map<Integer,Boolean>> allWords = new HashSet<>(NotCovered);
       while (!NotCovered.isEmpty()) {
           // find a potential new center
           Map<Integer, Boolean> cover  = null;
           int max = -1 ;

           // loop : look for which one covers the most
           for (Map<Integer,Boolean> center : allWords) {
                    // how much does it cover
               int covered = 0;
               for(Map<Integer,Boolean> uncovered : NotCovered) {
                   if(HammingDistance(center,uncovered) <= r) covered++;
               }
               // is it the actual maximum (at this point) ?
               if(covered > max ) {
                   max = covered;
                   cover = center;
               }

           }

           if (cover != null) {
           // add the cover
             Result.add(cover);
           // remove what it covers
           Map<Integer, Boolean> finalCover = cover; // final needed for removeIf
           NotCovered.removeIf(a -> HammingDistance(a, finalCover)<=r); }
       }



        return Result;
    }










    /**
     *
     * @param  codes of each part of the variables
     * @return  combination of them into the final code
     */
    public List<Map<Integer,Boolean>> concatCodes( List<List<Map<Integer,Boolean>>> codes  ) {
        if ( codes == null || codes.isEmpty() ) return new ArrayList<>();
    return concatCodesRecursive( codes , codes.size() -1 );
    }

    /**
     *   recursive core algorithm
     *
     * @param codes the list of codes to be concatenated
     * @param  pointer is where the actual concatenation reached
     * @return concatenate with the current concatenated code and put it at concatenated -1
     */
    private   List<Map<Integer,Boolean>> concatCodesRecursive( List<List<Map<Integer,Boolean>>> codes , int pointer ) {
        // base case
        if (pointer == 0) return  codes.get(0);

        // some helper variables
        List<Map<Integer,Boolean>> result  = new ArrayList<>();
        List<Map<Integer,Boolean>> concatenatedCode = concatCodesRecursive(codes,pointer- 1);
        List<Map<Integer,Boolean>> toBeConcatenated = codes.get(pointer);
        Map<Integer,Boolean> concatenation;

        // recursion
        // get what was already concatenated.
        // concatenate it with new code
        for ( Map<Integer,Boolean> CC : concatenatedCode ) {
            for ( Map<Integer,Boolean> tBC : toBeConcatenated ) {
            // at each point take both and add them to result
                concatenation = new HashMap<>(CC);
                concatenation.putAll(tBC);
                result.add(concatenation);
            }
        }
        return result;
    }

}
