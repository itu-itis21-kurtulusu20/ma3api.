#ifndef KRIPTOUTILS_H
#define KRIPTOUTILS_H

#include "ECertificate.h"

#include <QByteArray>
#include "AlgorithmIdentifier.h"

namespace esya {

    class KriptoUtils
    {
    public:
        KriptoUtils();

        static QByteArray calculateDigest(const QByteArray& iPlainData, const QString & algorithm);

        static bool verifySignature(const ECertificate & iSignerCert, const QByteArray & iVerifyData,const QByteArray & iSignature,
                                    const AlgorithmIdentifier & iDigestAlgorithm = AlgorithmIdentifier(QByteArray(),ALGOS_sha_1) );
    };
}

#endif // KRIPTOUTILS_H
