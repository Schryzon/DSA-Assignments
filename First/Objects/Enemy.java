package Objects;

/**
 * Enemy represents a foe (Heartless/Nobody).
 * Children must define attack pattern and special on_spell_hit reaction.
 */
public abstract class Enemy {
    public final String species;
    public int hp;

    /**
     * @param species Display name / type
     * @param hp      Hit points
     */
    public Enemy(String species, int hp) {
        this.species = species;
        this.hp = hp;
    }

    public boolean is_down() { return hp <= 0; }

    /**
     * Receive damage.
     * @param dmg Damage amount
     */
    public void receive_damage(int damage) {
        KHLogger.log(species + " takes " + damage + " damage.");
        hp -= damage;
        if(hp < 0) hp = 0;
        KHLogger.log(species + " HP = " + hp);
    }

    /**
     * Enemy attacks a wielder.
     * @param wielder Keyblade_Wielder target (abstract parent A)
     */
    public abstract void attack(Keyblade_Wielder wielder);

    /**
     * Called when a spell hits this enemy (for special resistances/weaknesses).
     * @param spell  The spell object (child of Spell)
     * @param caster The casting wielder (abstract parent A)
     */
    public abstract void on_spell_hit(Spell spell, Keyblade_Wielder caster);
}