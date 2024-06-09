package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * AbstractPosting的具体实现类
 * Posting对象代表倒排索引里每一项，是一个文档中所有关于某个单词的所有信息 一个Posting对象包括:
 * <br>
 * 包含单词的文档id.
 * <br>
 * 单词在文档里出现的次数.
 * <br>
 * 单词在文档里出现的位置列表（位置下标不是以字符为编号，而是以单词为单位进行编号.
 *
 * @Author icyyoung
 * @Date 2024/4/18
 */
public class Posting extends AbstractPosting {
    /**
     * 缺省构造函数
     */
    public Posting() {
    }

    /**
     * 构造函数
     *
     * @param docId     ：包含单词的文档id
     * @param freq      ：单词在文档里出现的次数
     * @param positions ：单词在文档里出现的位置
     */
    public Posting(int docId, int freq, List<Integer> positions) {
        super(docId, freq, positions);
    }

    /**
     * 判断二个Posting内容是否相同
     *
     * @param obj ：要比较的另外一个Posting
     * @return 如果内容相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof Posting posting) {
            Set<Integer> set1 = new HashSet<>(posting.getPositions());
            Set<Integer> set2 = new HashSet<>(positions);
            return (posting.getDocId() == docId) && (posting.getFreq() == freq) && (set1.equals(set2));
        }
        return false;
    }

    /**
     * 返回Posting的字符串表示
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("Posting{" +
                "docId:" + docId +
                ", freq:" + freq +
                ", positions:");
        for (Integer position : positions) {
            out.append(' ').append(position.toString());
        }
        out.append('}');
        return out.toString();
    }

    /**
     * 返回包含单词的文档id
     *
     * @return ：文档id
     */
    @Override
    public int getDocId() {
        return docId;
    }

    /**
     * 设置包含单词的文档id
     *
     * @param docId ：包含单词的文档id
     */
    @Override
    public void setDocId(int docId) {
        this.docId = docId;
    }

    /**
     * 返回单词在文档里出现的次数
     *
     * @return ：出现次数
     */
    @Override
    public int getFreq() {
        return freq;
    }

    /**
     * 设置单词在文档里出现的次数
     *
     * @param freq :单词在文档里出现的次数
     */
    @Override
    public void setFreq(int freq) {
        this.freq = freq;
    }

    /**
     * 返回单词在文档里出现的位置列表
     *
     * @return ：位置列表
     */
    @Override
    public List<Integer> getPositions() {
        return positions;
    }

    /**
     * 设置单词在文档里出现的位置列表
     *
     * @param positions ：单词在文档里出现的位置列表
     */
    @Override
    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    /**
     * 比较二个Posting对象的大小（根据docId）
     *
     * @param o ： 另一个Posting对象
     * @return ：二个Posting对象的docId的差值
     */
    @Override
    public int compareTo(AbstractPosting o) {
        return docId - o.getDocId();
    }

    /**
     * 对内部positions从小到大排序
     */
    @Override
    public void sort() {
        positions.sort(Comparator.naturalOrder());
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(docId);
            out.writeInt(freq);
            out.writeObject(positions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docId = in.readInt();
            this.freq = in.readInt();
            @SuppressWarnings("unchecked")
            List<Integer> tempPositions = (List<Integer>) in.readObject();
            this.positions = new ArrayList<>(tempPositions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
