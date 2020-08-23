package org.stonlexx.test;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.GameLibraryCore;
import org.stonlexx.gamelibrary.core.netty.CoreNettyBootstrap;

import java.net.SocketAddress;

public class TestStarter {

    public static void main(String[] args) throws Exception {
        //LibraryCoreConfiguration coreConfiguration = GameLibrary.INSTANCE.getLibraryCore().getCoreConfiguration();
        //ConfigurationApplicationContext applicationContext = coreConfiguration.getApplicationContext();
        //
        //TestPlayer testPlayer = applicationContext.getBean("testPlayer", TestPlayer.class);
        //testPlayer.test();
        //
        //applicationContext.destroy();

        GameLibraryCore gameLibraryCore = GameLibrary.getInstance().getLibraryCore();
        CoreNettyBootstrap nettyBootstrap = gameLibraryCore.getNettyManager().getNettyBootstrap();

        SocketAddress socketAddress = nettyBootstrap.createSocketAddress("localhost", 1010);

        ChannelInitializer<SocketChannel> channelInitializer = nettyBootstrap.createChannelInitializer(null);
        ChannelFutureListener channelFutureListener = nettyBootstrap.createFutureListener((channelFuture, isSuccess) -> {

            if (isSuccess) {
                GameLibrary.getInstance().getLogger().info("Channel " + channelFuture.channel().remoteAddress() + " was success connected!");
                return;
            }

            GameLibrary.getInstance().getLogger().info("Channel " + channelFuture.channel().remoteAddress() + " has failed to connect!");
        });

        nettyBootstrap.createServerBootstrap(socketAddress, channelFutureListener, channelInitializer);
    }

}
