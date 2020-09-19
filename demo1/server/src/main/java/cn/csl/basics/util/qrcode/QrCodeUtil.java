package cn.csl.basics.util.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class QrCodeUtil
{
    public static void makeQRCodeImg(String text, String format, int width, int height, String imgName) throws IOException, WriterException
    {
        Hashtable<EncodeHintType, Object> hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        File outputFile = new File(imgName + "." + format);
        MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
    }
}
