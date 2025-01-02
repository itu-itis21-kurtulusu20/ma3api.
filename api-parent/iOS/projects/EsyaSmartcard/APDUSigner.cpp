#include "APDUSigner.h"

namespace esya
{
    APDUSigner::APDUSigner(const APDUSmartCard &sc, const QByteArray &cert,const QString& signinAlg)
        :mSc(sc), mCertificate(cert), mSigningAlg(signinAlg)
    {
    }
    QByteArray APDUSigner::sign(const QByteArray data){
       QByteArray signature = mSc.sign(data, mSigningAlg);
       return signature;
    }
}
