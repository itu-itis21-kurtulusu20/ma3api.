//Thomas Bellenger - PCSC Smartcard Library for Mono/C# 
 
using System;
using System.Reflection;
using System.Text;
using log4net;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.winscard 
{ 
    public class Smart
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public static event ConnectionEvent Connected;
        public static event ConnectionEvent Disconnected;
        public delegate void ConnectionEvent(string readerName);

        static IntPtr ctx;
        static SCardProtocol currentProtocol = SCardProtocol.SCARD_PROTOCOL_T0;

        static Smart()
        {
            init();
        }

        static void init()
        {
            if ((uint)SCardError.SCARD_S_SUCCESS != SCard.SCardEstablishContext(SCardScope.SCARD_SCOPE_SYSTEM, IntPtr.Zero, IntPtr.Zero, ref ctx))
            {
                throw new Exception("Unable to establish smart card context");
            }
        }

        /// <summary> 
        /// Gets the smartcard context handle 
        /// </summary> 
        public static IntPtr Context 
        { 
            get 
            { 
                return ctx; 
            } 
        }

       
 
        /// <summary> 
        /// Gives a listing of all the smartcard readers registered with the PCSC smartcard manager 
        /// </summary> 
        /// <returns>A string array containing the reader names</returns> 
        public static String[] ListReaders() 
        { 
            String[] readers; 
            int buffLen = 512; 
            byte[] buff = new byte[buffLen];

            uint scardResult = SCard.SCardListReaders(ctx, null, buff, ref buffLen);
            if ((uint) SCardError.SCARD_E_SERVICE_STOPPED == scardResult)
            { 
                logger.Error("SCard.SCardListReaders Failed: 0x" + scardResult.ToString("X") + ". Retrying");

                //Release Context and init.
                SCard.SCardReleaseContext(ctx);
                ctx = IntPtr.Zero;
                init();

                uint secondResult = SCard.SCardListReaders(ctx, null, buff, ref buffLen);
                if ((uint)SCardError.SCARD_S_SUCCESS == secondResult)
                {
                    scardResult = secondResult;
                }
                else
                {
                    logger.Error("SCard.SCardListReaders Second Fail: 0x" + scardResult.ToString("X"));
                    return null;
                }
            }
            else if ((uint)SCardError.SCARD_S_SUCCESS != scardResult)
            {
                return null;
            }


            //SCardListReaders is SCARD_S_SUCCESS.
            int count = 0; 
            for (int i = 0; i < buffLen - 1; i++) { 
                if (buff[i] == 0x00) { 
                    count++; 
                } 
            } 
            readers = new String[count]; 
            for (int i = 0, offset = 0; i < count; i++) { 
                readers[i] = byteArrayToString(buff,offset); 
                offset += readers[i].Length + 1; 
            } 
            return readers;
        } 
 
        private static String byteArrayToString(byte[] array, int offset) 
        { 
            StringBuilder sb = new StringBuilder(); 
            for (int i = offset; i < array.Length; i++) 
            { 
                if (array[i] == 0x00) 
                { 
                    break; 
                } 
                else 
                { 
                    sb.Append(String.Format("{0}",(char)array[i])); 
                } 
            } 
            return sb.ToString(); 
        } 
 
        /// <summary> 
        /// Connect to a reader using the reader name 
        /// </summary> 
        /// <param name="readerName">The name of the reader to connect. Use ListReaders to return a listing of valid reader names.</param> 
        /// <returns>The handle to the smartcard reader or zero on error. 
        /// </returns> 
        public static IntPtr Connect(String readerName) 
        { 
            return Connect(readerName, SCardShare.SCARD_SHARE_SHARED, SCardProtocol.SCARD_PROTOCOL_T0 | SCardProtocol.SCARD_PROTOCOL_T1); 
        }

        public static IntPtr Connect(String readerName, SCardShare access) 
        { 
            return Connect(readerName, access, SCardProtocol.SCARD_PROTOCOL_T0 | SCardProtocol.SCARD_PROTOCOL_T1); 
        }

        public static IntPtr Connect(String readerName, SCardShare access, SCardProtocol protocol) 
        { 
            uint ret; 
            int proto = 1;
            IntPtr hCard=(IntPtr)0; 
            ret = SCard.SCardConnect(ctx, readerName, access, protocol, ref hCard, ref proto); 
            currentProtocol = (SCardProtocol)proto; 
            if (ret == (uint)SCardError.SCARD_S_SUCCESS) 
            { 
                if (Connected != null) { Connected(readerName); } 
                return hCard; 
            } 
            else 
            { 
                return (IntPtr)0; 
            } 
        } 
 
        public static uint Reconnect(int hCard) 
        { 
            return Reconnect(hCard, SCardShare.SCARD_SHARE_SHARED, SCardProtocol.SCARD_PROTOCOL_T0 | SCardProtocol.SCARD_PROTOCOL_T1); 
        } 
        public static uint Reconnect(int hCard, SCardShare access) 
        { 
            return Reconnect(hCard, access, SCardProtocol.SCARD_PROTOCOL_T0 | SCardProtocol.SCARD_PROTOCOL_T1); 
        } 
        public static uint Reconnect(int hCard, SCardShare access, SCardProtocol protocol) 
        { 
            uint ret; 
            int proto = 1; 
            ret = SCard.SCardReconnect(ref hCard, access, protocol, SCardDisposition.UNPOWER_CARD, ref proto); 
            currentProtocol = (SCardProtocol)proto; 
            return ret; 
        } 
 
        /// <summary> 
        /// Disconnects a previously connected smartcard reader 
        /// </summary> 
        /// <param name="hCard">Handle of a previously connected smartcard reader</param> 
        public static void Disconnect(IntPtr hCard) 
        { 
            SCard.SCardDisconnect(hCard, SCardDisposition.LEAVE_CARD); 
            if (Disconnected != null) { Disconnected(""); } 
        }     
 
        /// <summary> 
        /// Gets the ATR of the card 
        /// </summary> 
        /// <param name="hCard">The handle to the card reader</param> 
        /// <returns>SMART_STATUS struct or null on error</returns> 
        public static SmartStatus GetStatus(IntPtr hCard) { 
            SmartStatus status = new SmartStatus(); 
            if (hCard==(IntPtr)0) { 
                return status; 
            } 
 
            byte[] szReaderName = new byte[256]; 
            int pcchReaderLen = 256; 
            int pdwState = 0; 
            int pdwProtocol = 0; 
            byte[] pbAtr = new byte[32]; 
            int pcbAtrLen = 32; 
 
            uint ret = SCard.SCardStatus(hCard, szReaderName, ref pcchReaderLen, ref pdwState, ref pdwProtocol, pbAtr, ref pcbAtrLen); 
 
            if (ret == (uint)SCardError.SCARD_S_SUCCESS) { 
 
                status.ReaderName = HexUtil.HexToAscii(HexUtil.BinToHex(szReaderName, 0, pcchReaderLen-2)); 
                status.ReaderState = (SCardReaderState)pdwState; 
                status.ReaderProtocol = (SCardProtocol)pdwProtocol; 
                status.ATR = new byte[pcbAtrLen]; 
                System.Array.Copy(pbAtr, status.ATR, pcbAtrLen); 
            } 
            return status; 
        } 
 
        /// <summary> 
        /// Resets a card 
        /// </summary> 
        /// <param name="hCard">handle to the card to be reset</param> 
        /// <returns>true if successful</returns> 
        public static bool Reset(int hCard) 
        { 
            int proto = 1; 
            uint ret = SCard.SCardReconnect(ref hCard, SCardShare.SCARD_SHARE_SHARED, SCardProtocol.SCARD_PROTOCOL_T0, SCardDisposition.RESET_CARD, ref proto); 
            return (ret == (uint)SCardError.SCARD_S_SUCCESS); 
        } 
 
        /// <summary> 
        /// Detects a change in status of a card reader within a defined timeout 
        /// </summary> 
        /// <param name="readerName">Reader name to detect status</param> 
        /// <param name="timeout">Timeout in ms</param> 
        /// <param name="currentState">The current state of the reader. If not know then send SCardState.STATE_UNAWARE which returns without waiting to indicate the current state</param> 
        /// <returns>The new state of the reader</returns> 
        public static SCardState GetStatusChange(String readerName, int timeout, SCardState currentState) 
        { 
            SCARD_READERSTATE[] readerState = new SCARD_READERSTATE[1]; 
            readerState[0].szReader = readerName; 
            readerState[0].dwCurrentState = currentState; 
            uint ret = SCard.SCardGetStatusChange(ctx, timeout, readerState, readerState.Length); 
            if (ret == (uint)SCardError.SCARD_S_SUCCESS) 
            { 
                return readerState[0].dwEventState; 
            } 
            else 
            { 
                return SCardState.SCARD_STATE_UNKNOWN; 
            } 
        }      
 
        ~Smart() 
        { 
            SCard.SCardReleaseContext(ctx);
        } 
    } 
 
    public struct SmartStatus { 
        public String ReaderName; 
        public SCardReaderState ReaderState; 
        public SCardProtocol ReaderProtocol; 
        public byte[] ATR; 
    } 
} 