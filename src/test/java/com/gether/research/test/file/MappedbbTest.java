package com.gether.research.test.file;

import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * 直接读536870910大小的物理文件，耗时892996毫秒。
 * 0: 读(236529489:9)=9(和:2415919095, 耗时:1097ms)
 * 1: 读(347856019:9)=9(和:2415919095, 耗时:513ms)
 * 2: 读(5762405:5)=5(和:2415919095, 耗时:499ms)
 * 3: 读(287474212:2)=2(和:2415919095, 耗时:501ms)
 * 4: 读(527185516:6)=6(和:2415919095, 耗时:512ms)
 * 5: 读(506178607:7)=7(和:2415919095, 耗时:522ms)
 * 6: 读(125292134:4)=4(和:2415919095, 耗时:516ms)
 * 7: 读(140428072:2)=2(和:2415919095, 耗时:521ms)
 * 8: 读(119337139:9)=9(和:2415919095, 耗时:524ms)
 * 9: 读(359881427:7)=7(和:2415919095, 耗时:536ms)
 * 10: 读(326841788:8)=8(和:2415919095, 耗时:548ms)
 * 11: 读(221739629:9)=9(和:2415919095, 耗时:537ms)
 * 12: 读(495639759:9)=9(和:2415919095, 耗时:543ms)
 * 13: 读(342997410:0)=0(和:2415919095, 耗时:549ms)
 * 14: 读(230151043:3)=3(和:2415919095, 耗时:556ms)
 * 15: 读(311368002:2)=2(和:2415919095, 耗时:561ms)
 * 16: 读(91712825:5)=5(和:2415919095, 耗时:580ms)
 * 17: 读(31801132:2)=2(和:2415919095, 耗时:572ms)
 * 18: 读(92321726:6)=6(和:2415919095, 耗时:582ms)
 * 19: 读(165950492:2)=2(和:2415919095, 耗时:580ms)
 * 20: 读(72115759:9)=9(和:2415919095, 耗时:592ms)
 * 21: 读(440562559:9)=9(和:2415919095, 耗时:597ms)
 * 22: 读(314527676:6)=6(和:2415919095, 耗时:617ms)
 * 23: 读(507369266:6)=6(和:2415919095, 耗时:607ms)
 * 24: 读(276866296:6)=6(和:2415919095, 耗时:612ms)
 * 25: 读(246394228:8)=8(和:2415919095, 耗时:614ms)
 * 26: 读(382905864:4)=4(和:2415919095, 耗时:695ms)
 * 27: 读(93413622:2)=2(和:2415919095, 耗时:627ms)
 * 28: 读(96191183:3)=3(和:2415919095, 耗时:1134ms)
 * 29: 读(118887246:6)=6(和:2415919095, 耗时:482ms)
 * 30: 读(155920110:0)=0(和:2415919095, 耗时:497ms)
 * 31: 读(377496817:7)=7(和:2415919095, 耗时:492ms)
 * 32: 读(229027230:0)=0(和:2415919095, 耗时:508ms)
 * 33: 读(100986888:8)=8(和:2415919095, 耗时:503ms)
 * 34: 读(489614216:6)=6(和:2415919095, 耗时:523ms)
 * 35: 读(11352214:4)=4(和:2415919095, 耗时:519ms)
 * 36: 读(249491485:5)=5(和:2415919095, 耗时:649ms)
 * 37: 读(446926927:7)=7(和:2415919095, 耗时:555ms)
 * 38: 读(22343090:0)=0(和:2415919095, 耗时:542ms)
 * 39: 读(105901139:9)=9(和:2415919095, 耗时:544ms)
 * 40: 读(316933051:1)=1(和:2415919095, 耗时:551ms)
 * 41: 读(524220438:8)=8(和:2415919095, 耗时:553ms)
 * 42: 读(229504623:3)=3(和:2415919095, 耗时:556ms)
 * 43: 读(70095540:0)=0(和:2415919095, 耗时:572ms)
 * 44: 读(61974027:7)=7(和:2415919095, 耗时:579ms)
 * 45: 读(356770746:6)=6(和:2415919095, 耗时:608ms)
 * 46: 读(78149836:6)=6(和:2415919095, 耗时:592ms)
 * 47: 读(300767118:8)=8(和:2415919095, 耗时:592ms)
 * 48: 读(263179250:0)=0(和:2415919095, 耗时:599ms)
 * 49: 读(248161766:6)=6(和:2415919095, 耗时:595ms)
 * 50: 读(31083445:5)=5(和:2415919095, 耗时:605ms)
 * 51: 读(300091767:7)=7(和:2415919095, 耗时:610ms)
 * 52: 读(374369352:2)=2(和:2415919095, 耗时:612ms)
 * 53: 读(23172334:4)=4(和:2415919095, 耗时:632ms)
 * 54: 读(509875242:2)=2(和:2415919095, 耗时:618ms)
 * 55: 读(152688452:2)=2(和:2415919095, 耗时:633ms)
 * 56: 读(448592967:7)=7(和:2415919095, 耗时:635ms)
 * 57: 读(110876929:9)=9(和:2415919095, 耗时:638ms)
 * 58: 读(179620395:5)=5(和:2415919095, 耗时:651ms)
 * 59: 读(347480999:9)=9(和:2415919095, 耗时:650ms)
 * 60: 读(402200435:5)=5(和:2415919095, 耗时:648ms)
 * 61: 读(406162883:3)=3(和:2415919095, 耗时:659ms)
 * 62: 读(286286422:2)=2(和:2415919095, 耗时:751ms)
 * 63: 读(282439910:0)=0(和:2415919095, 耗时:702ms)
 * 64: 读(142437704:4)=4(和:2415919095, 耗时:817ms)
 * 65: 读(343623841:1)=1(和:2415919095, 耗时:693ms)
 * 66: 读(52643296:6)=6(和:2415919095, 耗时:677ms)
 * 67: 读(285705956:6)=6(和:2415919095, 耗时:688ms)
 * 68: 读(304459373:3)=3(和:2415919095, 耗时:720ms)
 * 69: 读(322057919:9)=9(和:2415919095, 耗时:699ms)
 * 70: 读(127722394:4)=4(和:2415919095, 耗时:702ms)
 * 71: 读(258544866:6)=6(和:2415919095, 耗时:710ms)
 * 72: 读(402510713:3)=3(和:2415919095, 耗时:784ms)
 * 73: 读(77538222:2)=2(和:2415919095, 耗时:718ms)
 * 74: 读(98976565:5)=5(和:2415919095, 耗时:722ms)
 * 75: 读(59519474:4)=4(和:2415919095, 耗时:837ms)
 * 76: 读(430356364:4)=4(和:2415919095, 耗时:733ms)
 * 77: 读(111008609:9)=9(和:2415919095, 耗时:747ms)
 * 78: 读(170796371:1)=1(和:2415919095, 耗时:744ms)\n
 * 79: 读(362921191:1)=1(和:2415919095, 耗时:751ms)
 * 80: 读(21995612:2)=2(和:2415919095, 耗时:746ms)
 * 81: 读(203389241:1)=1(和:2415919095, 耗时:772ms)
 * 82: 读(299031104:4)=4(和:2415919095, 耗时:770ms)
 * 83: 读(253673218:8)=8(和:2415919095, 耗时:769ms)
 * 84: 读(88206816:6)=6(和:2415919095, 耗时:787ms)
 * 85: 读(285038155:5)=5(和:2415919095, 耗时:785ms)
 * 86: 读(273324321:1)=1(和:2415919095, 耗时:786ms)
 * 87: 读(340660777:7)=7(和:2415919095, 耗时:792ms)
 * 88: 读(39177174:4)=4(和:2415919095, 耗时:795ms)
 * 89: 读(536307878:8)=8(和:2415919095, 耗时:806ms)
 * 90: 读(28164607:7)=7(和:2415919095, 耗时:804ms)
 * 91: 读(270446041:1)=1(和:2415919095, 耗时:808ms)
 * 92: 读(486170236:6)=6(和:2415919095, 耗时:822ms)
 * 93: 读(504366510:0)=0(和:2415919095, 耗时:821ms)
 * 94: 读(244519988:8)=8(和:2415919095, 耗时:823ms)
 * 95: 读(85908386:6)=6(和:2415919095, 耗时:881ms)
 * 96: 读(208242031:1)=1(和:2415919095, 耗时:854ms)
 * 97: 读(40114644:4)=4(和:2415919095, 耗时:847ms)
 * 98: 读(195330978:8)=8(和:2415919095, 耗时:852ms)
 * 99: 读(102151719:9)=9(和:2415919095, 耗时:854ms)
 */
