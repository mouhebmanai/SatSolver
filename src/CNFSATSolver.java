import java.util.*;

/**
 * An interface for SAT solver algorithms
 */
public interface CNFSATSolver {
    /**
     * @param formula The CNF formula to be solved.
     * @return A Solution object indicating whether the formula is satisfiable
     * and providing the satisfying assignment if it is.
     */
    SatResult solve(CnfFormula formula );


    /**
     *
     * @param formula the cnf formula to be dealt with
     *  gives the output of the algorithm on the formula
     *  + Benchmarking
     */
      void Output(CnfFormula formula);

}