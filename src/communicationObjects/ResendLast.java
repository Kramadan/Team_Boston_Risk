package communicationObjects;
/**
 * @author Oliver Kamperis and Mark Alston
 */
public class ResendLast extends Request {
	private static final long serialVersionUID = 408296045272957719L;
	
	public ResendLast(String message) {
		super(message);
	}
}
