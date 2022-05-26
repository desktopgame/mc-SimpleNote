package com.github.desktopgame.simplenote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.common.io.Files;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NoteCommand extends CommandBase {

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

	@Override
	public String getCommandName() {
		return "note";
	}

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "note is simple note command.";
    }

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
        try {
            run(sender, args);
        } catch(Throwable e) {
            if(e instanceof NoteException) {
                errorToFile(e.getMessage());
            } else {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                errorToFile(sw.toString());
            }
        }
	}

    private void run(ICommandSender sender, String[] args) throws NoteException {
        // エラーチェック
        if(sender == null) {
            throw new NoteException("sender is null");
        }
        // ユーザ名の取得
        String userName = sender.getCommandSenderName();
        if(userName == null || userName.equals("")) {
            throw new NoteException("user is null");
        }
        // プレイヤー情報と共に出力
        World world = sender.getEntityWorld();
        EntityPlayer player = world.getPlayerEntityByName(userName);
        ItemStack heldItem = player.getHeldItem();
        int px = (int)Math.floor(player.posX);
        int py = (int)Math.floor(player.posY);
        int pz = (int)Math.floor(player.posZ);
        String posText = String.format("(%d, %d, %d)", px, py, pz);
        String itemText = heldItem == null ? "empty" : heldItem.getDisplayName();
        String worldDirName = world.getSaveHandler().getWorldDirectoryName();
        // ファイルへ出力
        StringBuilder sb = new StringBuilder();
        sb.append(worldDirName);
        sb.append("/");
        sb.append(userName);
        sb.append(":");
        sb.append(" ");
        sb.append(posText);
        sb.append(" ");
        sb.append("'" + itemText + "'");
        sb.append(" ");
        sb.append("[");
        sb.append(String.join(" ", args));
        sb.append("]");
        logToFile(sb.toString());
    }

    private static void errorToFile(String error) {
        logToFile("Error: " + error);
    }

    private static void logToFile(String str) {
        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        File rootDir = new File("SimpleNote");
        File child = new File(rootDir, formatter.format(nowDate) + ".txt");
        // 親フォルダ作成
        rootDir.mkdir();
        // ファイルがなければ作成
        if(!child.exists()) {
            try {
                child.createNewFile();
            } catch(IOException ioe) {
                return;
            }
        }
        String nl = System.lineSeparator();
        try {
            if(!str.endsWith(nl)) {
                str = str + nl;
            }
            Files.append(str, child, Charset.forName("UTF-8"));
        } catch(IOException ioe) {
        }
    }
}
