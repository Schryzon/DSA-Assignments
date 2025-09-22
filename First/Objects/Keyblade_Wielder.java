package Objects;

/**
 * Keyblade_Wielder represents a Keyblade wielder ADT.
 * Children must define basic_attack and finisher behavior.
 */
public abstract class Keyblade_Wielder {
    public final String name;
    public int hp;
    public int mp;
    public boolean barrier_up;

    /**
     * @param name Display name
     * @param hp   Hit points
     * @param mp   Mana points
     */
    public Keyblade_Wielder(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
        this.barrier_up = false;
    }

    public boolean is_down() { return hp <= 0; }

    /**
     * Receive damage, factoring in barrier if active.
     * @param raw_damage Incoming damage
     */
    public void receive_damage(int raw_damage) {
        int reduced = barrier_up ? Math.max(0, raw_damage - 10) : raw_damage;
        KHLogger.log(name + " receives " + reduced + " damage" + (barrier_up ? " (Barrier absorbed 10)" : ""));
        hp -= reduced;
        if(hp < 0) hp = 0;
        KHLogger.log(name + " HP = " + hp);
    }

    /**
     * Spend mana if possible.
     * @param cost Mana cost
     * @return True if spending succeeded.
     */
    public boolean spend_mp(int cost) {
        if(mp < cost) {
            KHLogger.log(name + " tried to spend " + cost + " MP but only has " + mp);
            return false;
        }
        mp -= cost;
        KHLogger.log(name + " spends " + cost + " MP (MP now " + mp + ")");
        return true;
    }

    /**
     * Apply or remove Barrier status.
     * @param on True to enable, false to disable.
     */
    public void set_barrier(boolean on) {
        this.barrier_up = on;
        KHLogger.log(name + (on ? " raises" : " lowers") + " Barrier.");
    }

    /**
     * Basic physical attack into an enemy.
     * @param enemy Enemy target (abstract parent B)
     */
    public abstract void basic_attack(Enemy enemy);

    /**
     * Signature finisher move into an enemy.
     * @param enemy Enemy target (abstract parent B)
     */
    public abstract void finisher(Enemy enemy);

    /**
     * Cast a spell; the spell itself decides how to use caster/target.
     * @param spell  Spell (abstract parent C)
     * @param target May be Enemy, Keyblade_Wielder (self), or null depending on spell
     */
    public void cast_spell(Spell spell, Object target) {
        spell.apply(this, target);
    }
}