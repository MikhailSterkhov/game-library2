package org.stonlexx.gamelibrary.core.netty.reconnect.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class NettyClientInactiveHandler extends ChannelInboundHandlerAdapter {

    private final AbstractNettyClientInactive nettyClientInactive;


    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        nettyClientInactive.onClientInactive(channelHandlerContext.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        if (throwable instanceof IOException) {
            return;
        }

        throwable.printStackTrace();
    }

}
