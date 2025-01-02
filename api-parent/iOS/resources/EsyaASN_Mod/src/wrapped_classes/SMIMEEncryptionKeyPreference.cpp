#include "SMIMEEncryptionKeyPreference.h"

namespace esya
{

SMIMEEncryptionKeyPreference::SMIMEEncryptionKeyPreference(void)
:	mSAKI(NULL),
	mRKI(NULL),
	mISA(NULL)
{
}

SMIMEEncryptionKeyPreference::SMIMEEncryptionKeyPreference(const QByteArray & iEKP)
:	mSAKI(NULL),
	mRKI(NULL),
	mISA(NULL)
{
	constructObject(iEKP);
}

SMIMEEncryptionKeyPreference::SMIMEEncryptionKeyPreference(const ASN1T_CMS_SMIMEEncryptionKeyPreference & iEKP)
:	mSAKI(NULL),
	mRKI(NULL),
	mISA(NULL)
{
	copyFromASNObject(iEKP);
}

SMIMEEncryptionKeyPreference::SMIMEEncryptionKeyPreference(const SMIMEEncryptionKeyPreference& iEKP)
:	mSAKI(NULL),
	mRKI(NULL),
	mISA(NULL),
	mType(iEKP.getType())
{
	switch (mType)
	{
	case  EKP_SubjectAltKeyIdentifier:
		{
			mSAKI = new SubjectKeyIdentifier(*iEKP.getSAKI());
			break;
		}
	case  EKP_RecipientKeyID:
		{
			mRKI = new RecipientKeyIdentifier(*iEKP.getRKI());
			break;
		}
	case  EKP_IssuerAndSerialNumber:
		{
			mISA = new IssuerAndSerialNumber(*iEKP.getISA());
			break;
		}
	}
}

SMIMEEncryptionKeyPreference& SMIMEEncryptionKeyPreference::operator=(const SMIMEEncryptionKeyPreference& iEKP)
{
	mSAKI = NULL;
	mRKI  = NULL;
	mISA  = NULL;
	mType = iEKP.getType();

	switch (mType)
	{
	case  EKP_SubjectAltKeyIdentifier:
		{
			mSAKI = new SubjectKeyIdentifier(*iEKP.getSAKI());
			break;
		}
	case  EKP_RecipientKeyID:
		{
			mRKI = new RecipientKeyIdentifier(*iEKP.getRKI());
			break;
		}
	case  EKP_IssuerAndSerialNumber:
		{
			mISA = new IssuerAndSerialNumber(*iEKP.getISA());
			break;
		}
	}	

	return *this;
}

bool operator==(const SMIMEEncryptionKeyPreference & iRHS, const SMIMEEncryptionKeyPreference& iLHS)
{
	if ( iRHS.getType() != iLHS.getType())
		return false;

	switch (iRHS.getType())
	{
	case  EKP_SubjectAltKeyIdentifier:
		{
			return  (	iRHS.getSAKI() && 
						iLHS.getSAKI() &&
						(*(iRHS.getSAKI()) == *(iLHS.getSAKI())) );
		}
	case  EKP_RecipientKeyID:
		{
			return  (	iRHS.getRKI() && 
						iLHS.getRKI() &&
						(*(iRHS.getRKI()) == *(iLHS.getRKI())) );
		}
	case  EKP_IssuerAndSerialNumber:
		{
			return  (	iRHS.getISA() && 
						iLHS.getISA() &&
						(*(iRHS.getISA()) == *(iLHS.getISA())) );
		}
	}	

}

bool operator!=(const SMIMEEncryptionKeyPreference & iRHS, const SMIMEEncryptionKeyPreference& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int SMIMEEncryptionKeyPreference::copyFromASNObject(const ASN1T_CMS_SMIMEEncryptionKeyPreference & iEKP ) 
{
	mType = (EKP_Type)iEKP.t;

	switch (mType)
	{
	case  EKP_SubjectAltKeyIdentifier:
		{
			setSAKI(new SubjectKeyIdentifier(*iEKP.u.subjectAltKeyIdentifier) );
			break;
		}
	case  EKP_RecipientKeyID:
		{
			setRKI(new RecipientKeyIdentifier(*iEKP.u.receipentKeyId) );
			break;
		}
	case  EKP_IssuerAndSerialNumber:
		{
			setISA(new IssuerAndSerialNumber(*iEKP.u.issuerAndSerialNumber) );
			break;
		}
	}	

	return SUCCESS;
}

int SMIMEEncryptionKeyPreference::copyToASNObject(ASN1T_CMS_SMIMEEncryptionKeyPreference & oEKP) const
{
	oEKP.t = mType ;

	switch (mType)
	{
	case  EKP_SubjectAltKeyIdentifier:
		{
			oEKP.u.subjectAltKeyIdentifier = mSAKI->getASNCopy();
			break;
		}
	case  EKP_RecipientKeyID:
		{
			oEKP.u.receipentKeyId = mRKI->getASNCopy();
			break;
		}
	case  EKP_IssuerAndSerialNumber:
		{
			oEKP.u.issuerAndSerialNumber = mISA->getASNCopy();
			break;
		}
	}	

	return SUCCESS;
}

void SMIMEEncryptionKeyPreference::freeASNObject(ASN1T_CMS_SMIMEEncryptionKeyPreference & oEKP)const
{
	switch ((EKP_Type) oEKP.t)
	{
	case  EKP_SubjectAltKeyIdentifier:
		{
			SubjectKeyIdentifier().freeASNObjectPtr(oEKP.u.subjectAltKeyIdentifier) ;
			break;
		}
	case  EKP_RecipientKeyID:
		{
			RecipientKeyIdentifier().freeASNObjectPtr(oEKP.u.receipentKeyId);
			break;
		}
	case  EKP_IssuerAndSerialNumber:
		{
			IssuerAndSerialNumber().freeASNObjectPtr(oEKP.u.issuerAndSerialNumber);
			break;
		}
	}
}

const EKP_Type&	SMIMEEncryptionKeyPreference::getType()const
{
	return mType;
}

const SubjectKeyIdentifier*	SMIMEEncryptionKeyPreference::getSAKI() const 
{
	return mSAKI;
}

const IssuerAndSerialNumber* SMIMEEncryptionKeyPreference::getISA() const 
{
	return mISA;
}

const RecipientKeyIdentifier* SMIMEEncryptionKeyPreference::getRKI() const 
{
	return mRKI;
}

void SMIMEEncryptionKeyPreference::setType(const EKP_Type & iType)
{
	mType = iType;
}

void SMIMEEncryptionKeyPreference::setSAKI(SubjectKeyIdentifier* iSAKI)
{
	mType = EKP_SubjectAltKeyIdentifier;
	DELETE_MEMORY(mRKI);
	DELETE_MEMORY(mISA);
	
	if (iSAKI != mSAKI )
	{ 
		DELETE_MEMORY(mSAKI);
		mSAKI = iSAKI;
	}
}

void SMIMEEncryptionKeyPreference::setISA(IssuerAndSerialNumber* iISA)
{
	mType = EKP_IssuerAndSerialNumber;
	DELETE_MEMORY(mRKI);
	DELETE_MEMORY(mSAKI);

	if (iISA != mISA )
	{ 
		DELETE_MEMORY(mISA);
		mISA = iISA;
	}
}

void SMIMEEncryptionKeyPreference::setRKI(RecipientKeyIdentifier* iRKI)
{
	mType = EKP_RecipientKeyID;
	DELETE_MEMORY(mSAKI);
	DELETE_MEMORY(mISA);

	if (iRKI != mRKI )
	{ 
		DELETE_MEMORY(mRKI);
		mRKI = iRKI;
	}
}

SMIMEEncryptionKeyPreference::~SMIMEEncryptionKeyPreference(void)
{
	DELETE_MEMORY(mSAKI)
	DELETE_MEMORY(mRKI)
	DELETE_MEMORY(mISA)
}


}