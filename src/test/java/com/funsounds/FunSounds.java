package com.funsounds;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FunSounds
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(FunSoundsPlugin.class);
		RuneLite.main(args);
	}
}