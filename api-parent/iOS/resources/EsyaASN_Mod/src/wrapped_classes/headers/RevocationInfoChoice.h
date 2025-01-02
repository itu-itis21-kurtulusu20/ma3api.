
#ifndef __REVOCATIONINFOCHOICE__
#define __REVOCATIONINFOCHOICE__

#include "ECertificateList.h"

namespace esya
{

	enum RIType {	T_CertificateList	= T_CMS_RevocationInfoChoice_crl , 
					T_OtherCRL				= T_CMS_RevocationInfoChoice_other 
				};

	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT RevocationInfoChoice  : public EASNWrapperTemplate<ASN1T_CMS_RevocationInfoChoice,ASN1C_CMS_RevocationInfoChoice>
	{
		RIType				mType;
		ECertificateList	mCertificateList;

	public:

		RevocationInfoChoice(void);
		RevocationInfoChoice(const QByteArray & );
		RevocationInfoChoice(const ASN1T_CMS_RevocationInfoChoice & );
		RevocationInfoChoice(const RevocationInfoChoice&);


		RevocationInfoChoice & operator=(const RevocationInfoChoice & );
		friend bool operator==(const RevocationInfoChoice & ,const RevocationInfoChoice & );
		friend bool operator!=(const RevocationInfoChoice & ,const RevocationInfoChoice & );
		
		int copyFromASNObject(const ASN1T_CMS_RevocationInfoChoice & iRIC);
		int copyToASNObject(ASN1T_CMS_RevocationInfoChoice & oRIC)const;
		void freeASNObject(ASN1T_CMS_RevocationInfoChoice & oRIC)const;

		int copyRICs(const ASN1T_CMS_RevocationInfoChoices & iRICs, QList<RevocationInfoChoice>& oList);
		int copyRICs(const QList<RevocationInfoChoice> & iList ,ASN1T_CMS_RevocationInfoChoices& oRICs);	
		int copyRICs(const QList<RevocationInfoChoice> & iList ,QByteArray& oRICs);	
		int copyRICs(const QByteArray & iList ,QList<RevocationInfoChoice>& oRICs);	

		virtual ~RevocationInfoChoice(void);

		// GETTERS AND SETTERS 

		const RIType & getType() const ;
		const ECertificateList & getCertificateList() const;
	};

}


#endif

