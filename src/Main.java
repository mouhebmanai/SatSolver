import java.io.*;
import java.util.*;
import determinsticAlgorithms.* ;
import helpers.*;
import randomizedAlgorithms.*;
public class Main {




/**
 ** The main class contains some benchmark functions, all of them are below the main function
 **/
    public static void main(String[] args) {

    ppsz_Benchmark(20);
    }

    public  static void initial_cleanup_Benchmark () {
        String buffered = "initial_cleanup_3.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(buffered))) {
            for (int k = 3 ; k < 6 ; k++) {
                writer.write(k+"-CNF:\n");
                for (int j = 100; j <= 500; j = j + 10) {
                    int sum = 0;
                    for (long seed = 1; seed <= 1000; seed++) {
                        CnfFormula formula = SatGenerator.generate(seed, 100, k, j);
                        int num = new RandomizedAlgorithm().clean_Formula(formula).NumberOfVariables();
                        sum += num;

                    }
                    writer.write((j) + "=" + sum / 1000.0 + "\t|\t" + (j % 100 == 0 ? "\n" : ""));
                    System.out.println(j + "\t" + sum / 1000.0);
                    writer.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("error reading");
        }
    }
   public  static void papa_Benchmark (long Seed) {
       String buffered = "2_sat_benchmark_sch_different_numbers2.txt";
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(buffered))) {
           for (int n = 125 ; n <= 125; n= n+25) {
               writer.write("n= \t"+n +"\n");
               List<Double> averagePerFormula = new ArrayList<>();
               writer.write("[|\t");
               for (long i = 1000; i < 2000; i++) {
                   double average = 0;
                  System.out.println(i);
                   CnfFormula formula = SatGenerator.generate(i, n, 3, (int) (4.03*n));

                   for (long seed = Seed; seed < Seed + 100; seed++) {

               /*  Papadimitriou pap = new Papadimitriou(seed);
                   average += (double) pap.papa_steps(formula) / 100.0 ; */
                       pureSCH Sch = new pureSCH(seed);
                       average += (double) Sch.sch_steps(formula, Integer.MAX_VALUE) / 100.0;

                   }
                   averagePerFormula.add(average);
                   writer.write((i - 999) + "=" + average + "\t|\t" + (i % 10 == 0 ? "\n" : ""));
                   writer.flush();

               }
               writer.write("|]\n\n\n");

               double average = averagePerFormula.stream().mapToDouble(x -> x)
                       .average()
                       .orElse(0.0);


               writer.write("average: " + average + "\n");
           }

       } catch (IOException e) {
           System.out.println("error reading");
       }
   }
    public static void ppsz_Benchmark(long Seed) {
        String buffered = "PPSZ_test_50_variables_20_instances_2_limit_50_000.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(buffered))) {
                    double sum = 0;
                //writer.write("Backbone size :\t\n" + j);
                writer.newLine();
                for (int i = 1 ; i < 21; i++) {
                    String filePath = "uf50/uf50-0"+i+".cnf";
                   CnfFormula formula = ParseCnf.parseKCnf(filePath, 101, 10);

                    double avgofSteps = 0;
                  PPSZ ppsz = new PPSZ(1,Integer.MAX_VALUE,4);
                    List<List<Integer>> clauses = ppsz.Resolve(formula.clauses(),4);
                    if (clauses.contains(new ArrayList<Integer>())) throw new RuntimeException(" ");
                    CnfFormula resolvedFormula = new CnfFormula(formula.type(),
                            clauses,
                            formula.variables(),
                            formula.NumberOfVariables(),
                            clauses.size() );
                    writer.write("clauses:"+ clauses.size() +"\n");
                    System.out.println("clauses:"+ clauses.size());
                    for (long s = 1; s <=  Seed;  s++) {
                     // int steps =  new SCH_withH(s).schH_No_CW_steps(formula,Integer.MAX_VALUE);
                    // int steps = new pureSCH(s).sch_steps(formula,Integer.MAX_VALUE);
                        ppsz =  new PPSZ(s,Integer.MAX_VALUE,4);


                       int steps = ppsz.ppsz_steps(resolvedFormula);
                        if (steps == -1)  {
                            System.out.println("problem");
                            break;
                        }
                        avgofSteps += ((double) steps)/Seed;

                        System.out.println("seed\t"+s+"\t\tSteps:\t"+steps);
                        writer.write("steps\t"+s +"\t"+steps+"\t\t");
                        writer.flush();
                           if(s==20) sum += avgofSteps;
                    }
                    writer.newLine();
                    writer.write("average\t"+i +"\t"+avgofSteps);
                    writer.newLine();
                    writer.flush();

                }
            System.out.println(sum);





        } catch (IOException e) {
            System.out.println("error reading");
        }


    }

    public static void ppz_Benchmark(long Seed) {
        String buffered = "sch_test_50_variables_20_instances.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(buffered))) {
            double sum = 0;
            double avgofSteps = 0;

            writer.newLine();
            for (int i = 1 ; i < 21; i++) {
                String filePath = "uf50/uf50-0"+i+".cnf";
                CnfFormula formula = ParseCnf.parseKCnf(filePath, 101, 10);


                for (long s = 1; s <=  Seed;  s++) {
                    // int steps =  new SCH_withH(s).schH_No_CW_steps(formula,Integer.MAX_VALUE);
                     int steps = new pureSCH(s).sch_steps(formula,Integer.MAX_VALUE);
             //      PPSZ ppsz =  new PPSZ(s,Integer.MAX_VALUE,4);
                   // int steps = ppsz.ppsz_steps(formula);
                    if (steps == -1)  {
                        System.out.println("problem");
                        break;
                    }
                    avgofSteps += ((double) steps)/Seed;

                    System.out.println("seed\t"+s+"\t\tSteps:\t"+steps);
                    writer.write("steps\t"+s +"\t"+steps+"\t\t");
                    writer.flush();
                    if(s==20) sum += avgofSteps;
                }
                writer.newLine();
                writer.write("average\t"+i +"\t"+avgofSteps);
                writer.newLine();
                writer.flush();

            }
            System.out.println(sum);





        } catch (IOException e) {
            System.out.println("error reading");
        }


    }

    public static void SCH_Benchmark(long Seed) {
        String buffered = "Sch_Benchmark_Steps_nextliteral_50.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(buffered))) {


            for (int j = 50; j < 90; j = j + 40) {
                writer.write("Backbone size :\t\n" + j);
                writer.newLine();
                for (int i = 0; i < 15; i++) {
                    String filePath = "CNF403/CBS_k3_n100_m403_b"+j+"_"+i+".cnf";
                    CnfFormula formula = ParseCnf.parseKCnf(filePath, 200, 10);
                    double avgofSteps = 0;

                    for (long s = 1; s <= Seed;  s++) {

                        int steps =  new SCH_withH(s).schH_No_CW_steps(formula,Integer.MAX_VALUE);
                        // int steps =  new pureSCH(s).sch_steps(formula,Integer.MAX_VALUE);
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

    public static void SCH_ratio_Benchmark() {
        String buffered = "Sch_ratio_benchmark5";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(buffered))) {



                writer.newLine();
                for (int i = 0 ; i < 15; i++) {
                    String filePath = "CNF403/CBS_k3_n100_m403_b"+50+"_"+i+".cnf";
                    CnfFormula formula = ParseCnf.parseKCnf(filePath, 101, 10);


                    for (long s = 15; s <= 15 ;  s++) {

                         // double steps = new SCH_withH(s).schH_No_CW_steps(formula,1000)/1000.0;
                         double steps = new pureSCH(s).sch_steps(formula,1000)/1000.0;

                        System.out.println("formula\t"+i+"\t\tSteps:\t"+steps);
                        writer.write("steps\t"+i +"\t"+steps+"\t\t");
                        writer.flush();

                    }
                    writer.newLine();
                    writer.flush();

                }






        } catch (IOException e) {
            System.out.println("error reading");
        }


    }
   public static void SCH_doubleSided_test() {
        String buffered =  "doubleSided.txt";
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(buffered))) {
        Random random = new Random(123);
        for (int kept = 0 ; kept <= 50; kept++) {
            System.out.println("kept "+ kept);
            int num = 0;
            for (int seed = 1000; seed < 101000; seed++) {
                Set<Integer> variables = new HashSet<>();
                Random ran = new Random(seed);
                Map<Integer, Boolean> satisfyingAssignment = new HashMap<>();
                for (int i = 1; i <= 100; i++) {
                    satisfyingAssignment.put(i, ran.nextBoolean());
                    variables.add(i);
                }
                // generate formula
                CnfFormula generatedFormula = SatGenerator.generate(seed, satisfyingAssignment,100,3,403);

                Map<Integer, Boolean> satisfyingAssignmentflipped = new RandomizedAlgorithm().flip(satisfyingAssignment) ;
                // find a set to return as it was
                Set<Integer> kept_variables = new HashSet<>();
                while (kept_variables.size() < kept) {
                    kept_variables.add(  ran.nextInt(100)+1);
                }
                // flip the kept again
               for(Integer integer : kept_variables) {
                   satisfyingAssignmentflipped.put(integer, satisfyingAssignment.get(integer));
               }
              if(new SCH_withH(random.nextInt(10_000)+1).doubleSided_test(generatedFormula,satisfyingAssignmentflipped)) num++;


            }

            writer.write("kept:\t"+ kept+"\t"+num+"\n");
            writer.flush();

        } } catch (Exception e) {
           throw new RuntimeException("file failure");
       }

   }

    public void trying() {

        for (int i  = 0 ; i < 1 ; i++) {

            String filePath = "CNF403/CBS_k3_n100_m403_b10_"+10+".cnf";

            try {
                List<Long> benchmarks = new ArrayList<>(100);

                CnfFormula formula = ParseCnf.parseKCnf(filePath, 35, 10);
                //change with respect to the solver

                CNFSATSolver DPLL = new DPLL();
                Dantsin dan = new Dantsin(true);
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
