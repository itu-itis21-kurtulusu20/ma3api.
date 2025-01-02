#ifndef GROUPKEYDECRYPTOR_H
#define GROUPKEYDECRYPTOR_H

#include "ECertificate.h"
#include "AnahtarBilgisi.h"

#include "KeyDecryptor.h"

namespace esya
{
	class GroupKeyDecryptor : public KeyDecryptor
	{
	protected:
		QList< QPair<ECertificate,OzelAnahtarBilgisi> > mCertKeyList;

		QByteArray _decryptKey();

	public:
		GroupKeyDecryptor(EnvelopedData* ipParentData,const QList< QPair<ECertificate,OzelAnahtarBilgisi> >& iCertKeyList);
		virtual ~GroupKeyDecryptor();

	private:

	};
}

#endif // GROUPKEYDECRYPTOR_H
