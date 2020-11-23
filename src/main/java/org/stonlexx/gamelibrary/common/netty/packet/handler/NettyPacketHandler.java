package org.stonlexx.gamelibrary.common.netty.packet.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.stonlexx.gamelibrary.common.netty.packet.NettyPacket;

public class NettyPacketHandler extends SimpleChannelInboundHandler<NettyPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, NettyPacket nettyPacket) {
        nettyPacket.handle(channelHandlerContext.channel());
    }

}
