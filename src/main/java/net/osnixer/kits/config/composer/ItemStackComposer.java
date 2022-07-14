package net.osnixer.kits.config.composer;

import net.dzikoysk.cdn.serdes.Composer;
import net.dzikoysk.cdn.serdes.SimpleDeserializer;
import net.dzikoysk.cdn.serdes.SimpleSerializer;
import net.osnixer.kits.serialize.GsonItemSerializer;
import org.bukkit.inventory.ItemStack;
import panda.std.Result;

public class ItemStackComposer implements Composer<ItemStack>, SimpleSerializer<ItemStack>, SimpleDeserializer<ItemStack> {

    private static final GsonItemSerializer SERIALIZER = new GsonItemSerializer();

    @Override
    public Result<ItemStack, Exception> deserialize(String stack) {
        return Result.attempt(Exception.class, () -> SERIALIZER.deserialize(stack));
    }

    @Override
    public Result<String, Exception> serialize(ItemStack itemStack) {
        return Result.attempt(Exception.class, () -> SERIALIZER.serialize(itemStack));
    }

}
