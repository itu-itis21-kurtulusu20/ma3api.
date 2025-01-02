
#include "EAyarKullaniciManager.h"
#include "EAyarGDMVarsayilanOzne.h"
#include "EAyarManagerFactory.h"
#include "EsyaOrtak_Ortak.h"
#include "EAyarGDMOzneBilgisi.h"
#include "EAyarGDMOzneBilgiManager.h"


NAMESPACE_BEGIN(esya)

QList<EAyarGDMOzneBilgisi> EAyarGDMVarsayilanOzne::gdmVarsayilanOzneleriAl()
{
	return EAyarGDMOzneBilgiManager::getInstance()->gdmVarsayilanOzneleriAl();	
}

bool EAyarGDMVarsayilanOzne::gdmVarsayilanOzneVarmi(int iOzneID)
{
	return EAyarGDMOzneBilgiManager::getInstance()->gdmVarsayilanOzneVarmi(iOzneID);	
}


void EAyarGDMVarsayilanOzne::gdmVarsayilanOzneEkle(int iOzneID)
{
	EAyarGDMOzneBilgiManager::getInstance()->gdmVarsayilanOzneEkle(iOzneID);
}

void EAyarGDMVarsayilanOzne::gdmVarsayilanOzneSil(int iOzneID)
{
	EAyarGDMOzneBilgiManager::getInstance()->gdmVarsayilanOzneSil(iOzneID);	
}

void EAyarGDMVarsayilanOzne::gdmVarsayilanOzneleriSil()
{
	EAyarGDMOzneBilgiManager::getInstance()->gdmVarsayilanOzneleriSil();	
}

NAMESPACE_END
