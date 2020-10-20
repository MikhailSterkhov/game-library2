package org.stonlexx.test.netty;

import io.netty.channel.socket.nio.NioSocketChannel;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.builder.NettyServerBuilder;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.reconnect.client.impl.NettyPrintableClientInactive;

import java.util.function.Consumer;

public class ServerStarter {

    public static void main(String[] args) {
        NettyServerBuilder.newLocalServerBuilder(1337, String.class)
                .standardCodec()
                .clientInactive(new NettyPrintableClientInactive("Client has been disconnected!"))

                .futureListener((channelFuture, isSuccess) -> {
                    if (isSuccess) {
                        GameLibrary.getInstance().getLogger().info("Channel " + channelFuture.channel().localAddress() + " was success bind!");

                        return;
                    }

                    GameLibrary.getInstance().getLogger().info("Channel " + channelFuture.channel().localAddress() + " failed to bind!");
                    channelFuture.cause().printStackTrace();
                })

                .channelInitializer((Consumer<NioSocketChannel>) null)
                .acceptPacketTyping(nettyPacketTyping -> nettyPacketTyping.setPacketKeyHandler(Class::getSimpleName))

                .registerPacket(NettyPacketDirection.TO_CLIENT, TestPacket.class)
                .registerPacket(NettyPacketDirection.TO_SERVER, TestPacket.class)

                .bindServer();
    }

}
