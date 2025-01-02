#include "EException.h"
#include <QTextStream>
#include <QFileInfo>


using namespace esya;

/**
 * \brief
 * EException için constructor.
 * 
 * \param iErrorDetail
 * Hata Mesajý.
 * 
 * \param iFileName
 * Ýstisnanýn oluþtuðu kaynak dosyasý adý.
 * 
 * \param iLineNumber
 * Ýstisnanýn oluþtuðu satýr numarasý.
 * 
 */
EException::EException(const  QString &iErrorDetail, const QString &iFileName,int iLineNumber)
: mLineNumber(iLineNumber), mFileName(iFileName), mErrorText(iErrorDetail)
{
	append(toString(),iFileName,iLineNumber);
}

/**
 * \brief
 * EException için copy constructor.
 * 
 * \param exc
 * Kopayalanacak istisna.
 * 
 * 
 */
EException::EException(const  EException &exc)
: mLineNumber(exc.getLineNumber()), mFileName(exc.getFileName())
{
	mStackTrace = exc.getStackTrace();
}



/**
 * \brief
 * Ýstisnanýn mesaj listesini döner.
 * 
 * \returns
 * Ýstisnanýn mesaj listesini döner.
 * 
 */
QList<QString> EException::getStackTrace() const
{
	return mStackTrace;
}

/**
 * \brief
 * Ýstisnanýn oluþtuðu satýr numarasýný döner.
 * 
 * \returns
 * Ýstisnanýn oluþtuðu satýr numarasýný döner.
 * 
 */
int EException::getLineNumber()const 
{
	return mLineNumber;
}


/**
 * \brief
 * Ýstisnanýn oluþtuðu satýr numarasýný belirler.
 * 
 * \param iLineNumber
 *  Ýstisnanýn oluþtuðu satýr numarasý.
 * 
 * 
 * 
 */
void EException::setLineNumber(int iLineNumber)
{
	mLineNumber = iLineNumber;
}

/**
 * \brief
 * mesaj listesini temizler.
 * 
 */
void EException::clearStack(void)
{
	mStackTrace.clear();
}

/**
 * \brief
 * Ýstisnaya son eklenen hata mesajýný döner
 * 
 * \returns
 * Ýstisnaya son eklenen hata mesajý.
 * 
 */
QString EException::getErrStr(void) const 
{
	return mStackTrace.last();
}

/**
 * \brief
 * Ýstisnanýn oluþtuðu kaynak dosyasý adýný döner
 * 
 * \returns
 * Ýstisnanýn oluþtuðu kaynak dosyasý adý.
 */
QString EException::getFileName(void) const 
{
	return mFileName;
}

/**
 * \brief
 * Ýstisnanýn oluþtuðu kaynak dosyasý adýný belirler.
 * 
 * \param 
 * Ýstisnanýn oluþtuðu kaynak dosyasý adý .
 * 
 */
void EException::setFileName(const QString & iFileName)
{
	mFileName = iFileName;
}


/**
 * \brief
 * Mesaj listesinin sonuna yeni bir hata mesajý ekler.
 * 
 * \param iErrorText
 * Ekelenecek hata mesajý.
 * 
 * \param iFileName
 * mesajýn  eklendiði kaynak dosyasýnýn adý.
 * 
 * \param iLineNumber
 * mesajýn  eklendiði satýr numarasýnýn adý.
 * 
 * \returns
 * Mesajýn eklenmiþ olduðu istisna objesini döner.
 * 
 */
EException EException::append(const QString &iErrorText,const QString & iFileName , int iLineNumber )
{
	if (!iFileName.isEmpty() && QFile::exists(iFileName))
	{
		mStackTrace.append(QString(iErrorText+QString(" %1 (%2) ").arg(QFileInfo(iFileName).fileName()).arg(iLineNumber)));
	}
	else mStackTrace.append(iErrorText);
	return (*this);
}

/**
 * \brief
 * Mesaj listesindeki bütün mesajlar alt alta listeler.
 * 
 * \returns
 * Mesaj listesindeki mesajlarýn alt alta listelenmiþ þekli.
 * 
 */
QString EException::printStackTrace()const
{
	QString errText;
	int indent = 0 , size = mStackTrace.size() ;
	QString stIndent;
	for (int i = 0 ; i < size ; i++)
	{
		stIndent.fill(QChar(' '), indent);
		errText += (stIndent+LINE_START + mStackTrace[size-i-1] + "\n");
		indent += QString(LINE_START).length();
	}
	errText = QString("%1%2%3%4").arg(HEADER).arg(LINE_SEPERATOR).arg(errText).arg(LINE_SEPERATOR);
	return errText;
}


/**
 * \brief
 * Mesaj listesini varsayýlan bir log dosyasýna yazar.
 * 
 */
int EException::writeLog()
{				
	return writeLog(DEFAULT_FILE_PATH);
}


/**
 * \brief
 * Mesaj listesini verilen log dosyasýna yazar.
 * 
 * \param logFilePath
 * Mesaj listesinin yazýlacaðý log dosyasý adresi
 */
int EException::writeLog(const QString &logFilePath)
{

		QString	data;
		QString	caption = "<< EException  >> " ;  
		data	=  caption + QTime::currentTime().toString("hh:mm:ss.zzz   :   ");
		data	+= printStackTrace();
		data	+= LINE_SEPERATOR;


		QFile logFile(logFilePath);

		if(!logFile.open(QFile::Append | QIODevice::Text)) 
		{
			return ERROR_FILE_OPEN;
		}

		QTextStream out(&logFile);

		out<<data<<endl;

		logFile.close();

		return 0;
}




