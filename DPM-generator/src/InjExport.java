import java.io.*;

/**
 * @program: ByteDance
 * @author: MiaoYangYang
 * @email: miaoyang@mail.ustc.edu.cn
 * @create: 2022-06-11 22:32
 **/
public class InjExport {

    private static final String PATH = "D:/";
    private static String filenameTemp;

    public static void main(String[] args) throws IOException {
        double v = 0.0003408;
        double t = 2400;
        double l0 = 0.01;
        boolean isCreate = InjExport.creatTxtFile("inclusions_" + t);
        if (isCreate) {
            System.out.println("文件创建成功");
        } else {
            System.out.println("文件已存在");
        }
        boolean isWrite = false;

        for (int i = 1; i <= 100; i++) {
            double x = Math.random() * l0 + v * t;
            double y = Math.random() * 0.14;
            double z = 0.0;
            double diameter = 1.0e-6;
            double temperature = 1803.0;
            double massFlow = 1.0e-20;
            // 删除原inj文件再生成，否则会添加到之前inj文件的末尾
            isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection1_:" + i + " )");
        }

        for (int i = 1; i <= 800; i++) {
            double x = Math.random() * l0 + v * t;
            double y = Math.random() * 0.14;
            double z = 0.0;
            double diameter = 2.0e-6;
            double temperature = 1803.0;
            double massFlow = 1.0e-20;
            isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection2_:" + i + " )");
        }

        for (int i = 1; i <= 200; i++) {
            double x = Math.random() * l0 + v * t;
            double y = Math.random() * 0.14;
            double z = 0.0;
            double diameter = 3.0e-6;
            double temperature = 1803.0;
            double massFlow = 1.0e-20;
            isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection3_:" + i + " )");
        }

        for (int i = 1; i <= 50; i++) {
            double x = Math.random() * l0 + v * t;
            double y = Math.random() * 0.14;
            double z = 0.0;
            double diameter = 4.0e-6;
            double temperature = 1803.0;
            double massFlow = 1.0e-20;
            isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection4_:" + i + " )");
        }

        for (int i = 1; i <= 30; i++) {
            double x = Math.random() * l0 + v * t;
            double y = Math.random() * 0.14;
            double z = 0.0;
            double diameter = 5.0e-6;
            double temperature = 1803.0;
            double massFlow = 1.0e-20;
            isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection5_:" + i + " )");
        }

        for (int i = 1; i <= 20; i++) {
            double x = Math.random() * l0 + v * t;
            double y = Math.random() * 0.14;
            double z = 0.0;
            double diameter = 6.0e-6;
            double temperature = 1803.0;
            double massFlow = 1.0e-20;
            isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection6_:" + i + " )");
        }

        for (int i = 1; i <= 10; i++) {
            double x = Math.random() * l0 + v * t;
            double y = Math.random() * 0.14;
            double z = 0.0;
            double diameter = 7.0e-6;
            double temperature = 1803.0;
            double massFlow = 1.0e-20;
            isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection7_:" + i + " )");
        }

        if (isWrite) {
            System.out.println("文件写入成功");
        } else {
            System.out.println("文件写入失败");
        }
    }

    /**
     * 创建文件
     */
    public static boolean creatTxtFile(String name) throws IOException {
        boolean flag = false;
        filenameTemp = PATH + name + ".inj";
        File filename = new File(filenameTemp);
        if (!filename.exists()) {
            flag = filename.createNewFile();
        }
        return flag;
    }

    /**
     * 写文件
     * @param newStr 新内容
     */
    public static boolean writeTxtFile(String newStr) throws IOException {
        // 先读取原有文件内容，然后进行写入操作
        boolean flag;
        String fileIn = newStr + " ";
        String temp;

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            // 文件路径
            File file = new File(filenameTemp);
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuilder buf = new StringBuilder();

            // 保存该文件原有的内容
            while ((temp = br.readLine()) != null) {
                buf.append(temp);
                // System.getProperty("line.separator")
                // 行与行之间的分隔符 相当于“ ”
                buf.append(System.getProperty("line.separator"));
            }
            buf.append(fileIn);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            flag = true;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }
}
