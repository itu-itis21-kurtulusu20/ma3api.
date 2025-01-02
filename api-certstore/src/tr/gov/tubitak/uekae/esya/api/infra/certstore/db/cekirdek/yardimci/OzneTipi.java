package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

public enum OzneTipi {
	KOK_SERTIFIKA(1),
	SERTIFIKA (2),
	SIL (3),
    OCSP_BASIC_RESPONSE(4),
    OCSP_RESPONSE(5);

	private int mValue;
	
	OzneTipi(int aValue)
	{
		mValue = aValue;
	}
	
	public int getIntValue()
	{
		return mValue;
	}
	
	//TODO default kismina bak
	public static OzneTipi getNesne(int aTip)
	{
		switch(aTip)
		{
			case 1:return OzneTipi.KOK_SERTIFIKA;
			case 2:return OzneTipi.SERTIFIKA;
			case 3:return OzneTipi.SIL;
            case 4:return OzneTipi.OCSP_BASIC_RESPONSE;
            case 5:return OzneTipi.OCSP_RESPONSE;
			default:return null;
		}
	}

}
