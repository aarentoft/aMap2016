package exceptions;

/**
 * An exception used whenever a a route could not be found on the map. 
 */
@SuppressWarnings("serial")
public class PathNotFoundException extends Throwable {
	/**
	 * @param string The error message.
	 */
	public PathNotFoundException(String string) {
		super(string);
	}
}