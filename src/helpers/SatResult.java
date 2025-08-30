package helpers;

import java.util.Map;

/**
 *  Result of running the algorithms and contains:
 * A boolean indicating if the formula is satisfiable.
 * @param satisfiable true if the formula is satisfiable, false otherwise.
 * The certificate (satisfying assignment) in case it exists / is found .
 * @param certificate The satisfying assignment
 *
 */
public record SatResult(boolean satisfiable, Map<Integer, Boolean> certificate) {






    /**
     * @return true if satisfiable, false otherwise.
     */
    @Override
    public boolean satisfiable() {
        return satisfiable;
    }




    /**
     * @return certificate
     */
    @Override
    public Map<Integer, Boolean> certificate() {
        return certificate;
    }





}
