package suffixTree;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zzt on 4/6/16.
 * <p>
 * Usage:
 */
public class LCSubString {
    public static void main(String[] args) {
        String bcacb = "bcacb";
//        lcs1(bcacb);
        System.out.println(lcs2(bcacb));

        // find a deepest node which have both end symbol
    }

    private static String lcs2(String input) {
        SuffixTree suffixTree = new SuffixTree();
        StringBuilder sb = new StringBuilder(input);
        StringBuilder append = sb.append(sb.reverse().toString());
        suffixTree.add(append.toString());
        return suffixTree.longestRepeat();
    }

    private static String lcs1(String input) {
        SuffixTree suffixTree = new SuffixTree();
        StringBuilder sb = new StringBuilder(input);
        suffixTree.add(sb.toString());
        suffixTree.add(sb.reverse().toString());
        return null;
    }
}
