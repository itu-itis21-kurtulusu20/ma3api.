#include "IetfAttrSyntax_values_element.h"
#include "ESeqOfList.h"

using namespace esya;

namespace esya
{

	IetfAttrSyntax_values_element::IetfAttrSyntax_values_element(void)
	:	mType(IVT_Octets)
	{
	}

	IetfAttrSyntax_values_element::IetfAttrSyntax_values_element(const QByteArray & iIetfAttrSyntax_values_element)
	{
		constructObject(iIetfAttrSyntax_values_element);
	}

	IetfAttrSyntax_values_element::IetfAttrSyntax_values_element(const ASN1T_ATTRCERT_IetfAttrSyntax_values_element & iIetfAttrSyntax_values_element )
	{
		copyFromASNObject(iIetfAttrSyntax_values_element);
	}

	IetfAttrSyntax_values_element::IetfAttrSyntax_values_element(const IetfAttrSyntax_values_element& iIetfAttrSyntax_values_element)
	:	mType(iIetfAttrSyntax_values_element.getType())
	{
		switch (mType)
		{
		case IVT_Octets :
			{
				mOctets = iIetfAttrSyntax_values_element.getOctets();
				break;
			}
		case IVT_OID :
			{
				mOID = iIetfAttrSyntax_values_element.getOID();
				break;
			}
		case IVT_String :
			{
				mString = iIetfAttrSyntax_values_element.getString();
				break;
			}
		}
	}

	IetfAttrSyntax_values_element& IetfAttrSyntax_values_element::operator=(const IetfAttrSyntax_values_element& iIetfAttrSyntax_values_element)
	{
		mType = iIetfAttrSyntax_values_element.getType();
		switch (mType)
		{
		case IVT_Octets :
			{
				mOctets = iIetfAttrSyntax_values_element.getOctets();
				break;
			}
		case IVT_OID :
			{
				mOID = iIetfAttrSyntax_values_element.getOID();
				break;
			}
		case IVT_String :
			{
				mString = iIetfAttrSyntax_values_element.getString();
				break;
			}
		}

		return *this;
	}

	bool operator==( const IetfAttrSyntax_values_element& iRHS, const IetfAttrSyntax_values_element& iLHS)
	{
		if ( iRHS.getType() != iLHS.getType() )
			return false;

		switch (iRHS.getType())
		{
		case IetfAttrSyntax_values_element::IVT_Octets :
			{
				if ( iRHS.getOctets() != iLHS.getOctets() )
					return false;
				break;
			}
		case IetfAttrSyntax_values_element::IVT_OID :
			{
				if ( iRHS.getOID() != iLHS.getOID() )
					return false;
				break;
			}
		case IetfAttrSyntax_values_element::IVT_String :
			{
				if ( iRHS.getString() != iLHS.getString() )
					return false;
				break;
			}
		}

		return true;
	}