public class MappedbbTest {

  public static void main(String[] args) throws Exception {
    final int FILE_SIZE = 1024 * 1024 * 1024;
    final int FILE_SIZE_HALF = FILE_SIZE / 20 * 10;//确保可被10整除
    File f = new File("/tmp/tmp.log");
    if (!f.exists()) {
      f.getParentFile().mkdirs();
      FileWriter fw = new FileWriter(f);
      final char[] cs = new char[1];
      for (int i = 0; i < FILE_SIZE; i++) {
        cs[0] = (char) (0x30 + (i % 10));
        fw.write(cs);
      }
      fw.close();
    }

    RandomAccessFile raf = new RandomAccessFile(f, "rw");
//    {
//      raf.seek(FILE_SIZE_HALF);
//      long c = 0;
//      long b = System.currentTimeMillis();
//      for (int x = 0; x < FILE_SIZE_HALF; x++) {
//        c += raf.readByte() - 0x30;
//      }
//      b = System.currentTimeMillis() - b;
//      System.out.println(String.format("直接读%s大小的物理文件，耗时%s毫秒。", FILE_SIZE_HALF, b, c));
//    }

    FileChannel channel = raf.getChannel();
    for (int i = 0; i < 100; i++) {
      MappedByteBuffer buf = channel.map(MapMode.READ_ONLY, FILE_SIZE_HALF, FILE_SIZE_HALF);
      long c = 0;
      long b = System.currentTimeMillis();
      for (int x = 0; x < FILE_SIZE_HALF; x++) {
        c += buf.get(x) - 0x30;
      }
      b = System.currentTimeMillis() - b;
      System.out.println(String.format("bmm读%s大小的物理文件，耗时%s毫秒。", FILE_SIZE_HALF, b, c));
      Thread.sleep(1000);
    }
    raf.close();
  }
}