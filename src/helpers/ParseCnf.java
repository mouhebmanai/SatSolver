package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Object used for parsing CNF formulas in DIMACS form
 * it currently contains three functions :
 *  parseCnf(String filePath, int MaxN) parses a formula but ensures the number of variables is limited by MAxN
 * parseCnf(String filePath, int MaxN, int MAxC) also ensures the formula is at most in c-CNF
 * ReadCNFFilePath() a simple wrapper for a reader that would read a filepath to cnf formula
 */
public class ParseCnf {
    /**
     * Parsing of the cnf
     * @param filePath The path to the .cnf file.
     * @param MaxN limit the maximum number of variables in the parsed formula (should be between 0 and 100)
     * @param MaxC limits the number of variables in a clause to c, for example if c = 2, the resulting formula is in 2-CNF
     * @return A CnfFormula object containing the clauses and metadata.
     * @throws IOException in case of input error
     * @throws NumberFormatException if the file contains invalid numbers.
     */
    public static CnfFormula parseKCnf(String filePath, int MaxN, int MaxC) throws IOException, NumberFormatException {
        System.out.println("Parsing: " + filePath);
        List<List<Integer>> clauses = new ArrayList<>();
        BufferedReader buffer = new BufferedReader(new FileReader(filePath));
        String line;
        int RealMaxClauseSize = 0;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("c") || line.isEmpty()) {
                continue;
            }
            // first line
            if (!line.startsWith("p cnf")) {

                // clauses
                String[] literals = line.split("\\s+");

                List<Integer> clause = new ArrayList<>();

                for (String literal : literals) {
                    if (!literal.isEmpty()) {
                        int val =Integer.parseInt(literal);
                        if (val  != 0 && Math.abs(val) <= MaxN && clause.size() < MaxC) {
                            clause.add(val);
                        }
                    }
                }

                if (!clause.isEmpty()) {
                    clauses.add(clause);
                    if (clause.size() > RealMaxClauseSize) RealMaxClauseSize = clause.size();
                }
            }

        }

        buffer.close();

        Set<Integer> Variables = new HashSet<>();

        for (List<Integer> clause : clauses ) {
            for (int literal : clause) {
                Variables.add(Math.abs(literal));
            }
        }

        System.out.println("Parsing successful:\t  " +
                Variables.size()+ " variables,\t" +
                clauses.size() + " clauses.");
         System.out.println("The formula is in  " + RealMaxClauseSize + "-CNF \n");

        return new CnfFormula(RealMaxClauseSize, clauses,Variables, Variables.size(), clauses.size());

    }



    /**
     * Takes in a file path from user, could be used later as an option in program args
     * @return path to .cnf file String
     */
    public static String ReadCNFFilePath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the path to the DIMACS CNF file:");
        String filePath = scanner.nextLine();
        scanner.close();
        return filePath;
    }


}