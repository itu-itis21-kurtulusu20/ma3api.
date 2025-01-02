
#ifndef __ETIME__
#define __ETIME__

#include "ortak.h"
#include "EException.h"
#include "Explicit.h"

namespace esya
{

	class Q_DECL_EXPORT ETime  : public EASNWrapperTemplate<ASN1T_EXP_Time,ASN1C_EXP_Time>
	{
		int mType;
		QString mTime;

	public:
		ETime(void);
		ETime(int , const QString & );
		ETime(const QByteArray & );
		ETime(const ASN1T_EXP_Time & );
		ETime(const ETime&);
		ETime(const QDateTime&);

		ETime & operator=(const ETime & );
		friend bool operator==(const ETime & ,const ETime & );
		friend bool operator!=(const ETime & ,const ETime & );
		
		int copyFromASNObject(const ASN1T_EXP_Time &);
		int copyToASNObject(ASN1T_EXP_Time &)const;
		void freeASNObject(ASN1T_EXP_Time & oTime)const;
		
		virtual~ETime(void);

		// GETTERS AND SETTERS

		QString getTime()const;
		int getType()const;

		virtual QString toString(const QString & iFormat ="yyyyMMddhhmmss" ) const;
		QDateTime toDateTime()const;

		static ETime fromDateTime(const QDateTime& iDT);
	};

}
#endif

