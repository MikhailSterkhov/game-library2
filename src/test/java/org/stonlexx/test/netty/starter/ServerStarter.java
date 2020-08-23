package org.stonlexx.test.netty.starter;

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
import org.stonlexx.test.netty.TestPacket;

import java.net.SocketAddress;

public class ServerStarter {

    public static void main(String[] args) {
        NettyManager nettyManager = GameLibrary.getInstance().getLibraryCore().getNettyManager();
        NettyBootstrap nettyBootstrap = nettyManager.getNettyBootstrap();

        registerPackets(nettyManager);

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
    }

    private static void registerPackets(NettyManager nettyManager) {
        nettyManager.registerPacket(nettyManager.getPacketCodecManager().getNettyPacketTyping(), NettyPacketDirection.TO_CLIENT, TestPacket.class, 0x00);
        nettyManager.registerPacket(nettyManager.getPacketCodecManager().getNettyPacketTyping(), NettyPacketDirection.TO_SERVER, TestPacket.class, 0x00);
    }

}
