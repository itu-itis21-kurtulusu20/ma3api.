#include "PolicyConstraints.h"
#include "OrtakDil.h"

using namespace esya;

PolicyConstraints::PolicyConstraints(void)
{
}

PolicyConstraints::PolicyConstraints(const PolicyConstraints &iPC)
:	mREPPresent(iPC.isREPPresent()),
	mREP(iPC.getREP()),
	mIPMPresent(iPC.isIPMPresent()),
	mIPM(iPC.getIPM())
{
}

PolicyConstraints::PolicyConstraints(const ASN1T_IMP_PolicyConstraints & iPC)
{
	copyFromASNObject(iPC);
}

PolicyConstraints::PolicyConstraints(const QByteArray & iPC)
{
	constructObject(iPC);
}

PolicyConstraints& PolicyConstraints::operator=(const PolicyConstraints & iPC)
{
	mREPPresent = iPC.isREPPresent();
	mIPMPresent = iPC.isIPMPresent();
	
	if ( mREPPresent )
		mREP = iPC.getREP();
	if (mIPMPresent)
		mIPM = iPC.getIPM();
	return (*this);
}

bool esya::operator==(const PolicyConstraints& iRHS, const PolicyConstraints& iLHS)
{
	if ( iRHS.isIPMPresent() != iLHS.isIPMPresent() || iRHS.isREPPresent() != iLHS.isREPPresent())
		return false;
	if ( iRHS.isIPMPresent() && iRHS.getIPM() != iLHS.getIPM() )
		return false;
	if ( iRHS.isREPPresent() && iRHS.getREP() != iLHS.getREP() )
		return false;
	return true;
}

bool esya::operator!=(const PolicyConstraints& iRHS, const PolicyConstraints& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int PolicyConstraints::copyFromASNObject(const ASN1T_IMP_PolicyConstraints& iPC)
{
	mREPPresent = iPC.m.requireExplicitPolicyPresent;
	mIPMPresent = iPC.m.inhibitPolicyMappingPresent;
	
	if ( mREPPresent )
		mREP = iPC.requireExplicitPolicy;
	if (mIPMPresent)
		mIPM = iPC.inhibitPolicyMapping;
	
	return SUCCESS;	
}

int PolicyConstraints::copyToASNObject(ASN1T_IMP_PolicyConstraints & oPC) const
{
	oPC.m.requireExplicitPolicyPresent	= mREPPresent;
	oPC.m.inhibitPolicyMappingPresent	= mIPMPresent;

	if ( mREPPresent )
		oPC.requireExplicitPolicy = mREP;
	if ( mIPMPresent)
		oPC.inhibitPolicyMapping = mIPM;
	
	return SUCCESS;
}

void PolicyConstraints::freeASNObject(ASN1T_IMP_PolicyConstraints & oPC)const
{
	/* NOTHING TO FREE */
}

const bool& PolicyConstraints::isREPPresent() const
{
	return mREPPresent;
}

const bool& PolicyConstraints::isIPMPresent() const
{
	return mIPMPresent;
}

const OSUINT32& PolicyConstraints::getREP() const
{
	return mREP;
}

const OSUINT32& PolicyConstraints::getIPM() const
{
	return mIPM;
}


PolicyConstraints::~PolicyConstraints(void)
{
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString PolicyConstraints::eklentiAdiAl() const 
{
	return DIL_EXT_ILKE_KISITLAMALARI;
}

QString PolicyConstraints::eklentiKisaDegerAl()	const 
{
	return "";
}

QString PolicyConstraints::eklentiUzunDegerAl()	const 
{
	return "";
}

AY_Eklenti* PolicyConstraints::kendiniKopyala() const 
{
	return (AY_Eklenti* )new PolicyConstraints(*this);
}
