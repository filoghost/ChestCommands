package com.gmail.filoghost.chestcommands.nms;

import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagList;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class v1_7_R4 implements AttributeRemover {

	@Override
	public ItemStack removeAttributes(ItemStack item) {
		
        if(item == null) {
            return item;
        }
        
        net.minecraft.server.v1_7_R4.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        
        if (nmsStack == null) return item;
        
        NBTTagCompound tag;
        if (!nmsStack.hasTag()){
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        else {
            tag = nmsStack.getTag();
        }
        NBTTagList am = new NBTTagList();
        tag.set("AttributeModifiers", am);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
        
    }

}
