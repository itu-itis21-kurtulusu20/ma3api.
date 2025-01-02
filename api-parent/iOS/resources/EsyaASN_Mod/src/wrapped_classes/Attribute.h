#ifndef __ATTRIBUTE__
#define __ATTRIBUTE__

#include "cms.h"
#include "ortak.h"
#include "AttributeValue.h"
#include "EException.h"
#include "EASNWrapperTemplate.h"

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
	class Q_DECL_EXPORT Attribute : public EASNWrapperTemplate<ASN1T_EXP_Attribute,ASN1C_EXP_Attribute>
	{

		ASN1T_EXP_AttributeType mAttributeType;
		QList<AttributeValue> mAttributeValues;

	public:

		Attribute();
		Attribute(const ASN1T_EXP_AttributeType & );
		Attribute(const ASN1T_EXP_Attribute & iAttribute);
		Attribute(const Attribute & iAttribute);
		Attribute(const  QByteArray &iAttribute);

		Attribute& operator=(const Attribute& iAttribute);
		Q_DECL_EXPORT friend bool operator==(const Attribute & iRHS, const Attribute & iLHS);
		Q_DECL_EXPORT friend bool operator!=(const Attribute & iRHS, const Attribute & iLHS);

		int copyToASNObject(ASN1T_EXP_Attribute & oAttribute) const;	
		int copyFromASNObject(const ASN1T_EXP_Attribute & iAttribute);
		void freeASNObject(ASN1T_EXP_Attribute & pAttr)const;

		const ASN1T_EXP_AttributeType& getType()const;
		void setType(const  ASN1T_EXP_AttributeType &iAttrType);

		int  copyAttributeList(const QList<Attribute> iList ,ASN1TPDUSeqOfList & oAttrList);
		int	copyAttributeList(const ASN1TPDUSeqOfList & iAttrList, QList<Attribute>& oList);
		int	copyAttributeList(const QByteArray & iASNBytes, QList<Attribute>& oList);
		int	copyAttributeList(const QList<Attribute>& iList,QByteArray & oASNBytes);

		virtual ~Attribute(void);

		// GETTERS AND SETTERS

		const	QList<AttributeValue> & getAttributeValues() const ;		
		QList<AttributeValue> & getAttributeValues() ;
		
		const ASN1TObjId & getAttributeType()const;


		int addAttributeValue(const ASN1T_EXP_AttributeValue &);
		int addAttributeValue(const QByteArray &);
		int addAttributeValue(const AttributeValue &);

		static int findAttribute(const QList<Attribute> iAttrList , const ASN1TObjId iAttrType , Attribute & oAttr );

	public:

	};


}

#endif

