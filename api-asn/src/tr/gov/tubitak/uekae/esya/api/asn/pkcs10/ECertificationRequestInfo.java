package tr.gov.tubitak.uekae.esya.api.asn.pkcs10;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.asn.pkcs10.Attributes;
import tr.gov.tubitak.uekae.esya.asn.pkcs10.CertificationRequestInfo;

/**
 * @author ayetgin
 */
public class ECertificationRequestInfo extends BaseASNWrapper<CertificationRequestInfo>
{

    public ECertificationRequestInfo(CertificationRequestInfo aObject)
    {
        super(aObject);
    }

    public ECertificationRequestInfo()
    {
        super(new CertificationRequestInfo());
    }

    public long getVersion()
    {
        return mObject.version.value;
    }

    public void setVersion(long aVersion)
    {
        mObject.version.value = aVersion;
    }

    public EName getSubject()
    {
        return new EName(mObject.subject);
    }

    public void setSubject(EName aSubject)
    {
        mObject.subject = aSubject.getObject();
    }

    public ESubjectPublicKeyInfo getSubjectPKInfo()
    {
        return new ESubjectPublicKeyInfo(mObject.subjectPKInfo);
    }

    public void setSubjectPKInfo(ESubjectPublicKeyInfo aSubjectPKInfo)
    {
        mObject.subjectPKInfo = aSubjectPKInfo.getObject();
    }

    public EAttributes getAttributes()
    {
        return new EAttributes(mObject.attributes);
    }

    public void setAttributes(Attributes aAttributes)
    {
        mObject.attributes = aAttributes;
    }
}
