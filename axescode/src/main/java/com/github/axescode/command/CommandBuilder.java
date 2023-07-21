package com.github.axescode.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandBuilder {
    private final Node startNode;
    
    public void openNode(String keyword, Consumer<Node> consumer) {
        startNode.addNode(keyword, consumer);
    }

    public void onExecute(BiFunction<CommandSender, String[], Boolean> onExecute) {
        startNode.onExecute(onExecute);
    }

    /**
     * {@link Command}를 등록합니다. 필히 {@code plugin.yml}에도 등록해야 합니다.
     * 
     * @param plugin 등록할 {@link JavaPlugin}
     * @param keyword 등록할 {@link Command}의 키
     * @param consumer 초기 설정
     */
    public static void register(JavaPlugin plugin, String keyword, Consumer<CommandBuilder> consumer) {
        PluginCommand command = Objects.requireNonNull(plugin.getCommand(keyword));
        CommandBuilder builder = new CommandBuilder(new Node(keyword, 0));

        consumer.accept(builder);
        command.setExecutor(new TabExecutor() {
            @Override
            public boolean onCommand(
                    @NotNull CommandSender sender, @NotNull Command command,
                    @NotNull String label, @NotNull String[] args
            ) {
                Node finalNode = builder.startNode;

                //depth == i
                for (String arg : args) {
                    if (finalNode.getChildren().get(arg) != null) {
                        finalNode = finalNode.getChildren().get(arg);
                    }
                    //final node
                    else if (finalNode.getParameters() != null && finalNode.getFinalDepth() == args.length - 1 &&
                                    finalNode.isDeserved()) {
                        return finalNode.getOnExecute().apply(sender, args);
                    }
                }

                //unreached
                return false;
            }

            @Override
            public @Nullable List<String> onTabComplete(
                    @NotNull CommandSender sender, @NotNull Command command,
                    @NotNull String label, @NotNull String[] args
            ) {
                List<String> list = new ArrayList<>();
                Node finalNode = builder.startNode;
                // 1 2 3 4 5 : length
                // 0 1 2 3 4 : idx == depth
                //tp p x y z : args
                // _ f 0 1 2 : param idx
                //depth == i
                for (String arg : args) {
                    if (finalNode.getChildren().get(arg) != null) {
                        finalNode = finalNode.getChildren().get(arg);

                        return StringUtil.copyPartialMatches(arg, finalNode.getNextArgs(), list);
                    }
                    //final node
                    else if (finalNode.getParameters() != null) {
                        int paramIdx = args.length - finalNode.getDepth() - 2;
                        String parameter = finalNode.getParameters()[paramIdx];

                        return StringUtil.copyPartialMatches(arg, Collections.singletonList(parameter), list);
                    }
                }

                //unreached
                return null;
            }
        });
    }
}
