package com.funsounds;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.NPC;

import javax.inject.Inject;
//Used for playing a sound file and accessing it
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
//Used for getting status IDs
import net.runelite.api.Varbits;
import net.runelite.api.events.VarbitChanged;
//Find the x and y co-ord
//Used for getting Nex chat messages
import net.runelite.api.events.ChatMessage;


@Slf4j
@PluginDescriptor(
		name = "Sound Alerts"
)
public class FunSoundsPlugin extends Plugin
{
	public static final File SOUND_FOLDER = new File(RuneLite.RUNELITE_DIR.getPath() + File.separator + "sound-alerts");
	public static final File SOUND_DIRT =  new File(SOUND_FOLDER, "I-got-a-jar-of-dirt.wav");
	public static final File SOUND_MGS =  new File(SOUND_FOLDER, "metal-gear-solid-snake-death-scream-sound-effect.wav");
	public static final File SOUND_ZUL =  new File(SOUND_FOLDER, "what-are-you-doing-in-my-swamp.wav");
	public static final File SOUND_CERB =  new File(SOUND_FOLDER, "fetch-me-their-souls.wav");
	public static final File SOUND_VENG =  new File(SOUND_FOLDER, "batman-im-vengeance.wav");
	public static final File SOUND_KRAK =  new File(SOUND_FOLDER, "release-the-kraken.wav");
	public static final File SOUND_BV =  new File(SOUND_FOLDER, "damn-boi-he-thicc.wav");
	public static final File SOUND_WOW =  new File(SOUND_FOLDER, "anime-wow-sound-effect.wav");
	public static final File[] SOUND_FILES = new File[]{
			SOUND_DIRT,
			SOUND_MGS,
			SOUND_ZUL,
			SOUND_CERB,
			SOUND_VENG,
			SOUND_KRAK,
			SOUND_BV,
			SOUND_WOW
	};
	File audioFile;

	private int lastIsVengeancedVarb;
	private String filename;

	@Inject
	private Client client;

	@Inject
	private FunSoundsConfig config;

	@Override
	protected void startUp() throws Exception
	{
		initializeSoundFiles();
		log.info("Sound Alerts started!");
	}

	private void initializeSoundFiles() {
		if (!SOUND_FOLDER.exists()) {
			if (!SOUND_FOLDER.mkdirs()) {
				log.warn("Failed to create folder for sounds");
			}
		}
		for (File f : SOUND_FILES)
		{
			try
			{
				if (f.exists()) {
					continue;
				}
				InputStream stream = FunSoundsPlugin.class.getClassLoader().getResourceAsStream(f.getName());
				OutputStream out = Files.newOutputStream(f.toPath());
				byte[] buffer = new byte[8 * 1024];
				int bytesRead;
				while ((bytesRead = stream.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				out.close();
				stream.close();
			}  catch (Exception e) {
				log.info("SoundAlertsPlugin - " + e + ": " + f);
			}
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Sound Alerts stopped!");
	}

	/*@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		//Example login message code
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}*/

	public Runnable audioSelection(int selection)
	{
		if(selection == 1)
		{
			audioFile = SOUND_ZUL.getAbsoluteFile();
		}
		else if(selection == 2)
		{
			audioFile = SOUND_CERB.getAbsoluteFile();
		}
		else if(selection == 3)
		{
			audioFile = SOUND_MGS.getAbsoluteFile();
		}
		else if(selection == 4)
		{
			audioFile = SOUND_VENG.getAbsoluteFile();
		}
		else if(selection == 5)
		{
			audioFile = SOUND_KRAK.getAbsoluteFile();
		}
		else if(selection == 6)
		{
			audioFile = SOUND_DIRT.getAbsoluteFile();
		}
		else if(selection == 7)
		{
			audioFile = SOUND_BV.getAbsoluteFile();
		}
		else if(selection == 8)
		{
			audioFile = SOUND_WOW.getAbsoluteFile();
		}

		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			//Plays audio once
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			throw new RuntimeException(e);
		} catch (LineUnavailableException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Subscribe
	public void onItemSpawned(ItemSpawned itemSpawned)
	{
		TileItem item = itemSpawned.getItem();
		Clip clip = null;
		if (config.alertDrop())
		{
			if(item.getId() == ItemID.ELYSIAN_SIGIL || item.getId() == ItemID.TWISTED_BOW || item.getId() == ItemID.TUMEKENS_SHADOW || item.getId() == ItemID.SCYTHE_OF_VITUR){
				audioSelection(8);
				log.info("Wow nice drop!");
			}
		}
		if (config.alertKrakenJar())
		{
			if(item.getId() == ItemID.JAR_OF_DIRT)
			{
				audioSelection(6);
				log.info("I've got a jar of dirt");
			}
		}
	}

	//Plays a sound clip when Vengeance procs
	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (config.alertVeng())
		{
			int isVengeancedVarb = client.getVarbitValue(Varbits.VENGEANCE_ACTIVE);

			if (lastIsVengeancedVarb != isVengeancedVarb) {
				if (isVengeancedVarb == 1) {
					log.info("Vengeance Active");
				} else {
					log.info("Vengeance proc");
					audioSelection(4);
				}

				lastIsVengeancedVarb = isVengeancedVarb;
			}
		}
	}

	//Plays a sound clip when you die
	@Subscribe
	public void onActorDeath(ActorDeath actorDeath) {
		if (config.alertDeath() && actorDeath.getActor() == client.getLocalPlayer()) {
			audioSelection(3);
			log.info("Snake snaaaaaaaaaake");
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();

		if (config.alertZulrah())
		{
			//Get Zulrah NPC ID and play sound clip on spawn
			if (npc.getId() == 2042)
			{
				log.info("What are you doing in my swamp!");
				audioSelection(1);
			}
		}
		if (config.alertKraken())
		{
			//Get Kraken NPC ID and play sound clip on spawn
			if (npc.getId() == 494)
			{
				log.info("Release the Kraken!");
				audioSelection(5);
			}
		}
		if (config.alertCerbSoul())
		{
			//Get Cerb Soul NPC ID and play sound clip on spawn
			if (npc.getId() == 5867)
			{
				log.info("Fetch me their souls!");
				audioSelection(2);
			}
		}
		if (config.alertSupeBlood())
		{
			//Get Superior Bloodveld NPC ID and play sound clip on spawn
			if (npc.getId() == 7397 || npc.getId() == 7398)
			{
				log.info("Damn boi he thicc!");
				audioSelection(7);
			}
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() == ChatMessageType.GAMEMESSAGE)
		{
			if (event.getMessage().contains("Fumus, don't fail me!"))
			{
				//notifier.notify("A bird nest has spawned!");
				//int cruor = NpcID.CRUOR;
				log.info("Attack Fumus!");
			}
			else if (event.getMessage().contains("Umbra, don't fail me!"))
			{
				log.info("Attack Umbra!");
			}
			else if (event.getMessage().contains("Cruor, don't fail me!"))
			{
				log.info("Attack Cruor!");
			}
			else if (event.getMessage().contains("Glacies, don't fail me!"))
			{
				log.info("Attack Glacies!");
			}
			else if (config.alertKraken())
			{
				if (event.getMessage().contains("You throw the fishing explosive into the whirlpool..."))
				{
					log.info("Release the Kraken!");
					audioSelection(5);
				}
			}
		}
	}



	@Provides
	FunSoundsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FunSoundsConfig.class);
	}
}
