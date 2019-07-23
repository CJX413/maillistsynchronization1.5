package com.weixin.maillistsynchronization.Utils;

import java.io.*;
import java.util.List;


public class FileUtils {

    /**
     * 创建CSV文件
     */
    public static void createCSV(File csvFile, List<List<Object>> dataList) {

        BufferedWriter csvWtriter = null;
        //数据
        try {
            // GB2312使正确读取分隔符","
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile,true), "UTF-8"), 1024);
            //写头
            writeRow(dataList.get(0),csvWtriter);
            // 写入文件内容
            for (int i=1;i<dataList.size();i++) {
                writeRow(dataList.get(i), csvWtriter);
            }
            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写一行数据
     *
     * @param row       数据列表
     * @param csvWriter
     * @throws IOException
     */
    private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append(data).toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }

    /**
     * 复制文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public static boolean copyFileByStream(File source, File dest){
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest)){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            return true;
        }
    }
}

