# research
* Spring Aop and Ioc
* Springboot
* JVM: about java concurrent、jmm、threadpool
* Btrace: online bugfix
* Disruptor: simple use of disruptor
* Prometheus、Flume
* Mongodb
* Redis
* Mycat
* Protobuff
* Flink & Kafka

# Jvm
* 堆     线程共享 Xms Xmx
  * 新生代 Xmn minorgc
    * Eden
    * S0
    * S1
  * 老生代
* 方法区 线程共享
  * 类信息、常量信息、方法数据、方法代码
  * jdk1.8 静态变量-->堆
  * jdk1.8 池化string对象-->堆
  * jdk1.8 符号引用-->native堆
* PC寄存器 程序计数器
* 本地方法栈
* 虚拟机栈 