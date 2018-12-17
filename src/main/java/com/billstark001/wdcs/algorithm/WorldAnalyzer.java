package com.billstark001.wdcs.algorithm;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class WorldAnalyzer {
	
	private static boolean Enabled = false;
	public static final boolean getEnabled() {return Enabled;}
	public static final boolean setEnabled(boolean Enabled) {return WorldAnalyzer.Enabled = Enabled;}
	public static final boolean ToggleEnabled() {return Enabled = !Enabled;}
	
	protected int distance = 20;
    public static final int defaultDistance = 20;
    public static final int minDistance = 2;
    public static final int maxDistance = 175;
    
    private double trsTileOverlay = 0.7;
    private double trsTileOverlayBehind = 0.3;
    private double trsImportant = 0.7;
    private double trsDangerous = 0.7;
    private double trsMob = 0.6;
    private double trsItem = 0.5;
    
    private static ArrayList<AnalyzedBlockPos> unsafeCache;
    
    private Minecraft mc = Minecraft.getMinecraft();
    private World world = mc.world;
    private EntityPlayer player = mc.player;
	
	public static final WorldAnalyzer instance = new WorldAnalyzer();
	
	protected WorldAnalyzer() {
		
	}
	
}
