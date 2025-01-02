#include "MQVUserKeyingMaterial.h"

using namespace esya;

MQVUserKeyingMaterial::MQVUserKeyingMaterial(void)
{
}

MQVUserKeyingMaterial::MQVUserKeyingMaterial(const QByteArray & iUKM)
{
	constructObject(iUKM);
}


MQVUserKeyingMaterial::MQVUserKeyingMaterial(const QByteArray & iAddedUKM, const SubjectPublicKeyInfo &iEPK)
: mAddedUKM(iAddedUKM),mEphemeralPublicKey(iEPK)
{
	mAddedUKMPresent= !mAddedUKM.isEmpty();
}

MQVUserKeyingMaterial::MQVUserKeyingMaterial(const ASN1T_CMS_MQVuserKeyingMaterial& iUKM)
{
	copyFromASNObject(iUKM);
}


MQVUserKeyingMaterial::MQVUserKeyingMaterial(const MQVUserKeyingMaterial& iUKM)
:	mAddedUKM(iUKM.getAddedUKM()),
	mEphemeralPublicKey(iUKM.getEphemeralPublicKey()),
	mAddedUKMPresent(iUKM.isAddedUKMPresent())
{
}

MQVUserKeyingMaterial& MQVUserKeyingMaterial::operator=(const MQVUserKeyingMaterial& iUKM)
{
	mEphemeralPublicKey	= iUKM.getEphemeralPublicKey();
	mAddedUKM			= iUKM.getAddedUKM();
	mAddedUKMPresent	= iUKM.isAddedUKMPresent();
	return *this;
}


bool esya::operator==(const MQVUserKeyingMaterial & iRHS, const MQVUserKeyingMaterial& iLHS)
{
	return ( iRHS.getEphemeralPublicKey() == iLHS.getEphemeralPublicKey() && iRHS.getAddedUKM()==iRHS.getAddedUKM() );
}

bool esya::operator!=(const MQVUserKeyingMaterial & iRHS, const MQVUserKeyingMaterial& iLHS)
{
	return ( ! ( iRHS == iLHS ));
}


int MQVUserKeyingMaterial::copyFromASNObject(const ASN1T_CMS_MQVuserKeyingMaterial& iUKM) 
{
	mEphemeralPublicKey.copyFromASNObject((const ASN1T_EXP_SubjectPublicKeyInfo& )iUKM.ephemeralPublicKey);
	mAddedUKMPresent= (iUKM.m.addedukmPresent == 1);

	if (iUKM.m.addedukmPresent)
	{
		mAddedUKM= QByteArray((char*)iUKM.addedukm.data,iUKM.addedukm.numocts);
	}

	return SUCCESS;
}


const QByteArray &MQVUserKeyingMaterial::getAddedUKM()const 
{
	return mAddedUKM;
}

const SubjectPublicKeyInfo &MQVUserKeyingMaterial::getEphemeralPublicKey() const
{
	return mEphemeralPublicKey;
}

bool MQVUserKeyingMaterial::isAddedUKMPresent() const
{
	return mAddedUKMPresent;
}


void MQVUserKeyingMaterial::setEphemeralPublicKey(const SubjectPublicKeyInfo& iEPK)
{
	mEphemeralPublicKey= iEPK;
}


void MQVUserKeyingMaterial::setAddedUKM(const QByteArray& iParams)
{
	mAddedUKMPresent = true;
	mAddedUKM = iParams;	
}

int MQVUserKeyingMaterial::copyToASNObject(ASN1T_CMS_MQVuserKeyingMaterial & oUKM) const 
{
	mEphemeralPublicKey.copyToASNObject((ASN1T_EXP_SubjectPublicKeyInfo&)oUKM.ephemeralPublicKey) ;
	oUKM.m.addedukmPresent = mAddedUKMPresent ? 1:0;
	if (oUKM.m.addedukmPresent)
	{
		oUKM.addedukm.data = (OSOCTET*)myStrDup(mAddedUKM.data(),mAddedUKM.size());
		oUKM.addedukm.numocts = mAddedUKM.size(); 
	}
	return SUCCESS;
}

void MQVUserKeyingMaterial::freeASNObject(ASN1T_CMS_MQVuserKeyingMaterial & oUKM)const
{
	SubjectPublicKeyInfo().freeASNObject((ASN1T_EXP_SubjectPublicKeyInfo&)oUKM.ephemeralPublicKey);
	if (oUKM.m.addedukmPresent && oUKM.addedukm.numocts>0 && oUKM.addedukm.data)
	{
		DELETE_MEMORY_ARRAY(oUKM.addedukm.data);
		oUKM.addedukm.numocts = 0;
	}
}

MQVUserKeyingMaterial::~MQVUserKeyingMaterial(void)
{
}
