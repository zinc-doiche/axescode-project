package com.github.axescode.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public class Node {
    private final String keyword;
    private final int depth;
    private BiFunction<Player, String[], Boolean> onExecute;
    private Predicate<Player> isDeserved = player -> true;
    private String[] parameters = null;
    private final Map<String, Node> children = new HashMap<>();

    /**
     * @return parameters 길이가 포함된 최종 depth
     */
    public int getFinalDepth() {
        return parameters != null ? depth + parameters.length : depth;
    }

    public List<String> getNextArgs() {
        return children.keySet().stream().toList();
    }

    public void onExecute(BiFunction<Player, String[], Boolean> onExecute) {
        this.onExecute = onExecute;
    }

    public void addRequirement(Predicate<Player> predicate) {
        isDeserved = isDeserved.and(predicate);
    }

    public void setParameters(String... parameters) {
        this.parameters = parameters;
    }

    public void addNode(String keyword, Consumer<Node> consumer) {
        Node node = new Node(keyword, depth + 1);
        consumer.accept(node);
        children.put(keyword, node);
    }
}
