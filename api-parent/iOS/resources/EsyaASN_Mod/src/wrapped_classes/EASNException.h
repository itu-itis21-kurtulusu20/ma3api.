/**
*  stisnalar m z 
* 
* Copyright (c) 2005 by <Dindar  z/ MA3>
*/

#ifndef __EASNEXCEPTION__
#define __EASNEXCEPTION__


#include "EException.h"

namespace esya
{

	class EASNException :
		public EException
	{
		bool mIsNULL ;
	public:
		enum ASNHataTipi { AHT_DecodeError ,AHT_EncodeError, AHT_IOError, AHT_Diger};

		EASNException();
		EASNException(const  QString &iErrorDetail, const ASNHataTipi & iHataKodu  = AHT_Diger , const QString &iFileName = "",int iLineNumber = 0);
		EASNException(const  EException &exc, ASNHataTipi iHataKodu = AHT_Diger );	
		EASNException(const  EASNException &exc );

		EASNException append(const  QString &iErrorDetail, const ASNHataTipi iHataKodu  = AHT_Diger , const QString &iFileName = "",int iLineNumber = 0);

		const ASNHataTipi& getHataKodu() const ;
		void setHataKodu(const ASNHataTipi& );

		const bool& isNULL() const;
		void setNULL(const bool & );
		
		virtual ~EASNException(void);

	protected:
	
		ASNHataTipi mHataKodu;



	};

}

#endif

