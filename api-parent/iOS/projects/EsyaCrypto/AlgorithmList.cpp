#include "AlgorithmList.h"

#include "Logger.h"
#include "CryptoException.h"

namespace esya
{
    QString AlgorithmList::getDigestAlgOfSignatureAlg(QString signatureAlg)
    {
        if(signatureAlg.contains("MD5"))
            return AlgorithmList::DIGEST_MD5;
        if(signatureAlg.contains("SHA1"))
            return AlgorithmList::DIGEST_SHA1;
        if(signatureAlg.contains("SHA224"))
            return AlgorithmList::DIGEST_SHA224;
        if(signatureAlg.contains("SHA256"))
            return AlgorithmList::DIGEST_SHA256;
        if(signatureAlg.contains("SHA384"))
            return AlgorithmList::DIGEST_SHA384;
        if(signatureAlg.contains("SHA512"))
            return AlgorithmList::DIGEST_SHA512;

        Logger::log("Error: Could not find correct digest algorithm from signing algorithm!");
        throw new CryptoException("Could not find correct digest algorithm from signing algorithm " + signatureAlg);
    }

}
