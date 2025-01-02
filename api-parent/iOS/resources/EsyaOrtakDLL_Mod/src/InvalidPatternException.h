#ifndef _INVALID_PATTERN_EXCEPTION_H_
#define _INVALID_PATTERN_EXCEPTION_H_

#include "EException.h"
NAMESPACE_BEGIN(esya)
class InvalidPatternException :
	public EException
{
public:
	InvalidPatternException(const  QString &iErrorDetail, const QString &iFileName = "",int iLineNumber = 0);	
	virtual ~InvalidPatternException(void);
};
NAMESPACE_END
#endif
