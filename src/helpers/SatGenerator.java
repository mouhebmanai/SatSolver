package helpers;

import randomizedAlgorithms.Papadimitriou;

import java.util.*;

public class SatGenerator {

 // generates a satisfying cnfFormula with exactly k variables ( type is k) per clause
 public static CnfFormula generate(long seed ,int n,  int type , int clausesNumber) {
     if (n < type) {
         throw new IllegalArgumentException("number of variables should at least be the number of variables per clause");
     }

     // init
     Set<Integer> variables = new HashSet<>();
     Random ran = new Random(seed);
     Map<Integer,Boolean> satisfyingAssignment = new HashMap<>();
   for (int i  = 1 ; i <= n ; i++) {
       satisfyingAssignment.put(i,ran.nextBoolean());
       variables.add(i);
   }



   // generate formula in each clause set one variable according to  satisfyingAssignment
 Set<List<Integer>> clausesSet = new HashSet<>();
   while (clausesSet.size()< clausesNumber) {
       // create a clause that contains each variable once

       Set<Integer> clauseVariablesSet = new HashSet<>();
       while (clauseVariablesSet.size()  < type) {
           clauseVariablesSet.add(ran.nextInt(n)+1);
       }



       List<Integer> clauseVariables = new ArrayList<>(clauseVariablesSet);
       List<Integer> clause = new ArrayList<>(type);

       int SatisfiedVariableIndex = ran.nextInt(type);

       for (int i = 0 ; i < type ; i++) {
           int var =  clauseVariables.get(i) ;
           if (i == SatisfiedVariableIndex) {
               clause.add(satisfyingAssignment.get(var) ? var : -var ) ;
           } else {
               // for all the other variables don't care
              // clause.add(ran.nextBoolean() ? var : -var);
              clause.add(satisfyingAssignment.get(var) ? -var : var ) ;

           }

       }


          clausesSet.add(clause);


   }

   List<List<Integer>> clausesList = new ArrayList<>(clausesSet);



     return new CnfFormula(type, clausesList, variables , n , clausesNumber ) ;
 }

    public static CnfFormula generate(long seed,Map<Integer,Boolean> satisfyingAssignment ,int n,  int type , int clausesNumber) {
        if (n < type) {
            throw new IllegalArgumentException("number of variables should at least be the number of variables per clause");
        }

        // init
        Set<Integer> variables = new HashSet<>();
        Random ran = new Random(seed);



        // generate formula in each clause set one variable according to  satisfyingAssignment
        Set<List<Integer>> clausesSet = new HashSet<>();
        while (clausesSet.size()< clausesNumber) {
            // create a clause that contains each variable once

            Set<Integer> clauseVariablesSet = new HashSet<>();
            while (clauseVariablesSet.size()  < type) {
                clauseVariablesSet.add(ran.nextInt(n)+1);
            }



            List<Integer> clauseVariables = new ArrayList<>(clauseVariablesSet);
            List<Integer> clause = new ArrayList<>(type);

            int SatisfiedVariableIndex = ran.nextInt(type);

            for (int i = 0 ; i < type ; i++) {
                int var =  clauseVariables.get(i) ;
                if (i == SatisfiedVariableIndex) {
                    clause.add(satisfyingAssignment.get(var) ? var : -var ) ;
                } else {
                    // for all the other variables don't care
                    // clause.add(ran.nextBoolean() ? var : -var);
                    clause.add(satisfyingAssignment.get(var) ? -var : var ) ;

                }

            }


            clausesSet.add(clause);


        }

        List<List<Integer>> clausesList = new ArrayList<>(clausesSet);



        return new CnfFormula(type, clausesList, variables , n , clausesNumber ) ;
    }


    public static void main(String[] args) {
          List<Double> averagePerFormula = new ArrayList<>();
        for ( long i = 1000 ; i < 2000 ; i++) {
            double average = 0 ;
            CnfFormula formula = generate(i, 100, 2, 403);
            for (long seed = 100; seed < 201; seed++) {

                Papadimitriou pap = new Papadimitriou(seed);
              average += (double) pap.papa_steps(formula) / 100.0 ;
            }
            averagePerFormula.add(average);
            System.out.print(average +"\t");
        }

        System.out.println();
        double average = averagePerFormula.stream().mapToDouble(x->x)
                .average()
                .orElse(0.0);


        System.out.println(averagePerFormula);
        System.out.println(average);


    }

}
