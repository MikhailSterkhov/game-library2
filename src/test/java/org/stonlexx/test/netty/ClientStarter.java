package org.stonlexx.test.netty;

import io.netty.channel.socket.nio.NioSocketChannel;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.NettyManager;
import org.stonlexx.gamelibrary.core.netty.builder.NettyClientBuilder;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.reconnect.server.impl.NettyPrintableReconnect;

import java.util.function.Consumer;

public class ClientStarter {

    public static void main(String[] args) {
        NettyManager nettyManager = GameLibrary.getInstance().getNettyManager();

        NettyClientBuilder.newLocalClientBuilder(1337, String.class)
                .standardCodec()
                .reconnectHandler(new NettyPrintableReconnect("Reconnecting to server..."))

                .futureListener((channelFuture, isSuccess) -> {
                    if (isSuccess) {
                        GameLibrary.getInstance().getLogger().info("Channel " + channelFuture.channel().remoteAddress() + " was success connected to server!");

                        nettyManager.sendPacket(new TestPacket());
                        return;
                    }

                    GameLibrary.getInstance().getLogger().info("Channel " + channelFuture.channel().remoteAddress() + " failed to connect!");
                })

                .channelInitializer((Consumer<NioSocketChannel>) null)
                .acceptPacketTyping(nettyPacketTyping -> nettyPacketTyping.setPacketKeyHandler(Class::getSimpleName))

                .registerPacket(NettyPacketDirection.TO_CLIENT, TestPacket.class)
                .registerPacket(NettyPacketDirection.TO_SERVER, TestPacket.class)

                .connectToServer();
    }

}
