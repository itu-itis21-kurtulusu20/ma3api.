#ifndef _E_SURE_H_
#define _E_SURE_H_

#include <QDateTime>
#include <QObject>

class Q_DECL_EXPORT ESure
{	
	int mGun;
	int mSaat;
	int mDakika;
	int mSaniye;
	int mMiliSaniye;
public:
	ESure();
	ESure(int iMiliSaniye);
	ESure(int iSaat,int iDakika,int iSaniye,int iMiliSaniye);
	~ESure(void);
	ESure(const ESure & other);
	bool isEmpty();

	void miliSaniyeEkle(int iMiliSaniye);
	void saniyeEkle(int iSaniye);
	void dakikaEkle(int iDakika);
	void saatEkle(int iSaat);
	void gunEkle(int iGun){mGun+=iGun;}

	void setSaat(int iSaat);
	void setDakika(int iDakika);
	void setSaniye(int iSaniye);
	void setMiliSaniye(int iMiliSaniye);
	void setGun(int iGun){mGun=iGun;};
	
	int getGun(){return mGun;};
	int getSaat(){return mSaat;};
	int getDakika(){return mDakika;};
	int getSaniye(){return mSaniye;};
	int getMiliSaniye(){return mMiliSaniye;};

	QString toString(bool iMilisaniyeEkle=true) const;	

	static ESure gecenSureVer(const QDateTime & iBaslangic,const QDateTime & iBitis);
	static ESure gecenSureVerMSec(const QDateTime & iBaslangic,const QDateTime & iBitis);
};
#endif