package org.example.linkedList;

class Node{
    int data;
    Node next;
    Node(int data){
        this.data = data;
        this.next = null;
    }
}

public class LinkedList {

    static void printList(Node head){
        while(head!=null){
            System.out.print(head.data+" ");
            head = head.next;
        }
    }
    static void reversePrintList(Node head){
        if(head == null)
            return;
        reversePrintList(head.next);
        System.out.print(head.data+" ");
    }

    static int searchIterative(Node head, int data){
        int count = 1;
        while(head!=null){
            if(head.data==data)
                return count;
            count++;
            head = head.next;
        }
        return -1;
    }

    static int searchRecursive(Node head, int data){
        if(head == null)
            return -1;
        else if(head.data == data)
            return 1;
        int res = searchRecursive(head.next, data);
        if(res==-1) return -1;
        else return res+1;
    }

    public static void main(String[] args){
        Node head = new Node(10);
        Node temp1 = new Node(5);
        Node temp2 = new Node(20);
        Node temp3 = new Node(15);
        head.next = temp1;
        temp1.next = temp2;
        temp2.next = temp3;
        printList(head);
        System.out.println();
        reversePrintList(head);
        System.out.println();
        System.out.println(searchIterative(head, 20));
        System.out.println(searchRecursive(head, 10));

    }
}
