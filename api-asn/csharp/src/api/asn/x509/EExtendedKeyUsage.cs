using System;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EExtendedKeyUsage : BaseASNWrapper<ExtKeyUsageSyntax>, ExtensionType
    {
        public EExtendedKeyUsage(ExtKeyUsageSyntax aObject)
            : base(aObject)
        {
        }

        public EExtendedKeyUsage(int[][] keyuse)
            : base(null)
        {
            //super(null);
            Asn1ObjectIdentifier[] elem = new Asn1ObjectIdentifier[keyuse.Length];

            for (int i = 0; i < keyuse.Length; i++)
            {
                elem[i] = new Asn1ObjectIdentifier(keyuse[i]);
            }

            mObject = new ExtKeyUsageSyntax(elem);
        }

        public bool hasElement(Asn1ObjectIdentifier aObjId)
        {
            return mObject.elements.Contains<Asn1ObjectIdentifier>(aObjId);
        }

        public EExtension toExtension(bool aCritic)
        {
            return new EExtension(EExtensions.oid_ce_extKeyUsage, aCritic, getBytes());
        }

        public override String ToString()
        {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < mObject.elements.Length; i++)
            {
                result.Append(" [").Append(i + 1).Append("] ");
                result.Append(_gelismisAnahtarKullanimiBul(mObject.elements[i])).Append("\n");
            }
            return result.ToString();
        }

        private String _gelismisAnahtarKullanimiBul(Asn1ObjectIdentifier aOID)
        {
            if (aOID.Equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_serverAuth)))
            {
                return Resource.message(Resource.EKU_SERVER_AUTHENTICATION);
            }
            else if (aOID.Equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_clientAuth)))
            {
                return Resource.message(Resource.EKU_CLIENT_AUTHENTICATION);
            }
            else if (aOID.Equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_codeSigning)))
            {
                return Resource.message(Resource.EKU_CODE_SIGNING);
            }
            else if (aOID.Equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_emailProtection)))
            {
                return Resource.message(Resource.EKU_EMAIL_PROTECTION);
            }
            else if (aOID.Equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_ipsecEndSystem)))
            {
                return Resource.message(Resource.EKU_IPSEC_END_SYSTEM);
            }
            else if (aOID.Equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_ipsecTunnel)))
            {
                return Resource.message(Resource.EKU_IPSEC_TUNNEL);
            }
            else if (aOID.Equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_ipsecUser)))
            {
                return Resource.message(Resource.EKU_IPSEC_USER);
            }
            else if (aOID.Equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_timeStamping)))
            {
                return Resource.message(Resource.EKU_TIME_STAMPING);
            }
            else if (aOID.Equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_OCSPSigning)))
            {
                return Resource.message(Resource.EKU_OCSP_SIGNING);
            }
            else if (aOID.Equals(new Asn1ObjectIdentifier(_ImplicitValues.id_kp_dvcs)))
            {
                return Resource.message(Resource.EKU_DVCS);
            }
            else
            {
                return aOID.ToString();
            }
        }
    }
}
