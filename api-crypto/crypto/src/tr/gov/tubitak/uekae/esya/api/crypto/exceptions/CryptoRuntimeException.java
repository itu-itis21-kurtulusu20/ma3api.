package tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;


/**
 * <p>Title: CC</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 *
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class CryptoRuntimeException
        extends ESYARuntimeException
{
    public CryptoRuntimeException()
    {
        super(GenelDil.mesaj(GenelDil.KRIPTO_GENEL_HATA));
    }


    public CryptoRuntimeException(String aMesaj)
    {
        super(aMesaj);
    }


    public CryptoRuntimeException(String aMesaj, Throwable aEx)
    {
        super(aMesaj, aEx);
    }

}