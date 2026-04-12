import java.util.NoSuchElementException;

public class ListTester {

    private static final Integer A = 1;
    private static final Integer B = 2;
    private static final Integer C = 3;
    private static final Integer X = 99;

    private int pass = 0;
    private int fail = 0;

    private IndexedUnsortedList<Integer> newList(boolean good) {
        return good ? new GoodList<>() : new BadList<>();
    }

    // =========================
    // VERIFY METHODS
    // =========================
    private void verify(String name, Object actual, Object expected) {
        if ((actual == null && expected != null) ||
            (actual != null && !actual.equals(expected))) {
            fail++;
            System.out.println("FAIL: " + name + " | expected: " + expected + " but got: " + actual);
        } else {
            pass++;
            System.out.println("PASS: " + name);
        }
    }

    private void expectException(String name, Runnable action, Class<?> expected) {
        try {
            action.run();
            fail++;
            System.out.println("FAIL: " + name + " | expected " + expected.getSimpleName());
        } catch (Exception e) {
            if (expected.isInstance(e)) {
                pass++;
                System.out.println("PASS: " + name);
            } else {
                fail++;
                System.out.println("FAIL: " + name + " | wrong exception");
            }
        }
    }

    // =========================
    // SCENARIO 2: [] → [A]
    // =========================
        private void scenario2(boolean good) {

        // ----------------------------
        // BASE STATE: [A]
        // ----------------------------
        IndexedUnsortedList<Integer> list = newList(good);
        list.addToFront(A);

        // ----------------------------
        // BASIC STATE TESTS
        // ----------------------------
        verify("S2 size", list.size(), 1);
        verify("S2 isEmpty", list.isEmpty(), false);
        verify("S2 first", list.first(), A);
        verify("S2 last", list.last(), A);
        verify("S2 get(0)", list.get(0), A);

        // ----------------------------
        // INDEX / SEARCH TESTS
        // ----------------------------
        verify("S2 indexOf A", list.indexOf(A), 0);
        verify("S2 indexOf X", list.indexOf(X), -1);
        verify("S2 contains A", list.contains(A), true);
        verify("S2 contains X", list.contains(X), false);

        // ----------------------------
        // GET EXCEPTIONS
        // ----------------------------
        expectException("S2 get(-1)", () -> list.get(-1), IndexOutOfBoundsException.class);
        expectException("S2 get(1)", () -> list.get(1), IndexOutOfBoundsException.class);

        // ----------------------------
        // ADD TESTS
        // ----------------------------

        // addToFront
        IndexedUnsortedList<Integer> l1 = newList(good);
        l1.addToFront(A);
        l1.addToFront(X);
        verify("S2 addToFront", l1.get(0), X);

        // addToRear
        IndexedUnsortedList<Integer> l2 = newList(good);
        l2.addToFront(A);
        l2.addToRear(X);
        verify("S2 addToRear", l2.get(1), X);

        // addAfter valid
        IndexedUnsortedList<Integer> l3 = newList(good);
        l3.addToFront(A);
        l3.addAfter(X, A);
        verify("S2 addAfter valid", l3.get(1), X);

        // addAfter invalid
        expectException("S2 addAfter missing",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToFront(A);
                l.addAfter(X, 999);
            },
            NoSuchElementException.class
        );

        // add(index) valid
        IndexedUnsortedList<Integer> l4 = newList(good);
        l4.addToFront(A);
        l4.add(0, X);
        verify("S2 add index 0", l4.get(0), X);

        IndexedUnsortedList<Integer> l5 = newList(good);
        l5.addToFront(A);
        l5.add(1, X);
        verify("S2 add index 1", l5.get(1), X);

        // add(index) invalid
        expectException("S2 add index -1",
            () -> newList(good).add(-1, X),
            IndexOutOfBoundsException.class
        );

        expectException("S2 add index 2",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToFront(A);
                l.add(2, X);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // REMOVE TESTS
        // ----------------------------

        // removeFirst
        IndexedUnsortedList<Integer> l6 = newList(good);
        l6.addToFront(A);
        verify("S2 removeFirst", l6.removeFirst(), A);

        // removeLast
        IndexedUnsortedList<Integer> l7 = newList(good);
        l7.addToFront(A);
        verify("S2 removeLast", l7.removeLast(), A);

        // remove(element)
        IndexedUnsortedList<Integer> l8 = newList(good);
        l8.addToFront(A);
        verify("S2 remove element", l8.remove(A), A);

        // remove(element) missing
        expectException("S2 remove missing",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToFront(A);
                l.remove(X);
            },
            NoSuchElementException.class
        );

