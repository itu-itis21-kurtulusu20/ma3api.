#pragma once
#include "EException.h"
#include <exception>



/**
 * \brief
 * CMS Modülünde oluþan istisnalar için oluþturulmuþ sýnýf
 */
namespace esya
{

	class Q_DECL_EXPORT ECMSException :
         public EException
	{
	public:
		enum CMSHataTipi { CHT_HataliFormat , CHT_HataliParola , CHT_AkilliKartHatasi ,CHT_ASNHatasi,CHT_KriptoHatasi, CHT_Diger};

		ECMSException(const QString & errSTR,const QString & iFileName = "", int iLineNumber = 0);
		ECMSException(const QString & errSTR,const CMSHataTipi& iHataKodu  , const QString & iFileName = "", int iLineNumber = 0);
		ECMSException(const EException & exc);


		ECMSException  append(const QString &iErrorText,const QString & iFileName ="" , int iLineNumber = 0 );


		const  CMSHataTipi & getHataKodu()const;

	public:
		~ECMSException(void);

	private:
		CMSHataTipi mHataKodu ;
	};
    class Q_DECL_EXPORT ECMSStdException :
             public std::exception
        {
        public:
            ECMSStdException(const QString & errSTR):mErrorMsg(errSTR){}
            ~ECMSStdException() throw() {};
            virtual const char* what() const throw(){
                return mErrorMsg.toLocal8Bit();
            }

        private:
            QString mErrorMsg ;
        };

}

