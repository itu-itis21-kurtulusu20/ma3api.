package tr.gov.tubitak.uekae.esya.api.xmlsignature.formats;

import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.ArchiveTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RefsOnlyTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.SigAndRefsTimeStamp;

import java.util.Calendar;

/**
 * @author ayetgin
 */
public class XL extends X
{
    public XL(Context aContext, XMLSignature aSignature)
    {
        super(aContext, aSignature);
    }

    @Override
    public SignatureFormat evolveToT() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X_L, SignatureType.XAdES_T);
    }

    @Override
    public SignatureFormat evolveToC() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X_L, SignatureType.XAdES_C);
    }

    @Override
    public SignatureFormat evolveToX1() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X_L, SignatureType.XAdES_X);
    }

    @Override
    public SignatureFormat evolveToX2() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X_L, SignatureType.XAdES_X);
    }

    @Override
    public SignatureFormat evolveToXL() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_X_L);
    }

    @Override
    public SignatureFormat evolveToA() throws XMLSignatureException
    {
        TimestampConfig tsConfig = mContext.getConfig().getTimestampConfig();
        TSSettings settings = tsConfig.getSettings();

        addTimestampValidationDataForRefsOnlyTS();
        addTimestampValidationDataForSigAndRefsTS();

        ArchiveTimeStamp archiveTimeStamp = new ArchiveTimeStamp(mContext, mSignature, tsConfig.getDigestMethod(), settings);
        UnsignedSignatureProperties usp = mSignature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties();
        usp.addArchiveTimeStamp(archiveTimeStamp);

        return new A(mContext , mSignature);
    }

    protected void addTimestampValidationDataForSigAndRefsTS()
            throws XMLSignatureException
    {
        UnsignedSignatureProperties usp = mSignature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp !=null){
            if (usp.getSigAndRefsTimeStampCount()>0){
                for (int i=0; i<usp.getSigAndRefsTimeStampCount();i++){
                    SigAndRefsTimeStamp srts  = usp.getSigAndRefsTimeStamp(i);
                    addTimestampValidationData(srts, Calendar.getInstance());
                }
            }
        }
    }

    protected void addTimestampValidationDataForRefsOnlyTS()
            throws XMLSignatureException
    {
        UnsignedSignatureProperties usp = mSignature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp !=null){
            if (usp.getRefsOnlyTimeStampCount()>0){
                for (int i=0; i<usp.getRefsOnlyTimeStampCount();i++){
                    RefsOnlyTimeStamp rots  = usp.getRefsOnlyTimeStamp(i);
                    addTimestampValidationData(rots, Calendar.getInstance());
                }
            }
        }
    }

}
