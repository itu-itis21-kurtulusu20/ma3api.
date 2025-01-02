#include "KriptoUtils.h"

#include "Logger.h"
#include "AlgorithmList.h"

#include "AlgorithmIdentifier.h"

#include <QCryptographicHash>
#include <QList>

#import <Foundation/Foundation.h>
#import "CryptoFunctions.h"

namespace esya {

    KriptoUtils::KriptoUtils()
    {
    }

    QByteArray KriptoUtils::calculateDigest(const QByteArray &iPlainData, const QString & algorithm){

        Logger::log("data length: " + QString::number(iPlainData.length()) + " algo: " + algorithm);

        // MD5
        if(algorithm == AlgorithmList::DIGEST_MD5) {
            QCryptographicHash cryptoHash(QCryptographicHash::Md5);
            cryptoHash.addData(iPlainData);
            QByteArray hash = cryptoHash.result();
            return hash;
        }
        // SHA-1
        else if(algorithm == AlgorithmList::DIGEST_SHA1) {
            QCryptographicHash cryptoHash(QCryptographicHash::Sha1);
            cryptoHash.addData(iPlainData);
            QByteArray hash = cryptoHash.result();
            return hash;
        }
        // SHA-224
        else if(algorithm == AlgorithmList::DIGEST_SHA224) {
            QCryptographicHash cryptoHash(QCryptographicHash::Sha224);
            cryptoHash.addData(iPlainData);
            QByteArray hash = cryptoHash.result();
            return hash;
        }
        // SHA-256
        else if(algorithm == AlgorithmList::DIGEST_SHA256) {
            QCryptographicHash cryptoHash(QCryptographicHash::Sha256);
            cryptoHash.addData(iPlainData);
            QByteArray hash = cryptoHash.result();
            return hash;
        }
        // SHA-384
        else if(algorithm == AlgorithmList::DIGEST_SHA384) {
            QCryptographicHash cryptoHash(QCryptographicHash::Sha384);
            cryptoHash.addData(iPlainData);
            QByteArray hash = cryptoHash.result();
            return hash;
        }
        // SHA-512
        else if(algorithm == AlgorithmList::DIGEST_SHA512) {
            QCryptographicHash cryptoHash(QCryptographicHash::Sha512);
            cryptoHash.addData(iPlainData);
            QByteArray hash = cryptoHash.result();
            return hash;
        }
        // throw new Exception("");
        Logger::log("Error: Could not digest data: Unknown digest algorithm!");

    }

    bool  KriptoUtils::verifySignature(const ECertificate & iSignerCert, const QByteArray & iVerifyData,
                                       const QByteArray & iSignature, const AlgorithmIdentifier & iDigestAlgorithm )
    {
        QByteArray certBytes = iSignerCert.getEncodedBytes();
        NSData* cert = [[NSData alloc] initWithBytes:certBytes.data() length:certBytes.length()];

        QByteArray digestedData = calculateDigest(iVerifyData,"SHA-1");

        NSData* data = [[NSData alloc] initWithBytes:digestedData.data() length:digestedData.length()];

        NSData* signature = [[NSData alloc] initWithBytes:iSignature.data() length:iSignature.length()];

        bool result = [CryptoFunctions verifySignature:signature withCert:cert withData:data];

        return result;
    }
}
