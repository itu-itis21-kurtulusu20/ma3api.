package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.util.Arrays;
import java.util.Vector;

/**
 * @author ahmet.yetgin
 */
public class ECertificatePolicies
        extends BaseASNWrapper<CertificatePolicies>
        implements ExtensionType
{
    private static Logger logger = LoggerFactory.getLogger(ECertificatePolicies.class);

    public ECertificatePolicies(CertificatePolicies aObject)
    {
        super(aObject);
    }

    public ECertificatePolicies(int[][] aPolicies, String[] aYerler, String[] aNoticeler) throws ESYAException
    {
        super(null);
        
        if ((aPolicies.length != aYerler.length) || (aPolicies.length != aNoticeler.length))
            throw new ESYAException();

        Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();

        int s = aPolicies.length;

        PolicyInformation[] elem = new PolicyInformation[s];
        Vector<PolicyQualifierInfo> quas = new Vector<PolicyQualifierInfo>();
        for (int i = 0; i < s; i++) //Her bir policy icin...
        {
            elem[i] = new PolicyInformation(aPolicies[i]);
            quas.clear();


            //CPS varsa onu encode et.
            if ((aYerler[i] != null) && (!aYerler[i].equals(""))) {
                safeEncBuf.reset();
                try {
                    Qualifier qua = new Qualifier();
                    qua.set_cPSuri(new Asn1IA5String(aYerler[i]));
                    qua.encode(safeEncBuf);
//                         (new Asn1IA5String(aYerler[i])).encode(safeEncBuf);
                }
                catch (Exception ex1) {
                    logger.error("Buraya hic gelmemeli", ex1);
                    throw new ESYAException("CertPolicy eklentisinde adreslerle ilgili bir hata var.", ex1);
                }
                quas.add(
                        new PolicyQualifierInfo(
                                _ExplicitValues.id_qt_cps,
                                new Asn1OpenType(safeEncBuf.getMsgCopy())
                        )
                );
            }

            //User Notice varsa onu encode et.
            if ((aNoticeler[i] != null) && (!aNoticeler[i].equals(""))) {
                safeEncBuf.reset();
                try {
                    Qualifier qua = new Qualifier();
                    qua.set_userNotice(new UserNotice(
                            null,
                            // RFC 5280 e gore utf8 olmali. 7 Ags 2010 ED
                            // new DisplayText(DisplayText._BMPSTRING, new com.objsys.asn1j.runtime.Asn1BMPString(aNoticeler[i]))
                            // RFC 5280 BMPSTRING to UTF8STRING 25.04.2017
                            new DisplayText(DisplayText._UTF8STRING, new com.objsys.asn1j.runtime.Asn1UTF8String(aNoticeler[i]))
                            // new DisplayText(DisplayText._IA5STRING,new com.objsys.asn1j.runtime.Asn1IA5String(aNoticeler[i]))
                            // new DisplayText(DisplayText._VISIBLESTRING,new com.objsys.asn1j.runtime.Asn1VisibleString(aNoticeler[i]))
                    ));
                    qua.encode(safeEncBuf);
//                         (new UserNotice(
//                             null,
//                             new DisplayText(DisplayText._UTF8STRING,new Asn1UTF8String(aNoticeler[i]))
//                             )).encode(safeEncBuf);

                }
                catch (Exception ex1) {
                    logger.error("Buraya da hic gelmemeli", ex1);
                    throw new ESYAException("CertPolicy eklentisinde noticelerle ilgili bir hata var.", ex1);
                }
                quas.add(
                        new PolicyQualifierInfo(
                                _ExplicitValues.id_qt_unotice,
                                new Asn1OpenType(safeEncBuf.getMsgCopy())
                        )
                );
            }

            //Eger Qualifier'lar varsa onlari ekle.
            if (quas.size() > 0) {
                PolicyQualifierInfo[] qua = new PolicyQualifierInfo[quas.size()];
                for (int j = 0; j < qua.length; j++)
                    qua[j] = quas.elementAt(j);
                elem[i].policyQualifiers = new PolicyInformation_policyQualifiers(qua);
            }

        }

        mObject = new CertificatePolicies(elem);
    }    

    public int indexOf(Asn1ObjectIdentifier aOID)
    {
        if (mObject.elements != null)
            for (int i = 0; i < mObject.elements.length; i++) {
                PolicyInformation pi = mObject.elements[i];
                if (pi.policyIdentifier.equals(aOID))
                    return i;
            }
        return -1;
    }

    public int getPolicyInformationCount()
    {
        return mObject.elements.length;
    }

    public PolicyInformation getPolicyInformation(int aIndex)
    {
        return mObject.elements[aIndex];
    }

    public void addPolicyInformation(PolicyInformation aPolicyInformation)
    {
        mObject.elements = extendArray(mObject.elements, aPolicyInformation);
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_certificatePolicies, aCritic, this);
    }

    @Override
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < mObject.elements.length; i++) {
            result.append(" [" + (i + 1) + "]\n" + CertI18n.message(CertI18n.POLICY_IDENTIFIER) + "=" + mObject.elements[i].policyIdentifier.toString() + "\n");
            if (mObject.elements[i].policyQualifiers != null) {
                for (int j = 0; j < mObject.elements[i].policyQualifiers.elements.length; j++) {
                    result.append("  [" + (i + 1) + "." + (j + 1) + "] " + CertI18n.message(CertI18n.POLICY_INFO) + "\n");
                    result.append(CertI18n.message(CertI18n.POLICY_ID) + " = ");
                    Asn1ObjectIdentifier oid = mObject.elements[i].policyQualifiers.elements[j].policyQualifierId;
                    Asn1OpenType qualifier = mObject.elements[i].policyQualifiers.elements[j].qualifier;
                    if (oid.equals(new Asn1ObjectIdentifier(_ExplicitValues.id_qt_cps))) {
                        result.append(CertI18n.message(CertI18n.POLICY_CPS) + "\n" + CertI18n.message(CertI18n.POLICY_QUALIFIER) + " = ");
                        Asn1IA5String cps = new Asn1IA5String();
                        Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
                        try {
                            qualifier.encode(enc);
                        } catch (Asn1Exception ex) {
                            logger.warn("Policy Qualifier encode edilirken hata oluştu", ex);
                            return "";
                        }
                        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(enc.getMsgCopy());
                        try {
                            cps.decode(decBuf);
                        } catch (Exception ex) {
                            logger.warn("Policy Qualifier decode edilirken hata oluştu", ex);
                            return "";
                        }
                        result.append(cps.toString() + "\n");

                    } else if (oid.equals(new Asn1ObjectIdentifier(_ExplicitValues.id_qt_unotice))) {
                        result.append(CertI18n.message(CertI18n.POLICY_NOTICE) + "\n" + CertI18n.message(CertI18n.POLICY_QUALIFIER) + " = ");
                        UserNotice notice = new UserNotice();
                        Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
                        try {
                            qualifier.encode(enc);
                        } catch (Asn1Exception ex) {
                            logger.warn("Policy Qualifier encode edilirken hata oluştu", ex);
                            return "";
                        }

                        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(enc.getMsgCopy());
                        try {
                            notice.decode(decBuf);
                        } catch (Exception ex) {
                            logger.warn("UserNotice decode edilirken hata oluştu", ex);
                            return "";
                        }
                        if (notice.explicitText != null) {
                            result.append((notice.explicitText.getElement()).toString() + "\n");
                        }
                        if (notice.noticeRef != null) {
                            result.append((notice.noticeRef.organization.getElement()).toString() + "\n");
                            result.append(Arrays.toString(notice.noticeRef.noticeNumbers.elements) + "\n");
                        }
                    }
                }
            }
        }
        return result.toString();
    }

}

