/*
Date: 04/06,2019, 09:40
字典树匹配过滤
*/
package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class SensitiveService {
    private TrieTree root = new TrieTree();

    public static void main(String[] args) {
        SensitiveService sensitiveService = new SensitiveService();
        sensitiveService.initTrieTree();
        System.out.println(sensitiveService.filter("11abcabf1seqing11"));
    }

    public void initTrieTree() {
        InputStream in = this.getClass().getResourceAsStream("/SensitiveWords.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                addWords(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addWords(String word) {
        TrieTree now = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            if (now.getSubNode(c) == null) {
                now.addSubNode(c, new TrieTree());
            }
            now = now.getSubNode(c);
        }
        now.setEnd(true);
    }

    private String filter(String word) {
        String replacement = "***";
        StringBuilder res = new StringBuilder();

        TrieTree root = this.root;
        // word 的位置，表示已经处理的位置
        int position = 0;
        // word 中 匹配 trie数 的位置，可能匹配失败，用于回滚
        int begin = 0;

        while (position < word.length()) {
            char c = word.charAt(begin);
            root = root.getSubNode(c);
            // 匹配失败， 加入 position 的位置， begin 回滚
            if (root == null) {
                res.append(word.charAt(position));
                position++;
                begin = position;
                root = this.root;
            } else {
                // 匹配成功， begin 加一， 赋予 position
                if (root.isEnd()) {
                    res.append(replacement);
                    position = ++begin;
                    root = this.root;
                // 中段匹配成功，只增加 begin 位置
                } else {
                    begin++;
                }
            }
        }

        return res.toString();
    }

    class TrieTree {
        private boolean end = false;

        private Map<Character, TrieTree> subNodes = new HashMap<>();

        private TrieTree getSubNode(Character c) {
            return subNodes.get(c);
        }

        private void addSubNode(Character c, TrieTree node) {
            subNodes.put(c, node);
        }

        private boolean isEnd() {
            return end;
        }

        private void setEnd(boolean end) {
            this.end = end;
        }

    }

}
