#include "Extension.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

Extension::Extension(const Extension &iExtension)
:	mCritical(iExtension.isCritical()), 
mExtensionId(iExtension.getExtensionId()),
mExtensionValue(iExtension.getExtensionValue())
{
}

Extension::Extension(const ASN1T_EXP_Extension & iExtension)
{
	copyFromASNObject(iExtension);
}

Extension::Extension(const QByteArray & iExtension)
{
	constructObject(iExtension);
}


Extension::Extension(void)
{
}

Extension::Extension(const ASN1OBJID & extnID,bool critical, const QByteArray &extnValue)
:mCritical(critical), mExtensionId(extnID),mExtensionValue(extnValue)
{
}


Extension & Extension::operator=(const Extension& iExtension)
{
	mCritical = iExtension.isCritical(); 
	mExtensionId = iExtension.getExtensionId();
	mExtensionValue = iExtension.getExtensionValue();
	return *this;
}

int Extension::copyFromASNObject(const ASN1T_EXP_Extension& iExtension)
{
	mCritical = iExtension.critical; 
	mExtensionId = iExtension.extnID;
	mExtensionValue = QByteArray((char*)iExtension.extnValue.data,iExtension.extnValue.numocts);
	return SUCCESS;
}

const ASN1TObjId & Extension::getExtensionId()const 
{
	return mExtensionId;
}

const QByteArray& Extension::getExtensionValue() const
{
	return mExtensionValue;
}

const bool Extension::isCritical() const 
{
	return mCritical;
}

void Extension::setExtensionValue(const QByteArray& iValue)
{
	mExtensionValue = iValue;
}

void Extension::setExtensionId(const ASN1TObjId& iObjID) 
{
	mExtensionId = iObjID;	
}

void Extension::setCritical(const bool iCritical)
{
	mCritical = iCritical;
}


int Extension::copyToASNObject(ASN1T_EXP_Extension & oExtension) const
{
	oExtension.critical =  mCritical;
	oExtension.extnID	 =  mExtensionId;

	oExtension.extnValue.numocts = mExtensionValue.size();
	oExtension.extnValue.data	=  new  OSOCTET[mExtensionValue.size()];	

	memcpy((char*)oExtension.extnValue.data,(char*)mExtensionValue.data(),oExtension.extnValue.numocts);

	return SUCCESS;
}


void Extension::freeASNObject(ASN1T_EXP_Extension & oExtension)const
{
	if (oExtension.extnValue.data && oExtension.extnValue.numocts>0)
	{
		DELETE_MEMORY_ARRAY(oExtension.extnValue.data)
	}
}


int Extension::copyExtensions(const ASN1T_EXP_Extensions & iExtensions, QList<Extension>& oList)const
{
	return copyASNObjects<Extension>(iExtensions,oList);
}

int Extension::copyExtensions(const QList<Extension> iList ,ASN1T_EXP_Extensions & oExtensions)const
{
	return copyASNObjects<Extension>(iList,oExtensions);
}

int	Extension::copyExtensions(const QByteArray & iASNBytes, QList<Extension>& oList)const
{
	return copyASNObjects<ASN1T_EXP_Extensions,ASN1C_EXP_Extensions,Extension>(iASNBytes,oList);
}

int	Extension::copyExtensions(const QList<Extension>& iList,QByteArray & oASNBytes)const
{
	return copyASNObjects<ASN1T_EXP_Extensions,ASN1C_EXP_Extensions,Extension>(iList,oASNBytes);
}

void Extension::freeExtensions(ASN1T_EXP_Extensions & oExtensions)const
{
	freeASNObjects(oExtensions);
}

bool operator==(const Extension & iRHS,const Extension & iLHS)
{
	if	(	(iRHS.isCritical() != iLHS.isCritical() )
		||	(iRHS.getExtensionId() != iLHS.getExtensionId())
		||	(iRHS.getExtensionValue() != iLHS.getExtensionValue())  )
		return false;

	return true;
}

bool operator!=(const Extension & iRHS, const Extension & iLHS)
{
	return !(iRHS == iLHS);
}



Extension::~Extension(void)
{
}
NAMESPACE_END
