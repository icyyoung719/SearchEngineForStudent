package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

/**
 * LengthTermTupleFilter是抽象类AbstractTermTupleFilter的子类.
 * 将长度小于3或长度大于20的单词过滤掉
 *
 * @Author Icyyoung
 * @Date 2024/5/1
 */
public class LengthTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public LengthTermTupleFilter(AbstractTermTupleStream input) {
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
        int len;
        while (now_term != null) {
            len = now_term.term.getContent().length();
            if (len <= Config.TERM_FILTER_MAXLENGTH && len >= Config.TERM_FILTER_MINLENGTH) {
                return now_term;
            } else {
                now_term = input.next();
            }
        }
        return null;
    }
}
