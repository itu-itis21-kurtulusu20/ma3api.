#ifndef __E_AYAR_VALUE_TOOL_H_
#define __E_AYAR_VALUE_TOOL_H_
#include "esyaOrtak.h"
#include <QVariant>
NAMESPACE_BEGIN(esya)
/**
 * \ingroup EsyaOrtak
 *
 * Ayarlarda ortak kullanýlan fonksiyonlarý tutan sýnýf 
 *
 * \version 1.0
 * first version
 *
 * \date 04-03-2009
 *
 * \author ramazang
 *
 * \par license 
 * 
 * \todo 
 *
 * \bug 
 *
 */
class  Q_DECL_EXPORT  EAyarValueTool
{
public:
	EAyarValueTool(void);
	~EAyarValueTool(void);
	static QString getStrBool(bool iDeger);
	static bool getBoolDeger(const QVariant & iVariant);
	static QString getEsitlikStatementBool(const QString &iFieldName,bool iVal);
};
NAMESPACE_END
#endif