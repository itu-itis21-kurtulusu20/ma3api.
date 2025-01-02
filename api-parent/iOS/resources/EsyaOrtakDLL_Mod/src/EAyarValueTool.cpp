#include "EAyarValueTool.h"
NAMESPACE_BEGIN(esya)

EAyarValueTool::EAyarValueTool(void)
{
}

EAyarValueTool::~EAyarValueTool(void)
{
}

bool EAyarValueTool::getBoolDeger(const QVariant & iVariant)
{
	QString str = iVariant.toString().toLower();
	return  !(str == QLatin1String("FALSE") || str == QLatin1String("n") || str == QLatin1String("0") || str == QLatin1String("false") || str == QLatin1String("False") || str == QLatin1String("FALSE") || str.isEmpty());
}

QString EAyarValueTool::getEsitlikStatementBool(const QString &iFieldName,bool iVal)
{
	return 
		(iVal?QString("NOT ("):QString("("))
		+iFieldName + "='N' OR "+iFieldName + "='n' OR " +iFieldName+"='0' OR "+iFieldName+"='false')";
}
QString EAyarValueTool::getStrBool(bool iDeger)
{
	QString retBoolStr;
	if (iDeger)
	{
		retBoolStr = "true";
	}
	else
	{
		retBoolStr = "false";
	}
	return retBoolStr;
}

NAMESPACE_END