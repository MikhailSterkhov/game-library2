package org.stonlexx.gamelibrary.core.netty.handler.server.active;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class NettyServerActiveHandler extends ChannelInboundHandlerAdapter {

    private final Collection<AbstractNettyServerActive> nettyServerActiveCollection;


    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        for (AbstractNettyServerActive nettyServerActive : nettyServerActiveCollection)
            nettyServerActive.onServerActive(channelHandlerContext.channel());
    }

}
