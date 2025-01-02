//
//  CommandsV12.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 1/23/14.
//  Copyright (c) 2014 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__CommandsV12__
#define __akisIOSCIF__CommandsV12__

#include <iostream>
#include "FCI.h"
#include "APDUResponse.h"
#include "Pin.h"
#include "CommandAPDU.h"
#include "Algorithm.h"
#include "CardErrorCodes.h"
#include "AkisException.h"
#include "AbstractAkisCommands.h"

class CommandsV12: public AbstractAkisCommands{
	Tags tags;
    CommandAPDU apduCommand;
    Algorithm algorithm;
    CardErrorCodes cardErrorCodes;
    bool isSecureMessagingActive;
protected:
    SdoHeader ReadHeader(unsigned char keyID, unsigned char keyType);
public:
    CommandsV12(ICommandTransmitter &itrans);
	
    FCI* selectMF();
	FCI* selectFileUnderMF(const unsigned char* FID);
	FCI* selectChildDF(const unsigned char *FID);  //test
	FCI* selectEFUnderDF(const unsigned char *FID); //test
    FCI* selectDFByName(const char* name);
	
    unsigned char* readBinaryFile(int len); //test
	unsigned char* readBinaryFile(int start, int len); //test
    unsigned char* readFileBySelectingUnderActiveDF(const unsigned char* FID);
	
    
    unsigned char* getData(unsigned char mode); //test
    Pin readPin(unsigned char pinID);
    
    void verify(commandAttribute pin, bool isMF);
	void verify(unsigned char pinNo, commandAttribute pin, bool isMF);
    unsigned char* sign(int signAlg, int hashAlg, commandAttribute key, commandAttribute data);
    
    unsigned char* sign(commandAttribute data, unsigned char keyID);
    void mse(unsigned char keyID);
    
    void verifyCertificate(int signAlg, int hashAlg, commandAttribute keyRef, commandAttribute cvCert);
	void verifyCertificate(int signAlg, int hashAlg, commandAttribute cvCert);
    
    
	unsigned char* getDataSdo(commandAttribute data);
    
    void unlockMFPIN(unsigned char pukNo, commandAttribute PUK, unsigned char pinNo, commandAttribute newPin);
    void unlockDFPIN(unsigned char pukNo, commandAttribute PUK, unsigned char pinNo, commandAttribute newPin);
    
    void unlockMFPIN(commandAttribute PUK, commandAttribute newPin);
    void unlockDFPIN(const unsigned char* FID, commandAttribute PUK, commandAttribute newPin);
    
    
    void changeMFPIN(unsigned char pinNo, commandAttribute oldPin, commandAttribute newPin);
    void changeDFPIN(unsigned char pinNo, commandAttribute oldPin, commandAttribute newPin);
    
    void changeMFPIN(commandAttribute oldPIN, commandAttribute newPIN);
    void changeDFPIN(const unsigned char* FID, commandAttribute oldPIN, commandAttribute newPIN);
    
    void changeMFPUK(commandAttribute mfPIN, commandAttribute oldPUK, commandAttribute newPUK);
    void changeDFPUK(const unsigned char* FID, commandAttribute oldPUK, commandAttribute newPUK);
    
    
    
    void changeMFPINPUK(bool isPUK, commandAttribute mfPin, commandAttribute oldPin, commandAttribute newPin);
    void changeDFPINPUK(const unsigned char* FID, bool isPUK, commandAttribute oldPin, commandAttribute newPin);
    
	
	unsigned char* PSO(unsigned char P1, unsigned char P2, commandAttribute data);
	void MseSet(unsigned char P1, unsigned char P2, commandAttribute data);
    //    APDUResponse getResponse(unsigned char le);
};

#endif /* defined(__akisIOSCIF__CommandsV12__) */

