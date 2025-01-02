//
//  APDUParam.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__APDUParam__
#define __akisIOSCIF__APDUParam__

#include <iostream>
class APDUParam {
    unsigned char m_bClass;
    unsigned char m_bChannel;
    unsigned char m_bP2;
    unsigned char m_bP1;
    unsigned char* m_baData;
    short m_nLe; //=-1
    
    bool m_fUseP1;// = false
    bool m_fUseP2;// = false
    bool m_fChannel; // = false
    bool m_fData; // = false
    bool m_fClass ; // = false
    bool m_fLe; // = false
    
public:
    
    APDUParam(APDUParam &param, int len);
    APDUParam (unsigned char bClass, unsigned char bP1, unsigned char bP2, unsigned char baData[], short nLe);
    
    APDUParam Clone();
    void Reset();
    
    bool UseClass(){ return m_fClass;}
    bool UseChannel(){ return m_fChannel;}
    bool UseLe(){ return m_fLe;}
    bool UseData() { return m_fData;}
    bool UseP1() {return m_fUseP1;}
    bool UseP2() { return m_fUseP2;}
    
    void setP1(unsigned char P1){m_bP1=P1;}
    unsigned char getP1(){return m_bP1;}
    
    
    void setP2(unsigned char P2){m_bP2=P2;}
    unsigned char getP2(){return m_bP2;}
    
    
    void setData(unsigned char* data){m_baData=data;}
    unsigned char* getData(){return m_baData;}
    
    void setLe(unsigned char Le){m_nLe=Le;}
    unsigned char getLe(){return m_nLe;}
    
    void setChannel(unsigned char channel){m_bChannel=channel;}
    unsigned char getChannel(){return m_bChannel;}
    
    void setClass(unsigned char Class){m_bClass=Class;}
    unsigned char getClass(){return m_bClass;}
    
};


#endif /* defined(__akisIOSCIF__APDUParam__) */
