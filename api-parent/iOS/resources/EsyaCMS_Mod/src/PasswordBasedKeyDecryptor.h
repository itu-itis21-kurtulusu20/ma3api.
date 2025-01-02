#ifndef PASSWORDBASEDKEYDECRYPTOR_H
#define PASSWORDBASEDKEYDECRYPTOR_H


#include <QString>
#include "KeyDecryptor.h"


namespace esya
{
	class PasswordBasedKeyDecryptor : public KeyDecryptor
	{
	protected:
		QString mPassword;
	
		QByteArray _decryptKey();

	public:
		PasswordBasedKeyDecryptor(EnvelopedData* ipParentData,const QString& iPassword);
		virtual ~PasswordBasedKeyDecryptor();

	private:

	};
}


#endif // PASSWORDBASEDKEYDECRYPTOR_H
