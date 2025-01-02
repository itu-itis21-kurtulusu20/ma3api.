#ifndef APDUSIGNER_H
#define APDUSIGNER_H

#include "BaseSigner.h"
#include "APDUSmartCard.h"
#include <QString>

namespace esya
{
    class APDUSigner : public BaseSigner
    {
    public:
        APDUSigner(const APDUSmartCard &sc, const QByteArray &cert, const QString& signinAlg);
        ~APDUSigner(){};
         virtual QByteArray sign(const QByteArray data);
    private:
         APDUSmartCard mSc;
         QByteArray mCertificate;
         QString mSigningAlg;
    };
}
#endif // APDUSIGNER_H
