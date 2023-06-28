import java.io.*;

/**
 * @program: ByteDance
 * @author: MiaoYangYang
 * @email: miaoyang@mail.ustc.edu.cn
 * @create: 2022-06-11 22:32
 **/
public class InjExport {

    // 输出inj文件的目录，这个可以改成你想要的
    private static final String PATH = "D:/";
    // 文件名，不需要修改，我已经写好了
    private static String filenameTemp;

    // 这是主函数，运行这个就可以了
    public static void main(String[] args) throws IOException {
        // 动网格速度，铸锭生长速度
        double v = 0.0003408;
        // 铸锭动网格生长时间，从100s开始
        int t0 = 100;
        // 铸锭动网格生长时间，总共2400s
        int tTotal = 2400;
        // 初始网格长度m
        double length0 = 0.01;

        // 开始创建文件
        for (int t = t0; t <= tTotal; t += 100) {
            // 创建文件，文件名inclusions_加时间
            boolean isCreate = InjExport.creatTxtFile("inclusions_" + t);
            if (isCreate) {
                System.out.println("文件创建成功");
            } else {
                System.out.println("文件已存在");
            }
            boolean isWrite = false;

            // 1μm夹杂物投放100个
            for (int i = 1; i <= 100; i++) {
                // 夹杂物随机x位置(轴向)
                double x = Math.random() * length0 + v * t;
                // 夹杂物随机y位置(径向)，0.14是铸锭半径
                double y = Math.random() * 0.14;
                // 夹杂物随机z位置，如果是三维的话，二维给0就行了
                double z = 0.0;
                // 夹杂物粒径
                double diameter = 1.0e-6;
                // 夹杂物温度
                double temperature = 1803.0;
                // 夹杂物流率，给1.0e-20很小的值就行了
                double massFlow = 1.0e-20;
                // 删除原inj文件再生成，否则会添加到之前inj文件的末尾
                isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection1_:" + i + " )");
            }

            // 2μm夹杂物投放800个
            for (int i = 1; i <= 800; i++) {
                double x = Math.random() * length0 + v * t;
                double y = Math.random() * 0.14;
                double z = 0.0;
                double diameter = 2.0e-6;
                double temperature = 1803.0;
                double massFlow = 1.0e-20;
                isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection2_:" + i + " )");
            }

            for (int i = 1; i <= 200; i++) {
                double x = Math.random() * length0 + v * t;
                double y = Math.random() * 0.14;
                double z = 0.0;
                double diameter = 3.0e-6;
                double temperature = 1803.0;
                double massFlow = 1.0e-20;
                isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection3_:" + i + " )");
            }

            for (int i = 1; i <= 50; i++) {
                double x = Math.random() * length0 + v * t;
                double y = Math.random() * 0.14;
                double z = 0.0;
                double diameter = 4.0e-6;
                double temperature = 1803.0;
                double massFlow = 1.0e-20;
                isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection4_:" + i + " )");
            }

            for (int i = 1; i <= 30; i++) {
                double x = Math.random() * length0 + v * t;
                double y = Math.random() * 0.14;
                double z = 0.0;
                double diameter = 5.0e-6;
                double temperature = 1803.0;
                double massFlow = 1.0e-20;
                isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection5_:" + i + " )");
            }

            for (int i = 1; i <= 20; i++) {
                double x = Math.random() * length0 + v * t;
                double y = Math.random() * 0.14;
                double z = 0.0;
                double diameter = 6.0e-6;
                double temperature = 1803.0;
                double massFlow = 1.0e-20;
                isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameter + " " + temperature + " " + massFlow + " ) injection6_:" + i + " )");
            }

            for (int i = 1; i <= 10; i++) {
                double x = Math.random() * length0 + v * t;
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
