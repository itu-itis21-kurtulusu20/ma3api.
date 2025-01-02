using System;
using System.Collections.Generic;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ECertificatePolicies : BaseASNWrapper<CertificatePolicies>, ExtensionType
    {
        //private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public ECertificatePolicies(CertificatePolicies aObject)
            : base(aObject)
        {
        }
        public ECertificatePolicies(int[][] aPolicies, String[] aYerler, String[] aNoticeler)
            : base(null)
        {
            //super(null);

            if ((aPolicies.Length != aYerler.Length) || (aPolicies.Length != aNoticeler.Length))
                throw new ESYAException();

            Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();

            int s = aPolicies.Length;

            PolicyInformation[] elem = new PolicyInformation[s];
            List<PolicyQualifierInfo> quas = new List<PolicyQualifierInfo>();
            for (int i = 0; i < s; i++) //Her bir policy icin...
            {
                elem[i] = new PolicyInformation(aPolicies[i]);
                quas.Clear();


                //CPS varsa onu encode et.
                if ((aYerler[i] != null) && (!aYerler[i].Equals("")))
                {
                    safeEncBuf.Reset();
                    try
                    {
                        Qualifier qua = new Qualifier();
                        qua.Set_cPSuri(new Asn1IA5String(aYerler[i]));
                        qua.Encode(safeEncBuf);
                        //                         (new Asn1IA5String(aYerler[i])).encode(safeEncBuf);
                    }
                    catch (Exception ex1)
                    {
                        logger.Error("Buraya hic gelmemeli", ex1);
                        throw new ESYAException("CertPolicy eklentisinde adreslerle ilgili bir hata var.", ex1);
                    }
                    quas.Add(
                            new PolicyQualifierInfo(
                                    _ExplicitValues.id_qt_cps,
                                    new Asn1OpenType(safeEncBuf.MsgCopy)
                            )
                    );
                }

                //User Notice varsa onu encode et.
                if ((aNoticeler[i] != null) && (!aNoticeler[i].Equals("")))
                {
                    safeEncBuf.Reset();
                    try
                    {
                        Qualifier qua = new Qualifier();
                        qua.Set_userNotice(new UserNotice(
                                null,
                            // RFC 5280 e gore utf8 olmali. 7 Ags 2010 ED
                            // new DisplayText(DisplayText._BMPSTRING, new Asn1BMPString(aNoticeler[i]))
                            // RFC 5280 BMPSTRING to UTF8STRING 25.04.2017
                                new DisplayText(DisplayText._UTF8STRING, new Asn1UTF8String(aNoticeler[i]))
                            // new DisplayText(DisplayText._IA5STRING,new com.objsys.asn1j.runtime.Asn1IA5String(aNoticeler[i]))
                            // new DisplayText(DisplayText._VISIBLESTRING,new com.objsys.asn1j.runtime.Asn1VisibleString(aNoticeler[i]))
                        ));
                        qua.Encode(safeEncBuf);
                        //                         (new UserNotice(
                        //                             null,
                        //                             new DisplayText(DisplayText._UTF8STRING,new Asn1UTF8String(aNoticeler[i]))
                        //                             )).encode(safeEncBuf);

                    }
                    catch (Exception ex1)
                    {
                        logger.Error("Buraya da hic gelmemeli", ex1);
                        throw new ESYAException("CertPolicy eklentisinde noticelerle ilgili bir hata var.", ex1);
                    }
                    quas.Add(
                            new PolicyQualifierInfo(
                                    _ExplicitValues.id_qt_unotice,
                                    new Asn1OpenType(safeEncBuf.MsgCopy)
                            )
                    );
                }

                //Eger Qualifier'lar varsa onlari ekle.
                if (quas.Count > 0)
                {
                    PolicyQualifierInfo[] qua = new PolicyQualifierInfo[quas.Count];
                    for (int j = 0; j < qua.Length; j++)
                        qua[j] = quas[j];
                    elem[i].policyQualifiers = new PolicyInformation_policyQualifiers(qua);
                }

            }

            mObject = new CertificatePolicies(elem);
        }

        public int indexOf(Asn1ObjectIdentifier aOID)
        {
            if (mObject.elements != null)
                for (int i = 0; i < mObject.elements.Length; i++)
                {
                    PolicyInformation pi = mObject.elements[i];
                    if (pi.policyIdentifier.Equals(aOID))
                        return i;
                }
            return -1;
        }

        public int getPolicyInformationCount()
        {
            return mObject.elements.Length;
        }

        public PolicyInformation getPolicyInformation(int aIndex)
        {
            return mObject.elements[aIndex];
        }

        public void addPolicyInformation(PolicyInformation aPolicyInformation)
        {
            mObject.elements = extendArray(mObject.elements, aPolicyInformation);
        }

        public EExtension toExtension(bool aCritic)
        {
            return new EExtension(EExtensions.oid_ce_certificatePolicies, aCritic, getBytes());
        }

        public override String ToString()
        {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < mObject.elements.Length; i++)
            {
                result.Append(" [" + (i + 1) + "]\n" + Resource.message(Resource.POLICY_IDENTIFIER) + "=" + mObject.elements[i].policyIdentifier.ToString() + "\n");
                if (mObject.elements[i].policyQualifiers != null)
                {
                    for (int j = 0; j < mObject.elements[i].policyQualifiers.elements.Length; j++)
                    {
                        result.Append("  [" + (i + 1) + "." + (j + 1) + "] " + Resource.message(Resource.POLICY_INFO) + "\n");
                        result.Append(Resource.message(Resource.POLICY_ID) + " = ");
                        Asn1ObjectIdentifier oid = mObject.elements[i].policyQualifiers.elements[j].policyQualifierId;
                        Asn1OpenType qualifier = mObject.elements[i].policyQualifiers.elements[j].qualifier;
                        if (oid.Equals(new Asn1ObjectIdentifier(_ExplicitValues.id_qt_cps)))
                        {
                            result.Append(Resource.message(Resource.POLICY_CPS) + "\n" + Resource.message(Resource.POLICY_QUALIFIER) + " = ");
                            Asn1IA5String cps = new Asn1IA5String();
                            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
                            try
                            {
                                qualifier.Encode(enc);
                            }
                            catch (Asn1Exception ex)
                            {
                                logger.Warn("Policy Qualifier encode edilirken hata oluştu", ex);
                                return "";
                            }
                            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(enc.MsgCopy);
                            try
                            {
                                cps.Decode(decBuf);
                            }
                            catch (Exception ex)
                            {
                                logger.Warn("Policy Qualifier decode edilirken hata oluştu", ex);
                                return "";
                            }
                            result.Append(cps.ToString() + "\n");

                        }
                        else if (oid.Equals(new Asn1ObjectIdentifier(_ExplicitValues.id_qt_unotice)))
                        {
                            result.Append(Resource.message(Resource.POLICY_NOTICE) + "\n" + Resource.message(Resource.POLICY_QUALIFIER) + " = ");
                            UserNotice notice = new UserNotice();
                            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
                            try
                            {
                                qualifier.Encode(enc);
                            }
                            catch (Asn1Exception ex)
                            {
                                logger.Warn("Policy Qualifier encode edilirken hata oluştu", ex);
                                return "";
                            }

                            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(enc.MsgCopy);
                            try
                            {
                                notice.Decode(decBuf);
                            }
                            catch (Exception ex)
                            {
                                logger.Warn("UserNotice decode edilirken hata oluştu", ex);
                                return "";
                            }
                            if (notice.explicitText != null)
                            {
                                result.Append((notice.explicitText.GetElement()).ToString() + "\n");
                            }
                            if (notice.noticeRef != null)
                            {
                                result.Append((notice.noticeRef.organization.GetElement()).ToString() + "\n");
                                result.Append(notice.noticeRef.noticeNumbers.elements.ToString() + "\n");
                            }
                        }
                    }
                }
            }
            return result.ToString();
        }
    }
}
