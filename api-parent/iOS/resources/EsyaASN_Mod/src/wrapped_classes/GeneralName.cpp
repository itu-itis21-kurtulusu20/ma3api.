#include "GeneralName.h"
#include "NameUtils.h"

using namespace esya;

GeneralName::GeneralName(void)
{
}

GeneralName::GeneralName(const ASN1T_IMP_GeneralName & iGN)
{
	copyFromASNObject(iGN);
}

GeneralName::GeneralName(const GeneralName & iGN)
:	mType(iGN.getType()),
	mDirectoryName(iGN.getDirectoryName()),
	mIPAddress(iGN.getIPAddress()),
	mRFC822Name(iGN.getRFC822Name()),
	mURI(iGN.getURI()),
	mDNSName(iGN.getDNSName()),
	mAnotherName(iGN.getAnotherName()),
	mX400Address(iGN.getX400Address())
{
	
}

GeneralName::GeneralName(const  QByteArray &iGN)
{
	constructObject(iGN);
}

GeneralName& GeneralName::operator=(const GeneralName& iGN)
{
	mType = iGN.getType();
	mDirectoryName = iGN.getDirectoryName();
	mIPAddress = iGN.getIPAddress();
	mRFC822Name = iGN.getRFC822Name();
	mURI = iGN.getURI();
	mDNSName = iGN.getDNSName();
	mAnotherName = iGN.getAnotherName();
	mX400Address = iGN.getX400Address();
	return (*this) ;
}

bool esya::operator==(const GeneralName & iRHS ,const GeneralName & iLHS)
{
	if ( iRHS.getType() != iLHS.getType())
		return false;
	switch (iRHS.getType())
	{
		case GNT_DNSName		: return (iRHS.getDNSName() == iLHS.getDNSName());
		case GNT_RFC822Name		: return (iRHS.getRFC822Name() == iLHS.getRFC822Name());
		case GNT_URI			: return (iRHS.getURI() == iLHS.getURI());
		case GNT_DirectoryName	: return (iRHS.getDirectoryName() == iLHS.getDirectoryName());
		case GNT_IPAddress		: return (iRHS.getIPAddress() == iLHS.getIPAddress());
		case GNT_OtherName		: return (iRHS.getAnotherName() == iLHS.getAnotherName());
		case GNT_X400Address	: return (iRHS.getX400Address() == iLHS.getX400Address());
		default				: return false;

	}
}

bool esya::operator!=(const GeneralName & iRHS, const GeneralName & iLHS)
{
	return (!(iRHS == iLHS));
}

int GeneralName::copyFromASNObject(const ASN1T_IMP_GeneralName & iGN)
{
	mType = (GN_Type) iGN.t;
	switch (mType)
	{
		case GNT_DNSName:
			{
				mDNSName = QString(iGN.u.dNSName);
				break;
			}
		case GNT_RFC822Name:
			{
				mRFC822Name = QString(iGN.u.rfc822Name);
				break;
			}
		case GNT_URI:
			{
				mURI = QString(iGN.u.uniformResourceIdentifier);
				break;
			}
		case GNT_DirectoryName: 
			{
				mDirectoryName = Name(*iGN.u.directoryName);
				break;
			}
		case GNT_IPAddress:
			{
				mIPAddress = QByteArray((const char*)iGN.u.iPAddress->data,iGN.u.iPAddress->numocts);
				break;
			}
		case GNT_OtherName:
			{
				mAnotherName= AnotherName(*iGN.u.otherName) ;
				break;
			}
		case GNT_X400Address:
			{
				mX400Address= ORAddress(*iGN.u.x400Address) ;
				break;
			}
		default:
			{
				//throw EException(QString("Desteklenmeyen GeneralName Tipi : %1").arg(mType));
			} 
	}
	return SUCCESS;
}

int GeneralName::copyToASNObject(ASN1T_IMP_GeneralName & oGN) const
{
	oGN.t = mType;
	switch (mType)
	{
		case GNT_DNSName:
			{
				oGN.u.dNSName = myStrDup(mDNSName);
				break;
			}
		case GNT_RFC822Name:
			{
				oGN.u.rfc822Name = myStrDup(mRFC822Name);
				break;
			}
		case GNT_URI:
			{
				oGN.u.uniformResourceIdentifier = myStrDup(mURI);
				break;
			}
		case GNT_DirectoryName: 
			{
				oGN.u.directoryName = mDirectoryName.getASNCopy();
				break;
			}
		case GNT_IPAddress:
			{
				oGN.u.iPAddress = new ASN1TDynOctStr();
				oGN.u.iPAddress->data = (OSOCTET*)myStrDup( mIPAddress.data(),mIPAddress.size());
				oGN.u.iPAddress->numocts = mIPAddress.size();
				break;
			}
		case GNT_OtherName:
			{
				oGN.u.otherName = mAnotherName.getASNCopy();
				break;
			}
		case GNT_X400Address:
			{
				oGN.u.x400Address= mX400Address.getASNCopy();
				break;
			}
		default: {}
	}
	return SUCCESS;
}

