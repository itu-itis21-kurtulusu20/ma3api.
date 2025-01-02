#ifndef WIN32
/*
 * ESambaIstemcisi.cpp
 *
 *  Created on: 03.Şub.2009
 *      Author: dindaro
 */

#include "ESambaIstemcisi.h"
#include "ELogger.h"

using namespace esya;

ESambaIstemcisi::ESambaIstemcisi() {
	// TODO Auto-generated constructor stub

}

ESambaIstemcisi::~ESambaIstemcisi() {
	// TODO Auto-generated destructor stub
}

QString ESambaIstemcisi::parseOwner(const QString & iSambaResult)
{
	if (iSambaResult.isEmpty() ) return QString();

	int a =-1;

	if ( (a = iSambaResult.indexOf("+"))>= 0 )
		return iSambaResult.right(a);
	return iSambaResult;

}

QList<QString> ESambaIstemcisi::parseACL(const QString & iSambaResult)
{
	if (iSambaResult.isEmpty()) return QList<QString>();

	QString tmp = iSambaResult;
	QList<QString> acls;
	int b,a = tmp.indexOf("ACL:");
	while (a >= 0)
	{
		tmp = tmp.mid(a+4);
		b = tmp.indexOf(",");
		if (b>=0)
		{
			acls.append(tmp.left(b));
			tmp = tmp.mid(b);
		}
		else
		{
			acls.append(tmp);
			tmp = "";
		}
		a = tmp.indexOf("ACL:");
	}
	return acls;
}



QString ESambaIstemcisi::ownerAl( const QString & iSambaURL )
{
	DEBUGLOGYAZ("MGM",QString("ownerAl(): BASLADI iSambaURL: %1").arg(iSambaURL))
	char buffer[10000];
	int size= 0;

	//iSambaURL.toStdString()
	size = mContext.GetAttr(iSambaURL.toStdString(),ATTR_OWNER,buffer,size);

	int a= size;

	if (a < 0)
	{
		ERRORLOGYAZ("MGM",QString("ownerAl(): owner bilgisi alinamadi iSambaURL: %1").arg(iSambaURL))
		return QString("");
	}

	//"smb://oz/smb/smbmm.h" deneme url
	a = mContext.GetAttr(iSambaURL.toStdString(),ATTR_OWNER,buffer,size+1);

	DEBUGLOGYAZ("MGM",QString(" buffer : %1 size : %2 buffer+= %3" ).arg(QString(buffer)).arg(size).arg(QString(QByteArray(buffer,size))));
	QString smbResult(buffer);

	if (!smbResult.isEmpty() )
	{
		DEBUGLOGYAZ("MGM",QString("ownerAl(): BASLADI iSambaURL: %1 smbResult: %2").arg(iSambaURL).arg(smbResult));

		if ( (a = smbResult.indexOf("+"))>= 0 )
			return smbResult.mid(a+1);
		return smbResult;
	}

	return "";
}

QList<QString> ESambaIstemcisi::okuyanlariAl( const QString & iSambaURL )
{
	DEBUGLOGYAZ("MGM",QString("okuyanlariAl(): BASLADI iSambaURL: %1").arg(iSambaURL));

	char buffer[10000];
	int size= 0;

	size = mContext.GetAttr(iSambaURL.toLocal8Bit().data(),ATTR_ALL,buffer,size);

	int a= size;
	if (a < 0)
	{
		ERRORLOGYAZ("MGM",QString("okuyanlariAl(): owner bilgisi alinamadi iSambaURL: %1").arg(iSambaURL))
			return QList<QString>();
	}

	a = mContext.GetAttr(iSambaURL.toLocal8Bit().data(),ATTR_ALL,buffer,size);

	QString sambaResult(buffer);

	QList<QString> okuyanlar,acls = parseACL(sambaResult);

	Q_FOREACH(QString acl,acls)
	{
		a = acl.indexOf(":");
		acl = acl.left(a);
		okuyanlar<<acl.mid(acl.indexOf("+")+1);
	}

	DEBUGLOGYAZ("MGM",QString("okuyanlariAl(): BITTI iSambaURL: %1 okuyanSayisi: %2").arg(okuyanlar.size()));
	return okuyanlar;
}


#endif