/**
 * �stisnalar�m�z 
 * 
 * Copyright (c) 2005 by <Dindar �z/ MA3>
 */

#ifndef __EEXCEPTION__
#define __EEXCEPTION__


#include "esyaOrtak.h"
#include <QString>
#include <QTime>
#include <QFile>
#include <QIODevice>
#include <QStringList>
#include <QDebug>

#define ERROR_FILE_OPEN 1001

#define DEFAULT_FILE_PATH "c:\\temp\\EException.log"

#define LINE_START "==="
#define LINE_SEPERATOR "----------------------------------------------\n"
#define HEADER "[EException]\n"


namespace esya{

/**
 * \brief
 * Genel �stisna s�n�f�
s
 */
class Q_DECL_EXPORT EException
{
protected:
		int			mLineNumber;
		QString		mFileName;
		QStringList mStackTrace;
		QString		mErrorText;
		
public:

		EException(const  QString &iErrorDetail, const QString &iFileName = "",int iLineNumber = 0);
		EException(const  EException &exc);	
		
		int		getLineNumber(void)const;
		void	setLineNumber(int);

		QString	getFileName(void)const;
		void	setFileName(const QString &);

		QList<QString> getStackTrace() const;

		QString		getErrStr(void)const;
		EException  append(const QString &iErrorText,const QString & iFileName ="" , int iLineNumber = 0 );
		QString		printStackTrace(void)const ;
		void		clearStack(void);
		
		int writeLog();
		int writeLog(const  QString &);

		virtual QString toString(){ return mErrorText; };

public:
	
};


inline QDebug operator<<(QDebug debug, const EException& exc)
{
	debug.nospace() << "[ EXCEPTION ]" 
		<< exc.getErrStr() << "\n" 
		<< "Detaylar : \n" 
		<< exc.printStackTrace()<< "\n";
	return debug.space();
}


}






#endif
