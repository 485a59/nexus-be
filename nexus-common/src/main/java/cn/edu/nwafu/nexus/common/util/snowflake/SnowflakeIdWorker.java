package cn.edu.nwafu.nexus.common.util.snowflake;

/**
 * 雪花算法解析结构 snowflake的结构如下(每部分用-分开):
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * 第一位为未使用，接下来的 41 位为毫秒级时间 (41位的长度可以使用69年)，
 * 后续是 5 位 datacenterId 和 5 位 workerId (10 位的长度最多支持部署1024个节点）
 * 最后 12 位是毫秒内的计数（12 位的计数顺序号支持每个节点每毫秒产生 4096 个 ID 序号）。
 *
 * @author Huang Z.Y.
 */
public class SnowflakeIdWorker {
    /**
     * 开始时间截 (2015-01-01)
     */
    private final long twepoch = 1489111610226L;

    /**
     * 机器 ID 所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识 ID 所占的位数
     */
    private final long dataCenterIdBits = 5L;

    /**
     * 支持的最大机器 ID，结果是 31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识 ID，结果是 31
     */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * 序列在 ID 中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器 ID 向左移 12 位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 时间截向左移 22 位 (5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 数据中心 ID (0~31)
     */
    private final long dataCenterId;
    /**
     * 工作机器ID(0~31)
     */
    private final long workerId;
    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成 ID 的时间截
     */
    private long lastTimestamp = -1L;


    /**
     * 构造函数。
     *
     * @param workerId     工作ID (0~31)
     * @param dataCenterId 数据中心ID (0~31)
     */
    public SnowflakeIdWorker(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 获得下一个ID (线程安全)。
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次 ID 生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            // 每次+1
            /**
             * 生成序列的掩码，4095 (0b111111111111=0xfff=4095)
             */
            long sequenceMask = -1L ^ (-1L << sequenceBits);
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;
        /**
         * 数据标识 ID 向左移 17 位 (12+5)
         */
        long dataCenterIdShift = sequenceBits + workerIdBits;
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (dataCenterId << dataCenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳。
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }


    /**
     * 返回以毫秒为单位的当前时间。
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}

