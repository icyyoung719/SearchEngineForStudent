package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;

/**
 * 一个TermTuple对象为三元组(单词 ， 出现频率 ， 出现的当前位置).
 * 当解析一个文档时，每解析到一个单词，应该产生一个三元组，其中freq始终为1
 * 最终应该将TermTuple合并成一个Posting，组合成该文件关于这个单词的信息
 *
 * @Author Icyyoung
 * @Date 2024/4/18
 */
public class TermTuple extends AbstractTermTuple {
    /**
     * @param
     * @return null
     * @Description 默认缺省构造函数
     */
    public TermTuple() {
        super();
    }

    /**
     * @param curPos
     * @param term
     * @return null
     * @Description 依据Term的内容构造三元组
     */
    public TermTuple(int curPos, AbstractTerm term) {
        this.curPos = curPos;
        this.term = term;
    }

    /**
     * 判断二个三元组内容是否相同
     *
     * @param obj ：要比较的另外一个三元组
     * @return 如果内容相等（三个属性内容都相等）返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof TermTuple termTuple) {
            return (term.equals(termTuple.term)) && (curPos == termTuple.curPos);
        }
        return false;
    }

    /**
     * 获得三元组的字符串表示
     *
     * @return ： 三元组的字符串表示
     */
    @Override
    public String toString() {
        return "TermTuple: " + term.toString() +
                " freq:" + freq +
                " curPos:" + curPos + '\n';
    }
}
