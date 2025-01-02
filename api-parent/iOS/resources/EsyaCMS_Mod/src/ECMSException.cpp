#include "ECMSException.h"



using namespace esya;

/**
 * \brief
 * ECMSException için constructor.
 * 
 * \param errSTR
 * Hata mesajý.
 * 
 * \param iFileName
 * Ýstisnanýn oluþtuðu kaynak dosyasý adý.
 * 
 * \param iLineNumber
 * Ýstisnanýn oluþtuðu satýr numarasý.
 * 
 * 
 */
ECMSException::ECMSException(const QString & errSTR,const QString & iFileName , int iLineNumber)
:EException(errSTR,iFileName,iLineNumber), mHataKodu(CHT_Diger)
{

}

/**
* \brief
* ECMSException için constructor.
* 
* \param errSTR
* Hata mesajý.
* 
* \param iHataKodu
* Ýstisnanýn tipi
*
* \param iFileName
* Ýstisnanýn oluþtuðu kaynak dosyasý adý.
* 
* \param iLineNumber
* Ýstisnanýn oluþtuðu satýr numarasý.
* 
* 
*/
ECMSException::ECMSException(const QString & errSTR,const CMSHataTipi & iHataKodu, const QString & iFileName , int iLineNumber)
:EException(errSTR,iFileName,iLineNumber), mHataKodu(iHataKodu)
{

}

/**
 * \brief
 * ECMSException için constructor.
 * 
 * \param exc
 * Ýstisnanýn kendisinden kopyalanacaðý istisna.
 * 
 */
ECMSException::ECMSException(const EException & exc)
:EException(exc),mHataKodu(CHT_Diger)
{

}

/**
 * \brief
 * Ýstisnanýn mesaj listesinin sonuna yeni bir mesaj ekler.
 * 
 * \param iErrorText
 * Eklenecek hata mesajý.
 * 
 * \param iFileName
 * Ýstisnanýn oluþtuðu kaynak dosyasý adý.
 * 
 * \param iLineNumber
 * Ýstisnanýn oluþtuðu satýr numarasý.
 * 
 * \returns
 * Ýstisnanýn kendisini döner.
 * 
 */
ECMSException  ECMSException::append(const QString &iErrorText,const QString & iFileName  , int iLineNumber  )
{
	EException::append(iErrorText,iFileName,iLineNumber);
	return *this;
}

/**
 * \brief
 * ECMSException için Destructor.
 */
ECMSException::~ECMSException(void)
{
}


/**
* \brief
* Hata Kodunu döner
*
* \return   	const  CMSHataTipi &
*/
const  ECMSException::CMSHataTipi & ECMSException::getHataKodu()const
{
	return mHataKodu;
}