	bool operator!=( const IetfAttrSyntax_values_element& iRHS, const IetfAttrSyntax_values_element& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int IetfAttrSyntax_values_element::copyFromASNObject(const ASN1T_ATTRCERT_IetfAttrSyntax_values_element & iIetfAttrSyntax_values_element)
	{
		mType = (IetfAttrSyntax_values_elementType)iIetfAttrSyntax_values_element.t;

		switch (mType)
		{
		case IVT_Octets :
			{
				mOctets = QByteArray((const char*)iIetfAttrSyntax_values_element.u.octets->data,iIetfAttrSyntax_values_element.u.octets->numocts);
				break;
			}
		case IVT_OID:
			{
				mOID = *(iIetfAttrSyntax_values_element.u.oid);
				break;
			}
		case IVT_String:
			{
				mString = QString((const char*)iIetfAttrSyntax_values_element.u.string);
				break;
			}
		}

		return SUCCESS;
	}

	int IetfAttrSyntax_values_element::copyToASNObject(ASN1T_ATTRCERT_IetfAttrSyntax_values_element &oIetfAttrSyntax_values_element)const
	{
		oIetfAttrSyntax_values_element.t = mType ;

		oIetfAttrSyntax_values_element.u.octets= NULL;
		oIetfAttrSyntax_values_element.u.oid = NULL;
		oIetfAttrSyntax_values_element.u.string = NULL;

		switch (mType)
		{
		case IVT_Octets :
			{
				oIetfAttrSyntax_values_element.u.octets = new ASN1TDynOctStr(mOctets.size(),(OSOCTET*)myStrDup(mOctets.data(),mOctets.size()));
				break;
			}
		case IVT_OID :
			{
				oIetfAttrSyntax_values_element.u.oid= new ASN1TObjId(mOID);
				break;
			}
		case IVT_String :
			{
				oIetfAttrSyntax_values_element.u.string = (OSUTF8CHAR*)myStrDup(mString);
				break;
			}
		}

		return SUCCESS;
	}

	void IetfAttrSyntax_values_element::freeASNObject(ASN1T_ATTRCERT_IetfAttrSyntax_values_element& oIetfAttrSyntax_values_element)const
	{
		switch (oIetfAttrSyntax_values_element.t)
		{
		case IVT_Octets :
			{
				DELETE_MEMORY_ARRAY(oIetfAttrSyntax_values_element.u.octets->data);
				DELETE_MEMORY(oIetfAttrSyntax_values_element.u.octets);
				break;
			}
		case IVT_OID :
			{
				DELETE_MEMORY(oIetfAttrSyntax_values_element.u.oid);
				break;
			}
		case IVT_String :
			{
				DELETE_MEMORY_ARRAY(oIetfAttrSyntax_values_element.u.string);
				break;
			}
		}
	}

	int IetfAttrSyntax_values_element::copyIetfAttrSyntax_values_elements(const ASN1T_ATTRCERT__SeqOfATTRCERT_IetfAttrSyntax_values_element & iIetfAttrSyntax_values_elements, QList<IetfAttrSyntax_values_element>& oList)
	{
		return copyASNObjects<IetfAttrSyntax_values_element>(iIetfAttrSyntax_values_elements,oList)	;
	}

	int IetfAttrSyntax_values_element::copyIetfAttrSyntax_values_elements(const QList<IetfAttrSyntax_values_element>& iList ,ASN1T_ATTRCERT__SeqOfATTRCERT_IetfAttrSyntax_values_element & oIetfAttrSyntax_values_elements)
	{
		return copyASNObjects<IetfAttrSyntax_values_element>(iList,oIetfAttrSyntax_values_elements)	;	
	}

	int IetfAttrSyntax_values_element::copyIetfAttrSyntax_values_elements(const QByteArray & iASNBytes, QList<IetfAttrSyntax_values_element>& oList)
	{
		return copyASNObjects<	ASN1T_ATTRCERT__SeqOfATTRCERT_IetfAttrSyntax_values_element,
								ASN1C_ATTRCERT__SeqOfATTRCERT_IetfAttrSyntax_values_element,
								IetfAttrSyntax_values_element> (iASNBytes,oList)	;			
	}


	const IetfAttrSyntax_values_element::IetfAttrSyntax_values_elementType&  IetfAttrSyntax_values_element::getType()const
	{
		return mType;
	}

	const QByteArray &  IetfAttrSyntax_values_element::getOctets()const
	{
		return mOctets;
	}

	const ASN1TObjId &  IetfAttrSyntax_values_element::getOID()const 
	{
		return mOID;
	}

	const QString &  IetfAttrSyntax_values_element::getString()const 
	{
		return mString;
	}

	void IetfAttrSyntax_values_element::setType(const IetfAttrSyntax_values_elementType&  iType)
	{
		mType = iType;
	}

	void IetfAttrSyntax_values_element::setOctets(const QByteArray & iOctets )
	{	
		mOctets = iOctets;
	}

	void IetfAttrSyntax_values_element::setOID(const ASN1TObjId & iOID) 
	{
		mOID = iOID;
	}

	void IetfAttrSyntax_values_element::setString(const QString & iString) 
	{
		mString = iString;
	}

	IetfAttrSyntax_values_element::~IetfAttrSyntax_values_element(void)
	{
	}

}