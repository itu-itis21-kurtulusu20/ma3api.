package test.esya.api.cmssignature.validation;

import junit.framework.TestCase;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.SignatureParser;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.TimeStampSignatureChecker;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;

/**
 * @author ayetgin
 */
public class TimeStampSignatureCheckerTest extends TestCase
{
    static String TUBITAK_STS_ENCAPSULATED_TS = "MIIM+wYJKoZIhvcNAQcCoIIM7DCCDOgCAQMxCzAJBgUrDgMCGgUAMHoGCyqGSIb3DQEJEAEEoGsE\n" +
            "aTBnAgEBBgtghhgBAgEBBQcDATAfMAcGBSsOAwIaBBT2BpnVCi/ptF9nHvclFvqE7umLLwIEAbVh\n" +
            "ohgTMjAxMjEwMjMxMzI1MTEuOTU2WjADAgEBAhT9gOsmPYr82ONoJi3n+eRtuJDJ/qCCCC0wgggp\n" +
            "MIIHEaADAgECAgcAwp/gTgh7MA0GCSqGSIb3DQEBBQUAMIIBHzELMAkGA1UEBhMCVFIxGDAWBgNV\n" +
            "BAcMD0dlYnplIC0gS29jYWVsaTFHMEUGA1UECgw+VMO8cmtpeWUgQmlsaW1zZWwgdmUgVGVrbm9s\n" +
            "b2ppayBBcmHFn3TEsXJtYSBLdXJ1bXUgLSBUw5xCxLBUQUsxSDBGBgNVBAsMP1VsdXNhbCBFbGVr\n" +
            "dHJvbmlrIHZlIEtyaXB0b2xvamkgQXJhxZ90xLFybWEgRW5zdGl0w7xzw7wgLSBVRUtBRTEjMCEG\n" +
            "A1UECwwaS2FtdSBTZXJ0aWZpa2FzeW9uIE1lcmtlemkxPjA8BgNVBAMMNUNpaGF6IFNlcnRpZmlr\n" +
            "YXPEsSBIaXptZXQgU2HEn2xhecSxY8Sxc8SxIC0gU8O8csO8bSAzMB4XDTEwMDkxNjExMzYxN1oX\n" +
            "DTE3MDYxNDExMzYxN1owggEZMQswCQYDVQQGEwJUUjEWMBQGA1UEBwwNR2ViemUtS29jYWVsaTFH\n" +
            "MEUGA1UECgw+VMO8cmtpeWUgQmlsaW1zZWwgdmUgVGVrbm9sb2ppayBBcmHFn3TEsXJtYSBLdXJ1\n" +
            "bXUgLSBUw5xCxLBUQUsxSDBGBgNVBAsMP1VsdXNhbCBFbGVrdHJvbmlrIHZlIEtyaXB0b2xvamkg\n" +
            "QXJhxZ90xLFybWEgRW5zdGl0w7xzw7wgLSBVRUtBRTEjMCEGA1UECwwaS2FtdSBTZXJ0aWZpa2Fz\n" +
            "eW9uIE1lcmtlemkxOjA4BgNVBAMMMVTDnELEsFRBSyBVRUtBRSBaYW1hbiBEYW1nYXPEsSBTdW51\n" +
            "Y3VzdSBTw7xyw7xtIDIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC9B+0wD3ah6Lft\n" +
            "vh4S4tXkaNUpTa03iOKw/NSTd9ttnfMbskKzehr40ZpMrQJ4+KTGtN0CDPtqNyWO+fiMvhkCy3fP\n" +
            "L+3sb4e+lU9TQSMdVjxB5ODtBDWFdeASRjViSV/VAE3UK9TMfbPpumr3xCw1V5HmwNh46dr4xALP\n" +
            "bSr/MQANglh6kN77uH9fY5yVfpYH1rW0oua9yodfWJ8UUAWyn4hP+jWrUdRxupSPv3ofIxDJzvFa\n" +
            "c0HuAryzz2VLcaJTDglo3HlkPfrDLDmd2shZyURaVGh7dCW45IHnPuuXbRUBDKFVXlrhP0CkBaiI\n" +
            "nrVjnORUjbIheqqfC6f0BJ3BAgMBAAGjggNqMIIDZjAfBgNVHSMEGDAWgBSWKoV2f7WDF+DhLjSG\n" +
            "C0s/2G4pTjAdBgNVHQ4EFgQU4D98z+/pcdjPYzQWXevE40R3YsswDgYDVR0PAQH/BAQDAgbAMIIB\n" +
            "RQYDVR0gBIIBPDCCATgwggE0BgtghhgBAgEBBQcBAjCCASMwPAYIKwYBBQUHAgEWMGh0dHA6Ly93\n" +
            "d3cua2FtdXNtLmdvdi50ci9CaWxnaURlcG9zdS9LU01fQ0VTX1NVRTCB4gYIKwYBBQUHAgIwgdUe\n" +
            "gdIAQgB1ACAAcwBlAHIAdABpAGYAaQBrAGEAIABpAGwAZQAgAGkAbABnAGkAbABpACAAcwBlAHIA\n" +
            "dABpAGYAaQBrAGEAIAB1AHkAZwB1AGwAYQBtAGEAIABlAHMAYQBzAGwAYQByATEAbgExACAAbwBr\n" +
            "AHUAbQBhAGsAIABpAOcAaQBuACAAYgBlAGwAaQByAHQAaQBsAGUAbgAgAHcAZQBiACAAcwBpAHQA\n" +
            "ZQBzAGkAbgBpACAAegBpAHkAYQByAGUAdAAgAGUAZABpAG4AaQB6AC4wCQYDVR0TBAIwADAWBgNV\n" +
            "HSUBAf8EDDAKBggrBgEFBQcDCDCB0gYDVR0fBIHKMIHHMDigNqA0hjJodHRwOi8vd3d3LmthbXVz\n" +
            "bS5nb3YudHIvQmlsZ2lEZXBvc3UvQ1NIU0lMLnYzLmNybDCBiqCBh6CBhIaBgWxkYXA6Ly9kaXpp\n" +
            "bi5rYW11c20uZ292LnRyL0NOPUNTSFNJTC52MyxPVT1LQU1VU00sTz1TTSxPPUtTTSxDPVRSP2Nl\n" +
            "cnRpZmljYXRlUmV2b2NhdGlvbkxpc3Q/YmFzZT9vYmplY3RjbGFzcz1jUkxEaXN0cmlidXRpb25Q\n" +
            "b2ludDCB0gYIKwYBBQUHAQEEgcUwgcIwPAYIKwYBBQUHMAKGMGh0dHA6Ly93d3cua2FtdXNtLmdv\n" +
            "di50ci9CaWxnaURlcG9zdS9DU0hTLnYzLmNydDCBgQYIKwYBBQUHMAKGdWxkYXA6Ly9kaXppbi5r\n" +
            "YW11c20uZ292LnRyL0NOPUNTSFMudjMsT1U9S0FNVVNNLE89U00sTz1LU00sQz1UUj9jQUNlcnRp\n" +
            "ZmljYXRlP2Jhc2U/b2JqZWN0Y2xhc3M9Y2VydGlmaWNhdGlvbkF1dGhvcml0eTANBgkqhkiG9w0B\n" +
            "AQUFAAOCAQEACC7rvDSHIEfvTOS/YWOmqxYvS0Y4qTqKY29iRsQeEPqATdDrJdUn47cCgUmfUJ0d\n" +
            "lL+WncLdLvFsCbnbZF61EcS+bJLkt17oMNV/dlOGulLXRd1SBAjvxJ8L7oXCh5xE1mwGA4fp9S7V\n" +
            "UynYe29pNTDWthAsMutRAD1C1aMYKUdb984N7VODKwEosnzv428YlBCgzTW0q3KV2281z2lDySyb\n" +
            "5+ve3AnTqZ7HgffV/4yfxGUnk6E5jD/cdMYeUgz/4Vf73rNRIOJXzQfGyBm/c9T/nhYEvOitba9k\n" +
            "7PlrjEqspC8onJMEyeBnR0k6zD39J2jSo/sgczOi6zVwdDs/WDGCBCcwggQjAgEBMIIBLDCCAR8x\n" +
            "CzAJBgNVBAYTAlRSMRgwFgYDVQQHDA9HZWJ6ZSAtIEtvY2FlbGkxRzBFBgNVBAoMPlTDvHJraXll\n" +
            "IEJpbGltc2VsIHZlIFRla25vbG9qaWsgQXJhxZ90xLFybWEgS3VydW11IC0gVMOcQsSwVEFLMUgw\n" +
            "RgYDVQQLDD9VbHVzYWwgRWxla3Ryb25payB2ZSBLcmlwdG9sb2ppIEFyYcWfdMSxcm1hIEVuc3Rp\n" +
            "dMO8c8O8IC0gVUVLQUUxIzAhBgNVBAsMGkthbXUgU2VydGlmaWthc3lvbiBNZXJrZXppMT4wPAYD\n" +
            "VQQDDDVDaWhheiBTZXJ0aWZpa2FzxLEgSGl6bWV0IFNhxJ9sYXnEsWPEsXPEsSAtIFPDvHLDvG0g\n" +
            "MwIHAMKf4E4IezAJBgUrDgMCGgUAoIIBzjAaBgkqhkiG9w0BCQMxDQYLKoZIhvcNAQkQAQQwHAYJ\n" +
            "KoZIhvcNAQkFMQ8XDTEyMTAyMzEzMjUxMVowIwYJKoZIhvcNAQkEMRYEFENeSyg3xds37WN+QehM\n" +
            "C1D2neVwMIIBawYLKoZIhvcNAQkQAgwxggFaMIIBVjCCAVIwggFOBBS82mHtP3nXN3/raP9UoTlo\n" +
            "lMZ73TCCATQwggEnpIIBIzCCAR8xCzAJBgNVBAYTAlRSMRgwFgYDVQQHDA9HZWJ6ZSAtIEtvY2Fl\n" +
            "bGkxRzBFBgNVBAoMPlTDvHJraXllIEJpbGltc2VsIHZlIFRla25vbG9qaWsgQXJhxZ90xLFybWEg\n" +
            "S3VydW11IC0gVMOcQsSwVEFLMUgwRgYDVQQLDD9VbHVzYWwgRWxla3Ryb25payB2ZSBLcmlwdG9s\n" +
            "b2ppIEFyYcWfdMSxcm1hIEVuc3RpdMO8c8O8IC0gVUVLQUUxIzAhBgNVBAsMGkthbXUgU2VydGlm\n" +
            "aWthc3lvbiBNZXJrZXppMT4wPAYDVQQDDDVDaWhheiBTZXJ0aWZpa2FzxLEgSGl6bWV0IFNhxJ9s\n" +
            "YXnEsWPEsXPEsSAtIFPDvHLDvG0gMwIHAMKf4E4IezANBgkqhkiG9w0BAQEFAASCAQCmISKYcgkJ\n" +
            "T+C+GtUAEk8Z0UuLc7pgZYWjvV9xoEqIb6rtVOszWDWXgwV3+Wn7DuyIQ44hPuxKz1THp4I+YnqR\n" +
            "Vhdi1riwkvY5I0PTdalvrUkPQaQWlSnvPzXUvj6wqVxFIgX3sGuKqNHZSyDeSv1cmjbU8gbFL67a\n" +
            "fRMTxOUbBblltVgQEg3mpJCE0ca1AHldji6CLZgOZnWA1810/NRKkfMN+mF5/5TFmn4ZQPNgK/ce\n" +
            "gcxmprWPqlnk+fATHcFPPiAERLwwfq2jfUFXfqkkRWKZmZX2BaGkgXtz8LfjQG3gVBICg0JAZyo5\n" +
            "bjoPq2QpHtT3tbrAKFmI2ey2BX8Q";

