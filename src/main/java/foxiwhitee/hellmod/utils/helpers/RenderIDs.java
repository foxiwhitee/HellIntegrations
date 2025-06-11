package foxiwhitee.hellmod.utils.helpers;

import cpw.mods.fml.client.registry.RenderingRegistry;

public enum RenderIDs {
    ASGARD_MANA_POOL(RenderingRegistry.getNextAvailableRenderId()),
    HELHELM_MANA_POOL(RenderingRegistry.getNextAvailableRenderId()),
    VALHALLA_MANA_POOL(RenderingRegistry.getNextAvailableRenderId()),
    MIDGARD_MANA_POOL(RenderingRegistry.getNextAvailableRenderId()),
    ASGARD_SPREADER(RenderingRegistry.getNextAvailableRenderId()),
    HELHELM_SPREADER(RenderingRegistry.getNextAvailableRenderId()),
    VALHALLA_SPREADER(RenderingRegistry.getNextAvailableRenderId()),
    MIDGARD_SPREADER(RenderingRegistry.getNextAvailableRenderId()),
    BASE_MOLECULAR_ASSEMBLER(RenderingRegistry.getNextAvailableRenderId()),
    HYBRID_MOLECULAR_ASSEMBLER(RenderingRegistry.getNextAvailableRenderId()),
    ULTIMATE_MOLECULAR_ASSEMBLER(RenderingRegistry.getNextAvailableRenderId()),
    DRACONIC_ASSEMBLER(RenderingRegistry.getNextAvailableRenderId()),
    FUSION_CORE(RenderingRegistry.getNextAvailableRenderId()),
    FUSION_INJECTOR(RenderingRegistry.getNextAvailableRenderId()),
    EU_ENERGY_PROVIDER(RenderingRegistry.getNextAvailableRenderId()),
    STABILIZER(RenderingRegistry.getNextAvailableRenderId());

    int id;
    RenderIDs(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