void GeneralName::freeASNObject(ASN1T_IMP_GeneralName & oGN)const
{
	switch (oGN.t)
	{
		case GNT_DNSName:
			{
				DELETE_MEMORY_ARRAY(oGN.u.dNSName)
				break;
			}
		case GNT_RFC822Name:
			{
				DELETE_MEMORY_ARRAY(oGN.u.rfc822Name)
				break;
			}
		case GNT_URI:
			{
				DELETE_MEMORY_ARRAY(oGN.u.uniformResourceIdentifier)
				break;
			}
		case GNT_DirectoryName: 
			{
				Name().freeASNObjectPtr( oGN.u.directoryName);
				break;
			}
		case GNT_IPAddress:
			{
				DELETE_MEMORY_ARRAY(oGN.u.iPAddress->data)
				DELETE_MEMORY(oGN.u.iPAddress)
				break;
			}
		case GNT_OtherName:
			{
				AnotherName().freeASNObjectPtr(oGN.u.otherName);				
				break;
			}
		case GNT_X400Address:
			{
				ORAddress().freeASNObjectPtr(oGN.u.x400Address);				
				break;
			}
		default: {}
	}
}

int GeneralName::copyGeneralNames(const ASN1T_IMP_GeneralNames & iGNs, QList<GeneralName>& oList)
{
	return copyASNObjects<GeneralName>(iGNs,oList);
}

int GeneralName::copyGeneralNames(const QList<GeneralName> iList ,ASN1T_IMP_GeneralNames & oGNs)
{
	return copyASNObjects<GeneralName>(iList,oGNs);
}

int GeneralName::copyGeneralNames(const QByteArray & iASNBytes, QList<GeneralName>& oList)
{
	return copyASNObjects<ASN1T_IMP_GeneralNames,ASN1C_IMP_GeneralNames,GeneralName>(iASNBytes,oList);
}


const GN_Type& GeneralName::getType()const
{
	return mType;
}

const QString& GeneralName::getRFC822Name()const
{
	return mRFC822Name;	
}

const QString& GeneralName::getDNSName()const 
{
	return mDNSName;
}

const QString& GeneralName::getURI()const 
{
	return mURI;
}

const Name& GeneralName::getDirectoryName()const
{
	return mDirectoryName;
}

void GeneralName::setDirectoryName(const Name& iName )
{
	mType = GNT_DirectoryName;
	mDirectoryName = iName;
}

void GeneralName::setRFC822Name(const QString& iRFC822Name)
{
	mType = GNT_RFC822Name;
	mRFC822Name = iRFC822Name;
}

void GeneralName::setDNSName(const QString& iDNSName)
{
	mType = GNT_DNSName;
	mDNSName = iDNSName;
}

void GeneralName::setURI(const QString& iURI)
{
	mType = GNT_URI;
	mURI = iURI;
}

void GeneralName::setX400Address(const ORAddress& iOA)
{
	mType = GNT_X400Address;
	mX400Address = iOA;
}

const QByteArray& GeneralName::getIPAddress()const
{
	return mIPAddress;
}

const AnotherName& GeneralName::getAnotherName()const
{
	return mAnotherName;
}

const ORAddress& GeneralName::getX400Address()const
{
	return mX400Address;
}

QString GeneralName::toString()const
{
	switch(getType())
      {
		case GNT_DirectoryName: 
			return mDirectoryName.toString();
		case GNT_DNSName: 
				return QString(mDNSName);
		case GNT_RFC822Name: 
				return QString(mRFC822Name);
		case GNT_URI: 
				return QString(mURI);
		case GNT_IPAddress: 
		  {
				int i;
				QString r("");
				if( mIPAddress.size()== 0 )
					return r;
				r.append(mIPAddress[0]);
				for(i = 1 ; i < mIPAddress.size() ; i++)
	                r+=QString(".%1").arg(mIPAddress[i]);
				return r;
		  }
		case GNT_X400Address:
			{
				return mX400Address.toString();
			}
      default:
           return "??";
      }
}

bool GeneralName::hasMatch(const QList<GeneralName>&iList, const Name& iName )
{
	Q_FOREACH(GeneralName gn,iList)	
	{
		if (gn.getType()==GNT_DirectoryName && NameUtils::isEqual(gn.getDirectoryName(),iName))
			return true;
	}
	return false;
}

GeneralName::~GeneralName(void)
{
}
