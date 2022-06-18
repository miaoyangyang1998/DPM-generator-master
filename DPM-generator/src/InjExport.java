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
        boolean isCreate = InjExport.creatTxtFile("inclusions");
        if (isCreate) {
            System.out.println("文件创建成功");
        } else {
            System.out.println("文件已存在");
        }
        boolean isWrite = false;
        for (int i = 0; i < 1000; i++) {
            double x = Math.random() * 0.14;
            double y = Math.random() * 0.83;
            // 改 5.00E-05 和 injection0 这两项
            // 5.00E-05 是夹杂物粒径， injection0 是夹杂物的名称
            // 更改粒径，夹杂物名称也要改，并且之前生成的文件要改名字，不然会覆盖原来的文件
            // 重复的数据不用管，否则2020R2不识别
            isWrite = InjExport.writeTxtFile("( ( " + x + " " + y + " 1.00E-10 1.00E-10 2.00E-10 273.00E+00 5.00E-05 273.00E+00 1.00E-20 ) injection0:" + i + " )");
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
