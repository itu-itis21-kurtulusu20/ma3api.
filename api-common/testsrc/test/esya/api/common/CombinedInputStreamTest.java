package test.esya.api.common;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.tools.CombinedInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CombinedInputStreamTest {

    //Java Stream Referans
    //Stream'den okuma sırasında readLen olarak Java stream sonunda -1, DotNet stream sonunda 0 dönüyor.
    @Test
    public void testByteArrayInputStream() throws IOException {

        byte [] buff = new byte[3];

        ByteArrayInputStream bis = new ByteArrayInputStream(new byte[] {1,2,3,4,5,6,7,8,9, 0, 1,2,3,4,5,6,7,8,9, 0});


        for(int i=0; i < 10; i++)
        {
            int readLen = bis.read(buff, 0, buff.length);
            System.out.println(readLen);
        }

    }

    public CombinedInputStream BuildCombinedInputStream()
    {
        ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(new byte[] {1,2,3,4,5,6,7,8,9, 0});
        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(new byte[] {1,2,3,4,5,6,7,8,9, 0});


        CombinedInputStream stream = new CombinedInputStream();
        stream.addInputStream(byteArrayInputStream1);
        stream.addInputStream(byteArrayInputStream2);

        return stream;
    }

    public void testCombinedInputStream_Size10(int buffSize, int [] output) throws IOException
    {
        byte[] buff = new byte[buffSize];

        ArrayList<Integer> readCounts = new ArrayList();
        CombinedInputStream stream = BuildCombinedInputStream();

        for (int i = 0; i < 10; i++)
        {
            int readLen = stream.read(buff, 0, buff.length);
            System.out.println(readLen);
            readCounts.add(readLen);
        }

        int[] readCountArr = new int[readCounts.size()];
        for(int i=0; i < readCountArr.length; i++){
            readCountArr[i] = readCounts.get(i).intValue();
        }


        Assert.assertArrayEquals(output, readCountArr);

    }


    @Test
    public void test_buff_15() throws IOException {
        testCombinedInputStream_Size10(15, new int[] { 15, 5, -1, -1, -1, -1, -1, -1, -1, -1 });
    }

    @Test
    public void test_buff_10() throws IOException
    {
        testCombinedInputStream_Size10(10, new int[] {10, 10, -1, -1, -1, -1, -1, -1, -1, -1});
    }

    @Test
    public void test_buff_9() throws IOException
    {
        testCombinedInputStream_Size10(9, new int[] { 9, 9, 2, -1, -1, -1, -1, -1, -1, -1 });
    }

    @Test
    public void test_buff_8() throws IOException
    {
        testCombinedInputStream_Size10(8, new int[] { 8, 8, 4, -1, -1, -1, -1, -1, -1, -1 });
    }

    @Test
    public void test_buff_7() throws IOException
    {
        testCombinedInputStream_Size10(7, new int[] { 7, 7, 6, -1, -1, -1, -1, -1, -1, -1 });
    }

    @Test
    public void test_buff_6() throws IOException
    {
        testCombinedInputStream_Size10(6, new int[] { 6, 6, 6, 2, -1, -1, -1, -1, -1, -1 });
    }

    @Test
    public void test_buff_5() throws IOException
    {
        testCombinedInputStream_Size10(5, new int[] { 5, 5, 5, 5, -1, -1, -1, -1, -1, -1 });
    }

    @Test
    public void test_buff_4() throws IOException
    {
        testCombinedInputStream_Size10(4, new int[] { 4, 4, 4, 4, 4, -1, -1, -1, -1, -1 });
    }

    @Test
    public void test_buff_3() throws IOException
    {
        testCombinedInputStream_Size10(3, new int[] { 3, 3, 3, 3, 3, 3, 2, -1, -1, -1 });
    }

    @Test
    public void test_buff_2() throws IOException
    {
        testCombinedInputStream_Size10(2, new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
    }

    @Test
    public void test_buff_1() throws IOException
    {
        int [] output = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
        byte[] buff = new byte[1];

        ArrayList<Integer> readCounts = new ArrayList();
        CombinedInputStream stream = BuildCombinedInputStream();

        for (int i = 0; i < 20; i++)
        {
            int readLen = stream.read(buff, 0, buff.length);
            System.out.println(readLen);
            readCounts.add(readLen);
        }

        int[] readCountArr = new int[readCounts.size()];
        for(int i=0; i < readCountArr.length; i++){
            readCountArr[i] = readCounts.get(i).intValue();
        }

        Assert.assertArrayEquals(output, readCountArr);
    }

}
