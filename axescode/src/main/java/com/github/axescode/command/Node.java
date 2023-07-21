package com.github.axescode.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class Node {
    private final String keyword;
    private final int depth;
    private BiFunction<CommandSender, String[], Boolean> onExecute;
    private boolean isDeserved;

    private String[] parameters = null;
    private final Map<String, Node> children = new HashMap<>();

    /**
     * 해당 트리의 최대 {@code depth}를 구합니다.
     * <br>
     * 계산이 오래걸릴 수 있으니 서버 로드 단계에서만 사용을 권합니다.
     *
     * @see Node#getDepth()
     * @return 최대 {@code depth}
     */
    public int getDeepest() {
        if(children.isEmpty()) {
            return parameters == null ? depth : depth + parameters.length;
        }

        return children.values().stream().map(Node::getDeepest).reduce(Math::max).orElse(0);
    }

    /**
     * @return parameters 길이가 포함된 최종 depth
     */
    public int getFinalDepth() {
        return parameters != null ? depth + parameters.length : depth;
    }

    /**
     * {@code dig}만큼의 깊이에 있는 {@link Node}를 전부 가져옵니다.
     * <br>
     * 계산이 오래걸릴 수 있으니 서버 로드 단계에서만 사용을 권합니다.
     *
     * @param dig 탐색 깊이
     * @return 해당 {@link Node}에서 {@code dig} 깊이에 있는 {@link List<Node>}
     */
    public List<Node> getTargetNodes(int dig) {
        return children.values().stream().toList();
    }

    public List<String> getNextArgs() {
        return children.keySet().stream().toList();
    }

    public void onExecute(BiFunction<CommandSender, String[], Boolean> onExecute) {
        this.onExecute = onExecute;
    }

    public void addRequirement(BooleanSupplier predicate) {
        isDeserved = isDeserved && predicate.getAsBoolean();
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
