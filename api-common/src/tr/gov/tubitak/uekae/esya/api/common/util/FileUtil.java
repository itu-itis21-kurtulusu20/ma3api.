package tr.gov.tubitak.uekae.esya.api.common.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ayetgin
 */
public class FileUtil {

    public static void writeBytes(String filePath, byte[] fileBytes) throws IOException {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(filePath);
            fout.write(fileBytes);
        } catch (IOException e) {
            throw e;
        } finally {
            if(fout != null) {
                fout.close();
            }
        }
    }

    public static byte[] readBytes (String aFile) throws IOException {
        return Files.readAllBytes(Paths.get(aFile));
    }

    public static void move(File source, File dest) throws IOException {
        boolean moved = source.renameTo(dest);
        if (!moved){
            copy(source, dest);
            boolean deleted = source.delete();
            if (!deleted){
                source.deleteOnExit();
            }
        }
    }

    public static boolean exists(String filePath)
    {
        return new File(filePath).exists();
    }


    public static void copy(File source, File dest) throws IOException {
         copyFileUsingChannel(source, dest);
    }

    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fout = new FileOutputStream(dest);

        try {
            sourceChannel = fis.getChannel();
            destChannel = fout.getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }finally{
            if (sourceChannel!=null) sourceChannel.close();
            if (destChannel!=null) destChannel.close();
            if (fis != null) fis.close();
            if (fout != null) fout.close();
        }
    }

    public static String getNameWithoutExtension(String filePath){
        int pos = filePath.lastIndexOf(".");
        if (pos > 0 && pos < (filePath.length() - 1)) { // If '.' is not the first or last character.
            return filePath.substring(0, pos);
        }
        return filePath;
    }

    public static void cleanDirectory(String directoryPath) throws ESYAException {
        File dir = new File(directoryPath);
        if(!dir.isDirectory())
            throw new ESYAException(directoryPath + " is not a directory");
        for(File file : dir.listFiles()) {
            file.delete();
        }
    }

    public static void createDummyFileIfNotExist(String filePath, long size) throws IOException {
        {
            final Path filePathPath = Paths.get(filePath);
            final Path directory = filePathPath.getParent();

            if (Files.exists(filePathPath))
                return;

            Files.createDirectories(directory);
        }

        FileOutputStream fos = new FileOutputStream(filePath);
        byte[] tenBytes = {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39};
        long times = size / tenBytes.length;
        int remainder = (int) (size % tenBytes.length);

        for (int i = 0; i < times; i++)
            fos.write(tenBytes, 0, tenBytes.length);

        fos.write(tenBytes, 0, remainder);

        fos.close();
    }
}