        // remove(index)
        IndexedUnsortedList<Integer> l9 = newList(good);
        l9.addToFront(A);
        verify("S2 remove index 0", l9.remove(0), A);

        expectException("S2 remove index -1",
            () -> newList(good).remove(-1),
            IndexOutOfBoundsException.class
        );

        expectException("S2 remove index 1",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToFront(A);
                l.remove(1);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // SET TESTS
        // ----------------------------

        IndexedUnsortedList<Integer> l10 = newList(good);
        l10.addToFront(A);
        l10.set(0, X);
        verify("S2 set valid", l10.get(0), X);

        expectException("S2 set -1",
            () -> newList(good).set(-1, X),
            IndexOutOfBoundsException.class
        );

        expectException("S2 set 1",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToFront(A);
                l.set(1, X);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // STRING TEST
        // ----------------------------
        verify("S2 toString", list.toString(), "[1]");
    }


    // ----------------------------
    // SCENARIO 3: [] → [A] using addToRear
    // ----------------------------


    private void scenario3(boolean good) {

        // ----------------------------
        // BASE STATE: [A] using addToRear
        // ----------------------------
        IndexedUnsortedList<Integer> list = newList(good);
        list.addToRear(A);

        // ----------------------------
        // BASIC STATE TESTS
        // ----------------------------
        verify("S3 size", list.size(), 1);
        verify("S3 isEmpty", list.isEmpty(), false);
        verify("S3 first", list.first(), A);
        verify("S3 last", list.last(), A);
        verify("S3 get(0)", list.get(0), A);

        // ----------------------------
        // INDEX / SEARCH TESTS
        // ----------------------------
        verify("S3 indexOf A", list.indexOf(A), 0);
        verify("S3 indexOf X", list.indexOf(X), -1);
        verify("S3 contains A", list.contains(A), true);
        verify("S3 contains X", list.contains(X), false);

        // ----------------------------
        // GET EXCEPTIONS
        // ----------------------------
        expectException("S3 get(-1)", () -> list.get(-1), IndexOutOfBoundsException.class);
        expectException("S3 get(1)", () -> list.get(1), IndexOutOfBoundsException.class);

        // ----------------------------
        // ADD TESTS
        // ----------------------------

        // addToFront
        IndexedUnsortedList<Integer> l1 = newList(good);
        l1.addToRear(A);
        l1.addToFront(X);
        verify("S3 addToFront", l1.get(0), X);

        // addToRear
        IndexedUnsortedList<Integer> l2 = newList(good);
        l2.addToRear(A);
        l2.addToRear(X);
        verify("S3 addToRear", l2.get(1), X);

        // addAfter valid
        IndexedUnsortedList<Integer> l3 = newList(good);
        l3.addToRear(A);
        l3.addAfter(X, A);
        verify("S3 addAfter valid", l3.get(1), X);

        // addAfter invalid
        expectException("S3 addAfter missing",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.addAfter(X, 999);
            },
            NoSuchElementException.class
        );

        // add(index) valid
        IndexedUnsortedList<Integer> l4 = newList(good);
        l4.addToRear(A);
        l4.add(0, X);
        verify("S3 add index 0", l4.get(0), X);

        IndexedUnsortedList<Integer> l5 = newList(good);
        l5.addToRear(A);
        l5.add(1, X);
        verify("S3 add index 1", l5.get(1), X);

        // add(index) invalid
        expectException("S3 add index -1",
            () -> newList(good).add(-1, X),
            IndexOutOfBoundsException.class
        );

        expectException("S3 add index 2",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.add(2, X);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // REMOVE TESTS
        // ----------------------------

        // removeFirst
        IndexedUnsortedList<Integer> l6 = newList(good);
        l6.addToRear(A);
        verify("S3 removeFirst", l6.removeFirst(), A);

        // removeLast
        IndexedUnsortedList<Integer> l7 = newList(good);
        l7.addToRear(A);
        verify("S3 removeLast", l7.removeLast(), A);

        // remove(element)
        IndexedUnsortedList<Integer> l8 = newList(good);
        l8.addToRear(A);
        verify("S3 remove element", l8.remove(A), A);

        // remove(element) missing
        expectException("S3 remove missing",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.remove(X);
            },
            NoSuchElementException.class
        );

        // remove(index)
        IndexedUnsortedList<Integer> l9 = newList(good);
        l9.addToRear(A);
        verify("S3 remove index 0", l9.remove(0), A);

        expectException("S3 remove index -1",
            () -> newList(good).remove(-1),
            IndexOutOfBoundsException.class
        );

        expectException("S3 remove index 1",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.remove(1);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // SET TESTS
        // ----------------------------

        IndexedUnsortedList<Integer> l10 = newList(good);
        l10.addToRear(A);
        l10.set(0, X);
        verify("S3 set valid", l10.get(0), X);

        expectException("S3 set -1",
            () -> newList(good).set(-1, X),
            IndexOutOfBoundsException.class
        );

        expectException("S3 set 1",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.set(1, X);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // STRING TEST
        // ----------------------------
        verify("S3 toString", list.toString(), "[1]");
    }

    // ----------------------------
    // SCENARIO 7: [A] → [] using removeFirst
    // ----------------------------

    private void scenario7(boolean good) {

        // ----------------------------
        // BASE STATE: [A, B]
        // ----------------------------
        IndexedUnsortedList<Integer> list = newList(good);
        list.addToRear(A);
        list.addToRear(B);

        // ----------------------------
        // BASIC STATE TESTS
        // ----------------------------
        verify("S7 size", list.size(), 2);
        verify("S7 isEmpty", list.isEmpty(), false);
        verify("S7 first", list.first(), A);
        verify("S7 last", list.last(), B);
        verify("S7 get(0)", list.get(0), A);
        verify("S7 get(1)", list.get(1), B);

        // ----------------------------
        // INDEX / SEARCH TESTS
        // ----------------------------
        verify("S7 indexOf A", list.indexOf(A), 0);
        verify("S7 indexOf B", list.indexOf(B), 1);
        verify("S7 indexOf X", list.indexOf(X), -1);

        verify("S7 contains A", list.contains(A), true);
        verify("S7 contains B", list.contains(B), true);
        verify("S7 contains X", list.contains(X), false);

        // ----------------------------
        // GET EXCEPTIONS
        // ----------------------------
        expectException("S7 get(-1)", () -> list.get(-1), IndexOutOfBoundsException.class);
        expectException("S7 get(2)", () -> list.get(2), IndexOutOfBoundsException.class);

        // ----------------------------
        // ADD TESTS
        // ----------------------------

        // addToFront → [X, A, B]
        IndexedUnsortedList<Integer> l1 = newList(good);
        l1.addToRear(A);
        l1.addToRear(B);
        l1.addToFront(X);
        verify("S7 addToFront", l1.get(0), X);

        // addToRear → [A, B, X]
        IndexedUnsortedList<Integer> l2 = newList(good);
        l2.addToRear(A);
        l2.addToRear(B);
        l2.addToRear(X);
        verify("S7 addToRear", l2.get(2), X);

        // addAfter A → [A, X, B]
        IndexedUnsortedList<Integer> l3 = newList(good);
        l3.addToRear(A);
        l3.addToRear(B);
        l3.addAfter(X, A);
        verify("S7 addAfter A", l3.get(1), X);

        // addAfter B → [A, B, X]
        IndexedUnsortedList<Integer> l4 = newList(good);
        l4.addToRear(A);
        l4.addToRear(B);
        l4.addAfter(X, B);
        verify("S7 addAfter B", l4.get(2), X);

        // addAfter invalid
        expectException("S7 addAfter missing",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.addToRear(B);
                l.addAfter(X, 999);
            },
            NoSuchElementException.class
        );

        // add(index) 0 → [X, A, B]
        IndexedUnsortedList<Integer> l5 = newList(good);
        l5.addToRear(A);
        l5.addToRear(B);
        l5.add(0, X);
        verify("S7 add index 0", l5.get(0), X);

        // add(index) 1 → [A, X, B]
        IndexedUnsortedList<Integer> l6 = newList(good);
        l6.addToRear(A);
        l6.addToRear(B);
        l6.add(1, X);
        verify("S7 add index 1", l6.get(1), X);

        // add(index) 2 → [A, B, X]
        IndexedUnsortedList<Integer> l7 = newList(good);
        l7.addToRear(A);
        l7.addToRear(B);
        l7.add(2, X);
        verify("S7 add index 2", l7.get(2), X);

        // invalid index
        expectException("S7 add index -1",
            () -> newList(good).add(-1, X),
            IndexOutOfBoundsException.class
        );

        expectException("S7 add index 3",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.addToRear(B);
                l.add(3, X);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // REMOVE TESTS
        // ----------------------------

        // removeFirst → removes A
        IndexedUnsortedList<Integer> l8 = newList(good);
        l8.addToRear(A);
        l8.addToRear(B);
        verify("S7 removeFirst", l8.removeFirst(), A);

        // removeLast → removes B
        IndexedUnsortedList<Integer> l9 = newList(good);
        l9.addToRear(A);
        l9.addToRear(B);
        verify("S7 removeLast", l9.removeLast(), B);

        // remove(A)
        IndexedUnsortedList<Integer> l10 = newList(good);
        l10.addToRear(A);
        l10.addToRear(B);
        verify("S7 remove A", l10.remove(A), A);

        // remove(B)
        IndexedUnsortedList<Integer> l11 = newList(good);
        l11.addToRear(A);
        l11.addToRear(B);
        verify("S7 remove B", l11.remove(B), B);

        // remove missing
        expectException("S7 remove missing",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.addToRear(B);
                l.remove(X);
            },
            NoSuchElementException.class
        );

        // remove(index)
        IndexedUnsortedList<Integer> l12 = newList(good);
        l12.addToRear(A);
        l12.addToRear(B);
        verify("S7 remove index 0", l12.remove(0), A);

        IndexedUnsortedList<Integer> l13 = newList(good);
        l13.addToRear(A);
        l13.addToRear(B);
        verify("S7 remove index 1", l13.remove(1), B);

        // invalid index
        expectException("S7 remove index -1",
            () -> newList(good).remove(-1),
            IndexOutOfBoundsException.class
        );

        expectException("S7 remove index 2",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.addToRear(B);
                l.remove(2);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // SET TESTS
        // ----------------------------

        IndexedUnsortedList<Integer> l14 = newList(good);
        l14.addToRear(A);
        l14.addToRear(B);
        l14.set(0, X);
        verify("S7 set index 0", l14.get(0), X);

        IndexedUnsortedList<Integer> l15 = newList(good);
        l15.addToRear(A);
        l15.addToRear(B);
        l15.set(1, X);
        verify("S7 set index 1", l15.get(1), X);

        // invalid set
        expectException("S7 set -1",
            () -> newList(good).set(-1, X),
            IndexOutOfBoundsException.class
        );

        expectException("S7 set 2",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.addToRear(B);
                l.set(2, X);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // STRING TEST
        // ----------------------------
        verify("S7 toString", list.toString(), "[1, 2]");
    }
    
    // ----------------------------
    // SCENARIO 10: [A, B, C] → [B, C] using remove(A)
    // ----------------------------

    private void scenario10(boolean good) {

        // ----------------------------
        // BASE STATE: [B, A]
        // ----------------------------
        IndexedUnsortedList<Integer> list = newList(good);
        list.addToRear(A);
        list.add(0, B);

        // ----------------------------
        // BASIC STATE TESTS
        // ----------------------------
        verify("S10 size", list.size(), 2);
        verify("S10 isEmpty", list.isEmpty(), false);
        verify("S10 first", list.first(), B);
        verify("S10 last", list.last(), A);
        verify("S10 get(0)", list.get(0), B);
        verify("S10 get(1)", list.get(1), A);

        // ----------------------------
        // INDEX / SEARCH TESTS
        // ----------------------------
        verify("S10 indexOf B", list.indexOf(B), 0);
        verify("S10 indexOf A", list.indexOf(A), 1);
        verify("S10 indexOf X", list.indexOf(X), -1);

        verify("S10 contains B", list.contains(B), true);
        verify("S10 contains A", list.contains(A), true);
        verify("S10 contains X", list.contains(X), false);

        // ----------------------------
        // GET EXCEPTIONS
        // ----------------------------
        expectException("S10 get(-1)", () -> list.get(-1), IndexOutOfBoundsException.class);
        expectException("S10 get(2)", () -> list.get(2), IndexOutOfBoundsException.class);

        // ----------------------------
        // ADD TESTS
        // ----------------------------

        // addToFront → [X, B, A]
        IndexedUnsortedList<Integer> l1 = newList(good);
        l1.addToRear(A);
        l1.add(0, B);
        l1.addToFront(X);
        verify("S10 addToFront", l1.get(0), X);

        // addToRear → [B, A, X]
        IndexedUnsortedList<Integer> l2 = newList(good);
        l2.addToRear(A);
        l2.add(0, B);
        l2.addToRear(X);
        verify("S10 addToRear", l2.get(2), X);

        // addAfter B → [B, X, A]
        IndexedUnsortedList<Integer> l3 = newList(good);
        l3.addToRear(A);
        l3.add(0, B);
        l3.addAfter(X, B);
        verify("S10 addAfter B", l3.get(1), X);

        // addAfter A → [B, A, X]
        IndexedUnsortedList<Integer> l4 = newList(good);
        l4.addToRear(A);
        l4.add(0, B);
        l4.addAfter(X, A);
        verify("S10 addAfter A", l4.get(2), X);

        // addAfter invalid
        expectException("S10 addAfter missing",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.add(0, B);
                l.addAfter(X, 999);
            },
            NoSuchElementException.class
        );

        // add(index) 0 → [X, B, A]
        IndexedUnsortedList<Integer> l5 = newList(good);
        l5.addToRear(A);
        l5.add(0, B);
        l5.add(0, X);
        verify("S10 add index 0", l5.get(0), X);

        // add(index) 1 → [B, X, A]
        IndexedUnsortedList<Integer> l6 = newList(good);
        l6.addToRear(A);
        l6.add(0, B);
        l6.add(1, X);
        verify("S10 add index 1", l6.get(1), X);

        // add(index) 2 → [B, A, X]
        IndexedUnsortedList<Integer> l7 = newList(good);
        l7.addToRear(A);
        l7.add(0, B);
        l7.add(2, X);
        verify("S10 add index 2", l7.get(2), X);

        // invalid index
        expectException("S10 add index -1",
            () -> newList(good).add(-1, X),
            IndexOutOfBoundsException.class
        );

        expectException("S10 add index 3",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.add(0, B);
                l.add(3, X);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // REMOVE TESTS
        // ----------------------------

        // removeFirst → removes B
        IndexedUnsortedList<Integer> l8 = newList(good);
        l8.addToRear(A);
        l8.add(0, B);
        verify("S10 removeFirst", l8.removeFirst(), B);

        // removeLast → removes A
        IndexedUnsortedList<Integer> l9 = newList(good);
        l9.addToRear(A);
        l9.add(0, B);
        verify("S10 removeLast", l9.removeLast(), A);

        // remove(B)
        IndexedUnsortedList<Integer> l10 = newList(good);
        l10.addToRear(A);
        l10.add(0, B);
        verify("S10 remove B", l10.remove(B), B);

        // remove(A)
        IndexedUnsortedList<Integer> l11 = newList(good);
        l11.addToRear(A);
        l11.add(0, B);
        verify("S10 remove A", l11.remove(A), A);

        // remove missing
        expectException("S10 remove missing",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.add(0, B);
                l.remove(X);
            },
            NoSuchElementException.class
        );

        // remove(index)
        IndexedUnsortedList<Integer> l12 = newList(good);
        l12.addToRear(A);
        l12.add(0, B);
        verify("S10 remove index 0", l12.remove(0), B);

        IndexedUnsortedList<Integer> l13 = newList(good);
        l13.addToRear(A);
        l13.add(0, B);
        verify("S10 remove index 1", l13.remove(1), A);

        // invalid index
        expectException("S10 remove index -1",
            () -> newList(good).remove(-1),
            IndexOutOfBoundsException.class
        );

        expectException("S10 remove index 2",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.add(0, B);
                l.remove(2);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // SET TESTS
        // ----------------------------

        IndexedUnsortedList<Integer> l14 = newList(good);
        l14.addToRear(A);
        l14.add(0, B);
        l14.set(0, X);
        verify("S10 set index 0", l14.get(0), X);

        IndexedUnsortedList<Integer> l15 = newList(good);
        l15.addToRear(A);
        l15.add(0, B);
        l15.set(1, X);
        verify("S10 set index 1", l15.get(1), X);

        // invalid set
        expectException("S10 set -1",
            () -> newList(good).set(-1, X),
            IndexOutOfBoundsException.class
        );

        expectException("S10 set 2",
            () -> {
                IndexedUnsortedList<Integer> l = newList(good);
                l.addToRear(A);
                l.add(0, B);
                l.set(2, X);
            },
            IndexOutOfBoundsException.class
        );

        // ----------------------------
        // STRING TEST
        // ----------------------------
        verify("S10 toString", list.toString(), "[2, 1]");
    }
     // =========================
    // SCENARIO 12: [A, B, C] → [A, C] using remove(B)
    // =========================
        
    private void scenario12(boolean good) {

    // ----------------------------
    // BASE STATE: []
    // ----------------------------
    IndexedUnsortedList<Integer> list = newList(good);
    list.addToRear(A);
    list.removeFirst();

    // ----------------------------
    // BASIC STATE TESTS
    // ----------------------------
    verify("S12 size", list.size(), 0);
    verify("S12 isEmpty", list.isEmpty(), true);

    // ----------------------------
    // EXCEPTION TESTS (EMPTY LIST)
    // ----------------------------
    expectException("S12 first", () -> list.first(), NoSuchElementException.class);
    expectException("S12 last", () -> list.last(), NoSuchElementException.class);
    expectException("S12 removeFirst", () -> list.removeFirst(), NoSuchElementException.class);
    expectException("S12 removeLast", () -> list.removeLast(), NoSuchElementException.class);
    expectException("S12 get(0)", () -> list.get(0), IndexOutOfBoundsException.class);

    // ----------------------------
    // ADD TESTS
    // ----------------------------

    // addToFront → [X]
    IndexedUnsortedList<Integer> l1 = newList(good);
    l1.addToRear(A);
    l1.removeFirst();
    l1.addToFront(X);
    verify("S12 addToFront", l1.get(0), X);

    // addToRear → [X]
    IndexedUnsortedList<Integer> l2 = newList(good);
    l2.addToRear(A);
    l2.removeFirst();
    l2.addToRear(X);
    verify("S12 addToRear", l2.get(0), X);

    // addAfter invalid (empty list)
    expectException("S12 addAfter missing",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.removeFirst();
            l.addAfter(X, A);
        },
        NoSuchElementException.class
    );

    // add(index) valid → [X]
    IndexedUnsortedList<Integer> l3 = newList(good);
    l3.addToRear(A);
    l3.removeFirst();
    l3.add(0, X);
    verify("S12 add index 0", l3.get(0), X);

    // add(index) invalid
    expectException("S12 add index -1",
        () -> newList(good).add(-1, X),
        IndexOutOfBoundsException.class
    );

    expectException("S12 add index 1",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.removeFirst();
            l.add(1, X);
        },
        IndexOutOfBoundsException.class
    );

    // ----------------------------
    // REMOVE TESTS
    // ----------------------------

    // remove(element) invalid
    expectException("S12 remove element",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.removeFirst();
            l.remove(A);
        },
        NoSuchElementException.class
    );

    // remove(index) invalid
    expectException("S12 remove index 0",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.removeFirst();
            l.remove(0);
        },
        IndexOutOfBoundsException.class
    );

