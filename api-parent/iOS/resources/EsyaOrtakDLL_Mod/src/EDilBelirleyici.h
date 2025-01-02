#ifndef __E_DIL_BELIRLEYICI_H_
#define __E_DIL_BELIRLEYICI_H_
#include <QApplication>
#include "EsyaOrtak_Ortak.h"

#define E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_BEGIN 
#define E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_END

NAMESPACE_BEGIN(esya)
class  Q_DECL_EXPORT EDilBelirleyici
{
	static QList<QTranslator * > sTranslators;
	QApplication * mpApp;	
	void _removeTranslators();
public:
	EDilBelirleyici(QApplication * ipApp);
	virtual void calismaDiliBelirle()=0;
	void calismaDiliUygula();
	virtual ~EDilBelirleyici(void);
protected:
	QString mDilSecenek;
};
NAMESPACE_END
#endif
