#include "RevokedInfo.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

RevokedInfo::RevokedInfo(void)
{
}

RevokedInfo::RevokedInfo(const RevokedInfo &iRI)
:	mRevocationReasonPresent(iRI.isRevocationReasonPresent()),
	mRevocationReason(iRI.getRevocationReason()),
	mRevocationTime(iRI.getRevocationTime())
{

}

RevokedInfo::RevokedInfo(const ASN1T_OCSP_RevokedInfo & iRI)
{
	copyFromASNObject(iRI);
}

RevokedInfo::RevokedInfo(const QByteArray & iRI)
{
	constructObject(iRI);
}


RevokedInfo & RevokedInfo::operator=(const RevokedInfo	&iRI)
{
	mRevocationReasonPresent	= iRI.isRevocationReasonPresent();
	mRevocationReason			= iRI.getRevocationReason();
	mRevocationTime				= iRI.getRevocationTime();
	return *this;
}

bool operator==(const RevokedInfo & iRHS, const RevokedInfo & iLHS)
{
	if (iRHS.getRevocationTime() != iLHS.getRevocationTime())
		return false;
	if (iRHS.isRevocationReasonPresent() != iLHS.isRevocationReasonPresent())
		return false;
	if (iRHS.isRevocationReasonPresent() && iRHS.getRevocationReason() != iLHS.getRevocationReason())
		return false;
	return true;
}

bool operator!=(const RevokedInfo & iRHS, const RevokedInfo & iLHS)
{
	return ( ! ( iRHS == iLHS ) );
}

int RevokedInfo::copyFromASNObject(const ASN1T_OCSP_RevokedInfo& iRI)
{
	mRevocationTime = QString(iRI.revocationTime);
	mRevocationReasonPresent = iRI.m.revocationReasonPresent;
	if (mRevocationReasonPresent)
		mRevocationReason = iRI.revocationReason;
	return SUCCESS;
}

int RevokedInfo::copyToASNObject(ASN1T_OCSP_RevokedInfo & oRI) const
{
	oRI.revocationTime = myStrDup(mRevocationTime);
	oRI.m.revocationReasonPresent = mRevocationReasonPresent;
	if (mRevocationReasonPresent)
		oRI.revocationReason = mRevocationReason;
	return SUCCESS;
}

void RevokedInfo::freeASNObject(ASN1T_OCSP_RevokedInfo& oRI)const
{
	DELETE_MEMORY_ARRAY(oRI.revocationTime);
}

const bool& RevokedInfo::isRevocationReasonPresent() const
{
	return mRevocationReasonPresent;
}

const OSUINT32& RevokedInfo::getRevocationReason() const
{
	return mRevocationReason;
}

const QString& RevokedInfo::getRevocationTime() const 
{
	return mRevocationTime;
}

void RevokedInfo::setRevocationReasonPresent(const bool & iRRP)
{
	mRevocationReasonPresent = iRRP;
}

void RevokedInfo::setRevocationReason(const OSUINT32 & iRR)
{
	mRevocationReason = iRR;
}

void RevokedInfo::setRevocationTime(const QString & iRT)
{
	mRevocationTime = iRT;
}

RevokedInfo::~RevokedInfo(void)
{
}
NAMESPACE_END
