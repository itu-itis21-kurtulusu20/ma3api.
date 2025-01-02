#include "deneme.h"
#include "RSAScheme.h"

Deneme::Deneme()
{
}

unsigned char* Deneme::getPrefix()
{
    esya::RSAScheme scheme;
    return (unsigned char*) scheme.getPrefixForDigestAlg("SHA-256").data();
}
