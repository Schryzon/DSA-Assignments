package Objects;

/**
 * Spell is an ADT for magic.
 * Children define cost and how they apply. They can accept both
 * Keyblade_Wielder (caster) and either Enemy or Keyblade_Wielder as target.
 */
public abstract class Spell {
    public final String name;
    public final int mp_cost;

    /**
     * @param name    Spell name
     * @param mp_cost Mana cost
     */
    public Spell(String name, int mp_cost) {
        this.name = name;
        this.mp_cost = mp_cost;
    }

    /**
     * Apply spell effect. Target can vary by spell:
     * - Offensive: target should be Enemy
     * - Support: target should be Keyblade_Wielder (may be the caster)
     *
     * @param caster Keyblade_Wielder
     * @param target Object (expected Enemy or Keyblade_Wielder; can be null if spell allows)
     */
    public abstract void apply(Keyblade_Wielder caster, Object target);
}