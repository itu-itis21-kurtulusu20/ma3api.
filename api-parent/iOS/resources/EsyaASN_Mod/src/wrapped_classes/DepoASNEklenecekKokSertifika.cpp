#include "DepoASNEklenecekKokSertifika.h"

using namespace esya;

DepoASNEklenecekKokSertifika::DepoASNEklenecekKokSertifika(void)
{
}

DepoASNEklenecekKokSertifika::DepoASNEklenecekKokSertifika(const ASN1T_SD_DepoASNEklenecekKokSertifika & iKS)
{
	copyFromASNObject(iKS);
}

DepoASNEklenecekKokSertifika::DepoASNEklenecekKokSertifika(const QByteArray & iKS)
{
	constructObject(iKS);
}

DepoASNEklenecekKokSertifika::DepoASNEklenecekKokSertifika(const DepoASNEklenecekKokSertifika & iKS)
: 	mKokSertifikaValue(iKS.getKokSertifikaValue()),
 	mKokSertifikaHash(iKS.getKokSertifikaHash()),
	mKokSerialNumber(iKS.getKokSerialNumber()),
	mKokIssuerName(iKS.getKokIssuerName()),
	mKokSubjectName(iKS.getKokSubjectName()),
	mKokStartDate(iKS.getKokStartDate()),
	mKokEndDate(iKS.getKokEndDate()),
	mKokKeyUsage(iKS.getKokKeyUsage()),
	mKokSubjectKeyIdentifier(iKS.getKokSubjectKeyIdentifier()),
	mKokSertifikaTipi(iKS.getKokSertifikaTipi()),
	mKokGuvenSeviyesi(iKS.getKokGuvenSeviyesi())
{
		
}

DepoASNEklenecekKokSertifika & DepoASNEklenecekKokSertifika::operator=(const DepoASNEklenecekKokSertifika & iKS)
{
	mKokSertifikaValue			= iKS.getKokSertifikaValue();
	mKokSertifikaHash			= iKS.getKokSertifikaHash();
	mKokSerialNumber			= iKS.getKokSerialNumber();
	mKokIssuerName				= iKS.getKokIssuerName();
	mKokSubjectName				= iKS.getKokSubjectName();
	mKokStartDate				= iKS.getKokStartDate();
	mKokEndDate					= iKS.getKokEndDate();
	mKokKeyUsage				= iKS.getKokKeyUsage();
	mKokSubjectKeyIdentifier	= iKS.getKokSubjectKeyIdentifier();
	mKokSertifikaTipi			= iKS.getKokSertifikaTipi();
	mKokGuvenSeviyesi			= iKS.getKokGuvenSeviyesi();
	return *this;
}

bool esya::operator==(const DepoASNEklenecekKokSertifika & iRHS, const DepoASNEklenecekKokSertifika& iLHS)
{
	return (	( iRHS.getKokSertifikaValue()		== iLHS.getKokSertifikaValue()			) &&
				( iRHS.getKokSertifikaHash()		== iLHS.getKokSertifikaHash()			) &&	
				( iRHS.getKokSerialNumber()			== iLHS.getKokSerialNumber()			) &&
				( iRHS.getKokIssuerName()			== iLHS.getKokIssuerName()			) &&
				( iRHS.getKokSubjectName()			== iLHS.getKokSubjectName()			) &&
				( iRHS.getKokStartDate()			== iLHS.getKokStartDate()			) &&
				( iRHS.getKokEndDate()				== iLHS.getKokEndDate()				) &&
				( iRHS.getKokKeyUsage()				== iLHS.getKokKeyUsage()			) &&
				( iRHS.getKokSubjectKeyIdentifier()	== iLHS.getKokSubjectKeyIdentifier()) &&
				( iRHS.getKokSertifikaTipi()		== iLHS.getKokSertifikaTipi()		) &&
				( iRHS.getKokGuvenSeviyesi()		== iLHS.getKokGuvenSeviyesi()		)		);

}

