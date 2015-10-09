package ru.tr1al.util;

import java.io.File;

public class FileUtil {

    public static FileInfo getFileInfo(File file) {
        String name = file.getName().substring(0, file.getName().lastIndexOf("."));
        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        return new FileInfo(name, ext);
    }

    public static FileInfo getFileInfo(String fileName) {
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        return new FileInfo(name, ext);
    }

    public static class FileInfo {
        public FileInfo(String name, String ext) {
            this.name = name;
            this.ext = ext;
        }

        private String name;
        private String ext;

        public String getName() {
            return name;
        }

        public String getExt() {
            return ext;
        }
    }

}
