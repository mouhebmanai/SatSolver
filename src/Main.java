import java.io.*;
import java.util.*;
import determinsticAlgorithms.* ;
import helpers.*;
import randomizedAlgorithms.*;
public class Main {



   public static void SCH_Benchmark(long Seed) {
       String buffered = "Sch_Benchmark_Steps_.txt";
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(buffered))) {


           for (int j = 10; j < 50; j = j + 40) {
               writer.write("Backbone size :\t\n" + j);
               writer.newLine();
               for (int i = 6; i < 7; i++) {
                   String filePath = "CNF403/CBS_k3_n100_m403_b"+j+"_"+i+".cnf";
                   CnfFormula formula = ParseCnf.parseKCnf(filePath, 200, 10);
                   double avgofSteps = 0;

                   for (long s = 0; s <= Seed;  s++) {
                       int steps =  new pureSCH(s).sch_steps(formula,Integer.MAX_VALUE);
                   avgofSteps += ((double) steps)/Seed;

                       System.out.println("seed\t"+s+"\t is done\tSteps:\t"+steps);
                       writer.write("steps\t"+s +"\t"+steps+"\t\t");

                   }
                   writer.newLine();
                   writer.write("average\t"+i +"\t"+avgofSteps);
                   writer.newLine();

               }

           }




         } catch (IOException e) {
           System.out.println("error reading");
       }


    }



    public static void main(String[] args) {
        SCH_Benchmark(15);
    }


    public void trying() {

        for (int i  = 0 ; i < 1 ; i++) {

            String filePath = "CNF403/CBS_k3_n100_m403_b10_"+10+".cnf";

            try {
                List<Long> benchmarks = new ArrayList<>(100);

                CnfFormula formula = ParseCnf.parseKCnf(filePath, 35, 10);
                //change with respect to the solver

                CNFSATSolver DPLL = new DPLL();
                Danstin dan = new Danstin(true);
                SearchWithRadius swr = new SearchWithRadius();
                pureSCH sch = new pureSCH();
                long startTime = System.currentTimeMillis();
                dan.solve_after_concat(formula);
                long benchmark = System.currentTimeMillis() - startTime;
                benchmarks.add(benchmark);
                System.out.println("dan   " + i + " \t " + benchmark+ "ms");

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
