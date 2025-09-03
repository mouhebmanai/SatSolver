import java.io.IOException;
import java.util.*;
import determinsticAlgorithms.* ;
import helpers.*;
import randomizedAlgorithms.*;
public class Main {




    public static void main(String[] args) {
                int allDPLL = 0, allSWR = 0;
           for (int i  = 0 ; i < 10 ; i++) {

               String filePath = "CNF403/CBS_k3_n100_m403_b10_"+i+".cnf";
               try {


                   CnfFormula formula = ParseCnf.parseKCnf(filePath, 10, 10);
                   System.out.println(formula.clauses());
                   //change with respect to the solver

                   CNFSATSolver DPLL = new DPLL();
                  CNFSATSolver dan = new Danstin();
                   SearchWithRadius swr = new SearchWithRadius();
                   long startTime = System.currentTimeMillis();

                   DPLL.solve(formula);
                   System.out.println("dpll   " + i+" \t " + (System.currentTimeMillis()-startTime));                   //  dan.Output(formula);
                   System.out.println("______________________");
                   startTime= System.currentTimeMillis();
                   swr.solve(formula);
                   System.out.println("swr    " + i +" \t " + (System.currentTimeMillis()-startTime));
                   System.out.println("______________________");
                   startTime= System.currentTimeMillis();
                   dan.solve(formula);
                   System.out.println("dan    " + i +" \t " + (System.currentTimeMillis()-startTime));
                   System.out.println("______________________");

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
}
