package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.StatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

/**
 * The structure for CRL Validation Result
 * @author IH
 */
public class CRLStatusInfo extends StatusInfo implements Cloneable
{

	private ECRL mCRL;

	private CRLStatus mCRLStatus;

    public CRLStatusInfo()
	{
		mCRLStatus = CRLStatus.NOT_CHECKED;
	}

	public CRLStatusInfo(ECRL aCRL)
	{
		mCRL = aCRL;
        mCRLStatus = CRLStatus.NOT_CHECKED;
	}

	public CRLStatus getCRLStatus()
	{

		return mCRLStatus;
	}

	public void setCRLStatus(CRLStatus aCRLStatus)
	{
		mCRLStatus = aCRLStatus;
	}

	public ECRL getCRL()
	{
		return mCRL;
	}

	public void setCRL(ECRL aCRL)
	{
		mCRL = aCRL;
	}

    @Override
    protected CRLStatusInfo clone()
    {
        try {
            return (CRLStatusInfo)super.clone();
        } catch (Exception x){
            throw new ESYARuntimeException(x);
        }
    }

    String checkResultsToString()
    {
        String res = "";
        for (int i = 0; i < mDetails.size(); i++)
        {
            res += "  [-] "+mDetails.get(i).toString();
        }
        //res.chop(1);
        return res;
    }


    public String printDetailedValidationReport()
    {
        String issuerLine       = "SİL Yayıncısı : "+mCRL.getTBSCertList().getIssuer().getCommonNameAttribute(); //.toTitle()));
        String crlNumberLine	= "SİL Numarası  : "+mCRL.getCRLNumber();
        String statusLine		= "SİL Durumu    : "+toString();

        String selfChecksStartLine		= "SİL Kontrol Detayları:\n";
        String selfCheckLines			= selfChecksStartLine + checkResultsToString();

        String smCertStatusStartLine,smCertStatusDetails;

        CertificateStatusInfo pSMCertStatus = getSigningCertficateInfo();

        if (pSMCertStatus!=null)
        {
            smCertStatusStartLine	= "İmzalayan Sertifika Geçerlilik Detayları: \n";
            smCertStatusDetails     = pSMCertStatus.printDetailedValidationReport();
        }
        else
        {
            smCertStatusStartLine		= "İmzalayan Sertifika Geçerlilik Detayları: \n";
            //smCertStatusDetails = MAKELINE(iLIndentation,iRIndentation,L("İmzalayan Sertifika Bulunamadı"));;
            smCertStatusDetails = "GÜVENİLİR KÖK\n";
        }
        String smCertStatusLines = smCertStatusStartLine + smCertStatusDetails;


        String report  =	//REPORT_HEADER(iLIndentation,iRIndentation)+
                            issuerLine				+
                            crlNumberLine			+
                            statusLine				+
                            "\n"+
                            selfCheckLines			+
                            "\n"+
                            smCertStatusLines		;

        return report;
    }
}
