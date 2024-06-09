package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.File;
import java.util.List;

/**
 * IndexBuilder是索引构造器的抽象父类
 * 需要实例化一个具体子类对象完成索引构造的工作
 *
 * @Author icyyoung
 * @Date 2024/4/18
 */

public class IndexBuilder extends AbstractIndexBuilder {
    /**
     * 用已经生成好的document初始化Index
     */
    public IndexBuilder(AbstractDocumentBuilder docBuilder) {
        super(docBuilder);
    }

    /**
     * </pre>
     * 构建指定目录下的所有文本文件的倒排索引.
     * 需要遍历和解析目录下的每个文本文件, 得到对应的Document对象，再依次加入到索引，并将索引保存到文件.
     *
     * @param rootDirectory ：指定目录
     * @return ：构建好的索引
     * </pre>
     */
    @Override
    public AbstractIndex buildIndex(String rootDirectory) {
        AbstractIndex index = new Index();
        List<String> filePaths = FileUtil.list(rootDirectory, ".txt");
        int i = 0;
        for (String filepath : filePaths) {
            AbstractDocument document = new DocumentBuilder().build(i, filepath, new File(filepath));
            index.addDocument(document);
            i++;
        }
        return index;
    }
}
