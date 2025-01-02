
#ifndef __MYMETATYPES__
#define __MYMETATYPES__

#include "ECertificate.h"
#include "AlgorithmIdentifier.h"

using namespace esya;


//Q_DECLARE_METATYPE(esya::ECertificate)
//Q_DECLARE_METATYPE(QList<esya::ECertificate>)
Q_DECLARE_METATYPE(QList<ASN1TObjId>)
Q_DECLARE_METATYPE(AlgorithmIdentifier)
Q_DECLARE_METATYPE(SerialNumber)


#endif

