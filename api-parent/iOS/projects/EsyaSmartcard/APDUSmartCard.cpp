#include "APDUSmartCard.h"
#include "CIFFactory.h"
#include "APDUSigner.h"
#include "Logger.h"
#include "SignatureSchemeFactory.h"
#include "SignatureScheme.h"
#include "SmartCardException.h"

#include <iostream>
#include <unistd.h>


namespace esya
{
    APDUSmartCard::APDUSmartCard()
    {

    }
    APDUSmartCard::~APDUSmartCard()
    {
    }
    QList<QByteArray> APDUSmartCard::getCertificate()
    {
        Logger::log("APDUSmartCard::getCertificate - start");

        try {

            QList<QByteArray> certList;// = new QList<QByteArray>();

            //
            Logger::log("About to create mpCommands with transmitter");
            mpCommands = CIFFactory::getInstance()->getAkisCommand(transmitter/*pcsc*/);
            Logger::log("mpCommands successfully created.");

            Logger::log("About to selectMF");
            mpCommands->selectMF();
            Logger::log("selectMF successful");

            Logger::log("About to selectDFByName(PKCS-15)");
            mpCommands->selectDFByName("PKCS-15");
            Logger::log("selectMF and selectDFByName(PKCS-15) passed");
            //

            unsigned char *certs[10];

            unsigned char EF1[2] = {0x2f,0x0F};

            // int i = 0; // loop mechanism introduced
            for( int i = 0; i < 10; i++ ) {

                EF1[1]++;

                Logger::log(QString::number(i));
                try {
                    certs[i] = mpCommands->readFileBySelectingUnderActiveDF(EF1);
                    Logger::log("Next statement in try");

                    Logger::log("readFileBySelectingUnderActiveDF done for above number, 0 means 2F 10");
                    // //
                    usleep(100000);
                    // //

                    if (certs[i] != NULL) {

                        Logger::log("before file len @APDUSmartCard");
                        int fileLen = mpCommands->getLastFCI()->getFileLen();
                        Logger::log("after file len @APDUSmartCard");
                        QByteArray cert((const char*) certs[i],fileLen);
                        //return cert;
                        certList.append(cert);

                    }
                }
                catch (...) {
                    Logger::log("Error in loop: cannot readFileBySelectingUnderActiveDF");
                    //throw SmartCardException(QString(e.ToString()));
                }
            }

            return certList;
        }
        catch (AkisException e) {
            Logger::log("Error: " + QString(e.ToString()));
            //throw SmartCardException(QString(e.ToString()));
        }
        catch(...) {
            Logger::log("Error: Unknown error");
            throw SmartCardException("Unknown error");
        }

        Logger::log("APDUSmartCard::getCertificate - end");
    }

    void APDUSmartCard::openSession()
    {
        try {
            Logger::log("Session is being opened to APDU Smartcard");
            //CommandTransmitterPCSC transmitter;
            MilkoCppLib transmitter;
            Logger::log("Transmitter created");
            transmitter.connectCard();
            Logger::log("Connected to card");

            this->transmitter = transmitter;

            Logger::log("Session is opened successfully, I hope");
        }
        catch(AkisException& e) {
            Logger::log("Error: " + QString(e.ToString()));
            throw SmartCardException(QString(e.ToString()));
        }
        catch(...) {
            Logger::log("Error: Unknown error");
            throw SmartCardException("Unknown error");
        }

        usleep(500000);
    }

    QByteArray APDUSmartCard::sign(const QByteArray data, const QString signingAlg)
    {
        //burda data'ya padding yapilacak
        SignatureScheme* scheme = SignatureSchemeFactory::getSignatureScheme(signingAlg);
        QByteArray dataToBeSigned = scheme->getSignatureInput(data);
        delete scheme;

        Logger::log("APDUSmartCard::sign - end");
        return tryAllKeys(dataToBeSigned);
    }

    QByteArray APDUSmartCard::tryAllKeys(const QByteArray data)
    {
        Logger::log("APDUSmartCard::tryAllKeys - beginning");
        return signAndCheck(data, 1);//TO DO implement trying all keys
    }

    QByteArray APDUSmartCard::signAndCheck(const QByteArray data, int keyID)
    {
        try {
        commandAttribute cadata;
        cadata.value = (unsigned char*)data.data();
        cadata.len = data.length();

        Logger::log(QString::number(data.length()));

        Logger::log("APDUSmartCard::signAndCheck - AKISCIF sign");
        Logger::log("APDUSmartCard::signAndCheck - keyid:");
        Logger::log(QString::number(keyID));
        // // //
        Logger::log("Entered sleep for 3 seconds");
        usleep(3000000);
        Logger::log("Woke up");
        // // //
        QByteArray signResult((const char *)mpCommands->sign(cadata,keyID),256);//todo
        Logger::log(QString::number(signResult.size()));
        Logger::log("APDUSmartCard::signAndCheck - after AKISCIF sign");
        return signResult;
        } catch(AkisException& e) {
            Logger::log(e.ToString());
            Logger::log(QString::number(e.GetErrorCode()));
        }
    }

    void APDUSmartCard::login(const QString & pin)
    {
        try {
        commandAttribute pinCommand;
        QByteArray pinArray = pin.toLocal8Bit();
        pinCommand.value = (unsigned char*)pinArray.data();
        pinCommand.len = pin.length();

        mpCommands->verify(pinCommand,false);
        } catch(AkisException e) {
            Logger::log("verify olamadi");
            Logger::log(e.ToString());
        }
    }

    BaseSigner * APDUSmartCard::getSigner(const QByteArray& cert, const QString& algorithm)
    {
        return new APDUSigner(*this, cert, algorithm);
    }
}
