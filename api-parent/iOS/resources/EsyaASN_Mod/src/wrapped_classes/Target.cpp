#include "Target.h"

using namespace esya;

Target::Target(void)
:	mType(TT_TargetName)
{
}

Target::Target(const QByteArray & iTarget)
{
	constructObject(iTarget);
}

Target::Target(const ASN1T_ATTRCERT_Target & iTarget )
{
	copyFromASNObject(iTarget);
}

Target::Target(const Target& iTarget)
:	mType(iTarget.getType())
{
	switch (mType)
	{
	case TT_TargetName :
		{
			mTargetName = iTarget.getTargetName();
			break;
		}
	case TT_TargetGroup :
		{
			mTargetGroup = iTarget.getTargetGroup();
			break;
		}
	case TT_TargetCert :
		{
			mTargetCert = iTarget.getTargetCert();
			break;
		}
	}
}

Target& Target::operator=(const Target& iTarget)
{
	mType = iTarget.getType();
	switch (mType)
	{
	case TT_TargetName :
		{
			mTargetName = iTarget.getTargetName();
			break;
		}
	case TT_TargetGroup :
		{
			mTargetGroup = iTarget.getTargetGroup();
			break;
		}
	case TT_TargetCert :
		{
			mTargetCert = iTarget.getTargetCert();
			break;
		}
	}

	return *this;
}

bool esya::operator==( const Target& iRHS, const Target& iLHS)
{
	if ( iRHS.getType() != iLHS.getType() )
		return false;

	switch (iRHS.getType())
	{
	case Target::TT_TargetName :
		{
			if ( iRHS.getTargetName() != iLHS.getTargetName() )
				return false;
			break;
		}
	case Target::TT_TargetGroup :
		{
			if ( iRHS.getTargetGroup() != iLHS.getTargetGroup() )
				return false;
			break;
		}
	case Target::TT_TargetCert :
		{
			if ( iRHS.getTargetCert() != iLHS.getTargetCert() )
				return false;
			break;
		}
	}

	return true;
}

bool esya::operator!=( const Target& iRHS, const Target& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int Target::copyFromASNObject(const ASN1T_ATTRCERT_Target & iTarget)
{
	mType = (TargetType)iTarget.t;

	switch (mType)
	{
	case TT_TargetName :
		{
			mTargetName.copyFromASNObject(*(iTarget.u.targetName));
			break;
		}
	case TT_TargetGroup :
		{
			mTargetGroup.copyFromASNObject(*iTarget.u.targetGroup);
			break;
		}
	case TT_TargetCert :
		{
			mTargetCert.copyFromASNObject(*iTarget.u.targetCert);
			break;
		}
	}

	return SUCCESS;
}

int Target::copyToASNObject(ASN1T_ATTRCERT_Target &oTarget)const
{
	oTarget.t = mType ;

	oTarget.u.targetName = NULL;
	oTarget.u.targetGroup = NULL;
	oTarget.u.targetCert = NULL;

	switch (mType)
	{
	case TT_TargetName :
		{
			oTarget.u.targetName = mTargetName.getASNCopy();
			break;
		}
	case TT_TargetGroup :
		{
			oTarget.u.targetGroup = mTargetGroup.getASNCopy();
			break;
		}
	case TT_TargetCert :
		{
			oTarget.u.targetCert = mTargetCert.getASNCopy();
			break;
		}
	}

	return SUCCESS;
}

void Target::freeASNObject(ASN1T_ATTRCERT_Target& oTarget)const
{
	switch (oTarget.t)
	{
	case TT_TargetName :
		{
			GeneralName().freeASNObjectPtr(oTarget.u.targetName);
			break;
		}
	case TT_TargetGroup :
		{
			GeneralName().freeASNObjectPtr(oTarget.u.targetGroup);
			break;
		}
	case TT_TargetCert :
		{
			TargetCert().freeASNObjectPtr(oTarget.u.targetCert);
			break;
		}
	}
}

int Target::copyTargets(const ASN1T_ATTRCERT_Targets & iTargets, QList<Target>& oList)
{
	return copyASNObjects<Target>(iTargets,oList);
}

int Target::copyTargets(const QList<Target> iList ,ASN1T_ATTRCERT_Targets & oTargets)
{
	return copyASNObjects<Target>(iList,oTargets);
}

int Target::copyTargets(const QByteArray & iASNBytes, QList<Target>& oList)
{
	return copyASNObjects<ASN1T_ATTRCERT_Targets,ASN1C_ATTRCERT_Targets,Target>(iASNBytes,oList);
}

const Target::TargetType&  Target::getType()const
{
	return mType;
}

const GeneralName& Target::getTargetName()const
{
	return mTargetName;
}

const GeneralName& Target::getTargetGroup()const
{
	return mTargetGroup;
}

const TargetCert&  Target::getTargetCert()const
{
	return mTargetCert;
}

void Target::setType(const TargetType&  iType)
{
	mType = iType;
}

void Target::setTargetName(const GeneralName& iTargetName)
{
	mTargetName = iTargetName;
}

void Target::setTargetGroup(const GeneralName& iTargetGroup)
{
	mTargetGroup = iTargetGroup;
}

void Target::setTargetCert(const TargetCert&  iTargetCert)
{
	mTargetCert = iTargetCert;
}

Target::~Target(void)
{
}