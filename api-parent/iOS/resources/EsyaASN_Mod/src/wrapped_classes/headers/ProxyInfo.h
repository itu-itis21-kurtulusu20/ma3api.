#ifndef __PROXYINFO__
#define __PROXYINFO__

#include "Targets.h"

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
	class Q_DECL_EXPORT ProxyInfo  : public EASNWrapperTemplate<ASN1T_ATTRCERT_ProxyInfo,ASN1C_ATTRCERT_ProxyInfo>
	{
		QList<Targets> mList;

	public:
		ProxyInfo(void);
		ProxyInfo(const ASN1T_ATTRCERT_ProxyInfo &);
		ProxyInfo(const QByteArray &);
		ProxyInfo(const QList<Targets>& iList);
		ProxyInfo(const ProxyInfo&);

		ProxyInfo & operator=(const ProxyInfo&);
		friend bool operator==(const ProxyInfo & ,const ProxyInfo & );
		friend bool operator!=(const ProxyInfo & iRHS, const ProxyInfo & iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_ProxyInfo&);
		int copyToASNObject(ASN1T_ATTRCERT_ProxyInfo& )const;
		void freeASNObject(ASN1T_ATTRCERT_ProxyInfo & )const;

		virtual ~ProxyInfo(void);

		// GETTERS AND SETTERS

		const QList<Targets> &getList() const;
	
		void setList(const QList<Targets> & iList);
		void appendTargets( const Targets & iTargets );
	};

}

#endif

