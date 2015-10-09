package ru.tr1al.util;

import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class HttpUtil {

    public static String doGetQuery(String servlet, String query) throws IOException {
        return doGetQuery(servlet, query, null);
    }

    public static String doGetQuery(String servlet, String query, String encoding) throws IOException {
        URL url = new URL(servlet + query);
        BufferedReader reader;
        if (encoding != null) {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
        } else {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    public static StringBuilder buildParamsString(String[] key, String[] value, String encoding) throws UnsupportedEncodingException {
        StringBuilder data = new StringBuilder();
        if (key != null && value != null) {
            for (int i = 0; i < key.length; i++) {
                data.append(URLEncoder.encode(key[i], encoding));
                data.append("=");
                data.append(URLEncoder.encode(value[i], encoding));
                if (i < (key.length - 1)) {
                    data.append("&");
                }
            }
        }
        return data;
    }

    public static String doPostQuery(String servlet, String[] key, String[] value, String encoding, String auth) throws IOException {
        StringBuilder data = buildParamsString(key, value, encoding);

        URL url = new URL(servlet);
        URLConnection urlConn = url.openConnection();
        if (auth != null) {
            urlConn.setRequestProperty("Authorization", "Basic " + auth);
        }
        urlConn.setDoInput(true);
        if (data.length() > 0) {
            urlConn.setDoOutput(true);
        }
        urlConn.setUseCaches(false);
        if (data.length() > 0) {
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
            printout.writeBytes(data.toString());
            printout.flush();
            printout.close();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = urlConn.getInputStream();
        long limit = 300 * 1024 * 1024;
        int i = 0;
        for (; i < 300 * 1024 * 1024; i++) {
            int ch = is.read();
            if (ch == -1) {
                break;
            }
            bos.write(ch);
        }
        if (i >= limit) {
            return null;
        }
        return new String(bos.toByteArray(), encoding);
    }

    public static byte[] doPostQueryByte(String servlet, String[] key, String[] value, String encoding, String auth) throws IOException {
        StringBuilder data = new StringBuilder();
        if (key != null && value != null) {
            for (int i = 0; i < key.length; i++) {
                data.append(URLEncoder.encode(key[i], encoding));
                data.append("=");
                data.append(URLEncoder.encode(value[i], encoding));
                if (i < (key.length - 1)) {
                    data.append("&");
                }
            }
        }
        URL url = new URL(servlet);
        URLConnection urlConn = url.openConnection();
        if (auth != null) {
            urlConn.setRequestProperty("Authorization", "Basic " + auth);
        }
        urlConn.setDoInput(true);
        if (data.length() > 0) {
            urlConn.setDoOutput(true);
        }
        urlConn.setUseCaches(false);
        if (data.length() > 0) {
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
            printout.writeBytes(data.toString());
            printout.flush();
            printout.close();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = urlConn.getInputStream();
        long limit = 300 * 1024 * 1024;
        int i = 0;
        for (; i < 300 * 1024 * 1024; i++) {
            int ch = is.read();
            if (ch == -1) {
                break;
            }
            bos.write(ch);
        }
        if (i >= limit) {
            return null;
        }
        return bos.toByteArray();
    }

    public static Cookie getCookie(String name, HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals(name)) {
                    return c;
                }
            }
        }
        return null;
    }

    public static String sendPostQueryToHostSsl(String method, String curl, String query, String data) {

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(curl);
            SSLSocketFactory factory
                    = (SSLSocketFactory) SSLSocketFactory.getDefault();
            javax.net.ssl.SSLSocket socket
                    = (javax.net.ssl.SSLSocket) factory.createSocket(url.getHost(), 443);

            String req = method + " " + url.getFile() + (url.getFile().contains("?") ? "&" : "?") + query + " HTTP/1.0\r\n"
                    + "User-Agent: Apishops\r\n"
                    + "Accept: text/*\r\n"
                    + "Connection: close\r\n"
                    + "Host: " + url.getHost() + "\r\n";
            if (data != null) {
                req += curl.contains(".json") ? "Content-Type: application/json\r\n" : "Content-Type: application/xml\r\n";
                req += "Content-Length: " + data.getBytes("UTF-8").length + "\r\n";
            }
            req += "\r\n";
            //if (data!=null) req += data;
            //socket.getOutputStream().write(req.getBytes());
            socket.setSoTimeout(120000);
            InputStreamReader input = new InputStreamReader(socket.getInputStream(), "UTF-8");

            //OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
            out.write(req.getBytes("ASCII"));
            if (data != null) {
                out.write(data.getBytes("UTF-8"));
            }
            out.flush();

            int n;
            while ((n = input.read()) != -1) {
                sb.append((char) n);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
        String r = sb.toString();
        if (r.contains("\r\n\r\n")) {
            r = r.substring(4 + r.indexOf("\r\n\r\n"));
        }
        return r;
    }
}
