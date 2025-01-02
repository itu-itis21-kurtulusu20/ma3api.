#ifndef __EKLENTI__
#define __EKLENTI__


#include "AY_Eklenti.h"
#include "EklentiFabrikasi.h"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* Tanýnmayan eklentiler için genel sýnýf
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT BilinmeyenEklenti : public AY_Eklenti
	{
	protected:
		ASN1TObjId mEklentiTipi;
		QByteArray mEklentiDegeri;
	
	public:
		BilinmeyenEklenti(void);
		BilinmeyenEklenti(const ASN1TObjId & , const QByteArray &);
		BilinmeyenEklenti(const BilinmeyenEklenti &);

		BilinmeyenEklenti & operator=(const BilinmeyenEklenti&);

		friend bool  operator==(const BilinmeyenEklenti& ,const BilinmeyenEklenti& );

		
		const ASN1TObjId& getEklentiTipi() const;
		const QByteArray& getEklentiDegeri() const;

		/************************************************************************/
		/*					AY_EKLENTI FONKSIYONLARI                            */
		/************************************************************************/


		virtual QString eklentiAdiAl()			const ;
		virtual QString eklentiKisaDegerAl()	const ;
		virtual QString eklentiUzunDegerAl()	const ;

		virtual AY_Eklenti* kendiniKopyala() const ;


		virtual ~BilinmeyenEklenti(void);
	};

}

#endif 

