using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.mobile
{
    public class FingerPrintInfo 
    {
        private String fingerPrint;
        private MobileSigner mobileSigner;

        public delegate void fingerPrintCalculated(FingerPrintInfo fingerPrintInfo);
        public event fingerPrintCalculated fingerPrintCalculatedEvent;


        public FingerPrintInfo(MobileSigner mobileSigner)
        {
            this.mobileSigner = mobileSigner;
        }

        public void setFingerPrint(String fingerPrint)
        {
            this.fingerPrint = fingerPrint;
            if(fingerPrintCalculatedEvent != null)
                fingerPrintCalculatedEvent(this);
        }

        public String getFingerPrint()
        {
            return fingerPrint;
        }

        public MobileSigner getMobileSigner()
        {
            return mobileSigner;
        }
    }
}
