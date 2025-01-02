package tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params;


import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

/*
	ecdhDecryptorParams.getKeyWrapAlgOid()        --> Şifreleme anahtarının Wrap'lenmesinde kullanılan algoritmanın oid'i.
	ecdhDecryptorParams.getKeyEncAlgOid()         --> KeyAgreement Algoritmasının oid'i.
	ecdhDecryptorParams.getSenderPublicKeyAlgId() --> Şifreleme aşamasında oluşturulan EC anahtar çiftinin public anahtarının algoritma id'si.
	ecdhDecryptorParams.getSenderPublicKey()      --> Şifreleme aşamasında oluşturulan EC anahtar çiftinin public anahtarının değeri.
	ecdhDecryptorParams.getWrappedKey()           --> Şifreleme'yi yapan anahtarın Wrap'Lenmiş hali. Wrap'leme anahtarı, şifrelemeyi oluşturacak kişinin publicKey'i ve gecici olarak oluşturulan EC anahtar çiftinin private anahtarı kullanılarak oluşturulur.
	ecdhDecryptorParams.getukm()                  --> API'de şifreleme yaparken bu alanla ilgili herhangi bir işlem yapılmıyor.
*/
public class ECDHDecryptorParams implements IDecryptorParams
{

	private byte [] mWrappedKey;
	private int  [] mKeyEncAlgOid;
	private int  [] mKeyWrapAlgOid;
	private byte [] mSenderPublicKey;
    private AlgorithmIdentifier senderPublicKeyAlgId;
    private byte [] mukm;


    public AlgorithmIdentifier getSenderPublicKeyAlgId() {
        return senderPublicKeyAlgId;
    }

    public ECDHDecryptorParams(byte [] wrappedKey, int  [] keyEncAlgOid, int  [] keyWrapAlgOid, AlgorithmIdentifier senderPublicKeyAlgId,byte [] senderPublicKey, byte [] ukm)
	{
		this.mWrappedKey = wrappedKey;
		this.mKeyEncAlgOid = keyEncAlgOid;
		this.mKeyWrapAlgOid = keyWrapAlgOid;
        this.senderPublicKeyAlgId = senderPublicKeyAlgId;

		this.mSenderPublicKey = senderPublicKey;
		this.mukm = ukm;
	}
	
	public byte[] getWrappedKey() {
		return mWrappedKey;
	}


	public int[] getKeyEncAlgOid() {
		return mKeyEncAlgOid;
	}


	public int[] getKeyWrapAlgOid() {
		return mKeyWrapAlgOid;
	}


	public byte[] getSenderPublicKey() {
		return mSenderPublicKey;
	}


	public byte[] getukm() {
		return mukm;
	}
}
