package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation;

import java.util.ArrayList;
import java.util.List;

public class ByteRangeCollection {

    List<ByteRange> byteRangeCollection = new ArrayList<ByteRange>();

    public void add(ByteRange byteRange){
        byteRangeCollection.add(byteRange);
    }

    public void add(long offset, int len){
        byteRangeCollection.add(new ByteRange(offset, len));
    }

    public boolean hasData(){
        return byteRangeCollection.size() > 0;
    }

    public boolean isCoverGivenRange(ByteRange range){
        for(ByteRange aRange : byteRangeCollection){
            if(aRange.isCoverGivenRange(range))
                return true;
        }

        return false;
    }
}
