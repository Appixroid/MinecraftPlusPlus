package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ItemIntIDToString implements IFixableData
{
	private static final String[] ID_MAP = new String[2268];

	@Override
	public int getFixVersion()
	{
		return 102;
	}

	@Override
	public NBTTagCompound fixTagCompound(NBTTagCompound compound)
	{
		if (compound.hasKey("id", 99))
		{
			short short1 = compound.getShort("id");

			if (short1 > 0 && short1 < ID_MAP.length && ID_MAP[short1] != null)
			{
				compound.setString("id", ID_MAP[short1]);
			}
		}

		return compound;
	}

	static
	{
		ID_MAP[1] = "minecraft:stone";
		ID_MAP[2] = "minecraft:grass";
		ID_MAP[3] = "minecraft:dirt";
		ID_MAP[4] = "minecraft:cobblestone";
		ID_MAP[5] = "minecraft:planks";
		ID_MAP[6] = "minecraft:sapling";
		ID_MAP[7] = "minecraft:bedrock";
		ID_MAP[8] = "minecraft:flowing_water";
		ID_MAP[9] = "minecraft:water";
		ID_MAP[10] = "minecraft:flowing_lava";
		ID_MAP[11] = "minecraft:lava";
		ID_MAP[12] = "minecraft:sand";
		ID_MAP[13] = "minecraft:gravel";
		ID_MAP[14] = "minecraft:gold_ore";
		ID_MAP[15] = "minecraft:iron_ore";
		ID_MAP[16] = "minecraft:coal_ore";
		ID_MAP[17] = "minecraft:log";
		ID_MAP[18] = "minecraft:leaves";
		ID_MAP[19] = "minecraft:sponge";
		ID_MAP[20] = "minecraft:glass";
		ID_MAP[21] = "minecraft:lapis_ore";
		ID_MAP[22] = "minecraft:lapis_block";
		ID_MAP[23] = "minecraft:dispenser";
		ID_MAP[24] = "minecraft:sandstone";
		ID_MAP[25] = "minecraft:noteblock";
		ID_MAP[27] = "minecraft:golden_rail";
		ID_MAP[28] = "minecraft:detector_rail";
		ID_MAP[29] = "minecraft:sticky_piston";
		ID_MAP[30] = "minecraft:web";
		ID_MAP[31] = "minecraft:tallgrass";
		ID_MAP[32] = "minecraft:deadbush";
		ID_MAP[33] = "minecraft:piston";
		ID_MAP[35] = "minecraft:wool";
		ID_MAP[37] = "minecraft:yellow_flower";
		ID_MAP[38] = "minecraft:red_flower";
		ID_MAP[39] = "minecraft:brown_mushroom";
		ID_MAP[40] = "minecraft:red_mushroom";
		ID_MAP[41] = "minecraft:gold_block";
		ID_MAP[42] = "minecraft:iron_block";
		ID_MAP[43] = "minecraft:double_stone_slab";
		ID_MAP[44] = "minecraft:stone_slab";
		ID_MAP[45] = "minecraft:brick_block";
		ID_MAP[46] = "minecraft:tnt";
		ID_MAP[47] = "minecraft:bookshelf";
		ID_MAP[48] = "minecraft:mossy_cobblestone";
		ID_MAP[49] = "minecraft:obsidian";
		ID_MAP[50] = "minecraft:torch";
		ID_MAP[51] = "minecraft:fire";
		ID_MAP[52] = "minecraft:mob_spawner";
		ID_MAP[53] = "minecraft:oak_stairs";
		ID_MAP[54] = "minecraft:chest";
		ID_MAP[56] = "minecraft:diamond_ore";
		ID_MAP[57] = "minecraft:diamond_block";
		ID_MAP[58] = "minecraft:crafting_table";
		ID_MAP[60] = "minecraft:farmland";
		ID_MAP[61] = "minecraft:furnace";
		ID_MAP[62] = "minecraft:lit_furnace";
		ID_MAP[65] = "minecraft:ladder";
		ID_MAP[66] = "minecraft:rail";
		ID_MAP[67] = "minecraft:stone_stairs";
		ID_MAP[69] = "minecraft:lever";
		ID_MAP[70] = "minecraft:stone_pressure_plate";
		ID_MAP[72] = "minecraft:wooden_pressure_plate";
		ID_MAP[73] = "minecraft:redstone_ore";
		ID_MAP[76] = "minecraft:redstone_torch";
		ID_MAP[77] = "minecraft:stone_button";
		ID_MAP[78] = "minecraft:snow_layer";
		ID_MAP[79] = "minecraft:ice";
		ID_MAP[80] = "minecraft:snow";
		ID_MAP[81] = "minecraft:cactus";
		ID_MAP[82] = "minecraft:clay";
		ID_MAP[84] = "minecraft:jukebox";
		ID_MAP[85] = "minecraft:fence";
		ID_MAP[86] = "minecraft:pumpkin";
		ID_MAP[87] = "minecraft:netherrack";
		ID_MAP[88] = "minecraft:soul_sand";
		ID_MAP[89] = "minecraft:glowstone";
		ID_MAP[90] = "minecraft:portal";
		ID_MAP[91] = "minecraft:lit_pumpkin";
		ID_MAP[95] = "minecraft:stained_glass";
		ID_MAP[96] = "minecraft:trapdoor";
		ID_MAP[97] = "minecraft:monster_egg";
		ID_MAP[98] = "minecraft:stonebrick";
		ID_MAP[99] = "minecraft:brown_mushroom_block";
		ID_MAP[100] = "minecraft:red_mushroom_block";
		ID_MAP[101] = "minecraft:iron_bars";
		ID_MAP[102] = "minecraft:glass_pane";
		ID_MAP[103] = "minecraft:melon_block";
		ID_MAP[106] = "minecraft:vine";
		ID_MAP[107] = "minecraft:fence_gate";
		ID_MAP[108] = "minecraft:brick_stairs";
		ID_MAP[109] = "minecraft:stone_brick_stairs";
		ID_MAP[110] = "minecraft:mycelium";
		ID_MAP[111] = "minecraft:waterlily";
		ID_MAP[112] = "minecraft:nether_brick";
		ID_MAP[113] = "minecraft:nether_brick_fence";
		ID_MAP[114] = "minecraft:nether_brick_stairs";
		ID_MAP[116] = "minecraft:enchanting_table";
		ID_MAP[119] = "minecraft:end_portal";
		ID_MAP[120] = "minecraft:end_portal_frame";
		ID_MAP[121] = "minecraft:end_stone";
		ID_MAP[122] = "minecraft:dragon_egg";
		ID_MAP[123] = "minecraft:redstone_lamp";
		ID_MAP[125] = "minecraft:double_wooden_slab";
		ID_MAP[126] = "minecraft:wooden_slab";
		ID_MAP[127] = "minecraft:cocoa";
		ID_MAP[128] = "minecraft:sandstone_stairs";
		ID_MAP[129] = "minecraft:emerald_ore";
		ID_MAP[130] = "minecraft:ender_chest";
		ID_MAP[131] = "minecraft:tripwire_hook";
		ID_MAP[133] = "minecraft:emerald_block";
		ID_MAP[134] = "minecraft:spruce_stairs";
		ID_MAP[135] = "minecraft:birch_stairs";
		ID_MAP[136] = "minecraft:jungle_stairs";
		ID_MAP[137] = "minecraft:command_block";
		ID_MAP[138] = "minecraft:beacon";
		ID_MAP[139] = "minecraft:cobblestone_wall";
		ID_MAP[141] = "minecraft:carrots";
		ID_MAP[142] = "minecraft:potatoes";
		ID_MAP[143] = "minecraft:wooden_button";
		ID_MAP[145] = "minecraft:anvil";
		ID_MAP[146] = "minecraft:trapped_chest";
		ID_MAP[147] = "minecraft:light_weighted_pressure_plate";
		ID_MAP[148] = "minecraft:heavy_weighted_pressure_plate";
		ID_MAP[151] = "minecraft:daylight_detector";
		ID_MAP[152] = "minecraft:redstone_block";
		ID_MAP[153] = "minecraft:quartz_ore";
		ID_MAP[154] = "minecraft:hopper";
		ID_MAP[155] = "minecraft:quartz_block";
		ID_MAP[156] = "minecraft:quartz_stairs";
		ID_MAP[157] = "minecraft:activator_rail";
		ID_MAP[158] = "minecraft:dropper";
		ID_MAP[159] = "minecraft:stained_hardened_clay";
		ID_MAP[160] = "minecraft:stained_glass_pane";
		ID_MAP[161] = "minecraft:leaves2";
		ID_MAP[162] = "minecraft:log2";
		ID_MAP[163] = "minecraft:acacia_stairs";
		ID_MAP[164] = "minecraft:dark_oak_stairs";
		ID_MAP[170] = "minecraft:hay_block";
		ID_MAP[171] = "minecraft:carpet";
		ID_MAP[172] = "minecraft:hardened_clay";
		ID_MAP[173] = "minecraft:coal_block";
		ID_MAP[174] = "minecraft:packed_ice";
		ID_MAP[175] = "minecraft:double_plant";
		ID_MAP[256] = "minecraft:iron_shovel";
		ID_MAP[257] = "minecraft:iron_pickaxe";
		ID_MAP[258] = "minecraft:iron_axe";
		ID_MAP[259] = "minecraft:flint_and_steel";
		ID_MAP[260] = "minecraft:apple";
		ID_MAP[261] = "minecraft:bow";
		ID_MAP[262] = "minecraft:arrow";
		ID_MAP[263] = "minecraft:coal";
		ID_MAP[264] = "minecraft:diamond";
		ID_MAP[265] = "minecraft:iron_ingot";
		ID_MAP[266] = "minecraft:gold_ingot";
		ID_MAP[267] = "minecraft:iron_sword";
		ID_MAP[268] = "minecraft:wooden_sword";
		ID_MAP[269] = "minecraft:wooden_shovel";
		ID_MAP[270] = "minecraft:wooden_pickaxe";
		ID_MAP[271] = "minecraft:wooden_axe";
		ID_MAP[272] = "minecraft:stone_sword";
		ID_MAP[273] = "minecraft:stone_shovel";
		ID_MAP[274] = "minecraft:stone_pickaxe";
		ID_MAP[275] = "minecraft:stone_axe";
		ID_MAP[276] = "minecraft:diamond_sword";
		ID_MAP[277] = "minecraft:diamond_shovel";
		ID_MAP[278] = "minecraft:diamond_pickaxe";
		ID_MAP[279] = "minecraft:diamond_axe";
		ID_MAP[280] = "minecraft:stick";
		ID_MAP[281] = "minecraft:bowl";
		ID_MAP[282] = "minecraft:mushroom_stew";
		ID_MAP[283] = "minecraft:golden_sword";
		ID_MAP[284] = "minecraft:golden_shovel";
		ID_MAP[285] = "minecraft:golden_pickaxe";
		ID_MAP[286] = "minecraft:golden_axe";
		ID_MAP[287] = "minecraft:string";
		ID_MAP[288] = "minecraft:feather";
		ID_MAP[289] = "minecraft:gunpowder";
		ID_MAP[290] = "minecraft:wooden_hoe";
		ID_MAP[291] = "minecraft:stone_hoe";
		ID_MAP[292] = "minecraft:iron_hoe";
		ID_MAP[293] = "minecraft:diamond_hoe";
		ID_MAP[294] = "minecraft:golden_hoe";
		ID_MAP[295] = "minecraft:wheat_seeds";
		ID_MAP[296] = "minecraft:wheat";
		ID_MAP[297] = "minecraft:bread";
		ID_MAP[298] = "minecraft:leather_helmet";
		ID_MAP[299] = "minecraft:leather_chestplate";
		ID_MAP[300] = "minecraft:leather_leggings";
		ID_MAP[301] = "minecraft:leather_boots";
		ID_MAP[302] = "minecraft:chainmail_helmet";
		ID_MAP[303] = "minecraft:chainmail_chestplate";
		ID_MAP[304] = "minecraft:chainmail_leggings";
		ID_MAP[305] = "minecraft:chainmail_boots";
		ID_MAP[306] = "minecraft:iron_helmet";
		ID_MAP[307] = "minecraft:iron_chestplate";
		ID_MAP[308] = "minecraft:iron_leggings";
		ID_MAP[309] = "minecraft:iron_boots";
		ID_MAP[310] = "minecraft:diamond_helmet";
		ID_MAP[311] = "minecraft:diamond_chestplate";
		ID_MAP[312] = "minecraft:diamond_leggings";
		ID_MAP[313] = "minecraft:diamond_boots";
		ID_MAP[314] = "minecraft:golden_helmet";
		ID_MAP[315] = "minecraft:golden_chestplate";
		ID_MAP[316] = "minecraft:golden_leggings";
		ID_MAP[317] = "minecraft:golden_boots";
		ID_MAP[318] = "minecraft:flint";
		ID_MAP[319] = "minecraft:porkchop";
		ID_MAP[320] = "minecraft:cooked_porkchop";
		ID_MAP[321] = "minecraft:painting";
		ID_MAP[322] = "minecraft:golden_apple";
		ID_MAP[323] = "minecraft:sign";
		ID_MAP[324] = "minecraft:wooden_door";
		ID_MAP[325] = "minecraft:bucket";
		ID_MAP[326] = "minecraft:water_bucket";
		ID_MAP[327] = "minecraft:lava_bucket";
		ID_MAP[328] = "minecraft:minecart";
		ID_MAP[329] = "minecraft:saddle";
		ID_MAP[330] = "minecraft:iron_door";
		ID_MAP[331] = "minecraft:redstone";
		ID_MAP[332] = "minecraft:snowball";
		ID_MAP[333] = "minecraft:boat";
		ID_MAP[334] = "minecraft:leather";
		ID_MAP[335] = "minecraft:milk_bucket";
		ID_MAP[336] = "minecraft:brick";
		ID_MAP[337] = "minecraft:clay_ball";
		ID_MAP[338] = "minecraft:reeds";
		ID_MAP[339] = "minecraft:paper";
		ID_MAP[340] = "minecraft:book";
		ID_MAP[341] = "minecraft:slime_ball";
		ID_MAP[342] = "minecraft:chest_minecart";
		ID_MAP[343] = "minecraft:furnace_minecart";
		ID_MAP[344] = "minecraft:egg";
		ID_MAP[345] = "minecraft:compass";
		ID_MAP[346] = "minecraft:fishing_rod";
		ID_MAP[347] = "minecraft:clock";
		ID_MAP[348] = "minecraft:glowstone_dust";
		ID_MAP[349] = "minecraft:fish";
		ID_MAP[350] = "minecraft:cooked_fished";
		ID_MAP[351] = "minecraft:dye";
		ID_MAP[352] = "minecraft:bone";
		ID_MAP[353] = "minecraft:sugar";
		ID_MAP[354] = "minecraft:cake";
		ID_MAP[355] = "minecraft:bed";
		ID_MAP[356] = "minecraft:repeater";
		ID_MAP[357] = "minecraft:cookie";
		ID_MAP[358] = "minecraft:filled_map";
		ID_MAP[359] = "minecraft:shears";
		ID_MAP[360] = "minecraft:melon";
		ID_MAP[361] = "minecraft:pumpkin_seeds";
		ID_MAP[362] = "minecraft:melon_seeds";
		ID_MAP[363] = "minecraft:beef";
		ID_MAP[364] = "minecraft:cooked_beef";
		ID_MAP[365] = "minecraft:chicken";
		ID_MAP[366] = "minecraft:cooked_chicken";
		ID_MAP[367] = "minecraft:rotten_flesh";
		ID_MAP[368] = "minecraft:ender_pearl";
		ID_MAP[369] = "minecraft:blaze_rod";
		ID_MAP[370] = "minecraft:ghast_tear";
		ID_MAP[371] = "minecraft:gold_nugget";
		ID_MAP[372] = "minecraft:nether_wart";
		ID_MAP[373] = "minecraft:potion";
		ID_MAP[374] = "minecraft:glass_bottle";
		ID_MAP[375] = "minecraft:spider_eye";
		ID_MAP[376] = "minecraft:fermented_spider_eye";
		ID_MAP[377] = "minecraft:blaze_powder";
		ID_MAP[378] = "minecraft:magma_cream";
		ID_MAP[379] = "minecraft:brewing_stand";
		ID_MAP[380] = "minecraft:cauldron";
		ID_MAP[381] = "minecraft:ender_eye";
		ID_MAP[382] = "minecraft:speckled_melon";
		ID_MAP[383] = "minecraft:spawn_egg";
		ID_MAP[384] = "minecraft:experience_bottle";
		ID_MAP[385] = "minecraft:fire_charge";
		ID_MAP[386] = "minecraft:writable_book";
		ID_MAP[387] = "minecraft:written_book";
		ID_MAP[388] = "minecraft:emerald";
		ID_MAP[389] = "minecraft:item_frame";
		ID_MAP[390] = "minecraft:flower_pot";
		ID_MAP[391] = "minecraft:carrot";
		ID_MAP[392] = "minecraft:potato";
		ID_MAP[393] = "minecraft:baked_potato";
		ID_MAP[394] = "minecraft:poisonous_potato";
		ID_MAP[395] = "minecraft:map";
		ID_MAP[396] = "minecraft:golden_carrot";
		ID_MAP[397] = "minecraft:skull";
		ID_MAP[398] = "minecraft:carrot_on_a_stick";
		ID_MAP[399] = "minecraft:nether_star";
		ID_MAP[400] = "minecraft:pumpkin_pie";
		ID_MAP[401] = "minecraft:fireworks";
		ID_MAP[402] = "minecraft:firework_charge";
		ID_MAP[403] = "minecraft:enchanted_book";
		ID_MAP[404] = "minecraft:comparator";
		ID_MAP[405] = "minecraft:netherbrick";
		ID_MAP[406] = "minecraft:quartz";
		ID_MAP[407] = "minecraft:tnt_minecart";
		ID_MAP[408] = "minecraft:hopper_minecart";
		ID_MAP[417] = "minecraft:iron_horse_armor";
		ID_MAP[418] = "minecraft:golden_horse_armor";
		ID_MAP[419] = "minecraft:diamond_horse_armor";
		ID_MAP[420] = "minecraft:lead";
		ID_MAP[421] = "minecraft:name_tag";
		ID_MAP[422] = "minecraft:command_block_minecart";
		ID_MAP[2256] = "minecraft:record_13";
		ID_MAP[2257] = "minecraft:record_cat";
		ID_MAP[2258] = "minecraft:record_blocks";
		ID_MAP[2259] = "minecraft:record_chirp";
		ID_MAP[2260] = "minecraft:record_far";
		ID_MAP[2261] = "minecraft:record_mall";
		ID_MAP[2262] = "minecraft:record_mellohi";
		ID_MAP[2263] = "minecraft:record_stal";
		ID_MAP[2264] = "minecraft:record_strad";
		ID_MAP[2265] = "minecraft:record_ward";
		ID_MAP[2266] = "minecraft:record_11";
		ID_MAP[2267] = "minecraft:record_wait";
	}
}
