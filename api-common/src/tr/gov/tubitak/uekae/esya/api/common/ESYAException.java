package tr.gov.tubitak.uekae.esya.api.common;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

  public class ESYAException extends Exception {

    public ESYAException() {
    }

    public ESYAException(String aMessage) {
      super(aMessage);
    }

    public ESYAException(String aMessageWithParams, Object...parameters) {
        super(String.format(aMessageWithParams, parameters));
    }

    public ESYAException(String aMessage, Throwable aCause) {
      super(aMessage, aCause);
    }

    public ESYAException(String aMessageWithParams, Throwable aCause, Object...parameters) {
      super(String.format(aMessageWithParams, parameters), aCause);
    }

    public ESYAException(Throwable aCause) {
      super(aCause);
    }
  }