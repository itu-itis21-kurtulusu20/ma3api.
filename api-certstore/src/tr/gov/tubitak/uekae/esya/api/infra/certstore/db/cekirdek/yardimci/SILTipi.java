package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

public enum SILTipi
{
	BASE (1),
	DELTA (2);
	
	private int mValue;
	SILTipi(int aValue)
	{
		mValue = aValue;
	}
	
	public int getIntValue()
	{
		return mValue;
	}
	
	//TODO null donmesi durumu
	public static SILTipi getNesne(int aTip)
	{
		switch(aTip)
		{
			case 1:return SILTipi.BASE;
			case 2:return SILTipi.DELTA;
			default:return null;
		}
		
	}
}
