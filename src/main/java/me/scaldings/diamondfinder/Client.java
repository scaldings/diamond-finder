package me.scaldings.diamondfinder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class Client implements ClientModInitializer {

	private static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {
			registerCommands();
	}

	private static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(CommandManager.literal("diamond").executes(context -> {
			findDiamonds();
			return 1;
		})));
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(CommandManager.literal("debris").executes(context -> {
			findDebris();
			return 1;
		})));
	}

	private static void findDiamonds() {
		ClientPlayerEntity clientPlayerEntity = client.player;
		if (clientPlayerEntity != null) {
			BlockPos playerBlockPos = clientPlayerEntity.getBlockPos();
			int posX = playerBlockPos.getX();
			int posY = playerBlockPos.getY();
			int posZ = playerBlockPos.getZ() ;
			ArrayList<BlockPos> diamondOres = new ArrayList<>();

			for (int x = posX - 10; x <= posX + 10; x++) {
				for (int y = posY - 10; y <= posY + 10; y++) {
					for (int z = posZ - 10; z <= posZ + 10; z++) {
						BlockPos blockPos = new BlockPos(x, y, z);
						if (clientPlayerEntity.world.getBlockState(blockPos).getBlock() == Blocks.DIAMOND_ORE) {
							diamondOres.add(blockPos);
						}
					}
				}
			}

			if (!diamondOres.isEmpty()) {
				sendMessage("§4§lDiamond ore §r§6found at: §r");
				for (BlockPos diamondOrePos : diamondOres) {
					sendMessage(formatCoordinates(diamondOrePos));
				}
			}
			else {
				sendMessage("§6No §4§ldiamond ores §r§6nearby!§r");
			}
		}
	}

	private static void findDebris() {
		ClientPlayerEntity clientPlayerEntity = client.player;
		if (clientPlayerEntity != null) {
			BlockPos playerBlockPos = clientPlayerEntity.getBlockPos();
			int posX = playerBlockPos.getX();
			int posY = playerBlockPos.getY();
			int posZ = playerBlockPos.getZ() ;
			ArrayList<BlockPos> ancientDebris = new ArrayList<>();

			for (int x = posX - 10; x <= posX + 10; x++) {
				for (int y = posY - 10; y <= posY + 10; y++) {
					for (int z = posZ - 10; z <= posZ + 10; z++) {
						BlockPos blockPos = new BlockPos(x, y, z);
						if (clientPlayerEntity.world.getBlockState(blockPos).getBlock() == Blocks.ANCIENT_DEBRIS) {
							ancientDebris.add(blockPos);
						}
					}
				}
			}

			if (!ancientDebris.isEmpty()) {
				sendMessage("§4§lAncient debris §r§6found at: §r");
				for (BlockPos ancientDebrisPos : ancientDebris) {
					sendMessage(formatCoordinates(ancientDebrisPos));
				}
			}
			else {
				sendMessage("§6No §4§lancient debris §r§6nearby!§r");
			}
		}
	}

	private static String formatCoordinates(BlockPos blockPos) {
		int x = blockPos.getX();
		int y = blockPos.getY();
		int z = blockPos.getZ();

		return "§6" + x + " §4§l/ §r§6" + y + " §4§l/ §r§6" + z + "§r";
	}

	private  static void sendMessage(String content) {
		if (client.player != null) {client.player.sendMessage(Text.of(content), false);}
	}
}
