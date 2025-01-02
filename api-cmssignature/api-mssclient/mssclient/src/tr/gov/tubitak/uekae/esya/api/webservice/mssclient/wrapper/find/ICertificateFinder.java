package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find;
/**
 * Created by IntelliJ IDEA.
 * User: int2
 * Date: 29.05.2012
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public interface ICertificateFinder {
    byte[] find(String certSerial);
}
