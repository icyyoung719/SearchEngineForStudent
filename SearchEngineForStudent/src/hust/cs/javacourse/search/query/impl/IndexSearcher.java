package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Posting;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.File;
import java.util.*;

/**
 * 用来存储对倒排索引的读取，搜索方法
 *
 * @Author icyyoung
 * @Date 2024/5/1
 */
public class IndexSearcher extends AbstractIndexSearcher {
    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     *
     * @param indexFile ：指定索引文件
     */
    @Override
    public void open(String indexFile) {
        File file = new File(indexFile);
        this.index.load(file);  //把文件加载到倒排索引结构中去
        this.index.optimize(); // 对索引内部进行排序
    }

    /**
     * 根据单个检索词进行搜索
     *
     * @param queryTerm ：检索词
     * @param sorter    ：排序器
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractPostingList postingList = this.index.search(queryTerm);
        if (postingList == null) {
            return null;
        }
        List<AbstractHit> hits = new ArrayList<>();
        int n;
        for (n = 0; n < postingList.size(); n++)//对所以命中的文档创建hit对象，加入到hits中去
        {
            AbstractPosting posting = postingList.get(n);
            String path = this.index.getDocName(posting.getDocId());
            Map<AbstractTerm, AbstractPosting> map = new HashMap<>();
            map.put(queryTerm, posting);
            AbstractHit hit = new Hit(posting.getDocId(), path, map);
            hit.setScore(sorter.score(hit));
            hits.add(hit);
        }
        sorter.sort(hits);//对命中的文档排序
        return hits.toArray(new AbstractHit[0]);
        //将集合强制转换成数组类型

    }

    /**
     * @param queryTerm1
     * @param queryTerm2
     * @param sorter
     * @return AbstractHit
     * @Description 根据输入的两个相邻的单词进行查询
     */
    public AbstractHit[] combineSearch(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter) {
        AbstractHit[] searchResult = search(queryTerm1, queryTerm2, sorter, LogicalCombination.AND);
        List<AbstractHit> hits = new ArrayList<>();
        for (AbstractHit hit : searchResult) {
            AbstractPosting posting1 = hit.getTermPostingMapping().get(queryTerm1);
            AbstractPosting posting2 = hit.getTermPostingMapping().get(queryTerm2);
            List<Integer> posting1Pos = posting1.getPositions();
            List<Integer> posting2Pos = posting2.getPositions();
            Map<AbstractTerm, AbstractPosting> map = new HashMap<>();
            List<Integer> positions = new ArrayList<>();
            int freq = 0;
            for (Integer pos1 : posting1Pos) {
                for (Integer pos2 : posting2Pos) {
                    if (pos2 - pos1 == 1) {
                        positions.add(pos1);
                        freq++;
                    }
                }
            }
            if (freq != 0) {
                AbstractPosting posting = new Posting(posting1.getDocId(), freq, positions);
                map.put(new Term(queryTerm1.getContent() + ' ' + queryTerm2.getContent()), posting);
                AbstractHit combineHit = new Hit(hit.getDocId(), hit.getDocPath(), map);
                combineHit.setScore(sorter.score(combineHit));
                hits.add(combineHit);
            }
        }
        sorter.sort(hits);//对命中的文档排序
        return hits.toArray(new AbstractHit[0]);
    }

    /**
     * 根据二个检索词进行搜索
     *
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter     ：    排序器
     * @param combine    ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        AbstractPostingList postingList1 = this.index.search(queryTerm1);
        AbstractPostingList postingList2 = this.index.search(queryTerm2);
        List<AbstractHit> hitList = new ArrayList<>();

        // 如果是 AND 关系
        if (combine == LogicalCombination.AND) {
            if (postingList1 == null || postingList2 == null) {
                return null;
            }
            int i = 0, j = 0;
            AbstractPosting posting1, posting2;
            while (i < postingList1.size() && j < postingList2.size()) {
                posting1 = postingList1.get(i);
                posting2 = postingList2.get(j);
                if (posting1.getDocId() == posting2.getDocId()) {
                    int docId = posting1.getDocId();
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
                    termPostingMapping.put(queryTerm1, posting1);
                    termPostingMapping.put(queryTerm2, posting2);
                    AbstractHit hit = new Hit(docId, index.getDocName(docId), termPostingMapping);
                    sorter.score(hit);
                    hitList.add(hit);
                    i++;
                    j++;
                } else if (posting1.getDocId() < posting2.getDocId()) {
                    i++;
                } else {
                    j++;
                }
            }
            // 检查一下所有关键词是否有重合的posting
            if (hitList.isEmpty()) {
                return null;
            }
            sorter.sort(hitList);
            AbstractHit[] hits = new AbstractHit[hitList.size()];
            hitList.toArray(hits);
            return hits;
        }
        //如果是OR关系
        else if (combine == LogicalCombination.OR) {
            if (postingList1 == null) {
                return search(queryTerm2, sorter);
            } else if (postingList2 == null) {
                return search(queryTerm1, sorter);
            } else {
                int i = 0, j = 0, docId;
                AbstractPosting posting1, posting2;
                while (i < postingList1.size() && j < postingList2.size()) {
                    posting1 = postingList1.get(i);
                    posting2 = postingList2.get(j);
                    //对于两个关键词都同时命中的情况单独处理
                    if (posting1.getDocId() == posting2.getDocId()) {
                        docId = posting1.getDocId();
                        Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
                        termPostingMapping.put(queryTerm1, posting1);
                        termPostingMapping.put(queryTerm2, posting2);
                        AbstractHit hit = new Hit(docId, index.getDocName(docId), termPostingMapping);
                        sorter.score(hit);
                        hitList.add(hit);
                        i++;
                        j++;
                    } else if (posting1.getDocId() < posting2.getDocId()) {
                        docId = posting1.getDocId();
                        Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
                        termPostingMapping.put(queryTerm1, posting1);
                        AbstractHit hit = new Hit(docId, index.getDocName(docId), termPostingMapping);
                        sorter.score(hit);
                        hitList.add(hit);
                        i++;
                    } else {
                        docId = posting2.getDocId();
                        Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
                        termPostingMapping.put(queryTerm2, posting2);
                        AbstractHit hit = new Hit(docId, index.getDocName(docId), termPostingMapping);
                        sorter.score(hit);
                        hitList.add(hit);
                        j++;
                    }
                }
                while (i < postingList1.size()) {
                    posting1 = postingList1.get(i);
                    docId = posting1.getDocId();
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
                    termPostingMapping.put(queryTerm1, posting1);
                    AbstractHit hit = new Hit(docId, index.getDocName(docId), termPostingMapping);
                    sorter.score(hit);
                    hitList.add(hit);
                    i++;
                }
                while (j < postingList2.size()) {
                    posting2 = postingList2.get(j);
                    docId = posting2.getDocId();
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
                    termPostingMapping.put(queryTerm2, posting2);
                    AbstractHit hit = new Hit(docId, index.getDocName(docId), termPostingMapping);
                    sorter.score(hit);
                    hitList.add(hit);
                    j++;
                }
                if (hitList.isEmpty()) {
                    return null;
                }
                sorter.sort(hitList);
                AbstractHit[] hits = new AbstractHit[hitList.size()];
                hitList.toArray(hits);
                return hits;
            }
        }
        return null;
    }
}