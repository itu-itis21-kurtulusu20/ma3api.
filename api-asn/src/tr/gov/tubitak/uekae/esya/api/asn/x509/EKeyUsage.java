package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1OpenType;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.x509.KeyUsage;

/**
 * @author ayetgin
 */
public class EKeyUsage extends BaseASNWrapper<KeyUsage> implements ExtensionType
{
    public EKeyUsage(KeyUsage aObject)
    {
        super(aObject);
    }

    public EKeyUsage(boolean[] keyu) throws ESYAException
    {
        super(new KeyUsage());

        //sondaki false degerleri atalim...
        int i = keyu.length-1;
        boolean[] keyu2;
        while( (i>0) && (!keyu[i]))i--;
        keyu2 = new boolean[i+1];
        while(i>=0)keyu2[i]=keyu[i--];

        mObject = new KeyUsage(keyu2);
    }


    public boolean isDigitalSignature(){ return mObject.isSet(KeyUsage.digitalSignature); }
    public boolean isNonRepudiation(){ return mObject.isSet(KeyUsage.nonRepudiation); }
    public boolean isKeyEncipherment(){ return mObject.isSet(KeyUsage.keyEncipherment); }
    public boolean isDataEncipherment(){ return mObject.isSet(KeyUsage.dataEncipherment); }
    public boolean isKeyAgreement(){ return mObject.isSet(KeyUsage.keyAgreement); }
    public boolean isKeyCertSign(){ return mObject.isSet(KeyUsage.keyCertSign); }
    public boolean isCRLSign(){ return mObject.isSet(KeyUsage.cRLSign); }
    public boolean isEncipherOnly(){ return mObject.isSet(KeyUsage.encipherOnly); }
    public boolean isDecipherOnly(){ return mObject.isSet(KeyUsage.decipherOnly); }

    public String getBitString(){
        return mObject.toString();
    }

    public String toString()
    {
	   	String keyUsage = " ";
	    for (int i = 0; i < Math.min(mObject.numbits, 9); i++)
	    {
	    	if (mObject.isSet(i))
	    	{
	    		keyUsage = keyUsage + _keyUsageBul(i) + " ";
	    	}
	    }
	    return keyUsage;
    }

    private String _keyUsageBul (int aBit)
    {
         String result = "";
         switch (aBit)
         {
              case 0:
                   result = CertI18n.message(CertI18n.DIGITALSIGNATURE);
                   break; //"Digital Signature";
              case 1:
                   result = CertI18n.message(CertI18n.NONREPUDIATION);
                   break; //"Non-Repudiation";
              case 2:
                   result = CertI18n.message(CertI18n.KEYENCIPHERMENT);
                   break; //"Key Encipherment";
              case 3:
                   result = CertI18n.message(CertI18n.DATAENCIPHERMENT);
                   break; //"Data Encipherment";
              case 4:
                   result = CertI18n.message(CertI18n.KEYAGREEMENT);
                   break; //"Key Agreement";
              case 5:
                   result = CertI18n.message(CertI18n.CERTSIGN);
                   break; //"Certificate Signing";
              case 6:
                   result = CertI18n.message(CertI18n.CRLSIGN);
                   break; //CRL Signing";
              case 7:
                   result = CertI18n.message(CertI18n.ENCIPHERONLY);
                   break; //"Encipher Only";
              case 8:
                   result = CertI18n.message(CertI18n.DECIPHERONLY);
                   break; //"Decipher Only";
              default:
                   result = "";
         }
         return result + "\n";
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_keyUsage, aCritic, this);
    }

    public Asn1OpenType toOpenType(){
        return new Asn1OpenType(getEncoded());
    }

}
