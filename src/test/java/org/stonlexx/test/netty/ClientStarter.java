package org.stonlexx.test.netty;

import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.bootstrap.NettyBootstrapChannelAttribute;
import org.stonlexx.gamelibrary.core.netty.builder.NettyClientBuilder;
import org.stonlexx.gamelibrary.core.netty.packet.codec.impl.StandardNettyPacketDecoder;
import org.stonlexx.gamelibrary.core.netty.packet.codec.impl.StandardNettyPacketEncoder;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.test.bean.TestPlayer;

public class ClientStarter {

    public static void main(String[] args) {
        NettyClientBuilder.newClientBuilder("localhost", 1337, String.class)

                .futureListener((channelFuture, isSuccess) -> {
                    if (isSuccess) {
                        GameLibrary.getInstance().getLogger().info("Channel " + channelFuture.channel().localAddress() + " was success bind!");

                        return;
                    }

                    GameLibrary.getInstance().getLogger().info("Channel " + channelFuture.channel().localAddress() + " failed to bind!");
                })
                .channelInitializer(nioSocketChannel -> {

                    nioSocketChannel.pipeline().addLast("packet-decoder", new StandardNettyPacketDecoder());
                    nioSocketChannel.pipeline().addLast("packet-encoder", new StandardNettyPacketEncoder());
                })
                .bootstrapAttributes(
                        NettyBootstrapChannelAttribute.create("player", new TestPlayer())
                )

                .acceptPacketTyping(nettyPacketTyping -> nettyPacketTyping.setPacketKeyHandler(Class::getSimpleName))
                .registerPacket(NettyPacketDirection.TO_CLIENT, TestPacket.class)
                .registerPacket(NettyPacketDirection.TO_SERVER, TestPacket.class)

                .connectToServer();
    }

}