    // ----------------------------
    // SET TESTS
    // ----------------------------

    expectException("S12 set 0",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.removeFirst();
            l.set(0, X);
        },
        IndexOutOfBoundsException.class
    );

    expectException("S12 set -1",
        () -> newList(good).set(-1, X),
        IndexOutOfBoundsException.class
    );

    // ----------------------------
    // SEARCH TESTS
    // ----------------------------
    verify("S12 indexOf X", list.indexOf(X), -1);
    verify("S12 contains X", list.contains(X), false);

    // ----------------------------
    // STRING TEST
    // ----------------------------
    verify("S12 toString", list.toString(), "[]");
}


// =========================
// SCENARIO 14: [A] → [] using remove(A)
// =========================

private void scenario14(boolean good) {

    // ----------------------------
    // BASE STATE: []
    // ----------------------------
    IndexedUnsortedList<Integer> list = newList(good);
    list.addToRear(A);
    list.remove(A);

    // ----------------------------
    // BASIC STATE TESTS
    // ----------------------------
    verify("S14 size", list.size(), 0);
    verify("S14 isEmpty", list.isEmpty(), true);

    // ----------------------------
    // EXCEPTION TESTS (EMPTY LIST)
    // ----------------------------
    expectException("S14 first", () -> list.first(), NoSuchElementException.class);
    expectException("S14 last", () -> list.last(), NoSuchElementException.class);
    expectException("S14 removeFirst", () -> list.removeFirst(), NoSuchElementException.class);
    expectException("S14 removeLast", () -> list.removeLast(), NoSuchElementException.class);
    expectException("S14 get(0)", () -> list.get(0), IndexOutOfBoundsException.class);

    // ----------------------------
    // ADD TESTS
    // ----------------------------

    // addToFront → [X]
    IndexedUnsortedList<Integer> l1 = newList(good);
    l1.addToRear(A);
    l1.remove(A);
    l1.addToFront(X);
    verify("S14 addToFront", l1.get(0), X);

    // addToRear → [X]
    IndexedUnsortedList<Integer> l2 = newList(good);
    l2.addToRear(A);
    l2.remove(A);
    l2.addToRear(X);
    verify("S14 addToRear", l2.get(0), X);

    // addAfter invalid (empty list)
    expectException("S14 addAfter missing",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.remove(A);
            l.addAfter(X, A);
        },
        NoSuchElementException.class
    );

    // add(index) valid → [X]
    IndexedUnsortedList<Integer> l3 = newList(good);
    l3.addToRear(A);
    l3.remove(A);
    l3.add(0, X);
    verify("S14 add index 0", l3.get(0), X);

    // invalid add index
    expectException("S14 add index -1",
        () -> newList(good).add(-1, X),
        IndexOutOfBoundsException.class
    );

    expectException("S14 add index 1",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.remove(A);
            l.add(1, X);
        },
        IndexOutOfBoundsException.class
    );

    // ----------------------------
    // REMOVE TESTS
    // ----------------------------

    // remove(element) on empty
    expectException("S14 remove element",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.remove(A);
            l.remove(A);
        },
        NoSuchElementException.class
    );

    // remove(index) invalid
    expectException("S14 remove index 0",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.remove(A);
            l.remove(0);
        },
        IndexOutOfBoundsException.class
    );

    // ----------------------------
    // SET TESTS
    // ----------------------------

    expectException("S14 set 0",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.remove(A);
            l.set(0, X);
        },
        IndexOutOfBoundsException.class
    );

    expectException("S14 set -1",
        () -> newList(good).set(-1, X),
        IndexOutOfBoundsException.class
    );

    // ----------------------------
    // SEARCH TESTS
    // ----------------------------
    verify("S14 indexOf A", list.indexOf(A), -1);
    verify("S14 contains A", list.contains(A), false);

    // ----------------------------
    // STRING TEST
    // ----------------------------
    verify("S14 toString", list.toString(), "[]");

    // ----------------------------
    // RETURN VALUE TEST (IMPORTANT)
    // ----------------------------
    IndexedUnsortedList<Integer> l4 = newList(good);
    l4.addToRear(A);
    verify("S14 remove return value", l4.remove(A), A);
}


