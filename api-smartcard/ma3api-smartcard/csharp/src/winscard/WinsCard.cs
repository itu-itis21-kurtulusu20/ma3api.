using System;
using System.Collections.Generic;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Text;
using log4net;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.winscard
{
    public struct SCard_ReaderState
    {
        [MarshalAs(UnmanagedType.LPWStr)]
        public string reader;
        public byte[] userData;
        public uint currentState;
        public uint eventState;
        public uint cbatr;
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 36)]
        public byte[] rgbAtr;

    }

    public class WinsCard
    {
        public static readonly char NULL_CHAR = (char)0;

        private static int SCARD_SHARE_EXCLUSIVE = 1;
        private static int SCARD_SHARE_SHARED = 2;

        private static int SCARD_PROTOCOL_T0 = 0x00000001;   // T=0 is the active protocol.
        private static int SCARD_PROTOCOL_T1 = 0x00000002;              // T=1 is the active protocol.


        private static uint SCARD_SCOPE_USER = 0;  // The context is a user context, and any
        // database operations are performed within the
        // domain of the user.
        private readonly int SCARD_SCOPE_TERMINAL = 1;   // The context is that of the current terminal,
        // and any database operations are performed
        // within the domain of that terminal.  (The
        // calling application must have appropriate
        // access permissions for any database actions.)
        private readonly int SCARD_SCOPE_SYSTEM = 2;    // The context is the system context, and any
        // database operations are performed within the
        // domain of the system.  (The calling
        // application must have appropriate access
        // permissions for any database actions.)

        private static int SCARD_S_SUCCESS = 0;

        private static int SCARD_ATTR_ICC_PRESENCE = 590592;

        private static long SCARD_W_REMOVED_CARD = 0x80100069L;
        private static long SCARD_E_INSUFFICIENT_BUFFER = 0x80100008L;


        private static int SCARD_RESET_CARD = 1;

        private static ASCIIEncoding ascii = new ASCIIEncoding();

        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public static String[] getTerminalList()
        {
            List<String> retTerminalList = new List<String>();
            string[] readerList = Smart.ListReaders();

            if (readerList == null)
                return new String[0];

            for (int i = 0; i < readerList.Length; i++)
            {
                string readerName = readerList[i];
                IntPtr scardhandle = Smart.Connect(readerName);
                SmartStatus cardStatus = Smart.GetStatus(scardhandle);
                if(cardStatus.ATR!=null)
                {
                  retTerminalList.Add(readerName);  
                }
                Smart.Disconnect(scardhandle);
            }
            return retTerminalList.ToArray();
          /*
      
            long ret = 0;
            IntPtr hContext = (IntPtr)0;
            int pcchReaders = 0;
            int nullindex = -1;

            List<String> terminalList = new List<String>();


            //establish context
            ret = SCardEstablishContext(SCARD_SCOPE_USER, 0, 0, ref hContext);
            if (ret != 0)
            {
                logger.Error("SCardEstablishContext Error: " + ret.ToString("X"));
                releaseContext(hContext);
                return terminalList.ToArray();
            }

            //get readers buffer len
            ret = SCardListReaders(hContext, null, null, ref pcchReaders);
            if (ret != 0)
            {
                logger.Error("SCardListReaders Error: " + ret.ToString("X"));
                releaseContext(hContext);
                return terminalList.ToArray();
            }

            byte[] mszReaders = new byte[pcchReaders];

            // fill readers' buffer
            ret = SCardListReaders(hContext, null, mszReaders, ref pcchReaders);
            if (ret != 0)
            {
                logger.Error("SCardListReaders Error: " + ret.ToString("X"));
                releaseContext(hContext);
                return terminalList.ToArray();
            }


            //fill readersList
            //remember that readers is a multistring with a double trailing \0
            // This is much easier and faster to do the allocation like this than the looping way
            //ASCIIEncoding asc = new ASCIIEncoding();
            //String[] Readers = asc.GetString( mszReaders ).Split( '\0' );

            string currbuff = ascii.GetString(mszReaders);
            int len = pcchReaders;

            while (currbuff[0] != NULL_CHAR)
            {
                nullindex = currbuff.IndexOf(NULL_CHAR);   //get null end character
                string reader = currbuff.Substring(0, nullindex);
                terminalList.Add(reader);
                len = len - (reader.Length + 1);
                currbuff = currbuff.Substring(nullindex + 1, len);
            }

            UIntPtr timeout = new UIntPtr(10);

            SCard_ReaderState[] stateList = new SCard_ReaderState[terminalList.Count()];
            for (int m = 0; m < terminalList.Count; m++)
            {
                String terminalName = terminalList[m];
                stateList[m].reader = terminalName;
            }
           
            uint deneme = SCardGetStatusChange(hContext, timeout, stateList, (uint)terminalList.Count());
            
            if(deneme == 0x0020)
                Console.WriteLine("card present");


            List<String> tempList = new List<String>(terminalList.ToArray());
            foreach (string terminal in tempList)
            {
                IntPtr session =(IntPtr) 0;
                int protocol = 0;
                byte[] readerBytes = ascii.GetBytes(terminal);
                int readerNameLen = ascii.GetBytes(terminal).Length;

                
                ret = SCardConnect(hContext, readerBytes, SCARD_SHARE_SHARED, SCARD_PROTOCOL_T0 | SCARD_PROTOCOL_T1,
                                   ref session, ref protocol);


                if (ret == SCARD_W_REMOVED_CARD)
                {
                    logger.Error("No card, terminal removed");
                    terminalList.Remove(terminal);
                }
                else if (ret != 0)
                {
                    logger.Error("SCardConnect Error: " + ret.ToString("X"));
                    terminalList.Remove(terminal);
                }

                ret = SCardDisconnect(session, SCARD_RESET_CARD);
                if (ret != 0)
                {
                    logger.Error("SCardDisconnect Error: " + ret.ToString("X"));
                }
            }

            releaseContext(hContext);
            return terminalList.ToArray();*/

        }


        public static byte[] getAtr(String terminalName)
        {
            IntPtr scardhandle = Smart.Connect(terminalName);
            SmartStatus cardStatus = Smart.GetStatus(scardhandle);
            byte[] atrBytes = cardStatus.ATR;
            Smart.Disconnect(scardhandle);
            return atrBytes;
            /*
            long ret = 0;
            IntPtr hContext = (IntPtr)0;

            //establish context
            ret = SCardEstablishContext(SCARD_SCOPE_USER, 0, 0, ref hContext);
            if (ret != 0)
            {
                logger.Error("SCardEstablishContext Error: " + ret.ToString("X"));
                releaseContext(hContext);
                throw new SmartCardException("Can not establish context.");
            }

            IntPtr session =(IntPtr) 0;
            int protocol = 0;
            byte[] readerBytes = getBytesWithNullEnd(terminalName);
            int readerBytesLen = readerBytes.Length;

            ret = SCardConnect(hContext, readerBytes, SCARD_SHARE_SHARED, SCARD_PROTOCOL_T0 | SCARD_PROTOCOL_T1,
                               ref session, ref protocol);

            if (ret != 0)
            {
                logger.Error("SCardConnect Error: " + ret.ToString("X"));
                releaseContext(hContext);
                throw new SmartCardException("Can not connect to card.");
            }

            byte[] atrBytes = new byte[0];
            int atrBytesLen = 0;
            int state = 0;

            ret = SCardStatus(session, readerBytes, ref readerBytesLen, ref state, ref protocol,
                atrBytes, ref atrBytesLen);

            if (ret != 0 && ret != SCARD_E_INSUFFICIENT_BUFFER)
            {
                logger.Error("SCardStatus Error: " + ret.ToString("X"));
                releaseContext(hContext);
                throw new SmartCardException("Can not get ATR from card.");
            }

            atrBytes = new byte[atrBytesLen];

            ret = SCardStatus(session, readerBytes, ref readerBytesLen, ref state, ref protocol,
                atrBytes, ref atrBytesLen);

            if (ret != 0)
            {
                logger.Error("SCardStatus Error: " + ret.ToString("X"));
                releaseContext(hContext);
                throw new SmartCardException("Can not get ATR from card.");
            }

            disconnect(session);
            releaseContext(hContext);

            return atrBytes;*/

        }

        private static byte[] getBytesWithNullEnd(string terminalName)
        {
            byte[] terminalBytes = ascii.GetBytes(terminalName);
            byte[] withNull = new byte[terminalBytes.Length + 2];
            Array.Copy(terminalBytes, 0, withNull, 0, terminalBytes.Length);
            withNull[terminalBytes.Length] = 0;
            withNull[terminalBytes.Length + 1] = 0;
            return withNull;
        }

        private static void disconnect(IntPtr session)
        {
            long ret = SCardDisconnect(session, SCARD_RESET_CARD);
            if (ret != 0)
            {
                logger.Error("SCardDisconnect Error: " + ret.ToString("X"));
            }
        }

        private static void releaseContext(IntPtr hContext)
        {
            long ret = SCardReleaseContext(hContext);
            if (ret != 0)
            {
                logger.Error("SCardReleaseContext Error: " + ret.ToString("X"));
            }
        }

        [DllImport("WinScard.dll")]
        public static extern uint SCardGetStatusChange(IntPtr phContext,
        UIntPtr timeout,
        [In,Out,MarshalAs(UnmanagedType.LPArray,SizeParamIndex = 3)]SCard_ReaderState[] states,
        UInt32 readers);

        [DllImport("WinScard.dll")]
        public static extern uint SCardEstablishContext(uint dwScope,
        int nNotUsed1,
        int nNotUsed2,
        ref IntPtr phContext);

        [DllImport("WinScard.dll")]
        public static extern uint SCardReleaseContext(IntPtr phContext);

        [DllImport("WinScard.dll", EntryPoint = "SCardConnect", CharSet = CharSet.Ansi)]
        public static extern uint SCardConnect(IntPtr hContext,
        byte[] cReaderName,
        int dwShareMode,
        int dwPrefProtocol,
        ref IntPtr phCard,
        ref int ActiveProtocol);

        [DllImport("WinScard.dll")]
        public static extern uint SCardDisconnect(IntPtr hCard, int Disposition);

        [DllImport("WinScard.dll")]
        public static extern uint SCardListReaderGroups(IntPtr hContext,
        ref string cGroups,
        ref int nStringSize);

        [DllImport("WinScard.dll")]
        public static extern uint SCardFreeMemory(IntPtr hContext,
        string cResourceToFree);

        [DllImport("WinScard.dll")]
        public static extern uint SCardGetAttrib(IntPtr hContext,
        int dwAttrId,
        ref byte[] bytRecvAttr,
        ref int nRecLen);

        [DllImport("winscard.dll", EntryPoint = "SCardListReaders", CharSet = CharSet.Ansi)]
        public static extern uint SCardListReaders(
          IntPtr hContext,
          byte[] mszGroups,
          byte[] mszReaders,
          ref int pcchReaders
          );

        [DllImport("winscard.dll")]
        public static extern uint SCardStatus(IntPtr hCard,
            byte[] mszReaders,
            ref int pcchReaderLen,
            ref int pdwState,
            ref int pdwProtocol,
            byte[] pbAtr,
            ref int pcbAtrLen);
    }
}
