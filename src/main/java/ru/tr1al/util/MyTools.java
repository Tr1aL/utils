package ru.tr1al.util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MyTools {

    private static void scanSpringRequestMapping(String path) throws IOException {
        File dir = new File(path);
        if (dir.isDirectory()) {
            scanSpringRequestMapping(dir);
        }
    }

    private static void scanSpringRequestMapping(File dir) throws IOException {
        if (dir != null && dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    scanSpringRequestMapping(file);
                } else if (file.isFile()) {
                    if (file.getName().equals("SpringUtil.java")) {
                        continue;
                    }
                    if (file.getName().toLowerCase().endsWith(".java")) {
                        Scanner scanner = new Scanner(file);
                        boolean b = false;
                        while (scanner.hasNext()) {
                            String line = scanner.nextLine();
                            if (line.contains("{")) {
                                b = true;
                            }
                            if (line.contains("@RequestMapping")) {
                                System.out.println(
                                        (b ? "\t" : "") +
                                                line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            scanSpringRequestMapping("/projects/shop-cms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
