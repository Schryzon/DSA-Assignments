import Objects.*;

/**
 * Kingdom Hearts ADT
 * ------------------------------------------------------------
 * Shows interactions between children of different abstract parents:
 * - Keyblade_Wielder (Terra, Aqua)
 * - Enemy (ShadowHeartless, Darksider)
 * - Spell (Firaga, Curaga, Barrier)
 */
public class Kingdom_Hearts {
    
    public static void main(String[] args) {
        Keyblade_Wielder terra = new Terra("Terra", 180, 60);
        Keyblade_Wielder aqua = new Aqua("Aqua", 150, 90);

        Enemy shadow = new Shadow_Heartless("Shadow", 90);
        Enemy darksider = new Darksider("Darksider", 220);

        Spell firaga = new Firaga();
        Spell curaga = new Curaga();
        Spell barrier = new Barrier();

        // Scripted battle for demonstration
        KHLogger.section("Encounter: Terra & Aqua vs Shadow & Darksider");

        terra.basic_attack(shadow);
        aqua.cast_spell(firaga, shadow);
        shadow.attack(terra);
        terra.cast_spell(barrier, terra);  // Barrier on self (spell supports enemy OR null)
        aqua.cast_spell(curaga, aqua);     // Aqua heals herself
        terra.cast_spell(firaga, darksider);
        darksider.attack(aqua);

        // Keyblade wielders use their stylized finishers
        terra.finisher(darksider);
        aqua.finisher(shadow);

        KHLogger.section("End of demo");
    }
}

/* ================================
   == CONCRETE WIELDERS EXAMPLE ==
   =============================== */

/** Terra: Heavy-hitting, MP-light. */
class Terra extends Keyblade_Wielder {
    public Terra(String name, int hp, int mp) { super(name, hp, mp); }

    @Override
    public void basic_attack(Enemy enemy){
        if(enemy.is_down()){
            KHLogger.log(name + " swings at " + enemy.species + ", but it's already down.");
            return;
        }
        KHLogger.log(name + " uses Heavy Slash!");
        enemy.receive_damage(30);
    }

    @Override
    public void finisher(Enemy enemy){
        if(enemy.is_down()){
            KHLogger.log(name + " readies Ultima Cannon, but the target is down.");
            return;
        }
        if(!spend_mp(25)) return;
        KHLogger.log(name + " unleashes Ultima Cannon!");
        enemy.receive_damage(65);
    }
}

/** Aqua: Agile caster, MP-rich. */
class Aqua extends Keyblade_Wielder {
    public Aqua(String name, int hp, int mp) { super(name, hp, mp); }

    @Override
    public void basic_attack(Enemy enemy){
        if(enemy.is_down()){
            KHLogger.log(name + " attempts a quick strike, but target is down.");
            return;
        }
        KHLogger.log(name + " performs Graceful Thrust!");
        enemy.receive_damage(18);
    }

    @Override
    public void finisher(Enemy enemy){
        if(enemy.is_down()){
            KHLogger.log(name + " channels Spellweaver, but target is down.");
            return;
        }
        if(!spend_mp(20)) return;
        KHLogger.log(name + " executes Spellweaver!");
        enemy.receive_damage(45);
    }
}

/* ==============================
   == CONCRETE SPELLS EXAMPLE ==
   ============================= */

/** Firaga: Offensive fire burst. */
class Firaga extends Spell {
    public Firaga() { super("Firaga", 15); }

    @Override
    public void apply(Keyblade_Wielder caster, Object target){
        if(!(target instanceof Enemy)){
            KHLogger.log(caster.name + " tries to cast Firaga, but there is no valid enemy target.");
            return;
        }
        if(!caster.spend_mp(mp_cost)) return;

        Enemy enemy = (Enemy) target;
        KHLogger.log(caster.name + " casts " + name + "!");
        enemy.receive_damage(40);
        enemy.on_spell_hit(this, caster);
    }
}

/** Curaga: Strong heal. */
class Curaga extends Spell {
    public Curaga() { super("Curaga", 20); }

    @Override
    public void apply(Keyblade_Wielder caster, Object target){
        Keyblade_Wielder tgt;
        if(target instanceof Keyblade_Wielder){
            tgt = (Keyblade_Wielder) target;
        }else{
            // Default to self if no proper target supplied.
            tgt = caster;
        }
        if(!caster.spend_mp(mp_cost)) return;

        KHLogger.log(caster.name + " casts " + name + " on " + tgt.name + "!");
        int heal = 45;
        tgt.hp += heal;
        KHLogger.log(tgt.name + " recovers " + heal + " HP (HP now " + tgt.hp + ")");
    }
}

/** Barrier: Defensive buff, reduces incoming damage by 10. */
class Barrier extends Spell {
    public Barrier() { super("Barrier", 10); }

    @Override
    public void apply(Keyblade_Wielder caster, Object target){
        Keyblade_Wielder tgt;
        if(target instanceof Keyblade_Wielder){
            tgt = (Keyblade_Wielder) target;
        }else if(target == null){
            tgt = caster;
        }else{
            KHLogger.log(caster.name + " tries to cast Barrier on a non-wielder target. No effect.");
            return;
        }
        if(!caster.spend_mp(mp_cost)) return;

        KHLogger.log(caster.name + " casts " + name + " on " + tgt.name + "!");
        tgt.set_barrier(true);
    }
}

/* ===============================
   == CONCRETE ENEMIES EXAMPLE ==
   ============================== */

/** Small Heartless with evasion; weak but nippy. */
class Shadow_Heartless extends Enemy {
    public Shadow_Heartless(String species, int hp) { super(species, hp); }

    @Override
    public void attack(Keyblade_Wielder wielder){
        if(is_down()){
            KHLogger.log(species + " can't attack; it's down.");
            return;
        }
        KHLogger.log(species + " uses Sneak Claw!");
        wielder.receive_damage(12);
    }

    @Override
    public void on_spell_hit(Spell spell, Keyblade_Wielder caster){
        // Shadows are slightly resistant to small burns but melt to big bursts
        if("Firaga".equals(spell.name)){
            KHLogger.log(species + " is scorched by Firaga (weakness)!");
            receive_damage(10); // extra tick
        }
    }
}

/** Large brute with high HP; shrugs off small hits. */
class Darksider extends Enemy {
    public Darksider(String species, int hp) { super(species, hp); }

    @Override
    public void attack(Keyblade_Wielder wielder){
        if(is_down()){
            KHLogger.log(species + " can't attack; it's down.");
            return;
        }
        KHLogger.log(species + " slams its fist (Heavy Smash)!");
        wielder.receive_damage(26);
    }

    @Override
    public void on_spell_hit(Spell spell, Keyblade_Wielder caster){
        // Darksider resists tiny dings, but comments as flavor
        if("Barrier".equals(spell.name)){
            KHLogger.log(species + " growls: your defense won't save you forever...");
        }
    }
}
