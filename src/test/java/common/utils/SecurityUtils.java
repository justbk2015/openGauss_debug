/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package common.utils;

import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * description:this for SecurityUtils Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/16]
 * @since 2021/3/16
 */
public class SecurityUtils {
    public void compareFile(String file1, String file2) throws Exception {
        File f1 = new File(file1);
        if (!f1.exists()) {
            throw new Exception("file1:" + file1 + " not exist!");
        }
        File f2 = new File(file2);
        if (!f2.exists()) {
            throw new Exception("file2:" + file2 + " not exist!");
        }
        if (f1.length() != f2.length()) {
            throw new Exception(String.format(Locale.ENGLISH,
                    "file size not equal,file1:%d, file2:%d",
                    f1.length(),
                    f2.length()));
        }
        if (f1.length() > 30 * 1024 * 1024) {
            throw new Exception("file size limit <= 30M");
        }
        String file1Sha256 = sha256File(f1);
        String file2Sha256 = sha256File(f2);
        if (!file1Sha256.equals(file2Sha256)) {
            throw new Exception(String.format(Locale.ENGLISH,
                    "file not equal:\r\nfile1 sha256=%s\r\nfile2 sha256=%s\r\n",
                    file1Sha256,
                    file2Sha256));
        }
    }

    protected String sha256File(File file) throws IOException, NoSuchAlgorithmException {
        byte[] fileBytes = new byte[(int)file.length()];
        try (InputStream in = new FileInputStream(file)) {
            in.read(fileBytes);
        }
        return byte2HexString(sha256(fileBytes));
    }

    public byte[] sha256(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes);
        return md.digest();
    }

    public String byte2HexString(byte[] input) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer(512);
        for (int i = 0, n = input.length; i < n; i++) {
            String hex = Integer.toHexString(input[i] & 0xff);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
