package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import java.util.Calendar;

/**
 * @author ayetgin
 */
public interface TimeProvider
{
    Calendar getCurrentTime();
}
