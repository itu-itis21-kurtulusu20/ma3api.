package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.DistributionPoint;
import tr.gov.tubitak.uekae.esya.asn.x509.DistributionPointName;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralNames;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.asn.x509.RDNSequence;
import tr.gov.tubitak.uekae.esya.asn.x509.RelativeDistinguishedName;

/**
 * @author ahmety
 * date: Feb 5, 2010
 */
public class ECRLDistributionPoint extends BaseASNWrapper<DistributionPoint> {

    protected static Logger logger = LoggerFactory.getLogger(ECRLDistributionPoint.class);
    private AddressType mAddressType = AddressType.DN;
    private String mAddress;
    private EName mCRLIssuer;

    public ECRLDistributionPoint(byte[] aBytes) throws ESYAException {
        super(aBytes, new DistributionPoint());
    }

    public ECRLDistributionPoint(DistributionPoint aObject) {
        super(aObject);
    }

    public ECRLDistributionPoint(DistributionPoint aObject, ECertificate aCertificate) {
        super(aObject);
        internalResolve(aObject, aCertificate.getIssuer().getObject());
    }

    private void internalResolve(DistributionPoint aDP, Name aIssuer){
        //CDP cdp = null;
        DistributionPointName dpName = aDP.distributionPoint;
        if (dpName!=null) {
            switch (dpName.getChoiceID()) {
            case DistributionPointName._FULLNAME:
                /*If the DistributionPointName contains multiple values, each name
                describes a different mechanism to obtain the same CRL. For example,
                the same CRL could be available for retrieval through both LDAP and
                HTTP.*/
                GeneralName[] gnArray = ((GeneralNames) (dpName.getElement())).elements;
                if ((gnArray == null) || (gnArray.length == 0)) {
                    break;
                } else {
                    for (GeneralName gn : gnArray) {
                        mAddress = UtilName.generalName2String(gn);
                        if (mAddress.startsWith(AddressType.HTTP.asString())) {
                            mAddressType = AddressType.HTTP;
                        } else if (mAddress.startsWith(AddressType.LDAP.asString())) {
                            mAddressType = AddressType.LDAP;
                        } else {
                            mAddressType = AddressType.DN;
                        }
                    }
                }
                break;
            case DistributionPointName._NAMERELATIVETOCRLISSUER:
                /*If the DistributionPointName contains the single value
                nameRelativeToCRLIssuer, the value provides a distinguished name
                fragment. The fragment is appended to the X.500 distinguished name
                of the CRL issuer to obtain the distribution point name. If the
                cRLIssuer field in the DistributionPoint is present, then the name
                fragment is appended to the distinguished name that it contains;
                otherwise, the name fragment is appended to the certificate issuer
                distinguished name. The DistributionPointName MUST NOT use the
                nameRealtiveToCRLIssuer alternative when cRLIssuer contains more than
                one distinguished name.*/
                Name crlIssuer = null;
                if (aDP.cRLIssuer != null) {
                    try {
                        crlIssuer = UtilName.string2Name(UtilName.generalName2String(aDP.cRLIssuer.elements[0]), true);
                    } catch (Asn1Exception aEx) {
                        logger.error("Error in ECRLDistributionPoint", aEx);
                        break;
                    }
                } else {
                    crlIssuer = aIssuer;
                }
                //issuer alalım
                RelativeDistinguishedName[] issuerDN = ( (RDNSequence) (crlIssuer.getElement())).elements;
                //dn parcasini alalım
                RelativeDistinguishedName dnFragment = (RelativeDistinguishedName) dpName.getElement();
                //append edelim
                RelativeDistinguishedName[] fullDN = new RelativeDistinguishedName[issuerDN.length + 1];
                System.arraycopy(issuerDN, 0, fullDN, 0, issuerDN.length);
                fullDN[issuerDN.length] = dnFragment;
                //son haline getirelim
                Name point = new Name();
                //point.set_rdnSequence(new RDNSequence(new RelativeDistinguishedName[]{dnFragment}));
                point.set_rdnSequence(new RDNSequence(fullDN));

                mAddressType = AddressType.DN;
                mAddress = UtilName.name2String(point);

            default:
                break;
            }
        }
        if(aDP.cRLIssuer != null) {
            try {
                mCRLIssuer = new EName(UtilName.string2Name(UtilName.generalName2String(aDP.cRLIssuer.elements[0]), true));
            } catch (Asn1Exception aEx) {
                logger.error("Error in ECRLDistributionPoint", aEx);
            }
        }
    }

    public AddressType getAddressType()
    {
        return mAddressType;
    }

    public String getAddress()
    {
        return mAddress;
    }

    public EName getCRLIssuer()
    {
        return mCRLIssuer;
    }

    public EDistributionPointName getDistributionPoint(){
        return new EDistributionPointName(mObject.distributionPoint);
    }
}
