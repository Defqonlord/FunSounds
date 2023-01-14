package com.funsounds;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("SoundAlerts")
public interface FunSoundsConfig extends Config
{
	@ConfigItem(
			keyName = "alertDrop",
			name = "Alert Ely and Primordial",
			description = "Play sound alert when an one of the max items is received",
			position = 0
	)
	default boolean alertDrop() {
		return true;
	}

	@ConfigItem(
			keyName = "alertDeath",
			name = "Plays Snake Dying",
			description = "Play a snake dying sound alert when you die",
			position = 1
	)
	default boolean alertDeath() {
		return true;
	}

	@ConfigItem(
			keyName = "alertVeng",
			name = "Vengeance Proc",
			description = "Play a Batman soundbite when you proc vengeance",
			position = 2
	)
	default boolean alertVeng() {
		return true;
	}

	@ConfigItem(
			keyName = "alertZulrah",
			name = "Zulrah Spawn",
			description = "Plays a Shrek swamp clip when Zulrah spawns",
			position = 3
	)
	default boolean alertZulrah() {
		return true;
	}

	@ConfigItem(
			keyName = "alertKraken",
			name = "Kraken Spawn",
			description = "Plays \"Release the Kraken\" when Kraken spawns",
			position = 4
	)
	default boolean alertKraken() {
		return true;
	}

	@ConfigItem(
			keyName = "alertKrakenJar",
			name = "Jar of Dirt Drop",
			description = "Plays \"I've got a jar of dirt\" when the Jar of Dirt drops",
			position = 5
	)
	default boolean alertKrakenJar() {
		return true;
	}

	@ConfigItem(
			keyName = "alertCerbSoul",
			name = "Cerberus Souls",
			description = "Plays \"Fetch me their souls\" when the souls spawn",
			position = 6
	)
	default boolean alertCerbSoul() {
		return true;
	}

	@ConfigItem(
			keyName = "alertSupeBlood",
			name = "Superior Bloodveld",
			description = "Plays \"Damn boi he thicc!\" when the superior bloodveld spawns",
			position = 7
	)
	default boolean alertSupeBlood() {
		return true;
	}
}
