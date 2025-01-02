#ifndef ASYMETRICKEYDECRYPTOR_H
#define ASYMETRICKEYDECRYPTOR_H

#include "KeyDecryptor.h"
#include "ECertificate.h"
#include "AnahtarBilgisi.h"

namespace esya
{
	class AsymetricKeyDecryptor : public KeyDecryptor
	{
		ECertificate mCert;
		OzelAnahtarBilgisi mOA;

	protected:
		QByteArray _decryptKey();
	public:
		AsymetricKeyDecryptor(EnvelopedData * ipParentData,const ECertificate & iCert, const OzelAnahtarBilgisi & iOA);
		virtual ~AsymetricKeyDecryptor();

	private:

	};


}


#endif // ASYMETRICKEYDECRYPTOR_H
