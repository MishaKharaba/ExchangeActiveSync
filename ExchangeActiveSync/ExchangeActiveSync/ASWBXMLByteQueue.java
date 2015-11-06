//
// Translated by CS2J (http://www.cs2j.com): 06/11/2015 15:37:33
//

package ExchangeActiveSync;


// This class extends the .NET Queue<byte> class
// to add some WBXML-specific functionality.
public class ASWBXMLByteQueue  extends Queue<byte> 
{
    public ASWBXMLByteQueue(byte[] bytes) throws Exception {
        super(bytes);
    }

    // This function will pop a multi-byte integer
    // (as specified in WBXML) from the WBXML stream.
    public int dequeueMultibyteInt() throws Exception {
        int returnValue = 0;
        byte singleByte = 0xFF;
        do
        {
            returnValue <<= 7;
            singleByte = this.Dequeue();
            returnValue += (int)(singleByte & 0x7F);
        }
        while (checkContinuationBit(singleByte));
        return returnValue;
    }

    // This function checks a byte to see if the continuation
    // bit is set. This is used in deciphering multi-byte integers
    // in a WBXML stream.
    private boolean checkContinuationBit(byte byteValue) throws Exception {
        byte continuationBitmask = 0x80;
        return (continuationBitmask & byteValue) != 0;
    }

    // This function pops a string from the WBXML stream.
    // It will read from the stream until a null byte is found.
    public String dequeueString() throws Exception {
        StringBuilder returnStringBuilder = new StringBuilder();
        byte currentByte = 0x00;
        do
        {
            // TODO: Improve this handling. We are technically UTF-8, meaning
            // that characters could be more than one byte long. This will fail if we have
            // characters outside of the US-ASCII range
            currentByte = this.Dequeue();
            if (currentByte != 0x00)
            {
                returnStringBuilder.Append((char)currentByte);
            }
             
        }
        while (currentByte != 0x00);
        return returnStringBuilder.ToString();
    }

    // This function pops a string of the specified length from
    // the WBXML stream.
    public String dequeueString(int length) throws Exception {
        StringBuilder returnStringBuilder = new StringBuilder();
        byte currentByte = 0x00;
        for (int i = 0;i < length;i++)
        {
            // TODO: Improve this handling. We are technically UTF-8, meaning
            // that characters could be more than one byte long. This will fail if we have
            // characters outside of the US-ASCII range
            currentByte = this.Dequeue();
            if ((char)currentByte == '\n')
                returnStringBuilder.AppendLine();
            else
                returnStringBuilder.Append((char)currentByte); 
        }
        return returnStringBuilder.ToString();
    }

    // This function dequeues a byte array of the specified length
    // from the WBXML stream.
    public byte[] dequeueBinary(int length) throws Exception {
        List<byte> returnBytes = new List<byte>();
        for (int i = 0;i < length;i++)
        {
            returnBytes.Add(this.Dequeue());
        }
        return returnBytes.ToArray();
    }

}


