#ifndef __ETESTUTIL_H__
#define __ETESTUTIL_H__
#include "esyaOrtak.h"
#include <QObject>


NAMESPACE_BEGIN(esya)

#define ESYATEST(x) {x testEdici; esyaTest(&testEdici,#x,argc,argv);}

void Q_DECL_EXPORT esyaTest(QObject *pTestObject, const char *iTestName , int argc = 0, char **argv = 0);

NAMESPACE_END

#endif

