package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation;

public class ByteRange {
    long startIndex;
    long len;

    public ByteRange(long startIndex, long len) {
        this.startIndex = startIndex;
        this.len = len;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public long getLen() {
        return len;
    }

    private long getEndIndex(){ return startIndex + len; }

    public boolean isCoverGivenRange(ByteRange givenRange){
        if(givenRange.getStartIndex() >= getStartIndex() && givenRange.getEndIndex() <= getEndIndex())
            return true;
        else
            return false;
    }
}
