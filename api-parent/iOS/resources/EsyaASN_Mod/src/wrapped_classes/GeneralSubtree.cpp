#include "GeneralSubtree.h"
#include "NameUtils.h"

using namespace esya;

GeneralSubtree::GeneralSubtree(void)
{
}

GeneralSubtree::GeneralSubtree(const GeneralSubtree &iGST)
:	mMaximumPresent(iGST.isMaximumPresent()),
	mMaximum(iGST.getMaximum()),
	mMinimum(iGST.getMinimum()),
	mBase(iGST.getBase())
{
}

GeneralSubtree::GeneralSubtree(const ASN1T_IMP_GeneralSubtree & iGST)
{
	copyFromASNObject(iGST);
}

GeneralSubtree::GeneralSubtree(const QByteArray & iGST)
{
	constructObject(iGST);
}


GeneralSubtree& GeneralSubtree::operator=(const GeneralSubtree & iGST)
{
	mBase			= iGST.getBase();
	mMinimum		= iGST.getMinimum();
	mMaximumPresent = iGST.isMaximumPresent();

	if (mMaximumPresent)
		mMaximum		= iGST.getMaximum();

	return (*this);
}

bool esya::operator==(const GeneralSubtree& iRHS, const GeneralSubtree& iLHS)
{
	if ( iRHS.getBase() != iLHS.getBase() ) 
		return false;
	if (iRHS.getMinimum() != iLHS.getMinimum())
		return false;
	if (iRHS.isMaximumPresent() != iLHS.isMaximumPresent() )
		return false;
	
	if (iRHS.isMaximumPresent() && ( iRHS.getMaximum() != iLHS.getMaximum() ) )
		return false;

	return true;
}

