package com.gmail.filoghost.chestcommands.nms;

import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.NBTTagInt;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class v1_8_R1 implements AttributeRemover {

	@Override
	public ItemStack removeAttributes(ItemStack item) {
		
        if(item == null) {
            return item;
        }
        
        net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        
        if (nmsStack == null) return item;
        
        NBTTagCompound tag;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        } else {
            tag = nmsStack.getTag();
        }
        
        tag.set("HideFlags", new NBTTagInt(Integer.MAX_VALUE));
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

}