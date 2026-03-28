package content.global.skill.farming

import shared.consts.NPCs

enum class Farmers(val id: Int, val patches: Array<FarmingPatch>){
    LYRA(NPCs.LYRA_2326, arrayOf(FarmingPatch.PORT_PHAS_ALLOTMENT_NW, FarmingPatch.PORT_PHAS_ALLOTMENT_SE)),
    ELSTAN(NPCs.ELSTAN_2323, arrayOf(FarmingPatch.S_FALADOR_ALLOTMENT_NW, FarmingPatch.S_FALADOR_ALLOTMENT_SE)),
    HESKEL(NPCs.HESKEL_2340, arrayOf(FarmingPatch.N_FALADOR_TREE)),
    ALAIN(NPCs.ALAIN_2339, arrayOf(FarmingPatch.TAVERLEY_TREE)),
    DANTAERA(NPCs.DANTAERA_2324, arrayOf(FarmingPatch.CATHERBY_ALLOTMENT_N, FarmingPatch.CATHERBY_ALLOTMENT_S)),
    ELLENA(NPCs.ELLENA_2331, arrayOf(FarmingPatch.CATHERBY_FRUIT_TREE)),
    GARTH(NPCs.GARTH_2330, arrayOf(FarmingPatch.BRIMHAVEN_FRUIT_TREE)),
    GILETH(NPCs.GILETH_2344, arrayOf(FarmingPatch.TREE_GNOME_VILLAGE_FRUIT_TREE)),
    AMAETHWR(NPCs.AMAETHWR_2860, arrayOf(FarmingPatch.LLETYA_FRUIT_TREE)),
    SELENA(NPCs.SELENA_2332, arrayOf(FarmingPatch.YANILLE_HOPS)),
    KRAGEN(NPCs.KRAGEN_2325, arrayOf(FarmingPatch.ARDOUGNE_ALLOTMENT_N, FarmingPatch.ARDOUGNE_ALLOTMENT_S)),
    BOLONGO(NPCs.BOLONGO_2343, arrayOf(FarmingPatch.GNOME_STRONGHOLD_FRUIT_TREE)),
    PRISSY_SCILLA(NPCs.PRISSY_SCILLA_1037, arrayOf(FarmingPatch.GNOME_STRONGHOLD_TREE)),
    FAYETH(NPCs.FAYETH_2342, arrayOf(FarmingPatch.LUMBRIDGE_TREE)),
    TREZNOR(NPCs.TREZNOR_2341, arrayOf(FarmingPatch.VARROCK_TREE)),
    VASQUEN(NPCs.VASQUEN_2333, arrayOf(FarmingPatch.LUMBRIDGE_HOPS)),
    RHONEN(NPCs.RHONEN_2334, arrayOf(FarmingPatch.MCGRUBOR_HOPS)),
    FRANCIS(NPCs.FRANCIS_2327, arrayOf(FarmingPatch.ENTRANA_HOPS)),
    DREVEN(NPCs.DREVEN_2335, arrayOf(FarmingPatch.CHAMPIONS_GUILD_BUSH)),
    TARIA(NPCs.TARIA_2336, arrayOf(FarmingPatch.RIMMINGTON_BUSH)),
    RHAZIEN(NPCs.RHAZIEN_2337, arrayOf(FarmingPatch.ETCETERIA_BUSH)),
    TORRELL(NPCs.TORRELL_2338, arrayOf(FarmingPatch.ARDOUGNE_BUSH)),
    FRIZZY(NPCs.FRIZZY_SKERNIP_4560, arrayOf(FarmingPatch.PORT_SARIM_SPIRIT_TREE)),
    YULF_SQUECKS(NPCs.YULF_SQUECKS_4561, arrayOf(FarmingPatch.ETCETERIA_SPIRIT_TREE)),
    PRAISTAN(NPCs.PRAISTAN_EBOLA_4562, arrayOf(FarmingPatch.KARAMJA_SPIRIT_TREE)),
    IMIAGO(NPCs.IMIAGO_8041, arrayOf(FarmingPatch.CALQUAT_TREE)), ;

    companion object {
        @JvmField
        val farmers = values().associateBy { it.id }

        @JvmStatic
        fun forId(id: Int): Farmers? = farmers[id]
    }
}
