package gavilan.jacop.kakuro;

import gavilan.jacop.kakuro.Cell;
import gavilan.jacop.kakuro.Kakuro;
import gavilan.jacop.kakuro.SimplePair;
import static gavilan.jacop.kakuro.Kakuro.a;
import static gavilan.jacop.kakuro.Kakuro.concatLists;
import static gavilan.jacop.kakuro.Kakuro.d;
import static gavilan.jacop.kakuro.Kakuro.da;
import static gavilan.jacop.kakuro.Kakuro.drawRow;
import static gavilan.jacop.kakuro.Kakuro.e;
import static gavilan.jacop.kakuro.Kakuro.gatherValues;
import static gavilan.jacop.kakuro.Kakuro.solver;
import static gavilan.jacop.kakuro.Kakuro.takeWhile;
import static gavilan.jacop.kakuro.Kakuro.transpose;
import static gavilan.jacop.kakuro.Kakuro.v;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestKakuroJacop {

@Test
public void testDrawEmpty() {
  String result = e().draw();
  assertEquals("   -----  ", result);
}

@Test
public void testDrawAcross() {
  String result = a(5).draw();
  assertEquals("   --\\ 5  ", result);
}

@Test
public void testDrawDown() {
  String result = d(4).draw();
  assertEquals("    4\\--  ", result);
}

@Test
public void testDrawDownAcross() {
  String result = da(3, 4).draw();
  assertEquals("    3\\ 4  ", result);
}

@Test
public void testDrawValues() {
  String result = v().draw();
  assertEquals(" 123456789", result);
  String result12 = v(1, 2).draw();
  assertEquals(" 12.......", result12);
}

@Test
public void testDrawRow() {
  List<Cell> line = asList(da(3, 4), v(), v(1, 2), d(4), e(), a(5), v(4), v(1));
  String result = drawRow(line);
  assertEquals("    3\\ 4   123456789 12.......    4\\--     -----     --\\ 5       4         1    \n", result);
}

@Test
public void testTranspose() {
  List<List<Integer>> ints = IntStream.range(0, 3)
    .mapToObj(i -> IntStream.range(0, 4)
      .boxed()
      .collect(toList()))
    .collect(toList());
  List<List<Integer>> tr = transpose(ints);
  assertEquals(ints.size(), tr.get(0).size());
  assertEquals(ints.get(0).size(), tr.size());
}

@Test
public void testValueEquality() {
  assertEquals(v(), v());
  assertEquals(v(1, 2), v(1, 2));
}

@Test
public void testTakeWhile() {
  List<Integer> result = takeWhile(n -> n < 4, IntStream.range(0, 10).mapToObj(i -> i).collect(toList()));
  assertEquals(4, result.size());
}

@Test
public void testConcat() {
  List<Integer> a = asList(1, 2, 3);
  List<Integer> b = asList(4, 5, 6, 1, 2, 3);
  List<Integer> result = concatLists(a, b);
  assertEquals(9, result.size());
}

@Test
public void testDrop() {
  List<Integer> a = asList(1, 2, 3, 4, 5, 6);
  List<Integer> result = Kakuro.drop(4, a);
  assertEquals(2, result.size());
}

@Test
public void testTake() {
  List<Integer> a = asList(1, 2, 3, 4, 5, 6);
  List<Integer> result = Kakuro.take(4, a);
  assertEquals(4, result.size());
}

@Test
public void testPartBy() {
  List<Integer> data = asList(1, 2, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 9);
  List<List<Integer>> result = Kakuro.partitionBy(n -> 0 == (n % 2), data);
  assertEquals(9, result.size());
}

@Test
public void testPartAll() {
  List<Integer> data = Arrays.asList(1, 2, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 9);
  List<List<Integer>> result = Kakuro.partitionAll(5, 3, data);
  assertEquals(5, result.size());
}

@Test
public void testPartN() {
  List<Integer> data = Arrays.asList(1, 2, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 9);
  List<List<Integer>> result = Kakuro.partitionN(5, data);
  assertEquals(3, result.size());
}

@Test
public void testGatherValues() {
  List<Cell> line = Arrays.asList(da(3, 4), v(), v(), d(4), e(), a(4), v(), v());
  List<List<Cell>> result = gatherValues(line);
  assertEquals(4, result.size());
  assertEquals(da(3, 4), result.get(0).get(0));
  assertEquals(d(4), result.get(2).get(0));
  assertEquals(e(), result.get(2).get(1));
  assertEquals(a(4), result.get(2).get(2));
}

@Test
public void testPairTargets() {
  List<Cell> line = asList(da(3, 4), v(), v(), d(4), e(), a(4), v(), v());
  List<SimplePair<List<Cell>>> result = Kakuro.pairTargetsWithValues(line);
  assertEquals(2, result.size());
  assertEquals(da(3, 4), result.get(0).left.get(0));
  assertEquals(d(4), result.get(1).left.get(0));
  assertEquals(e(), result.get(1).left.get(1));
  assertEquals(a(4), result.get(1).left.get(2));
}

@Test
public void testSolver() {
  List<List<Cell>> grid1 = asList(
    asList(e(), d(4), d(22), e(), d(16), d(3)),
    asList(a(3), v(), v(), da(16, 6), v(), v()),
    asList(a(18), v(), v(), v(), v(), v()),
    asList(e(), da(17, 23), v(), v(), v(), d(14)),
    asList(a(9), v(), v(), a(6), v(), v()),
    asList(a(15), v(), v(), a(12), v(), v()));
  List<List<Cell>> result = solver(grid1);
  assertEquals("   --\\ 3       1         2       16\\ 6       4         2    \n", drawRow(result.get(1)));
  assertEquals("   --\\18       3         5         7         2         1    \n", drawRow(result.get(2)));
  assertEquals("   -----     17\\23       8         9         6       14\\--  \n", drawRow(result.get(3)));
  assertEquals("   --\\ 9       8         1       --\\ 6       1         5    \n", drawRow(result.get(4)));
  assertEquals("   --\\15       9         6       --\\12       3         9    \n", drawRow(result.get(5)));
}

}
