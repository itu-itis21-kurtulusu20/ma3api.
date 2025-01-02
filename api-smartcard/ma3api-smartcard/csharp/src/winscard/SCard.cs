//Thomas Bellenger - PCSC Smartcard Library for Mono/C# 

using System; 
using System.Runtime.InteropServices;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.winscard 
{ 
 
	public static class SCard 
	{ 
        /// <summary> 
        /// Wrapper on the SCardEstablishContext function from the  
        /// PC/SC library (winscard.dll or libpcsclite.so.1). More SCard functions can be wrapped as needed. 
        /// </summary> 
        /// <param name="dwScope">Scope of context</param> 
        /// <param name="pvReserved1">RFU</param> 
        /// <param name="pvReserved2">RFU</param> 
        /// <param name="phContext">handle to the context</param> 
        /// <returns>SCARD_S_SUCCESS or a PCSC error code</returns> 
        [DllImport("winscard")] 
        public static extern uint SCardEstablishContext( 
			SCardScope dwScope, 
			IntPtr pvReserved1, 
			IntPtr pvReserved2, 
			ref IntPtr phContext); 
		 
        /// <summary> 
        /// Release smartcard context 
        /// </summary> 
        /// <param name="hContext">The handle to the smartcard context</param> 
        /// <returns>SCARD_S_SUCCESS or a PCSC error code</returns> 
		[DllImport("winscard")] 
        public static extern uint SCardReleaseContext(IntPtr hContext); 
		 
        /// <summary> 
        /// List the readers registered with the PCSC smartcard manager 
        /// </summary> 
        /// <param name="hContext">The handle to the smartcard context</param> 
        /// <param name="mszGroups">null</param> 
        /// <param name="mszReaders">A byte array to contain the returned reader list 
        /// . This is a multi-string i.e it is a null terminated list of null terminated strings</param> 
        /// <param name="pcchReaders">The size of the byte array - memory should be assigned by the caller</param> 
        /// <returns>SCARD_S_SUCCESS or a PCSC error code</returns> 
		[DllImport("winscard")] 
        public static extern uint SCardListReaders( 
			IntPtr hContext, 
			byte[] mszGroups, 
			byte[] mszReaders, 
			ref int pcchReaders); 
		 
        /// <summary> 
        ///  
        /// </summary> 
        /// <param name="hContext"></param> 
        /// <param name="szReader"></param> 
        /// <param name="dwShareMode"></param> 
        /// <param name="dwPreferredProtocols"></param> 
        /// <param name="phCard"></param> 
        /// <param name="pdwActiveProtocol"></param> 
        /// <returns>SCARD_S_SUCCESS or a PCSC error code</returns> 
		[DllImport("winscard")] 
		public static extern uint SCardConnect( 
			IntPtr hContext, 
			[MarshalAs(UnmanagedType.LPStr)] string szReader, 
			SCardShare dwShareMode, 
			SCardProtocol dwPreferredProtocols,
            ref IntPtr phCard, 
			ref int pdwActiveProtocol); 
 
        /// <summary> 
        ///  
        /// </summary> 
        /// <param name="hCard"></param> 
        /// <param name="dwShareMode"></param> 
        /// <param name="dwPreferredProtocols"></param> 
        /// <param name="dwInitialization"></param> 
        /// <param name="pdwActiveProtocol"></param> 
        /// <returns>SCARD_S_SUCCESS or a PCSC error code</returns> 
        [DllImport("winscard")] 
        public static extern uint SCardReconnect( 
            ref int phCard, 
            SCardShare dwShareMode, 
            SCardProtocol dwPreferredProtocols, 
            SCardDisposition dwInitialization, 
            ref int pdwActiveProtocol); 
		 
        /// <summary> 
        ///  
        /// </summary> 
        /// <param name="hCard"></param> 
        /// <param name="dwDisposition"></param> 
        /// <returns>SCARD_S_SUCCESS or a PCSC error code</returns> 
		[DllImport("winscard")] 
		public static extern uint SCardDisconnect(
            IntPtr hCard, 
			SCardDisposition dwDisposition); 
		 
        /// <summary> 
        ///  
        /// </summary> 
        /// <param name="hCard"></param> 
        /// <param name="pioSendPci"></param> 
        /// <param name="pbSendBuffer"></param> 
        /// <param name="cbSendBuffer"></param> 
        /// <param name="pioRecvPci"></param> 
        /// <param name="pbRecvBuffer"></param> 
        /// <param name="pcbRecvLength"></param> 
        /// <returns>SCARD_S_SUCCESS or a PCSC error code</returns> 
		[DllImport("winscard")] 
		public static extern uint SCardTransmit( 
			int hCard, 
			ref SCARD_IO_REQUEST pioSendPci, 
			byte[] pbSendBuffer, 
			int cbSendBuffer, 
            ref SCARD_IO_REQUEST pioRecvPci, 
			byte[] pbRecvBuffer, 
			ref int pcbRecvLength); 
		 
        /// <summary> 
        ///  
        /// </summary> 
        /// <param name="hCard"></param> 
        /// <param name="szReaderName"></param> 
        /// <param name="pcchReaderLen"></param> 
        /// <param name="pdwState"></param> 
        /// <param name="pdwProtocol"></param> 
        /// <param name="pbAtr"></param> 
        /// <param name="pcbAtrLen"></param> 
        /// <returns>SCARD_S_SUCCESS or a PCSC error code</returns> 
		[DllImport("winscard", CharSet=CharSet.Ansi)] 
        public static extern uint SCardStatus( 
            IntPtr hCard, 
            byte[] szReaderName,  
            ref int pcchReaderLen,  
            ref int pdwState,  
            ref int pdwProtocol,  
            byte[] pbAtr,  
            ref int pcbAtrLen); 
 
        /// <summary> 
        ///  
        /// </summary> 
        /// <param name="hContext"></param> 
        /// <param name="dwTimeout"></param> 
        /// <param name="rgReaderStates"></param> 
        /// <param name="cReaders"></param> 
        /// <returns>SCARD_S_SUCCESS or a PCSC error code</returns> 
        [DllImport("winscard")] 
        public static extern uint SCardGetStatusChange(IntPtr hContext,  
            int dwTimeout,  
            [In, Out] SCARD_READERSTATE[] rgReaderStates,  
            int cReaders); 
 
        /// <summary> 
        ///  
        /// </summary> 
        /// <param name="hContext"></param> 
        /// <returns></returns> 
        [DllImport("winscard")] 
        public static extern uint SCardCancel(int hContext); 
 
         
	} 
 
    /// <summary> 
    /// A structure containing the reader state. Used by SCardGetStatusChange. 
    /// </summary> 
    [StructLayout(LayoutKind.Sequential)] 
    public struct SCARD_READERSTATE 
    { 
        /// <summary> 
        /// Reader 
        /// </summary> 
        [MarshalAs(UnmanagedType.LPStr)] 
        public string szReader; 
        /// <summary> 
        /// User Data 
        /// </summary> 
        public IntPtr pvUserData; 
        /// <summary> 
        /// Current State 
        /// </summary> 
        public SCardState dwCurrentState; 
        /// <summary> 
        /// Event State/ New State 
        /// </summary> 
        public SCardState dwEventState; 
        /// <summary> 
        /// ATR Length 
        /// </summary> 
        public int cbAtr; 
        /// <summary> 
        /// Card ATR 
        /// </summary> 
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 36)] 
        public byte[] rgbAtr; 
    } 
 
    /// <summary> 
    ///  
    /// </summary> 
    [StructLayout(LayoutKind.Sequential)] 
    public struct SCARD_IO_REQUEST 
    { 
        public SCardProtocol dwProtocol; 
        public int cbPciLength; 
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = 256)] 
        public byte[] buffer; 
    } 
 
    /// <summary> 
    ///  
    /// </summary> 
    public enum SCardError : uint 
    { 
        SCARD_S_SUCCESS = 0, 
        // 
        // MessageId: SCARD_F_INTERNAL_ERROR 
        // 
        // MessageText: 
        // 
        //  An internal consistency check failed. 
        // 
        SCARD_F_INTERNAL_ERROR = 0x80100001, 
 
        // 
        // MessageId: SCARD_E_CANCELLED 
        // 
        // MessageText: 
        // 
        //  The action was cancelled by an SCardCancel request. 
        // 
        SCARD_E_CANCELLED = 0x80100002, 
 
        // 
        // MessageId: SCARD_E_INVALID_HANDLE 
        // 
        // MessageText: 
        // 
        //  The supplied handle was invalid. 
        // 
        SCARD_E_INVALID_HANDLE = 0x80100003, 
 
        // 
        // MessageId: SCARD_E_INVALID_PARAMETER 
        // 
        // MessageText: 
        // 
        //  One or more of the supplied parameters could not be properly interpreted. 
        // 
        SCARD_E_INVALID_PARAMETER = 0x80100004, 
 
        // 
        // MessageId: SCARD_E_INVALID_TARGET 
        // 
        // MessageText: 
        // 
        //  Registry startup information is missing or invalid. 
        // 
        SCARD_E_INVALID_TARGET = 0x80100005, 
 
        // 
        // MessageId: SCARD_E_NO_MEMORY 
        // 
        // MessageText: 
        // 
        //  Not enough memory available to complete this command. 
        // 
        SCARD_E_NO_MEMORY = 0x80100006, 
 
        // 
        // MessageId: SCARD_F_WAITED_TOO_LONG 
        // 
        // MessageText: 
        // 
        //  An internal consistency timer has expired. 
        // 
        SCARD_F_WAITED_TOO_LONG = 0x80100007, 
 
        // 
        // MessageId: SCARD_E_INSUFFICIENT_BUFFER 
        // 
        // MessageText: 
        // 
        //  The data buffer to receive returned data is too small for the returned data. 
        // 
        SCARD_E_INSUFFICIENT_BUFFER = 0x80100008, 
 
        // 
        // MessageId: SCARD_E_UNKNOWN_READER 
        // 
        // MessageText: 
        // 
        //  The specified reader name is not recognized. 
        // 
        SCARD_E_UNKNOWN_READER = 0x80100009, 
 
        // 
        // MessageId: SCARD_E_TIMEOUT 
        // 
        // MessageText: 
        // 
        //  The user-specified timeout value has expired. 
        // 
        SCARD_E_TIMEOUT = 0x8010000A, 
 
        // 
        // MessageId: SCARD_E_SHARING_VIOLATION 
        // 
        // MessageText: 
        // 
        //  The smart card cannot be accessed because of other connections outstanding. 
        // 
        SCARD_E_SHARING_VIOLATION = 0x8010000B, 
 
        // 
        // MessageId: SCARD_E_NO_SMARTCARD 
        // 
        // MessageText: 
        // 
        //  The operation requires a Smart Card, but no Smart Card is currently in the device. 
        // 
        SCARD_E_NO_SMARTCARD = 0x8010000C, 
 
        // 
        // MessageId: SCARD_E_UNKNOWN_CARD 
        // 
        // MessageText: 
        // 
        //  The specified smart card name is not recognized. 
        // 
        SCARD_E_UNKNOWN_CARD = 0x8010000D, 
 
        // 
        // MessageId: SCARD_E_CANT_DISPOSE 
        // 
        // MessageText: 
        // 
        //  The system could not dispose of the media in the requested manner. 
        // 
        SCARD_E_CANT_DISPOSE = 0x8010000E, 
 
        // 
        // MessageId: SCARD_E_PROTO_MISMATCH 
        // 
        // MessageText: 
        // 
        //  The requested protocols are incompatible with the protocol currently in use with the smart card. 
        // 
        SCARD_E_PROTO_MISMATCH = 0x8010000F, 
 
        // 
        // MessageId: SCARD_E_NOT_READY 
        // 
        // MessageText: 
        // 
        //  The reader or smart card is not ready to accept commands. 
        // 
        SCARD_E_NOT_READY = 0x80100010, 
 
        // 
        // MessageId: SCARD_E_INVALID_VALUE 
        // 
        // MessageText: 
        // 
        //  One or more of the supplied parameters values could not be properly interpreted. 
        // 
        SCARD_E_INVALID_VALUE = 0x80100011, 
 
        // 
        // MessageId: SCARD_E_SYSTEM_CANCELLED 
        // 
        // MessageText: 
        // 
        //  The action was cancelled by the system, presumably to log off or shut down. 
        // 
        SCARD_E_SYSTEM_CANCELLED = 0x80100012, 
 
        // 
        // MessageId: SCARD_F_COMM_ERROR 
        // 
        // MessageText: 
        // 
        //  An internal communications error has been detected. 
        // 
        SCARD_F_COMM_ERROR = 0x80100013, 
 
        // 
        // MessageId: SCARD_F_UNKNOWN_ERROR 
        // 
        // MessageText: 
        // 
        //  An internal error has been detected, but the source is unknown. 
        // 
        SCARD_F_UNKNOWN_ERROR = 0x80100014, 
 
        // 
        // MessageId: SCARD_E_INVALID_ATR 
        // 
        // MessageText: 
        // 
        //  An ATR obtained from the registry is not a valid ATR string. 
        // 
        SCARD_E_INVALID_ATR = 0x80100015, 
 
        // 
        // MessageId: SCARD_E_NOT_TRANSACTED 
        // 
        // MessageText: 
        // 
        //  An attempt was made to end a non-existent transaction. 
        // 
        SCARD_E_NOT_TRANSACTED = 0x80100016, 
 
        // 
        // MessageId: SCARD_E_READER_UNAVAILABLE 
        // 
        // MessageText: 
        // 
        //  The specified reader is not currently available for use. 
        // 
        SCARD_E_READER_UNAVAILABLE = 0x80100017, 
 
        // 
        // MessageId: SCARD_P_SHUTDOWN 
        // 
        // MessageText: 
        // 
        //  The operation has been aborted to allow the server application to exit. 
        // 
        SCARD_P_SHUTDOWN = 0x80100018, 
 
        // 
        // MessageId: SCARD_E_PCI_TOO_SMALL 
        // 
        // MessageText: 
        // 
        //  The PCI Receive buffer was too small. 
        // 
        SCARD_E_PCI_TOO_SMALL = 0x80100019, 
 
        // 
        // MessageId: SCARD_E_READER_UNSUPPORTED 
        // 
        // MessageText: 
        // 
        //  The reader driver does not meet minimal requirements for support. 
        // 
        SCARD_E_READER_UNSUPPORTED = 0x8010001A, 
 
        // 
        // MessageId: SCARD_E_DUPLICATE_READER 
        // 
        // MessageText: 
        // 
        //  The reader driver did not produce a unique reader name. 
        // 
        SCARD_E_DUPLICATE_READER = 0x8010001B, 
 
        // 
        // MessageId: SCARD_E_CARD_UNSUPPORTED 
        // 
        // MessageText: 
        // 
        //  The smart card does not meet minimal requirements for support. 
        // 
        SCARD_E_CARD_UNSUPPORTED = 0x8010001C, 
 
        // 
        // MessageId: SCARD_E_NO_SERVICE 
        // 
        // MessageText: 
        // 
        //  The Smart card resource manager is not running. 
        // 
        SCARD_E_NO_SERVICE = 0x8010001D, 
 
        // 
        // MessageId: SCARD_E_SERVICE_STOPPED 
        // 
        // MessageText: 
        // 
        //  The Smart card resource manager has shut down. 
        // 
        SCARD_E_SERVICE_STOPPED = 0x8010001E, 
 
        // 
        // MessageId: SCARD_E_UNEXPECTED 
        // 
        // MessageText: 
        // 
        //  An unexpected card error has occurred. 
        // 
        SCARD_E_UNEXPECTED = 0x8010001F, 
 
        // 
        // MessageId: SCARD_E_ICC_INSTALLATION 
        // 
        // MessageText: 
        // 
        //  No Primary Provider can be found for the smart card. 
        // 
        SCARD_E_ICC_INSTALLATION = 0x80100020, 
 
        // 
        // MessageId: SCARD_E_ICC_CREATEORDER 
        // 
        // MessageText: 
        // 
        //  The requested order of object creation is not supported. 
        // 
        SCARD_E_ICC_CREATEORDER = 0x80100021, 
 
        // 
        // MessageId: SCARD_E_UNSUPPORTED_FEATURE 
        // 
        // MessageText: 
        // 
        //  This smart card does not support the requested feature. 
        // 
        SCARD_E_UNSUPPORTED_FEATURE = 0x80100022, 
 
        // 
        // MessageId: SCARD_E_DIR_NOT_FOUND 
        // 
        // MessageText: 
        // 
        //  The identified directory does not exist in the smart card. 
        // 
        SCARD_E_DIR_NOT_FOUND = 0x80100023, 
 
        // 
        // MessageId: SCARD_E_FILE_NOT_FOUND 
        // 
        // MessageText: 
        // 
        //  The identified file does not exist in the smart card. 
        // 
        SCARD_E_FILE_NOT_FOUND = 0x80100024, 
 
        // 
        // MessageId: SCARD_E_NO_DIR 
        // 
        // MessageText: 
        // 
        //  The supplied path does not represent a smart card directory. 
        // 
        SCARD_E_NO_DIR = 0x80100025, 
 
        // 
        // MessageId: SCARD_E_NO_FIE 
        // 
        // MessageText: 
        // 
        //  The supplied path does not represent a smart card file. 
        // 
        SCARD_E_NO_FILE = 0x80100026, 
 
        // 
        // MessageId: SCARD_E_NO_ACCESS 
        // 
        // MessageText: 
        // 
        //  Access is denied to this file. 
        // 
        SCARD_E_NO_ACCESS = 0x80100027, 
 
        // 
        // MessageId: SCARD_E_WRITE_TOO_MANY 
        // 
        // MessageText: 
        // 
        //  The smartcard does not have enough memory to store the information. 
        // 
        SCARD_E_WRITE_TOO_MANY = 0x80100028, 
 
        // 
        // MessageId: SCARD_E_BAD_SEEK 
        // 
        // MessageText: 
        // 
        //  There was an error trying to set the smart card file object pointer. 
        // 
        SCARD_E_BAD_SEEK = 0x80100029, 
 
        // 
        // MessageId: SCARD_E_INVALID_CHV 
        // 
        // MessageText: 
        // 
        //  The supplied PIN is incorrect. 
        // 
        SCARD_E_INVALID_CHV = 0x8010002A, 
 
        // 
        // MessageId: SCARD_E_UNKNOWN_RES_MNG 
        // 
        // MessageText: 
        // 
        //  An unrecognized error code was returned from a layered component. 
        // 
        SCARD_E_UNKNOWN_RES_MNG = 0x8010002B, 
 
        // 
        // MessageId: SCARD_E_NO_SUCH_CERTIFICATE 
        // 
        // MessageText: 
        // 
        //  The requested certificate does not exist. 
        // 
        SCARD_E_NO_SUCH_CERTIFICATE = 0x8010002C, 
 
        // 
        // MessageId: SCARD_E_CERTIFICATE_UNAVAILABLE 
        // 
        // MessageText: 
        // 
        //  The requested certificate could not be obtained. 
        // 
        SCARD_E_CERTIFICATE_UNAVAILABLE = 0x8010002D, 
 
        // 
        // MessageId: SCARD_E_NO_READERS_AVAILABLE 
        // 
        // MessageText: 
        // 
        //  Cannot find a smart card reader. 
        // 
        SCARD_E_NO_READERS_AVAILABLE = 0x8010002E, 
 
        // 
        // MessageId: SCARD_E_COMM_DATA_LOST 
        // 
        // MessageText: 
        // 
        //  A communications error with the smart card has been detected.  Retry the operation. 
        // 
        SCARD_E_COMM_DATA_LOST = 0x8010002F, 
 
        // 
        // MessageId: SCARD_E_NO_KEY_CONTAINER 
        // 
        // MessageText: 
        // 
        //  The requested key container does not exist on the smart card. 
        // 
        SCARD_E_NO_KEY_CONTAINER = 0x80100030, 
 
        // 
        // MessageId: SCARD_E_SERVER_TOO_BUSY 
        // 
        // MessageText: 
        // 
        //  The Smart card resource manager is too busy to complete this operation. 
        // 
        SCARD_E_SERVER_TOO_BUSY = 0x80100031, 
 
        // 
        // These are warning codes. 
        // 
        // 
        // MessageId: SCARD_W_UNSUPPORTED_CARD 
        // 
        // MessageText: 
        // 
        //  The reader cannot communicate with the smart card, due to ATR configuration conflicts. 
        // 
        SCARD_W_UNSUPPORTED_CARD = 0x80100065, 
 
        // 
        // MessageId: SCARD_W_UNRESPONSIVE_CARD 
        // 
        // MessageText: 
        // 
        //  The smart card is not responding to a reset. 
        // 
        SCARD_W_UNRESPONSIVE_CARD = 0x80100066, 
 
        // 
        // MessageId: SCARD_W_UNPOWERED_CARD 
        // 
        // MessageText: 
        // 
        //  Power has been removed from the smart card, so that further communication is not possible. 
        // 
        SCARD_W_UNPOWERED_CARD = 0x80100067, 
 
        // 
        // MessageId: SCARD_W_RESET_CARD 
        // 
        // MessageText: 
        // 
        //  The smart card has been reset, so any shared state information is invalid. 
        // 
        SCARD_W_RESET_CARD = 0x80100068, 
 
        // 
        // MessageId: SCARD_W_REMOVED_CARD 
        // 
        // MessageText: 
        // 
        //  The smart card has been removed, so that further communication is not possible. 
        // 
        SCARD_W_REMOVED_CARD = 0x80100069, 
 
        // 
        // MessageId: SCARD_W_SECURITY_VIOLATION 
        // 
        // MessageText: 
        // 
        //  Access was denied because of a security violation. 
        // 
        SCARD_W_SECURITY_VIOLATION = 0x8010006A, 
 
        // 
        // MessageId: SCARD_W_WRONG_CHV 
        // 
        // MessageText: 
        // 
        //  The card cannot be accessed because the wrong PIN was presented. 
        // 
        SCARD_W_WRONG_CHV = 0x8010006B, 
 
        // 
        // MessageId: SCARD_W_CHV_BLOCKED 
        // 
        // MessageText: 
        // 
        //  The card cannot be accessed because the maximum number of PIN entry attempts has been reached. 
        // 
        SCARD_W_CHV_BLOCKED = 0x8010006C, 
 
        // 
        // MessageId: SCARD_W_EOF 
        // 
        // MessageText: 
        // 
        //  The end of the smart card file has been reached. 
        // 
        SCARD_W_EOF = 0x8010006D, 
 
        // 
        // MessageId: SCARD_W_CANCELLED_BY_USER 
        // 
        // MessageText: 
        // 
        //  The action was cancelled by the user. 
        // 
        SCARD_W_CANCELLED_BY_USER = 0x8010006E, 
 
        // 
        // MessageId: SCARD_W_CARD_NOT_AUTHENTICATED 
        // 
        // MessageText: 
        // 
        //  No PIN was presented to the smart card. 
        // 
        SCARD_W_CARD_NOT_AUTHENTICATED = 0x8010006F 
    } 
 
    /// <summary> 
    ///  
    /// </summary> 
    public enum SCardScope 
    { 
        SCARD_SCOPE_USER = 0, 
        SCARD_SCOPE_TERMINAL = 1, 
        SCARD_SCOPE_SYSTEM = 2 
    } 
 
    /// <summary> 
    ///  
    /// </summary> 
    public enum SCardShare 
    { 
        SCARD_SHARE_EXCLUSIVE = 0x00000001, 
        SCARD_SHARE_SHARED = 0x00000002, 
        SCARD_SHARE_DIRECT = 0x00000003 
    } 
 
    /// <summary> 
    ///  
    /// </summary> 
    public enum SCardProtocol 
    { 
        SCARD_PROTOCOL_T0 = 0x00000001, 
        SCARD_PROTOCOL_T1 = 0x00000002, 
        SCARD_PROTOCOL_RAW = 0x00010000 
    } 
 
    /// <summary> 
    ///  
    /// </summary> 
    public enum SCardDisposition 
    { 
        LEAVE_CARD = 0, 
        RESET_CARD = 1, 
        UNPOWER_CARD = 2, 
        EJECT_CARD = 3 
    } 
 
    public enum SCardState 
    { 
        SCARD_STATE_UNAWARE = 0x00000000, 
        SCARD_STATE_IGNORE = 0x00000001, 
        SCARD_STATE_CHANGED = 0x00000002, 
        SCARD_STATE_UNKNOWN = 0x00000004, 
        SCARD_STATE_UNAVAILABLE = 0x00000008, 
        SCARD_STATE_EMPTY = 0x00000010, 
        SCARD_STATE_PRESENT = 0x00000020, 
        SCARD_STATE_ATRMATCH = 0x00000040, 
        SCARD_STATE_EXCLUSIVE = 0x00000080, 
        SCARD_STATE_INUSE = 0x00000100, 
        SCARD_STATE_MUTE = 0x00000200, 
        SCARD_STATE_UNPOWERED = 0x00000400 
    } 
 
    public enum SCardReaderState : int { 
        SCARD_UNKNOWN = 0,   // This value implies the driver is unaware 
        // of the current state of the reader. 
        SCARD_ABSENT = 1,   // This value implies there is no card in 
        // the reader. 
        SCARD_PRESENT = 2,  // This value implies there is a card is 
        // present in the reader, but that it has 
        // not been moved into position for use. 
        SCARD_SWALLOWED = 3,   // This value implies there is a card in the 
        // reader in position for use.  The card is 
        // not powered. 
        SCARD_POWERED = 4,  // This value implies there is power is 
        // being provided to the card, but the 
        // Reader Driver is unaware of the mode of 
        // the card. 
        SCARD_NEGOTIABLE = 5,   // This value implies the card has been 
        // reset and is awaiting PTS negotiation. 
        SCARD_SPECIFIC = 6,  // This value implies the card has been 
        // reset and specific communication 
        // protocols have been established. 
    } 
}