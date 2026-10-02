### Preorder Traversal

```java
void preorderTraversal(Node root) {
    if (root == null) {
        return;
    }
    System.out.print(root.data + " ");
    preorderTraversal(root.left);
    preorderTraversal(root.right);
}

void preOrderTraversalIterative(Node root) {
    if (root == null) return;
    Stack<Node> st = new Stack<>();
    st.push(root);
    while (!st.isEmpty()) {
        Node curr = st.pop();
        System.out.print(curr.data + " ");
        if (curr.right != null) st.push(curr.right);
        if (curr.left != null) st.push(curr.left);
    }
}

void morrisPreorderTraversal(Node root) {
    Node current = root;
    while (current != null) {
        if (current.left == null) {
            System.out.print(current.data + " ");
            current = current.right;
        } else {
            Node predecessor = current.left;
            while (predecessor.right != null && predecessor.right != current) {
                predecessor = predecessor.right;
            }
            if (predecessor.right == null) {
                System.out.print(current.data + " ");
                predecessor.right = current;
                current = current.left;
            } else {
                predecessor.right = null;
                current = current.right;
            }
        }
    }
}
```

### Inorder Traversal

```java
void inorderTraversal(Node root) {
    if (root == null) return;
    inorderTraversal(root.left);
    System.out.print(root.data + " ");
    inorderTraversal(root.right);
}

void inorderTraversalIterative(Node root) {
    Stack<Node> st = new Stack<>();
    Node curr = root;
    while (curr != null || !st.isEmpty()) {
        while (curr != null) {
            st.push(curr);
            curr = curr.left;
        }
        curr = st.pop();
        System.out.print(curr.data + " ");
        curr = curr.right;
    }
}

void morrisInorderTraversal(Node root) {
    Node current = root;
    while (current != null) {
        if (current.left == null) {
            System.out.print(current.data + " ");
            current = current.right;
        } else {
            Node predecessor = current.left;
            while (predecessor.right != null && predecessor.right != current) {
                predecessor = predecessor.right;
            }
            if (predecessor.right == null) {
                predecessor.right = current;
                current = current.left;
            } else {
                predecessor.right = null;
                System.out.print(current.data + " ");
                current = current.right;
            }
        }
    }
}
```

### Postorder Traversal

```java
void postorderTraversal(Node root) {
    if (root == null) return;
    postorderTraversal(root.left);
    postorderTraversal(root.right);
    System.out.print(root.data + " ");
}   

void postorderTraversalIterative(Node root) {
    if (root == null) return;
    Stack<Node> st1 = new Stack<>();
    Stack<Node> st2 = new Stack<>();
    st1.push(root);
    while (!st1.isEmpty()) {
        Node curr = st1.pop();
        st2.push(curr);
        if (curr.left != null) st1.push(curr.left);
        if (curr.right != null) st1.push(curr.right);
    }
    while (!st2.isEmpty()) {
        System.out.print(st2.pop().data + " ");
    }
}

void postorderTraversalIterativeSingleStack(Node root) {
    if (root == null) return;
    Stack<Node> st = new Stack<>();
    Node curr = root;
    while (curr != null || !st.isEmpty()) {
        if (curr != null) {
            st.push(curr);
            curr = curr.left;
        } else {
            Node temp = st.peek().right;
            if (temp == null) {
                temp = st.pop();
                System.out.print(temp.data + " ");
                while (!st.isEmpty() && temp == st.peek().right) {
                    temp = st.pop();
                    System.out.print(temp.data + " ");
                }
            } else {
                curr = temp;
            }
        }
    }
}
```
### Level Order Traversal

```java
void levelOrderTraversal(Node root) {
    if (root == null) return;
    Queue<Node> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
        Node curr = queue.poll();
        System.out.print(curr.data + " ");
        if (curr.left != null) queue.add(curr.left);
        if (curr.right != null) queue.add(curr.right);
    }
}
```