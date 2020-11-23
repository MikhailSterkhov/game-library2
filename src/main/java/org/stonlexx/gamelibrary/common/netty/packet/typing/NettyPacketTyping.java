package org.stonlexx.gamelibrary.common.netty.packet.typing;

import lombok.*;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.stonlexx.gamelibrary.common.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.common.netty.packet.annotation.PacketAutoRegister;
import org.stonlexx.gamelibrary.common.netty.packet.mapping.NettyPacketMapper;
import org.stonlexx.gamelibrary.utility.query.ResponseHandler;

import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NettyPacketTyping<T> {

    private final String typingName;


    public NettyPacketMapper<T> packetMapper = new NettyPacketMapper<>();
    private Class<T> packetKeyClass;

    @Setter
    private ResponseHandler<T, Class<? extends NettyPacket>> packetKeyHandler;


// ================================================================================================================== //

    @Getter
    private static final Map<String, NettyPacketTyping<?>> packetTypingMap = new HashMap<>();


    public static <T> NettyPacketTyping<T> createPacketTyping(@NonNull Class<T> packetKeyClass,
                                                              @NonNull String typingName) {

        return createPacketTyping(packetKeyClass, typingName, null);
    }

    public static <T> NettyPacketTyping<T> createPacketTyping(@NonNull Class<T> packetKeyClass,
                                                              @NonNull String typingName,

                                                              ResponseHandler<T, Class<? extends NettyPacket>> packetKeyHandler) {

        NettyPacketTyping<T> nettyPacketTyping = (NettyPacketTyping<T>) packetTypingMap.computeIfAbsent(typingName.toLowerCase(), NettyPacketTyping::new);
        nettyPacketTyping.packetKeyHandler = packetKeyHandler;
        nettyPacketTyping.packetKeyClass = packetKeyClass;

        return nettyPacketTyping;
    }

// ================================================================================================================== //


    /**
     * Зарегистрировать все пакеты, которые
     * имеют аннотацию {@link org.stonlexx.gamelibrary.common.netty.packet.annotation.PacketAutoRegister}
     * и хранятся в указанном пакейдже
     *
     * @param packageName - имя пакейджа для скана
     */
    public void autoRegisterPackets(@NonNull String packageName) {
        List<ClassLoader> classLoadersList = new LinkedList<>();

        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))

                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

        for (Class<? extends NettyPacket> packetClass : reflections.getSubTypesOf(NettyPacket.class)) {
            PacketAutoRegister packetAutoRegister = packetClass.getDeclaredAnnotation(PacketAutoRegister.class);

            if (packetAutoRegister == null) {
                continue;
            }

            String packetId = packetAutoRegister.id();
            NettyPacketDirection nettyPacketDirection = packetAutoRegister.direction();

            registerPacket(nettyPacketDirection, packetClass, (T) packetId);
        }
    }

    /**
     * Зарегистрировать пакет в маппере
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс регистрируемого пакета
     * @param packetId - номер пакета для регистрации
     */
    public void registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                               @NonNull Class<? extends NettyPacket> nettyPacketClass,

                               T packetId) {

        packetMapper.registerPacket(nettyPacketDirection, nettyPacketClass, packetId);
    }

    /**
     * Зарегистрировать пакет в маппере
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс регистрируемого пакета
     */
    public void registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                               @NonNull Class<? extends NettyPacket> nettyPacketClass) {

        packetMapper.registerPacket(nettyPacketDirection, nettyPacketClass, packetKeyHandler);
    }

    /**
     * Получить зарегистрированный пакет по его номеру
     *
     * @param nettyPacketDirection - директория пакета
     * @param packetId - номер получаемого пакета
     */
    public NettyPacket getNettyPacket(@NonNull NettyPacketDirection nettyPacketDirection, T packetId) {
        return packetMapper.getNettyPacket(nettyPacketDirection, packetId);
    }
}
