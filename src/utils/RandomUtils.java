package utils;

import utils.exceptions.InvalidBoundsException;

import java.util.List;
import java.util.Random;

/**
 * Une classe utilitaire pour les nombres aléatoires que nous utilisons sur de nombreux projets
 */
public class RandomUtils {

    private static final int BOUND_FOR_DOUBLES = Integer.MAX_VALUE;

    /***
     * @return a random double value >= 0 and <1
     */
    public static double randDouble() {
        return (new Random().nextDouble());
    }

    /***
     * @return a random integer value >= 0
     */
    public static int randInt() {
        return new Random().nextInt();
    }

    /**
     * @param minNb exclusive upper bound
     * @return a random integer value >= minNb
     */
    public static int randIntMinVal(int minNb) {
        return randInt() + minNb;
    }

    /**
     * @param maxNb exclusive upper bound
     * @return a random integer value >= 0 and < maxNb
     */
    public static int randIntMavVal(int maxNb) {
        if (maxNb <= 0) throw new InvalidBoundsException(String.format("Error, maximum value '%d' is inferior or equal to zero", maxNb));
        return new Random().nextInt(maxNb);
    }

    /**
     * @param minNb inclusive lower bound
     * @param maxNb exclusive upper bound
     * @return a random integer value >= minNb and < maxNb
     */
    public static int randIntBetween(int minNb, int maxNb) {
        if (minNb >= maxNb) throw new InvalidBoundsException(String.format("Error, minimum value '%d' is inferior or equal to max value '%d'", minNb, maxNb));
        return (randIntMavVal(maxNb - minNb) + minNb);
    }

    /**
     * @param minNb inclusive lower bound
     * @param maxNb exclusive upper bound
     * @param values list of values that the random number must not be in
     * @return a random integer value >= minNb and < maxNb
     */
    public static int randIntBetweenAndNot(int minNb, int maxNb, List<Integer> values) {
        int res = randIntBetween(minNb, maxNb);
        while(values.contains(res)){
            res = randIntBetween(minNb, maxNb);
        }
        return res;
    }
}
