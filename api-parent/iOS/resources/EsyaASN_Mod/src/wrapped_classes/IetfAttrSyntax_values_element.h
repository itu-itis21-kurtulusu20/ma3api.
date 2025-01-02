#ifndef __IETFATTRSYNTAX_VALUES_ELEMENT__
#define __IETFATTRSYNTAX_VALUES_ELEMENT__


#include "attrcert.h"
#include "ortak.h"



namespace esya
{

	class Q_DECL_EXPORT IetfAttrSyntax_values_element : public EASNWrapperTemplate<ASN1T_ATTRCERT_IetfAttrSyntax_values_element,ASN1C_ATTRCERT_IetfAttrSyntax_values_element>
	{
	public:
		enum IetfAttrSyntax_values_elementType 
		{
			IVT_Octets	= T_ATTRCERT_IetfAttrSyntax_values_element_octets  ,
			IVT_OID		= T_ATTRCERT_IetfAttrSyntax_values_element_oid , 
			IVT_String	= T_ATTRCERT_IetfAttrSyntax_values_element_string   
		};

	protected:
		
		IetfAttrSyntax_values_elementType mType;

		QByteArray mOctets;
		ASN1TObjId mOID;
		QString mString;

	public:
		IetfAttrSyntax_values_element(void);
		IetfAttrSyntax_values_element(const QByteArray & iIetfAttrSyntax_values_element);
		IetfAttrSyntax_values_element(const ASN1T_ATTRCERT_IetfAttrSyntax_values_element & iIetfAttrSyntax_values_element );
		IetfAttrSyntax_values_element(const IetfAttrSyntax_values_element& iIetfAttrSyntax_values_element);

		IetfAttrSyntax_values_element& operator=(const IetfAttrSyntax_values_element& iIetfAttrSyntax_values_element);
		friend bool operator==( const IetfAttrSyntax_values_element& iRHS, const IetfAttrSyntax_values_element& iLHS);
		friend bool operator!=( const IetfAttrSyntax_values_element& iRHS, const IetfAttrSyntax_values_element& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_IetfAttrSyntax_values_element & iIetfAttrSyntax_values_elementr);
		int copyToASNObject(ASN1T_ATTRCERT_IetfAttrSyntax_values_element &oIetfAttrSyntax_values_element)const;
		void freeASNObject(ASN1T_ATTRCERT_IetfAttrSyntax_values_element& oIetfAttrSyntax_values_element)const;

		int copyIetfAttrSyntax_values_elements(const ASN1T_ATTRCERT__SeqOfATTRCERT_IetfAttrSyntax_values_element& iIetfAttrSyntax_values_elements, QList<IetfAttrSyntax_values_element>& oList);
		int copyIetfAttrSyntax_values_elements(const QList<IetfAttrSyntax_values_element>& iList ,ASN1T_ATTRCERT__SeqOfATTRCERT_IetfAttrSyntax_values_element & oIetfAttrSyntax_values_elements);	
		int copyIetfAttrSyntax_values_elements(const QByteArray & iASNBytes, QList<IetfAttrSyntax_values_element>& oList);

		virtual ~IetfAttrSyntax_values_element(void);

		// GETTERS AND SETTERS

		const IetfAttrSyntax_values_elementType&  getType()const;
		const QByteArray & getOctets()const;
		const ASN1TObjId & getOID()const ;
		const QString & getString()const ;

		void setType(const IetfAttrSyntax_values_elementType&  iType);
		void setOctets(const QByteArray & );
		void setOID(const ASN1TObjId & iOID) ;
		void setString(const QString & iString) ;

	};

}

#endif

