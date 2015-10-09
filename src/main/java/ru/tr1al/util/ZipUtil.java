package ru.tr1al.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {


    public static void main(String[] args) {
        try {
            compressFolder("/tmp", "/tmp.zip");
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public static void compressFolder(String folderPath, String zipPath) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipPath));
            out.setLevel(Deflater.BEST_COMPRESSION);
            File dir = new File(folderPath);
            if (dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    FileInputStream fis = new FileInputStream(file);
                    out.putNextEntry(new ZipEntry(file.getName()));
                    int len;
                    while ((len = fis.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public static void compressFiles(List<File> files, File zip) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
            out.setLevel(Deflater.BEST_COMPRESSION);
            for (File file : files) {
                FileInputStream fis = new FileInputStream(file);
                out.putNextEntry(new ZipEntry(file.getName()));
                int len;
                while ((len = fis.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
            }
            out.close();
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public static void compressData(final byte[] data, final String filename, File zip) {
        compressData(new HashMap<String, byte[]>() {{
            put(filename, data);
        }}, zip);
    }

    public static void compressData(Map<String, byte[]> bytes, File zip) {
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
            out.setLevel(Deflater.BEST_COMPRESSION);
            for (Map.Entry<String, byte[]> e : bytes.entrySet()) {
                out.putNextEntry(new ZipEntry(e.getKey()));
                out.write(e.getValue());
                out.closeEntry();
            }
            out.close();
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public static List<File> zipDir(File srcDir, String destPath, String destName, Long partSize, String encoding) throws IOException {
        ChunkedZipOutputStream out = new ChunkedZipOutputStream(destPath, destName, partSize, encoding);
        zipDir(srcDir, "", out);
        out.closeStream();
        return out.getFiles();
    }

    private static void zipFile(File srcFile, String destPath, ChunkedZipOutputStream out) throws IOException {
        out.putNextEntry(srcFile, destPath);
    }

    private static void zipDir(File srcDir, String destPath, ChunkedZipOutputStream out) throws IOException {
        for (File file : srcDir.listFiles())
            if (file.isDirectory())
                zipDir(file, concatPathAndFilename(destPath, file.getName()), out);
            else
                zipFile(file, destPath, out);

    }

    private static String concatPathAndFilename(String path, String filename) {
        if (path == null)
            return filename;
        String trimmedPath = path.trim();
        if (trimmedPath.length() == 0)
            return filename;
        String trimmedFilename = filename.trim();
        if (trimmedPath.endsWith(File.separator))
            return trimmedPath + trimmedFilename;
        else
            return trimmedPath + File.separator + trimmedFilename;
    }

    public static class ChunkedZipOutputStream {
        private ZipOutputStream zipOutputStream;
        private String path;
        private String name;
        private long currentSize;
        private int currentChunkIndex;
        private Long partSize = 1024 * 1024L;
        private final String PART_POSTFIX = ".part.";
        private final String FILE_EXTENSION = ".zip";
        private ArrayList<File> files = new ArrayList<File>();
        private String encoding;
        private ZipEntry customEntry = null;
        private int level = Deflater.NO_COMPRESSION;

        public ChunkedZipOutputStream(String path, String name, Long partSize, String encoding) throws IOException {
            this.path = path;
            this.name = name;
            this.partSize = partSize;
            this.encoding = encoding;
            constructNewStream();
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public void putNextEntry(String path, String content) throws IOException {
            ZipEntry entry = new ZipEntry(path);
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write(content.getBytes(encoding));
            zipOutputStream.closeEntry();
            checkSize(entry);
        }

        public void startEntry(String path) throws IOException {
            customEntry = new ZipEntry(path);
            zipOutputStream.putNextEntry(customEntry);
        }

        public void write(String content) throws IOException {
            zipOutputStream.write(content.getBytes(encoding));
        }

        public void write(byte[] content) throws IOException {
            zipOutputStream.write(content);
        }

        public void closeEntry() throws IOException {
            zipOutputStream.closeEntry();
            checkSize(customEntry);
        }

        private void checkSize(ZipEntry entry) throws IOException {
            if (partSize != null) {
                long entrySize = entry.getCompressedSize();
                if ((currentSize + entrySize) > partSize) {
                    closeStream();
                    constructNewStream();
                }
                currentSize += entrySize;
            }
        }

        public void putNextEntry(File srcFile, String destPath) throws IOException {
            ZipEntry entry = new ZipEntry(concatPathAndFilename(destPath, srcFile.getName()));
            putEntry(entry, srcFile);
        }

        public void putNextEntry(String path, File file) throws IOException {
            ZipEntry entry = new ZipEntry(path);
            putEntry(entry, file);
        }

        public void putNextEntry(String path, byte[] bytes) throws IOException {
            ZipEntry entry = new ZipEntry(path);
            putEntry(entry, bytes);
        }

        private void putEntry(ZipEntry entry, File file) throws IOException {
            zipOutputStream.putNextEntry(entry);
            byte buf[] = new byte[1024];
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            int len;
            while ((len = in.read(buf)) > 0)
                zipOutputStream.write(buf, 0, len);
            zipOutputStream.closeEntry();
            in.close();
            checkSize(entry);
        }

        private void putEntry(ZipEntry entry, byte[] bytes) throws IOException {
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write(bytes);
            zipOutputStream.closeEntry();
            checkSize(entry);
        }

        public void closeStream() throws IOException {
            zipOutputStream.close();
        }

        private void constructNewStream() throws IOException {
            File file = new File(path, constructCurrentPartName());
            if (!file.exists()) {
                file.createNewFile();
            }
            file.deleteOnExit();
            this.files.add(file);
            zipOutputStream = new ZipOutputStream(new FileOutputStream(file));
            zipOutputStream.setLevel(level);
            currentChunkIndex++;
            currentSize = 0;
        }

        private String constructCurrentPartName() {
            StringBuilder partNameBuilder = new StringBuilder(name);
            if (partSize != null) {
                partNameBuilder.append(PART_POSTFIX);
                partNameBuilder.append(currentChunkIndex);
            }
            partNameBuilder.append(FILE_EXTENSION);
            return partNameBuilder.toString();
        }

        public ArrayList<File> getFiles() {
            return files;
        }
    }

    public static void compressFolderWithSubdir(String folderPath, String zipPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipPath);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zos.setLevel(Deflater.BEST_COMPRESSION);
        addFolder(zos, folderPath, folderPath);
        zos.close();
    }

    private static void addFolder(ZipOutputStream zos, String folderName, String baseFolderName) throws IOException {
        File f = new File(folderName);
        if (f.exists()) {
            if (f.isDirectory()) {
                for (File f2 : f.listFiles()) {
                    addFolder(zos, f2.getAbsolutePath(), baseFolderName);
                }
            } else {
                String entryName = folderName.substring(baseFolderName.length() + 1, folderName.length());
                ZipEntry ze = new ZipEntry(entryName);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(folderName);
                int len;
                byte buffer[] = new byte[1024];
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
                zos.closeEntry();
            }
        }
    }
}