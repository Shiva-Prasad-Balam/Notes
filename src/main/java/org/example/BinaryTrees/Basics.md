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
        System.out.print(current.data + " ");
        if (current.right != null) stack.push(current.right);
        if (current.left != null) stack.push(current.left);
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
                System.out.print(right.data + " ");
                while (!st.isEmpty() && temp == st.peek().right) {
                    temp = st.pop();
                    System.out.print(right.data + " ");
                }
            } else {
                curr = right;
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