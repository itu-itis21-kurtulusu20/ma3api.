#ifndef _E_KUL_GRUP_BILGI_H___
#define _E_KUL_GRUP_BILGI_H___

#include <QString>

class Q_DECL_EXPORT EKulGrupBilgi
{
	bool mIsNull;
	QString mAd;
	QString mMail;
	QString mTKA;
	bool mIsGroup;
public:
	EKulGrupBilgi(void);
	EKulGrupBilgi(const QString &iAd,const QString & iMail,const QString &iTKA,bool iIsGrup=false);
	
	const QString & getAd() const {return mAd;};
	const QString & getMail() const {return mMail;};
	const QString & getTKA() const {return mTKA;};
	bool getIsNULL() const{return mIsNull;};
	bool getIsGroup() const{return mIsGroup;};

	void setAd(const QString & iAd){mAd=iAd;};
	void setMail(const QString & iMail){mMail=iMail;};
	void setTKA(const QString & iTKA){mTKA=iTKA;};
	void setIsGroup(bool iIsGroup){mIsGroup=iIsGroup;};
	~EKulGrupBilgi(void);

};
#endif

