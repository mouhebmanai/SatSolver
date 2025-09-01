package randomizedAlgorithms;
import helpers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SCH_withH extends RandomizedAlgorithm implements CNFSATSolver {

    private int Repetitions;
    private boolean DoubleSided ;
    private  boolean InitialCleanup ;
    private boolean ClauseWeighing;
    private boolean SmallestNextClause ;
    private  boolean NextLiteral ;
   public  SCH_withH() {
       super();
       Repetitions   = 10_000;
       DoubleSided = true;
       InitialCleanup = true ;
       ClauseWeighing = true ;
       SmallestNextClause = true;
       NextLiteral = true;
   }

   public SCH_withH(long seed, int repetitions,boolean doubleSided, boolean initialCleanup,
                    boolean clauseWeighing, boolean SmallestNextClause, boolean nextLiteral
   ) {
       super(seed);
       this.Repetitions   = repetitions;
       this.DoubleSided = doubleSided;
       this.InitialCleanup = initialCleanup ;
       this.ClauseWeighing = clauseWeighing ;
       this.SmallestNextClause = SmallestNextClause;
       this.NextLiteral = nextLiteral;

   }

    @Override
    public SatResult solve(CnfFormula formula) {
        return isClauseWeighing() ? schH_CW(formula,  Repetitions) : schH_No_CW(formula,  Repetitions)  ;
   }

    /**
     * Main Schoening aglorithm's codes
     * The first implements  the logic of clause weighing
     * Same function as in the class pureSCH but adds heuristics where needed
     * @param formula the Formula to be solved for SAT
     * @param Repetitions number of Phases and Restarts
     * @return True with Certificate if satisfying clause is found and false else.
     */
    private SatResult schH_CW(CnfFormula formula,  int Repetitions) {
        Map<Integer, Boolean>   beta ;
        if (isInitialCleanup())  { formula = clean_Formula(formula); }
        // initially each clause has weight = 1
      // Weight of clauses increase if they are not satisfied after a phase
        List<WeightedClause> weightedClauses = new ArrayList<>() ;
        for (List<Integer> c : formula.clauses()) {
            weightedClauses.add(new WeightedClause(c,1));
        }

        for (int i = 0 ; i < Repetitions ; i++) {

            //initialize the assignment
            beta =   Random_init(formula);

            // do  one phase
            for(int j = 0 ;  j < 3* formula.NumberOfVariables() ; j++ ) {

                // check if the flipped assignment satisfy  all the clauses
                if(isDoubleSided()) {
                 Map<Integer,Boolean> flipped =   flip(beta);
                    for (List<Integer> clause : formula.clauses()) {
                        if(!ClauseSatisfied(clause,flipped)) break;
                    }
                      return new SatResult(true,flipped)   ;
                }

                // pick unsatisfied clause from the queue based on the clauses that were found unsatisfied the most
                List<Integer> pickedClause = High_Priority_Pick_UClause(weightedClauses,beta) ;

                if (pickedClause == null) {
                    // all clauses are satisfied
                    System.out.println("\n\n| Number of  phases  run is " + i + " |") ;
                    return new SatResult(true,beta);
                }


                int pickedLiteral = isNextLiteral() ?
                        Highly_frequent_literal(formula.clauses(), pickedClause) :
                        pickedClause.get(rand.nextInt(pickedClause.size())) ;



                int val = Math.abs(pickedLiteral);

                //flip the value
                beta.put(val, !beta.get(val));

            }
          // increase the weight of unsatisfied clauses
            for (WeightedClause weightedClause : weightedClauses) {
                if (ClauseSatisfied(weightedClause.clause(),beta)) {
                    // take old weight and add 1
                      weightedClause.setWeight(weightedClause.weight()+1);
                }
            }

        }
        return new SatResult(false,null);
    }
    private SatResult schH_No_CW(CnfFormula formula,  int Repetitions) {
           // remove all the variables that obeys OLR and PLR
      if (isInitialCleanup())  { formula = clean_Formula(formula); }

        Map<Integer, Boolean>  beta ;
        for (int i = 0 ; i < Repetitions ; i++) {

            //initialize the assignment
            beta   = Random_init(formula);

            // do  one phase
            for(int j = 0 ;  j < 3* formula.NumberOfVariables() ; j++ ) {

                if(isDoubleSided()) {
                    Map<Integer,Boolean> flipped =   flip(beta);
                    for (List<Integer> clause : formula.clauses()) {
                        if(!ClauseSatisfied(clause,flipped)) break;
                    }
                    return new SatResult(true,flipped)   ;
                }

                // pick unsatisfied clause
                List<Integer> pickedClause = isSmallestNextClause()? Smallest_Pick_UClause(formula.clauses(),beta)
                        : Random_Pick_UClause(formula.clauses(),beta);


                if (pickedClause == null) {
                    // all clauses are satisfied
                    System.out.println("\n\n| Number of  phases  run is " + i + " |") ;
                    return new SatResult(true,beta);
                }



                int pickedLiteral = isNextLiteral() ?
                        Highly_frequent_literal(formula.clauses(), pickedClause) :
                        pickedClause.get(rand.nextInt(pickedClause.size())) ;



                int val = Math.abs(pickedLiteral);

                //flip the value
                beta.put(val, !beta.get(val));

            }


        }
        return new SatResult(false,null);
    }

    @Override
    public void Output(CnfFormula formula) {
        long startTime = System.currentTimeMillis();
        SatResult result = this.solve(formula);
        long endTime = System.currentTimeMillis();

        //System.out.println("Formula " + formula.clauses());
        //____Output
        System.out.println("\n| Schoening approach with Heurstics | Randomized |\n ");
        System.out.println("Possible Heuristics:\n"
        + "DoubleSidedCheck\t:\t"+ isDoubleSided()
         + "\nInitialCleanup\t:\t" + isInitialCleanup()
        + "\nClauseWeighing\t:\t" + isClauseWeighing()
         + "\nSmallestNextClause\t:\t" + isSmallestNextClause()
         + "\nNextLiteral\t:\t" + isNextLiteral() +"\n\n"
        );

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
            System.out.println("|");
        } else {

            int k = formula.type();
            double value = Repetitions * 2.0 / 3.0 * Math.pow(k /(2.0* (k-1)), formula.NumberOfVariables());

            System.out.printf("The probability that the formula is satisfiable is less than exp(-%g) \n", value);
            System.out.println("| Number of  phases  run is " + Repetitions + " |");
        }

        System.out.println("\n Time taken: " + (endTime - startTime) + " ms \n");
    }

    // The following part contains only getters and setters

        public boolean isDoubleSided () {
        return DoubleSided;
    }

        public void setDoubleSided ( boolean doubleSided){
        DoubleSided = doubleSided;
    }

        public boolean isInitialCleanup () {
        return InitialCleanup;
    }

        public void setInitialCleanup ( boolean initialCleanup){
        this.InitialCleanup = initialCleanup;
    }

        public boolean isClauseWeighing () {
        return ClauseWeighing;
    }

        public void setClauseWeighing ( boolean clauseWeighing){
        ClauseWeighing = clauseWeighing;
    }

        public boolean isSmallestNextClause () {
        return SmallestNextClause;
    }

        public void setSmallestNextClause ( boolean sNextClause){
        this.SmallestNextClause = sNextClause;
    }

        public boolean isNextLiteral () {
        return NextLiteral;
    }

        public void setNextLiteral ( boolean nextLiteral){
        this.NextLiteral = nextLiteral;
    }

        public int getRepetitions () {
        return Repetitions;
    }

        public void setRepetitions ( int repetitions){
        Repetitions = repetitions;
    }

}
