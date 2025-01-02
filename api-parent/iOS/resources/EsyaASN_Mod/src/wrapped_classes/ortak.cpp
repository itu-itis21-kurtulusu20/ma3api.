
#include "ortak.h"

using namespace esya;

char * esya::myStrDup( const char * src, const int size )
{
	char * ptr = new char[size];
	
	for (int i = 0; i<size ; i++)
		ptr[i] = src[i];

	return ptr;
}

char * esya::myStrDup( const QString & src )
{
	char * dst = esya::myStrDup(src.toLocal8Bit().data(),src.length()+1);
	dst[src.length()]= '\0';
	return dst;
}

QByteArray esya::toByteArray(const ASN1TDynOctStr & iOctStr)
{
	return QByteArray((char*)iOctStr.data, iOctStr.numocts);
}



QString esya::toString(const ASN1ConstCharPtr & iASNString)
{
	return QString(iASNString);
}
