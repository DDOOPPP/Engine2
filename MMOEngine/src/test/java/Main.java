import org.gi.mMOEngine.container.CombatEngine;
import org.gi.mMOEngine.container.StatContainer;
import org.gi.mMOEngine.manager.ModifierManager;
import org.gi.mMOEngine.math.DamageCalculator;
import org.gi.mMOEngine.math.RateCalculator;
import org.gi.mMOEngine.math.StatCalculator;
import org.gi.mMOEngine.register.CombatRegister;
import org.gi.mMOEngine.register.StatRegister;
import org.gi.model.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Main {
    static Logger logger = Logger.getLogger("Test");

    public static void main(String[] args) {
        // ===== 1. StatRegister 초기화 =====
        StatRegister statRegister = new StatRegister();
        for (Stat stat : getDummyStats()) {
            statRegister.register(stat);
        }

        // ===== 2. 공격자 생성 (전사) =====
        EntityStatData attacker = createEntity(statRegister);
        for (StatModifier mod : getAttackerModifiers()) {
            attacker.getManager().add(mod);
        }
        attacker.getCalculator().recalculate();

        // ===== 3. 방어자 생성 (탱커) =====
        EntityStatData defender = createEntity(statRegister);
        for (StatModifier mod : getDefenderModifiers()) {
            defender.getManager().add(mod);
        }
        defender.getCalculator().recalculate();

        // ===== 4. Check Stats =====
        logger.info("=== Attacker Stats ===");
        logger.info("Physical Attack: " + attacker.getContainer().getFinalValue("physical_attack"));
        logger.info("Accuracy: " + attacker.getContainer().getFinalValue("accuracy"));
        logger.info("Critical Chance: " + attacker.getContainer().getFinalValue("critical_chance"));
        logger.info("Critical Damage: " + attacker.getContainer().getFinalValue("critical_damage"));

        logger.info("=== Defender Stats ===");
        logger.info("Physical Defense: " + defender.getContainer().getFinalValue("physical_defense"));
        logger.info("Evasion: " + defender.getContainer().getFinalValue("evasion"));
        logger.info("Critical Resist: " + defender.getContainer().getFinalValue("critical_resist"));
        logger.info("Block Value: " + defender.getContainer().getFinalValue("block_value"));
        logger.info("Block Reduction: " + defender.getContainer().getFinalValue("block_reduction"));

        // ===== 5. CombatRegister 초기화 =====
        CombatRegister combatRegister = new CombatRegister();
        for (Setting setting : getCombatSettings()) {
            combatRegister.register(setting);
        }

        // ===== 6. CombatEngine 생성 =====
        // TODO: CombatEngine 생성자에서 defenseConstant를 파라미터로 받도록 수정 필요
        RateCalculator rateCalc = new RateCalculator();
        DamageCalculator dmgCalc = new DamageCalculator();
        CombatEngine engine = new CombatEngine(rateCalc, dmgCalc, combatRegister);

        // ===== 7. 전투 10회 실행 =====
        logger.info("=== Combat Start (10 rounds) ===");
        for (int i = 1; i <= 10; i++) {
            DamageLog log = engine.runCombat(attacker, defender, Enums.DamageType.PHYSICAL);

            StringBuilder sb = new StringBuilder();
            sb.append("[").append(i).append("] ");

            if (log.isEvade()) {
                sb.append("EVADE! Damage: 0");
            } else {
                sb.append("Damage: ").append(String.format("%.1f", log.getFinalDamage()));
                if (log.isCritical()) sb.append(" [CRITICAL!]");
                if (log.isBlock()) sb.append(" [BLOCK!]");
            }

            logger.info(sb.toString());
        }
    }

    // ===== 엔티티 생성 헬퍼 =====
    public static EntityStatData createEntity(StatRegister register) {
        ModifierManager manager = new ModifierManager();
        StatContainer container = new StatContainer();
        StatCalculator calculator = new StatCalculator(register, manager, container);
        return new EntityStatData(UUID.randomUUID(), manager, container, calculator);
    }

    // ===== 공격자 Modifier (전사) =====
    public static List<StatModifier> getAttackerModifiers() {
        return List.of(
                // BASE
                new StatModifier("atk_base_1", "attacker_base", "physical_attack", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 100),
                new StatModifier("atk_base_2", "attacker_base", "accuracy", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 300),
                new StatModifier("atk_base_3", "attacker_base", "critical_chance", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 200),
                new StatModifier("atk_base_4", "attacker_base", "critical_damage", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 30),
                new StatModifier("atk_base_5", "attacker_base", "block_penetration", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 100),

                // EQUIPMENT (검)
                new StatModifier("atk_sword_1", "sword_001", "physical_attack", Enums.Operator.FLAT, Enums.ModifierSource.EQUIPMENT, 200),
                new StatModifier("atk_sword_2", "sword_001", "critical_chance", Enums.Operator.FLAT, Enums.ModifierSource.EQUIPMENT, 100),

                // EQUIPMENT (반지)
                new StatModifier("atk_ring_1", "ring_001", "physical_attack", Enums.Operator.PERCENT, Enums.ModifierSource.EQUIPMENT, 10),
                new StatModifier("atk_ring_2", "ring_001", "accuracy", Enums.Operator.FLAT, Enums.ModifierSource.EQUIPMENT, 50)
        );
    }

    // ===== 방어자 Modifier (탱커) =====
    public static List<StatModifier> getDefenderModifiers() {
        return List.of(
                // BASE
                new StatModifier("def_base_1", "defender_base", "physical_defense", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 500),
                new StatModifier("def_base_2", "defender_base", "evasion", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 200),
                new StatModifier("def_base_3", "defender_base", "critical_resist", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 150),
                new StatModifier("def_base_4", "defender_base", "critical_damage_resist", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 100),
                new StatModifier("def_base_5", "defender_base", "block_value", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 300),
                new StatModifier("def_base_6", "defender_base", "block_reduction", Enums.Operator.FLAT, Enums.ModifierSource.BASE, 15),

                // EQUIPMENT (방패)
                new StatModifier("def_shield_1", "shield_001", "physical_defense", Enums.Operator.FLAT, Enums.ModifierSource.EQUIPMENT, 300),
                new StatModifier("def_shield_2", "shield_001", "block_value", Enums.Operator.FLAT, Enums.ModifierSource.EQUIPMENT, 200),
                new StatModifier("def_shield_3", "shield_001", "block_reduction", Enums.Operator.FLAT, Enums.ModifierSource.EQUIPMENT, 10)
        );
    }

    // ===== 전투 Setting =====
    public static List<Setting> getCombatSettings() {
        return List.of(
                new Setting("evasion", "accuracy", "evasion", 60, 0),
                new Setting("chance", "critical_chance", "critical_resist", 70, 10),
                new Setting("multiplier", "critical_damage", "critical_damage_resist", 250, 150),
                new Setting("block", "block_penetration", "block_value", 65, 5),
                new Setting("blockReduction", "block_reduction_resist", "block_reduction", 50, 0),
                new Setting("physical", "physical_attack", "physical_defense", 0, 0),
                new Setting("magical", "magic_attack", "magic_defense", 0, 0)
        );
    }

    // ===== 스탯 정의 =====
    public static List<Stat> getDummyStats() {
        return List.of(
                new Stat("physical_attack", "gi.mmo.stat.physical_attack", Enums.StatCategory.OFFENSE, 10, 0, 999999),
                new Stat("magic_attack", "gi.mmo.stat.magic_attack", Enums.StatCategory.OFFENSE, 10, 0, 999999),
                new Stat("accuracy", "gi.mmo.stat.accuracy", Enums.StatCategory.OFFENSE, 0, 0, 999999),
                new Stat("critical_chance", "gi.mmo.stat.critical_chance", Enums.StatCategory.OFFENSE, 0, 0, 999999),
                new Stat("critical_damage", "gi.mmo.stat.critical_damage", Enums.StatCategory.OFFENSE, 150, 150, 250),
                new Stat("block_penetration", "gi.mmo.stat.block_penetration", Enums.StatCategory.OFFENSE, 0, 0, 999999),
                new Stat("block_reduction_resist", "gi.mmo.stat.block_reduction_resist", Enums.StatCategory.OFFENSE, 0, 0, 999999),
                new Stat("physical_defense", "gi.mmo.stat.physical_defense", Enums.StatCategory.DEFENSE, 0, 0, 999999),
                new Stat("magic_defense", "gi.mmo.stat.magic_defense", Enums.StatCategory.DEFENSE, 0, 0, 999999),
                new Stat("evasion", "gi.mmo.stat.evasion", Enums.StatCategory.DEFENSE, 0, 0, 999999),
                new Stat("critical_resist", "gi.mmo.stat.critical_resist", Enums.StatCategory.DEFENSE, 0, 0, 999999),
                new Stat("critical_damage_resist", "gi.mmo.stat.critical_damage_resist", Enums.StatCategory.DEFENSE, 0, 0, 999999),
                new Stat("block_value", "gi.mmo.stat.block_value", Enums.StatCategory.DEFENSE, 0, 0, 999999),
                new Stat("block_reduction", "gi.mmo.stat.block_reduction", Enums.StatCategory.DEFENSE, 20, 0, 50),
                new Stat("max_hp", "gi.mmo.stat.max_hp", Enums.StatCategory.UTILITY, 100, 1, 9999999),
                new Stat("max_mp", "gi.mmo.stat.max_mp", Enums.StatCategory.UTILITY, 50, 0, 9999999),
                new Stat("attack_speed", "gi.mmo.stat.attack_speed", Enums.StatCategory.UTILITY, 100, 10, 300),
                new Stat("move_speed", "gi.mmo.stat.move_speed", Enums.StatCategory.UTILITY, 100, 10, 300)
        );
    }
}