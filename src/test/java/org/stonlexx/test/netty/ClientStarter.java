package org.stonlexx.test.netty;

import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.CoreLogger;
import org.stonlexx.gamelibrary.core.netty.bootstrap.NettyBootstrap;
import org.stonlexx.gamelibrary.core.netty.bootstrap.impl.NettyClient;
import org.stonlexx.gamelibrary.core.netty.handler.server.active.impl.NettyConsumerServerActive;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.handler.server.reconnect.impl.NettyPrintableReconnect;
import org.stonlexx.test.netty.packet.IntTestPacket;
import org.stonlexx.test.netty.packet.StringTestPacket;

public class ClientStarter {

    public static void main(String[] args) {
        CoreLogger coreLogger = GameLibrary.getInstance().getLogger();

        NettyBootstrap nettyBootstrap = GameLibrary.getInstance().getNettyManager().getNettyBootstrap();
        NettyClient nettyClient = nettyBootstrap.createLocalClient(1337);

        // парочка классных уникальных фич для клиентского бутстрапа
        nettyClient.addNettyServerActive(new NettyConsumerServerActive(channel -> coreLogger.info("Server " + channel.remoteAddress() + " activated!")));
        nettyClient.setNettyReconnect(new NettyPrintableReconnect("Reconnecting to server..."));

        // установка стандартных кодеков, если нужно установить свои,
        //то это можно сделать через setChannelInitializer()
        nettyClient.setStandardCodec();

        nettyClient.setChannelFutureListener((channelFuture, isSuccess) -> {
            if (isSuccess) {
                GameLibrary.getInstance().getLogger().info("Client " + channelFuture.channel().localAddress() + " was success connected to server!");

                // nettyClient.sendPacketToServer(new TestPacket());
                return;
            }

            GameLibrary.getInstance().getLogger().info("Client failed was connecting to server!");
        });


        // можно регистрировать пакеты по разным ключам
        //регистрация с автоопределением ключа по названию класса пакета
        nettyClient.createPacketRegistry(String.class, Class::getSimpleName, packetRegistry -> {

            packetRegistry.registerPacket(NettyPacketDirection.TO_SERVER, StringTestPacket.class);
            packetRegistry.registerPacket(NettyPacketDirection.TO_CLIENT, StringTestPacket.class);
        });

        //регистрация без автоопределения ключа по указанному самостоятельно номеру пакета
        nettyClient.createPacketRegistry(int.class, null, packetRegistry -> {

            packetRegistry.registerPacket(NettyPacketDirection.TO_SERVER, IntTestPacket.class, 0x01);
            packetRegistry.registerPacket(NettyPacketDirection.TO_CLIENT, IntTestPacket.class, 0x01);
        });

        nettyClient.connect();
    }

}
