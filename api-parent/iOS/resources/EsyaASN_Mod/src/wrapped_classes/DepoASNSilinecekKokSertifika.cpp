#include "DepoASNSilinecekKokSertifika.h"

using namespace esya;

DepoASNSilinecekKokSertifika::DepoASNSilinecekKokSertifika(void)
{
}

DepoASNSilinecekKokSertifika::DepoASNSilinecekKokSertifika(const ASN1T_SD_DepoASNSilinecekKokSertifika & iKS)
{
	copyFromASNObject(iKS);
}

DepoASNSilinecekKokSertifika::DepoASNSilinecekKokSertifika(const QByteArray & iKS)
{
	constructObject(iKS);
}

DepoASNSilinecekKokSertifika::DepoASNSilinecekKokSertifika(const DepoASNSilinecekKokSertifika & iKS)
: 	mKokSertifikaValue(iKS.getKokSertifikaValue()),
	mKokSerialNumber(iKS.getKokSerialNumber()),
	mKokIssuerName(iKS.getKokIssuerName()),
	mKokSubjectName(iKS.getKokSubjectName())
{

}

DepoASNSilinecekKokSertifika & DepoASNSilinecekKokSertifika::operator=(const DepoASNSilinecekKokSertifika & iKS)
{
	mKokSertifikaValue			= iKS.getKokSertifikaValue();
	mKokSerialNumber			= iKS.getKokSerialNumber();
	mKokIssuerName				= iKS.getKokIssuerName();
	mKokSubjectName				= iKS.getKokSubjectName();
	return *this;
}

bool esya::operator==(const DepoASNSilinecekKokSertifika & iRHS, const DepoASNSilinecekKokSertifika& iLHS)
{
	return (	( iRHS.getKokSertifikaValue()		== iLHS.getKokSertifikaValue()	) &&
				( iRHS.getKokSerialNumber()			== iLHS.getKokSerialNumber()	) &&
				( iRHS.getKokIssuerName()			== iLHS.getKokIssuerName()		) &&
				( iRHS.getKokSubjectName()			== iLHS.getKokSubjectName()		)		);

}

bool esya::operator!=(const DepoASNSilinecekKokSertifika& iRHS, const DepoASNSilinecekKokSertifika& iLHS)
{
	return ( !( iRHS == iLHS ) );
}


int DepoASNSilinecekKokSertifika::copyFromASNObject(const ASN1T_SD_DepoASNSilinecekKokSertifika& iKS)
{
	mKokSertifikaValue			= toByteArray( iKS.kokSertifikaValue );

	mKokSerialNumber.copyFromASNObject(iKS.kokSerialNumber);
	mKokIssuerName.copyFromASNObject(iKS.kokIssuerName);
	mKokSubjectName.copyFromASNObject(iKS.kokSubjectName);

	return SUCCESS;
}

int DepoASNSilinecekKokSertifika::copyToASNObject(ASN1T_SD_DepoASNSilinecekKokSertifika & oKS) const
{
	oKS.kokSertifikaValue.data		= (OSOCTET*) myStrDup(mKokSertifikaValue.data(),mKokSertifikaValue.size() ) ;
	oKS.kokSertifikaValue.numocts	= mKokSertifikaValue.size();

	mKokSerialNumber.copyToASNObject(oKS.kokSerialNumber);
	mKokIssuerName.copyToASNObject(oKS.kokIssuerName);
	mKokSubjectName.copyToASNObject(oKS.kokSubjectName);

	return SUCCESS;
}


void DepoASNSilinecekKokSertifika::freeASNObject(ASN1T_SD_DepoASNSilinecekKokSertifika& oKS)const
{
	if (oKS.kokSertifikaValue.numocts > 0  )
		DELETE_MEMORY_ARRAY(oKS.kokSertifikaValue.data);

	SerialNumber().freeASNObject(oKS.kokSerialNumber);
	Name().freeASNObject(oKS.kokIssuerName);
	Name().freeASNObject(oKS.kokSubjectName);
}

const QByteArray & DepoASNSilinecekKokSertifika::getKokSertifikaValue() const 
{
	return mKokSertifikaValue;
}

const SerialNumber & DepoASNSilinecekKokSertifika::getKokSerialNumber() const 
{
	return mKokSerialNumber;
}

const Name & DepoASNSilinecekKokSertifika::getKokIssuerName() const 
{
	return mKokIssuerName;
}

const Name	& DepoASNSilinecekKokSertifika::getKokSubjectName() const 
{
	return mKokSubjectName;
}


void DepoASNSilinecekKokSertifika::setKokSertifikaValue(const QByteArray & iKokSertifikaValue)
{
	mKokSertifikaValue = iKokSertifikaValue;
}

void DepoASNSilinecekKokSertifika::setKokSerialNumber(const SerialNumber & iKokSerialNumber)
{
	mKokSerialNumber = iKokSerialNumber;
}

void DepoASNSilinecekKokSertifika::setKokIssuerName(const Name & iKokIssuerName)
{
	mKokIssuerName = iKokIssuerName;
}

void DepoASNSilinecekKokSertifika::setKokSubjectName(const Name & iKokSubjectName)
{
	mKokSubjectName = iKokSubjectName;
}

DepoASNSilinecekKokSertifika::~DepoASNSilinecekKokSertifika(void)
{
}
