package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;

public enum OzetTipi {

	SHA1 (1),
	SHA224 (2),
	SHA256 (3),
	SHA384( 4),
	SHA512 (5);

	private int mValue;

	OzetTipi(int aValue)
	{
		mValue = aValue;
	}

	public int getIntValue()
	{
		return mValue;
	}


    public static OzetTipi getNesne(int aTip)
    {
        switch(aTip)
        {
            case 1:return OzetTipi.SHA1;
            case 2:return OzetTipi.SHA224;
            case 3:return OzetTipi.SHA256;
            case 4:return OzetTipi.SHA384;
            case 5:return OzetTipi.SHA512;
            default:return null;
        }
    }


    public DigestAlg toDigestAlg()
    {
        switch (mValue)
        {
            case 1: return DigestAlg.SHA1;
            case 2: return DigestAlg.SHA224;
            case 3: return DigestAlg.SHA256;
            case 4: return DigestAlg.SHA384;
            case 5: return DigestAlg.SHA512;
            default: return null;
        }
    }


    public static OzetTipi fromDigestAlg(DigestAlg aDigestAlg)
    {
        switch (aDigestAlg)
        {
            case SHA1  : return OzetTipi.SHA1;
            case SHA224: return OzetTipi.SHA224;
            case SHA256: return OzetTipi.SHA256;
            case SHA384: return OzetTipi.SHA384;
            case SHA512: return OzetTipi.SHA512;
            default: return null;
        }
    }
   
}
