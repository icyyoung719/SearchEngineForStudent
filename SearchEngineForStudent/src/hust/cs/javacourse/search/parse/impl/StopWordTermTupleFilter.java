package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.StopWords;

import java.util.Arrays;

/**
 * StopWordTermTupleFilter是抽象类AbstractTermTupleFilter的子类.
 * 用于过滤含StopWords类中定义的停用词的三元组
 *
 * @Author Icyyoung
 * @Date 2024/5/1
 */
public class StopWordTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public StopWordTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    /**
     * 获得下一个三元组
     *
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple now_term = input.next();
        while (now_term != null) {
            if (!Arrays.asList(StopWords.STOP_WORDS).contains(now_term.term.getContent())) {
                return now_term;
            } else
                now_term = input.next();
        }
        return null;
    }
}
