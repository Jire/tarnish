package com.osroyale.game.world.entity.combat.weapon;

public enum WeaponAttackStyle {

    /*

    328   - Magic Staff
    425   - Hammer
    5855  - default
    6103  - staff

    12290 - whip

    776   - scythe
    1698  - axe
    2276  - short sword
    4705  - 2h sword
    5570  - pickaxe

    2423  - longsword
    3796  - mace
    7762  - claws

    4679  - spear

    8460  - halberd

    1764  - bow
    4446  - thrown
    24055 - chinchompa

     */

    /** Interfaces: 328, 425, 5855, 6103 */
    ACCURATE_AGGRESSIVE_DEFENSIVE,

    /** Interfaces: 12290 */
    ACCURATE_CONTROLLED_DEFENSIVE,

    /** Interfaces: 776, 1698, 2276, 4705, 5570 */
    ACCURATE_AGGRESSIVE_AGGRESSIVE_DEFENSIVE,

    /** Interfaces: 2423, 3796, 7762 */
    ACCURATE_AGGRESSIVE_CONTROLLED_DEFENSIVE,

    /** Interfaces: 4679 */
    CONTROLLED_CONTROLLED_CONTROLLED_DEFENSIVE,

    /** Interfaces: 8460 */
    CONTROLLED_AGGRESSIVE_DEFENSIVE,

    /** Interfaces: 1764, 4446, 24055 */
    ACCURATE_RAPID_LONGRANGE

}
