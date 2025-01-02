#include "CMSSignature.h"
#include "Logger.h"
#include "EXMLLisansKontrolcu.h"

namespace  esya {

    CMSSignature::CMSSignature(SignatureContainer *container, const SignedData &signedData, const ECertificate & certificate)
        :mpContainer(container), mSignedData(signedData), mSignersCertificate(certificate)
    {

    }

    void CMSSignature::addContent(Signable *data, bool includeContent)
    {
        if(!includeContent)
             mSignedData.setEncapContenInfo(EncapContentInfo(CMS_id_data));
        else
            mSignedData.setEncapContenInfo(EncapContentInfo(data->getContent(),CMS_id_data));
    }

    Signature * CMSSignature::createCounterSignature(const ECertificate &certificate){

    }

    QList<Signature*> CMSSignature::getCounterSignatures(){

    }

    bool CMSSignature::sign(BaseSigner *signer)
    {
        //lisans
//        try {
            EXMLLisansKontrolcu::getInstance()->gecerlilikKontroluYap();
            const QList< QPair< ASN1TObjId, QByteArray > > & iAdditionalUnSignedAttributes = QList< QPair< ASN1TObjId, QByteArray > >();
            SignerParam signerParam(SignedData::DEFAULT_VERSION, mSignersCertificate,
                                    SignedData::DEFAULT_DIGEST_ALGORITHM,
                                    iAdditionalUnSignedAttributes,signer);
            SignParam sp(false,QList< SignerParam >());
            sp.addSignerParam(signerParam);
            sp.setAyrikImza(false);
            QByteArray data = mSignedData.getEncapContentInfo().getEContent();
            Logger::log("CMSSignature::sign - addParallelBBESSigners");
            mSignedData.addParallelBESSigners(sp, data);
            return true;
//        }
//        catch(CMSException e) {
//            throw CMSException();
//            //return false;
//        }

    }

    QByteArray CMSSignature::getSignature()
    {
        ContentInfo contentInfo(mSignedData.getEncodedBytes(),CMS_id_signedData);
        return contentInfo.getEncodedBytes();
    }

}
