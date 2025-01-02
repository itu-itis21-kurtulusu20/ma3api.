package tr.gov.tubitak.uekae.esya.api.asn.passport;


import tr.gov.tubitak.uekae.esya.api.common.ESYAException;



/**
 * Created by ahmet.asa on 19.07.2017.
 */
public class ECar extends ECertificateReference
{
   public ECar(byte [] bytes)
   {
        super(bytes);
   }

    public ECar(String countryCode, String holderMnemonic, int sequenceNumber) throws ESYAException
    {
        super(countryCode, holderMnemonic, sequenceNumber);
    }

    public ECar (String cvcName, int sequenceNumber) throws ESYAException{
        super(cvcName,sequenceNumber);
    }




}
