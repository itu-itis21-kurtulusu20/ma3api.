package tr.gov.tubitak.uekae.esya.api.infra.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by orcun.ertugrul on 25-Jan-18.
 */

public class TCPUtil
{
    //ToDo: İki okuma arasındaki timeout'a bakılarak timeout hesabı yapılması lazım. Tabii ki 1GB bir dosyasnın indirilmesi birkaç saniyeden uzun sürecektir.
    //ToDo: Zaten burada byte array kullanıldığı için büyük dosya indirmeye elverişli değil.
    /**
     * Reads data from stream until the peer closes connection, timeout passes or all data read. Use this function for reading small data.
     * @param bis
     * @param lenght
     * @param timeoutInSeconds
     * @return
     * @throws IOException
     * @throws ESYAException
     */
    public static byte [] readWithLenght(BufferedInputStream bis, int lenght, int timeoutInSeconds) throws IOException, ESYAException
    {
        try
        {
            byte[] buff = new byte[lenght];
            int buffIndex = 0;
            int remainingLen = lenght;
            long maxMiliSecond = System.currentTimeMillis() + timeoutInSeconds * 1000;
            while (remainingLen > 0)
            {
                int readLen = bis.read(buff, buffIndex, remainingLen);

                if (readLen == -1)
                    throw new ESYAException("Can not read stream!");

                if(readLen > 0)
                {
                    remainingLen = remainingLen - readLen;
                    buffIndex = buffIndex + readLen;
                }

                if(remainingLen > 0) {
                    if(System.currentTimeMillis() > maxMiliSecond)
                        throw new ESYAException("Timeout Error. The data could not be read in " + timeoutInSeconds + " seconds!");
                    Thread.sleep(100);
                }
            }
            return buff;
        }
        catch (InterruptedException ex)
        {
            throw new ESYAException("Thread.sleep error!", ex);
        }
    }

    /**
     * Reads data from stream until the peer closes connection or timeout passes. Use this function for reading small data.
     * @param bis
     * @param timeoutInSeconds
     * @return
     * @throws IOException
     * @throws ESYAException
     */
    public static byte [] readUntilStreamClose(BufferedInputStream bis, int timeoutInSeconds) throws IOException, ESYAException
    {
        try
        {
            ByteArrayOutputStream bos= new ByteArrayOutputStream();
            boolean readOnce = false;
            int BUF_LEN = 1024;
            byte [] buff = new byte [BUF_LEN];
            long maxMiliSecond = System.currentTimeMillis() + timeoutInSeconds * 1000;

            while (true)
            {
                int readLen = bis.read(buff, 0, BUF_LEN);

                if (readLen == -1 && readOnce == false)
                    throw new ESYAException("Can not read stream!");

                if(readLen == -1  && readOnce == true)
                    break;

                if(readLen > 0)
                {
                    readOnce = true;
                    bos.write(buff, 0, readLen);
                }

                if(System.currentTimeMillis() > maxMiliSecond)
                    throw new ESYAException("Timeout Error. The data could not be read in " + timeoutInSeconds + " seconds!");

                Thread.sleep(100);
            }
            return bos.toByteArray();
        }
        catch (InterruptedException ex)
        {
            throw new ESYAException("Thread.sleep error!", ex);
        }
    }
}
