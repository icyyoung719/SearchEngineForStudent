package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;

import java.util.Locale;
import java.util.Scanner;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     * 搜索程序入口
     *
     * @param args ：命令行参数
     */
    public static void main(String[] args) {
        Sort hitsSorter = new SimpleSorter();
        String indexFile = Config.INDEX_DIR + "index.dat"; //索引文件的觉得地址
        IndexSearcher searcher = new IndexSearcher();
        searcher.open(indexFile);
        Scanner input = new Scanner(System.in);
        AbstractHit[] hits = null;
        while (true) {
            System.out.println("input:");
            String str = input.nextLine();
            String[] spliter = str.split("\\s+");
            if (spliter[0].equals("q") || spliter[0].equals("quit")) {
                break;
            }
            if (spliter.length == 1) {  //查一个词
                Term term = new Term(spliter[0]);
                hits = searcher.search(term, hitsSorter);
                if (hits != null) {
                    System.out.println("Document Number:" + hits.length);
                    for (AbstractHit hit : hits) {
                        System.out.println(hit.toString());
                    }
                } else
                    System.out.println("No doc!");
            } else if (spliter.length == 3) {     //查两个词
                Term term1 = new Term(spliter[0]);
                Term term2 = new Term(spliter[2]);
                if (spliter[1].toLowerCase(Locale.ROOT).equals("or"))
                    hits = searcher.search(term1, term2, hitsSorter, AbstractIndexSearcher.LogicalCombination.OR);
                if (spliter[1].toLowerCase(Locale.ROOT).equals("and"))
                    hits = searcher.search(term1, term2, hitsSorter, AbstractIndexSearcher.LogicalCombination.AND);
                System.out.println("Document Number:" + hits.length);
                for (AbstractHit hit : hits) {
                    System.out.println(hit.toString());
                }
            } else if (spliter.length == 2) {
                Term term1 = new Term(spliter[0]);
                Term term2 = new Term(spliter[1]);
                hits = searcher.combineSearch(term1, term2, hitsSorter);
                System.out.println("Document Number:" + hits.length);
                for (AbstractHit hit : hits) {
                    System.out.println(hit.toString());
                }
            } else {
                System.out.println("输入不符合要求，请重新输入");
            }
        }
    }
}
/*
    coffee  2
    shops  2
    according 4
    people 5
    测试 and:according and people   1
    or:according or people 8            8+1=4+5
    附加：coffee shops 2
 */