#ifndef __EKLENTI__
#define __EKLENTI__


#include "AY_Eklenti.h"
#include "EklentiFabrikasi.h"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 Eklentilerine "Kritik"lik bilgisinin eklendiði wrapper sýnýf
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT Eklenti
	{

		bool			mKritik;
		AY_Eklenti *	pEklenti;

	public:
		Eklenti(void);
		Eklenti(const bool & , const AY_Eklenti * );
		Eklenti(const Extension & );
		Eklenti(const Eklenti & );


		Eklenti& operator=(const Eklenti & );

		friend bool operator==(const Eklenti & iRHS, const Eklenti & iLHS );

		void fromExtension(const Extension &);

		const	AY_Eklenti* getEklenti()const; 
				AY_Eklenti* getEklenti(); 
				void		setEklenti(const AY_Eklenti*);

		bool	isKritik()const; 
		void	setKritik(const bool&);



		virtual ~Eklenti(void);
	};

}

#endif

