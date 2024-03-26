import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * ANSYS Fluent 2020 R2 3D
 * 焊接纳米颗粒注入 Java NIO优化
 * 不支持继续写入，续写需要改offset，本次不实现
 *
 * @author YangyangMiao
 * @email yangyangmiao666@outlook.com
 * @since 2024/3/27 0:13
 * @version 2.0
 */
public class NanoInjExport {

    // 输出inj文件的目录，这个可以改成你想要的目录
    private static final String PATH = "D:/";
    // 文件名，不需要修改，我已经写好了
    private static final String FILE_NAME = "nano_inclusions.inj";
    // 投放区域的长度 单位 m
    private static final double LENGTH = 1.4e-2;
    // 投放区域的宽度 单位 m
    private static final double WIDTH = 8.0e-3;
    // 投放区域的高度 单位 m
    private static final double HEIGHT = 5.0e-3;
    // 夹杂物数量 单位 个
    private static final int NUMBER = 2000000;
    // 夹杂物尺寸 单位 m
    private static final double DIAMETER = 1.0e-7;
    // 夹杂物温度 单位 k
    private static final double TEMPERATURE = 300.0;
    // 夹杂物质量流率，给1.0e-20很小的值就行了 单位 kg/s
    private static final double MASS_FLOW = 1.0e-20;

    // 这是主函数，运行这个就可以了
    public static void main(String[] args) {
        System.out.println("NIO内存映射-开始高效写入离散相file类型注入文件...");
        long startTime = System.currentTimeMillis();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(PATH + FILE_NAME, "rw")) {
            FileChannel fileChannel = randomAccessFile.getChannel();
            int offset = 0;
            StringBuilder sb = new StringBuilder();

            // DIAMETER 的夹杂物投放 NUMBER 个
            for (int i = 1; i <= NUMBER; i++) {
                // 夹杂物随机x位置(轴向)
                double x = getRandom(-1.0 * LENGTH, LENGTH);
                // 夹杂物随机y位置(径向)，0.14是铸锭半径
                double y = getRandom(0, WIDTH);
                // 夹杂物随机z位置，如果是三维的话，二维给0就行了
                double z = getRandom(-1.0 * HEIGHT, HEIGHT);
                // 删除原inj文件再生成，否则会添加到之前inj文件的末尾
                String context = "( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + DIAMETER + " " + TEMPERATURE + " " + MASS_FLOW + " ) injection1_:" + i + " )";
                sb.append(context).append("\n");
            }
            byte[] bytes = sb.toString().getBytes();
            MappedByteBuffer mappedByteBuffer;
            // 直接内存映射提升 NIO 性能，高效写入
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, bytes.length);
            mappedByteBuffer.put(bytes);
            long endTime = System.currentTimeMillis();
            System.out.println("NIO内存映射-高效写入离散相file类型注入文件成功！总共写入" + NUMBER + "条数据，耗时：" + (endTime - startTime) + "毫秒！");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取范围随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    public static double getRandom(double min, double max) {
        return Math.random() * (max - min) + min;
    }
}