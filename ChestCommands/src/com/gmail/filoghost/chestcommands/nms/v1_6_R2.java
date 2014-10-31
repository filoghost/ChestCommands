package com.gmail.filoghost.chestcommands.nms;

import net.minecraft.server.v1_6_R2.NBTTagCompound;
import net.minecraft.server.v1_6_R2.NBTTagList;

import org.bukkit.craftbukkit.v1_6_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class v1_6_R2 implements AttributeRemover {

	@Override
	public ItemStack removeAttributes(ItemStack item) {
		
        if(item == null) {
            return item;
        }
        
        net.minecraft.server.v1_6_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        
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
