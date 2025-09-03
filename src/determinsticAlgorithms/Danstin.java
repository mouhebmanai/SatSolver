package determinsticAlgorithms;

import java.util.*;
import helpers.*;

public class Danstin extends DeterministicAlgorithm   {


    // split the variables in 6 parts and create a covering code , search for each word in the covering code
    // search happen online, i.e. every center is for covering code is checked with search exactly the moment it is created
    // and is never stored
    @Override
    public SatResult solve(CnfFormula formula) {

        // As long as needed, create dummy variables, note variables is now a set because we need the order
        List<Integer> variables  = new ArrayList<>(formula.variables());

        int num = formula.NumberOfVariables();
        while (num % 6 != 0) {
            variables.add(num+1); // assumption: variables are of the form 1,2,3,....,n
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

        // recursively create cover code centers and check if they can find a satisfying assignment
        return ConcatAndSearch(formula.clauses(),  Codes, new HashMap<>(), r,0);
    }

    private SatResult ConcatAndSearch(List<List<Integer>> clauses , List<List<Map<Integer, Boolean>>> codes, Map<Integer, Boolean> current, int r, int pointer) {
        // variation of concatCodesRecursive (below) but when a code is created it is directly used in search
        if (pointer == codes.size()) {
            // Now, run the search for the center found
            SearchWithRadius search= new SearchWithRadius();
            return search.search(clauses, r, current);
        }


        // Get the list of centers for the current step. (like the other version)
        List<Map<Integer, Boolean>> toBeConcatenated = codes.get(pointer);
        // Iterate through  centers :
        for (Map<Integer, Boolean> partialCode : toBeConcatenated) {
            // Create the new level of the code
            Map<Integer, Boolean> next = new HashMap<>(current);
            next.putAll(partialCode); // same idea from the other version
            // branch to the next partition.
            SatResult result = ConcatAndSearch(clauses, codes, next, r, pointer + 1);
            // If the recursive call found a solution,  we are done!
            if (result.satisfiable()) {
                return result;
            }
            // else discard and try the next step  (for loop with the next branch)
        }

        // If  the current branch fails return (false,null) to allow backtracking
        return new SatResult(false, null);
    }



  // first variation of Danstin and al.'s algorithm ; creates the whole cover code and then check for each center of a ball in it
    public SatResult solve_after_concat(CnfFormula formula) {

        // As long as needed, create dummy variables, note variables is now a set because we need the order
        List<Integer> variables  = new ArrayList<>(formula.variables());

        int num = formula.NumberOfVariables();
        while (num % 6 != 0) {
            variables.add(num+1); // assumption: variables are of the form 1,2,3,....,n
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

        List<Map<Integer,Boolean>> code = new ArrayList<>();
        // now concatenate the codes

        System.out.println( concatCodes(Codes) );
        System.out.println(code);


         // now search for each center
          SearchWithRadius Search = new SearchWithRadius();
        for(Map<Integer,Boolean> c : code) {
            SatResult temp  = Search.search(formula.clauses(), r,c) ;
            if (temp.satisfiable()  ) return temp;
        }


        // if no center found a result, there is none

        return new SatResult(false,null);

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
        if (pointer == 0) {
           // return the center
            return   codes.get(0);
        }

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
        System.out.println("concat done " + pointer);
        System.out.println(result.get(0));

        return result;
    }







}
