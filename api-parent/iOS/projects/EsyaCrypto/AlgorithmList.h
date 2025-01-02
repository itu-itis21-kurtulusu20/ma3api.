#ifndef ALGORITHMLIST_H
#define ALGORITHMLIST_H

#include <QString>

namespace esya {

    class AlgorithmList {
    public:
        static const QString DIGEST_MD5;
        static const QString DIGEST_SHA1;
        static const QString DIGEST_SHA224;
        static const QString DIGEST_SHA256;
        static const QString DIGEST_SHA384;
        static const QString DIGEST_SHA512;

        static const QString SIGNATURE_RSA;
        static const QString SIGNATURE_RSA_RAW;
        static const QString SIGNATURE_RSA_MD5;
        static const QString SIGNATURE_RSA_SHA1;
        static const QString SIGNATURE_RSA_SHA224;
        static const QString SIGNATURE_RSA_SHA256;
        static const QString SIGNATURE_RSA_SHA384;
        static const QString SIGNATURE_RSA_SHA512;

        static QString getDigestAlgOfSignatureAlg(QString signatureAlg);
    };

     const QString AlgorithmList::DIGEST_MD5 = "MD5";
     const QString AlgorithmList::DIGEST_SHA1 = "SHA-1";
     const QString AlgorithmList::DIGEST_SHA224 = "SHA-224";
     const QString AlgorithmList::DIGEST_SHA256 = "SHA-256";
     const QString AlgorithmList::DIGEST_SHA384 = "SHA-384";
     const QString AlgorithmList::DIGEST_SHA512 = "SHA-512";

     const QString AlgorithmList::SIGNATURE_RSA = "RSA-with-NONE";
     const QString AlgorithmList::SIGNATURE_RSA_RAW = "RSA-RAW";
     const QString AlgorithmList::SIGNATURE_RSA_MD5 = "RSA-with-MD5";
     const QString AlgorithmList::SIGNATURE_RSA_SHA1 = "RSA-with-SHA1";
     const QString AlgorithmList::SIGNATURE_RSA_SHA224 = "RSA-with-SHA224";
     const QString AlgorithmList::SIGNATURE_RSA_SHA256 = "RSA-with-SHA256";
     const QString AlgorithmList::SIGNATURE_RSA_SHA384 = "RSA-with-SHA382";
     const QString AlgorithmList::SIGNATURE_RSA_SHA512 = "RSA-with-SHA512";

}

#endif // ALGORITHMS_H