// =========================
// SCENARIO 26: [A, B] → [A] using removeLast  
// =========================



private void scenario26(boolean good) {

    // ----------------------------
    // BASE STATE: [A]
    // ----------------------------
    IndexedUnsortedList<Integer> list = newList(good);
    list.addToRear(A);
    list.addToRear(B);
    list.removeLast();

    // ----------------------------
    // BASIC STATE TESTS
    // ----------------------------
    verify("S26 size", list.size(), 1);
    verify("S26 isEmpty", list.isEmpty(), false);
    verify("S26 first", list.first(), A);
    verify("S26 last", list.last(), A);
    verify("S26 get(0)", list.get(0), A);

    // ----------------------------
    // INDEX / SEARCH TESTS
    // ----------------------------
    verify("S26 indexOf A", list.indexOf(A), 0);
    verify("S26 indexOf B", list.indexOf(B), -1);
    verify("S26 contains A", list.contains(A), true);
    verify("S26 contains B", list.contains(B), false);

    // ----------------------------
    // GET EXCEPTIONS
    // ----------------------------
    expectException("S26 get(-1)", () -> list.get(-1), IndexOutOfBoundsException.class);
    expectException("S26 get(1)", () -> list.get(1), IndexOutOfBoundsException.class);

    // ----------------------------
    // ADD TESTS
    // ----------------------------

    // addToFront → [X, A]
    IndexedUnsortedList<Integer> l1 = newList(good);
    l1.addToRear(A);
    l1.addToRear(B);
    l1.removeLast();
    l1.addToFront(X);
    verify("S26 addToFront", l1.get(0), X);

    // addToRear → [A, X]
    IndexedUnsortedList<Integer> l2 = newList(good);
    l2.addToRear(A);
    l2.addToRear(B);
    l2.removeLast();
    l2.addToRear(X);
    verify("S26 addToRear", l2.get(1), X);

    // addAfter A → [A, X]
    IndexedUnsortedList<Integer> l3 = newList(good);
    l3.addToRear(A);
    l3.addToRear(B);
    l3.removeLast();
    l3.addAfter(X, A);
    verify("S26 addAfter", l3.get(1), X);

    // addAfter invalid
    expectException("S26 addAfter missing",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.addToRear(B);
            l.removeLast();
            l.addAfter(X, 999);
        },
        NoSuchElementException.class
    );

    // add(index)
    IndexedUnsortedList<Integer> l4 = newList(good);
    l4.addToRear(A);
    l4.addToRear(B);
    l4.removeLast();
    l4.add(0, X);
    verify("S26 add index 0", l4.get(0), X);

    IndexedUnsortedList<Integer> l5 = newList(good);
    l5.addToRear(A);
    l5.addToRear(B);
    l5.removeLast();
    l5.add(1, X);
    verify("S26 add index 1", l5.get(1), X);

    // invalid add
    expectException("S26 add index -1",
        () -> newList(good).add(-1, X),
        IndexOutOfBoundsException.class
    );

    expectException("S26 add index 2",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.addToRear(B);
            l.removeLast();
            l.add(2, X);
        },
        IndexOutOfBoundsException.class
    );

    // ----------------------------
    // REMOVE TESTS
    // ----------------------------

    // removeFirst → removes A
    IndexedUnsortedList<Integer> l6 = newList(good);
    l6.addToRear(A);
    l6.addToRear(B);
    l6.removeLast();
    verify("S26 removeFirst", l6.removeFirst(), A);

    // removeLast → removes A (only element left)
    IndexedUnsortedList<Integer> l7 = newList(good);
    l7.addToRear(A);
    l7.addToRear(B);
    l7.removeLast();
    verify("S26 removeLast", l7.removeLast(), A);

    // remove(A)
    IndexedUnsortedList<Integer> l8 = newList(good);
    l8.addToRear(A);
    l8.addToRear(B);
    l8.removeLast();
    verify("S26 remove A", l8.remove(A), A);

    // remove missing
    expectException("S26 remove missing",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.addToRear(B);
            l.removeLast();
            l.remove(B);
        },
        NoSuchElementException.class
    );

    // remove(index)
    IndexedUnsortedList<Integer> l9 = newList(good);
    l9.addToRear(A);
    l9.addToRear(B);
    l9.removeLast();
    verify("S26 remove index 0", l9.remove(0), A);

    // invalid remove
    expectException("S26 remove index -1",
        () -> newList(good).remove(-1),
        IndexOutOfBoundsException.class
    );

    expectException("S26 remove index 1",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.addToRear(B);
            l.removeLast();
            l.remove(1);
        },
        IndexOutOfBoundsException.class
    );

    // ----------------------------
    // SET TESTS
    // ----------------------------

    IndexedUnsortedList<Integer> l10 = newList(good);
    l10.addToRear(A);
    l10.addToRear(B);
    l10.removeLast();
    l10.set(0, X);
    verify("S26 set", l10.get(0), X);

    expectException("S26 set -1",
        () -> newList(good).set(-1, X),
        IndexOutOfBoundsException.class
    );

    expectException("S26 set 1",
        () -> {
            IndexedUnsortedList<Integer> l = newList(good);
            l.addToRear(A);
            l.addToRear(B);
            l.removeLast();
            l.set(1, X);
        },
        IndexOutOfBoundsException.class
    );

    // ----------------------------
    // STRING TEST
    // ----------------------------
    verify("S26 toString", list.toString(), "[1]");

    // ----------------------------
    // RETURN VALUE TEST (IMPORTANT)
    // ----------------------------
    IndexedUnsortedList<Integer> l11 = newList(good);
    l11.addToRear(A);
    l11.addToRear(B);
    verify("S26 removeLast return", l11.removeLast(), B);
}


    // =========================
    // RUN ALL
    // =========================
    
    private void runAll(boolean good) {
        System.out.println("\n==============================");
        System.out.println(good ? "Testing GoodList" : "Testing BadList");
        System.out.println("==============================");

        scenario2(good);
        scenario3(good);
        scenario7(good);
        scenario10(good);
        scenario12(good);
        scenario14(good);
        scenario26(good);

        System.out.println("\nTotal Passed: " + pass);
        System.out.println("Total Failed: " + fail);
        System.out.println("Total Tests: " + (pass + fail));
    }

    public static void main(String[] args) {
        ListTester t = new ListTester();

        t.runAll(true);
        t.pass = 0;
        t.fail = 0;
        t.runAll(false);
    }
}