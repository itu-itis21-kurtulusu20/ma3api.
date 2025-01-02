using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ECRLDistributionPoint : BaseASNWrapper<DistributionPoint>
    {
        private AddressType mAddressType = AddressType.DN;

        //private AddressType mAddressType;
        private String mAddress;
        private EName mCRLIssuer;

        public ECRLDistributionPoint(byte[] aBytes) : base(aBytes, new DistributionPoint())
        {
        }

        public ECRLDistributionPoint(DistributionPoint aObject) : base(aObject)
        {
        }

        public ECRLDistributionPoint(DistributionPoint aObject, ECertificate aCertificate)
            : base(aObject)
        {
            internalResolve(aObject, aCertificate.getIssuer().getObject());
        }

        private void internalResolve(DistributionPoint aDP, Name aIssuer)
        {
            //CDP cdp = null;
            DistributionPointName dpName = aDP.distributionPoint;
            if (dpName != null)
            {
                switch (dpName.ChoiceID)
                {
                    case DistributionPointName._FULLNAME:
                        /*If the DistributionPointName contains multiple values, each name
                        describes a different mechanism to obtain the same CRL. For example,
                        the same CRL could be available for retrieval through both LDAP and
                        HTTP.*/
                        GeneralName[] gnArray = ((GeneralNames) (dpName.GetElement())).elements;

                        if ((gnArray == null) || (gnArray.Length == 0))
                        {
                            break;
                        }
                        else
                        {
                            foreach (GeneralName gn in gnArray)
                            {
                                mAddress = UtilName.generalName2String(gn);
                                if (mAddress.StartsWith(AddressType.HTTP.asString()))
                                {
                                    mAddressType = AddressType.HTTP;
                                }
                                else if (mAddress.StartsWith(AddressType.LDAP.asString()))
                                {
                                    mAddressType = AddressType.LDAP;
                                }
                                else
                                {
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
                        if (aDP.cRLIssuer != null)
                        {
                            try
                            {
                                crlIssuer = UtilName.string2Name(
                                    UtilName.generalName2String(aDP.cRLIssuer.elements[0]), true);
                            }
                            catch (Asn1Exception aEx)
                            {
                                //aEx.printStackTrace();
                                Console.WriteLine(aEx.StackTrace);
                                break;
                            }
                        }
                        else
                        {
                            crlIssuer = aIssuer;
                        }
                        //issuer alalım
                        RelativeDistinguishedName[] issuerDN = ((RDNSequence) (crlIssuer.GetElement())).elements;
                        //dn parcasini alalım
                        RelativeDistinguishedName dnFragment = (RelativeDistinguishedName) dpName.GetElement();
                        //append edelim
                        RelativeDistinguishedName[] fullDN = new RelativeDistinguishedName[issuerDN.Length + 1];
                        Array.Copy(issuerDN, 0, fullDN, 0, issuerDN.Length);
                        fullDN[issuerDN.Length] = dnFragment;
                        //son haline getirelim
                        Name point = new Name();
                        //point.set_rdnSequence(new RDNSequence(new RelativeDistinguishedName[]{dnFragment}));
                        point.Set_rdnSequence(new RDNSequence(fullDN));

                        mAddressType = AddressType.DN;
                        mAddress = UtilName.name2String(point);
                        break;

                    default:
                        break;
                }
            }

            if (aDP.cRLIssuer != null)
            {
                try
                {
                    mCRLIssuer =
                        new EName(UtilName.string2Name(UtilName.generalName2String(aDP.cRLIssuer.elements[0]), true));
                }
                catch (Asn1Exception aEx)
                {
                    //aEx.printStackTrace();
                    Console.WriteLine(aEx.StackTrace);
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

        public EDistributionPointName getDistributionPoint()
        {
            return new EDistributionPointName(mObject.distributionPoint);
        }
    }
}