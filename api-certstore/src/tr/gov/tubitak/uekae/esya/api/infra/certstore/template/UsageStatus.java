package tr.gov.tubitak.uekae.esya.api.infra.certstore.template;

public enum UsageStatus
{
	YOK (0),
	VAR (1),
	FARKETMEZ (2);
	
	int mValue;
	UsageStatus(int aValue)
	{
		mValue = aValue;
	}

    public int getValue()
	{
		return mValue;
	}
	
}
