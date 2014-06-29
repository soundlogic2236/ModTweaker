package modtweaker.bloodmagic;

import static modtweaker.util.Helper.ItemStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;

@ZenClass("mods.bloodmagic.Altar")
public class BloodAltar {
	//Adding a Blood Magic Altar recipe
	@ZenMethod
	public static void addRecipe(@NotNull IItemStack output, @NotNull IItemStack input, @NotNull int tier, @NotNull int lp, @Optional int consume, @Optional int drain) {
		consume = consume > 0? consume: 20;
		drain = drain > 0? drain: 20;
		
		MineTweakerAPI.tweaker.apply(new Add(new AltarRecipe(ItemStack(output), ItemStack(input), tier, lp, consume, drain, false)));
	}
	
	//Passes the list to the base list implementation, and adds the recipe
	private static class Add extends BaseListAddition {
		public Add(AltarRecipe recipe) {
			super("Blood Altar", AltarRecipeRegistry.altarRecipes, recipe);
		}

		@Override
		public String getRecipeInfo() {
			return ((AltarRecipe)recipe).getResult().getDisplayName();
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Removing a Blood Magic Altar recipe
	@ZenMethod
	public static void removeRecipe(@NotNull IItemStack output) {
		MineTweakerAPI.tweaker.apply(new Remove(ItemStack(output)));
	}
	
	//Removes a recipe, apply is never the same for anything, so will always need to override it
	private static class Remove extends BaseListRemoval {
		public Remove(ItemStack stack) {
			super("Blood Altar", AltarRecipeRegistry.altarRecipes, stack);
		}

		//Loops through the registry, to find the item that matches, saves that recipe then removes it
		@Override
		public void apply() {
			for(AltarRecipe r: AltarRecipeRegistry.altarRecipes) {
				if(r.getResult() != null && r.getResult().isItemEqual(stack)) {
					recipe = r; break;
				}
			}
			
			AltarRecipeRegistry.altarRecipes.remove(recipe);
		}
		
		@Override
		public String getRecipeInfo() {
			return stack.getDisplayName();
		}
	}
}
