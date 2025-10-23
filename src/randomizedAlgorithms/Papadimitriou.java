package randomizedAlgorithms;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import helpers.*;

/**
 * Implements Papadimitriou's approach for 2-SAT
 *   Repetitions gives the number of phases wanted (more => smaller error rate + less efficiency )
 *   Similar to Schoening's algorithm in the class pureSCH
 *   To be implemented mainly for formulas in 2-SAT with a solution expected to be found in quadratic time
 */
public class Papadimitriou extends RandomizedAlgorithm  {

    // how many phases of 2n^2 should we have
    private int Repetitions;

    //initial assignment
    private final Map<Integer, Boolean> beta_0 ;


    public  Papadimitriou( long seed) {
        super(seed);
        beta_0 = null;
    }
    public Papadimitriou(int Repetitions,long Seed, Map<Integer, Boolean> beta_0) {
        super(Seed);
        this.Repetitions = Repetitions;
        this.beta_0 = beta_0;
    }
    public Papadimitriou(int Repetitions,long Seed ) {
        super(Seed);
        this.Repetitions = Repetitions;
        this.beta_0 = null;
    }
    public Papadimitriou(){
        super(123);
        // error rate is less than 0.005 by default , (int) Math.ceil( Math.log(1.0/0.005) ) = 8
        // 8 would actually ensure the error rate is less than 0.004 because of the ceil function
        this.Repetitions = 8;
        this.beta_0 = null;
    }

    /**
     * ensures  the error probability when the algorithm is run for a formula to be at most @param m
     */
    public void setErrorProbability (double m) {
        // Math.log has base e so change by dividing by log(2)
        this.Repetitions = (int) Math.ceil( Math.log(1.0/m) / Math.log(2) );
    }




    public SatResult solve(CnfFormula formula) {
        // in case no initial assignment is given, generate randomly
        Map<Integer,Boolean> assignment  = beta_0 == null ? Random_init(formula) : beta_0;
        return pap(formula,Repetitions, assignment);
    }

    /**
     *  The main Papadimitriou algorithm
     * @param formula the formula in question
     * @param Repetitions how many phases
     * @param beta initial assignment
     * @return (hopefully) returns a satisfying assignment to @formula
     */
    private SatResult pap(CnfFormula formula,  int Repetitions, Map<Integer,Boolean> beta) {
        int n = formula.NumberOfVariables();
        for (int  i = 0 ; i< 2*n*n*Repetitions ; i++) {
            List<Integer>  pickedClause = Random_Pick_UClause(formula.clauses(),beta);

            if (pickedClause == null) {
                // all clauses are satisfied
                return new SatResult(true,beta);
            }
            int pickedLiteral = pickedClause.get(rand.nextInt(pickedClause.size())) ;
            int val = Math.abs(pickedLiteral);

            //flip the value
            beta.put(val, !beta.get(val));


        }

        return new SatResult(false,null);
    }

    public int papa_steps(CnfFormula formula) {
        Map<Integer,Boolean>  beta =  Random_init(formula);
        for (int  i = 0 ; i < Integer.MAX_VALUE ; i++) {
            List<Integer>  pickedClause = Random_Pick_UClause(formula.clauses(),beta);

            if (pickedClause == null) {
                // all clauses are satisfied
                return i ;
            }
            int pickedLiteral = pickedClause.get(rand.nextInt(pickedClause.size())) ;
            int val = Math.abs(pickedLiteral);

            //flip the value
            beta.put(val, !beta.get(val));


        }

     throw new RuntimeException("Algorithm could not find a solution in Integer.MAX_VALUE steps !");
    }



    public void Output(CnfFormula formula) {
        long startTime = System.currentTimeMillis();
        SatResult result = this.solve(formula);
        long endTime = System.currentTimeMillis();

        //System.out.println("Formula " + formula.clauses());
        //____Output
        System.out.println("\n| Papadimitirou's approach on 2-SAT | Randomized |\n");

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
            if ( cnt % 10 !=0 )   System.out.println("|");
        } else {
            // error derived from the formula for setting the repetition to ensure the error
            double error = 1.0 /( 1<<Repetitions );
            System.out.printf("The probability that the formula is satisfiable is less than %g\n", error);
            System.out.println("| Number of  phases of length 2n^2 run is " + Repetitions + " |");

        }

        System.out.println("\n Time taken: " + (endTime - startTime) + " ms \n");


    }
}