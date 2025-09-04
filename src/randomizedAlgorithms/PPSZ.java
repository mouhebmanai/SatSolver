package randomizedAlgorithms;
import java.util.*;
import helpers.*;
public class PPSZ extends RandomizedAlgorithm  {
 private  int s ;
 private int I ;

 public  PPSZ(int I, int s) {
     this.I = I;
     this.s = s;
 }
 public  PPSZ(long seed, int I, int s) {
     super(seed);
        this.I = I;
        this.s = s;

    }


    public SatResult solve(CnfFormula formula) {

        // Preprocess : Resolve
        List<List<Integer>> clauses = Resolve(formula.clauses(),s);
        CnfFormula resolvedFormula = new CnfFormula(formula.type(),
                                                        clauses,
                                                        formula.variables(),
                                                        formula.NumberOfVariables(),
                                                        clauses.size() );
        List<Integer> listedVars = new ArrayList<>(resolvedFormula.variables());

        // Preprocess is done
        // now search
        for (int i = 0 ; i < I ; i++) {
            List<Integer> permutation  = create_random_permutation(listedVars);
            Map<Integer,Boolean> assignment = Random_init(formula);
            SatResult result  = Search(formula,permutation,assignment);
            if(result.satisfiable()) return result;
        }


        return new SatResult(false,null);
    }




    private List<List<Integer>> Resolve(List<List<Integer>> clauses , int s)  {
        List<List<Integer>> resultingClauses = new ArrayList<>(clauses);
        Set<Set<Integer>> visitedClauses = new HashSet<>(); // only needed for lookup => set is enough


        boolean newClauseAdded;

        do {
            newClauseAdded = false;
            List<List<Integer>> newClauses = new ArrayList<>();

            for (int i = 0 ; i < resultingClauses.size() ; i++) {

                for (int j = i+1 ; j < resultingClauses.size() ; i++) {
                    List<Integer> resolvent = ResolveAPair(resultingClauses.get(i), resultingClauses.get(j), s);

                    if (resolvent != null && visitedClauses.add(new HashSet<>(resolvent))) {
                       newClauses.add(resolvent);
                        newClauseAdded  = true;
                    }

                }

            }

         resultingClauses.addAll(newClauses);
        } while (newClauseAdded);


        return resultingClauses;
    }

    private List<Integer> ResolveAPair(List<Integer> first, List<Integer> second, int s) {
        if(first.size() > s || second.size()> s) return null ;
        List<Integer> resolventLiteral = new ArrayList<>();
     for (Integer literal_1 : first) {
         for (Integer literal_2 : second) {
             if (literal_2 == -literal_1) resolventLiteral.add(literal_1);
         }
     }
     // we want only one literal to resolve on
        if (resolventLiteral.size() != 1) return null;
        int literal_1 = resolventLiteral.get(0);
        List<Integer> result = new ArrayList<>();
        for (Integer literal : first) if(literal != literal_1) result.add(literal);
        for (Integer literal : second) if(literal != -literal_1) result.add(literal);

        // result should be less than s
        if (result.size() > s) return null;

     return result;

    }

    private  SatResult Search(CnfFormula formula,List<Integer> permutation, Map<Integer,Boolean> assignment) {
     List<List<Integer>> resultingClauses = formula.clauses();

     for (int i = 0 ; i <permutation.size();i++) {
       Integer  currentVar = permutation.get(i);

       // remove if olr with the variable
        if (   formula.clauses().contains(new ArrayList<>( List.of(currentVar) ) )   ) {
          assignment.put(currentVar,true);
        } else
        // remove if olr with its negation
         if (   formula.clauses().contains(new ArrayList<>( List.of(-currentVar) ) )   ) {
             assignment.put(currentVar,false);
          }

         resultingClauses = removeLiteral(resultingClauses, assignment.get(currentVar)? currentVar : -currentVar);



      if(resultingClauses == null) return   new SatResult(false,null);
      if (resultingClauses.isEmpty()) return  new SatResult(true,assignment);



     }
     return new SatResult(false,null);
    }

    private   List<Integer> create_random_permutation(List<Integer> vars) {
        Collections.shuffle(vars, rand);
       return vars;
    }
    private  List<List<Integer>> removeLiteral (List<List<Integer>> clauses , int literal) {
     List<List<Integer>> resultingClauses = new ArrayList<>();
      List<Integer> temp ;
     for (List<Integer> c : clauses) {
         if (c.contains(literal)) continue;
         temp = new ArrayList<>(c);
         if (temp.contains(-literal))  {
             temp.remove((Integer) (-literal)) ;
         }
         if (temp.isEmpty()) return null;
         resultingClauses.add(temp);
     }

     return resultingClauses;
    }


    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        PPSZ pp= new PPSZ(2,10,20);
        set.add(1);
        set.add(7);
        set.add(50);
        set.add(70);
        set.add(90);
        List<Integer> result = new ArrayList<>(set);
        for (int i = 0 ; i <10 ; i++) {
            System.out.println(pp.create_random_permutation(result));
        }
    }

}
