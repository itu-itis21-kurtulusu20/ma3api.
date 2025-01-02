package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: int2
 * Date: 27.07.2012
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */
public class EDateTool {

    private static Logger logger = LoggerFactory.getLogger(EDateTool.class);

    public static XMLGregorianCalendar getCurrentDtGreg() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = null;
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            logger.error("Error int EDateTool:", e);
        }

        if(datatypeFactory == null) {
            logger.error("datatypeFactory is null");
            return null;
        }

        return datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
    }
}