bool esya::operator!=(const GeneralSubtree& iRHS, const GeneralSubtree& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int GeneralSubtree::copyFromASNObject(const ASN1T_IMP_GeneralSubtree& iGST)
{
	mBase.copyFromASNObject(iGST.base);
	mMinimum		= iGST.minimum;
	mMaximumPresent = iGST.m.maximumPresent;

	if (mMaximumPresent)
		mMaximum		= iGST.maximum;

	return SUCCESS;	
}

int GeneralSubtree::copyToASNObject(ASN1T_IMP_GeneralSubtree & oGST) const
{
	mBase.copyToASNObject(oGST.base);
	oGST.minimum = mMinimum;
	oGST.m.maximumPresent = mMaximumPresent;

	if (mMaximumPresent)
		oGST.maximum = mMaximum;

	return SUCCESS;
}

void GeneralSubtree::freeASNObject(ASN1T_IMP_GeneralSubtree & oGST)const
{
	GeneralName().freeASNObject(oGST.base);
}

const bool	&GeneralSubtree::isMaximumPresent()const
{
	return mMaximumPresent;
}

const GeneralName	&GeneralSubtree::getBase()const
{
	return mBase;
}

GeneralName	&GeneralSubtree::getBase()
{
	return mBase;
}

const int &GeneralSubtree::getMinimum()const
{
	return mMinimum;
}

const int &GeneralSubtree::getMaximum()const
{
	return mMaximum;
}

int GeneralSubtree::copyGSTs(const ASN1T_IMP_GeneralSubtrees & iGSTs, QList<GeneralSubtree>& oList)
{
	return copyASNObjects<GeneralSubtree>(iGSTs,oList);
}

int GeneralSubtree::copyGSTs(const QList<GeneralSubtree> iList ,ASN1T_IMP_GeneralSubtrees & oGSTs)
{
	return copyASNObjects<GeneralSubtree>(iList,oGSTs);
}

GeneralSubtree::~GeneralSubtree(void)
{
}

bool GeneralSubtree::permits(const Name& iName)const
{
	if (mBase.getType() != GNT_DirectoryName)
		return true;

	return iName.isSubNameOf(mBase.getDirectoryName());

}
	
bool GeneralSubtree::permits(const GeneralName& iGN)const
{
	if (mBase.getType() != iGN.getType())
		return true;

	switch (mBase.getType())
	{
	case GNT_DirectoryName:
		{
			return permits(iGN.getDirectoryName());
			break;
		}
	case GNT_RFC822Name:
		{
			return NameUtils::isSubNameOf(iGN.getRFC822Name(),mBase.getRFC822Name(),"@");
		}
	case GNT_DNSName:
		{
			return  NameUtils::isSubNameOf(iGN.getDNSName(),mBase.getDNSName());
		}
	case GNT_URI:
		{
			return  NameUtils::isSubURINameOf(iGN.getURI(),mBase.getURI());
		}
	default:
		return true;
	}
}

bool GeneralSubtree::excludes(const Name& iName)const
{
	if (mBase.getType() != GNT_DirectoryName)
		return false;

	return iName.isSubNameOf(mBase.getDirectoryName());

}

bool GeneralSubtree::excludes(const GeneralName& iGN)const
{
	if (mBase.getType() != iGN.getType())
		return false;

	switch (mBase.getType())
	{
	case GNT_DirectoryName:
		{
			return excludes(iGN.getDirectoryName());
			break;
		}
	case GNT_RFC822Name:
		{
			return NameUtils::isSubNameOf(iGN.getRFC822Name(),mBase.getRFC822Name(),"@");
		}
	case GNT_DNSName:
		{
			return  NameUtils::isSubNameOf(iGN.getDNSName(),mBase.getDNSName());
		}
	case GNT_URI:
		{
			return  NameUtils::isSubURINameOf(iGN.getURI(),mBase.getURI());
		}
	default:
		return false;
	}
}


QList<GeneralSubtree> GeneralSubtree::intersect(const QList<GeneralSubtree>& iListA,const QList<GeneralSubtree>& iListB)
{
	QList<GeneralSubtree> intersection;
	Q_FOREACH(GeneralSubtree gstA,iListA)
	{
		bool found = false;
		Q_FOREACH(GeneralSubtree gstB,iListB)
		{
			if (gstA.getBase().getType() != gstB.getBase().getType())
				continue;

			found = true;
			switch (gstA.getBase().getType())
			{
			case GNT_DirectoryName:
				{
					if (gstA.getBase().getDirectoryName().isSubNameOf(gstB.getBase().getDirectoryName()))
						intersection.append(gstA);
					else if (gstB.getBase().getDirectoryName().isSubNameOf(gstA.getBase().getDirectoryName()))
						intersection.append(gstB);
					else 
					{
						GeneralSubtree emptyGSTName;
						emptyGSTName.getBase().setDirectoryName(Name());
						intersection.append(emptyGSTName);
					}
					break;
				}
			case GNT_RFC822Name:
				{
					if (NameUtils::isSubNameOf(gstA.getBase().getRFC822Name(),gstB.getBase().getRFC822Name(),"@"))
						intersection.append(gstA);
					else if  (NameUtils::isSubNameOf(gstB.getBase().getRFC822Name(),gstA.getBase().getRFC822Name(),"@"))
						intersection.append(gstB);
					else
					{
						GeneralSubtree emptyGSTName;
						emptyGSTName.getBase().setRFC822Name(QString());
						intersection.append(emptyGSTName);
					}
					break;
				}
			case GNT_DNSName:
				{
					if (NameUtils::isSubNameOf(gstA.getBase().getDNSName(),gstB.getBase().getDNSName()))
						intersection.append(gstA);
					else if  (NameUtils::isSubNameOf(gstB.getBase().getDNSName(),gstA.getBase().getDNSName()))
						intersection.append(gstB);
					else
					{
						GeneralSubtree emptyGSTDNSName;
						emptyGSTDNSName.getBase().setDNSName(QString());
						intersection.append(emptyGSTDNSName);
					}
					break;
				}
			case GNT_URI:
				{
					if (NameUtils::isSubURINameOf(gstA.getBase().getURI(),gstB.getBase().getURI()))
						intersection.append(gstA);
					else if  (NameUtils::isSubURINameOf(gstB.getBase().getURI(),gstA.getBase().getURI()))
						intersection.append(gstB);
					else
					{
						GeneralSubtree emptyGSTURI;
						emptyGSTURI.getBase().setURI(QString());
						intersection.append(emptyGSTURI);
					}
					break;
				}
			default:
				continue ;
			}
		}
		if (!found) 
			intersection.append(gstA);
	}

	Q_FOREACH(GeneralSubtree gstB,iListB)
	{
		bool found = false;
		Q_FOREACH(GeneralSubtree gstA,iListA)
		{
			if (gstA.getBase().getType() != gstB.getBase().getType())
				continue;
			found = true;
		}
		if (!found)
			intersection.append(gstB);
	}

	return intersection;
}

QList<GeneralSubtree> GeneralSubtree::unite(const QList<GeneralSubtree>& iListA,const QList<GeneralSubtree>& iListB)
{
	QList<GeneralSubtree> united;
	Q_FOREACH(GeneralSubtree gstA,iListA)
	{
		bool found = false;
		Q_FOREACH(GeneralSubtree gstB,iListB)
		{
			if (gstA.getBase().getType() != gstB.getBase().getType())
				continue;

			found = true;
			switch (gstA.getBase().getType())
			{
			case GNT_DirectoryName:
				{
					if (gstA.getBase().getDirectoryName().isSubNameOf(gstB.getBase().getDirectoryName()))
						united.append(gstB);
					else if (gstB.getBase().getDirectoryName().isSubNameOf(gstA.getBase().getDirectoryName()))
						united.append(gstA);
					else 
					{
						united.append(gstB);
						united.append(gstA);
					}
					break;
				}
			case GNT_RFC822Name:
				{
					if (NameUtils::isSubNameOf(gstA.getBase().getRFC822Name(),gstB.getBase().getRFC822Name(),"@"))
						united.append(gstA);
					else if  (NameUtils::isSubNameOf(gstB.getBase().getRFC822Name(),gstA.getBase().getRFC822Name(),"@"))
						united.append(gstB);
					else
					{
						united.append(gstB);
						united.append(gstA);
					}
					break;
				}
			case GNT_DNSName:
				{
					if (NameUtils::isSubNameOf(gstA.getBase().getDNSName(),gstB.getBase().getDNSName()))
						united.append(gstB);
					else if  (NameUtils::isSubNameOf(gstB.getBase().getDNSName(),gstA.getBase().getDNSName()))
						united.append(gstA);
					else
					{
						united.append(gstB);
						united.append(gstA);
					}
					break;
				}
			case GNT_URI:
				{
					if (NameUtils::isSubURINameOf(gstA.getBase().getURI(),gstB.getBase().getURI()))
						united.append(gstB);
					else if  (NameUtils::isSubURINameOf(gstB.getBase().getURI(),gstA.getBase().getURI()))
						united.append(gstA);
					else
					{
						united.append(gstB);
						united.append(gstA);
					}
					break;
				}
			default:
				continue ;
			}
		}
		if (!found) 
			united.append(gstA);
	}

	Q_FOREACH(GeneralSubtree gstB,iListB)
	{
		bool found = false;
		Q_FOREACH(GeneralSubtree gstA,iListA)
		{
			if (gstA.getBase().getType() != gstB.getBase().getType())
				continue;
			found = true;
		}
		if (!found)
			united.append(gstB);
	}

	return united;
}