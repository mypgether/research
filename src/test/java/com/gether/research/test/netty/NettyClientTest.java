package com.gether.research.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientTest {

  private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

  public static void main(String[] args) {
    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new StringDecoder());
            ch.pipeline().addLast(new StringEncoder());
            ch.pipeline().addLast(new NettyClientHandler());
          }
        });
    try {
      ChannelFuture future = bootstrap.connect("127.0.0.1", 9999).sync();
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      group.shutdownGracefully();
    }
  }


  public static class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      for (int i = 0; i < 100; i++) {
        String msg = "hello from client " + i;
        ctx.writeAndFlush(msg + System.getProperty("line.separator"));
      }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception {
      String body = (String) msg;
      logger.info("client received msg: {}", body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception {
      logger.error("client caught exception", cause);
      ctx.close();
    }
  }
}