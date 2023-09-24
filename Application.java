import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Application {

  private static final Random RAND = new Random();

  private static final StringBuilder RESULT = new StringBuilder();

  public static void main(String[] args) throws IOException {

    measure(
        "docs/random.csv",
        (List<Pair<Integer, Integer>> pairs) -> {
        }
    );

    measure(
        "docs/sortedAsc.csv",
        Collections::sort
    );

    measure(
        "docs/sortedDesc.csv",
        (List<Pair<Integer, Integer>> pairs) -> {
          Collections.sort(pairs);
          Collections.reverse(pairs);
        }
    );
  }

  private static void measure(String fileName, Consumer<List<Pair<Integer, Integer>>> consumer)
      throws IOException {
    RESULT.append("size,insertTime,getTime,deleteTime\n");
    for (int i = 0; i < 10000; i += 100) {
      RESULT.append(String.format("%s,", i));
      List<Pair<Integer, Integer>> pairs = generateSequence(i);
      consumer.accept(pairs);
      measure(pairs);
      RESULT.append("\n");
    }
    PrintWriter writer = new PrintWriter(new FileWriter(fileName));
    writer.write(RESULT.toString());
    RESULT.setLength(0);
    writer.close();
  }

  private static void measure(List<Pair<Integer, Integer>> pairs) {
    BST<Integer, Integer> tree = new BST<>();
    measure(() -> pairs.forEach(pair -> tree.put(pair.key, pair.value)), false);
    measure(() -> pairs.forEach(pair -> tree.get(pair.key)), false);
    measure(() -> pairs.forEach(pair -> tree.delete(pair.key)), true);
  }

  private static List<Pair<Integer, Integer>> generateSequence(int size) {
    List<Pair<Integer, Integer>> result = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      int value = RAND.nextInt(size);
      result.add(new Pair<>(value, value));
    }
    return result;
  }

  private static void measure(Runnable function, boolean isLast) {
    Instant now = Instant.now();
    function.run();

    RESULT.append(String.format(isLast ? "%s" : "%s,",
        Duration.between(now, Instant.now()).toNanos() / 1000))
    ;
  }
}

