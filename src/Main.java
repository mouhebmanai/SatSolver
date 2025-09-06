import java.io.IOException;
import java.util.*;
import determinsticAlgorithms.* ;
import helpers.*;
import randomizedAlgorithms.*;
public class Main {




    public static void main(String[] args) {

           for (int i  = 0 ; i < 1 ; i++) {

               String filePath = "CNF403/CBS_k3_n100_m403_b50_"+i+".cnf";
               try {
                   List<Long> benchmarks = new ArrayList<>(100);
               for (int j = 50 ; j< 100 ; j++) {
                   CnfFormula formula = ParseCnf.parseKCnf(filePath, j, 10);
                   //change with respect to the solver

                   CNFSATSolver DPLL = new DPLL();
                   CNFSATSolver dan = new Danstin();
                   SearchWithRadius swr = new SearchWithRadius();
                   pureSCH sch = new pureSCH();
                   long startTime = System.currentTimeMillis();
                   sch.solve(formula);
                   long benchmark = System.currentTimeMillis() - startTime;
                   benchmarks.add(benchmark);
                   System.out.println("sch   " + i + " \t " + benchmark);
               }
              /*     DPLL.solve(formula);
                   System.out.println("dpll   " + i+" \t " + (System.currentTimeMillis()-startTime));                   //  dan.Output(formula);
                   System.out.println("______________________");
                   startTime= System.currentTimeMillis();
                   swr.solve(formula);
                   System.out.println("swr    " + i +" \t " + (System.currentTimeMillis()-startTime));
                   System.out.println("______________________");
                   startTime= System.currentTimeMillis();
                   dan.solve(formula);
                   System.out.println("dan    " + i +" \t " + (System.currentTimeMillis()-startTime));
                   System.out.println("______________________"); */

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
