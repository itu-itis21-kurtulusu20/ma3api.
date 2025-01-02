#ifndef __ESYA_UTILS_H__
#define __ESYA_UTILS_H__
#include "esyaOrtak.h"
#include <QObject>

NAMESPACE_BEGIN(esya)

class Q_DECL_EXPORT EsyaUtils
{
public:
	EsyaUtils(void);
	~EsyaUtils(void);
	template <class T>
	static void appendUnique(QList<T> &iListe, const T &iEklenecek)
	{
		if(!iListe.contains(iEklenecek))
		{
			iListe.append(iEklenecek);
		}
	}
};

NAMESPACE_END
#endif