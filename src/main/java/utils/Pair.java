package utils;

public record Pair<T, U>(T first, U second) {

  public T getFirst() {
    return first;
  }

  public U getSecond() {
    return second;
  }
}
