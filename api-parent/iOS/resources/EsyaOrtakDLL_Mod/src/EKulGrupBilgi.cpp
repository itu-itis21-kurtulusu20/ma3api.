#include "EKulGrupBilgi.h"

EKulGrupBilgi::EKulGrupBilgi(void)
:mIsNull(true)
{
}

EKulGrupBilgi::EKulGrupBilgi(const QString &iAd,const QString & iMail,const QString &iTKA,bool iIsGrup/* =false */)
:mAd(iAd),mMail(iMail),mTKA(iTKA),mIsGroup(iIsGrup),mIsNull(false)
{
}

EKulGrupBilgi::~EKulGrupBilgi(void)
{
}
