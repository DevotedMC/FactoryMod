package com.github.igotyou.FactoryMod.recipes;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.igotyou.FactoryMod.factories.FurnCraftChestFactory;

import vg.civcraft.mc.civmodcore.itemHandling.ISUtils;
import vg.civcraft.mc.civmodcore.itemHandling.ItemMap;

public class IncrementalUpgradeRecipe extends InputRecipe {

	private List<IRecipe> add = new LinkedList<IRecipe>();
	private List<IRecipe> remove = new LinkedList<IRecipe>();
	
	public IncrementalUpgradeRecipe(String identifier, String name, int productionTime, ItemMap input) {
		super(identifier, name, productionTime, input);
	}

	@Override
	public void applyEffect(Inventory i, FurnCraftChestFactory f) {
		logAfterRecipeRun(i, f);
		if(input.isContainedIn(i)) {
			if(input.removeSafelyFrom(i)) {
				for(IRecipe recipe : add) {
					f.addRecipe(recipe);
				}
				for(IRecipe recipe : remove){
					f.removeRecipe(recipe);
				}
			}
		}
		logAfterRecipeRun(i, f);
	}

	@Override
	public String getTypeIdentifier() {
		return "INCREMENT_UPGRADE";
	}

	@Override
	public List<ItemStack> getInputRepresentation(Inventory i, FurnCraftChestFactory fccf) {
		if(i == null) {
			return input.getItemStackRepresentation();
		}
		LinkedList<ItemStack> result = new LinkedList<ItemStack>();
		ItemMap invMap = new ItemMap(i);
		ItemMap possibleRuns = new ItemMap();
		for(Entry<ItemStack, Integer> entry : input.getEntrySet()) {
			if(invMap.getAmount(entry.getKey()) != 0) {
				possibleRuns.addItemAmount(entry.getKey(), invMap.getAmount(entry.getKey()) / entry.getValue());
			} else {
				possibleRuns.addItemAmount(entry.getKey(), 0);
			}
		}
		for(ItemStack is : input.getItemStackRepresentation()) {
			if(possibleRuns.getAmount(is) != 0) {
				ISUtils.addLore(is, ChatColor.GREEN 
						+ "Enough of this material is available to upgrade");
			} else {
				ISUtils.addLore(is, ChatColor.RED
						+ "Not enough of this material is available to udgrade");
			}
			result.add(is);
		}
		return result;
	}

	@Override
	public List<ItemStack> getOutputRepresentation(Inventory i, FurnCraftChestFactory fccf) {
		List<ItemStack> res = new LinkedList<ItemStack>();
		ItemStack cr = new ItemStack(Material.WORKBENCH);
		ISUtils.setName(cr, name);
		ISUtils.setLore(cr, ChatColor.LIGHT_PURPLE
				+ "Upgrade to get new and better recipes");
		res.add(cr);
		ItemStack fur = new ItemStack(Material.FURNACE);
		ISUtils.setName(fur, name);
		ISUtils.setLore(fur, ChatColor.LIGHT_PURPLE
				+ "Adds recipes:");
		for(IRecipe rec : add) {
			ISUtils.addLore(fur, ChatColor.YELLOW + rec.getName());
		}
		res.add(fur);
		ItemStack che = new ItemStack(Material.CHEST);
		ISUtils.setName(che, name);
		ISUtils.setLore(che, ChatColor.LIGHT_PURPLE
				+ "Removes recipes:");
		for(IRecipe rec : remove) {
			ISUtils.addLore(che, ChatColor.YELLOW + rec.getName());
		}
		res.add(che);
		return res;
	}

	@Override
	public ItemStack getRecipeRepresentation() {
		ItemStack res = new ItemStack(Material.COMMAND);
		res.setAmount(1);
		ItemMeta meta = res.getItemMeta();
		meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		res.setItemMeta(meta);
		ISUtils.setName(res, name);
		return res;
	}

	public void setAddRecipes(List<IRecipe> add) {
		this.add = add;
	}
	
	public void setRemoveRecipes(List<IRecipe> remove) {
		this.remove = remove;
	}
}
