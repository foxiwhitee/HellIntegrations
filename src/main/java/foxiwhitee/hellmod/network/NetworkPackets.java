package foxiwhitee.hellmod.network;

import foxiwhitee.hellmod.network.packets.*;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum NetworkPackets {
    DEFAULT_PACKET(DefaultPacket.class),
    ADVANCED_INTERFACE_TERMINAL_PACKET(PacketAdvancedInterfaceTerminal.class),
    DRACONIC_ASSEMBLER_PACKET(PacketDraconicAssembler.class),
    DRACONIC_ASSEMBLER_NEI_PACKET(PacketDrAssemblerRecipe.class),
    SPAWN_PARTICLE_Packet(PacketSpawnParticle.class),
    FUSION_CORE_PACKET(PacketFusionCore.class),
    UPDATE_ENERGY_PACKET(PacketUpdateEnergy.class),
    UPDATE_MANA_PACKET(PacketUpdateMana.class);

    private final Class<? extends BasePacket> pktClass;
    private final Constructor<? extends BasePacket> pktConstructor;
    private static final Map<Class<? extends BasePacket>, NetworkPackets> CLASS_LOOKUP = new HashMap<>();

    NetworkPackets(Class<? extends BasePacket> packetClass) {
        this.pktClass = packetClass;
        Constructor<? extends BasePacket> constructor = null;

        try {
            constructor = packetClass.getConstructor(ByteBuf.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalStateException("Packet class " + packetClass.getName() + " must have a ByteBuf constructor", e);
        }

        this.pktConstructor = constructor;
    }

    static {
        for (NetworkPackets packet : values()) {
            CLASS_LOOKUP.put(packet.pktClass, packet);
        }
    }

    public static NetworkPackets fromId(int packetId) {
        return values()[packetId];
    }

    public static NetworkPackets fromClass(Class<? extends BasePacket> packetClass) {
        return CLASS_LOOKUP.get(packetClass);
    }

    public BasePacket createInstance(ByteBuf buffer) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return pktConstructor.newInstance(buffer);
    }
}