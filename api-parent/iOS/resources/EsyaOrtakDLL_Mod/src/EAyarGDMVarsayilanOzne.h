#ifndef __EAYARGDMVARSAYILANOZNE_H__
#define __EAYARGDMVARSAYILANOZNE_H__

#include "esyaOrtak.h"
#include <QString>
#include <QSqlDatabase>
#include <QVariant>
#include "EAyarGDMDizin.h"
#include "EAyarGDMOzneBilgisi.h"


NAMESPACE_BEGIN(esya)

/**
 * \ingroup EsyaOrtakDLL
 *
 * GDM altýndaki kullanýcý ve gruplarýn tutulmasý için kullanýlan entry sýnýfý 
 *
 * \version 1.0
 * first version
 *
 * \date 05-15-2009
 *
 * \author ramazang
 * 
 * \todo 
 *
 * \bug 
 *
 */
class Q_DECL_EXPORT EAyarGDMVarsayilanOzne
{

public:
	static QList<EAyarGDMOzneBilgisi> gdmVarsayilanOzneleriAl();
	static void gdmVarsayilanOzneleriSil();
	static void gdmVarsayilanOzneSil(int iOzneID);
	static void gdmVarsayilanOzneEkle(int iOzneID);
	static bool gdmVarsayilanOzneVarmi(int iOzneID);

private:

};

NAMESPACE_END


#endif

