package org.stonlexx.gamelibrary.core.netty.handler.client.inactive;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class NettyClientInactiveHandler extends ChannelInboundHandlerAdapter {

    private final Collection<AbstractNettyClientInactive> nettyClientInactiveCollection;


    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        for (AbstractNettyClientInactive nettyClientInactive : nettyClientInactiveCollection)
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
