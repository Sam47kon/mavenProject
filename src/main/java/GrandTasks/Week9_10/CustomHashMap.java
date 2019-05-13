package GrandTasks.Week9_10;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CustomHashMap<K, V> implements Map<K, V> {
    //    private Set<Map.Entry<K, V>> entrySet;
    private MyNode<K, V>[] hashTable;
    private int size;
    private double threshold;   // порог размера

    public CustomHashMap() {
        hashTable = new MyNode[16];
        threshold = hashTable.length * 0.75;
    }


    /**
     * Для вставки в нужную ячейку массива hashTable по переданному ключу
     *
     * @return индекс, куда положить пару
     */
    private int getIndex(final K key) {
        int hash = 17 * 37 + key.hashCode();
        return hash % hashTable.length;
    }

//    /**
//     * Для вставки в нужную ячейку массива hashTable по переданному node
//     *
//     * @return индекс, куда положить пару
//     */
//    private int getIndex(MyNode<K, V> node) {
//        return node.hashCode() % hashTable.length;
//    }


    private void increaseTable() {
        MyNode<K, V>[] oldTable = hashTable;
        hashTable = new MyNode[oldTable.length * 2];
        size = 0;
        for (MyNode<K, V> nodes : oldTable) {
            if (nodes != null) {
                for (MyNode<K, V> node : nodes.getNodes()) {
                    put(node.key, node.value);
                }
            }
        }
    }

    private boolean keyExist(MyNode<K, V> node, MyNode<K, V> newNode) {
        return Objects.equals(node.key, newNode.key) && !Objects.equals(node.value, newNode.value);
    }

    private V newValueToExistingKey(MyNode<K, V> node, V value) {
        return node.setValue(value);
    }

    private boolean itCollision(MyNode<K, V> node, MyNode<K, V> newNode) {
        return node.hashCode() == newNode.hashCode() &&
                !Objects.equals(node.key, newNode.key) &&
                !Objects.equals(node.value, newNode.value);
    }

    private void collision(MyNode<K, V> newNode, List<MyNode<K, V>> nodes) {
        nodes.add(newNode);
        size++;
    }

//----------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------
// Override Map

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Содержит ли данная коллекция объект с ключем key
     *
     * @return true, если есть такой ключ
     */
    @Override
    public boolean containsKey(Object key) {
        K k = (K) key;
        int index = getIndex(k);
        if (hashTable[index] != null) {
            return hashTable[index].getNodes() != null;
        }
        return false;
    }

    /**
     * Содержит ли данная коллекция объект value
     *
     * @return true, если имеется такой объект
     */
    @Override
    public boolean containsValue(Object value) {
        if (size > 0) {
            for (MyNode<K, V> node : hashTable) {
                if (node != null) {
                    List<MyNode<K, V>> nodeList = node.getNodes();
                    for (MyNode<K, V> n : nodeList) {
                        if (Objects.equals(n.value, value)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Возвращает объект коллецкии по ключу key, если таковой есть
     */
    @Override
    public V get(Object key) {
        if (size > 0) {
            K k = (K) key;
            int index = getIndex(k);
            try {
                if (hashTable[index] == null) {
                    return null;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
            List<MyNode<K, V>> nodeList = hashTable[index].getNodes();
            if (nodeList.size() == 1) {
                return nodeList.get(0).value;
            }

            // не совсем уверен на счет цикла do while, он нужен чтобы вытаскивать значения при коллизии
            do {

                for (MyNode<K, V> node : nodeList) {
                    if (Objects.equals(node.key, k)) {
                        return node.value;
                    }
                }

                nodeList = nodeList.get(0).getNodes();
            } while (nodeList != null);
        }
        return null;
    }

    /**
     * Вставить пару ключ key и значение value в коллекцию CustomHashMap
     *
     * @return oldValue, если значение перезаписанно, иначе null
     */
    @Nullable
    @Override
    public V put(K key, V value) {
        if (size + 1 >= threshold) {
            threshold *= 2;
            increaseTable();
        }

        MyNode<K, V> newNode = new MyNode<>(key, value);
        int index = getIndex(key);

        if (hashTable[index] == null) {
            hashTable[index] = new MyNode<>(null, null);
            hashTable[index].getNodes().add(newNode);
            size++;
            return null;
        }

        List<MyNode<K, V>> nodes = hashTable[index].getNodes();

        for (MyNode<K, V> node : nodes) {
            if (keyExist(node, newNode)) {
                return newValueToExistingKey(node, value);
            }
            if (itCollision(node, newNode)) {
                collision(newNode, nodes);
                return null;
            }
        }
        return null;
    }

    /**
     * Удаляет объект коллекции по ключу key, если таковой имеется
     *
     * @return удаленный объект
     */
    @Override
    public V remove(Object key) {
        K k = (K) key;
        int index = getIndex(k);
        if (hashTable[index] == null) {
            return null;
        }

        List<MyNode<K, V>> nodeList = hashTable[index].getNodes();
        V oldElement = null;
        if (nodeList.size() == 1) {
            oldElement = nodeList.get(0).value;
            hashTable[index] = null;
            size--;
            return oldElement;
        }
        for (MyNode<K, V> node : nodeList) {
            if (k.equals(node.key)) {
                oldElement = node.value;
                nodeList.remove(node);
                size--;
            }
        }
        return oldElement;
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) { // TODO остановился здесь

    }

    @Override
    public void clear() {

    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return null;
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return null;
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }


    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("{");
        if (size == 0) {
            return st.append("}").toString();
        }
        for (MyNode<K, V> node : hashTable) {
            try {
                List<MyNode<K, V>> nodes = node.getNodes();
                if (nodes != null) {
                    st.append(nodes.toString());
                }
            } catch (NullPointerException e) {
                continue;
            }

        }
        return st.append("}").toString();
    }

//----------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------

//    @Override
//    public int hashCode() {
//        return super.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        return super.equals(obj);
//    }
//
//    @Override
//    public String toString() {
//        return super.toString();
//    }

//----------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------

    class MyNode<k, v> implements Map.Entry<k, v> {
        //        MyNode<k, v> next;
        List<MyNode<k, v>> nodes;
        k key;
        int hash;
        v value;

        private MyNode(k key, v value) {
            this.key = key;
            this.value = value;
            nodes = new LinkedList<>();
        }

        private List<MyNode<k, v>> getNodes() {
            return nodes;
        }

//        private int getIndex() {
//            return hashCode() % hashTable.length;
//        }

        @Override
        public k getKey() {
            return key;
        }

        @Override
        public v getValue() {
            return value;
        }

        @Override
        public v setValue(v newValue) { // возвращает старое значение, устанавливает новое
            v oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public final int hashCode() {
            hash = 17 * 37 + key.hashCode();
            return hash;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Map.Entry) {
                MyNode<?, ?> node = (MyNode<?, ?>) obj; // unchecked
                return Objects.equals(value, node.value) && Objects.equals(key, node.key);
            }
            return false;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
