package no.priv.kehm.bamodelapplication.lib;

/**
 * Exception to be thrown when a bound value isn't supported by the logarithmic axis<br>
 * <br>
 *
 * @author Kevin Senechal mailto: kevin.senechal@dooapp.com
 */
public class IllegalLogarithmicRangeException extends Exception {

    /**
     * @param message
     */
    public IllegalLogarithmicRangeException(String message) {
        super(message);
    }

}