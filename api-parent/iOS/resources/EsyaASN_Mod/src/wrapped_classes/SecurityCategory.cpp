
#include "SecurityCategory.h"
#include "ESeqOfList.h"


using namespace esya;

namespace esya
{
	SecurityCategory::SecurityCategory(void)
	{
	}

	SecurityCategory::SecurityCategory(const QByteArray & iSecurityCategory)
	{
		constructObject(iSecurityCategory);
	}

	SecurityCategory::SecurityCategory(const ASN1T_ATTRCERT_SecurityCategory & iSecurityCategory )
	{
		copyFromASNObject(iSecurityCategory);
	}

	SecurityCategory::SecurityCategory(const SecurityCategory& iSecurityCategory)
	:	mType(iSecurityCategory.getType()),
		mValue(iSecurityCategory.getValue())
	{
	}

	SecurityCategory& SecurityCategory::operator=(const SecurityCategory& iSecurityCategory)
	{
		mType	= iSecurityCategory.getType();
		mValue	= iSecurityCategory.getValue();

		return *this;
	}

	bool operator==( const SecurityCategory& iRHS, const SecurityCategory& iLHS)
	{
		return  (	( iRHS.getType()	== iLHS.getType() ) &&
					( iRHS.getValue()	== iLHS.getValue() )	); 
	}

	bool operator!=( const SecurityCategory& iRHS, const SecurityCategory& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int SecurityCategory::copyFromASNObject(const ASN1T_ATTRCERT_SecurityCategory & iSecurityCategory)
	{
		mType = iSecurityCategory.type;	
		mValue = QByteArray((const char*)iSecurityCategory.value.data,iSecurityCategory.value.numocts);

		return SUCCESS;
	}

	int SecurityCategory::copyToASNObject(ASN1T_ATTRCERT_SecurityCategory &oSecurityCategory)const
	{
		oSecurityCategory.type = mType ;	
		oSecurityCategory.value.data = (OSOCTET*)myStrDup(mValue.data(),mValue.size());
		oSecurityCategory.value.numocts = mValue.size();

		return SUCCESS;
	}

	void SecurityCategory::freeASNObject(ASN1T_ATTRCERT_SecurityCategory& oSecurityCategory)const
	{
		if (oSecurityCategory.value.numocts>0)
		{
			DELETE_MEMORY_ARRAY(oSecurityCategory.value.data);
		}
	}

	int SecurityCategory::copySecurityCategories(const ASN1T_ATTRCERT__SetOfATTRCERT_SecurityCategory & iSecurityCategories, QList<SecurityCategory>& oList)
	{
		return copyASNObjects<SecurityCategory>(iSecurityCategories,oList);
	}

	int SecurityCategory::copySecurityCategories(const QList<SecurityCategory> iList ,ASN1T_ATTRCERT__SetOfATTRCERT_SecurityCategory & oSecurityCategories)
	{
		return copyASNObjects<SecurityCategory>(iList,oSecurityCategories);
	}

	int SecurityCategory::copySecurityCategories(const QByteArray & iASNBytes, QList<SecurityCategory>& oList)
	{
		return copyASNObjects<	ASN1T_ATTRCERT__SetOfATTRCERT_SecurityCategory,
								ASN1C_ATTRCERT__SetOfATTRCERT_SecurityCategory,
								SecurityCategory > (iASNBytes,oList);
	}

	const ASN1TObjId & SecurityCategory::getType()const
	{
		return mType;
	}

	const QByteArray & SecurityCategory::getValue()const
	{
		return mValue;
	}

	void SecurityCategory::setType(const ASN1TObjId & iType)
	{
		mType = iType;
	}

	void SecurityCategory::setValue(const QByteArray & iValue)
	{
		mValue = iValue;
	}

	SecurityCategory::~SecurityCategory(void)
	{
	}
}