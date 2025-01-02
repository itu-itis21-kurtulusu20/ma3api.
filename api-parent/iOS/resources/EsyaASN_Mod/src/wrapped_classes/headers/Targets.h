#ifndef __TARGETS__
#define __TARGETS__

#include "Target.h"

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
	class Targets  : public EASNWrapperTemplate<ASN1T_ATTRCERT_Targets,ASN1C_ATTRCERT_Targets>
	{
		QList<Target> mList;

	public:
		Targets(void);
		Targets(const ASN1T_ATTRCERT_Targets &);
		Targets(const QByteArray &);
		Targets(const QList<Target>& iList);
		Targets(const Targets&);

		Targets & operator=(const Targets&);
		friend bool operator==(const Targets & ,const Targets & );
		friend bool operator!=(const Targets & iRHS, const Targets & iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_Targets&);
		int copyToASNObject(ASN1T_ATTRCERT_Targets& )const;
		void freeASNObject(ASN1T_ATTRCERT_Targets & )const;

		int copyProxyInfo(const ASN1T_ATTRCERT_ProxyInfo & iProxyInfo, QList<Targets>& oList);
		int copyProxyInfo(const QList<Targets> iList ,ASN1T_ATTRCERT_ProxyInfo & oProxyInfo);
		int copyProxyInfo(const QByteArray & iASNBytes, QList<Targets>& oList);
		
		virtual ~Targets(void);

		// GETTERS AND SETTERS

		const QList<Target> &getList() const;
	
		void setList(const QList<Target> & iList);
		void appendTarget( const Target & iTarget );
	};

}

#endif

