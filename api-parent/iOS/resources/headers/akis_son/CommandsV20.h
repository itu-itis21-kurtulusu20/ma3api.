//
//  CommandsV20.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__CommandsV20__
#define __akisIOSCIF__CommandsV20__

#include <iostream>
#include "FCI.h"
#include "APDUResponse.h"
#include "Pin.h"
#include "CommandAPDU.h"
#include "Algorithm.h"
#include "CardErrorCodes.h"
#include "AkisException.h"
#include "AbstractAkisCommands.h"

class CommandsV20 : public AbstractAkisCommands{
	Tags tags;
    CommandAPDU apduCommand;
    Algorithm algorithm;
    CardErrorCodes cardErrorCodes;
    bool isSecureMessagingActive;
protected:
	SdoHeader ReadHeader(unsigned char keyID, unsigned char keyType);
public:
    CommandsV20(ICommandTransmitter &itrans);
    
	FCI* selectMF();
	FCI* selectFileUnderMF(const unsigned char* FID);
	FCI* selectChildDF(const unsigned char *FID);
	FCI* selectEFUnderDF(const unsigned char *FID);
    FCI* selectDFByName(const char* name);
	
    unsigned char* readBinaryFile(int len);
	unsigned char* readBinaryFile(int start, int len);
    unsigned char* readFileBySelectingUnderActiveDF(const unsigned char* FID);
    
	Pin readPin(unsigned char pinID);
	unsigned char* getData(unsigned char mode); //test
    void verify(commandAttribute pin, bool isMF);
	void verify(unsigned char pinNo, commandAttribute pin, bool isMF);  //unsigned char *pin
	unsigned char* sign(int signAlg, int hashAlg, commandAttribute key, commandAttribute data);
	
    unsigned char* sign(commandAttribute data, unsigned char keyID);
    void mse(unsigned char keyID);
    
    unsigned char* getDataSdo(commandAttribute data);
    
	
	void unlockPIN(unsigned char pukNo, commandAttribute PUK, unsigned char pinNo, commandAttribute newPIN, bool isMF);
    
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
    
    
    void changePIN(unsigned char pinNo, commandAttribute oldPIN, commandAttribute newPIN, bool isMF);
    
	
    
    unsigned char* PSO(unsigned char P1, unsigned char P2, commandAttribute data);
	void MseSet(unsigned char P1, unsigned char P2, commandAttribute data);
	
    void verifyCertificate(int signAlg, int hashAlg, commandAttribute keyRef, commandAttribute cvCert);
	void verifyCertificate(int signAlg, int hashAlg, commandAttribute cvCert);
	
};
#endif /* defined(__akisCIF__CommandsV20__) */