    static String TURKTRUST_STS_CONTENT_INFO_B64 = "MIIOogYJKoZIhvcNAQcCoIIOkzCCDo8CAQMxCzAJBgUrDgMCGgUAMGMGCyqGSIb3DQEJEAEEoFQE\n" +
            "UjBQAgEBBghghhgDAAMCATAhMAkGBSsOAwIaBQAEFC+3ZNNUAw+bJlOJ7WwwTC+mVqbjAgMOBCkY\n" +
            "DzIwMTIxMDIxMTYzNTU3WgIIe6aJjEfs7j6ggglxMIIEPTCCAyWgAwIBAgIBATANBgkqhkiG9w0B\n" +
            "AQUFADCBvzE/MD0GA1UEAww2VMOcUktUUlVTVCBFbGVrdHJvbmlrIFNlcnRpZmlrYSBIaXptZXQg\n" +
            "U2HEn2xhecSxY8Sxc8SxMQswCQYDVQQGEwJUUjEPMA0GA1UEBwwGQW5rYXJhMV4wXAYDVQQKDFVU\n" +
            "w5xSS1RSVVNUIEJpbGdpIMSwbGV0acWfaW0gdmUgQmlsacWfaW0gR8O8dmVubGnEn2kgSGl6bWV0\n" +
            "bGVyaSBBLsWeLiAoYykgQXJhbMSxayAyMDA3MB4XDTA3MTIyNTE4MzcxOVoXDTE3MTIyMjE4Mzcx\n" +
            "OVowgb8xPzA9BgNVBAMMNlTDnFJLVFJVU1QgRWxla3Ryb25payBTZXJ0aWZpa2EgSGl6bWV0IFNh\n" +
            "xJ9sYXnEsWPEsXPEsTELMAkGA1UEBhMCVFIxDzANBgNVBAcMBkFua2FyYTFeMFwGA1UECgxVVMOc\n" +
            "UktUUlVTVCBCaWxnaSDEsGxldGnFn2ltIHZlIEJpbGnFn2ltIEfDvHZlbmxpxJ9pIEhpem1ldGxl\n" +
            "cmkgQS7Fni4gKGMpIEFyYWzEsWsgMjAwNzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
            "AKu3PgqMyKVYFeaK7yc9SrToJdPNM8Ig3BnuiD9NYvDdE3ePYakqtdTyuTFYKTsvP2qcb3N2Je40\n" +
            "IIDu6rfwxArNK4aUyeNgsURSsloptJGXg9i3phQvKUmi8wUG+7RP2qFsmmaf8EMJyupyj+sA1zU5\n" +
            "11YXRxcw9L6/P8JorzZAwan0qafoEGsIiveGHtyaKhUG9qPw9ODHFNRRf8+0222vR5YXm3dx2Kdx\n" +
            "nSQM9pQ/hTEST7ruToK4uT6PIzdezKKqdfcYbwnTrqdUKDT74eA7YH2gvnmJhsifLfkKS8RQouf9\n" +
            "eRbHegsYz85M733WB2+Y8a+xwXrXgTW4qhe04MsCAwEAAaNCMEAwHQYDVR0OBBYEFCnFkKslrxHk\n" +
            "Yb+j/4hhkeYO/pyBMA4GA1UdDwEB/wQEAwIBBjAPBgNVHRMBAf8EBTADAQH/MA0GCSqGSIb3DQEB\n" +
            "BQUAA4IBAQAQDdr4Ouwo0RSVgrESLFF6QSU2TJ/sPx+EnWVUXKgWAkD6bho3hO9ynYYKVZ1WKKxm\n" +
            "LNA6VpM0ByWtCLCPyA8JWcqdmBzlVPi5RX9ql2+IaE1KBiY3iAIOtsbWcpnOa3faYjGkVh+uX413\n" +
            "2l32iPwa2Z61gfAyuOOI0JzzaqC5mxRZNTZPz/OOXl0XrRWV2N2y1RVuAE6zS89mlOTgzbUF2mNX\n" +
            "i+WzqtvALhyQRNsaXRik7r4EW5nVcV9VZWRi1aKbBFmGyGJ353yCRWo9F7/snXUMrqNvWtMvmDb0\n" +
            "8PUZqxFdyKbjKlhqQgnDvZImZjINXQhVdP+MmNAKpoRq0Tl9MIIFLDCCBBSgAwIBAgIBLDANBgkq\n" +
            "hkiG9w0BAQUFADCBvzE/MD0GA1UEAww2VMOcUktUUlVTVCBFbGVrdHJvbmlrIFNlcnRpZmlrYSBI\n" +
            "aXptZXQgU2HEn2xhecSxY8Sxc8SxMQswCQYDVQQGEwJUUjEPMA0GA1UEBwwGQW5rYXJhMV4wXAYD\n" +
            "VQQKDFVUw5xSS1RSVVNUIEJpbGdpIMSwbGV0acWfaW0gdmUgQmlsacWfaW0gR8O8dmVubGnEn2kg\n" +
            "SGl6bWV0bGVyaSBBLsWeLiAoYykgQXJhbMSxayAyMDA3MB4XDTExMDkyODA3NDk0MloXDTEzMDky\n" +
            "NzA3NDk0MlowgZsxLTArBgNVBAMMJFTDnFJLVFJVU1QgWmFtYW4gRGFtZ2FzxLEgSGl6bWV0bGVy\n" +
            "aTELMAkGA1UEBhMCVFIxXTBbBgNVBAoMVFTDnFJLVFJVU1QgQmlsZ2kgxLBsZXRpxZ9pbSB2ZSBC\n" +
            "aWxpxZ9pbSBHw7x2ZW5sacSfaSBIaXptZXRsZXJpIEEuxZ4uIChjKSBFeWzDvGwgMjAxMTCCASIw\n" +
            "DQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANVG8kPQK9lc/O6XoZKIAa7h+rhQ1gZS5xQAsC4b\n" +
            "eNgQXS+SzJxN5t3Oi9PmT1VoVsvgFldEIV6BvaSfuYe2/2urNWKf3aolhUWAMZXhEtX3+YMTL1Gi\n" +
            "RoV7X4q3X0hJxs/5cUKrStj3abxOE6DQfC9gIlP8uDee4y8lOeXVNGsHJV8HV4R1+9tl2xId7aOz\n" +
            "NjP0H51Eqbgrr42vQPnNr4YqSl0o/Zjpp/HkAr/MztLmRMBkSH3b+pI1MbLhWPW14HOn4HQaEq2b\n" +
            "FNtB1ABRlt5/m48IhtamjJ5lmgb9lgMqlwuUdbRgqFyIBOnBEhgFg7NG4cszwf2odt1Xh7J5nGsC\n" +
            "AwEAAaOCAVMwggFPMB8GA1UdIwQYMBaAFCnFkKslrxHkYb+j/4hhkeYO/pyBMB0GA1UdDgQWBBRo\n" +
            "7euDUgaenXg0WnTLT7IezqY9lzAWBgNVHSUBAf8EDDAKBggrBgEFBQcDCDBJBgNVHR8EQjBAMD6g\n" +
            "PKA6hjhodHRwOi8vd3d3LnR1cmt0cnVzdC5jb20udHIvc2lsL1RVUktUUlVTVF9Lb2tfU0lMX3Mz\n" +
            "LmNybDCBqQYIKwYBBQUHAQEEgZwwgZkwbQYIKwYBBQUHMAKGYWh0dHA6Ly93d3cudHVya3RydXN0\n" +
            "LmNvbS50ci9zZXJ0aWZpa2FsYXIvVFVSS1RSVVNUX0VsZWt0cm9uaWtfU2VydGlmaWthX0hpem1l\n" +
            "dF9TYWdsYXlpY2lzaV9zMy5jcnQwKAYIKwYBBQUHMAGGHGh0dHA6Ly9vY3NwLnR1cmt0cnVzdC5j\n" +
            "b20udHIwDQYJKoZIhvcNAQEFBQADggEBAD7y9r+D++ODe2QOAzTYOI6HJZ2VuWNzk+bLkeDSYsfR\n" +
            "B2gFwLEr7ADVcWMtCFMbPBWjBbn0TtkvLz9kHO5x04iSgvEL89oJSlESYNUF48/OhunPdBTekcEe\n" +
            "DEifeXrrIpT71vkP7943UvK4ueWJWGNe1KEREVMpH/+AiIVa7dPnasbzhfU8LKBinDwgdC/utqE+\n" +
            "OmXHQtJG/N0SLHdoalusW1k8RJ7CAt/Rv71TqhqySSUhwBvuieaP+FLl0Dlt8GS3Xdu1FCIr2PKr\n" +
            "oT//KaiDneivAz8K/RY1tk4m1YGu05Hhk8d8Jtl8s1kE/wgOkhIbMRZX0yl+qJkGSZ+NGjehggJA\n" +
            "MIICPDCCASQCAQEwDQYJKoZIhvcNAQEFBQAwgb8xPzA9BgNVBAMMNlTDnFJLVFJVU1QgRWxla3Ry\n" +
            "b25payBTZXJ0aWZpa2EgSGl6bWV0IFNhxJ9sYXnEsWPEsXPEsTELMAkGA1UEBhMCVFIxDzANBgNV\n" +
            "BAcMBkFua2FyYTFeMFwGA1UECgxVVMOcUktUUlVTVCBCaWxnaSDEsGxldGnFn2ltIHZlIEJpbGnF\n" +
            "n2ltIEfDvHZlbmxpxJ9pIEhpem1ldGxlcmkgQS7Fni4gKGMpIEFyYWzEsWsgMjAwNxcNMTIwMTI1\n" +
            "MTQxMjA5WhcNMTMwMTI0MTQxMjA5WqAwMC4wHwYDVR0jBBgwFoAUKcWQqyWvEeRhv6P/iGGR5g7+\n" +
            "nIEwCwYDVR0UBAQCAk4hMA0GCSqGSIb3DQEBBQUAA4IBAQBYEIG2p5LDDomf1hs4Icf7Y0rovalv\n" +
            "7QeY0qNxe3EfERNI75Jfmi3yOaeajx3W3B6T4g2Bvz65p/c0hwVBzoxUmrBaMVFlUn54bqN1zLuY\n" +
            "YAiVbgmp/6Y1CVLzOpLnez1Ja9H5trwg80gitUkoDEwDzUzBMrcFZJBUySzw6zu/qvQm1/D/4JQr\n" +
            "9pETgbTuJK8ZDUXJijamGI8uj3+zmTln0Cy/wZFy6y+gDrka7YvlFUX9+s5C6sexOUBtJJV28Jib\n" +
            "gG3XDjXq3D5XV2F+DIhszl/hMAJm4k5auOh+Q5JRt7k+aTOqBVAasB89xDXjtZyhxcUzPOpDFZzu\n" +
            "owkk1lOoMYICXTCCAlkCAQEwgcUwgb8xPzA9BgNVBAMMNlTDnFJLVFJVU1QgRWxla3Ryb25payBT\n" +
            "ZXJ0aWZpa2EgSGl6bWV0IFNhxJ9sYXnEsWPEsXPEsTELMAkGA1UEBhMCVFIxDzANBgNVBAcMBkFu\n" +
            "a2FyYTFeMFwGA1UECgxVVMOcUktUUlVTVCBCaWxnaSDEsGxldGnFn2ltIHZlIEJpbGnFn2ltIEfD\n" +
            "vHZlbmxpxJ9pIEhpem1ldGxlcmkgQS7Fni4gKGMpIEFyYWzEsWsgMjAwNwIBLDAJBgUrDgMCGgUA\n" +
            "oG4wGgYJKoZIhvcNAQkDMQ0GCyqGSIb3DQEJEAEEMCMGCSqGSIb3DQEJBDEWBBR5OSCqEbr5rMZq\n" +
            "CyCEK02nu6LPizArBgsqhkiG9w0BCRACDDEcMBowGDAWBBTC6IsKWYiNe5WrwnKzKsPq9cw6jzAN\n" +
            "BgkqhkiG9w0BAQEFAASCAQBoNKXwk8M4snyFm6Dow0X22Xt4Z9CBhb9vtTHlyYB9neZQnKAD7MiK\n" +
            "NEmnkpl1DHWC8tCnVhNUsRLhV25VykxmAbrzRIHoU+aTZilKb0udLqBYG916m6xkLho0NM+Pu4jn\n" +
            "2V1EvrOg8DorC1Rx7wcmH07CeWpSdIoSCvw+CV3zIYImgyr3Oe4JU/AJKYCNKs4KvenKmbHRH9C9\n" +
            "qi7MzASLi9cVT3/Y0tE5UMlIY2b5VR3z8PtDlH6+7Y0naou7iUcpJUlHGEjGKsMganppSrHMiSVp\n" +
            "ORFWoRULPGC18UU+LGeU/yY6WLI5LH8Q0CyY4+0um/Cg4v1klgtzlHrLUxlz";


    @Test
    public void testTurkTrustTimestamp() throws Exception {
        validateTimeStamp(TURKTRUST_STS_CONTENT_INFO_B64);
    }

    @Test
    public void testTubitakTimestamp() throws Exception
    {
        validateTimeStamp(TUBITAK_STS_ENCAPSULATED_TS);
    }

    public void validateTimeStamp(String base64DecodedTS)throws Exception{
        EContentInfo ci = new EContentInfo(Base64.decode(base64DecodedTS));
        ESignedData sd = new ESignedData(ci.getContent());

        ESignerInfo signerInfo = sd.getSignerInfo(0);

        ESignatureType type = SignatureParser.parse(signerInfo, false);

        Signer signer = ESignatureType.createSigner(type, new BaseSignedData(ci), signerInfo);

        TimeStampSignatureChecker c = new TimeStampSignatureChecker(sd);

        CheckerResult cr = new CheckerResult();
        c.check(signer, cr);
    }
}
