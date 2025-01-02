package tr.gov.tubitak.uekae.esya.api.asn.passport;


import tr.gov.tubitak.uekae.esya.api.common.ESYAException;


/**
 * Created by ahmet.asa on 28.07.2017.
 */
public class EChr extends ECertificateReference
{
    public EChr(byte [] bytes)
    {
        super(bytes);
    }

    public EChr(String countryCode, String holderMnemonic, int sequenceNumber) throws ESYAException
    {
        super(countryCode, holderMnemonic, sequenceNumber);
    }

}
