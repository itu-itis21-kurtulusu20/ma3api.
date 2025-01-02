using System;
using tr.gov.tubitak.uekae.esya.api.common;
using System.Threading;
using System.Collections.Generic;
using System.Linq;

namespace tr.gov.tubitak.uekae.esya.api.infra.mobile
{
    public class MultiMobileSigner
    {
        int signatureCount;

        MobileSigner signer;
        bool cancel_;

        List<MultiSignResult> result = null;

        Object signOnceLock = new Object();
        Object requestCountLock = new Object();
        int requestCount = 0;

        byte[][] toBeSignedBytesList;
        String[] informativeTextList;

        public MultiMobileSigner(MobileSigner signer, int signatureCount)
        {
            this.signer = signer;
            this.signatureCount = signatureCount;
            this.cancel_ = false;
            this.toBeSignedBytesList = new byte[signatureCount][];
            this.informativeTextList = new String[signatureCount];
        }

        public int getSignatureCount()
        {
            return signatureCount;
        }

        public byte[] sign(byte[] bytes, String informativeText, int index)
        {
            lock (requestCountLock)
            {
                toBeSignedBytesList[index] = bytes;
                informativeTextList[index] = informativeText;
                requestCount++;
            }
            try
            {
                while (requestCount < signatureCount && cancel_ == false)
                {
                    Thread.Sleep(200);
                }

                if (cancel_ == true)
                    throw new ESYAException("User cancelled operation!");
             
                lock(signOnceLock)
                {
                    if (result == null)
                    {
                        List<byte[]> toBeSignedBytes = toBeSignedBytesList.ToList();
                        List<String> informativeTexts = informativeTextList.ToList();
                        result = signer.sign(toBeSignedBytes, informativeTexts);
                    }
                }

                byte[] signature = ((MultiSignResult)result[index]).getSignature();

                if (signature == null)
                {
                    String errorMsg = "Signature is not available for :" + ((MultiSignResult)result[index]).getInformativeText();
                    throw new ThreadInterruptedException(errorMsg);
                }
                else
                {
                    return signature;
                }
            }
            catch (ThreadInterruptedException ex)
            {
                throw new ESYAException(ex);
            }
        }

        public void cancel()
        {
            this.cancel_ = true;
        }

        public MobileSigner getMobileSigner()
        {
            return signer;
        }

        
    }

}
