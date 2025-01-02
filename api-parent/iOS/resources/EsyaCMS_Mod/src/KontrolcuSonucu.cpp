#include "KontrolcuSonucu.h"
#include "aciklamalar.h"
#include "EsyaCMS_DIL.h"

using namespace esya;

/**
* \brief
* KontrolcuSonucu constructoru
*/
KontrolcuSonucu::KontrolcuSonucu(void)
{
}

/**
* \brief
* KontrolcuSonucu kopya constructoru
*
* \param 		const KontrolcuSonucu& iKS
* KontrolcuSonucu
*/
KontrolcuSonucu::KontrolcuSonucu(const KontrolcuSonucu& iKS)
:	mKontrolAdi(iKS.getKontrolAdi()),
	mAciklama(iKS.getAciklama()),
	mKontrolSonucu(iKS.getKontrolSonucu())
{

}
/**
* \brief
* KontrolcuSonucu destructoru
*
* \param 		const QString & iKontrolAdi
* Kontrol Adý
*
* \param 		const QString & iAciklama
* Açýklama
*
* \param 		const KS_Type & iKS
* Kontrol Sonucu
*/
KontrolcuSonucu::KontrolcuSonucu(	const QString & iKontrolAdi ,
									const QString & iAciklama,
									const KS_Type &iKS	)
:	mKontrolAdi(iKontrolAdi),
	mAciklama(iAciklama),
	mKontrolSonucu(iKS)
{

}


/**
* \brief
* Kontrol Adý alanýný döner
*
* \return const QString & 
* Kontrol Adý
*/
const	QString &	KontrolcuSonucu::getKontrolAdi() const 
{
	return mKontrolAdi;
}

/**
* \brief
* Kontrol Adý alanýný döner
*
* \return QString & 
* Kontrol Adý
*/
QString &	KontrolcuSonucu::getKontrolAdi() 
{
	return mKontrolAdi;
}

/**
* \brief
* Kontrol Adý alanýný belirler
*
* \param  const QString & iKA
* Kontrol Adý
*/
void	KontrolcuSonucu::setKontrolAdi(const QString & iKA)
{
	mKontrolAdi = iKA;
}

/**
* \brief
* Açýklama alanýný döner
*
* \return const QString & 
* Açýklama
*/
const	QString & KontrolcuSonucu::getAciklama() const 
{
	return mAciklama ;
}

/**
* \brief
* Açýklama alanýný döner
*
* \return QString & 
* Açýklama
*/
QString & KontrolcuSonucu::getAciklama() 
{
	return mAciklama;
}

/**
* \brief
* Açýklama alanýný belirler
*
* \param  const QString & iAciklama
* Açýklama
*
*/
void KontrolcuSonucu::setAciklama(const QString & iAciklama)
{
	mAciklama = iAciklama;
}

/**
* \brief
* Kontrol Sonucunu döner
*
* \return const KontrolcuSonucu::KS_Type & 
* Kontrol Sonucu
*
*/
const KontrolcuSonucu::KS_Type & KontrolcuSonucu::getKontrolSonucu() const
{
	return mKontrolSonucu;
}

/**
* \brief
* Kontrol Sonucunu döner
*
* \return KontrolcuSonucu::KS_Type & 
* Kontrol Sonucu
*
*/
KontrolcuSonucu::KS_Type & KontrolcuSonucu::getKontrolSonucu()
{
	return mKontrolSonucu;
}


/**
* \brief
* Kontrol Sonucunu belirler
*
* \param 		const KontrolcuSonucu::KS_Type & iKS
* Kontrol Sonucu
*
*/
void KontrolcuSonucu::setKontrolSonucu(const KontrolcuSonucu::KS_Type & iKS)
{
	mKontrolSonucu = iKS;
}


/**
* \brief
* Kontrol Sonucunu yazýya çevirir.
*
* \return   	QT_NAMESPACE::QString
*/
QString KontrolcuSonucu::getKontrolSonucuAsString() const
{
	switch (mKontrolSonucu)
	{
	case BASARILI: 
		{
			return A_BASARILI;
		}
	case BASARISIZ: 
		{
			return A_BASARISIZ;
		}
	case KONTROL_TAMAMLANAMADI: 
		{
			return A_KONTROL_TAMAMLANAMADI;
		}
	}
}


/**
* \brief
* Kontrol Detayýný yazýya çevirir.
*
* \return   	QT_NAMESPACE::QString
*/
QString KontrolcuSonucu::toString() const
{
	QString st_sonuc;
	
	st_sonuc += QString("%1 [%2]\n").arg(LBL_KONTROLADI).arg(getKontrolAdi());
	st_sonuc += QString("%1 %2\n").arg(LBL_KONTROLSONUCU).arg(getKontrolSonucuAsString());
	st_sonuc += QString("%1 %2\n").arg(LBL_ACIKLAMA).arg(getAciklama());

	return st_sonuc;
}


/**
* \brief
* KontrolcuSonucu  destructoru
*/
KontrolcuSonucu::~KontrolcuSonucu(void)
{
}
