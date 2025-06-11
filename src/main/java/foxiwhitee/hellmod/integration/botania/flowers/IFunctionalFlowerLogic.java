package foxiwhitee.hellmod.integration.botania.flowers;

public interface IFunctionalFlowerLogic {
    int getDefaultNeedTicks();
    long getManaNeed();
    void use(IFunctionalMana tile);

    default boolean canUse() {
        return true;
    }

}
