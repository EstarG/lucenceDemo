/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.lucene.demo.token;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 *自定义分词器
 *
 * @author baoxing.gbx
 * @version $Id:MyAnalyzer.java, V 0.1 2015-11-17 19:24 baoxing.gbx Exp $$
 */
public class MyAnalyzer extends Analyzer {

    /** 停用词集合 */
    private Set stopWords = new HashSet();

    /** 同义词组件 */
    private SamewordComponent samewordComponent;

    /**
     * 构造函数
     *
     * @param samewordComponent
     * @param stopWords
     */
    public MyAnalyzer(SamewordComponent samewordComponent, String[] stopWords) {

        this.samewordComponent = samewordComponent;
        // 外部有停用词
        if (null != stopWords) {
            this.stopWords = StopFilter.makeStopSet(Version.LUCENE_35, stopWords, true);
        }
        // 默认添加 StopAnalyzer 中定义的停用词
        this.stopWords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);

    }

    @Override
    public final TokenStream reusableTokenStream(String fieldName,
                                                 Reader reader) throws IOException {
        return super.reusableTokenStream(fieldName, reader);
    }

    @Override
    public final TokenStream tokenStream(String fieldName, Reader reader) {
        String dicUrl = MyAnalyzer.class.getClassLoader().getResource("mydata").getPath();
        Dictionary dic = Dictionary.getInstance(dicUrl);

        return new MyTokenFilter(new StopFilter(Version.LUCENE_35,
            new MMSegTokenizer(new MaxWordSeg(dic), reader), stopWords), this.samewordComponent);
    }
}