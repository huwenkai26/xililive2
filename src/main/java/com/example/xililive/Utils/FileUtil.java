package com.example.xililive.Utils;

import java.io.*;

public class FileUtil {

    private static void writerTolocal(String string) {
        FileOutputStream writerStream;
        try {
            writerStream = new FileOutputStream("json.txt");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
            writer.write(string);
            writer.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static String readerTolocal() {
        FileInputStream readerStream;
        StringBuilder result = new StringBuilder();

        try {

            readerStream = new FileInputStream("json.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(readerStream, "UTF-8"));
            String s = null;
            while((s = reader.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }

            reader.close();
            return result.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

}
