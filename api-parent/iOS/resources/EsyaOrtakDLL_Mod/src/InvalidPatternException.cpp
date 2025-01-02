#include "InvalidPatternException.h"
using namespace esya;

InvalidPatternException::InvalidPatternException(const QString &iErrorDetail, const QString &iFileName /* =  */,int iLineNumber /* = 0 */)
:EException(iErrorDetail,iFileName,iLineNumber)
{
}

InvalidPatternException::~InvalidPatternException(void)
{
}
