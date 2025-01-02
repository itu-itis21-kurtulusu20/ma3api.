//
//  CryptoFunctions.m
//  MA3API
//
//  Created by Suleyman Uslu on 1/2/14.
//  Copyright (c) 2014 TUBITAK. All rights reserved.
//

#import "CryptoFunctions.h"
#import <Security/Security.h>
#import <Security/SecCertificate.h>
#import <Security/SecBase.h>

@implementation CryptoFunctions

+ (BOOL)verifySignature:(NSData*)signature withCert:(NSData*)cert withData:(NSData*)data {

    SecCertificateRef certRef = SecCertificateCreateWithData(NULL,(__bridge CFDataRef)cert);
    SecPolicyRef x509policy = SecPolicyCreateBasicX509();
    SecTrustRef trustRef;
    SecTrustCreateWithCertificates(certRef,x509policy,&trustRef);
    SecKeyRef keyRef = SecTrustCopyPublicKey(trustRef);
    
    OSStatus result = SecKeyRawVerify(keyRef,
                             kSecPaddingPKCS1SHA1,
                             (const uint8_t *)[data bytes],
                             (size_t)[data length],
                             (const uint8_t *)[signature bytes],
                             (size_t)[signature length]);

    return (result == errSecSuccess);
}

@end
