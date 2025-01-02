#include "RecipientInfo.h"

using namespace esya;

RecipientInfo::RecipientInfo(void)
{
}

RecipientInfo::RecipientInfo( const ASN1T_CMS_RecipientInfo &iRI)
{
	copyFromASNObject(iRI);
}

RecipientInfo::RecipientInfo( const QByteArray &iRI)
{
	constructObject(iRI);
}

RecipientInfo::RecipientInfo( const RecipientInfo& iRI)
{
	mType = iRI.getType();
	switch (mType)
	{
	case T_KTRI:{
		mKTRI = iRI.getKTRI();
		break;
				}
	case T_PWRI:{
		mPWRI = iRI.getPWRI();
		break;
				}
	case T_ORI:{
		mORI = iRI.getORI();
		break;
				}
	case T_KARI:{
		mKARI = iRI.getKARI();
		break;
			   }
	default : throw EException("Desteklenmeyen RecipientInfoTipi");
	}
}

RecipientInfo::RecipientInfo( const KeyTransRecipientInfo& iKTRI)
:	mType(T_KTRI),
	mKTRI(iKTRI)
{
}

RecipientInfo::RecipientInfo( const KeyAgreeRecipientInfo& iKARI)
:	mType(T_KARI),
	mKARI(iKARI)
{
}

RecipientInfo::RecipientInfo( const PasswordRecipientInfo& iPWRI)
:	mType(T_PWRI),
	mPWRI(iPWRI)
{
}

RecipientInfo::RecipientInfo( const OtherRecipientInfo& iORI)
:	mType(T_ORI),
	mORI(iORI)
{
}

RecipientInfo & RecipientInfo::operator=(const RecipientInfo & iRI)
{
	mType = iRI.getType();
	switch (mType)
	{
	case T_KTRI:{
		mKTRI = iRI.getKTRI();
		break;
				}
	case T_PWRI:{
		mPWRI = iRI.getPWRI();
		break;
				}
	case T_ORI:{
		mORI = iRI.getORI();
		break;
				}
	case T_KARI:{
		mKARI = iRI.getKARI();
		break;
			   }
	default : throw EException("Desteklenmeyen RecipientInfoTipi");
	}
	return *this;
}

bool esya::operator==(const RecipientInfo & iRHS,const RecipientInfo & iLHS)
{
	if ( iRHS.getType() != iLHS.getType())
		return false;

	switch (iRHS.getType())
	{
	case RecipientInfo::T_KTRI	:
		{
			return (iRHS.getKTRI() == iLHS.getKTRI());
		}
	case RecipientInfo::T_PWRI	:
		{
			return (iRHS.getPWRI() == iLHS.getPWRI());
		}
	case RecipientInfo::T_ORI	:
		{
			return (iRHS.getORI() == iLHS.getORI());
		}
	case RecipientInfo::T_KARI	:
		{
			return (iRHS.getKARI() == iLHS.getKARI());
		}
	default		:		return false;
	}
}

bool esya::operator!=(const RecipientInfo & iRHS,const RecipientInfo & iLHS)
{
	return ( !(iRHS == iLHS) );
}

int RecipientInfo::copyFromASNObject(const ASN1T_CMS_RecipientInfo & iRI)
{
	mType = (RInfoType)iRI.t;
	switch (mType)
	{
	case T_KTRI:{
					mKTRI.copyFromASNObject(*iRI.u.ktri);
					break;
				}
	case T_PWRI:{
					mPWRI.copyFromASNObject(*iRI.u.pwri);
					break;
				}
	case T_ORI:{
					mORI.copyFromASNObject(*iRI.u.ori);
					break;
				}
	case T_KARI:{
					mKARI.copyFromASNObject(*iRI.u.kari);
					break;
				}
	default : throw EException("Desteklenmeyen RecipientInfoTipi");
	}
	return SUCCESS;
}

int RecipientInfo::copyToASNObject(ASN1T_CMS_RecipientInfo & oRI)const
{
	oRI.t = mType;
	switch (mType)
	{
	case T_KTRI:{
					oRI.u.ktri = mKTRI.getASNCopy();
					break;
				}
	case T_PWRI:{
					oRI.u.pwri = mPWRI.getASNCopy();
					break;
				}
	case T_ORI:{
					oRI.u.ori = mORI.getASNCopy();
					break;
				}
	case T_KARI:{
					oRI.u.kari = mKARI.getASNCopy();
					break;
			   }
	default : throw EException("Desteklenmeyen RecipientInfoTipi");
	}
}

void RecipientInfo::freeASNObject(ASN1T_CMS_RecipientInfo & oRI)const
{
	switch (oRI.t)
	{
	case T_KTRI:{
					KeyTransRecipientInfo().freeASNObjectPtr( oRI.u.ktri );
					break;
				}
	case T_PWRI:{
					PasswordRecipientInfo().freeASNObjectPtr(oRI.u.pwri);
					break;
				}
	case T_ORI:{
					OtherRecipientInfo().freeASNObjectPtr(oRI.u.ori);
					break;
				}
	case T_KARI:{
					KeyAgreeRecipientInfo().freeASNObjectPtr( oRI.u.kari);
					break;
			   }
	default : throw EException("Desteklenmeyen RecipientInfoTipi");
	}
}

int RecipientInfo::copyRIs(const ASN1T_CMS_RecipientInfos & iRIs, QList<RecipientInfo>& oList)
{
	return copyASNObjects<RecipientInfo>(iRIs,oList);
}


int RecipientInfo::copyRIs(const QList<RecipientInfo> & iList ,ASN1T_CMS_RecipientInfos& oRIs)
{
	return copyASNObjects<RecipientInfo>(iList,oRIs);
}

int RecipientInfo::copyRIs(const QByteArray & iASNBytes ,QList<RecipientInfo>& oList)
{
	return copyASNObjects<ASN1T_CMS_RecipientInfos,ASN1C_CMS_RecipientInfos,RecipientInfo>(iASNBytes,oList);
}

int RecipientInfo::copyRIs(const QList<RecipientInfo>& iList, QByteArray & oASNBytes )
{
	return copyASNObjects<ASN1T_CMS_RecipientInfos,ASN1C_CMS_RecipientInfos,RecipientInfo>(iList,oASNBytes);
}

const PasswordRecipientInfo	& RecipientInfo::getPWRI()const
{
	return mPWRI;
}

const KeyTransRecipientInfo	& RecipientInfo::getKTRI()const
{
	return mKTRI;
}

const OtherRecipientInfo	& RecipientInfo::getORI()const
{
	return mORI;
}

const KeyAgreeRecipientInfo	& RecipientInfo::getKARI()const
{
	return mKARI;
}

const RecipientInfo::RInfoType	& RecipientInfo::getType()const
{
	return mType;
}

void RecipientInfo::setKTRI(const KeyTransRecipientInfo & iKTRI )
{
	mType = T_KTRI;
	mKTRI = iKTRI;
}

void RecipientInfo::setKARI(const KeyAgreeRecipientInfo & iKARI )
{
	mType = T_KARI;
	mKARI = iKARI;
}

void RecipientInfo::setPWRI(const PasswordRecipientInfo & iPWRI )
{
	mType = T_PWRI;
	mPWRI = iPWRI;
}

void RecipientInfo::setORI(const OtherRecipientInfo & iORI )
{
	mType = T_ORI;
	mORI = iORI;
}

RecipientInfo::~RecipientInfo(void)
{
}

