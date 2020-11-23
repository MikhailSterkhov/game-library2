package org.stonlexx.test.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.stonlexx.gamelibrary.GameLibrary;

public class TestConnectionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        GameLibrary.getInstance().getLogger().info("server channel active!");
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        GameLibrary.getInstance().getLogger().info("server channel inactive.");
    }

}
