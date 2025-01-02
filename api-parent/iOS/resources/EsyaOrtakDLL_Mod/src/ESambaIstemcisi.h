/*
 * ESambaIstemcisi.h
 *
 *  Created on: 03.Şub.2009
 *      Author: dindaro
 */





#ifndef ESAMBAISTEMCISI_H_
#define ESAMBAISTEMCISI_H_

#ifndef WIN32

#include "smbmm.h"
#include <QList>
#include <QString>


#define ATTR_ALL 	"system.nt_sec_desc.*+"
#define ATTR_OWNER 	"system.nt_sec_desc.owner+"

class  Q_DECL_EXPORT ESambaIstemcisi {
	
	SMBContext mContext;

public:
	ESambaIstemcisi();
	virtual ~ESambaIstemcisi();

	static QString   		 parseOwner(const QString &);
	static QList<QString>  parseACL(const QString &);

	QString 		ownerAl( const QString & iSambaURL );
	QList<QString> 	okuyanlariAl( const QString & iSambaURL );

};

#endif 

#endif /* ESAMBAISTEMCISI_H_ */