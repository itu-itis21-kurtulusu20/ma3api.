
package com.scdroid.smartcard.protocol;

/** <tt>T1BlockFactory</tt>
 *
 * creates T1Block-objects - for detailed informations see ISO-7816-3.
 *
 * @author  Stephan Breideneich (sbreiden@de.ibm.com)
 * @version $Id: T1BlockFactory.java,v 1.1.1.1 1999/10/05 15:08:48 damke Exp $
 *
 */
public class T1BlockFactory {

  /** <tt>createIBlock</tt>
   *
   * creates I-Block according to ISO7816-3
   *
   * @param sourceID
   *        the sourceID of the frame for the NAD-byte (0-7)
   * @param destID
   *        the destID of the frame for the NAD-byte (0-7)
   * @param edcAlg
   *        the used algorithm for calculation of the EDC byte
   *        possible values: T1Block.USE_LDR, T1Block.USE_CRC
   * @param sendSequenceNumber
   *        the sequence number of the frame
   *        internal used value is (sendSequenceNumber mod 2)
   * @param moreDataBit
   *        true for chained block (not implemented), false for normal operation
   * @param data
   *        application data
   * @see   T1Block
   */
  public static T1Block createIBlock(int sourceID, int destID, int edcAlg, 
                                     int sendSequenceNumber, boolean moreDataBit, byte[] data) 
                                     throws T1Exception {

    // set sendSequenceBit
    int pcb = 0xFF & ((sendSequenceNumber % 2) << 6);

    // set moreDataBit
    if (moreDataBit)
      pcb = 0xFF & (pcb | 0x20);

    // create I-Block
    return new T1Block(sourceID, destID, pcb, data, edcAlg);
  }

  /** <tt>createRBlock</tt>
   *
   * creates R-Block according to ISO7816-3
   *
   * @param sourceID
   *        the sourceID of the frame for the NAD-byte (0-7)
   * @param destID
   *        the destID of the frame for the NAD-byte (0-7)
   * @param edcAlg
   *        the used algorithm for calculation of the EDC byte
   *        possible values: EDC_LDR, EDC_CRC
   * @param sequenceNumber
   *        the sequence number of the related frame
   *        internal used value is (sequenceNumber mod 2)
   * @param errInfo
   *        indicates the error
   *        possible values: T1Block.ERROR_NONE, T1Block.ERROR_EDC, T1Block.ERROR_OTHER
   * @see   T1Block
   */
  public static T1Block createRBlock(int sourceID, int destID, int edcAlg, int sequenceNumber, int errInfo) 
                        throws T1Exception {

    // setup PCB-byte for R-Block
    int pcb = 0xFF & (0x80 | ((sequenceNumber % 2) << 4));

    // add errInfo
    pcb = 0xFF & (pcb | errInfo);

    // create R-Block
    return new T1Block(sourceID, destID, pcb, null, edcAlg);
  }

  /** <tt>createSBlock</tt>
   *
   * creates S-Block according to ISO7816-3
   *
   * @param sourceID
   *        the sourceID of the frame for the NAD-byte (0-7)
   * @param destID
   *        the destID of the frame for the NAD-byte (0-7)
   * @param edcAlg
   *        the used algorithm for calculation of the EDC byte
   *        possible values: EDC_LDR, EDC_CRC
   * @param statusInfo
   *        indicates the error<br>
   *        possible values:<br>
   *        T1Block.S_RESYNCH_REQUEST, T1Block.S_RESYNCH_RESPONSE,<br>
   *        T1Block.S_IFS_REQUEST, T1Block.S_IFS_RESPONSE,<br>
   *        T1Block.S_ABORT_REQUEST, T1Block.S_ABORT_RESPONSE,<br>
   *        T1Block.S_WTX_REQUEST, T1Block.S_WTX_RESPONSE,<br>
   *        T1Block.S_VPP_STATE_ERROR_RESPONSE
   * @param data
   *        application data
   * @see   T1Block
   */
  public static T1Block createSBlock(int sourceID, int destID, int edcAlg, int statusInfo, byte[] data) 
                        throws T1Exception {

    // setup PCB-byte for S-Block
    int pcb = 0xC0;

    // add statusInfo
    pcb = 0xFF & (pcb | statusInfo);

    // create I-Block
    return new T1Block(sourceID, destID, pcb, data, edcAlg);
  }

}
