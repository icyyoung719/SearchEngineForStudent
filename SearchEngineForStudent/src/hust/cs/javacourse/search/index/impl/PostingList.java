package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * PostingList对象包含了各个文件中一个单词的Posting列表.
 * 也就是这些文件中单词的所有信息
 *
 * @Author Icyyoung
 * @Date 2024/4/18
 */
public class PostingList extends AbstractPostingList {
    /**
     * 添加Posting,要求不能有内容重复的posting
     *
     * @param posting ：Posting对象
     */
    @Override
    public void add(AbstractPosting posting) {
        if (list.isEmpty()) {
            list.add(posting);
        } else if (!list.contains(posting)) {
            list.add(posting);
        }
    }

    /**
     * 获得PosingList的字符串表示
     *
     * @return ： PosingList的字符串表示
     */
    @Override
    public String toString() {
        Iterator<AbstractPosting> it = list.iterator();
        StringBuilder out = new StringBuilder();
        out.append("PostingList:");
        while (it.hasNext()) {
            out.append(it.next().toString()).append('\n');
        }
        return out.toString();
    }

    /**
     * 添加Posting列表,,要求不能有内容重复的posting
     *
     * @param postings ：Posting列表
     */
    @Override
    public void add(List<AbstractPosting> postings) {
        for (AbstractPosting abstractPosting : postings) {
            if (!list.contains(abstractPosting)) {
                list.add(abstractPosting);
            }
        }
    }

    /**
     * 返回指定下标位置的Posting
     *
     * @param index ：下标
     * @return： 指定下标位置的Posting
     */
    @Override
    public AbstractPosting get(int index) {
        return list.get(index);
    }

    /**
     * 返回指定Posting对象的下标
     *
     * @param posting ：指定的Posting对象
     * @return ：如果找到返回对应下标；否则返回-1
     */
    @Override
    public int indexOf(AbstractPosting posting) {
        return list.indexOf(posting);
    }

    /**
     * 返回指定文档id的Posting对象的下标
     *
     * @param docId ：文档id
     * @return ：如果找到返回对应下标；否则返回-1
     */
    @Override
    public int indexOf(int docId) {
        for (int i = 0; i < list.size(); i++) {
            AbstractPosting posting = list.get(i);
            if (posting.getDocId() == docId) { // 假设AbstractPosting有一个getDocId()方法获取docId
                return i;
            }
        }
        return -1;
    }

    /**
     * 是否包含指定Posting对象
     *
     * @param posting ： 指定的Posting对象
     * @return : 如果包含返回true，否则返回false
     */
    @Override
    public boolean contains(AbstractPosting posting) {
        return list.contains(posting);
    }

    /**
     * 删除指定下标的Posting对象
     *
     * @param index ：指定的下标
     */
    @Override
    public void remove(int index) {
        list.remove(index);
    }

    /**
     * 删除指定的Posting对象
     *
     * @param posting ：定的Posting对象
     */
    @Override
    public void remove(AbstractPosting posting) {
        list.remove(posting);
    }

    /**
     * 返回PostingList的大小，即包含的Posting的个数
     *
     * @return ：PostingList的大小
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * 清除PostingList
     */
    @Override
    public void clear() {
        list.clear();
    }

    /**
     * PostingList是否为空
     *
     * @return 为空返回true;否则返回false
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * 根据文档id的大小对PostingList进行从小到大的排序
     */
    @Override
    public void sort() {
        list.sort(Comparator.naturalOrder());
    }

    /**
     * 写到二进制文件
     * 先写入整个PostingList的List<AbstractPosting>的大小</AbstractPosting>
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (AbstractPosting posting : list) {
            try {
                posting.writeObject(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从二进制文件读
     * 先读入整个PostingList的List<AbstractPosting>的大小</AbstractPosting>
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                Posting posting = new Posting();
                // 调用Posting的readObject方法读取对象
                posting.readObject(in);
                this.list.add(posting);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
