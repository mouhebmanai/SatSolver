package randomizedAlgorithms;
import java.util.*;
import helpers.*;
public class PPSZ extends RandomizedAlgorithm implements  CNFSATSolver {
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

    @Override
    public SatResult solve(CnfFormula formula) {

        // Preprocess : Resolve
        List<List<Integer>> clauses = Resolve(formula.clauses(),s);
        CnfFormula resolvedFormula = new CnfFormula(formula.type(),
                                                        clauses,
                                                        formula.variables(),
                                                        formula.NumberOfVariables(),
                                                        clauses.size() );
        List<Integer> listedvars = new ArrayList<>(resolvedFormula.variables());

        // Preprocess is done
        // now search
        for (int i = 0 ; i < I ; i++) {
            List<Integer> permutation  = create_random_permutation(listedvars);
            Map<Integer,Boolean> assignment = Random_init(formula);
            SatResult result  = Search(formula,permutation,assignment);
            if(result.satisfiable()) return result;
        }


        return new SatResult(false,null);
    }


    private List<List<Integer>> Resolve(List<List<Integer>> clauses , int s)  {
        List<List<Integer>> resultingClauses = new ArrayList<>(clauses);
    //TODO

        return resultingClauses;
    }
    private  SatResult Search(CnfFormula formula,List<Integer> permutation, Map<Integer,Boolean> assignment) {
     //TODO
     return new SatResult(false,null);
    }

    private   List<Integer> create_random_permutation(List<Integer> vars) {
        Collections.shuffle(vars, rand);
       return vars;
    }

    @Override
    public void Output(CnfFormula formula) {
   //TODO
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
