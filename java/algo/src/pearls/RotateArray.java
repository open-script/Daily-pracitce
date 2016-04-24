package pearls;

import java.util.Arrays;

/**
 * Created by zzt on 4/22/16.
 * <p>
 * <h3>Rotation usage</h3>
 * <li>swapping adjacent blocks of memory of unequal size</li>
 */
public class RotateArray<T> {
    enum Direction {
        LEFT {
            @Override
            public Direction reverse() {
                return RIGHT;
            }
        }, RIGHT {
            @Override
            public Direction reverse() {
                return LEFT;
            }
        },;

        public abstract Direction reverse();
    }

    private class Rotation {
        private int dis;
        private Direction dir;

        public Rotation(int dis, Direction dir) {
            this.dis = dis;
            this.dir = dir;
        }

        public void setDirAndDis(Direction dir, int dis) {
            this.dis = dis;
            this.dir = dir;
        }

        public int getDis() {
            return dis;
        }

        public Direction getDir() {
            return dir;
        }

        public Rotation minimize(int len) {
            if (len < dis * 2) {
                return new Rotation(len - dis, dir.reverse());
            }
            return this;
        }
    }

    private T[] array;

    public RotateArray(T[] array) {
        this.array = array;
    }

    public T[] rotate(Direction direction, int n) {
        Rotation rotation = minimizeDistance(array.length, new Rotation(n, direction));
        rotate(rotation, array);
        //        reverseRotate(rotation, array);
        dynamicRotate(rotation, array);
        return array;
    }

    /**
     * @param rotation rotation request
     * @param array    input
     *
     * @implNote ab -> ba
     * <li>a^r b^r</li>
     * <li>(a^r b^r)^r = ba</li>
     */
    private void reverseRotate(Rotation rotation, T[] array) {
        int len = rotation.dis;
        int length = array.length;
        if (rotation.getDir() == Direction.RIGHT) {
            reverse(array, 0, length - len);
            reverse(array, length - len, len);
            reverse(array, 0, length);
        } else {
            reverse(array, 0, len);
            reverse(array, len, length - len);
            reverse(array, 0, length);
        }
    }

    private void reverse(T[] array, int start, int len) {
        if (len <= 1) {
            return;
        }
        int last = start + len - 1;
        swap(array, start, last);
        reverse(array, start + 1, len - 2);
    }

    private void swap(T[] array, int t, int i) {
        T tmp = array[t];
        array[t] = array[i];
        array[i] = tmp;
    }

    /**
     * @param rotation rotation request
     * @param array    input
     *
     * @implNote solve array of N by solve array of N-n
     */
    private void dynamicRotate(Rotation rotation, T[] array) {
        recursiveRotate(array, 0, array.length, rotation);
    }

    private void recursiveRotate(T[] array, int start, int end, Rotation rotation) {
        int len = end - start;
        if (len <= 1) {
            return;
        }
        Rotation real = rotation.minimize(len);
        blockSwap(array, start, end - real.dis, real.dis);
        int nextS;
        int nextE;
        if (real.getDir() == Direction.LEFT) {
            nextS = start;
            nextE = end - real.getDis();
        } else {
            nextS = start + real.getDis();
            nextE = end;
        }
        recursiveRotate(array, nextS, nextE, real);
    }


    private void blockSwap(T[] array, int f, int s, int len) {
        assert f + len <= s;
        for (int i = f; i < f + len; i++) {
            swap(array, i, s);
            s++;
        }
    }

    /**
     * @param rotation rotate specification
     * @param array    input
     *
     * @implNote classify the number into n types according to their reminder
     * when divide to n
     */
    private void rotate(Rotation rotation, T[] array) {
        int length = array.length;
        int n = GCD(length, rotation.dis);
        int dis = rotation.dis;
        if (rotation.dir == Direction.LEFT) {
            for (int i = 0; i < n; i++) {
                T first = array[i];
                int j;
                for (j = i; ; j += dis) {
                    if (j > length) {
                        j -= length;
                    }
                    if (j == i) {
                        break;
                    }
                    array[j] = array[j + dis];
                }
                array[j] = first;
            }
        } else {
            for (int i = 0; i < n; i++) {
                T last = array[length - 1 - i];
                int j;
                for (j = array.length - 1 - i; ; j -= dis) {
                    if (j < 0) {
                        j += length;
                    }
                    if (j == length - 1 - i) {
                        break;
                    }
                    array[j] = array[j - dis];
                }
                array[j] = last;
            }
        }
    }

//    private T[] leftRotate(T arr[], int d) {
//        int n = arr.length;
//        int i, j, k;
//        T temp;
//        int gcd = GCD(d, n);
//        for (i = 0; i < gcd; i++) {
//            temp = arr[i];
//            j = i;
//            while (true) {
//                k = j + d;
//                if (k >= n)
//                    k = k - n;
//                if (k == i)
//                    break;
//                arr[j] = arr[k];
//                j = k;
//            }
//            arr[j] = temp;
//        }
//        return arr;
//    }

    private int GCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        return GCD(b, a % b);
    }

    private Rotation minimizeDistance(int length, Rotation rotation) {
        int n = rotation.getDis();
        while (n >= length) {
            n -= length;
        }
        Direction dir = rotation.getDir();
        if (n * 2 > length) {
            dir = dir.reverse();
            n = length - n;
        }
        return new Rotation(n, dir);
    }

    public static void main(String[] args) {
        int size = 10;
        Integer[] integers = new Integer[size];
        for (int i = 0; i < size; i++) {
            integers[i] = i + 1;
        }
        RotateArray<Integer> rotateArray = new RotateArray<>(integers);
        //        System.out.println(Arrays.toString(rotateArray.leftRotate(integers, 3)));
//        rotateArray.rotate(Direction.LEFT, 3);
//        System.out.println(Arrays.toString(rotateArray.array));
        rotateArray.rotate(Direction.RIGHT, 3);
        System.out.println(Arrays.toString(rotateArray.array));
    }
}