bool esya::operator!=(const DepoASNEklenecekKokSertifika& iRHS, const DepoASNEklenecekKokSertifika& iLHS)
{
	return ( !( iRHS == iLHS ) );
}


int DepoASNEklenecekKokSertifika::copyFromASNObject(const ASN1T_SD_DepoASNEklenecekKokSertifika& iKS)
{
	mKokSertifikaValue			= toByteArray( iKS.kokSertifikaValue );
	mKokSertifikaHash			= toByteArray( iKS.kokSertifikaHash );
	mKokSertifikaTipi			= (KS_Type)iKS.kokSertifikaTipi;
	mKokGuvenSeviyesi			= (KGS_Type)iKS.kokGuvenSeviyesi;

	mKokSerialNumber.copyFromASNObject(iKS.kokSerialNumber);
	mKokIssuerName.copyFromASNObject(iKS.kokIssuerName);
	mKokSubjectName.copyFromASNObject(iKS.kokSubjectName);
	mKokStartDate.copyFromASNObject(iKS.kokStartDate);
	mKokEndDate.copyFromASNObject(iKS.kokEndDate);
	mKokKeyUsage.copyFromASNObject(iKS.kokKeyUsage);
	mKokSubjectKeyIdentifier.copyFromASNObject(iKS.kokSubjectKeyIdentifier);

	return SUCCESS;
}

int DepoASNEklenecekKokSertifika::copyToASNObject(ASN1T_SD_DepoASNEklenecekKokSertifika & oKS) const
{
	oKS.kokSertifikaValue.data		= (OSOCTET*) myStrDup(mKokSertifikaValue.data(),mKokSertifikaValue.size() ) ;
	oKS.kokSertifikaValue.numocts	= mKokSertifikaValue.size();

	oKS.kokSertifikaHash.data		= (OSOCTET*) myStrDup(mKokSertifikaHash.data(),mKokSertifikaHash.size() ) ;
	oKS.kokSertifikaHash.numocts	= mKokSertifikaHash.size();

	oKS.kokSertifikaTipi			= mKokSertifikaTipi;
	oKS.kokGuvenSeviyesi			= mKokGuvenSeviyesi;

	mKokSerialNumber.copyToASNObject(oKS.kokSerialNumber);
	mKokIssuerName.copyToASNObject(oKS.kokIssuerName);
	mKokSubjectName.copyToASNObject(oKS.kokSubjectName);
	mKokStartDate.copyToASNObject(oKS.kokStartDate);
	mKokEndDate.copyToASNObject(oKS.kokEndDate);
	mKokKeyUsage.copyToASNObject(oKS.kokKeyUsage);
	mKokSubjectKeyIdentifier.copyToASNObject(oKS.kokSubjectKeyIdentifier);

	return SUCCESS;

}

void DepoASNEklenecekKokSertifika::freeASNObject(ASN1T_SD_DepoASNEklenecekKokSertifika& oKS)const
{
	if (oKS.kokSertifikaValue.numocts > 0  )
		DELETE_MEMORY_ARRAY(oKS.kokSertifikaValue.data);

	oKS.kokSertifikaValue.numocts = 0;

	if (oKS.kokSertifikaHash.numocts > 0  )
		DELETE_MEMORY_ARRAY(oKS.kokSertifikaHash.data);

	oKS.kokSertifikaHash.numocts = 0;

	SerialNumber().freeASNObject(oKS.kokSerialNumber);
	Name().freeASNObject(oKS.kokIssuerName);
	Name().freeASNObject(oKS.kokSubjectName);
	ETime().freeASNObject(oKS.kokStartDate);
	ETime().freeASNObject(oKS.kokEndDate);
	KeyUsage().freeASNObject(oKS.kokKeyUsage);
	SubjectKeyIdentifier().freeASNObject(oKS.kokSubjectKeyIdentifier);

}

