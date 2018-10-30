package com.lc.msf.server;

import com.alibaba.fastjson.JSON;
import com.lc.msf.protocol.RpcRequest;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest request = (RpcRequest) msg;
        System.out.println(JSON.toJSONString(request));
        // TODO 自定义ClassLoader，把服务的具体实现，加载到工厂，这里获取，通过反射调用
        ctx.writeAndFlush("hello");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
