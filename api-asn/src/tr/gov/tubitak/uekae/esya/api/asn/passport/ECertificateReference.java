package tr.gov.tubitak.uekae.esya.api.asn.passport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Created by ahmet.asa on 22.09.2017.
 */
public class ECertificateReference {

    protected static Logger logger = LoggerFactory.getLogger(ECertificateReference.class);

    String refStr;

    public ECertificateReference(String countryCode, String holderMnemonic, int sequenceNumber) throws ESYAException {
        init(countryCode, holderMnemonic, sequenceNumber);
    }

    public ECertificateReference(String countryCodeAndholderMnemonic, int sequenceNumber) throws ESYAException {
        String countryCode = countryCodeAndholderMnemonic.substring(0, 2);
        String holderMnemonic = countryCodeAndholderMnemonic.substring(2);

        init(countryCode, holderMnemonic, sequenceNumber);
    }

    private void init(String countryCode, String holderMnemonic, int sequenceNumber) throws ESYAException {
        String sequenceNumberStr = String.format("%05d", sequenceNumber);

        if(sequenceNumberStr.length() > 5)
            throw new ESYAException("SequenceNumber of Passport CV Certificate digit count cannot be longer than 5");

        if(countryCode.length() != 2)
            throw new ESYAException("Country Code of Passport CV Certificate lenght must be 2");

        if(countryCode.length() > 9)
            throw new ESYAException("Holder Mnemonic of Passport CV Certificate cannot be longer than 9");

        refStr = countryCode + holderMnemonic + sequenceNumberStr;
    }

    public ECertificateReference(byte [] bytes) {
        try {
            refStr = new String(bytes, "US-ASCII");
        } catch (Exception ex) {
            logger.warn("Warning in ECertificateReference", ex);
            //ASCII olmama ihtimali yok.
        }
    }

    public byte [] getByteValues() {
        byte [] value = null;
        try {
            value = refStr.getBytes("US-ASCII");
        } catch (Exception ex) {
            logger.warn("Warning in ECertificateReference", ex);
            //ASCII olmama ihtimali yok.
        }
        return value;
    }

    public String getCountryCode() {
        return refStr.substring(0,2);
    }

    public String getHolderMnemonic() {
        int holderMnemonicLen = refStr.length() - 2 - 5;
        return refStr.substring(2, 2 + holderMnemonicLen);
    }

    public int getSequence() {
        String sequenceStr = refStr.substring(refStr.length() - 5, refStr.length());
        return Integer.parseInt(sequenceStr);
    }
}
