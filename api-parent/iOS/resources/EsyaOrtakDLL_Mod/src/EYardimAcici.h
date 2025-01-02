#ifndef _E_YARDIM_ACICI_H_
#define _E_YARDIM_ACICI_H_
#include <QString>
#include <QStringList>
#include <QProcess>
class Q_DECL_EXPORT EYardimAcici
{
	QString mYardimDosyaYolu;
	QProcess * mpProcess;
public:
	enum YARDIM_MODUL_TIPI{YARDIM_MGM,YARDIM_GDM,YARDIM_EPOSTA,YARDIM_DEPO,YARDIM_HEPSI};
	void _yardimGoster(EYardimAcici::YARDIM_MODUL_TIPI iYardimTipi=EYardimAcici::YARDIM_HEPSI);
	void _dosyaGoruntule(const QString & iDosyaYolu="",const QStringList & iParametreList=QStringList());
	EYardimAcici(const QString & iYardimDosyaYolu);
	~EYardimAcici(void);
	static void yardimGoster(EYardimAcici::YARDIM_MODUL_TIPI iYardimTipi=EYardimAcici::YARDIM_HEPSI);
	void yardimGoruntule(EYardimAcici::YARDIM_MODUL_TIPI iYardimTipi=EYardimAcici::YARDIM_HEPSI);
};

#endif