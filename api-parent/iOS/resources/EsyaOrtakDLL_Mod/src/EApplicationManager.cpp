#include "EApplicationManager.h"
#include "FileUtil.h"
#include <QApplication>
#include "OrtakDil.h"
#include <QMessageBox>
#include <QAbstractButton>
#include "EDilBelirleyiciFactory.h"
#include "QTextCodec"

#define KERMEN_STYLE_FILE_NAME "KermenStyle.qss"

#define Q_DEBUG_LOG_FUNC_GIRIS qDebug(qPrintable(QString(__EFUNC__)+" GIRIS"));
#define Q_DEBUG_LOG_FUNC_TAMAMLANDI qDebug(qPrintable(QString(__EFUNC__)+" TAMAMLANDI"));
#define Q_DEBUG_LOG_FUNC_HATA(x) qDebug(qPrintable(QString(__EFUNC__)+QString("HATA :")+QString(x)));

NAMESPACE_BEGIN(esya)

QApplication * EApplicationManager::sGenelAppManager = NULL ;
void EApplicationManager::qtLibraryListesineSystem32At()
{	
	Q_DEBUG_LOG_FUNC_GIRIS
	#ifdef WIN32
		QString lSystemPath = FileUtil::getWinSystem32Path();
		if (!lSystemPath.isEmpty())
		{		
			qApp->addLibraryPath(lSystemPath);			
		}			
	#endif	
	Q_DEBUG_LOG_FUNC_TAMAMLANDI
}

void EApplicationManager::hataMesajGoster(const QString & iHataMesaji)
{
	Q_DEBUG_LOG_FUNC_GIRIS
	QMessageBox lMesaj(QMessageBox::Critical,DIL_GNL_HATA,iHataMesaji,QMessageBox::Ok);	
	QAbstractButton * okButton = lMesaj.button(QMessageBox::Ok);
	okButton->setText(DIL_BTN_TAMAM);
	lMesaj.exec();
	Q_DEBUG_LOG_FUNC_TAMAMLANDI
}

void EApplicationManager::qtAppKontrolEtYoksaBaslat()
{
	if(!qApp)
	{
		EApplicationManager::qtApplicationBaslat();
		EApplicationManager::calismaDiliBelirle();
	}
}

QApplication * EApplicationManager::qtApplicationBaslat()
{	
	Q_DEBUG_LOG_FUNC_GIRIS
	int arg=0;
	char * arv="";
	EApplicationManager::sGenelAppManager  = new QApplication(arg,&arv);		
	EApplicationManager::qtLibraryListesineSystem32At();								
	EApplicationManager::textCodecBelirle();
	EApplicationManager::styleBelirle();	
	//EApplicationManager::calismaDiliBelirle();
	Q_DEBUG_LOG_FUNC_TAMAMLANDI
	return qApp;
	//ESYA_ORTAK_FUNC_BEGIN
}

void EApplicationManager::calismaDiliBelirle()
{
	Q_DEBUG_LOG_FUNC_GIRIS
	if (qApp)
	{
		KERMEN_CALISMA_DILI_BELIRLE(*qApp)
	}	
	Q_DEBUG_LOG_FUNC_TAMAMLANDI
}

void EApplicationManager::textCodecBelirle()
{
	Q_DEBUG_LOG_FUNC_GIRIS
	QTextCodec * codec = QTextCodec::codecForName("UTF-8");
	QTextCodec::setCodecForTr(codec);
	Q_DEBUG_LOG_FUNC_TAMAMLANDI	
}

void EApplicationManager::styleBelirle()
{
	Q_DEBUG_LOG_FUNC_GIRIS;
	QString styleFilePath;
	if (QFile::exists(KERMEN_STYLE_FILE_NAME))
	{
		styleFilePath = KERMEN_STYLE_FILE_NAME;
	}
	else
	{
		#ifdef WIN32
				styleFilePath = FileUtil::getWinSystem32Path()+"/"+KERMEN_STYLE_FILE_NAME;
		#else
				styleFilePath = QString("%1/%2").arg("/usr/lib").arg(KERMEN_STYLE_FILE_NAME);
		#endif
	}
	
	QFile fileStyleFile(styleFilePath);
	if (!fileStyleFile.open(QIODevice::ReadOnly))
	{
		Q_DEBUG_LOG_FUNC_HATA("Belirtilen style dosyasý acýlamadý => Dosya Adi = "+styleFilePath)
		return;
	}	
	qApp->setStyleSheet( fileStyleFile.readAll() );
	fileStyleFile.close();		
	Q_DEBUG_LOG_FUNC_TAMAMLANDI
}

EApplicationManager::EApplicationManager(void)
{
	Q_DEBUG_LOG_FUNC_GIRIS
	Q_DEBUG_LOG_FUNC_TAMAMLANDI
}

EApplicationManager::~EApplicationManager(void)
{
	Q_DEBUG_LOG_FUNC_GIRIS
	Q_DEBUG_LOG_FUNC_TAMAMLANDI
}


void EApplicationManager::qtApplicationKontroluYap()
{
	if (!qApp)
	{
		EApplicationManager::qtApplicationBaslat();	
	}	
	EApplicationManager::qtLibraryListesineSystem32At();
	EApplicationManager::styleBelirle();
}
NAMESPACE_END
