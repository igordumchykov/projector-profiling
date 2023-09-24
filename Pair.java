class Pair<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<Pair<K, V>> {
  K key;
  V value;

  public Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public int compareTo(Pair<K, V> that) {
    return this.key.compareTo(that.key);
  }
}
