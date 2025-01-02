package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

public enum SertifikaTipi
{
	KOKSERTIFIKA (1),
	CAPRAZSERTIFIKA (2),
	SMSERTIFIKASI (3),
	HIZMETSERTIFIKASI (4);
	
	private int mValue;
	
	SertifikaTipi(int aValue)
	{
		mValue = aValue;
	}
	
	public int getIntValue()
	{
		return mValue;
	}
	
	//TODO default kismina bak
	public static SertifikaTipi getNesne(int aTip)
	{
		switch(aTip)
		{
			case 1:return SertifikaTipi.KOKSERTIFIKA;
			case 2:return SertifikaTipi.CAPRAZSERTIFIKA;
			case 3:return SertifikaTipi.SMSERTIFIKASI;
			case 4:return SertifikaTipi.HIZMETSERTIFIKASI;
			default:return null;
		}
	}
	
	
}
