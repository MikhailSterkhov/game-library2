package org.stonlexx.gamelibrary.core.netty.handler.server.reconnect;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class NettyServerReconnectHandler extends ChannelInboundHandlerAdapter {

    private final AbstractNettyReconnect nettyReconnect;
    private ExceptionSession exceptionSession;


    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelInactive(channelHandlerContext);

        if (exceptionSession != null && (System.currentTimeMillis() - exceptionSession.sessionMillis) > 1000) {

            exceptionSession.throwable.printStackTrace();
            exceptionSession = null;
            return;
        }

        tryReconnect(channelHandlerContext.channel(), EnumReconnectReason.CHANNEL_INACTIVE, null);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        if (throwable instanceof IOException) {
            exceptionSession = new ExceptionSession(System.currentTimeMillis(), throwable);

            return;
        }

        throwable.printStackTrace();
    }


    public void tryReconnect(@NonNull Channel channel,
                             @NonNull EnumReconnectReason reconnectReason,

                             Throwable throwable) {

        if (nettyReconnect == null) {
            return;
        }

        nettyReconnect.setReason(reconnectReason);
        nettyReconnect.setChannel(channel);
        nettyReconnect.setException(throwable);

        nettyReconnect.handleReconnect();
    }

    @RequiredArgsConstructor
    @Getter
    private static class ExceptionSession {

        private final long sessionMillis;
        private final Throwable throwable;
    }

}
