//
//  AbstractAkisCommands.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__AbstractAkisCommands__
#define __akisIOSCIF__AbstractAkisCommands__

#include <iostream>
#include "FCI.h"
#include "APDUResponse.h"
#include "Pin.h"
#include "ICommandTransmitter.h"

class AbstractAkisCommands {
    
    FCI *lastFCI;
    
public:
	ICommandTransmitter *transmitter;
    bool isSecureMessagingActive;
    
    APDUResponse sendCommand(unsigned char *command , int len);
    APDUResponse getResponse(unsigned char le);
    AbstractAkisCommands(ICommandTransmitter &itrans);
    
    void setLastFCI(FCI &fci){lastFCI = &fci; }
    FCI* getLastFCI(){ return lastFCI;}
    
    virtual FCI* selectMF() = 0;
    virtual FCI* selectFileUnderMF(const unsigned char* FID) = 0;
    virtual FCI* selectChildDF(const unsigned char* FID) = 0;
    virtual FCI* selectEFUnderDF(const unsigned char* FID) = 0;
    virtual FCI* selectDFByName(const char* name)=0;
    
    virtual unsigned char* readBinaryFile(int len) = 0;
    virtual unsigned char* readBinaryFile(int start, int len) = 0;
    virtual unsigned char* readFileBySelectingUnderActiveDF(const unsigned char* FID) = 0;
    
    virtual unsigned char* getData(unsigned char mode) = 0;
    virtual Pin readPin(unsigned char pinID) = 0;
    virtual void verify(commandAttribute pin, bool isMF) = 0;
    virtual void verify(unsigned char pinNo, commandAttribute pin, bool isMF) = 0;
    virtual unsigned char* sign(commandAttribute data, unsigned char keyID) = 0;
    virtual unsigned char* sign(int signAlg, int hashAlg, commandAttribute key, commandAttribute data) = 0;
    virtual void mse(unsigned char keyID) = 0;
    
    virtual void verifyCertificate(int signAlg, int hashAlg, commandAttribute cvCert) = 0;
    virtual void verifyCertificate(int signAlg, int hashAlg, commandAttribute keyRef, commandAttribute cvCert) = 0;
    
    virtual void changeDFPIN(unsigned char pinNo, commandAttribute oldPin, commandAttribute newPin) = 0;
    virtual void changeMFPIN(unsigned char pinNo, commandAttribute oldPin, commandAttribute newPin) = 0;
    
    virtual void changeMFPIN(commandAttribute oldPin, commandAttribute newPin) = 0;
    virtual void changeMFPUK(commandAttribute mfPIN, commandAttribute oldPUK, commandAttribute newPUK) = 0;
    
    virtual void changeDFPIN(const unsigned char* FID, commandAttribute oldPin, commandAttribute newPin) = 0;
    virtual void changeDFPUK(const unsigned char* FID, commandAttribute oldPUK, commandAttribute newPUK) = 0;
    
    virtual void unlockDFPIN(const unsigned char* FID, commandAttribute PUK, commandAttribute newPin) = 0;
    virtual void unlockMFPIN(commandAttribute PUK, commandAttribute newPin) = 0;
    
    virtual void unlockDFPIN(unsigned char pukNo, commandAttribute PUK, unsigned char pinNo, commandAttribute newPin) = 0;
    virtual void unlockMFPIN(unsigned char pukNo, commandAttribute PUK, unsigned char pinNo, commandAttribute newPin) = 0;
    
    
    virtual unsigned char* getDataSdo(commandAttribute data)= 0;
    virtual void MseSet(unsigned char P1, unsigned char P2, commandAttribute data)= 0;
    virtual unsigned char* PSO(unsigned char P1, unsigned char P2, commandAttribute data)= 0;
    
    
};
#endif /* defined(__akisIOSCIF__AbstractAkisCommands__) */
