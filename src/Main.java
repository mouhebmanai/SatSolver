import java.io.IOException;
import java.util.*;


public class Main {




    public static void main(String[] args) {

       String filePath = "CNF403/CBS_k3_n100_m403_b10_2.cnf";

        try {

            System.out.println("Parsing: " + filePath);
            CnfFormula formula = ParseCnf.parseKCnf(filePath,50, 2);
            //System.out.println(formula.clauses());
            System.out.println("Parsing successful:\t  " +
                    formula.NumberOfVariables() + " variables,\t" +
                    formula.NumberOfClauses() + " clauses.");

            //change with respect to the solver
           CNFSATSolver papSolver = new Papadimitriou();
           CNFSATSolver dpllSolver = new DPLL();
         //  pureSCH solver2= new pureSCH(10000,420);
       // SCH_withH solver3 = new SCH_withH();
            System.out.println(formula.clauses());
        papSolver.Output(formula);
        dpllSolver.Output(formula);

            //____Solve and Output
           // solver2.Output(formula);
           // solver2.Output(formula);



        } catch (IOException e) {
            System.err.println("Input error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Parsing error occurred: " + e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }


    }
}
