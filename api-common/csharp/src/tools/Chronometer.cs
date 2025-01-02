using System;
using System.Runtime.CompilerServices;

namespace tr.gov.tubitak.uekae.esya.api.common.tools
{
    public class Chronometer
    {

        private long _mStart;
        private long _mTotal;
        private bool _mRunning;
        private long _mCount;
        private long _mSure;
        private readonly string _mName;


        /*public static Chronometer getInstance()
        {
            if (INSTANCE.get() == null) {
                INSTANCE.set(new Chronometer("Default"));
            }
            return INSTANCE.get();
        }*/

        public Chronometer(string aName)
        {
            _mName = aName;
        }


        public void restart()
        {
            reset();
            start();
        }

        public void reset()
        {
            _mTotal = 0;
            _mCount = 0;
            _mRunning = false;
        }

        public static double GetCurrentMilli()
        {
            DateTime Jan1970 = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Local);
            TimeSpan javaSpan = DateTime.Now - Jan1970;
            return javaSpan.TotalMilliseconds;
        }

        [MethodImpl(MethodImplOptions.Synchronized)]
        public /*synchronized*/ void start()
        {
            _mStart = (long)GetCurrentMilli();
            //mStart = (DateTime.UtcNow).TotalMillisecond;
            _mRunning = true;
        }

        [MethodImpl(MethodImplOptions.Synchronized)]
        public /*synchronized*/ long stop()
        {
            if (!_mRunning) return 0;
            _mSure = (long)GetCurrentMilli() - _mStart;
            _mTotal += _mSure;
            _mCount++;
            _mRunning = false;
            return _mSure;
        }

        [MethodImpl(MethodImplOptions.Synchronized)]
        public /*synchronized*/ String toString()
        {
            var st = _mName + " : ";
            if (_mRunning)
                st = "Calismaya devam ediyor. Calisan haric durum:\n        ";

            st += _mCount + " baslat-durdur, toplam " + _mTotal / 1000L + " saniye " + _mTotal % 1000L + " ms   -- 1000 Birim islem:" + ((double) (_mTotal * 1000)) / _mCount + "ms";

            return st;
        }

        [MethodImpl(MethodImplOptions.Synchronized)]
        public /*synchronized*/ String stopSingleRun()
        {
            stop();
            return _mName + " : " + _mSure + " ms. ";
        }

        [MethodImpl(MethodImplOptions.Synchronized)]
        public /*synchronized*/ String toString(String aMessage)
        {
            return aMessage + " --> " + toString();
        }
    }
}
