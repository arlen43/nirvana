package com.arlen.test.netty.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloServer {
	public void start(int port) throws Exception {
		// 1.第一个线程组是用于接收Client端连接的
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// 2.第二个线程组是用于实际的业务处理的
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			// 绑定线程池
			b.group(bossGroup, workerGroup)
					// 指定NIO的模式，如果是客户端就是NioSocketChannel
					.channel(NioServerSocketChannel.class)
					// TCP的缓冲区设置
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							// 注册handler，职责链可以选择性的拦截自己关心的事件
							ch.pipeline().addLast(new HelloServerInHandler());
						}
					});

			// 绑定端口  
			ChannelFuture f = b.bind(port).sync();
			// 等待关闭(程序阻塞在这里等待客户端请求)  
			f.channel().closeFuture().sync();
		} finally {
			// 关闭线程
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		HelloServer server = new HelloServer();
		server.start(8000);
	}

	// 该handler是InboundHandler类型
	public static class HelloServerInHandler extends ChannelInboundHandlerAdapter {

		private final static Logger logger = LoggerFactory.getLogger(HelloServerInHandler.class);

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			logger.info("HelloServerInHandler.channelRead");
			ByteBuf result = (ByteBuf) msg;
			byte[] result1 = new byte[result.readableBytes()];
			// msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
			result.readBytes(result1);
			String resultStr = new String(result1);
			// 接收并打印客户端的信息
			System.out.println("Client said:" + resultStr);
			// 释放资源，这行很关键
			result.release();

			// 向客户端发送消息
			String response = "I am ok!";
			// 在当前场景下，发送的数据必须转换成ByteBuf数组
			ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
			encoded.writeBytes(response.getBytes());
			ctx.write(encoded);
			ctx.flush();
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.flush();
		}
	}
}
