package org.stonlexx.test.netty;

import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.CoreLogger;
import org.stonlexx.gamelibrary.core.netty.bootstrap.NettyBootstrap;
import org.stonlexx.gamelibrary.core.netty.bootstrap.impl.NettyServer;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.handler.client.active.impl.NettyConsumerClientActive;
import org.stonlexx.gamelibrary.core.netty.handler.client.inactive.impl.NettyConsumerClientInactive;
import org.stonlexx.test.netty.packet.CallbackTestPacket;
import org.stonlexx.test.netty.packet.IntTestPacket;
import org.stonlexx.test.netty.packet.StringTestPacket;

public class ServerStarter {

    public static void main(String[] args) {
        CoreLogger coreLogger = GameLibrary.getInstance().getLogger();

        NettyBootstrap nettyBootstrap = GameLibrary.getInstance().getNettyManager().getNettyBootstrap();
        NettyServer nettyServer = nettyBootstrap.createLocalServer(1337);

        // парочка классных уникальных фич для серверного бутстрапа
        nettyServer.addNettyClientInactive(new NettyConsumerClientInactive(channel -> coreLogger.info(channel.remoteAddress() + " has been disconnected!")));
        nettyServer.addNettyClientActive(new NettyConsumerClientActive(channel -> coreLogger.info(channel.remoteAddress() + " was success connected! (id:" + nettyServer.getClientChannelMap().size() + ")")));

        // установка стандартных кодеков, если нужно установить свои,
        //то это можно сделать через setChannelInitializer()
        nettyServer.setStandardCodec();

        nettyServer.setChannelFutureListener((channelFuture, isSuccess) -> {
            if (isSuccess) {
                GameLibrary.getInstance().getLogger().info("Server " + channelFuture.channel().localAddress() + " was success bind!");

                return;
            }

            GameLibrary.getInstance().getLogger().info("Server failed to bind!");
            channelFuture.cause().printStackTrace();
        });


        // можно регистрировать пакеты по разным ключам
        //регистрация с автоопределением ключа по названию класса пакета
        nettyServer.createPacketRegistry(String.class, Class::getSimpleName, packetRegistry -> {

            packetRegistry.registerPacket(NettyPacketDirection.CALLBACK, CallbackTestPacket.class);
            packetRegistry.registerPacket(NettyPacketDirection.GLOBAL, StringTestPacket.class);
        });

        //регистрация без автоопределения ключа по указанному самостоятельно номеру пакета
        nettyServer.createPacketRegistry(int.class, null, packetRegistry -> {

            packetRegistry.registerPacket(NettyPacketDirection.ONLY_DECODE, IntTestPacket.class, 0x01);
            packetRegistry.registerPacket(NettyPacketDirection.ONLY_ENCODE, IntTestPacket.class, 0x01);
        });

        //автоматический скан и регистрация пакетов по аннотации @PacketAutoRegister
        nettyServer.autoRegisterPackets("org.stonlexx.test.netty.packet");

        nettyServer.bind();
    }

}
