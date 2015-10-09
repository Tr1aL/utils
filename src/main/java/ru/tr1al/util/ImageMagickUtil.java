package ru.tr1al.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class ImageMagickUtil {

    public class ImageSize {
        private Integer width;
        private Integer height;

        public ImageSize(Integer width, Integer height) {
            this.width = width;
            this.height = height;
        }

        public Integer getWidth() {
            return width;
        }

        public Integer getHeight() {
            return height;
        }
    }

    private String IMDir;

    public ImageMagickUtil() {
        this.IMDir = "";
    }

    public ImageMagickUtil(String IMDir) {
        if (IMDir == null || IMDir.trim().isEmpty()) {
            this.IMDir = "";
        } else {
            IMDir = IMDir.trim();
            if (!IMDir.endsWith("/") && !IMDir.endsWith("\\"))
                this.IMDir = IMDir + "/";
            else
                this.IMDir = IMDir;
        }
    }

    private String readStream(InputStream is) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer);
        return writer.toString();
    }

    private String executeCommand(String[] cmd) {
        try {
            String a = "";
            for (String c : cmd) {
                a += c + " ";
            }
            Process p = Runtime.getRuntime().exec(cmd);

            String s_inp = readStream(p.getInputStream());
            try {
                String err = readStream(p.getErrorStream());
//                LogUtil.writeToImageMagickLog(a + "\nret: " + s_inp+"\nerr: " + err);
            } catch (IOException e) {
                System.out.println(a + "\nret: " + s_inp + "\nerr: " + readStream(p.getErrorStream()));
            }
            return s_inp;
        } catch (Exception ex) {
//            log.error(ex, ex);
            return null;
        }
    }

    private String executeCommand(String cmd) {
        try {
            Process p = Runtime.getRuntime().exec(cmd);

            String s_inp = readStream(p.getInputStream());
            try {
                String err = readStream(p.getErrorStream());
//                LogUtil.writeToImageMagickLog(cmd + "\nret: " + s_inp+"\nerr: " + err);
            } catch (IOException e) {
                System.out.println(cmd + "\nret: " + s_inp + "\nerr: " + readStream(p.getErrorStream()));
            }
            return s_inp;
        } catch (Exception ex) {
//            log.error(ex, ex);
            return null;
        }
    }

    public ImageSize getImageSize(String imagePath) {
        ArrayList<String> cmd = new ArrayList<>();
        cmd.add(IMDir + "identify");
        cmd.add(imagePath);
        String ret = executeCommand(cmd.toArray(new String[]{""}));
        if (ret == null || ret.length() == 0)
            return null;
        String[] size = ret.substring(imagePath.length() + 1).split(" ")[1].split("x");
        return new ImageSize(Integer.valueOf(size[0]), Integer.valueOf(size[1]));
    }

    public Boolean cropImage(String fromPath, String toPath, Integer width, Integer height, Integer indentLeft, Integer indentTop) {
        ImageSize is = getImageSize(fromPath);
        if (is != null) {
            ArrayList<String> cmd = new ArrayList<>();
            cmd.add(IMDir + "convert");
            cmd.add("-crop");
            cmd.add(width.toString() + "x" + height.toString() + "+" + indentLeft + "+" + indentTop);
            cmd.add(fromPath);
            cmd.add(toPath);
            String ret = executeCommand(cmd.toArray(new String[]{""}));
            return (ret.trim().length() == 0);
        } else {
            return false;
        }
    }

    public Boolean convertImage(String fromPath, String toPath) {
        ArrayList<String> cmd = new ArrayList<>();
        cmd.add(IMDir + "convert");
        cmd.add(fromPath);
        cmd.add(toPath);
        String ret = executeCommand(cmd.toArray(new String[]{""}));
        return (ret.trim().length() == 0);
    }

    public Boolean resizeImageToMaxSize(String fromPath, String toPath, Integer width, Integer height) {
        ImageSize is = getImageSize(fromPath);
        Integer maxSize = Math.max(width, height);
        if (is.getWidth() >= is.getHeight()) {
            if (is.getWidth() > width) {
                maxSize = width;
            }
        } else {
            if (is.getHeight() > height) {
                maxSize = height;
            }
        }
        return resizeImageToMaxSize(fromPath, toPath, maxSize);
    }

    public Boolean resizeImageToMaxSize(String fromPath, String toPath, Integer maxSize) {
        ImageSize is = getImageSize(fromPath);
        if (is != null) {
            int width = is.getWidth();
            int height = is.getHeight();
            if (width > height) {
                height = (maxSize * height) / width;
                width = maxSize;
            } else {
                width = (maxSize * width) / height;
                height = maxSize;
            }
            return resize(fromPath, toPath, width, height);
        } else {
            return false;
        }
    }

    public Boolean resize(String fromPath, String toPath, Integer width, Integer height) {
        ArrayList<String> cmd = new ArrayList<>();
        cmd.add(IMDir + "convert");
        cmd.add("-resize");
        cmd.add(width + "x" + height);
        cmd.add(fromPath);
        cmd.add(toPath);
        String ret = executeCommand(cmd.toArray(new String[]{""}));
        return (ret.trim().length() == 0);
    }

    public Boolean resizeImageWithCrop(String fromPath, String toPath, Integer maxWidth, Integer maxHeight) throws Exception {
        ImageSize is = getImageSize(fromPath);
        if (is != null) {
            int width = is.getWidth();
            int height = is.getHeight();
            int h;
            int w;
            if (width / maxWidth < height / maxHeight) {
                w = maxWidth;
                h = height * maxWidth / width;
            } else {
                h = maxHeight;
                w = width * maxHeight / height;
            }
            File temp = File.createTempFile(System.currentTimeMillis() + "_" + new Random().nextInt(9999) + 1, ".img");
            temp.deleteOnExit();

            resize(fromPath, temp.getAbsolutePath(), w, h);

            return cropImage(temp.getAbsolutePath(), toPath, maxWidth, maxHeight, (w - maxWidth) / 2, (h - maxHeight) / 2);
        } else {
            return false;
        }
    }

    private String getGravityAsString(Integer gravity) {
        if (gravity == null)
            return "Center";
        switch (gravity) {
            case 1:
                return "NorthWest";
            case 2:
                return "North";
            case 3:
                return "NorthEast";
            case 4:
                return "West";
            case 5:
                return "Center";
            case 6:
                return "East";
            case 7:
                return "SouthWest";
            case 8:
                return "South";
            case 9:
                return "SouthEast";
            default:
                return "Center";
        }
    }

    public void drawTextOnImage(String fromPath, String toPath, String text, String font, String fontsize, Integer gravity, String color1, String color2, boolean roundCorners) {
        if (roundCorners) {
            StringBuilder sb = new StringBuilder(IMDir);
            sb.append("convert ");
            sb.append(fromPath);
            sb.append(" -matte -virtual-pixel transparent -channel A -blur 0x8 -evaluate subtract 40% -evaluate multiply 2.001 ");
            sb.append(toPath).append(".png");
            String ret = executeCommand(sb.toString());

            sb = new StringBuilder(IMDir);
            sb.append("convert ");
            sb.append(toPath).append(".png");
            sb.append(" -matte -background white -flatten ");
            sb.append(toPath);
            ret = executeCommand(sb.toString());
        }

        ArrayList<String> cmd = new ArrayList<>();
        cmd.add(IMDir + "convert");
        cmd.add(roundCorners ? toPath : fromPath);
        cmd.add("-font");
        cmd.add(font);
        cmd.add("-pointsize");
        cmd.add(fontsize);
        cmd.add("-draw");
        try {
            text = new String(text.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
//            LogUtil.writeToImageMagickLog(LogUtil.getStackTraceAsString(e));
        }

        if (color2 == null && color1 != null)
            cmd.add("gravity " + getGravityAsString(gravity) + " fill " + color1 + " text 10,0 '" + text + "' ");
        else if (color1 == null && color2 != null)
            cmd.add("gravity " + getGravityAsString(gravity) + " fill " + color2 + " text 10,0 '" + text + "' ");
        else
            cmd.add("gravity " + getGravityAsString(gravity) + " fill " + color1 + " text 12,2 '" + text + "' " + " fill " + color2 + " text 10,0 '" + text + "' ");


        cmd.add(toPath);
        String ret = executeCommand(cmd.toArray(new String[]{""}));
    }
}
