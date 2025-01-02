
#ifndef __ORTAK__
#define __ORTAK__

#include "EException.h"
#include "EASNWrapperTemplate.h"
#include <QBitArray>
#include "ASN1TOctStr.h"
#include "asn1CppTypes.h"
#include "asn1compat.h"


#define MAX_BUF_SIZE 10000

namespace esya
{

	Q_DECL_EXPORT char * myStrDup( const char * src, const int size );
	Q_DECL_EXPORT char * myStrDup( const QString & src );


	Q_DECL_EXPORT QByteArray	toByteArray(const ASN1TDynOctStr &);

	Q_DECL_EXPORT QString		toString(const ASN1ConstCharPtr &);

}
#endif 

