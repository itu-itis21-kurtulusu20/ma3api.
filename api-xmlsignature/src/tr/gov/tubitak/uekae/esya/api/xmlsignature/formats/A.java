package tr.gov.tubitak.uekae.esya.api.xmlsignature.formats;

import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.ArchiveTimeStamp;

import java.util.Calendar;

/**
 * @author ayetgin
 */
public class A extends XL
{
    public A(Context aContext, XMLSignature aSignature)
    {
        super(aContext, aSignature);
    }

    @Override
    public SignatureFormat evolveToT() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_A, SignatureType.XAdES_T);
    }

    @Override
    public SignatureFormat evolveToC() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_A, SignatureType.XAdES_C);
    }

    @Override
    public SignatureFormat evolveToX1() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_A, SignatureType.XAdES_X);
    }

    @Override
    public SignatureFormat evolveToX2() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_A, SignatureType.XAdES_X);
    }

    @Override
    public SignatureFormat evolveToXL() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_A, SignatureType.XAdES_X_L);
    }

    @Override
    public SignatureFormat evolveToA() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_A);
    }

    @Override
    public SignatureFormat addArchiveTimeStamp() throws XMLSignatureException
    {
        addTimestampValidationDataForLastArchiveTS();

        TimestampConfig tsConfig = mContext.getConfig().getTimestampConfig();
        TSSettings settings = tsConfig.getSettings();
        ArchiveTimeStamp archiveTimeStamp = new ArchiveTimeStamp(mContext, mSignature, tsConfig.getDigestMethod(), settings);

        UnsignedSignatureProperties usp = mSignature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties();
        usp.addArchiveTimeStamp(archiveTimeStamp);
        return this;
    }

    protected void addTimestampValidationDataForLastArchiveTS()
            throws XMLSignatureException
    {
        UnsignedSignatureProperties usp = mSignature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp !=null){
            if (usp.getArchiveTimeStampCount()>0){
                ArchiveTimeStamp ats  = usp.getArchiveTimeStamp(usp.getArchiveTimeStampCount()-1);
                addTimestampValidationData(ats, Calendar.getInstance());
            }
        }

    }
}
