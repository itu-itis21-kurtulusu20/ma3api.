#ifndef __ACCLEARATTRS__
#define __ACCLEARATTRS__


#include "GeneralName.h"
#include "Attribute.h"
#include "attrcert.h"


namespace esya
{
	class Q_DECL_EXPORT ACClearAttrs : public EASNWrapperTemplate<ASN1T_ATTRCERT_ACClearAttrs,ASN1C_ATTRCERT_ACClearAttrs>
	{
		GeneralName			mACIssuer;
		int					mACSerial;
		QList<Attribute>	mAttrs;

	public:
		ACClearAttrs(void);
		ACClearAttrs(const QByteArray & iACClearAttrs);
		ACClearAttrs(const ASN1T_ATTRCERT_ACClearAttrs & iACClearAttrs );
		ACClearAttrs(const ACClearAttrs& iACClearAttrs);

		ACClearAttrs& operator=(const ACClearAttrs& iACClearAttrs);
		friend bool operator==( const ACClearAttrs& iRHS, const ACClearAttrs& iLHS);
		friend bool operator!=( const ACClearAttrs& iRHS, const ACClearAttrs& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_ACClearAttrs & iACClearAttrs);
		int copyToASNObject(ASN1T_ATTRCERT_ACClearAttrs &oACClearAttrs)const;
		void freeASNObject(ASN1T_ATTRCERT_ACClearAttrs& oACClearAttrs);

		virtual ~ACClearAttrs(void);


		//GETTERS AND SETTERS
	
		const GeneralName& getACIssuer()const;
		const int& getACSerial()const;
		const QList<Attribute>&	getAttrs()const;

		void setACIssuer(const GeneralName&  iACIssuer);
		void setACSerial(const int& iACSerial);
		void setAttrs(const QList<Attribute>& iAttrs);
		void appendAttribute(const Attribute& iAttr);	

		
	};

}

#endif

