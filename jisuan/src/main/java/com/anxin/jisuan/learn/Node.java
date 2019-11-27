package com.anxin.jisuan.learn;

import lombok.Data;

/**
 * 树节点
 * 
 * @author pang
 * 
 * @param <T>
 */
@Data
public class Node<T> implements Comparable<Node<T>> {
    private T data;
    private int weight;
    private Node<T> left;
    private Node<T> right;

    public Node(T data, int weight) {
        this.data = data;
        this.weight = weight;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "data:" + this.data + ",weight:" + this.weight + ";   ";
    }

    @Override
    public int compareTo(Node<T> o) {
        // TODO Auto-generated method stub
        if (o.weight > this.weight) {
            return 1;
        } else if (o.weight < this.weight) {
            return -1;
        }
        return 0;
    }

}
