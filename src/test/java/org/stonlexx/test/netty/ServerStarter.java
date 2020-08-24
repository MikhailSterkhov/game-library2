package org.stonlexx.test.netty;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.NettyManager;
import org.stonlexx.gamelibrary.core.netty.bootstrap.NettyBootstrap;
import org.stonlexx.gamelibrary.core.netty.bootstrap.NettyBootstrapChannelAttribute;
import org.stonlexx.gamelibrary.core.netty.packet.codec.impl.StandardNettyPacketDecoder;
import org.stonlexx.gamelibrary.core.netty.packet.codec.impl.StandardNettyPacketEncoder;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketTyping;
import org.stonlexx.test.bean.TestPlayer;

import java.net.SocketAddress;

public class ServerStarter {

    public static void main(String[] args) {
        NettyManager nettyManager = GameLibrary.getInstance().getNettyManager();
        NettyBootstrap nettyBootstrap = nettyManager.getNettyBootstrap();

        SocketAddress socketAddress = nettyBootstrap.createSocketAddress("localhost", 1337);

        ChannelFutureListener channelFutureListener = nettyBootstrap.createFutureListener(
                (channelFuture, isSuccess) -> {
                    if (isSuccess) {
                        GameLibrary.getInstance().getLogger().info("Channel " + channelFuture.channel().localAddress() + " was success bind!");

                        return;
                    }

                    GameLibrary.getInstance().getLogger().info("Channel " + channelFuture.channel().localAddress() + " failed to bind!");
                });

        StandardNettyPacketDecoder nettyPacketDecoder = new StandardNettyPacketDecoder();
        StandardNettyPacketEncoder nettyPacketEncoder = new StandardNettyPacketEncoder();

        ChannelInitializer<SocketChannel> channelInitializer = nettyBootstrap.createChannelInitializer(null, nettyPacketDecoder, nettyPacketEncoder);

        // create server bootstrap
        nettyBootstrap.createServerBootstrap(socketAddress, channelFutureListener, channelInitializer,
                /* test attribute */ NettyBootstrapChannelAttribute.create("player", new TestPlayer()),

                /* test attribute */ NettyBootstrapChannelAttribute.create("decoder", nettyPacketDecoder),
                /* test attribute */ NettyBootstrapChannelAttribute.create("encoder", nettyPacketEncoder));

        registerPackets(nettyManager);
    }

    private static void registerPackets(NettyManager nettyManager) {
        NettyPacketTyping nettyPacketTyping = nettyManager.getPacketCodecManager().getNettyPacketTyping();

        nettyPacketTyping.registerPacket(NettyPacketDirection.TO_CLIENT, TestPacket.class, 0x00);
        nettyPacketTyping.registerPacket(NettyPacketDirection.TO_SERVER, TestPacket.class, 0x00);
    }

}
