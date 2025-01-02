package tr.gov.tubitak.uekae.esya.api.common.tools;

public class Chronometer
{

    private static final ThreadLocal<Chronometer> INSTANCE = new ThreadLocal<Chronometer>();

    private long mStart;
    private long mTotal = 0;
    private boolean mRunning = false;
    private long mCount = 0;
    private long mSure = 0;
    private final String mName;


    public static Chronometer getInstance()
    {
        if (INSTANCE.get() == null) {
            INSTANCE.set(new Chronometer("Default"));
        }
        return INSTANCE.get();
    }

    public Chronometer(String aName)
    {
        super();
        mName = aName;
    }


    public void restart()
    {
        reset();
        start();
    }

    public void reset()
    {
        mTotal = 0;
        mCount = 0;
        mRunning = false;
    }

    public synchronized void start()
    {
        mStart = System.currentTimeMillis();
        mRunning = true;
    }

    public synchronized long stop()
    {
        if (mRunning) {
            mSure = System.currentTimeMillis() - mStart;
            mTotal += mSure;
            mCount++;
            mRunning = false;
            return mSure;
        }
        else return 0;

    }

    public synchronized String toString()
    {
        String st;

        st = mName + " : ";
        if (mRunning)
            st = "Calismaya devam ediyor. Calisan haric durum:\n        ";

        st += mCount + " baslat-durdur, toplam " + mTotal / 1000L + " saniye " + mTotal % 1000L + " ms   -- 1000 Birim islem:" + ((double) (mTotal * 1000)) / mCount + "ms ,[ 1 Birim İşlem :"+ ((double) (mTotal)) / mCount+"]";

        return st;
    }
    public synchronized String toTitle()
    {
        String st="";
        if (mRunning)
            st = "Calismaya devam ediyor. Calisan haric durum:\n        ";

        st += mCount + " adet işlem, Toplam Süre " + mTotal / 1000L + " saniye " + mTotal % 1000L + " ms,"+"[ 1 Birim İşlem :"+ (double)(((double) (mTotal)) / mCount)/1000+" saniye ]";

        return st;
    }

    public synchronized String stopSingleRun()
    {
        stop();
        return mName + " : " + mSure + " ms. ";
    }

    public synchronized String toString(String aMessage)
    {
        return aMessage + " --> " + toString();
    }
    
    public long getTotalTime(){
    	return mTotal;
    }
    

}