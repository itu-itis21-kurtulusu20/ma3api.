#ifndef __ECPKPARAMETERS__
#define __ECPKPARAMETERS__

#include "SpecifiedECDomain.h"
#include "ASN1TObjId.h"


namespace esya
{

	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT ECParameters : public EASNWrapperTemplate<ASN1T_ALGOS_ECParameters,ASN1C_ALGOS_ECParameters>
	{
	public : 
		
		enum PKParamType {	PT_ImplicitlyCA = T_ALGOS_ECParameters_implicitCurve, 
							PT_NamedCurve	= T_ALGOS_ECParameters_namedCurve, 
							PT_SpecifiedCurve = T_ALGOS_ECParameters_specifiedCurve};

		ASN1TObjId			mNamedCurve;
		SpecifiedECDomain	mSpecifiedCurve;
		PKParamType			mType;


	public:
		ECParameters(void);
		ECParameters(const QByteArray & iECParameters);
		ECParameters(const ASN1T_ALGOS_ECParameters & iECParameters);
		ECParameters(const SpecifiedECDomain& iSpecifiedCurve);
		ECParameters(const ASN1TObjId& iNamedCurve);
		ECParameters(const ECParameters& iECParameters);

		ECParameters& operator=(const ECParameters& );
		friend bool operator==(const ECParameters & iRHS, const ECParameters& iLHS);
		friend bool operator!=(const ECParameters & iRHS, const ECParameters& iLHS);

		int copyFromASNObject(const ASN1T_ALGOS_ECParameters & iECPKParameters) ;
		int copyToASNObject(ASN1T_ALGOS_ECParameters & oECParameters) const;
		void freeASNObject(ASN1T_ALGOS_ECParameters & oECParameters)const;

		virtual ~ECParameters(void);

		// GETTERS AND SETTERS

		const PKParamType&  getType()const;
		const SpecifiedECDomain&	getSpecifiedCurve()const ;
		const ASN1TObjId&	getNamedCurve()const;

		void setSpecifiedCurve(const SpecifiedECDomain& iSpecifiedCurve);
		void setNamedCurve(const ASN1TObjId& iNamedCurve);
		void setType(const PKParamType& iType);


	};

}

#endif
