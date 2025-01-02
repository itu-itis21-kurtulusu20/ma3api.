package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import java.security.cert.CertStoreException;

public enum GuvenlikSeviyesi
{
	LEGAL (1, "legal"),
	ORGANIZATIONAL (2, "organizational"),
	PERSONAL (3, "personal");
	
	private int mValue;
	private String mDefinition;
	
	GuvenlikSeviyesi(int aValue, String aDefinition)
	{
		mValue = aValue;
		mDefinition = aDefinition;
	}
	
	public int getIntValue()
	{
		return mValue;
	}
	
	public String getDefinition()
	{
		return mDefinition;
	}
	
	
	public boolean equals(GuvenlikSeviyesi guvenlikSeviyesi)
	{
		if(this.mValue == guvenlikSeviyesi.getIntValue())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	//TODO default kismina bak
	public static GuvenlikSeviyesi getNesne(int aTip)
	{
		switch(aTip)
		{
			case 1:return GuvenlikSeviyesi.LEGAL;
			case 2:return GuvenlikSeviyesi.ORGANIZATIONAL;
			case 3:return GuvenlikSeviyesi.PERSONAL;
			default:return null;
		}
	}
	
	public static GuvenlikSeviyesi getNesne(String aDefinition) throws CertStoreException
	{
		GuvenlikSeviyesi [] values = GuvenlikSeviyesi.values();
		for (GuvenlikSeviyesi value : values) 
		{
			if(value.getDefinition().equals(aDefinition))
				return value;
		}
		
		throw new CertStoreException("Unknown Güvenlik seviyesi");
	}
	
}
