package tr.gov.tubitak.uekae.esya.api.common.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CombinedInputStream  extends InputStream
{
	List<InputStream> mStreams = new ArrayList<InputStream>();
	int mStreamIndex = 0;
	
	
	public void addInputStream(InputStream aInputStream)
	{
		mStreams.add(aInputStream);
	}

	@Override
	public synchronized int read() throws IOException 
	{
		while(mStreamIndex < mStreams.size())
		{
			int value = mStreams.get(mStreamIndex).read();
			if(value == -1)
				mStreamIndex++;
			else
				return value;
		}
		return -1;
	}

	//Java standartında stream sonunda -1 dönülüyor.
	public synchronized int read(byte b[], int off, int len) throws IOException
	{
		if (b == null) {
		    throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
		    throw new IndexOutOfBoundsException();
		} else if (len == 0) {
		    return 0;
		}

		if(mStreamIndex >= mStreams.size())
			return -1;
		
		int total = 0;
		int activeStreamRead = 0;
		while(mStreamIndex < mStreams.size() && len > 0)
		{
			activeStreamRead = mStreams.get(mStreamIndex).read(b,off,len);
			if(activeStreamRead > 0)
			{
				off = off + activeStreamRead;
				len = len - activeStreamRead;
				total = total + activeStreamRead;
			}
			else
				mStreamIndex++;
		}

		if(total > 0)
			return total;

		//Total 0 ise ve son stream -1 dönmüş ise stream'ler sonlandığı için -1 dönülsün.
		if(activeStreamRead == -1)
			return -1;

		//Kodun buraya gelmemesi lazım.
		throw new IOException("Unexpected Error. Total can not be negative! " + total );
	}

	
}
