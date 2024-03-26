import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ANSYS Fluent 2020 R2 VAR-2D Model
 * Java NIO 多核并行写入文件
 *
 * @author YangyangMiao
 * @email yangyangmiao666@outlook.com
 * @since 2024/3/27 2:22
 * @version 2.0
 */

public class InjExport {

    // 输出inj文件的目录，这个可以改成你想要的
    private static final String PATH = "D:/";
    // 动网格速度，铸锭生长速度 单位 m/s
    public static final double V = 0.0003408;
    // 铸锭动网格生长时间，从100s开始 单位 s
    private static final int T0 = 100;
    // 铸锭动网格生长时间，总共2400s 单位 s
    private static final int T_TOTAL = 2400;
    // 每多少秒投放一次夹杂物 单位 s
    private static final int INTERVAL = 100;
    // 初始网格长度 单位 m
    private static final double LENGTH_MESH_INIT = 0.01;
    // 铸锭半径 单位 m
    private static final double R_INGOT = 0.14;
    // 夹杂物尺寸 单位 m
    private static final double[] DIAMETERS = {1.0e-6, 2.0e-6, 3.0e-6, 4.0e-6, 5.0e-6, 6.0e-6, 7.0e-6};
    // 夹杂物数量（和上面尺寸按顺序一一对应） 单位 个
    private static final int[] NUMBERS = {200000, 200000, 300000, 200000, 150000, 200000, 50000};
    // 夹杂物温度 单位 k
    private static final double TEMPERATURE = 1803.0;
    // 夹杂物质量流率，给1.0e-20很小的值就行了 单位 kg/s
    private static final double MASS_FLOW = 1.0e-20;

    // 这是主函数，运行这个就可以了
    public static void main(String[] args) {
        if (DIAMETERS.length != NUMBERS.length) {
            throw new RuntimeException("夹杂物尺寸和夹杂物数量必须一一对应!");
        }
        int segment = ((T_TOTAL - T0) / INTERVAL) + 1;
        System.out.println("NIO内存映射-开始" + segment + "核并行写入离散相file类型注入文件...");
        long startTime = System.currentTimeMillis();
        try (ThreadPoolExecutor executor = new ThreadPoolExecutor(segment, segment, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>())) {
            CountDownLatch latch = new CountDownLatch(segment);
            for (int t = T0; t <= T_TOTAL; t += INTERVAL) {
                AtomicInteger currentTime = new AtomicInteger(t);
                executor.execute(() -> {
                    writeInjFiles(PATH, "inclusions_" + currentTime.get(), currentTime, NUMBERS, DIAMETERS);
                    latch.countDown();
                });
            }
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        int sum = 0;
        for (int num : NUMBERS) {
            sum += num;
        }
        System.out.println("NIO内存映射-" + segment + "核并行写入离散相file类型注入文件成功！总共写入" + sum + "条数据，耗时：" + (endTime - startTime) + "毫秒！");
    }


    public static void writeInjFiles(String path, String fileName, AtomicInteger t, int[] numbers, double[] diameters) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path + fileName, "rw")) {
            FileChannel fileChannel = randomAccessFile.getChannel();
            int offset = 0;
            StringBuilder sb = new StringBuilder();
            AtomicInteger incr = new AtomicInteger(1);
            for (int i = 0; i < diameters.length; i++) {
                int index = incr.getAndIncrement();
                for (int j = 1; j <= numbers[i]; j++) {
                    // 夹杂物随机x位置(轴向)
                    double x = Math.random() * LENGTH_MESH_INIT + V * t.get();
                    // 夹杂物随机y位置(径向)，R_INGOT 是铸锭半径
                    double y = Math.random() * R_INGOT;
                    // 夹杂物随机z位置，如果是三维的话，二维给0就行了
                    double z = 0.0;
                    // 删除原inj文件再生成，否则会添加到之前inj文件的末尾
                    String context = "( ( " + x + " " + y + " " + z + " 0.0 0.0 0.0 " + diameters[i] + " " + TEMPERATURE + " " + MASS_FLOW + " ) injection_" + index + ":" + j + " )";
                    sb.append(context).append("\n");
                }
            }
            byte[] bytes = sb.toString().getBytes();
            MappedByteBuffer mappedByteBuffer;
            // 直接内存映射提升 NIO 性能，高效写入
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, bytes.length);
            mappedByteBuffer.put(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
