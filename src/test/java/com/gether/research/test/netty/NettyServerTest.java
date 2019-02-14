package com.gether.research.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author myp
 * @date 2019/2/14 下午3:44
 */
public class NettyServerTest {

  private static Logger logger = LoggerFactory.getLogger(NettyServerTest.class);

  public static void main(String[] args) {
    EventLoopGroup bossgroup = new NioEventLoopGroup();
    EventLoopGroup workgroup = new NioEventLoopGroup();
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap.group(bossgroup, workgroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 1024)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new StringDecoder());
            ch.pipeline().addLast(new StringEncoder());
            ch.pipeline().addLast(new NettyServerHandler());
          }
        });
    try {
      ChannelFuture cf = serverBootstrap.bind(9999).sync();
      cf.channel().closeFuture().sync();
    } catch (InterruptedException ignored) {
    } finally {
      bossgroup.shutdownGracefully();
      workgroup.shutdownGracefully();
    }
  }

  public static class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      String body = (String) msg;

      logger.info("server received msg: {}", body);
      ctx.writeAndFlush(
          String.valueOf(System.currentTimeMillis()) + System.getProperty("line.separator"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      ctx.close();
    }
  }
}
