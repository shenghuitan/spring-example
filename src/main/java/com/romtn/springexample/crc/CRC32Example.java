package com.romtn.springexample.crc;

import com.romtn.springexample.util.Timer;
import com.romtn.springexample.util.Uuid;
import org.apache.kafka.common.utils.Crc32C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.zip.CRC32;

public class CRC32Example {

    private static Logger logger = LoggerFactory.getLogger(CRC32Example.class);

    String source = "1";

    public void test() {
        byte[] bytes = source.getBytes();
        long compute = Crc32C.compute(bytes, 0, bytes.length);
        long expect = Long.parseLong("83DCEFB7", 16);
        logger.info("--- 0 source:{}, compute:{}, expect:{}, eq:{}", source, compute, expect, compute == expect);

        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        compute = crc32.getValue();
        logger.info("--- 1 source:{}, compute:{}, expect:{}, eq:{}", source, compute, expect, compute == expect);
    }

    public void hashCode(int limit) {
        Timer t0 = Timer.init();
        for (int i = 0; i < limit; i++) {
            Uuid.uuid().hashCode();
        }
        t0.mark();
        logger.info("--- hashCode limit:{}, cost:{}", limit, t0.costs());
    }

    public void crc32(int limit) {
        Timer t0 = Timer.init();
        CRC32 crc32 = new CRC32();
        for (int i = 0; i < limit; i++) {
            crc32.reset();
            crc32.update(Uuid.uuid().getBytes());
            crc32.getValue();
        }
        t0.mark();
        logger.info("--- crc32 limit:{}, cost:{}", limit, t0.costs());
    }

    public static void main(String[] args) {
        CRC32Example example = new CRC32Example();
        example.test();

        int limit = 100_000_000;
        int loop = 8;

        new Thread(() -> {
            for (int i = 0; i < loop; i++) {
                example.hashCode(limit);
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < loop; i++) {
                example.crc32(limit);
            }
        }).start();
    }

}

/*
需要校验的数据：Hex      Ascii
1
输入的数据为ascii字符串，例如：1234
参数模型 NAME：
CRC-32　　　　　　　　x32+x26+x23+x22+x16+x12+x11+x10+x8+x7+x5+x4+x2+x+1
宽度 WIDTH：
32
多项式 POLY（Hex）：
04C11DB7
例如：3D65
初始值 INIT（Hex）：
FFFFFFFF
例如：FFFF
结果异或值 XOROUT（Hex）：
FFFFFFFF
例如：0000
输入数据反转（REFIN）      输出数据反转（REFOUT）

校验计算结果（Hex）：
83DCEFB7
高位在左低位在右，使用时请注意高低位顺序！！！
校验计算结果（Bin）：
10000011110111001110111110110111
 */
