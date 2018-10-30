package com.lc.msf.client.factory;

import com.lc.msf.client.ClientHandler;
import com.lc.msf.client.Server;
import com.lc.msf.client.ServerRouter;
import com.lc.msf.protocol.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

public class MsfProxyFactory {
    private static ConcurrentHashMap<String, Object> proxyMap = new ConcurrentHashMap<>();

    public static <T> T create(Class<?> clazz) {
        String key = clazz.getName();
        Object proxy;
        if (proxyMap.containsKey(key)) {
            proxy = proxyMap.get(key);
        } else {
            proxy = Proxy.newProxyInstance(clazz.getClassLoader(),
                    new Class<?>[]{clazz}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            RpcRequest rpcRequest = new RpcRequest();
                            rpcRequest.setClassName(method.getDeclaringClass().getName());
                            rpcRequest.setMethodName(method.getName());
                            rpcRequest.setTypes(method.getParameterTypes());
                            rpcRequest.setParams(args);

                            ClientHandler handler = new ClientHandler();

                            EventLoopGroup workgroup = new NioEventLoopGroup();
                            Bootstrap bootstrap = new Bootstrap();
                            bootstrap.group(workgroup);
                            bootstrap.channel(NioSocketChannel.class);
                            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel sc) throws Exception {
                                    ChannelPipeline pipeline = sc.pipeline();
                                    // 定长传输解决拆包粘包问题
                                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                                    // 序列化反序列化
                                    pipeline.addLast(new ObjectEncoder());
                                    pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                                    pipeline.addLast(handler);
                                }
                            });

                            Server server = new ServerRouter().getServer();

                            ChannelFuture cf = bootstrap.connect(server.getIp(), server.getPort()).sync();
                            cf.channel().writeAndFlush(rpcRequest);
                            cf.channel().closeFuture().sync();
                            workgroup.shutdownGracefully();
                            return handler.getResponse();
                        }
                    });
        }
        if (proxy != null) {
            proxyMap.put(key, proxy);
        }
        return (T) proxy;
    }
}
