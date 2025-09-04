package randomizedAlgorithms;
import java.util.*;
import helpers.*;


/**
 * Implements schöning's approach for k-SAT
 *   Repetitions gives the number of phases wanted (more => smaller error rate + less efficiency )
 *   Heuristics set if added heuristic is wanted for the algorithm (see paper)
 */
public class pureSCH extends RandomizedAlgorithm{

    private int Repetitions;


    public pureSCH(int Repetitions,long Seed){
        super(Seed);
        this.Repetitions = Repetitions;

    }

    public pureSCH(long seed) {
        super(seed);        this.Repetitions = 10_000;
    }

    /**
     *  For a given @param m and a @param formula F in k-Cnf, this function sets the smallest number of repetitions that ensures
     *  the error probability is less than e^-m, only if k>= 3 else leave the number of reps as it is
     */
    public void setExpErrorProbability (CnfFormula formula , double m) {

        if(formula.type()>=3) {
            this.Repetitions = (int) Math.ceil( m *  3.0/2 * Math.pow(2.0*(formula.type())-1/ (double) formula.type() , formula.NumberOfVariables()) );
        }
       // System.out.println("to ensure the error rate e^(-"+m+"), the number of repetitions is set to"+ this.Repetitions);

    }

    public pureSCH(){
        super(123);

        this.Repetitions = 10_000;
    }




    public SatResult solve(CnfFormula formula) {
        return sch(formula,  Repetitions);

    }


    /**
     * Main Schoening aglorithm's code
     * @param formula the Formula to be solved for SAT
     * @param Repetitions number of Phases and Restarts
     * @return True with Certificate if satisfying clause is found and false else.
     */
    private SatResult sch(CnfFormula formula,  int Repetitions) {

        Map<Integer, Boolean>   beta ;

        for (int i = 0 ; i < Repetitions ; i++) {
            //initialize the assignment
            beta =  Random_init(formula);

            // do  one phase
            for(int j = 0 ;  j < 3* formula.NumberOfVariables() ; j++ ) {
                // pick unsatisfied clause
                List<Integer>  pickedClause = Random_Pick_UClause(formula.clauses(),beta);

                if (pickedClause == null) {
                    // all clauses are satisfied
                  //  System.out.println("\n\n| Number of  phases  run is " + i + " |") ;
                    return new SatResult(true,beta);
                }

             int rando =  rand.nextInt(pickedClause.size());
                System.out.println(rando);
                int pickedLiteral = pickedClause.get(rand.nextInt(pickedClause.size())) ;
                int val = Math.abs(pickedLiteral);

                //flip the value
                beta.put(val, !beta.get(val));

            }


        }
        return new SatResult(false,null);

    }


    public void Output(CnfFormula formula) {
        long startTime = System.currentTimeMillis();
        SatResult result = this.solve(formula);
        long endTime = System.currentTimeMillis();

        //System.out.println("Formula " + formula.clauses());
        //____Output
        System.out.println("\n| Schoening approach | Randomized |\n");

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
            if ( cnt % 10 !=0 )  System.out.println("|");
        } else {
            int  k = formula.type();
          double   value = Repetitions * 2.0 / 3.0 * Math.pow(k /(2.0* (k-1)), formula.NumberOfVariables());

            System.out.printf("The probability that the formula is satisfiable is less than exp(-%g) \n", value);
            System.out.println("| Number of  phases  run is " + Repetitions + " |");

        }

        System.out.println("\n Time taken: " + (endTime - startTime) + " ms \n");
    }

    public int getRepetitions() {
        return Repetitions;
    }

    public void setRepetitions(int repetitions) {
        Repetitions = repetitions;
    }


}