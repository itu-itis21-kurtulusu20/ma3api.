#include "RSAScheme.h"

#include <QString>
#include <QCryptographicHash>
#include "AlgorithmList.h"
#include "KriptoUtils.h"
#include "Logger.h"

namespace esya {

    RSAScheme::RSAScheme()
    {
    }

    RSAScheme::RSAScheme(QString signingAlg)
    {
        mSignatureAlg = signingAlg;
        //mMechanisms = mechanisms;
    }

    void RSAScheme::init() {

    }

    // get mechanisms to be implemented

    QByteArray RSAScheme::getSignatureInput(QByteArray toBeSigned)
    {
        // TODO mechanism check

        // get prefix according to the digest algorithm
        Logger::log(mSignatureAlg);
        QString digestAlg = AlgorithmList::getDigestAlgOfSignatureAlg(mSignatureAlg);
        Logger::log(digestAlg);
        QByteArray result = getPrefixForDigestAlg(digestAlg);

        // get digest of data to be signed (signed attributes)
        Logger::log("RSAScheme::getSignatureInput - before hash calculating");
        QByteArray digest = KriptoUtils::calculateDigest(toBeSigned,digestAlg);

        // append digest of data to be signed (signed attributes) to prefix
        Logger::log("RSAScheme::getSignatureInput - before appending");
        result.append(digest);

        return result;
    }

    QByteArray RSAScheme::getPrefixForDigestAlg(QString digestAlg)
    {
        if(digestAlg == AlgorithmList::DIGEST_SHA1)
            //return QByteArray::fromRawData((char*)esya::sha1Prefix,15);
            return QByteArray((char*)esya::sha1Prefix,15);
        if(digestAlg == AlgorithmList::DIGEST_SHA256)
            //return QByteArray::fromRawData((char*)esya::sha256Prefix,19);
            return QByteArray((char*)esya::sha256Prefix,19);
        if(digestAlg == AlgorithmList::DIGEST_SHA384)
            //return QByteArray::fromRawData((char*)esya::sha384Prefix,19);
            return QByteArray((char*)esya::sha384Prefix,19);
        if(digestAlg == AlgorithmList::DIGEST_SHA512)
            //return QByteArray::fromRawData((char*)esya::sha512Prefix,19);
            return QByteArray((char*)esya::sha512Prefix,19);

    }

}