const QByteArray & DepoASNEklenecekKokSertifika::getKokSertifikaValue() const 
{
	return mKokSertifikaValue;
}

const QByteArray & DepoASNEklenecekKokSertifika::getKokSertifikaHash() const 
{
	return mKokSertifikaHash;
}

const SerialNumber & DepoASNEklenecekKokSertifika::getKokSerialNumber() const 
{
	return mKokSerialNumber;
}

const Name & DepoASNEklenecekKokSertifika::getKokIssuerName() const 
{
	return mKokIssuerName;
}

const Name	& DepoASNEklenecekKokSertifika::getKokSubjectName() const 
{
	return mKokSubjectName;
}

const ETime	& DepoASNEklenecekKokSertifika::getKokStartDate() const 
{
	return mKokStartDate;
}

const ETime	& DepoASNEklenecekKokSertifika::getKokEndDate() const 
{
	return mKokEndDate;
}

const KeyUsage & DepoASNEklenecekKokSertifika::getKokKeyUsage() const 
{
	return mKokKeyUsage;
}

const SubjectKeyIdentifier & DepoASNEklenecekKokSertifika::getKokSubjectKeyIdentifier() const 
{
	return mKokSubjectKeyIdentifier;
}

const DepoASNEklenecekKokSertifika::KS_Type & DepoASNEklenecekKokSertifika::getKokSertifikaTipi() const 
{
	return mKokSertifikaTipi;
}

const DepoASNEklenecekKokSertifika::KGS_Type & DepoASNEklenecekKokSertifika::getKokGuvenSeviyesi() const 
{
	return mKokGuvenSeviyesi;
}


void DepoASNEklenecekKokSertifika::setKokSertifikaValue(const QByteArray & iKokSertifikaValue)
{
	mKokSertifikaValue = iKokSertifikaValue;
}

void DepoASNEklenecekKokSertifika::setKokSertifikaHash(const QByteArray & iKokSertifikaHash)
{
	mKokSertifikaHash = iKokSertifikaHash;
}

void DepoASNEklenecekKokSertifika::setKokSerialNumber(const SerialNumber & iKokSerialNumber)
{
	mKokSerialNumber = iKokSerialNumber;
}

void DepoASNEklenecekKokSertifika::setKokIssuerName(const Name & iKokIssuerName)
{
	mKokIssuerName = iKokIssuerName;
}

void DepoASNEklenecekKokSertifika::setKokSubjectName(const Name & iKokSubjectName)
{
	mKokSubjectName = iKokSubjectName;
}

void DepoASNEklenecekKokSertifika::setKokStartDate(const ETime & iKokStartDate)
{
	mKokStartDate = iKokStartDate;
}

void DepoASNEklenecekKokSertifika::setKokEndDate(const ETime & iKokEndDate)
{
	mKokEndDate = iKokEndDate;
}

void DepoASNEklenecekKokSertifika::setKokKeyUsage(const KeyUsage & iKokKeyUsage)
{
	mKokKeyUsage = iKokKeyUsage;
}

void DepoASNEklenecekKokSertifika::setKokSubjectKeyIdentifier(const SubjectKeyIdentifier & iKokSubjectKeyIdentifier)
{
	mKokSubjectKeyIdentifier = iKokSubjectKeyIdentifier;
}

void DepoASNEklenecekKokSertifika::setKokSertifikaTipi(const KS_Type & iKokSertifikaTipi)
{
	mKokSertifikaTipi = iKokSertifikaTipi;
}

void DepoASNEklenecekKokSertifika::setKokGuvenSeviyesi(const KGS_Type	& iKokGuvenSeviyesi) 
{
	mKokGuvenSeviyesi = iKokGuvenSeviyesi;
}

DepoASNEklenecekKokSertifika::~DepoASNEklenecekKokSertifika(void)
{
}
