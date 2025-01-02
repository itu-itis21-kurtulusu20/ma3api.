package tr.gov.tubitak.uekae.esya.api.infra.certstore.template;


public class KeyUsageSearchTemplate
{
	private UsageStatus mDigitalSignature = UsageStatus.FARKETMEZ;
	private UsageStatus mNonRepudition = UsageStatus.FARKETMEZ;
	private UsageStatus mKeyEncipherment = UsageStatus.FARKETMEZ;
	private UsageStatus mDataEncipherment = UsageStatus.FARKETMEZ;
	private UsageStatus mKeyAgreement = UsageStatus.FARKETMEZ;
	private UsageStatus mKeyCertSign = UsageStatus.FARKETMEZ;
	private UsageStatus mCRLSign = UsageStatus.FARKETMEZ;
	private UsageStatus mEncipherOnly = UsageStatus.FARKETMEZ;
	private UsageStatus mDecipherOnly = UsageStatus.FARKETMEZ;
	
	public void setDigitalSignature(UsageStatus aDurum)
	{
		mDigitalSignature = aDurum;
	}
	
	public void setNonRepudition(UsageStatus aDurum)
	{
		mNonRepudition = aDurum;
	}
	
	public void setKeyEncipherment(UsageStatus aDurum)
	{
		mKeyEncipherment = aDurum;
	}
	
	public void setDataEncipherment(UsageStatus aDurum)
	{
		mDataEncipherment = aDurum;
	}
	
	public void setKeyAgreement(UsageStatus aDurum)
	{
		mKeyAgreement = aDurum;
	}
	
	public void setKeyCertSign(UsageStatus aDurum)
	{
		mKeyCertSign = aDurum;
	}

	public void setCRLSign(UsageStatus aDurum)
	{
		mCRLSign = aDurum;
	}
	
	public void setEncipherOnly(UsageStatus aDurum)
	{
		mEncipherOnly = aDurum;
	}
	
	public void setDecipherOnly(UsageStatus aDurum)
	{
		mDecipherOnly = aDurum;
	}
	
	public boolean isUyumluMu(String aKeyUsage)
	{
		return true;
	}
	
	public String sorguOlustur()
	{
		StringBuffer sorgu = new StringBuffer();
		sorgu.append(_degerBul(mDigitalSignature));
		sorgu.append(_degerBul(mNonRepudition));
		sorgu.append(_degerBul(mKeyEncipherment));
		sorgu.append(_degerBul(mDataEncipherment));
		sorgu.append(_degerBul(mKeyAgreement));
		sorgu.append(_degerBul(mKeyCertSign));
		sorgu.append(_degerBul(mCRLSign));
		sorgu.append(_degerBul(mEncipherOnly));
		sorgu.append(_degerBul(mDecipherOnly));
		String sonuc = sorgu.toString();
		return sonuc;
	}
	
	private String _degerBul(UsageStatus aValue)
	{
		if(aValue == UsageStatus.YOK)
			return "0";
		else if(aValue == UsageStatus.VAR)
			return "1";
		else
			return "_";
	}
	
	
}
