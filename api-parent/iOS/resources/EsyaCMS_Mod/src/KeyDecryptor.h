#ifndef KEYDECRYPTOR_H
#define KEYDECRYPTOR_H

#include <QByteArray>

namespace esya
{
	class EnvelopedData;

	class KeyDecryptor 
	{
	protected:
		EnvelopedData * mpParentData;

		virtual QByteArray _decryptKey() = 0;
	public:
		KeyDecryptor(EnvelopedData * ipParentData);
		virtual ~KeyDecryptor();

		QByteArray decryptKey() ;

	private:



	};

}


#endif // KEYDECRYPTOR_H
