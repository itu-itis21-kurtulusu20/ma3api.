package test.esya.api.cmssignature.multithread;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.x509.Time;

public class MultiThreadAsn1TimeTest {

    public static int THREAD_COUNT = 32;
    public static int RUN_COUNT = 500;

    Thread [] threads;

    public MultiThreadAsn1TimeTest(){
        threads = new Thread[THREAD_COUNT];
    }


    @Test
    public void testTimeThread() throws Exception, Asn1Exception {
        byte[] bytes = StringUtil.toByteArray("170D3233303530373038323232315A");

        Time notAfter = new Time();
        Asn1DerDecodeBuffer decodeBuffer = new Asn1DerDecodeBuffer(bytes);
        notAfter.decode(decodeBuffer);

        for(int i=0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for(int r=0; r < RUN_COUNT; r++) {
                    Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
                    notAfter.encode(buff);
                    if(buff.getMsgLength() != 15)
                        System.out.println("ERROR " + "thread id: " + Thread.currentThread().getId() + " run: " + r + " len: " + buff.getMsgLength());
                }
            });
        }

        runAndJoin();
    }


    public void runAndJoin() throws InterruptedException {
        for(int i=0; i < THREAD_COUNT; i++) {
            threads[i].start();
            System.out.println("Thread Started: " + i);
        }

        Thread.currentThread().sleep(5000);

        for(int i=0; i < THREAD_COUNT; i++) {
            threads[i].join();
            System.out.println("Thread Joined: " + i);
        }
    }
}