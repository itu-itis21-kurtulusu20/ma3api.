#ifndef __EASNWRAPPER__
#define __EASNWRAPPER__


#include <QByteArray>
#include <QString>
#include <QFile>
#include "EException.h"
#include "asn1CppTypes.h"
#include "esyaOrtak.h"

#define ST_ASN_OBJECT "ASN Wrapped Object"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper ata sýnýfý. 
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT EASNWrapper
	{
	public:

		EASNWrapper(void);

	public:
		
		virtual QByteArray	getEncodedBytes() const = 0;
		virtual int			constructObject(const QByteArray& )	= 0;


		virtual QString toString() const
		{
			return QString(ST_ASN_OBJECT);
		};

		virtual int  write2File(const QString &) const;
		virtual int  loadFromFile(const QString &);

		virtual ~EASNWrapper(void);

		void copyToASNObject() const;
		void copyFromASNObject();
		void * getASNCopy(QByteArray) const;
		static void * getASNCopyOf(QByteArray);
		static void freeASNObject(void * );



	};

}

#endif 

