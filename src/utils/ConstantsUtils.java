package utils;

public class ConstantsUtils {

    public static double FORWARD_MAIL_RATIO = 1.0-0.7;
    public static double BASIC_MOVE_PROBABILITY = 0.5;
    public static double IS_SATISFIED_MOVE_PROBA_MODIFIER = -0.35;
    public static double PUSH_REQUEST_UNREAD_MOVE_PROBA_MODIFIER = -0.2;
    public static double PUSH_REQUEST_FORWARDED_MOVE_PROBA_MODIFIER = -0.10;
    public static double PUSH_REQUEST_SATISFIED_MOVE_PROBA_MODIFIER = 0.4;
    public static double PUSH_REQUEST_EXPIRED_OR_UNSATISFIED_MOVE_PROBA_MODIFIER = 0.1;
    public static double IS_PUSHED_PROBA_MODIFIER = 0.2;
    public static double SHORTEST_DIST_TO_TARGET_GREATER_THAN_ACTUAL = -0.15;
    public static double SHORTEST_DIST_TO_TARGET_SMALLER_THAN_ACTUAL = +0.35;
}
