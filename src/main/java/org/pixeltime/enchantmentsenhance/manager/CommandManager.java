package org.pixeltime.enchantmentsenhance.manager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.pixeltime.enchantmentsenhance.Main;
import org.pixeltime.enchantmentsenhance.command.*;
import org.pixeltime.enchantmentsenhance.command.console.*;
import org.pixeltime.enchantmentsenhance.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CommandManager implements CommandExecutor {

    // Sub Commands
    public String main = "enhance";
    public String add = "add";
    public String help = "help";
    public String inventory = "inventory";
    public String list = "list";
    public String lore = "lore";
    public String menu = "menu";
    public String reload = "reload";
    public String select = "select";
    public String version = "version";
    public String debug = "debug";
    public String reform = "reform";
    private ArrayList<SubCommand> commands = new ArrayList<SubCommand>();
    private ArrayList<SubConsoleCommand> consoleCommands =
            new ArrayList<SubConsoleCommand>();
    private Main plugin = Main.getMain();

    public CommandManager() {
    }

    /**
     * Register all the command.
     */
    public void setup() {
        plugin.getCommand(main).setExecutor(this);
        this.commands.add(new AddCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new InventoryCommand());
        this.commands.add(new ListCommand());
        this.commands.add(new LoreCommand());
        this.commands.add(new MenuCommand());
        this.commands.add(new ReloadCommand());
        this.commands.add(new SelectCommand());
        this.commands.add(new VersionCommand());
        this.commands.add(new DebugCommand());
        this.commands.add(new ReformCommand());
        this.consoleCommands.add(new AddConsoleCommand());
        this.consoleCommands.add(new DebugConsoleCommand());
        this.consoleCommands.add(new HelpConsoleCommand());
        this.consoleCommands.add(new ReloadConsoleCommand());
        this.consoleCommands.add(new VersionConsoleCommand());
    }


    public boolean onCommand(
            CommandSender sender,
            Command command,
            String s,
            String[] args) {
        if (!command.getName().equalsIgnoreCase(main)) {
            return true;
        }
        if (!(sender instanceof Player)) {

            if (args.length == 0) {
                this.getConsoleCommand(help).onCommand(sender, args);
                return true;
            }

            SubConsoleCommand target = this.getConsoleCommand(args[0]);

            if (target == null) {
                Util.sendMessage(SettingsManager.lang.getString(
                        "Config.consoleCommand"), sender);
                return true;
            }

            try {
                // This removes the first argument.
                target.onCommand(sender, Arrays.copyOfRange(args, 1,
                        args.length));
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "An error has occurred.");

                e.printStackTrace();

            }

            return true;

        }

        Player player = (Player) sender;

        if (args.length == 0) {
            this.get(help).onCommand(player, args);
            return true;
        }

        SubCommand target = this.get(args[0]);

        if (target == null) {
            Util.sendMessage(SettingsManager.lang.getString(
                    "Config.invalidCommand"), player);
            return true;
        }

        if (!player.hasPermission(target.getPermission())) {
            Util.sendMessage(SettingsManager.lang.getString("Config.noPerm"),
                    player);
            return true;
        }

        try {
            // This removes the first argument.
            target.onCommand(player, Arrays.copyOfRange(args, 1, args.length));
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "An error has occurred.");

            e.printStackTrace();

        }

        return true;

    }


    /**
     * Get a command by a string.
     *
     * @param name
     * @return
     */
    private SubCommand get(String name) {
        Iterator<SubCommand> subcommands = this.commands.iterator();

        while (subcommands.hasNext()) {
            SubCommand sc = subcommands.next();
            if (sc.name().equalsIgnoreCase(name)) {
                return sc;
            }

            String[] aliases;
            int length = (aliases = sc.aliases()).length;

            for (int var5 = 0; var5 < length; ++var5) {
                String alias = aliases[var5];
                if (name.equalsIgnoreCase(alias)) {
                    return sc;
                }

            }
        }
        return null;
    }


    private SubConsoleCommand getConsoleCommand(String name) {
        Iterator<SubConsoleCommand> subcommands = this.consoleCommands
                .iterator();

        while (subcommands.hasNext()) {
            SubConsoleCommand sc = subcommands.next();

            if (sc.name().equalsIgnoreCase(name)) {
                return sc;
            }

            String[] aliases;
            int length = (aliases = sc.aliases()).length;

            for (int var5 = 0; var5 < length; ++var5) {
                String alias = aliases[var5];
                if (name.equalsIgnoreCase(alias)) {
                    return sc;
                }

            }
        }
        return null;
    }


    /**
     * Print help for a player.
     *
     * @param player
     */
    public void printHelp(Player player) {
        String help = "&b&l&m          &d EnchantmentsEnhance&b&l&m          ";
        Iterator<SubCommand> subcommands = this.commands.iterator();
        while (subcommands.hasNext()) {
            SubCommand sc = subcommands.next();
            if (player.hasPermission(sc.getPermission())) {
                help += sc.info();
            }
        }
        Util.sendMessage(help, player);
    }


    /**
     * Print help for console.
     *
     * @param sender
     */
    public void printHelp(CommandSender sender) {
        String help = "&b&l&m          &d EnchantmentsEnhance&b&l&m          ";
        Iterator<SubConsoleCommand> subcommands = this.consoleCommands.iterator();
        while (subcommands.hasNext()) {
            SubConsoleCommand sc = subcommands.next();
            help += sc.info();
        }
        Util.sendMessage(help, sender);
    }
}